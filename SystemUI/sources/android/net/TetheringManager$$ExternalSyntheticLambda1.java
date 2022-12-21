package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda1 implements TetheringManager.ConnectorConsumer {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ TetheringManager.TetheringRequest f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ IIntResultListener f$3;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda1(TetheringManager tetheringManager, TetheringManager.TetheringRequest tetheringRequest, String str, IIntResultListener iIntResultListener) {
        this.f$0 = tetheringManager;
        this.f$1 = tetheringRequest;
        this.f$2 = str;
        this.f$3 = iIntResultListener;
    }

    public final void onConnectorAvailable(ITetheringConnector iTetheringConnector) {
        this.f$0.m1947lambda$startTethering$6$androidnetTetheringManager(this.f$1, this.f$2, this.f$3, iTetheringConnector);
    }
}
