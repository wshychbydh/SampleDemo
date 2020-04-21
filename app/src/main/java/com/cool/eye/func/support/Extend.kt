package com.cool.eye.func.support

import android.text.Editable
import com.cool.eye.func.util.PhoneUtil

fun Double.formatMoney() = String.format("%.2f", this)

fun Long.formatMoney() = String.format("%.2f", if (this == 0L) 0.00 else this / 100.00)

/*

中国大陆：开头1 3-9号段，后边跟9位数字

台湾：09开头后面跟8位数字

香港：9或6开头后面跟7位数字

澳门：6开头后面跟7位数字
 */
fun String.isMobileNumber(): Boolean {
  return PhoneUtil.isPhone(this)
}

/*

中国大陆：开头1 3-9号段，后边跟9位数字

台湾：09开头后面跟8位数字

香港：9或6开头后面跟7位数字

澳门：6开头后面跟7位数字
 */
fun Editable.isMobileNumber(): Boolean {
  return PhoneUtil.isPhone(toString())
}

fun androidx.recyclerview.widget.RecyclerView.clearItemDecoration() {
  if (this.adapter?.itemCount ?: 0 >= 1 && this.itemDecorationCount >= 1) {
    this.removeItemDecoration(this.getItemDecorationAt(0))
  }
}