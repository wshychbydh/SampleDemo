package com.cool.eye.func.widget.edit

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import java.util.regex.Pattern

/**
 * Created by cool on 2018/5/10
 */
class FilterEmojiEditText : AppCompatEditText {
  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  init {
    filters = arrayOf<InputFilter>(emojiFilter)
  }

  companion object {

    private val emojiFilter = object : InputFilter {

      val emoji = Pattern.compile(
          "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
          Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)

      override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val emojiMatcher = emoji.matcher(source)
        return if (emojiMatcher.find()) "" else null
      }
    }
  }
}
