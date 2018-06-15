package com.cool.eye.func.notify.util

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException
import android.media.RingtoneManager
import android.net.Uri


/**
 *Created by cool on 2018/5/29
 */
object Util {

    @JvmStatic
    fun startAlarm(context: Context) {
        val mediaPlayer = MediaPlayer.create(context, getSystemDefaultRingtoneUri(context))
        mediaPlayer.isLooping = false
        try {
            mediaPlayer.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mediaPlayer.start()
    }

    //获取系统默认铃声的Uri
    private fun getSystemDefaultRingtoneUri(context: Context): Uri {
        return RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE)
    }
}