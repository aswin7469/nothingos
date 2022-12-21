package android.net.wifi;

import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ int f$0;

    public /* synthetic */ WifiConfiguration$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    public final boolean test(Object obj) {
        return ((SecurityParams) obj).isSecurityType(this.f$0);
    }
}
