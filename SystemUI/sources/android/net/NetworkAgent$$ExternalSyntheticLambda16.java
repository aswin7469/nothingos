package android.net;

import android.net.NetworkAgent;
import android.telephony.data.NrQosSessionAttributes;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda16 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ QosSessionAttributes f$2;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda16(int i, int i2, QosSessionAttributes qosSessionAttributes) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = qosSessionAttributes;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendNrQosSessionAvailable(this.f$0, new QosSession(this.f$1, 2), (NrQosSessionAttributes) this.f$2);
    }
}
