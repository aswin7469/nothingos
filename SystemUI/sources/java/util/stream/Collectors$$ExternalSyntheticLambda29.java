package java.util.stream;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda29 implements BiConsumer {
    public final /* synthetic */ BinaryOperator f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda29(BinaryOperator binaryOperator, Function function) {
        this.f$0 = binaryOperator;
        this.f$1 = function;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$reducing$49(this.f$0, this.f$1, (Object[]) obj, obj2);
    }
}
