package com.eye.cool.photo

import android.app.Activity
import android.app.Fragment
import android.content.DialogInterface
import androidx.fragment.app.FragmentActivity
import android.view.View
import com.cool.eye.func.R
import com.eye.cool.photo.params.DialogParams
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.support.CompatContext
import com.eye.cool.photo.support.OnSelectListenerWrapper
import com.eye.cool.photo.view.EmptyView

/**
 *Created by ycb on 2019/8/14 0014
 */
class PhotoHelper {

  private val compat: CompatContext

  constructor(supportFragment: androidx.fragment.app.Fragment) {
    compat = CompatContext(supportFragment)
  }

  constructor(fragment: Fragment) {
    compat = CompatContext(fragment)
  }

  constructor(activity: Activity) {
    compat = CompatContext(activity)
  }

  /**
   * take a photo
   * @param params settings of photo
   * @param requestCameraPermission If registered permission of 'android.permission.CAMERA' in manifest,
   * you must set it to true, default false
   */
  fun onTakePhoto(params: ImageParams, requestCameraPermission: Boolean = false) {
    val contentView = EmptyView(compat.context())
    val builder = createDefaultDialogParams(contentView)
    builder.setOnShowListener(DialogInterface.OnShowListener {
      contentView.onTakePhoto()
    })
    execute(builder.build(), params, requestCameraPermission)
  }

  /**
   * select from album
   * @param params settings of photo
   */
  fun onSelectAlbum(params: ImageParams) {
    val contentView = EmptyView(compat.context())
    val builder = createDefaultDialogParams(contentView)
    builder.setOnShowListener(DialogInterface.OnShowListener {
      contentView.onSelectAlbum()
    })
    execute(builder.build(), params)
  }

  private fun createDefaultDialogParams(contentView: View): DialogParams.Builder {
    return DialogParams.Builder()
        .setDialogStyle(R.style.PhotoDialog_Translucent)
        .setCancelable(false)
        .setCanceledOnTouchOutside(false)
        .setContentView(contentView)
  }

  private fun execute(dialogParams: DialogParams, params: ImageParams, requestCameraPermission: Boolean = false) {
    val activity = compat.activity()
    if (activity is androidx.fragment.app.FragmentActivity) {
      val dialog = createSupportDialogFragment(dialogParams, params, requestCameraPermission)
      params.onSelectListener = OnSelectListenerWrapper(
          compatDialogFragment = dialog,
          listener = params.onSelectListener
      )
      dialog.show(activity.supportFragmentManager)
    } else {
      val dialog = createDialogFragment(dialogParams, params)
      params.onSelectListener = OnSelectListenerWrapper(
          dialogFragment = dialog,
          listener = params.onSelectListener
      )
      dialog.show(activity.fragmentManager)
    }
  }

  private fun createDialogFragment(dialogParams: DialogParams, params: ImageParams): PhotoDialogFragment {
    return PhotoDialogFragment.Builder()
        .setImageParams(params)
        .setDialogParams(dialogParams)
        .build()
  }

  private fun createSupportDialogFragment(
      dialogParams: DialogParams,
      imageParams: ImageParams,
      requestCameraPermission: Boolean
  ): com.eye.cool.photo.support.v4.PhotoDialogFragment {
    return com.eye.cool.photo.support.v4.PhotoDialogFragment.Builder()
        .setImageParams(imageParams)
        .setDialogParams(dialogParams)
        .requestCameraPermission(requestCameraPermission)
        .build()
  }
}