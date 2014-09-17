package com.vanesoftware.opendoors.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OdApi {

  private static final String LOG_TAG = OdApi.class.getCanonicalName();

  /**
   * Opens the door by sending a httprequest on background thread
   */
  public void openDoor() {
    SendMessageTask msgTask = new SendMessageTask();
    String []params = {}; // empty message
    msgTask.execute(params);
  }

  private class SendMessageTask extends AsyncTask<String, Void, String[]> {

    private static final String baseUrlString = "http://YOUR_SERVER_IP/cgi-bin/o.py";
    private static final String authHeader = "x-od";

    @Override
    protected String[] doInBackground(String... params) {
      StringBuffer urlString = new StringBuffer(baseUrlString);
      if (params != null && params.length > 0) {
        urlString.append("?");
        for (int i = 0; i < params.length; i++) {
          urlString.append(params[i]);
          if (i + 1 > params.length) {
            urlString.append("&");
          }
        }
      }
      try {
        URL url = new URL(urlString.toString());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty(authHeader, generateAuthString());
        urlConnection.connect();
        urlConnection.getInputStream();
      } catch (IOException e) {
        Log.e(LOG_TAG, "Error", e);
      }
      return null;
    }

    /**
     * Generates auth header for request
     * @return Auth string to be added to request header
     */
    private String generateAuthString() {
      final String client = "YOUR_CLIENT_STRING";
      final String secret = "YOUR_SECRET_KEY";

      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
      Date date = new Date();
      String dateString = dateFormat.format(date);
      String hashbase = client + "&" + secret + "&" + dateString;
      StringBuffer authString = new StringBuffer();
      authString.append(client).append("&");
      try {
        byte[] bytesOfMessage = hashbase.getBytes();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        for (int i = 0; i < thedigest.length; i++) {
          String hex = Integer.toHexString(0xFF & thedigest[i]);
          if (hex.length() == 1) {
            authString.append('0');
          }
          authString.append(hex);
        }
      } catch(NoSuchAlgorithmException e) {
        Log.e(LOG_TAG, "Error", e);
      }
      return authString.toString();
    }

  }

}
