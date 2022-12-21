package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda6 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ NetworkCapabilities f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda6(NetworkCapabilities networkCapabilities) {
        this.f$0 = networkCapabilities;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendNetworkCapabilities(this.f$0);
    }
}
