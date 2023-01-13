package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifi.p018x.com.android.internal.util.Protocol;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;

public abstract class ForkJoinTask<V> implements Future<V>, Serializable {
    private static final int ABNORMAL = 262144;
    private static final int DONE = Integer.MIN_VALUE;
    private static final int SIGNAL = 65536;
    private static final int SMASK = 65535;
    private static final VarHandle STATUS;
    private static final int THROWN = 131072;
    private static final ExceptionNode[] exceptionTable = new ExceptionNode[32];
    private static final ReentrantLock exceptionTableLock = new ReentrantLock();
    private static final ReferenceQueue<ForkJoinTask<?>> exceptionTableRefQueue = new ReferenceQueue<>();
    private static final long serialVersionUID = -7721805057305804111L;
    volatile int status;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.abnormalCompletion(int):int, dex: classes4.dex
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
    private int abnormalCompletion(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.abnormalCompletion(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.abnormalCompletion(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.externalAwaitDone():int, dex: classes4.dex
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
    private int externalAwaitDone() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.externalAwaitDone():int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.externalAwaitDone():int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.externalInterruptibleAwaitDone():int, dex: classes4.dex
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
    private int externalInterruptibleAwaitDone() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.externalInterruptibleAwaitDone():int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.externalInterruptibleAwaitDone():int");
    }

    static boolean isExceptionalStatus(int i) {
        return (i & 131072) != 0;
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.setDone():int, dex: classes4.dex
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
    private int setDone() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.setDone():int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.setDone():int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.compareAndSetForkJoinTaskTag(short, short):boolean, dex: classes4.dex
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
    public final boolean compareAndSetForkJoinTaskTag(short r1, short r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.compareAndSetForkJoinTaskTag(short, short):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.compareAndSetForkJoinTaskTag(short, short):boolean");
    }

    /* access modifiers changed from: protected */
    public abstract boolean exec();

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.get(long, java.util.concurrent.TimeUnit):V, dex: classes4.dex
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
    public final V get(long r1, java.util.concurrent.TimeUnit r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.get(long, java.util.concurrent.TimeUnit):V, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.get(long, java.util.concurrent.TimeUnit):java.lang.Object");
    }

    public abstract V getRawResult();

    /* access modifiers changed from: package-private */
    public void internalPropagateException(Throwable th) {
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.internalWait(long):void, dex: classes4.dex
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
    final void internalWait(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.internalWait(long):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.internalWait(long):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.setForkJoinTaskTag(short):short, dex: classes4.dex
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
    public final short setForkJoinTaskTag(short r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinTask.setForkJoinTaskTag(short):short, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.setForkJoinTaskTag(short):short");
    }

    /* access modifiers changed from: protected */
    public abstract void setRawResult(V v);

    /* access modifiers changed from: package-private */
    public final int doExec() {
        boolean z;
        int i = this.status;
        if (i < 0) {
            return i;
        }
        try {
            z = exec();
        } catch (Throwable th) {
            i = setExceptionalCompletion(th);
            z = false;
        }
        return z ? setDone() : i;
    }

    private int tryExternalHelp() {
        int i = this.status;
        if (i < 0) {
            return i;
        }
        if (this instanceof CountedCompleter) {
            return ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0);
        }
        if (ForkJoinPool.common.tryExternalUnpush(this)) {
            return doExec();
        }
        return 0;
    }

    private int doJoin() {
        int doExec;
        int i = this.status;
        if (i < 0) {
            return i;
        }
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof ForkJoinWorkerThread)) {
            return externalAwaitDone();
        }
        ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread;
        ForkJoinPool.WorkQueue workQueue = forkJoinWorkerThread.workQueue;
        if (!workQueue.tryUnpush(this) || (doExec = doExec()) >= 0) {
            return forkJoinWorkerThread.pool.awaitJoin(workQueue, this, 0);
        }
        return doExec;
    }

    private int doInvoke() {
        int doExec = doExec();
        if (doExec < 0) {
            return doExec;
        }
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof ForkJoinWorkerThread)) {
            return externalAwaitDone();
        }
        ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread;
        return forkJoinWorkerThread.pool.awaitJoin(forkJoinWorkerThread.workQueue, this, 0);
    }

