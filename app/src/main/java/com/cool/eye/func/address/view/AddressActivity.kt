package com.cool.eye.func.address.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.address.model.Address
import com.cool.eye.func.address.model.DataHelper
import com.cool.eye.func.address.model.Hot
import com.cool.eye.func.address.model.SearchHelper
import com.eye.cool.book.adapter.DataViewHolder
import com.eye.cool.book.adapter.LayoutId
import com.eye.cool.book.params.QuickDataParams
import com.eye.cool.book.params.QuickViewParams
import com.eye.cool.book.support.IQuickProvider
import com.eye.cool.book.view.QuickView
import kotlinx.android.synthetic.main.activity_address.*

class AddressActivity : AppCompatActivity() {

  private lateinit var quickView: QuickView
  private lateinit var searchHelper: SearchHelper

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_address)
    backBtn.setOnClickListener { finish() }
    quickView = findViewById(R.id.quickView)
    searchView.isIconified = false
    searchHelper = SearchHelper.Builder()
        .setSearchView(searchView)
        .setOnQueryTextListener {
          quickView.search(it)
        }
        .setItemClickListener {
          val content = if (it is Hot) it.content else if (it is Address) it.content else it.getKey()
          Toast.makeText(this@AddressActivity, content, Toast.LENGTH_SHORT).show()
        }
        .build(this)
    setup()
  }

  private fun setup() {
    quickView.setup(QuickViewParams.Builder(
        QuickDataParams.build {
          data = DataHelper.data
          registerViewHolder(Hot::class.java, HotViewHolder::class.java)
          registerViewHolder(Address::class.java, AddressViewHolder::class.java)
        }
    ).build())
  }

  fun search(query: String): List<IQuickProvider> {
    val list = DataHelper.data
    val queryList = mutableListOf<IQuickProvider>()
    list.values.forEach { list ->
      val result = list.filter {
        (it is Hot && it.content.contains(query))
            || (it is Address && it.content.contains(query))
            || (it.getKey().contains(query))
      }
      queryList.addAll(result)
    }
    return queryList
  }

  @LayoutId(R.layout.item)
  class AddressViewHolder(itemView: View) : DataViewHolder<Address>(itemView) {
    private var tv: TextView = itemView.findViewById(R.id.tv)

    override fun updateViewByData(data: Address) {
      super.updateViewByData(data)
      tv.text = data.content
    }
  }

  @LayoutId(R.layout.header)
  class HotViewHolder(itemView: View) : DataViewHolder<Hot>(itemView) {
    override fun updateViewByData(data: Hot) {
      super.updateViewByData(data)
    }
  }
}
