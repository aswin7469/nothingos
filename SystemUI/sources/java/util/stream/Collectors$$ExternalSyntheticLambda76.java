package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda76 implements BiConsumer {
    public final /* synthetic */ Function f$0;
    public final /* synthetic */ BiConsumer f$1;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda76(Function function, BiConsumer biConsumer) {
        this.f$0 = function;
        this.f$1 = biConsumer;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$flatMapping$15(this.f$0, this.f$1, obj, obj2);
    }
}
