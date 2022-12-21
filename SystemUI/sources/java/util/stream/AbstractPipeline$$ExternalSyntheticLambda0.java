package java.util.stream;

import java.util.Spliterator;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AbstractPipeline$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ Spliterator f$0;

    public /* synthetic */ AbstractPipeline$$ExternalSyntheticLambda0(Spliterator spliterator) {
        this.f$0 = spliterator;
    }

    public final Object get() {
        return AbstractPipeline.lambda$wrapSpliterator$1(this.f$0);
    }
}
