package com.chamika.research.datacollector.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chamika.research.datacollector.util.CallsUtils;
import com.chamika.research.datacollector.util.SMSUtil;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by chamika on 3/12/17.
 */

public class DataCollectorService extends BroadcastReceiver {

    private GoogleApiClient client;

    public DataCollectorService() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        SMSUtil.getSMS(context);
        CallsUtils.getCalls(context);
    }
}
