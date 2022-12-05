package kotlinx.coroutines.scheduling;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.TimeSourceKt;
import kotlinx.coroutines.internal.Symbol;
import kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CoroutineScheduler.kt */
/* loaded from: classes2.dex */
public final class CoroutineScheduler implements Executor, Closeable {
    private static final int MAX_PARK_TIME_NS;
    private static final int MAX_SPINS;
    private static final int MAX_YIELDS;
    private static final int MIN_PARK_TIME_NS;
    private volatile int _isTerminated;
    volatile long controlState;
    private final int corePoolSize;
    private final Semaphore cpuPermits;
    private final GlobalQueue globalQueue;
    private final long idleWorkerKeepAliveNs;
    private final int maxPoolSize;
    private volatile long parkedWorkersStack;
    private final Random random;
    private final String schedulerName;
    private final Worker[] workers;
    public static final Companion Companion = new Companion(null);
    private static final Symbol NOT_IN_STACK = new Symbol("NOT_IN_STACK");
    private static final AtomicLongFieldUpdater parkedWorkersStack$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "parkedWorkersStack");
    static final AtomicLongFieldUpdater controlState$FU = AtomicLongFieldUpdater.newUpdater(CoroutineScheduler.class, "controlState");
    private static final AtomicIntegerFieldUpdater _isTerminated$FU = AtomicIntegerFieldUpdater.newUpdater(CoroutineScheduler.class, "_isTerminated");

    /* loaded from: classes2.dex */
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[WorkerState.values().length];
            $EnumSwitchMapping$0 = iArr;
            iArr[WorkerState.PARKING.ordinal()] = 1;
            iArr[WorkerState.BLOCKING.ordinal()] = 2;
            iArr[WorkerState.CPU_ACQUIRED.ordinal()] = 3;
            iArr[WorkerState.RETIRING.ordinal()] = 4;
            iArr[WorkerState.TERMINATED.ordinal()] = 5;
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    /* loaded from: classes2.dex */
    public enum WorkerState {
        CPU_ACQUIRED,
        BLOCKING,
        PARKING,
        RETIRING,
        TERMINATED
    }

    public CoroutineScheduler(int i, int i2, long j, @NotNull String schedulerName) {
        Intrinsics.checkParameterIsNotNull(schedulerName, "schedulerName");
        this.corePoolSize = i;
        this.maxPoolSize = i2;
        this.idleWorkerKeepAliveNs = j;
        this.schedulerName = schedulerName;
        if (!(i >= 1)) {
            throw new IllegalArgumentException(("Core pool size " + i + " should be at least 1").toString());
        }
        if (!(i2 >= i)) {
            throw new IllegalArgumentException(("Max pool size " + i2 + " should be greater than or equals to core pool size " + i).toString());
        }
        if (!(i2 <= 2097150)) {
            throw new IllegalArgumentException(("Max pool size " + i2 + " should not exceed maximal supported number of threads 2097150").toString());
        }
        if (!(j > 0)) {
            throw new IllegalArgumentException(("Idle worker keep alive time " + j + " must be positive").toString());
        }
        this.globalQueue = new GlobalQueue();
        this.cpuPermits = new Semaphore(i, false);
        this.parkedWorkersStack = 0L;
        this.workers = new Worker[i2 + 1];
        this.controlState = 0L;
        this.random = new Random();
        this._isTerminated = 0;
    }

    public final void parkedWorkersStackPush(Worker worker) {
        long j;
        long j2;
        int indexInArray;
        if (worker.getNextParkedWorker() != NOT_IN_STACK) {
            return;
        }
        do {
            j = this.parkedWorkersStack;
            int i = (int) (2097151 & j);
            j2 = (2097152 + j) & (-2097152);
            indexInArray = worker.getIndexInArray();
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(indexInArray != 0)) {
                    throw new AssertionError();
                }
            }
            worker.setNextParkedWorker(this.workers[i]);
        } while (!parkedWorkersStack$FU.compareAndSet(this, j, indexInArray | j2));
    }

    private final int parkedWorkersStackNextIndex(Worker worker) {
        Object nextParkedWorker = worker.getNextParkedWorker();
        while (nextParkedWorker != NOT_IN_STACK) {
            if (nextParkedWorker == null) {
                return 0;
            }
            Worker worker2 = (Worker) nextParkedWorker;
            int indexInArray = worker2.getIndexInArray();
            if (indexInArray != 0) {
                return indexInArray;
            }
            nextParkedWorker = worker2.getNextParkedWorker();
        }
        return -1;
    }

    public final int getCreatedWorkers() {
        return (int) (this.controlState & 2097151);
    }

    public final boolean isTerminated() {
        return this._isTerminated != 0;
    }

    /* compiled from: CoroutineScheduler.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    static {
        int systemProp$default;
        int systemProp$default2;
        long coerceAtLeast;
        long coerceAtMost;
        systemProp$default = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.spins", 1000, 1, 0, 8, (Object) null);
        MAX_SPINS = systemProp$default;
        systemProp$default2 = SystemPropsKt__SystemProps_commonKt.systemProp$default("kotlinx.coroutines.scheduler.yields", 0, 0, 0, 8, (Object) null);
        MAX_YIELDS = systemProp$default + systemProp$default2;
        int nanos = (int) TimeUnit.SECONDS.toNanos(1L);
        MAX_PARK_TIME_NS = nanos;
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(TasksKt.WORK_STEALING_TIME_RESOLUTION_NS / 4, 10L);
        coerceAtMost = RangesKt___RangesKt.coerceAtMost(coerceAtLeast, nanos);
        MIN_PARK_TIME_NS = (int) coerceAtMost;
    }

    @Override // java.util.concurrent.Executor
    public void execute(@NotNull Runnable command) {
        Intrinsics.checkParameterIsNotNull(command, "command");
        dispatch$default(this, command, null, false, 6, null);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        shutdown(10000L);
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0068, code lost:
        if (r9 != null) goto L39;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void shutdown(long j) {
        int i;
        Task removeFirstOrNull;
        boolean z = false;
        if (!_isTerminated$FU.compareAndSet(this, 0, 1)) {
            return;
        }
        Worker currentWorker = currentWorker();
        synchronized (this.workers) {
            i = (int) (this.controlState & 2097151);
        }
        if (1 <= i) {
            int i2 = 1;
            while (true) {
                Worker worker = this.workers[i2];
                if (worker == null) {
                    Intrinsics.throwNpe();
                }
                if (worker != currentWorker) {
                    while (worker.isAlive()) {
                        LockSupport.unpark(worker);
                        worker.join(j);
                    }
                    WorkerState state = worker.getState();
                    if (DebugKt.getASSERTIONS_ENABLED()) {
                        if (!(state == WorkerState.TERMINATED)) {
                            throw new AssertionError();
                        }
                    }
                    worker.getLocalQueue().offloadAllWork$kotlinx_coroutines_core(this.globalQueue);
                }
                if (i2 == i) {
                    break;
                }
                i2++;
            }
        }
        this.globalQueue.close();
        while (true) {
            if (currentWorker != null) {
                removeFirstOrNull = currentWorker.findTask$kotlinx_coroutines_core();
            }
            removeFirstOrNull = this.globalQueue.removeFirstOrNull();
            if (removeFirstOrNull == null) {
                break;
            }
            runSafely(removeFirstOrNull);
        }
        if (currentWorker != null) {
            currentWorker.tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
        }
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (this.cpuPermits.availablePermits() == this.corePoolSize) {
                z = true;
            }
            if (!z) {
                throw new AssertionError();
            }
        }
        this.parkedWorkersStack = 0L;
        this.controlState = 0L;
    }

    public static /* synthetic */ void dispatch$default(CoroutineScheduler coroutineScheduler, Runnable runnable, TaskContext taskContext, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            taskContext = NonBlockingContext.INSTANCE;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        coroutineScheduler.dispatch(runnable, taskContext, z);
    }

    public final void dispatch(@NotNull Runnable block, @NotNull TaskContext taskContext, boolean z) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        kotlinx.coroutines.TimeSource timeSource = TimeSourceKt.getTimeSource();
        if (timeSource != null) {
            timeSource.trackTask();
        }
        Task createTask$kotlinx_coroutines_core = createTask$kotlinx_coroutines_core(block, taskContext);
        int submitToLocalQueue = submitToLocalQueue(createTask$kotlinx_coroutines_core, z);
        if (submitToLocalQueue != -1) {
            if (submitToLocalQueue == 1) {
                if (!this.globalQueue.addLast(createTask$kotlinx_coroutines_core)) {
                    throw new RejectedExecutionException(this.schedulerName + " was terminated");
                }
                requestCpuWorker();
                return;
            }
            requestCpuWorker();
        }
    }

    @NotNull
    public final Task createTask$kotlinx_coroutines_core(@NotNull Runnable block, @NotNull TaskContext taskContext) {
        Intrinsics.checkParameterIsNotNull(block, "block");
        Intrinsics.checkParameterIsNotNull(taskContext, "taskContext");
        long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
        if (block instanceof Task) {
            Task task = (Task) block;
            task.submissionTime = nanoTime;
            task.taskContext = taskContext;
            return task;
        }
        return new TaskImpl(block, nanoTime, taskContext);
    }

    public final void requestCpuWorker() {
        if (this.cpuPermits.availablePermits() == 0) {
            tryUnpark();
        } else if (tryUnpark()) {
        } else {
            long j = this.controlState;
            if (((int) (2097151 & j)) - ((int) ((j & 4398044413952L) >> 21)) < this.corePoolSize) {
                int createNewWorker = createNewWorker();
                if (createNewWorker == 1 && this.corePoolSize > 1) {
                    createNewWorker();
                }
                if (createNewWorker > 0) {
                    return;
                }
            }
            tryUnpark();
        }
    }

    private final boolean tryUnpark() {
        while (true) {
            Worker parkedWorkersStackPop = parkedWorkersStackPop();
            if (parkedWorkersStackPop != null) {
                parkedWorkersStackPop.idleResetBeforeUnpark();
                boolean isParking = parkedWorkersStackPop.isParking();
                LockSupport.unpark(parkedWorkersStackPop);
                if (isParking && parkedWorkersStackPop.tryForbidTermination()) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private final int createNewWorker() {
        synchronized (this.workers) {
            if (isTerminated()) {
                return -1;
            }
            long j = this.controlState;
            int i = (int) (j & 2097151);
            int i2 = i - ((int) ((j & 4398044413952L) >> 21));
            boolean z = false;
            if (i2 >= this.corePoolSize) {
                return 0;
            }
            if (i < this.maxPoolSize && this.cpuPermits.availablePermits() != 0) {
                int i3 = ((int) (this.controlState & 2097151)) + 1;
                if (!(i3 > 0 && this.workers[i3] == null)) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                Worker worker = new Worker(this, i3);
                worker.start();
                if (i3 == ((int) (2097151 & controlState$FU.incrementAndGet(this)))) {
                    z = true;
                }
                if (!z) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                this.workers[i3] = worker;
                return i2 + 1;
            }
            return 0;
        }
    }

    private final int submitToLocalQueue(Task task, boolean z) {
        boolean add;
        Worker currentWorker = currentWorker();
        if (currentWorker == null || currentWorker.getState() == WorkerState.TERMINATED) {
            return 1;
        }
        int i = -1;
        if (task.getMode() == TaskMode.NON_BLOCKING) {
            if (currentWorker.isBlocking()) {
                i = 0;
            } else if (!currentWorker.tryAcquireCpuPermit()) {
                return 1;
            }
        }
        if (z) {
            add = currentWorker.getLocalQueue().addLast(task, this.globalQueue);
        } else {
            add = currentWorker.getLocalQueue().add(task, this.globalQueue);
        }
        if (add && currentWorker.getLocalQueue().getBufferSize$kotlinx_coroutines_core() <= TasksKt.QUEUE_SIZE_OFFLOAD_THRESHOLD) {
            return i;
        }
        return 0;
    }

    private final Worker currentWorker() {
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof Worker)) {
            currentThread = null;
        }
        Worker worker = (Worker) currentThread;
        if (worker == null || !Intrinsics.areEqual(worker.getScheduler(), this)) {
            return null;
        }
        return worker;
    }

    @NotNull
    public String toString() {
        Worker[] workerArr;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        for (Worker worker : this.workers) {
            if (worker != null) {
                int size$kotlinx_coroutines_core = worker.getLocalQueue().size$kotlinx_coroutines_core();
                int i6 = WhenMappings.$EnumSwitchMapping$0[worker.getState().ordinal()];
                if (i6 == 1) {
                    i3++;
                } else if (i6 == 2) {
                    i2++;
                    arrayList.add(String.valueOf(size$kotlinx_coroutines_core) + "b");
                } else if (i6 == 3) {
                    i++;
                    arrayList.add(String.valueOf(size$kotlinx_coroutines_core) + "c");
                } else if (i6 == 4) {
                    i4++;
                    if (size$kotlinx_coroutines_core > 0) {
                        arrayList.add(String.valueOf(size$kotlinx_coroutines_core) + "r");
                    }
                } else if (i6 == 5) {
                    i5++;
                }
            }
        }
        long j = this.controlState;
        return this.schedulerName + '@' + DebugStringsKt.getHexAddress(this) + "[Pool Size {core = " + this.corePoolSize + ", max = " + this.maxPoolSize + "}, Worker States {CPU = " + i + ", blocking = " + i2 + ", parked = " + i3 + ", retired = " + i4 + ", terminated = " + i5 + "}, running workers queues = " + arrayList + ", global queue size = " + this.globalQueue.getSize() + ", Control State Workers {created = " + ((int) (2097151 & j)) + ", blocking = " + ((int) ((j & 4398044413952L) >> 21)) + "}]";
    }

    public final void runSafely(Task task) {
        try {
            task.run();
        } catch (Throwable th) {
            try {
                Thread thread = Thread.currentThread();
                Intrinsics.checkExpressionValueIsNotNull(thread, "thread");
                thread.getUncaughtExceptionHandler().uncaughtException(thread, th);
                kotlinx.coroutines.TimeSource timeSource = TimeSourceKt.getTimeSource();
                if (timeSource == null) {
                }
            } finally {
                kotlinx.coroutines.TimeSource timeSource2 = TimeSourceKt.getTimeSource();
                if (timeSource2 != null) {
                    timeSource2.unTrackTask();
                }
            }
        }
    }

    /* compiled from: CoroutineScheduler.kt */
    /* loaded from: classes2.dex */
    public final class Worker extends Thread {
        private static final AtomicIntegerFieldUpdater terminationState$FU = AtomicIntegerFieldUpdater.newUpdater(Worker.class, "terminationState");
        private volatile int indexInArray;
        private long lastExhaustionTime;
        private int lastStealIndex;
        @NotNull
        private final WorkQueue localQueue;
        @Nullable
        private volatile Object nextParkedWorker;
        private int parkTimeNs;
        private int rngState;
        private volatile int spins;
        @NotNull
        private volatile WorkerState state;
        private long terminationDeadline;
        private volatile int terminationState;

        private Worker() {
            CoroutineScheduler.this = r2;
            setDaemon(true);
            this.localQueue = new WorkQueue();
            this.state = WorkerState.RETIRING;
            this.terminationState = 0;
            this.nextParkedWorker = CoroutineScheduler.NOT_IN_STACK;
            this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            this.rngState = r2.random.nextInt();
        }

        public final int getIndexInArray() {
            return this.indexInArray;
        }

        public final void setIndexInArray(int i) {
            StringBuilder sb = new StringBuilder();
            sb.append(CoroutineScheduler.this.schedulerName);
            sb.append("-worker-");
            sb.append(i == 0 ? "TERMINATED" : String.valueOf(i));
            setName(sb.toString());
            this.indexInArray = i;
        }

        public Worker(CoroutineScheduler coroutineScheduler, int i) {
            this();
            setIndexInArray(i);
        }

        @NotNull
        public final CoroutineScheduler getScheduler() {
            return CoroutineScheduler.this;
        }

        @NotNull
        public final WorkQueue getLocalQueue() {
            return this.localQueue;
        }

        @Override // java.lang.Thread
        @NotNull
        public final WorkerState getState() {
            return this.state;
        }

        public final boolean isParking() {
            return this.state == WorkerState.PARKING;
        }

        public final boolean isBlocking() {
            return this.state == WorkerState.BLOCKING;
        }

        @Nullable
        public final Object getNextParkedWorker() {
            return this.nextParkedWorker;
        }

        public final void setNextParkedWorker(@Nullable Object obj) {
            this.nextParkedWorker = obj;
        }

        public final boolean tryForbidTermination() {
            int i = this.terminationState;
            if (i == 1 || i == -1) {
                return false;
            }
            if (i == 0) {
                return terminationState$FU.compareAndSet(this, 0, -1);
            }
            throw new IllegalStateException(("Invalid terminationState = " + i).toString());
        }

        public final boolean tryAcquireCpuPermit() {
            WorkerState workerState = this.state;
            WorkerState workerState2 = WorkerState.CPU_ACQUIRED;
            if (workerState == workerState2) {
                return true;
            }
            if (!CoroutineScheduler.this.cpuPermits.tryAcquire()) {
                return false;
            }
            this.state = workerState2;
            return true;
        }

        public final boolean tryReleaseCpu$kotlinx_coroutines_core(@NotNull WorkerState newState) {
            Intrinsics.checkParameterIsNotNull(newState, "newState");
            WorkerState workerState = this.state;
            boolean z = workerState == WorkerState.CPU_ACQUIRED;
            if (z) {
                CoroutineScheduler.this.cpuPermits.release();
            }
            if (workerState != newState) {
                this.state = newState;
            }
            return z;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            boolean z = false;
            while (!CoroutineScheduler.this.isTerminated() && this.state != WorkerState.TERMINATED) {
                Task findTask$kotlinx_coroutines_core = findTask$kotlinx_coroutines_core();
                if (findTask$kotlinx_coroutines_core == null) {
                    if (this.state == WorkerState.CPU_ACQUIRED) {
                        cpuWorkerIdle();
                    } else {
                        blockingWorkerIdle();
                    }
                    z = true;
                } else {
                    TaskMode mode = findTask$kotlinx_coroutines_core.getMode();
                    if (z) {
                        idleReset(mode);
                        z = false;
                    }
                    beforeTask(mode, findTask$kotlinx_coroutines_core.submissionTime);
                    CoroutineScheduler.this.runSafely(findTask$kotlinx_coroutines_core);
                    afterTask(mode);
                }
            }
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.TERMINATED);
        }

        private final void beforeTask(TaskMode taskMode, long j) {
            if (taskMode == TaskMode.NON_BLOCKING) {
                if (CoroutineScheduler.this.cpuPermits.availablePermits() == 0) {
                    return;
                }
                long nanoTime = TasksKt.schedulerTimeSource.nanoTime();
                long j2 = TasksKt.WORK_STEALING_TIME_RESOLUTION_NS;
                if (nanoTime - j < j2 || nanoTime - this.lastExhaustionTime < j2 * 5) {
                    return;
                }
                this.lastExhaustionTime = nanoTime;
                CoroutineScheduler.this.requestCpuWorker();
                return;
            }
            CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, 2097152L);
            if (!tryReleaseCpu$kotlinx_coroutines_core(WorkerState.BLOCKING)) {
                return;
            }
            CoroutineScheduler.this.requestCpuWorker();
        }

        private final void afterTask(TaskMode taskMode) {
            if (taskMode != TaskMode.NON_BLOCKING) {
                CoroutineScheduler.controlState$FU.addAndGet(CoroutineScheduler.this, -2097152L);
                WorkerState workerState = this.state;
                if (workerState == WorkerState.TERMINATED) {
                    return;
                }
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(workerState == WorkerState.BLOCKING)) {
                        throw new AssertionError();
                    }
                }
                this.state = WorkerState.RETIRING;
            }
        }

        public final int nextInt$kotlinx_coroutines_core(int i) {
            int i2 = this.rngState;
            int i3 = i2 ^ (i2 << 13);
            this.rngState = i3;
            int i4 = i3 ^ (i3 >> 17);
            this.rngState = i4;
            int i5 = i4 ^ (i4 << 5);
            this.rngState = i5;
            int i6 = i - 1;
            return (i6 & i) == 0 ? i6 & i5 : (Integer.MAX_VALUE & i5) % i;
        }

        private final void cpuWorkerIdle() {
            int coerceAtMost;
            int i = this.spins;
            if (i > CoroutineScheduler.MAX_YIELDS) {
                if (this.parkTimeNs < CoroutineScheduler.MAX_PARK_TIME_NS) {
                    coerceAtMost = RangesKt___RangesKt.coerceAtMost((this.parkTimeNs * 3) >>> 1, CoroutineScheduler.MAX_PARK_TIME_NS);
                    this.parkTimeNs = coerceAtMost;
                }
                tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
                doPark(this.parkTimeNs);
                return;
            }
            this.spins = i + 1;
            if (i < CoroutineScheduler.MAX_SPINS) {
                return;
            }
            Thread.yield();
        }

        private final void blockingWorkerIdle() {
            tryReleaseCpu$kotlinx_coroutines_core(WorkerState.PARKING);
            if (!blockingQuiescence()) {
                return;
            }
            this.terminationState = 0;
            if (this.terminationDeadline == 0) {
                this.terminationDeadline = System.nanoTime() + CoroutineScheduler.this.idleWorkerKeepAliveNs;
            }
            if (!doPark(CoroutineScheduler.this.idleWorkerKeepAliveNs) || System.nanoTime() - this.terminationDeadline < 0) {
                return;
            }
            this.terminationDeadline = 0L;
            tryTerminateWorker();
        }

        private final boolean doPark(long j) {
            CoroutineScheduler.this.parkedWorkersStackPush(this);
            if (!blockingQuiescence()) {
                return false;
            }
            LockSupport.parkNanos(j);
            return true;
        }

        private final void tryTerminateWorker() {
            synchronized (CoroutineScheduler.this.workers) {
                if (CoroutineScheduler.this.isTerminated()) {
                    return;
                }
                if (CoroutineScheduler.this.getCreatedWorkers() <= CoroutineScheduler.this.corePoolSize) {
                    return;
                }
                if (!blockingQuiescence()) {
                    return;
                }
                if (!terminationState$FU.compareAndSet(this, 0, 1)) {
                    return;
                }
                int i = this.indexInArray;
                setIndexInArray(0);
                CoroutineScheduler.this.parkedWorkersStackTopUpdate(this, i, 0);
                int andDecrement = (int) (CoroutineScheduler.controlState$FU.getAndDecrement(CoroutineScheduler.this) & 2097151);
                if (andDecrement != i) {
                    Worker worker = CoroutineScheduler.this.workers[andDecrement];
                    if (worker == null) {
                        Intrinsics.throwNpe();
                    }
                    CoroutineScheduler.this.workers[i] = worker;
                    worker.setIndexInArray(i);
                    CoroutineScheduler.this.parkedWorkersStackTopUpdate(worker, andDecrement, i);
                }
                CoroutineScheduler.this.workers[andDecrement] = null;
                Unit unit = Unit.INSTANCE;
                this.state = WorkerState.TERMINATED;
            }
        }

        private final boolean blockingQuiescence() {
            Task removeFirstWithModeOrNull = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
            if (removeFirstWithModeOrNull != null) {
                this.localQueue.add(removeFirstWithModeOrNull, CoroutineScheduler.this.globalQueue);
                return false;
            }
            return true;
        }

        private final void idleReset(TaskMode taskMode) {
            this.terminationDeadline = 0L;
            this.lastStealIndex = 0;
            if (this.state == WorkerState.PARKING) {
                if (DebugKt.getASSERTIONS_ENABLED()) {
                    if (!(taskMode == TaskMode.PROBABLY_BLOCKING)) {
                        throw new AssertionError();
                    }
                }
                this.state = WorkerState.BLOCKING;
                this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            }
            this.spins = 0;
        }

        public final void idleResetBeforeUnpark() {
            this.parkTimeNs = CoroutineScheduler.MIN_PARK_TIME_NS;
            this.spins = 0;
        }

        @Nullable
        public final Task findTask$kotlinx_coroutines_core() {
            if (tryAcquireCpuPermit()) {
                return findTaskWithCpuPermit();
            }
            Task poll = this.localQueue.poll();
            return poll != null ? poll : CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.PROBABLY_BLOCKING);
        }

        private final Task findTaskWithCpuPermit() {
            Task removeFirstOrNull;
            Task removeFirstWithModeOrNull;
            boolean z = nextInt$kotlinx_coroutines_core(CoroutineScheduler.this.corePoolSize * 2) == 0;
            if (!z || (removeFirstWithModeOrNull = CoroutineScheduler.this.globalQueue.removeFirstWithModeOrNull(TaskMode.NON_BLOCKING)) == null) {
                Task poll = this.localQueue.poll();
                return poll != null ? poll : (z || (removeFirstOrNull = CoroutineScheduler.this.globalQueue.removeFirstOrNull()) == null) ? trySteal() : removeFirstOrNull;
            }
            return removeFirstWithModeOrNull;
        }

        private final Task trySteal() {
            int createdWorkers = CoroutineScheduler.this.getCreatedWorkers();
            if (createdWorkers < 2) {
                return null;
            }
            int i = this.lastStealIndex;
            if (i == 0) {
                i = nextInt$kotlinx_coroutines_core(createdWorkers);
            }
            int i2 = 1;
            int i3 = i + 1;
            if (i3 <= createdWorkers) {
                i2 = i3;
            }
            this.lastStealIndex = i2;
            Worker worker = CoroutineScheduler.this.workers[i2];
            if (worker != null && worker != this && this.localQueue.trySteal(worker.localQueue, CoroutineScheduler.this.globalQueue)) {
                return this.localQueue.poll();
            }
            return null;
        }
    }

    public final void parkedWorkersStackTopUpdate(Worker worker, int i, int i2) {
        while (true) {
            long j = this.parkedWorkersStack;
            int i3 = (int) (2097151 & j);
            long j2 = (2097152 + j) & (-2097152);
            if (i3 == i) {
                i3 = i2 == 0 ? parkedWorkersStackNextIndex(worker) : i2;
            }
            if (i3 >= 0 && parkedWorkersStack$FU.compareAndSet(this, j, j2 | i3)) {
                return;
            }
        }
    }

    private final Worker parkedWorkersStackPop() {
        while (true) {
            long j = this.parkedWorkersStack;
            Worker worker = this.workers[(int) (2097151 & j)];
            if (worker != null) {
                long j2 = (2097152 + j) & (-2097152);
                int parkedWorkersStackNextIndex = parkedWorkersStackNextIndex(worker);
                if (parkedWorkersStackNextIndex >= 0 && parkedWorkersStack$FU.compareAndSet(this, j, parkedWorkersStackNextIndex | j2)) {
                    worker.setNextParkedWorker(NOT_IN_STACK);
                    return worker;
                }
            } else {
                return null;
            }
        }
    }
}
