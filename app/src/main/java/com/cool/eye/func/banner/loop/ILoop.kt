package com.cool.eye.func.banner.loop

/**
 * @author: chuanbo
 * @date: 2021/11/11 15:03
 * @desc:
 */
interface ILoop {

    fun getItemCount(): Int {
        val count = getRealItemCount()
        if (count <= 0) return 0
        return if (count == 1) 1 else count + 2
    }

    fun getRealItemCount(): Int

    fun toRealPosition(position: Int): Int {
        val realCount = getRealItemCount()
        if (realCount == 1) return 0
        if (getItemCount() <= 2) return position
        val realPosition = (position - 1) % realCount
        return if (realPosition < 0) realPosition + realCount else realPosition
    }

    fun toRealPositionByIndex(index: Int): Int {
        val realCount = getRealItemCount()
        if (realCount <= 1) return 0
        val count = getItemCount()
        return if (index > count) 0 else (index + 1) % count
    }
}