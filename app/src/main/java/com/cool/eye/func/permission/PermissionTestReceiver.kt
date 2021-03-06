package com.cool.eye.func.permission

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.PermissionChecker
import com.eye.cool.permission.checker.Request

class PermissionTestReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent?) {
    context.unregisterReceiver(this)
    PermissionChecker(
        Request.Builder(context)
            .permission(Manifest.permission.CAMERA)
            .requestInstallPackages()
            .showInstallRationaleWhenRequest(true)
            .showRationaleWhenRequest(true)
            .build()
    ).check {
      ToastHelper.showToast(context, "授权${it.isSucceed()}")
      Log.i("Denied permission", "未授权权限-->${it.toDeniedText(context)}")
    }
  }
}