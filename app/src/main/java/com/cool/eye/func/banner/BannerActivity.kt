package com.cool.eye.func.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.banner.CarouselParams
import kotlinx.android.synthetic.main.activity_banner.*
import kotlin.random.Random

/**
 *Created by ycb on 2019/11/14 0014
 */
class BannerActivity : AppCompatActivity() {

  private val pages = mutableListOf<Any>(1)
  private val adapter = BannerPagerAdapter(pages)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_banner)
    viewPager.adapter = adapter
    indicator.alwaysShownWhenOnlyOne(false)
    viewPager.params = CarouselParams.Builder()
        .setIndicator(indicator)
        .setScrollAble(true)
        .setInterval(3000)
        .setScrollDuration(3000)
        .setReversible(true)
        .setRecyclable(false)
        .setAutoCarousel(false)
        .enableCarousel(true)
        .setDirection(CarouselParams.LEFT_TO_RIGHT)
        .setScrollWhenOne(false)
        .build()
  }

  override fun onResume() {
    super.onResume()
    viewPager.start()
  }

  override fun onPause() {
    super.onPause()
    viewPager.stop()
  }

  fun changePagerNum(v: View) {
    pages.clear()
    val size = Random.nextInt(5)
    (0..size).forEach {
      pages.add(it)
    }
    adapter.notifyDataSetChanged()
  }
}