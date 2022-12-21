package java.util.function;

import java.util.Comparator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BinaryOperator$$ExternalSyntheticLambda0 implements BinaryOperator {
    public final /* synthetic */ Comparator f$0;

    public /* synthetic */ BinaryOperator$$ExternalSyntheticLambda0(Comparator comparator) {
        this.f$0 = comparator;
    }

    public final Object apply(Object obj, Object obj2) {
        return BinaryOperator.lambda$maxBy$1(this.f$0, obj, obj2);
    }
}
