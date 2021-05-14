package com.cool.eye.func.widget.ripple

import android.graphics.Canvas
import android.graphics.Paint
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.annotation.ColorRes
import com.cool.eye.func.widget.ripple.RippleType.RIPPLE_CIRCLE

/**
 * @link [https://github.com/traex/RippleEffect]
 *
 * Created by cool on 2021/5/10
 */
internal class RippleDelegate(val view: View) : Runnable {
  private var x = -1f
  private var y = -1f
  private var viewWidth = 0
  private var viewHeight = 0
  private var scaleInAnim: ScaleAnimation? = null
  private var scaleOutAnim: ScaleAnimation? = null
  private var ripplePaint = Paint()

  var animationRunning = false
  var radiusMax = 0f
  var timer = 0f
  var timerEmpty = 0f
  var durationEmpty = -1f

  var frameRate = RippleConfig.frameRate
  var rippleDuration = RippleConfig.rippleDuration
  var rippleAlpha = RippleConfig.rippleAlpha
  var zoomDuration = RippleConfig.zoomDuration
  var zoomScale = RippleConfig.zoomScale
  var zoomAble = RippleConfig.zoomAble
    set(value) {
      field = value
      if (value) {
        setupZoom()
      }
    }
  var rippleAble = RippleConfig.rippleAble

  var isCenter: Boolean = RippleConfig.isRippleCenter

  @RippleTypeDef
  var rippleType: Int = RippleConfig.rippleType

  @ColorRes
  var rippleColor = RippleConfig.rippleColor

  var ripplePadding = RippleConfig.ripplePadding
  lateinit var gestureDetector: GestureDetector

  private var onCompletionListener: OnRippleCompleteListener? = null

  override fun run() {
    view.postInvalidate()
  }

  fun init() {

    ripplePaint.isAntiAlias = true
    ripplePaint.style = Paint.Style.FILL
    ripplePaint.color = rippleColor
    ripplePaint.alpha = rippleAlpha

    gestureDetector = GestureDetector(view.context, object : SimpleOnGestureListener() {
      override fun onLongPress(event: MotionEvent) {
        super.onLongPress(event)
        animateRipple(event)
      }

      override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return true
      }

      override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
      }
    })
  }

  fun animateRipple(event: MotionEvent) {
    createAnimation(event.x, event.y)
  }

  /**
   * Launch Ripple animation for the current view centered at x and y position
   *
   * @param x Horizontal position of the ripple center
   * @param y Vertical position of the ripple center
   */
  fun animateRipple(x: Float, y: Float) {
    createAnimation(x, y)
  }

  private fun createAnimation(x: Float, y: Float) {
    if (!animationRunning) {
      radiusMax = viewWidth.coerceAtLeast(viewHeight).toFloat()
      if (rippleType == RIPPLE_CIRCLE) radiusMax /= 2f
      radiusMax -= ripplePadding.toFloat()
      if (isCenter) {
        this.x = viewWidth / 2f
        this.y = viewHeight / 2f
      } else {
        this.x = x
        this.y = y
      }
      animationRunning = true
      view.invalidate()
    }
  }

  fun onSizeChanged(w: Int, h: Int) {
    viewWidth = w
    viewHeight = h

    setupZoom()
  }

  private fun setupZoom() {
    val pivotX = viewWidth / 2f
    val pivotY = viewHeight / 2f

    val scaleInAnim = ScaleAnimation(
        1.0f, zoomScale, 1.0f, zoomScale, pivotX, pivotY
    )
    scaleInAnim.duration = zoomDuration.toLong()
    scaleInAnim.fillAfter = true
    this.scaleInAnim = scaleInAnim

    var scaled = zoomScale

    val scaleOutAnim = ScaleAnimation(
        scaled, 1.0f, scaled, 1.0f, pivotX, pivotY
    )
    scaleOutAnim.duration = zoomDuration.toLong()
    scaleOutAnim.fillAfter = true
    this.scaleOutAnim = scaleOutAnim
  }

  fun onTouchEvent(event: MotionEvent) {
    if (!view.isEnabled) return

    if (rippleAble && gestureDetector.onTouchEvent(event)) {
      animateRipple(event)
    }

    if (zoomAble) {
      doZoom(event)
    }
  }

  private fun doZoom(event: MotionEvent) {
    if (event.action == MotionEvent.ACTION_DOWN) {
      view.startAnimation(scaleInAnim)
    } else if (event.action == MotionEvent.ACTION_UP) {
      view.startAnimation(scaleOutAnim)
    }
  }

  private fun onRippleCompleted() {
    animationRunning = false
    timer = 0f
    durationEmpty = -1f
    timerEmpty = 0f

    onCompletionListener?.onComplete(view)
  }

  fun drawRipple(canvas: Canvas) {
    if (!rippleAble) return
    if (!animationRunning) return
    val paint = this.ripplePaint
    canvas.save()
    if (rippleDuration > timer * frameRate) {
      view.postDelayed(this, frameRate)
    } else {
      canvas.restore()
      onRippleCompleted()
      return
    }

    canvas.drawCircle(x, y, radiusMax * (timer * frameRate / rippleDuration), paint)

    paint.color = rippleColor
    paint.alpha = (rippleAlpha - rippleAlpha * (timer * frameRate / rippleDuration)).toInt()

    timer++
  }
}