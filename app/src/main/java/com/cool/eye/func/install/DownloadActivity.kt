package com.cool.eye.func.install

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.install.DialogParams
import com.eye.cool.install.DownloadHelper
import com.eye.cool.install.DownloadParams
import com.eye.cool.install.Params
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
        .forceUpdate(true)
        .enableLog(true)
        .setDialogParams(DialogParams.Builder()
            .cancelAble(true)
            .dimAmount(1.0f)
            .gravity(Gravity.BOTTOM)
            .size(400, 400)
            .setCoordinate(130, 200)
            .windowAnim(R.style.AnimBottom)
            .backgroundDrawable(resources.getDrawable(R.drawable.sidebar_background))
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