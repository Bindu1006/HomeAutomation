package com.example.shruti.homeautomation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class TimeReceiver extends BroadcastReceiver {

    private Pubnub pubnub;
    DatabaseController databaseController;

//    public static final String PUBLISH_KEY = "pub-c-e2930ace-7719-4803-9d9d-339ae326b4e6";
//    public static final String SUBSCRIBE_KEY = "sub-c-1d10a854-080e-11e6-996b-0619f8945a4f";
    public static final String CHANNEL = "phue";

    Context context;

    public TimeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("We are in Receiver ", "Hurray!!");
        IPAddressDetails deviceDetails = new IPAddressDetails();
        deviceDetails = (IPAddressDetails) intent.getSerializableExtra("DEVICEDETAILS");
        String switchStatus = intent.getStringExtra("SWITCHSTATUS");
        String alarmTime = intent.getStringExtra("ALARMTIME");
        Log.d("DEVICE",deviceDetails+" "+switchStatus);
        initPubnub(context);
        databaseController = new DatabaseController(context.getApplicationContext());
        if(switchStatus.equalsIgnoreCase("ON")){
            String switchType = "ON";
            Log.d("SWITCH",switchType);
            databaseController.updateStatus(deviceDetails.getDeviceIpAddress(), switchType);
            publish(deviceDetails.getDeviceName(), deviceDetails.getDeviceIpAddress(), switchType);
            Toast.makeText(context, "Turned ON", Toast.LENGTH_LONG).show();
        } else {
            String switchType = "OFF";
            Log.d("SWITCH",switchType);
            databaseController.updateStatus(deviceDetails.getDeviceIpAddress(), switchType);
            publish(deviceDetails.getDeviceName(), deviceDetails.getDeviceIpAddress(), switchType);
            Toast.makeText(context, "Turned OFF", Toast.LENGTH_LONG).show();
        }
        DeviceSchedulerBO schedulerDetails = new DeviceSchedulerBO();
        schedulerDetails.setAlarmTime(alarmTime);
        schedulerDetails.setDeviceName(deviceDetails.getDeviceName());
        schedulerDetails.setDeviceIP(deviceDetails.getDeviceIpAddress());
        schedulerDetails.setDeviceStatus(switchStatus);
        databaseController.deleteAlarmEntry(schedulerDetails);
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

    public void initPubnub(Context context){
        databaseController = new DatabaseController(context.getApplicationContext());
        PubNubKeysBO pubnubKeys = databaseController.getKeys();
        this.pubnub = new Pubnub(pubnubKeys.getPubKey(),pubnubKeys.getSubKey());
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
                    if (message.toString().contains("MOTION_DETECTED")){
                        Log.d("Motiion","Detected");
                        databaseController = new DatabaseController(context.getApplicationContext());
                        String phoneNumber = databaseController.getPhoneNumber();
                        if (!phoneNumber.equalsIgnoreCase("")){
                            String smsMessage = "Motion Detected!!!  Please go to the below URL to see the Video: http://pihomesjsu.dyndns.org:8090/stream.html";
                            long time = System.currentTimeMillis(); //System.currentTimeMillis() + 1000*60*30;
                            boolean result = databaseController.setMessageSentTime(time);
                            if (result){

                            }

                        }

                    }
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
