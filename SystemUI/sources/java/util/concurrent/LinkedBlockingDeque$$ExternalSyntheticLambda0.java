package java.util.concurrent;

import java.util.Collection;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LinkedBlockingDeque$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ LinkedBlockingDeque$$ExternalSyntheticLambda0(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return this.f$0.contains(obj);
    }
}
