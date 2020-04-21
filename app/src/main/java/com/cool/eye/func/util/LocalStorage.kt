package com.cool.eye.func.util

import java.io.File

/**
 *Created by ycb on 2019/10/30 0030
 */
object LocalStorage {

  private const val LOCAL_DIR = "library"
  private const val QR = "qr"
  const val PNG = ".png"
  const val JPEG = ".jpeg"
  const val JPG = ".jpg"

  @JvmStatic
  fun composeCacheDir(): StringBuilder {
    val sb = StringBuilder()
    sb.append(android.os.Environment.getExternalStorageDirectory())
    sb.append(File.separator)
    sb.append(LOCAL_DIR)
    return sb
  }

  @JvmStatic
  fun composeQrDir(): StringBuilder {
    val sb = composeCacheDir()
    sb.append(File.separator)
        .append(QR)
    return sb
  }

  // create image storage path
  @JvmStatic
  fun composeQrImage(name: String, suffix: String = PNG): String {
    val sb = composeQrDir()
    val file = File(sb.toString())
    if (!file.exists()) {
      val result = file.mkdirs()
      check(result) { "Can not crate files on external storage, please check your WRITE_STORAGE permission!" }
    }
    sb.append(File.separator)
        .append(name)
        .append(suffix)
    return sb.toString()
  }
}