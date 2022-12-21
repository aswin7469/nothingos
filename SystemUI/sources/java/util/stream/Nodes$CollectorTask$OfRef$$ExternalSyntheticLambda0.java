package java.util.stream;

import java.util.function.IntFunction;
import java.util.function.LongFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Nodes$CollectorTask$OfRef$$ExternalSyntheticLambda0 implements LongFunction {
    public final /* synthetic */ IntFunction f$0;

    public /* synthetic */ Nodes$CollectorTask$OfRef$$ExternalSyntheticLambda0(IntFunction intFunction) {
        this.f$0 = intFunction;
    }

    public final Object apply(long j) {
        return Nodes.builder(j, this.f$0);
    }
}
