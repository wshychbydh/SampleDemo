package com.cool.eye.func.recyclerview

import android.view.View
import com.cool.eye.demo.R
import com.eye.cool.adapter.support.DataViewHolder
import com.eye.cool.adapter.support.Empty
import com.eye.cool.adapter.support.LayoutId
import kotlinx.android.synthetic.main.test_empty_view_holder.view.*

@LayoutId(R.layout.test_empty_view_holder)
class TestEmptyViewHolder(itemView: View) : DataViewHolder<Empty>(itemView) {
  override fun updateViewByData(data: Empty) {
    super.updateViewByData(data)
    if (data.drawable != null) {
      itemView.textView.setCompoundDrawablesWithIntrinsicBounds(null, data.drawable, null, null)
    }
    if (!data.text.isNullOrEmpty()) {
      itemView.textView.text = data.text
    }
  }
}
