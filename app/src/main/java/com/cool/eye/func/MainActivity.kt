package com.cool.eye.func

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.address.mvp.view.AddressActivity
import com.cool.eye.func.banner.BannerActivity
import com.cool.eye.func.notify.NotifyActivity
import com.cool.eye.func.permission.FilePermissionActivity
import com.cool.eye.func.photo.PhotoActivity
import com.cool.eye.func.recyclerview.mock.RecyclerAdapterActivity
import com.cool.eye.func.task.TaskActivity
import com.cool.eye.func.theme.ThemeActivity
import com.cool.eye.func.view.trend.TrendActivity
import com.eye.cool.permission.Permission
import com.eye.cool.permission.PermissionHelper
import java.io.File

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    var file = File(Environment.getExternalStorageDirectory(), ".")
    println("file--1-->${file.absolutePath}")
    file = File(Environment.getExternalStorageDirectory(), "/")
    println("file--2-->${file.absolutePath}")
    file = File(Environment.getExternalStorageDirectory(), "")
    println("file--3-->${file.absolutePath}")

    file = File("/", "a")
    println("file--4-->${file.absolutePath}")

    file = File("", "b")
    println("file--5-->${file.absolutePath}")

    file = File(".", "")
    println("file--6-->${file.absolutePath}")
  }

  fun toFilePermission(view: View) {
    startActivity(Intent(this, FilePermissionActivity::class.java))
  }

  fun toBanner(view: View) {
    startActivity(Intent(this, BannerActivity::class.java))
  }

  fun toTheme(view: View) {
    startActivity(Intent(this, ThemeActivity::class.java))
  }

  fun toPermission(view: View) {
    PermissionHelper.Builder(this)
        .permission(android.Manifest.permission.REQUEST_INSTALL_PACKAGES)
        .permissions(Permission.CALL_LOG)
        .showRationaleWhenRequest(true)
        .deniedPermissionCallback {
         it.forEach {i->
           println("denied permission--->$i")
         }
        }
        .permissionCallback {
          Toast.makeText(this, "授权$it", Toast.LENGTH_SHORT).show()
        }
        .build()
        .request()
  }

  fun toAddress(view: View) {
    startActivity(Intent(this, AddressActivity::class.java))
  }

  fun toNotify(view: View) {
    startActivity(Intent(this, NotifyActivity::class.java))
  }

  fun toTrendView(v: View) {
    startActivity(Intent(this, TrendActivity::class.java))
  }

  fun toPhotoView(v: View) {
    startActivity(Intent(this, PhotoActivity::class.java))
  }

  fun toTask(v: View) {
    startActivity(Intent(this, TaskActivity::class.java))
  }

  fun toAdapter(v: View) {
    startActivity(Intent(this, RecyclerAdapterActivity::class.java))
  }
}
