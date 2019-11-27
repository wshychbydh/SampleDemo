package com.cool.eye.func.address

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.cool.eye.demo.R

/**
 * Created by cool on 2018/4/18.
 */

class QuickBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

  private var selected = -1
  private val paint = Paint()
  private var letterHeight = 0
  private val defaultBackground = ColorDrawable(0x00000000)
  private val selectedBackground = resources.getDrawable(R.drawable.sidebar_background)
  private var animator: Animator? = null
  var duration = 1500L
    set(value) {
      field = value
      animator?.duration = duration
    }

  //FIXME Toast is recommend, but it's not easy to use.
  var toastView: TextView? = null
    set(value) {
      field = value
      animator = AnimatorInflater.loadAnimator(context, R.animator.fade_out)
      animator!!.setTarget(toastView)
      animator!!.duration = duration
    }
  var isShowBackground = false
  var onLetterChangedListener: ((letter: String) -> Unit)? = null

  var content = DEFAULTS
    set(value) {
      field = value
      contentSize = value.size
    }
  private var contentSize = content.size

  var textSize = 14f
    set(value) {
      paint.textSize = dip2px(context, value).toFloat()
    }
  var textColor = Color.BLACK
  var selectedColor = Color.RED

  init {
    paint.typeface = Typeface.DEFAULT
    paint.isAntiAlias = true
    paint.textSize = dip2px(context, textSize).toFloat()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    var minWidth = 0
    content.forEach {
      minWidth = Math.max(minWidth, paint.measureText(it).toInt())
    }
    minWidth = Math.max(minWidth, MeasureSpec.getSize(widthMeasureSpec))
    val specHeight = MeasureSpec.getSize(heightMeasureSpec)

    setMeasuredDimension(Math.min(minWidth, 90), specHeight)
    letterHeight = specHeight / content.size
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    for (pos in content.indices) {
      if (pos == selected) {
        paint.color = selectedColor
        paint.isFakeBoldText = true
      } else {
        paint.color = textColor
        paint.isFakeBoldText = false
      }
      // coordX is half width of content
      val xPos = (width - paint.measureText(content[pos])) / 2
      val yPos = (letterHeight * pos + letterHeight).toFloat()
      canvas.drawText(content[pos], xPos, yPos, paint)
    }
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    val oldChoose = selected
    val pos = (event.y / height * contentSize).toInt()

    when (event.action) {
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        if (isShowBackground) {
          setBackgroundDrawable(defaultBackground)
        }
        selected = -1
        invalidate()
        cancelToast()
      }

      else -> {
        if (isShowBackground) {
          setBackgroundDrawable(selectedBackground)
        }
        if (oldChoose != pos) {
          if (pos in 0 until contentSize) {
            val text = content[pos]
            onLetterChangedListener?.invoke(text)
            selected = pos
            invalidate()
            showToast(text)
          }
        }
      }
    }
    return true
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    cancelToast()
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    if (visibility != VISIBLE) {
      cancelToast()
    }
  }

  private fun showToast(str: String) {
    if (toastView != null) {
      toastView!!.text = str
      toastView!!.visibility = VISIBLE
      toastView!!.postDelayed(runnable, duration)
      animator?.start()
    }
  }

  private fun cancelToast() {
    if (toastView != null) {
      toastView!!.removeCallbacks(runnable)
      if (animator == null) {
        toastView!!.visibility = INVISIBLE
      } else {
        if (!animator!!.isRunning) {
          animator?.start()
        }
      }
    }
  }

  private val runnable = Runnable {
    if (toastView != null) {
      toastView!!.visibility = INVISIBLE
      animator?.cancel()
    }
  }

  companion object {
    var DEFAULTS = arrayOf("热", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
        "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

    fun dip2px(context: Context, dpValue: Float): Int {
      val scale = context.resources.displayMetrics.density
      return (dpValue * scale + 0.5f).toInt()
    }
  }
}
