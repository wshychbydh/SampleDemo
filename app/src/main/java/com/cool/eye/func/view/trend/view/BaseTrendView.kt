package com.cool.eye.func.view.trend.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by cool on 2018/3/28.
 */
open class BaseTrendView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var coordX: List<String> = ArrayList()  //X坐标
    private var coordY: List<String> = ArrayList()  //Y坐标

    protected var showImaginaryH = false  //水平虚线
    protected var showImaginaryV = false  //垂直虚线
    protected var showCoordinate: Boolean = true  //显示坐标
    protected var showCoordArrow = true //显示坐标两端的箭头
    protected var showCoordinateUnit: Boolean = true  //显示坐标上的单位

    protected var bgColor = Color.WHITE
    protected var coordColor = Color.GRAY

    protected val paint = Paint()

    protected var startX = 0f
    protected var startY = 0f

    protected val density = resources.displayMetrics.density

    init {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(bgColor)
        drawCoordinate(canvas)
    }

    private fun drawCoordinate(canvas: Canvas) {
        if (showCoordinate) {
            val linePaint = getLinePaint()
            canvas.drawLine(startX, startY, width.toFloat(), height.toFloat(), linePaint)
            if (showCoordArrow) {
            }
            if (showCoordinateUnit) {
            }
        }
    }

    final override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    open fun measureWidth(widthMeasureSpec: Int): Int {
        return (MIN_WIDTH * density).toInt()
    }

    open fun measureHeight(heightMeasureSpec: Int): Int {
        return (MIN_HEIGHT * density).toInt()
    }

    open fun getLinePaint(): Paint {
        paint.reset()
        paint.color = coordColor
        paint.isDither = true
        paint.isAntiAlias = true
        paint.strokeWidth = 4f
        return paint
    }

    companion object {
        private const val MIN_WIDTH = 300
        private const val MIN_HEIGHT = 235
    }
}