package java.util;

import java.util.function.IntConsumer;
import java.util.function.IntToLongFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Arrays$$ExternalSyntheticLambda0 implements IntConsumer {
    public final /* synthetic */ long[] f$0;
    public final /* synthetic */ IntToLongFunction f$1;

    public /* synthetic */ Arrays$$ExternalSyntheticLambda0(long[] jArr, IntToLongFunction intToLongFunction) {
        this.f$0 = jArr;
        this.f$1 = intToLongFunction;
    }

    public final void accept(int i) {
        Arrays.lambda$parallelSetAll$2(this.f$0, this.f$1, i);
    }
}
