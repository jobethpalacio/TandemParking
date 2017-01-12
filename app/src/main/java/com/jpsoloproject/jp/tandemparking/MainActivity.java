package com.jpsoloproject.jp.tandemparking;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.jpsoloproject.jp.MESSAGE";
    private static final int REQUEST_ENABLE_BT = 1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    protected TextView txtSpot1;
    protected TextView txtSpot2;
    protected EditText edtText1;
    protected EditText edtText2;
    protected Button   btnMain;
    protected boolean isFrontPageShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //end template
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        txtSpot1 = (TextView) findViewById(R.id.textView1);
        txtSpot2 = (TextView) findViewById(R.id.textView2);
        edtText1 = (EditText) findViewById(R.id.editText1);
        edtText2 = (EditText) findViewById(R.id.editText2);
        btnMain = (Button) findViewById(R.id.buttonMain);

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
            }
        }

        isFrontPageShown = loadTheFrontPage();



    }

    public void editSpots(View view) {
        // Do something in response to button
//        Intent intent = new Intent(this, ManualEdit.class);
//        startActivity(intent);

        if (isFrontPageShown) {


            txtSpot1.setVisibility(View.INVISIBLE);
            txtSpot2.setVisibility(View.INVISIBLE);

            edtText1.setVisibility(View.VISIBLE);
            edtText2.setVisibility(View.VISIBLE);

            btnMain.setText("Accept");
            isFrontPageShown = false;
        } else {
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

}
