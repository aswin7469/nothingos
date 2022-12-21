package java.util.stream;

import java.util.function.Function;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda80 implements Function {
    public final /* synthetic */ Supplier f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda80(Supplier supplier) {
        this.f$0 = supplier;
    }

    public final Object apply(Object obj) {
        return this.f$0.get();
    }
}
