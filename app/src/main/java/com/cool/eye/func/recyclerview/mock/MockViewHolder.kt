package com.cool.eye.func.recyclerview.mock

import android.view.View
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.DataViewHolder
import com.cool.eye.func.recyclerview.LayoutId
import kotlinx.android.synthetic.main.holder_mock.view.*


@LayoutId(R.layout.holder_mock)
class MockViewHolder(view: View) : DataViewHolder<MockData>(view) {

    override fun updateViewByData(data: MockData) {
        super.updateViewByData(data)
        itemView.tv_title.text = data.title
        itemView.tv_content.text = data.content
    }
}