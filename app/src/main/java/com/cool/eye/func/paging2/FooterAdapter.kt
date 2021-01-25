package com.cool.eye.func.paging2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.pagging_footer_view_holder.view.*

/**
 * @Created by ycb on 2020/11/6
 */
class FooterAdapter : RecyclerView.Adapter<FooterHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.pagging_footer_view_holder, parent, false)
    return FooterHolder(view)
  }

  override fun onBindViewHolder(holder: FooterHolder, position: Int) {
    holder.itemView.footerTv.text = "底部$position"
  }

  override fun getItemCount() = 3
}

class FooterHolder(view: View) : RecyclerView.ViewHolder(view) {}