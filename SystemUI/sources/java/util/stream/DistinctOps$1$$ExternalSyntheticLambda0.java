package java.util.stream;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.DistinctOps;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DistinctOps$1$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ AtomicBoolean f$0;
    public final /* synthetic */ ConcurrentHashMap f$1;

    public /* synthetic */ DistinctOps$1$$ExternalSyntheticLambda0(AtomicBoolean atomicBoolean, ConcurrentHashMap concurrentHashMap) {
        this.f$0 = atomicBoolean;
        this.f$1 = concurrentHashMap;
    }

    public final void accept(Object obj) {
        DistinctOps.C44401.lambda$opEvaluateParallel$0(this.f$0, this.f$1, obj);
    }
}
