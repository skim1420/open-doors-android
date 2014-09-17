package com.vanesoftware.opendoors.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.vanesoftware.opendoors.layout.OpenDoorActivity;


public class NotificationUtils {

  public static void sendNotification(Context context, int id, int icon, String tickerText,
                                      String contentTitle, String contentText) {
    PendingIntent intent = PendingIntent.getActivity(context, id, new Intent(context, OpenDoorActivity.class), 0);
    NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setTicker(tickerText)
        .setSmallIcon(icon)
        .setAutoCancel(true)
        .setContentIntent(intent);
    NotificationManager manager = (NotificationManager) context.getSystemService(
        Context.NOTIFICATION_SERVICE);
    manager.notify(id, notification.build());
  }

}
