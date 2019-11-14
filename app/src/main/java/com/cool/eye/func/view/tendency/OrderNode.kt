package com.cool.eye.func.view.tendency

/**
 *Created by ycb on 2019/4/17 0017
 */
data class OrderNode(
    var date: String,
    var increase: Int = 0,
    var deal: Int = 0,
    val avg: String,
    var x: Float = 0f,
    var increaseY: Float = 0f,
    var dealY: Float = 0f
)
