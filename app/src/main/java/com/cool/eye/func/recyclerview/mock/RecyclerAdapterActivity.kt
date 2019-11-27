package com.cool.eye.func.recyclerview.mock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.loadmore.DefaultLoadingViewHolder
import com.cool.eye.func.recyclerview.loadmore.DefaultNoDataViewHolder
import com.cool.eye.func.recyclerview.loadmore.LoadMore
import com.cool.eye.func.recyclerview.loadmore.LoadMoreAdatper
import kotlinx.android.synthetic.main.activity_recycler_adapter.*
import java.util.*
import kotlin.collections.ArrayList

class RecyclerAdapterActivity : AppCompatActivity(), androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener {

    private var adapter = LoadMoreAdatper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_adapter)
        recyclerView.adapter = adapter
        adapter.registerViewHolder(LoadMore::class.java, DefaultNoDataViewHolder::class.java)
        adapter.registerViewHolder(LoadMore::class.java, DefaultLoadingViewHolder::class.java)
        adapter.registerViewHolder(MockData::class.java, MockViewHolder::class.java)

        adapter.setDefaultCount(10)
        adapter.empowerLoadMoreAbility(recyclerView)
        adapter.setLoading(LoadMore("加载更多中..."))
        adapter.setNoData(LoadMore("没有更多数据"))
        adapter.setLoadMoreListener {
            onLoadMore()
        }

        refreshLayout.setOnRefreshListener(this)

        onRefresh()
    }

    override fun onRefresh() {
        adapter.updateData(mockData())
        refreshLayout.isRefreshing = false
    }


    private fun onLoadMore() {
        adapter.appendData(mockData())
    }


    private val random = Random()

    private fun mockData(): List<MockData> {
        val count = random.nextInt(6) + 5
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
