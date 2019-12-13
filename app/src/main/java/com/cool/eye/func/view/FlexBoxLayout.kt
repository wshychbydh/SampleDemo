package com.cool.eye.func.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class FlexBoxLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {
  private val screenWidth: Int = context.resources.displayMetrics.widthPixels
  private val density = context.resources.displayMetrics.density
  private var horizontalSpace = 0
  private var verticalSpace = 0

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    //确定此容器的宽高
    val widthMode = MeasureSpec.getMode(widthMeasureSpec)
    var widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightMode = MeasureSpec.getMode(heightMeasureSpec)
    var heightSize = MeasureSpec.getSize(heightMeasureSpec)
    //测量子View的宽高
    val childCount = childCount
    var child: View? = null
    //子view摆放的起始位置
    var left = paddingLeft
    //一行view中将最大的高度存于此变量，用于子view进行换行时高度的计算
    var maxHeightInLine = 0
    //存储所有行的高度相加，用于确定此容器的高度
    var allHeight = 0
    for (i in 0 until childCount) {
      child = getChildAt(i)
      //测量子View宽高
      measureChild(child, widthMeasureSpec, heightMeasureSpec)
      //两两对比，取得一行中最大的高度
      if (child.measuredHeight + child.paddingTop + child.paddingBottom
          > maxHeightInLine) {
        maxHeightInLine = child.measuredHeight + child.paddingTop + child.paddingBottom
      }
      left += child.measuredWidth + dip2px(horizontalSpace.toFloat()) + child.paddingLeft + child
          .paddingRight
      if (left >= widthSize - paddingRight - paddingLeft) { //换行
        left = paddingLeft
        //累积行的总高度
        allHeight += maxHeightInLine + dip2px(verticalSpace.toFloat())
        //因为换行了，所以每行的最大高度置0
        maxHeightInLine = 0
      }
    }
    //再加上最后一行的高度,因为之前的高度累积条件是换行
//最后一行没有换行操作，所以高度应该再加上
    allHeight += maxHeightInLine
    if (widthMode != MeasureSpec.EXACTLY) {
      widthSize = screenWidth //如果没有指定宽，则默认为屏幕宽
    }
    if (heightMode != MeasureSpec.EXACTLY) { //如果没有指定高度
      heightSize = allHeight + paddingBottom + paddingTop
    }
    setMeasuredDimension(widthSize, heightSize)
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    if (changed) { //摆放子view
      var child: View? = null
      //初始子view摆放的左上位置
      var left = paddingLeft
      var top = paddingTop
      //一行view中将最大的高度存于此变量，用于子view进行换行时高度的计算
      var maxHeightInLine = 0
      var i = 0
      val len = childCount
      while (i < len) {
        child = getChildAt(i)
        //从第二个子view开始算起
//因为第一个子view默认从头开始摆放
        if (i > 0) { //两两对比，取得一行中最大的高度
          if (getChildAt(i - 1).measuredHeight > maxHeightInLine) {
            maxHeightInLine = getChildAt(i - 1).measuredHeight
          }
          //当前子view的起始left为 上一个子view的宽度+水平间距
          left += getChildAt(i - 1).measuredWidth + dip2px(horizontalSpace.toFloat())
          if (left + child.measuredWidth
              >= width - paddingRight - paddingLeft) { //这一行所有子view相加的宽度大于容器的宽度，需要换行
//换行的首个子view，起始left应该为0+容器的paddingLeft
            left = paddingLeft
            //top的位置为上一行中拥有最大高度的某个View的高度+垂直间距
            top += maxHeightInLine + dip2px(verticalSpace.toFloat())
            //将上一行View的最大高度置0
            maxHeightInLine = 0
          }
        }
        //摆放子view
        child.layout(left, top, left + child.measuredWidth, top + child.measuredHeight)
        i++
      }
    }
  }

  /**
   * dp转为px
   */
  private fun dip2px(dpValue: Float): Int {
    return (dpValue * density + 0.5f).toInt()
  }

  /**
   * 设置子view间的水平间距 单位dp
   */
  fun setHorizontalSpace(horizontalSpace: Int) {
    this.horizontalSpace = horizontalSpace
  }

  /**
   * 设置子view间的垂直间距 单位dp
   */
  fun setVerticalSpace(verticalSpace: Int) {
    this.verticalSpace = verticalSpace
  }
}