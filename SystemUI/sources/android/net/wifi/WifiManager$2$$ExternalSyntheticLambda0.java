package android.net.wifi;

import android.net.wifi.WifiManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiManager.OnWifiUsabilityStatsListener f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ WifiUsabilityStatsEntry f$3;

    public /* synthetic */ WifiManager$2$$ExternalSyntheticLambda0(WifiManager.OnWifiUsabilityStatsListener onWifiUsabilityStatsListener, int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) {
        this.f$0 = onWifiUsabilityStatsListener;
        this.f$1 = i;
        this.f$2 = z;
        this.f$3 = wifiUsabilityStatsEntry;
    }

    public final void run() {
        this.f$0.onWifiUsabilityStats(this.f$1, this.f$2, this.f$3);
    }
}
