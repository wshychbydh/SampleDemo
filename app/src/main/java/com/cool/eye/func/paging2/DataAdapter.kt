package com.cool.eye.func.paging2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.pagging_data_view_holder.view.*

/**
 * @Created by ycb on 2020/11/6 0016
 */
class DataAdapter : RecyclerView.Adapter<DataHolder>() {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.pagging_data_view_holder, parent, false)
    return DataHolder(view)
  }

  override fun onBindViewHolder(holder: DataHolder, position: Int) {
    holder.itemView.dataTv.text = "第${position}条内容"
  }

  override fun getItemCount() = 10
}

class DataHolder(view: View) : RecyclerView.ViewHolder(view) {}