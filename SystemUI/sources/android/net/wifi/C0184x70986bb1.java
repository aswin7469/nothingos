package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$ScanResultsCallback$ScanResultsCallbackProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0184x70986bb1 implements Runnable {
    public final /* synthetic */ WifiManager.ScanResultsCallback f$0;

    public /* synthetic */ C0184x70986bb1(WifiManager.ScanResultsCallback scanResultsCallback) {
        this.f$0 = scanResultsCallback;
    }

    public final void run() {
        this.f$0.onScanResultsAvailable();
    }
}
