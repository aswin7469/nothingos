package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ StandardWifiEntry f$0;
    public final /* synthetic */ StringBuilder f$1;
    public final /* synthetic */ long f$2;

    public /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda6(StandardWifiEntry standardWifiEntry, StringBuilder sb, long j) {
        this.f$0 = standardWifiEntry;
        this.f$1 = sb;
        this.f$2 = j;
    }

    public final void accept(Object obj) {
        this.f$0.mo47850x2217af08(this.f$1, this.f$2, (ScanResult) obj);
    }
}
