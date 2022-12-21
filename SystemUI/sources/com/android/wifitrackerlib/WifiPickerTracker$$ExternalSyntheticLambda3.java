package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiPickerTracker;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ WifiPickerTracker.WifiPickerTrackerCallback f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda3(WifiPickerTracker.WifiPickerTrackerCallback wifiPickerTrackerCallback) {
        this.f$0 = wifiPickerTrackerCallback;
    }

    public final void run() {
        this.f$0.onWifiEntriesChanged();
    }
}
