package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$4$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ TetheringManager.C01124 f$0;
    public final /* synthetic */ TetheringManager.TetheringEventCallback f$1;
    public final /* synthetic */ TetheringCallbackStartedParcel f$2;

    public /* synthetic */ TetheringManager$4$$ExternalSyntheticLambda6(TetheringManager.C01124 r1, TetheringManager.TetheringEventCallback tetheringEventCallback, TetheringCallbackStartedParcel tetheringCallbackStartedParcel) {
        this.f$0 = r1;
        this.f$1 = tetheringEventCallback;
        this.f$2 = tetheringCallbackStartedParcel;
    }

    public final void run() {
        this.f$0.m1952lambda$onCallbackStarted$1$androidnetTetheringManager$4(this.f$1, this.f$2);
    }
}
