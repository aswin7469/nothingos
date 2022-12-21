package sun.nio.p033ch;

import java.security.AccessController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import sun.security.action.GetPropertyAction;

/* renamed from: sun.nio.ch.ThreadPool */
public class ThreadPool {
    private static final String DEFAULT_THREAD_POOL_INITIAL_SIZE = "java.nio.channels.DefaultThreadPool.initialSize";
    private static final String DEFAULT_THREAD_POOL_THREAD_FACTORY = "java.nio.channels.DefaultThreadPool.threadFactory";
    private final ExecutorService executor;
    private final boolean isFixed;
    private final int poolSize;

    private ThreadPool(ExecutorService executorService, boolean z, int i) {
        this.executor = executorService;
        this.isFixed = z;
        this.poolSize = i;
    }

    /* access modifiers changed from: package-private */
    public ExecutorService executor() {
        return this.executor;
    }

    /* access modifiers changed from: package-private */
    public boolean isFixedThreadPool() {
        return this.isFixed;
    }

    /* access modifiers changed from: package-private */
    public int poolSize() {
        return this.poolSize;
    }

    static ThreadFactory defaultThreadFactory() {
        return new ThreadPool$$ExternalSyntheticLambda0();
    }

    static /* synthetic */ Thread lambda$defaultThreadFactory$0(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }

    /* renamed from: sun.nio.ch.ThreadPool$DefaultThreadPoolHolder */
    private static class DefaultThreadPoolHolder {
        static final ThreadPool defaultThreadPool = ThreadPool.createDefault();

        private DefaultThreadPoolHolder() {
        }
    }

    static ThreadPool getDefault() {
        return DefaultThreadPoolHolder.defaultThreadPool;
    }

    static ThreadPool createDefault() {
        int defaultThreadPoolInitialSize = getDefaultThreadPoolInitialSize();
        if (defaultThreadPoolInitialSize < 0) {
            defaultThreadPoolInitialSize = Runtime.getRuntime().availableProcessors();
        }
        ThreadFactory defaultThreadPoolThreadFactory = getDefaultThreadPoolThreadFactory();
        if (defaultThreadPoolThreadFactory == null) {
            defaultThreadPoolThreadFactory = defaultThreadFactory();
        }
        return new ThreadPool(Executors.newCachedThreadPool(defaultThreadPoolThreadFactory), false, defaultThreadPoolInitialSize);
    }

    static ThreadPool create(int i, ThreadFactory threadFactory) {
        if (i > 0) {
            return new ThreadPool(Executors.newFixedThreadPool(i, threadFactory), true, i);
        }
        throw new IllegalArgumentException("'nThreads' must be > 0");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001e, code lost:
        if (r4 < 0) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static sun.nio.p033ch.ThreadPool wrap(java.util.concurrent.ExecutorService r3, int r4) {
        /*
            if (r3 == 0) goto L_0x0027
            boolean r0 = r3 instanceof java.util.concurrent.ThreadPoolExecutor
            r1 = 0
            if (r0 == 0) goto L_0x001e
            r0 = r3
            java.util.concurrent.ThreadPoolExecutor r0 = (java.util.concurrent.ThreadPoolExecutor) r0
            int r0 = r0.getMaximumPoolSize()
            r2 = 2147483647(0x7fffffff, float:NaN)
            if (r0 != r2) goto L_0x0021
            if (r4 >= 0) goto L_0x0020
            java.lang.Runtime r4 = java.lang.Runtime.getRuntime()
            int r4 = r4.availableProcessors()
            goto L_0x0021
        L_0x001e:
            if (r4 >= 0) goto L_0x0021
        L_0x0020:
            r4 = r1
        L_0x0021:
            sun.nio.ch.ThreadPool r0 = new sun.nio.ch.ThreadPool
            r0.<init>(r3, r1, r4)
            return r0
        L_0x0027:
            java.lang.NullPointerException r3 = new java.lang.NullPointerException
            java.lang.String r4 = "'executor' is null"
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.ThreadPool.wrap(java.util.concurrent.ExecutorService, int):sun.nio.ch.ThreadPool");
    }

    private static int getDefaultThreadPoolInitialSize() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(DEFAULT_THREAD_POOL_INITIAL_SIZE));
        if (str == null) {
            return -1;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new Error("Value of property 'java.nio.channels.DefaultThreadPool.initialSize' is invalid: " + e);
        }
    }

    private static ThreadFactory getDefaultThreadPoolThreadFactory() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(DEFAULT_THREAD_POOL_THREAD_FACTORY));
        if (str == null) {
            return null;
        }
        try {
            return (ThreadFactory) Class.forName(str, true, ClassLoader.getSystemClassLoader()).newInstance();
        } catch (ClassNotFoundException e) {
            throw new Error((Throwable) e);
        } catch (InstantiationException e2) {
            throw new Error((Throwable) e2);
        } catch (IllegalAccessException e3) {
            throw new Error((Throwable) e3);
        }
    }
}
