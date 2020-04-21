package com.cool.eye.func.view.percent

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.cool.eye.func.view.percent.Percent

/**
 *Created by ycb on 2019/10/26 0026
 */
class PercentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private val paint = Paint()

  private val size = 480
  private val radius = 200f
  private val width = 30f

  private var percent: Percent? = null

  private val rectF = RectF(36f, 36f, 464f, 464f)

  init {
    paint.isDither = true
    paint.isAntiAlias = true
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 14f * context.resources.displayMetrics.density
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    setMeasuredDimension(size, size)
  }

  fun setData(percent: Percent) {
    this.percent = percent
    invalidate()
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    if (canvas == null || percent == null) return
    paint.style = Paint.Style.FILL
    paint.color = percent!!.innerColor
    paint.strokeWidth = 5f
    paint.style = Paint.Style.FILL
    canvas.drawCircle(250f, 250f, radius, paint)
    paint.color = percent!!.textColor
    canvas.drawText(percent!!.ratio, 250f, 264f, paint)
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = width
    paint.color = percent!!.outColor
    canvas.drawArc(rectF, -90f, percent!!.angle, false, paint)
  }
}