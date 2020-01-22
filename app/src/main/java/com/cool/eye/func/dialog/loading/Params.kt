package com.cool.eye.func.dialog.loading

import android.view.Gravity
import android.view.View
import androidx.annotation.IdRes

class Params private constructor() {
  internal var contentView: View? = null
  internal var contentLayoutId: Int = 0
  internal var cancelAble: Boolean = true
  internal var canceledOnTouchOutside = false
  internal var timeout: Long = 60 * 1000L
  internal var width: Int = 0
  internal var height: Int = 0
  internal var dialogStyle: Int = 0
  internal var xPos: Int = 0
  internal var yPos: Int = 0
  internal var gravity: Int = Gravity.CENTER

  class Builder {

    private val params = Params()

    /**
     * Dialog's contentView to be shown
     *
     * @param view the shown view
     */
    fun setContentView(view: View): Builder {
      params.contentView = view
      return this
    }

    /**
     * Dialog's contentView to be shown
     *
     * @param view the shown view
     */
    fun setContentLayoutId(@IdRes layoutId: Int): Builder {
      params.contentLayoutId = layoutId
      return this
    }

    /**
     * Sets whether this dialog is cancelable with the
     * {@link KeyEvent#KEYCODE_BACK BACK} key.
     *
     * @param cancelAble
     */
    fun setCancelAble(cancelAble: Boolean): Builder {
      params.cancelAble = cancelAble
      return this
    }

    /**
     * Sets whether this dialog is canceled when touched outside the window's
     * bounds. If setting to true, the dialog is set to be cancelable if not
     * already set.
     *
     * @param cancel Whether the dialog should be canceled when touched outside the window.
     */
    fun setCanceledOnTouchOutside(cancel: Boolean): Builder {
      params.canceledOnTouchOutside = cancel
      return this
    }

    /**
     * A style resource describing the theme to use for the window, or {@code 0} to use the default dialog theme
     *
     * @param dialogStyle
     */
    fun setDialogStyle(dialogStyle: Int): Builder {
      params.dialogStyle = dialogStyle
      return this
    }

    /**
     *
     * @param width
     * @param height
     */
    fun setSize(width: Int, height: Int): Builder {
      params.width = width
      params.height = height
      return this
    }

    /**
     * Placement of window within the screen as per {@link Gravity}.  Both
     * {@link Gravity#apply(int, int, int, android.graphics.Rect, int, int,
     * android.graphics.Rect) Gravity.apply} and
     * {@link Gravity#applyDisplay(int, android.graphics.Rect, android.graphics.Rect)
     * Gravity.applyDisplay} are used during window layout, with this value
     * given as the desired gravity.  For example you can specify
     * {@link Gravity#DISPLAY_CLIP_HORIZONTAL Gravity.DISPLAY_CLIP_HORIZONTAL} and
     * {@link Gravity#DISPLAY_CLIP_VERTICAL Gravity.DISPLAY_CLIP_VERTICAL} here
     * to control the behavior of
     * {@link Gravity#applyDisplay(int, android.graphics.Rect, android.graphics.Rect)
     * Gravity.applyDisplay}.
     *
     * @see Gravity
     */
    fun setGravity(gravity: Int): Builder {
      params.gravity = gravity
      return this
    }

    /**
     * X and Y position for this window.  With the default gravity it is ignored.
     * When using {@link Gravity#TOP} or {@link Gravity#BOTTOM} it provides
     * an offset from the given edge.
     *
     * @param x
     * @param y
     */
    fun setCoordinate(x: Int, y: Int): Builder {
      params.xPos = x
      params.yPos = y
      return this
    }

    /**
     * How long does the dialog showing, 0 or negative meaning never auto dismiss
     *
     * @param timeout millisecond , default 60 * 1000L
     */
    fun setTimeout(timeout: Long): Builder {
      params.timeout = timeout
      return this
    }

    fun build(): Params {
      return params
    }
  }
}