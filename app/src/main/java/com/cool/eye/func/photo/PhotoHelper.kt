package com.cool.eye.func.photo

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import com.cool.eye.func.R
import com.cool.eye.func.permission.Permission
import com.cool.eye.func.permission.PermissionHelper
import java.io.File

/**
 *Created by cool on 2018/6/12
 */
class PhotoHelper(private val contextWrapper: ContextWrapper) : IPhotoListener {

    private var photoListener: ((bitmap: Bitmap) -> Unit)? = null
    private var clickListener: (() -> Unit)? = null
    private var isCut = true // 是否剪切
    private var ratio = 1f // 剪切照片时x/y的比例
    private var uploadListener: ((fileUrl: String) -> Unit)? = null

    private var uri: Uri? = null
    private var outputFile: File? = null
    private var photoFile: File? = null

    fun setRatio(ratio: Float) {
        this.ratio = ratio
    }

    fun setPhotoListener(listener: ((bitmap: Bitmap) -> Unit)) {
        this.photoListener = listener
    }

    fun setClickListener(listener: (() -> Unit)) {
        clickListener = listener
    }

    fun setUploadListener(listener: (fileUrl: String) -> Unit) {
        this.uploadListener = listener
    }

    fun isCut(cut: Boolean) {
        this.isCut = cut
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> {
                if (resultCode == SURE) {
                    //拍照完成，进行图片裁切
                    uri = FileProviderUtil.uriFromFile(contextWrapper.getActivity(), photoFile!!)
                    if (isCut) {
                        adjustPhotoSize()
                    } else {
                        onPhotoReady()
                    }
                }
            }
            SELECT_ALBUM -> {
                if (resultCode == SURE) {
                    uri = intent?.data
                    if (isCut) {
                        adjustPhotoSize()
                    } else {
                        onPhotoReady()
                    }
                }
            }
            ADJUST_PHOTO -> {
                uri = Uri.fromFile(outputFile)
                onPhotoReady()
            }
        }
    }

    override fun onTakePhoto() {
        // android 7.0系统解决拍照的问题
        //                val builder = StrictMode.VmPolicy.Builder()
        //                StrictMode.setVmPolicy(builder.build())
        //                builder.detectFileUriExposure()
        PermissionHelper.Builder(contextWrapper.getActivity())
                .permission(Permission.Group.CAMERA)
                .permissionCallback {
                    if (it) {
                        photoFile = File(LocalStorage.composePhotoImageFile())
                        PhotoUtil.takePhoto(contextWrapper, photoFile!!)
                    } else {
                        Toast.makeText(contextWrapper.getActivity(), contextWrapper.getActivity().getString(R.string.permission_take_photos), Toast.LENGTH_SHORT).show()
                    }
                }
                .build()
                .request()
        clickListener?.invoke()
    }

    override fun onSelectAlbum() {
        PermissionHelper.Builder(contextWrapper.getActivity())
                .permission(Permission.Group.STORAGE)
                .permissionCallback {
                    if (it) {
                        PhotoUtil.takeAlbum(contextWrapper)
                    } else {
                        Toast.makeText(contextWrapper.getActivity(), contextWrapper.getActivity().getString(R.string.permission_storage), Toast.LENGTH_SHORT).show()
                    }
                }
                .build()
                .request()
        clickListener?.invoke()
    }

    override fun onCancel() {
        clickListener?.invoke()
    }

    private fun onPhotoReady() {
        photoListener?.invoke(BitmapFactory.decodeStream(contextWrapper.getActivity().contentResolver.openInputStream(uri)))
        val fileUrl = FileProviderUtil.getPathByUri(contextWrapper.getActivity(), uri!!)
        if (fileUrl.isNullOrEmpty()) {
            Toast.makeText(contextWrapper.getActivity(), "Error path '${uri!!.path}'", Toast.LENGTH_SHORT).show()
        } else {
            uploadListener?.invoke(fileUrl!!)
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    private fun adjustPhotoSize() {
        outputFile = File(LocalStorage.composeThumbFile())
        PhotoUtil.cut(contextWrapper, this.uri!!, outputFile!!)
    }

    companion object {
        /**
         * 拍照标识
         */
        const val TAKE_PHOTO = 2001
        const val SELECT_ALBUM = 2002
        const val ADJUST_PHOTO = 2003

        const val SURE = -1 // 确定拍照/选照片
        const val CANCEL = 0 // 取消
    }
}