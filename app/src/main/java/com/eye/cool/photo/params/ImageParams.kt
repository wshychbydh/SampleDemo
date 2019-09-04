package com.eye.cool.photo.params

import com.eye.cool.photo.support.OnSelectListener

/**
 *Created by ycb on 2019/8/8 0008
 */
class ImageParams private constructor() {

  internal var cutAble = true

  internal var outputW = 300

  internal var outputH = 300

  internal var onSelectListener: OnSelectListener? = null

  class Builder {

    private var params = ImageParams()

    /**
     * Callback after image selection, callback in ui thread
     * @param listener
     */
    fun setOnSelectListener(listener: OnSelectListener): Builder {
      params.onSelectListener = listener
      return this
    }

    /**
     *  Whether the selected picture needs to be cut，default true
     *  @param cutAble
     */
    fun setCutAble(cutAble: Boolean): Builder {
      params.cutAble = cutAble
      return this
    }

    /**
     * Output the size of the image，default width:300,height:300
     * @param outputW
     * @param outputH
     */
    fun setOutput(outputW: Int, outputH: Int): Builder {
      params.outputW = outputW
      params.outputH = outputH
      return this
    }

    fun build() = params
  }
}