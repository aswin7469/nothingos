package android.net.wifi;

import android.net.wifi.WifiManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiManager.WifiVerboseLoggingStatusChangedListener f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ WifiManager$3$$ExternalSyntheticLambda0(WifiManager.WifiVerboseLoggingStatusChangedListener wifiVerboseLoggingStatusChangedListener, boolean z) {
        this.f$0 = wifiVerboseLoggingStatusChangedListener;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.onWifiVerboseLoggingStatusChanged(this.f$1);
    }
}
