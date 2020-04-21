package com.cool.eye.func.paging

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.EmptyViewHolder
import com.eye.cool.adapter.paging.StatePageAdapter
import com.eye.cool.adapter.support.Empty
import kotlinx.android.synthetic.main.common_refresh_view.view.*

/**
 *Created by ycb on 2020/4/21 0021
 */
class RefreshView<T> : SwipeRefreshLayout.OnRefreshListener, Observer<PagedList<T>> {

  private val adapter = StatePageAdapter<T>()

  private val owner: LifecycleOwner

  private val dataLoader: IDataLoader<T>

  private lateinit var commonRefreshLayout: SwipeRefreshLayout

  constructor(fragment: Fragment, container: ViewGroup, dataLoader: IDataLoader<T>) {
    owner = fragment.viewLifecycleOwner
    this.dataLoader = dataLoader
    init(container)
  }

  constructor(activity: AppCompatActivity, container: ViewGroup, dataLoader: IDataLoader<T>) {
    owner = activity
    this.dataLoader = dataLoader
    init(container)
  }

  private fun init(container: ViewGroup) {
    val view = LayoutInflater.from(container.context).inflate(R.layout.common_refresh_view, container, true)
    commonRefreshLayout = view.commonRefreshLayout
    commonRefreshLayout.setOnRefreshListener(this)
    view.commonRecyclerView.layoutManager = WrappedLinearLayoutManager(container.context)
    adapter.registerStateViewHolder(Empty::class.java, EmptyViewHolder::class.java)
    adapter.setOnClickListener(OnClickListener {
      commonRefreshLayout.isRefreshing = true
      createDataProvider().observe(owner, this)
    })
  }

  fun getAdapter(): StatePageAdapter<T> = adapter

  override fun onRefresh() {
    commonRefreshLayout.isRefreshing = true
    createDataProvider().observe(owner, this)
  }

  override fun onChanged(it: PagedList<T>?) {
    commonRefreshLayout.isRefreshing = false
    if (it.isNullOrEmpty()) {
      adapter.submitStatus(Empty())
    } else {
      adapter.submitList(it)
    }
  }

  private fun createDataProvider(): LiveData<PagedList<T>> {
    return LivePagedListBuilder(
        PageDataSourceFactory(PageDataSource(dataLoader)),
        PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(20)
            .build()
    ).build()
  }
}