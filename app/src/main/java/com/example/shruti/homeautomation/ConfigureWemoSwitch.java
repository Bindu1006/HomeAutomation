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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void refreshSwithes(View view) throws IOException {
        Log.d("REFRESH", "refreshing switches");

        databaseController = new DatabaseController(getBaseContext());
        ArrayList<IPAddressDetails> ipAddressList1;

        ipAddressList1 = databaseController.getAllDeviceDetails();

        ArrayList<IPAddressDetails> ipAddressList = new ArrayList<>();

        IPAddressDetails ipAddressDetails = new IPAddressDetails();
        ipAddressDetails.setDeviceIpAddress("10.0.0.4");
        ipAddressDetails.setDeviceName("WeMo");

        IPAddressDetails ipAddressDetails1 = new IPAddressDetails();
        ipAddressDetails1.setDeviceIpAddress("10.0.0.5");
        ipAddressDetails1.setDeviceName("WeMo1");

        ipAddressList.add(ipAddressDetails);
        ipAddressList.add(ipAddressDetails1);


        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInf = wm.getConnectionInfo();
        String ip1 = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("IPADDRESS", ip1);
        System.out.println(wifiInf.toString());


        ListView listView =  (ListView) findViewById(R.id.ListViewId);

        Log.d("VIEW DEVICES :", ipAddressList + " ");

        IPAddressAdapter adapter = new IPAddressAdapter(this, ipAddressList1);
        listView.setAdapter(adapter);



    }

    public void addDevices(View view) {
        final Dialog dialog = new Dialog(ConfigureWemoSwitch.this);
        dialog.setContentView(R.layout.add_wemo_dialog);
        dialog.setTitle("Add WeMo Device");

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
