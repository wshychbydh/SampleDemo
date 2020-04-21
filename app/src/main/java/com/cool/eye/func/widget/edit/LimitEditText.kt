package com.cool.eye.func.widget.edit

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.cool.eye.demo.R

/**
 * Created by cool on 2018/5/10
 */
class LimitEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

  private var count: Int = 0
  private var maxValue: Double = 100000.00

  private var listener: ((String) -> Unit)? = null

  private var doubleType = true

  init {
    val typedArray = resources.obtainAttributes(attrs, R.styleable.LimitEditText)
    count = typedArray.getInt(R.styleable.LimitEditText_decimalNum, 4)
    maxValue = typedArray.getInt(R.styleable.LimitEditText_maxValue, Int.MAX_VALUE).toDouble()
    typedArray.recycle()
    addTextChangedListener(object : TextWatcherAdapter() {
      override fun onTextChanged(text: String) {
        if (text.startsWith(".")) {
          setText("0.")
          setSelection(length())
          return
        }
        if (text.count { c -> c == '.' } > 1) {
          setText(text.substring(0, length() - 1))
          setSelection(length())
          return
        }
        if (text.contains(".")) {
          val strs = text.split(".")
          if (strs[0].length > 9) {
            var max = strs[1].length
            max = if (max > 4) 4 else max
            setText("${strs[0].substring(0, 9)}.${strs[1].substring(0, max)}")
            setSelection(length())
            return
          }
        } else if (text.length > 9) {
          setText(text.substring(0, 9))
          setSelection(9)
          return
        }
        val update = updateValue(text)
        if (!update) {
          listener?.invoke(text)
        }
      }
    })
  }

  private fun getShowText(value: Double): String {
    return if (doubleType) {
      value.toString()
    } else {
      value.toInt().toString()
    }
  }

  fun setDoubleType(doubleType: Boolean) {
    this.doubleType = doubleType
  }

  fun onTextChanged(listener: (String) -> Unit) {
    this.listener = listener
  }

  fun setMaxValue(value: Double) {
    this.maxValue = value
  }

  fun setMaxValue(value: Int) {
    this.maxValue = value.toDouble()
  }

  @SuppressLint("SetTextI18n")
  private fun updateValue(str: String): Boolean {
    if (str.isEmpty()) return false
    if (str.toDouble() > maxValue) {
      setText(getShowText(maxValue))
      setSelection(length())
      return true
    }
    if (str.contains(".")) {
      val array = str.split(".")
      val decimal = array[1]
      if (decimal.length > count) {
        val spit = decimal.substring(0, count)
        setText("${array[0]}.$spit")
        setSelection(length())
        return true
      }
    }
    return false
  }
}
