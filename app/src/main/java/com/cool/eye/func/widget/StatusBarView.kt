package com.cool.eye.func.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.cool.eye.func.util.StatusBarUtil

/**
 *Created by ycb on 2019/12/5 0005
 */
class StatusBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

    val height = if (Build.VERSION.SDK_INT >= 21) StatusBarUtil.getStatusBarHeight(context) else 0

    setMeasuredDimension(context.resources.displayMetrics.widthPixels, height)
  }
}