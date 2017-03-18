package com.chamika.research.datacollector.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

/**
 * Created by chamika on 3/18/17.
 */

public class FenceReceiver extends BroadcastReceiver {

    private final String TAG = FenceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        FenceState fenceState = FenceState.extract(intent);

        if ("headphoneFence".equals(fenceState.getFenceKey())) {
            switch (fenceState.getCurrentState()) {
                case FenceState.TRUE:
                    Log.i(TAG, "Headphones are plugged in.");
                    break;
                case FenceState.FALSE:
                    Log.i(TAG, "Headphones are NOT plugged in.");
                    break;
                case FenceState.UNKNOWN:
                    Log.i(TAG, "The headphone fence is in an unknown state.");
                    break;
            }
        }
    }
}
