package com.sampleandroidtest.sampleandroidtest;

import android.support.test.espresso.IdlingResource;

/**
 * Taken from here: https://github.com/chiuki/espresso-samples/blob/master/idling-resource-elapsed-time/app/src/androidTest/java/com/sqisland/espresso/idling_resource/elapsed_time/ElapsedTimeIdlingResource.java
 * Used to handle async thread syncronization https://developer.android.com/training/testing/espresso/idling-resource
 */

public class ElapsedTimeIdlingResource implements IdlingResource {

    private final long startTime;
    private final long waitingTime;
    private ResourceCallback resourceCallback;

    public ElapsedTimeIdlingResource(long waitingTime) {
        this.startTime = System.currentTimeMillis();
        this.waitingTime = waitingTime;
    }

    @Override
    public String getName() {
        return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
    }

    @Override
    public boolean isIdleNow() {
        long elapsed = System.currentTimeMillis() - startTime;
        boolean idle = (elapsed >= waitingTime);
        if (idle) {
            resourceCallback.onTransitionToIdle();
        }
        return idle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
