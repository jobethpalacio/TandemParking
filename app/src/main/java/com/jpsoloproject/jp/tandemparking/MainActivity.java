package com.jpsoloproject.jp.tandemparking;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "Listener";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    protected TextView txtSpot1;
    protected TextView txtSpot2;
    protected EditText edtText1;
    protected EditText edtText2;
    protected Button btnMain;
    protected boolean isFrontPageShown;
    protected TextView debugText;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //end template



        txtSpot1 = (TextView) findViewById(R.id.textView1);
        txtSpot2 = (TextView) findViewById(R.id.textView2);
        edtText1 = (EditText) findViewById(R.id.editText1);
        edtText2 = (EditText) findViewById(R.id.editText2);
        btnMain = (Button) findViewById(R.id.buttonMain);
        debugText = (TextView) findViewById(R.id.textView);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Context context = getApplicationContext();
            CharSequence text = "This device does not support bluetooth";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        if (deviceName.equals("SmartWatch 2")) {
                            debugText.setText("SW2 connected");
                        }
                    }

                }
            }

//            // Write a message to the database
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("message");
//            myRef.setValue("Hello, World!");
//            // Read from the database
//            myRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    String value = dataSnapshot.getValue(String.class);
//                    Log.d(TAG, "Value is: " + value);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w(TAG, "Failed to read value.", error.toException());
//                }
//            });


            DatabaseReference DBspot1 = database.getReference("Spot 1");
            DBspot1.setValue("Olivia");
            // Read from the database
            DBspot1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    txtSpot1.setText(value);
                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });


            DatabaseReference DBspot2 = database.getReference("Spot 2");
            DBspot2.setValue("Adam");
            // Read from the database
            DBspot2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    txtSpot2.setText(value);
                    Log.d(TAG, "Value is: " + value);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        isFrontPageShown = loadTheFrontPage();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void editSpots(View view) {
        // Do something in response to button

        if (isFrontPageShown) {


            txtSpot1.setVisibility(View.INVISIBLE);
            txtSpot2.setVisibility(View.INVISIBLE);

            edtText1.setVisibility(View.VISIBLE);
            edtText2.setVisibility(View.VISIBLE);

            btnMain.setText("Accept");
            isFrontPageShown = false;
        } else {

            DatabaseReference DBspot1 = database.getReference("Spot 1");
            DBspot1.setValue(edtText1.getText().toString());
            DatabaseReference DBspot2 = database.getReference("Spot 2");
            DBspot2.setValue(edtText2.getText().toString());
            isFrontPageShown = loadTheFrontPage();
        }


    }

    public boolean loadTheFrontPage() {
        txtSpot1.setVisibility(View.VISIBLE);
        txtSpot2.setVisibility(View.VISIBLE);

        edtText1.setVisibility(View.INVISIBLE);
        edtText2.setVisibility(View.INVISIBLE);
        btnMain.setText("Manual Edit");
        return true;
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }



}
