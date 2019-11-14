package com.eye.cool.photo

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.func.R
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.params.Params
import com.eye.cool.photo.support.OnClickListener
import com.eye.cool.photo.support.OnSelectListener
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {

  private lateinit var dialog: PhotoDialog

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo)
    val params = Params.Builder(this).build()

    params.imageParams.onSelectListener = object : OnSelectListener {
      override fun onSelect(path: String) {
        resultIv.setImageURI(Uri.parse(path))
      }
    }

    params.dialogParams.onCancelListener = DialogInterface.OnCancelListener {
      println("onCancelListener")
    }

    params.dialogParams.onShowListener = DialogInterface.OnShowListener {
      println("onShownListener")
    }

    params.dialogParams.onDismissListener = DialogInterface.OnDismissListener {
      println("onDismissListener")
    }

    params.dialogParams.onClickListener = object : OnClickListener {
      override fun onClick(which: Int) {
        println("onClicked--->$which")
      }
    }

    params.requestCameraPermission = true
    params.imageParams.cutAble = false

    val helper = PhotoHelper(this)

    dialog = PhotoDialog(params)

    button1.setOnClickListener {
      resultIv.setImageDrawable(null)
      dialog.show()
    }

    button2.setOnClickListener {
      resultIv.setImageDrawable(null)
      PhotoDialogFragment
          .Builder()
          .requestCameraPermission(true)
          .setImageParams(ImageParams.Builder()
              .setOnSelectListener(object : OnSelectListener {
                override fun onSelect(path: String) {
                  resultIv.setImageURI(Uri.parse(path))
                }
              }).build())
          .build()
          .show(fragmentManager)
    }

    button22.setOnClickListener {
      resultIv.setImageDrawable(null)
      com.eye.cool.photo.support.v4.PhotoDialogFragment
          .Builder()
          .requestCameraPermission(true)
          .setImageParams(ImageParams.Builder()
              .setOnSelectListener(object : OnSelectListener {
                override fun onSelect(path: String) {
                  resultIv.setImageURI(Uri.parse(path))
                }
              }).build())
          .build()
          .show(supportFragmentManager)
    }

    button3.setOnClickListener {
      resultIv.setImageDrawable(null)
      helper.onSelectAlbum(params.imageParams)
    }

    button4.setOnClickListener {
      resultIv.setImageDrawable(null)
      helper.onTakePhoto(params.imageParams, true)
    }

    button5.setOnClickListener {
      resultIv.setImageDrawable(null)
      PhotoPickerDialog.setDialogParams(params.dialogParams)
      PhotoPickerDialog.setImageParams(ImageParams.Builder()
          .setOnSelectListener(object : OnSelectListener {
            override fun onSelect(path: String) {
              resultIv.setImageURI(Uri.parse(path))
            }
          }).build())
      PhotoPickerDialog.show(this)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    dialog.onActivityResult(requestCode, resultCode, data)
  }
}
