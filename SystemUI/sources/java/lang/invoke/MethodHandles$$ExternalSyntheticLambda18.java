package java.lang.invoke;

import java.util.List;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MethodHandles$$ExternalSyntheticLambda18 implements Predicate {
    public final /* synthetic */ List f$0;

    public /* synthetic */ MethodHandles$$ExternalSyntheticLambda18(List list) {
        this.f$0 = list;
    }

    public final boolean test(Object obj) {
        return MethodHandles.lambda$loopChecks2$16(this.f$0, (MethodType) obj);
    }
}
