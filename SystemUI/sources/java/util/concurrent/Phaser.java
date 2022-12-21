package java.util.concurrent;

import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class Phaser {
    private static final long COUNTS_MASK = 4294967295L;
    private static final int EMPTY = 1;
    private static final int MAX_PARTIES = 65535;
    private static final int MAX_PHASE = Integer.MAX_VALUE;
    private static final int NCPU;
    private static final int ONE_ARRIVAL = 1;
    private static final int ONE_DEREGISTER = 65537;
    private static final int ONE_PARTY = 65536;
    private static final long PARTIES_MASK = 4294901760L;
    private static final int PARTIES_SHIFT = 16;
    private static final int PHASE_SHIFT = 32;
    static final int SPINS_PER_ARRIVAL;
    private static final VarHandle STATE;
    private static final long TERMINATION_BIT = Long.MIN_VALUE;
    private static final int UNARRIVED_MASK = 65535;
    private final AtomicReference<QNode> evenQ;
    private final AtomicReference<QNode> oddQ;
    private final Phaser parent;
    private final Phaser root;
    private volatile long state;

    private static int arrivedOf(long j) {
        int i = (int) j;
        if (i == 1) {
            return 0;
        }
        return (i >>> 16) - (i & 65535);
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.doArrive(int):int, dex: classes4.dex
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
    private int doArrive(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.doArrive(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Phaser.doArrive(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.doRegister(int):int, dex: classes4.dex
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
    private int doRegister(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.doRegister(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Phaser.doRegister(int):int");
    }

    private static int partiesOf(long j) {
        return ((int) j) >>> 16;
    }

    private static int phaseOf(long j) {
        return (int) (j >>> 32);
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.reconcileState():long, dex: classes4.dex
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
    private long reconcileState() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.reconcileState():long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Phaser.reconcileState():long");
    }

    private static int unarrivedOf(long j) {
        int i = (int) j;
        if (i == 1) {
            return 0;
        }
        return i & 65535;
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.arriveAndAwaitAdvance():int, dex: classes4.dex
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
    public int arriveAndAwaitAdvance() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.arriveAndAwaitAdvance():int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Phaser.arriveAndAwaitAdvance():int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.forceTermination():void, dex: classes4.dex
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
    public void forceTermination() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.Phaser.forceTermination():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Phaser.forceTermination():void");
    }

    /* access modifiers changed from: protected */
    public boolean onAdvance(int i, int i2) {
        return i2 == 0;
    }

    private String badArrive(long j) {
        return "Attempted arrival of unregistered party for " + stateToString(j);
    }

    private String badRegister(long j) {
        return "Attempt to register more than 65535 parties for " + stateToString(j);
    }

    public Phaser() {
        this((Phaser) null, 0);
    }

    public Phaser(int i) {
        this((Phaser) null, i);
    }

    public Phaser(Phaser phaser) {
        this(phaser, 0);
    }

    public Phaser(Phaser phaser, int i) {
        long j;
        if ((i >>> 16) == 0) {
            this.parent = phaser;
            int i2 = 0;
            if (phaser != null) {
                Phaser phaser2 = phaser.root;
                this.root = phaser2;
                this.evenQ = phaser2.evenQ;
                this.oddQ = phaser2.oddQ;
                if (i != 0) {
                    i2 = phaser.doRegister(1);
                }
            } else {
                this.root = this;
                this.evenQ = new AtomicReference<>();
                this.oddQ = new AtomicReference<>();
            }
            if (i == 0) {
                j = 1;
            } else {
                long j2 = (long) i;
                j = j2 | (((long) i2) << 32) | (j2 << 16);
            }
            this.state = j;
            return;
        }
        throw new IllegalArgumentException("Illegal number of parties");
    }

    public int register() {
        return doRegister(1);
    }

    public int bulkRegister(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        } else if (i == 0) {
            return getPhase();
        } else {
            return doRegister(i);
        }
    }

    public int arrive() {
        return doArrive(1);
    }

    public int arriveAndDeregister() {
        return doArrive(ONE_DEREGISTER);
    }

    public int awaitAdvance(int i) {
        Phaser phaser = this.root;
        int reconcileState = (int) ((phaser == this ? this.state : reconcileState()) >>> 32);
        if (i < 0) {
            return i;
        }
        return reconcileState == i ? phaser.internalAwaitAdvance(i, (QNode) null) : reconcileState;
    }

    public int awaitAdvanceInterruptibly(int i) throws InterruptedException {
        Phaser phaser = this.root;
        int reconcileState = (int) ((phaser == this ? this.state : reconcileState()) >>> 32);
        if (i < 0) {
            return i;
        }
        if (reconcileState != i) {
            return reconcileState;
        }
        QNode qNode = new QNode(this, i, true, false, 0);
        int internalAwaitAdvance = phaser.internalAwaitAdvance(i, qNode);
        if (!qNode.wasInterrupted) {
            return internalAwaitAdvance;
        }
        throw new InterruptedException();
    }

    public int awaitAdvanceInterruptibly(int i, long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        long nanos = timeUnit.toNanos(j);
        Phaser phaser = this.root;
        int reconcileState = (int) ((phaser == this ? this.state : reconcileState()) >>> 32);
        if (i < 0) {
            return i;
        }
        if (reconcileState != i) {
            return reconcileState;
        }
        QNode qNode = new QNode(this, i, true, true, nanos);
        int internalAwaitAdvance = phaser.internalAwaitAdvance(i, qNode);
        if (qNode.wasInterrupted) {
            throw new InterruptedException();
        } else if (internalAwaitAdvance != i) {
            return internalAwaitAdvance;
        } else {
            throw new TimeoutException();
        }
    }

    public final int getPhase() {
        return (int) (this.root.state >>> 32);
    }

    public int getRegisteredParties() {
        return partiesOf(this.state);
    }

    public int getArrivedParties() {
        return arrivedOf(reconcileState());
    }

    public int getUnarrivedParties() {
        return unarrivedOf(reconcileState());
    }

    public Phaser getParent() {
        return this.parent;
    }

    public Phaser getRoot() {
        return this.root;
    }

    public boolean isTerminated() {
        return this.root.state < 0;
    }

    public String toString() {
        return stateToString(reconcileState());
    }

    private String stateToString(long j) {
        return super.toString() + "[phase = " + phaseOf(j) + " parties = " + partiesOf(j) + " arrived = " + arrivedOf(j) + NavigationBarInflaterView.SIZE_MOD_END;
    }

    private void releaseWaiters(int i) {
        Thread thread;
        AtomicReference<QNode> atomicReference = (i & 1) == 0 ? this.evenQ : this.oddQ;
        while (true) {
            QNode qNode = atomicReference.get();
            if (qNode != null && qNode.phase != ((int) (this.root.state >>> 32))) {
                if (atomicReference.compareAndSet(qNode, qNode.next) && (thread = qNode.thread) != null) {
                    qNode.thread = null;
                    LockSupport.unpark(thread);
                }
            } else {
                return;
            }
        }
    }

    private int abortWait(int i) {
        int i2;
        Thread thread;
        AtomicReference<QNode> atomicReference = (i & 1) == 0 ? this.evenQ : this.oddQ;
        while (true) {
            QNode qNode = atomicReference.get();
            i2 = (int) (this.root.state >>> 32);
            if (qNode == null || ((thread = qNode.thread) != null && qNode.phase == i2)) {
                return i2;
            }
            if (atomicReference.compareAndSet(qNode, qNode.next) && thread != null) {
                qNode.thread = null;
                LockSupport.unpark(thread);
            }
        }
        return i2;
    }

    static {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        NCPU = availableProcessors;
        SPINS_PER_ARRIVAL = availableProcessors < 2 ? 1 : 256;
        try {
            STATE = MethodHandles.lookup().findVarHandle(Phaser.class, AuthDialog.KEY_BIOMETRIC_STATE, Long.TYPE);
            Class<LockSupport> cls = LockSupport.class;
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    private int internalAwaitAdvance(int i, QNode qNode) {
        int i2;
        releaseWaiters(i - 1);
        int i3 = SPINS_PER_ARRIVAL;
        int i4 = 0;
        boolean z = false;
        while (true) {
            long j = this.state;
            i2 = (int) (j >>> 32);
            if (i2 != i) {
                break;
            } else if (qNode == null) {
                int i5 = ((int) j) & 65535;
                if (i5 != i4) {
                    if (i5 < NCPU) {
                        i3 += SPINS_PER_ARRIVAL;
                    }
                    i4 = i5;
                }
                boolean interrupted = Thread.interrupted();
                if (interrupted || i3 - 1 < 0) {
                    qNode = new QNode(this, i, false, false, 0);
                    qNode.wasInterrupted = interrupted;
                }
            } else if (qNode.isReleasable()) {
                break;
            } else if (!z) {
                AtomicReference<QNode> atomicReference = (i & 1) == 0 ? this.evenQ : this.oddQ;
                QNode qNode2 = atomicReference.get();
                qNode.next = qNode2;
                if ((qNode2 == null || qNode2.phase == i) && ((int) (this.state >>> 32)) == i) {
                    z = atomicReference.compareAndSet(qNode2, qNode);
                }
            } else {
                try {
                    ForkJoinPool.managedBlock(qNode);
                } catch (InterruptedException unused) {
                    qNode.wasInterrupted = true;
                }
            }
        }
        if (qNode != null) {
            if (qNode.thread != null) {
                qNode.thread = null;
            }
            if (qNode.wasInterrupted && !qNode.interruptible) {
                Thread.currentThread().interrupt();
            }
            if (i2 == i && (i2 = (int) (this.state >>> 32)) == i) {
                return abortWait(i);
            }
        }
        releaseWaiters(i);
        return i2;
    }

    static final class QNode implements ForkJoinPool.ManagedBlocker {
        final long deadline;
        final boolean interruptible;
        long nanos;
        QNode next;
        final int phase;
        final Phaser phaser;
        volatile Thread thread;
        final boolean timed;
        boolean wasInterrupted;

        QNode(Phaser phaser2, int i, boolean z, boolean z2, long j) {
            this.phaser = phaser2;
            this.phase = i;
            this.interruptible = z;
            this.nanos = j;
            this.timed = z2;
            this.deadline = z2 ? System.nanoTime() + j : 0;
            this.thread = Thread.currentThread();
        }

        public boolean isReleasable() {
            if (this.thread == null) {
                return true;
            }
            if (this.phaser.getPhase() != this.phase) {
                this.thread = null;
                return true;
            }
            if (Thread.interrupted()) {
                this.wasInterrupted = true;
            }
            if (this.wasInterrupted && this.interruptible) {
                this.thread = null;
                return true;
            } else if (!this.timed) {
                return false;
            } else {
                if (this.nanos > 0) {
                    long nanoTime = this.deadline - System.nanoTime();
                    this.nanos = nanoTime;
                    if (nanoTime > 0) {
                        return false;
                    }
                }
                this.thread = null;
                return true;
            }
        }

        public boolean block() {
            while (!isReleasable()) {
                if (this.timed) {
                    LockSupport.parkNanos(this, this.nanos);
                } else {
                    LockSupport.park(this);
                }
            }
            return true;
        }
    }
}
