package java.util;

import java.p026io.Serializable;
import java.util.function.ToDoubleFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Comparator$$ExternalSyntheticLambda0 implements Comparator, Serializable {
    public final /* synthetic */ ToDoubleFunction f$0;

    public /* synthetic */ Comparator$$ExternalSyntheticLambda0(ToDoubleFunction toDoubleFunction) {
        this.f$0 = toDoubleFunction;
    }

    public final int compare(Object obj, Object obj2) {
        return Double.compare(this.f$0.applyAsDouble(obj), this.f$0.applyAsDouble(obj2));
    }
}
