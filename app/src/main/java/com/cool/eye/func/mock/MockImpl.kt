package com.cool.eye.func.mock

import android.os.SystemClock
import com.cool.eye.func.mock.data.MockTraffic
import com.cool.eye.func.mock.data.MockWeather
import com.cool.eye.mock.MOCK
import retrofit2.http.GET

//以下定义了多个重复低模拟类，只会解析第一个，以反编译的declaredMethods顺序为准。
object MockImpl : DataSourceProvider {

  @GET("/weather")
  fun fetchWeather1(): String {
    SystemClock.sleep(500)
    val weather = MockWeather("北京1", "晴朗1")
    return toJson(weather)
  }

  @MOCK(
      method = "GET",
      url = "/weather",
      delay = 1000
  )
  fun fetchWeather2(): String {
    SystemClock.sleep(500)
    val weather = MockWeather("北京2", "小雨2")
    return toJson(weather)
  }

  @MOCK(
      method = "GET",
      url = "/traffic",
      delay = 500
  )
  fun fetchTraffic1(): String {
    SystemClock.sleep(500)
    val traffic = MockTraffic("上海1", "拥堵1")
    return toJson(traffic)
  }


  @MOCK(
      method = "GET",
      url = "/traffic",
      delay = 500
  )
  fun fetchTraffic2(): String {
    SystemClock.sleep(500)
    val traffic = MockTraffic("上海3", "拥堵3")
    return toJson(traffic)
  }

  @GET("/traffic")
  fun fetchTraffic3(): String {
    SystemClock.sleep(500)
    val traffic = MockTraffic("上海2", "畅通2")
    return toJson(traffic)
  }
}