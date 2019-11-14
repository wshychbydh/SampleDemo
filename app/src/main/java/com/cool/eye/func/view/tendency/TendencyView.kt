package com.cool.eye.func.view.tendency

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import kotlin.math.abs

/**
 * Created by cool on 2019/4/17.
 */
class TendencyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private var overScroller: OverScroller = OverScroller(context)

  //速度获取
  private var velocityTracker: VelocityTracker? = null
  //惯性最大最小速度
  private var maximumVelocity: Int = 0
  private var minimumVelocity: Int = 0

  private var calculator: TendencyCalculator? = null
    set(value) {
      field = value
      if (value != null) {
        val halfWidth = context.resources.displayMetrics.widthPixels / 2f
        startX = value.getStartX() - halfWidth
        endX = value.getEndX() - halfWidth
     //   selectedNode = getNearbyNode(endX)
      }
      requestLayout()
    }

  private val imaginaryVPath = Path()
  private val chartPaint = Paint()
  private val linePaint = Paint()
  private val imaginaryHPaint = Paint()
  private val imaginaryVPaint = Paint()
  private val indicatorPaint = Paint()
  private val circlePaint = Paint()

  private val textRectF = RectF()

  private var imaginaryHColor: Int = Color.GRAY
  private var textPaintColor = TEXT_COLOR

  private var pointTextWidth: Int = 0
  private var pointTextHeight: Int = 0
  private var pointPadding: Int = 0

  private var startX: Float = 0f
  private var endX: Float = 0f

  private var pointEffect: CornerPathEffect

  private var selectedNode: Node? = null
    set(value) {
      field = value
      if (value != null) {
        onCheckedChangedListener?.invoke(value)
      }
    }

  private var onCheckedChangedListener: ((Node) -> Unit)? = null

  init {
    val density = resources.displayMetrics.density
    pointTextWidth = (POINT_TEXT_WIDTH * density).toInt()
    pointTextHeight = (POINT_TEXT_HEIGHT * density).toInt()
    pointPadding = (POINT_PADDING * density).toInt()

    maximumVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    minimumVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    linePaint.strokeWidth = 12f
    linePaint.isDither = true
    linePaint.isAntiAlias = true
    linePaint.style = Paint.Style.STROKE

    imaginaryHPaint.strokeWidth = .3f
    imaginaryHPaint.isDither = true
    imaginaryHPaint.isAntiAlias = true
    imaginaryHPaint.style = Paint.Style.STROKE
    imaginaryHPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 8f, 15f, 8f), 0f)

    imaginaryVPaint.strokeWidth = .5f
    imaginaryVPaint.isDither = true
    imaginaryVPaint.isAntiAlias = true
    imaginaryVPaint.style = Paint.Style.STROKE
    imaginaryVPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 8f, 15f, 8f), 0f)

    indicatorPaint.strokeWidth = 6f
    indicatorPaint.isDither = true
    indicatorPaint.isAntiAlias = true
    indicatorPaint.color = INDICATOR_COLOR

    chartPaint.strokeWidth = 6f
    chartPaint.isAntiAlias = true
    chartPaint.isDither = true
    chartPaint.style = Paint.Style.FILL

    pointEffect = CornerPathEffect(pointTextHeight / 2f)
    circlePaint.isDither = true
    circlePaint.isAntiAlias = true
    circlePaint.style = Paint.Style.FILL_AND_STROKE
    circlePaint.pathEffect = pointEffect
    circlePaint.strokeWidth = 4f
  }

  fun setData(nodes: List<Node>, onCheckedChangedListener: ((Node) -> Unit)? = null) {
    TendencyCalculator.calculatorByData(nodes) {
      this.onCheckedChangedListener = onCheckedChangedListener
      this.calculator = it
    }
  }

