package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ReferencePipeline$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ReferencePipeline$$ExternalSyntheticLambda1(BiConsumer biConsumer, Object obj) {
        this.f$0 = biConsumer;
        this.f$1 = obj;
    }

    public final void accept(Object obj) {
        this.f$0.accept(this.f$1, obj);
    }
}
