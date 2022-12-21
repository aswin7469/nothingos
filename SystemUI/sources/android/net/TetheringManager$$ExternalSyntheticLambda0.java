package android.net;

import android.net.TetheringManager;
import android.os.ResultReceiver;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$$ExternalSyntheticLambda0 implements TetheringManager.ConnectorConsumer {
    public final /* synthetic */ TetheringManager f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ResultReceiver f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ String f$4;

    public /* synthetic */ TetheringManager$$ExternalSyntheticLambda0(TetheringManager tetheringManager, int i, ResultReceiver resultReceiver, boolean z, String str) {
        this.f$0 = tetheringManager;
        this.f$1 = i;
        this.f$2 = resultReceiver;
        this.f$3 = z;
        this.f$4 = str;
    }

    public final void onConnectorAvailable(ITetheringConnector iTetheringConnector) {
        this.f$0.mo3484x602116e8(this.f$1, this.f$2, this.f$3, this.f$4, iTetheringConnector);
    }
}
