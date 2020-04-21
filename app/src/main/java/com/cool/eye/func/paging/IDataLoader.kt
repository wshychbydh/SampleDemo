package com.cool.eye.func.paging

/**
 *Created by ycb on 2020/3/27 0027
 */
interface IDataLoader<T> {
  fun loadData(page: Int, pageSize: Int): List<T>?
}