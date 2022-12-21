package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda11 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ NetworkScore f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda11(NetworkScore networkScore) {
        this.f$0 = networkScore;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendScore(this.f$0);
    }
}
