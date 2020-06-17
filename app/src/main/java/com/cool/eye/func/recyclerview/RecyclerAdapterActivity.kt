package com.cool.eye.func.recyclerview

import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cool.eye.demo.R
import com.eye.cool.adapter.loadmore.*
import com.eye.cool.adapter.support.Loading
import kotlinx.android.synthetic.main.activity_recycler_adapter.*
import kotlin.random.Random

class RecyclerAdapterActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

  private lateinit var adapter: LoadMoreAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recycler_adapter)

    val drawable = ContextCompat.getDrawable(recyclerView.context, R.drawable.divider_gray)
    if (drawable != null) {
      val divider = DividerItemDecoration(recyclerView.context, LinearLayout.VERTICAL)
      divider.setDrawable(drawable)
      recyclerView.addItemDecoration(divider)
    }

    adapter = LoadMoreAdapter.Builder(recyclerView)
        .registerViewHolder(MockData::class.java, MockViewHolder::class.java)
        .showNoMoreData(true)
        .showNoMoreStatusAlways(true)
        .showLoadMore(true)
        .setDefaultCount(5)
        .setLoadMoreListener(object : ILoadMoreListener {
          override fun onLoadMore() {
            handler.postDelayed({
              adapter.appendData(mockData())
            }, 1000)
          }
        })
        .build()

    refreshLayout.setOnRefreshListener(this)

    adapter.updateData(Loading())

    onRefresh()
  }

  override fun onRefresh() {
    handler.postDelayed({
      adapter.updateData(mockData())
      refreshLayout.isRefreshing = false
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
