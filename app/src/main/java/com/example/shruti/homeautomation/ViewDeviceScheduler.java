package com.example.shruti.homeautomation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewDeviceScheduler extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_device_scheduler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView =  (ListView) findViewById(R.id.id_schedulerList);
        ArrayList<DeviceSchedulerBO> deviceschedulerlist = (ArrayList<DeviceSchedulerBO>) getIntent().getSerializableExtra("DEVICESCHEDULERLIST");
        Log.d("VIEWDEVICE :", deviceschedulerlist + " ");

        SchedulerAdapter adapter = new SchedulerAdapter(this, deviceschedulerlist);
        listView.setAdapter(adapter);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }





}
