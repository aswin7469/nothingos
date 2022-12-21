package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda40 implements BiConsumer {
    public final /* synthetic */ ToIntFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda40(ToIntFunction toIntFunction) {
        this.f$0 = toIntFunction;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$summingInt$19(this.f$0, (int[]) obj, obj2);
    }
}
