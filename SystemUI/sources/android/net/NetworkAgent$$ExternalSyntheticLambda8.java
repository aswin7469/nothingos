package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda8 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ NetworkInfo f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda8(NetworkInfo networkInfo) {
        this.f$0 = networkInfo;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendNetworkInfo(this.f$0);
    }
}
