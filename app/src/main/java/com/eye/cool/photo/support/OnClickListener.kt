package com.eye.cool.photo.support

/**
 *Created by ycb on 2019/8/14 0014
 */
interface OnClickListener {

  /**
   * @param which which the button that was clicked.
   * {@link Constants
   *        #TAKE_PHOTO,
   *        #SELECT_ALBUM,
   *        #CANCEL,
   *        #PERMISSION_FORBID
   * }
   */
  fun onClick(which: Int)
}