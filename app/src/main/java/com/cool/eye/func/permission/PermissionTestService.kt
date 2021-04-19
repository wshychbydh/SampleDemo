package com.cool.eye.func.permission

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.PermissionChecker
import com.eye.cool.permission.checker.Request
import kotlinx.coroutines.runBlocking

/**
 * Created by ycb on 2020/9/3
 */
class PermissionTestService : IntentService("permission") {

  override fun onHandleIntent(intent: Intent?) {
    runBlocking {
      val result = PermissionChecker(
          Request.Builder(this@PermissionTestService)
             // .permission(android.Manifest.permission.READ_SMS) //manifest中未注册，授权不会成功
              .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
              .requestInstallPackages()
              .showRationaleWhenRequest(true)
              .build()
      ).check()
      ToastHelper.showToast(this@PermissionTestService, "授权${result.isSucceed()}")
      Log.i("Denied permission", "未授权权限-->${result.toDeniedText(this@PermissionTestService)}")
    }
  }
}