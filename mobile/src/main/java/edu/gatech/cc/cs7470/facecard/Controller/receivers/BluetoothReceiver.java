package edu.gatech.cc.cs7470.facecard.Controller.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import edu.gatech.cc.cs7470.facecard.Constants;
import edu.gatech.cc.cs7470.facecard.Controller.services.BackgroundService;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.BluetoothCommunicationTask;
import edu.gatech.cc.cs7470.facecard.Controller.tasks.DiscoverBluetoothTask;

/**
 * Created by miseonpark on 3/25/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {

    public static final String TAG = "BluetoothReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        Intent i = new Intent(context, BackgroundService.class);
        context.stopService(i);
        context.startService(i);
//        IBinder binder = peekService(context, new Intent(context, BackgroundService.class));
//        if(binder != null) {
//            Log.d(TAG, "onReceive discoverBluetooth");
//            BackgroundService service = ((BackgroundService.MyBinder) binder).getService();
//            service.discoverBluetooth();
//        }
        //context.bindService(i, myConnection, Context.BIND_AUTO_CREATE);

//        DiscoverBluetoothTask discoverBluetoothTask = new DiscoverBluetoothTask(context);
//        discoverBluetoothTask.discoverBluetooth();

    }

    public void setAlarm(Context context)
    {
        Log.d(TAG, "setAlarm");
        AlarmManager manager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BluetoothReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                Constants.DISCOVERY_INTERVAL, pendingIntent);
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
