package com.cool.eye.func.banner

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.cool.eye.demo.R


/**
 *Created by ycb on 2019/1/8 0008
 */
class BannerPagerAdapter(private val pages: List<Any>) : androidx.viewpager.widget.PagerAdapter() {
  override fun isViewFromObject(p0: View, p1: Any): Boolean {
    return p0 == p1
  }

  override fun getCount(): Int {
    return pages.size
  }

  override fun instantiateItem(container: ViewGroup, position: Int): Any {
    val fl = FrameLayout(container.context)
    val tv = TextView(container.context)
    val imageView = ImageView(container.context)
    imageView.layoutParams = ViewGroup.LayoutParams(-1, -1)
    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
    imageView.setImageResource(R.mipmap.ic_launcher)
    fl.addView(imageView)
    fl.addView(tv)
    tv.textSize = 30f
    tv.setTextColor(Color.BLACK)
    tv.text = "Position:$position"
    container.addView(fl)
    fl.setOnClickListener {
      Toast.makeText(container.context, "Click $position", Toast.LENGTH_SHORT).show()
    }
    return fl
  }

  override fun destroyItem(container: View, position: Int, `object`: Any) {
    (container as ViewGroup).removeView(`object` as View)
  }
}