package android.net;

import android.net.NetworkAgent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda1 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda1(int i, int i2, int i3) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = i3;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendQosSessionLost(this.f$0, new QosSession(this.f$1, this.f$2));
    }
}
