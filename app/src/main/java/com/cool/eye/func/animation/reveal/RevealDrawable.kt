package com.cool.eye.func.animation.reveal

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.Gravity
import kotlin.math.abs

class RevealDrawable(
    private val selected: Drawable,
    private val unSelected: Drawable
) : Drawable() {

  override fun getIntrinsicHeight(): Int {
    val sHeight = selected.intrinsicHeight
    val uHeight = unSelected.intrinsicHeight
    return if (sHeight > uHeight) sHeight else uHeight
  }

  override fun getIntrinsicWidth(): Int {
    val sWidth = selected.intrinsicWidth
    val uWidth = unSelected.intrinsicWidth
    return if (sWidth > uWidth) sWidth else uWidth
  }

  override fun onBoundsChange(bounds: Rect) {
    selected.bounds = bounds
    unSelected.bounds = bounds
  }

  override fun setAlpha(alpha: Int) {
  }

  override fun setColorFilter(colorFilter: ColorFilter?) {
  }

  override fun getOpacity() = PixelFormat.TRANSLUCENT

  override fun onLevelChange(level: Int): Boolean {
    //当level变更时，触发draw
    invalidateSelf()
    return true
  }

  /**
   * level: 0~10000
   * 全彩色：5000
   * 全灰色: 0或者10000
   * 渐变：0~5000~10000
   */
  override fun draw(canvas: Canvas) {
    val level = level
    if (level == 0 || level == 10000) {
      //全灰
      unSelected.draw(canvas)
    } else if (level == 5000) {
      //全彩色
      selected.draw(canvas)
    } else {
      draw1(canvas)
      draw2(canvas)
    }
  }

  //fixme 这里剪切区域有问题
  private fun getCutRect(oldRect: Rect, level: Int, isLeft: Boolean): Rect {
    val outRect = Rect()
    val ratio = oldRect.width() / 5000
    if (isLeft) {
      outRect.left = oldRect.left
      outRect.right = oldRect.left + level * ratio
    } else {
      outRect.right = oldRect.right
      outRect.left = oldRect.left + level * ratio
    }

    outRect.top = oldRect.top
    outRect.bottom = oldRect.bottom
    return outRect
  }

  private fun draw1(canvas: Canvas) {
    //渐变：部分灰色+部分彩色
    //获取drawable边界
    val bounds = bounds
    val outRect = getCutRect(bounds, level, true)
    //1、从灰色的图片抠出左边的部分矩形
    //level: 0~5000~10000
    val ratio = level / 5000f - 1f
    val w = (bounds.width() * abs(ratio)).toInt()
    val h = bounds.height()
    val gravity = if (ratio < 0) Gravity.LEFT else Gravity.RIGHT
    Gravity.apply(
        gravity,    //从左边开始切还是从右边开始切
        w,      //目标矩形的宽
        h,      //目标矩形的高
        bounds, //被抠出的原rect矩形
        outRect //目标矩形，最终画布里面需要的rect
    )

    //保存当前画布
    canvas.save()
    //剪切部分画布
    canvas.clipRect(outRect)
    unSelected.draw(canvas)
    //恢复画布
    canvas.restore()
  }

  private fun draw2(canvas: Canvas) {
    //渐变：部分灰色+部分彩色
    //获取drawable边界
    val bounds = bounds
    val outRect = getCutRect(bounds, level, false)
    //1、从灰色的图片抠出左边的部分矩形
    //level: 0~5000~10000
    val ratio = level / 5000f - 1f
    val w = (bounds.width() * abs(ratio)).toInt()
    val h = bounds.height()
    val gravity = if (ratio < 0) Gravity.RIGHT else Gravity.LEFT
    Gravity.apply(
        gravity,    //从左边开始切还是从右边开始切
        w,      //目标矩形的宽
        h,      //目标矩形的高
        bounds, //被抠出的原rect矩形
        outRect //目标矩形，最终画布里面需要的rect
    )

    //保存当前画布
    canvas.save()
    //剪切部分画布
    canvas.clipRect(outRect)
    selected.draw(canvas)
    //恢复画布
    canvas.restore()
  }
}