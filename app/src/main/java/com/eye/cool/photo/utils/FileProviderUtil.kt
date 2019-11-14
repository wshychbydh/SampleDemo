package com.eye.cool.photo.utils

import android.annotation.TargetApi
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

/**
 * Created by cool on 2018/6/12
 */
object FileProviderUtil {
  /**
   * Get the URI from the file
   *
   * @param context
   * @param file
   */
  fun uriFromFile(context: Context, file: File): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      FileProvider.getUriForFile(context, composeAuthority(context), file)
    } else {
      Uri.fromFile(file)
    }
  }

  private fun composeAuthority(context: Context): String {
    return context.packageName + ".FileProvider"
  }

  /**
   * Grant temporary access to files
   *
   * @param context
   * @param intent The intent to access the file
   * @param file access file
   */
  fun grantUriPermission(context: Context, intent: Intent, file: File): Uri {
    val uri = uriFromFile(context, file)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      val result = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
      result.forEach {
        context.grantUriPermission(it.activityInfo.packageName, uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
      }
    }
    return uri
  }

  /**
   * Sets the data and type of the Intent and gives the target temporary URI read and write permissions
   *
   * @param context
   * @param intent The intent to access the file
   * @param type The MIME type of the data being handled by this intent.
   * @param file
   * @param writeAble Whether to grant permissions to writable uris
   */
  fun setIntentDataAndType(context: Context, intent: Intent, type: String, file: File, writeAble: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      intent.setDataAndType(uriFromFile(context, file), type)
      //Temporarily grant read and write Uri permissions
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      if (writeAble) {
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
      }
    } else {
      intent.setDataAndType(Uri.fromFile(file), type)
    }
  }

  /**
   * Sets the data and type of the Intent and gives the target temporary URI read and write permissions
   *
   * @param intent
   * @param type The MIME type of the data being handled by this intent.
   * @param fileUri
   * @param writeAble Whether to grant permissions to writable uris
   */
  fun setIntentDataAndType(intent: Intent, type: String, fileUri: Uri, writeAble: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      intent.setDataAndType(fileUri, type)
      //Temporarily grant read and write Uri permissions
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      if (writeAble) {
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
      }
    } else {
      intent.setDataAndType(fileUri, type)
    }
  }

  /**
   * Get the absolute path to the image based on its Uri (multiple apis have been adapted)
   *
   * @param context
   * @param uri
   * @return If the image corresponding to the Uri exists,
   * then the absolute path to the image is returned,
   * otherwise null is returned
   */
  fun getPathFromUri(context: Context, uri: Uri): String? {
    if (ContentResolver.SCHEME_FILE == uri.scheme) {
      return uri.path
    } else {
      val sdkVersion = Build.VERSION.SDK_INT
      if (sdkVersion < 11) {
        // SDK < Api11
        return getRealPathFromUriBelowApi11(context, uri)
      }
      return if (sdkVersion < 19) {
        // SDK > 11 && SDK < 19
        getRealPathFromUriApi11To18(context, uri)
      } else getRealPathFromUriAboveApi19(context, uri)
      // SDK > 19
    }
  }

  /**
   * Api19 above, according to the uri to get the absolute path of the picture
   */
  @TargetApi(19)
  private fun getRealPathFromUriAboveApi19(context: Context, uri: Uri): String? {
    if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
      if (isCustomAuthority(context, uri)) {
        val path = uri.path ?: return null
        val name = path.substring(path.lastIndexOf("/"))
        return LocalStorage.composePhotoImageDir(context).append(name).toString()
      } else if (DocumentsContract.isDocumentUri(context, uri)) {
        val docId = DocumentsContract.getDocumentId(uri) ?: return null
        if (isExternalStorageDocument(uri)) {
          val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
          if ("primary".equals(split[0], ignoreCase = true)) {
            return "${Environment.getExternalStorageDirectory()}/" + split[1]
          }
        } else if (isDownloadsDocument(uri)) {
          val contentUri = ContentUris.withAppendedId(
              Uri.parse("content://downloads/public_downloads"), docId.toLong())
          return getRealPathFromUriBelowApi11(context, contentUri)
        } else if (isMediaDocument(uri)) {
          var filePath: String? = null
          // Use ':' to split
          val id = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

          val projection = arrayOf(MediaStore.Images.Media.DATA)
          val selection = MediaStore.Images.Media._ID + "=?"
          val selectionArgs = arrayOf(id)

          val cursor = context.contentResolver.query(
              MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
              selection, selectionArgs, null)
          val columnIndex = cursor!!.getColumnIndex(projection[0])

          if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
          }
          cursor.close()
          return filePath
        }
      } else {
        return getRealPathFromUriBelowApi11(context, uri)
      }
    }
    return null
  }

  private fun isCustomAuthority(context: Context, uri: Uri): Boolean {
    return composeAuthority(context) == uri.authority
  }

  private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
  }

  private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
  }

  private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
  }

  /**
   * Api11-api18, which gets the absolute path of the image based on the uri
   */
  private fun getRealPathFromUriApi11To18(context: Context, uri: Uri): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)

    val loader = CursorLoader(context, uri, projection, null, null, null)
    val cursor = loader.loadInBackground()

    if (cursor != null) {
      cursor.moveToFirst()
      filePath = cursor.getString(0)
      cursor.close()
    }
    return filePath
  }

  /**
   * Api11 is applied below (excluding api11), and the absolute path of the image is obtained according to the uri
   */
  private fun getRealPathFromUriBelowApi11(context: Context, uri: Uri): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
      cursor.moveToFirst()
      return it.getString(0)
    }
    return null
  }
}