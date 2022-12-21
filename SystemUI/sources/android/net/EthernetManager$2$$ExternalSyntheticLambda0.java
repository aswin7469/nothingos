package android.net;

import android.net.EthernetManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class EthernetManager$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ EthernetManager.TetheredInterfaceCallback f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ EthernetManager$2$$ExternalSyntheticLambda0(EthernetManager.TetheredInterfaceCallback tetheredInterfaceCallback, String str) {
        this.f$0 = tetheredInterfaceCallback;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.onAvailable(this.f$1);
    }
}
