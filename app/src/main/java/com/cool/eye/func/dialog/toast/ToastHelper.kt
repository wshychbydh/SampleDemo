package com.cool.eye.func.dialog.toast

import android.content.Context
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes

/**
 *Created by cool on 2018/5/2
 */
object ToastHelper {

  private val handler = Handler(Looper.getMainLooper())

  @Volatile
  private var durationToast: CustomDurationToast? = null

  @Volatile
  private var viewToast: CustomViewToast? = null

  @Volatile
  private var toast: Toast? = null

  /**
   * Cancel toast as it is being displayed
   */
  fun cancel(): ToastHelper {
    durationToast?.cancelAll()
    durationToast = null
    viewToast?.cancelAll()
    viewToast = null
    toast?.cancel()
    toast = null
    return this
  }

  @JvmStatic
  fun showToast(context: Context, @StringRes resId: Int) {
    showToast(context, context.getString(resId))
  }

  @JvmStatic
  fun showToastL(context: Context, @StringRes resId: Int) {
    showToastL(context, context.getString(resId))
  }

  @JvmStatic
  fun showError(context: Context, throwable: Throwable?) {
    if (throwable?.message?.isNotEmpty() == true) {
      showToast(context, throwable.message!!)
    }
  }

  @JvmStatic
  fun showErrorL(context: Context, throwable: Throwable?) {
    if (throwable?.message?.isNotEmpty() == true) {
      showToastL(context, throwable.message!!)
    }
  }

  @JvmStatic
  fun showToast(context: Context, msg: String?) {
    if (msg.isNullOrEmpty()) return
    if (Looper.getMainLooper() == Looper.myLooper()) {
      toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
      toast?.show()
    } else {
      handler.post {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast?.show()
      }
    }
  }

  @JvmStatic
  fun showToastL(context: Context, msg: String?) {
    if (msg.isNullOrEmpty()) return
    if (Looper.getMainLooper() == Looper.myLooper()) {
      toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
      toast?.show()
    } else {
      handler.post {
        toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
        toast?.show()
      }
    }
  }

  /**
   * @param context
   * @param msg
   * @param duration ms
   */
  fun showToast(context: Context, msg: String?, duration: Long) {
    if (msg.isNullOrEmpty()) return
    durationToast?.cancelAll()
    durationToast = CustomDurationToast(context, msg, duration).show()
  }

  fun showToast(view: View) {
    val toast = Toast(view.context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = view
    toast.show()
    ToastHelper.toast = toast
  }

  fun showToastL(view: View) {
    val toast = Toast(view.context)
    toast.duration = Toast.LENGTH_LONG
    toast.view = view
    toast.show()
    ToastHelper.toast = toast
  }

  fun showToast(view: View, duration: Long) {
    viewToast?.cancelAll()
    viewToast = CustomViewToast(view.context, view, duration).show()
  }

  private class CustomDurationToast(
      private val context: Context,
      private val msg: String?,
      duration: Long
  ) : CountDownTimer(duration, 3500) {

    private var toast: Toast? = null

    private fun showToast(): Toast {
      val toast = Toast.makeText(context, msg, Toast.LENGTH_LONG)
      toast.show()
      return toast
    }

    fun show(): CustomDurationToast {
      start()
      toast = showToast()
      return this
    }

    fun cancelAll() {
      cancel()
      toast?.cancel()
      durationToast = null
    }

    override fun onTick(millisUntilFinished: Long) {
      toast = showToast()
    }

    override fun onFinish() {
      toast?.cancel()
      toast = null
      durationToast = null
    }
  }

  private class CustomViewToast(
      private val context: Context,
      private val view: View,
      duration: Long
  ) : CountDownTimer(duration, 3500) {

    private var toast: Toast? = null

    private fun showToast(): Toast {
      val toast = Toast(context)
      toast.duration = Toast.LENGTH_LONG
      toast.view = view
      toast.show()
      return toast
    }

    fun show(): CustomViewToast {
      start()
      toast = showToast()
      return this
    }

    fun cancelAll() {
      cancel()
      toast?.cancel()
      viewToast = null
    }

    override fun onTick(millisUntilFinished: Long) {
      toast = showToast()
    }

    override fun onFinish() {
      toast?.cancel()
      toast = null
      viewToast = null
    }
  }
}