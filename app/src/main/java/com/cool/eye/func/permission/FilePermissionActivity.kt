package com.cool.eye.func.permission

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.cool.eye.demo.R
import java.io.File

class FilePermissionActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_file_permission)
  }

  fun installApk(view: View) {
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        , "duty.apk")
    val intent = Intent(Intent.ACTION_VIEW)
    // 由于没有在Activity环境下启动Activity,设置下面的标签
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //判读版本是否在7.0以上
      val file = File(
          Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "duty.apk")
      //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
      val apkUri = FileProvider.getUriForFile(this, "bbb", file)
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
    } else {
      intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
    }
    startActivity(intent)
  }
}
