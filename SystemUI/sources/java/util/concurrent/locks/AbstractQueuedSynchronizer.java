package java.util.concurrent.locks;

import com.android.systemui.biometrics.AuthDialog;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
    private static final VarHandle HEAD;
    static final long SPIN_FOR_TIMEOUT_THRESHOLD = 1000;
    private static final VarHandle STATE;
    private static final VarHandle TAIL;
    private static final long serialVersionUID = 7373984972572414691L;
    private volatile transient Node head;
    private volatile int state;
    private volatile transient Node tail;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private final boolean compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r1, java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetTail(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.initializeSyncQueue():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private final void initializeSyncQueue() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.initializeSyncQueue():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.initializeSyncQueue():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetState(int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    protected final boolean compareAndSetState(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetState(int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetState(int, int):boolean");
    }

    protected AbstractQueuedSynchronizer() {
    }

    static final class Node {
        static final int CANCELLED = 1;
        static final int CONDITION = -2;
        static final Node EXCLUSIVE = null;
        private static final VarHandle NEXT;
        private static final VarHandle PREV;
        static final int PROPAGATE = -3;
        static final Node SHARED = new Node();
        static final int SIGNAL = -1;
        private static final VarHandle THREAD;
        private static final VarHandle WAITSTATUS;
        volatile Node next;
        Node nextWaiter;
        volatile Node prev;
        volatile Thread thread;
        volatile int waitStatus;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.<init>(int):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        Node(int r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.<init>(int):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.<init>(int):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.<init>(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        Node(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.<init>(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.<init>(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.compareAndSetNext(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final boolean compareAndSetNext(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r1, java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.compareAndSetNext(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.compareAndSetNext(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node, java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.compareAndSetWaitStatus(int, int):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final boolean compareAndSetWaitStatus(int r1, int r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.compareAndSetWaitStatus(int, int):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.compareAndSetWaitStatus(int, int):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.setPrevRelaxed(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final void setPrevRelaxed(java.util.concurrent.locks.AbstractQueuedSynchronizer.Node r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.setPrevRelaxed(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.Node.setPrevRelaxed(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void");
        }

        static {
            Class<Node> cls = Node.class;
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                NEXT = lookup.findVarHandle(cls, "next", cls);
                PREV = lookup.findVarHandle(cls, "prev", cls);
                THREAD = lookup.findVarHandle(cls, "thread", Thread.class);
                WAITSTATUS = lookup.findVarHandle(cls, "waitStatus", Integer.TYPE);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }

        /* access modifiers changed from: package-private */
        public final boolean isShared() {
            return this.nextWaiter == SHARED;
        }

        /* access modifiers changed from: package-private */
        public final Node predecessor() {
            Node node = this.prev;
            node.getClass();
            return node;
        }

        Node() {
        }
    }

    /* access modifiers changed from: protected */
    public final int getState() {
        return this.state;
    }

    /* access modifiers changed from: protected */
    public final void setState(int i) {
        this.state = i;
    }

    private Node enq(Node node) {
        while (true) {
            Node node2 = this.tail;
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

    private Node addWaiter(Node node) {
        Node node2 = new Node(node);
        while (true) {
            Node node3 = this.tail;
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

    private void setHead(Node node) {
        this.head = node;
        node.thread = null;
        node.prev = null;
    }

    private void unparkSuccessor(Node node) {
        int i = node.waitStatus;
        if (i < 0) {
            node.compareAndSetWaitStatus(i, 0);
        }
        Node node2 = node.next;
        if (node2 == null || node2.waitStatus > 0) {
            Node node3 = this.tail;
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
            Node node = this.head;
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

    private void setHeadAndPropagate(Node node, int i) {
        Node node2;
        Node node3 = this.head;
        setHead(node);
        if (i > 0 || node3 == null || node3.waitStatus < 0 || (node2 = this.head) == null || node2.waitStatus < 0) {
            Node node4 = node.next;
            if (node4 == null || node4.isShared()) {
                doReleaseShared();
            }
        }
    }

    private void cancelAcquire(Node node) {
        int i;
        if (node != null) {
            node.thread = null;
            Node node2 = node.prev;
            while (node2.waitStatus > 0) {
                node2 = node2.prev;
                node.prev = node2;
            }
            Node node3 = node2.next;
            node.waitStatus = 1;
            if (node != this.tail || !compareAndSetTail(node, node2)) {
                if (node2 == this.head || (((i = node2.waitStatus) != -1 && (i > 0 || !node2.compareAndSetWaitStatus(i, -1))) || node2.thread == null)) {
                    unparkSuccessor(node);
                } else {
                    Node node4 = node.next;
                    if (node4 != null && node4.waitStatus <= 0) {
                        node2.compareAndSetNext(node3, node4);
                    }
                }
                node.next = node;
                return;
            }
            node2.compareAndSetNext(node3, (Node) null);
        }
    }

    private static boolean shouldParkAfterFailedAcquire(Node node, Node node2) {
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
    public final boolean acquireQueued(Node node, int i) {
        boolean z = false;
        while (true) {
            try {
                Node predecessor = node.predecessor();
                if (predecessor == this.head && tryAcquire(i)) {
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

    private void doAcquireInterruptibly(int i) throws InterruptedException {
        Node addWaiter = addWaiter(Node.EXCLUSIVE);
        while (true) {
            try {
                Node predecessor = addWaiter.predecessor();
                if (predecessor == this.head && tryAcquire(i)) {
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

    private boolean doAcquireNanos(int i, long j) throws InterruptedException {
        if (j <= 0) {
            return false;
        }
        long nanoTime = System.nanoTime() + j;
        Node addWaiter = addWaiter(Node.EXCLUSIVE);
        while (true) {
            try {
                Node predecessor = addWaiter.predecessor();
                if (predecessor != this.head || !tryAcquire(i)) {
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

    private void doAcquireShared(int i) {
        Node predecessor;
        int tryAcquireShared;
        Node addWaiter = addWaiter(Node.SHARED);
        boolean z = false;
        while (true) {
            predecessor = addWaiter.predecessor();
            if (predecessor == this.head && (tryAcquireShared = tryAcquireShared(i)) >= 0) {
                break;
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

    private void doAcquireSharedInterruptibly(int i) throws InterruptedException {
        int tryAcquireShared;
        Node addWaiter = addWaiter(Node.SHARED);
        while (true) {
            try {
                Node predecessor = addWaiter.predecessor();
                if (predecessor == this.head && (tryAcquireShared = tryAcquireShared(i)) >= 0) {
                    setHeadAndPropagate(addWaiter, tryAcquireShared);
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

    private boolean doAcquireSharedNanos(int i, long j) throws InterruptedException {
        int tryAcquireShared;
        if (j <= 0) {
            return false;
        }
        long nanoTime = System.nanoTime() + j;
        Node addWaiter = addWaiter(Node.SHARED);
        while (true) {
            try {
                Node predecessor = addWaiter.predecessor();
                if (predecessor != this.head || (tryAcquireShared = tryAcquireShared(i)) < 0) {
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
                    setHeadAndPropagate(addWaiter, tryAcquireShared);
                    predecessor.next = null;
                    return true;
                }
            } finally {
                cancelAcquire(addWaiter);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean tryAcquire(int i) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public boolean tryRelease(int i) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public int tryAcquireShared(int i) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public boolean tryReleaseShared(int i) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    public final void acquire(int i) {
        if (!tryAcquire(i) && acquireQueued(addWaiter(Node.EXCLUSIVE), i)) {
            selfInterrupt();
        }
    }

    public final void acquireInterruptibly(int i) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        } else if (!tryAcquire(i)) {
            doAcquireInterruptibly(i);
        }
    }

    public final boolean tryAcquireNanos(int i, long j) throws InterruptedException {
        if (!Thread.interrupted()) {
            return tryAcquire(i) || doAcquireNanos(i, j);
        }
        throw new InterruptedException();
    }

    public final boolean release(int i) {
        if (!tryRelease(i)) {
            return false;
        }
        Node node = this.head;
        if (node == null || node.waitStatus == 0) {
            return true;
        }
        unparkSuccessor(node);
        return true;
    }

    public final void acquireShared(int i) {
        if (tryAcquireShared(i) < 0) {
            doAcquireShared(i);
        }
    }

    public final void acquireSharedInterruptibly(int i) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        } else if (tryAcquireShared(i) < 0) {
            doAcquireSharedInterruptibly(i);
        }
    }

    public final boolean tryAcquireSharedNanos(int i, long j) throws InterruptedException {
        if (!Thread.interrupted()) {
            return tryAcquireShared(i) >= 0 || doAcquireSharedNanos(i, j);
        }
        throw new InterruptedException();
    }

    public final boolean releaseShared(int i) {
        if (!tryReleaseShared(i)) {
            return false;
        }
        doReleaseShared();
        return true;
    }

    public final boolean hasQueuedThreads() {
        Node node = this.tail;
        Node node2 = this.head;
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
        Node node;
        Node node2;
        Node node3;
        Node node4 = this.head;
        if ((node4 != null && (node3 = node4.next) != null && node3.prev == this.head && (thread = node3.thread) != null) || ((node = this.head) != null && (node2 = node.next) != null && node2.prev == this.head && (thread = node2.thread) != null)) {
            return thread;
        }
        Node node5 = this.tail;
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
        for (Node node = this.tail; node != null; node = node.prev) {
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
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.apparentlyFirstQueuedIsExclusive():boolean");
    }

    public final boolean hasQueuedPredecessors() {
        Node node = this.head;
        if (node == null) {
            return false;
        }
        Node node2 = node.next;
        if (node2 == null || node2.waitStatus > 0) {
            Node node3 = this.tail;
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
        for (Node node = this.tail; node != null; node = node.prev) {
            if (node.thread != null) {
                i++;
            }
        }
        return i;
    }

    public final Collection<Thread> getQueuedThreads() {
        ArrayList arrayList = new ArrayList();
        for (Node node = this.tail; node != null; node = node.prev) {
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
        for (Node node = this.tail; node != null; node = node.prev) {
            if (!node.isShared() && (thread = node.thread) != null) {
                arrayList.add(thread);
            }
        }
        return arrayList;
    }

    public final Collection<Thread> getSharedQueuedThreads() {
        Thread thread;
        ArrayList arrayList = new ArrayList();
        for (Node node = this.tail; node != null; node = node.prev) {
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
    public final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == -2 || node.prev == null) {
            return false;
        }
        if (node.next != null) {
            return true;
        }
        return findNodeFromTail(node);
    }

    private boolean findNodeFromTail(Node node) {
        for (Node node2 = this.tail; node2 != node; node2 = node2.prev) {
            if (node2 == null) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public final boolean transferForSignal(Node node) {
        if (!node.compareAndSetWaitStatus(-2, 0)) {
            return false;
        }
        Node enq = enq(node);
        int i = enq.waitStatus;
        if (i <= 0 && enq.compareAndSetWaitStatus(i, -1)) {
            return true;
        }
        LockSupport.unpark(node.thread);
        return true;
    }

    /* access modifiers changed from: package-private */
    public final boolean transferAfterCancelledWait(Node node) {
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
    public final int fullyRelease(Node node) {
        try {
            int state2 = getState();
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
        private transient Node firstWaiter;
        private transient Node lastWaiter;

        public ConditionObject() {
        }

        private Node addConditionWaiter() {
            if (AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                Node node = this.lastWaiter;
                if (!(node == null || node.waitStatus == -2)) {
                    unlinkCancelledWaiters();
                    node = this.lastWaiter;
                }
                Node node2 = new Node(-2);
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
                java.util.concurrent.locks.AbstractQueuedSynchronizer r0 = java.util.concurrent.locks.AbstractQueuedSynchronizer.this
                boolean r3 = r0.transferForSignal(r3)
                if (r3 != 0) goto L_0x0017
                java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r3 = r2.firstWaiter
                if (r3 != 0) goto L_0x0000
            L_0x0017:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject.doSignal(java.util.concurrent.locks.AbstractQueuedSynchronizer$Node):void");
        }

        private void doSignalAll(Node node) {
            this.firstWaiter = null;
            this.lastWaiter = null;
            while (true) {
                Node node2 = node.nextWaiter;
                node.nextWaiter = null;
                AbstractQueuedSynchronizer.this.transferForSignal(node);
                if (node2 != null) {
                    node = node2;
                } else {
                    return;
                }
            }
        }

        private void unlinkCancelledWaiters() {
            Node node = this.firstWaiter;
            Node node2 = null;
            while (node != null) {
                Node node3 = node.nextWaiter;
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
            if (AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                Node node = this.firstWaiter;
                if (node != null) {
                    doSignal(node);
                    return;
                }
                return;
            }
            throw new IllegalMonitorStateException();
        }

        public final void signalAll() {
            if (AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                Node node = this.firstWaiter;
                if (node != null) {
                    doSignalAll(node);
                    return;
                }
                return;
            }
            throw new IllegalMonitorStateException();
        }

        public final void awaitUninterruptibly() {
            Node addConditionWaiter = addConditionWaiter();
            int fullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(addConditionWaiter);
            boolean z = false;
            while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                LockSupport.park(this);
                if (Thread.interrupted()) {
                    z = true;
                }
            }
            if (AbstractQueuedSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) || z) {
                AbstractQueuedSynchronizer.selfInterrupt();
            }
        }

        private int checkInterruptWhileWaiting(Node node) {
            if (Thread.interrupted()) {
                return AbstractQueuedSynchronizer.this.transferAfterCancelledWait(node) ? -1 : 1;
            }
            return 0;
        }

        private void reportInterruptAfterWait(int i) throws InterruptedException {
            if (i == -1) {
                throw new InterruptedException();
            } else if (i == 1) {
                AbstractQueuedSynchronizer.selfInterrupt();
            }
        }

        public final void await() throws InterruptedException {
            if (!Thread.interrupted()) {
                Node addConditionWaiter = addConditionWaiter();
                int fullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(addConditionWaiter);
                int i = 0;
                while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                    LockSupport.park(this);
                    i = checkInterruptWhileWaiting(addConditionWaiter);
                    if (i != 0) {
                        break;
                    }
                }
                if (AbstractQueuedSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
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
                Node addConditionWaiter = addConditionWaiter();
                int fullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(addConditionWaiter);
                int i = 0;
                long j2 = j;
                while (true) {
                    if (AbstractQueuedSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                        break;
                    } else if (j2 <= 0) {
                        AbstractQueuedSynchronizer.this.transferAfterCancelledWait(addConditionWaiter);
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
                if (AbstractQueuedSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
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
                Node addConditionWaiter = addConditionWaiter();
                int fullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(addConditionWaiter);
                boolean z = false;
                int i = 0;
                while (true) {
                    if (!AbstractQueuedSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                        if (System.currentTimeMillis() < time) {
                            LockSupport.parkUntil(this, time);
                            i = checkInterruptWhileWaiting(addConditionWaiter);
                            if (i != 0) {
                                break;
                            }
                        } else {
                            z = AbstractQueuedSynchronizer.this.transferAfterCancelledWait(addConditionWaiter);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (AbstractQueuedSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
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
                Node addConditionWaiter = addConditionWaiter();
                int fullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(addConditionWaiter);
                boolean z = false;
                int i = 0;
                while (true) {
                    if (AbstractQueuedSynchronizer.this.isOnSyncQueue(addConditionWaiter)) {
                        break;
                    } else if (nanos <= 0) {
                        z = AbstractQueuedSynchronizer.this.transferAfterCancelledWait(addConditionWaiter);
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
                if (AbstractQueuedSynchronizer.this.acquireQueued(addConditionWaiter, fullyRelease) && i != -1) {
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
        public final boolean isOwnedBy(AbstractQueuedSynchronizer abstractQueuedSynchronizer) {
            return abstractQueuedSynchronizer == AbstractQueuedSynchronizer.this;
        }

        /* access modifiers changed from: protected */
        public final boolean hasWaiters() {
            if (AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                for (Node node = this.firstWaiter; node != null; node = node.nextWaiter) {
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
            if (AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                int i = 0;
                for (Node node = this.firstWaiter; node != null; node = node.nextWaiter) {
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
            if (AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                ArrayList arrayList = new ArrayList();
                for (Node node = this.firstWaiter; node != null; node = node.nextWaiter) {
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
        Class<AbstractQueuedSynchronizer> cls = AbstractQueuedSynchronizer.class;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            STATE = lookup.findVarHandle(cls, AuthDialog.KEY_BIOMETRIC_STATE, Integer.TYPE);
            HEAD = lookup.findVarHandle(cls, "head", Node.class);
            TAIL = lookup.findVarHandle(cls, "tail", Node.class);
            Class<LockSupport> cls2 = LockSupport.class;
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }
}
