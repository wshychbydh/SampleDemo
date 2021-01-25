package com.cool.eye.func.paging2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.paging_activity.*

/**
 * Created by ycb on 2020/11/6 0016
 */
class Paging2Activity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.paging_activity)
    init()
  }

  private fun init() {

    val header = HeaderAdapter()
    val footer = HeaderAdapter()
    val data = DataAdapter()

    val concatAdapter = ConcatAdapter(header, data, footer)
    pagingRv.layoutManager = LinearLayoutManager(this)
    pagingRv.adapter = concatAdapter

  }
}