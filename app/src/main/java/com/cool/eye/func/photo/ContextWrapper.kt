package com.cool.eye.func.photo

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle

/**
 *Created by cool on 2018/6/12
 */
class ContextWrapper {

    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var supportFragment: android.support.v4.app.Fragment? = null

    constructor(supportFragment: android.support.v4.app.Fragment) {
        this.supportFragment = supportFragment
    }

    constructor(activity: Activity) {
        this.activity = activity
    }

    constructor(fragment: Fragment) {
        this.fragment = fragment
    }


    fun startActivityForResult(intent: Intent?, requestCode: Int, bundle: Bundle? = null) {
        when {
            activity != null -> activity!!.startActivityForResult(intent, requestCode, bundle)
            fragment != null -> fragment!!.startActivityForResult(intent, requestCode, bundle)
            supportFragment != null -> supportFragment!!.startActivityForResult(intent, requestCode, bundle)
        }
    }

    fun getActivity(): Activity {
        if (activity != null) return activity!!
        if (fragment != null) return fragment!!.activity!!
        if (supportFragment != null) return supportFragment!!.activity!!
        throw IllegalStateException("ContextWrapper init error")
    }
}