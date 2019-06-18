package com.cool.eye.func.address

import android.content.Context
import android.support.v7.widget.ListPopupWindow
import android.support.v7.widget.SearchView
import android.widget.ArrayAdapter
import android.widget.Toast
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


/**
 * Created by cool on 2018/4/20.
 */
class SearchHelper private constructor() {

  private lateinit var searchView: SearchView
  private lateinit var context: Context
  private lateinit var itemClickListener: (String) -> Unit
  private lateinit var onQueryTextListener: ((text: String) -> List<String>)
  private lateinit var popupWindow: ListPopupWindow
  private lateinit var queryList: MutableList<String>

  private fun initAll() {
    queryList = Collections.synchronizedList(ArrayList<String>())
    popupWindow = ListPopupWindow(context)
    popupWindow.setAdapter(ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, queryList))
    popupWindow.anchorView = searchView
    popupWindow.isModal = true
    popupWindow.setOnItemClickListener { _, _, position, _ ->
      popupWindow.dismiss()
      itemClickListener.invoke(queryList[position])
    }
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()) {
          if (popupWindow.isShowing) {
            popupWindow.dismiss()
          }
        } else {
          executor.execute {
            val result = onQueryTextListener.invoke(newText!!)
            queryList.clear()
            if (result.isNotEmpty()) {
              queryList.addAll(result)
            }
            searchView.post { refreshQueryView(queryList.isNotEmpty()) }
          }
        }
        return false
      }
    })
  }

  private fun refreshQueryView(show: Boolean) {
    Toast.makeText(context, "Find ${queryList.size}条数据", Toast.LENGTH_SHORT).show()
    if (show) {
      if (!popupWindow.isShowing) {
        popupWindow.show()
      }
    } else {
      if (popupWindow.isShowing) {
        popupWindow.dismiss()
      }
    }
  }

  class Builder {
    private var itemClickListener: ((String) -> Unit)? = null
    private var onQueryTextListener: ((text: String) -> List<String>)? = null
    private var searchView: SearchView? = null

    fun setItemClickListener(listener: (String) -> Unit): Builder {
      itemClickListener = listener
      return this
    }

    fun setOnQueryTextListener(listener: ((text: String) -> List<String>)): Builder {
      onQueryTextListener = listener
      return this
    }

    fun setSearchView(searchView: SearchView): Builder {
      this.searchView = searchView
      return this
    }

    fun build(context: Context): SearchHelper {
      if (searchView == null) {
        throw IllegalArgumentException("You should SearchView can not be null.")
      }
      val helper = SearchHelper()
      helper.context = context
      helper.searchView = searchView!!
      helper.itemClickListener = itemClickListener!!
      helper.onQueryTextListener = onQueryTextListener!!
      helper.initAll()
      return helper
    }
  }

  private companion object {
    private val executor = Executors.newSingleThreadExecutor()
  }
}