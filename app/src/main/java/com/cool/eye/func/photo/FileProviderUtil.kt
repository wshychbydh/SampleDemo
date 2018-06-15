package com.cool.eye.func.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider

import java.io.File

/**
 * Created by cool on 2018/6/12
 */
object FileProviderUtil {
    /**
     * 从文件获得URI
     *
     * @param context 上下文
     * @param file    文件
     * @return 文件对应的URI
     */
    fun uriFromFile(context: Context, file: File): Uri {
        //7.0以上进行适配
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val p = context.packageName + ".FileProvider"
            FileProvider.getUriForFile(context, p, file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * 设置Intent的data和类型，并赋予目标程序临时的URI读写权限
     *
     * @param context   上下文
     * @param intent    意图
     * @param type      类型
     * @param file      文件
     * @param writeAble 是否赋予可写URI的权限
     */
    fun setIntentDataAndType(context: Context, intent: Intent, type: String, file: File, writeAble: Boolean) {
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(uriFromFile(context, file), type)
            //临时赋予读写Uri的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }

    /**
     * 设置Intent的data和类型，并赋予目标程序临时的URI读写权限
     *
     * @param intent    意图
     * @param type      类型
     * @param fileUri   文件uri
     * @param writeAble 是否赋予可写URI的权限
     */
    fun setIntentDataAndType(intent: Intent, type: String, fileUri: Uri, writeAble: Boolean) {
        //7.0以上进行适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(fileUri, type)
            //临时赋予读写Uri的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(fileUri, type)
        }
    }

    fun getPathByUri(activity: Activity, uri: Uri): String? {
        try {
            val uriStr = uri.toString().toLowerCase()

            val type = arrayOf(MediaStore.Images.Media.DATA)
            return if (uriStr.endsWith(".jpg") || uriStr.endsWith(".png") || uriStr.endsWith(".jpeg")) {
                uriStr
            } else {
                val cursor = activity.managedQuery(uri, type, null, null, null)
                val imgIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(imgIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}