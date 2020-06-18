package com.cool.eye.func.recyclerview

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.cool.eye.demo.R
import com.cool.eye.func.dialog.loading.LoadingView
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.adapter.loadmore.ILoadMoreListener
import com.eye.cool.adapter.loadmore.LoadMoreAdapter
import com.eye.cool.adapter.support.DefaultEmptyViewHolder
import com.eye.cool.adapter.support.DefaultLoadingViewHolder
import com.eye.cool.adapter.support.Empty
import com.eye.cool.adapter.support.Loading
import kotlinx.android.synthetic.main.activity_recycler_adapter.*
import kotlin.random.Random

class RecyclerAdapterActivity : AppCompatActivity() {

  private lateinit var adapter1: LoadMoreAdapter
  private lateinit var adapter2: LoadMoreAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recycler_adapter)

    val drawable = ContextCompat.getDrawable(this, R.drawable.divider_gray)
    if (drawable != null) {
      val divider = DividerItemDecoration(this, LinearLayout.VERTICAL)
      divider.setDrawable(drawable)
      recyclerView1.addItemDecoration(divider)
      recyclerView2.addItemDecoration(divider)
    }

    adapter1 = LoadMoreAdapter.Builder(recyclerView1)
        .registerViewHolder(MockData::class.java, MockViewHolder::class.java)
        .setLoadMoreListener(object : ILoadMoreListener {
          override fun onLoadMore() {
            handler.postDelayed({
              adapter1.appendData(mockData())
            }, 1000)
          }
        })
        .build()

    adapter2 = LoadMoreAdapter.Builder(recyclerView2)
        .registerViewHolder(MockData::class.java, MockViewHolder::class.java)
        .replaceEmptyViewHolder(
            Empty(text = "替换的空视图"),
            DefaultEmptyViewHolder::class.java
        )
        .replaceLoadingViewHolder(
            Loading(
                LoadingView(this)
            ),
            DefaultLoadingViewHolder::class.java
        )
        .setLoadMoreListener(object : ILoadMoreListener {
          override fun onLoadMore() {
            handler.postDelayed({
              adapter2.appendData(mockData())
            }, 1000)
          }
        })
        .setOnClickListener(View.OnClickListener {
          if (adapter2.isEmptyClicked(it)) {
            ToastHelper.showToast(this, "空视图被点击了")
          }
        })
        .build()

    refreshLayout1.setOnRefreshListener {
      refresh1()
    }

    refreshLayout2.setOnRefreshListener {
      refresh2()
    }

    adapter1.showEmpty()
    refresh1()

    adapter2.showLoading()
    refresh2()
  }

  private fun refresh1() {
    handler.postDelayed({
      adapter1.updateData(mockData())
      refreshLayout1.isRefreshing = false
    }, 1000)
  }

  private fun refresh2() {
    handler.postDelayed({
      adapter2.updateData(mockData())
      refreshLayout2.isRefreshing = false
    }, 1000)
  }

  private val handler = Handler()

  private fun mockData(): List<MockData> {
    val count = Random.nextInt(6) //无穷加载
    val mockData = ArrayList<MockData>()
    (0 until count).forEach {
      val data = MockData()
      data.title = "title$it"
      data.content = "content$it"
      mockData.add(data)
    }
    return mockData
  }
}