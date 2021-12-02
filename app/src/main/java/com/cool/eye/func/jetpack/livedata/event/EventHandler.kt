package com.cool.eye.func.jetpack.livedata.event

/**
 * @author: chuanbo
 * @date: 2021/11/29 9:36
 * @desc: @See https://github.com/nirmaljeffrey/SingleLiveEvent-EventWrapper-LiveData
 */
interface EventHandler<D> {
    fun onEventUnHandled(data: D)
}