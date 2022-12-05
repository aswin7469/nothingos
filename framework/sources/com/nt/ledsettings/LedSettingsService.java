package com.nt.ledsettings;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.provider.NtSettings;
import android.provider.Settings;
import android.text.TextUtils;
import com.nt.ledsettings.manager.BedTimeTaskManager;
/* loaded from: classes4.dex */
public class LedSettingsService extends Service {
    private static final String TAG = "LedSettingsService";
    private BroadcastReceiver mTimeUpdateReceiver = new BroadcastReceiver() { // from class: com.nt.ledsettings.LedSettingsService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_CHANGED)) {
                BedTimeTaskManager.getInstance(LedSettingsService.this.getApplication()).checkNextAlarm();
            }
        }
    };

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(getPackageName(), getClass().getSimpleName(), 4);
            NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
            notificationManager.createNotificationChannel(notificationChannel);
            Notification notification = new Notification.Builder(this, getPackageName()).setSmallIcon(17301651).build();
            notification.flags = 268435456;
            startForeground(-999, notification);
        }
        BedTimeTaskManager.getInstance(getApplication());
        LedBrightnessUtils.writeLedBrightness(getLedBrightnessFormSettings());
        registerUpdateTimeReceiver();
    }

    private int getLedBrightnessFormSettings() {
        return Settings.Global.getInt(getContentResolver(), NtSettings.Global.LED_BRIGHTNESS_VALUE, LedBrightnessUtils.getLedDefaultBrightness());
    }

    @Override // android.app.Service
    public void onDestroy() {
        unRegisterUpdateTimeReceiver();
        super.onDestroy();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerUpdateTimeReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(this.mTimeUpdateReceiver, filter);
    }

    private void unRegisterUpdateTimeReceiver() {
        unregisterReceiver(this.mTimeUpdateReceiver);
    }
}
