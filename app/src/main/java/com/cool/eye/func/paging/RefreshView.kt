package com.cool.eye.func.paging

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.common_refresh_view.view.*

/**
 *Created by ycb on 2020/4/21 0021
 */
class RefreshView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

  private val commonRefreshLayout: SwipeRefreshLayout
  private val commonRecyclerView: RecyclerView

  init {
    val view = LayoutInflater.from(context).inflate(R.layout.common_refresh_view, this, true)
    commonRefreshLayout = view.commonRefreshLayout
    commonRecyclerView = view.commonRecyclerView
  }

  fun getSwipeRefreshLayout(): SwipeRefreshLayout {
    return commonRefreshLayout
  }

  fun getRecyclerView(): RecyclerView {
    return commonRecyclerView
  }
}