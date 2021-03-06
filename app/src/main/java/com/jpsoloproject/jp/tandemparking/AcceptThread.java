package com.jpsoloproject.jp.tandemparking;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private BluetoothAdapter mBluetoothAdapter = MainActivity.getmBluetoothAdapter();


    public AcceptThread() {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("TandemParking", MainActivity.MY_UUID);
            Log.d(MainActivity.TAG, "Socket's listen() method ",null);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
                Log.d(MainActivity.TAG, "Socket accepted",null);
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                // manageMyConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Could not close the connect socket", e);
        }
    }
    //Bluetooth connection begin end
}