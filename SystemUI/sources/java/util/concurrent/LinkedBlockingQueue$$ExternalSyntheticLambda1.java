package java.util.concurrent;

import java.util.Collection;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LinkedBlockingQueue$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ LinkedBlockingQueue$$ExternalSyntheticLambda1(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return LinkedBlockingQueue.lambda$retainAll$1(this.f$0, obj);
    }
}