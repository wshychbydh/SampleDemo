package com.cool.eye.func.photo.util

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import java.io.*

/**
 *Created by ycb on 2019/12/30 0030
 */
object ImageUtil {


  @JvmStatic
  @WorkerThread
  @TargetApi(Build.VERSION_CODES.Q)
  open fun saveImageAboveQ(
      context: Context,
      sourceFile: File,
      saveFileName: String,
      saveDirName: String
  ): Boolean {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
    values.put(MediaStore.Images.Media.DISPLAY_NAME, saveFileName)
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.TITLE, "Image.png")
    values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$saveDirName")
    val external: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val resolver = context.contentResolver
    val insertUri: Uri? = resolver.insert(external, values)
    var inputStream: BufferedInputStream? = null
    var os: OutputStream? = null
    var result: Boolean
    try {
      inputStream = BufferedInputStream(FileInputStream(sourceFile))
      if (insertUri != null) {
        os = resolver.openOutputStream(insertUri)
      }
      if (os != null) {
        val buffer = ByteArray(1024 * 4)
        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
          os.write(buffer, 0, len)
        }
        os.flush()
      }
      result = true
    } catch (e: IOException) {
      result = false
    } finally {
      os?.close()
      inputStream?.close()
    }
    return result
  }
}