package java.util.function;

import java.util.Comparator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BinaryOperator$$ExternalSyntheticLambda1 implements BinaryOperator {
    public final /* synthetic */ Comparator f$0;

    public /* synthetic */ BinaryOperator$$ExternalSyntheticLambda1(Comparator comparator) {
        this.f$0 = comparator;
    }

    public final Object apply(Object obj, Object obj2) {
        return BinaryOperator.lambda$minBy$0(this.f$0, obj, obj2);
    }
}
