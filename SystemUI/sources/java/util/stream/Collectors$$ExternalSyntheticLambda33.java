package java.util.stream;

import java.util.DoubleSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.ToDoubleFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda33 implements BiConsumer {
    public final /* synthetic */ ToDoubleFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda33(ToDoubleFunction toDoubleFunction) {
        this.f$0 = toDoubleFunction;
    }

    public final void accept(Object obj, Object obj2) {
        ((DoubleSummaryStatistics) obj).accept(this.f$0.applyAsDouble(obj2));
    }
}
