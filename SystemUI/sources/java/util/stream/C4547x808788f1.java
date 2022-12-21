package java.util.stream;

import java.util.function.IntConsumer;
import java.util.stream.Sink;

/* renamed from: java.util.stream.StreamSpliterators$IntWrappingSpliterator$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4547x808788f1 implements Sink.OfInt {
    public final /* synthetic */ IntConsumer f$0;

    public /* synthetic */ C4547x808788f1(IntConsumer intConsumer) {
        this.f$0 = intConsumer;
    }

    public final void accept(int i) {
        this.f$0.accept(i);
    }
}
