package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import com.android.wifitrackerlib.StandardWifiEntry;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ StandardWifiEntry.ScanResultKey f$0;

    public /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda0(StandardWifiEntry.ScanResultKey scanResultKey) {
        this.f$0 = scanResultKey;
    }

    public final boolean test(Object obj) {
        return this.f$0.equals(new StandardWifiEntry.ScanResultKey((ScanResult) obj));
    }
}
