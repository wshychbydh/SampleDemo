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
    val builder = Params.Builder()
    //自定义下载通知样式 (可选)
//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//      builder.notifyParams(
//          NotifyParams.Builder()
//              .channelId()
//              .notification()
//              .notificationChannel()
//              .notifyId()
//              .build()
//      )
//    }
    builder
        .fileParams(  //下载文件信息（可选）
            FileParams
                .Builder()
                //将要下载的apk版本。如果该的版本与下面设置的版本一致，将不会重复下载
                .version(3, "1.0.2")
                .isApk(true) //默认为apk，会自动引导安装
                //.length()  //同设置的长度一致的文件，将不会重复下载
                //.md5()    //同设置的md5一致的文件将不会重复下载
                .build()
        )
        .downloadParams( //下载文件地址及方式设置
            DownloadParams.Builder()
                .downloadUrl(url)   //下载的链接 （必填）
                .repeatDownload(false) //是否重复下载，默认不重复
                .forceUpdate(true) //是否强制下载，默认不强制
                // .downloadPath() //自定义下载文件路径，可能需自行授权相应权限
                // .request() //自定义DownloadManager.Request
                // .useDownloadManager() //是否使用DownloadManager
                .build()
        )
        .promptParams(  //升级提示框 (可选)
            PromptParams.Builder()

                //.prompt()  //自定义升级提示框

                //以下是默认的提示框
                //.title()
                .content("测试升级")
                //.cancelAble()
                //.cancelOnTouchOutside()
                //.coordinate()
                //.gravity()
                .windowAnim(R.style.DialogAnim)
                .dimAmount(.8f)
                .size((resources.displayMetrics.widthPixels * 4f / 5f).toInt(), -2)
                .build()
        )
        .enableLog(BuildConfig.DEBUG) //是否打印日志
        .progressParams( //升级进度显示框
            ProgressParams.Builder()
                .cancelAble(true)
                .windowAnim(R.style.DialogAnim)
                .dimAmount(0.6f)
                .gravity(Gravity.CENTER)
                .size((resources.displayMetrics.widthPixels * 3f / 5f).toInt(), 0)
                .setCoordinate(80, 200)
                .build())

    DownloadHelper(this, builder.build()).start()
  }

  fun installOffline(view: View) {
    val url = urlEt.text.toString()
    if (!URLUtil.isValidUrl(url)) {
      Toast.makeText(this, "下载地址不正确", Toast.LENGTH_SHORT).show()
      return
    }
    DownloadHelper(this, Params.Builder()
        .downloadParams(
            DownloadParams.Builder()
                .downloadUrl(url)
                .build()
        )
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
    DownloadHelper(this, Params.Builder()
        .downloadParams(
            DownloadParams.Builder()
                .repeatDownload(false)
                .forceUpdate(true)
                .useDownloadManager(true)
                .downloadUrl(url)
                .build()
        )
        .progressParams(
            ProgressParams.Builder()
                .cancelAble(true)
                .build()
        )
        .enableLog(true)
        .build()
    ).start()
  }
}