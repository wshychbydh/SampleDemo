package com.cool.eye.func.banner

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import com.cool.eye.func.banner.vp2.CarouselAdapter
import kotlinx.android.synthetic.main.banner_item_layout.view.*

/**
 * Created by ycb on 2020/6/9 0009
 */
class Banner2Adapter(private val pages: List<Any>) : CarouselAdapter<Banner2ViewHolder>() {

  private val colors = intArrayOf(Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.DKGRAY)

  override fun getRealItemCount(): Int {
    return pages.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Banner2ViewHolder {
    return Banner2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner_item_layout, parent, false))
  }

  override fun onBindViewHolder2(holder: Banner2ViewHolder, position: Int) {
    holder.itemLayout.setBackgroundColor(colors[position % colors.size])
    holder.content.text = "ViewPager2 ：第${position}页"
  }
}

class Banner2ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
  val itemLayout: FrameLayout = v.bannerLayout
  val content: TextView = v.bannerTv
}