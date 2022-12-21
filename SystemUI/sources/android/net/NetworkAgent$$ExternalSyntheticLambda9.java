package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda9 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ int f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda9(int i) {
        this.f$0 = i;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendTeardownDelayMs(this.f$0);
    }
}
