package java.util.stream;

import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

/* renamed from: java.util.stream.StreamSpliterators$DoubleWrappingSpliterator$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4542xc57d6cf1 implements Sink.OfDouble {
    public final /* synthetic */ SpinedBuffer.OfDouble f$0;

    public /* synthetic */ C4542xc57d6cf1(SpinedBuffer.OfDouble ofDouble) {
        this.f$0 = ofDouble;
    }

    public final void accept(double d) {
        this.f$0.accept(d);
    }
}
