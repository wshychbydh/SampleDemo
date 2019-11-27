package com.cool.eye.func.address.mvp.view

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.address.DataViewHolder
import com.cool.eye.func.address.LayoutId
import com.cool.eye.func.address.QuickView
import com.cool.eye.func.address.SearchHelper
import com.cool.eye.func.address.mvp.persenter.AddressPresenter
import kotlinx.android.synthetic.main.activity_address.*

class AddressActivity : AppCompatActivity(), IAddressView<String> {

  private lateinit var quickView: QuickView
  private lateinit var searchHelper: SearchHelper
  private lateinit var addressPresenter: AddressPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_address)
    quickView = findViewById(R.id.quickView)
    quickView.adapter.registerViewHolder(Integer::class.java, HeaderViewHolder::class.java)
    quickView.adapter.registerViewHolder(String::class.java, MyViewHolder::class.java)
    searchView.isIconified = false
    searchHelper = SearchHelper.Builder()
        .setSearchView(searchView)
        .setOnQueryTextListener {
          addressPresenter.search(it)
        }
        .setItemClickListener {
          Toast.makeText(this@AddressActivity, it, Toast.LENGTH_SHORT).show()
        }
        .build(this)
    addressPresenter = AddressPresenter(this)
    addressPresenter.start()
  }

  override fun showAddress(data: List<String>) {
    quickView.adapter.updateData(0)
    quickView.adapter.appendData(data)
  }

  override fun groupAddress(group: SparseArray<String>) {
    quickView.itemDecoration.keys = group
  }

  @LayoutId(R.layout.item)
  class MyViewHolder(itemView: View) : DataViewHolder<String>(itemView) {
    private var tv: TextView = itemView.findViewById(R.id.tv)

    override fun updateViewByData(data: String) {
      super.updateViewByData(data)
      tv.text = data
    }
  }

  @LayoutId(R.layout.header)
  class HeaderViewHolder(itemView: View) : DataViewHolder<Int>(itemView) {

    override fun updateViewByData(data: Int) {
      super.updateViewByData(data)
    }
  }

}
