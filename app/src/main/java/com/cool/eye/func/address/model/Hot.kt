package com.cool.eye.func.address.model

import com.eye.cool.book.support.IQuickProvider

/**
 *Created by ycb on 2020/1/8 0008
 */

data class Hot(
    private val key: String,
    val content: String
) : IQuickProvider {

  override fun getKey(): String {
    return key
  }

  override fun getSearchKey(): String {
    return content
  }
}