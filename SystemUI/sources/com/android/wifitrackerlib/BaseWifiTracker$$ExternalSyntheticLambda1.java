package com.android.wifitrackerlib;

import com.android.wifitrackerlib.BaseWifiTracker;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BaseWifiTracker$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BaseWifiTracker.BaseWifiTrackerCallback f$0;

    public /* synthetic */ BaseWifiTracker$$ExternalSyntheticLambda1(BaseWifiTracker.BaseWifiTrackerCallback baseWifiTrackerCallback) {
        this.f$0 = baseWifiTrackerCallback;
    }

    public final void run() {
        this.f$0.onWifiStateChanged();
    }
}
