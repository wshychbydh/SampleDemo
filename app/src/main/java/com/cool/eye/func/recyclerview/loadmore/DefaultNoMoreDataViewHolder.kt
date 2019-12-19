package com.cool.eye.func.recyclerview.loadmore

import android.view.View
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.DataViewHolder
import com.cool.eye.func.recyclerview.LayoutId
import kotlinx.android.synthetic.main.holder_no_data.view.*

@LayoutId(R.layout.holder_no_data)
class DefaultNoMoreDataViewHolder(view: View) : DataViewHolder<NoMoreData>(view) {

  override fun updateViewByData(data: NoMoreData) {
    super.updateViewByData(data)
    itemView.tv_load.text = data.data
  }
}