package com.chamika.research.datacollector;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.chamika.research.datacollector.adapter.DataCursorAdapter;
import com.chamika.research.datacollector.store.BaseStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DataViewActivity extends AppCompatActivity {

    private static final String TAG = DataViewActivity.class.getSimpleName();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        // Find ListView to populate
        ListView listView = (ListView) findViewById(R.id.listview);
        DataCursorAdapter dataCursorAdapter = new DataCursorAdapter(this, BaseStore.getDataDesc(this));
        listView.setAdapter(dataCursorAdapter);

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Cursor cursor = BaseStore.getDataDesc(DataViewActivity.this);
                cursor.moveToFirst();


                try {
                    String path = Environment.getExternalStorageDirectory() + "/research";
                    boolean createdirs = new File(path).mkdirs();
                    File fout = new File(path + "/data.txt");
                    boolean createFile = fout.createNewFile();
                    FileOutputStream fos = null;

                    fos = new FileOutputStream(fout);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

                    while (cursor.moveToNext()) {
                        double dayOfWeek, timeOfDay, event, d1, d2, d3;

                        int actionType = cursor.getInt(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_ACTION_TYPE));
                        String action = "N/A";
                        if (actionType == 1) {
                            action = "ACT";
                        } else if (actionType == 2) {
                            action = "EVENT";
                        } else if (actionType == 3) {
                            action = "ACT/EVT";
                        }
                        String eventType = cursor.getString(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_EVENT_TYPE));

                        long timeInMilis = cursor.getLong(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_TIME));
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(timeInMilis);
                        String data1 = cursor.getString(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_DATA1));
                        String data2 = cursor.getString(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_DATA2));
                        String data3 = cursor.getString(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_DATA3));
                        String data4 = cursor.getString(cursor.getColumnIndexOrThrow(BaseStore.Structure.COLUMN_NAME_DATA4));

                        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                        timeOfDay = cal.get(Calendar.HOUR_OF_DAY) * 10000 + cal.get(Calendar.MINUTE) * 100 + cal.get(Calendar.SECOND);
                        switch (eventType) {
                            case "ACT":
                                event = 1.0;
                                break;
                            case "CALL":
                                event = 3.0;
                                break;
                            case "SMS":
                                event = 4.0;
                                break;
                            default:
                                event = 0.0;
                        }

                        d1 = data1.hashCode();
                        String text = eventType + "|" + data1 + "|" + sdf.format(cal.getTime());

//                        String format = String.format("%.0f,%.0f,%.0f,%.0f,%s", dayOfWeek, timeOfDay, event, d1, text);
                        String format = String.format(Locale.US, "%.0f,%.0f,%.0f,%s", dayOfWeek, timeOfDay, event, text);
//                        String format = String.format("%.0f,%.0f,%.0f,%.0f,%s", d1, event, timeOfDay, dayOfWeek, text);
//                        String format = String.format(Locale.US,"%.0f,%.0f,%.0f,%s", event, timeOfDay, dayOfWeek, text);
                        Log.d(TAG, format);
                        bw.write(format);
                        bw.newLine();
                    }


                    bw.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
        };
        asyncTask.execute();
    }
}
