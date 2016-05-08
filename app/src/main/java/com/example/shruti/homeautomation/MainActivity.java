package com.example.shruti.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("MainActivity   ::: ", " Settings ");

            Intent intent = new Intent(this, ConfigureLedPins.class);
            //intent.putExtra("USERLIST",userDetailsList);
            startActivity(intent);
        }

        if (id == R.id.id_ConfigureLedPins) {
            Log.d("MainActivity   ::: ", " Configure Led Pins");

            Intent intent = new Intent(this, ConfigureLedPins.class);
            startActivity(intent);

//            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
        }

        if (id == R.id.id_configureWemoSwitch) {
            Log.d("MainActivity   ::: ", " Configure Led Pins");
            Intent intent = new Intent(this, ConfigureWemoSwitch.class);
            startActivity(intent);
        }

        if (id == R.id.id_configureRecording) {
            Log.d("MainActivity   ::: ", " Configure Recording");
            Intent intent = new Intent(this, VideoStreaming.class);
            startActivity(intent);
        }

        if (id == R.id.id_addPubnubKeys) {
            Log.d("MainActivity   ::: ", " Configure Recording");
            Intent intent = new Intent(this, AddPubnubKeysActivity.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    public void getStarted(View view){
        Log.d("MainActivity   ::: ", " Get Started With Steps");
        Intent intent = new Intent(this, GetStartedActivity.class);
        startActivity(intent);
    }

    public void configureLed(View view) {
        Log.d("MainActivity   ::: ", " Configure Led Pins");

        Intent intent = new Intent(this, ConfigureLedPins.class);
        startActivity(intent);
    }

    public void configureWemo(View view) {
        Log.d("MainActivity   ::: ", " Configure Led Pins");
        Intent intent = new Intent(this, ConfigureWemoSwitch.class);
        startActivity(intent);
    }

    public void configureVideo(View view) {
        Log.d("MainActivity   ::: ", " Configure Recording");
        Intent intent = new Intent(this, VideoStreaming.class);
        databaseController = new DatabaseController(getApplicationContext().getApplicationContext());
        String status = databaseController.getVideoStatus();
        intent.putExtra("STATUS",status);
        startActivity(intent);
    }

    public void configureKeys(View view) {
        Log.d("MainActivity   ::: ", " Configure Recording");
        Intent intent = new Intent(this, AddPubnubKeysActivity.class);

        startActivity(intent);
    }
}
