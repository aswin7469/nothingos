package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$4$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ TetheringManager.TetheringEventCallback f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ TetheringManager$4$$ExternalSyntheticLambda1(TetheringManager.TetheringEventCallback tetheringEventCallback, long j) {
        this.f$0 = tetheringEventCallback;
        this.f$1 = j;
    }

    public final void run() {
        this.f$0.onSupportedTetheringTypes(TetheringManager.unpackBits(this.f$1));
    }
}
