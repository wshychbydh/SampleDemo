package com.eye.cool.photo.support

/**
 *Created by ycb on 2019/8/8 0008
 */
interface OnSelectListener {

  /**
   * callback when select image successful
   * @param path output image's local file path
   */
  fun onSelect(path: String)
}