package com.cool.eye.func.scan

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import com.cool.eye.demo.R
import com.eye.cool.photo.PhotoHelper
import com.eye.cool.photo.params.ImageParams
import com.eye.cool.scan.decode.DecodeActivity
import com.eye.cool.scan.decode.DecodeParams
import com.eye.cool.scan.decode.listener.DecodeListener
import com.eye.cool.scan.decode.supprot.DecodeException
import kotlinx.android.synthetic.main.scan_layout.*

class ScanActivity : DecodeActivity(), DecodeListener, CompoundButton.OnCheckedChangeListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.scan_layout)

    scanAlbumIv.setOnClickListener {
      PhotoHelper(this)
          .onSelectAlbum(
              ImageParams.Builder()
                  .setOnSelectListener(object : ImageParams.OnSelectListener {
                    override fun onSelect(path: String) {
                      parseImage(path)
                    }
                  })
                  .build()
          )
    }

    scanFlashlightCb.setOnCheckedChangeListener(this)
  }

  override fun getDecodeParams() = DecodeParams.Builder()
      .captureView(scanCaptureView)
      .surfaceView(scanSurfaceView)
      .decodeListener(this)
      .permissionChecker(this)
      .scaleBitmap()
      .build()

  override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
    val result = if (isChecked) {
      enableFlashlight()
    } else {
      disableFlashlight()
    }
    if (!result) {
      scanFlashlightCb.setOnCheckedChangeListener(null)
      scanFlashlightCb.isChecked = scanFlashlightCb.isChecked
      scanFlashlightCb.setOnCheckedChangeListener(this)
    }
  }

  override fun onScanSucceed(bitmap: Bitmap, content: String) {
    ScanResultActivity.launch(this@ScanActivity, bitmap, content)
  }

  override fun onScanFailed(error: DecodeException) {
    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
  }
}
