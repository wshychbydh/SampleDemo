package com.cool.eye.func.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.cool.eye.demo.R;
import com.cool.eye.func.address.view.AddressActivity;
import com.cool.eye.func.notify.util.Util;

public class NotifyActivity extends AppCompatActivity implements MyMessage {

  private ComeWxMessage comeWxMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notify);
    //    ComeWxMessage.gotoNotificationAccessSetting(this);
    comeWxMessage = new ComeWxMessage(this, this);
    comeWxMessage.toggleNotificationListenerService();
    comeWxMessage.openSetting();
  }

  @Override
  public void comePhone() {
    Toast.makeText(this, "回调中，收到来电", Toast.LENGTH_SHORT).show();
    Log.d("AAA", "====回调中，收到来电===");
  }

  @Override
  public void comeShortMessage() {
    Toast.makeText(this, "回调中，收到短信消息", Toast.LENGTH_SHORT).show();
    Log.d("AAA", "====回调中，收到短信消息===");
  }

  @Override
  public void comeWxMessage() {
    Toast.makeText(this, "回调中，收到微信消息", Toast.LENGTH_SHORT).show();
    Log.d("AAA", "====回调中，收到微信消息===");
  }

  @Override
  public void comeQQmessage() {
    Toast.makeText(this, "回调中，收到QQ消息", Toast.LENGTH_SHORT).show();
    Log.d("AAA", "====回调中，收到QQ消息===");
    Util.startAlarm(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    comeWxMessage.unRegistBroadcast();
  }

  public void createNotify(View view) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    builder.setSmallIcon(R.mipmap.ic_launcher);
    builder.setContentTitle("标题");
    builder.setContentText("内存");
    builder.setAutoCancel(true);
    builder.setOnlyAlertOnce(true);

    // 需要VIBRATE权限
    //   builder.setDefaults(Notification.DEFAULT_VIBRATE);
    builder.setPriority(Notification.PRIORITY_DEFAULT);

    // Creates an explicit intent for an Activity in your app
    //自定义打开的界面
    Intent resultIntent = new Intent(this, AddressActivity.class);
    resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setContentIntent(contentIntent);

    NotificationManager notificationManager = (NotificationManager) this
        .getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification = builder.build();
    notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;
    notificationManager.notify(0, notification);
  }
}