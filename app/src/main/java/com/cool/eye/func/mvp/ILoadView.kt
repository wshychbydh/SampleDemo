package com.cool.eye.func.mvp

import android.app.Activity
import android.content.Context
import com.cool.eye.demo.R
import com.cool.eye.func.dialog.loading.LoadingHelper
import com.cool.eye.func.dialog.toast.ToastHelper

/**
 *Created by ycb on 2018/09/06
 */
interface ILoadView : IView {

  fun showError(throwable: Throwable? = null) {
    if (!isAttachContextAlive()) return
    ToastHelper.showError(withContext(), throwable)
    hideLoading()
  }

  fun showLoading() {
    if (!isAttachContextAlive()) return
    LoadingHelper.show(withContext())
  }

  fun hideLoading() {
    LoadingHelper.dismiss()
  }

  fun showToast(msg: String?) {
    if (!isAttachContextAlive()) return
    hideLoading()
    if (msg.isNullOrEmpty()) return
    ToastHelper.showToast(withContext(), msg)
  }

  fun showToast(resId: Int) {
    ToastHelper.showToast(withContext(), resId)
  }


  fun showLoading(msg: String) {
    if (!isAttachContextAlive()) return
    LoadingHelper.show(withContext(), msg)
  }

  fun showLoading(resId: Int) {
    if (!isAttachContextAlive()) return
    val context = withContext()
    LoadingHelper.show(context, context.getString(resId))
  }

  fun withContext(): Context

  fun isAttachContextAlive(): Boolean {
    val context = withContext()
    if (context is Activity) {
      return !context.isFinishing && !context.isDestroyed
    }
    try {
      context.getString(R.string.app_name)
    } catch (e: Exception) {
      return false
    }
    return true
  }
}