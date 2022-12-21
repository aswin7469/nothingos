package com.android.wifitrackerlib;

import com.android.wifitrackerlib.WifiEntry;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ StandardWifiEntry f$0;
    public final /* synthetic */ WifiEntry.DisconnectCallback f$1;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda2(StandardWifiEntry standardWifiEntry, WifiEntry.DisconnectCallback disconnectCallback) {
        this.f$0 = standardWifiEntry;
        this.f$1 = disconnectCallback;
    }

    public final void run() {
        this.f$0.m3373lambda$disconnect$2$comandroidwifitrackerlibStandardWifiEntry(this.f$1);
    }
}
