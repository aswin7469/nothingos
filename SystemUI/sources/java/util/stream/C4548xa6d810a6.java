package java.util.stream;

import java.util.function.LongConsumer;
import java.util.stream.Sink;

/* renamed from: java.util.stream.StreamSpliterators$LongWrappingSpliterator$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4548xa6d810a6 implements Sink.OfLong {
    public final /* synthetic */ LongConsumer f$0;

    public /* synthetic */ C4548xa6d810a6(LongConsumer longConsumer) {
        this.f$0 = longConsumer;
    }

    public final void accept(long j) {
        this.f$0.accept(j);
    }
}
