package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$WifiConnectedNetworkScorerProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0189xfd979a77 implements Runnable {
    public final /* synthetic */ WifiManager.WifiConnectedNetworkScorerProxy f$0;
    public final /* synthetic */ WifiConnectedSessionInfo f$1;

    public /* synthetic */ C0189xfd979a77(WifiManager.WifiConnectedNetworkScorerProxy wifiConnectedNetworkScorerProxy, WifiConnectedSessionInfo wifiConnectedSessionInfo) {
        this.f$0 = wifiConnectedNetworkScorerProxy;
        this.f$1 = wifiConnectedSessionInfo;
    }

    public final void run() {
        this.f$0.mo5122x33b91a30(this.f$1);
    }
}
