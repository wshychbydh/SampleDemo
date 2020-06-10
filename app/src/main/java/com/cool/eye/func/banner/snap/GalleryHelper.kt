package com.cool.eye.func.banner.snap

import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ycb on 2020/6/10 0010
 */
class GalleryHelper : LinearSnapHelper() {

  override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
    val out = super.calculateDistanceToFinalSnap(layoutManager, targetView)!!
    if (layoutManager.canScrollHorizontally()) {
      out[0] *= 2
    }

    if (layoutManager.canScrollVertically()) {
      out[1] *= 2
    }
    return out
  }
}