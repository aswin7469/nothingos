package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$4$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TetheringManager.TetheringEventCallback f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ TetheringManager$4$$ExternalSyntheticLambda0(TetheringManager.TetheringEventCallback tetheringEventCallback, int i) {
        this.f$0 = tetheringEventCallback;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onOffloadStatusChanged(this.f$1);
    }
}
