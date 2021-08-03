package com.cool.eye.func.banner.vp2

import android.view.animation.Interpolator
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.eye.cool.banner.IIndicator

/**
 *Created by ycb on 2019/2/20 0020
 */
class CarouselParams2 private constructor() {
  internal var interval: Long = 5000L
  internal var reversible = false
  internal var recyclable = true
  internal var carouselAble = true
  internal var autoCarousel = true
  internal var pauseWhenTouch = true
  internal var isUserInputEnabled = true
  internal var scrollWhenOne = true
  internal var direction = RIGHT_TO_LEFT
  internal var scrollDuration: Int? = null
  internal var interpolator: Interpolator? = null
  internal var attachLifecycle: Lifecycle? = null
  internal var indicator: IIndicator? = null

  fun getOrientation(): Int {
    return if (direction == UP_TO_DOWN || direction == DOWN_TO_UP) {
      ViewPager2.ORIENTATION_VERTICAL
    } else ViewPager2.ORIENTATION_HORIZONTAL
  }

  class Builder {
    private val params = CarouselParams2()

    /**
     * Switching interval time, default 5s. unit(ms)
     */
    fun setInterval(millisecond: Long): Builder {
      params.interval = millisecond
      return this
    }

    /**
     * Reverse rotation, default false
     */
    fun setReversible(reversible: Boolean): Builder {
      params.reversible = reversible
      return this
    }

    /**
     * Stops the round when pressed, default true
     */
    fun setPauseWhenTouch(pauseWhenTouch: Boolean): Builder {
      params.pauseWhenTouch = pauseWhenTouch
      return this
    }


    /**
     * Support to round, default true。If set false, it will never auto-rotation.
     */
    fun setCarouselAble(carouselAble: Boolean): Builder {
      params.carouselAble = carouselAble
      return this
    }

    /**
     * Auto start/end round, default true
     */
    fun setAutoCarousel(autoCarousel: Boolean): Builder {
      params.autoCarousel = autoCarousel
      return this
    }

    /**
     * Loop round robin, default true
     */
    fun setRecyclable(recyclable: Boolean): Builder {
      params.recyclable = recyclable
      return this
    }

    /**
     * Enable or disable user initiated scrolling. This includes touch input (scroll and fling
     * gestures) and accessibility input. Disabling keyboard input is not yet supported. When user
     * initiated scrolling is disabled, programmatic scrolls through {@link #setCurrentItem(int,
     * boolean) setCurrentItem} still work. By default, user initiated scrolling is enabled.
     *
     * @param enabled {@code true} to allow user initiated scrolling, {@code false} to block user
     *        initiated scrolling
     * @see #isUserInputEnabled()
     */
    fun isUserInputEnabled(isUserInputEnabled: Boolean): Builder {
      params.isUserInputEnabled = isUserInputEnabled
      return this
    }

    /**
     * The rotation time of single picture is 1s by default. unit(milliseconds)
     */
    fun setScrollDuration(millisecond: Int): Builder {
      params.scrollDuration = millisecond
      return this
    }

    /**
     * Scroll direction can only be {@link LEFT_TO_RIGHT,@link RIGHT_TO_LEFT}, default RIGHT_TO_LEFT
     * It's equivalent to swiping to the left
     */
    fun setDirection(direction: Int): Builder {
      params.direction = direction
      return this
    }

    /**
     * Scroll with only one image, default false
     */
    fun setScrollWhenOne(scrollWhenOne: Boolean): Builder {
      params.scrollWhenOne = scrollWhenOne
      return this
    }

    /**
     * Scroller's interpolator for ViewPager
     */
    fun setInterpolator(interpolator: Interpolator): Builder {
      params.interpolator = interpolator
      return this
    }

    /**
     * Automatically rounds by Lifecycle with OnResume-onPause
     */
    fun setAttachLifecycle(lifecycle: Lifecycle): Builder {
      params.attachLifecycle = lifecycle
      return this
    }

    /**
     * Indicator which conjunction with Viewpager
     */
    fun setIndicator(indicator: IIndicator): Builder {
      params.indicator = indicator
      return this
    }

    fun build(): CarouselParams2 {
      return params
    }
  }

  companion object {
    const val RIGHT_TO_LEFT = 0   // arrow left
    const val LEFT_TO_RIGHT = 1   // arrow right
    const val DOWN_TO_UP = 0      // arrow up
    const val UP_TO_DOWN = 1      // arrow down
  }
}