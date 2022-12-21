package java.util.stream;

import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

/* renamed from: java.util.stream.StreamSpliterators$IntWrappingSpliterator$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C4545x808788ef implements Sink.OfInt {
    public final /* synthetic */ SpinedBuffer.OfInt f$0;

    public /* synthetic */ C4545x808788ef(SpinedBuffer.OfInt ofInt) {
        this.f$0 = ofInt;
    }

    public final void accept(int i) {
        this.f$0.accept(i);
    }
}
