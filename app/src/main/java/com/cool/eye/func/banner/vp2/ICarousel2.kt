package com.cool.eye.func.banner.vp2

/**
 * Created by cool on 2018/4/18.
 */
internal interface ICarousel2 {

  fun getItemCount(): Int

  fun getNextPage(currentPage: Int): Int {
    val params = getCarouselParams2()
    if (!params.recyclable && !params.reversible) return currentPage
    val size = getItemCount()
    if (params.reversible) {
      if (params.direction == CarouselParams2.RIGHT_TO_LEFT
          || params.direction == CarouselParams2.DOWN_TO_UP
      ) {
        val next = currentPage + 1
        return if (next % size == 0) {
          params.direction = if (params.direction == CarouselParams2.RIGHT_TO_LEFT)
            CarouselParams2.LEFT_TO_RIGHT
          else CarouselParams2.UP_TO_DOWN
          currentPage - 1
        } else {
          next
        }
      } else {
        val next = currentPage - 1
        return if (next % size <= 0) {
          params.direction = if (params.direction == CarouselParams2.LEFT_TO_RIGHT)
            CarouselParams2.RIGHT_TO_LEFT
          else CarouselParams2.DOWN_TO_UP
          0
        } else {
          next
        }
      }
    }
    if (params.recyclable) {
      return if (params.direction == CarouselParams2.RIGHT_TO_LEFT
          || params.direction == CarouselParams2.DOWN_TO_UP) currentPage + 1
      else currentPage - 1
    }
    return currentPage
  }

  fun getCarouselParams2(): CarouselParams2
}