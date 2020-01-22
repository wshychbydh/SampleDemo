package com.cool.eye.func.dialog.loading

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.cool.eye.demo.R
import kotlin.math.min
import kotlin.math.sqrt

/**
 *Created by cool on 2019/1/2 0002
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {

  private val arcPaint = Paint()
  private val bgPaint = Paint()
  private var logo: Bitmap? = null
  private val rectF: RectF = RectF()
  private val descRect: Rect = Rect()
  private val srcRect: Rect = Rect()

  private val bgColor: Int
  private val isCircle: Boolean
  private var startAngle = START_ANGLE
  private var sweepAngle = SWEEP_ANGLE
  private val showBg: Boolean
  private var attachedToWindow: Boolean = false
  private val iconMargin: Float
  private val arcMargin: Float
  private val strokeWidth: Float
  private var radius: Float = 0f

  private val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 360f)
  private val density = context.resources.displayMetrics.density

  init {
    val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView, defStyleAttr, 0)
    val moveSpeed = array.getFloat(R.styleable.LoadingView_lv_speed, 1.0f)
    isCircle = array.getBoolean(R.styleable.LoadingView_lv_circle, false)
    showBg = array.getBoolean(R.styleable.LoadingView_lv_showBg, true)
    bgColor = array.getColor(R.styleable.LoadingView_lv_bgColor, BG_COLOR_DEFAULT)
    iconMargin = array.getDimension(R.styleable.LoadingView_lv_padding, VIEW_PADDING_DEFAULT * density)
    strokeWidth = array.getDimension(R.styleable.LoadingView_lv_strokeWidth, STROKE_WIDTH_DEFAULT * density)
    val iconResId = array.getResourceId(R.styleable.LoadingView_lv_logo, 0)
    arcMargin = array.getDimension(R.styleable.LoadingView_lv_margin, 0f)
    val arcColor = array.getColor(R.styleable.LoadingView_lv_arcColor, Color.parseColor(ARC_COLOR_DEFAULT))
    array.recycle()

    if (iconResId != 0) {
      val logo = (ContextCompat.getDrawable(context, iconResId) as BitmapDrawable).bitmap
      srcRect.right = logo.width
      srcRect.bottom = logo.height
      this.logo = logo
    }

    bgPaint.isAntiAlias = true
    bgPaint.isDither = true
    bgPaint.style = Paint.Style.FILL
    bgPaint.color = bgColor

    arcPaint.isAntiAlias = true
    arcPaint.isDither = true
    arcPaint.style = Paint.Style.STROKE
    arcPaint.strokeWidth = strokeWidth
    arcPaint.strokeCap = Paint.Cap.ROUND
    arcPaint.color = arcColor

    animator.repeatCount = -1
    animator.duration = (moveSpeed * 1000f).toLong()
    animator.interpolator = null
    animator.addUpdateListener(this)
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = getMeasureSize(widthMeasureSpec)
    val height = getMeasureSize(heightMeasureSpec)
    val viewSize = min(width, height)
    radius = viewSize / 2f
    val resultSize = viewSize.toInt()
    setMeasuredDimension(resultSize, resultSize)
    onMeasured(viewSize)
  }

  private fun getMeasureSize(measureSpec: Int): Float {
    val specMode = MeasureSpec.getMode(measureSpec)
    val specSize = MeasureSpec.getSize(measureSpec)
    return when (specMode) {
      MeasureSpec.AT_MOST -> min(specSize.toFloat(), VIEW_SIZE_DEFAULT * density)
      MeasureSpec.EXACTLY -> specSize.toFloat()
      else -> VIEW_SIZE_DEFAULT * density
    }
  }

  private fun onMeasured(viewSize: Float) {
    val start = strokeWidth + arcMargin
    rectF.left = start
    rectF.top = start
    rectF.right = viewSize - start
    rectF.bottom = viewSize - start
    val offset = radius - radius / sqrt(2.0).toFloat()
    val from = ((arcMargin + iconMargin + strokeWidth + offset)).toInt()
    val to = viewSize.toInt() - from
    descRect.left = from
    descRect.top = from
    descRect.right = to
    descRect.bottom = to
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    attachedToWindow = true
    animator.start()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    attachedToWindow = false
    animator.cancel()
  }

  override fun onAnimationUpdate(animation: ValueAnimator) {
    startAngle = animation.animatedValue as Float
    if (startAngle > 0f) {
      invalidate()
    }
  }

  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    super.onVisibilityChanged(changedView, visibility)
    if (attachedToWindow) {
      if (visibility == VISIBLE) {
        animator.start()
      } else {
        animator.cancel()
      }
    }
  }

  override fun onDraw(canvas: Canvas) {
    if (showBg) {
      val half = radius
      if (isCircle) {
        canvas.drawCircle(half, half, half, bgPaint)
      } else {
        canvas.drawColor(bgColor)
      }
    }
    logo?.let {
      srcRect.left = 0
      srcRect.top = 0
      srcRect.right = it.width
      srcRect.bottom = it.height
      canvas.drawBitmap(it, srcRect, descRect, arcPaint)
    }
    canvas.drawArc(rectF, startAngle, sweepAngle, false, arcPaint)
  }

  companion object {
    const val VIEW_SIZE_DEFAULT = 132f
    const val VIEW_PADDING_DEFAULT = 0f
    const val START_ANGLE = -90f
    const val SWEEP_ANGLE = 90f
    const val STROKE_WIDTH_DEFAULT = 4f
    const val ARC_COLOR_DEFAULT = "#FEAB38"
    const val BG_COLOR_DEFAULT = Color.WHITE
  }
}