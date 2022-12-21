package java.util;

import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Arrays$$ExternalSyntheticLambda2 implements IntConsumer {
    public final /* synthetic */ int[] f$0;
    public final /* synthetic */ IntUnaryOperator f$1;

    public /* synthetic */ Arrays$$ExternalSyntheticLambda2(int[] iArr, IntUnaryOperator intUnaryOperator) {
        this.f$0 = iArr;
        this.f$1 = intUnaryOperator;
    }

    public final void accept(int i) {
        Arrays.lambda$parallelSetAll$1(this.f$0, this.f$1, i);
    }
}
