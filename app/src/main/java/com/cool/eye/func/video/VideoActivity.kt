package com.cool.eye.func.video

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

class VideoActivity : AppCompatActivity() {

  private lateinit var videoView: MyVideoView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    videoView = MyVideoView(this)
    setContentView(videoView, ViewGroup.LayoutParams(-1, -2))
  }

  override fun onBackPressed() {
    if (!videoView.onBackPressed()) {
      super.onBackPressed()
    }
  }

//  private val runnable = SeekRunnable()
//  private val seekHandler = Handler()
//  private lateinit var orientationDetector: MyOrientationDetector
//
//  private var isPaused = false
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
//    window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_video)
//
//    setupVideoView()
//
//    setSurfaceSize()
//  }
//
//  private fun setupVideoView() {
//    orientationDetector = MyOrientationDetector(this)
//    val uri = Uri.parse("android.resource://${packageName}/${R.raw.test}")
//    videoView.setVideoURI(uri)
//    videoView.setMediaController(MediaController(this))
//    // videoView.setVideoURI(Uri.parse("http://valipl.cp31.ott.cibntv.net/6773662aeb835715bdcf34907/03000801005E009DD6EC958003E880C52CFC16-B9E4-4568-8FBC-E56C4F7C5A2F.mp4?ccode=0502&duration=135&expire=18000&psid=7ee287e139e9d0862e7d9c98fedf5747&ups_client_netip=ab582d49&ups_ts=1578292321&ups_userid=null&utid=Sc3SFVp46UECAbaLhmtCHxHJ&vid=XMzg5MjgwMjgxMg&vkey=A221738d06f267b9c7dfd392650f65819"))
//    videoView.setOnPreparedListener {
//      println("onPrepared")
//      it.start()
//      seekBar.max = it.duration
//      seekHandler.postDelayed(runnable, 300)
//    }
//    videoView.setOnCompletionListener {
//      println("onComplete")
//      seekHandler.removeCallbacks(runnable)
//    }
//    videoView.setOnErrorListener { mp, what, extra ->
//      println("onError-->$what ; $extra")
//      seekHandler.removeCallbacks(runnable)
//      return@setOnErrorListener false
//    }
//    if (Build.VERSION.SDK_INT >= 17) {
//      videoView.setOnInfoListener { mp, what, extra ->
//        println("onInfo-->$what ; $extra")
//        return@setOnInfoListener false
//      }
//    }
//
//    fullIv.setOnClickListener {
//      val rotation = windowManager.defaultDisplay.rotation
//      requestedOrientation = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
//        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//      } else {
//        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//      }
//    }
//  }
//
//  private inner class SeekRunnable : Runnable {
//    override fun run() {
//      seekBar.progress = videoView.currentPosition
//      seekHandler.postDelayed(this, 300)
//    }
//  }
//
//  override fun onResume() {
//    super.onResume()
//    if (isPaused) {
//      videoView.start()
//      isPaused = false
//    }
//    orientationDetector.enable()
//  }
//
//  override fun onPause() {
//    super.onPause()
//    orientationDetector.disable()
//  }
//
//  override fun onStop() {
//    super.onStop()
//    if (videoView.isPlaying) {
//      isPaused = true
//    }
//    videoView.stopPlayback()
//  }
//
//  override fun onConfigurationChanged(newConfig: Configuration) {
//    super.onConfigurationChanged(newConfig)
//    setSurfaceSize()
//  }
//
//  private fun setSurfaceSize() {
//    val dm = DisplayMetrics()
//    windowManager.defaultDisplay.getMetrics(dm)
//    val isWideScreen = orientationDetector!!.isWideScreen
//    //竖屏
//    if (!isWideScreen) {
//      //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
//      videoLayout.layoutParams = FrameLayout.LayoutParams(dm.widthPixels, -2)
//    } else {
//      //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
//      videoLayout.layoutParams = FrameLayout.LayoutParams(dm.widthPixels, dm.heightPixels)
//    }
//  }
//
//  override fun onBackPressed() {
//    val rotation = windowManager.defaultDisplay.rotation
//    if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
//      requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//    } else {
//      super.onBackPressed()
//    }
//  }
}
