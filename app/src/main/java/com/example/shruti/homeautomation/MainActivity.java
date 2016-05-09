package com.example.shruti.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("MainActivity   ::: ", " Settings ");

            Intent intent = new Intent(this, ConfigureLedPins.class);
            //intent.putExtra("USERLIST",userDetailsList);
            startActivity(intent);
        }

        if (id == R.id.id_getStarted) {
            Log.d("MainActivity   ::: ", " Get Started");
            Intent intent = new Intent(this, GetStartedActivity.class);
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
        Log.d("MainActivity   ::: ", " Configure Smart Switch");

        Intent intent = new Intent(this, ConfigureWemoSwitch.class);
        startActivity(intent);
    }

    public void configureVideo(View view) {
        Log.d("MainActivity   ::: ", " Configure Recording");
        Intent intent = new Intent(this, VideoStreaming.class);
        databaseController = new DatabaseController(getApplicationContext().getApplicationContext());
        String status = databaseController.getVideoStatus();
        Log.d(" video status",status);
        intent.putExtra("STATUS",status);
        startActivity(intent);
    }

    public void configureKeys(View view) {
        Log.d("MainActivity   ::: ", " Configure Recording");
        Intent intent = new Intent(this, AddPubnubKeysActivity.class);

        startActivity(intent);
    }


}
