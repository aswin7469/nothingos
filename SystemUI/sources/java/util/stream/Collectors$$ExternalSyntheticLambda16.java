package java.util.stream;

import java.util.Map;
import java.util.function.BinaryOperator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda16 implements BinaryOperator {
    public final /* synthetic */ BinaryOperator f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda16(BinaryOperator binaryOperator) {
        this.f$0 = binaryOperator;
    }

    public final Object apply(Object obj, Object obj2) {
        return Collectors.lambda$mapMerger$12(this.f$0, (Map) obj, (Map) obj2);
    }
}
