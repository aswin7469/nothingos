package android.net.nsd;

import android.net.nsd.NsdManager;

/* renamed from: android.net.nsd.NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0133x95a94bc0 implements Runnable {
    public final /* synthetic */ NsdManager.PerNetworkDiscoveryTracker.DelegatingDiscoveryListener f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ C0133x95a94bc0(NsdManager.PerNetworkDiscoveryTracker.DelegatingDiscoveryListener delegatingDiscoveryListener, String str) {
        this.f$0 = delegatingDiscoveryListener;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.mo3835x8f61d8f4(this.f$1);
    }
}
