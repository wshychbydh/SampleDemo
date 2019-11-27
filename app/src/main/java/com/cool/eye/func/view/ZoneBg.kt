package com.cool.eye.func.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.cool.eye.demo.R

/**
 * Created by cool on 2018/6/19.
 */
class ZoneBg @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val bitmapPaint = Paint()
    private var ovalRectF: RectF = RectF()
    private var bgSrcRect: Rect
    private var bgDestRect: Rect = Rect()

    private var bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_head)

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = Color.WHITE

        bitmapPaint.isDither = false
        bitmapPaint.isAntiAlias = false
        bitmapPaint.colorFilter = null

        bgSrcRect = Rect(0, 0, bgBitmap.width, bgBitmap.height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = Math.min(MeasureSpec.getSize(widthMeasureSpec), resources.displayMetrics.widthPixels)
        val designHeight = (185 * resources.displayMetrics.density).toInt()
        val height = Math.min(designHeight, MeasureSpec.getSize(widthMeasureSpec))
        setMeasuredDimension(width, height)

        ovalRectF.left = -width / 6f
        ovalRectF.right = width + width / 6f
        ovalRectF.top = height / 2f
        ovalRectF.bottom = height * 1.5f

        bgDestRect.right = width
        bgDestRect.bottom = height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawArc(canvas)
    }

    private fun drawArc(canvas: Canvas) {
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawArc(ovalRectF, 180f, 180f, true, paint)
    }

    private fun drawBg(canvas: Canvas) {
        canvas.drawBitmap(bgBitmap, bgSrcRect, bgDestRect, bitmapPaint)
        //   canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}