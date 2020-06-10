package com.cool.eye.func.banner

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.gallery_item_layout.view.*

/**
 * Created by ycb on 2020/6/9 0009
 */
class Banner4Adapter(private val pages: List<Any>, private val name: String) : RecyclerView.Adapter<Banner4ViewHolder>() {

  private val colors = intArrayOf(Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN, Color.DKGRAY)

  override fun getItemCount(): Int {
    return pages.size
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Banner4ViewHolder {
    return Banner4ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_layout, parent, false))
  }

  override fun onBindViewHolder(holder: Banner4ViewHolder, position: Int) {
    holder.itemLayout.setBackgroundColor(colors[position % colors.size])
    holder.content.text = "$name 第${position}页"
  }
}

class Banner4ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
  val itemLayout: FrameLayout = v.bannerLayout
  val content: TextView = v.bannerTv
}