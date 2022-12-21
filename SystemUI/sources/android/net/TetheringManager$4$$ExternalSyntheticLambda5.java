package android.net;

import android.net.TetheringManager;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TetheringManager$4$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ TetheringManager.TetheringEventCallback f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ TetheringManager$4$$ExternalSyntheticLambda5(TetheringManager.TetheringEventCallback tetheringEventCallback, List list) {
        this.f$0 = tetheringEventCallback;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.onClientsChanged(this.f$1);
    }
}
