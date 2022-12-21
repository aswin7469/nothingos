package com.nothing.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.nothing.proxy.INothingService;
import com.nothing.sdk.utils.Common;

public class PowerManager {
    private static final String TAG = "ProxyPowerManager";
    private static PowerManager mInstance;
    /* access modifiers changed from: private */
    public Callback mCallback;
    private RemoteServiceConnection mConnection = new RemoteServiceConnection();
    private Context mContext;
    /* access modifiers changed from: private */
    public INothingService mService;

    public interface Callback {
        void onServiceConnected(ComponentName componentName);

        void onServiceDisconnected(ComponentName componentName);
    }

    public static PowerManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PowerManager(context);
        }
        return mInstance;
    }

    private PowerManager(Context context) {
        Log.d(TAG, "SDK Version:" + Common.getSDKVersion());
        this.mContext = context;
    }

    public void init(Callback callback) {
        this.mCallback = callback;
        Intent intent = new Intent();
        intent.setPackage("com.nothing.proxy");
        intent.setAction("com.nothing.proxy.bind_nothing_service");
        intent.setComponent(new ComponentName("com.nothing.proxy", "com.nothing.proxy.NothingService"));
        this.mContext.bindService(intent, this.mConnection, 1);
    }

    public void unInit() {
        this.mContext.unbindService(this.mConnection);
    }

    public void goToSleep(long j) {
        INothingService iNothingService = this.mService;
        if (iNothingService == null) {
            Log.e(TAG, "Service is null");
            return;
        }
        try {
            iNothingService.deviceGoToSleep(j);
        } catch (RemoteException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private class RemoteServiceConnection implements ServiceConnection {
        private RemoteServiceConnection() {
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(PowerManager.TAG, "Service connected");
            PowerManager.this.mService = INothingService.Stub.asInterface(iBinder);
            if (PowerManager.this.mCallback != null) {
                PowerManager.this.mCallback.onServiceConnected(componentName);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(PowerManager.TAG, "Service disconnected");
            PowerManager.this.mService = null;
            if (PowerManager.this.mCallback != null) {
                PowerManager.this.mCallback.onServiceDisconnected(componentName);
            }
        }
    }
}
