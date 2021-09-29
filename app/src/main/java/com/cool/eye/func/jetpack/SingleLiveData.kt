package com.cool.eye.func.jetpack

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.random.Random

class SingleLiveData<T>(
    private val acceptOnlyOnce: Boolean = false,
    private val ignoreDuplicate: Boolean = false
) : MutableLiveData<T>() {

    private val lock = Any()
    private val set = CopyOnWriteArraySet<Int>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, object : SingleObserver<T>() {
            override fun onChanged(t: T) {
                val code = this.hashCode()
                if (set.contains(code)) return
                set.add(code)
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(value: T) {
        if (acceptOnlyOnce && this.value != null) return
        if (ignoreDuplicate && value == this.value) return
        set.clear()
        super.setValue(value)
    }

    override fun postValue(value: T) {
        if (acceptOnlyOnce || ignoreDuplicate) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                setValue(value)
            } else {
                synchronized(lock) {
                    if (acceptOnlyOnce && this.value != null) return
                    if (ignoreDuplicate && value == this.value) return
                    set.clear()
                    super.postValue(value)
                }
            }
        } else {
            super.postValue(value)
        }
    }

    private abstract class SingleObserver<T> : Observer<T> {
        private val hasCode: Int = Random.nextInt()

        override fun hashCode() = hasCode
    }
}