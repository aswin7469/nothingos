package android.net.wifi;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda12 implements Consumer {
    public final /* synthetic */ StringBuilder f$0;

    public /* synthetic */ WifiConfiguration$$ExternalSyntheticLambda12(StringBuilder sb) {
        this.f$0 = sb;
    }

    public final void accept(Object obj) {
        this.f$0.append(((SecurityParams) obj).toString());
    }
}
