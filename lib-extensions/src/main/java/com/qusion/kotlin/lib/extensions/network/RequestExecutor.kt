package com.qusion.kotlin.lib.extensions.network

import com.qusion.kotlin.lib.extensions.IRepository

/**
 * How to use:
 *
 * Create a single { } of RequestExecutor from the repository you want to use
 * (@see DI module)
 *
 * Use it in the constructor of a ViewModel
 *
 * Pass functions for onSuccess and onError and call execute()
 * Update LiveData and handle errors accordingly.
 *
 * Since execute() is suspend function this should be called inside viewModelScope
 */
class RequestExecutor<Entity : Any>(private val repository: IRepository<Entity>) {

    suspend fun execute(policy: CachePolicy) {

        when (policy) {
            CachePolicy.CACHE_ONLY -> {
                load()
            }
            CachePolicy.CACHE_AND_NETWORK -> {
                load()
                fetch(writeToDb = true)
            }
            CachePolicy.NETWORK_ONLY -> {
                fetch(writeToDb = false)
            }
        }
    }

    private suspend fun load() {
        repository.loadFromDb()?.let {
            success(it)
        }
    }

    private suspend fun fetch(writeToDb: Boolean) {
        repository.fetch()
            .onSuccess {
                success(it)

                if (writeToDb) {
                    repository.writeToDb(it)
                }
            }
            .onError {
                error
            }
    }

    lateinit var success: (Entity) -> Unit

    lateinit var error: (NetworkResult.Error) -> Unit
}

/** @block Pass a lambda to update the LiveData in the ViewModel. */
fun <T: Any> RequestExecutor<T>.onSuccess(block: (T) -> Unit): RequestExecutor<T> {
    this.success = block
    return this
}

/** @block Pass a lambda to handle error in the ViewModel. */
fun <T: Any> RequestExecutor<T>.onError(block: (NetworkResult.Error) -> Unit): RequestExecutor<T> {
    this.error = block
    return this
}

