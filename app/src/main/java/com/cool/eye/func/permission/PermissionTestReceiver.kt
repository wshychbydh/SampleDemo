package com.cool.eye.func.permission

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.permission.support.Permission

public class PermissionTestReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent?) {
    context.unregisterReceiver(this)
    PermissionHelper.Builder(context)
        .permissions(Permission.CAMERA)
        .showRationaleWhenRequest(true)
        .deniedPermissionCallback {
          it.forEach { i ->
            println("receiver denied permission--->$i")
          }
        }
        .permissionCallback {
          Toast.makeText(context, "receiver 授权$it", Toast.LENGTH_SHORT).show()
        }
        .build()
        .request()
  }
}