    static {
        try {
            STATUS = MethodHandles.lookup().findVarHandle(ForkJoinTask.class, "status", Integer.TYPE);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    static final class ExceptionNode extends WeakReference<ForkJoinTask<?>> {

        /* renamed from: ex */
        final Throwable f755ex;
        final int hashCode;
        ExceptionNode next;
        final long thrower = Thread.currentThread().getId();

        ExceptionNode(ForkJoinTask<?> forkJoinTask, Throwable th, ExceptionNode exceptionNode, ReferenceQueue<ForkJoinTask<?>> referenceQueue) {
            super(forkJoinTask, referenceQueue);
            this.f755ex = th;
            this.next = exceptionNode;
            this.hashCode = System.identityHashCode(forkJoinTask);
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public final int recordExceptionalCompletion(Throwable th) {
        int i = this.status;
        if (i < 0) {
            return i;
        }
        int identityHashCode = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        try {
            expungeStaleExceptions();
            ExceptionNode[] exceptionNodeArr = exceptionTable;
            int length = identityHashCode & (exceptionNodeArr.length - 1);
            ExceptionNode exceptionNode = exceptionNodeArr[length];
            while (true) {
                if (exceptionNode == null) {
                    exceptionNodeArr[length] = new ExceptionNode(this, th, exceptionNodeArr[length], exceptionTableRefQueue);
                    break;
                } else if (exceptionNode.get() == this) {
                    break;
                } else {
                    exceptionNode = exceptionNode.next;
                }
            }
            reentrantLock.unlock();
            return abnormalCompletion(-2147090432);
        } catch (Throwable th2) {
            reentrantLock.unlock();
            throw th2;
        }
    }

    private int setExceptionalCompletion(Throwable th) {
        int recordExceptionalCompletion = recordExceptionalCompletion(th);
        if ((131072 & recordExceptionalCompletion) != 0) {
            internalPropagateException(th);
        }
        return recordExceptionalCompletion;
    }

    static final void cancelIgnoringExceptions(ForkJoinTask<?> forkJoinTask) {
        if (forkJoinTask != null && forkJoinTask.status >= 0) {
            try {
                forkJoinTask.cancel(false);
            } catch (Throwable unused) {
            }
        }
    }

    private void clearExceptionalCompletion() {
        int identityHashCode = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        try {
            ExceptionNode[] exceptionNodeArr = exceptionTable;
            int length = identityHashCode & (exceptionNodeArr.length - 1);
            ExceptionNode exceptionNode = exceptionNodeArr[length];
            ExceptionNode exceptionNode2 = null;
            while (true) {
                if (exceptionNode == null) {
                    break;
                }
                ExceptionNode exceptionNode3 = exceptionNode.next;
                if (exceptionNode.get() != this) {
                    exceptionNode2 = exceptionNode;
                    exceptionNode = exceptionNode3;
                } else if (exceptionNode2 == null) {
                    exceptionNodeArr[length] = exceptionNode3;
                } else {
                    exceptionNode2.next = exceptionNode3;
                }
            }
            expungeStaleExceptions();
            this.status = 0;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    private Throwable getThrowableException() {
        Throwable th;
        int identityHashCode = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        try {
            expungeStaleExceptions();
            ExceptionNode[] exceptionNodeArr = exceptionTable;
            ExceptionNode exceptionNode = exceptionNodeArr[identityHashCode & (exceptionNodeArr.length - 1)];
            while (exceptionNode != null && exceptionNode.get() != this) {
                exceptionNode = exceptionNode.next;
            }
            reentrantLock.unlock();
            Constructor constructor = null;
            if (exceptionNode == null || (th = exceptionNode.f755ex) == null) {
                return null;
            }
            if (exceptionNode.thrower != Thread.currentThread().getId()) {
                try {
                    for (Constructor constructor2 : th.getClass().getConstructors()) {
                        Class<Throwable>[] parameterTypes = constructor2.getParameterTypes();
                        if (parameterTypes.length == 0) {
                            constructor = constructor2;
                        } else if (parameterTypes.length == 1 && parameterTypes[0] == Throwable.class) {
                            return (Throwable) constructor2.newInstance(th);
                        }
                    }
                    if (constructor != null) {
                        Throwable th2 = (Throwable) constructor.newInstance(new Object[0]);
                        th2.initCause(th);
                        return th2;
                    }
                } catch (Exception unused) {
                }
            }
            return th;
        } catch (Throwable th3) {
            reentrantLock.unlock();
            throw th3;
        }
    }

    private static void expungeStaleExceptions() {
        while (true) {
            Reference<? extends ForkJoinTask<?>> poll = exceptionTableRefQueue.poll();
            if (poll == null) {
                return;
            }
            if (poll instanceof ExceptionNode) {
                ExceptionNode[] exceptionNodeArr = exceptionTable;
                int length = ((ExceptionNode) poll).hashCode & (exceptionNodeArr.length - 1);
                ExceptionNode exceptionNode = exceptionNodeArr[length];
                ExceptionNode exceptionNode2 = null;
                while (true) {
                    if (exceptionNode == null) {
                        break;
                    }
                    ExceptionNode exceptionNode3 = exceptionNode.next;
                    if (exceptionNode != poll) {
                        exceptionNode2 = exceptionNode;
                        exceptionNode = exceptionNode3;
                    } else if (exceptionNode2 == null) {
                        exceptionNodeArr[length] = exceptionNode3;
                    } else {
                        exceptionNode2.next = exceptionNode3;
                    }
                }
            }
        }
    }

    static final void helpExpungeStaleExceptions() {
        ReentrantLock reentrantLock = exceptionTableLock;
        if (reentrantLock.tryLock()) {
            try {
                expungeStaleExceptions();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    static void rethrow(Throwable th) {
        uncheckedThrow(th);
    }

    static <T extends Throwable> void uncheckedThrow(Throwable th) throws Throwable {
        if (th != null) {
            throw th;
        }
        throw new Error("Unknown Exception");
    }

    private void reportException(int i) {
        Throwable th;
        if ((i & 131072) != 0) {
            th = getThrowableException();
        } else {
            th = new CancellationException();
        }
        rethrow(th);
    }

    public final ForkJoinTask<V> fork() {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            ((ForkJoinWorkerThread) currentThread).workQueue.push(this);
        } else {
            ForkJoinPool.common.externalPush(this);
        }
        return this;
    }

    public final V join() {
        int doJoin = doJoin();
        if ((262144 & doJoin) != 0) {
            reportException(doJoin);
        }
        return getRawResult();
    }

    public final V invoke() {
        int doInvoke = doInvoke();
        if ((262144 & doInvoke) != 0) {
            reportException(doInvoke);
        }
        return getRawResult();
    }

    public static void invokeAll(ForkJoinTask<?> forkJoinTask, ForkJoinTask<?> forkJoinTask2) {
        forkJoinTask2.fork();
        int doInvoke = forkJoinTask.doInvoke();
        if ((doInvoke & 262144) != 0) {
            forkJoinTask.reportException(doInvoke);
        }
        int doJoin = forkJoinTask2.doJoin();
        if ((doJoin & 262144) != 0) {
            forkJoinTask2.reportException(doJoin);
        }
    }

    public static void invokeAll(ForkJoinTask<?>... forkJoinTaskArr) {
        int length = forkJoinTaskArr.length - 1;
        Throwable th = null;
        for (int i = length; i >= 0; i--) {
            ForkJoinTask<?> forkJoinTask = forkJoinTaskArr[i];
            if (forkJoinTask == null) {
                if (th == null) {
                    th = new NullPointerException();
                }
            } else if (i != 0) {
                forkJoinTask.fork();
            } else if ((262144 & forkJoinTask.doInvoke()) != 0 && th == null) {
                th = forkJoinTask.getException();
            }
        }
        for (int i2 = 1; i2 <= length; i2++) {
            ForkJoinTask<?> forkJoinTask2 = forkJoinTaskArr[i2];
            if (forkJoinTask2 != null) {
                if (th != null) {
                    forkJoinTask2.cancel(false);
                } else if ((forkJoinTask2.doJoin() & 262144) != 0) {
                    th = forkJoinTask2.getException();
                }
            }
        }
        if (th != null) {
            rethrow(th);
        }
    }

    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> collection) {
        if (!(collection instanceof RandomAccess) || !(collection instanceof List)) {
            invokeAll((ForkJoinTask<?>[]) (ForkJoinTask[]) collection.toArray((T[]) new ForkJoinTask[0]));
            return collection;
        }
        List list = (List) collection;
        int size = list.size() - 1;
        Throwable th = null;
        for (int i = size; i >= 0; i--) {
            ForkJoinTask forkJoinTask = (ForkJoinTask) list.get(i);
            if (forkJoinTask == null) {
                if (th == null) {
                    th = new NullPointerException();
                }
            } else if (i != 0) {
                forkJoinTask.fork();
            } else if ((262144 & forkJoinTask.doInvoke()) != 0 && th == null) {
                th = forkJoinTask.getException();
            }
        }
        for (int i2 = 1; i2 <= size; i2++) {
            ForkJoinTask forkJoinTask2 = (ForkJoinTask) list.get(i2);
            if (forkJoinTask2 != null) {
                if (th != null) {
                    forkJoinTask2.cancel(false);
                } else if ((forkJoinTask2.doJoin() & 262144) != 0) {
                    th = forkJoinTask2.getException();
                }
            }
        }
        if (th != null) {
            rethrow(th);
        }
        return collection;
    }

    public boolean cancel(boolean z) {
        return (abnormalCompletion(-2147221504) & Protocol.BASE_NSD_MANAGER) == 262144;
    }

    public final boolean isDone() {
        return this.status < 0;
    }

    public final boolean isCancelled() {
        return (this.status & Protocol.BASE_NSD_MANAGER) == 262144;
    }

    public final boolean isCompletedAbnormally() {
        return (this.status & 262144) != 0;
    }

    public final boolean isCompletedNormally() {
        return (this.status & -2147221504) == Integer.MIN_VALUE;
    }

    public final Throwable getException() {
        int i = this.status;
        if ((262144 & i) == 0) {
            return null;
        }
        if ((i & 131072) == 0) {
            return new CancellationException();
        }
        return getThrowableException();
    }

    public void completeExceptionally(Throwable th) {
        if (!(th instanceof RuntimeException) && !(th instanceof Error)) {
            th = new RuntimeException(th);
        }
        setExceptionalCompletion(th);
    }

    public void complete(V v) {
        try {
            setRawResult(v);
            setDone();
        } catch (Throwable th) {
            setExceptionalCompletion(th);
        }
    }

    public final void quietlyComplete() {
        setDone();
    }

    public final V get() throws InterruptedException, ExecutionException {
        int doJoin = Thread.currentThread() instanceof ForkJoinWorkerThread ? doJoin() : externalInterruptibleAwaitDone();
        if ((131072 & doJoin) != 0) {
            throw new ExecutionException(getThrowableException());
        } else if ((doJoin & 262144) == 0) {
            return getRawResult();
        } else {
            throw new CancellationException();
        }
    }

    public final void quietlyJoin() {
        doJoin();
    }

    public final void quietlyInvoke() {
        doInvoke();
    }

    public static void helpQuiesce() {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread;
            forkJoinWorkerThread.pool.helpQuiescePool(forkJoinWorkerThread.workQueue);
            return;
        }
        ForkJoinPool.quiesceCommonPool();
    }

    public void reinitialize() {
        if ((this.status & 131072) != 0) {
            clearExceptionalCompletion();
        } else {
            this.status = 0;
        }
    }

    public static ForkJoinPool getPool() {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) currentThread).pool;
        }
        return null;
    }

    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }

    public boolean tryUnfork() {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) currentThread).workQueue.tryUnpush(this);
        }
        return ForkJoinPool.common.tryExternalUnpush(this);
    }

    public static int getQueuedTaskCount() {
        ForkJoinPool.WorkQueue workQueue;
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            workQueue = ((ForkJoinWorkerThread) currentThread).workQueue;
        } else {
            workQueue = ForkJoinPool.commonSubmitterQueue();
        }
        if (workQueue == null) {
            return 0;
        }
        return workQueue.queueSize();
    }

    public static int getSurplusQueuedTaskCount() {
        return ForkJoinPool.getSurplusQueuedTaskCount();
    }

    protected static ForkJoinTask<?> peekNextLocalTask() {
        ForkJoinPool.WorkQueue workQueue;
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            workQueue = ((ForkJoinWorkerThread) currentThread).workQueue;
        } else {
            workQueue = ForkJoinPool.commonSubmitterQueue();
        }
        if (workQueue == null) {
            return null;
        }
        return workQueue.peek();
    }

    protected static ForkJoinTask<?> pollNextLocalTask() {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) currentThread).workQueue.nextLocalTask();
        }
        return null;
    }

    protected static ForkJoinTask<?> pollTask() {
        Thread currentThread = Thread.currentThread();
        if (!(currentThread instanceof ForkJoinWorkerThread)) {
            return null;
        }
        ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread;
        return forkJoinWorkerThread.pool.nextTaskFor(forkJoinWorkerThread.workQueue);
    }

    protected static ForkJoinTask<?> pollSubmission() {
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) currentThread).pool.pollSubmission();
        }
        return null;
    }

    public final short getForkJoinTaskTag() {
        return (short) this.status;
    }

    static final class AdaptedRunnable<T> extends ForkJoinTask<T> implements RunnableFuture<T> {
        private static final long serialVersionUID = 5232453952276885070L;
        T result;
        final Runnable runnable;

        AdaptedRunnable(Runnable runnable2, T t) {
            runnable2.getClass();
            this.runnable = runnable2;
            this.result = t;
        }

        public final T getRawResult() {
            return this.result;
        }

        public final void setRawResult(T t) {
            this.result = t;
        }

        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        public final void run() {
            invoke();
        }

        public String toString() {
            return super.toString() + "[Wrapped task = " + this.runnable + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    static final class AdaptedRunnableAction extends ForkJoinTask<Void> implements RunnableFuture<Void> {
        private static final long serialVersionUID = 5232453952276885070L;
        final Runnable runnable;

        public final Void getRawResult() {
            return null;
        }

        public final void setRawResult(Void voidR) {
        }

        AdaptedRunnableAction(Runnable runnable2) {
            runnable2.getClass();
            this.runnable = runnable2;
        }

        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        public final void run() {
            invoke();
        }

        public String toString() {
            return super.toString() + "[Wrapped task = " + this.runnable + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    static final class RunnableExecuteAction extends ForkJoinTask<Void> {
        private static final long serialVersionUID = 5232453952276885070L;
        final Runnable runnable;

        public final Void getRawResult() {
            return null;
        }

        public final void setRawResult(Void voidR) {
        }

        RunnableExecuteAction(Runnable runnable2) {
            runnable2.getClass();
            this.runnable = runnable2;
        }

        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        /* access modifiers changed from: package-private */
        public void internalPropagateException(Throwable th) {
            rethrow(th);
        }
    }

    static final class AdaptedCallable<T> extends ForkJoinTask<T> implements RunnableFuture<T> {
        private static final long serialVersionUID = 2838392045355241008L;
        final Callable<? extends T> callable;
        T result;

        AdaptedCallable(Callable<? extends T> callable2) {
            callable2.getClass();
            this.callable = callable2;
        }

        public final T getRawResult() {
            return this.result;
        }

        public final void setRawResult(T t) {
            this.result = t;
        }

        public final boolean exec() {
            try {
                this.result = this.callable.call();
                return true;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new RuntimeException((Throwable) e2);
            }
        }

        public final void run() {
            invoke();
        }

        public String toString() {
            return super.toString() + "[Wrapped task = " + this.callable + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    public static ForkJoinTask<?> adapt(Runnable runnable) {
        return new AdaptedRunnableAction(runnable);
    }

    public static <T> ForkJoinTask<T> adapt(Runnable runnable, T t) {
        return new AdaptedRunnable(runnable, t);
    }

    public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable) {
        return new AdaptedCallable(callable);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(getException());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object readObject = objectInputStream.readObject();
        if (readObject != null) {
            setExceptionalCompletion((Throwable) readObject);
        }
    }
}
