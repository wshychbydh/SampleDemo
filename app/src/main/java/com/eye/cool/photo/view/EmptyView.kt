package com.eye.cool.photo.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.eye.cool.photo.support.OnActionListener

/**
 *Created by ycb on 2019/8/14 0014
 */
internal class EmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  init {
    layoutParams = ViewGroup.LayoutParams(1, 1)
  }

  private var listener: OnActionListener? = null

  private fun setOnActionListener(listener: OnActionListener) {
    this.listener = listener
  }

  fun onTakePhoto() {
    listener?.onTakePhoto()
  }

  fun onSelectAlbum() {
    listener?.onSelectAlbum()
  }
}