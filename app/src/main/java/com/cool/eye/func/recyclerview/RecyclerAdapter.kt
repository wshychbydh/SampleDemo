package com.cool.eye.func.recyclerview

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by cool on 18/6/14.
 */
open class RecyclerAdapter : RecyclerView.Adapter<DataViewHolder<Any>>() {

  private val viewHolder = SparseArray<Class<out DataViewHolder<*>>>()
  protected val data = ArrayList<Any>()
  private var inflater: LayoutInflater? = null
  private var clickObserver: View.OnClickListener? = null
  private var checkObserver: CompoundButton.OnCheckedChangeListener? = null
  private var longClickObserver: View.OnLongClickListener? = null

  override fun getItemCount(): Int {
    return data.size
  }

  override fun getItemViewType(position: Int): Int {
    return data[position].javaClass.name.hashCode()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder<Any> {

    val clazz = viewHolder.get(viewType)
        ?: throw IllegalArgumentException("You should call registerViewHolder() first !")
    var layoutId = clazz.getAnnotation(LayoutId::class.java)?.value
    if (layoutId == null || layoutId < 1) {
      val layoutName = clazz.getAnnotation(LayoutName::class.java)?.value
      if (!layoutName.isNullOrEmpty()) {
        layoutId = parent.resources.getIdentifier(layoutName, "layout", parent.context.packageName)
      }
    }

    require(!(layoutId == null || layoutId < 1)) { clazz.simpleName + " must be has @LayoutId annotation" }

    if (inflater == null) {
      inflater = LayoutInflater.from(parent.context.applicationContext)
    }

    val itemView = inflater!!.inflate(layoutId, parent, false)
    val holder: DataViewHolder<Any>
    try {
      holder = clazz.getConstructor(View::class.java).newInstance(itemView) as DataViewHolder<Any>
    } catch (e: Exception) {
      throw RuntimeException(e)
    }

    return holder
  }

  override fun onBindViewHolder(holder: DataViewHolder<Any>, position: Int) {
    holder.clickListener = clickObserver
    holder.longClickListener = longClickObserver
    holder.checkedListener = checkObserver
    holder.dataSize = data.size
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

  fun setOnClickListener(clickListener: View.OnClickListener) {
    this.clickObserver = clickListener
  }

  fun setOnCheckedChangeListener(checkedListener: CompoundButton.OnCheckedChangeListener) {
    this.checkObserver = checkedListener
  }

  fun setOnLongClickListener(longClickListener: View.OnLongClickListener) {
    this.longClickObserver = longClickListener
  }

  open fun appendData(data: List<Any>?) {
    if (data != null && data.isNotEmpty()) {
      this.data.addAll(data)
      doNotifyDataSetChanged()
    }
  }

  open fun removeData(data: Any) {
    if (this.data.contains(data) && this.data.size == 1) {
      updateData(Empty())
    } else {
      if (this.data.remove(data)) {
        doNotifyDataSetChanged()
      }
    }
  }

  open fun appendData(index: Int, data: List<Any>?) {
    if (data != null && data.isNotEmpty()) {
      this.data.addAll(index, data)
      doNotifyDataSetChanged()
    }
  }

  open fun appendData(data: Any?) {
    if (data != null) {
      this.data.add(data)
      doNotifyDataSetChanged()
    }
  }

  open fun appendData(index: Int, data: Any?) {
    if (data != null) {
      this.data.add(index, data)
      doNotifyDataSetChanged()
    }
  }

  open fun updateData(data: List<Any>?) {
    this.data.clear()
    if (data != null && data.isNotEmpty()) {
      this.data.addAll(data)
    }
    doNotifyDataSetChanged()
  }

  open fun updateData(data: Any?) {
    this.data.clear()
    if (null != data) {
      this.data.add(data)
    }
    doNotifyDataSetChanged()
  }

  @CallSuper
  open fun doNotifyDataSetChanged() {
    notifyDataSetChanged()
  }

  open fun isLastPosition(position: Int): Boolean {
    return position == itemCount - 1
  }

  open fun getLastData(): Any? {
    return if (data.isEmpty()) {
      null
    } else data[data.size - 1]
  }
}
