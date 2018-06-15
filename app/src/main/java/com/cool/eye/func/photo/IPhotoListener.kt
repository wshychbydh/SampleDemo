package com.cool.eye.func.photo

/**
 *Created by cool on 2018/6/12
 */
interface IPhotoListener {

    /**
     * 拍照
     */
    fun onTakePhoto() {}

    /**
     * 从相册选取
     */
    fun onSelectAlbum() {}

    fun onCancel() {}
}