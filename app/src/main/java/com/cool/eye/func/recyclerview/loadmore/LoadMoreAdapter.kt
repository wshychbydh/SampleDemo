package com.cool.eye.func.recyclerview.loadmore

import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.func.recyclerview.RecyclerAdapter

/**
 * Only for LinearLayout
 * Created by cool on 18/4/18.
 */
class LoadMoreAdapter : RecyclerAdapter() {

  companion object {
    const val STATUS_DEFAULT = 0
    const val STATUS_LOADING = 1
    const val STATUS_NO_DATA = 2
  }

  private var loadMoreListener: (() -> Unit)? = null
  private var noMoreData: Any? = null
  private var loadingData: Any? = null
  private var defaultCount = 10
  private var loadMoreAble = false
  private var status = STATUS_LOADING

  override fun doNotifyDataSetChanged() {
    data.remove(loadingData)
    data.remove(noMoreData)
    if (data.size < defaultCount) status = STATUS_DEFAULT
    when (status) {
      STATUS_LOADING -> {
        if (loadingData != null) {
          data.add(loadingData!!)
        }
      }
      STATUS_NO_DATA -> {
        if (noMoreData != null) {
          data.add(noMoreData!!)
        }
      }
    }
    super.doNotifyDataSetChanged()
  }

  fun setLoading(data: Any?) {
    this.loadingData = data
  }

  fun setNoData(data: Any?) {
    this.noMoreData = data
  }

  /**
   * Maximum data per request
   */
  fun setDefaultCount(defaultCount: Int) {
    this.defaultCount = defaultCount
  }

  override fun updateData(data: List<Any>?) {
    if (!data.isNullOrEmpty()) {
      status = STATUS_DEFAULT
    }
    enableLoadMoreData(data?.size ?: 0 >= defaultCount)
    super.updateData(data)
  }

  override fun appendData(data: List<Any>?) {
    if (data != null && data.size < defaultCount) {
      enableLoadMore(false)
    }
    enableLoadMoreData(data?.size ?: 0 >= defaultCount)
    super.appendData(data)
  }

  private fun enableLoadMoreData(enable: Boolean) {
    this.loadMoreAble = enable
    if (!loadMoreAble) {
      if (itemCount >= defaultCount) {
        status = STATUS_NO_DATA
      }
    }
  }

  fun enableLoadMore(enable: Boolean) {
    enableLoadMoreData(enable)
    doNotifyDataSetChanged()
  }

  fun setLoadMoreListener(listener: (() -> Unit)?) {
    this.loadMoreListener = listener
  }

  fun empowerLoadMoreAbility(recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!loadMoreAble) return
        if (itemCount < defaultCount) return
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
          //   val lastItemPosition = layoutManager.findLastVisibleItemPosition()
          val lastItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
          if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == itemCount) {
            status = STATUS_LOADING
            doNotifyDataSetChanged()
            loadMoreListener?.invoke()
          }
        }
      }
    })
  }
}
