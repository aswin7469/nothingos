package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.Thread;
import java.lang.ref.Reference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Executors {
    public static ExecutorService newFixedThreadPool(int i) {
        return new ThreadPoolExecutor(i, i, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    public static ExecutorService newWorkStealingPool(int i) {
        return new ForkJoinPool(i, ForkJoinPool.defaultForkJoinWorkerThreadFactory, (Thread.UncaughtExceptionHandler) null, true);
    }

    public static ExecutorService newWorkStealingPool() {
        return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), ForkJoinPool.defaultForkJoinWorkerThreadFactory, (Thread.UncaughtExceptionHandler) null, true);
    }

    public static ExecutorService newFixedThreadPool(int i, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(i, i, 0, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) new LinkedBlockingQueue(), threadFactory);
    }

    public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue()));
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) new LinkedBlockingQueue(), threadFactory));
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, (BlockingQueue<Runnable>) new SynchronousQueue(), threadFactory);
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new DelegatedScheduledExecutorService(new ScheduledThreadPoolExecutor(1, threadFactory));
    }

    public static ScheduledExecutorService newScheduledThreadPool(int i) {
        return new ScheduledThreadPoolExecutor(i);
    }

    public static ScheduledExecutorService newScheduledThreadPool(int i, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(i, threadFactory);
    }

    public static ExecutorService unconfigurableExecutorService(ExecutorService executorService) {
        executorService.getClass();
        return new DelegatedExecutorService(executorService);
    }

    public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.getClass();
        return new DelegatedScheduledExecutorService(scheduledExecutorService);
    }

    public static ThreadFactory defaultThreadFactory() {
        return new DefaultThreadFactory();
    }

    public static ThreadFactory privilegedThreadFactory() {
        return new PrivilegedThreadFactory();
    }

    public static <T> Callable<T> callable(Runnable runnable, T t) {
        runnable.getClass();
        return new RunnableAdapter(runnable, t);
    }

    public static Callable<Object> callable(Runnable runnable) {
        runnable.getClass();
        return new RunnableAdapter(runnable, null);
    }

    public static Callable<Object> callable(final PrivilegedAction<?> privilegedAction) {
        privilegedAction.getClass();
        return new Callable<Object>() {
            public Object call() {
                return PrivilegedAction.this.run();
            }
        };
    }

    public static Callable<Object> callable(final PrivilegedExceptionAction<?> privilegedExceptionAction) {
        privilegedExceptionAction.getClass();
        return new Callable<Object>() {
            public Object call() throws Exception {
                return PrivilegedExceptionAction.this.run();
            }
        };
    }

    public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
        callable.getClass();
        return new PrivilegedCallable(callable);
    }

    public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
        callable.getClass();
        return new PrivilegedCallableUsingCurrentClassLoader(callable);
    }

    private static final class RunnableAdapter<T> implements Callable<T> {
        private final T result;
        private final Runnable task;

        RunnableAdapter(Runnable runnable, T t) {
            this.task = runnable;
            this.result = t;
        }

        public T call() {
            this.task.run();
            return this.result;
        }

        public String toString() {
            return super.toString() + "[Wrapped task = " + this.task + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    private static final class PrivilegedCallable<T> implements Callable<T> {
        final AccessControlContext acc = AccessController.getContext();
        final Callable<T> task;

        PrivilegedCallable(Callable<T> callable) {
            this.task = callable;
        }

        public T call() throws Exception {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<T>() {
                    public T run() throws Exception {
                        return PrivilegedCallable.this.task.call();
                    }
                }, this.acc);
            } catch (PrivilegedActionException e) {
                throw e.getException();
            }
        }

        public String toString() {
            return super.toString() + "[Wrapped task = " + this.task + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    private static final class PrivilegedCallableUsingCurrentClassLoader<T> implements Callable<T> {
        final AccessControlContext acc = AccessController.getContext();
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        final Callable<T> task;

        PrivilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
            this.task = callable;
        }

        public T call() throws Exception {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<T>() {
                    public T run() throws Exception {
                        Thread currentThread = Thread.currentThread();
                        ClassLoader contextClassLoader = currentThread.getContextClassLoader();
                        if (PrivilegedCallableUsingCurrentClassLoader.this.ccl == contextClassLoader) {
                            return PrivilegedCallableUsingCurrentClassLoader.this.task.call();
                        }
                        currentThread.setContextClassLoader(PrivilegedCallableUsingCurrentClassLoader.this.ccl);
                        try {
                            return PrivilegedCallableUsingCurrentClassLoader.this.task.call();
                        } finally {
                            currentThread.setContextClassLoader(contextClassLoader);
                        }
                    }
                }, this.acc);
            } catch (PrivilegedActionException e) {
                throw e.getException();
            }
        }

        public String toString() {
            return super.toString() + "[Wrapped task = " + this.task + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        DefaultThreadFactory() {
            ThreadGroup threadGroup;
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                threadGroup = securityManager.getThreadGroup();
            } else {
                threadGroup = Thread.currentThread().getThreadGroup();
            }
            this.group = threadGroup;
            this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable runnable) {
            ThreadGroup threadGroup = this.group;
            Thread thread = new Thread(threadGroup, runnable, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != 5) {
                thread.setPriority(5);
            }
            return thread;
        }
    }

    private static class PrivilegedThreadFactory extends DefaultThreadFactory {
        final AccessControlContext acc = AccessController.getContext();
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();

        PrivilegedThreadFactory() {
        }

        public Thread newThread(final Runnable runnable) {
            return super.newThread(new Runnable() {
                public void run() {
                    AccessController.doPrivileged(new PrivilegedAction<Object>() {
                        public Void run() {
                            Thread.currentThread().setContextClassLoader(PrivilegedThreadFactory.this.ccl);
                            runnable.run();
                            return null;
                        }
                    }, PrivilegedThreadFactory.this.acc);
                }
            });
        }
    }

    private static class DelegatedExecutorService implements ExecutorService {

        /* renamed from: e */
        private final ExecutorService f751e;

        DelegatedExecutorService(ExecutorService executor) {
            this.f751e = executor;
        }

        public void execute(Runnable command) {
            try {
                this.f751e.execute(command);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public void shutdown() {
            this.f751e.shutdown();
        }

        public List<Runnable> shutdownNow() {
            try {
                return this.f751e.shutdownNow();
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public boolean isShutdown() {
            try {
                return this.f751e.isShutdown();
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public boolean isTerminated() {
            try {
                return this.f751e.isTerminated();
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            try {
                return this.f751e.awaitTermination(timeout, unit);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public Future<?> submit(Runnable task) {
            try {
                return this.f751e.submit(task);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public <T> Future<T> submit(Callable<T> callable) {
            try {
                return this.f751e.submit(callable);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public <T> Future<T> submit(Runnable task, T t) {
            try {
                return this.f751e.submit(task, t);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
            try {
                return this.f751e.invokeAll(collection);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long timeout, TimeUnit unit) throws InterruptedException {
            try {
                return this.f751e.invokeAll(collection, timeout, unit);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
            try {
                return this.f751e.invokeAny(collection);
            } finally {
                Reference.reachabilityFence(this);
            }
        }

        public <T> T invokeAny(Collection<? extends Callable<T>> collection, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            try {
                return this.f751e.invokeAny(collection, timeout, unit);
            } finally {
                Reference.reachabilityFence(this);
            }
        }
    }

    private static class FinalizableDelegatedExecutorService extends DelegatedExecutorService {
        FinalizableDelegatedExecutorService(ExecutorService executorService) {
            super(executorService);
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            super.shutdown();
        }
    }

    private static class DelegatedScheduledExecutorService extends DelegatedExecutorService implements ScheduledExecutorService {

        /* renamed from: e */
        private final ScheduledExecutorService f752e;

        DelegatedScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
            super(scheduledExecutorService);
            this.f752e = scheduledExecutorService;
        }

        public ScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            return this.f752e.schedule(runnable, j, timeUnit);
        }

        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long j, TimeUnit timeUnit) {
            return this.f752e.schedule(callable, j, timeUnit);
        }

        public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            return this.f752e.scheduleAtFixedRate(runnable, j, j2, timeUnit);
        }

        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            return this.f752e.scheduleWithFixedDelay(runnable, j, j2, timeUnit);
        }
    }

    private Executors() {
    }
}
