package com.cool.eye.func.recyclerview.mock

import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.Empty
import com.cool.eye.func.recyclerview.RecyclerAdapter
import com.cool.eye.func.recyclerview.loadmore.*

/**
 *Created by cool on 2018/6/29
 */
object RecyclerHelper {

  @JvmStatic
  fun configRecyclerAdapter(recyclerView: RecyclerView, showDivider: Boolean = false): RecyclerAdapter {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    if (showDivider) {
      val drawable = ContextCompat.getDrawable(recyclerView.context, R.drawable.divider_gray)
      if (drawable != null) {
        val divider = DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL)
        divider.setDrawable(drawable)
        recyclerView.addItemDecoration(divider)
      }
    }
    val adapter = RecyclerAdapter()
    adapter.registerViewHolder(Empty::class.java, EmptyViewHolder::class.java)
    recyclerView.adapter = adapter
    return adapter
  }

  @JvmStatic
  fun configLoadMoreAdapter(recyclerView: RecyclerView, showDivider: Boolean = false): LoadMoreAdapter {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    if (showDivider) {
      val divider = DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL)
      divider.setDrawable(recyclerView.resources.getDrawable(R.drawable.divider_gray))
      recyclerView.addItemDecoration(divider)
    }
    val adapter = LoadMoreAdapter()
    adapter.registerViewHolder(Empty::class.java, EmptyViewHolder::class.java)
    adapter.registerViewHolder(NoMoreData::class.java, DefaultNoMoreDataViewHolder::class.java)
    adapter.registerViewHolder(Loading::class.java, DefaultLoadingViewHolder::class.java)
    adapter.empowerLoadMoreAbility(recyclerView)
    recyclerView.adapter = adapter
    return adapter
  }

//  @JvmStatic
//  fun configLoadMoreGridAdapter(recyclerView: RecyclerView, spanCount: Int): LoadMoreAdapter {
//    recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spanCount)
//    if (recyclerView.itemDecorationCount > 0) {
//      val itemDecoration = recyclerView.getItemDecorationAt(0)
//      if (itemDecoration is SpaceItemDecoration) {
//        recyclerView.removeItemDecoration(itemDecoration)
//      }
//    }
//    val space = 12 * recyclerView.context.resources.displayMetrics.density
//    recyclerView.addItemDecoration(DividerItemDecoration())
//    val adapter = LoadMoreAdapter()
//    adapter.registerViewHolder(LoadMore::class.java, LoadMoreViewHolder::class.java)
//    adapter.setLoadMore(LoadMore(recyclerView.context.getString(R.string.no_more_data)))
//    adapter.empowerLoadMoreAbility(recyclerView)
//    recyclerView.adapter = adapter
//    return adapter
//  }
//
//  @JvmStatic
//  fun configGridAdapter(recyclerView: RecyclerView, spanCount: Int, defaultSpace: Int = 12): RecyclerAdapter {
//    recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spanCount)
//    if (recyclerView.itemDecorationCount > 0) {
//      val itemDecoration = recyclerView.getItemDecorationAt(0)
//      if (itemDecoration is SpaceItemDecoration) {
//        recyclerView.removeItemDecoration(itemDecoration)
//      }
//    }
//    val space = defaultSpace * recyclerView.context.resources.displayMetrics.density
//    recyclerView.addItemDecoration(SpaceItemDecoration(space.toInt()))
//    val adapter = RecyclerAdapter()
//    recyclerView.adapter = adapter
//    return adapter
//  }
}