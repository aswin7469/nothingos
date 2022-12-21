package android.net;

import android.net.NetworkStats;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkStats$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ int[] f$0;

    public /* synthetic */ NetworkStats$$ExternalSyntheticLambda1(int[] iArr) {
        this.f$0 = iArr;
    }

    public final boolean test(Object obj) {
        return NetworkStats.lambda$removeUids$0(this.f$0, (NetworkStats.Entry) obj);
    }
}
