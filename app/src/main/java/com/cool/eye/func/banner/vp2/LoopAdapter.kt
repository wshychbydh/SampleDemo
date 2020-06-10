package com.cool.eye.func.banner.vp2

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by ycb on 2020/6/8 0008
 */
abstract class LoopAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

  protected var viewPager: ViewPager2? = null
  protected var params: CarouselParams2 = CarouselParams2.Builder().build()

  init {
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
      override fun onChanged() {
        onDataChanged()
      }
    })
  }

  @CallSuper
  open fun attachToViewPager(viewPager: ViewPager2) {
    this.viewPager = viewPager
    viewPager.adapter = this
    viewPager.orientation = params.getOrientation()
    if (getRealItemCount() > 0) {
      viewPager.setCurrentItem(1, false)
    }
    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        if (positionOffset == 0f) {
          if (position == 0) {
            viewPager.setCurrentItem(itemCount - 2, false)
          } else if (position == itemCount - 1) {
            viewPager.setCurrentItem(1, false)
          }
        }
      }
    })
  }

  @CallSuper
  open fun setCarouselParams(params: CarouselParams2) {
    this.params = params
    viewPager?.orientation = params.getOrientation()
  }

  /**
   * Called when data changes
   */
  protected open fun onDataChanged() {
    if (getRealItemCount() > 0) {
      viewPager?.setCurrentItem(1, false)
    }
  }

  abstract fun getRealItemCount(): Int

  override fun getItemCount(): Int {
    val count = getRealItemCount()
    if (count <= 0) return 0
    if (!params.scrollWhenOne && count == 1) return 1
    return getRealItemCount() + 2
  }

  final override fun getItemViewType(position: Int): Int {
    return getItemViewType2(toRealPosition(position))
  }

  final override fun onBindViewHolder(holder: VH, position: Int) {
    onBindViewHolder2(holder, toRealPosition(position))
  }

  open fun getItemViewType2(position: Int): Int {
    return super.getItemViewType(position)
  }

  abstract fun onBindViewHolder2(holder: VH, realPosition: Int)

  fun getCurrentRealPosition(): Int {
    return toRealPosition(viewPager?.currentItem ?: 0)
  }

  fun toRealPosition(position: Int): Int {
    val realCount = getRealItemCount()
    if (realCount == 0) return 0
    if (itemCount < 3) return position
    var realPosition = (position - 1) % realCount
    if (realPosition < 0) realPosition += realCount
    return realPosition
  }
}
