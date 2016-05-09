package com.example.shruti.homeautomation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shruti.homeautomation.PlayerFiles.PlayerInputStream;
import com.example.shruti.homeautomation.PlayerFiles.PlayerView;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoStreaming extends AppCompatActivity {

    private Pubnub pubnub;
//    public static final String PUBLISH_KEY = "pub-c-e2930ace-7719-4803-9d9d-339ae326b4e6";
//    public static final String SUBSCRIBE_KEY = "sub-c-1d10a854-080e-11e6-996b-0619f8945a4f";
    public static final String CHANNEL = "phue";

    DatabaseController databaseController;

    private PlayerView videoView = null;
    SharedPreferences prefs;
    Button button_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Switch videoSwitch = (Switch) findViewById(R.id.id_videoStatusSwitch);

        Bundle bundle = getIntent().getExtras();
        String switchStatus = bundle.getString("STATUS");
        //String switchStatus = intent.getStringExtra("SWITCHSTATUS");
        databaseController = new DatabaseController(getApplicationContext());
        boolean result = false;
        String status = "";
        if (switchStatus.equalsIgnoreCase("")){
            result = databaseController.setDefaultVideoStatus();
            videoSwitch.setChecked(false);
        } else {
            status = databaseController.getVideoStatus();
            Log.d("Status",status);
            if(status.equalsIgnoreCase("VIDEO_OFF")){
                videoSwitch.setChecked(false);
                TextView videoURL = (TextView) findViewById(R.id.id_videoURL);
                videoURL.setText("");
            } else if (status.equalsIgnoreCase("VIDEO_ON")) {
                videoSwitch.setChecked(true);
                TextView videoURL = (TextView) findViewById(R.id.id_videoURL);
                videoURL.setText("http://especsjsu.dyndns.org:8090/stream.html");
            }
        }
        initPubnub();



        videoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                boolean noText = false;

                if (isChecked) {
                    boolean result = databaseController.setVideoStatus("ON");
                    String switchType = "ON";
                    String mode = "VIDEO";
                    publish(switchType, mode);
                    TextView videoURL = (TextView) findViewById(R.id.id_videoURL);
                    videoURL.setText("http://especsjsu.dyndns.org:8090/stream.html");
                    Log.d("Switch Clicked :: ", " ON ");
                } else {
                    boolean result = databaseController.setVideoStatus("OFF");
                    String switchType = "OFF";
                    String mode = "VIDEO";
                    publish(switchType, mode);
                    TextView videoURL = (TextView) findViewById(R.id.id_videoURL);
                    videoURL.setText("");
                    Log.d("Switch Clicked :: ", " OFF ");
                }

            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void publish(String switchType, String mode){
        JSONObject js = new JSONObject();
        try {
//            js.put("DEVICE_NAME", deviceName);
//            js.put("DEVICE_IP_ADDR",  ipAddr);
            js.put("SWITCH_TYPE",switchType);
            js.put("MODE",mode);
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
        databaseController = new DatabaseController(getBaseContext());
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
                    if (message.toString().contains("MOTION_DETECTED")){
                        Log.d("Motiion","Detected");
                    }

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
                    if (message.toString().contains("MOTION_DETECTED")) {
                        Log.d("Motion","Detected");
                        databaseController = new DatabaseController(getApplicationContext());
                        String phoneNumber = databaseController.getPhoneNumber();
                        if (!phoneNumber.equalsIgnoreCase("")){
                            String smsMessage = "Motion Detected!!!  Please go to the below URL to see the Video: http://especsjsu.dyndns.org:8090/stream.html";
                            long time = System.currentTimeMillis(); //System.currentTimeMillis() + 1000*60*30;
                            boolean result = databaseController.setMessageSentTime(time);
                            if (result){
                                SmsManager.getDefault().sendTextMessage(phoneNumber, null, smsMessage, null,null);
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

    public void addUrl(View view) {
        Intent settingsActivity = new Intent(getBaseContext(), AddUrlToStream.class);
        startActivity(settingsActivity);

    }

    public void openVideoSettings(View view) {
        Log.d("Video","Open settings");
    }

//    public void startVideo(View view) {
//        final Switch switch_status = (Switch) findViewById(R.id.id_videoStatusSwitch);
//        boolean switchStatus = switch_status.getShowText();
//        if(!switchStatus) {
//            AlertDialog alertDialog = new AlertDialog.Builder(VideoStreaming.this).create();
//            alertDialog.setTitle("ERROR");
//            alertDialog.setMessage("Please set the video configuration ON to watch the video!!");
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();
//        } else {
//
//            if (videoView == null)
//                showPlayer();
//            else {
//                if (videoView.isPlaying()) {
//                    Toast.makeText(getApplicationContext(), "video is already playing, stopping now...", Toast.LENGTH_SHORT).show();
////                Log.v(TAG, "video is already playing, stopping now...");
//                    button_video = (Button) findViewById(R.id.button_video);
//                    button_video.setText(R.string.video_start);
//                    videoView.stopPlayback();
//                } else {
//                    Toast.makeText(getApplicationContext(), "video is not playing, starting it now...", Toast.LENGTH_SHORT).show();
////                Log.v(TAG, "video is not playing, starting it now...");
//                    button_video = (Button) findViewById(R.id.button_video);
//                    button_video.setText(R.string.video_stop);
//                    videoView.startPlayback();
//                }
//            }
//        }
//
//    }

    public void setNumber(View view){

        final Dialog dialog = new Dialog(VideoStreaming.this);
        dialog.setContentView(R.layout.set_phonenumber_dialog);
        dialog.setTitle("Set Phone Number");
        //alert.setMessage("Set secret message to ring the phone");
        // Create TextView
        databaseController = new DatabaseController(getBaseContext());

        Button button = (Button) dialog.findViewById(R.id.id_saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText edit = (EditText) dialog.findViewById(R.id.id_phoneNumber);
                String phoneNum = edit.getText().toString();

                databaseController = new DatabaseController(getBaseContext());
                boolean result = databaseController.savePhoneNumber(phoneNum);
                databaseController.close();
                if(!result) {
                    Toast.makeText(getApplicationContext(), "Error occurred in saving the number!! Delete the stored number and try again.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Number Saved", Toast.LENGTH_LONG).show();
                }

                dialog.dismiss();

            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.id_cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }

//    private void showPlayer() {
//        //VideoView videoView = (VideoView) findViewById(R.id.videoView1);
//        String url = prefs.getString("video_url", "");
//        Log.d("Video", "starting video playback: " + url);
//        button_video.setText(R.string.video_stop);
//
//        videoView = (PlayerView) findViewById(R.id.videoView1);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//
//        Uri video = Uri.parse(url);
//        videoView.setMediaController(mediaController);
//
//        videoView.setSource(PlayerInputStream.read(url, getCacheDir()));
//        videoView.start();
//        //MjpegView mv = new MjpegView(this);
//
//        //setContentView(mv);
//        //videoView.setVideoPath(MjpegInputStream.read(url));
//        //videoView.setDisplayMode(MjpegView.SIZE_BEST_FIT);
//        //videoView.showFps(false);
//    }



}
