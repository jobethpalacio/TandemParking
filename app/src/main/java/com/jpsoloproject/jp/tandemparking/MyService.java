package com.jpsoloproject.jp.tandemparking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.internal.zzs.TAG;
import static com.jpsoloproject.jp.tandemparking.MainActivity.txtSpot1;
import static com.jpsoloproject.jp.tandemparking.MainActivity.txtSpot2;

public class MyService extends Service {

    String otherPerson;

    public MyService() {




    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AcceptThread thread = new AcceptThread();
        thread.start();
        Toast.makeText(this, "Service started...", Toast.LENGTH_LONG).show();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);

        //Get the name of the other person
        SharedPreferences prefs =
                getSharedPreferences(MainActivity.MY_GLOBAL_PREFS, MODE_PRIVATE);
        String strDriver1 = prefs.getString("Driver1", "");
        String strDriver2 = prefs.getString("Driver2", "");
        if (strDriver1.equals(MainActivity.myself)) {
            otherPerson = strDriver2;
        } else {
            otherPerson = strDriver1;
        }

        //If I'm on the first spot, I want to be notified of changes
        if (MainActivity.txtSpot1.getText().toString().equals(MainActivity.myself)) {
            DatabaseReference DBspot2 = MainActivity.database.getReference("Spot 2");
            DBspot2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = dataSnapshot.getValue(String.class);
                    txtSpot2.setText(value);
                    if (txtSpot2.getText().toString().equals("")) {
                        //It is now empty behind you
                        genericNotify("State 0","You are no longer blocked","Empty spot");
                    } else if (txtSpot2.getText().toString().equals(otherPerson)) {
                        //You are blocked
                        genericNotify("State 1 (or 2)","You are blocked",otherPerson + " is parked behind you");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped...", Toast.LENGTH_LONG).show();

    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
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
                Log.d(MainActivity.TAG, "Name is: " + deviceName);
                if (deviceName.equals(MainActivity.bluetoothNameToListenFor)) {
                    Log.d(MainActivity.TAG, "The connected device is the one we want");

                    if (txtSpot2.getText().toString().equals(MainActivity.myself)) {
                        //I'm parked behind, I am leaving
                        DatabaseReference DBspot2 = MainActivity.database.getReference("Spot 2");
                        DBspot2.setValue("");
                    } else if (txtSpot2.getText().toString().equals("")) {
                        //It is empty, I am parking
                        DatabaseReference DBspot2 = MainActivity.database.getReference("Spot 2");
                        DBspot2.setValue(MainActivity.myself);

                        DatabaseReference DBspot1 = MainActivity.database.getReference("Spot 1");
                        DBspot1.setValue(otherPerson);


                    } else {
                        //Alert to manually edit
                        genericNotify("State 4", "Please manually edit who's parked where",
                                "Unknown state");
                    }


                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                 //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                 //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                 //Device has disconnected
            }
        }
    };

    private void genericNotify(String title, String text, String title1) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(getApplicationContext())
                .setContentTitle(title).setContentText(text).setContentTitle(title1)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(0,notify);
    }
}
