package com.cool.eye.func.banner.snap

/**
 *Created by ycb on 2019/2/20 0020
 */
class GalleryParams private constructor() {
  internal var fastScroll: Boolean = false

  class Builder {
    private val params = GalleryParams()

    fun setFastScroll(fastScroll: Boolean): Builder {
      params.fastScroll = fastScroll
      return this
    }

    fun build(): GalleryParams {
      return params
    }
  }
}