package com.example.shruti.homeautomation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class wemoSettings extends AppCompatActivity {

    TimePicker timepicker;
    PendingIntent pending_intent;
    AlarmManager my_alarm_manager;
    DatabaseController databaseController;
    boolean alarmStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wemo_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Switch alarmSwitch = (Switch) findViewById(R.id.id_alarmSwitch);
        alarmSwitch.setChecked(false);

        alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                boolean noText = false;

                if (isChecked) {
                    alarmStatus = true;
                    Log.d("Alarm Switch :: ", " ON ");
                } else {
                    alarmStatus = false;
                    Log.d("Alarm Switch :: ", " OFF ");
                }

            }
        });

        Bundle bundle = getIntent().getExtras();
        IPAddressDetails deviceDetails = (IPAddressDetails) bundle.getSerializable("DeviceDetails");
        Log.d("settingsPage",deviceDetails+" ");

    }

    public void setTime(View view) {

        Bundle bundle = getIntent().getExtras();
        final IPAddressDetails deviceDetails = (IPAddressDetails) bundle.getSerializable("DeviceDetails");
        Log.d("settingsPage",deviceDetails+" ");

        //create an instance of calendar
        Calendar calendar = Calendar.getInstance();

        // initializing time picker
        timepicker = (TimePicker) findViewById(R.id.timePicker);

        calendar.set(Calendar.HOUR_OF_DAY, timepicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timepicker.getCurrentMinute());

        int hour_picked;
        int minutes_picked;
        hour_picked = timepicker.getCurrentHour();
        minutes_picked = timepicker.getCurrentMinute();

        //converts the values from int to string
        String hour_value = String.valueOf(hour_picked);
        String minute_value = String.valueOf(minutes_picked);

        if (hour_picked > 12) {
            hour_value = String.valueOf(hour_picked - 12);
        }

        if (minutes_picked < 10) {
            minute_value = "0" + String.valueOf(minutes_picked);
        }

        Log.d("Time is Set to:",  hour_value + ":" + minute_value);
        String alarmSetTime = hour_value + ":" + minute_value;
        Log.d("Alarm set",calendar.getTimeInMillis()+" ");
        Log.d("Alarm set at", System.currentTimeMillis() + " ");

        Toast.makeText(getApplicationContext(), "Alarm set at "+hour_value +":"+minute_value, Toast.LENGTH_LONG).show();

//        final Switch switch_status = (Switch) findViewById(R.id.id_alarmSwitch);
//        boolean set = switch_status.getShowText();
       // boolean switchStatus = switch_status.getShowText();

        //String switchStatusTxt = "OFF";
//        if (switchStatus) {
//            switchStatusTxt = "ON";
//        }
//        Log.d("SWITCH STATUS",switchStatusTxt + " ");
        String switchStatus = "OFF";
        if(alarmStatus){
            switchStatus = "ON";
        }

        databaseController = new DatabaseController(getBaseContext());
        databaseController.setAlarmForDevice(alarmSetTime, deviceDetails.getDeviceName(), deviceDetails.getDeviceIpAddress(), switchStatus);

        AlarmManager service = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(wemoSettings.this, TimeReceiver.class);
        i.putExtra("DEVICEDETAILS", deviceDetails);
        i.putExtra("SWITCHSTATUS", switchStatus);
        i.putExtra("ALARMTIME", alarmSetTime);
        PendingIntent pending = PendingIntent.getBroadcast(wemoSettings.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        service.set(AlarmManager.RTC, calendar.getTimeInMillis() , pending);
                //setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 20, pending);

    }

    public void viewSchedule(View view) {

        Log.d("settingsPage"," View Schedule ");
        databaseController = new DatabaseController(getBaseContext());
        DeviceSchedulerBO deviceScheduler = new DeviceSchedulerBO();
        ArrayList<DeviceSchedulerBO> deviceSchedulerList ;
        deviceSchedulerList = databaseController.viewSchedule();

        Intent intent = new Intent(this, ViewDeviceScheduler.class);
        intent.putExtra("DEVICESCHEDULERLIST",deviceSchedulerList);
        startActivity(intent);


    }

}
