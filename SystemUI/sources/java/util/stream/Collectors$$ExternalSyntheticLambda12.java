package java.util.stream;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda12 implements BiConsumer {
    public final /* synthetic */ Function f$0;
    public final /* synthetic */ Supplier f$1;
    public final /* synthetic */ BiConsumer f$2;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda12(Function function, Supplier supplier, BiConsumer biConsumer) {
        this.f$0 = function;
        this.f$1 = supplier;
        this.f$2 = biConsumer;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$2.accept(((ConcurrentMap) obj).computeIfAbsent(Objects.requireNonNull(this.f$0.apply(obj2), "element cannot be mapped to a null key"), new Collectors$$ExternalSyntheticLambda80(this.f$1)), obj2);
    }
}
