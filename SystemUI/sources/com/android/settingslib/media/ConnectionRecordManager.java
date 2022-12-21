package com.android.settingslib.media;

import android.content.Context;
import android.content.SharedPreferences;

public class ConnectionRecordManager {
    private static final String KEY_LAST_SELECTED_DEVICE = "last_selected_device";
    private static final String SHARED_PREFERENCES_NAME = "seamless_transfer_record";
    private static final String TAG = "ConnectionRecordManager";
    private static ConnectionRecordManager sInstance;
    private static final Object sInstanceSync = new Object();
    private String mLastSelectedDevice;

    public static ConnectionRecordManager getInstance() {
        synchronized (sInstanceSync) {
            if (sInstance == null) {
                sInstance = new ConnectionRecordManager();
            }
        }
        return sInstance;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
    }

    public synchronized int fetchConnectionRecord(Context context, String str) {
        return getSharedPreferences(context).getInt(str, 0);
    }

    public synchronized void fetchLastSelectedDevice(Context context) {
        this.mLastSelectedDevice = getSharedPreferences(context).getString(KEY_LAST_SELECTED_DEVICE, (String) null);
    }

    public synchronized void setConnectionRecord(Context context, String str, int i) {
        SharedPreferences.Editor edit = getSharedPreferences(context).edit();
        this.mLastSelectedDevice = str;
        edit.putInt(str, i);
        edit.putString(KEY_LAST_SELECTED_DEVICE, this.mLastSelectedDevice);
        edit.apply();
    }

    public synchronized String getLastSelectedDevice() {
        return this.mLastSelectedDevice;
    }
}
