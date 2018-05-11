package com.cool.eye.func.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.cool.eye.func.R


/**
 * Call invalidate() or postInvalidate() after change attribute value
 * Created by cool on 2018/4/24.
 */
class ZoneView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private var ovalRectF: RectF = RectF()

    var bgColor = Color.RED
    var frontColor = Color.WHITE
    var textColor = Color.BLACK
    var phoneHeadPadding = 10f
    var phoneNamePadding = 5f
    var headRadius = 100f

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
            }
        }

    init {
        paint.isDither = true
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.textSize = 30f

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZoneView)
            bgColor = typedArray.getColor(R.styleable.ZoneView_bg_color, Color.RED)
            frontColor = typedArray.getColor(R.styleable.ZoneView_front_color, Color.WHITE)
            textColor = typedArray.getColor(R.styleable.ZoneView_text_color, Color.BLACK)
            phoneHeadPadding = typedArray.getDimension(R.styleable.ZoneView_phone_head_padding, 18f)
            phoneNamePadding = typedArray.getDimension(R.styleable.ZoneView_phone_name_padding, 4f)

            headRadius = typedArray.getDimension(R.styleable.ZoneView_head_radius, 100f)
            val drawable = typedArray.getDrawable(R.styleable.ZoneView_head_drawable)
            if (drawable != null) {
                head = createCircleImage((drawable as BitmapDrawable).bitmap)
            }
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = resources.displayMetrics.widthPixels
        val height = Math.min(500, MeasureSpec.getSize(heightMeasureSpec))
        setMeasuredDimension(width, height)

        ovalRectF.left = -width / 6f
        ovalRectF.right = width + width / 6f
        ovalRectF.top = height / 2f
        ovalRectF.bottom = height * 1.5f

        phoneStartY = height / 2f + headRadius + phoneHeadPadding * resources.displayMetrics.density
        val phoneHeight = paint.descent() - paint.ascent()
        nameStartY = phoneStartY + phoneHeight + phoneNamePadding * resources.displayMetrics.density
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawHead(canvas)
        drawText(canvas)
    }


    private fun drawBg(canvas: Canvas) {
        paint.color = bgColor
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        paint.color = frontColor
        canvas.drawArc(ovalRectF, 180f, 180f, true, paint)
    }

    private fun drawHead(canvas: Canvas) {
        if (head != null) {
            canvas.drawBitmap(head, width / 2 - headRadius, height / 2 - headRadius, paint)
        }
    }

    private fun drawText(canvas: Canvas) {
        name = if (name.isEmpty()) "张三" else name
        phone = if (phone.isEmpty()) "12345***901" else phone
        paint.color = textColor
        if (phone.isNotEmpty()) {
            canvas.drawText(phone, phoneStartX, phoneStartY, paint)
        }
        if (name.isNotEmpty()) {
            canvas.drawText(name, nameStartX, nameStartY, paint)
        }
    }

    private fun createCircleImage(source: Bitmap): Bitmap {

        val bitmapSize = headRadius * 2f
        val matrix = Matrix()
        matrix.postScale(bitmapSize / source.width, bitmapSize / source.height)
        val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.isFilterBitmap = true
        val target = Bitmap.createBitmap(bitmapSize.toInt(), bitmapSize.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(target)
        canvas.drawCircle(bitmapSize / 2f, bitmapSize / 2f, headRadius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return target
    }
}