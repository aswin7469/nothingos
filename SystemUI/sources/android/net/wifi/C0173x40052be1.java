package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$NetworkRequestMatchCallbackProxy$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0173x40052be1 implements Runnable {
    public final /* synthetic */ WifiManager.NetworkRequestMatchCallbackProxy f$0;
    public final /* synthetic */ WifiConfiguration f$1;

    public /* synthetic */ C0173x40052be1(WifiManager.NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy, WifiConfiguration wifiConfiguration) {
        this.f$0 = networkRequestMatchCallbackProxy;
        this.f$1 = wifiConfiguration;
    }

    public final void run() {
        this.f$0.mo5059xa951800c(this.f$1);
    }
}
