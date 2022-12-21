package java.util;

import java.util.function.IntConsumer;
import java.util.function.IntToDoubleFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Arrays$$ExternalSyntheticLambda1 implements IntConsumer {
    public final /* synthetic */ double[] f$0;
    public final /* synthetic */ IntToDoubleFunction f$1;

    public /* synthetic */ Arrays$$ExternalSyntheticLambda1(double[] dArr, IntToDoubleFunction intToDoubleFunction) {
        this.f$0 = dArr;
        this.f$1 = intToDoubleFunction;
    }

    public final void accept(int i) {
        Arrays.lambda$parallelSetAll$3(this.f$0, this.f$1, i);
    }
}
