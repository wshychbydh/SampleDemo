package com.cool.eye.func.support

import androidx.lifecycle.Observer

class HandleObserver<T : DataWrapper<*>>(
    private val onUnhandledData: (T) -> Unit
) : Observer<T> {

  override fun onChanged(data: T?) {
    data?.getIfNotHandled()?.let {
      onUnhandledData(data)
    }
  }
}