package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$NetworkRequestMatchCallbackProxy$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0176x40052be4 implements Runnable {
    public final /* synthetic */ WifiManager.NetworkRequestMatchCallbackProxy f$0;
    public final /* synthetic */ INetworkRequestUserSelectionCallback f$1;

    public /* synthetic */ C0176x40052be4(WifiManager.NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy, INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) {
        this.f$0 = networkRequestMatchCallbackProxy;
        this.f$1 = iNetworkRequestUserSelectionCallback;
    }

    public final void run() {
        this.f$0.mo5058x7ed1c7a6(this.f$1);
    }
}
