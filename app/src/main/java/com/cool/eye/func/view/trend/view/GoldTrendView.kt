package com.cool.eye.func.view.trend.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.cool.eye.chart.mode.Coord
import com.cool.eye.func.R
import com.cool.eye.func.view.trend.mode.GoldNode
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by cool on 2018/3/28.
 */
class GoldTrendView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var nodes: List<GoldNode>? = null
    private lateinit var coordY: Array<String?>
    private lateinit var coordXArray: Array<Coord?>

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
    private var pointEffect : CornerPathEffect
    private var xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
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
        get() = coordXArray[0]!!.x + coordXArray[0]!!.width / 2

    private val coordYPadding: Float
        get() = 1f

    private var minWidth:Int = 0
    private var minHeight:Int = 0
    private var cellMinWidth:Int = 0
    //  private var cellMinHeight:Int = 0
    private var pointTextWidth:Int = 0
    private var pointTextHeight:Int = 0
    private var pointPadding:Int = 0

    init {
        val density = resources.displayMetrics.density
        minWidth = (MIN_WIDTH * density).toInt()
        minHeight = (MIN_HEIGHT * density).toInt()
        cellMinWidth = (CELL_MIN_WIDTH * density).toInt()
        //    cellMinHeight = (CELL_MIN_HEIGHT * density).toInt()
        pointTextWidth = (POINT_TEXT_WIDTH * density).toInt()
        pointTextHeight = (POINT_TEXT_HEIGHT * density).toInt()
        pointPadding = (POINT_PADDING * density).toInt()

        pointEffect  = CornerPathEffect(pointTextHeight / 2f)

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
        this.nodes = data
        //FIXME run on AsyncThread
        reCalculate()
        //run on ui thread
        invalidate()
    }

    private fun getCoordX(nodes: List<GoldNode>): Array<String?> {
        val size = nodes.size
        val coordX = arrayOfNulls<String>(size)
        for (i in 0 until size) {
            coordX[i] = format(nodes[i].valueX)
        }
        return coordX
    }

    private fun format(ms: Long): String {
        val format2 = SimpleDateFormat("MM-dd")
        return format2.format(Date(ms))
    }

    private var scaleUnit: Int = 1  // 每个刻度的值

    private fun reCalculate() {

        var minPrice = Double.MAX_VALUE
        var maxPrice = 0.0
        nodes!!.forEach {
            if (it.valueY > maxPrice) {
                maxPrice = it.valueY
            }
            if (it.valueY < minPrice) {
                minPrice = it.valueY
            }
        }

        minPrice -= MAX_EXPEND
        maxPrice += MAX_EXPEND

        val showMinPrice = minPrice.toInt()
        scaleUnit = (Math.round(maxPrice - minPrice) / MAX_SCALE).toInt()
        coordY = arrayOfNulls(MAX_SCALE + 1)
        for (i in  0 until MAX_SCALE + 1) {
            coordY[i] = (showMinPrice + i * scaleUnit).toString()
        }

        var coordWidth = 0f

        coordY.forEach {
            coordWidth = Math.max(coordWidth, textPaint.measureText(it))
        }

        val coordX = getCoordX(nodes!!)

        viewWidth = Math.max(minWidth, (coordX.size + 1) * cellMinWidth)
        //  viewHeight = MIN_HEIGHT
        //  viewHeight = Math.max(MIN_HEIGHT, (coordY.size + 2) * CELL_MIN_HEIGHT)
        cellHeight = (getTrendViewHeight() / (coordY.size + 1)).toFloat()

        coordY.forEachIndexed { index, _ ->
            val height = startY + coordYPadding - index * cellHeight
            imaginaryHPath.moveTo(0f, height)
            imaginaryHPath.lineTo(viewWidth.toFloat(), height)
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
        val spaceWidth = (viewWidth - coordWidth - countCoordWidth) / coordX.size / 2f
        var leftPadding = coordWidth
        val bottomPadding = minHeight - cellHeight + coordHeight
        coordXArray.forEach { coord ->
            coord!!.leftSpace = spaceWidth
            coord.rightSpace = spaceWidth
            coord.x = leftPadding + spaceWidth
            coord.y = bottomPadding
            leftPadding += coord.getFullWidth()
        }

        unitX = viewWidth - textPaint.measureText(UNIT) - coordWidth
        unitY = (cellHeight + coordHeight) / 2f + pointTextHeight - pointPadding

        linearGradient = LinearGradient(0f, startY, 0f, -getTrendViewHeight() / 2f,
                fromColor, toColor, Shader.TileMode.MIRROR)

        var maxWidth = 0f
        chartPath.reset()
        linePath.reset()
        val heightUnit = cellHeight / scaleUnit
        nodes!!.forEachIndexed { index, it ->
            if (index <= coordXArray.size) {
                it.y = startY - (it.valueY.toFloat() - showMinPrice) * heightUnit
                val coord = coordXArray[index]!!
                when {
                    index == 0 -> it.x = coord.x + coord.width / 2
                    index <= coordXArray.size - 1 -> it.x = coord.x + coord.getFullWidth() / 2 - coord.leftSpace
                    index == coordXArray.size -> it.x = viewWidth.toFloat() - coord.leftSpace
                }
                maxWidth = Math.max(maxWidth, it.x)

                if (index == 0) {
                    linePath.moveTo(it.x, it.y)
                } else {
                    linePath.lineTo(it.x, it.y)
                }
                chartPath.lineTo(it.x, it.y)
                if (nodes!!.size <= coordXArray.size && index == nodes!!.size - 1 ||
                        nodes!!.size > coordXArray.size && index == coordXArray.size
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(viewWidth, minHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val nearbyGoldNode = getNearbyGoldNode(event.x, event.y)
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
        drawChart(canvas)
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
        coordXArray.forEach { coord ->
            canvas.drawText(coord!!.value, coord.x, coord.y, textPaint)
        }
    }

    private fun drawCoordY(canvas: Canvas) {
        textPaint.color = textPaintColor
        coordY.forEachIndexed { index, it ->
            canvas.drawText(it, 0f, startY - cellHeight * index, textPaint)
        }
    }

    private fun drawChart(canvas: Canvas) {
        canvas.drawPath(linePath, linePaint)
        if (showGoldNode) {
            nodes?.forEachIndexed { index, node ->
                if (index <= nodes!!.size) {
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

    private fun getNearbyGoldNode(x: Float, y: Float): GoldNode? {
        if (nodes != null) {
            var preOne: GoldNode? = null
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

    private fun getTrendViewHeight(): Int {
        return minHeight - pointTextHeight - pointPadding
    }

    companion object {
        private const val MIN_WIDTH = 300
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