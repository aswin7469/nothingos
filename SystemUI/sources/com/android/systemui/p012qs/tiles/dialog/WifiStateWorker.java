package com.android.systemui.p012qs.tiles.dialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.util.concurrency.DelayableExecutor;
import javax.inject.Inject;

@SysUISingleton
/* renamed from: com.android.systemui.qs.tiles.dialog.WifiStateWorker */
public class WifiStateWorker extends BroadcastReceiver {
    private static final String TAG = "WifiStateWorker";
    private DelayableExecutor mBackgroundExecutor;
    private WifiManager mWifiManager;
    private int mWifiState = 1;

    @Inject
    public WifiStateWorker(BroadcastDispatcher broadcastDispatcher, @Background DelayableExecutor delayableExecutor, WifiManager wifiManager) {
        this.mWifiManager = wifiManager;
        this.mBackgroundExecutor = delayableExecutor;
        broadcastDispatcher.registerReceiver(this, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        this.mBackgroundExecutor.execute(new WifiStateWorker$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-tiles-dialog-WifiStateWorker */
    public /* synthetic */ void mo37097xefab5b9d() {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            this.mWifiState = wifiManager.getWifiState();
            Log.i(TAG, "WifiManager.getWifiState():" + this.mWifiState);
        }
    }

    public void setWifiEnabled(boolean z) {
        this.mBackgroundExecutor.execute(new WifiStateWorker$$ExternalSyntheticLambda0(this, z));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setWifiEnabled$1$com-android-systemui-qs-tiles-dialog-WifiStateWorker */
    public /* synthetic */ void mo37098xcf6eb5d8(boolean z) {
        WifiManager wifiManager = this.mWifiManager;
        if (wifiManager != null) {
            this.mWifiState = z ? 2 : 0;
            if (!wifiManager.setWifiEnabled(z)) {
                Log.e(TAG, "Failed to WifiManager.setWifiEnabled(" + z + ");");
            }
        }
    }

    public int getWifiState() {
        return this.mWifiState;
    }

    public boolean isWifiEnabled() {
        int i = this.mWifiState;
        return i == 3 || i == 2;
    }

    public void onReceive(Context context, Intent intent) {
        int intExtra;
        if (intent != null && WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction()) && (intExtra = intent.getIntExtra("wifi_state", 1)) != 4) {
            this.mWifiState = intExtra;
        }
    }
}
