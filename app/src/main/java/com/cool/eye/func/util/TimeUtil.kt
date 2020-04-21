package com.cool.eye.func.util

import java.text.SimpleDateFormat
import java.util.*

/**
 *Created by cool on 2018/6/4
 */
object TimeUtil {

  private val date = Date()
  private val YM = SimpleDateFormat("yyyy年MM月", Locale.CHINESE)
  private val YMDHm = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINESE)
  private val YMDHms = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE)
  private val YMD = SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE)

  fun formatYMDHms(time: Long): String {
    if (time <= 0) return ""
    date.time = time
    return YMDHms.format(date)
  }

  fun formatYMDHm(time: Long): String {
    if (time <= 0) return ""
    date.time = time
    return YMDHm.format(date)
  }

  fun formatYMDHm(date: Date): String {
    return YMDHm.format(date)
  }

  fun parseYMDHm(dateStr: String?): Date {
    if (dateStr.isNullOrEmpty()) return Date()
    return YMDHm.parse(dateStr)
  }

  fun formatYMDHm(time: String?): String {
    if (time.isNullOrEmpty()) return ""
    return formatYMDHm(time!!.toLong())
  }

  fun formatYM(time: String?): String {
    if (time.isNullOrEmpty()) return ""
    date.time = time!!.toLong()
    return YM.format(date)
  }

  fun formatYMD(time: String?): String {
    if (time.isNullOrEmpty()) return ""
    date.time = time!!.toLong()
    return YMD.format(date)
  }

  fun formatYMD(time: Long): String {
    date.time = time
    return YMD.format(date)
  }

  fun getTodayOfYMD(): String {
    date.time = System.currentTimeMillis()
    return YMD.format(date)
  }

  fun getWeekAgoTime(): Long {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.WEEK_OF_YEAR, -1)
    return calendar.timeInMillis
  }

  fun getWeekAgoTimeYMDHm(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.WEEK_OF_YEAR, -1)
    return formatYMDHm(calendar.time)
  }

  fun getCurrentTimeYmdHm(): String {
    date.time = System.currentTimeMillis()
    return formatYMDHm(date)
  }
}