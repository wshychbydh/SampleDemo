package com.cool.eye.func.permission

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.PermissionChecker
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.permission.checker.Request
import com.eye.cool.permission.support.Permission
import kotlinx.coroutines.MainScope

class PermissionTestReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent?) {
    context.unregisterReceiver(this)
    PermissionChecker(
        Request.Builder(context)
            .permission(Manifest.permission.CAMERA)
            .permission(Manifest.permission.REQUEST_INSTALL_PACKAGES)
            .showInstallRationaleWhenRequest(true)
            .showRationaleWhenRequest(true)
            .build()
    ).check {
      Log.i("Denied permission", it.denied?.joinToString(" ; ") ?: "None")
      ToastHelper.showToast(context, "授权${it.isSucceed()}")
    }
  }
}