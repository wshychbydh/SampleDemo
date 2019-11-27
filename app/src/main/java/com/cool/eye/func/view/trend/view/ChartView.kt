package com.cool.eye.func.view.trend.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.cool.eye.chart.mode.Coord
import com.cool.eye.demo.R
import com.cool.eye.func.view.trend.mode.Node

/**
 * Created by cool on 2018/3/28.
 */
class ChartView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var nodes: List<Node>? = null
    private var coordY: Array<String?> = getCoordY()
    private var coordXArray: Array<Coord?>

    private var viewWidth = 0
    private var viewHeight = 0
    private var cellHeight = 0f
    private var spaceWidth = 0f
    private var coordWidth: Float = 0f  //竖向坐标的宽度 （0,100,200的宽度）
    private var coordHeight: Float = 0f //横向坐标的高度 ("01:00"的高度)
    private var unitX = 0f
    private var unitY = 0f

    private val chartPath = Path()
    private val linePath = Path()
    private val imaginaryPath = Path()

    private val textPaint = Paint()
    private val chartPaint = Paint()
    private val linePaint = Paint()
    private val imaginaryPaint = Paint()
    private val pointPaint = Paint()

    private var linearGradient: LinearGradient? = null
    private var pathEffect = CornerPathEffect(10f)
    private var pointEffect = CornerPathEffect(POINT_TEXT_HEIGHT.toFloat())
    private var xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private val bgRectF = RectF()
    private val textRectF = RectF()

    private var selectedNode: Node? = null
    public var isFill = true
    public var showNode = true

    private val startY: Float
        get() = viewHeight - cellHeight

    private val startX: Float
        get() = coordXArray[0]!!.x + coordXArray[0]!!.getFullWidth() / 2

    private val coordYPadding: Float
        get() = 1f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrendView)
        textPaint.textSize = typedArray.getDimension(R.styleable.TrendView_textSize, 28f)
        textPaint.isAntiAlias = false
        textPaint.isDither = false
        textPaint.style = Paint.Style.STROKE
        textPaint.color = typedArray.getColor(R.styleable.TrendView_textColor, Color.BLACK)

        linePaint.color = typedArray.getColor(R.styleable.TrendView_lineColor, Color.RED)
        linePaint.strokeWidth = 3f
        linePaint.isDither = true
        linePaint.isAntiAlias = true
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = typedArray.getColor(R.styleable.TrendView_circleColor, Color.RED)
        pointPaint.isDither = true
        pointPaint.isAntiAlias = true
        pointPaint.style = Paint.Style.FILL_AND_STROKE
        pointPaint.pathEffect = pointEffect
        pointPaint.strokeWidth = 1f

        imaginaryPaint.color = typedArray.getColor(R.styleable.TrendView_imaginaryHColor, Color.GRAY)
        imaginaryPaint.strokeWidth = .3f
        imaginaryPaint.isDither = true
        imaginaryPaint.isAntiAlias = true
        imaginaryPaint.style = Paint.Style.STROKE
        imaginaryPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 8f, 15f, 8f), 0f)

        chartPaint.strokeWidth = .1f
        chartPaint.isAntiAlias = true
        chartPaint.isDither = true

        coordHeight = textPaint.descent() - textPaint.ascent()

        coordY.forEach {
            coordWidth = Math.max(coordWidth, textPaint.measureText(it))
        }

        val coordX = getCoordX()

        viewWidth = Math.max(MIN_WIDTH, (coordX.size + 1) * CELL_MIN_WIDTH)
        viewHeight = Math.max(MIN_HEIGHT, (coordY.size + 2) * CELL_MIN_HEIGHT)
        cellHeight = startY / (coordY.size + 1)

        coordY.forEachIndexed { index, _ ->
            val height = startY + coordYPadding - index * cellHeight
            imaginaryPath.moveTo(0f, height)
            imaginaryPath.lineTo(viewWidth.toFloat(), height)
        }

        coordXArray = arrayOfNulls(coordX.size)
        var countCoordWidth = 0f
        coordX.forEachIndexed { index, it ->
            val width = textPaint.measureText(it)
            val coord = Coord()
            coord.value = it!!
            coord.width = width
            coordXArray[index] = coord
            countCoordWidth += width
        }
        spaceWidth = (viewWidth - coordWidth - countCoordWidth) / (coordX.size) / 2
        var leftPadding = coordWidth
        val bottomPadding = viewHeight - cellHeight + coordHeight
        coordXArray.forEach {
            it!!.leftSpace = spaceWidth
            it.rightSpace = spaceWidth
            it.x = leftPadding
            it.y = bottomPadding
            leftPadding += it.getFullWidth()
        }

        unitX = viewWidth - textPaint.measureText(UNIT) - coordWidth
        unitY = (cellHeight + coordHeight) / 2f

        val fromStart = typedArray.getColor(R.styleable.TrendView_fromColor, Color.WHITE)
        val toColor = typedArray.getColor(R.styleable.TrendView_toColor, Color.RED)

        linearGradient = LinearGradient(0f, startY, 0f, (-viewHeight).toFloat(),
                fromStart, toColor, Shader.TileMode.CLAMP)
    }

    fun setData(data: List<Node>) {
        this.nodes = data
        locationData()
        invalidate()
    }

    private fun locationData() {
        var maxWidth = 0f
        chartPath.reset()
        linePath.reset()
        nodes!!.forEachIndexed { index, it ->
            if (index <= coordXArray.size) {
                val heightUnit = cellHeight / 100f
                it.y = startY - it.valueY * heightUnit
                if (index <= coordXArray.size - 1) {
                    it.x = coordXArray[index]!!.x + coordXArray[index]!!.getFullWidth() / 2
                } else if (index == coordXArray.size) {
                    it.x = viewWidth.toFloat()
                }
                maxWidth = Math.max(maxWidth, it.x)

                if (index == 0) {
                    linePath.moveTo(it.x, it.y)
                } else {
                    linePath.lineTo(it.x, it.y)
                }
                chartPath.lineTo(it.x, it.y)
                if (nodes!!.size <= coordXArray.size && index == nodes!!.size - 1 ||
                        nodes!!.size > coordXArray.size && index == coordXArray.size) {
                    chartPath.lineTo(it.x, startY)
                }
            }
        }

        bgRectF.set(startX, 0f, maxWidth, startY)
        chartPath.lineTo(startX, startY)
        chartPath.close()
        linePaint.pathEffect = pathEffect
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val nearbyNode = getNearbyNode(event.x, event.y)
                if (selectedNode != nearbyNode) {
                    selectedNode = nearbyNode
                    invalidate()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        drawUnit(canvas)
        drawImaginary(canvas)
        drawCoordY(canvas)
        drawCoordX(canvas)
        drawChart(canvas)
        drawTouchedNode(canvas)
    }

    private fun drawUnit(canvas: Canvas) {
        textPaint.color = Color.BLACK
        canvas.drawText(UNIT, unitX, unitY, textPaint)
    }

    private fun drawImaginary(canvas: Canvas) {
        canvas.drawPath(imaginaryPath, imaginaryPaint)
    }

    private fun drawCoordX(canvas: Canvas) {
        textPaint.color = Color.BLACK
        coordXArray.forEach { coord ->
            canvas.drawText(coord!!.value, coord.x, coord.y, textPaint)
        }
    }

    private fun drawCoordY(canvas: Canvas) {
        textPaint.color = Color.BLACK
        coordY.forEachIndexed { index, it ->
            canvas.drawText(it, 0f, startY - cellHeight * index, textPaint)
        }
    }

    private fun drawChart(canvas: Canvas) {
        canvas.drawPath(linePath, linePaint)
        if (showNode) {
            nodes?.forEachIndexed { index, node ->
                if (index <= MAX_TIME) {
                    canvas.drawCircle(node.x, node.y, 5f, pointPaint)
                }
            }
        }
        if (!isFill) return
        val saveLayer = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)

        chartPaint.pathEffect = null
        chartPaint.xfermode = null
        chartPaint.shader = linearGradient
        canvas.drawRect(bgRectF, chartPaint)

        chartPaint.xfermode = xfermode
        //  chartPaint.setStrokeJoin(Paint.Join.ROUND);
        chartPaint.pathEffect = pathEffect
        canvas.drawPath(chartPath, chartPaint)

        canvas.restoreToCount(saveLayer)
    }

    private fun drawTouchedNode(canvas: Canvas) {
        selectedNode?.apply {
            textRectF.left = x - POINT_TEXT_WIDTH / 2
            textRectF.top = y - POINT_TEXT_HEIGHT - POINT_PADDING
            textRectF.right = textRectF.left + POINT_TEXT_WIDTH
            textRectF.bottom = textRectF.top + POINT_TEXT_HEIGHT
            canvas.drawRect(textRectF, pointPaint)
            val textWidth = textPaint.measureText(valueY.toString())
            textPaint.color = Color.WHITE
            canvas.drawText(valueY.toString(), textRectF.left + (textRectF.width() - textWidth) / 2,
                    textRectF.top + textRectF.height() / 2 + coordHeight / 3, textPaint)
            canvas.drawCircle(x, y, 5f, pointPaint)
        }
    }

    private fun getCoordY(): Array<String?> {
        val divide = MAX_PRICE / 100
        val coordY = arrayOfNulls<String>(divide + 1)
        for (i in divide downTo 0) {
            coordY[divide - i] = (MAX_PRICE - i * 100).toString()
        }
        return coordY
    }

    private fun getCoordX(): Array<String?> {
        val coordX = arrayOfNulls<String>(MAX_TIME)
        for (i in 0 until MAX_TIME) {
            coordX[i] = getNumber(i) + ":00"
        }
        return coordX
    }

    private fun getNumber(number: Int): String {
        return if (number > 9) {
            number.toString()
        } else {
            "0$number"
        }
    }

    private fun getNearbyNode(x: Float, y: Float): Node? {
        if (nodes != null) {
            var preOne: Node? = null
            nodes!!.forEach {
                if (preOne != null) {
                    if (x > preOne!!.x && x < it.x) {
                        return if (x - preOne!!.x > it.x - x) it else preOne
                    }
                }
                preOne = it
            }
        }
        return null
    }

    companion object {
        private const val MIN_WIDTH = 800
        private const val MIN_HEIGHT = 600
        private const val CELL_MIN_WIDTH = 90
        private const val CELL_MIN_HEIGHT = 90

        private const val POINT_TEXT_WIDTH = 150
        private const val POINT_TEXT_HEIGHT = 80
        private const val POINT_PADDING = 10

        private const val MAX_PRICE = 400
        private const val MAX_TIME = 24
        private const val UNIT = "单元：元／克"
    }
}