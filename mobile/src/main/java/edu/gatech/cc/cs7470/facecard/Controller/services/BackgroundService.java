package edu.gatech.cc.cs7470.facecard.Controller.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.DiscoverNearbyPeopleTask;
import edu.gatech.cc.cs7470.facecard.Model.FaceCard;

/**
 * Created by miseonpark on 3/25/15.
 */
public class BackgroundService extends Service implements OnTaskCompleted {

    private static final String TAG = "BackgroundService";
    public static final String EXTRA_MESSENGER="edu.gatech.cc.cs7470.facecard.Controller.services.EXTRA_MESSENGER";
    private final IBinder mBinder = new MyBinder();
    private Messenger outMessenger;

    private boolean isDemo;

    private Context context;
    private FaceCard[] faceCards;

    private Messenger messenger;

    //For General Bluetooth stuff
    private BluetoothAdapter mBluetoothAdapter;

    //For Communicating with Glass
    public static String glassUUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final UUID MY_UUID = UUID.fromString(glassUUID);
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    protected static final int SUCCESS_CONNECT = 0;
    protected static final int MESSAGE_READ = 1;
    protected static final int MESSAGE_WRITE = 2;
    private BluetoothDevice glassDevice;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    //For Discovering Bluetooth
    private Handler mDiscoverBluetoothHandler = new Handler();
    private boolean mScanning;
    private static final long SCAN_PERIOD = 10000; //10 seconds
    private Set<String> btDeviceList = new HashSet<String>();

