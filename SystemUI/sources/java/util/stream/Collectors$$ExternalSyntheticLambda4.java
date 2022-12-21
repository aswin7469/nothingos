package java.util.stream;

import java.util.function.Function;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda4 implements Function {
    public final /* synthetic */ Supplier f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda4(Supplier supplier) {
        this.f$0 = supplier;
    }

    public final Object apply(Object obj) {
        return this.f$0.get();
    }
}