//  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//    scrollTo(endX.toInt(), 0)
//    val halfX = context.resources.displayMetrics.widthPixels / 2f + scrollX
//    val node = getNearbyNode(halfX) ?: return
//    selectedNode = node
//    invalidate()
//  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (calculator == null) {
      setMeasuredDimension(0, 0)
    } else {
      setMeasuredDimension(calculator!!.viewWidth, calculator!!.viewHeight)
    }
  }

  override fun onDraw(canvas: Canvas) {
    if (calculator == null) return
    canvas.drawColor(Color.WHITE)
    drawCoordX(canvas)
    drawCoordY(canvas)
    drawTrendView(canvas)
    drawIndicator(canvas)
    drawHImaginary(canvas)
    drawNodeValue(canvas)
  }

  private fun drawIndicator(canvas: Canvas) {
    val cal = calculator ?: return
    val startX = cal.getStartX()
    val endX = cal.getEndX()
    val startY = cal.getIndicatorStartY()
    val halfX = context.resources.displayMetrics.widthPixels / 2f + scrollX
    canvas.drawLine(startX, startY, endX, startY, indicatorPaint)
    canvas.drawLines(cal.indicator, indicatorPaint)
    if (selectedNode == null || rollback) {
      imaginaryVPath.reset()
      imaginaryVPath.moveTo(halfX, 0f)
      imaginaryVPath.lineTo(halfX, startY)
      imaginaryVPaint.color = V_IMAGINARY_COLOR
      canvas.drawPath(imaginaryVPath, imaginaryVPaint)
    } else {
      val node = selectedNode!!
      canvas.drawLine(node.x, node.y, node.x, startY, indicatorPaint)
      //   circlePaint.color = CIRCLE_COLOR
      // canvas.drawCircle(node.x, node.y, cal.circleRadius, circlePaint)
      circlePaint.color = Color.WHITE
      canvas.drawCircle(node.x, node.y, 5f, circlePaint)
      circlePaint.style = Paint.Style.STROKE
      circlePaint.color = CIRCLE_COLOR
      canvas.drawCircle(node.x, node.y, 7f, circlePaint)
    }
  }

  private fun drawNodeValue(canvas: Canvas) {
    if (rollback) return
    selectedNode?.also {
      val textPaint = calculator?.textPaint ?: return
      textRectF.left = it.x - pointTextWidth / 2
      textRectF.top = it.y - pointTextHeight - pointPadding
      textRectF.right = textRectF.left + pointTextWidth
      textRectF.bottom = textRectF.top + pointTextHeight

      circlePaint.style = Paint.Style.FILL_AND_STROKE
      circlePaint.color = CIRCLE_COLOR

      canvas.drawRect(textRectF, circlePaint)

      //    textPaint.textSize = 35f
      textPaint.color = Color.WHITE
      //    val textHeight = textPaint.descent() - textPaint.ascent()
      val textWidth = textPaint.measureText(it.value.toString())
      canvas.drawText(it.value.toString(), textRectF.left + (textRectF.width() - textWidth) / 2,
          textRectF.top + textRectF.height() / 2 + calculator!!.itemHeight / 3, textPaint)
    }
  }

  private fun drawHImaginary(canvas: Canvas) {
    imaginaryHPaint.color = imaginaryHColor
    imaginaryHPaint.strokeWidth = .3f
    canvas.drawPath(calculator!!.imaginaryHPath, imaginaryHPaint)
  }

  private fun drawCoordX(canvas: Canvas) {
    val cal = calculator ?: return
    cal.textPaint.color = textPaintColor
    cal.coordX.forEach {
      canvas.drawText(it.value, it.x, it.y, cal.textPaint)
    }
  }

  private fun drawCoordY(canvas: Canvas) {
    val cal = calculator ?: return
    cal.textPaint.color = textPaintColor
    val start = cal.coordX[0].leftSpace
    cal.coordY.forEachIndexed { index, it ->
      canvas.drawText(it, start, cal.getIndicatorStartY() - cal.itemHeight * index, cal.textPaint)
    }
  }

  private fun drawTrendView(canvas: Canvas) {
    val cal = calculator ?: return
    chartPaint.style = Paint.Style.FILL
    chartPaint.color = BG_COLOR
    canvas.drawPath(cal.curvePath, chartPaint)
    chartPaint.style = Paint.Style.STROKE
    chartPaint.color = LINE_COLOR
    canvas.drawPath(cal.linePath, chartPaint)
  }

  private fun getNearbyNode(x: Float): Node? {
    var preOne: Node? = null
    calculator?.nodes?.forEach {

      if (abs(x - it.x) < calculator!!.errorRange) return it

      if (preOne != null) {
        if (x > preOne!!.x && x < it.x) {
          return if (x - preOne!!.x > it.x - x) it else preOne
        }
      }
      preOne = it
    }
    return null
  }

  private var mLastX = 0f

  override fun onTouchEvent(event: MotionEvent): Boolean {
    if (calculator == null) return false
    selectedNode = null
    rollback = false
    val currentX = event.x
    //开始速度检测
    if (velocityTracker == null) {
      velocityTracker = VelocityTracker.obtain()
    }
    velocityTracker?.addMovement(event)
    val parent = parent as ViewGroup//为了解决刻度尺在scrollview这种布局里面滑动冲突问题
    when (event.action) {
      MotionEvent.ACTION_DOWN -> {
        if (!overScroller.isFinished) {
          overScroller.abortAnimation()
        }
        mLastX = currentX
        parent.requestDisallowInterceptTouchEvent(true)//按下时开始让父控件不要处理任何touch事件，up或者cancel的时候恢复
      }
      MotionEvent.ACTION_MOVE -> {
        val moveX = mLastX - currentX
        if (moveX + scrollX <= startX) {
          mLastX = startX
          scrollTo(startX.toInt(), 0)
          return true
        }
        if (moveX + scrollX >= endX) {
          mLastX = endX
          scrollTo(endX.toInt(), 0)
          return true
        }
        mLastX = currentX
        scrollBy(moveX.toInt(), 0)
      }
      MotionEvent.ACTION_UP -> {
        //处理松手后的Fling
        velocityTracker?.computeCurrentVelocity(500, maximumVelocity.toFloat())
        val velocityX = velocityTracker?.xVelocity?.toInt() ?: 0
        if (abs(velocityX) > minimumVelocity) {
          fling(-velocityX)
        } else {
          rollBackToScale()
        }
        parent.requestDisallowInterceptTouchEvent(false)
        velocityTracker?.recycle()
        velocityTracker = null
      }
      MotionEvent.ACTION_CANCEL -> {
        if (!overScroller.isFinished) {
          overScroller.abortAnimation()
        }
        //回滚到整点刻度
        parent.requestDisallowInterceptTouchEvent(false)
        rollBackToScale()
        velocityTracker?.recycle()
        velocityTracker = null
      }
    }
    return true
  }

  private fun fling(vX: Int) {
    overScroller.fling(scrollX, 0, vX, 0, startX.toInt(), endX.toInt(), 0, 0)
    invalidate()
  }

  override fun computeScroll() {
    if (rollback && selectedNode != null) {
      val halfX = context.resources.displayMetrics.widthPixels / 2f + scrollX
      val dx = selectedNode!!.x - halfX
      rollback(dx)
    } else {
      if (overScroller.computeScrollOffset()) {
        scrollTo(overScroller.currX, overScroller.currY)
        if (!overScroller.computeScrollOffset()) {
          rollBackToScale()
        } else invalidate()
      }
    }
  }

  private var rollback = false

  private fun rollBackToScale() {
    rollback = true
    val halfX = context.resources.displayMetrics.widthPixels / 2f + scrollX
    val node = getNearbyNode(halfX) ?: return
    selectedNode = node
    val dx = node.x - halfX
    rollback(dx)
  }


  private fun rollback(dx: Float) {
    val value = if (dx > ROLL_BACK_STEP) ROLL_BACK_STEP else if (dx < -ROLL_BACK_STEP) -ROLL_BACK_STEP else dx
    scrollBy(value.toInt(), 0)
    if (value == 0f || value == dx) {
      rollback = false
    }
  }

  companion object {
    private const val POINT_TEXT_WIDTH = 36
    private const val POINT_TEXT_HEIGHT = 20
    private const val POINT_PADDING = 7

    private const val ROLL_BACK_STEP = 5f
    val BG_COLOR = Color.parseColor("#44FFDEBB")
    val LINE_COLOR = Color.parseColor("#FFDEBB")
    val CIRCLE_COLOR = Color.parseColor("#00B66B")
    val INDICATOR_COLOR = Color.parseColor("#333333")
    val TEXT_COLOR = Color.parseColor("#727272")
    val V_IMAGINARY_COLOR = Color.parseColor("#7aaa0000")
  }
}