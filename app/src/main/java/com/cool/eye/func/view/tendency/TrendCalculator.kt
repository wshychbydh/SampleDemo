package com.cool.eye.func.view.tendency

import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 *Created by ycb on 2019/4/17 0017
 */
internal class TrendCalculator private constructor() {

  val increasePath = Path()
  val dealPath = Path()

  val increaseLinePath = Path()
  val dealLinePath = Path()
  lateinit var indicator: FloatArray
  //  val imaginaryHPath = Path()

  val nodes: ArrayList<OrderNode> = arrayListOf()
  //  val coordY: ArrayList<String> = arrayListOf()
  val coordX: ArrayList<Coord> = arrayListOf()

  var viewWidth: Int = 0
  val viewHeight: Int

  private var cellMinWidth: Int = 0

  private var coordSpaceX = 0f //坐标间隔

  private val offsetX: Float
  private var offsetY: Float
  private var indicatorHeight: Float

  private var unitHeight: Float = 0f
  private val coordHeight: Float

  val textPaint = Paint()
  val circleRadius: Float
  val errorRange: Float

  init {
    val dm = Resources.getSystem().displayMetrics
    offsetX = dm.density * X_OFFSET
    offsetY = dm.density * Y_OFFSET
    circleRadius = dm.density * CIRCLE_RADIUS
    errorRange = dm.density * ERROR_RANGE
    indicatorHeight = dm.density * INDICATOR_HEIGHT
    viewHeight = (dm.density * MIN_HEIGHT).toInt()
    viewWidth = dm.widthPixels
    cellMinWidth = (CELL_MIN_WIDTH * dm.density).toInt()
    val textSize = TEXT_SIZE
    textPaint.textSize = textSize
    textPaint.isAntiAlias = true
    textPaint.isDither = true
    textPaint.style = Paint.Style.STROKE

    coordHeight = textPaint.descent() - textPaint.ascent()
  }

  private fun calculate(nodes: List<OrderNode>) {
    this.nodes.clear()
    this.nodes.addAll(nodes)
    calculateCellHeight()
    calculateCoord()
    calculateTrendViewPath()
    //  calculateImaginaryHPath()
  }

  /**
   * 计算每个数值对应的单位高度
   */
  private fun calculateCellHeight() {
    var maxValue = 0
    nodes.forEach {
      maxValue = Math.max(it.increase, maxValue)
      maxValue = Math.max(it.deal, maxValue)
    }
    val expendValue = maxValue.toFloat() * (1 + MAX_EXPEND)
    unitHeight = (viewHeight - offsetY - circleRadius) / expendValue
  }


//  private fun calculateImaginaryHPath() {
//    val startX = getStartX()
//    val endX = getEndX()
//    val startY = viewHeight - offsetY
//    val itemHeight = (viewHeight - offsetY) / MAX_SCALE
//    imaginaryHPath.reset()
//    (0..MAX_SCALE).forEach {
//      val y = startY - it * itemHeight
//      imaginaryHPath.moveTo(startX, y)
//      imaginaryHPath.lineTo(endX, y)
//    }
//  }

  private fun calculateCoord() {
    coordX.clear()
    val size = nodes.size
    var countCoordWidth = 0f
    val avgY = unitHeight
    val startY = viewHeight - offsetY// 坐标原点在左上角（0,0）
    nodes.forEach { node ->
      val width = textPaint.measureText(node.date)
      val coord = Coord()
      coord.value = node.date
      coord.width = width
      coordX.add(coord)
      countCoordWidth += width
      node.dealY = startY - node.deal * avgY
      node.increaseY = startY - node.increase * avgY
    }
    viewWidth = Math.max(viewWidth, (coordX.size + 1) * cellMinWidth)
    val spaceWidth = (viewWidth - countCoordWidth) / size / 2f
    coordSpaceX = spaceWidth
    var leftPadding = offsetX
    val bottomPadding = viewHeight.toFloat() - (offsetY - coordHeight) / 2f
    coordX.forEachIndexed { index, coord ->
      coord.leftSpace = spaceWidth
      coord.rightSpace = spaceWidth
      coord.x = leftPadding + spaceWidth
      coord.y = bottomPadding
      leftPadding += coord.getFullWidth()
      nodes[index].x = coord.getMiddleX()
    }
  }

  private fun calculateTrendViewPath() {
    increasePath.reset()
    dealPath.reset()
    increaseLinePath.reset()
    dealLinePath.reset()
    val startY = getIndicatorStartY()
    val endY = startY + indicatorHeight
    indicator = FloatArray(nodes.size * 4)
    nodes.forEachIndexed { index, it ->
      if (index == 0) {
        increasePath.moveTo(it.x, startY)
        dealPath.moveTo(it.x, startY)
        increaseLinePath.moveTo(it.x, it.increaseY)
        dealLinePath.moveTo(it.x, it.dealY)
      } else {
        increaseLinePath.lineTo(it.x, it.increaseY)
        dealLinePath.lineTo(it.x, it.dealY)
      }
      increasePath.lineTo(it.x, it.increaseY)
      dealPath.lineTo(it.x, it.dealY)
      indicator[index * 4 + 0] = it.x
      indicator[index * 4 + 1] = startY
      indicator[index * 4 + 2] = it.x
      indicator[index * 4 + 3] = endY
      if (index == nodes.size - 1) {
        increasePath.lineTo(it.x, startY)
        dealPath.lineTo(it.x, startY)
      }
    }
    increasePath.close()
    dealPath.close()
  }

  fun getStartX(): Float {
    return coordX[0].getMiddleX()
  }

  fun getEndX(): Float {
    return coordX[coordX.size - 1].getMiddleX()
  }

  fun getIndicatorStartY(): Float {
    return viewHeight - offsetY
  }

  companion object {
    private val date = java.util.Date()
    private val format = SimpleDateFormat("MM-dd", Locale.CHINA)

    private fun format(ms: Long): String {
      date.time = ms
      return format.format(date)
    }

    private const val MIN_HEIGHT = 160f
    private const val CELL_MIN_WIDTH = 40f

    private const val MAX_EXPEND = .0f //刻度范围扩增倍数

    private const val TEXT_SIZE = 29f
    private const val X_OFFSET = 0f
    private const val Y_OFFSET = 20f  // 横坐标日期值的高度
    private const val INDICATOR_HEIGHT = 6f

    private const val CIRCLE_RADIUS = 4f
    private const val ERROR_RANGE = 10f  //选择时的误差范围

    private val executors = Executors.newSingleThreadExecutor()

    fun calculatorByData(data: List<OrderNode>, callback: (TrendCalculator) -> Unit) {
      executors.execute {
        val calculator = TrendCalculator()
        calculator.calculate(data)
        Handler(Looper.getMainLooper()).post {
          callback.invoke(calculator)
        }
      }
    }
  }
}