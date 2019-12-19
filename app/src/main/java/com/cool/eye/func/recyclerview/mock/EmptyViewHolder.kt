package com.cool.eye.func.recyclerview.mock

import android.view.View
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.DataViewHolder
import com.cool.eye.func.recyclerview.Empty
import com.cool.eye.func.recyclerview.LayoutId
import kotlinx.android.synthetic.main.empty_view_holder.view.*

/**
 * DutyWork
 * Created by cool on 2019/4/23.
 */

@LayoutId(R.layout.empty_view_holder)
class EmptyViewHolder(itemView: View) : DataViewHolder<Empty>(itemView) {
  override fun updateViewByData(data: Empty) {
    super.updateViewByData(data)
    if (data.resId > 0) {
      itemView.textView.setCompoundDrawablesWithIntrinsicBounds(0, data.resId, 0, 0)
    }
    if (!data.data.isNullOrEmpty()) {
      itemView.textView.text = data.data
    }
  }
}
