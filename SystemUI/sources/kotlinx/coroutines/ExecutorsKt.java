package kotlinx.coroutines;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0011\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0007¢\u0006\u0002\b\u0003\u001a\u0011\u0010\u0000\u001a\u00020\u0004*\u00020\u0005H\u0007¢\u0006\u0002\b\u0003\u001a\n\u0010\u0006\u001a\u00020\u0002*\u00020\u0001¨\u0006\u0007"}, mo65043d2 = {"asCoroutineDispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "Ljava/util/concurrent/Executor;", "from", "Lkotlinx/coroutines/ExecutorCoroutineDispatcher;", "Ljava/util/concurrent/ExecutorService;", "asExecutor", "kotlinx-coroutines-core"}, mo65044k = 2, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: Executors.kt */
public final class ExecutorsKt {
    public static final ExecutorCoroutineDispatcher from(ExecutorService executorService) {
        return new ExecutorCoroutineDispatcherImpl(executorService);
    }

    public static final CoroutineDispatcher from(Executor executor) {
        CoroutineDispatcher coroutineDispatcher = null;
        DispatcherExecutor dispatcherExecutor = executor instanceof DispatcherExecutor ? (DispatcherExecutor) executor : null;
        if (dispatcherExecutor != null) {
            coroutineDispatcher = dispatcherExecutor.dispatcher;
        }
        return coroutineDispatcher == null ? new ExecutorCoroutineDispatcherImpl(executor) : coroutineDispatcher;
    }

    public static final Executor asExecutor(CoroutineDispatcher coroutineDispatcher) {
        ExecutorCoroutineDispatcher executorCoroutineDispatcher = coroutineDispatcher instanceof ExecutorCoroutineDispatcher ? (ExecutorCoroutineDispatcher) coroutineDispatcher : null;
        return executorCoroutineDispatcher == null ? new DispatcherExecutor(coroutineDispatcher) : executorCoroutineDispatcher.getExecutor();
    }
}
