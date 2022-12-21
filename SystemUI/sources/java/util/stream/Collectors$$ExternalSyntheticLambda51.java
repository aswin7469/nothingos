package java.util.stream;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda51 implements BiConsumer {
    public final /* synthetic */ Function f$0;
    public final /* synthetic */ Supplier f$1;
    public final /* synthetic */ BiConsumer f$2;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda51(Function function, Supplier supplier, BiConsumer biConsumer) {
        this.f$0 = function;
        this.f$1 = supplier;
        this.f$2 = biConsumer;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$2.accept(((Map) obj).computeIfAbsent(Objects.requireNonNull(this.f$0.apply(obj2), "element cannot be mapped to a null key"), new Collectors$$ExternalSyntheticLambda78(this.f$1)), obj2);
    }
}
