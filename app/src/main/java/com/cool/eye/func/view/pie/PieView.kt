package com.cool.eye.func.view.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin


/**
 *Created by ycb on 2019/10/25 0025
 */
class PieView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private val colors = intArrayOf(Color.BLUE, Color.DKGRAY, Color.CYAN, Color.RED, Color.GREEN)
  private val textColor = Color.RED
  private var pieWidth: Int = 0
  private var pieHeight: Int = 0
  private var paint = Paint()
  private var rectF = RectF()
  private var outline: Float = 1f

  private val pies = arrayListOf<Pie>()

  init {
    paint.color = Color.WHITE
    paint.style = Paint.Style.FILL
    paint.textSize = 30f
    paint.isAntiAlias = true
  }

  fun setData(pies: List<Pie>) {
    if (pies.isNullOrEmpty()) return
    val sum = pies.sumByDouble { it.value }
    pies.forEachIndexed { index, pie ->
      pie.percent = pie.value / sum
      pie.angle = pie.percent * 360.0
      pie.color = colors[index % colors.size]
    }
    this.pies.clear()
    this.pies.addAll(pies)
    invalidate()
  }

  //确定View大小
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    this.pieWidth = w     //获取宽高
    this.pieHeight = h
    outline = pieWidth.coerceAtMost(pieHeight) / 2f     //饼状图半径(取宽高里最小的值)
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    if (canvas == null || pies.isNullOrEmpty()) return
    canvas.translate(pieWidth / 2f, pieHeight / 2f)             //将画布坐标原点移到中心位置
    var currentStartAngle = 0.0                //起始角度
    rectF!!.set(-outline, -outline, outline, outline)                    //设置将要用来画扇形的矩形的轮廓
    for (pie in pies) {
      paint.color = pie.color
      //绘制扇形(通过绘制圆弧)
      canvas.drawArc(rectF, currentStartAngle.toFloat(), pie.angle.toFloat(), true, paint)
      //绘制扇形上文字
      val textAngle = currentStartAngle + pie.angle / 2    //计算文字位置角度
      val x = (outline / 2f * cos(textAngle * Math.PI / 180.0)).toFloat()    //计算文字位置坐标
      val y = (outline / 2f * sin(textAngle * Math.PI / 180.0)).toFloat()
      paint.color = textColor      //文字颜色
      canvas.drawText(pie.name ?: "", x, y, paint)    //绘制文字

      currentStartAngle += pie.angle     //改变起始角度
    }
  }
}