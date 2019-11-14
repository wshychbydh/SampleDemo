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
internal class TendencyCalculator private constructor() {

  val curvePath = Path()

  val linePath = Path()
  lateinit var indicator: FloatArray
  val imaginaryHPath = Path()

  val nodes: ArrayList<Node> = arrayListOf()
  val coordY: ArrayList<String> = arrayListOf()
  val coordX: ArrayList<Coord> = arrayListOf()

  var viewWidth: Int = 0

  val viewHeight: Int

  private var cellMinWidth: Int = 0

  private var coordSpaceX = 0f //坐标间隔

  private val offsetX: Float
  private var offsetY: Float
  private var indicatorHeight: Float

  private val coordHeight: Float

  val textPaint = Paint()
  val errorRange: Float

  init {
    val dm = Resources.getSystem().displayMetrics
    offsetX = dm.density * X_OFFSET
    offsetY = dm.density * Y_OFFSET
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

  private fun calculate(nodes: List<Node>) {
    this.nodes.clear()
    this.nodes.addAll(nodes)
    calculateCellHeight()
    calculateTrendViewPath()
    calculateImaginaryHPath()
  }

  /**
   * 计算每个数值对应的单位高度
   */
  private fun calculateCellHeight() {
    var maxValue = 0f
    var minValue = Float.MAX_VALUE
    nodes.forEach {
      maxValue = it.value.coerceAtLeast(maxValue)
      minValue = minValue.coerceAtMost(it.value)
    }

    //纵坐标最大值
    val expendValue = maxValue * (1f + MAX_EXPEND)

    //计算每一行的高度值
    val unitHeightValue = getIndicatorStartY() / expendValue

    //纵坐标每一行对应的值
    val unitValue = expendValue / MAX_SCALE
    coordY.clear()
    (0 until MAX_SCALE).forEach {
      coordY.add((it * unitValue).toInt().toString())
    }

    calculateCoord(unitHeightValue)
  }


  private fun calculateImaginaryHPath() {
    val startX = getStartX()
    val endX = getEndX()
    val startY = getIndicatorStartY()
    imaginaryHPath.reset()
    (0 until MAX_SCALE).forEach {
      val y = startY - it * itemHeight
      imaginaryHPath.moveTo(startX, y)
      imaginaryHPath.lineTo(endX, y)
    }
  }

  private fun calculateCoord(unitHeightValue: Float) {
    coordX.clear()
    val size = nodes.size
    var countCoordWidth = 0f
    val startY = getIndicatorStartY()
    nodes.forEach { node ->
      val width = textPaint.measureText(node.date)
      val coord = Coord()
      coord.value = node.date
      coord.width = width
      coordX.add(coord)
      countCoordWidth += width
      node.y = startY - node.value * unitHeightValue
    }
    viewWidth = viewWidth.coerceAtLeast((coordX.size + 1) * cellMinWidth)
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
    curvePath.reset()
    linePath.reset()
    val startY = getIndicatorStartY()
    val endY = startY + indicatorHeight
    indicator = FloatArray(nodes.size * 4)
    nodes.forEachIndexed { index, it ->
      if (index == 0) {
        curvePath.moveTo(it.x, startY)
        linePath.moveTo(it.x, it.y)
      } else {
        linePath.lineTo(it.x, it.y)
      }
      curvePath.lineTo(it.x, it.y)
      indicator[index * 4 + 0] = it.x
      indicator[index * 4 + 1] = startY
      indicator[index * 4 + 2] = it.x
      indicator[index * 4 + 3] = endY
      if (index == nodes.size - 1) {
        curvePath.lineTo(it.x, startY)
      }
    }
    curvePath.close()
  }

  val itemHeight = (viewHeight - offsetY) / MAX_SCALE

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
    private val date = Date()
    private val format = SimpleDateFormat("MM-dd", Locale.CHINA)

    private fun format(ms: Long): String {
      date.time = ms
      return format.format(date)
    }

    private const val MAX_SCALE = 10

    private const val MIN_HEIGHT = 200f
    private const val CELL_MIN_WIDTH = 40f

    private const val MAX_EXPEND = .5f //刻度范围扩增倍数

    private const val TEXT_SIZE = 29f
    private const val X_OFFSET = 0f
    private const val Y_OFFSET = 20f  // 横坐标日期值的高度
    private const val INDICATOR_HEIGHT = 6f

    private const val ERROR_RANGE = 10f  //选择时的误差范围

    private val executors = Executors.newSingleThreadExecutor()

    fun calculatorByData(data: List<Node>, callback: (TendencyCalculator) -> Unit) {
      executors.execute {
        val calculator = TendencyCalculator()
        calculator.calculate(data)
        Handler(Looper.getMainLooper()).post {
          callback.invoke(calculator)
        }
      }
    }
  }
}