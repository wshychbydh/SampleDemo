package com.eye.cool.photo.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.eye.cool.photo.support.CompatContext
import com.eye.cool.photo.support.Constants
import java.io.File


/**
 * Created by cool on 2018/6/12
 */
object PhotoUtil {

  /**
   * take a photo
   * @param wrapper
   * @param outputFile The path for photo output
   */
  fun takePhoto(wrapper: CompatContext, outputFile: File) {
    val intent = Intent()
    intent.action = "android.media.action.IMAGE_CAPTURE"
    intent.addCategory("android.intent.category.DEFAULT")
    val uri = FileProviderUtil.uriFromFile(wrapper.context(), outputFile)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    wrapper.startActivityForResult(intent, Constants.TAKE_PHOTO)
  }

  /**
   * select image from album
   *@param wrapper
   */
  fun takeAlbum(wrapper: CompatContext) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_PICK
    intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    intent.addCategory("android.intent.category.DEFAULT")
    wrapper.startActivityForResult(intent, Constants.SELECT_ALBUM)
  }

  /**
   * Shear pictures
   * @param wrapper
   * @param uri    The image uri to be clipped
   * @param outputFile The path for clipped image output
   * @param outputW output width, default 300px
   * @param outputH output height, default 300px
   */
  fun cut(wrapper: CompatContext, uri: Uri, outputFile: File, outputW: Int = 300, outputH: Int = 300) {
    val intent = Intent("com.android.camera.action.CROP")
    FileProviderUtil.setIntentDataAndType(intent, "image/*", uri, true)
    intent.putExtra("crop", true)
    intent.putExtra("aspectX", 1)
    intent.putExtra("aspectY", outputW.toFloat() / outputH.toFloat())
    intent.putExtra("outputW", outputW)
    intent.putExtra("outputH", outputH)
    //When return-data is true, it directly returns bitmap, which will occupy a lot of memory. It is not recommended
    //The URI saved after cutting is not one we share outwards, so we can use the URI of type file://
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile))
    intent.putExtra("return-data", false)

    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
    intent.putExtra("noFaceDetection", true)
    wrapper.startActivityForResult(intent, Constants.ADJUST_PHOTO)
  }
}