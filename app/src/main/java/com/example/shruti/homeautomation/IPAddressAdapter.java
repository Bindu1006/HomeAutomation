package com.example.shruti.homeautomation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vishwas on 4/27/16.
 */
public class IPAddressAdapter extends ArrayAdapter<IPAddressDetails> {

    private Context thisContext;
    private Pubnub pubnub;
    DatabaseController databaseController;

    public static final String PUBLISH_KEY = "pub-c-e2930ace-7719-4803-9d9d-339ae326b4e6";
    public static final String SUBSCRIBE_KEY = "sub-c-1d10a854-080e-11e6-996b-0619f8945a4f";
    public static final String CHANNEL = "phue";

    public IPAddressAdapter(Context context, ArrayList<IPAddressDetails> users) {
        super(context, 0, users);
        thisContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        initPubnub();
        SharedPreferences prefs = thisContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE);
        // Get the data item for this position
        final IPAddressDetails ipAddressDetails = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ipaddress_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView ipAddress = (TextView) convertView.findViewById(R.id.id_ipAddress);
        TextView deviceName = (TextView) convertView.findViewById(R.id.id_deviceName);

        // Populate the data into the template view using the data object
        ipAddress.setText(ipAddressDetails.getDeviceIpAddress());
        deviceName.setText(ipAddressDetails.getDeviceName());

        final Switch switch_text = (Switch) convertView.findViewById(R.id.id_switchWemo);

        switch_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String deviceNameStr = ipAddressDetails.getDeviceName();
                String deviceIpAddr = ipAddressDetails.getDeviceIpAddress();
                if(isChecked){
                    String switchType = "ON";
                    Log.d("SWITCH",switchType);
                    databaseController = new DatabaseController(getContext().getApplicationContext());
                    databaseController.updateStatus(deviceIpAddr, switchType);
                    publish(deviceNameStr, deviceIpAddr, switchType);
                } else {
                    String switchType = "OFF";
                    Log.d("SWITCH",switchType);
                    databaseController = new DatabaseController(getContext().getApplicationContext());
                    databaseController.updateStatus(deviceIpAddr, switchType);
                    publish(deviceNameStr, deviceIpAddr, switchType);
                }

                Log.d("SWITCH",deviceNameStr);

            }
        });

        ImageView imageView = (ImageView) convertView.findViewById(R.id.id_settings);

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = ipAddressDetails.getDeviceName();
                String ipAddress = ipAddressDetails.getDeviceIpAddress();
                Log.d("Settings", name);
                IPAddressDetails deviceDetails = new IPAddressDetails();
                deviceDetails.setDeviceName(name);
                deviceDetails.setDeviceIpAddress(ipAddress);

                Intent intent = new Intent(thisContext, wemoSettings.class);
                intent.putExtra("DeviceDetails",(Serializable) deviceDetails);
                thisContext.startActivity(intent);

            }

        });

        // Return the completed view to render on screen
        return convertView;
    }

    public void publish(String deviceName, String ipAddr, String switchType){
        JSONObject js = new JSONObject();
        try {
            js.put("DEVICE_NAME", deviceName);
            js.put("DEVICE_IP_ADDR",  ipAddr);
            js.put("SWITCH_TYPE",switchType);
            js.put("MODE","WEMO");
        } catch (JSONException e) { e.printStackTrace(); }

        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Log.d("PUBNUB", response.toString());
            }
            public void errorCallback(String channel, PubnubError error) {
                Log.d("PUBNUB",error.toString());
            }
        };
        this.pubnub.publish(CHANNEL, js, callback);
    }

    public void initPubnub(){
        this.pubnub = new Pubnub(PUBLISH_KEY,SUBSCRIBE_KEY);
        this.pubnub.setUUID("AndroidPHue");
        subscribe();
    }

    public void subscribe(){
        try {
            this.pubnub.subscribe(CHANNEL, new Callback() {
                @Override
                public void connectCallback(String channel, Object message) {
                    Log.d("PUBNUB", "SUBSCRIBE : CONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                public void reconnectCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel, Object message) {
                    Log.d("PUBNUB","SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    Log.d("PUBNUB","SUBSCRIBE : ERROR on channel " + channel
                            + " : " + error.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
