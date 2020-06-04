package com.cool.eye.func.permission

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.permission.support.Permission

/**
 *Created by ycb on 2019/12/18 0018
 */
class PermissionTestActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_permission_test)
  }

  fun requestInActivity(v: View) {
    PermissionHelper.Builder(this)
        .permission(android.Manifest.permission.CAMERA)
        .permissions(Permission.INSTALL_PACKAGE)
        .showRationaleWhenRequest(true)
        .showInstallRationaleWhenRequest(true)
        .deniedPermissionCallback {
          it.forEach { i ->
            Log.i("permission", "denied permission--->$i")
          }
        }
        .permissionCallback {
          Toast.makeText(this, "授权$it", Toast.LENGTH_SHORT).show()
        }
        .build()
        .request()
  }

  fun requestInFragment(v: View) {
    PermissionTestDialogFragment().show(supportFragmentManager, "test")
  }

  fun requestInContext(v: View) {
    val intentFilter = IntentFilter()
    intentFilter.addAction("permission")
    registerReceiver(PermissionTestReceiver(), intentFilter)

    val intent = Intent()
    intent.action = "permission"
    sendBroadcast(intent)
  }
}