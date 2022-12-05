package kotlinx.coroutines;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.text.StringsKt__StringNumberConversionsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: CommonPool.kt */
/* loaded from: classes2.dex */
public final class CommonPool extends ExecutorCoroutineDispatcher {
    public static final CommonPool INSTANCE = new CommonPool();
    private static volatile Executor pool;
    private static final int requestedParallelism;
    private static boolean usePrivatePool;

    @Override // kotlinx.coroutines.CoroutineDispatcher
    @NotNull
    public String toString() {
        return "CommonPool";
    }

    static {
        String str;
        int i;
        Integer intOrNull;
        try {
            str = System.getProperty("kotlinx.coroutines.default.parallelism");
        } catch (Throwable unused) {
            str = null;
        }
        if (str != null) {
            intOrNull = StringsKt__StringNumberConversionsKt.toIntOrNull(str);
            if (intOrNull == null || intOrNull.intValue() < 1) {
                throw new IllegalStateException(("Expected positive number in kotlinx.coroutines.default.parallelism, but has " + str).toString());
            }
            i = intOrNull.intValue();
        } else {
            i = -1;
        }
        requestedParallelism = i;
    }

    private CommonPool() {
    }

    private final int getParallelism() {
        int coerceAtLeast;
        Integer valueOf = Integer.valueOf(requestedParallelism);
        if (!(valueOf.intValue() > 0)) {
            valueOf = null;
        }
        if (valueOf != null) {
            return valueOf.intValue();
        }
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
        return coerceAtLeast;
    }

    private final ExecutorService createPool() {
        Class<?> cls;
        ExecutorService executorService;
        if (System.getSecurityManager() != null) {
            return createPlainPool();
        }
        ExecutorService executorService2 = null;
        try {
            cls = Class.forName("java.util.concurrent.ForkJoinPool");
        } catch (Throwable unused) {
            cls = null;
        }
        if (cls == null) {
            return createPlainPool();
        }
        if (!usePrivatePool && requestedParallelism < 0) {
            try {
                Method method = cls.getMethod("commonPool", new Class[0]);
                Object invoke = method != null ? method.invoke(null, new Object[0]) : null;
                if (!(invoke instanceof ExecutorService)) {
                    invoke = null;
                }
                executorService = (ExecutorService) invoke;
            } catch (Throwable unused2) {
                executorService = null;
            }
            if (executorService != null) {
                if (!INSTANCE.isGoodCommonPool$kotlinx_coroutines_core(cls, executorService)) {
                    executorService = null;
                }
                if (executorService != null) {
                    return executorService;
                }
            }
        }
        try {
            Object newInstance = cls.getConstructor(Integer.TYPE).newInstance(Integer.valueOf(INSTANCE.getParallelism()));
            if (!(newInstance instanceof ExecutorService)) {
                newInstance = null;
            }
            executorService2 = (ExecutorService) newInstance;
        } catch (Throwable unused3) {
        }
        return executorService2 != null ? executorService2 : createPlainPool();
    }

    public final boolean isGoodCommonPool$kotlinx_coroutines_core(@NotNull Class<?> fjpClass, @NotNull ExecutorService executor) {
        Intrinsics.checkParameterIsNotNull(fjpClass, "fjpClass");
        Intrinsics.checkParameterIsNotNull(executor, "executor");
        executor.submit(CommonPool$isGoodCommonPool$1.INSTANCE);
        Integer num = null;
        try {
            Object invoke = fjpClass.getMethod("getPoolSize", new Class[0]).invoke(executor, new Object[0]);
            if (!(invoke instanceof Integer)) {
                invoke = null;
            }
            num = (Integer) invoke;
        } catch (Throwable unused) {
        }
        return num != null && num.intValue() >= 1;
    }

    private final ExecutorService createPlainPool() {
        final AtomicInteger atomicInteger = new AtomicInteger();
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(getParallelism(), new ThreadFactory() { // from class: kotlinx.coroutines.CommonPool$createPlainPool$1
            @Override // java.util.concurrent.ThreadFactory
            @NotNull
            public final Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "CommonPool-worker-" + atomicInteger.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(newFixedThreadPool, "Executors.newFixedThread…Daemon = true }\n        }");
        return newFixedThreadPool;
    }

    private final synchronized Executor getOrCreatePoolSync() {
        Executor executor;
        executor = pool;
        if (executor == null) {
            executor = createPool();
            pool = executor;
        }
        return executor;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public void dispatch(@NotNull CoroutineContext context, @NotNull Runnable block) {
        Runnable runnable;
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(block, "block");
        try {
            Executor executor = pool;
            if (executor == null) {
                executor = getOrCreatePoolSync();
            }
            TimeSource timeSource = TimeSourceKt.getTimeSource();
            if (timeSource == null || (runnable = timeSource.wrapTask(block)) == null) {
                runnable = block;
            }
            executor.execute(runnable);
        } catch (RejectedExecutionException unused) {
            TimeSource timeSource2 = TimeSourceKt.getTimeSource();
            if (timeSource2 != null) {
                timeSource2.unTrackTask();
            }
            DefaultExecutor.INSTANCE.enqueue(block);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw new IllegalStateException("Close cannot be invoked on CommonPool".toString());
    }
}
