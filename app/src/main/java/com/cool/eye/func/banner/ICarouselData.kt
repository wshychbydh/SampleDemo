package com.cool.eye.func.banner

import com.enternityfintech.gold.app.view.indicator.ICarousel

/**
 * Created by cool on 2018/4/18.
 */
interface ICarouselData<T> : ICarousel {

    var data: List<T>

    fun getData(position: Int): T {
        return data[convertPosition(position)]
    }

    override fun getInitItem(): Int {
        val half = Int.MAX_VALUE / 2
        val size = getDataSize()
        return if (size > 0) {
            half - half % size
        } else 0
    }

    override fun getDataSize(): Int {
        return data.size
    }

    fun convertPosition(position: Int): Int {
        val size = getDataSize()
        return if (size > 0) {
            position % getDataSize()
        } else 0
    }

}