package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda6 implements TetheringManager.RequestHelper {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda6(TetheringManager tetheringManager, String str) {
        this.f$0 = tetheringManager;
        this.f$1 = str;
    }

    public final void runRequest(ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        this.f$0.m1943lambda$isTetheringSupported$11$androidnetTetheringManager(this.f$1, iTetheringConnector, iIntResultListener);
    }
}
