package com.eye.cool.photo.support.v4

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatDialog
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.eye.cool.permission.Rationale
import com.eye.cool.photo.params.DialogParams
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.params.Params
import com.eye.cool.photo.support.Constants
import com.eye.cool.photo.support.OnActionListener
import com.eye.cool.photo.support.OnClickListener
import com.eye.cool.photo.support.OnSelectListenerWrapper
import com.eye.cool.photo.utils.PhotoExecutor
import com.eye.cool.photo.view.DefaultView

/**
 * Created by cool on 18-3-9
 */
class PhotoDialogFragment : AppCompatDialogFragment() {

  private lateinit var executor: PhotoExecutor
  private lateinit var params: Params
  private var createByBuilder = false

  override fun onAttach(context: Context?) {
    if (!createByBuilder) throw IllegalStateException("You must create it by Builder.build()!")
    super.onAttach(context)
    executor = PhotoExecutor(params)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = params.dialogParams.contentView ?: DefaultView(requireContext())
    val method = view.javaClass.getDeclaredMethod("setOnActionListener", OnActionListener::class.java)
        ?: throw IllegalArgumentException("Custom View must declare method setOnActionListener(OnActionListener)")
    method.isAccessible = true
    method.invoke(view, executor)
    return view
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialogParams = params.dialogParams
    val dialog = AppCompatDialog(context, dialogParams.dialogStyle)
    val window = dialog.window ?: return dialog
    window.setWindowAnimations(dialogParams.animStyle)
    val layoutParams = window.attributes
    layoutParams.x = dialogParams.xPos
    layoutParams.y = dialogParams.yPos
    dialog.onWindowAttributesChanged(layoutParams)
    dialog.setOnShowListener(dialogParams.onShowListener)
    dialog.setOnDismissListener(dialogParams.onDismissListener)
    dialog.setOnCancelListener(dialogParams.onCancelListener)
    return dialog
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    executor.setOnClickListener(object : OnClickListener {
      override fun onClick(which: Int) {
        when (which) {
          Constants.ADJUST_PHOTO, Constants.SELECT_ALBUM -> playExitAnim()
          Constants.CANCEL, Constants.PERMISSION_FORBID -> dismissAllowingStateLoss()
        }
        params.dialogParams.onClickListener?.onClick(which)
      }
    })
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    super.onActivityResult(requestCode, resultCode, intent)
    if (resultCode == Activity.RESULT_OK) {
      executor.onActivityResult(requestCode, intent)
    } else if (resultCode == Activity.RESULT_CANCELED) {
      dismissAllowingStateLoss()
    }
  }

  fun show(manager: FragmentManager) {
    show(manager, javaClass.simpleName)
  }

  private fun playExitAnim() {
    if (dialog == null || view == null) return
    val animator1 = ObjectAnimator.ofFloat(
        view,
        "translationY",
        0f,
        view!!.height.toFloat()
    )
    val window = dialog?.window ?: return
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

  class Builder {
    private val dialog = PhotoDialogFragment()
    private val paramsBuilder = Params.Builder(dialog)

    /**
     * The params for shown dialog
     * @param dialogParams
     */
    fun setDialogParams(dialogParams: DialogParams): Builder {
      paramsBuilder.setDialogParams(dialogParams)
      return this
    }

    /**
     * The params for selected image
     * @param imageParams
     */
    fun setImageParams(imageParams: ImageParams): Builder {
      paramsBuilder.setImageParams(imageParams)
      return this
    }

    /**
     * Permission setRationale when need
     * @param rationale
     * @param showRationaleWhenRequest
     */
    fun setRationale(rationale: Rationale?, showRationaleWhenRequest: Boolean = false): Builder {
      paramsBuilder.setRationale(rationale, showRationaleWhenRequest)
      return this
    }

    /**
     * Permission setting's setRationale when need
     * @param rationaleSetting
     * @param showRationaleSettingWhenDenied
     */
    fun setRationaleSetting(rationaleSetting: Rationale?, showRationaleSettingWhenDenied: Boolean = true): Builder {
      paramsBuilder.setRationaleSetting(rationaleSetting, showRationaleSettingWhenDenied)
      return this
    }

    /**
     * If registered permission of 'android.permission.CAMERA' in manifest,
     * you must set it to true, default false
     * @param requestCameraPermission
     */
    fun requestCameraPermission(requestCameraPermission: Boolean): Builder {
      paramsBuilder.requestCameraPermission(requestCameraPermission)
      return this
    }

    fun build(): PhotoDialogFragment {
      val params = paramsBuilder.build()
      val wrapper = OnSelectListenerWrapper(
          compatDialogFragment = dialog,
          listener = params.imageParams.onSelectListener
      )
      params.imageParams.onSelectListener = wrapper
      dialog.params = params
      dialog.createByBuilder = true
      return dialog
    }
  }
}