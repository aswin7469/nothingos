package java.util.stream;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StreamSpliterators$WrappingSpliterator$$ExternalSyntheticLambda2 implements Sink {
    public final /* synthetic */ Consumer f$0;

    public /* synthetic */ StreamSpliterators$WrappingSpliterator$$ExternalSyntheticLambda2(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.accept(obj);
    }
}
