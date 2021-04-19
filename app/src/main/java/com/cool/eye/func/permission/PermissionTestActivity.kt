package com.cool.eye.func.permission

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.DIRECTORY_PICTURES
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.PermissionChecker
import com.eye.cool.permission.checker.Request
import java.io.File

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
           // .permission(android.Manifest.permission_group.STORAGE)
            .permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .showManageFileRationaleWhenRequest(true)
            .showRationaleWhenRequest(true)
            .build()
    ).check {
      if (it.isSucceed()) {
        val test = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
        test.listFiles()?.forEach { file ->
          Log.i("test file access", "${file.name}-->access:${file.canWrite()}")
        }
        val file = File(test,"test_file")
        if (file.exists()) {
          file.deleteRecursively()
          Log.i("test file access", "是否删除成功: ${!file.exists()}")
        }
        file.writeBytes("This is a test content".toByteArray())
        Log.i("test file access", "文本内容: ${String(file.readBytes())}")
      }
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