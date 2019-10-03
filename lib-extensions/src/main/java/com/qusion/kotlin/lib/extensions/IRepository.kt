package com.qusion.kotlin.lib.extensions

import com.qusion.kotlin.lib.extensions.network.NetworkResult

interface IRepository<Entity: Any> {
    suspend fun fetch(): NetworkResult<Entity>

    suspend fun loadFromDb(): Entity?

    suspend fun writeToDb(entity: Entity)
}
