package com.example.shruti.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pubnub.api.Pubnub;

import java.util.ArrayList;

/**
 * Created by Shruti on 5/3/16.
 */
public class SchedulerAdapter extends ArrayAdapter<DeviceSchedulerBO> {

    private Context thisContext;
    private Pubnub pubnub;
    DatabaseController databaseController;


    public SchedulerAdapter(Context context, ArrayList<DeviceSchedulerBO> users) {
        super(context, 0, users);
        thisContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final DeviceSchedulerBO deviceSchedulerBO = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.devicescheduler_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView ipAddress = (TextView) convertView.findViewById(R.id.id_ipAddress);
        TextView deviceName = (TextView) convertView.findViewById(R.id.id_deviceName);
        TextView deviceStatus = (TextView) convertView.findViewById(R.id.id_deviceStatus);
        TextView schedulerTime = (TextView) convertView.findViewById(R.id.id_schedulerTime);

        // Populate the data into the template view using the data object
        ipAddress.setText(deviceSchedulerBO.getDeviceIP());
        deviceName.setText(deviceSchedulerBO.getDeviceName());
        if(deviceSchedulerBO.getDeviceStatus().equalsIgnoreCase("DEVICE_ON")) {
            deviceStatus.setText("ON");
        } else if (deviceSchedulerBO.getDeviceStatus().equalsIgnoreCase("DEVICE_OFF")) {
            deviceStatus.setText("OFF");
        }
        schedulerTime.setText(deviceSchedulerBO.getAlarmTime());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.id_deleteScheduler);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceIP = deviceSchedulerBO.getDeviceIP();
                String schedulerTime = deviceSchedulerBO.getAlarmTime();
                Log.d("Settings", deviceIP + " "+schedulerTime);
                //Delete the details form the database
                databaseController = new DatabaseController(getContext().getApplicationContext());
                databaseController.deleteAlarmEntry(deviceSchedulerBO);


                Log.d("settingsPage"," refresh Schedule ");
                DeviceSchedulerBO deviceScheduler = new DeviceSchedulerBO();
                ArrayList<DeviceSchedulerBO> deviceSchedulerList ;
                deviceSchedulerList = databaseController.viewSchedule();

                Intent intent = new Intent(thisContext, ViewDeviceScheduler.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("DEVICESCHEDULERLIST", deviceSchedulerList);
                thisContext.startActivity(intent);

                Toast.makeText(getContext(), "Deleted Entry", Toast.LENGTH_LONG).show();

            }

        });

        // Return the completed view to render on screen
        return convertView;

    }


}
