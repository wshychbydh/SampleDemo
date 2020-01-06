package com.cool.eye.func.video

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.cool.eye.demo.R
import kotlinx.android.synthetic.main.video_controller_view.view.*

/**
 *Created by ycb on 2020/1/6 0006
 */
class MyVideoView @JvmOverloads constructor(
    private val activity: AppCompatActivity
) : ConstraintLayout(activity), View.OnClickListener, CompoundButton.OnCheckedChangeListener,
    SeekBar.OnSeekBarChangeListener, Runnable, LifecycleObserver {

  private val videoView: VideoView
  private val seekBar: SeekBar
  private val videoPauseCb: CheckBox

  private var isPaused = false

  private val orientationDetector = MyOrientationDetector(activity)
  var rotation = activity.windowManager.defaultDisplay.rotation

  init {
    setBackgroundColor(Color.RED)
    activity.lifecycle.addObserver(this)
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    activity.window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    val view = LayoutInflater.from(context).inflate(R.layout.video_controller_view, this, true)
    videoView = view.videoView
    videoPauseCb = view.videoPauseCb
    seekBar = view.videoSeekBar
    seekBar.setOnSeekBarChangeListener(this)
    view.videoFullScreenIv.setOnClickListener(this)
    videoPauseCb.setOnCheckedChangeListener(this)
    setupVideoView()
    onConfigurationChanged()
  }

  private fun setupVideoView() {
    val uri = Uri.parse("android.resource://${activity.packageName}/${R.raw.test}")
    videoView.setVideoURI(uri)
    // videoView.setMediaController(MediaController(activity))
    // videoView.setVideoURI(Uri.parse("http://valipl.cp31.ott.cibntv.net/6773662aeb835715bdcf34907/03000801005E009DD6EC958003E880C52CFC16-B9E4-4568-8FBC-E56C4F7C5A2F.mp4?ccode=0502&duration=135&expire=18000&psid=7ee287e139e9d0862e7d9c98fedf5747&ups_client_netip=ab582d49&ups_ts=1578292321&ups_userid=null&utid=Sc3SFVp46UECAbaLhmtCHxHJ&vid=XMzg5MjgwMjgxMg&vkey=A221738d06f267b9c7dfd392650f65819"))
    videoView.setOnPreparedListener {
      seekBar.progress = 0
      seekBar.max = it.duration
      videoPauseCb.isChecked = true
    }
    videoView.setOnCompletionListener {
      videoPauseCb.setOnCheckedChangeListener(null)
      removeCallbacks(this)
      videoPauseCb.isChecked = false
      seekBar.progress = 0
      videoPauseCb.setOnCheckedChangeListener(this)
    }
    videoView.setOnErrorListener { mp, what, extra ->
      videoPauseCb.visibility = View.GONE
      return@setOnErrorListener false
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  private fun onResume() {
    val currentRotation = activity.windowManager.defaultDisplay.rotation
    if (rotation != currentRotation) {
      rotation = currentRotation
      onConfigurationChanged()
    }
    if (isPaused) {
      isPaused = false
      videoPauseCb.isChecked = true
    }
    orientationDetector.enable()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  private fun onPause() {
    isPaused = true
    videoPauseCb.isChecked = false
    orientationDetector.disable()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  private fun onStop() {
    videoView.stopPlayback()
  }

  private fun onConfigurationChanged() {
    val dm = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(dm)
    val isWideScreen = orientationDetector!!.isWideScreen
    //竖屏
    layoutParams = if (!isWideScreen) {
      //竖屏调整播放区域大小，宽全屏，高根据视频分辨率自适应
      FrameLayout.LayoutParams(dm.widthPixels, -2)
    } else {
      //横屏屏调整播放区域大小，宽、高均全屏，播放区域根据视频分辨率自适应
      FrameLayout.LayoutParams(dm.widthPixels, dm.heightPixels)
    }
  }

  fun onBackPressed(): Boolean {
    val rotation = activity.windowManager.defaultDisplay.rotation
    return if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
      activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
      true
    } else {
      false
    }
  }

  override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
    removeCallbacks(this)
    if (isChecked) {
      videoView.start()
      postDelayed(this, 60L)
    } else {
      videoView.pause()
    }
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.videoFullScreenIv -> {
        val rotation = activity.windowManager.defaultDisplay.rotation
        activity.requestedOrientation = if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
          ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
          ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
      }
    }
  }

  override fun run() {
    videoView.currentPosition
    val pos = videoView.currentPosition
    if (pos != seekBar.progress) {
      seekBar.progress = pos
    }
    if (videoView.isPlaying) {
      postDelayed(this, 60L)
    }
  }

  override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    if (!fromUser) return
    videoView.seekTo(progress)
  }

  override fun onStartTrackingTouch(seekBar: SeekBar?) {
    videoPauseCb.isChecked = false
  }

  override fun onStopTrackingTouch(seekBar: SeekBar?) {
    videoPauseCb.isChecked = true
  }
}