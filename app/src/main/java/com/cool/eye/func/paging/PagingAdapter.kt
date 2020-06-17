package com.cool.eye.func.paging

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.eye.cool.adapter.paging.DefaultDataSourceFactory
import com.eye.cool.adapter.paging.StateLinearLayoutManager
import com.eye.cool.adapter.paging.StatePagingAdapter
import com.eye.cool.adapter.support.Loading

/**
 * Created by ycb on 2020/6/16 0016
 */
class PagingAdapter<T> : StatePagingAdapter<T>(), LifecycleObserver, SwipeRefreshLayout.OnRefreshListener, Observer<PagedList<T>> {

  private var refreshLayout: SwipeRefreshLayout? = null
  private var dataLoader: DefaultDataSourceFactory.IDataLoader<T>? = null
  private var lifecycleOwner: LifecycleOwner? = null

  fun attachToRefreshView(refreshView: RefreshView) {
    refreshLayout = refreshView.getSwipeRefreshLayout()
    val recyclerView = refreshView.getRecyclerView()
    recyclerView.layoutManager = StateLinearLayoutManager(recyclerView.context)
    refreshLayout!!.setOnRefreshListener(this)
    recyclerView.adapter = this
  }

  fun setDataLoader(lifecycleOwner: LifecycleOwner, dataLoader: DefaultDataSourceFactory.IDataLoader<T>) {
    this.dataLoader = dataLoader
    this.lifecycleOwner = lifecycleOwner
    submitStatus(Loading())
    onRefresh()
  }

  override fun onRefresh() {
    createDataProvider()?.observe(lifecycleOwner ?: return, this)
  }

  override fun onChanged(it: PagedList<T>?) {
    refreshLayout?.isRefreshing = false
    submitList(it)
  }

  private fun createDataProvider(): LiveData<PagedList<T>>? {
    return LivePagedListBuilder(
        DefaultDataSourceFactory(1, dataLoader ?: return null),
        PagedList.Config.Builder()
            .setPageSize(5)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(5)
            .build()
    ).build()
  }
}