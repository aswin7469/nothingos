package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda9 implements TetheringManager.ConnectorConsumer {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda9(TetheringManager tetheringManager, String str) {
        this.f$0 = tetheringManager;
        this.f$1 = str;
    }

    public final void onConnectorAvailable(ITetheringConnector iTetheringConnector) {
        this.f$0.m1942lambda$finalize$1$androidnetTetheringManager(this.f$1, iTetheringConnector);
    }
}
