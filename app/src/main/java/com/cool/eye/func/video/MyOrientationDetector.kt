package com.cool.eye.func.video

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.view.OrientationEventListener
import android.view.WindowManager

class MyOrientationDetector(private val activity: Activity) : OrientationEventListener(activity) {

  private val mWindowManager: WindowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  private var mLastOrientation = 0

  val isWideScreen: Boolean
    get() {
      val display = mWindowManager.defaultDisplay
      val pt = Point()
      display.getSize(pt)
      return pt.x > pt.y
    }

  override fun onOrientationChanged(orientation: Int) {
    val value = getCurentOrientationEx(orientation)
    if (value != mLastOrientation) {
      mLastOrientation = value
      val current = activity.requestedOrientation
      if (current == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || current == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
      }
    }
  }

  private fun getCurentOrientationEx(orientation: Int): Int {
    var value = 0
    if (orientation >= 315 || orientation < 45) {
      // 0度
      value = 0
      return value
    }
    if (orientation in 45..134) {
      // 90度
      value = 90
      return value
    }
    if (orientation in 135..224) {
      // 180度
      value = 180
      return value
    }
    if (orientation in 225..314) {
      // 270度
      value = 270
      return value
    }
    return value
  }
}