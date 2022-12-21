package java.util.concurrent.locks;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ReentrantReadWriteLock implements ReadWriteLock, Serializable {
    private static final long serialVersionUID = -6992448646407690164L;
    private final ReadLock readerLock;
    final Sync sync;
    private final WriteLock writerLock;

    public ReentrantReadWriteLock() {
        this(false);
    }

    public ReentrantReadWriteLock(boolean z) {
        this.sync = z ? new FairSync() : new NonfairSync();
        this.readerLock = new ReadLock(this);
        this.writerLock = new WriteLock(this);
    }

    public WriteLock writeLock() {
        return this.writerLock;
    }

    public ReadLock readLock() {
        return this.readerLock;
    }

    static abstract class Sync extends AbstractQueuedSynchronizer {
        static final int EXCLUSIVE_MASK = 65535;
        static final int MAX_COUNT = 65535;
        static final int SHARED_SHIFT = 16;
        static final int SHARED_UNIT = 65536;
        private static final long serialVersionUID = 6317671515068378041L;
        private transient HoldCounter cachedHoldCounter;
        private transient Thread firstReader;
        private transient int firstReaderHoldCount;
        private transient ThreadLocalHoldCounter readHolds = new ThreadLocalHoldCounter();

        static int exclusiveCount(int i) {
            return i & 65535;
        }

        static int sharedCount(int i) {
            return i >>> 16;
        }

        /* access modifiers changed from: package-private */
        public abstract boolean readerShouldBlock();

        /* access modifiers changed from: package-private */
        public abstract boolean writerShouldBlock();

        static final class HoldCounter {
            int count;
            final long tid = LockSupport.getThreadId(Thread.currentThread());

            HoldCounter() {
            }
        }

        static final class ThreadLocalHoldCounter extends ThreadLocal<HoldCounter> {
            ThreadLocalHoldCounter() {
            }

            public HoldCounter initialValue() {
                return new HoldCounter();
            }
        }

        Sync() {
            setState(getState());
        }

        /* access modifiers changed from: protected */
        public final boolean tryRelease(int i) {
            if (isHeldExclusively()) {
                int state = getState() - i;
                boolean z = exclusiveCount(state) == 0;
                if (z) {
                    setExclusiveOwnerThread((Thread) null);
                }
                setState(state);
                return z;
            }
            throw new IllegalMonitorStateException();
        }

        /* access modifiers changed from: protected */
        public final boolean tryAcquire(int i) {
            Thread currentThread = Thread.currentThread();
            int state = getState();
            int exclusiveCount = exclusiveCount(state);
            if (state != 0) {
                if (exclusiveCount == 0 || currentThread != getExclusiveOwnerThread()) {
                    return false;
                }
                if (exclusiveCount + exclusiveCount(i) <= 65535) {
                    setState(state + i);
                    return true;
                }
                throw new Error("Maximum lock count exceeded");
            } else if (writerShouldBlock() || !compareAndSetState(state, i + state)) {
                return false;
            } else {
                setExclusiveOwnerThread(currentThread);
                return true;
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter} */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean tryReleaseShared(int r7) {
            /*
                r6 = this;
                java.lang.Thread r7 = java.lang.Thread.currentThread()
                java.lang.Thread r0 = r6.firstReader
                r1 = 1
                if (r0 != r7) goto L_0x0015
                int r7 = r6.firstReaderHoldCount
                if (r7 != r1) goto L_0x0011
                r7 = 0
                r6.firstReader = r7
                goto L_0x0042
            L_0x0011:
                int r7 = r7 - r1
                r6.firstReaderHoldCount = r7
                goto L_0x0042
            L_0x0015:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r0 = r6.cachedHoldCounter
                if (r0 == 0) goto L_0x0023
                long r2 = r0.tid
                long r4 = java.util.concurrent.locks.LockSupport.getThreadId(r7)
                int r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r7 == 0) goto L_0x002c
            L_0x0023:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r7 = r6.readHolds
                java.lang.Object r7 = r7.get()
                r0 = r7
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r0 = (java.util.concurrent.locks.ReentrantReadWriteLock.Sync.HoldCounter) r0
            L_0x002c:
                int r7 = r0.count
                if (r7 > r1) goto L_0x003d
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r2 = r6.readHolds
                r2.remove()
                if (r7 <= 0) goto L_0x0038
                goto L_0x003d
            L_0x0038:
                java.lang.IllegalMonitorStateException r6 = unmatchedUnlockException()
                throw r6
            L_0x003d:
                int r7 = r0.count
                int r7 = r7 - r1
                r0.count = r7
            L_0x0042:
                int r7 = r6.getState()
                r0 = 65536(0x10000, float:9.18355E-41)
                int r0 = r7 - r0
                boolean r7 = r6.compareAndSetState(r7, r0)
                if (r7 == 0) goto L_0x0042
                if (r0 != 0) goto L_0x0053
                goto L_0x0054
            L_0x0053:
                r1 = 0
            L_0x0054:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.ReentrantReadWriteLock.Sync.tryReleaseShared(int):boolean");
        }

        private static IllegalMonitorStateException unmatchedUnlockException() {
            return new IllegalMonitorStateException("attempt to unlock read lock, not locked by current thread");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v3, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter} */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final int tryAcquireShared(int r7) {
            /*
                r6 = this;
                java.lang.Thread r7 = java.lang.Thread.currentThread()
                int r0 = r6.getState()
                int r1 = exclusiveCount(r0)
                if (r1 == 0) goto L_0x0016
                java.lang.Thread r1 = r6.getExclusiveOwnerThread()
                if (r1 == r7) goto L_0x0016
                r6 = -1
                return r6
            L_0x0016:
                int r1 = sharedCount(r0)
                boolean r2 = r6.readerShouldBlock()
                if (r2 != 0) goto L_0x006a
                r2 = 65535(0xffff, float:9.1834E-41)
                if (r1 >= r2) goto L_0x006a
                r2 = 65536(0x10000, float:9.18355E-41)
                int r2 = r2 + r0
                boolean r0 = r6.compareAndSetState(r0, r2)
                if (r0 == 0) goto L_0x006a
                r0 = 1
                if (r1 != 0) goto L_0x0036
                r6.firstReader = r7
                r6.firstReaderHoldCount = r0
                goto L_0x0069
            L_0x0036:
                java.lang.Thread r1 = r6.firstReader
                if (r1 != r7) goto L_0x0040
                int r7 = r6.firstReaderHoldCount
                int r7 = r7 + r0
                r6.firstReaderHoldCount = r7
                goto L_0x0069
            L_0x0040:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r1 = r6.cachedHoldCounter
                if (r1 == 0) goto L_0x0059
                long r2 = r1.tid
                long r4 = java.util.concurrent.locks.LockSupport.getThreadId(r7)
                int r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                if (r7 == 0) goto L_0x004f
                goto L_0x0059
            L_0x004f:
                int r7 = r1.count
                if (r7 != 0) goto L_0x0064
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r6 = r6.readHolds
                r6.set(r1)
                goto L_0x0064
            L_0x0059:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r7 = r6.readHolds
                java.lang.Object r7 = r7.get()
                r1 = r7
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r1 = (java.util.concurrent.locks.ReentrantReadWriteLock.Sync.HoldCounter) r1
                r6.cachedHoldCounter = r1
            L_0x0064:
                int r6 = r1.count
                int r6 = r6 + r0
                r1.count = r6
            L_0x0069:
                return r0
            L_0x006a:
                int r6 = r6.fullTryAcquireShared(r7)
                return r6
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.ReentrantReadWriteLock.Sync.tryAcquireShared(int):int");
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v5, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter} */
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final int fullTryAcquireShared(java.lang.Thread r9) {
            /*
                r8 = this;
                r0 = 0
            L_0x0001:
                int r1 = r8.getState()
                int r2 = exclusiveCount(r1)
                r3 = -1
                if (r2 == 0) goto L_0x0013
                java.lang.Thread r2 = r8.getExclusiveOwnerThread()
                if (r2 == r9) goto L_0x0044
                return r3
            L_0x0013:
                boolean r2 = r8.readerShouldBlock()
                if (r2 == 0) goto L_0x0044
                java.lang.Thread r2 = r8.firstReader
                if (r2 != r9) goto L_0x001e
                goto L_0x0044
            L_0x001e:
                if (r0 != 0) goto L_0x003f
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r0 = r8.cachedHoldCounter
                if (r0 == 0) goto L_0x002e
                long r4 = r0.tid
                long r6 = java.util.concurrent.locks.LockSupport.getThreadId(r9)
                int r2 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r2 == 0) goto L_0x003f
            L_0x002e:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r0 = r8.readHolds
                java.lang.Object r0 = r0.get()
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r0 = (java.util.concurrent.locks.ReentrantReadWriteLock.Sync.HoldCounter) r0
                int r2 = r0.count
                if (r2 != 0) goto L_0x003f
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r2 = r8.readHolds
                r2.remove()
            L_0x003f:
                int r2 = r0.count
                if (r2 != 0) goto L_0x0044
                return r3
            L_0x0044:
                int r2 = sharedCount(r1)
                r3 = 65535(0xffff, float:9.1834E-41)
                if (r2 == r3) goto L_0x0098
                r2 = 65536(0x10000, float:9.18355E-41)
                int r2 = r2 + r1
                boolean r2 = r8.compareAndSetState(r1, r2)
                if (r2 == 0) goto L_0x0001
                int r1 = sharedCount(r1)
                r2 = 1
                if (r1 != 0) goto L_0x0062
                r8.firstReader = r9
                r8.firstReaderHoldCount = r2
                goto L_0x0097
            L_0x0062:
                java.lang.Thread r1 = r8.firstReader
                if (r1 != r9) goto L_0x006c
                int r9 = r8.firstReaderHoldCount
                int r9 = r9 + r2
                r8.firstReaderHoldCount = r9
                goto L_0x0097
            L_0x006c:
                if (r0 != 0) goto L_0x0070
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r0 = r8.cachedHoldCounter
            L_0x0070:
                if (r0 == 0) goto L_0x0087
                long r3 = r0.tid
                long r5 = java.util.concurrent.locks.LockSupport.getThreadId(r9)
                int r9 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r9 == 0) goto L_0x007d
                goto L_0x0087
            L_0x007d:
                int r9 = r0.count
                if (r9 != 0) goto L_0x0090
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r9 = r8.readHolds
                r9.set(r0)
                goto L_0x0090
            L_0x0087:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r9 = r8.readHolds
                java.lang.Object r9 = r9.get()
                r0 = r9
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r0 = (java.util.concurrent.locks.ReentrantReadWriteLock.Sync.HoldCounter) r0
            L_0x0090:
                int r9 = r0.count
                int r9 = r9 + r2
                r0.count = r9
                r8.cachedHoldCounter = r0
            L_0x0097:
                return r2
            L_0x0098:
                java.lang.Error r8 = new java.lang.Error
                java.lang.String r9 = "Maximum lock count exceeded"
                r8.<init>((java.lang.String) r9)
                throw r8
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.ReentrantReadWriteLock.Sync.fullTryAcquireShared(java.lang.Thread):int");
        }

        /* access modifiers changed from: package-private */
        public final boolean tryWriteLock() {
            Thread currentThread = Thread.currentThread();
            int state = getState();
            if (state != 0) {
                int exclusiveCount = exclusiveCount(state);
                if (exclusiveCount == 0 || currentThread != getExclusiveOwnerThread()) {
                    return false;
                }
                if (exclusiveCount == 65535) {
                    throw new Error("Maximum lock count exceeded");
                }
            }
            if (!compareAndSetState(state, state + 1)) {
                return false;
            }
            setExclusiveOwnerThread(currentThread);
            return true;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter} */
        /* access modifiers changed from: package-private */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean tryReadLock() {
            /*
                r7 = this;
                java.lang.Thread r0 = java.lang.Thread.currentThread()
            L_0x0004:
                int r1 = r7.getState()
                int r2 = exclusiveCount(r1)
                if (r2 == 0) goto L_0x0016
                java.lang.Thread r2 = r7.getExclusiveOwnerThread()
                if (r2 == r0) goto L_0x0016
                r7 = 0
                return r7
            L_0x0016:
                int r2 = sharedCount(r1)
                r3 = 65535(0xffff, float:9.1834E-41)
                if (r2 == r3) goto L_0x0064
                r3 = 65536(0x10000, float:9.18355E-41)
                int r3 = r3 + r1
                boolean r1 = r7.compareAndSetState(r1, r3)
                if (r1 == 0) goto L_0x0004
                r1 = 1
                if (r2 != 0) goto L_0x0030
                r7.firstReader = r0
                r7.firstReaderHoldCount = r1
                goto L_0x0063
            L_0x0030:
                java.lang.Thread r2 = r7.firstReader
                if (r2 != r0) goto L_0x003a
                int r0 = r7.firstReaderHoldCount
                int r0 = r0 + r1
                r7.firstReaderHoldCount = r0
                goto L_0x0063
            L_0x003a:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r2 = r7.cachedHoldCounter
                if (r2 == 0) goto L_0x0053
                long r3 = r2.tid
                long r5 = java.util.concurrent.locks.LockSupport.getThreadId(r0)
                int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
                if (r0 == 0) goto L_0x0049
                goto L_0x0053
            L_0x0049:
                int r0 = r2.count
                if (r0 != 0) goto L_0x005e
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r7 = r7.readHolds
                r7.set(r2)
                goto L_0x005e
            L_0x0053:
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$ThreadLocalHoldCounter r0 = r7.readHolds
                java.lang.Object r0 = r0.get()
                r2 = r0
                java.util.concurrent.locks.ReentrantReadWriteLock$Sync$HoldCounter r2 = (java.util.concurrent.locks.ReentrantReadWriteLock.Sync.HoldCounter) r2
                r7.cachedHoldCounter = r2
            L_0x005e:
                int r7 = r2.count
                int r7 = r7 + r1
                r2.count = r7
            L_0x0063:
                return r1
            L_0x0064:
                java.lang.Error r7 = new java.lang.Error
                java.lang.String r0 = "Maximum lock count exceeded"
                r7.<init>((java.lang.String) r0)
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.ReentrantReadWriteLock.Sync.tryReadLock():boolean");
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
            if (exclusiveCount(getState()) == 0) {
                return null;
            }
            return getExclusiveOwnerThread();
        }

        /* access modifiers changed from: package-private */
        public final int getReadLockCount() {
            return sharedCount(getState());
        }

        /* access modifiers changed from: package-private */
        public final boolean isWriteLocked() {
            return exclusiveCount(getState()) != 0;
        }

        /* access modifiers changed from: package-private */
        public final int getWriteHoldCount() {
            if (isHeldExclusively()) {
                return exclusiveCount(getState());
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public final int getReadHoldCount() {
            if (getReadLockCount() == 0) {
                return 0;
            }
            Thread currentThread = Thread.currentThread();
            if (this.firstReader == currentThread) {
                return this.firstReaderHoldCount;
            }
            HoldCounter holdCounter = this.cachedHoldCounter;
            if (holdCounter != null && holdCounter.tid == LockSupport.getThreadId(currentThread)) {
                return holdCounter.count;
            }
            int i = ((HoldCounter) this.readHolds.get()).count;
            if (i == 0) {
                this.readHolds.remove();
            }
            return i;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            this.readHolds = new ThreadLocalHoldCounter();
            setState(0);
        }

        /* access modifiers changed from: package-private */
        public final int getCount() {
            return getState();
        }
    }

    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -8159625535654395037L;

        /* access modifiers changed from: package-private */
        public final boolean writerShouldBlock() {
            return false;
        }

        NonfairSync() {
        }

        /* access modifiers changed from: package-private */
        public final boolean readerShouldBlock() {
            return apparentlyFirstQueuedIsExclusive();
        }
    }

    static final class FairSync extends Sync {
        private static final long serialVersionUID = -2274990926593161451L;

        FairSync() {
        }

        /* access modifiers changed from: package-private */
        public final boolean writerShouldBlock() {
            return hasQueuedPredecessors();
        }

        /* access modifiers changed from: package-private */
        public final boolean readerShouldBlock() {
            return hasQueuedPredecessors();
        }
    }

    public static class ReadLock implements Lock, Serializable {
        private static final long serialVersionUID = -5992448646407690164L;
        private final Sync sync;

        protected ReadLock(ReentrantReadWriteLock reentrantReadWriteLock) {
            this.sync = reentrantReadWriteLock.sync;
        }

        public void lock() {
            this.sync.acquireShared(1);
        }

        public void lockInterruptibly() throws InterruptedException {
            this.sync.acquireSharedInterruptibly(1);
        }

        public boolean tryLock() {
            return this.sync.tryReadLock();
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            return this.sync.tryAcquireSharedNanos(1, timeUnit.toNanos(j));
        }

        public void unlock() {
            this.sync.releaseShared(1);
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            int readLockCount = this.sync.getReadLockCount();
            return super.toString() + "[Read locks = " + readLockCount + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    public static class WriteLock implements Lock, Serializable {
        private static final long serialVersionUID = -4992448646407690164L;
        private final Sync sync;

        protected WriteLock(ReentrantReadWriteLock reentrantReadWriteLock) {
            this.sync = reentrantReadWriteLock.sync;
        }

        public void lock() {
            this.sync.acquire(1);
        }

        public void lockInterruptibly() throws InterruptedException {
            this.sync.acquireInterruptibly(1);
        }

        public boolean tryLock() {
            return this.sync.tryWriteLock();
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

        public boolean isHeldByCurrentThread() {
            return this.sync.isHeldExclusively();
        }

        public int getHoldCount() {
            return this.sync.getWriteHoldCount();
        }
    }

    public final boolean isFair() {
        return this.sync instanceof FairSync;
    }

    /* access modifiers changed from: protected */
    public Thread getOwner() {
        return this.sync.getOwner();
    }

    public int getReadLockCount() {
        return this.sync.getReadLockCount();
    }

    public boolean isWriteLocked() {
        return this.sync.isWriteLocked();
    }

    public boolean isWriteLockedByCurrentThread() {
        return this.sync.isHeldExclusively();
    }

    public int getWriteHoldCount() {
        return this.sync.getWriteHoldCount();
    }

    public int getReadHoldCount() {
        return this.sync.getReadHoldCount();
    }

    /* access modifiers changed from: protected */
    public Collection<Thread> getQueuedWriterThreads() {
        return this.sync.getExclusiveQueuedThreads();
    }

    /* access modifiers changed from: protected */
    public Collection<Thread> getQueuedReaderThreads() {
        return this.sync.getSharedQueuedThreads();
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
        int count = this.sync.getCount();
        int exclusiveCount = Sync.exclusiveCount(count);
        int sharedCount = Sync.sharedCount(count);
        return super.toString() + "[Write locks = " + exclusiveCount + ", Read locks = " + sharedCount + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
