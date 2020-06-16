package com.cool.eye.func.paging

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

class PageDataSourceFactory<T>(
    private val loader: PagingAdapter.IDataLoader<T>
) : DataSource.Factory<Int, T>() {

  override fun create(): DataSource<Int, T> = PageDataSource(loader)

  class PageDataSource<T>(
      val loader: PagingAdapter.IDataLoader<T>,
      val initPage: Int = 1
  ) : PageKeyedDataSource<Int, T>() {

    override inline fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
      val data = loader.loadData(initPage, params.requestedLoadSize)
      data?.let {
        callback.onResult(it, null, initPage + 1)
      }
    }

    override inline fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

      val data =
          loader.loadData(params.key + 1, params.requestedLoadSize)
      data?.let {
        callback.onResult(it, params.key + 1)
      }
    }

    override inline fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

      val data = loader.loadData(params.key - 1, params.requestedLoadSize)
      data?.let {
        callback.onResult(it, params.key - 1)
      }
    }
  }
}