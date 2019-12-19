package com.cool.eye.func.recyclerview

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by cool on 18/4/18.
 * Child class must public static inner class or public outer class.
 */
abstract class DataViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {

  var clickListener: View.OnClickListener? = null
  var checkedListener: CompoundButton.OnCheckedChangeListener? = null
  var longClickListener: View.OnLongClickListener? = null
  var dataSize = 1

  protected var data: D? = null
    private set

  @CallSuper
  open fun updateViewByData(data: D) {
    this.data = data
  }

  /**
   * bind with special data
   */
  fun registerClickListener(view: View) {
    registerClickListener(view, null)
  }

  /**
   * bind with special data
   */
  fun <T> registerClickListener(view: View, specialData: T? = null) {
    if (clickListener != null) {
      if (specialData == null) {
        view.tag = this.data
      } else {
        view.tag = specialData
      }
      view.setOnClickListener(clickListener)
    }
  }

  fun registerCheckedListener(view: CompoundButton) {
    registerCheckedListener(view, null)
  }

  fun <T> registerCheckedListener(view: CompoundButton, specialData: T? = null) {
    if (checkedListener != null) {
      if (specialData == null) {
        view.tag = this.data
      } else {
        view.tag = specialData
      }
      view.setOnCheckedChangeListener(checkedListener)
    }
  }

  /**
   * bind with special data
   */
  fun setOnLongClickListener(view: View, specialData: Any?) {
    if (longClickListener != null) {
      if (specialData == null) {
        view.tag = this.data
      } else {
        view.tag = specialData
      }
      view.setOnLongClickListener(longClickListener)
    }
  }

  fun onViewLongClicked(view: View) {
    longClickListener?.onLongClick(view)
  }

  fun onViewClicked(view: View) {
    clickListener?.onClick(view)
  }
}
