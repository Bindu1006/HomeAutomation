package com.example.shruti.homeautomation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONException;
import org.json.JSONObject;


public class ConfigureLedPins extends AppCompatActivity {

    public static final String TYPE = "CONFUGURE_LED";

    public static final int COLOR_RED = 1;
    public static final int COLOR_BLUE = 2;
    public static final int COLOR_GREEN = 3;

    public static int DEFAULT_PIN1 = 18;
    public static int DEFAULT_PIN2 = 23;
    public static int DEFAULT_PIN3 = 24;

    int PIN1,PIN2,PIN3;
    SeekBar redSeek, blueSeek, greenSeek;

    private Pubnub pubnub;
    public static final String PUBLISH_KEY = "pub-c-e2930ace-7719-4803-9d9d-339ae326b4e6";
    public static final String SUBSCRIBE_KEY = "sub-c-1d10a854-080e-11e6-996b-0619f8945a4f";
    public static final String CHANNEL = "phue";

    private long lastUpdate = System.currentTimeMillis();
    private boolean pHueOn = false;
    DatabaseController databaseController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_led_pins);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initPubnub();

        //initialize seek bars
        redSeek = (SeekBar) findViewById(R.id.id_seekBarRed);
        blueSeek = (SeekBar) findViewById(R.id.id_seekBarBlue);
        greenSeek = (SeekBar) findViewById(R.id.id_seekBarGreen);

        initializeSeekBar(redSeek, COLOR_RED);
        initializeSeekBar(blueSeek, COLOR_BLUE);
        initializeSeekBar(greenSeek, COLOR_GREEN);

        final Switch ledSwitch = (Switch) findViewById(R.id.switch1);

        ledSwitch.setChecked(false);
        //attach a listener to check for changes in state
        ledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                boolean noText = false;

                if (isChecked) {
                    //get the pin values
                    EditText editTextPin1 = (EditText) findViewById(R.id.id_pin1Value);
                    if(!editTextPin1.getText().toString().equals("")){
                        PIN1 = Integer.parseInt(editTextPin1.getText().toString());
                    } else {
                        noText = true;
                    }


                    EditText editTextPin2 = (EditText) findViewById(R.id.id_pin2Value);
                    if(!editTextPin2.getText().toString().equals("")){
                        PIN2 = Integer.parseInt(editTextPin2.getText().toString());
                    } else {
                        noText = true;
                    }


                    EditText editTextPin3 = (EditText) findViewById(R.id.id_pin3Value);
                    if(!editTextPin3.getText().toString().equals("")){
                        PIN3 = Integer.parseInt(editTextPin3.getText().toString());
                    } else {
                        noText = true;
                    }

                    if(!noText){
                        publish(255,255,255);
                        setRGBSeekBar(255,255,255);
                    } else {

                        AlertDialog alertDialog = new AlertDialog.Builder(ConfigureLedPins.this).create();
                        alertDialog.setTitle("ERROR");
                        alertDialog.setMessage("Please enter the pin values before proceeding!!");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        ledSwitch.setChecked(false);
                        publish(0, 0, 0);
                        setRGBSeekBar(0, 0, 0);
                        Log.d("Switch Clicked :: ", " OFF ");

                    }


                    Log.d("Switch Clicked :: ", " ON ");
                } else {
                    publish(0,0,0);
                    setRGBSeekBar(0,0,0);
                    Log.d("Switch Clicked :: ", " OFF ");
                }

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setRGBSeekBar(int red, int blue, int green){
        redSeek.setProgress(red);
        blueSeek.setProgress(blue);
        greenSeek.setProgress(green);
    }

    public void useDefault(View view){
        EditText editTextPin1 = (EditText) findViewById(R.id.id_pin1Value);
        editTextPin1.setText(String.valueOf(DEFAULT_PIN1));

        EditText editTextPin2 = (EditText) findViewById(R.id.id_pin2Value);
        editTextPin2.setText(String.valueOf(DEFAULT_PIN2));

        EditText editTextPin3 = (EditText) findViewById(R.id.id_pin3Value);
        editTextPin3.setText(String.valueOf(DEFAULT_PIN3));

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
                    if (message.toString().contains("MOTION_DETECTED")) {
                        Log.d("Motiion", "Detected");
                        databaseController = new DatabaseController(getApplicationContext());
                        String phoneNumber = databaseController.getPhoneNumber();
                        if (!phoneNumber.equalsIgnoreCase("")) {
                            String smsMessage = "Motion Detected!!!  Please go to the below URL to see the Video: http://especsjsu.dyndns.org:8090/stream.html";
                            SmsManager.getDefault().sendTextMessage(phoneNumber, null, smsMessage, null, null);
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

    public void initializeSeekBar(SeekBar seekBar, final int color_id){
        seekBar.setMax(255);
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   publish(redSeek.getProgress(), greenSeek.getProgress(), blueSeek.getProgress());
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {
                                                   publish(redSeek.getProgress(), greenSeek.getProgress(), blueSeek.getProgress());
                                               }

                                               @Override
                                               public void onProgressChanged(SeekBar seekBar, int progress,
                                                                             boolean fromUser) {
//                                                   TextView colorValueText;
//                                                   switch (color_id){  // Get the TextView identified by the colorID
//                                                       case COLOR_RED:
//                                                           colorValueText = (TextView)findViewById(R.id.red_value);
//                                                           break;
//                                                       case COLOR_GREEN:
//                                                           colorValueText = (TextView) findViewById(R.id.green_value);
//                                                           break;
//                                                       case COLOR_BLUE:
//                                                           colorValueText = (TextView)findViewById(R.id.blue_value);
//                                                           break;
//                                                       default:
//                                                           Log.e("SetupSeek","Invalid color.");
//                                                           return;
//                                                   }
//                                                 colorValueText.setText(String.valueOf(progress));  // Update the 0-255 text
                                                   int red   = redSeek.getProgress();     // Get Red value 0-255
                                                   int green = greenSeek.getProgress();   // Get Grn value 0-255
                                                   int blue  = blueSeek.getProgress();    // Get Blu value 0-255
//                                                 updateRGBViewHolderColor(red, green, blue); // Change the background of the viewholder
                                                   if(red > 0 || green > 0 || blue > 0){
                                                       Log.d("Changed"," wemo switch");
                                                       Switch ledSwitch = (Switch) findViewById(R.id.switch1);
                                                       ledSwitch.setChecked(true);
                                                   }

                                                   if (red == 0 && green == 0 && blue == 0) {
                                                       Switch ledSwitch = (Switch) findViewById(R.id.switch1);
                                                       ledSwitch.setChecked(false);
                                                   }

                                                   long now = System.currentTimeMillis();    // Only allow 1 pub every 100 milliseconds
                                                   if (now - lastUpdate > 100 && fromUser) { // Threshold and only send when user sliding
                                                       lastUpdate = now;
                                                       publish(red, green, blue);          // Stream RGB values to the Pi
                                                   }
                                               }

                                           }
        );

    }

    public void publish(int red, int green, int blue){
        JSONObject js = new JSONObject();
        try {
            js.put("RED",   red);
            js.put("GREEN", green);
            js.put("BLUE",  blue);
            js.put("MODE","LED");
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



}
