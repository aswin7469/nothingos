package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda2 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda2(boolean z, boolean z2) {
        this.f$0 = z;
        this.f$1 = z2;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendExplicitlySelected(this.f$0, this.f$1);
    }
}
