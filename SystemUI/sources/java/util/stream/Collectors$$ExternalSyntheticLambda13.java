package java.util.stream;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda13 implements BiConsumer {
    public final /* synthetic */ Function f$0;
    public final /* synthetic */ Supplier f$1;
    public final /* synthetic */ BiConsumer f$2;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda13(Function function, Supplier supplier, BiConsumer biConsumer) {
        this.f$0 = function;
        this.f$1 = supplier;
        this.f$2 = biConsumer;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$groupingByConcurrent$59(this.f$0, this.f$1, this.f$2, (ConcurrentMap) obj, obj2);
    }
}
