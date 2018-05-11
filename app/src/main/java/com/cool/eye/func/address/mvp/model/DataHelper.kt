package com.cool.eye.func.address.mvp.model

import android.util.SparseArray
import java.util.ArrayList

/**
 * Created by cool on 2018/4/19.
 */
object DataHelper {
    val datas = SparseArray<List<String>>()//模拟服务器返回数据
    val list = ArrayList<String>()//adapter数据源
    val keys = SparseArray<String>()//存放所有key的位置和内容

    init {
        mockData()
    }

    fun mockData() {
        for (i in 0..26) {//(5-15)
            val list = ArrayList<String>()
            if (i == 0) {
                list.add("header")
            } else {
                for (j in 0..4) {//(5-15)
                    list.add("第" + (j + 1) + "个item，我属于标题" + i)
                }
            }
            datas.put(i, list)
        }
        for (i in 0 until datas.size()) {
            if (i == 0) {
                keys.put(0, "热")
            } else {
                keys.put(list.size, ('A'.toInt() + i - 1).toChar().toString())
            }
            list.addAll(datas.get(i))
        }
    }
}