package com.cool.eye.func.view.percent

/**
 *Created by ycb on 2019/10/25 0025
 */
class Percent(
    val value: Int,
    val total: Int,
    val ratio: String,
    val outColor: Int,
    val innerColor: Int,
    val textColor: Int
) {
  val angle = (value.toFloat() * 360f / total.toFloat())
}