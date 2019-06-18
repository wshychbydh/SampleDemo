package com.cool.eye.func.address.mvp.view

import android.util.SparseArray

/**
 * Created by cool on 2018/4/19.
 */
interface IAddressView<T> {

  fun showAddress(data: List<T>)

  fun groupAddress(group: SparseArray<T>)
}