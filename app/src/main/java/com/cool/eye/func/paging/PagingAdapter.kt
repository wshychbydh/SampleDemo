package com.cool.eye.func.paging

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cool.eye.func.recyclerview.EmptyViewHolder
import com.eye.cool.adapter.paging.StateLinearLayoutManager
import com.eye.cool.adapter.paging.StatePageAdapter
import com.eye.cool.adapter.support.Empty

/**
 * Created by ycb on 2020/6/16 0016
 */
class PagingAdapter<T> : StatePageAdapter<T>(), LifecycleObserver, SwipeRefreshLayout.OnRefreshListener, Observer<PagedList<T>> {

  private var refreshLayout: SwipeRefreshLayout? = null
  private var dataLoader: IDataLoader<T>? = null
  private var lifecycleOwner: LifecycleOwner? = null

  fun attachToRefreshView(refreshView: RefreshView) {
    refreshLayout = refreshView.getSwipeRefreshLayout()
    val recyclerView = refreshView.getRecyclerView()
    recyclerView.layoutManager = StateLinearLayoutManager(recyclerView.context)
    registerStateViewHolder(Empty::class.java, EmptyViewHolder::class.java)
    refreshLayout!!.setOnRefreshListener(this)
    recyclerView.adapter = this
  }

  fun setDataLoader(lifecycleOwner: LifecycleOwner, dataLoader: IDataLoader<T>) {
    this.dataLoader = dataLoader
    this.lifecycleOwner = lifecycleOwner
    onRefresh()
  }

  override fun onRefresh() {
    createDataProvider()?.observe(lifecycleOwner ?: return, this)
  }

  override fun onChanged(it: PagedList<T>?) {
    refreshLayout?.isRefreshing = false
    if (it.isNullOrEmpty()) {
      submitStatus(Empty())
    } else {
      submitList(it)
    }
  }

  private fun createDataProvider(): LiveData<PagedList<T>>? {
    return LivePagedListBuilder(
        PageDataSourceFactory(dataLoader ?: return null),
        PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .build()
    ).build()
  }

  interface IDataLoader<T> {
    fun loadData(page: Int, pageSize: Int): List<T>?
  }
}