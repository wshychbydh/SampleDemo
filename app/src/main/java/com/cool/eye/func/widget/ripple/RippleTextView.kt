package com.cool.eye.func.widget.ripple

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.cool.eye.demo.R

/**
 * @Description: 带水波纹的文本
 * @Author: 杨川博
 * @Date: 5/11/21
 * @License: Copyright Since 2020 Hive Box Technology. All rights reserved.
 * @Notice: This content is limited to the internal circulation of Hive Box, 
 * and it is prohibited to leak or used for other commercial purposes.
 */
class RippleTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {
  private val delegate = RippleDelegate(this)

  init {
    init(context, attrs)
  }

  private fun init(context: Context, attrs: AttributeSet?) {
    if (attrs == null) return
    if (isInEditMode) return
    val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleTextView)
    delegate.rippleAble =
        typedArray.getBoolean(R.styleable.RippleTextView_rtv_ripple, RippleConfig.rippleAble)
    delegate.rippleColor =
        typedArray.getColor(R.styleable.RippleTextView_rtv_color, RippleConfig.rippleColor)
    delegate.rippleType =
        typedArray.getInt(R.styleable.RippleTextView_rtv_type, RippleConfig.rippleType)
    delegate.isCenter =
        typedArray.getBoolean(
            R.styleable.RippleTextView_rtv_center,
            RippleConfig.isRippleCenter
        )
    delegate.rippleDuration =
        typedArray.getInteger(
            R.styleable.RippleTextView_rtv_ripple_duration,
            RippleConfig.rippleDuration
        )
    delegate.frameRate =
        typedArray.getInteger(
            R.styleable.RippleTextView_rtv_frame_rate,
            RippleConfig.frameRate.toInt()
        ).toLong()
    delegate.rippleAlpha =
        typedArray.getInteger(R.styleable.RippleTextView_rtv_alpha, RippleConfig.rippleAlpha)
    delegate.ripplePadding =
        typedArray.getDimensionPixelSize(
            R.styleable.RippleTextView_rtv_ripple_padding,
            RippleConfig.ripplePadding
        )
    delegate.zoomAble =
        typedArray.getBoolean(R.styleable.RippleTextView_rtv_zoom, RippleConfig.zoomAble)
    delegate.zoomScale =
        typedArray.getFloat(R.styleable.RippleTextView_rtv_zoom_scale, RippleConfig.zoomScale)
    delegate.zoomDuration =
        typedArray.getInt(
            R.styleable.RippleTextView_rtv_zoom_duration,
            RippleConfig.zoomDuration
        )
    typedArray.recycle()

    delegate.init()

    isClickable = isClickable || delegate.rippleAble || delegate.zoomAble

    this.isDrawingCacheEnabled = true
  }

  override fun draw(canvas: Canvas) {
    super.draw(canvas)
    delegate.drawRipple(canvas)
  }

  override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
    super.onSizeChanged(w, h, oldW, oldH)
    delegate.onSizeChanged(w, h)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    delegate.onTouchEvent(event)
    return super.onTouchEvent(event)
  }
}