package com.android.settingslib.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.HandlerThread;
import android.util.Log;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WifiStateWorker {
    private static final String TAG = "WifiStateWorker";
    private static WifiStateWorker sInstance;
    private static final Object sLock = new Object();
    private static Map<Context, WifiStateWorker> sTestInstances;
    static WifiManager sWifiManager;
    /* access modifiers changed from: private */
    public static int sWifiState;
    private static HandlerThread sWorkerThread;

    public static WifiStateWorker getInstance(Context context) {
        synchronized (sLock) {
            Map<Context, WifiStateWorker> map = sTestInstances;
            if (map == null || !map.containsKey(context)) {
                WifiStateWorker wifiStateWorker = sInstance;
                if (wifiStateWorker != null) {
                    return wifiStateWorker;
                }
                sInstance = new WifiStateWorker();
                HandlerThread handlerThread = new HandlerThread("WifiStateWorker:{" + context.getApplicationInfo().className + "}", -4);
                sWorkerThread = handlerThread;
                handlerThread.start();
                sWorkerThread.getThreadHandler().post(new WifiStateWorker$$ExternalSyntheticLambda0(context));
                WifiStateWorker wifiStateWorker2 = sInstance;
                return wifiStateWorker2;
            }
            WifiStateWorker wifiStateWorker3 = sTestInstances.get(context);
            Log.w(TAG, "The context owner try to use a test instance:" + wifiStateWorker3);
            return wifiStateWorker3;
        }
    }

    public static void setTestInstance(Context context, WifiStateWorker wifiStateWorker) {
        synchronized (sLock) {
            if (sTestInstances == null) {
                sTestInstances = new ConcurrentHashMap();
            }
            Log.w(TAG, "Try to set a test instance by context:" + context);
            sTestInstances.put(context, wifiStateWorker);
        }
    }

    /* access modifiers changed from: private */
    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        applicationContext.registerReceiver(new IntentReceiver(), new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION), (String) null, sWorkerThread.getThreadHandler());
        sWifiManager = (WifiManager) applicationContext.getSystemService(WifiManager.class);
        refresh();
    }

    public static void refresh() {
        if (sWifiManager != null) {
            Log.d(TAG, "Start calling WifiManager#getWifiState.");
            sWifiState = sWifiManager.getWifiState();
            Log.d(TAG, "WifiManager#getWifiState return state:" + sWifiState);
        }
    }

    public int getWifiState() {
        return sWifiState;
    }

    public boolean isWifiEnabled() {
        return sWifiState == 3;
    }

    private static class IntentReceiver extends BroadcastReceiver {
        private IntentReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int unused = WifiStateWorker.sWifiState = intent.getIntExtra("wifi_state", 1);
            }
        }
    }
}
