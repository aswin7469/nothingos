package android.net.wifi;

import android.net.wifi.WifiManager;

/* renamed from: android.net.wifi.WifiManager$LocalOnlyHotspotCallbackProxy$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0166x7b940274 implements Runnable {
    public final /* synthetic */ WifiManager.LocalOnlyHotspotCallbackProxy f$0;
    public final /* synthetic */ WifiManager.LocalOnlyHotspotReservation f$1;

    public /* synthetic */ C0166x7b940274(WifiManager.LocalOnlyHotspotCallbackProxy localOnlyHotspotCallbackProxy, WifiManager.LocalOnlyHotspotReservation localOnlyHotspotReservation) {
        this.f$0 = localOnlyHotspotCallbackProxy;
        this.f$1 = localOnlyHotspotReservation;
    }

    public final void run() {
        this.f$0.mo5036x8c08aba5(this.f$1);
    }
}
