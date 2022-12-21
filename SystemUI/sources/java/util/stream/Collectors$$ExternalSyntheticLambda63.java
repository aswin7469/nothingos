package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.ToDoubleFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda63 implements BiConsumer {
    public final /* synthetic */ ToDoubleFunction f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda63(ToDoubleFunction toDoubleFunction) {
        this.f$0 = toDoubleFunction;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$summingDouble$27(this.f$0, (double[]) obj, obj2);
    }
}
