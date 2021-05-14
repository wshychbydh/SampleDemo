package com.cool.eye.func.widget.ripple

import androidx.annotation.IntDef


/**
 * Created by cool on 2021/5/10
 */
@IntDef(RippleType.RIPPLE_CIRCLE, RippleType.RIPPLE_RECTANGLE)
@Retention(AnnotationRetention.SOURCE)
annotation class RippleTypeDef

object RippleType {
  const val RIPPLE_CIRCLE = 0
  const val RIPPLE_RECTANGLE = 1
}