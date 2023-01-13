package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda7 implements TetheringManager.ConnectorConsumer {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda7(TetheringManager tetheringManager, int i, String str) {
        this.f$0 = tetheringManager;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void onConnectorAvailable(ITetheringConnector iTetheringConnector) {
        this.f$0.m1955lambda$stopTethering$7$androidnetTetheringManager(this.f$1, this.f$2, iTetheringConnector);
    }
}
