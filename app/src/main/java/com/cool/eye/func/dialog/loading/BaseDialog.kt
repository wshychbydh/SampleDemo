package com.cool.eye.func.dialog.loading

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog


/**
 *Created by ycb on 2018/12/6 0006
 */
internal class BaseDialog constructor(
    context: Context,
    private val params: Params
) : AppCompatDialog(context, params.dialogStyle) {

  private var countDownTimer: LoadingCountDownTimer? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val contentView = params.contentView
    if (contentView == null) {
      val resId = params.contentLayoutId
      if (resId == 0) throw IllegalArgumentException("ContentView can not be empty!")
      setContentView(resId)
    } else {
      val parent = contentView.parent
      (parent as? ViewGroup)?.removeView(contentView)
      setContentView(contentView)
    }
  }

  override fun onStart() {
    super.onStart()
    setupParams()
  }

  override fun onStop() {
    countDownTimer?.cancel()
    super.onStop()
  }

  private fun setupParams() {
    val layoutParams = window!!.attributes
    layoutParams.x = params.xPos
    layoutParams.y = params.yPos
    layoutParams.gravity = params.gravity
    val density = context.resources.displayMetrics.density
    if (params.width > 0) {
      layoutParams.width = (params.width * density).toInt()
    }
    if (params.height > 0) {
      layoutParams.height = (params.height * density).toInt()
    }
    onWindowAttributesChanged(layoutParams)
    setCancelable(params.cancelAble)
    setCanceledOnTouchOutside(params.canceledOnTouchOutside)
    if (params.timeout > 0) {
      countDownTimer = LoadingCountDownTimer(params.timeout)
      countDownTimer?.start()
    }
  }

  inner class LoadingCountDownTimer(duration: Long) : CountDownTimer(duration, 100) {

    override fun onTick(millisUntilFinished: Long) {
    }

    override fun onFinish() {
      dismiss()
    }
  }
}