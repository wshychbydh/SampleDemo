package com.cool.eye.func.paging

import androidx.paging.DataSource

class PageDataSourceFactory<T>(
    private val dateSource: DataSource<Int, T>
) : DataSource.Factory<Int, T>() {

  override fun create(): DataSource<Int, T> = dateSource
}