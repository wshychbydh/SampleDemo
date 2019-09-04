package com.eye.cool.photo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.cool.eye.func.BuildConfig
import com.eye.cool.permission.Rationale
import com.eye.cool.photo.params.DialogParams
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.params.Params
import com.eye.cool.photo.support.*
import com.eye.cool.photo.utils.PhotoExecutor
import com.eye.cool.photo.view.DefaultView

/**
 * Compatible with androidx, PhotoDialogFragment is recommended
 *
 *Created by ycb on 2019/8/16 0016
 */
class PhotoPickerDialog : Activity(), DialogInterface {

  private val params = PhotoPickerDialog.params ?: Params.Builder().build()

  private lateinit var executor: PhotoExecutor

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    invasionStatusBar(this)
    params.wrapper = CompatContext(this)
    executor = PhotoExecutor(params)
    val selectListenerWrapper = OnSelectListenerWrapper(this, params.imageParams.onSelectListener)
    params.imageParams.onSelectListener = selectListenerWrapper
    window.setWindowAnimations(params.dialogParams.animStyle)

    val container = FrameLayout(this)
    container.layoutParams = ViewGroup.LayoutParams(-1, -1)
    val contentView = params.dialogParams.contentView ?: DefaultView(this)
    val layoutParams = FrameLayout.LayoutParams(-1, -2)
    layoutParams.gravity = Gravity.BOTTOM
    val method = contentView.javaClass.getDeclaredMethod("setOnActionListener", OnActionListener::class.java)
        ?: throw IllegalArgumentException("Custom View must declare method setOnActionListener(OnActionListener)")
    method.isAccessible = true
    method.invoke(contentView, executor)
    container.addView(contentView, layoutParams)
    setContentView(container)

    container.setOnClickListener {
      if (params.dialogParams.canceledOnTouchOutside) {
        cancel()
      }
    }

    executor.setOnClickListener(object : OnClickListener {
      override fun onClick(which: Int) {
        when (which) {
          Constants.ADJUST_PHOTO, Constants.SELECT_ALBUM -> playExitAnim(contentView)
          Constants.CANCEL, Constants.PERMISSION_FORBID -> dismiss()
        }
        params.dialogParams.onClickListener?.onClick(which)
      }
    })

    params.dialogParams.onShowListener?.onShow(this)
  }

  override fun cancel() {
    if (isFinishing) return
    params.dialogParams.onCancelListener?.onCancel(this)
    finish()
  }

  override fun dismiss() {
    if (isFinishing) return
    params.dialogParams.onDismissListener?.onDismiss(this)
    finish()
  }

  override fun onBackPressed() {
    if (params.dialogParams.cancelable) {
      dismiss()
    }
  }

  private fun playExitAnim(view: View) {
    val animator1 = ObjectAnimator.ofFloat(
        view,
        "translationY",
        0f,
        view.height.toFloat()
    )
    val animator2 = ValueAnimator.ofFloat(window.attributes.dimAmount, 0f)
    animator2.addUpdateListener {
      val dim = it.animatedValue as Float
      val lp = window.attributes
      lp.dimAmount = dim
      window.attributes = lp
    }
    val set = AnimatorSet()
    set.interpolator = LinearInterpolator()
    set.duration = 150
    set.playTogether(animator1, animator2)
    set.start()
  }

  override fun finish() {
    super.finish()
    PhotoPickerDialog.params = null
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (BuildConfig.DEBUG) {
      Log.d(Constants.TAG, "requestCode-->$requestCode")
    }
    if (resultCode == RESULT_OK) {
      executor.onActivityResult(requestCode, data)
    } else if (resultCode == RESULT_CANCELED) {
      dismiss()
    }
  }

  private class OnSelectListenerWrapper(
      val activity: PhotoPickerDialog,
      val listener: OnSelectListener?
  ) : OnSelectListener {
    override fun onSelect(path: String) {
      activity.dismiss()
      listener?.onSelect(path)
    }
  }

  companion object {

    private var params: Params? = null

    /**
     * The params for shown dialog
     * @param dialogParams
     */
    fun setDialogParams(dialogParams: DialogParams): Companion {
      if (params == null) params = Params.Builder().build()
      params!!.dialogParams = dialogParams
      return this
    }

    /**
     * The params for selected image
     * @param imageParams
     */
    fun setImageParams(imageParams: ImageParams): Companion {
      if (params == null) params = Params.Builder().build()
      params!!.imageParams = imageParams
      return this
    }

    /**
     * Permission setRationale when need
     * @param rationale
     */
    fun setRationale(rationale: Rationale?, showRationaleWhenRequest: Boolean = false): Companion {
      if (params == null) params = Params.Builder().build()
      params!!.rationale = rationale
      params!!.showRationaleWhenRequest = showRationaleWhenRequest
      return this
    }

    /**
     * Permission setting's setRationale when need
     * @param rationale
     */
    fun setRationaleSetting(rationaleSetting: Rationale?, showRationaleSettingWhenDenied: Boolean = true): Companion {
      if (params == null) params = Params.Builder().build()
      params!!.rationaleSetting = rationaleSetting
      params!!.showRationaleSettingWhenDenied = showRationaleSettingWhenDenied
      return this
    }

    /**
     * If registered permission of 'android.permission.CAMERA' in manifest,
     * you must set it to true, default false
     */
    fun requestCameraPermission(requestCameraPermission: Boolean): Companion {
      if (params == null) params = Params.Builder().build()
      params!!.requestCameraPermission = requestCameraPermission
      return this
    }

    fun show(context: Context) {
      if (params == null) params = Params.Builder().build()
      context.startActivity(Intent(context, PhotoPickerDialog::class.java))
    }

    /**
     * Set the content layout full the StatusBar, but do not hide StatusBar.
     */
    private fun invasionStatusBar(activity: Activity) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        val decorView = window.decorView
        decorView.systemUiVisibility = (
            decorView.systemUiVisibility
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
      }
    }
  }
}