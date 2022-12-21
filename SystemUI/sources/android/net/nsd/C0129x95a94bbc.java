package android.net.nsd;

import android.net.nsd.NsdManager;

/* renamed from: android.net.nsd.NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0129x95a94bbc implements Runnable {
    public final /* synthetic */ NsdManager.PerNetworkDiscoveryTracker.DelegatingDiscoveryListener f$0;
    public final /* synthetic */ NsdServiceInfo f$1;

    public /* synthetic */ C0129x95a94bbc(NsdManager.PerNetworkDiscoveryTracker.DelegatingDiscoveryListener delegatingDiscoveryListener, NsdServiceInfo nsdServiceInfo) {
        this.f$0 = delegatingDiscoveryListener;
        this.f$1 = nsdServiceInfo;
    }

    public final void run() {
        this.f$0.mo3837x79ccae7a(this.f$1);
    }
}
