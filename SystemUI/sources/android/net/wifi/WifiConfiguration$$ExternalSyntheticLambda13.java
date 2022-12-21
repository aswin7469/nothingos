package android.net.wifi;

import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda13 implements Predicate {
    public final /* synthetic */ SecurityParams f$0;

    public /* synthetic */ WifiConfiguration$$ExternalSyntheticLambda13(SecurityParams securityParams) {
        this.f$0 = securityParams;
    }

    public final boolean test(Object obj) {
        return ((SecurityParams) obj).isSameSecurityType(this.f$0);
    }
}
