package android.net.wifi;

import android.net.wifi.WifiScanner;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiScanner$ServiceHandler$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ WifiScanner.ScanListener f$0;
    public final /* synthetic */ ScanResult f$1;

    public /* synthetic */ WifiScanner$ServiceHandler$$ExternalSyntheticLambda3(WifiScanner.ScanListener scanListener, ScanResult scanResult) {
        this.f$0 = scanListener;
        this.f$1 = scanResult;
    }

    public final void run() {
        this.f$0.onFullResult(this.f$1);
    }
}
