package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda11(Object obj, Object obj2) {
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    public final void run() {
        ((NsdManager.RegistrationListener) this.f$0).onServiceRegistered((NsdServiceInfo) this.f$1);
    }
}
