package com.cool.eye.func.util

import android.graphics.*
import androidx.annotation.WorkerThread
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

/**
 *Created by ycb on 2018/09/06
 */
object ImageUtil {

  /**
   * Create rounded corner images
   *
   * @param path The path of the rounded picture
   * @param bitmapSize The output bitmap size
   */
  @JvmStatic
  @WorkerThread
  fun createCircleImage(path: String, bitmapSize: Float): Bitmap {
    val source = getBitmapFromFile(path, bitmapSize.toInt(), bitmapSize.toInt())
    return createCircleImage(source, bitmapSize)
  }

  /**
   * Create rounded corner images
   *
   * @param source The source of the rounded picture
   * @param bitmapSize The output bitmap size
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
   *
   * @param path The path of picture
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
      inSampleSize = widthRatio.coerceAtMost(heightRatio)
    }
    return inSampleSize
  }

  @JvmStatic
  @WorkerThread
  fun getBitmapFromNet(imgUrl: String, width: Int, height: Int): Bitmap? {
    val url: URL
    val connection: HttpURLConnection?
    try {
      url = URL(imgUrl)
      connection = url.openConnection() as HttpURLConnection
      connection.connectTimeout = 60000 //超时设置
      connection.doInput = true
      connection.useCaches = false //设置不使用缓存
      val inputStream = connection.inputStream
      val options = BitmapFactory.Options()
      options.inSampleSize = getBitmapInSampleSize(width, height, options)
      val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
      inputStream.close()
      return bitmap
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return null
  }
}