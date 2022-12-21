package java.util.stream;

import java.util.function.BinaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda25 implements BinaryOperator {
    public final /* synthetic */ BinaryOperator f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda25(BinaryOperator binaryOperator) {
        this.f$0 = binaryOperator;
    }

    public final Object apply(Object obj, Object obj2) {
        return Collectors.lambda$reducing$43(this.f$0, (Object[]) obj, (Object[]) obj2);
    }
}
