package com.jpsoloproject.jp.tandemparking;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    protected static final String TAG = "Listener";
    protected static final UUID MY_UUID = UUID.fromString("bd3e97dc-25ae-44a2-bd0c-b6ab463928a1");
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    private static final String DB_SPOT1 = "db_spot1";
    private static final String DB_SPOT2 = "db_spot2";
    public static String myself;
    public static String bluetoothNameToListenFor;

    private static boolean isFirstTime;
    public static TextView txtSpot1;
    public static TextView txtSpot2;
    protected EditText edtText1;
    protected EditText edtText2;
    protected Button btnMain;
    protected boolean isFrontPageShown;
    protected static TextView debugText;
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private GoogleApiClient client;
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    protected static boolean isListeningInBackground = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
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

        //reads from disk
        SharedPreferences prefs =
                getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE);
        isFirstTime = prefs.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            //writes to disk
            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
            Log.d(TAG, "isFirstTime: " + isFirstTime);



        } else {
            Log.d(TAG, "isFirstTime: " + isFirstTime);
        }




        if (thereIsNoBluetoothOnThisDevice()) {
            Context context = getApplicationContext();
            CharSequence text = "This device does not support bluetooth";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            if (isBluetoothNotOn()) {
                turnBluetoothOn();
            } else {
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.

                    for (BluetoothDevice device : pairedDevices) {
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address
                        if (deviceName.equals("SmartWatch 2")) {

                            }

                            BluetoothSocket socket = null;
                            //socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(String.valueOf(MY_UUID)));
                            if (!isListeningInBackground) {
                                isListeningInBackground = isListeningForTheBluetooth();



                        } else {
                            //debugText.setText("Device in car not paired");
                        }
                    }

                }
            }



            DatabaseReference DBspot1 = database.getReference("Spot 1");
//            DBspot1.setValue("Olivia");
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
//            DBspot2.setValue("Adam");
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

    /**
     * The button that corresponds to the manual editing when entering state 3 or user clicks button
     * @param view
     */
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

    public void startService(View view) {
        isListeningInBackground = isListeningForTheBluetooth();
    }

    public boolean isListeningForTheBluetooth() {
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
        return true;
    }

    public void stopService(View view) {
        isListeningInBackground = stoppedListeningForTheBluetooth();
    }
    public boolean stoppedListeningForTheBluetooth() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        return false;
    }

    private boolean loadTheFrontPage() {
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

    public static BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
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
        if (id == R.id.action_settings_setup) {
            Intent intent = new Intent(this, Setup.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_settings_service_on) {
            isListeningInBackground = isListeningForTheBluetooth();
        } else if (id == R.id.action_settings_service_off) {
            isListeningInBackground = stoppedListeningForTheBluetooth();
        } else if (id == R.id.debug) {
            Intent intent = new Intent(this, Debug.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean thereIsNoBluetoothOnThisDevice() {
        return mBluetoothAdapter == null;
    }
    private boolean isBluetoothNotOn() {
        return !mBluetoothAdapter.isEnabled();
    }
    private void turnBluetoothOn() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    /**
     * The edit manually "page" is not a separate Activity. Therefore, a back press exits the program.
     * This intercepts the back button only when viewing the edit manually page to return to the display page.
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && !isFrontPageShown) {
            Log.d("CDA", "onKeyDown Called");
            isFrontPageShown = loadTheFrontPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
