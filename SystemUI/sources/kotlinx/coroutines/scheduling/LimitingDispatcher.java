package kotlinx.coroutines.scheduling;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Dispatcher.kt */
/* loaded from: classes2.dex */
public final class LimitingDispatcher extends ExecutorCoroutineDispatcher implements TaskContext, Executor {
    private static final AtomicIntegerFieldUpdater inFlightTasks$FU = AtomicIntegerFieldUpdater.newUpdater(LimitingDispatcher.class, "inFlightTasks");
    @NotNull
    private final ExperimentalCoroutineDispatcher dispatcher;
    private final int parallelism;
    @NotNull
    private final TaskMode taskMode;
    private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
    private volatile int inFlightTasks = 0;

    @Override // kotlinx.coroutines.scheduling.TaskContext
    @NotNull
    public TaskMode getTaskMode() {
        return this.taskMode;
    }

    public LimitingDispatcher(@NotNull ExperimentalCoroutineDispatcher dispatcher, int i, @NotNull TaskMode taskMode) {
        Intrinsics.checkParameterIsNotNull(dispatcher, "dispatcher");
        Intrinsics.checkParameterIsNotNull(taskMode, "taskMode");
        this.dispatcher = dispatcher;
        this.parallelism = i;
        this.taskMode = taskMode;
    }

    @Override // java.util.concurrent.Executor
    public void execute(@NotNull Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        dispatch(command, false);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Close cannot be invoked on LimitingBlockingDispatcher".toString());
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(@NotNull CoroutineContext context, @NotNull Runnable block) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        dispatch(block, false);
    }

    private final void dispatch(Runnable runnable, boolean z) {
        do {
            AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = inFlightTasks$FU;
            if (atomicIntegerFieldUpdater.incrementAndGet(this) <= this.parallelism) {
                this.dispatcher.dispatchWithContext$kotlinx_coroutines_core(runnable, this, z);
                return;
            }
            this.queue.add(runnable);
            if (atomicIntegerFieldUpdater.decrementAndGet(this) >= this.parallelism) {
                return;
            }
            runnable = this.queue.poll();
        } while (runnable != null);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    @NotNull
    public String toString() {
        return super.toString() + "[dispatcher = " + this.dispatcher + ']';
    }

    @Override // kotlinx.coroutines.scheduling.TaskContext
    public void afterTask() {
        Runnable poll = this.queue.poll();
        if (poll != null) {
            this.dispatcher.dispatchWithContext$kotlinx_coroutines_core(poll, this, true);
            return;
        }
        inFlightTasks$FU.decrementAndGet(this);
        Runnable poll2 = this.queue.poll();
        if (poll2 == null) {
            return;
        }
        dispatch(poll2, true);
    }
}
