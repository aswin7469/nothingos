package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda3 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ DscpPolicy f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda3(DscpPolicy dscpPolicy) {
        this.f$0 = dscpPolicy;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendAddDscpPolicy(this.f$0);
    }
}
