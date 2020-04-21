package com.cool.eye.func.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.*

/**
 * Created by cool on 2018/3/9.
 */
object FontUtils {

  @JvmStatic
  fun formatSizeOfStr(cs: CharSequence, size: Int, from: Int, to: Int): SpannableString {
    val span = SpannableString(cs)
    span.setSpan(AbsoluteSizeSpan(size, true), from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatPartSize(size: Int, all: CharSequence, part: String): SpannableString {
    val span = SpannableString(all)
    val start = all.indexOf(part)
    val len = part.length
    span.setSpan(AbsoluteSizeSpan(size, true), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatColorOfStr(cs: CharSequence, color: Int, from: Int, to: Int): SpannableString {
    val span = SpannableString(cs)
    span.setSpan(ForegroundColorSpan(color), from, to, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatPartColorOfStr(color: Int, all: CharSequence, part: String): SpannableString {
    val span = SpannableString(all)
    val start = all.indexOf(part)
    val len = part.length
    span.setSpan(ForegroundColorSpan(color), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatPartColorAndUnderLineOfStr(color: Int, all: CharSequence, part: String): SpannableString {
    val span = SpannableString(all)
    val start = all.indexOf(part)
    val len = part.length
    span.setSpan(UnderlineSpan(), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    span.setSpan(ForegroundColorSpan(color), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatUnderLineOfStr(str: CharSequence): SpannableString {
    val span = SpannableString(str)
    span.setSpan(UnderlineSpan(), 0, str.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatStrikethroughOfStr(str: CharSequence): SpannableString {
    val span = SpannableString(str)
    span.setSpan(StrikethroughSpan(), 0, str.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatPartColorAndBoldOfStr(color: Int, all: CharSequence, part: String): SpannableString {
    val span = SpannableString(all)
    val start = all.indexOf(part)
    val len = part.length
    span.setSpan(StyleSpan(Typeface.BOLD), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    span.setSpan(ForegroundColorSpan(color), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return span
  }

  @JvmStatic
  fun formatPartSize(size: Int, all: CharSequence, vararg part: String): SpannableString {
    val span = SpannableString(all)
    part.forEach {
      val start = all.lastIndexOf(it)
      val len = it.length
      span.setSpan(AbsoluteSizeSpan(size, true), start, start + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return span
  }
}