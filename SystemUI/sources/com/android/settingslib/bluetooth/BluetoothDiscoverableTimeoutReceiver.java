package com.android.settingslib.bluetooth;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class BluetoothDiscoverableTimeoutReceiver extends BroadcastReceiver {
    private static final String INTENT_DISCOVERABLE_TIMEOUT = "android.bluetooth.intent.DISCOVERABLE_TIMEOUT";
    private static final String TAG = "BluetoothDiscoverableTimeoutReceiver";

    public static void setDiscoverableAlarm(Context context, long j) {
        Log.d(TAG, "setDiscoverableAlarm(): alarmTime = " + j);
        Intent intent = new Intent(INTENT_DISCOVERABLE_TIMEOUT);
        intent.setClass(context, BluetoothDiscoverableTimeoutReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 67108864);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        if (broadcast != null) {
            alarmManager.cancel(broadcast);
            Log.d(TAG, "setDiscoverableAlarm(): cancel prev alarm");
        }
        alarmManager.set(0, j, PendingIntent.getBroadcast(context, 0, intent, 67108864));
    }

    public static void cancelDiscoverableAlarm(Context context) {
        Log.d(TAG, "cancelDiscoverableAlarm(): Enter");
        Intent intent = new Intent(INTENT_DISCOVERABLE_TIMEOUT);
        intent.setClass(context, BluetoothDiscoverableTimeoutReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 603979776);
        if (broadcast != null) {
            ((AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(broadcast);
        }
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(INTENT_DISCOVERABLE_TIMEOUT)) {
            LocalBluetoothAdapter instance = LocalBluetoothAdapter.getInstance();
            if (instance == null || instance.getState() != 12) {
                Log.e(TAG, "localBluetoothAdapter is NULL!!");
                return;
            }
            Log.d(TAG, "Disable discoverable...");
            instance.setScanMode(21);
        }
    }
}
