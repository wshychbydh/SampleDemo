package com.cool.eye.func.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cool.eye.demo.R
import com.cool.eye.func.dialog.loading.BaseDialog
import com.cool.eye.func.dialog.loading.LoadingHelper
import com.cool.eye.func.dialog.loading.Params

/**
 *Created by ycb on 2020/1/22 0022
 */
class DialogActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_progress)
  }

  fun showLoading1(v: View) {
    LoadingHelper.show(this)
  }

  fun showLoading2(v: View) {
    LoadingHelper.show(this, R.string.loading)
  }

  fun showCustomDialog(v: View) {
    val tv = TextView(this)
    tv.text = "自定义对话框"
    tv.gravity = Gravity.CENTER
    tv.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
    tv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    val params = Params.Builder()
        .setCancelAble(true)
        .setContentView(tv)
        .setSize(300, 300)
        .build()
    BaseDialog(this, params).show()
  }

  fun showFullDialog(v: View) {
    val container = LinearLayout(this)
    container.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
    container.gravity = Gravity.CENTER
    val tv = TextView(this)
    container.addView(tv)
    tv.text = "全屏对话框"
    val params = Params.Builder()
        .setCancelAble(true)
        .setContentView(container)
        .setTimeout(0L)
        .setDialogStyle(R.style.FullDialog)
        .build()
    BaseDialog(this, params).show()
  }
}