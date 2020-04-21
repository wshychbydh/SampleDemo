package com.cool.eye.func.view.pie

/**
 *Created by ycb on 2019/10/25 0025
 */
class Pie(
    var name: String? = null,
    var value: Double,
    var color: Int
) {
  var percent: Double = 0.0
  var angle: Double = 0.0
}