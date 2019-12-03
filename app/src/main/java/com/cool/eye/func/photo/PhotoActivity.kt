package com.cool.eye.func.photo

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.photo.PhotoDialog
import com.eye.cool.photo.PhotoDialogActivity
import com.eye.cool.photo.PhotoDialogFragment
import com.eye.cool.photo.PhotoHelper
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.params.Params
import com.eye.cool.photo.support.OnSelectListener
import com.eye.cool.photo.support.PermissionInvoker
import com.eye.cool.photo.utils.ImageUtil
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo)
  }

  fun selectUsePhotoDialog(v: View) {
    PhotoDialog.create(
        Params.Builder()
            .setImageParams(
                ImageParams.Builder()
                    .setOnSelectListener(object : OnSelectListener {
                      override fun onSelect(path: String) {
                        loadImage(path) {
                          resultIv.setImageBitmap(it)
                        }
                      }
                    })
                    .build()
            )
            .setPermissionInvoker(object : PermissionInvoker {
              override fun request(permissions: Array<String>, invoker: (Boolean) -> Unit) {
                PermissionHelper.Builder(this@PhotoActivity)
                    .permissions(permissions)
                    .permissionCallback(invoker)
                    .showRationaleWhenRequest(true)
                    .build()
                    .request()
              }
            })
            .requestCameraPermission(true) //manifest中声明了Camera，必须设置为true
            .setAuthority("test")
            .build()
    ).show(supportFragmentManager)
  }

  fun selectUsePhotoDialogFragment(v: View) {
    PhotoDialogFragment.create(
        ImageParams.Builder()
            .setCutAble(false)
            .setOnSelectListener(object : OnSelectListener {
              override fun onSelect(path: String) {
                loadImage(path) {
                  resultIv.setImageBitmap(it)
                }
              }
            })
            .build(),
        true,  //manifest中声明了Camera，必须设置为true
        null
    ).show(fragmentManager)
  }

  fun selectUsePhotoHelper(v: View) {
    val helper = PhotoHelper(this)
    val imageParams = ImageParams.Builder()
        .setCutAble(false)
        .setOnSelectListener(object : OnSelectListener {
          override fun onSelect(path: String) {
            loadImage(path) {
              resultIv.setImageBitmap(it)
            }
          }
        })
        .build()
    if (v?.id == R.id.picker_image) {
      helper.onSelectAlbum(imageParams)
    } else if (v?.id == R.id.take_photo) {
      //manifest中声明了Camera，必须设置为true
      helper.onTakePhoto(imageParams, true)
    }
  }

  fun selectUsePhotoActivity(v: View) {
    PhotoDialogActivity
        .setImageParams(ImageParams.Builder()
            .setOnSelectListener(object : OnSelectListener {
              override fun onSelect(path: String) {
                loadImage(path) {
                  resultIv.setImageBitmap(it)
                }
              }
            })
            .build())
        .requestCameraPermission(true) //manifest中声明了Camera，必须设置为true
        .show(this)
  }


  //only for test
  private fun loadImage(path: String, callback: (Bitmap) -> Unit) {
    Thread {
      val bitmap = ImageUtil.getBitmapFromFile(path)
      runOnUiThread {
        callback.invoke(bitmap)
      }
    }.start()
  }
}
