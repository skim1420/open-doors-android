package com.vanesoftware.opendoors.layout;

import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vanesoftware.opendoors.R;
import com.vanesoftware.opendoors.service.OdApi;

public class OpenButtonFragment extends Fragment {

  private static final String LOG_TAG = OpenButtonFragment.class.getCanonicalName();

  private Button button;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_open_door, container, false);
    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    button = (Button) rootView.findViewById(R.id.button1);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View arg0) {
        OdApi odapi = new OdApi();
        odapi.openDoor();
      }
    });
    return rootView;
  }

}
