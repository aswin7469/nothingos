package java.util;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrimitiveIterator$OfDouble$$ExternalSyntheticLambda0 implements DoubleConsumer {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ PrimitiveIterator$OfDouble$$ExternalSyntheticLambda0(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void accept(double d) {
        this.f$0.accept(Double.valueOf(d));
    }
}
