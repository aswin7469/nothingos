package sun.nio.p033ch;

import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.Channel;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import sun.security.action.GetIntegerAction;

/* renamed from: sun.nio.ch.AsynchronousChannelGroupImpl */
abstract class AsynchronousChannelGroupImpl extends AsynchronousChannelGroup implements Executor {
    private static final int internalThreadCount = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.nio.ch.internalThreadPoolSize", 1))).intValue();
    /* access modifiers changed from: private */
    public final ThreadPool pool;
    private final AtomicBoolean shutdown = new AtomicBoolean();
    private final Object shutdownNowLock = new Object();
    private final Queue<Runnable> taskQueue;
    private volatile boolean terminateInitiated;
    private final AtomicInteger threadCount = new AtomicInteger();
    /* access modifiers changed from: private */
    public ScheduledThreadPoolExecutor timeoutExecutor;

    /* access modifiers changed from: package-private */
    public abstract Object attachForeignChannel(Channel channel, FileDescriptor fileDescriptor) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void closeAllChannels() throws IOException;

    /* access modifiers changed from: package-private */
    public abstract void detachForeignChannel(Object obj);

    /* access modifiers changed from: package-private */
    public abstract void executeOnHandlerTask(Runnable runnable);

    /* access modifiers changed from: package-private */
    public abstract boolean isEmpty();

    /* access modifiers changed from: package-private */
    public abstract void shutdownHandlerTasks();

    AsynchronousChannelGroupImpl(AsynchronousChannelProvider asynchronousChannelProvider, ThreadPool threadPool) {
        super(asynchronousChannelProvider);
        this.pool = threadPool;
        if (threadPool.isFixedThreadPool()) {
            this.taskQueue = new ConcurrentLinkedQueue();
        } else {
            this.taskQueue = null;
        }
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, ThreadPool.defaultThreadFactory());
        this.timeoutExecutor = scheduledThreadPoolExecutor;
        scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
    }

    /* access modifiers changed from: package-private */
    public final ExecutorService executor() {
        return this.pool.executor();
    }

    /* access modifiers changed from: package-private */
    public final boolean isFixedThreadPool() {
        return this.pool.isFixedThreadPool();
    }

    /* access modifiers changed from: package-private */
    public final int fixedThreadCount() {
        if (isFixedThreadPool()) {
            return this.pool.poolSize();
        }
        return this.pool.poolSize() + internalThreadCount;
    }

    private Runnable bindToGroup(final Runnable runnable) {
        return new Runnable() {
            public void run() {
                Invoker.bindToGroup(this);
                runnable.run();
            }
        };
    }

    private void startInternalThread(final Runnable runnable) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                ThreadPool.defaultThreadFactory().newThread(runnable).start();
                return null;
            }
        });
    }

    /* access modifiers changed from: protected */
    public final void startThreads(Runnable runnable) {
        int i = 0;
        if (!isFixedThreadPool()) {
            for (int i2 = 0; i2 < internalThreadCount; i2++) {
                startInternalThread(runnable);
                this.threadCount.incrementAndGet();
            }
        }
        if (this.pool.poolSize() > 0) {
            Runnable bindToGroup = bindToGroup(runnable);
            while (i < this.pool.poolSize()) {
                try {
                    this.pool.executor().execute(bindToGroup);
                    this.threadCount.incrementAndGet();
                    i++;
                } catch (RejectedExecutionException unused) {
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final int threadCount() {
        return this.threadCount.get();
    }

    /* access modifiers changed from: package-private */
    public final int threadExit(Runnable runnable, boolean z) {
        if (z) {
            try {
                if (Invoker.isBoundToAnyGroup()) {
                    this.pool.executor().execute(bindToGroup(runnable));
                } else {
                    startInternalThread(runnable);
                }
                return this.threadCount.get();
            } catch (RejectedExecutionException unused) {
            }
        }
        return this.threadCount.decrementAndGet();
    }

    /* access modifiers changed from: package-private */
    public final void executeOnPooledThread(Runnable runnable) {
        if (isFixedThreadPool()) {
            executeOnHandlerTask(runnable);
        } else {
            this.pool.executor().execute(bindToGroup(runnable));
        }
    }

    /* access modifiers changed from: package-private */
    public final void offerTask(Runnable runnable) {
        this.taskQueue.offer(runnable);
    }

    /* access modifiers changed from: package-private */
    public final Runnable pollTask() {
        Queue<Runnable> queue = this.taskQueue;
        if (queue == null) {
            return null;
        }
        return queue.poll();
    }

    /* access modifiers changed from: package-private */
    public final Future<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
        try {
            return this.timeoutExecutor.schedule(runnable, j, timeUnit);
        } catch (RejectedExecutionException e) {
            if (this.terminateInitiated) {
                return null;
            }
            throw new AssertionError((Object) e);
        }
    }

    public final boolean isShutdown() {
        return this.shutdown.get();
    }

    public final boolean isTerminated() {
        return this.pool.executor().isTerminated();
    }

    private void shutdownExecutors() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                AsynchronousChannelGroupImpl.this.pool.executor().shutdown();
                AsynchronousChannelGroupImpl.this.timeoutExecutor.shutdown();
                return null;
            }
        });
    }

    public final void shutdown() {
        if (!this.shutdown.getAndSet(true) && isEmpty()) {
            synchronized (this.shutdownNowLock) {
                if (!this.terminateInitiated) {
                    this.terminateInitiated = true;
                    shutdownHandlerTasks();
                    shutdownExecutors();
                }
            }
        }
    }

    public final void shutdownNow() throws IOException {
        this.shutdown.set(true);
        synchronized (this.shutdownNowLock) {
            if (!this.terminateInitiated) {
                this.terminateInitiated = true;
                closeAllChannels();
                shutdownHandlerTasks();
                shutdownExecutors();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void detachFromThreadPool() {
        if (this.shutdown.getAndSet(true)) {
            throw new AssertionError((Object) "Already shutdown");
        } else if (isEmpty()) {
            shutdownHandlerTasks();
        } else {
            throw new AssertionError((Object) "Group not empty");
        }
    }

    public final boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.pool.executor().awaitTermination(j, timeUnit);
    }

    public final void execute(final Runnable runnable) {
        if (System.getSecurityManager() != null) {
            final AccessControlContext context = AccessController.getContext();
            runnable = new Runnable() {
                public void run() {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() {
                        public Void run() {
                            runnable.run();
                            return null;
                        }
                    }, context);
                }
            };
        }
        executeOnPooledThread(runnable);
    }
}
