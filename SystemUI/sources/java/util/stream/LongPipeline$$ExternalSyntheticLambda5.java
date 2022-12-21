package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LongPipeline$$ExternalSyntheticLambda5 implements BinaryOperator {
    public final /* synthetic */ BiConsumer f$0;

    public /* synthetic */ LongPipeline$$ExternalSyntheticLambda5(BiConsumer biConsumer) {
        this.f$0 = biConsumer;
    }

    public final Object apply(Object obj, Object obj2) {
        return this.f$0.accept(obj, obj2);
    }
}
