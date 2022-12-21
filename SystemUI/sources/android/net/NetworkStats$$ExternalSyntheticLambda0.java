package android.net;

import android.net.NetworkStats;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkStats$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ String[] f$2;

    public /* synthetic */ NetworkStats$$ExternalSyntheticLambda0(int i, int i2, String[] strArr) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = strArr;
    }

    public final boolean test(Object obj) {
        return NetworkStats.lambda$filter$2(this.f$0, this.f$1, this.f$2, (NetworkStats.Entry) obj);
    }
}
