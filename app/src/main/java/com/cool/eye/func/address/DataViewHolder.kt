package com.cool.eye.func.address

import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by cool on 18/4/18.
 * Child class must public static inner class or public outer class.
 */
abstract class DataViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected var data: D? = null
        private set

    open fun updateViewByData(data: D) {
        this.data = data
    }
}
