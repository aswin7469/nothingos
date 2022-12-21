package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$3$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TetheringManager.OnTetheringEntitlementResultListener f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ TetheringManager$3$$ExternalSyntheticLambda0(TetheringManager.OnTetheringEntitlementResultListener onTetheringEntitlementResultListener, int i) {
        this.f$0 = onTetheringEntitlementResultListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onTetheringEntitlementResult(this.f$1);
    }
}
