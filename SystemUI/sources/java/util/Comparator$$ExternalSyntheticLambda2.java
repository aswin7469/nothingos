package java.util;

import java.p026io.Serializable;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Comparator$$ExternalSyntheticLambda2 implements Comparator, Serializable {
    public final /* synthetic */ Comparator f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ Comparator$$ExternalSyntheticLambda2(Comparator comparator, Function function) {
        this.f$0 = comparator;
        this.f$1 = function;
    }

    public final int compare(Object obj, Object obj2) {
        return this.f$0.compare(this.f$1.apply(obj), this.f$1.apply(obj2));
    }
}
