package com.cool.eye.func.mock

import com.google.gson.Gson

/**
 * @Description: 模拟数据包装
 * @Author: 杨川博
 * @Date: 6/15/21
 * @License: Copyright Since 2020 Hive Box Technology. All rights reserved.
 * @Notice: This content is limited to the internal circulation of Hive Box, 
 * and it is prohibited to leak or used for other commercial purposes.
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