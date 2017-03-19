package com.chamika.research.datacollector;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.chamika.research.datacollector.service.BackgroundService;
import com.chamika.research.datacollector.service.DataCollectorService;
import com.chamika.research.datacollector.service.DataUploaderService;
import com.chamika.research.datacollector.util.Config;
import com.chamika.research.datacollector.util.Constant;
import com.chamika.research.datacollector.util.SettingsUtil;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_READ_CALL_LOG = 1000;
    private static final int PERMISSIONS_REQUEST_READ_SMS = 1001;
    private static final int PERMISSIONS_REQUEST_ACTIVITY = 1002;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1003;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent alarmIntent = new Intent(MainActivity.this, DataCollectorService.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        initCheckBoxDangerousPermission(R.id.check_calls, Manifest.permission.READ_CALL_LOG, PERMISSIONS_REQUEST_READ_CALL_LOG, Constant.PREF_CALL);
        initCheckBoxDangerousPermission(R.id.check_msgs, Manifest.permission.READ_SMS, PERMISSIONS_REQUEST_READ_SMS, Constant.PREF_MSG);
        initCheckBoxDangerousPermission(R.id.check_location, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSIONS_REQUEST_LOCATION, Constant.PREF_LOCATION);
        initCheckBoxNormalPermission(R.id.check_activities, Constant.PREF_ACTIVITY);
    }

    private void scheduleDatabaseUpload(Context context) {
        Intent alarmIntent = new Intent(context, DataUploaderService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = Config.DATA_UPLOAD_INTERVAL;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, interval, pendingIntent);
    }

    private void initCheckBoxDangerousPermission(int checkboxResId, final String permission, final int permissionRequest, final String settingsPrefKey) {
        final Context context = MainActivity.this;
        CheckBox checkBox = (CheckBox) findViewById(checkboxResId);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{permission},
                                permissionRequest);
                    } else {
                        SettingsUtil.setBooleanPref(context, settingsPrefKey, true);
                    }
                } else {
                    SettingsUtil.setBooleanPref(context, settingsPrefKey, false);
                }
            }
        });
        checkBox.setChecked(SettingsUtil.getBooleanPref(context, settingsPrefKey));
    }

    private void initCheckBoxNormalPermission(int checkboxResId, final String settingsPrefKey) {
        final Context context = MainActivity.this;
        CheckBox checkBox = (CheckBox) findViewById(checkboxResId);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SettingsUtil.setBooleanPref(context, settingsPrefKey, isChecked);
            }
        });
        checkBox.setChecked(SettingsUtil.getBooleanPref(context, settingsPrefKey));
    }

    public void start(View v) {
        //SMS,CALL
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = Config.DATA_COLLECTION_REFRESH_INTERVAL;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        //ACTIVITY, LOCATION
        startService(new Intent(getApplicationContext(), BackgroundService.class));

        scheduleDatabaseUpload(this);
        Toast.makeText(this, "Started collecting data", Toast.LENGTH_SHORT).show();
    }

    public void cancel(View v) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        stopService(new Intent(getApplicationContext(), BackgroundService.class));

        Toast.makeText(this, "Stopped collecting data", Toast.LENGTH_SHORT).show();
    }

    public void view(View v) {
        startActivity(new Intent(this, DataViewActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String setting = null;
        int resId = 0;
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CALL_LOG: {
                setting = Constant.PREF_CALL;
                resId = R.id.check_calls;
                break;
            }
            case PERMISSIONS_REQUEST_READ_SMS: {
                setting = Constant.PREF_MSG;
                resId = R.id.check_msgs;
                break;
            }
            case PERMISSIONS_REQUEST_LOCATION: {
                setting = Constant.PREF_LOCATION;
                resId = R.id.check_location;
                break;
            }
        }
        if (setting != null) {
            ((CheckBox) findViewById(resId)).setChecked(granted);
            SettingsUtil.setBooleanPref(this, setting, granted);
        }
    }
}
