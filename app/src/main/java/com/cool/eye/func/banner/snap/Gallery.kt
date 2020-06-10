package com.cool.eye.func.banner.snap

import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ycb on 2020/6/10 0010
 */
class Gallery(
    val recyclerView: RecyclerView,
    var params: GalleryParams = GalleryParams.Builder().build()
) {

  fun setup() {
    val helper = if (params.fastScroll) LinearSnapHelper() else LoopPagerSnapHelper()
    helper.attachToRecyclerView(recyclerView)
  }
}