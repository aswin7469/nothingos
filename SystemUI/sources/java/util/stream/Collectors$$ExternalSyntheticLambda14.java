package java.util.stream;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda14 implements Function {
    public final /* synthetic */ Function f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda14(Function function) {
        this.f$0 = function;
    }

    public final Object apply(Object obj) {
        return ((ConcurrentMap) obj).replaceAll(new Collectors$$ExternalSyntheticLambda34(this.f$0));
    }
}
