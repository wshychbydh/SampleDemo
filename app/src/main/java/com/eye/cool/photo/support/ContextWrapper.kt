package com.eye.cool.photo.support

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

/**
 *Created by cool on 2018/6/12
 */
class ContextWrapper {

  private var fragment: Fragment? = null
  private var activity: Activity? = null

  constructor(supportFragment: Fragment) {
    this.fragment = supportFragment
  }

  constructor(activity: Activity) {
    this.activity = activity
  }

  fun startActivityForResult(intent: Intent?, requestCode: Int) {
    when {
      fragment != null -> fragment!!.startActivityForResult(intent, requestCode)
      activity != null -> activity!!.startActivityForResult(intent, requestCode)
      else -> throw IllegalStateException("ContextWrapper init error")
    }
  }

  fun context(): Context {
    return activity ?: fragment?.context ?: throw IllegalStateException("ContextWrapper init error")
  }

  fun activity(): Activity {
    return activity ?: fragment?.activity ?: throw IllegalStateException("ContextWrapper init error")
  }
}