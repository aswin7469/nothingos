package java.util;

import java.p026io.Serializable;
import java.util.function.ToLongFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Comparator$$ExternalSyntheticLambda4 implements Comparator, Serializable {
    public final /* synthetic */ ToLongFunction f$0;

    public /* synthetic */ Comparator$$ExternalSyntheticLambda4(ToLongFunction toLongFunction) {
        this.f$0 = toLongFunction;
    }

    public final int compare(Object obj, Object obj2) {
        return Long.compare(this.f$0.applyAsLong(obj), this.f$0.applyAsLong(obj2));
    }
}
