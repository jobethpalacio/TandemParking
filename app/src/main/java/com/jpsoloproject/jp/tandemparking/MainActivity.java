package com.jpsoloproject.jp.tandemparking;

import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import static android.bluetooth.BluetoothProfile.GATT;
import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.bluetooth.BluetoothProfile.STATE_CONNECTING;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    protected static final String TAG = "Listener";
    protected static final UUID MY_UUID = UUID.fromString("bd3e97dc-25ae-44a2-bd0c-b6ab463928a1");
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
    protected static TextView debugText;
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    private GoogleApiClient client;
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private boolean isListeningInBackground = false;

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

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);


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


                            }

                            BluetoothSocket socket = null;
                            //socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(String.valueOf(MY_UUID)));
                            if (!isListeningInBackground) {
                                isListeningInBackground = isListeningForTheBluetooth();

//                                final BluetoothManager bluetoothManager =
//                                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//                            if () {
//                                debugText.setText("SW2 connected");
//                            } else {
//                                debugText.setText("Not connected");
//                            }


                        } else {
                            //debugText.setText("Device in car not paired");
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


//        NewMessageNotification myNewMessageNotification;
//        myNewMessageNotification.notify();

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                 //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals("SmartWatch 2")) {

                    MainActivity.debugText.setText("is now connected");
                    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notify = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Title").setContentText("Text").setContentTitle("Title1")
                            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    nm.notify(0,notify);
                }


            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
                MainActivity.debugText.setText("done searching");
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
                MainActivity.debugText.setText("about to dc");
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                MainActivity.debugText.setText("has disconnected");
            }
        }
    };
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
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        isListeningInBackground = false;
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
        if (id == R.id.action_settings_apartment_setup) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void apartmentSetup(View view) {

    }

}
