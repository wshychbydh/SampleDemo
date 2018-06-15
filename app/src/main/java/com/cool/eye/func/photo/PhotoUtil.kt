package com.cool.eye.func.photo

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.File
/**
 * Created by cool on 2018/6/12
 */
object PhotoUtil {

    fun takePhoto(wrapper: ContextWrapper, outputFile: File) {
        val intent = Intent()
        intent.action = "android.media.action.IMAGE_CAPTURE"
        intent.addCategory("android.intent.category.DEFAULT")
        val uri = FileProviderUtil.uriFromFile(wrapper.getActivity(), outputFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        wrapper.startActivityForResult(intent, PhotoHelper.TAKE_PHOTO)
    }

    fun takeAlbum(wrapper: ContextWrapper) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = "android.intent.action.PICK"
        intent.addCategory("android.intent.category.DEFAULT")
        wrapper.startActivityForResult(intent, PhotoHelper.SELECT_ALBUM)
    }

    fun cut(wrapper: ContextWrapper, uri: Uri, outputFile: File) {
        val intent = Intent("com.android.camera.action.CROP")
        FileProviderUtil.setIntentDataAndType(intent, "image/*", uri, true)
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
        //return-data为true时，直接返回bitmap，可能会很占内存，不建议，小米等个别机型会出异常！！！
        //所以适配小米等个别机型，裁切后的图片，不能直接使用data返回，应使用uri指向
        //裁切后保存的URI，不属于我们向外共享的，所以可以使用fill://类型的URI
        val outputUri = Uri.fromFile(outputFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        intent.putExtra("return-data", false)

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)
        wrapper.startActivityForResult(intent, PhotoHelper.ADJUST_PHOTO)
    }
}