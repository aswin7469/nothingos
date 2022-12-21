package java.util.concurrent;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CopyOnWriteArrayList$COWSubList$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Collection f$0;

    public /* synthetic */ CopyOnWriteArrayList$COWSubList$$ExternalSyntheticLambda0(Collection collection) {
        this.f$0 = collection;
    }

    public final boolean test(Object obj) {
        return CopyOnWriteArrayList.COWSubList.lambda$retainAll$1(this.f$0, obj);
    }
}
