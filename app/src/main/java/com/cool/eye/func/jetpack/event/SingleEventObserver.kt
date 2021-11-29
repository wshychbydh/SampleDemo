package com.cool.eye.func.jetpack.event

import androidx.lifecycle.Observer

/**
 * @author: chuanbo
 * @date: 2021/11/29 9:36
 * @desc: livedata只回调一次，支持多个监听
 * @See https://github.com/nirmaljeffrey/SingleLiveEvent-EventWrapper-LiveData
 */
class SingleEventObserver<T>(
    private val onEventUnhandledContent: EventHandler<T?>
) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        if (event == null) return
        onEventUnhandledContent.onEventUnHandled(event.contentIfNotHandled)
    }
}