package com.cool.eye.func.scan

import android.graphics.Bitmap
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Toast
import com.cool.eye.demo.R
import com.eye.cool.photo.PhotoHelper
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.scan.CaptureActivity
import com.eye.cool.scan.listener.CaptureListener
import com.eye.cool.scan.view.CaptureView
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : CaptureActivity(), CaptureListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan)

    imagePicker.setOnClickListener {
      PhotoHelper(this)
          .onSelectAlbum(
              ImageParams.Builder()
                  .setOnSelectListener(object : ImageParams.OnSelectListener {
                    override suspend fun onSelect(path: String) {
                      parseImage(path)
                    }
                  })
                  .build()
          )
    }

    flashlight.setOnClickListener {
      toggleFlashlight()
    }
  }

  override fun getCaptureListener(): CaptureListener = this

  override fun getCaptureView(): CaptureView = captureView

  override fun getSurfaceView(): SurfaceView = surfaceView

  override fun onScanSucceed(bitmap: Bitmap, content: String) {
    ScanResultActivity.launch(this, bitmap, content)
  }

  override fun onScanFailed(throwable: Throwable) {
    Toast.makeText(this, throwable.message ?: "扫码失败!", Toast.LENGTH_SHORT).show()
  }
}
