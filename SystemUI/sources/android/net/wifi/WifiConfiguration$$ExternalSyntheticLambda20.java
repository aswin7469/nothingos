package android.net.wifi;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda20 implements Consumer {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ WifiConfiguration$$ExternalSyntheticLambda20(boolean z, boolean z2) {
        this.f$0 = z;
        this.f$1 = z2;
    }

    public final void accept(Object obj) {
        ((SecurityParams) obj).enableSuiteBCiphers(this.f$0, this.f$1);
    }
}