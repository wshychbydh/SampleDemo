package com.cool.eye.func.mock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.cool.eye.demo.R
import com.cool.eye.mock.MockHelper
import com.cool.eye.mock.MockInterceptor
import kotlinx.android.synthetic.main.activity_mock.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MockActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_mock)
    MockHelper.release();
    lifecycleScope.launch(Dispatchers.IO) {
      MockHelper.addMockDataSource(MockImpl)
      MockHelper.enableMock(true)
      val retrofit = createRetrofit()
      val api = retrofit.create(MockApiService::class.java)
      val weather = api.fetchWeather()
      val traffic = api.fetchTraffic()

      withContext(Dispatchers.Main) {
        trafficTv.text = traffic.toString()
        weatherTv.text = weather.toString()
        progressBar.visibility = View.GONE
      }
    }
  }

  private fun createRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://mock/api/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient().newBuilder()
                .addInterceptor(MockInterceptor())
                .build()
        )
        .build()
  }
}