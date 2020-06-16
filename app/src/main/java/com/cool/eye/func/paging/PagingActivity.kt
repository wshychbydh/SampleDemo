package com.cool.eye.func.paging

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.recyclerview.MockData
import com.eye.cool.adapter.support.DataViewHolder
import com.eye.cool.adapter.support.LayoutId
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.android.synthetic.main.holder_mock.view.*
import kotlin.random.Random

/**
 * Created by ycb on 2020/6/16 0016
 */
class PagingActivity : AppCompatActivity(), PagingAdapter.IDataLoader<MockData> {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_paging)

    val adapter = PagingAdapter<MockData>()
    adapter.attachToRefreshView(refreshView)
    adapter.registerDataViewHolder(MockData::class.java, PagingViewHolder::class.java)
    adapter.setDataLoader(this, this)
  }

  override fun loadData(page: Int, pageSize: Int): List<MockData>? {
    val data = arrayListOf<MockData>()
    val count = (page - 1) * pageSize + Random.nextInt(pageSize)
    SystemClock.sleep(1000)
    (0 until count).forEach {
      val item = MockData()
      item.title = "第${page}页"
      item.content = "第${it}项"
      data.add(item)
    }
    return data
  }

  @LayoutId(R.layout.holder_mock)
  class PagingViewHolder(view: View) : DataViewHolder<MockData>(view) {

    override fun updateViewByData(data: MockData) {
      super.updateViewByData(data)
      itemView.tv_title.text = data.title
      itemView.tv_content.text = data.content
    }
  }
}