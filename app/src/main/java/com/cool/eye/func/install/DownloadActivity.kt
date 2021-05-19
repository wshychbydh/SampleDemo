package com.cool.eye.func.install

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.BuildConfig
import com.cool.eye.demo.R
import com.eye.cool.install.DownloadHelper
import com.eye.cool.install.params.*
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

    val downloadParams = DownloadParams.build(downloadUrl = url) {
      repeatDownload = false        //是否重复下载，默认不重复
      forceDownload = true          //是否强制下载，默认不强制
      // downloadPath = null        //自定义下载文件路径，可能需自行授权相应权限
      // request = null             //自定义DownloadManager.Request
      // useDownloadManager = false //是否使用DownloadManager
    }

    val params = Params.build(downloadParams = downloadParams) {

      //可选
      enableLog = BuildConfig.DEBUG  //是否打印日志

      //可选
      fileParams = FileParams.build {
        isApk = true
        //version() //将要下载的apk版本。如果该的版本与下面设置的版本一致，将不会重复下载
        //length()  //同设置的长度一致的文件，将不会重复下载
        //md5()     //同设置的md5一致的文件将不会重复下载
      }

      //可选
      promptParams = PromptParams.build {
        //prompt()  //自定义升级提示框
        //windowParams() //自定义升级提示框窗体设置

        //默认的提示框
        title = "版本升级"
        content = "发现新的升级包"
      }

      //可选
      progressParams = ProgressParams.build {
        // progressView
        // progressListener
        // progressTimeout
        window = WindowParams.build {
          cancelable = true
          windowAnimations = R.style.DialogAnim
          dimAmount = 0.6f
          gravity = Gravity.CENTER
          size((resources.displayMetrics.widthPixels * 4f / 5f).toInt(), -2)
          position(80, 200)
        }
      }

      // 自定义下载通知样式 (可选)
      // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      //   notifyParams = NotifyParams.build {
      //     notifyId
      //     channelId
      //     notification
      //     notifyChannel
      //  }
    }

    DownloadHelper(this, params).start()
  }

  fun installOffline(view: View) {
    val url = urlEt.text.toString()
    if (!URLUtil.isValidUrl(url)) {
      Toast.makeText(this, "下载地址不正确", Toast.LENGTH_SHORT).show()
      return
    }
    DownloadHelper(
        this,
        Params.Builder(DownloadParams.Builder(url).build())
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
    DownloadHelper(this,
        Params.build(
            DownloadParams.build(url) {
              repeatDownload = false
              forceDownload = true
              useDownloadManager = true
            }
        ) {
          progressParams = ProgressParams.build {
            window = WindowParams.build { cancelable = true }
          }
          enableLog = true
        }
    ).start()
  }
}