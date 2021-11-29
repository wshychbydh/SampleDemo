package com.cool.eye.func.jetpack.event

/**
 * @author: chuanbo
 * @date: 2021/11/29 9:36
 * @desc: @See https://github.com/nirmaljeffrey/SingleLiveEvent-EventWrapper-LiveData
 */
class Event<T>(content: T) {
    private val mContent: T
    private var hasBeenHandled = false
    val contentIfNotHandled: T?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            mContent
        }

    fun peekContent(): T {
        return mContent
    }

    fun hasBeenHandled(): Boolean {
        return hasBeenHandled
    }

    init {
        requireNotNull(content) { "null values in Event are not allowed." }
        mContent = content
    }
}