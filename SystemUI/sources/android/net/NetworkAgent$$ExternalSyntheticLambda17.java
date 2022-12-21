package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda17 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ LinkProperties f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda17(LinkProperties linkProperties) {
        this.f$0 = linkProperties;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendLinkProperties(this.f$0);
    }
}
