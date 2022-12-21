package java.util.concurrent.locks;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ReentrantLock implements Lock, Serializable {
    private static final long serialVersionUID = 7373984872572414699L;
    private final Sync sync;

    static abstract class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        Sync() {
        }

        /* access modifiers changed from: package-private */
        public final boolean nonfairTryAcquire(int i) {
            Thread currentThread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                if (compareAndSetState(0, i)) {
                    setExclusiveOwnerThread(currentThread);
                    return true;
                }
            } else if (currentThread == getExclusiveOwnerThread()) {
                int i2 = state + i;
                if (i2 >= 0) {
                    setState(i2);
                    return true;
                }
                throw new Error("Maximum lock count exceeded");
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public final boolean tryRelease(int i) {
            boolean z;
            int state = getState() - i;
            if (Thread.currentThread() == getExclusiveOwnerThread()) {
                if (state == 0) {
                    setExclusiveOwnerThread((Thread) null);
                    z = true;
                } else {
                    z = false;
                }
                setState(state);
                return z;
            }
            throw new IllegalMonitorStateException();
        }

        /* access modifiers changed from: protected */
        public final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        /* access modifiers changed from: package-private */
        public final AbstractQueuedSynchronizer.ConditionObject newCondition() {
            return new AbstractQueuedSynchronizer.ConditionObject();
        }

        /* access modifiers changed from: package-private */
        public final Thread getOwner() {
            if (getState() == 0) {
                return null;
            }
            return getExclusiveOwnerThread();
        }

        /* access modifiers changed from: package-private */
        public final int getHoldCount() {
            if (isHeldExclusively()) {
                return getState();
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            setState(0);
        }
    }

    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        NonfairSync() {
        }

        /* access modifiers changed from: protected */
        public final boolean tryAcquire(int i) {
            return nonfairTryAcquire(i);
        }
    }

    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        FairSync() {
        }

        /* access modifiers changed from: protected */
        public final boolean tryAcquire(int i) {
            Thread currentThread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, i)) {
                    setExclusiveOwnerThread(currentThread);
                    return true;
                }
            } else if (currentThread == getExclusiveOwnerThread()) {
                int i2 = state + i;
                if (i2 >= 0) {
                    setState(i2);
                    return true;
                }
                throw new Error("Maximum lock count exceeded");
            }
            return false;
        }
    }

    public ReentrantLock() {
        this.sync = new NonfairSync();
    }

    public ReentrantLock(boolean z) {
        this.sync = z ? new FairSync() : new NonfairSync();
    }

    public void lock() {
        this.sync.acquire(1);
    }

    public void lockInterruptibly() throws InterruptedException {
        this.sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return this.sync.nonfairTryAcquire(1);
    }

    public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.sync.tryAcquireNanos(1, timeUnit.toNanos(j));
    }

    public void unlock() {
        this.sync.release(1);
    }

    public Condition newCondition() {
        return this.sync.newCondition();
    }

    public int getHoldCount() {
        return this.sync.getHoldCount();
    }

    public boolean isHeldByCurrentThread() {
        return this.sync.isHeldExclusively();
    }

    public boolean isLocked() {
        return this.sync.isLocked();
    }

    public final boolean isFair() {
        return this.sync instanceof FairSync;
    }

    /* access modifiers changed from: protected */
    public Thread getOwner() {
        return this.sync.getOwner();
    }

    public final boolean hasQueuedThreads() {
        return this.sync.hasQueuedThreads();
    }

    public final boolean hasQueuedThread(Thread thread) {
        return this.sync.isQueued(thread);
    }

    public final int getQueueLength() {
        return this.sync.getQueueLength();
    }

    /* access modifiers changed from: protected */
    public Collection<Thread> getQueuedThreads() {
        return this.sync.getQueuedThreads();
    }

    public boolean hasWaiters(Condition condition) {
        condition.getClass();
        if (condition instanceof AbstractQueuedSynchronizer.ConditionObject) {
            return this.sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject) condition);
        }
        throw new IllegalArgumentException("not owner");
    }

    public int getWaitQueueLength(Condition condition) {
        condition.getClass();
        if (condition instanceof AbstractQueuedSynchronizer.ConditionObject) {
            return this.sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject) condition);
        }
        throw new IllegalArgumentException("not owner");
    }

    /* access modifiers changed from: protected */
    public Collection<Thread> getWaitingThreads(Condition condition) {
        condition.getClass();
        if (condition instanceof AbstractQueuedSynchronizer.ConditionObject) {
            return this.sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject) condition);
        }
        throw new IllegalArgumentException("not owner");
    }

    public String toString() {
        String str;
        Thread owner = this.sync.getOwner();
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (owner == null) {
            str = "[Unlocked]";
        } else {
            str = "[Locked by thread " + owner.getName() + NavigationBarInflaterView.SIZE_MOD_END;
        }
        sb.append(str);
        return sb.toString();
    }
}
