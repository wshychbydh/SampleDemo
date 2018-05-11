package com.cool.eye.func.banner

import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import com.enternityfintech.gold.app.view.indicator.ICarousel

/**
 * Created by cool on 2018/4/18.
 */
class CarouselViewPager constructor(private val viewPager: ViewPager, private var interval: Long = 2000L) : View.OnTouchListener {

    private var carouseAble = true
    private var action: Runnable? = null
    private var carousel: ICarousel

    init {
        if (viewPager.adapter !is ICarousel) {
            throw IllegalArgumentException("ViewPager's adapter must be implement ICarousel.")
        }
        carousel = viewPager.adapter as ICarousel
        viewPager.currentItem = carousel.getInitItem()
        viewPager.setOnTouchListener(this)
        action = Runnable {
            if (carouseAble) {
                viewPager.apply {
                    //FIXME Bug in ++currentItem or currentItem++
                    val nextItem = currentItem + 1
                    currentItem = if (nextItem >= viewPager.adapter.count) 0 else nextItem
                    postDelayed(action, interval)
                }
            }
        }
    }

    /**
     * switching period
     */
    private fun setPageChangeDuration(duration: Int) {
        try {
            val scrollerField =  ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            scrollerField.set(viewPager, PageScroller(viewPager.context, duration))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                onStart()
            else -> onStop()
        }
        return false
    }

    /**
     * Call on activity or fragment onStart
     */
    fun onStart() {
        carouseAble = true
        viewPager.postDelayed(action, interval)
    }

    /**
     * Call on activity or fragment onStop
     */
    fun onStop() {
        carouseAble = false
        viewPager.removeCallbacks(action)
    }
}