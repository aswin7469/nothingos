package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.ToLongFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda89 implements BiConsumer {
    public final /* synthetic */ ToLongFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda89(ToLongFunction toLongFunction) {
        this.f$0 = toLongFunction;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$summingLong$23(this.f$0, (long[]) obj, obj2);
    }
}
