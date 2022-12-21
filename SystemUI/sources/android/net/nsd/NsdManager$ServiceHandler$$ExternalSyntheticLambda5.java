package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ NsdServiceInfo f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda5(Object obj, NsdServiceInfo nsdServiceInfo, int i) {
        this.f$0 = obj;
        this.f$1 = nsdServiceInfo;
        this.f$2 = i;
    }

    public final void run() {
        ((NsdManager.DiscoveryListener) this.f$0).onStartDiscoveryFailed(NsdManager.getNsdServiceInfoType(this.f$1), this.f$2);
    }
}
