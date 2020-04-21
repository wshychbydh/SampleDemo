package com.cool.eye.func.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.util.regex.Pattern

/**
 *Created by ycb on 2020/3/25 0025
 */
object PhoneUtil {

  @JvmStatic
  fun isPhone(phone: String?): Boolean {
    if (phone.isNullOrEmpty()) return false
    if (phone.length != 11) return false
    val regex = "0?(13|14|15|17|18|19)[0-9]{9}"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(phone)
    return matcher.matches()
  }

  /**
   * 拨打电话（跳转到拨号界面，用户手动点击拨打）
   *
   * @param phone 电话号码
   */
  fun callPhone(context: Context, phone: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    val data = Uri.parse("tel:$phone")
    intent.data = data
    context.startActivity(intent)
  }
}