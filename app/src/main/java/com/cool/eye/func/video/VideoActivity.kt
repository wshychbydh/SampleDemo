package com.cool.eye.func.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

  private lateinit var videoView: MyVideoView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val bundle = savedInstanceState ?: Bundle()
    bundle.putString(MyVideoView.URL, intent.getStringExtra(URL))
    videoView = MyVideoView(this, bundle)
    if (videoView.isWideScreen()) {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
    setContentView(videoView, ViewGroup.LayoutParams(-1, -1))
  }

  override fun onBackPressed() {
    if (!videoView.onBackPressed()) {
      super.onBackPressed()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    videoView.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  companion object {

    private const val URL = "url"

    fun launch(context: Context, url: String) {
      val intent = Intent(context, VideoActivity::class.java)
      intent.putExtra(URL, url)
      context.startActivity(intent)
    }
  }
}
