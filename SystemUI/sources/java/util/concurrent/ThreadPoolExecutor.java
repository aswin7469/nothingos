package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolExecutor extends AbstractExecutorService {
    private static final int COUNT_BITS = 29;
    private static final int COUNT_MASK = 536870911;
    private static final boolean ONLY_ONE = true;
    private static final int RUNNING = -536870912;
    private static final int SHUTDOWN = 0;
    private static final int STOP = 536870912;
    private static final int TERMINATED = 1610612736;
    private static final int TIDYING = 1073741824;
    private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();
    private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
    private volatile boolean allowCoreThreadTimeOut;
    private long completedTaskCount;
    private volatile int corePoolSize;
    private final AtomicInteger ctl;
    private volatile RejectedExecutionHandler handler;
    private volatile long keepAliveTime;
    private int largestPoolSize;
    private final ReentrantLock mainLock;
    private volatile int maximumPoolSize;
    private final Condition termination;
    private volatile ThreadFactory threadFactory;
    private final BlockingQueue<Runnable> workQueue;
    private final HashSet<Worker> workers;

    public static class DiscardPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        }
    }

    private static int ctlOf(int i, int i2) {
        return i | i2;
    }

    private static boolean isRunning(int i) {
        return i < 0;
    }

    private static boolean runStateAtLeast(int i, int i2) {
        return i >= i2;
    }

    private static boolean runStateLessThan(int i, int i2) {
        return i < i2;
    }

    private static int runStateOf(int i) {
        return RUNNING & i;
    }

    private static int workerCountOf(int i) {
        return COUNT_MASK & i;
    }

    /* access modifiers changed from: protected */
    public void afterExecute(Runnable runnable, Throwable th) {
    }

    /* access modifiers changed from: protected */
    public void beforeExecute(Thread thread, Runnable runnable) {
    }

    /* access modifiers changed from: protected */
    @Deprecated(since = "9")
    public void finalize() {
    }

    /* access modifiers changed from: package-private */
    public void onShutdown() {
    }

    /* access modifiers changed from: protected */
    public void terminated() {
    }

    private boolean compareAndIncrementWorkerCount(int expect) {
        return this.ctl.compareAndSet(expect, expect + 1);
    }

    private boolean compareAndDecrementWorkerCount(int expect) {
        return this.ctl.compareAndSet(expect, expect - 1);
    }

    private void decrementWorkerCount() {
        this.ctl.addAndGet(-1);
    }

    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
        private static final long serialVersionUID = 6138294804551838833L;
        volatile long completedTasks;
        Runnable firstTask;
        final Thread thread;

        Worker(Runnable runnable) {
            setState(-1);
            this.firstTask = runnable;
            this.thread = ThreadPoolExecutor.this.getThreadFactory().newThread(this);
        }

        public void run() {
            ThreadPoolExecutor.this.runWorker(this);
        }

        /* access modifiers changed from: protected */
        public boolean isHeldExclusively() {
            return getState() != 0;
        }

        /* access modifiers changed from: protected */
        public boolean tryAcquire(int i) {
            if (!compareAndSetState(0, 1)) {
                return false;
            }
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean tryRelease(int i) {
            setExclusiveOwnerThread((Thread) null);
            setState(0);
            return true;
        }

        public void lock() {
            acquire(1);
        }

        public boolean tryLock() {
            return tryAcquire(1);
        }

        public void unlock() {
            release(1);
        }

        public boolean isLocked() {
            return isHeldExclusively();
        }

        /* access modifiers changed from: package-private */
        public void interruptIfStarted() {
            Thread thread2;
            if (getState() >= 0 && (thread2 = this.thread) != null && !thread2.isInterrupted()) {
                try {
                    thread2.interrupt();
                } catch (SecurityException unused) {
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void advanceRunState(int r4) {
        /*
            r3 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicInteger r0 = r3.ctl
            int r0 = r0.get()
            boolean r1 = runStateAtLeast(r0, r4)
            if (r1 != 0) goto L_0x001c
            java.util.concurrent.atomic.AtomicInteger r1 = r3.ctl
            int r2 = workerCountOf(r0)
            int r2 = ctlOf(r4, r2)
            boolean r1 = r1.compareAndSet(r0, r2)
            if (r1 == 0) goto L_0x0000
        L_0x001c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ThreadPoolExecutor.advanceRunState(int):void");
    }

    /* access modifiers changed from: package-private */
    public final void tryTerminate() {
        while (true) {
            int i = this.ctl.get();
            if (!isRunning(i) && !runStateAtLeast(i, 1073741824)) {
                if (runStateLessThan(i, 536870912) && !this.workQueue.isEmpty()) {
                    return;
                }
                if (workerCountOf(i) != 0) {
                    interruptIdleWorkers(true);
                    return;
                }
                ReentrantLock reentrantLock = this.mainLock;
                reentrantLock.lock();
                try {
                    if (this.ctl.compareAndSet(i, ctlOf(1073741824, 0))) {
                        terminated();
                        this.ctl.set(ctlOf(TERMINATED, 0));
                        this.termination.signalAll();
                        reentrantLock.unlock();
                        return;
                    }
                    reentrantLock.unlock();
                } catch (Throwable th) {
                    reentrantLock.unlock();
                    throw th;
                }
            } else {
                return;
            }
        }
    }

    private void checkShutdownAccess() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(shutdownPerm);
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                securityManager.checkAccess(it.next().thread);
            }
        }
    }

    private void interruptWorkers() {
        Iterator<Worker> it = this.workers.iterator();
        while (it.hasNext()) {
            it.next().interruptIfStarted();
        }
    }

    private void interruptIdleWorkers(boolean onlyOne) {
        Worker next;
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                next = it.next();
                Thread thread = next.thread;
                if (!thread.isInterrupted() && next.tryLock()) {
                    thread.interrupt();
                    next.unlock();
                    continue;
                }
                if (onlyOne) {
                    break;
                }
            }
        } catch (SecurityException e) {
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
        reentrantLock.unlock();
    }

    private void interruptIdleWorkers() {
        interruptIdleWorkers(false);
    }

    /* access modifiers changed from: package-private */
    public final void reject(Runnable command) {
        this.handler.rejectedExecution(command, this);
    }

    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> blockingQueue = this.workQueue;
        ArrayList arrayList = new ArrayList();
        blockingQueue.drainTo(arrayList);
        if (!blockingQueue.isEmpty()) {
            for (Runnable runnable : (Runnable[]) blockingQueue.toArray((T[]) new Runnable[0])) {
                if (blockingQueue.remove(runnable)) {
                    arrayList.add(runnable);
                }
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0083 A[Catch:{ all -> 0x008e, all -> 0x009a }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean addWorker(java.lang.Runnable r9, boolean r10) {
        /*
            r8 = this;
            java.util.concurrent.atomic.AtomicInteger r0 = r8.ctl
            int r0 = r0.get()
        L_0x0006:
            r1 = 0
            boolean r2 = runStateAtLeast(r0, r1)
            r3 = 536870912(0x20000000, float:1.0842022E-19)
            if (r2 == 0) goto L_0x00af
            boolean r2 = runStateAtLeast(r0, r3)
            if (r2 != 0) goto L_0x001f
            if (r9 != 0) goto L_0x001f
            java.util.concurrent.BlockingQueue<java.lang.Runnable> r2 = r8.workQueue
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x00af
        L_0x001f:
            return r1
        L_0x0021:
            int r2 = workerCountOf(r0)
            if (r10 == 0) goto L_0x002a
            int r4 = r8.corePoolSize
            goto L_0x002c
        L_0x002a:
            int r4 = r8.maximumPoolSize
        L_0x002c:
            r5 = 536870911(0x1fffffff, float:1.0842021E-19)
            r4 = r4 & r5
            if (r2 < r4) goto L_0x0033
            return r1
        L_0x0033:
            boolean r2 = r8.compareAndIncrementWorkerCount(r0)
            if (r2 == 0) goto L_0x00a1
            r0 = 0
            r1 = 0
            r2 = 0
            java.util.concurrent.ThreadPoolExecutor$Worker r4 = new java.util.concurrent.ThreadPoolExecutor$Worker     // Catch:{ all -> 0x009a }
            r4.<init>(r9)     // Catch:{ all -> 0x009a }
            r2 = r4
            java.lang.Thread r4 = r2.thread     // Catch:{ all -> 0x009a }
            if (r4 == 0) goto L_0x0093
            java.util.concurrent.locks.ReentrantLock r5 = r8.mainLock     // Catch:{ all -> 0x009a }
            r5.lock()     // Catch:{ all -> 0x009a }
            java.util.concurrent.atomic.AtomicInteger r6 = r8.ctl     // Catch:{ all -> 0x008e }
            int r6 = r6.get()     // Catch:{ all -> 0x008e }
            boolean r7 = isRunning(r6)     // Catch:{ all -> 0x008e }
            if (r7 != 0) goto L_0x0062
            boolean r3 = runStateLessThan(r6, r3)     // Catch:{ all -> 0x008e }
            if (r3 == 0) goto L_0x0061
            if (r9 != 0) goto L_0x0061
            goto L_0x0062
        L_0x0061:
            goto L_0x007d
        L_0x0062:
            java.lang.Thread$State r3 = r4.getState()     // Catch:{ all -> 0x008e }
            java.lang.Thread$State r7 = java.lang.Thread.State.NEW     // Catch:{ all -> 0x008e }
            if (r3 != r7) goto L_0x0088
            java.util.HashSet<java.util.concurrent.ThreadPoolExecutor$Worker> r3 = r8.workers     // Catch:{ all -> 0x008e }
            r3.add(r2)     // Catch:{ all -> 0x008e }
            r1 = 1
            java.util.HashSet<java.util.concurrent.ThreadPoolExecutor$Worker> r3 = r8.workers     // Catch:{ all -> 0x008e }
            int r3 = r3.size()     // Catch:{ all -> 0x008e }
            int r7 = r8.largestPoolSize     // Catch:{ all -> 0x008e }
            if (r3 <= r7) goto L_0x0061
            r8.largestPoolSize = r3     // Catch:{ all -> 0x008e }
            goto L_0x0061
        L_0x007d:
            r5.unlock()     // Catch:{ all -> 0x009a }
            if (r1 == 0) goto L_0x0093
            r4.start()     // Catch:{ all -> 0x009a }
            r0 = 1
            goto L_0x0094
        L_0x0088:
            java.lang.IllegalThreadStateException r3 = new java.lang.IllegalThreadStateException     // Catch:{ all -> 0x008e }
            r3.<init>()     // Catch:{ all -> 0x008e }
            throw r3     // Catch:{ all -> 0x008e }
        L_0x008e:
            r3 = move-exception
            r5.unlock()     // Catch:{ all -> 0x009a }
            throw r3     // Catch:{ all -> 0x009a }
        L_0x0093:
        L_0x0094:
            if (r0 != 0) goto L_0x0099
            r8.addWorkerFailed(r2)
        L_0x0099:
            return r0
        L_0x009a:
            r3 = move-exception
            if (r0 != 0) goto L_0x00a0
            r8.addWorkerFailed(r2)
        L_0x00a0:
            throw r3
        L_0x00a1:
            java.util.concurrent.atomic.AtomicInteger r2 = r8.ctl
            int r0 = r2.get()
            boolean r2 = runStateAtLeast(r0, r1)
            if (r2 == 0) goto L_0x00af
            goto L_0x0006
        L_0x00af:
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ThreadPoolExecutor.addWorker(java.lang.Runnable, boolean):boolean");
    }

    private void addWorkerFailed(Worker w) {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        if (w != null) {
            try {
                this.workers.remove(w);
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
        decrementWorkerCount();
        tryTerminate();
        reentrantLock.unlock();
    }

    /* JADX INFO: finally extract failed */
    private void processWorkerExit(Worker w, boolean completedAbruptly) {
        if (completedAbruptly) {
            decrementWorkerCount();
        }
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            this.completedTaskCount += w.completedTasks;
            this.workers.remove(w);
            reentrantLock.unlock();
            tryTerminate();
            int i = this.ctl.get();
            if (runStateLessThan(i, 536870912)) {
                if (!completedAbruptly) {
                    int i2 = this.allowCoreThreadTimeOut ? 0 : this.corePoolSize;
                    if (i2 == 0 && !this.workQueue.isEmpty()) {
                        i2 = 1;
                    }
                    if (workerCountOf(i) >= i2) {
                        return;
                    }
                }
                addWorker((Runnable) null, false);
            }
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    private Runnable getTask() {
        Runnable runnable;
        boolean z = false;
        while (true) {
            int i = this.ctl.get();
            boolean z2 = false;
            if (!runStateAtLeast(i, 0) || (!runStateAtLeast(i, 536870912) && !this.workQueue.isEmpty())) {
                int workerCountOf = workerCountOf(i);
                if (this.allowCoreThreadTimeOut || workerCountOf > this.corePoolSize) {
                    z2 = true;
                }
                if ((workerCountOf <= this.maximumPoolSize && (!z2 || !z)) || (workerCountOf <= 1 && !this.workQueue.isEmpty())) {
                    if (z2) {
                        try {
                            runnable = this.workQueue.poll(this.keepAliveTime, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                            z = false;
                        }
                    } else {
                        runnable = this.workQueue.take();
                    }
                    if (runnable != null) {
                        return runnable;
                    }
                    z = true;
                } else if (compareAndDecrementWorkerCount(i)) {
                    return null;
                }
            }
        }
        decrementWorkerCount();
        return null;
    }

    /* access modifiers changed from: package-private */
    public final void runWorker(Worker w) {
        Thread currentThread = Thread.currentThread();
        Runnable runnable = w.firstTask;
        w.firstTask = null;
        w.unlock();
        boolean z = true;
        while (true) {
            if (runnable == null) {
                try {
                    Runnable task = getTask();
                    runnable = task;
                    if (task == null) {
                        z = false;
                        return;
                    }
                } finally {
                    processWorkerExit(w, z);
                }
            }
            w.lock();
            if ((runStateAtLeast(this.ctl.get(), 536870912) || (Thread.interrupted() && runStateAtLeast(this.ctl.get(), 536870912))) && !currentThread.isInterrupted()) {
                currentThread.interrupt();
            }
            try {
                beforeExecute(currentThread, runnable);
                runnable.run();
                afterExecute(runnable, (Throwable) null);
                runnable = null;
                w.completedTasks++;
                w.unlock();
            } catch (Throwable th) {
                w.completedTasks++;
                w.unlock();
                throw th;
            }
        }
    }

    public ThreadPoolExecutor(int corePoolSize2, int maximumPoolSize2, long keepAliveTime2, TimeUnit unit, BlockingQueue<Runnable> blockingQueue) {
        this(corePoolSize2, maximumPoolSize2, keepAliveTime2, unit, blockingQueue, Executors.defaultThreadFactory(), defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize2, int maximumPoolSize2, long keepAliveTime2, TimeUnit unit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory2) {
        this(corePoolSize2, maximumPoolSize2, keepAliveTime2, unit, blockingQueue, threadFactory2, defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize2, int maximumPoolSize2, long keepAliveTime2, TimeUnit unit, BlockingQueue<Runnable> blockingQueue, RejectedExecutionHandler handler2) {
        this(corePoolSize2, maximumPoolSize2, keepAliveTime2, unit, blockingQueue, Executors.defaultThreadFactory(), handler2);
    }

    public ThreadPoolExecutor(int corePoolSize2, int maximumPoolSize2, long keepAliveTime2, TimeUnit unit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory2, RejectedExecutionHandler handler2) {
        this.ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        ReentrantLock reentrantLock = new ReentrantLock();
        this.mainLock = reentrantLock;
        this.workers = new HashSet<>();
        this.termination = reentrantLock.newCondition();
        if (corePoolSize2 < 0 || maximumPoolSize2 <= 0 || maximumPoolSize2 < corePoolSize2 || keepAliveTime2 < 0) {
            throw new IllegalArgumentException();
        } else if (blockingQueue == null || threadFactory2 == null || handler2 == null) {
            throw new NullPointerException();
        } else {
            this.corePoolSize = corePoolSize2;
            this.maximumPoolSize = maximumPoolSize2;
            this.workQueue = blockingQueue;
            this.keepAliveTime = unit.toNanos(keepAliveTime2);
            this.threadFactory = threadFactory2;
            this.handler = handler2;
        }
    }

    public void execute(Runnable command) {
        if (command != null) {
            int i = this.ctl.get();
            if (workerCountOf(i) < this.corePoolSize) {
                if (!addWorker(command, true)) {
                    i = this.ctl.get();
                } else {
                    return;
                }
            }
            if (isRunning(i) && this.workQueue.offer(command)) {
                int i2 = this.ctl.get();
                if (!isRunning(i2) && remove(command)) {
                    reject(command);
                } else if (workerCountOf(i2) == 0) {
                    addWorker((Runnable) null, false);
                }
            } else if (!addWorker(command, false)) {
                reject(command);
            }
        } else {
            throw new NullPointerException();
        }
    }

    /* JADX INFO: finally extract failed */
    public void shutdown() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(0);
            interruptIdleWorkers();
            onShutdown();
            reentrantLock.unlock();
            tryTerminate();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    public List<Runnable> shutdownNow() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(536870912);
            interruptWorkers();
            List<Runnable> drainQueue = drainQueue();
            reentrantLock.unlock();
            tryTerminate();
            return drainQueue;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public boolean isShutdown() {
        return runStateAtLeast(this.ctl.get(), 0);
    }

    /* access modifiers changed from: package-private */
    public boolean isStopped() {
        return runStateAtLeast(this.ctl.get(), 536870912);
    }

    public boolean isTerminating() {
        int i = this.ctl.get();
        if (!runStateAtLeast(i, 0) || !runStateLessThan(i, TERMINATED)) {
            return false;
        }
        return true;
    }

    public boolean isTerminated() {
        return runStateAtLeast(this.ctl.get(), TERMINATED);
    }

    /* JADX INFO: finally extract failed */
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        while (runStateLessThan(this.ctl.get(), TERMINATED)) {
            try {
                if (nanos <= 0) {
                    reentrantLock.unlock();
                    return false;
                }
                nanos = this.termination.awaitNanos(nanos);
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }
        reentrantLock.unlock();
        return true;
    }

    public void setThreadFactory(ThreadFactory threadFactory2) {
        if (threadFactory2 != null) {
            this.threadFactory = threadFactory2;
            return;
        }
        throw new NullPointerException();
    }

    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler handler2) {
        if (handler2 != null) {
            this.handler = handler2;
            return;
        }
        throw new NullPointerException();
    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return this.handler;
    }

    public void setCorePoolSize(int corePoolSize2) {
        if (corePoolSize2 >= 0) {
            int i = corePoolSize2 - this.corePoolSize;
            this.corePoolSize = corePoolSize2;
            if (workerCountOf(this.ctl.get()) > corePoolSize2) {
                interruptIdleWorkers();
            } else if (i > 0) {
                int min = Math.min(i, this.workQueue.size());
                while (true) {
                    int i2 = min - 1;
                    if (min > 0 && addWorker((Runnable) null, true) && !this.workQueue.isEmpty()) {
                        min = i2;
                    } else {
                        return;
                    }
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getCorePoolSize() {
        return this.corePoolSize;
    }

    public boolean prestartCoreThread() {
        if (workerCountOf(this.ctl.get()) >= this.corePoolSize || !addWorker((Runnable) null, true)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void ensurePrestart() {
        int workerCountOf = workerCountOf(this.ctl.get());
        if (workerCountOf < this.corePoolSize) {
            addWorker((Runnable) null, true);
        } else if (workerCountOf == 0) {
            addWorker((Runnable) null, false);
        }
    }

    public int prestartAllCoreThreads() {
        int i = 0;
        while (addWorker((Runnable) null, true)) {
            i++;
        }
        return i;
    }

    public boolean allowsCoreThreadTimeOut() {
        return this.allowCoreThreadTimeOut;
    }

    public void allowCoreThreadTimeOut(boolean value) {
        if (value && this.keepAliveTime <= 0) {
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        } else if (value != this.allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = value;
            if (value) {
                interruptIdleWorkers();
            }
        }
    }

    public void setMaximumPoolSize(int maximumPoolSize2) {
        if (maximumPoolSize2 <= 0 || maximumPoolSize2 < this.corePoolSize) {
            throw new IllegalArgumentException();
        }
        this.maximumPoolSize = maximumPoolSize2;
        if (workerCountOf(this.ctl.get()) > maximumPoolSize2) {
            interruptIdleWorkers();
        }
    }

    public int getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public void setKeepAliveTime(long time, TimeUnit unit) {
        if (time < 0) {
            throw new IllegalArgumentException();
        } else if (time != 0 || !allowsCoreThreadTimeOut()) {
            long nanos = unit.toNanos(time);
            this.keepAliveTime = nanos;
            if (nanos - this.keepAliveTime < 0) {
                interruptIdleWorkers();
            }
        } else {
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        }
    }

    public long getKeepAliveTime(TimeUnit unit) {
        return unit.convert(this.keepAliveTime, TimeUnit.NANOSECONDS);
    }

    public BlockingQueue<Runnable> getQueue() {
        return this.workQueue;
    }

    public boolean remove(Runnable task) {
        boolean remove = this.workQueue.remove(task);
        tryTerminate();
        return remove;
    }

    public void purge() {
        BlockingQueue<Runnable> blockingQueue = this.workQueue;
        try {
            Iterator<Runnable> it = blockingQueue.iterator();
            while (it.hasNext()) {
                Runnable next = it.next();
                if ((next instanceof Future) && ((Future) next).isCancelled()) {
                    it.remove();
                }
            }
        } catch (ConcurrentModificationException e) {
            for (Object obj : blockingQueue.toArray()) {
                if ((obj instanceof Future) && ((Future) obj).isCancelled()) {
                    blockingQueue.remove(obj);
                }
            }
        }
        tryTerminate();
    }

    public int getPoolSize() {
        int i;
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            if (runStateAtLeast(this.ctl.get(), 1073741824)) {
                i = 0;
            } else {
                i = this.workers.size();
            }
            return i;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getActiveCount() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        int i = 0;
        try {
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                if (it.next().isLocked()) {
                    i++;
                }
            }
            return i;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getLargestPoolSize() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            return this.largestPoolSize;
        } finally {
            reentrantLock.unlock();
        }
    }

    public long getTaskCount() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            long j = this.completedTaskCount;
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                Worker next = it.next();
                j += next.completedTasks;
                if (next.isLocked()) {
                    j++;
                }
            }
            return ((long) this.workQueue.size()) + j;
        } finally {
            reentrantLock.unlock();
        }
    }

    public long getCompletedTaskCount() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            long j = this.completedTaskCount;
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                j += it.next().completedTasks;
            }
            return j;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public String toString() {
        String str;
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            long j = this.completedTaskCount;
            int i = 0;
            int size = this.workers.size();
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                Worker next = it.next();
                j += next.completedTasks;
                if (next.isLocked()) {
                    i++;
                }
            }
            reentrantLock.unlock();
            int i2 = this.ctl.get();
            if (isRunning(i2)) {
                str = "Running";
            } else {
                str = runStateAtLeast(i2, TERMINATED) ? "Terminated" : "Shutting down";
            }
            return super.toString() + NavigationBarInflaterView.SIZE_MOD_START + str + ", pool size = " + size + ", active threads = " + i + ", queued tasks = " + this.workQueue.size() + ", completed tasks = " + j + NavigationBarInflaterView.SIZE_MOD_END;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public static class CallerRunsPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            if (!threadPoolExecutor.isShutdown()) {
                runnable.run();
            }
        }
    }

    public static class AbortPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + threadPoolExecutor.toString());
        }
    }

    public static class DiscardOldestPolicy implements RejectedExecutionHandler {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            if (!threadPoolExecutor.isShutdown()) {
                threadPoolExecutor.getQueue().poll();
                threadPoolExecutor.execute(runnable);
            }
        }
    }
}
