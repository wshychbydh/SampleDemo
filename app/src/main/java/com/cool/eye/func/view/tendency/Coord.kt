package com.cool.eye.func.view.tendency

/**
 * Created by cool on 2019/4/17.
 */
class Coord {
  var value: String = ""
  var width: Float = 0f
  var leftSpace: Float = 0f
  var rightSpace: Float = 0f
  var x: Float = 0f
  var y: Float = 0f

  fun getFullWidth(): Float {
    return width + leftSpace + rightSpace
  }

  fun getMiddleX(): Float {
    return x + width / 2f
  }
}