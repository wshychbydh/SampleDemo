package com.cool.eye.func.install

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.install.DownloadHelper
import com.eye.cool.install.params.DownloadParams
import com.eye.cool.install.params.Params
import com.eye.cool.install.params.ProgressParams
import com.eye.cool.install.params.PromptParams
import kotlinx.android.synthetic.main.activity_download.*

/**
 *Created by ycb on 2019/11/29 0029
 */
class DownloadActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_download)
  }

  fun installOnline(view: View) {
    val url = urlEt.text.toString()
    if (!URLUtil.isValidUrl(url)) {
      Toast.makeText(this, "下载地址不正确", Toast.LENGTH_SHORT).show()
      return
    }
    DownloadHelper(this, Params.Builder()
        .setDownloadParams(
            DownloadParams.Builder()
                .setDownloadUrl(url)
                .build()
        )
        .setPromptParams(
            PromptParams.Builder()
                .setContent("测试升级")
                .windowAnim(R.style.DialogAnim)
                .dimAmount(.8f)
                .cancelAble(true)
                .size((resources.displayMetrics.widthPixels * 4f / 5f).toInt(), -2)
                .build()
        )
        .forceUpdate(true)
        .enableLog(true)
        .setProgressParams(ProgressParams.Builder()
            .cancelAble(true)
            .windowAnim(R.style.DialogAnim)
            .dimAmount(0.6f)
            .gravity(Gravity.CENTER)
            .size((resources.displayMetrics.widthPixels * 3f / 5f).toInt(), 0)
            .setCoordinate(130, 200)
            .build())
        .build()
    ).start()
  }

  fun installOutline(view: View) {
    val url = urlEt.text.toString()
    if (!URLUtil.isValidUrl(url)) {
      Toast.makeText(this, "下载地址不正确", Toast.LENGTH_SHORT).show()
      return
    }
    DownloadHelper(this, Params.Builder()
        .setDownloadParams(
            DownloadParams.Builder()
                .setDownloadUrl(url)
                .build()
        )
        .useDownloadManager(false)
        .enableLog(true)
        .build()
    ).start()
  }

  fun installByDM(view: View) {
    val url = urlEt.text.toString()
    if (!URLUtil.isValidUrl(url)) {
      Toast.makeText(this, "下载地址不正确", Toast.LENGTH_SHORT).show()
      return
    }
    DownloadHelper(this, url).start()
  }
}