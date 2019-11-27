package com.cool.eye.func.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.activity_task.*
import java.util.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
    }

    val random = Random()

    fun flowTask(view: View) {
        TaskFlow().create {
            "create"
        }.doOnNext {
            "..doOnNext1"
        }.doOnNext {
            "..doOnNext2"
        }.execute {
            tv_task.text = it
        }
    }

    fun wrapTask(view: View) {
        TaskPack().zip<String, String, String>({ "1" }, { "pack2" })
                .compose { data1, data2 ->
                    "$data1..$data2"
                }
                .execute {
                    tv_task.text = it
                }
    }

    fun multiTask(view: View) {
        TaskPack().zip<String, String>({
            Thread.sleep(random.nextInt(500)+ 100L)
            "pack1"
        }, {
            Thread.sleep(random.nextInt(500)+ 100L)
            "pack2"
        }, {
            Thread.sleep(random.nextInt(500)+ 100L)
            "pack3"
        }, {
            Thread.sleep(random.nextInt(500)+ 100L)
            "pack4"
        }, {
            Thread.sleep(random.nextInt(500)+ 100L)
            "pack5"
        }, {
            Thread.sleep(random.nextInt(500)+ 100L)
            " pack6 "
        })
                .compose { data ->
                    val sb = StringBuilder()
                    data.forEach {
                        sb.append(it)
                    }
                    sb.toString()
                }
                .execute {
                    tv_task.text = it
                }
    }

    fun asyncTask(view: View) {
        AsyncPack().zip<Double, String, String>({
            Thread({
                Thread.sleep(random.nextInt(500)+ 100L)
                it.invoke(random.nextDouble())
            }).start()
        }, {
            Thread({
                Thread.sleep(random.nextInt(500)+ 100L)
                it.invoke("asyncTask")
            }).start()
        })
                .compose { data1, data2 ->
                    "$data1 $data2"
                }
                .execute {
                    tv_task.text = it
                }
    }

    fun asyncMultiTask(v: View) {
        AsyncPack().zip<String, String>({
            Thread({
                Thread.sleep(random.nextInt(500)+ 100L)
                it.invoke("asyncTask1")
            }).start()
        }, {
            Thread({
                Thread.sleep(random.nextInt(500)+ 100L)
                it.invoke("asyncTask2")
            }).start()
        }, {
            Thread({
                Thread.sleep(random.nextInt(500)+ 100L)
                it.invoke("asyncTask3")
            }).start()
        }).compose {
            val sb = StringBuilder()
            it.forEach {
                sb.append(it)
            }
            sb.toString()
        }.execute {
            tv_task.text = it
        }
    }
}
