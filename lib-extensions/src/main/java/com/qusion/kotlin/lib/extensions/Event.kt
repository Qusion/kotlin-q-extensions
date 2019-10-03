package com.qusion.kotlin.lib.extensions

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var consumed = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            content
        }
    }
}