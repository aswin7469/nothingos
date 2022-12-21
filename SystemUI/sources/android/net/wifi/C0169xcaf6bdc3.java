package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$LocalOnlyHotspotObserverProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0169xcaf6bdc3 implements Runnable {
    public final /* synthetic */ WifiManager.LocalOnlyHotspotObserverProxy f$0;
    public final /* synthetic */ SoftApConfiguration f$1;

    public /* synthetic */ C0169xcaf6bdc3(WifiManager.LocalOnlyHotspotObserverProxy localOnlyHotspotObserverProxy, SoftApConfiguration softApConfiguration) {
        this.f$0 = localOnlyHotspotObserverProxy;
        this.f$1 = softApConfiguration;
    }

    public final void run() {
        this.f$0.mo5041x6b9f3713(this.f$1);
    }
}
