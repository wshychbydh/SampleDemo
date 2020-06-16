package com.cool.eye.func.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.cool.eye.demo.R
import java.io.File
import java.util.regex.Pattern


/**
 *Created by ycb on 2019/10/27 0027
 */
object Util {

  fun toDp(context: Context, size: Int): Float {
    return context.resources.displayMetrics.density * size
  }

  fun dpToPx(context: Context, dpSize: Float): Int {
    return (context.resources.displayMetrics.density * dpSize).toInt()
  }

  fun copyText(context: Context, text: CharSequence) {
    //获取剪贴板管理器：
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    // 创建普通字符型ClipData
    val clipData = ClipData.newPlainText("Label", text)
    // 将ClipData内容放到系统剪贴板里。
    cm?.setPrimaryClip(clipData)
  }

  /**
   * 分享功能
   * @param context 上下文
   * @param activityTitle Activity的名字
   * @param msgTitle 消息标题
   * @param msgText 消息内容
   * @param imgPath 图片路径，不分享图片则传null
   */
  fun shareMsg(context: Context, msgText: String, imagePath: String) {
    val intent = Intent(Intent.ACTION_SEND)
    if (imagePath.isNullOrEmpty()) {
      intent.type = "text/plain" // 纯文本
    } else {
      val file = File(imagePath)
      if (file?.exists() && file.isFile) {
        intent.type = "image/jpg"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
      }
    }
    intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.choose_to_share))
    intent.putExtra(Intent.EXTRA_TEXT, msgText)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)))
  }

  @JvmStatic
  fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0) //强制隐藏键盘
  }

  @JvmStatic
  fun showKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
  }

  @JvmStatic
  fun getVersionName(context: Context): String? { //获取包管理器
    val pm = context.packageManager
    //获取包信息
    try {
      val packageInfo = pm.getPackageInfo(context.packageName, 0)
      //返回版本号
      return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
    }
    return null
  }

  @JvmStatic
  fun getVersionCode(context: Context): Int? { //获取包管理器
    val pm = context.packageManager
    //获取包信息
    try {
      val packageInfo = pm.getPackageInfo(context.packageName, 0)
      //返回版本号
      return packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
    }
    return null
  }

  /**
   * 密码长度 6~16

   * @param pwd 密码
   * *
   * @return boolean
   */
  fun checkUserPasswordLength(pwd: String): Boolean {
    return pwd.length in 6..16
  }


  fun checkIp(ip: String?): Boolean {
    var regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$"
    return Pattern.matches(regex, ip)
  }

  fun replaceAllSpecial(str: CharSequence): String {
    // val regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）_——+|{}【】‘；：”“’。，、？\\s+]"
    val reg = "[^\\w]"
    var p = Pattern.compile(reg)
    var m = p.matcher(str)
    return m.replaceAll("").trim()
  }
}