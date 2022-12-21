package java.util.stream;

import java.util.IntSummaryStatistics;
import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda54 implements BiConsumer {
    public final /* synthetic */ ToIntFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda54(ToIntFunction toIntFunction) {
        this.f$0 = toIntFunction;
    }

    public final void accept(Object obj, Object obj2) {
        ((IntSummaryStatistics) obj).accept(this.f$0.applyAsInt(obj2));
    }
}
