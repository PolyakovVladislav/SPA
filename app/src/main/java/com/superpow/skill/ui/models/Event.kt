package com.superpow.skill.ui.models

class Event<T>(val data: T) {

    var handled = false
        private set

    fun getIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            data
        }
    }
}