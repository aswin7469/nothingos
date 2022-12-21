package android.net.wifi;

import android.net.wifi.WifiScanner;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiScanner$ServiceHandler$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ WifiScanner.PnoScanListener f$0;
    public final /* synthetic */ WifiScanner.ParcelableScanResults f$1;

    public /* synthetic */ WifiScanner$ServiceHandler$$ExternalSyntheticLambda4(WifiScanner.PnoScanListener pnoScanListener, WifiScanner.ParcelableScanResults parcelableScanResults) {
        this.f$0 = pnoScanListener;
        this.f$1 = parcelableScanResults;
    }

    public final void run() {
        this.f$0.onPnoNetworkFound(this.f$1.getResults());
    }
}
