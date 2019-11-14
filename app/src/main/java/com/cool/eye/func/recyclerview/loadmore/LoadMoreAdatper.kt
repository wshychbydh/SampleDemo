package com.cool.eye.func.recyclerview.loadmore

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.func.recyclerview.BaseAdapter

/**
 * Created by cool on 18/4/18.
 */
class LoadMoreAdatper : BaseAdapter() {

    companion object {
        const val STATUS_DEFAULT = 0
        const val STATUS_LOADING = 1
        const val STATUS_NO_DATA = 2
    }

    private var loadMoreListener: (() -> Unit)? = null
    private var noData: Any? = null
    private var loadingData: Any? = null
    private var defaultCount = 10
    private var loadMoreAble = false
    private var status = STATUS_LOADING

    override fun notifyData() {
        data.remove(loadingData)
        data.remove(noData)
        if (data.size < defaultCount) status = STATUS_DEFAULT
        when (status) {
            STATUS_LOADING -> {
                if (loadingData != null) {
                    data.add(loadingData!!)
                }
            }
            STATUS_NO_DATA -> {
                if (noData != null) {
                    data.add(noData!!)
                }
            }
        }
        super.notifyData()
    }

    fun setLoading(data: Any?) {
        this.loadingData = data
    }

    fun setNoData(data: Any?) {
        this.noData = data
    }

    /**
     * Maximum data per request
     */
    fun setDefaultCount(defaultCount: Int) {
        this.defaultCount = defaultCount
    }

    override fun updateData(data: List<Any>?) {
        if (data != null) {
            status = STATUS_DEFAULT
        }
        super.updateData(data)
    }

    override fun appendData(data: List<Any>?) {
        if (data != null && data.size < defaultCount) {
            enableLoadMore(false)
        }
        super.appendData(data)
    }

    fun enableLoadMore(loadMoreAble: Boolean) {
        this.loadMoreAble = loadMoreAble
        if (!loadMoreAble) {
            if (itemCount >= defaultCount) {
                status = STATUS_NO_DATA
            }
        }
        notifyData()
    }

    fun setLoadMoreListener(listener: (() -> Unit)?) {
        this.loadMoreListener = listener
    }

    fun empowerLoadMoreAbility(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!loadMoreAble) return
                if (itemCount < defaultCount) return
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
                    //   val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                    val lastItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == itemCount) {
                        status = STATUS_LOADING
                        notifyData()
                        loadMoreListener?.invoke()
                    }
                }
            }
        })
    }
}
