package com.cool.eye.func.banner

import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

class CarouselPagerAdapter(override var data: List<String>) : PagerAdapter(), ICarouselData<String> {

    init {
        if (data.isEmpty()) {
            throw IllegalArgumentException("Page data can not be empty")
        }
    }

    private val random = Random()

    override fun getCount(): Int {
        if (getDataSize() == 1) {
            return 1
        }
        return Integer.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(view: ViewGroup, position: Int, `object`: Any) {
        view.removeView(`object` as View)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val textView = TextView(view.context)
        textView.text = getData(position)
        textView.setBackgroundColor(-0x1000000 or random.nextInt(0x00ffffff))
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.WHITE)
        textView.textSize = 48f
        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        return textView
    }
}