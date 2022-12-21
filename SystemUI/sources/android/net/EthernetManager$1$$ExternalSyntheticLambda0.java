package android.net;

import android.net.EthernetManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class EthernetManager$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ EthernetManager.InterfaceStateListener f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ IpConfiguration f$4;

    public /* synthetic */ EthernetManager$1$$ExternalSyntheticLambda0(EthernetManager.InterfaceStateListener interfaceStateListener, String str, int i, int i2, IpConfiguration ipConfiguration) {
        this.f$0 = interfaceStateListener;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = ipConfiguration;
    }

    public final void run() {
        this.f$0.onInterfaceStateChanged(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
