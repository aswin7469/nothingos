package android.net.nsd;

import android.net.nsd.NsdManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NsdManager$ServiceHandler$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ NsdServiceInfo f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NsdManager$ServiceHandler$$ExternalSyntheticLambda1(Object obj, NsdServiceInfo nsdServiceInfo, int i) {
        this.f$0 = obj;
        this.f$1 = nsdServiceInfo;
        this.f$2 = i;
    }

    public final void run() {
        ((NsdManager.RegistrationListener) this.f$0).onUnregistrationFailed(this.f$1, this.f$2);
    }
}
