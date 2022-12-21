package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda9 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ NsdServiceInfo f$1;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda9(Object obj, NsdServiceInfo nsdServiceInfo) {
        this.f$0 = obj;
        this.f$1 = nsdServiceInfo;
    }

    public final void run() {
        ((NsdManager.DiscoveryListener) this.f$0).onDiscoveryStopped(NsdManager.getNsdServiceInfoType(this.f$1));
    }
}
