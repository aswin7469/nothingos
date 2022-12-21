package java.util;

import java.p026io.Serializable;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Comparator$$ExternalSyntheticLambda5 implements Comparator, Serializable {
    public final /* synthetic */ Function f$0;

    public /* synthetic */ Comparator$$ExternalSyntheticLambda5(Function function) {
        this.f$0 = function;
    }

    public final int compare(Object obj, Object obj2) {
        return ((Comparable) this.f$0.apply(obj)).compareTo(this.f$0.apply(obj2));
    }
}
