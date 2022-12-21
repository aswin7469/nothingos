package java.util;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrimitiveIterator$OfInt$$ExternalSyntheticLambda0 implements IntConsumer {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ PrimitiveIterator$OfInt$$ExternalSyntheticLambda0(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void accept(int i) {
        this.f$0.accept(Integer.valueOf(i));
    }
}
