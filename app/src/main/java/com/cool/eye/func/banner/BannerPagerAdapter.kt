package com.cool.eye.func.banner

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.cool.eye.demo.R


/**
 *Created by ycb on 2019/1/8 0008
 */
class BannerPagerAdapter() : androidx.viewpager.widget.PagerAdapter() {
  override fun isViewFromObject(p0: View, p1: Any): Boolean {
    return p0 == p1
  }

  override fun getCount(): Int {
    return 1
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val imageView = ImageView(container.context)
    imageView.layoutParams = ViewGroup.LayoutParams(-1, -1)
    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    imageView.setImageResource(R.mipmap.ic_launcher)
    container.addView(imageView)
    return imageView
  }

  override fun destroyItem(container: View, position: Int, `object`: Any) {
    (container as ViewGroup).removeView(`object` as View)
  }
}