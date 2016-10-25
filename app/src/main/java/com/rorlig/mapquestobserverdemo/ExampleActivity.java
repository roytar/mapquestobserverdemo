package com.rorlig.mapquestobserverdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mapquest.tracking.LocationTracker;

//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;


/**
 * @author gaurav gupta
 */

public class ExampleActivity extends AppCompatActivity {
    private ToggleButton mTrackingToggle;
    private LocationTracker.TrackingStateListener mTrackingStateListener;
    private final String TAG     = "DemoApplication";
//    Info adInfo = null;

//    adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mTrackingToggle = (ToggleButton)findViewById(R.id.trackingToggle);

        mTrackingStateListener = buildTrackingStateListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Update toggle to reflect whether tracking is in progress
        mTrackingToggle.setChecked(LocationTracker.getInstance().isTracking());

        mTrackingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggle, boolean checked) {
                if (checked) {
                    startTracking();
                } else {
                    stopTracking();
                }
            }
        });

        LocationTracker.getInstance().addTrackingStateListener(mTrackingStateListener);
    }

    @Override
    protected void onStop() {
        // Because LocationTracker exists as a singleton, if you use TrackingStateListeners, be sure
        // to unregister any that might leak a reference to something like an Activity.
        LocationTracker.getInstance().removeTrackingStateListener(mTrackingStateListener);

        // Clear listener
        mTrackingToggle.setOnCheckedChangeListener(null);

        super.onStop();
    }

    private void startTracking() {
        Log.d(TAG, "startTracking");
        LocationTracker.getInstance().startTrackingActively();
//        LocationTracker.getInstance().startTrackingPassively();

        // Keep the screen on while tracking
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void stopTracking() {
        Log.d(TAG, "stopTracking");
        LocationTracker.getInstance().stopTracking();

        // Allow the screen to turn off again
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private LocationTracker.TrackingStateListener buildTrackingStateListener() {
        return new LocationTracker.TrackingStateListener() {
            @Override
            public void onTrackingStopped(final boolean userInitiated) {
                if(!userInitiated) {
                    Log.d(TAG, "TrackingStateListener: not user initiated!");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTrackingToggle.setChecked(false);
                            mTrackingToggle.setEnabled(false); // No use in letting a user try again if their key wasn't valid.

                            Toast.makeText(ExampleActivity.this,
                                    R.string.tracking_disabled_message,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Log.d(TAG, "TrackingStateListener: User Initiated!");
                }

            }
        };
    }
}
