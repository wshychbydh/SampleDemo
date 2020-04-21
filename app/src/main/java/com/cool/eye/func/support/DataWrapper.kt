package com.cool.eye.func.support

import java.util.concurrent.atomic.AtomicBoolean

abstract class DataWrapper<out T> {

  private val hasBeenHandled = AtomicBoolean(false)

  fun getIfNotHandled(): DataWrapper<T>? {
    return if (hasBeenHandled.compareAndSet(false, true)) this else null
  }
}