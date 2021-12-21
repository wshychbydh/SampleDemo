package com.cool.eye.func.mock

import com.google.gson.Gson

/**
 * Created by ycb on 6/15/21
 */
interface DataSourceProvider {

  fun <T> createMockWrapper(data: T): String {
    val wrapper = NetResult(data = data)
    return gson.toJson(wrapper)
  }

  fun <T> toJson(data: T): String = gson.toJson(data)

  private class NetResult<T>(
      val code: Int = 0,
      val message: String = "success",
      val data: T?,
  )

  companion object {
    private val gson = Gson()
  }
}