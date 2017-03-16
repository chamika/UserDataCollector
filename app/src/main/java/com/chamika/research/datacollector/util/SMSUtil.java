package com.chamika.research.datacollector.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.chamika.research.datacollector.store.BaseStore;

import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;
import me.everything.providers.core.Data;

/**
 * Created by chamika on 3/16/17.
 */

public class SMSUtil {
    public static void getSMS(Context context) {
        TelephonyProvider provider = new TelephonyProvider(context);
        Data<Sms> sms = provider.getSms(TelephonyProvider.Filter.ALL);
        Cursor cursor = sms.getCursor();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Sms msg = sms.fromCursor(cursor);
                Log.d(SMSUtil.class.getSimpleName(), "SMS:" + msg.toString());
                int actionType = (msg.type == Sms.MessageType.SENT) ? 1 : 2;
                BaseStore.saveEvent(context, actionType, "SMS", msg.receivedDate, msg.address);
            }
        }
    }
}