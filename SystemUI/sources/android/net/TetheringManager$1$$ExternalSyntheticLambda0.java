package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ TetheringManager.StartTetheringCallback f$1;

    public /* synthetic */ TetheringManager$1$$ExternalSyntheticLambda0(int i, TetheringManager.StartTetheringCallback startTetheringCallback) {
        this.f$0 = i;
        this.f$1 = startTetheringCallback;
    }

    public final void run() {
        TetheringManager.C01091.lambda$onResult$0(this.f$0, this.f$1);
    }
}
