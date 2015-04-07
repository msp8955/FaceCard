package edu.gatech.cc.cs7470.facecard.Controller.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.DiscoverBluetoothTask;

/**
 * Created by miseonpark on 3/25/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {

    public static final String TAG = "BluetoothReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        DiscoverBluetoothTask discoverBluetoothTask = new DiscoverBluetoothTask(context);
        discoverBluetoothTask.discoverBluetooth();
        Log.d(TAG, "onReceive");
    }

    public void setAlarm(Context context)
    {
        Log.d(TAG, "setAlarm");
        AlarmManager manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BluetoothReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.DISCOVERY_INTERVAL, pendingIntent);
    }

    public void cancelAlarm(Context context)
    {
        Log.d(TAG, "cancelAlarm");
        Intent intent = new Intent(context, BluetoothReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
}
