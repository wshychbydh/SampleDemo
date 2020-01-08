package com.cool.eye.func.recyclerview

import android.view.View
import com.cool.eye.demo.R
import com.eye.cool.adapter.support.DataViewHolder
import com.eye.cool.adapter.support.Empty
import com.eye.cool.adapter.support.LayoutId
import kotlinx.android.synthetic.main.empty_view_holder.view.*

/**
 * DutyWork
 * Created by cool on 2019/4/23.
 */

@LayoutId(R.layout.empty_view_holder)
class EmptyViewHolder(itemView: View) : DataViewHolder<Empty>(itemView) {
  override fun updateViewByData(data: Empty) {
    super.updateViewByData(data)
    if (data.drawableResId > 0) {
      itemView.textView.setCompoundDrawablesWithIntrinsicBounds(0, data.drawableResId, 0, 0)
    }
    if (!data.text.isNullOrEmpty()) {
      itemView.textView.text = data.text
    }
  }
}
