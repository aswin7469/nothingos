package android.net.wifi;

import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda21 implements Predicate {
    public final boolean test(Object obj) {
        return ((SecurityParams) obj).getAllowedSuiteBCiphers().get(0);
    }
}
