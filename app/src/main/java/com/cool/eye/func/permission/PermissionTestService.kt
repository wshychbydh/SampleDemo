package com.cool.eye.func.permission

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.checker.Request
import com.eye.cool.permission.checker.permissionForResult
import kotlinx.coroutines.runBlocking

/**
 * Created by ycb on 2020/9/3
 */
class PermissionTestService : IntentService("permission") {

  override fun onHandleIntent(intent: Intent?) {
    println("==onHandleIntent=>")
    runBlocking {
      println("==runBlocking=>")
      val result = permissionForResult(
          Request.Builder(this@PermissionTestService)
              .permission(android.Manifest.permission.SEND_SMS)
              .showRationaleWhenRequest(true)
              .build()
      )
      Log.i("Denied permission", result.denied?.joinToString(" ; ") ?: "None")
      ToastHelper.showToast(this@PermissionTestService, "授权${result.isSucceed()}")
    }
  }
}