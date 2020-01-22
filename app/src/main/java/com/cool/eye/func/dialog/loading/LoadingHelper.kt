package com.cool.eye.func.dialog.loading

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleObserver
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.message_loading_dialog.view.*

/**
 *Created by ycb on 2018/12/6 0006
 */
object LoadingHelper : LifecycleObserver {

  @Volatile
  private var loadingDialog: BaseDialog? = null

  private val handler = Handler(Looper.getMainLooper())

  fun show(context: Context) {
    show(context, null)
  }

  fun show(context: Context, @StringRes resId: Int) {
    show(context, context.getString(resId))
  }

  fun show(context: Context, message: CharSequence?) {
    val builder = Params.Builder()
    val view = if (message.isNullOrEmpty()) LoadingView(context) else {
      val temp = LayoutInflater.from(context).inflate(R.layout.message_loading_dialog, null)
      temp.loadingTv.text = message
      temp
    }
    builder.setContentView(view)
    show(context, builder.build())
  }

  fun show(context: Context, params: Params) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
      showDialog(context, params)
    } else {
      handler.post {
        showDialog(context, params)
      }
    }
  }

  private fun showDialog(context: Context, params: Params) {
    if (isShowing()) {
      dismiss()
    }
    val dialog = BaseDialog(context, params)
    dialog.setOnDismissListener {
      loadingDialog = null
    }
    dialog.show()
    loadingDialog = dialog
  }

  fun dismiss() {
    handler.removeCallbacksAndMessages(null)
    loadingDialog?.dismiss()
    loadingDialog = null
  }

  fun isShowing(): Boolean {
    return loadingDialog?.isShowing == true
  }
}