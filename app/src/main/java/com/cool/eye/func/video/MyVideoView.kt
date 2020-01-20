package com.cool.eye.func.video

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
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
    private val activity: AppCompatActivity,
    private val bundle: Bundle
) : FrameLayout(activity), View.OnClickListener, CompoundButton.OnCheckedChangeListener,
    SeekBar.OnSeekBarChangeListener, Runnable, LifecycleObserver {

  private val videoView: VideoView
  private val videoContainer: ConstraintLayout
  private val videoDurationTv: TextView
  private val seekBar: SeekBar
  private val videoPauseCb: CheckBox
  private val videoPlayBtn: ImageButton

  private var isPaused = false

  private var isVideoPrepared = false

  private lateinit var duration: String

  private val orientationDetector = MyOrientationDetector(activity)
  var rotation = activity.windowManager.defaultDisplay.rotation

  private val portraitHeight = (resources.displayMetrics.density * 210f).toInt()

  init {
    setBackgroundColor(Color.WHITE)
    activity.lifecycle.addObserver(this)
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    activity.window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    val view = LayoutInflater.from(context).inflate(R.layout.video_controller_view, this, true)
    videoView = view.videoView
    videoContainer = view.videoContainer
    videoPauseCb = view.videoPlayCb
    videoPlayBtn = view.videoPlayBtn
    videoDurationTv = view.videoDurationTv
    seekBar = view.videoSeekBar
    seekBar.setOnSeekBarChangeListener(this)
    view.videoFullScreenIv.setOnClickListener(this)
    view.videoPlayBtn.setOnClickListener(this)
    videoPauseCb.setOnCheckedChangeListener(this)
    setupVideoView()
    onConfigurationChanged()
  }

  private fun setupVideoView() {
    val uri = Uri.parse("android.resource://${activity.packageName}/${R.raw.test}")
    videoView.setVideoURI(uri)
    // videoView.setMediaController(MediaController(activity))
//    videoView.setVideoURI(Uri.parse(bundle.getString(URL)
//        ?: throw IllegalArgumentException("URL can not be empty.")))
    videoView.setOnPreparedListener {
      isVideoPrepared = true
      seekBar.max = it.duration
      duration = formatTime(it.duration)
      it.seekTo(bundle.getInt(PROGRESS))
      start()
    }
    videoView.setOnCompletionListener {
      it.seekTo(0)
      seekBar.progress = 0
      videoPauseCb.isChecked = false
      videoDurationTv.text = "0:00/$duration"
    }

    videoView.setOnErrorListener { mp, _, _ ->
      mp.stop()
      mp.release()
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

  fun onSaveInstanceState(outState: Bundle) {
    outState.putInt(PROGRESS, videoView.currentPosition)
  }

  fun isWideScreen(): Boolean {
    val dm = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(dm)
    return orientationDetector.isWideScreen
  }

  private fun onConfigurationChanged() {
    videoContainer.layoutParams = if (!isWideScreen()) {
      LayoutParams(resources.displayMetrics.widthPixels, portraitHeight)
    } else {
      LayoutParams(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
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
    if (isChecked) {
      videoView.start()
      postDelayed(this, 60L)
      videoPlayBtn.visibility = View.GONE
    } else {
      videoView.pause()
      videoPlayBtn.visibility = View.VISIBLE
    }
  }

  fun start() {
    if (!isVideoPrepared) return
    videoPb.visibility = View.GONE
    if (videoView.currentPosition >= videoView.duration) {
      videoView.seekTo(0)
    }
    videoPauseCb.isChecked = true
  }

  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.videoPlayBtn -> {
        videoPauseCb.isChecked = true
      }
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
    if (!videoView.isPlaying) return
    val pos = videoView.currentPosition
    if (pos != seekBar.progress) {
      seekBar.progress = pos
    }
    videoDurationTv.text = String.format("%s/%s", formatTime(pos), duration)
    //播放sdy截取8秒的流视频的时候，会出现currentPosition大于duration的情况
    if (videoView.currentPosition < videoView.duration) {
      postDelayed(this, 60L)
    } else {
      videoPlayCb.isChecked = false
      videoView.seekTo(0)
      seekBar.progress = 0
      videoDurationTv.text = "0:00/$duration"
    }
  }

  override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    if (!fromUser) return
    videoView.seekTo(progress)
  }

  override fun onStartTrackingTouch(seekBar: SeekBar?) {
    //  videoPauseCb.isChecked = false
  }

  override fun onStopTrackingTouch(seekBar: SeekBar?) {
    //   videoPauseCb.isChecked = true
  }

  companion object {

    private fun formatTime(time: Int): String {

      val second = 1000
      val minute = 60 * second
      val hour = 60 * minute

      if (time >= hour) {
        val hours = time / hour
        val left = time - hours * hour
        val minutes = left / minute
        val seconds = (left - minutes * minute) / second
        return "${formatNumber(hours)}:${formatNumber(minutes)}:${formatNumber(seconds)}"
      }

      if (time >= minute) {
        val minutes = time / minute
        val seconds = (time - minutes * minute) / second
        return "${formatNumber(minutes)}:${formatNumber(seconds)}"
      }

      return "00:${formatNumber(time / second)}"
    }

    private fun formatNumber(number: Int): String {
      return if (number <= 9) {
        "0$number"
      } else "$number"
    }

    const val PROGRESS = "progress"
    const val URL = "url"
  }
}