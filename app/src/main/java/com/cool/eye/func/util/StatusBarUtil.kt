package com.cool.eye.func.util

import android.content.Context

/**
 *Created by ycb on 2020/3/25 0025
 */
object StatusBarUtil {

  /**
   * 获取状态栏高度
   * @param context
   * @return
   */
  @JvmStatic
  fun getStatusBarHeight(context: Context): Int {
    val resources = context.resources
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
  }
}