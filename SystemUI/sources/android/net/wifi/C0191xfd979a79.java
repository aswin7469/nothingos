package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$WifiConnectedNetworkScorerProxy$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0191xfd979a79 implements Runnable {
    public final /* synthetic */ WifiManager.WifiConnectedNetworkScorerProxy f$0;
    public final /* synthetic */ IScoreUpdateObserver f$1;

    public /* synthetic */ C0191xfd979a79(WifiManager.WifiConnectedNetworkScorerProxy wifiConnectedNetworkScorerProxy, IScoreUpdateObserver iScoreUpdateObserver) {
        this.f$0 = wifiConnectedNetworkScorerProxy;
        this.f$1 = iScoreUpdateObserver;
    }

    public final void run() {
        this.f$0.mo5121xe92edd89(this.f$1);
    }
}
