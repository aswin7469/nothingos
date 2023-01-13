package kotlinx.coroutines;

import java.util.concurrent.Executor;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo65043d2 = {"Lkotlinx/coroutines/ExecutorCoroutineDispatcherImpl;", "Lkotlinx/coroutines/ExecutorCoroutineDispatcherBase;", "executor", "Ljava/util/concurrent/Executor;", "(Ljava/util/concurrent/Executor;)V", "getExecutor", "()Ljava/util/concurrent/Executor;", "kotlinx-coroutines-core"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: Executors.kt */
final class ExecutorCoroutineDispatcherImpl extends ExecutorCoroutineDispatcherBase {
    private final Executor executor;

    public ExecutorCoroutineDispatcherImpl(Executor executor2) {
        this.executor = executor2;
        initFutureCancellation$kotlinx_coroutines_core();
    }

    public Executor getExecutor() {
        return this.executor;
    }
}
