package android.net.nsd;

import android.net.nsd.NsdManager;

/* renamed from: android.net.nsd.NsdManager$PerNetworkDiscoveryTracker$DelegatingDiscoveryListener$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0131x95a94bbe implements Runnable {
    public final /* synthetic */ NsdManager.PerNetworkDiscoveryTracker.DelegatingDiscoveryListener f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ C0131x95a94bbe(NsdManager.PerNetworkDiscoveryTracker.DelegatingDiscoveryListener delegatingDiscoveryListener, String str) {
        this.f$0 = delegatingDiscoveryListener;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.mo3838xcc9f8e69(this.f$1);
    }
}
