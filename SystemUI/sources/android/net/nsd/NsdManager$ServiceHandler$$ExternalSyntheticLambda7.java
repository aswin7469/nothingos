package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda7(Object obj, Object obj2) {
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    public final void run() {
        ((NsdManager.DiscoveryListener) this.f$0).onServiceLost((NsdServiceInfo) this.f$1);
    }
}