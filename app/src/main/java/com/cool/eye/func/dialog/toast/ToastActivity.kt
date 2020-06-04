package com.cool.eye.func.dialog.toast

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cool.eye.demo.R

/**
 *Created by ycb on 2020/1/21 0021
 */
class ToastActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_toast)
  }

  fun showDefaultShot(v: View) {
    ToastHelper.showToast(this, "默认Short Toast")
  }

  fun showDefaultLong(v: View) {
    ToastHelper.showToast(this, "默认Long Toast")
  }

  fun showCustomDuration(v: View) {
    ToastHelper.showToast(this, "自定义Toast", 5000L)
  }

  fun showCustomView(v: View) {
    val tv = TextView(this)
    tv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
    tv.text = "自定义view的Toast"
    tv.setPadding(20, 20, 20, 20)
    ToastHelper.showToast(tv, 5000L)
  }
}