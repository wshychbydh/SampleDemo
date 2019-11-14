package com.eye.cool.photo.params

import android.content.DialogInterface
import android.content.res.Resources
import androidx.annotation.StyleRes
import android.view.View
import com.cool.eye.func.R
import com.eye.cool.photo.support.OnClickListener

/**
 *Created by ycb on 2019/8/8 0008
 */
class DialogParams private constructor() {

  internal var contentView: View? = null

  /**
   * Nonsupport for PhotoPickerDialog
   */
  @StyleRes
  internal var dialogStyle: Int = R.style.PhotoDialog

  @StyleRes
  internal var animStyle: Int = R.style.AnimBottom

  internal var cancelable: Boolean = true

  internal var canceledOnTouchOutside: Boolean = true

  /**
   * Nonsupport for PhotoPickerDialog
   */
  internal var xPos = 0

  /**
   * Nonsupport for PhotoPickerDialog
   */
  internal var yPos = Resources.getSystem().displayMetrics.heightPixels

  internal var onShowListener: DialogInterface.OnShowListener? = null

  internal var onDismissListener: DialogInterface.OnDismissListener? = null

  internal var onCancelListener: DialogInterface.OnCancelListener? = null

  internal var onClickListener: OnClickListener? = null

  class Builder {

    private var params = DialogParams()

    /**
     * dialog's contentView for shown
     * @param view the shown view
     */
    fun setContentView(view: View): Builder {
      params.contentView = view
      return this
    }

    /**
     * Sets whether this dialog is cancelable with the
     * {@link KeyEvent#KEYCODE_BACK BACK} key.
     * @param cancelable
     */
    fun setCancelable(cancelable: Boolean): Builder {
      params.cancelable = cancelable
      return this
    }

    /**
     * Sets whether this dialog is canceled when touched outside the window's
     * bounds. If setting to true, the dialog is set to be cancelable if not
     * already set.
     *
     * @param cancel Whether the dialog should be canceled when touched outside
     *            the window.
     */
    fun setCanceledOnTouchOutside(cancel: Boolean): Builder {
      params.canceledOnTouchOutside = cancel
      return this
    }

    /**
     * Pop-up animation style，default from bottom
     * @param animStyle
     */
    fun setAnimStyle(animStyle: Int): Builder {
      params.animStyle = animStyle
      return this
    }

    /**
     * a style resource describing the theme to use for the window, or {@code 0} to use the default dialog theme
     * @param dialogStyle
     */
    fun setDialogStyle(dialogStyle: Int): Builder {
      params.dialogStyle = dialogStyle
      return this
    }

    /**
     * Dialog popup location
     * @param x
     * @param y
     */
    fun setCoordinate(x: Int, y: Int): Builder {
      params.xPos = x
      params.yPos = y
      return this
    }

    /**
     * Sets a listener to be invoked when the dialog is shown.
     * @param listener The {@link DialogInterface.OnShowListener} to use.
     */
    fun setOnShowListener(listener: DialogInterface.OnShowListener): Builder {
      params.onShowListener = listener
      return this
    }

    /**
     * Set a listener to be invoked when the dialog is dismissed.
     * @param listener The {@link DialogInterface.OnDismissListener} to use.
     */
    fun setOnDismissListener(listener: DialogInterface.OnDismissListener): Builder {
      params.onDismissListener = listener
      return this
    }

    /**
     * Set a listener to be invoked when the dialog is canceled.
     *
     * <p>This will only be invoked when the dialog is canceled.
     * Cancel events alone will not capture all ways that
     * the dialog might be dismissed. If the creator needs
     * to know when a dialog is dismissed in general, use
     * {@link #setOnDismissListener}.</p>
     *
     * @param listener The {@link DialogInterface.OnCancelListener} to use.
     */
    fun setOnCancelListener(listener: DialogInterface.OnCancelListener): Builder {
      params.onCancelListener = listener
      return this
    }

    /**
     * Set a listener to be invoked when the button is clicked.
     * <p>
     *   Only one of these button's {@link Constants#TAKE_PHOTO, SELECT_ALBUM, ADJUST_PHOTO} onclick will be invoked
     * </p>
     * @param listener
     */
    fun setOnClickListener(listener: OnClickListener): Builder {
      params.onClickListener = listener
      return this
    }

    fun build() = params
  }
}