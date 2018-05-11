package com.cool.eye.func.address

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.cool.eye.func.R
import kotlinx.android.synthetic.main.quick_view.view.*

/**
 * Created by cool on 2018/4/19.
 */
class QuickView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val recyclerView: RecyclerView
    private val quickBar: QuickBar
    private val layoutManager: LinearLayoutManager
    val itemDecoration: StickyItemDecoration
    var adapter: RecyclerAdapter
        private set

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.quick_view, this, true)
        recyclerView = view.recyclerView

        //设置布局管理器
        layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter()
        recyclerView.adapter = adapter

        quickBar = view.quickBar
        quickBar.toastView = view.toastView
        quickBar.onLetterChangedListener = {
            layoutManager.scrollToPositionWithOffset(getGroupPosition(it), 0)
        }

        itemDecoration = StickyItemDecoration(context, Color.GRAY, 1f, 1f)
        itemDecoration.isShowFirstGroup = false
        itemDecoration.setTitleHeight(40f)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.setHasFixedSize(true)
    }

    private fun getGroupPosition(s: String): Int {
        val size = itemDecoration.keys.size()
        for (i in 0 until size) {
            if (itemDecoration.keys.valueAt(i) == s) return itemDecoration.keys.keyAt(i)
        }
        return 0
    }
}