package com.chamika.research.datacollector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.chamika.research.datacollector.service.BackgroundService;
import com.chamika.research.datacollector.service.DataCollectorService;
import com.chamika.research.datacollector.util.Config;

public class MainActivity extends AppCompatActivity {
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent alarmIntent = new Intent(MainActivity.this, DataCollectorService.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

    }

    public void start(View v) {
        //SMS,CALL
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = Config.DATA_COLLECTION_REFRESH_INTERVAL;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        //ACTIVITY, LOCATION
        startService(new Intent(getApplicationContext(), BackgroundService.class));

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
}
