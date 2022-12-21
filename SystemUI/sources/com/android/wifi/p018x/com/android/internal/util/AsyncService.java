package com.android.wifi.p018x.com.android.internal.util;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/* renamed from: com.android.wifi.x.com.android.internal.util.AsyncService */
public abstract class AsyncService extends Service {
    public static final int CMD_ASYNC_SERVICE_DESTROY = 16777216;
    public static final int CMD_ASYNC_SERVICE_ON_START_INTENT = 16777215;
    protected static final boolean DBG = true;
    private static final String TAG = "AsyncService";
    AsyncServiceInfo mAsyncServiceInfo;
    Handler mHandler;
    protected Messenger mMessenger;

    /* renamed from: com.android.wifi.x.com.android.internal.util.AsyncService$AsyncServiceInfo */
    public static final class AsyncServiceInfo {
        public Handler mHandler;
        public int mRestartFlags;
    }

    public abstract AsyncServiceInfo createHandler();

    public Handler getHandler() {
        return this.mHandler;
    }

    public void onCreate() {
        super.onCreate();
        AsyncServiceInfo createHandler = createHandler();
        this.mAsyncServiceInfo = createHandler;
        this.mHandler = createHandler.mHandler;
        this.mMessenger = new Messenger(this.mHandler);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "onStartCommand");
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 16777215;
        obtainMessage.arg1 = i;
        obtainMessage.arg2 = i2;
        obtainMessage.obj = intent;
        this.mHandler.sendMessage(obtainMessage);
        return this.mAsyncServiceInfo.mRestartFlags;
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        Message obtainMessage = this.mHandler.obtainMessage();
        obtainMessage.what = 16777216;
        this.mHandler.sendMessage(obtainMessage);
    }

    public IBinder onBind(Intent intent) {
        return this.mMessenger.getBinder();
    }
}
