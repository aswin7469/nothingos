package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda28 implements BiConsumer {
    public final /* synthetic */ Predicate f$0;
    public final /* synthetic */ BiConsumer f$1;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda28(Predicate predicate, BiConsumer biConsumer) {
        this.f$0 = predicate;
        this.f$1 = biConsumer;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$filtering$16(this.f$0, this.f$1, obj, obj2);
    }
}
