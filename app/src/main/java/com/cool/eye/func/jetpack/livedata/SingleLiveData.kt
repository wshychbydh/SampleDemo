package com.cool.eye.func.jetpack.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.CopyOnWriteArraySet

/**
 * @author: chuanbo
 * @date: 2021/11/9 16:48
 * @desc: livedata只回调一次，支持多个监听
 */
class SingleLiveData<T> : MutableLiveData<T>() {
    private val set = CopyOnWriteArraySet<Int>()
    fun reset() {
        set.clear()
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, object : ProxyObserver<T>() {
            override fun onChanged(t: T) {
                if (!set.add(hashCode())) return
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(value: T?) {
        set.clear()
        super.setValue(value)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    private abstract inner class ProxyObserver<T> : Observer<T> {
        private val hasCode = random.nextInt()
        override fun hashCode(): Int {
            return hasCode
        }
    }

    companion object {
        private val random = Random()
    }
}