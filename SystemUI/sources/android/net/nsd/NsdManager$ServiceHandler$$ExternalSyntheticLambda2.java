package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ NsdServiceInfo f$1;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda2(Object obj, NsdServiceInfo nsdServiceInfo) {
        this.f$0 = obj;
        this.f$1 = nsdServiceInfo;
    }

    public final void run() {
        ((NsdManager.RegistrationListener) this.f$0).onServiceUnregistered(this.f$1);
    }
}
