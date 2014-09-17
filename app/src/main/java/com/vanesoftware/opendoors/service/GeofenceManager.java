package com.vanesoftware.opendoors.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationStatusCodes;

import java.util.ArrayList;
import java.util.List;


public class GeofenceManager implements LocationClient.OnAddGeofencesResultListener,
                                        LocationClient.OnRemoveGeofencesResultListener,
                                        GooglePlayServicesClient.ConnectionCallbacks,
                                        GooglePlayServicesClient.OnConnectionFailedListener {

  private final static Double GEOFENCE_LAT  = 0.00000; // REPLACE WITH YOUR HOME LATITUDE
  private final static Double GEOFENCE_LONG = 0.00000; // REPLACE WITH YOUR LONGITUDE
  private final static Float GEOFENCE_RAD   = 100f;    // meters
  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

  private final Activity mActivity;
  private PendingIntent mGeofencePendingIntent;
  private ArrayList<Geofence> mCurrentGeofences;
  private Geofence mCurrentGeofence;
  private List<String> mCurrentGeofenceIds;
  private LocationClient mLocationClient;
  private REQUEST_TYPE mRequestType;
  private boolean mInProgress;

  public enum REQUEST_TYPE {
    ADD, REMOVE
  }

  public GeofenceManager(Activity activityContext) {
    mActivity = activityContext;
    mGeofencePendingIntent = null;
    mCurrentGeofenceIds = null;
    mLocationClient = null;
    mRequestType = null;
    mInProgress = false;
    mCurrentGeofences = new ArrayList<Geofence>();
  }

  public void registerGeofence() {

    mCurrentGeofence = new Geofence.Builder().setRequestId("1")
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
        .setCircularRegion(GEOFENCE_LAT, GEOFENCE_LONG, GEOFENCE_RAD)
        .setExpirationDuration(Geofence.NEVER_EXPIRE).build();
    mCurrentGeofences.add(mCurrentGeofence);

    try {
      addGeofences(mCurrentGeofences);
    } catch (UnsupportedOperationException e) {
      // Handle that previous request hasn't finished.
    }
  }


  public void setInProgressFlag(boolean flag) {
    mInProgress = flag;
  }

  public boolean getInProgressFlag() {
    return mInProgress;
  }

  public void addGeofences(List<Geofence> geofences) throws UnsupportedOperationException {
    mCurrentGeofences = (ArrayList<Geofence>) geofences;

    if (!mInProgress) {
      mInProgress = true;
      // If a failure occurs, onActivityResult is eventually called, and
      // it needs to know what type of request was in progress.
      mRequestType = REQUEST_TYPE.ADD;
      getLocationClient().connect();

    } else {
      throw new UnsupportedOperationException();
    }
  }

  private GooglePlayServicesClient getLocationClient() {
    if (mLocationClient == null) {
      mLocationClient = new LocationClient(mActivity, this, this);
    }
    return mLocationClient;
  }

  /**
   * Remove the geofences in a list of geofence IDs. To remove all current
   * geofences associated with a request, you can also call
   * removeGeofencesByIntent.
   *
   * @param geofenceIds A List of geofence IDs
   */
  public void removeGeofencesById(List<String> geofenceIds) throws IllegalArgumentException,
      UnsupportedOperationException {
    if ((null == geofenceIds) || (geofenceIds.size() == 0)) {
      throw new IllegalArgumentException();
    } else {
      // If a removal request is not already in progress, continue
      if (!mInProgress) {
        mCurrentGeofenceIds = geofenceIds;
        mRequestType = REQUEST_TYPE.REMOVE;
        // The request is not complete until onConnected() or
        // onConnectionFailure() is called
        getLocationClient().connect();
      } else {
        throw new UnsupportedOperationException();
      }
    }
  }

  @Override
  public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
    if (LocationStatusCodes.SUCCESS == statusCode) {
      // Handle success
    } else {
      // Handle error
    }
    mInProgress = false;
    getLocationClient().disconnect();
  }

  @Override
  public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
    if (LocationStatusCodes.SUCCESS == statusCode) {
      // Handle success
    } else {
      // Handle error
    }
    mInProgress = false;
    getLocationClient().disconnect();
  }

  /**
   * Called by Location Services once the location client is connected.
   */
  @Override
  public void onConnected(Bundle arg0) {
    switch (mRequestType) {
      case ADD:
        mGeofencePendingIntent = createRequestPendingIntent();
        mLocationClient.addGeofences(mCurrentGeofences, mGeofencePendingIntent, this);
        break;
      case REMOVE:
        mLocationClient.removeGeofences(mCurrentGeofenceIds, this);
        break;
      default:
        break;

    }
  }

  @Override
  public void onDisconnected() {
    mInProgress = false;
    mLocationClient = null;
  }

  /**
   * @return A PendingIntent for the IntentService that handles geofence
   *         transitions.
   */
  private PendingIntent createRequestPendingIntent() {
    if (null != mGeofencePendingIntent) {
      return mGeofencePendingIntent;
    } else {
      // Create an Intent pointing to the ReceiveTransitionsIntentService
      Intent intent = new Intent(mActivity, ReceiveTransitionsIntentService.class);
      return PendingIntent.getService(mActivity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    mInProgress = false;
    if (connectionResult.hasResolution()) {
      try {
        // This will be handled by Activity.onActivityResult
        connectionResult
            .startResolutionForResult(mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
      } catch (IntentSender.SendIntentException e) {
        e.printStackTrace();
      }
    } else {
      // Handle error
    }
  }

  @Override
  public void onRemoveGeofencesByPendingIntentResult(int arg0, PendingIntent arg1) {
    // TODO Auto-generated method stub
  }

}
