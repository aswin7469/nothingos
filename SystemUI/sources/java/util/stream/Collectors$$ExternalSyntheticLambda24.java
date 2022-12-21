package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda24 implements BiConsumer {
    public final /* synthetic */ BinaryOperator f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda24(BinaryOperator binaryOperator) {
        this.f$0 = binaryOperator;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$reducing$42(this.f$0, (Object[]) obj, obj2);
    }
}
