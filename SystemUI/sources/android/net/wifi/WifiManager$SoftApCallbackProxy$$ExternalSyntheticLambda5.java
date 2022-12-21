package android.net.wifi;

import android.net.wifi.WifiManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ WifiManager.SoftApCallbackProxy f$0;
    public final /* synthetic */ WifiClient f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ WifiManager$SoftApCallbackProxy$$ExternalSyntheticLambda5(WifiManager.SoftApCallbackProxy softApCallbackProxy, WifiClient wifiClient, int i) {
        this.f$0 = softApCallbackProxy;
        this.f$1 = wifiClient;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.mo5099x47984039(this.f$1, this.f$2);
    }
}
