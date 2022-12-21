package java.util;

import java.p026io.Serializable;
import java.util.function.ToIntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Comparator$$ExternalSyntheticLambda3 implements Comparator, Serializable {
    public final /* synthetic */ ToIntFunction f$0;

    public /* synthetic */ Comparator$$ExternalSyntheticLambda3(ToIntFunction toIntFunction) {
        this.f$0 = toIntFunction;
    }

    public final int compare(Object obj, Object obj2) {
        return Integer.compare(this.f$0.applyAsInt(obj), this.f$0.applyAsInt(obj2));
    }
}
