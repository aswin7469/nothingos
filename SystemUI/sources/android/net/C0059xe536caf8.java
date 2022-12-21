package android.net;

import android.net.EthernetManager;

/* renamed from: android.net.EthernetManager$NetworkInterfaceOutcomeReceiver$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0059xe536caf8 implements Runnable {
    public final /* synthetic */ EthernetManager.NetworkInterfaceOutcomeReceiver f$0;
    public final /* synthetic */ EthernetNetworkManagementException f$1;

    public /* synthetic */ C0059xe536caf8(EthernetManager.NetworkInterfaceOutcomeReceiver networkInterfaceOutcomeReceiver, EthernetNetworkManagementException ethernetNetworkManagementException) {
        this.f$0 = networkInterfaceOutcomeReceiver;
        this.f$1 = ethernetNetworkManagementException;
    }

    public final void run() {
        this.f$0.mo2138x83454e8f(this.f$1);
    }
}
