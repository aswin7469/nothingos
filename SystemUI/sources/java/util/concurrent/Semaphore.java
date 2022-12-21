package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.Serializable;
import java.util.Collection;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Semaphore implements Serializable {
    private static final long serialVersionUID = -3222578661600680210L;
    private final Sync sync;

    static abstract class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1192457210091910933L;

        Sync(int i) {
            setState(i);
        }

        /* access modifiers changed from: package-private */
        public final int getPermits() {
            return getState();
        }

        /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: 
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
            	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
            */
        final int nonfairTryAcquireShared(int r3) {
            /*
                r2 = this;
            L_0x0000:
                int r0 = r2.getState()
                int r1 = r0 - r3
                if (r1 < 0) goto L_0x000e
                boolean r0 = r2.compareAndSetState(r0, r1)
                if (r0 == 0) goto L_0x0000
            L_0x000e:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Semaphore.Sync.nonfairTryAcquireShared(int):int");
        }

        /* access modifiers changed from: protected */
        public final boolean tryReleaseShared(int i) {
            int state;
            int i2;
            do {
                state = getState();
                i2 = state + i;
                if (i2 < state) {
                    throw new Error("Maximum permit count exceeded");
                }
            } while (!compareAndSetState(state, i2));
            return true;
        }

        /* access modifiers changed from: package-private */
        public final void reducePermits(int i) {
            int state;
            int i2;
            do {
                state = getState();
                i2 = state - i;
                if (i2 > state) {
                    throw new Error("Permit count underflow");
                }
            } while (!compareAndSetState(state, i2));
        }

        /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: 
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
            	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
            */
        final int drainPermits() {
            /*
                r2 = this;
            L_0x0000:
                int r0 = r2.getState()
                if (r0 == 0) goto L_0x000d
                r1 = 0
                boolean r1 = r2.compareAndSetState(r0, r1)
                if (r1 == 0) goto L_0x0000
            L_0x000d:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Semaphore.Sync.drainPermits():int");
        }
    }

    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        NonfairSync(int i) {
            super(i);
        }

        /* access modifiers changed from: protected */
        public int tryAcquireShared(int i) {
            return nonfairTryAcquireShared(i);
        }
    }

    static final class FairSync extends Sync {
        private static final long serialVersionUID = 2014338818796000944L;

        FairSync(int i) {
            super(i);
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Removed duplicated region for block: B:3:0x0008  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int tryAcquireShared(int r3) {
            /*
                r2 = this;
            L_0x0000:
                boolean r0 = r2.hasQueuedPredecessors()
                if (r0 == 0) goto L_0x0008
                r2 = -1
                return r2
            L_0x0008:
                int r0 = r2.getState()
                int r1 = r0 - r3
                if (r1 < 0) goto L_0x0016
                boolean r0 = r2.compareAndSetState(r0, r1)
                if (r0 == 0) goto L_0x0000
            L_0x0016:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Semaphore.FairSync.tryAcquireShared(int):int");
        }
    }

    public Semaphore(int i) {
        this.sync = new NonfairSync(i);
    }

    public Semaphore(int i, boolean z) {
        this.sync = z ? new FairSync(i) : new NonfairSync(i);
    }

    public void acquire() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    public void acquireUninterruptibly() {
        this.sync.acquireShared(1);
    }

    public boolean tryAcquire() {
        return this.sync.nonfairTryAcquireShared(1) >= 0;
    }

    public boolean tryAcquire(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, timeUnit.toNanos(j));
    }

    public void release() {
        this.sync.releaseShared(1);
    }

    public void acquire(int i) throws InterruptedException {
        if (i >= 0) {
            this.sync.acquireSharedInterruptibly(i);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void acquireUninterruptibly(int i) {
        if (i >= 0) {
            this.sync.acquireShared(i);
            return;
        }
        throw new IllegalArgumentException();
    }

    public boolean tryAcquire(int i) {
        if (i >= 0) {
            return this.sync.nonfairTryAcquireShared(i) >= 0;
        }
        throw new IllegalArgumentException();
    }

    public boolean tryAcquire(int i, long j, TimeUnit timeUnit) throws InterruptedException {
        if (i >= 0) {
            return this.sync.tryAcquireSharedNanos(i, timeUnit.toNanos(j));
        }
        throw new IllegalArgumentException();
    }

    public void release(int i) {
        if (i >= 0) {
            this.sync.releaseShared(i);
            return;
        }
        throw new IllegalArgumentException();
    }

    public int availablePermits() {
        return this.sync.getPermits();
    }

    public int drainPermits() {
        return this.sync.drainPermits();
    }

    /* access modifiers changed from: protected */
    public void reducePermits(int i) {
        if (i >= 0) {
            this.sync.reducePermits(i);
            return;
        }
        throw new IllegalArgumentException();
    }

    public boolean isFair() {
        return this.sync instanceof FairSync;
    }

    public final boolean hasQueuedThreads() {
        return this.sync.hasQueuedThreads();
    }

    public final int getQueueLength() {
        return this.sync.getQueueLength();
    }

    /* access modifiers changed from: protected */
    public Collection<Thread> getQueuedThreads() {
        return this.sync.getQueuedThreads();
    }

    public String toString() {
        return super.toString() + "[Permits = " + this.sync.getPermits() + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
