package java.util;

import java.util.function.Consumer;
import java.util.function.LongConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrimitiveIterator$OfLong$$ExternalSyntheticLambda0 implements LongConsumer {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ PrimitiveIterator$OfLong$$ExternalSyntheticLambda0(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void accept(long j) {
        this.f$0.accept(Long.valueOf(j));
    }
}
