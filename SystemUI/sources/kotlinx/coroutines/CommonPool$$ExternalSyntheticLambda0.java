package kotlinx.coroutines;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CommonPool$$ExternalSyntheticLambda0 implements ThreadFactory {
    public final /* synthetic */ AtomicInteger f$0;

    public /* synthetic */ CommonPool$$ExternalSyntheticLambda0(AtomicInteger atomicInteger) {
        this.f$0 = atomicInteger;
    }

    public final Thread newThread(Runnable runnable) {
        return CommonPool.m5413createPlainPool$lambda12(this.f$0, runnable);
    }
}
