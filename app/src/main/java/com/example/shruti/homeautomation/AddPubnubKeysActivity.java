package com.example.shruti.homeautomation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddPubnubKeysActivity extends AppCompatActivity {

    DatabaseController databaseController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pubnub_keys);
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

    public void savePubnubKeys(View view) {

        String PUB_KEY = "";
        String SUB_KEY = "";
        boolean noText = false;
        databaseController = new DatabaseController(getBaseContext());

        EditText editTextPubKey = (EditText) findViewById(R.id.id_publishKeyTxt);
        if(!editTextPubKey.getText().toString().equals("")){
            PUB_KEY = editTextPubKey.getText().toString();
        } else {
            noText = true;
        }

        EditText editTextSubKey = (EditText) findViewById(R.id.id_subscribeKeyTxt);
        if(!editTextSubKey.getText().toString().equals("")){
            SUB_KEY = editTextSubKey.getText().toString();
        } else {
            noText = true;
        }

        if(!noText){
            final String replacePubKey = PUB_KEY;
            final String replaceSubKey = SUB_KEY;
            boolean res = databaseController.saveKeys(PUB_KEY,SUB_KEY);
            if (!res){


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("INFO");
                builder.setMessage("There is already a key stored. Do you wan to replace it?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                databaseController.replaceKeys(replacePubKey, replaceSubKey);
                                Toast.makeText(getApplicationContext(), "Replaced Keys", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                AlertDialog alert = builder.create();
                alert.show();
            }

        } else {

            AlertDialog alertDialog = new AlertDialog.Builder(AddPubnubKeysActivity.this).create();
            alertDialog.setTitle("ERROR");
            alertDialog.setMessage("Please enter the key values before proceeding!!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            Log.d("save Keys:: ", " -> database ");

        }

    }

}
