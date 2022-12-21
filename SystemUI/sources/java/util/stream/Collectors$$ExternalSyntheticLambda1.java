package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda1 implements BiConsumer {
    public final /* synthetic */ ToIntFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda1(ToIntFunction toIntFunction) {
        this.f$0 = toIntFunction;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$averagingInt$31(this.f$0, (long[]) obj, obj2);
    }
}
