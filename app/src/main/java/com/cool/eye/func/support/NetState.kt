package com.cool.eye.func.support

/**
 *Created by ycb on 2019/5/30 0030
 */
sealed class NetState<out T> : DataWrapper<T>() {

  data class Canceled(val msg: String? = null) : NetState<Nothing>()//未达到请求条件
  data class Succeed<out D>(val data: D?) : NetState<D>()
  data class Failed(val e: Throwable) : NetState<Nothing>()
  data class Loading(val msg: String? = "loading...") : NetState<Nothing>()
}