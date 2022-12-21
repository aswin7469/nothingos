package java.util.stream;

import java.util.Map;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda52 implements Function {
    public final /* synthetic */ Function f$0;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda52(Function function) {
        this.f$0 = function;
    }

    public final Object apply(Object obj) {
        return ((Map) obj).replaceAll(new Collectors$$ExternalSyntheticLambda92(this.f$0));
    }
}
