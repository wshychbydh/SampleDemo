package com.cool.eye.func.banner.snap

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class LoopPagerSnapHelper : PagerSnapHelper() {
  override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
    val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
    return position % layoutManager.itemCount
  }
}