package com.cool.eye.func.task

import android.os.Handler
import android.os.Looper
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 *Created by cool on 2018/6/14
 */
class AsyncPack {

  companion object {
    private val executor = Executors.newCachedThreadPool()
  }

  fun <T1, T2, T> zip(
      R1: (callback: (data: T1) -> Unit) -> Unit,
      R2: (callback: (data: T2) -> Unit) -> Unit
  ): Executor<T1, T2, T> {
    val executeQueue = Executor<T1, T2, T>()
    executeQueue.zip(R1, R2)
    return executeQueue
  }

  fun <T1, T2, T3, T> zip(
      R1: (callback: (data: T1) -> Unit) -> Unit,
      R2: (callback: (data: T2) -> Unit) -> Unit,
      R3: (callback: (data: T3) -> Unit) -> Unit
  ) {
    //todo
  }

  fun <D, T> zip(
      vararg R: (callback: (data: D) -> Unit) -> Unit
  ): ExecuteQueue<D, T> {
    val executeQueue = ExecuteQueue<D, T>()
    executeQueue.zip(*R)
    return executeQueue
  }

  class Executor<T1, T2, T> internal constructor() {

    private var R1: ((callback: (data: T1) -> Unit) -> Unit)? = null
    private var R2: ((callback: (data: T2) -> Unit) -> Unit)? = null
    private var compose: ((data1: T1, data2: T2) -> T)? = null

    /**
     * run on sub thread
     */
    fun zip(
        R1: (callback: (data: T1) -> Unit) -> Unit,
        R2: (callback: (data: T2) -> Unit) -> Unit
    ): Executor<T1, T2, T> {
      this.R1 = R1
      this.R2 = R2
      return this
    }

    /**
     * run on sub thread
     */
    fun compose(
        compose: (data1: T1, data2: T2) -> T
    ): Executor<T1, T2, T> {
      this.compose = compose
      return this
    }

    /**
     * run on ui thread
     */
    fun execute(R: (data: T?) -> Unit) {
      if (R1 == null || R2 == null) return
      var data1: T1? = null
      var data2: T2? = null
      R1!!.invoke {
        data1 = it
        if (data2 != null) {
          tryCompose(data1!!, data2!!, R)
        }
      }

      R2!!.invoke {
        data2 = it
        if (data1 != null) {
          tryCompose(data1!!, data2!!, R)
        }
      }
    }

    private fun tryCompose(data1: T1, data2: T2, R: (data: T?) -> Unit) {
      if (compose == null) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
          R.invoke(null)
        } else {
          runOnUi(R, null)
        }
      } else {
        executor.execute {
          runOnUi(R, compose!!.invoke(data1, data2))
        }
      }
    }

    private fun runOnUi(R: (data: T?) -> Unit, data: T?) {
      Handler(Looper.getMainLooper()).post {
        R.invoke(data)
      }
    }
  }

  class ExecuteQueue<D, T> internal constructor() {

    private var compose: ((data: List<D>) -> T)? = null

    private var array: Array<out (callback: (data: D) -> Unit) -> Unit>? = null

    /**
     * run on sub thread
     */
    fun zip(vararg R: (callback: (data: D) -> Unit) -> Unit): ExecuteQueue<D, T> {
      array = R
      return this
    }

    /**
     * run on sub thread
     */
    fun compose(R: (data: List<D>) -> T): ExecuteQueue<D, T> {
      this.compose = R
      return this
    }

    /**
     * run on ui thread
     */
    fun execute(R: (data: T?) -> Unit) {
      if (array == null || array!!.isEmpty()) return
      val data = Collections.synchronizedList(ArrayList<D>())
      val counter = AtomicInteger()
      array!!.forEach {
        counter.incrementAndGet()
        it.invoke { D ->
          data.add(D)
          if (counter.decrementAndGet() == 0) {
            if (compose == null) {
              if (Looper.myLooper() == Looper.getMainLooper()) {
                R.invoke(null)
              } else {
                runOnUi(R, null)
              }
            } else {
              runOnUi(R, data)
            }
          }
        }
      }
    }

    private fun runOnUi(R: (data: T?) -> Unit, data: MutableList<D>?) {
      if (data != null && data.isNotEmpty()) {
        executor.execute {
          val result = compose!!.invoke(data)
          Handler(Looper.getMainLooper()).post {
            R.invoke(result)
          }
        }
      } else {
        Handler(Looper.getMainLooper()).post {
          R.invoke(null)
        }
      }
    }
  }
}