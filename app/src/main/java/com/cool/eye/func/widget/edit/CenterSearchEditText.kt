package com.cool.eye.func.widget.edit

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.cool.eye.demo.R

class CenterSearchEditText : ClearAbleEditText {
  private var searchDrawable: Drawable? = null
  private var offset: Int = 0
  private var searchWidth: Int = 0
  private var w: Int = 0
  private val paint = Paint()
  private val rect = Rect()

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init()
  }

  private fun init() {

    inputType = InputType.TYPE_CLASS_TEXT
    setLines(1)
    maxLines = 1
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    background = ContextCompat.getDrawable(context, R.drawable.search_bg)

    onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) { // 获得焦点
        offset = 0
        textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
      } else { // 失去焦点
        offset = searchWidth / 2 - w * 2
        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
      }
      setSearchDrawable()
    }
    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
  }

  override fun onFinishInflate() {
    super.onFinishInflate()
    searchDrawable = compoundDrawables[0]
    searchWidth = measuredWidth
    if (!hint.isNullOrEmpty()) {
      paint.getTextBounds(hint.toString(), 0, hint.length, rect)
    }
    w = dip2px(context, rect.width().toFloat())
    offset = searchWidth / 2 - w * 2
    setSearchDrawable()
  }

  private fun setSearchDrawable() {
    if (searchDrawable == null) return
    searchDrawable!!.setBounds(offset, 0, offset + searchDrawable!!.intrinsicWidth,
        searchDrawable!!.intrinsicHeight)
    setCompoundDrawables(searchDrawable, null, null, null)
  }

  companion object {
    fun dip2px(context: Context, dpValue: Float): Int {
      val scale = context.resources.displayMetrics.density
      return (dpValue * scale + 0.5f).toInt()
    }
  }
}