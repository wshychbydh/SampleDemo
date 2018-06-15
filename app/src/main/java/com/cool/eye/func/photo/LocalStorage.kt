package com.cool.eye.func.photo

import java.io.File
import java.lang.StringBuilder

/**
 * Created by cool on 2018/3/9.
 */
object LocalStorage {
    /**
     * 文件夹根目录
     */

    private const val PHOTO = "photo" // 拍照
    private const val LOCAL_DIR = "photo"
    private const val PHOTO_PRE = "photo_"
    private const val IMAGE_SUFFIX = ".jpg"

    // create image storage path
    private fun composePhotoImage(name: String): String {
        val sb = composePhotoImageDir()
        sb.append(File.separator).append(name)
        return sb.toString()
    }

    // create image storage dir
    private fun composePhotoImageDir(): StringBuilder {
        val sb = StringBuilder()
        sb.append(android.os.Environment.getExternalStorageDirectory())
        sb.append(File.separator)
        sb.append(LOCAL_DIR)
        sb.append(File.separator)
        sb.append(PHOTO)
        return sb
    }

    // create image storage dir
    private fun composeThumbDir(): StringBuilder {
        val sb = StringBuilder()
        sb.append(android.os.Environment.getDataDirectory())
        sb.append(File.separator)
        sb.append(LOCAL_DIR)
        sb.append(File.separator)
        sb.append(PHOTO)
        return sb
    }

    @JvmStatic
    fun composePhotoImageFile(): String {
        val dir = File(composePhotoImageDir().toString())
        if (!dir.exists()) dir.mkdirs()
        return composePhotoImage("$PHOTO_PRE${System.currentTimeMillis()}$IMAGE_SUFFIX")
    }

    @JvmStatic
    fun composeThumbFile(): String {
        val dir = File(composeThumbDir().toString())
        if (!dir.exists()) dir.mkdirs()
        return composePhotoImage("$PHOTO_PRE${System.currentTimeMillis()}$IMAGE_SUFFIX")
    }
}