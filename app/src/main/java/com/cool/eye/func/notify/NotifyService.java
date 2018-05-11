package com.cool.eye.func.notify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

public class NotifyService extends NotificationListenerService {

    public static final String SEND_WX_BROADCAST = "SEND_WX_BROADCAST";

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NotifyService.this, " NotificationListenerService onCreate, " +
                        "notification " +
                        getActiveNotifications().length, Toast.LENGTH_SHORT).show();
                Log.d("AAA", "============ NotificationListenerService onCreate, notification " +
                        "count ：" + getActiveNotifications().length);
            }
        }, 1000);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Toast.makeText(this, "onNotificationPosted" + sbn, Toast.LENGTH_SHORT).show();
        Log.d("AAA", "onNotificationPosted==============>" + sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Toast.makeText(this, "onNotificationRemoved" + sbn, Toast.LENGTH_SHORT).show();
        Log.d("AAA", "onNotificationRemoved==============>" + sbn);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        Toast.makeText(this, "onNotificationPosted" + sbn, Toast.LENGTH_SHORT).show();
        System.out.println("RankingMap====>" + sbn);
        System.out.println("RankingMap====>" + rankingMap);
        Log.d("AAA", "======2====onNotificationPosted   ID :"
                + sbn.getId() + "\t"
                + sbn.getNotification().tickerText + "\t"
                + sbn.getPackageName());
        String packageName = sbn.getPackageName();
        Intent intent = new Intent();
        intent.setAction(SEND_WX_BROADCAST);
        Bundle bundle = new Bundle();
        bundle.putString("packageName", packageName);
        intent.putExtras(bundle);
        this.sendBroadcast(intent);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        Toast.makeText(this, "onNotificationRemoved" + sbn, Toast.LENGTH_SHORT).show();
        Log.d("AAA", "=4==onNotificationRemoved   ID :"
                + sbn.getId() + "\t"
                + sbn.getNotification().tickerText
                + "\t" + sbn.getPackageName());
    }
}