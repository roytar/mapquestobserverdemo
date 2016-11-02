package com.rorlig.mapquestobserverdemo;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mapquest.tracking.AdditionalObservationProvider;
import com.mapquest.tracking.LocationTracker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gaurav gupta
 */

public class DemoApplication extends Application {
    private final String TAG= "DemoApplication";
    private final String APIKEY= "jzHKWTrSFAPmu5eLfEv09pF0tIiAZ3pn";
    private final String DEVID= "33416657-377e-4621-b919-8d141befadb9";
    static int seq=-1;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTracker();
    }


    private void initializeTracker() {

        Context mContext = getApplicationContext();

        TelephonyManager tMgr = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String mPhoneNumber = tMgr.getLine1Number();
        final String mObserved = "location observed: "+ mPhoneNumber;




        new LocationTracker.Builder(this, APIKEY)
                .withActiveLocationUpdatesDuringPassiveListening(TimeUnit.SECONDS.toMillis(300), true)      // location every 5 minutes if no passive loc avail, wake up for it.
                .withPassiveListeningMinimumLocationUpdateIntervalDuration(TimeUnit.MINUTES.toMillis(2))    // wait at most 2 minutes between passive locations
                .withPassiveListeningMinimumLocationUpdateDistance(0)                                       // 0 meters between locations
                .withWakeForReporting(true)                                                                 // Wake up for reporting
                .withReportingIntervalDuration(TimeUnit.MINUTES.toMillis(1)   )                             // report every minute (default)

                .withDeviceId(mPhoneNumber + "-PassiveActive")                                              // put mdn in device id
                .withTrackingId(mPhoneNumber)
                .withAdditionalObservationProvider(new AdditionalObservationProvider() {
                    @Override
                    public Map<String, Double> getAdditionalObservationData() {
                        seq++;
                        Log.d(TAG,mObserved + " seq: " + Integer.toString(seq));

                        return Collections.singletonMap("sequence", (double) seq);
                    }
                }
                )

                .buildAndInitialize();
    }

}
