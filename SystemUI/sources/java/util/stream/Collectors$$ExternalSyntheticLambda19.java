package java.util.stream;

import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda19 implements BinaryOperator {
    public final /* synthetic */ BinaryOperator f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda19(BinaryOperator binaryOperator) {
        this.f$0 = binaryOperator;
    }

    public final Object apply(Object obj, Object obj2) {
        return Collectors.lambda$partitioningBy$63(this.f$0, (Collectors.Partition) obj, (Collectors.Partition) obj2);
    }
}
