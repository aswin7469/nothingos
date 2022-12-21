package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda18 implements BiConsumer {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ Predicate f$1;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda18(BiConsumer biConsumer, Predicate predicate) {
        this.f$0 = biConsumer;
        this.f$1 = predicate;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$partitioningBy$62(this.f$0, this.f$1, (Collectors.Partition) obj, obj2);
    }
}
