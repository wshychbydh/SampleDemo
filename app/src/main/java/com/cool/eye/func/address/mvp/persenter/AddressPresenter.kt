package com.cool.eye.func.address.mvp.persenter

import com.cool.eye.func.address.mvp.model.DataHelper
import com.cool.eye.func.address.mvp.view.IAddressView

/**
 * Created by cool on 2018/4/19.
 */
class AddressPresenter(val view: IAddressView<String>) {


    fun start() {
        getData()
    }

    private fun getData() {
        view.showAddress(DataHelper.list)
        view.groupAddress(DataHelper.keys)
    }

    fun search(query: String): List<String> {
        val list = DataHelper.list
        val queryList = mutableListOf<String>()
        list.forEach {
            if (it.contains(query)) {
                queryList.add(it)
            }
        }
        return queryList
    }

    fun stop() {

    }
}