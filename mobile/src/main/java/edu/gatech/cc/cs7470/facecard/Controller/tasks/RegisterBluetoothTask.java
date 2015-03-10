package edu.gatech.cc.cs7470.facecard.Controller.tasks;

import android.bluetooth.BluetoothAdapter;
import android.os.ParcelUuid;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by miseonpark on 3/9/15.
 */
public class RegisterBluetoothTask {

    private static final String TAG = "FaceCard RegisterBluetoothTask";

    public boolean registerBluetooth(){
        //TODO
        return true;
    }

    public String getBluetoothId(){

        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
            for (ParcelUuid uuid : uuids) {
                Log.d(TAG, "UUID: " + uuid.getUuid().toString());
            }
            return uuids[0].getUuid().toString();
        }catch(Exception e){
            return "";
        }

    }
}
