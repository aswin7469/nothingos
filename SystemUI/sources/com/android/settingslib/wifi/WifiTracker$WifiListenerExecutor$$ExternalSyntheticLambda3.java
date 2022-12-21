package com.android.settingslib.wifi;

import com.android.settingslib.wifi.WifiTracker;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ WifiTracker.WifiListener f$0;

    public /* synthetic */ WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda3(WifiTracker.WifiListener wifiListener) {
        this.f$0 = wifiListener;
    }

    public final void run() {
        this.f$0.onAccessPointsChanged();
    }
}
