package edu.gatech.cc.cs7470.facecard.Controller.utils;

import android.bluetooth.BluetoothAdapter;
import android.os.ParcelUuid;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by miseonpark on 3/10/15.
 */
public class BluetoothUtil {

    private static final String TAG = "FaceCard BluetoothUtil";

    /**
     * getBluetoothId
     * @return Bluetooth UUID for the device
     */
    public String getBluetoothId(){

        try {
            String address = BluetoothAdapter.getDefaultAdapter().getAddress();
//            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
//            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
//            for (ParcelUuid uuid : uuids) {
//                Log.d(TAG, "UUID: " + uuid.getUuid().toString());
//            }
            Log.d(TAG, "bluetooth address: " + address);
            return address;
        }catch(Exception e){
            return "";
        }

    }
}
