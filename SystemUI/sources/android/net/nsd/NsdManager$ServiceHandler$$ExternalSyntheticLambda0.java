package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda0(Object obj, String str) {
        this.f$0 = obj;
        this.f$1 = str;
    }

    public final void run() {
        ((NsdManager.DiscoveryListener) this.f$0).onDiscoveryStarted(this.f$1);
    }
}
