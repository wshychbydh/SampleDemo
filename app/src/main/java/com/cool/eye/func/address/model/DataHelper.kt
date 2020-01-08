package com.cool.eye.func.address.model

import com.eye.cool.book.support.IQuickProvider
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * Created by cool on 2018/4/19.
 */
object DataHelper {

  val data = LinkedHashMap<String, List<IQuickProvider>>()

  var DEFAULTS = arrayOf("热", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
      "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

  init {
    mockAddress()
  }

  private fun mockAddress() {
    data.clear()
    val size = DEFAULTS.size
    val random = Random()
    for (i in 0 until size) {
      val key = DEFAULTS[i]

      if (i == 0) {
        data[key] = arrayListOf(Hot(key, "热门城市"))
        continue
      }

      val count = random.nextInt(10) + 1
      val content = ArrayList<Address>()
      (0 until count).forEach {
        content.add(Address(key, "第 ${it + 1}个地址，我属于城市$key"))
      }
      data[key] = content
    }
  }
}