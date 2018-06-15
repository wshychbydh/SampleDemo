package com.cool.eye.func.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.cool.eye.func.R


/**
 * Call invalidate() or postInvalidate() after change attribute value
 * Created by cool on 2018/4/24.
 */
class ZoneView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val bitmapPaint = Paint()
    private var ovalRectF: RectF = RectF()
    private var bgSrcRect: Rect
    private var bgDestRect: Rect = Rect()
    private var headBgSrcRect: Rect
    private var headSrcRect: Rect? = null
    private var headDestRect: Rect = Rect()
    private var headBgDestRect: Rect = Rect()
    private var aureoleRect: RectF = RectF()

    // private var bgColor = Color.RED
    private var frontColor = Color.WHITE
    private var textColor = Color.BLACK
    private var phoneHeadPadding = 10f
    private var phoneNamePadding = 5f
    private var headRadius = 63f
    val density = resources.displayMetrics.density

    var onClickListener: (() -> Unit)? = null

    var drawName = false
        set(value) {
            field = value
            invalidate()
        }

    private var bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_head)
    private var headBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_head_default)

    private var nameStartX = 0f
    private var nameStartY = 0f
    private var phoneStartX = 0f
    private var phoneStartY = 0f

    var name: String = ""
        set(value) {
            field = value
            val len = paint.measureText(value)
            nameStartX = (width - len) / 2f
        }
    var phone: String = ""
        set(value) {
            field = value
            val len = paint.measureText(value)
            phoneStartX = (width - len) / 2f
        }

    var head: Bitmap? = null
        set(value) {
            if (value != null) {
                field = createCircleImage(value)
                headSrcRect = Rect(0, 0, value.width, value.height)
            }
        }

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.textSize = 15f * density
        paint.strokeWidth = 4f * density

        bitmapPaint.isDither = false
        bitmapPaint.isAntiAlias = false
        bitmapPaint.colorFilter = null

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZoneView)
            //  bgColor = typedArray.getColor(R.styleable.ZoneView_bg_color, Color.RED)
            frontColor = typedArray.getColor(R.styleable.ZoneView_front_color, Color.WHITE)
            textColor = typedArray.getColor(R.styleable.ZoneView_text_color, Color.BLACK)
            phoneHeadPadding = typedArray.getDimension(R.styleable.ZoneView_phone_head_padding, 18f)
            phoneNamePadding = typedArray.getDimension(R.styleable.ZoneView_phone_name_padding, 4f)

            headRadius = typedArray.getDimension(R.styleable.ZoneView_head_radius, 73f * density)
            val drawable = typedArray.getDrawable(R.styleable.ZoneView_head_drawable)
            if (drawable != null) {
                head = createCircleImage((drawable as BitmapDrawable).bitmap)
            }
            typedArray.recycle()
        }

        bgSrcRect = Rect(0, 0, bgBitmap.width, bgBitmap.height)
        headBgSrcRect = Rect(0, 0, headBitmap.width, headBitmap.height)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = Math.min(MeasureSpec.getSize(widthMeasureSpec), resources.displayMetrics.widthPixels)
        val designHeight = (185 * density).toInt()
        val height = Math.min(designHeight, MeasureSpec.getSize(widthMeasureSpec))
        setMeasuredDimension(width, height)

        ovalRectF.left = -width / 6f
        ovalRectF.right = width + width / 6f
        ovalRectF.top = height / 2f
        ovalRectF.bottom = height * 1.5f

        phoneStartY = height / 2f + (headRadius + 6 + phoneHeadPadding) * density
        val phoneHeight = paint.descent() - paint.ascent()
        nameStartY = phoneStartY + phoneHeight + phoneNamePadding * density

        bgDestRect.right = width
        bgDestRect.bottom = height

        headBgDestRect.left = ((width - headRadius) / 2).toInt()
        headBgDestRect.top = ((height - headRadius) / 2).toInt()
        headBgDestRect.right = ((width + headRadius) / 2).toInt()
        headBgDestRect.bottom = ((height + headRadius) / 2).toInt()

        aureoleRect.left = (width - headRadius) / 2
        aureoleRect.top = (height - headRadius) / 2
        aureoleRect.right = (width + headRadius) / 2
        aureoleRect.bottom = (height + headRadius) / 2

        val shadow = 12 * density
        headDestRect.left = ((width - headRadius + shadow + 2) / 2).toInt()
        headDestRect.top = ((height - headRadius + shadow - 6) / 2).toInt()
        headDestRect.right = ((width + headRadius - shadow - 2) / 2).toInt()
        headDestRect.bottom = ((height + headRadius - shadow - 2) / 2).toInt()
    }

    private var oldX = 0f
    private var oldY = 0f
    private var pressTime = 0L

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    pressTime = System.currentTimeMillis()
                    oldX = x
                    oldY = y
                }
                MotionEvent.ACTION_UP -> {
                    if (isHeadClicked(event)) {
                        onClickListener?.invoke()
                    }
                }
            }
        }
        return true
    }

    private fun isHeadClicked(event: MotionEvent): Boolean {
        return event.x > headBgDestRect.left && event.x < headBgDestRect.right
                && event.y > headBgDestRect.top && event.y < headBgDestRect.bottom
                && System.currentTimeMillis() - pressTime < 1000
                && space(event) < ViewConfiguration.get(context).scaledTouchSlop

    }

    private fun space(event: MotionEvent): Double {
        return Math.sqrt(Math.pow((event.x - oldX).toDouble(), 2.0) + Math.pow((event.y - oldY).toDouble(), 2.0))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawArc(canvas)
        drawAureole(canvas)
        drawHead(canvas)
        drawText(canvas)
    }

    private fun drawArc(canvas: Canvas) {
        paint.color = frontColor
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawArc(ovalRectF, 180f, 180f, true, paint)
    }

    private fun drawBg(canvas: Canvas) {
        canvas.drawBitmap(bgBitmap, bgSrcRect, bgDestRect, bitmapPaint)
        //   canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawHead(canvas: Canvas) {
        if (head != null) {
            canvas.drawBitmap(head, headSrcRect, headDestRect, bitmapPaint)
        }
    }

    private fun drawText(canvas: Canvas) {
        if (phone.isNotEmpty()) {
            paint.color = textColor
            canvas.drawText(phone, phoneStartX, phoneStartY, paint)
        }
        if (drawName && name.isNotEmpty()) {
            if (name.isNotEmpty()) {
                canvas.drawText(name, nameStartX, nameStartY, paint)
            }
        }
    }

    private fun drawAureole(canvas: Canvas) {
        canvas.drawBitmap(headBitmap, headBgSrcRect, headBgDestRect, bitmapPaint)
        paint.color = Color.WHITE
        paint.alpha = 102
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(width / 2f, height / 2f - 2 * density, headRadius / 2 - 4 * density, paint)
        //  canvas.drawArc(aureoleRect, 180f, 180f, true, paint)
    }

    private fun createCircleImage(source: Bitmap): Bitmap {

        val bitmapSize = headRadius
        val matrix = Matrix()
        matrix.postScale(bitmapSize / source.width.toFloat(), bitmapSize / source.height.toFloat())
        val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.isFilterBitmap = true
        val target = Bitmap.createBitmap(bitmapSize.toInt(), bitmapSize.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        canvas.drawCircle(bitmapSize / 2f, bitmapSize / 2f, headRadius / 2, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return target
    }
}