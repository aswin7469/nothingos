package java.nio.channels;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Selector$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ Selector f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ Selector$$ExternalSyntheticLambda0(Selector selector, Consumer consumer) {
        this.f$0 = selector;
        this.f$1 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.m3724lambda$doSelect$0$javaniochannelsSelector(this.f$1, (SelectionKey) obj);
    }
}
