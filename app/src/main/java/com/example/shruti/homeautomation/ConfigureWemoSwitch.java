package com.example.shruti.homeautomation;

import android.app.Dialog;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class ConfigureWemoSwitch extends AppCompatActivity {

    DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_wemo_switch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

//      Get the stored devices from the Database
        databaseController = new DatabaseController(getBaseContext());
        ArrayList<IPAddressDetails> ipAddressList1;
        ipAddressList1 = databaseController.getAllDeviceDetails();

        Toast.makeText(getApplicationContext(), "Refresh to get all the stored smart devices", Toast.LENGTH_LONG).show();
//      Check for no devices stored devices
        if(ipAddressList1.size()==0){
            Toast.makeText(getApplicationContext(), "No Devices found!!! Add device to view them.", Toast.LENGTH_LONG).show();
        }

        ListView listView =  (ListView) findViewById(R.id.ListViewId);
        Log.d("VIEW DEVICES :", ipAddressList1 + " ");

//      Send the list to the adapter
        IPAddressAdapter adapter = new IPAddressAdapter(this, ipAddressList1);
        listView.setAdapter(adapter);

    }

    @SuppressWarnings("deprecation")
    public void refreshSwithes(View view) throws IOException {

        Log.d("REFRESH", "refreshing switches");
        Toast.makeText(getApplicationContext(), "Refreshing the devices", Toast.LENGTH_LONG).show();

        databaseController = new DatabaseController(getBaseContext());
        ArrayList<IPAddressDetails> ipAddressList1;

        ipAddressList1 = databaseController.getAllDeviceDetails();

        if(ipAddressList1.size()==0){
            Toast.makeText(getApplicationContext(), "No Devices found!!! Add device to view them.", Toast.LENGTH_LONG).show();
        }


        ListView listView =  (ListView) findViewById(R.id.ListViewId);

        Log.d("VIEW DEVICES :", ipAddressList1 + " ");

        IPAddressAdapter adapter = new IPAddressAdapter(this, ipAddressList1);
        listView.setAdapter(adapter);



    }

    public void addDevices(View view) {
        final Dialog dialog = new Dialog(ConfigureWemoSwitch.this);
        dialog.setContentView(R.layout.add_wemo_dialog);
        dialog.setTitle("Add Smart Device");

        databaseController = new DatabaseController(getBaseContext());

        Button button = (Button) dialog.findViewById(R.id.id_saveDevice);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText editDeviceName = (EditText) dialog.findViewById(R.id.id_deviceNameTxt);
                String deviceName = editDeviceName.getText().toString();

                IPAddressDetails deviceDetails = new IPAddressDetails();
                deviceDetails.setDeviceName(deviceName);

                EditText editDeviceIPAddress = (EditText) dialog.findViewById(R.id.id_deviceIPTxt);
                String deviceIPAddress = editDeviceIPAddress.getText().toString();

                deviceDetails.setDeviceIpAddress(deviceIPAddress);
                deviceDetails.setDeviceStatus("OFF");

                databaseController = new DatabaseController(getBaseContext());
                databaseController.insertWemoDeviceData(deviceDetails);
                databaseController.close();
                dialog.dismiss();

            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.id_cancelSaveDevice);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }


}
