package com.qusion.kotlin.lib.extensions.network

/**
 * This provides a nice way of catching and reacting to errors and responses from network calls,
 * with as little boilerplate code as possible.
 */
sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val value: T) : NetworkResult<T>()
    data class Error(val cause: Exception? = null, val code: Int? = null) : NetworkResult<Nothing>()
}


/** Extension functions to make the code more concise  */

inline fun <T : Any> NetworkResult<T>.onSuccess(block: (response: T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) {
        block(this.value)
    }
    return this
}

inline fun <T : Any> NetworkResult<T>.onError(block: (response: NetworkResult.Error) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) {
        block(this)
    }
    return this
}


inline fun <reified T: Any> safeApiCall(block: () -> NetworkResult<T>): NetworkResult<T> {
    return try {
        block()
    } catch (e: Exception) {
        NetworkResult.Error(cause = e)
    }
}
