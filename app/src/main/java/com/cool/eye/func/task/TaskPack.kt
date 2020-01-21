package com.cool.eye.func.task

import android.os.Handler
import android.os.Looper
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

/**
 *Created by cool on 2018/6/12
 */
class TaskPack {

    companion object {
        private val executor = Executors.newCachedThreadPool()
    }

    fun <T1, T2, T> zip(R1: () -> T1, R2: () -> T2): Executor<T1, T2, T> {
        val executeQueue = Executor<T1, T2, T>()
        executeQueue.zip(R1, R2)
        return executeQueue
    }

    fun <D, T> zip(vararg R: () -> D): ExecuteQueue<D, T> {
        val executeQueue = ExecuteQueue<D, T>()
        executeQueue.zip(*R)
        return executeQueue
    }

    class ExecuteQueue<D, T> internal constructor() {

        private var compose: ((data: List<D>) -> T)? = null

        private var array: Array<out () -> D>? = null

        /**
         * run on sub thread
         */
        fun zip(vararg R: () -> D): ExecuteQueue<D, T> {
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
                executor.execute {
                    data.add(it.invoke())
                    if (counter.decrementAndGet() == 0) {
                        if (compose == null) {
                            R.invoke(null)
                        } else {
                            runOnUi(R, data)
                        }
                    }
                }
            }
        }

        private fun runOnUi(R: (data: T) -> Unit, data: MutableList<D>) {
            val result = compose!!.invoke(data)
            Handler(Looper.getMainLooper()).post {
                R.invoke(result)
            }
        }
    }

    class Executor<T1, T2, T> internal constructor() {

        private var R1: (() -> T1)? = null
        private var R2: (() -> T2)? = null
        private var compose: ((data1: T1, data2: T2) -> T)? = null

        /**
         * run on sub thread
         */
        fun zip(R1: () -> T1, R2: () -> T2): Executor<T1, T2, T> {
            this.R1 = R1
            this.R2 = R2
            return this
        }

        /**
         * run on sub thread
         */
        fun compose(compose: (data1: T1, data2: T2) -> T): Executor<T1, T2, T> {
            this.compose = compose
            return this
        }

        /**
         * run on ui thread
         */
        fun execute(R: (data: T) -> Unit) {

            if (R1 == null || R2 == null || compose == null) return

            val data1: AtomicReference<T1> = AtomicReference()
            val data2: AtomicReference<T2> = AtomicReference()
            executor.execute {
                val result = R1!!.invoke()
                if (data2.get() != null) {
                    runOnUi(R, compose!!.invoke(result, data2.get()!!))
                } else {
                    data1.set(result)
                }
            }
            executor.execute {
                val result = R2!!.invoke()
                if (data1.get() != null) {
                    runOnUi(R, compose!!.invoke(data1.get(), result))
                } else {
                    data2.set(result)
                }
            }
        }

        private fun runOnUi(R: (data: T) -> Unit, data: T) {
            Handler(Looper.getMainLooper()).post {
                R.invoke(data)
            }
        }
    }
}