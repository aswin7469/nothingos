package java.util.concurrent;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.time.DurationKt;

public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {
    private static final long DEFAULT_KEEPALIVE_MILLIS = 10;
    private static final AtomicLong sequencer = new AtomicLong();
    private volatile boolean continueExistingPeriodicTasksAfterShutdown;
    private volatile boolean executeExistingDelayedTasksAfterShutdown = true;
    volatile boolean removeOnCancel;

    /* access modifiers changed from: protected */
    public <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> runnableScheduledFuture) {
        return runnableScheduledFuture;
    }

    /* access modifiers changed from: protected */
    public <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> runnableScheduledFuture) {
        return runnableScheduledFuture;
    }

    private class ScheduledFutureTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
        int heapIndex;
        RunnableScheduledFuture<V> outerTask = this;
        private final long period;
        private final long sequenceNumber;
        private volatile long time;

        ScheduledFutureTask(Runnable runnable, V v, long j, long j2) {
            super(runnable, v);
            this.time = j;
            this.period = 0;
            this.sequenceNumber = j2;
        }

        ScheduledFutureTask(Runnable runnable, V v, long j, long j2, long j3) {
            super(runnable, v);
            this.time = j;
            this.period = j2;
            this.sequenceNumber = j3;
        }

        ScheduledFutureTask(Callable<V> callable, long j, long j2) {
            super(callable);
            this.time = j;
            this.period = 0;
            this.sequenceNumber = j2;
        }

        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.time - System.nanoTime(), TimeUnit.NANOSECONDS);
        }

        public int compareTo(Delayed delayed) {
            if (delayed == this) {
                return 0;
            }
            if (delayed instanceof ScheduledFutureTask) {
                ScheduledFutureTask scheduledFutureTask = (ScheduledFutureTask) delayed;
                int i = ((this.time - scheduledFutureTask.time) > 0 ? 1 : ((this.time - scheduledFutureTask.time) == 0 ? 0 : -1));
                if (i < 0) {
                    return -1;
                }
                return (i <= 0 && this.sequenceNumber < scheduledFutureTask.sequenceNumber) ? -1 : 1;
            }
            int i2 = ((getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS)) > 0 ? 1 : ((getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS)) == 0 ? 0 : -1));
            if (i2 < 0) {
                return -1;
            }
            if (i2 > 0) {
                return 1;
            }
            return 0;
        }

        public boolean isPeriodic() {
            return this.period != 0;
        }

        private void setNextRunTime() {
            long j = this.period;
            if (j > 0) {
                this.time += j;
            } else {
                this.time = ScheduledThreadPoolExecutor.this.triggerTime(-j);
            }
        }

        public boolean cancel(boolean z) {
            boolean cancel = super.cancel(z);
            if (cancel && ScheduledThreadPoolExecutor.this.removeOnCancel && this.heapIndex >= 0) {
                ScheduledThreadPoolExecutor.this.remove(this);
            }
            return cancel;
        }

        public void run() {
            if (!ScheduledThreadPoolExecutor.this.canRunInCurrentRunState(this)) {
                cancel(false);
            } else if (!isPeriodic()) {
                super.run();
            } else if (super.runAndReset()) {
                setNextRunTime();
                ScheduledThreadPoolExecutor.this.reExecutePeriodic(this.outerTask);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean canRunInCurrentRunState(RunnableScheduledFuture<?> runnableScheduledFuture) {
        if (!isShutdown()) {
            return true;
        }
        if (isStopped()) {
            return false;
        }
        if (runnableScheduledFuture.isPeriodic()) {
            return this.continueExistingPeriodicTasksAfterShutdown;
        }
        return this.executeExistingDelayedTasksAfterShutdown;
    }

    private void delayedExecute(RunnableScheduledFuture<?> runnableScheduledFuture) {
        if (isShutdown()) {
            reject(runnableScheduledFuture);
            return;
        }
        super.getQueue().add(runnableScheduledFuture);
        if (canRunInCurrentRunState(runnableScheduledFuture) || !remove(runnableScheduledFuture)) {
            ensurePrestart();
        } else {
            runnableScheduledFuture.cancel(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void reExecutePeriodic(RunnableScheduledFuture<?> runnableScheduledFuture) {
        if (canRunInCurrentRunState(runnableScheduledFuture)) {
            super.getQueue().add(runnableScheduledFuture);
            if (canRunInCurrentRunState(runnableScheduledFuture) || !remove(runnableScheduledFuture)) {
                ensurePrestart();
                return;
            }
        }
        runnableScheduledFuture.cancel(false);
    }

    /* access modifiers changed from: package-private */
    public void onShutdown() {
        BlockingQueue<Runnable> queue = super.getQueue();
        boolean executeExistingDelayedTasksAfterShutdownPolicy = getExecuteExistingDelayedTasksAfterShutdownPolicy();
        boolean continueExistingPeriodicTasksAfterShutdownPolicy = getContinueExistingPeriodicTasksAfterShutdownPolicy();
        for (Object obj : queue.toArray()) {
            if (obj instanceof RunnableScheduledFuture) {
                RunnableScheduledFuture runnableScheduledFuture = (RunnableScheduledFuture) obj;
                if (!runnableScheduledFuture.isPeriodic() ? executeExistingDelayedTasksAfterShutdownPolicy : continueExistingPeriodicTasksAfterShutdownPolicy) {
                    if (!runnableScheduledFuture.isCancelled()) {
                    }
                }
                if (queue.remove(runnableScheduledFuture)) {
                    runnableScheduledFuture.cancel(false);
                }
            }
        }
        tryTerminate();
    }

    public ScheduledThreadPoolExecutor(int i) {
        super(i, Integer.MAX_VALUE, DEFAULT_KEEPALIVE_MILLIS, TimeUnit.MILLISECONDS, new DelayedWorkQueue());
    }

    public ScheduledThreadPoolExecutor(int i, ThreadFactory threadFactory) {
        super(i, Integer.MAX_VALUE, (long) DEFAULT_KEEPALIVE_MILLIS, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) new DelayedWorkQueue(), threadFactory);
    }

    public ScheduledThreadPoolExecutor(int i, RejectedExecutionHandler rejectedExecutionHandler) {
        super(i, Integer.MAX_VALUE, (long) DEFAULT_KEEPALIVE_MILLIS, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) new DelayedWorkQueue(), rejectedExecutionHandler);
    }

    public ScheduledThreadPoolExecutor(int i, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        super(i, Integer.MAX_VALUE, DEFAULT_KEEPALIVE_MILLIS, TimeUnit.MILLISECONDS, new DelayedWorkQueue(), threadFactory, rejectedExecutionHandler);
    }

    private long triggerTime(long j, TimeUnit timeUnit) {
        if (j < 0) {
            j = 0;
        }
        return triggerTime(timeUnit.toNanos(j));
    }

    /* access modifiers changed from: package-private */
    public long triggerTime(long j) {
        long nanoTime = System.nanoTime();
        if (j >= DurationKt.MAX_MILLIS) {
            j = overflowFree(j);
        }
        return nanoTime + j;
    }

    private long overflowFree(long j) {
        Delayed delayed = (Delayed) super.getQueue().peek();
        if (delayed == null) {
            return j;
        }
        long delay = delayed.getDelay(TimeUnit.NANOSECONDS);
        return (delay >= 0 || j - delay >= 0) ? j : delay + Long.MAX_VALUE;
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
        if (runnable == null || timeUnit == null) {
            throw null;
        }
        RunnableScheduledFuture decorateTask = decorateTask(runnable, new ScheduledFutureTask(runnable, null, triggerTime(j, timeUnit), sequencer.getAndIncrement()));
        delayedExecute(decorateTask);
        return decorateTask;
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long j, TimeUnit timeUnit) {
        if (callable == null || timeUnit == null) {
            throw null;
        }
        RunnableScheduledFuture<V> decorateTask = decorateTask(callable, new ScheduledFutureTask(callable, triggerTime(j, timeUnit), sequencer.getAndIncrement()));
        delayedExecute(decorateTask);
        return decorateTask;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        long j3 = j2;
        TimeUnit timeUnit2 = timeUnit;
        if (runnable == null || timeUnit2 == null) {
            throw null;
        } else if (j3 > 0) {
            ScheduledFutureTask scheduledFutureTask = new ScheduledFutureTask(runnable, null, triggerTime(j, timeUnit2), timeUnit2.toNanos(j3), sequencer.getAndIncrement());
            RunnableScheduledFuture<V> decorateTask = decorateTask(runnable, scheduledFutureTask);
            scheduledFutureTask.outerTask = decorateTask;
            delayedExecute(decorateTask);
            return decorateTask;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        long j3 = j2;
        TimeUnit timeUnit2 = timeUnit;
        if (runnable == null || timeUnit2 == null) {
            throw null;
        } else if (j3 > 0) {
            ScheduledFutureTask scheduledFutureTask = new ScheduledFutureTask(runnable, null, triggerTime(j, timeUnit2), -timeUnit2.toNanos(j3), sequencer.getAndIncrement());
            RunnableScheduledFuture<V> decorateTask = decorateTask(runnable, scheduledFutureTask);
            scheduledFutureTask.outerTask = decorateTask;
            delayedExecute(decorateTask);
            return decorateTask;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void execute(Runnable runnable) {
        schedule(runnable, 0, TimeUnit.NANOSECONDS);
    }

    public Future<?> submit(Runnable runnable) {
        return schedule(runnable, 0, TimeUnit.NANOSECONDS);
    }

    public <T> Future<T> submit(Runnable runnable, T t) {
        return schedule(Executors.callable(runnable, t), 0, TimeUnit.NANOSECONDS);
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return schedule(callable, 0, TimeUnit.NANOSECONDS);
    }

    public void setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean z) {
        this.continueExistingPeriodicTasksAfterShutdown = z;
        if (!z && isShutdown()) {
            onShutdown();
        }
    }

    public boolean getContinueExistingPeriodicTasksAfterShutdownPolicy() {
        return this.continueExistingPeriodicTasksAfterShutdown;
    }

    public void setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean z) {
        this.executeExistingDelayedTasksAfterShutdown = z;
        if (!z && isShutdown()) {
            onShutdown();
        }
    }

    public boolean getExecuteExistingDelayedTasksAfterShutdownPolicy() {
        return this.executeExistingDelayedTasksAfterShutdown;
    }

    public void setRemoveOnCancelPolicy(boolean z) {
        this.removeOnCancel = z;
    }

    public boolean getRemoveOnCancelPolicy() {
        return this.removeOnCancel;
    }

    public void shutdown() {
        super.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return super.shutdownNow();
    }

    public BlockingQueue<Runnable> getQueue() {
        return super.getQueue();
    }

    static class DelayedWorkQueue extends AbstractQueue<Runnable> implements BlockingQueue<Runnable> {
        private static final int INITIAL_CAPACITY = 16;
        private final Condition available;
        private Thread leader;
        private final ReentrantLock lock;
        private RunnableScheduledFuture<?>[] queue = new RunnableScheduledFuture[16];
        private int size;

        public int remainingCapacity() {
            return Integer.MAX_VALUE;
        }

        DelayedWorkQueue() {
            ReentrantLock reentrantLock = new ReentrantLock();
            this.lock = reentrantLock;
            this.available = reentrantLock.newCondition();
        }

        private static void setIndex(RunnableScheduledFuture<?> runnableScheduledFuture, int i) {
            if (runnableScheduledFuture instanceof ScheduledFutureTask) {
                ((ScheduledFutureTask) runnableScheduledFuture).heapIndex = i;
            }
        }

        private void siftUp(int i, RunnableScheduledFuture<?> runnableScheduledFuture) {
            while (i > 0) {
                int i2 = (i - 1) >>> 1;
                RunnableScheduledFuture<?> runnableScheduledFuture2 = this.queue[i2];
                if (runnableScheduledFuture.compareTo(runnableScheduledFuture2) >= 0) {
                    break;
                }
                this.queue[i] = runnableScheduledFuture2;
                setIndex(runnableScheduledFuture2, i);
                i = i2;
            }
            this.queue[i] = runnableScheduledFuture;
            setIndex(runnableScheduledFuture, i);
        }

        private void siftDown(int i, RunnableScheduledFuture<?> runnableScheduledFuture) {
            int i2 = this.size >>> 1;
            while (i < i2) {
                int i3 = (i << 1) + 1;
                RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.queue;
                RunnableScheduledFuture<?> runnableScheduledFuture2 = runnableScheduledFutureArr[i3];
                int i4 = i3 + 1;
                if (i4 < this.size && runnableScheduledFuture2.compareTo(runnableScheduledFutureArr[i4]) > 0) {
                    runnableScheduledFuture2 = this.queue[i4];
                    i3 = i4;
                }
                if (runnableScheduledFuture.compareTo(runnableScheduledFuture2) <= 0) {
                    break;
                }
                this.queue[i] = runnableScheduledFuture2;
                setIndex(runnableScheduledFuture2, i);
                i = i3;
            }
            this.queue[i] = runnableScheduledFuture;
            setIndex(runnableScheduledFuture, i);
        }

        private void grow() {
            RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.queue;
            int length = runnableScheduledFutureArr.length;
            int i = length + (length >> 1);
            if (i < 0) {
                i = Integer.MAX_VALUE;
            }
            this.queue = (RunnableScheduledFuture[]) Arrays.copyOf((T[]) runnableScheduledFutureArr, i);
        }

        private int indexOf(Object obj) {
            if (obj == null) {
                return -1;
            }
            if (obj instanceof ScheduledFutureTask) {
                int i = ((ScheduledFutureTask) obj).heapIndex;
                if (i < 0 || i >= this.size || this.queue[i] != obj) {
                    return -1;
                }
                return i;
            }
            for (int i2 = 0; i2 < this.size; i2++) {
                if (obj.equals(this.queue[i2])) {
                    return i2;
                }
            }
            return -1;
        }

        public boolean contains(Object obj) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return indexOf(obj) != -1;
            } finally {
                reentrantLock.unlock();
            }
        }

        /* JADX INFO: finally extract failed */
        public boolean remove(Object obj) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int indexOf = indexOf(obj);
                if (indexOf < 0) {
                    reentrantLock.unlock();
                    return false;
                }
                setIndex(this.queue[indexOf], -1);
                int i = this.size - 1;
                this.size = i;
                RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.queue;
                RunnableScheduledFuture<?> runnableScheduledFuture = runnableScheduledFutureArr[i];
                runnableScheduledFutureArr[i] = null;
                if (i != indexOf) {
                    siftDown(indexOf, runnableScheduledFuture);
                    if (this.queue[indexOf] == runnableScheduledFuture) {
                        siftUp(indexOf, runnableScheduledFuture);
                    }
                }
                reentrantLock.unlock();
                return true;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        public int size() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return this.size;
            } finally {
                reentrantLock.unlock();
            }
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public RunnableScheduledFuture<?> peek() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return this.queue[0];
            } finally {
                reentrantLock.unlock();
            }
        }

        /* JADX INFO: finally extract failed */
        public boolean offer(Runnable runnable) {
            runnable.getClass();
            RunnableScheduledFuture<?> runnableScheduledFuture = (RunnableScheduledFuture) runnable;
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int i = this.size;
                if (i >= this.queue.length) {
                    grow();
                }
                this.size = i + 1;
                if (i == 0) {
                    this.queue[0] = runnableScheduledFuture;
                    setIndex(runnableScheduledFuture, 0);
                } else {
                    siftUp(i, runnableScheduledFuture);
                }
                if (this.queue[0] == runnableScheduledFuture) {
                    this.leader = null;
                    this.available.signal();
                }
                reentrantLock.unlock();
                return true;
            } catch (Throwable th) {
                reentrantLock.unlock();
                throw th;
            }
        }

        public void put(Runnable runnable) {
            offer(runnable);
        }

        public boolean add(Runnable runnable) {
            return offer(runnable);
        }

        public boolean offer(Runnable runnable, long j, TimeUnit timeUnit) {
            return offer(runnable);
        }

        private RunnableScheduledFuture<?> finishPoll(RunnableScheduledFuture<?> runnableScheduledFuture) {
            int i = this.size - 1;
            this.size = i;
            RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.queue;
            RunnableScheduledFuture<?> runnableScheduledFuture2 = runnableScheduledFutureArr[i];
            runnableScheduledFutureArr[i] = null;
            if (i != 0) {
                siftDown(0, runnableScheduledFuture2);
            }
            setIndex(runnableScheduledFuture, -1);
            return runnableScheduledFuture;
        }

        public RunnableScheduledFuture<?> poll() {
            RunnableScheduledFuture<?> runnableScheduledFuture;
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                RunnableScheduledFuture<?> runnableScheduledFuture2 = this.queue[0];
                if (runnableScheduledFuture2 != null) {
                    if (runnableScheduledFuture2.getDelay(TimeUnit.NANOSECONDS) <= 0) {
                        runnableScheduledFuture = finishPoll(runnableScheduledFuture2);
                        return runnableScheduledFuture;
                    }
                }
                runnableScheduledFuture = null;
                return runnableScheduledFuture;
            } finally {
                reentrantLock.unlock();
            }
        }

        public RunnableScheduledFuture<?> take() throws InterruptedException {
            RunnableScheduledFuture<?> runnableScheduledFuture;
            Thread currentThread;
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lockInterruptibly();
            while (true) {
                try {
                    runnableScheduledFuture = this.queue[0];
                    if (runnableScheduledFuture == null) {
                        this.available.await();
                    } else {
                        long delay = runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS);
                        if (delay <= 0) {
                            break;
                        } else if (this.leader != null) {
                            this.available.await();
                        } else {
                            currentThread = Thread.currentThread();
                            this.leader = currentThread;
                            this.available.awaitNanos(delay);
                            if (this.leader == currentThread) {
                                this.leader = null;
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (this.leader == null && this.queue[0] != null) {
                        this.available.signal();
                    }
                    reentrantLock.unlock();
                    throw th;
                }
            }
            RunnableScheduledFuture<?> finishPoll = finishPoll(runnableScheduledFuture);
            if (this.leader == null && this.queue[0] != null) {
                this.available.signal();
            }
            reentrantLock.unlock();
            return finishPoll;
        }

        public RunnableScheduledFuture<?> poll(long j, TimeUnit timeUnit) throws InterruptedException {
            Thread currentThread;
            long nanos = timeUnit.toNanos(j);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lockInterruptibly();
            while (true) {
                try {
                    RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[0];
                    if (runnableScheduledFuture != null) {
                        long delay = runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS);
                        if (delay <= 0) {
                            RunnableScheduledFuture<?> finishPoll = finishPoll(runnableScheduledFuture);
                            if (this.leader == null && this.queue[0] != null) {
                                this.available.signal();
                            }
                            reentrantLock.unlock();
                            return finishPoll;
                        } else if (nanos <= 0) {
                            if (this.leader == null && this.queue[0] != null) {
                                this.available.signal();
                            }
                            reentrantLock.unlock();
                            return null;
                        } else {
                            if (nanos >= delay) {
                                if (this.leader == null) {
                                    currentThread = Thread.currentThread();
                                    this.leader = currentThread;
                                    nanos -= delay - this.available.awaitNanos(delay);
                                    if (this.leader == currentThread) {
                                        this.leader = null;
                                    }
                                }
                            }
                            nanos = this.available.awaitNanos(nanos);
                        }
                    } else if (nanos <= 0) {
                        if (this.leader == null && runnableScheduledFuture != null) {
                            this.available.signal();
                        }
                        reentrantLock.unlock();
                        return null;
                    } else {
                        nanos = this.available.awaitNanos(nanos);
                    }
                } catch (Throwable th) {
                    if (this.leader == null && this.queue[0] != null) {
                        this.available.signal();
                    }
                    reentrantLock.unlock();
                    throw th;
                }
            }
        }

        public void clear() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            int i = 0;
            while (i < this.size) {
                try {
                    RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.queue;
                    RunnableScheduledFuture<?> runnableScheduledFuture = runnableScheduledFutureArr[i];
                    if (runnableScheduledFuture != null) {
                        runnableScheduledFutureArr[i] = null;
                        setIndex(runnableScheduledFuture, -1);
                    }
                    i++;
                } finally {
                    reentrantLock.unlock();
                }
            }
            this.size = 0;
        }

        public int drainTo(Collection<? super Runnable> collection) {
            return drainTo(collection, Integer.MAX_VALUE);
        }

        public int drainTo(Collection<? super Runnable> collection, int i) {
            Objects.requireNonNull(collection);
            if (collection == this) {
                throw new IllegalArgumentException();
            } else if (i <= 0) {
                return 0;
            } else {
                ReentrantLock reentrantLock = this.lock;
                reentrantLock.lock();
                int i2 = 0;
                while (i2 < i) {
                    try {
                        RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[0];
                        if (runnableScheduledFuture == null || runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS) > 0) {
                            break;
                        }
                        collection.add(runnableScheduledFuture);
                        finishPoll(runnableScheduledFuture);
                        i2++;
                    } catch (Throwable th) {
                        reentrantLock.unlock();
                        throw th;
                    }
                }
                reentrantLock.unlock();
                return i2;
            }
        }

        public Object[] toArray() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return Arrays.copyOf(this.queue, this.size, Object[].class);
            } finally {
                reentrantLock.unlock();
            }
        }

        public <T> T[] toArray(T[] tArr) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int length = tArr.length;
                int i = this.size;
                if (length < i) {
                    return Arrays.copyOf(this.queue, i, tArr.getClass());
                }
                System.arraycopy((Object) this.queue, 0, (Object) tArr, 0, i);
                int length2 = tArr.length;
                int i2 = this.size;
                if (length2 > i2) {
                    tArr[i2] = null;
                }
                reentrantLock.unlock();
                return tArr;
            } finally {
                reentrantLock.unlock();
            }
        }

        public Iterator<Runnable> iterator() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return new Itr((RunnableScheduledFuture[]) Arrays.copyOf((T[]) this.queue, this.size));
            } finally {
                reentrantLock.unlock();
            }
        }

        private class Itr implements Iterator<Runnable> {
            final RunnableScheduledFuture<?>[] array;
            int cursor;
            int lastRet = -1;

            Itr(RunnableScheduledFuture<?>[] runnableScheduledFutureArr) {
                this.array = runnableScheduledFutureArr;
            }

            public boolean hasNext() {
                return this.cursor < this.array.length;
            }

            public Runnable next() {
                int i = this.cursor;
                RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.array;
                if (i < runnableScheduledFutureArr.length) {
                    this.cursor = i + 1;
                    this.lastRet = i;
                    return runnableScheduledFutureArr[i];
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                int i = this.lastRet;
                if (i >= 0) {
                    DelayedWorkQueue.this.remove(this.array[i]);
                    this.lastRet = -1;
                    return;
                }
                throw new IllegalStateException();
            }
        }
    }
}
