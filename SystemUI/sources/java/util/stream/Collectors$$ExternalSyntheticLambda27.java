package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda27 implements BiConsumer {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda27(BiConsumer biConsumer, Function function) {
        this.f$0 = biConsumer;
        this.f$1 = function;
    }

    public final void accept(Object obj, Object obj2) {
        this.f$0.accept(obj, this.f$1.apply(obj2));
    }
}
