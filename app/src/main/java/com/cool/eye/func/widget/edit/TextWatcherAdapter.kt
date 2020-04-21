package com.cool.eye.func.widget.edit

import android.text.Editable
import android.text.TextWatcher

/**
 *Created by ycb on 2019/6/3 0003
 */
open class TextWatcherAdapter : TextWatcher {
  override fun afterTextChanged(s: Editable?) {
  }

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    onTextChanged(s?.toString() ?: "")
  }

  open fun onTextChanged(text: String) {

  }
}