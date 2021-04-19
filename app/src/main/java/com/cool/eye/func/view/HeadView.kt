package com.cool.eye.func.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.cool.eye.demo.R


/**
 * Created by cool on 2018/6/19.
 */
class HeadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

  private val paint = Paint()
  private val bitmapPaint = Paint()
  private var headBgSrcRect: Rect
  private var headSrcRect: Rect? = null
  private var headDestRect: Rect = Rect()
  private var headBgDestRect: Rect = Rect()
  private var aureoleRect: RectF = RectF()

  private var headSize = 73f
  val density = resources.displayMetrics.density

  private var headBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_head_default)

  private var head: Bitmap? = null
    set(value) {
      if (value != null) {
        field = createCircleImage(value, headSize)
        headSrcRect = Rect(0, 0, value.width, value.height)
        invalidate()
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
      val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeadView)
      headSize = typedArray.getDimension(R.styleable.HeadView_head_size, 73f * density)
      typedArray.recycle()
    }

    headBgSrcRect = Rect(0, 0, headBitmap.width, headBitmap.height)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    val width = Math.min(MeasureSpec.getSize(widthMeasureSpec), resources.displayMetrics.widthPixels)
    val designHeight = headSize.toInt()
    val height = Math.min(designHeight, MeasureSpec.getSize(widthMeasureSpec))
    setMeasuredDimension(width, height)

    headBgDestRect.left = ((width - headSize) / 2).toInt()
    headBgDestRect.top = ((height - headSize) / 2).toInt()
    headBgDestRect.right = ((width + headSize) / 2).toInt()
    headBgDestRect.bottom = ((height + headSize) / 2).toInt()

    aureoleRect.left = (width - headSize) / 2
    aureoleRect.top = (height - headSize) / 2
    aureoleRect.right = (width + headSize) / 2
    aureoleRect.bottom = (height + headSize) / 2

    val shadow = 12 * density
    headDestRect.left = ((width - headSize + shadow + 2) / 2).toInt()
    headDestRect.top = ((height - headSize + shadow - 6) / 2).toInt()
    headDestRect.right = ((width + headSize - shadow - 2) / 2).toInt()
    headDestRect.bottom = ((height + headSize - shadow - 2) / 2).toInt()
  }

  override fun onDraw(canvas: Canvas) {
    //super.onDraw(canvas)
    if (head == null && drawable != null) {
      head = (drawable as? BitmapDrawable)?.bitmap
    }
    drawAureole(canvas)
    drawHead(canvas)
  }

  private fun drawHead(canvas: Canvas) {
    if (head != null) {
      canvas.drawBitmap(head!!, headSrcRect, headDestRect, bitmapPaint)
    }
  }

  private fun drawAureole(canvas: Canvas) {
    canvas.drawBitmap(headBitmap, headBgSrcRect, headBgDestRect, bitmapPaint)
    paint.color = Color.WHITE
    paint.alpha = 102
    paint.style = Paint.Style.STROKE
    canvas.drawCircle(width / 2f, height / 2f - 2 * density, headSize / 2 - 4 * density, paint)
  }

  private fun createCircleImage(source: Bitmap, bitmapSize: Float): Bitmap? {
    if (source.width <= 0 || source.height <= 0) return null
    val matrix = Matrix()
    matrix.postScale(bitmapSize / source.width.toFloat(), bitmapSize / source.height.toFloat())
    val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)

    val paint = Paint()
    paint.isAntiAlias = true
    paint.isDither = true
    paint.isFilterBitmap = true
    val target = Bitmap.createBitmap(bitmapSize.toInt(), bitmapSize.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)
    canvas.drawCircle(bitmapSize / 2f, bitmapSize / 2f, bitmapSize / 2, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)

    return target
  }
}