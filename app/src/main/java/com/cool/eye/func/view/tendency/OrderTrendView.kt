package com.cool.eye.func.view.tendency

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller

/**
 * Created by cool on 2019/4/17.
 */
class OrderTrendView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  private var overScroller: OverScroller = OverScroller(context)

  //速度获取
  private var velocityTracker: VelocityTracker? = null
  //惯性最大最小速度
  private var maximumVelocity: Int = 0
  private var minimumVelocity: Int = 0

  private var calculator: TrendCalculator? = null
    set(value) {
      field = value
      if (value != null) {
        val halfWidth = context.resources.displayMetrics.widthPixels / 2f
        startX = value.getStartX() - halfWidth
        endX = value.getEndX() - halfWidth
        selectedNode = getNearbyNode(endX)
      }
      requestLayout()
    }

  private val imaginaryVPath = Path()
  private val chartPaint = Paint()
  private val linePaint = Paint()
  // private val imaginaryHPaint = Paint()
  private val imaginaryVPaint = Paint()
  private val indicatorPaint = Paint()
  private val circlePaint = Paint()

  //  private var imaginaryHColor: Int = Color.GRAY
  private var textPaintColor = TEXT_COLOR

  private var startX: Float = 0f
  private var endX: Float = 0f

  private var selectedNode: OrderNode? = null
    set(value) {
      field = value
      if (value != null) {
        onCheckedChangedListener?.invoke(value)
      }
    }

  private var onCheckedChangedListener: ((OrderNode) -> Unit)? = null

  init {
    maximumVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    minimumVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    linePaint.strokeWidth = 12f
    linePaint.isDither = true
    linePaint.isAntiAlias = true
    linePaint.style = Paint.Style.STROKE

//    imaginaryHPaint.strokeWidth = .3f
//    imaginaryHPaint.isDither = true
//    imaginaryHPaint.isAntiAlias = true
//    imaginaryHPaint.style = Paint.Style.STROKE
//    imaginaryHPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 8f, 15f, 8f), 0f)

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

    circlePaint.isDither = true
    circlePaint.isAntiAlias = true
    circlePaint.style = Paint.Style.FILL
    circlePaint.strokeWidth = .3f
  }

  fun setData(nodes: List<OrderNode>, onCheckedChangedListener: ((OrderNode) -> Unit)? = null) {
    TrendCalculator.calculatorByData(nodes) {
      this.onCheckedChangedListener = onCheckedChangedListener
      this.calculator = it
    }
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    scrollTo(endX.toInt(), 0)
    val halfX = context.resources.displayMetrics.widthPixels / 2f + scrollX
    val node = getNearbyNode(halfX) ?: return
    selectedNode = node
    invalidate()
  }

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
    drawTrendView(canvas)
    drawIndicator(canvas)
//    drawHImaginary(canvas)
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
      val maxY = Math.min(node.increaseY, node.dealY)
      canvas.drawLine(node.x, maxY, node.x, startY, indicatorPaint)
      circlePaint.color = INCREASE_CIRCLE_COLOR
      canvas.drawCircle(node.x, node.increaseY, cal.circleRadius, circlePaint)
      circlePaint.color = DEAL_CIRCLE_COLOR
      canvas.drawCircle(node.x, node.dealY, cal.circleRadius, circlePaint)
    }
  }

//  private fun drawHImaginary(canvas: Canvas) {
//    imaginaryHPaint.color = imaginaryHColor
//    imaginaryHPaint.strokeWidth = .3f
//    canvas.drawPath(calculator!!.imaginaryHPath, imaginaryHPaint)
//  }

  private fun drawCoordX(canvas: Canvas) {
    val cal = calculator ?: return
    cal.textPaint.color = textPaintColor
    cal.coordX.forEach { coord ->
      canvas.drawText(coord.value, coord.x, coord.y, cal.textPaint)
    }
  }

  private fun drawTrendView(canvas: Canvas) {
    val cal = calculator ?: return
    chartPaint.style = Paint.Style.FILL
    chartPaint.color = INCREASE_COLOR
    canvas.drawPath(cal.increasePath, chartPaint)
    //   drawIncreasedNodes(canvas)
    chartPaint.color = DEAL_COLOR
    canvas.drawPath(cal.dealPath, chartPaint)
    chartPaint.style = Paint.Style.STROKE
    chartPaint.color = INCREASE_LINE_COLOR
    canvas.drawPath(cal.increaseLinePath, chartPaint)
    chartPaint.color = DEAL_LINE_COLOR
    canvas.drawPath(cal.dealLinePath, chartPaint)
//    drawDealedNodes(canvas)
  }

//  private fun drawIncreasedNodes(canvas: Canvas) {
//    calculator!!.nodes.forEachIndexed { index, node ->
//      if (index != 0 && index != calculator!!.nodes.size - 1) {
//        canvas.drawCircle(node.x, node.increaseY + CIRCLE_DELAY, CIRCLE_SIZE, circlePaint)
//      }
//    }
//  }
//
//  private fun drawDealedNodes(canvas: Canvas) {
//    calculator!!.nodes.forEachIndexed { index, node ->
//      if (index != 0 && index != calculator!!.nodes.size - 1) {
//        canvas.drawCircle(node.x, node.dealY + CIRCLE_DELAY, CIRCLE_SIZE, circlePaint)
//      }
//    }
//  }

  private fun getNearbyNode(x: Float): OrderNode? {
    var preOne: OrderNode? = null
    calculator?.nodes?.forEach {

      if (Math.abs(x - it.x) < calculator!!.errorRange) return it

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
        velocityTracker?.computeCurrentVelocity(1000, maximumVelocity.toFloat())
        val velocityX = velocityTracker?.xVelocity?.toInt() ?: 0
        if (Math.abs(velocityX) > minimumVelocity) {
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
    private const val ROLL_BACK_STEP = 2f
    val INCREASE_COLOR = Color.parseColor("#448BF0C6")
    val DEAL_COLOR = Color.parseColor("#44FFDEBB")
    val INCREASE_LINE_COLOR = Color.parseColor("#8BF0C6")
    val DEAL_LINE_COLOR = Color.parseColor("#FFDEBB")
    val INCREASE_CIRCLE_COLOR = Color.parseColor("#00B66B")
    val DEAL_CIRCLE_COLOR = Color.parseColor("#FF8400")
    val INDICATOR_COLOR = Color.parseColor("#333333")
    val TEXT_COLOR = Color.parseColor("#727272")
    val V_IMAGINARY_COLOR = Color.parseColor("#7aaa0000")
  }
}