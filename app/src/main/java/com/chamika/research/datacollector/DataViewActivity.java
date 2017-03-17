package com.chamika.research.datacollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.chamika.research.datacollector.adapter.DataCursorAdapter;
import com.chamika.research.datacollector.store.BaseStore;

public class DataViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        // Find ListView to populate
        ListView listView = (ListView) findViewById(R.id.listview);
        DataCursorAdapter dataCursorAdapter = new DataCursorAdapter(this, BaseStore.getDataDesc(this));
        listView.setAdapter(dataCursorAdapter);
    }
}
