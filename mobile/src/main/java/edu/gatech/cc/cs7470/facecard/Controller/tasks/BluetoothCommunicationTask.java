package edu.gatech.cc.cs7470.facecard.Controller.tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Model.FaceCard;

/**
 * Created by miseonpark on 3/31/15.
 */
public class BluetoothCommunicationTask {

    private static final String TAG = "BluetoothCommunicationTask";
    private static String glassUUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final UUID MY_UUID = UUID.fromString(glassUUID);
    private static BluetoothAdapter mBluetoothAdapter;
    private Context context;
    private ArrayList<BluetoothDevice> devices;
    private static BroadcastReceiver mReceiver;
    private static IntentFilter filter;

//    public static String msgToSend;
//    ConnectedThread mConnectedThread;
//    BluetoothAdapter myBt;
//    public String NAME =" BLE";
//    Handler handle;
//    BroadcastReceiver receiver;

//    ArrayList<BluetoothSocket> mSockets = new ArrayList<BluetoothSocket>();
//    // list of addresses for devices we've connected to
//    ArrayList<String> mDeviceAddresses = new ArrayList<String>();
//
//    // We can handle up to 7 connections... or something...
//    UUID[] uuids = new UUID[2];
//    // some uuid's we like to use..
//    String uuid1 = "00001101-0000-1000-8000-00805F9B34FB";
//    String uuid2 = "c2911cd0-5c3c-11e3-949a-0800200c9a66";
//
//    int REQUEST_ENABLE_BT = 1;
//    AcceptThread accThread;

    public BluetoothCommunicationTask(Context c){
        context = c;
    }

    public Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.i(TAG, "in handler");
            super.handleMessage(msg);
            switch(msg.what){
                case Constants.SUCCESS_CONNECT:
                    //read and write data from remote device
                    context.unregisterReceiver(mReceiver);
                    ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    connectedThread.start();
                    Log.d(TAG, "bluetooth connected");
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    int bufferContent = ByteBuffer.wrap(readBuf).getInt();
                    String string = new String(readBuf);
                    Log.d(TAG, "bluetooth message read: " + string);
                    break;
            }
        }
    };

    public void sendToGlass(FaceCard[] fc){
        for(FaceCard faceCard : fc) {
            Log.d(TAG, "FaceCards to be sent: " + faceCard.getBluetoothId());
        }
    }

    public void connectToGlass(){

        Log.d(TAG, "connectToGlass");

        initBluetooth();

        //check the bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Log.d(TAG, "Bluetooth disabled");
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled()) {

                Log.d(TAG, "Bluetooth turned off");
            }
            Set<BluetoothDevice> devicesArray = mBluetoothAdapter.getBondedDevices();
            // If there are devices
            if (devicesArray != null && devicesArray.size() > 0) {
                // Loop through paired devices
                Log.d(TAG, "Bluetooth founded!");
                for (BluetoothDevice device : devicesArray) {
                    if(device.getName().contains("Glass")){
                        Log.d(TAG, "connecting to Glass device: " + device.getName() + " " + device.getAddress() + " " + device.getUuids());
                        BluetoothDevice selectedDevice = device;
                        ConnectThread connect = new ConnectThread(selectedDevice);
                        connect.start();
                    }
                    Log.d(TAG, device.getName() + " " + device.getAddress() + " " + device.getUuids());
                    devices.add(device);
                }
            }
        }

    }

    private void initBluetooth(){
        Log.d(TAG, "initBluetooth");
        devices = new ArrayList<BluetoothDevice>();
        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            //once the receiver receives an action, it will break and stop receiving notifications.
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(TAG, device.getName() + " " + device.getAddress() + " " + device.getUuids());
                    devices.add(device);
                }
            }
        };
        // Register the BroadcastReceiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        context.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(mReceiver, filter);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    buffer = new byte[1024];

                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                    Log.i(TAG, "message received!");

                } catch (IOException e) {
                    Log.i(TAG, "message failed!");
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            Log.i(TAG, "construct");
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.i(TAG, "get socket failed");
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();
            Log.i(TAG, "connect - run");
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                Log.i(TAG, "connect - succeeded");
            } catch (IOException connectException) {
                Log.i(TAG, "connect failed");
                Log.i(TAG,connectException.getMessage());
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            // Do work to manage the connection, the handler will send the connecting successful message with the socket back to the pool
            mHandler.obtainMessage(Constants.SUCCESS_CONNECT, mmSocket).sendToTarget();
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}
