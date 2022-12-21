package java.util;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Arrays$$ExternalSyntheticLambda3 implements IntConsumer {
    public final /* synthetic */ Object[] f$0;
    public final /* synthetic */ IntFunction f$1;

    public /* synthetic */ Arrays$$ExternalSyntheticLambda3(Object[] objArr, IntFunction intFunction) {
        this.f$0 = objArr;
        this.f$1 = intFunction;
    }

    public final void accept(int i) {
        Arrays.lambda$parallelSetAll$0(this.f$0, this.f$1, i);
    }
}
