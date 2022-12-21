package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda0 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ long f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda0(long j) {
        this.f$0 = j;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendLingerDuration((int) this.f$0);
    }
}
