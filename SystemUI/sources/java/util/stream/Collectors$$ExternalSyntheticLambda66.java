package java.util.stream;

import java.util.LongSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.ToLongFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda66 implements BiConsumer {
    public final /* synthetic */ ToLongFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda66(ToLongFunction toLongFunction) {
        this.f$0 = toLongFunction;
    }

    public final void accept(Object obj, Object obj2) {
        ((LongSummaryStatistics) obj).accept(this.f$0.applyAsLong(obj2));
    }
}
