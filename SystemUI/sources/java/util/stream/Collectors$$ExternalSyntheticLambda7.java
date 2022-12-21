package java.util.stream;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda7 implements Supplier {
    public final /* synthetic */ BinaryOperator f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda7(BinaryOperator binaryOperator) {
        this.f$0 = binaryOperator;
    }

    public final Object get() {
        return Collectors.lambda$reducing$46(this.f$0);
    }
}
