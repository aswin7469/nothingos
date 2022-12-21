package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda13 implements TetheringManager.RequestHelper {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda13(TetheringManager tetheringManager, String str, String str2) {
        this.f$0 = tetheringManager;
        this.f$1 = str;
        this.f$2 = str2;
    }

    public final void runRequest(ITetheringConnector iTetheringConnector, IIntResultListener iIntResultListener) {
        this.f$0.m1951lambda$untether$4$androidnetTetheringManager(this.f$1, this.f$2, iTetheringConnector, iIntResultListener);
    }
}
