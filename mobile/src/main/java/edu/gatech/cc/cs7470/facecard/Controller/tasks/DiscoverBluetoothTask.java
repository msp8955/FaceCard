package edu.gatech.cc.cs7470.facecard.Controller.tasks;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cc.cs7470.facecard.Model.Bluetooth;

public class DiscoverBluetoothTask {

    private static final String TAG = "FaceCard DiscoverBluetoothTask";

    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler = new Handler();

    private static final long SCAN_PERIOD = 5000; //5 seconds

//    private BluetoothAdapter btAdapter;
    private List<String> btDeviceList = new ArrayList<String>();

    public void discoverBluetooth(Context context){

        Log.d(TAG, "discover bluetooth");

        BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //Register the BroadcastReceiver
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothDevice.ACTION_UUID);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(ActionFoundReceiver, filter); // Don't forget to unregister during onDestroy

        if(checkBTState()){
            scanLeDevice(true);
        }

        return;
    }

    private void scanLeDevice(final boolean enable) {

        Log.d(TAG, "scanLeDevice");

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.d(TAG, "scanLeDevice stopped");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);

            String[] bluetoothList = btDeviceList.toArray(new String[btDeviceList.size()]);
            new DiscoverNearbyPeopleTask().execute(bluetoothList);
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

//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            String address = msg.getData().getString("ble_device_address");
//            Log.d(TAG, "handleMessage " + address);
//        }
//    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {

            Log.i("Found: ", device.getName() + " - " + device.getAddress());
            btDeviceList.add(device.getAddress());

//            Bundle bundle = new Bundle();
//            bundle.putString("ble_device_name", device.getName());
//            bundle.putString("ble_device_address", device.getAddress());
//
//            Message msg = new Message();
//            msg.setData(bundle);
//            mHandler.sendMessage(msg);

        }
    };

}