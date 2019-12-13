package com.cool.eye.func.scan

import android.graphics.Bitmap
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Toast
import com.cool.eye.demo.R
import com.cool.eye.scan.CaptureActivity
import com.cool.eye.scan.listener.CaptureListener
import com.cool.eye.scan.view.CaptureView
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : CaptureActivity(), CaptureListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan)
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
