package java.util.stream;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda32 implements BiConsumer {
    public final /* synthetic */ Function f$0;
    public final /* synthetic */ Function f$1;

    public /* synthetic */ Collectors$$ExternalSyntheticLambda32(Function function, Function function2) {
        this.f$0 = function;
        this.f$1 = function2;
    }

    public final void accept(Object obj, Object obj2) {
        Collectors.lambda$uniqKeysMapAccumulator$1(this.f$0, this.f$1, (Map) obj, obj2);
    }
}
