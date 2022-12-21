package android.net;

import android.net.NetworkAgent;
import java.util.ArrayList;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda13 implements NetworkAgent.RegistryAction {
    public final /* synthetic */ ArrayList f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda13(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    public final void execute(INetworkAgentRegistry iNetworkAgentRegistry) {
        iNetworkAgentRegistry.sendUnderlyingNetworks(this.f$0);
    }
}
