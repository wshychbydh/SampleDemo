package com.cool.eye.func.permission

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.PermissionChecker
import com.eye.cool.permission.checker.Request

/**
 *Created by ycb on 2019/12/18 0018
 */
class PermissionTestActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_permission_test)
  }

  fun requestInActivity(v: View) {
    PermissionChecker(
        Request.Builder(this)
            .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .permission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .showInstallRationaleWhenRequest(false)
            .showRationaleWhenRequest(false)
            .requestManageExternalStorage()
            .build()
    ).check {
      ToastHelper.showToast(this, "授权${it.isSucceed()}")
      Log.i("Denied permission", "未授权权限-->${it.toDeniedText(this)}")
    }
  }

  fun requestInFragment(v: View) {
    PermissionTestDialogFragment().show(supportFragmentManager, "test")
  }

  fun requestInReceiver(v: View) {
    val intentFilter = IntentFilter()
    intentFilter.addAction("permission")
    registerReceiver(PermissionTestReceiver(), intentFilter)

    val intent = Intent()
    intent.action = "permission"
    sendBroadcast(intent)
  }

  fun requestInService(v: View) {
    startService(Intent(this, PermissionTestService::class.java))
  }
}