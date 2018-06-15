package com.cool.eye.func.task

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/**
 *Created by cool on 2018/6/12
 */
class TaskFlow {

    companion object {
        private val executor = Executors.newSingleThreadExecutor()
    }

    fun <T> create(R: () -> T): ExecuteQueue<T> {
        val executeQueue = ExecuteQueue<T>()
        executeQueue.create(R)
        return executeQueue
    }

    class ExecuteQueue<T> internal constructor() {

        private lateinit var create: (() -> T)

        private var mutableList = mutableListOf<(data: T) -> T>()

        /**
         * run on sub thread
         */
        fun create(R: () -> T): ExecuteQueue<T> {
            create = R
            return this
        }

        /**
         * run on sub thread
         */
        fun doOnNext(R: (data: T) -> T): ExecuteQueue<T> {
            mutableList.add(R)
            return this
        }

        /**
         * run on ui thread
         */
        fun execute(R: (data: T) -> Unit) {
            executor.execute {
                var data = create.invoke()
                mutableList.forEach {
                    data = it.invoke(data)
                }
                Handler(Looper.getMainLooper()).post {
                    R.invoke(data)
                }
            }
        }
    }
}