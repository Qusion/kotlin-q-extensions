package com.qusion.kotlin.lib.extensions

/**
 * Used for session persistance.
 * Implement using EncryptedSharedPreferences. (ideally)
 * */
interface ISessionProvider {

    fun get(): String?

    fun set(sid: String)

    fun clear()
}