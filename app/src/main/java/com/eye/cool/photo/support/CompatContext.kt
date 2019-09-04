package com.eye.cool.photo.support

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent

/**
 *Created by cool on 2018/6/12
 */
class CompatContext {

  private var fragment: Fragment? = null
  private var supportFragment: android.support.v4.app.Fragment? = null
  private var activity: Activity? = null

  constructor(supportFragment: android.support.v4.app.Fragment) {
    this.supportFragment = supportFragment
  }

  constructor(fragment: Fragment) {
    this.fragment = fragment
  }

  constructor(activity: Activity) {
    this.activity = activity
  }

  fun startActivityForResult(intent: Intent?, requestCode: Int) {
    when {
      supportFragment != null -> supportFragment!!.startActivityForResult(intent, requestCode)
      fragment != null -> fragment!!.startActivityForResult(intent, requestCode)
      activity != null -> activity!!.startActivityForResult(intent, requestCode)
      else -> throw IllegalStateException("CompatContext init error")
    }
  }

  fun context(): Context {
    return supportFragment?.context ?: fragment?.activity ?: activity
    ?: throw IllegalStateException("CompatContext init error")
  }

  fun activity(): Activity {
    return supportFragment?.activity ?: fragment?.activity ?: activity
    ?: throw IllegalStateException("CompatContext init error")
  }
}