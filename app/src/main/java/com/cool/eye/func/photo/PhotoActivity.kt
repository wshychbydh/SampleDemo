package com.cool.eye.func.photo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import com.cool.eye.func.dialog.toast.ToastHelper
import com.eye.cool.permission.PermissionHelper
import com.eye.cool.photo.PhotoDialog
import com.eye.cool.photo.PhotoDialogActivity
import com.eye.cool.photo.PhotoDialogFragment
import com.eye.cool.photo.PhotoHelper
import com.eye.cool.photo.expand.Photo.select
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.photo.params.Params
import com.eye.cool.photo.support.Action
import com.eye.cool.photo.utils.ImageUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_photo.*
import kotlinx.coroutines.*

class PhotoActivity : AppCompatActivity() {

  private val scope = MainScope()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_photo)
    selectUseCoroutineBtn.setOnClickListener { selectUseCoroutine(it) }
  }

  override fun onDestroy() {
    super.onDestroy()
    scope.cancel()
  }

  fun selectUseCoroutine(v: View) {
    scope.launch(Dispatchers.Default) { //测试任意线程调用
      val result = select(
          this@PhotoActivity,
          imageParams = ImageParams.build { cutAble = true },
          requestCameraPermission = true, //manifest中声明了Camera，必须设置为true
          onActionListener = object : Params.OnActionListener {
            override fun onAction(action: Int) {
              if (action == Action.PERMISSION_DENIED) {
                //如果需要处理具体的权限，可以自定义的权限请求器：params.permissionInvoker
                ToastHelper.showToast(this@PhotoActivity, "缺少相应权限!")
              }
            }
          }
      )
      val bitmap = ImageUtil.getBitmapFromFile(result)
      withContext(Dispatchers.Main) {
        resultIv.setImageBitmap(bitmap)
      }
    }
  }

  fun selectUsePhotoDialog(v: View) {
    PhotoDialog.create(
        Params.build {
          onSelectListener = object : Params.OnSelectListener {
            override fun onSelect(path: String) {
              loadImage(path)
            }
          }
          imageParams = ImageParams.build { cutAble = false }
          permissionInvoker = object : Params.PermissionInvoker {
            override fun request(permissions: Array<String>, invoker: (Boolean) -> Unit) {
              PermissionHelper.Builder(this@PhotoActivity)
                  .permissions(permissions)
                  .permissionCallback(invoker)
                  .build()
                  .request()
            }
          }
          requestCameraPermission = true  //manifest中声明了Camera，必须设置为true
        }
    ).show(supportFragmentManager)
  }

  fun selectUsePhotoDialogFragment(v: View) {
    PhotoDialogFragment.create(
        Params.build {
          onSelectListener = object : Params.OnSelectListener {
            override fun onSelect(path: String) {
              loadImage(path)
            }
          }
          imageParams = ImageParams.build { cutAble = false }
          requestCameraPermission = true //manifest中声明了Camera，必须设置为true
        }
    ).show(fragmentManager)
  }

  fun selectUsePhotoHelper(v: View) {
    val helper = PhotoHelper(this)
    val imageParams = ImageParams.build { cutAble = false }
    val onSelectListener = object : Params.OnSelectListener {
      override fun onSelect(path: String) {
        loadImage(path)
      }
    }
    if (v?.id == R.id.picker_image) {
      helper.onSelectAlbum(onSelectListener, imageParams)
    } else if (v?.id == R.id.take_photo) {
      //manifest中声明了Camera，必须设置为true
      helper.onTakePhoto(onSelectListener, imageParams, true)
    }
  }

  fun selectUsePhotoActivity(v: View) {
    val params = Params.build {
      onSelectListener = object : Params.OnSelectListener {
        override fun onSelect(path: String) {
          loadImage(path)
        }
      }
      imageParams = ImageParams.build { cutAble = false }
      requestCameraPermission = true
    }
    PhotoDialogActivity.show(this, params)
  }


  //only for test
  private fun loadImage(path: String) {
    scope.launch {
      val bitmap = withContext(Dispatchers.Default) { ImageUtil.getBitmapFromFile(path) }
      resultIv.setImageBitmap(bitmap)
    }
  }
}
