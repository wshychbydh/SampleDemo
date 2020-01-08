package com.cool.eye.func.recyclerview

import android.view.View
import com.cool.eye.demo.R
import com.eye.cool.adapter.support.DataViewHolder
import com.eye.cool.adapter.support.LayoutId
import kotlinx.android.synthetic.main.holder_mock.view.*


@LayoutId(R.layout.holder_mock)
class MockViewHolder(view: View) : DataViewHolder<MockData>(view) {

  override fun updateViewByData(data: MockData) {
    super.updateViewByData(data)
    itemView.tv_title.text = data.title
    itemView.tv_content.text = data.content
  }
}