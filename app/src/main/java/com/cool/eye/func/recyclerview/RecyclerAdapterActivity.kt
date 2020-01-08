package com.cool.eye.func.recyclerview

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cool.eye.demo.R
import com.eye.cool.adapter.loadmore.ILoadMoreListener
import com.eye.cool.adapter.loadmore.LoadMoreAdapter
import com.eye.cool.adapter.loadmore.Loading
import com.eye.cool.adapter.loadmore.NoMoreData
import kotlinx.android.synthetic.main.activity_recycler_adapter.*
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapterActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

  private lateinit var adapter: LoadMoreAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recycler_adapter)
    adapter = RecyclerHelper.configLoadMoreAdapter(recyclerView, true)
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.adapter = adapter
    adapter.registerViewHolder(MockData::class.java, MockViewHolder::class.java)

    adapter.setDefaultCount(10)
    adapter.setLoading(Loading("加载更多中..."))
    adapter.setNoData(NoMoreData("没有更多数据"))
    adapter.setLoadMoreListener(object : ILoadMoreListener {
      override fun onLoadMore() {
        handler.postDelayed({
          adapter.appendData(mockData())
        }, 2000)
      }
    })

    refreshLayout.setOnRefreshListener(this)

    onRefresh()
  }

  override fun onRefresh() {
    refreshLayout.isRefreshing = true
    handler.postDelayed({
      adapter.updateData(mockData())
      refreshLayout.isRefreshing = false
    }, 2000)
  }

  private val handler = Handler()

  private val random = Random()

  private fun mockData(): List<MockData> {
    val count = random.nextInt(10) + 5
    val mockData = ArrayList<MockData>()
    (0..count).forEach {
      val data = MockData()
      data.title = "title$it"
      data.content = "content$it"
      mockData.add(data)
    }
    return mockData
  }
}
