package java.util.concurrent.locks;

import com.android.systemui.biometrics.AuthDialog;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public abstract class AbstractQueuedLongSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
    private static final VarHandle HEAD;
    static final long SPIN_FOR_TIMEOUT_THRESHOLD = 1000;
    private static final VarHandle STATE;
    private static final VarHandle TAIL;
    private static final long serialVersionUID = 7373984972572414692L;
    private volatile transient AbstractQueuedSynchronizer.Node head;
    private volatile long state;
    private volatile transient AbstractQueuedSynchronizer.Node tail;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 4 more
        */
    private final boolean compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r1, java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.initializeSyncQueue():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 4 more
        */
    private final void initializeSyncQueue() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.initializeSyncQueue():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.initializeSyncQueue():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.compareAndSetState(long, long):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 4 more
        */
    protected final boolean compareAndSetState(long r1, long r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.compareAndSetState(long, long):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.compareAndSetState(long, long):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.setState(long):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 4 more
        */
    protected final void setState(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.setState(long):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.setState(long):void");
    }

    protected AbstractQueuedLongSynchronizer() {
    }

    /* access modifiers changed from: protected */
    public final long getState() {
        return this.state;
    }

    private AbstractQueuedSynchronizer.Node enq(AbstractQueuedSynchronizer.Node node) {
        while (true) {
            AbstractQueuedSynchronizer.Node node2 = this.tail;
            if (node2 != null) {
                node.setPrevRelaxed(node2);
                if (compareAndSetTail(node2, node)) {
                    node2.next = node;
                    return node2;
                }
            } else {
                initializeSyncQueue();
            }
        }
    }

    private AbstractQueuedSynchronizer.Node addWaiter(AbstractQueuedSynchronizer.Node node) {
        AbstractQueuedSynchronizer.Node node2 = new AbstractQueuedSynchronizer.Node(node);
        while (true) {
            AbstractQueuedSynchronizer.Node node3 = this.tail;
            if (node3 != null) {
                node2.setPrevRelaxed(node3);
                if (compareAndSetTail(node3, node2)) {
                    node3.next = node2;
                    return node2;
                }
            } else {
                initializeSyncQueue();
            }
        }
    }

    private void setHead(AbstractQueuedSynchronizer.Node node) {
        this.head = node;
        node.thread = null;
        node.prev = null;
    }

    private void unparkSuccessor(AbstractQueuedSynchronizer.Node node) {
        int i = node.waitStatus;
        if (i < 0) {
            node.compareAndSetWaitStatus(i, 0);
        }
        AbstractQueuedSynchronizer.Node node2 = node.next;
        if (node2 == null || node2.waitStatus > 0) {
            AbstractQueuedSynchronizer.Node node3 = this.tail;
            node2 = null;
            while (node3 != node && node3 != null) {
                if (node3.waitStatus <= 0) {
                    node2 = node3;
                }
                node3 = node3.prev;
            }
        }
        if (node2 != null) {
            LockSupport.unpark(node2.thread);
        }
    }

    private void doReleaseShared() {
        while (true) {
            AbstractQueuedSynchronizer.Node node = this.head;
            if (!(node == null || node == this.tail)) {
                int i = node.waitStatus;
                if (i == -1) {
                    if (!node.compareAndSetWaitStatus(-1, 0)) {
                        continue;
                    } else {
                        unparkSuccessor(node);
                    }
                } else if (i == 0 && !node.compareAndSetWaitStatus(0, -3)) {
                }
            }
            if (node == this.head) {
                return;
            }
        }
    }

    private void setHeadAndPropagate(AbstractQueuedSynchronizer.Node node, long j) {
        AbstractQueuedSynchronizer.Node node2;
        AbstractQueuedSynchronizer.Node node3 = this.head;
        setHead(node);
        if (j > 0 || node3 == null || node3.waitStatus < 0 || (node2 = this.head) == null || node2.waitStatus < 0) {
            AbstractQueuedSynchronizer.Node node4 = node.next;
            if (node4 == null || node4.isShared()) {
                doReleaseShared();
            }
        }
    }

    private void cancelAcquire(AbstractQueuedSynchronizer.Node node) {
        int i;
        if (node != null) {
            node.thread = null;
            AbstractQueuedSynchronizer.Node node2 = node.prev;
            while (node2.waitStatus > 0) {
                node2 = node2.prev;
                node.prev = node2;
            }
            AbstractQueuedSynchronizer.Node node3 = node2.next;
            node.waitStatus = 1;
            if (node != this.tail || !compareAndSetTail(node, node2)) {
                if (node2 == this.head || (((i = node2.waitStatus) != -1 && (i > 0 || !node2.compareAndSetWaitStatus(i, -1))) || node2.thread == null)) {
                    unparkSuccessor(node);
                } else {
                    AbstractQueuedSynchronizer.Node node4 = node.next;
                    if (node4 != null && node4.waitStatus <= 0) {
                        node2.compareAndSetNext(node3, node4);
                    }
                }
                node.next = node;
                return;
            }
            node2.compareAndSetNext(node3, (AbstractQueuedSynchronizer.Node) null);
        }
    }

    private static boolean shouldParkAfterFailedAcquire(AbstractQueuedSynchronizer.Node node, AbstractQueuedSynchronizer.Node node2) {
        int i = node.waitStatus;
        if (i == -1) {
            return true;
        }
        if (i > 0) {
            do {
                node = node.prev;
                node2.prev = node;
            } while (node.waitStatus > 0);
            node.next = node2;
            return false;
        }
        node.compareAndSetWaitStatus(i, -1);
        return false;
    }

    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    /* access modifiers changed from: package-private */
    public final boolean acquireQueued(AbstractQueuedSynchronizer.Node node, long j) {
        boolean z = false;
        while (true) {
            try {
                AbstractQueuedSynchronizer.Node predecessor = node.predecessor();
                if (predecessor == this.head && tryAcquire(j)) {
                    setHead(node);
                    predecessor.next = null;
                    return z;
                } else if (shouldParkAfterFailedAcquire(predecessor, node)) {
                    z |= parkAndCheckInterrupt();
                }
            } catch (Throwable th) {
                cancelAcquire(node);
                if (z) {
                    selfInterrupt();
                }
                throw th;
            }
        }
    }

    private void doAcquireInterruptibly(long j) throws InterruptedException {
        AbstractQueuedSynchronizer.Node addWaiter = addWaiter(AbstractQueuedSynchronizer.Node.EXCLUSIVE);
        while (true) {
            try {
                AbstractQueuedSynchronizer.Node predecessor = addWaiter.predecessor();
                if (predecessor == this.head && tryAcquire(j)) {
                    setHead(addWaiter);
                    predecessor.next = null;
                    return;
                } else if (shouldParkAfterFailedAcquire(predecessor, addWaiter)) {
                    if (parkAndCheckInterrupt()) {
                        throw new InterruptedException();
                    }
                }
            } catch (Throwable th) {
                cancelAcquire(addWaiter);
                throw th;
            }
        }
    }

    private boolean doAcquireNanos(long j, long j2) throws InterruptedException {
        if (j2 <= 0) {
            return false;
        }
        long nanoTime = System.nanoTime() + j2;
        AbstractQueuedSynchronizer.Node addWaiter = addWaiter(AbstractQueuedSynchronizer.Node.EXCLUSIVE);
        while (true) {
            try {
                AbstractQueuedSynchronizer.Node predecessor = addWaiter.predecessor();
                if (predecessor != this.head || !tryAcquire(j)) {
                    long nanoTime2 = nanoTime - System.nanoTime();
                    if (nanoTime2 <= 0) {
                        return false;
                    }
                    if (shouldParkAfterFailedAcquire(predecessor, addWaiter) && nanoTime2 > 1000) {
                        LockSupport.parkNanos(this, nanoTime2);
                    }
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                } else {
                    setHead(addWaiter);
                    predecessor.next = null;
                    return true;
                }
            } finally {
                cancelAcquire(addWaiter);
            }
        }
    }

    private void doAcquireShared(long j) {
        AbstractQueuedSynchronizer.Node predecessor;
        long tryAcquireShared;
        AbstractQueuedSynchronizer.Node addWaiter = addWaiter(AbstractQueuedSynchronizer.Node.SHARED);
        boolean z = false;
        while (true) {
            predecessor = addWaiter.predecessor();
            if (predecessor == this.head) {
                tryAcquireShared = tryAcquireShared(j);
                if (tryAcquireShared >= 0) {
                    break;
                }
            }
            try {
                if (shouldParkAfterFailedAcquire(predecessor, addWaiter)) {
                    z |= parkAndCheckInterrupt();
                }
            } catch (Throwable th) {
                if (z) {
                    selfInterrupt();
                }
                throw th;
            }
        }
        setHeadAndPropagate(addWaiter, tryAcquireShared);
        predecessor.next = null;
        if (z) {
            selfInterrupt();
        }
    }

    private void doAcquireSharedInterruptibly(long j) throws InterruptedException {
        AbstractQueuedSynchronizer.Node addWaiter = addWaiter(AbstractQueuedSynchronizer.Node.SHARED);
        while (true) {
            try {
                AbstractQueuedSynchronizer.Node predecessor = addWaiter.predecessor();
                if (predecessor == this.head) {
                    long tryAcquireShared = tryAcquireShared(j);
                    if (tryAcquireShared >= 0) {
                        setHeadAndPropagate(addWaiter, tryAcquireShared);
                        predecessor.next = null;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(predecessor, addWaiter)) {
                    if (parkAndCheckInterrupt()) {
                        throw new InterruptedException();
                    }
                }
            } catch (Throwable th) {
                cancelAcquire(addWaiter);
                throw th;
            }
        }
    }

    private boolean doAcquireSharedNanos(long j, long j2) throws InterruptedException {
        if (j2 <= 0) {
            return false;
        }
        long nanoTime = System.nanoTime() + j2;
        AbstractQueuedSynchronizer.Node addWaiter = addWaiter(AbstractQueuedSynchronizer.Node.SHARED);
        while (true) {
            try {
                AbstractQueuedSynchronizer.Node predecessor = addWaiter.predecessor();
                if (predecessor == this.head) {
                    long tryAcquireShared = tryAcquireShared(j);
                    if (tryAcquireShared >= 0) {
                        setHeadAndPropagate(addWaiter, tryAcquireShared);
                        predecessor.next = null;
                        return true;
                    }
                }
                long nanoTime2 = nanoTime - System.nanoTime();
                if (nanoTime2 <= 0) {
                    return false;
                }
                if (shouldParkAfterFailedAcquire(predecessor, addWaiter) && nanoTime2 > 1000) {
                    LockSupport.parkNanos(this, nanoTime2);
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            } finally {
                cancelAcquire(addWaiter);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean tryAcquire(long j) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public boolean tryRelease(long j) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public long tryAcquireShared(long j) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public boolean tryReleaseShared(long j) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    public final void acquire(long j) {
        if (!tryAcquire(j) && acquireQueued(addWaiter(AbstractQueuedSynchronizer.Node.EXCLUSIVE), j)) {
            selfInterrupt();
        }
    }

    public final void acquireInterruptibly(long j) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        } else if (!tryAcquire(j)) {
            doAcquireInterruptibly(j);
        }
    }

    public final boolean tryAcquireNanos(long j, long j2) throws InterruptedException {
        if (!Thread.interrupted()) {
            return tryAcquire(j) || doAcquireNanos(j, j2);
        }
        throw new InterruptedException();
    }

    public final boolean release(long j) {
        if (!tryRelease(j)) {
            return false;
        }
        AbstractQueuedSynchronizer.Node node = this.head;
        if (node == null || node.waitStatus == 0) {
            return true;
        }
        unparkSuccessor(node);
        return true;
    }

    public final void acquireShared(long j) {
        if (tryAcquireShared(j) < 0) {
            doAcquireShared(j);
        }
    }

    public final void acquireSharedInterruptibly(long j) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        } else if (tryAcquireShared(j) < 0) {
            doAcquireSharedInterruptibly(j);
        }
    }

    public final boolean tryAcquireSharedNanos(long j, long j2) throws InterruptedException {
        if (!Thread.interrupted()) {
            return tryAcquireShared(j) >= 0 || doAcquireSharedNanos(j, j2);
        }
        throw new InterruptedException();
    }

    public final boolean releaseShared(long j) {
        if (!tryReleaseShared(j)) {
            return false;
        }
        doReleaseShared();
        return true;
    }

    public final boolean hasQueuedThreads() {
        AbstractQueuedSynchronizer.Node node = this.tail;
        AbstractQueuedSynchronizer.Node node2 = this.head;
        while (node != node2 && node != null) {
            if (node.waitStatus <= 0) {
                return true;
            }
            node = node.prev;
        }
        return false;
    }

    public final boolean hasContended() {
        return this.head != null;
    }

    public final Thread getFirstQueuedThread() {
        if (this.head == this.tail) {
            return null;
        }
        return fullGetFirstQueuedThread();
    }

    private Thread fullGetFirstQueuedThread() {
        Thread thread;
        AbstractQueuedSynchronizer.Node node;
        AbstractQueuedSynchronizer.Node node2;
        AbstractQueuedSynchronizer.Node node3;
        AbstractQueuedSynchronizer.Node node4 = this.head;
        if ((node4 != null && (node3 = node4.next) != null && node3.prev == this.head && (thread = node3.thread) != null) || ((node = this.head) != null && (node2 = node.next) != null && node2.prev == this.head && (thread = node2.thread) != null)) {
            return thread;
        }
        AbstractQueuedSynchronizer.Node node5 = this.tail;
        Thread thread2 = null;
        while (node5 != null && node5 != this.head) {
            Thread thread3 = node5.thread;
            if (thread3 != null) {
                thread2 = thread3;
            }
            node5 = node5.prev;
        }
        return thread2;
    }

    public final boolean isQueued(Thread thread) {
        thread.getClass();
        for (AbstractQueuedSynchronizer.Node node = this.tail; node != null; node = node.prev) {
            if (node.thread == thread) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r1 = r1.next;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean apparentlyFirstQueuedIsExclusive() {
        /*
            r1 = this;
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r1 = r1.head
            if (r1 == 0) goto L_0x0014
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r1 = r1.next
            if (r1 == 0) goto L_0x0014
            boolean r0 = r1.isShared()
            if (r0 != 0) goto L_0x0014
            java.lang.Thread r1 = r1.thread
            if (r1 == 0) goto L_0x0014
            r1 = 1
            goto L_0x0015
        L_0x0014:
            r1 = 0
        L_0x0015:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.apparentlyFirstQueuedIsExclusive():boolean");
    }

    public final boolean hasQueuedPredecessors() {
        AbstractQueuedSynchronizer.Node node = this.head;
        if (node == null) {
            return false;
        }
        AbstractQueuedSynchronizer.Node node2 = node.next;
        if (node2 == null || node2.waitStatus > 0) {
            AbstractQueuedSynchronizer.Node node3 = this.tail;
            node2 = null;
            while (node3 != node && node3 != null) {
                if (node3.waitStatus <= 0) {
                    node2 = node3;
                }
                node3 = node3.prev;
            }
        }
        return (node2 == null || node2.thread == Thread.currentThread()) ? false : true;
    }

    public final int getQueueLength() {
        int i = 0;
        for (AbstractQueuedSynchronizer.Node node = this.tail; node != null; node = node.prev) {
            if (node.thread != null) {
                i++;
            }
        }
        return i;
    }

    public final Collection<Thread> getQueuedThreads() {
        ArrayList arrayList = new ArrayList();
        for (AbstractQueuedSynchronizer.Node node = this.tail; node != null; node = node.prev) {
            Thread thread = node.thread;
            if (thread != null) {
                arrayList.add(thread);
            }
        }
        return arrayList;
    }

    public final Collection<Thread> getExclusiveQueuedThreads() {
        Thread thread;
        ArrayList arrayList = new ArrayList();
        for (AbstractQueuedSynchronizer.Node node = this.tail; node != null; node = node.prev) {
            if (!node.isShared() && (thread = node.thread) != null) {
                arrayList.add(thread);
            }
        }
        return arrayList;
    }

    public final Collection<Thread> getSharedQueuedThreads() {
        Thread thread;
        ArrayList arrayList = new ArrayList();
        for (AbstractQueuedSynchronizer.Node node = this.tail; node != null; node = node.prev) {
            if (node.isShared() && (thread = node.thread) != null) {
                arrayList.add(thread);
            }
        }
        return arrayList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("[State = ");
        sb.append(getState());
        sb.append(", ");
        sb.append(hasQueuedThreads() ? "non" : "");
        sb.append("empty queue]");
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public final boolean isOnSyncQueue(AbstractQueuedSynchronizer.Node node) {
        if (node.waitStatus == -2 || node.prev == null) {
            return false;
        }
        if (node.next != null) {
            return true;
        }
        return findNodeFromTail(node);
    }

    private boolean findNodeFromTail(AbstractQueuedSynchronizer.Node node) {
        for (AbstractQueuedSynchronizer.Node node2 = this.tail; node2 != node; node2 = node2.prev) {
            if (node2 == null) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public final boolean transferForSignal(AbstractQueuedSynchronizer.Node node) {
        if (!node.compareAndSetWaitStatus(-2, 0)) {
            return false;
        }
        AbstractQueuedSynchronizer.Node enq = enq(node);
        int i = enq.waitStatus;
        if (i <= 0 && enq.compareAndSetWaitStatus(i, -1)) {
            return true;
        }
        LockSupport.unpark(node.thread);
        return true;
    }

    /* access modifiers changed from: package-private */
    public final boolean transferAfterCancelledWait(AbstractQueuedSynchronizer.Node node) {
        if (node.compareAndSetWaitStatus(-2, 0)) {
            enq(node);
            return true;
        }
        while (!isOnSyncQueue(node)) {
            Thread.yield();
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public final long fullyRelease(AbstractQueuedSynchronizer.Node node) {
        try {
            long state2 = getState();
            if (release(state2)) {
                return state2;
            }
            throw new IllegalMonitorStateException();
        } catch (Throwable th) {
            node.waitStatus = 1;
            throw th;
        }
    }

    public final boolean owns(ConditionObject conditionObject) {
        return conditionObject.isOwnedBy(this);
    }

    public final boolean hasWaiters(ConditionObject conditionObject) {
        if (owns(conditionObject)) {
            return conditionObject.hasWaiters();
        }
        throw new IllegalArgumentException("Not owner");
    }

    public final int getWaitQueueLength(ConditionObject conditionObject) {
        if (owns(conditionObject)) {
            return conditionObject.getWaitQueueLength();
        }
        throw new IllegalArgumentException("Not owner");
    }

    public final Collection<Thread> getWaitingThreads(ConditionObject conditionObject) {
        if (owns(conditionObject)) {
            return conditionObject.getWaitingThreads();
        }
        throw new IllegalArgumentException("Not owner");
    }

    public class ConditionObject implements Condition, Serializable {
        private static final int REINTERRUPT = 1;
        private static final int THROW_IE = -1;
        private static final long serialVersionUID = 1173984872572414699L;
        private transient AbstractQueuedSynchronizer.Node firstWaiter;
        private transient AbstractQueuedSynchronizer.Node lastWaiter;

        public ConditionObject() {
        }

        private AbstractQueuedSynchronizer.Node addConditionWaiter() {
            if (AbstractQueuedLongSynchronizer.this.isHeldExclusively()) {
                AbstractQueuedSynchronizer.Node node = this.lastWaiter;
                if (!(node == null || node.waitStatus == -2)) {
                    unlinkCancelledWaiters();
                    node = this.lastWaiter;
                }
                AbstractQueuedSynchronizer.Node node2 = new AbstractQueuedSynchronizer.Node(-2);
                if (node == null) {
                    this.firstWaiter = node2;
                } else {
                    node.nextWaiter = node2;
                }
                this.lastWaiter = node2;
                return node2;
            }
            throw new IllegalMonitorStateException();
        }

        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK, PHI: r3 
          PHI: (r3v1 java.util.concurrent.locks.AbstractQueuedSynchronizer$Node) = (r3v0 java.util.concurrent.locks.AbstractQueuedSynchronizer$Node), (r3v3 java.util.concurrent.locks.AbstractQueuedSynchronizer$Node) binds: [B:0:0x0000, B:6:0x0015] A[DONT_GENERATE, DONT_INLINE]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void doSignal(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r3) {
            /*
                r2 = this;
            L_0x0000:
                java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r3.nextWaiter
                r2.firstWaiter = r0
                r1 = 0
                if (r0 != 0) goto L_0x0009
                r2.lastWaiter = r1
            L_0x0009:
                r3.nextWaiter = r1
                java.util.concurrent.locks.AbstractQueuedLongSynchronizer r0 = java.util.concurrent.locks.AbstractQueuedLongSynchronizer.this
                boolean r3 = r0.transferForSignal(r3)
                if (r3 != 0) goto L_0x0017
                java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r3 = r2.firstWaiter
                if (r3 != 0) goto L_0x0000
            L_0x0017:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedLongSynchronizer.ConditionObject.doSignal(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void");
        }

        private void doSignalAll(AbstractQueuedSynchronizer.Node node) {
            this.firstWaiter = null;
            this.lastWaiter = null;
            while (true) {
                AbstractQueuedSynchronizer.Node node2 = node.nextWaiter;
                node.nextWaiter = null;
                AbstractQueuedLongSynchronizer.this.transferForSignal(node);
                if (node2 != null) {
                    node = node2;
                } else {
                    return;
                }
            }
        }

        private void unlinkCancelledWaiters() {
            AbstractQueuedSynchronizer.Node node = this.firstWaiter;
            AbstractQueuedSynchronizer.Node node2 = null;
            while (node != null) {
                AbstractQueuedSynchronizer.Node node3 = node.nextWaiter;
                if (node.waitStatus != -2) {
                    node.nextWaiter = null;
                    if (node2 == null) {
                        this.firstWaiter = node3;
                    } else {
                        node2.nextWaiter = node3;
                    }
                    if (node3 == null) {
                        this.lastWaiter = node2;
                    }
                } else {
                    node2 = node;
                }
                node = node3;
            }
        }

        public final void signal() {
            if (AbstractQueuedLongSynchronizer.this.isHeldExclusively()) {
                AbstractQueuedSynchronizer.Node node = this.firstWaiter;
                if (node != null) {
                    doSignal(node);
                    return;
                }
                return;
            }
            throw new IllegalMonitorStateException();
        }

        public final void signalAll() {
            if (AbstractQueuedLongSynchronizer.this.isHeldExclusively()) {
                AbstractQueuedSynchronizer.Node node = this.firstWaiter;
                if (node != null) {
                    doSignalAll(node);
                    return;
                }
                return;
            }
            throw new IllegalMonitorStateException();
        }

        public final void awaitUninterruptibly() {
            AbstractQueuedSynchronizer.Node addConditionWaiter = addConditionWaiter();
            long fullyRelease = AbstractQueuedLongSynchronizer.this.fullyRelease(addConditionWaiter);
            boolean z = false;
            while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                LockSupport.park(this);
                if (Thread.interrupted()) {
                    z = true;
                }
            }
            if (AbstractQueuedLongSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) || z) {
                AbstractQueuedLongSynchronizer.selfInterrupt();
            }
        }

        private int checkInterruptWhileWaiting(AbstractQueuedSynchronizer.Node node) {
            if (Thread.interrupted()) {
                return AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(node) ? -1 : 1;
            }
            return 0;
        }

        private void reportInterruptAfterWait(int i) throws InterruptedException {
            if (i == -1) {
                throw new InterruptedException();
            } else if (i == 1) {
                AbstractQueuedLongSynchronizer.selfInterrupt();
            }
        }

        public final void await() throws InterruptedException {
            if (!Thread.interrupted()) {
                AbstractQueuedSynchronizer.Node addConditionWaiter = addConditionWaiter();
                long fullyRelease = AbstractQueuedLongSynchronizer.this.fullyRelease(addConditionWaiter);
                int i = 0;
                while (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                    LockSupport.park(this);
                    i = checkInterruptWhileWaiting(addConditionWaiter);
                    if (i != 0) {
                        break;
                    }
                }
                if (AbstractQueuedLongSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
                    i = 1;
                }
                if (addConditionWaiter.nextWaiter != null) {
                    unlinkCancelledWaiters();
                }
                if (i != 0) {
                    reportInterruptAfterWait(i);
                    return;
                }
                return;
            }
            throw new InterruptedException();
        }

        public final long awaitNanos(long j) throws InterruptedException {
            if (!Thread.interrupted()) {
                long nanoTime = System.nanoTime() + j;
                AbstractQueuedSynchronizer.Node addConditionWaiter = addConditionWaiter();
                long fullyRelease = AbstractQueuedLongSynchronizer.this.fullyRelease(addConditionWaiter);
                int i = 0;
                long j2 = j;
                while (true) {
                    if (AbstractQueuedLongSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                        break;
                    } else if (j2 <= 0) {
                        AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(addConditionWaiter);
                        break;
                    } else {
                        if (j2 > 1000) {
                            LockSupport.parkNanos(this, j2);
                        }
                        i = checkInterruptWhileWaiting(addConditionWaiter);
                        if (i != 0) {
                            break;
                        }
                        j2 = nanoTime - System.nanoTime();
                    }
                }
                if (AbstractQueuedLongSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
                    i = 1;
                }
                if (addConditionWaiter.nextWaiter != null) {
                    unlinkCancelledWaiters();
                }
                if (i != 0) {
                    reportInterruptAfterWait(i);
                }
                long nanoTime2 = nanoTime - System.nanoTime();
                if (nanoTime2 <= j) {
                    return nanoTime2;
                }
                return Long.MIN_VALUE;
            }
            throw new InterruptedException();
        }

        public final boolean awaitUntil(Date date) throws InterruptedException {
            long time = date.getTime();
            if (!Thread.interrupted()) {
                AbstractQueuedSynchronizer.Node addConditionWaiter = addConditionWaiter();
                long fullyRelease = AbstractQueuedLongSynchronizer.this.fullyRelease(addConditionWaiter);
                boolean z = false;
                int i = 0;
                while (true) {
                    if (!AbstractQueuedLongSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                        if (System.currentTimeMillis() < time) {
                            LockSupport.parkUntil(this, time);
                            i = checkInterruptWhileWaiting(addConditionWaiter);
                            if (i != 0) {
                                break;
                            }
                        } else {
                            z = AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(addConditionWaiter);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (AbstractQueuedLongSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
                    i = 1;
                }
                if (addConditionWaiter.nextWaiter != null) {
                    unlinkCancelledWaiters();
                }
                if (i != 0) {
                    reportInterruptAfterWait(i);
                }
                return !z;
            }
            throw new InterruptedException();
        }

        public final boolean await(long j, TimeUnit timeUnit) throws InterruptedException {
            long nanos = timeUnit.toNanos(j);
            if (!Thread.interrupted()) {
                long nanoTime = System.nanoTime() + nanos;
                AbstractQueuedSynchronizer.Node addConditionWaiter = addConditionWaiter();
                long fullyRelease = AbstractQueuedLongSynchronizer.this.fullyRelease(addConditionWaiter);
                boolean z = false;
                int i = 0;
                while (true) {
                    if (AbstractQueuedLongSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                        break;
                    } else if (nanos <= 0) {
                        z = AbstractQueuedLongSynchronizer.this.transferAfterCancelledWait(addConditionWaiter);
                        break;
                    } else {
                        if (nanos > 1000) {
                            LockSupport.parkNanos(this, nanos);
                        }
                        i = checkInterruptWhileWaiting(addConditionWaiter);
                        if (i != 0) {
                            break;
                        }
                        nanos = nanoTime - System.nanoTime();
                    }
                }
                if (AbstractQueuedLongSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
                    i = 1;
                }
                if (addConditionWaiter.nextWaiter != null) {
                    unlinkCancelledWaiters();
                }
                if (i != 0) {
                    reportInterruptAfterWait(i);
                }
                return !z;
            }
            throw new InterruptedException();
        }

        /* access modifiers changed from: package-private */
        public final boolean isOwnedBy(AbstractQueuedLongSynchronizer abstractQueuedLongSynchronizer) {
            return abstractQueuedLongSynchronizer == AbstractQueuedLongSynchronizer.this;
        }

        /* access modifiers changed from: protected */
        public final boolean hasWaiters() {
            if (AbstractQueuedLongSynchronizer.this.isHeldExclusively()) {
                for (AbstractQueuedSynchronizer.Node node = this.firstWaiter; node != null; node = node.nextWaiter) {
                    if (node.waitStatus == -2) {
                        return true;
                    }
                }
                return false;
            }
            throw new IllegalMonitorStateException();
        }

        /* access modifiers changed from: protected */
        public final int getWaitQueueLength() {
            if (AbstractQueuedLongSynchronizer.this.isHeldExclusively()) {
                int i = 0;
                for (AbstractQueuedSynchronizer.Node node = this.firstWaiter; node != null; node = node.nextWaiter) {
                    if (node.waitStatus == -2) {
                        i++;
                    }
                }
                return i;
            }
            throw new IllegalMonitorStateException();
        }

        /* access modifiers changed from: protected */
        public final Collection<Thread> getWaitingThreads() {
            Thread thread;
            if (AbstractQueuedLongSynchronizer.this.isHeldExclusively()) {
                ArrayList arrayList = new ArrayList();
                for (AbstractQueuedSynchronizer.Node node = this.firstWaiter; node != null; node = node.nextWaiter) {
                    if (node.waitStatus == -2 && (thread = node.thread) != null) {
                        arrayList.add(thread);
                    }
                }
                return arrayList;
            }
            throw new IllegalMonitorStateException();
        }
    }

    static {
        Class<AbstractQueuedLongSynchronizer> cls = AbstractQueuedLongSynchronizer.class;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            STATE = lookup.findVarHandle(cls, AuthDialog.KEY_BIOMETRIC_STATE, Long.TYPE);
            HEAD = lookup.findVarHandle(cls, "head", AbstractQueuedSynchronizer.Node.class);
            TAIL = lookup.findVarHandle(cls, "tail", AbstractQueuedSynchronizer.Node.class);
            Class<LockSupport> cls2 = LockSupport.class;
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
