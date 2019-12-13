package com.cool.eye.func.scan

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.activity_scan_result.*

class ScanResultActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_scan_result)

    scanResultIv.setImageBitmap(intent.getParcelableExtra(BITMAP))
    scanResultTv.text = intent.getStringExtra(CONTENT)
  }

  companion object {

    private const val BITMAP = "bitmap"
    private const val CONTENT = "content"

    fun launch(context: Context, bitmap: Bitmap, content: String) {
      val intent = Intent(context, ScanResultActivity::class.java)
      intent.putExtra(BITMAP, bitmap)
      intent.putExtra(CONTENT, content)
      context.startActivity(intent)
    }
  }
}
