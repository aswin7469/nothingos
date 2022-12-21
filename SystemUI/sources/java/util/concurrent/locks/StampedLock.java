package java.util.concurrent.locks;

import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.Serializable;
import java.util.concurrent.TimeUnit;

public class StampedLock implements Serializable {
    private static final long ABITS = 255;
    private static final int CANCELLED = 1;
    private static final int HEAD_SPINS;
    private static final long INTERRUPTED = 1;
    private static final int LG_READERS = 7;
    private static final int MAX_HEAD_SPINS;
    private static final int NCPU;
    private static final long ORIGIN = 256;
    private static final int OVERFLOW_YIELD_RATE = 7;
    private static final long RBITS = 127;
    private static final long RFULL = 126;
    private static final int RMODE = 0;
    private static final long RUNIT = 1;
    private static final long SBITS = -128;
    private static final int SPINS;
    private static final VarHandle STATE;
    private static final int WAITING = -1;
    private static final long WBIT = 128;
    private static final VarHandle WCOWAIT;
    private static final VarHandle WHEAD;
    private static final int WMODE = 1;
    private static final VarHandle WNEXT;
    private static final VarHandle WSTATUS;
    private static final VarHandle WTAIL;
    private static final long serialVersionUID = -6001602636862214147L;
    transient ReadLockView readLockView;
    transient ReadWriteLockView readWriteLockView;
    private transient int readerOverflow;
    private volatile transient long state = 256;
    private volatile transient WNode whead;
    transient WriteLockView writeLockView;
    private volatile transient WNode wtail;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.acquireRead(boolean, long):long, dex: classes4.dex
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
    private long acquireRead(boolean r1, long r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.acquireRead(boolean, long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.acquireRead(boolean, long):long");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.acquireWrite(boolean, long):long, dex: classes4.dex
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
    private long acquireWrite(boolean r1, long r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.acquireWrite(boolean, long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.acquireWrite(boolean, long):long");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.cancelWaiter(java.util.concurrent.locks.StampedLock$WNode, java.util.concurrent.locks.StampedLock$WNode, boolean):long, dex: classes4.dex
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
    private long cancelWaiter(java.util.concurrent.locks.StampedLock.WNode r1, java.util.concurrent.locks.StampedLock.WNode r2, boolean r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.cancelWaiter(java.util.concurrent.locks.StampedLock$WNode, java.util.concurrent.locks.StampedLock$WNode, boolean):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.cancelWaiter(java.util.concurrent.locks.StampedLock$WNode, java.util.concurrent.locks.StampedLock$WNode, boolean):long");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.locks.StampedLock.casState(long, long):boolean, dex: classes4.dex
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
    private boolean casState(long r1, long r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.locks.StampedLock.casState(long, long):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.casState(long, long):boolean");
    }

    public static boolean isLockStamp(long j) {
        return (j & ABITS) != 0;
    }

    public static boolean isOptimisticReadStamp(long j) {
        return (ABITS & j) == 0 && j != 0;
    }

    public static boolean isReadLockStamp(long j) {
        return (j & RBITS) != 0;
    }

    public static boolean isWriteLockStamp(long j) {
        return (j & ABITS) == 128;
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.readObject(java.io.ObjectInputStream):void, dex: classes4.dex
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
    private void readObject(java.p026io.ObjectInputStream r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.readObject(java.io.ObjectInputStream):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.readObject(java.io.ObjectInputStream):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.release(java.util.concurrent.locks.StampedLock$WNode):void, dex: classes4.dex
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
    private void release(java.util.concurrent.locks.StampedLock.WNode r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.release(java.util.concurrent.locks.StampedLock$WNode):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.release(java.util.concurrent.locks.StampedLock$WNode):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.tryDecReaderOverflow(long):long, dex: classes4.dex
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
    private long tryDecReaderOverflow(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.tryDecReaderOverflow(long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.tryDecReaderOverflow(long):long");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.tryIncReaderOverflow(long):long, dex: classes4.dex
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
    private long tryIncReaderOverflow(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.tryIncReaderOverflow(long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.tryIncReaderOverflow(long):long");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.unlockWriteInternal(long):long, dex: classes4.dex
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
    private long unlockWriteInternal(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.unlockWriteInternal(long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.unlockWriteInternal(long):long");
    }

    private static long unlockWriteState(long j) {
        long j2 = j + 128;
        if (j2 == 0) {
            return 256;
        }
        return j2;
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.tryConvertToReadLock(long):long, dex: classes4.dex
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
    public long tryConvertToReadLock(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.locks.StampedLock.tryConvertToReadLock(long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.tryConvertToReadLock(long):long");
    }

    static {
        Class<StampedLock> cls = StampedLock.class;
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        NCPU = availableProcessors;
        int i = 1;
        SPINS = availableProcessors > 1 ? 64 : 1;
        HEAD_SPINS = availableProcessors > 1 ? 1024 : 1;
        if (availableProcessors > 1) {
            i = 65536;
        }
        MAX_HEAD_SPINS = i;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            STATE = lookup.findVarHandle(cls, AuthDialog.KEY_BIOMETRIC_STATE, Long.TYPE);
            WHEAD = lookup.findVarHandle(cls, "whead", WNode.class);
            WTAIL = lookup.findVarHandle(cls, "wtail", WNode.class);
            WSTATUS = lookup.findVarHandle(WNode.class, "status", Integer.TYPE);
            WNEXT = lookup.findVarHandle(WNode.class, "next", WNode.class);
            WCOWAIT = lookup.findVarHandle(WNode.class, "cowait", WNode.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    static final class WNode {
        volatile WNode cowait;
        final int mode;
        volatile WNode next;
        volatile WNode prev;
        volatile int status;
        volatile Thread thread;

        WNode(int i, WNode wNode) {
            this.mode = i;
            this.prev = wNode;
        }
    }

    private long tryWriteLock(long j) {
        long j2 = 128 | j;
        if (!casState(j, j2)) {
            return 0;
        }
        VarHandle.storeStoreFence();
        return j2;
    }

    public long writeLock() {
        long tryWriteLock = tryWriteLock();
        return tryWriteLock != 0 ? tryWriteLock : acquireWrite(false, 0);
    }

    public long tryWriteLock() {
        long j = this.state;
        if ((ABITS & j) == 0) {
            return tryWriteLock(j);
        }
        return 0;
    }

    public long tryWriteLock(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        if (!Thread.interrupted()) {
            long tryWriteLock = tryWriteLock();
            if (tryWriteLock != 0) {
                return tryWriteLock;
            }
            if (nanos <= 0) {
                return 0;
            }
            long nanoTime = System.nanoTime() + nanos;
            if (nanoTime == 0) {
                nanoTime = 1;
            }
            long acquireWrite = acquireWrite(true, nanoTime);
            if (acquireWrite != 1) {
                return acquireWrite;
            }
        }
        throw new InterruptedException();
    }

    public long writeLockInterruptibly() throws InterruptedException {
        if (!Thread.interrupted()) {
            long acquireWrite = acquireWrite(true, 0);
            if (acquireWrite != 1) {
                return acquireWrite;
            }
        }
        throw new InterruptedException();
    }

    public long readLock() {
        if (this.whead == this.wtail) {
            long j = this.state;
            if ((ABITS & j) < RFULL) {
                long j2 = 1 + j;
                if (casState(j, j2)) {
                    return j2;
                }
            }
        }
        return acquireRead(false, 0);
    }

    public long tryReadLock() {
        while (true) {
            long j = this.state;
            long j2 = ABITS & j;
            if (j2 == 128) {
                return 0;
            }
            if (j2 < RFULL) {
                long j3 = 1 + j;
                if (casState(j, j3)) {
                    return j3;
                }
            } else {
                long tryIncReaderOverflow = tryIncReaderOverflow(j);
                if (tryIncReaderOverflow != 0) {
                    return tryIncReaderOverflow;
                }
            }
        }
    }

    public long tryReadLock(long j, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j);
        if (!Thread.interrupted()) {
            long j2 = this.state;
            long j3 = ABITS & j2;
            if (j3 != 128) {
                if (j3 < RFULL) {
                    long j4 = j2 + 1;
                    if (casState(j2, j4)) {
                        return j4;
                    }
                } else {
                    long tryIncReaderOverflow = tryIncReaderOverflow(j2);
                    if (tryIncReaderOverflow != 0) {
                        return tryIncReaderOverflow;
                    }
                }
            }
            if (nanos <= 0) {
                return 0;
            }
            long nanoTime = System.nanoTime() + nanos;
            if (nanoTime == 0) {
                nanoTime = 1;
            }
            long acquireRead = acquireRead(true, nanoTime);
            if (acquireRead != 1) {
                return acquireRead;
            }
        }
        throw new InterruptedException();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001f, code lost:
        if (casState(r0, r4) == false) goto L_0x0021;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r4 != 1) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long readLockInterruptibly() throws java.lang.InterruptedException {
        /*
            r8 = this;
            boolean r0 = java.lang.Thread.interrupted()
            if (r0 != 0) goto L_0x002d
            java.util.concurrent.locks.StampedLock$WNode r0 = r8.whead
            java.util.concurrent.locks.StampedLock$WNode r1 = r8.wtail
            r2 = 1
            if (r0 != r1) goto L_0x0021
            long r0 = r8.state
            r4 = 255(0xff, double:1.26E-321)
            long r4 = r4 & r0
            r6 = 126(0x7e, double:6.23E-322)
            int r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r4 >= 0) goto L_0x0021
            long r4 = r0 + r2
            boolean r0 = r8.casState(r0, r4)
            if (r0 != 0) goto L_0x002c
        L_0x0021:
            r0 = 1
            r4 = 0
            long r4 = r8.acquireRead(r0, r4)
            int r8 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r8 == 0) goto L_0x002d
        L_0x002c:
            return r4
        L_0x002d:
            java.lang.InterruptedException r8 = new java.lang.InterruptedException
            r8.<init>()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.readLockInterruptibly():long");
    }

    public long tryOptimisticRead() {
        long j = this.state;
        if ((128 & j) == 0) {
            return j & SBITS;
        }
        return 0;
    }

    public boolean validate(long j) {
        VarHandle.acquireFence();
        return (j & SBITS) == (SBITS & this.state);
    }

    public void unlockWrite(long j) {
        if (this.state != j || (128 & j) == 0) {
            throw new IllegalMonitorStateException();
        }
        unlockWriteInternal(j);
    }

    public void unlockRead(long j) {
        WNode wNode;
        while (true) {
            long j2 = this.state;
            if ((j2 & SBITS) != (SBITS & j) || (j & RBITS) <= 0) {
                break;
            }
            long j3 = RBITS & j2;
            if (j3 <= 0) {
                break;
            } else if (j3 < RFULL) {
                if (casState(j2, j2 - 1)) {
                    if (j3 == 1 && (wNode = this.whead) != null && wNode.status != 0) {
                        release(wNode);
                        return;
                    }
                    return;
                }
            } else if (tryDecReaderOverflow(j2) != 0) {
                return;
            }
        }
        throw new IllegalMonitorStateException();
    }

    public void unlock(long j) {
        if ((128 & j) != 0) {
            unlockWrite(j);
        } else {
            unlockRead(j);
        }
    }

    public long tryConvertToWriteLock(long j) {
        long j2 = j & ABITS;
        while (true) {
            long j3 = this.state;
            if ((j3 & SBITS) != (j & SBITS)) {
                break;
            }
            long j4 = j3 & ABITS;
            if (j4 != 0) {
                if (j4 != 128) {
                    if (j4 != 1 || j2 == 0) {
                        break;
                    }
                    long j5 = (j3 - 1) + 128;
                    if (casState(j3, j5)) {
                        VarHandle.storeStoreFence();
                        return j5;
                    }
                } else if (j2 != j4) {
                    return 0;
                } else {
                    return j;
                }
            } else if (j2 != 0) {
                break;
            } else {
                long tryWriteLock = tryWriteLock(j3);
                if (tryWriteLock != 0) {
                    return tryWriteLock;
                }
            }
        }
        return 0;
    }

    public long tryConvertToOptimisticRead(long j) {
        WNode wNode;
        VarHandle.acquireFence();
        while (true) {
            long j2 = this.state;
            if ((j2 & SBITS) != (j & SBITS)) {
                break;
            }
            long j3 = j & ABITS;
            if (j3 >= 128) {
                if (j2 == j) {
                    return unlockWriteInternal(j2);
                }
            } else if (j3 == 0) {
                return j;
            } else {
                long j4 = ABITS & j2;
                if (j4 == 0) {
                    break;
                } else if (j4 < RFULL) {
                    long j5 = j2 - 1;
                    if (casState(j2, j5)) {
                        if (!(j4 != 1 || (wNode = this.whead) == null || wNode.status == 0)) {
                            release(wNode);
                        }
                        return j5 & SBITS;
                    }
                } else {
                    long tryDecReaderOverflow = tryDecReaderOverflow(j2);
                    if (tryDecReaderOverflow != 0) {
                        return tryDecReaderOverflow & SBITS;
                    }
                }
            }
        }
        return 0;
    }

    public boolean tryUnlockWrite() {
        long j = this.state;
        if ((128 & j) == 0) {
            return false;
        }
        unlockWriteInternal(j);
        return true;
    }

    public boolean tryUnlockRead() {
        WNode wNode;
        while (true) {
            long j = this.state;
            long j2 = ABITS & j;
            if (j2 == 0 || j2 >= 128) {
                return false;
            }
            if (j2 < RFULL) {
                if (casState(j, j - 1)) {
                    if (!(j2 != 1 || (wNode = this.whead) == null || wNode.status == 0)) {
                        release(wNode);
                    }
                    return true;
                }
            } else if (tryDecReaderOverflow(j) != 0) {
                return true;
            }
        }
    }

    private int getReadLockCount(long j) {
        long j2 = j & RBITS;
        if (j2 >= RFULL) {
            j2 = ((long) this.readerOverflow) + RFULL;
        }
        return (int) j2;
    }

    public boolean isWriteLocked() {
        return (this.state & 128) != 0;
    }

    public boolean isReadLocked() {
        return (this.state & RBITS) != 0;
    }

    public int getReadLockCount() {
        return getReadLockCount(this.state);
    }

    public String toString() {
        String str;
        long j = this.state;
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if ((ABITS & j) == 0) {
            str = "[Unlocked]";
        } else if ((128 & j) != 0) {
            str = "[Write-locked]";
        } else {
            str = "[Read-locks:" + getReadLockCount(j) + NavigationBarInflaterView.SIZE_MOD_END;
        }
        sb.append(str);
        return sb.toString();
    }

    public Lock asReadLock() {
        ReadLockView readLockView2 = this.readLockView;
        if (readLockView2 != null) {
            return readLockView2;
        }
        ReadLockView readLockView3 = new ReadLockView();
        this.readLockView = readLockView3;
        return readLockView3;
    }

    public Lock asWriteLock() {
        WriteLockView writeLockView2 = this.writeLockView;
        if (writeLockView2 != null) {
            return writeLockView2;
        }
        WriteLockView writeLockView3 = new WriteLockView();
        this.writeLockView = writeLockView3;
        return writeLockView3;
    }

    public ReadWriteLock asReadWriteLock() {
        ReadWriteLockView readWriteLockView2 = this.readWriteLockView;
        if (readWriteLockView2 != null) {
            return readWriteLockView2;
        }
        ReadWriteLockView readWriteLockView3 = new ReadWriteLockView();
        this.readWriteLockView = readWriteLockView3;
        return readWriteLockView3;
    }

    final class ReadLockView implements Lock {
        ReadLockView() {
        }

        public void lock() {
            StampedLock.this.readLock();
        }

        public void lockInterruptibly() throws InterruptedException {
            StampedLock.this.readLockInterruptibly();
        }

        public boolean tryLock() {
            return StampedLock.this.tryReadLock() != 0;
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            return StampedLock.this.tryReadLock(j, timeUnit) != 0;
        }

        public void unlock() {
            StampedLock.this.unstampedUnlockRead();
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    final class WriteLockView implements Lock {
        WriteLockView() {
        }

        public void lock() {
            StampedLock.this.writeLock();
        }

        public void lockInterruptibly() throws InterruptedException {
            StampedLock.this.writeLockInterruptibly();
        }

        public boolean tryLock() {
            return StampedLock.this.tryWriteLock() != 0;
        }

        public boolean tryLock(long j, TimeUnit timeUnit) throws InterruptedException {
            return StampedLock.this.tryWriteLock(j, timeUnit) != 0;
        }

        public void unlock() {
            StampedLock.this.unstampedUnlockWrite();
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    final class ReadWriteLockView implements ReadWriteLock {
        ReadWriteLockView() {
        }

        public Lock readLock() {
            return StampedLock.this.asReadLock();
        }

        public Lock writeLock() {
            return StampedLock.this.asWriteLock();
        }
    }

    /* access modifiers changed from: package-private */
    public final void unstampedUnlockWrite() {
        long j = this.state;
        if ((128 & j) != 0) {
            unlockWriteInternal(j);
            return;
        }
        throw new IllegalMonitorStateException();
    }

    /* access modifiers changed from: package-private */
    public final void unstampedUnlockRead() {
        WNode wNode;
        while (true) {
            long j = this.state;
            long j2 = RBITS & j;
            if (j2 <= 0) {
                throw new IllegalMonitorStateException();
            } else if (j2 < RFULL) {
                if (casState(j, j - 1)) {
                    if (j2 == 1 && (wNode = this.whead) != null && wNode.status != 0) {
                        release(wNode);
                        return;
                    }
                    return;
                }
            } else if (tryDecReaderOverflow(j) != 0) {
                return;
            }
        }
    }
}
