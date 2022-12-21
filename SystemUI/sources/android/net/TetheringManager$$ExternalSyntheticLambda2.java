package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda2 implements TetheringManager.ConnectorConsumer {
    public final /* synthetic */ ITetheringEventCallback f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda2(ITetheringEventCallback iTetheringEventCallback, String str) {
        this.f$0 = iTetheringEventCallback;
        this.f$1 = str;
    }

    public final void onConnectorAvailable(ITetheringConnector iTetheringConnector) {
        iTetheringConnector.unregisterTetheringEventCallback(this.f$0, this.f$1);
    }
}
