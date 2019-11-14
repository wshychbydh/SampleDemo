package com.cool.eye.func.view.trend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.cool.eye.func.R
import com.cool.eye.func.view.tendency.Node
import com.cool.eye.func.view.tendency.OrderNode
import com.cool.eye.func.view.tendency.OrderTrendView
import com.cool.eye.func.view.tendency.TendencyView
import com.cool.eye.func.view.trend.helper.GoldTariffHelper
import com.cool.eye.func.view.trend.view.GoldTrendView
import com.cool.eye.func.view.trend.view.TrendView
import kotlin.random.Random

/**
 *Created by cool on 2018/5/24
 */

class TrendActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_trend)

    val chartView = findViewById<View>(R.id.chartView1) as TrendView
    chartView.setData(GoldTariffHelper.mList)
    chartView.isFill = true
    chartView.showNode = false

    val chartView2 = findViewById<View>(R.id.chartView2) as GoldTrendView
    chartView2.setData(GoldTariffHelper.mList2)

    val trendView = findViewById<View>(R.id.trendView) as OrderTrendView
    trendView.setData(mock())
    val trendView2 = findViewById<View>(R.id.trendView2) as TendencyView
    trendView2.setData(mock2())
  }

  private fun mock(): List<OrderNode> {
    val list = arrayListOf<OrderNode>()
    for (i in 0..5) {
      list.add(OrderNode(
          date = "11-13",
          increase = Random.nextInt(10) + 10,
          deal = Random.nextInt(20) + 20,
          avg = "30"
      ))
    }
    return list
  }

  private fun mock2(): List<Node> {
    val list = arrayListOf<Node>()
    for (i in 0..5) {
      list.add(Node(
          date = "11-13",
          value = "${Random.nextInt(30) + 10}.${Random.nextInt(99)}".toFloat()
      ))
    }
    return list
  }
}