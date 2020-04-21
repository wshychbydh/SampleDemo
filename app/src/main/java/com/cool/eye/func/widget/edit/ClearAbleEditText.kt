package com.cool.eye.func.widget.edit

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.AppCompatEditText
import com.cool.eye.demo.R

open class ClearAbleEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyle) {

  private var mTextCount = 0
  private var mClearIconGravity = Gravity.TOP
  private var mClearIconTop = 0
  private var mClearIconLeft = 0
  private var mClearIconWidth = 0
  private var mClearIconHeight = 0
  private var mClearIconPadding = 0
  private var mLastX = -1f
  private var mLastY = -1f

  private var mClearIconDrawable: Drawable? = null

  private var mNeedInvalidate = true
  private var mIsClearIconClipPadding = false
  private var mIsClearIconDevision = true
  private var mIsConfigChanged = false

  private var isClearIconDisabled = false

  init {
    val theme = context.theme
    val typedArray = theme.obtainStyledAttributes(attrs, R.styleable
        .ClearAbleEditText, defStyle, 0)
    val count = typedArray.indexCount
    for (i in 0 until count) {
      val attr = typedArray.getIndex(i)
      if (attr == R.styleable.ClearAbleEditText_clearIconPadding) {
        setClearIconPadding(typedArray.getDimension(attr, 0f).toInt())
      } else if (attr == R.styleable.ClearAbleEditText_clearIconDrawable) {
        val drawable = typedArray.getDrawable(attr)
        if (drawable != null) {
          mClearIconDrawable = drawable
        }
      } else if (attr == R.styleable.ClearAbleEditText_clearIconGravity) {
        setClearIconGravity(typedArray.getInt(attr, -1))
      } else if (attr == R.styleable.ClearAbleEditText_clearIconClipParentPadding) {
        mIsClearIconClipPadding = typedArray.getBoolean(attr, false)
      } else if (attr == R.styleable.ClearAbleEditText_clearIconDivision) {
        setClearIconDivision(typedArray.getBoolean(attr, true))
      } else if (attr == R.styleable.ClearAbleEditText_clearIconDisabled) {
        mIsClearIconClipPadding = typedArray.getBoolean(attr, false)
      }
    }
    typedArray.recycle()
    onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        showClear(text!!.length)
      } else {
        hideClear()
      }
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    mIsConfigChanged = true
  }

  fun setClearIconDivision(isClearIconDivision: Boolean) {
    mIsClearIconDevision = isClearIconDivision
  }

  fun setClearIconPadding(clearIconPadding: Int) {
    mClearIconPadding = clearIconPadding
  }

  fun setClearIconGravity(clearIconGravity: Int) {
    if (clearIconGravity == -1) {
      return
    }
    if (clearIconGravity != mClearIconGravity) {
      mNeedInvalidate = true
      mClearIconGravity = clearIconGravity
      invalidate()
    }
  }

  fun hideClear() {
    mTextCount = 0
  }

  fun showClear(size: Int) {
    mTextCount = size
  }

  fun setClearDisable(isClearDisabled: Boolean) {
    isClearIconDisabled = isClearDisabled
  }

  override fun draw(canvas: Canvas) {
    super.draw(canvas)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    if (mClearIconDrawable != null && isEnabled) {
      val bitmap = (mClearIconDrawable as BitmapDrawable).bitmap
      mClearIconHeight = bitmap.height
      mClearIconWidth = bitmap.width
      if (mTextCount > 0 && !isClearIconDisabled && hasFocus()) {
        if (mNeedInvalidate || mIsConfigChanged) {
          mNeedInvalidate = false
          setupLeftTopOffset()
        }
        val left = mClearIconLeft + scrollX
        val top = mClearIconTop + scrollY
        val paint = paint
        canvas.drawBitmap(bitmap, left.toFloat(), top.toFloat(), paint)
      }
    }

  }

  private fun setupLeftTopOffset() {
    val verticalGravity = mClearIconGravity and Gravity.VERTICAL_GRAVITY_MASK
    var topPadding = paddingTop
    var bottomPadding = paddingBottom
    var leftPadding = paddingLeft
    var rightPadding = paddingRight
    when (verticalGravity) {
      Gravity.TOP -> {
        topPadding += mClearIconPadding + mClearIconHeight - 15
        mClearIconTop = (if (mIsClearIconClipPadding) 0 else paddingTop) + mClearIconPadding
      }
      Gravity.BOTTOM -> {
        bottomPadding += mClearIconPadding + mClearIconHeight - 15
        mClearIconTop = bottom - mClearIconHeight - (if (mIsClearIconClipPadding)
          0
        else
          paddingBottom) - mClearIconPadding - top
      }
      Gravity.CENTER_VERTICAL -> mClearIconTop = (bottom - top - mClearIconHeight) / 2
      else -> mClearIconTop = mClearIconPadding
    }
    when (mClearIconGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
      Gravity.LEFT -> {
        if (!mIsConfigChanged) {
          leftPadding += mClearIconPadding + mClearIconWidth
        }
        mClearIconLeft = (if (mIsClearIconClipPadding) 0 else paddingLeft) + mClearIconPadding
      }
      Gravity.RIGHT -> {
        if (!mIsConfigChanged) {
          rightPadding += mClearIconPadding + mClearIconWidth
        }
        mClearIconLeft = right - mClearIconWidth - (if (mIsClearIconClipPadding)
          0
        else
          paddingRight) - mClearIconPadding / 2 - left
      }
      Gravity.CENTER_HORIZONTAL -> mClearIconLeft = (right - left - mClearIconWidth) / 2
      else -> mClearIconLeft = mClearIconPadding
    }
    if (mIsClearIconDevision) {
      setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
    }
    mIsConfigChanged = false
  }

  override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int,
                             lengthAfter: Int) {
    super.onTextChanged(text, start, lengthBefore, lengthAfter)
    mTextCount = text?.length ?: 0
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (!isEnabled) {
      return super.onTouchEvent(event)
    }
    val action = event.action
    val x = event.x
    val y = event.y
    val left = mClearIconLeft - mClearIconPadding
    val top = mClearIconTop - mClearIconPadding
    val right = mClearIconLeft + mClearIconWidth + mClearIconPadding
    val bottom = mClearIconTop + mClearIconHeight + mClearIconPadding

    when (action) {
      MotionEvent.ACTION_DOWN -> if (mTextCount > 0 && mClearIconDrawable != null && x > left && x < right && y < bottom && y > top) {
        mLastX = event.x
        mLastY = event.y
      }
      MotionEvent.ACTION_UP -> if (mLastX != -1f && mLastY != -1f) {
        if (x > left && x < right && y < bottom && y > top) {
          setText("")
        }
        mLastX = -1f
        mLastY = -1f
      }
      else -> {
      }
    }
    return super.onTouchEvent(event)
  }
}