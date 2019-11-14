package com.cool.eye.func.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by cool on 18/4/18.
 */
class RecyclerAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<DataViewHolder<Any>>() {

    private val viewHolder = SparseArray<Class<out DataViewHolder<*>>>()
    private val data = ArrayList<Any>()
    private var inflater: LayoutInflater? = null
    private var loadMoreListener: (() -> Unit)? = null
    private var loadData: Any? = null
    private var defaultCount = 10
    private var loadMoreAble = false

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].javaClass.name.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<Any> {

        val clazz = viewHolder.get(viewType)
        val layoutId = clazz?.getAnnotation(LayoutId::class.java)
                ?: throw IllegalArgumentException(clazz?.simpleName + "must be has @LayoutId " + "annotation!")

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

    fun appendData(data: List<Any>?) {
        if (data != null && data.isNotEmpty()) {
            this.data.addAll(data)
        }
        doNotifyDataSetChanged()
    }

    fun appendData(data: Any?) {
        if (data != null) {
            this.data.add(data)
        }
        doNotifyDataSetChanged()
    }

    fun updateData(data: List<Any>?) {
        this.data.clear()
        if (data != null && data.isNotEmpty()) {
            this.data.addAll(data)
        }
        doNotifyDataSetChanged()
    }

    fun updateData(data: Any?) {
        this.data.clear()
        if (null != data) {
            this.data.add(data)
        }
        doNotifyDataSetChanged()
    }

    private fun doNotifyDataSetChanged() {
        if (loadData != null) {
            data.remove(loadData!!)
            if (data.size > defaultCount && !loadMoreAble) {
                data.add(loadData!!)
            }
        }
        notifyDataSetChanged()
    }

    /**
     * Use for load more
     */
    fun setLoadMore(data: Any?) {
        this.loadData = data
    }

    /**
     * Use for load more
     * Maximum data per request
     */
    fun setDefaultCount(defaultCount: Int) {
        this.defaultCount = defaultCount
    }

    /**
     * Use for load more
     */
    fun enableLoadMore(loadMoreAble: Boolean) {
        this.loadMoreAble = loadMoreAble
        doNotifyDataSetChanged()
    }

    /**
     * Use for load more
     * @callback
     */
    fun setLoadMoreListener(listener: (() -> Unit)?) {
        this.loadMoreListener = listener
    }

    /**
     * Use for load more
     */
    fun empowerLoadMoreAbility(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!loadMoreAble) return
                if (itemCount < defaultCount) return
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
                    val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                    if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE && lastItemPosition + 1 == itemCount) {
                        loadMoreListener?.invoke()
                    }
                }
            }
        })
    }

    val lastData: Any?
        get() = if (data.isEmpty()) {
            null
        } else data[data.size - 1]
}
