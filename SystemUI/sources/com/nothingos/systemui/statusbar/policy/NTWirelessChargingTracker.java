package com.nothingos.systemui.statusbar.policy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.nothing.systemui.util.SystemUIEventUtils;
import java.util.Calendar;
import java.util.TimeZone;

public class NTWirelessChargingTracker {
    private static final String ACTION_UPLOAD_TRACK_RESULT = "com.nothing.systemui.UPLOAD_TRACK_RESULT";
    private static final String KEY_BATTERY_SHARE_TIME = "persist.battery.share.time";
    private static final String KEY_WIRELESS_CHARGING_TIME = "persist.wireless.charging.time";
    private static final String PREF_NAME_BATTERY = "PREF_BATTERY";
    private static final String TAG = "WirelessChargingTracker";
    private static NTWirelessChargingTracker sInstance;
    private AlarmManager mAlarmManager;
    private Long mBatteryShareStartTime = 0L;
    private Context mContext;
    private PendingIntent mPendingIntent;
    private Long mWirelessChargingStartTime = 0L;
    private WirelessChargingTrackerAlarmReceiver mWirelessChargingTrackerAlarmReceiver;

    private NTWirelessChargingTracker(Context context) {
        this.mContext = context;
        this.mAlarmManager = (AlarmManager) context.getSystemService(NotificationCompat.CATEGORY_ALARM);
        this.mWirelessChargingTrackerAlarmReceiver = new WirelessChargingTrackerAlarmReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPLOAD_TRACK_RESULT);
        this.mContext.registerReceiver(this.mWirelessChargingTrackerAlarmReceiver, intentFilter);
        scheduleAlarm();
    }

    public static NTWirelessChargingTracker getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NTWirelessChargingTracker(context);
        }
        return sInstance;
    }

    /* access modifiers changed from: private */
    public void scheduleAlarm() {
        this.mPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_UPLOAD_TRACK_RESULT), 67108864);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.setTimeZone(TimeZone.getDefault());
        instance.set(11, 0);
        instance.set(12, 0);
        instance.set(13, 0);
        instance.set(14, 0);
        if (System.currentTimeMillis() > instance.getTimeInMillis()) {
            instance.add(5, 1);
        }
        this.mAlarmManager.setExact(0, instance.getTimeInMillis(), this.mPendingIntent);
        Log.d(TAG, "scheduleAlarm triggerTime " + instance.getTimeInMillis());
    }

    public void startRecordWirelessChargingTime() {
        if (this.mWirelessChargingStartTime.longValue() <= 0) {
            this.mWirelessChargingStartTime = Long.valueOf(System.currentTimeMillis());
            Log.d(TAG, "startRecordWirelessChargingTime " + this.mWirelessChargingStartTime);
        }
    }

    public void startRecordBatteryShareTime() {
        if (this.mBatteryShareStartTime.longValue() <= 0) {
            this.mBatteryShareStartTime = Long.valueOf(System.currentTimeMillis());
            Log.d(TAG, "startRecordBatteryShareTime " + this.mBatteryShareStartTime);
        }
    }

    public void endRecordWirelessChargingTime() {
        if (this.mWirelessChargingStartTime.longValue() != 0) {
            saveRecordInternal(KEY_WIRELESS_CHARGING_TIME, (int) ((Long.valueOf(System.currentTimeMillis()).longValue() - this.mWirelessChargingStartTime.longValue()) / 1000));
            this.mWirelessChargingStartTime = 0L;
        }
    }

    public void endRecordBatteryShareTime() {
        if (this.mBatteryShareStartTime.longValue() != 0) {
            saveRecordInternal(KEY_BATTERY_SHARE_TIME, (int) ((Long.valueOf(System.currentTimeMillis()).longValue() - this.mBatteryShareStartTime.longValue()) / 1000));
            this.mBatteryShareStartTime = 0L;
        }
    }

    private void saveRecordInternal(String str, int i) {
        int i2 = 0;
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PREF_NAME_BATTERY, 0);
        int i3 = sharedPreferences.getInt(str, 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (i > 0) {
            i2 = i3 + i;
        }
        edit.putInt(str, i2);
        edit.apply();
        Log.d(TAG, "saveRecordInternal key = " + str + ", time = " + i + ", record time = " + i2);
    }

    private void checkIfRecordingBeforeUpload() {
        if (this.mWirelessChargingStartTime.longValue() > 0) {
            endRecordWirelessChargingTime();
            startRecordWirelessChargingTime();
        }
        if (this.mBatteryShareStartTime.longValue() > 0) {
            endRecordBatteryShareTime();
            startRecordBatteryShareTime();
        }
    }

    public void uploadRecord() {
        Log.d(TAG, "uploadRecord");
        checkIfRecordingBeforeUpload();
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PREF_NAME_BATTERY, 0);
        if (sharedPreferences.getInt(KEY_WIRELESS_CHARGING_TIME, 0) > 0) {
            SystemUIEventUtils.collectWirelessChargingTime(this.mContext, sharedPreferences.getInt(KEY_WIRELESS_CHARGING_TIME, 0));
            saveRecordInternal(KEY_WIRELESS_CHARGING_TIME, -1);
        }
        if (sharedPreferences.getInt(KEY_BATTERY_SHARE_TIME, 0) > 0) {
            SystemUIEventUtils.collectBatteryShareTime(this.mContext, sharedPreferences.getInt(KEY_BATTERY_SHARE_TIME, 0));
            saveRecordInternal(KEY_BATTERY_SHARE_TIME, -1);
        }
    }

    private class WirelessChargingTrackerAlarmReceiver extends BroadcastReceiver {
        private WirelessChargingTrackerAlarmReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            NTWirelessChargingTracker.this.uploadRecord();
            NTWirelessChargingTracker.this.scheduleAlarm();
        }
    }
}
