package com.chamika.research.datacollector.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.chamika.research.datacollector.store.BaseStore;

import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.core.Data;

/**
 * Created by chamika on 3/17/17.
 */

public class CallsUtils {

    public static final String CALL = "CALL";

    public static void getCalls(Context context) {
        CallsProvider provider = new CallsProvider(context);
        Data<Call> calls = provider.getCalls();
        Cursor cursor = calls.getCursor();
        long startTime = SettingsUtil.getTime(context, CALL);
        long endTime = 0;
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Call call = calls.fromCursor(cursor);
                Log.d(SMSUtil.class.getSimpleName(), "CALL:" + call.toString());
                int actionType = (call.type == Call.CallType.OUTGOING) ? 1 : 2;
                BaseStore.saveEvent(context, actionType, CALL, call.callDate, call.number, String.valueOf(call.duration));
                if (endTime < call.callDate) {
                    endTime = call.callDate;
                }
            }
            SettingsUtil.setTime(context, CALL, endTime);
        }
    }
}
