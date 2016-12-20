package com.rorlig.mapquestobserverdemo;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mapquest.tracking.AdditionalObservationProvider;
import com.mapquest.tracking.LocationTracker;

import java.util.Collections;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.lang.*;


/**
 * @author gaurav gupta
 */

public class DemoApplication extends Application {
    private final String TAG= "DemoApplication";
    private final String APIKEY= "jzHKWTrSFAPmu5eLfEv09pF0tIiAZ3pn";
    private final String DEVID= "33416657-377e-4621-b919-8d141befadb9";
    static int seq=-1;
    static double ds = -1.0;

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
//                .withActiveLocationUpdatesDuringPassiveListening(TimeUnit.SECONDS.toMillis(300), true)      // location every 5 minutes if no passive loc avail, wake up for it.
//                .withPassiveListeningMinimumLocationUpdateIntervalDuration(TimeUnit.MINUTES.toMillis(2))    // wait at most 2 minutes between passive locations
//                .withPassiveListeningMinimumLocationUpdateDistance(0)                                       // 0 meters between locations
                .withWakeForReporting(true)                                                                 // Wake up for reporting
                .withReportingIntervalDuration(TimeUnit.SECONDS.toMillis(10)   )                             // report every minute (default)

//                .withDeviceId(mPhoneNumber + "-PassiveActive")                                              // put mdn in device id
                .withDeviceId(mPhoneNumber + "-Active")                                              // put mdn in device id
                .withTrackingId(mPhoneNumber)
                .withProperty("p1", 123456789)
                .withProperty("p2", "property string p2")
                .withAdditionalObservationProvider(new AdditionalObservationProvider() {
                    @Override
                    public Map<String, Double> getAdditionalObservationData() {

                        Map<String, Double>  myMap = new HashMap<String, Double>();

                        seq++;
                        ds++;

                        myMap.put("ObservationData1_ds", ds);
                        myMap.put("ObservationData2_ds_times_2", 2 * ds);
                        myMap.put("ObservationData3_ds_squared", ds * ds);

                        Log.d(TAG,mObserved + " seq: " + Integer.toString(seq) + " " + String.valueOf(ds));
//                        return Collections.singletonMap("sequence", ds);
                        return myMap;
                    }
                }
                )

                .buildAndInitialize();
    }

}
