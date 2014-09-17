package com.vanesoftware.opendoors.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.vanesoftware.opendoors.R;


public class ReceiveTransitionsIntentService extends IntentService {

  public ReceiveTransitionsIntentService() {
    super("ReceiveTransitionsIntentService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    int transition = LocationClient.getGeofenceTransition(intent);
    String ticker;
    String title;
    String text;
    if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER) ||
        (transition == Geofence.GEOFENCE_TRANSITION_EXIT)) {
      if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
        ticker = getString(R.string.geofence_entered_ticker);
        title = getString(R.string.geofence_entered_title);
        text = getString(R.string.geofence_entered_text);
      } else {
        ticker = getString(R.string.geofence_exited_ticker);
        title = getString(R.string.geofence_exited_title);
        text = getString(R.string.geofence_exited_text);
      }
      NotificationUtils.sendNotification(this, 1, R.drawable.notification_icon, ticker,
          title, text);
    }
  }

}
