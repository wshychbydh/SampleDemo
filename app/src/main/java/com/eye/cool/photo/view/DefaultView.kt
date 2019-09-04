package com.eye.cool.photo.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.cool.eye.func.R
import com.eye.cool.photo.support.OnActionListener

/**
 * Created by cool on 2018/6/12
 */
internal class DefaultView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    LinearLayout(context, attrs, defStyle), OnClickListener {

  private var listener: OnActionListener? = null

  private fun setOnActionListener(listener: OnActionListener) {
    this.listener = listener
  }

  init {
    orientation = VERTICAL
    val padding = (context.resources.displayMetrics.density * 20f).toInt()
    setPadding(padding, padding, padding, padding)
    layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    val view = LayoutInflater.from(context).inflate(R.layout.layout_photo, this, true)
    view.findViewById<View>(R.id.albumBtn).setOnClickListener(this)
    view.findViewById<View>(R.id.photoBtn).setOnClickListener(this)
    view.findViewById<View>(R.id.cancelBtn).setOnClickListener(this)
  }

  override fun onClick(v: View) {
    when (v.id) {
      R.id.albumBtn -> listener?.onSelectAlbum()
      R.id.photoBtn -> listener?.onTakePhoto()
      R.id.cancelBtn -> listener?.onCancel()
    }
  }
}
