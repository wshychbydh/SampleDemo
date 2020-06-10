package com.cool.eye.func.banner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cool.eye.demo.R
import com.cool.eye.func.banner.snap.Gallery
import com.cool.eye.func.banner.snap.GalleryParams
import com.cool.eye.func.banner.vp2.CarouselParams2
import com.eye.cool.banner.CarouselParams
import kotlinx.android.synthetic.main.activity_banner.*
import kotlin.random.Random

/**
 *Created by ycb on 2019/11/14 0014
 */
class BannerActivity : AppCompatActivity() {

  private val pages = mutableListOf<Any>(1)
  private val adapter1 = BannerPagerAdapter(pages)
  private val adapter2 = Banner2Adapter(pages)
  private val adapter3 = Banner3Adapter(pages)
  private val adapter4 = Banner3Adapter(pages)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_banner)
    viewPager.adapter = adapter1
    indicator.alwaysShownWhenOnlyOne(true)
    viewPager.params = CarouselParams.Builder()
        .setIndicator(indicator)
        .setScrollAble(true)
        .setInterval(3000)
        .setScrollDuration(3000)
        .setReversible(true)
        .setRecyclable(true)
        .setAttachLifecycle(lifecycle)
        .setDirection(CarouselParams.LEFT_TO_RIGHT)
        .setScrollWhenOne(false)
        .build()

    setupBanner2()
    setupGallery()
  }

  private fun setupGallery() {
    gallery1.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    gallery1.adapter = adapter3
    gallery2.adapter = adapter4
    Gallery(gallery1, GalleryParams
        .Builder()
        .setFastScroll(true)
        .build()
    ).setup()
  }

  private fun setupBanner2() {
    adapter2.attachToViewPager(viewPager2)
    adapter2.setCarouselParams(
        CarouselParams2.Builder()
            .isUserInputEnabled(true)
            .setPauseWhenTouch(true)
            // .setAttachLifecycle(lifecycle)
            .setReversible(true)
            .setInterval(3000L)
            //.setIndicator(null)
            //.setInterpolator(null)
            .setDirection(CarouselParams2.RIGHT_TO_LEFT)
            .build()
    )
  }

  fun changePagerNum(v: View) {
    pages.clear()
    val size = Random.nextInt(20) + 1
    (0..size).forEach {
      pages.add(it)
    }
    adapter1.notifyDataSetChanged()
    adapter2.notifyDataSetChanged()
    adapter3.notifyDataSetChanged()
    adapter4.notifyDataSetChanged()
  }
}