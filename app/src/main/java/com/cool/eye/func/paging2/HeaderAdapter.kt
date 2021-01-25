package com.cool.eye.func.paging2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.pagging_header_view_holder.view.*

/**
 * @Created by ycb on 2020/11/6 0016
 */
class HeaderAdapter : RecyclerView.Adapter<HeaderViewHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.pagging_header_view_holder, parent, false)
    return HeaderViewHolder(view)
  }

  override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
    holder.itemView.headerTv.text = "头部$position"
  }

  override fun getItemCount() = 5
}

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {}