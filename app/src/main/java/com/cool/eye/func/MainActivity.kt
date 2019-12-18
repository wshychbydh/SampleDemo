package com.cool.eye.func

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.address.mvp.view.AddressActivity
import com.cool.eye.func.banner.BannerActivity
import com.cool.eye.func.install.DownloadActivity
import com.cool.eye.func.notify.NotifyActivity
import com.cool.eye.func.permission.FilePermissionActivity
import com.cool.eye.func.permission.PermissionTestActivity
import com.cool.eye.func.photo.PhotoActivity
import com.cool.eye.func.recyclerview.mock.RecyclerAdapterActivity
import com.cool.eye.func.scan.ScanActivity
import com.cool.eye.func.task.TaskActivity
import com.cool.eye.func.theme.ThemeActivity
import com.cool.eye.func.view.trend.TrendActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun toFilePermission(view: View) {
    startActivity(Intent(this, FilePermissionActivity::class.java))
  }

  fun toDownloadApk(view: View) {
    startActivity(Intent(this, DownloadActivity::class.java))
  }

  fun toBanner(view: View) {
    startActivity(Intent(this, BannerActivity::class.java))
  }

  fun toTheme(view: View) {
    startActivity(Intent(this, ThemeActivity::class.java))
  }

  fun toPermission(view: View) {
    startActivity(Intent(this, PermissionTestActivity::class.java))
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

  fun toScan(v: View) {
    startActivity(Intent(this, ScanActivity::class.java))
  }
}
