package com.chamika.research.datacollector.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.chamika.research.datacollector.util.CallsUtils;
import com.chamika.research.datacollector.util.SMSUtil;

/**
 * Created by chamika on 3/12/17.
 */

public class DataCollectorService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        SMSUtil.getSMS(context);
        CallsUtils.getCalls(context);
    }
}
