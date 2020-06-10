package com.cool.eye.func.banner.vp2

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ycb on 2020/6/9 0009
 */
abstract class CarouselAdapter<VH : RecyclerView.ViewHolder>
  : LoopAdapter<VH>(), LifecycleObserver, ICarousel2, Runnable {

  private val handler = Handler(Looper.getMainLooper())

  override fun setCarouselParams(params: CarouselParams2) {
    this.params.attachLifecycle?.removeObserver(this)
    super.setCarouselParams(params)
    params.attachLifecycle?.addObserver(this)
    viewPager?.isUserInputEnabled = params.isUserInputEnabled
    restart()
  }

  override fun onDataChanged() {
    super.onDataChanged()
    restart()
  }

  override fun run() {
    val vp = viewPager ?: return
    vp.currentItem = getNextPage(vp.currentItem)
    handler.postDelayed(this, params.interval)
  }

  private fun restart() {
    onStop()
    onStart()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  private fun onStart() {
    if (params.autoCarousel) {
      start()
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  private fun onStop() {
    stop()
  }

  fun start() {
    if (!params.carouselAble) return
    val count = getRealItemCount()
    if (count <= 0 || (count == 1 && !params.scrollWhenOne)) return
    handler.removeCallbacks(this)
    handler.postDelayed(this, params.interval)
  }

  fun stop() {
    handler.removeCallbacks(this)
  }

  override fun getCarouselParams2(): CarouselParams2 = params
}