package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda4 implements TetheringManager.RequestHelper {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda4(TetheringManager tetheringManager, boolean z, String str) {
        this.f$0 = tetheringManager;
        this.f$1 = z;
        this.f$2 = str;
    }

    public final void runRequest(ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        this.f$0.m1945lambda$setUsbTethering$5$androidnetTetheringManager(this.f$1, this.f$2, iTetheringConnector, iIntResultListener);
    }
}
