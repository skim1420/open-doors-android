package com.vanesoftware.opendoors.layout;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.vanesoftware.opendoors.R;
import com.vanesoftware.opendoors.service.OdApi;

public class OpenDoorWidget extends AppWidgetProvider {

  private static final String LOG_TAG = OpenDoorWidget.class.getCanonicalName();
  private static final String OPEN_ACTION = "open door";

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    ComponentName thisWidget = new ComponentName(context, OpenDoorWidget.class);
    int[] widgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.od_widget_layout);

    Intent intent = new Intent(context, OpenDoorWidget.class);
    intent.setAction(OPEN_ACTION);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
        PendingIntent.FLAG_UPDATE_CURRENT);
    views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

    appWidgetManager.updateAppWidget(widgetIds, views);
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);
    String action = intent.getAction();
    if (action.equals(OPEN_ACTION)) {
      OdApi odapi = new OdApi();
      odapi.openDoor();
    }
  }

}
