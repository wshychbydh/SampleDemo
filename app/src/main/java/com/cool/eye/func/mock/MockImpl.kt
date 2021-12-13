package com.cool.eye.func.mock

import android.os.SystemClock
import com.cool.eye.func.mock.data.MockTraffic
import com.cool.eye.func.mock.data.MockWeather
import retrofit2.http.GET

object MockImpl : DataSourceProvider {

  @GET("/weather")
  fun fetchWeather(): String {
    SystemClock.sleep(1000)
    val weather = MockWeather("北京", "小雨")
    return toJson(weather)
  }

  @GET("/traffic")
  fun fetchTraffic(): String {
    SystemClock.sleep(1000)
    val traffic = MockTraffic("北京", "拥堵")
    return toJson(traffic)
  }
}