    public Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.i(TAG, "in handler");
            super.handleMessage(msg);
            switch(msg.what){
                case SUCCESS_CONNECT:
                    //read and write data from remote device
                    Log.d(TAG, "SUCCESS_CONNECT");
                    unregisterReceiver(receiver);
                    connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
                    connectedThread.start();
                    sendFaceCards();
//                    Toast.makeText(getApplicationContext(), "CONNECTED", 2).show();
//                    setContentView(R.layout.activity_main);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf);
                    Log.d(TAG, "MESSAGE_READ: " + readMessage);
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    Log.d(TAG, "MESSAGE_WRITE: " + writeMessage);
                    break;
            }
        }
    };

    public class MyBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Log.d("service","onBind with extra");
            outMessenger = (Messenger) extras.get("MESSENGER");
        }
        return mBinder;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Bundle extras=intent.getExtras();
        messenger=(Messenger)extras.get(EXTRA_MESSENGER);
        return Service.START_STICKY;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate");
        context = getApplicationContext();

        SharedPreferences prefs = getSharedPreferences(Constants.PACKAGE_NAME, MODE_PRIVATE);
        if(!prefs.contains(Constants.SHARED_PREFERENCES_ALARM) || !prefs.getBoolean(Constants.SHARED_PREFERENCES_ALARM, false)){
            //alarm set to off but is running; which means running a demo
            isDemo = true;
        }else{
            isDemo = false;
        }

        //check the bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            this.stopSelf();
        }else if (!mBluetoothAdapter.isEnabled()) {
                this.stopSelf();
        }

        discoverBluetooth();
        initBluetooth();
    }

    private  void initBluetooth(){
        Log.d(TAG, "initBluetooth");
        // Create a BroadcastReceiver for ACTION_FOUND
        receiver = new BroadcastReceiver() {
            //once the receiver receives an action, it will break and stop receiving notifications.
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    devices.add(device);
                    //pairedDevices.add(device.getName());
                }
            }
        };
        // Register the BroadcastReceiver
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter); // Don't forget to unregister during onDestroy
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver, filter);
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread constructor");
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
            Log.d(TAG, "ConnectedThread run");
            byte[] buffer;  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    buffer = new byte[1024];

                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
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
            Log.d(TAG, "ConnectedThread write");
            try {
                mmOutStream.write(bytes);
                mmOutStream.flush();
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            Log.d(TAG, "ConnectedThread cancel");
            try {
                mmInStream.close();
                mmOutStream.close();
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            Log.d(TAG, "ConnectThread cancel");
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
                    mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                    mmSocket.connect();
//                    mmSocket.close();
                } catch (Exception e) {
                    Log.d(TAG, "fallback failed: " + e.getMessage());
                }
                return;
            }
            // Do work to manage the connection, the handler will send the connecting successful message with the socket back to the pool
            mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            Log.d(TAG, "ConnectThread cancel");
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        try {
            if(connectThread!=null){
                connectThread.cancel();
            }
            if(connectedThread!=null) {
                connectedThread.cancel();
            }
            unregisterReceiver(receiver);
        }catch(IllegalArgumentException e){
            Log.e(TAG, e.getMessage());
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onTaskCompleted(FaceCard[] result){
        Log.d(TAG, "onTaskCompleted");
        faceCards = result;
        if(faceCards!=null){
            for (FaceCard card : faceCards){
                Log.d(TAG, "card: " + card.getName());
            }
            connectToGlass();
        }
    }

    /**
     * @return true if connection to glass successful
     */
    public void connectToGlass(){
        Log.d(TAG, "connectToGlass");
        Set<BluetoothDevice> devicesArray = mBluetoothAdapter.getBondedDevices();
        // If there are devices
        if (devicesArray != null && devicesArray.size() > 0) {
            // Loop through paired devices
            Log.d(TAG, "Bluetooth founded!");
            for (BluetoothDevice device : devicesArray) {
                if(device.getName().contains("Glass")){
                    Log.d(TAG, "connecting to Glass device: " + device.getName() + " " + device.getAddress() + " " + device.getUuids());
                    glassDevice = device;
//                    if(connectThread==null) {
//                        connectThread = new ConnectThread(glassDevice);
//                        connectThread.start();
//                    }else{
//                        connectThread = new ConnectThread(glassDevice);
//                        connectThread.start();
//                    }
                    connectThread = new ConnectThread(glassDevice);
                    connectThread.start();
                }
                Log.d(TAG, device.getName() + " " + device.getAddress() + " " + device.getUuids());
            }
        }
    }

    public void sendFaceCards(){
        if(faceCards==null){
            return;
        }
        for(FaceCard card : faceCards) {
//            byte[] sendBuf = (byte[]) card.getName().getBytes();
            try {
                byte[] sendBuf = card.serialize();
                Log.d(TAG, "sendBuf size: " + sendBuf.length);
                connectedThread.write(sendBuf);
                mHandler.obtainMessage(MESSAGE_READ, sendBuf)
                        .sendToTarget();
                Log.d(TAG, card.getBluetoothId() + " facecard sent");
            }catch(Exception e){
                Log.d(TAG, "sendFaceCards exception : " + e.getMessage());
            }
        }

    }

    public void discoverBluetooth(){
        Log.d(TAG, "discover bluetooth");
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if(checkBTState()){
            scanLeDevice(true);
        }

        return;
    }

    private void scanLeDevice(final boolean enable) {

        Log.d(TAG, "scanLeDevice");

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mDiscoverBluetoothHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Log.d(TAG, "scanLeDevice stopped");
                    StringBuilder stringBuilder = new StringBuilder();
                    int i=0;
                    for(String bt : btDeviceList){
                        if(isDemo){
                            Message msg=Message.obtain();
                            msg.obj = bt;
                            try {
                                messenger.send(msg);
                            }
                            catch (android.os.RemoteException e1) {
                                Log.w(getClass().getName(), "Exception sending message", e1);
                            }
                        }
                        if(i+1<btDeviceList.size()){
                            stringBuilder.append(bt + ",");
                        }else{
                            stringBuilder.append(bt + "," + "AA:AA:AA:AA:AA:AA,123"); //test
                        }
                        i++;
                    }
                    new DiscoverNearbyPeopleTask(context, BackgroundService.this).execute(stringBuilder.toString());

                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private boolean checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // If it isn't request to turn it on
        // List paired devices
        // Emulator doesn't support Bluetooth and will return null
        if(mBluetoothAdapter==null) {
            Log.d(TAG,"Bluetooth NOT supported. Aborting.");
            return false;
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "Bluetooth is enabled...");

                // Starting the device discovery
                mBluetoothAdapter.startDiscovery();
                return true;
            }
            return false;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            Log.i(TAG, "Found: " + device.getName() + " - " + device.getAddress());
            if(!btDeviceList.contains(device.getAddress())){
                btDeviceList.add(device.getAddress());
            }
        }
    };

}
