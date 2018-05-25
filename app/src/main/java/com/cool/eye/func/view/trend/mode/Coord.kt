package com.cool.eye.chart.mode

/**
 * Created by cool on 2018/4/12.
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
}