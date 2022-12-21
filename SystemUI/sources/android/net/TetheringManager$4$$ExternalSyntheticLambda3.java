package android.net;

import android.net.TetheringManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$4$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ TetheringManager.TetheringEventCallback f$0;
    public final /* synthetic */ Network f$1;

    public /* synthetic */ TetheringManager$4$$ExternalSyntheticLambda3(TetheringManager.TetheringEventCallback tetheringEventCallback, Network network) {
        this.f$0 = tetheringEventCallback;
        this.f$1 = network;
    }

    public final void run() {
        this.f$0.onUpstreamChanged(this.f$1);
    }
}
