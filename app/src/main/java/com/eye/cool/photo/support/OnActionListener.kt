package com.eye.cool.photo.support

/**
 *Created by cool on 2018/6/12
 */
interface OnActionListener {

  /**
   * Taking a photo specifies the cache path,
   * which requires permissions 18 and below, declared in the manifest.
   * The 19 above default has permissions
   */
  fun onTakePhoto() {}

  /**
   * Selecting an album requires read and write permissions
   */
  fun onSelectAlbum() {}

  fun onCancel() {}
}