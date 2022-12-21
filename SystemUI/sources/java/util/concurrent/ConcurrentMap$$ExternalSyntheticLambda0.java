package java.util.concurrent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConcurrentMap$$ExternalSyntheticLambda0 implements BiConsumer {
    public final /* synthetic */ ConcurrentMap f$0;
    public final /* synthetic */ BiFunction f$1;

    public /* synthetic */ ConcurrentMap$$ExternalSyntheticLambda0(ConcurrentMap concurrentMap, BiFunction biFunction) {
        this.f$0 = concurrentMap;
        this.f$1 = biFunction;
    }

    public final void accept(Object obj, Object obj2) {
        ConcurrentMap.lambda$replaceAll$0(this.f$0, this.f$1, obj, obj2);
    }
}
