package android.net.wifi;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ WifiConfiguration$$ExternalSyntheticLambda1(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((SecurityParams) obj).enableSaeH2eOnlyMode(this.f$0);
    }
}