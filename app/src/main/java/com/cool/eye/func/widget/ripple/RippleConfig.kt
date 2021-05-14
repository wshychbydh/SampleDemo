package com.cool.eye.func.widget.ripple

import android.graphics.Color

/**
 * Created by cool on 2021/5/10
 */
object RippleConfig {

    private const val RIPPLE_DURATION = 400
    private const val RIPPLE_ALPHA = 60
    private const val FRAME_RATE = 16L
    private const val RIPPLE_COLOR = "#ffffff"
    private const val RIPPLE_COLOR_VICE = "#ff4444"
    private const val ZOOM_DURATION = 400
    private const val ZOOM_SCALE = 0.85f

    internal var rippleAble = true
    internal var isRippleCenter = false

    @RippleTypeDef
    internal var rippleType: Int = RippleType.RIPPLE_RECTANGLE
    internal var rippleDuration = RIPPLE_DURATION
    internal var rippleAlpha = RIPPLE_ALPHA
    internal var frameRate = FRAME_RATE
    internal var ripplePadding = 0
    internal var rippleColor = Color.parseColor(RIPPLE_COLOR)
    internal var rippleColorVice = Color.parseColor(RIPPLE_COLOR_VICE)

    internal var zoomAble = false
    internal var zoomDuration = ZOOM_DURATION
    internal var zoomScale = ZOOM_SCALE

    fun config(block: RippleConfig.() -> Unit) {
        block.invoke(this)
    }
}