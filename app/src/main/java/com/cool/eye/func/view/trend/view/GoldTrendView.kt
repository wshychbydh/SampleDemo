package com.cool.eye.func.view.trend.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.cool.eye.chart.mode.Coord
import com.cool.eye.demo.R
import com.cool.eye.func.view.trend.mode.GoldNode
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cool on 2018/3/28.
 */
class GoldTrendView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val nodes: ArrayList<GoldNode> = arrayListOf()
    private val coordY: ArrayList<String> = arrayListOf()
    private val coordX: ArrayList<Coord> = arrayListOf()

    private var viewWidth = 0
    //  private var viewHeight = 0
    private var cellHeight = 0f
    //   private var coordWidth: Float = 0f  //竖向坐标的宽度 （0,100,200的宽度）
    private var coordHeight: Float = 0f //横向坐标的高度 ("01:00"的高度)
    private var unitX = 0f
    private var unitY = 0f

    private val chartPath = Path()
    private val linePath = Path()
    private val imaginaryHPath = Path()
    private val imaginaryVPath = Path()

    private val textPaint = Paint()
    private val chartPaint = Paint()
    private val linePaint = Paint()
    private val imaginaryPaint = Paint()
    private val circlePaint = Paint()

    private var linearGradient: LinearGradient? = null
    // private var pathEffect = CornerPathEffect(CORNER)
    private var pointEffect: CornerPathEffect
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private val bgRectF = RectF()
    private val textRectF = RectF()

    private var selectedGoldNode: GoldNode? = null

    private var circleColor: Int = Color.RED
    private var imaginaryHColor: Int = Color.GRAY
    private var imaginaryVColor: Int = Color.RED
    private var textPaintColor = Color.BLACK

    val fromColor: Int
    val toColor: Int

    var isFill = true
        set(value) {
            field = value
            invalidate()
        }

    var showGoldNode = false
        set(value) {
            field = value
            invalidate()
        }

    private val startY: Float
        get() = minHeight - cellHeight

    private val startX: Float
        get() = coordX[0].x + coordX[0].width / 2

    private val coordYPadding: Float
        get() = 1f

    private var minWidth: Int = 0
    private var minHeight: Int = 0
    private var cellMinWidth: Int = 0
    //  private var cellMinHeight:Int = 0
    private var pointTextWidth: Int = 0
    private var pointTextHeight: Int = 0
    private var pointPadding: Int = 0

    init {
        val density = resources.displayMetrics.density
        minWidth = (MIN_WIDTH * density).toInt()
        minHeight = (MIN_HEIGHT * density).toInt()
        cellMinWidth = (CELL_MIN_WIDTH * density).toInt()
        //    cellMinHeight = (CELL_MIN_HEIGHT * density).toInt()
        pointTextWidth = (POINT_TEXT_WIDTH * density).toInt()
        pointTextHeight = (POINT_TEXT_HEIGHT * density).toInt()
        pointPadding = (POINT_PADDING * density).toInt()

        pointEffect = CornerPathEffect(pointTextHeight / 2f)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TrendView)
        circleColor = typedArray.getColor(R.styleable.TrendView_circleColor, Color.RED)
        imaginaryHColor = typedArray.getColor(R.styleable.TrendView_imaginaryHColor, Color.GRAY)
        imaginaryVColor = typedArray.getColor(R.styleable.TrendView_imaginaryVColor, Color.RED)
        fromColor = typedArray.getColor(R.styleable.TrendView_fromColor, Color.WHITE)
        toColor = typedArray.getColor(R.styleable.TrendView_toColor, Color.RED)
        textPaintColor = typedArray.getColor(R.styleable.TrendView_textColor, Color.BLACK)
        linePaint.color = typedArray.getColor(R.styleable.TrendView_lineColor, Color.RED)
        val textSize = typedArray.getDimension(R.styleable.TrendView_textSize, resources.displayMetrics.density * 10f)
        typedArray.recycle()

        textPaint.textSize = textSize
        textPaint.isAntiAlias = false
        textPaint.isDither = false
        textPaint.style = Paint.Style.STROKE

        linePaint.strokeWidth = 4f
        linePaint.isDither = true
        linePaint.isAntiAlias = true
        linePaint.style = Paint.Style.STROKE

        circlePaint.color = circleColor
        circlePaint.isDither = true
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        circlePaint.pathEffect = pointEffect
        circlePaint.strokeWidth = 4f

        imaginaryPaint.strokeWidth = 1f
        imaginaryPaint.isDither = true
        imaginaryPaint.isAntiAlias = true
        imaginaryPaint.style = Paint.Style.STROKE
        imaginaryPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 8f, 15f, 8f), 0f)

        chartPaint.strokeWidth = 1f
        chartPaint.isAntiAlias = true
        chartPaint.isDither = true
        chartPaint.style = Paint.Style.FILL

        coordHeight = textPaint.descent() - textPaint.ascent()
    }

    fun setData(data: List<GoldNode>) {
        this.nodes.clear()
        this.nodes.addAll(data)
        calculateAsync()
    }

    private fun calculateAsync() {
        Thread({
            reCalculate()
            postInvalidate()
        }).start()
    }

    private fun getMinAndMaxPrice(): DoubleArray {
        var minPrice = Double.MAX_VALUE
        var maxPrice = 0.0
        nodes.forEach {
            if (it.valueY > maxPrice) {
                maxPrice = it.valueY
            }
            if (it.valueY < minPrice) {
                minPrice = it.valueY
            }
        }

        minPrice -= MAX_EXPEND
        maxPrice += MAX_EXPEND

        return doubleArrayOf(minPrice, maxPrice)
    }

    private fun fillCoord(minPrice: Int, scaleUnit: Int) {
        coordY.clear()
        var coordYWidth = 0f
        for (i in 0 until MAX_SCALE + 1) {
            val text = (minPrice + i * scaleUnit).toString()
            coordY.add(text)
            coordYWidth = Math.max(coordYWidth, textPaint.measureText(text))
        }

        val size = nodes.size
        coordX.clear()
        var countCoordWidth = 0f
        for (i in 0 until size) {
            val value = format(nodes[i].valueX)
            val width = textPaint.measureText(value)
            val coord = Coord()
            coord.value = value
            coord.width = width
            coordX.add(coord)
            countCoordWidth += width
        }
        cellHeight = (getTrendViewHeight() / (coordY.size + 1)).toFloat()
        viewWidth = Math.max(minWidth, (coordX.size + 1) * cellMinWidth)
        val spaceWidth = (viewWidth - coordYWidth - countCoordWidth) / size / 2f
        var leftPadding = coordYWidth
        val bottomPadding = minHeight - cellHeight + coordHeight
        coordX.forEach { coord ->
            coord.leftSpace = spaceWidth
            coord.rightSpace = spaceWidth
            coord.x = leftPadding + spaceWidth
            coord.y = bottomPadding
            leftPadding += coord.getFullWidth()
        }

        unitX = viewWidth - textPaint.measureText(UNIT) - coordYWidth
        unitY = (cellHeight + coordHeight) / 2f + pointTextHeight - pointPadding
    }

    private fun calculateImaginaryHPath() {
        coordY.forEachIndexed { index, _ ->
            val height = startY + coordYPadding - index * cellHeight
            imaginaryHPath.moveTo(0f, height)
            imaginaryHPath.lineTo(viewWidth.toFloat(), height)
        }
    }

    private fun calculateTrendViewPath(minPrice: Int, scaleUnit: Int) {
        var maxWidth = 0f
        chartPath.reset()
        linePath.reset()
        val heightUnit = cellHeight / scaleUnit
        nodes.forEachIndexed { index, it ->
            if (index <= coordX.size) {
                it.y = startY - (it.valueY.toFloat() - minPrice) * heightUnit
                val coord = coordX[index]!!
                when {
                    index == 0 -> it.x = coord.x + coord.width / 2
                    index <= coordX.size - 1 -> it.x = coord.x + coord.getFullWidth() / 2 - coord.leftSpace
                    index == coordX.size -> it.x = viewWidth.toFloat() - coord.leftSpace
                }
                maxWidth = Math.max(maxWidth, it.x)

                if (index == 0) {
                    linePath.moveTo(it.x, it.y)
                } else {
                    linePath.lineTo(it.x, it.y)
                }
                chartPath.lineTo(it.x, it.y)
                if (nodes.size <= coordX.size && index == nodes.size - 1 ||
                        nodes.size > coordX.size && index == coordX.size
                ) {
                    chartPath.lineTo(it.x, startY)
                }
            }
        }

        bgRectF.set(startX, 0f, maxWidth, startY)
        chartPath.lineTo(startX, startY)
        chartPath.close()
        //  linePaint.pathEffect = pathEffect
    }

    private fun reCalculate() {
        val prices = getMinAndMaxPrice()
        val showMinPrice = prices[0].toInt()
        val scaleUnit = (Math.round((prices[1] - prices[0]) / MAX_SCALE)).toInt()

        fillCoord(showMinPrice, scaleUnit)
        //  viewHeight = MIN_HEIGHT
        //  viewHeight = Math.max(MIN_HEIGHT, (coordY.size + 2) * CELL_MIN_HEIGHT)

        calculateImaginaryHPath()

        linearGradient = LinearGradient(0f, startY, 0f, -getTrendViewHeight() / 2f,
                fromColor, toColor, Shader.TileMode.MIRROR)

        calculateTrendViewPath(showMinPrice, scaleUnit)
  
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(viewWidth, minHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val nearbyGoldNode = getNearbyGoldNode(event.x)
                if (selectedGoldNode != nearbyGoldNode) {
                    selectedGoldNode = nearbyGoldNode
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
        drawTrendView(canvas)
        drawTouchedGoldNode(canvas)
    }

    private fun drawUnit(canvas: Canvas) {
        textPaint.color = textPaintColor
        canvas.drawText(UNIT, unitX, unitY, textPaint)
    }

    private fun drawImaginary(canvas: Canvas) {
        imaginaryPaint.color = imaginaryHColor
        canvas.drawPath(imaginaryHPath, imaginaryPaint)
    }

    private fun drawCoordX(canvas: Canvas) {
        textPaint.color = textPaintColor
        coordX.forEach { coord ->
            canvas.drawText(coord.value, coord.x, coord.y, textPaint)
        }
    }

    private fun drawCoordY(canvas: Canvas) {
        textPaint.color = textPaintColor
        coordY.forEachIndexed { index, it ->
            canvas.drawText(it, 0f, startY - cellHeight * index, textPaint)
        }
    }

    private fun drawTrendView(canvas: Canvas) {
        canvas.drawPath(linePath, linePaint)
        if (showGoldNode) {
            nodes.forEachIndexed { index, node ->
                if (index <= nodes.size) {
                    canvas.drawCircle(node.x, node.y, 5f, circlePaint)
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
        //  chartPaint.setStrokeJoin(Paint.Join.ROUND)
        //  chartPaint.pathEffect = pathEffect
        canvas.drawPath(chartPath, chartPaint)

        canvas.restoreToCount(saveLayer)
    }

    private fun drawTouchedGoldNode(canvas: Canvas) {
        selectedGoldNode?.apply {
            textRectF.left = x - pointTextWidth / 2
            textRectF.top = y - pointTextHeight - pointPadding
            textRectF.right = textRectF.left + pointTextWidth
            textRectF.bottom = textRectF.top + pointTextHeight

            circlePaint.style = Paint.Style.FILL_AND_STROKE
            circlePaint.color = circleColor

            canvas.drawRect(textRectF, circlePaint)

            //    textPaint.textSize = 35f
            textPaint.color = Color.WHITE
            //    val textHeight = textPaint.descent() - textPaint.ascent()
            val textWidth = textPaint.measureText(valueY.toString())
            canvas.drawText(valueY.toString(), textRectF.left + (textRectF.width() - textWidth) / 2,
                    textRectF.top + textRectF.height() / 2 + coordHeight / 3, textPaint)

            circlePaint.color = Color.WHITE
            canvas.drawCircle(x, y, 5f, circlePaint)
            circlePaint.style = Paint.Style.STROKE
            circlePaint.color = circleColor
            canvas.drawCircle(x, y, 7f, circlePaint)


            imaginaryVPath.reset()
            imaginaryVPath.moveTo(x, startY)
            imaginaryVPath.lineTo(x, cellHeight / 2f + pointTextHeight + pointPadding)
            imaginaryPaint.color = imaginaryVColor
            canvas.drawPath(imaginaryVPath, imaginaryPaint)
        }
    }

    private fun getNearbyGoldNode(x: Float): GoldNode? {
        var preOne: GoldNode? = null
        nodes.forEach {
            if (preOne != null) {
                if (x > preOne!!.x && x < it.x) {
                    return if (x - preOne!!.x > it.x - x) it else preOne
                }
            }
            preOne = it
        }
        return null
    }

    private fun getTrendViewHeight(): Int {
        return minHeight - pointTextHeight - pointPadding
    }

    companion object {

        val date = java.util.Date()
        val format = SimpleDateFormat("MM-dd", Locale.CHINA)

        private fun format(ms: Long): String {
            date.time = ms
            return format.format(date)
        }

        private const val MIN_WIDTH = 336
        private const val MIN_HEIGHT = 235
        private const val CELL_MIN_WIDTH = 40
        //  private const val CELL_MIN_HEIGHT = 100

        private const val POINT_TEXT_WIDTH = 36
        private const val POINT_TEXT_HEIGHT = 20
        private const val POINT_PADDING = 7
        private const val CORNER = 5f

        private const val MAX_SCALE = 10 //刻度
        private const val MAX_EXPEND = 10 //刻度范围扩增

        //  private const val MAX_PRICE = 400
        //   private const val MAX_TIME = 7
        private const val UNIT = "单元：元／克"
    }
}