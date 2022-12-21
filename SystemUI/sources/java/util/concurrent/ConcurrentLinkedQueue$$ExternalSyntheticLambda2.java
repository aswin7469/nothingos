package java.util.concurrent;

import java.util.Collection;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ConcurrentLinkedQueue$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ ConcurrentLinkedQueue$$ExternalSyntheticLambda2(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return this.f$0.contains(obj);
    }
}
