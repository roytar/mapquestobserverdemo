package com.rorlig.mapquestobserverdemo;

import android.app.Application;

import com.mapquest.tracking.AdditionalObservationProvider;
import com.mapquest.tracking.LocationTracker;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author gaurav gupta
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initializeTracker();
    }


    private void initializeTracker() {
        new LocationTracker.Builder(this, "LjeS0kYCqo33EqqwN9iW7A9s7jBj0meT") // TODO Update with your access key
                .withActiveLocationUpdatesDuringPassiveListening(TimeUnit.SECONDS.toMillis(15), true)
                .withPassiveListeningMinimumLocationUpdateIntervalDuration(TimeUnit.MINUTES.toMillis(3))
                .withPassiveListeningMinimumLocationUpdateDistance(0)
                .withProperty("deliveryNumber", 1L)
                .withProperty("driverEmployeeId", "ABC123")
                .withAdditionalObservationProvider(new AdditionalObservationProvider() {
                    @Override
                    public Map<String, Double> getAdditionalObservationData() {
                        return Collections.singletonMap("temperature", 23.4);
                    }
                }).buildAndInitialize();
    }

}
