package com.cool.eye.func.address

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by cool on 18/4/18.
 */
class RecyclerAdapter : RecyclerView.Adapter<DataViewHolder<Any>>() {

    private val viewHolder = SparseArray<Class<out DataViewHolder<*>>>()
    private val data = ArrayList<Any>()
    private var inflater: LayoutInflater? = null

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].javaClass.name.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<Any> {
        val clazz = viewHolder.get(viewType)
        val layoutId = clazz.getAnnotation(LayoutId::class.java)
                ?: throw IllegalArgumentException(clazz.simpleName + "must be has @LayoutId annotation!")

        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context.applicationContext)
        }

        val itemView = inflater!!.inflate(layoutId.value, parent, false)
        val holder: DataViewHolder<Any>
        try {
            holder = clazz.getConstructor(View::class.java).newInstance(itemView) as DataViewHolder<Any>
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

        return holder
    }

    override fun onBindViewHolder(holder: DataViewHolder<Any>, position: Int) {
        holder.updateViewByData(data[position])
    }

    /**
     * Register ViewHolder by dataClass, data is exclusive.
     *
     * @param dataClazz data Class
     * @param clazz     ViewHolder Class
     */
    fun registerViewHolder(dataClazz: Class<*>, clazz: Class<out DataViewHolder<*>>) {
        viewHolder.put(dataClazz.name.hashCode(), clazz)
    }

    fun appendData(data: List<String>?) {
        if (data != null) {
            this.data.addAll(data)
        }
    }

    fun appendData(data: Any?) {
        if (data != null) {
            this.data.add(data)
        }
    }

    fun updateData(data: List<Any>?) {
        this.data.clear()
        if (data != null) {
            this.data.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun updateData(data: Any?) {
        this.data.clear()
        if (null != data) {
            this.data.add(data)
        }
        notifyDataSetChanged()
    }

    val lastData: Any?
        get() = if (data.isEmpty()) {
            null
        } else data[data.size - 1]
}
