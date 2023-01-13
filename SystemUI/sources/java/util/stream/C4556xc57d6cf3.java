package java.util.stream;

import java.util.function.DoubleConsumer;
import java.util.stream.Sink;

/* renamed from: java.util.stream.StreamSpliterators$DoubleWrappingSpliterator$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4556xc57d6cf3 implements Sink.OfDouble {
    public final /* synthetic */ DoubleConsumer f$0;

    public /* synthetic */ C4556xc57d6cf3(DoubleConsumer doubleConsumer) {
        this.f$0 = doubleConsumer;
    }

    public final void accept(double d) {
        this.f$0.accept(d);
    }
}
