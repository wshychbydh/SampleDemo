package com.cool.eye.func.view.trend

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.cool.eye.func.R
import com.cool.eye.func.view.trend.helper.GoldTariffHelper
import com.cool.eye.func.view.trend.view.GoldTrendView
import com.cool.eye.func.view.trend.view.TrendView

/**
 *Created by cool on 2018/5/24
 */

class TrendActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trend)

        val chartView = findViewById<View>(R.id.chartView1) as TrendView
        chartView.setData(GoldTariffHelper.mList)
        chartView.isFill = true
        chartView.showNode = false

        val chartView2 = findViewById<View>(R.id.chartView2) as GoldTrendView
        chartView2.setData(GoldTariffHelper.mList2)
    }
}