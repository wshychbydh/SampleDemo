package com.cool.eye.func.recyclerview.loadmore

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.DataViewHolder
import com.cool.eye.func.recyclerview.LayoutId
import kotlinx.android.synthetic.main.holder_loading.view.*


@LayoutId(R.layout.holder_loading)
class DefaultLoadingViewHolder(view: View) : DataViewHolder<LoadMore>(view) {

    override fun updateViewByData(data: LoadMore) {
        super.updateViewByData(data)
        itemView.tv_loading.text = data.data
        val anim = ObjectAnimator.ofFloat(itemView.iv_loading, "rotationY", 0.0f, 359.0f)
        anim.repeatCount = -1
        anim.duration = 1000
        anim.interpolator = LinearInterpolator()
        anim.start()
    }
}