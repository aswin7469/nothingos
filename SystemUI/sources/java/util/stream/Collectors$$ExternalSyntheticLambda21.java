package java.util.stream;

import java.util.function.Function;
import java.util.stream.Collectors;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda21 implements Function {
    public final /* synthetic */ Collector f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda21(Collector collector) {
        this.f$0 = collector;
    }

    public final Object apply(Object obj) {
        return Collectors.lambda$partitioningBy$65(this.f$0, (Collectors.Partition) obj);
    }
}
