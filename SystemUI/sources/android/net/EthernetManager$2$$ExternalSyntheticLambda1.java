package android.net;

import android.net.EthernetManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class EthernetManager$2$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ EthernetManager.TetheredInterfaceCallback f$0;

    public /* synthetic */ EthernetManager$2$$ExternalSyntheticLambda1(EthernetManager.TetheredInterfaceCallback tetheredInterfaceCallback) {
        this.f$0 = tetheredInterfaceCallback;
    }

    public final void run() {
        this.f$0.onUnavailable();
    }
}
