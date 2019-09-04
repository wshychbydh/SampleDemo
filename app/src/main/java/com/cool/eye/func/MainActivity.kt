package com.cool.eye.func

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.cool.eye.func.address.mvp.view.AddressActivity
import com.cool.eye.func.notify.NotifyActivity
import com.cool.eye.func.photo.PhotoActivity
import com.cool.eye.func.recyclerview.mock.RecyclerAdapterActivity
import com.cool.eye.func.task.TaskActivity
import com.cool.eye.func.view.trend.TrendActivity
import com.eye.cool.permission.Permission
import com.eye.cool.permission.PermissionHelper

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun toPermission(view: View) {
    PermissionHelper.Builder(this)
        .permissions(Permission.CAMERA)
        .permissions(Permission.STORAGE)
        .permissions(Permission.MICROPHONE)
        .showRationaleWhenRequest(false)
        .showRationaleSettingWhenDenied(true)
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
