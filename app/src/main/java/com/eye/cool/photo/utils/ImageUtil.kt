package com.eye.cool.photo.utils

import android.graphics.*
import android.support.annotation.WorkerThread
import kotlin.math.min
import kotlin.math.roundToInt

/**
 *Created by cool on 2018/7/5
 */
object ImageUtil {

  /**
   *Create rounded corner images
   * @param source
   * @param bitmapSize the output bitmap size
   */
  @JvmStatic
  @WorkerThread
  fun createCircleImage(source: Bitmap, bitmapSize: Float): Bitmap {
    val matrix = Matrix()
    matrix.postScale(bitmapSize / source.width.toFloat(), bitmapSize / source.height.toFloat())
    val bitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)

    val paint = Paint()
    paint.isAntiAlias = true
    paint.isDither = true
    paint.isFilterBitmap = true
    val target = Bitmap.createBitmap(bitmapSize.toInt(), bitmapSize.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)
    canvas.drawCircle(bitmapSize / 2f, bitmapSize / 2f, bitmapSize / 2, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, 0f, 0f, paint)

    return target
  }

  /**
   * Convert image to bitmap
   * If the width and height are smaller than the image itself, it will not compress
   * @param path The path of image
   * @param width output bitmap's width, default -1
   * @param height output bitmap's height, default -1
   */
  @JvmStatic
  @WorkerThread
  fun getBitmapFromFile(path: String, width: Int = -1, height: Int = -1): Bitmap {
    if (width <= 0 || height <= 0) {
      return BitmapFactory.decodeFile(path)
    }
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)
    options.inSampleSize = getBitmapInSampleSize(width, height, options)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(path, options)
  }

  private fun getBitmapInSampleSize(reqWidth: Int, reqHeight: Int, options: BitmapFactory.Options): Int {
    var inSampleSize = 1
    if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
      val widthRatio = (options.outWidth.toFloat() / reqWidth.toFloat()).roundToInt()
      val heightRatio = (options.outHeight.toFloat() / reqHeight.toFloat()).roundToInt()
      inSampleSize = min(widthRatio, heightRatio)
    }
    return inSampleSize
  }
}