package com.cool.eye.func.banner.loop

import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import java.util.*

/**
 * @author: chuanbo
 * @date: 2021/11/11 14:50
 * @desc: 无限循环的FragmentAdapter
 */
abstract class LooperFragmentAdapter : FragmentStateAdapter, ILoop {

    private var viewPager2: ViewPager2? = null
    private val onPageChangeListeners: MutableSet<OnPageChangeListener> = LinkedHashSet()

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fm: FragmentManager, lifecycle: Lifecycle) : super(fm, lifecycle)

    fun registerOnPageChangedListener(listener: OnPageChangeListener) {
        onPageChangeListeners.add(listener)
    }

    fun unregisterOnPageChangeCallback(listener: OnPageChangeListener) {
        onPageChangeListeners.remove(listener)
    }

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        tryObserverViewPager2(recyclerView)
    }

    private fun tryObserverViewPager2(recyclerView: RecyclerView) {
        val parent = recyclerView.parent ?: return
        if (parent !is ViewPager2) return
        viewPager2 = parent
        parent.overScrollMode = View.OVER_SCROLL_NEVER
        if (getRealItemCount() > 1) {
            parent.setCurrentItem(1, false)
        }
        parent.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val relPos = toRealPosition(position)
                for (listener in onPageChangeListeners) {
                    listener.onPageSelected(relPos)
                }
            }

            override fun onPageScrolled(pos: Int, posOffset: Float, posOffsetPixels: Int) {
                if (posOffset == 0f) {
                    if (itemCount <= 2) return
                    if (pos == 0) {
                        parent.setCurrentItem(itemCount - 2, false)
                    } else if (pos == itemCount - 1) {
                        parent.setCurrentItem(1, false)
                    }
                }
            }
        })
    }

    override fun createFragment(position: Int): Fragment {
        return onCreateFragment(toRealPosition(position))
    }

    abstract fun onCreateFragment(realPosition: Int): Fragment

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: List<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    final override fun getItemCount() = super.getItemCount()

    fun setCurrentItem(index: Int, smoothScroll: Boolean) {
        val vp = viewPager2 ?: return
        val realPos = toRealPositionByIndex(index)
        if (vp.currentItem == realPos) return
        vp.setCurrentItem(realPos, smoothScroll)
    }

    var currentItem: Int
        get() = viewPager2?.currentItem ?: 0
        set(index) {
            setCurrentItem(index, false)
        }

    interface OnPageChangeListener {
        fun onPageSelected(position: Int)
    }
}