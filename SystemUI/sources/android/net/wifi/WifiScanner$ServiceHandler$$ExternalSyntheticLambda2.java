package android.net.wifi;

import android.net.wifi.WifiScanner;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiScanner$ServiceHandler$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ WifiScanner.ScanListener f$0;
    public final /* synthetic */ WifiScanner.ParcelableScanData f$1;

    public /* synthetic */ WifiScanner$ServiceHandler$$ExternalSyntheticLambda2(WifiScanner.ScanListener scanListener, WifiScanner.ParcelableScanData parcelableScanData) {
        this.f$0 = scanListener;
        this.f$1 = parcelableScanData;
    }

    public final void run() {
        this.f$0.onResults(this.f$1.getResults());
    }
}
