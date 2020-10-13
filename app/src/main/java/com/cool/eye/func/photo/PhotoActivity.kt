package com.cool.eye.func.photo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.photo.PhotoDialog
import com.eye.cool.photo.PhotoDialogActivity
import com.eye.cool.photo.PhotoDialogFragment
import com.eye.cool.photo.PhotoHelper
import com.eye.cool.photo.expand.select
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.params.Params
import com.eye.cool.photo.utils.ImageUtil
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.coroutines.*

class PhotoActivity : AppCompatActivity() {

  private val scope = MainScope()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo)
  }

  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
  }

  fun selectUseCoroutine(v: View) {
    scope.launch(Dispatchers.Default) { //测试任意线程调用
      val result = select(
          Params.Builder()
              .requestCameraPermission(true)//manifest中声明了Camera，必须设置为true
              .build()
      )
      val bitmap = ImageUtil.getBitmapFromFile(result)
      withContext(Dispatchers.Main) {
        resultIv.setImageBitmap(bitmap)
      }
    }
  }

  fun selectUsePhotoDialog(v: View) {
    PhotoDialog.create(
        Params.Builder()
            .setImageParams(
                ImageParams.Builder()
                    .setOnSelectListener(object : ImageParams.OnSelectListener {
                      override fun onSelect(path: String) {
                        loadImage(path)
                      }
                    })
                    .build()
            )
            .setPermissionInvoker(object : Params.PermissionInvoker {
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
            .build()
    ).show(supportFragmentManager)
  }

  fun selectUsePhotoDialogFragment(v: View) {
    PhotoDialogFragment.create(
        ImageParams.Builder()
            .setCutAble(false)
            .setOnSelectListener(object : ImageParams.OnSelectListener {
              override fun onSelect(path: String) {
                loadImage(path)
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
        .setOnSelectListener(object : ImageParams.OnSelectListener {
          override fun onSelect(path: String) {
            loadImage(path)
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
            .setOnSelectListener(object : ImageParams.OnSelectListener {
              override fun onSelect(path: String) {
                loadImage(path)
              }
            })
            .build())
        .requestCameraPermission(true) //manifest中声明了Camera，必须设置为true
        .show(this)
  }


  //only for test
  private fun loadImage(path: String) {
    scope.launch {
      val bitmap = withContext(Dispatchers.Default) { ImageUtil.getBitmapFromFile(path) }
      resultIv.setImageBitmap(bitmap)
    }
  }
}
