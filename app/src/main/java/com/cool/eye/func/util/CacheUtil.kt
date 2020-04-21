package com.cool.eye.func.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.math.BigDecimal

object CacheUtil {

  @JvmStatic
  fun getCacheSize(context: Context): String {
    var cacheSize = getFolderSize(context.cacheDir)
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
      cacheSize += getFolderSize(context.externalCacheDir)
    }
    return format(cacheSize)
  }

  @JvmStatic
  fun clearCache(context: Context) {
    deleteDir(context.cacheDir)
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
      deleteDir(context.externalCacheDir)
    }
  }

  @JvmStatic
  fun deleteDir(dir: File?): Boolean {
    if (dir == null) return true
    return dir.deleteRecursively()
  }

  @JvmStatic
  fun getFolderSize(file: File?): Long {
    var size = 0L
    if (file == null) return 0L
    if (file.isFile) {
      return file.length()
    }
    for (listFile in file.listFiles()) {
      size += if (listFile.isDirectory) {
        getFolderSize(listFile)
      } else {
        listFile.length()
      }
    }
    return size
  }

  @JvmStatic
  fun format(size: Long): String {

    val k = 1024L
    val m = 1024L * k
    val g = 1024L * m
    val t = 1024L * g

    if (size < k) {
      return "${size}B"
    }

    if (size < m) {
      return "${size / 1024L}KB"
    }

    if (size < g) {
      val mb = BigDecimal(size / m.toDouble())
      return mb.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }

    if (size < t) {
      val gb = BigDecimal(size / g.toDouble())
      return gb.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }

    val tb = BigDecimal(size / t.toDouble())
    return tb.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
  }

  /**
   * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) *
   * @param context
   */
  fun cleanInternalCache(context: Context) {
    deleteFilesByDirectory(context.cacheDir)
  }

  /**
   * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases)
   * @param context
   */
  fun cleanDatabases(context: Context) {
    deleteFilesByDirectory(File("/data/data/"
        + context.packageName + "/databases"))
  }

  /**
   * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs)
   * @param context
   */
  fun cleanSharedPreference(context: Context) {
    deleteFilesByDirectory(File("/data/data/"
        + context.packageName + "/shared_prefs"))
  }

  /**
   *  Clear data base of name
   */
  fun cleanDatabaseByName(context: Context, dbName: String?) {
    context.deleteDatabase(dbName)
  }

  /**
   * 清除/data/data/com.xxx.xxx/files下的内容
   * @param context
   */
  fun cleanFiles(context: Context) {
    deleteFilesByDirectory(context.filesDir)
  }

  /**
   * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
   * @param context
   */
  fun cleanExternalCache(context: Context) {
    if (Environment.getExternalStorageState() ==
        Environment.MEDIA_MOUNTED) {
      deleteFilesByDirectory(context.externalCacheDir)
    }
  }

  /**
   * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理
   * @param directory
   */
  private fun deleteFilesByDirectory(directory: File?) {
    if (directory != null && directory.exists() && directory.isDirectory) {
      for (item in directory.listFiles()) {
        item.delete()
      }
    }
  }
}