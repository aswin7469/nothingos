package java.util.stream;

import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

/* renamed from: java.util.stream.StreamSpliterators$LongWrappingSpliterator$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4561xa6d810a7 implements Sink.OfLong {
    public final /* synthetic */ SpinedBuffer.OfLong f$0;

    public /* synthetic */ C4561xa6d810a7(SpinedBuffer.OfLong ofLong) {
        this.f$0 = ofLong;
    }

    public final void accept(long j) {
        this.f$0.accept(j);
    }
}
