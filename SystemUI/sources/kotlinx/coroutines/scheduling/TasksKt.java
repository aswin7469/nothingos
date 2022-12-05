package kotlinx.coroutines.scheduling;

import java.util.concurrent.TimeUnit;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.internal.SystemPropsKt;
import kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: Tasks.kt */
/* loaded from: classes2.dex */
public final class TasksKt {
    public static final int BLOCKING_DEFAULT_PARALLELISM;
    public static final int CORE_POOL_SIZE;
    public static final long IDLE_WORKER_KEEP_ALIVE_NS;
    public static final int MAX_POOL_SIZE;
    public static final int QUEUE_SIZE_OFFLOAD_THRESHOLD;
    public static final long WORK_STEALING_TIME_RESOLUTION_NS;
    @NotNull
    public static TimeSource schedulerTimeSource = NanoTimeSource.INSTANCE;

    static {
        long systemProp$default;
        int systemProp$default2;
        int systemProp$default3;
        int coerceAtLeast;
        int systemProp$default4;
        int coerceIn;
        int systemProp$default5;
        long systemProp$default6;
        systemProp$default = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.resolution.ns", 100000L, 0L, 0L, 12, (Object) null);
        WORK_STEALING_TIME_RESOLUTION_NS = systemProp$default;
        systemProp$default2 = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.offload.threshold", 96, 0, 128, 4, (Object) null);
        QUEUE_SIZE_OFFLOAD_THRESHOLD = systemProp$default2;
        systemProp$default3 = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.blocking.parallelism", 16, 0, 0, 12, (Object) null);
        BLOCKING_DEFAULT_PARALLELISM = systemProp$default3;
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(SystemPropsKt.getAVAILABLE_PROCESSORS(), 2);
        systemProp$default4 = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.core.pool.size", coerceAtLeast, 1, 0, 8, (Object) null);
        CORE_POOL_SIZE = systemProp$default4;
        coerceIn = RangesKt___RangesKt.coerceIn(SystemPropsKt.getAVAILABLE_PROCESSORS() * 128, systemProp$default4, 2097150);
        systemProp$default5 = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.max.pool.size", coerceIn, 0, 2097150, 4, (Object) null);
        MAX_POOL_SIZE = systemProp$default5;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        systemProp$default6 = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.keep.alive.sec", 5L, 0L, 0L, 12, (Object) null);
        IDLE_WORKER_KEEP_ALIVE_NS = timeUnit.toNanos(systemProp$default6);
    }
}
