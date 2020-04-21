package com.cool.eye.func.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cool.eye.demo.R

/**
 *Created by ycb on 2019/12/24 0024
 */
object NotifyUtil {

  var CHANNEL_ID = "com.cool.eye.notify"

  const val NOTIFY = "notify"

  fun createNotification(
      context: Context,
      notifyId: Int,
      title: String,
      content: String,
      time: Long = System.currentTimeMillis(),
      cls: Class<*>
  ) {
    createIntentNotification(context, notifyId, title, content, time, Intent(context, cls))
  }

  fun createIntentNotification(
      context: Context,
      notifyId: Int,
      title: String,
      content: String,
      time: Long = System.currentTimeMillis(),
      intent: Intent
  ) {

    createNotificationChannel(context)

    //fixme 就一个activity
//    intent.apply {
//      flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//    }

    intent.putExtra(NOTIFY, true)

    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, notifyId, intent, 0)

    val builder = NotificationCompat.Builder(context, "warn")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setTicker(title)
        .setSubText(title)
        .setContentTitle(title)
        .setOngoing(true)
        .setDefaults(Notification.DEFAULT_ALL)
        .setContentText(content)
        .setWhen(time)
        .setStyle(NotificationCompat.BigTextStyle().bigText(content))
        .setPriority(NotificationCompat.PRIORITY_MAX)
        //     .setContentIntent(pendingIntent)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .setFullScreenIntent(pendingIntent, true)
        .setAutoCancel(true)
    with(NotificationManagerCompat.from(context)) {
      // notificationId is a unique int for each notification that you must define
      notify(notifyId, builder.build())
    }
  }

  fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val name = context.getString(R.string.app_name)
      val descriptionText = context.getString(R.string.app_notify_description)
      val importance = NotificationManager.IMPORTANCE_HIGH
      val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
      }
      // Register the channel with the system
      val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      notificationManager.createNotificationChannel(channel)
    }
  }

  fun areNotificationsEnabled(context: Context): Boolean {
    val notifyManager = NotificationManagerCompat.from(context.applicationContext)
    return notifyManager.areNotificationsEnabled()
  }

  fun openNotificationSettingsForApp(context: Context) {
    try {
      // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
      val intent = Intent()
      intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
      //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
      if (Build.VERSION.SDK_INT >= 26) {
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        intent.putExtra(Notification.EXTRA_CHANNEL_ID, context.applicationInfo.uid)
      } else if (Build.VERSION.SDK_INT >= 21) {
        //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
        intent.putExtra("app_package", context.packageName)
        intent.putExtra("app_uid", context.applicationInfo.uid)
      }
      context.startActivity(intent)
    } catch (e: Exception) {
      // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
      val intent = Intent()
      //下面这种方案是直接跳转到当前应用的设置界面。
      intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
      val uri = Uri.fromParts("package", context.packageName, null)
      intent.data = uri
      context.startActivity(intent)
    }
  }
}