package com.cool.eye.func.banner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.func.R
import com.eye.cool.banner.CarouselParams
import kotlinx.android.synthetic.main.activity_banner.*

/**
 *Created by ycb on 2019/11/14 0014
 */
class BannerActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_banner)
    viewPager.adapter = BannerPagerAdapter()
    indicator.alwaysShownWhenOnlyOne(false)
    viewPager.params = CarouselParams.Builder()
        .setIndicator(indicator)
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
}