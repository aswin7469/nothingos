package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda10 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda10(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendQosCallbackError(this.f$0, this.f$1);
    }
}
