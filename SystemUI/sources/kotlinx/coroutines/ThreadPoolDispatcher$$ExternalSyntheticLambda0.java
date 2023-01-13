package kotlinx.coroutines;

import java.util.concurrent.ThreadFactory;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ThreadPoolDispatcher$$ExternalSyntheticLambda0 implements ThreadFactory {
    public final /* synthetic */ ThreadPoolDispatcher f$0;

    public /* synthetic */ ThreadPoolDispatcher$$ExternalSyntheticLambda0(ThreadPoolDispatcher threadPoolDispatcher) {
        this.f$0 = threadPoolDispatcher;
    }

    public final Thread newThread(Runnable runnable) {
        return ThreadPoolDispatcher.m5437executor$lambda0(this.f$0, runnable);
    }
}
