package com.cool.eye.func.photo

import android.app.Activity
import android.app.Dialog
import android.app.Fragment
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.cool.eye.func.R

/**
 * Created by cool on 18-3-9
 */
class PhotoDialog : Dialog {

    private var photoHelper: PhotoHelper
    private var view: View? = null

    private constructor(fragment: android.support.v4.app.Fragment, style: Int = R.style.CustomDialog) : super(fragment.context, style) {
        photoHelper = PhotoHelper(ContextWrapper(fragment))
    }

    private constructor(activity: Activity, style: Int = R.style.CustomDialog) : super(activity, style) {
        photoHelper = PhotoHelper(ContextWrapper(activity))
    }

    private constructor(fragment: Fragment, style: Int = R.style.CustomDialog) : super(fragment.activity, style) {
        photoHelper = PhotoHelper(ContextWrapper(fragment))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoHelper.setClickListener {
            dismiss()
        }
        if (view == null) {
            view = DefaultView(context)
            (view as DefaultView).setPhotoListener(photoHelper)
        } else {
            val method = view!!.javaClass.getDeclaredMethod("setPhotoListener", IPhotoListener::class.java)
                    ?: throw IllegalArgumentException("Custom View must has method setPhotoListener(IPhotoListener)")
            method.invoke(view, photoHelper)
        }
        setContentView(view)
        setParams()
    }

    private fun setParams() {
        val window = window
        window!!.setWindowAnimations(R.style.PopupWindowAnimation)
        val layoutParams = window.attributes
        val dm = context.resources.displayMetrics
        layoutParams.x = 0
        layoutParams.y = dm.heightPixels
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        onWindowAttributesChanged(layoutParams)
    }

    class Builder {
        private var dialog: PhotoDialog

        constructor(fragment: android.support.v4.app.Fragment) {
            dialog = PhotoDialog(fragment)
        }

        constructor(fragment: android.support.v4.app.Fragment, style: Int) {
            dialog = PhotoDialog(fragment, style)
        }

        constructor(activity: Activity) {
            dialog = PhotoDialog(activity)
        }

        constructor(activity: Activity, style: Int) {
            dialog = PhotoDialog(activity, style)
        }

        constructor(fragment: Fragment) {
            dialog = PhotoDialog(fragment)
        }

        constructor(fragment: Fragment, style: Int) {
            dialog = PhotoDialog(fragment, style)
        }

        fun setContentView(view: View): Builder {
            dialog.view = view
            return this
        }

        fun setRatio(ratio: Float): Builder {
            dialog.photoHelper.setRatio(ratio)
            return this
        }

        fun setPhotoListener(listener: ((bitmap: Bitmap) -> Unit)): Builder {
            dialog.photoHelper.setPhotoListener(listener)
            return this
        }

        fun setUploadListener(listener: (fileUrl: String) -> Unit): Builder {
            dialog.photoHelper.setUploadListener(listener)
            return this
        }

        fun setCut(cut: Boolean): Builder {
            dialog.photoHelper.isCut(cut)
            return this
        }

        fun build(): PhotoDialog {
            return dialog
        }
    }

    /**
     * 拍照后回调接口，在相应的OnActivityResult里面调用，必须手动调用
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        photoHelper.onActivityResult(requestCode, resultCode, intent)
    }
}