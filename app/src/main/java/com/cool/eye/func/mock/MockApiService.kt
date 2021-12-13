package com.cool.eye.func.mock

import com.cool.eye.func.mock.data.MockTraffic
import com.cool.eye.func.mock.data.MockWeather
import retrofit2.http.GET

interface MockApiService {

  @GET("/weather")
  suspend fun fetchWeather(): MockWeather

  @GET("/traffic")
  suspend fun fetchTraffic(): MockTraffic
}