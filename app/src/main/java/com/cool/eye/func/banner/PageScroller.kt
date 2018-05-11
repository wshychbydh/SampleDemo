package com.cool.eye.func.banner

import android.content.Context
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller

/**
 * Created by cool on 2018/4/23.
 */
class PageScroller internal constructor(context: Context, private var sDuration: Int = 800) :
        Scroller(context, DecelerateInterpolator()) {

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, sDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, sDuration)
    }
}