package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.Thread;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Predicate;
import kotlin.UShort;

public class ForkJoinPool extends AbstractExecutorService {
    private static final long ADD_WORKER = 140737488355328L;
    private static final int COMMON_MAX_SPARES;
    static final int COMMON_PARALLELISM;
    private static final VarHandle CTL;
    private static final int DEFAULT_COMMON_MAX_SPARES = 256;
    private static final long DEFAULT_KEEPALIVE = 60000;
    static final int DORMANT = -1073741824;
    static final int FIFO = 65536;
    static final int INITIAL_QUEUE_CAPACITY = 8192;
    static final int MAXIMUM_QUEUE_CAPACITY = 67108864;
    static final int MAX_CAP = 32767;
    private static final VarHandle MODE;
    static final int OWNED = 1;

    /* renamed from: QA */
    static final VarHandle f753QA;
    static final int QLOCK = 1;
    static final int QUIET = 1073741824;
    private static final long RC_MASK = -281474976710656L;
    private static final int RC_SHIFT = 48;
    private static final long RC_UNIT = 281474976710656L;
    private static final int SEED_INCREMENT = -1640531527;
    static final int SHUTDOWN = 262144;
    static final int SMASK = 65535;
    private static final long SP_MASK = 4294967295L;
    static final int SQMASK = 126;
    static final int SS_SEQ = 65536;
    static final int STOP = Integer.MIN_VALUE;
    static final int SWIDTH = 16;
    private static final long TC_MASK = 281470681743360L;
    private static final int TC_SHIFT = 32;
    private static final long TC_UNIT = 4294967296L;
    static final int TERMINATED = 524288;
    private static final long TIMEOUT_SLOP = 20;
    static final int TOP_BOUND_SHIFT = 10;
    private static final long UC_MASK = -4294967296L;
    static final int UNSIGNALLED = Integer.MIN_VALUE;
    static final ForkJoinPool common;
    public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
    static final RuntimePermission modifyThreadPermission = new RuntimePermission("modifyThread");
    private static int poolNumberSequence;
    final int bounds;
    volatile long ctl;
    final ForkJoinWorkerThreadFactory factory;
    int indexSeed;
    final long keepAlive;
    volatile int mode;
    final Predicate<? super ForkJoinPool> saturate;
    volatile long stealCount;
    final Thread.UncaughtExceptionHandler ueh;
    WorkQueue[] workQueues;
    final String workerNamePrefix;

    public interface ForkJoinWorkerThreadFactory {
        ForkJoinWorkerThread newThread(ForkJoinPool forkJoinPool);
    }

    public interface ManagedBlocker {
        boolean block() throws InterruptedException;

        boolean isReleasable();
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.managedBlock(java.util.concurrent.ForkJoinPool$ManagedBlocker):void, dex: classes4.dex
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
    public static void managedBlock(java.util.concurrent.ForkJoinPool.ManagedBlocker r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.managedBlock(java.util.concurrent.ForkJoinPool$ManagedBlocker):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.managedBlock(java.util.concurrent.ForkJoinPool$ManagedBlocker):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.scan(java.util.concurrent.ForkJoinPool$WorkQueue, int):boolean, dex: classes4.dex
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
    private boolean scan(java.util.concurrent.ForkJoinPool.WorkQueue r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.scan(java.util.concurrent.ForkJoinPool$WorkQueue, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.scan(java.util.concurrent.ForkJoinPool$WorkQueue, int):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.tryAddWorker(long):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private void tryAddWorker(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.tryAddWorker(long):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.tryAddWorker(long):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.tryCompensate(java.util.concurrent.ForkJoinPool$WorkQueue):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    private int tryCompensate(java.util.concurrent.ForkJoinPool.WorkQueue r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.tryCompensate(java.util.concurrent.ForkJoinPool$WorkQueue):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.tryCompensate(java.util.concurrent.ForkJoinPool$WorkQueue):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.tryTerminate(boolean, boolean):boolean, dex: classes4.dex
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
    private boolean tryTerminate(boolean r1, boolean r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.tryTerminate(boolean, boolean):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.tryTerminate(boolean, boolean):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.awaitJoin(java.util.concurrent.ForkJoinPool$WorkQueue, java.util.concurrent.ForkJoinTask, long):int, dex: classes4.dex
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
    final int awaitJoin(java.util.concurrent.ForkJoinPool.WorkQueue r1, java.util.concurrent.ForkJoinTask<?> r2, long r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.awaitJoin(java.util.concurrent.ForkJoinPool$WorkQueue, java.util.concurrent.ForkJoinTask, long):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.awaitJoin(java.util.concurrent.ForkJoinPool$WorkQueue, java.util.concurrent.ForkJoinTask, long):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.deregisterWorker(java.util.concurrent.ForkJoinWorkerThread, java.lang.Throwable):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void deregisterWorker(java.util.concurrent.ForkJoinWorkerThread r1, java.lang.Throwable r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.deregisterWorker(java.util.concurrent.ForkJoinWorkerThread, java.lang.Throwable):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.deregisterWorker(java.util.concurrent.ForkJoinWorkerThread, java.lang.Throwable):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.helpQuiescePool(java.util.concurrent.ForkJoinPool$WorkQueue):void, dex: classes4.dex
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
    final void helpQuiescePool(java.util.concurrent.ForkJoinPool.WorkQueue r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.helpQuiescePool(java.util.concurrent.ForkJoinPool$WorkQueue):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.helpQuiescePool(java.util.concurrent.ForkJoinPool$WorkQueue):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.runWorker(java.util.concurrent.ForkJoinPool$WorkQueue):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void runWorker(java.util.concurrent.ForkJoinPool.WorkQueue r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.runWorker(java.util.concurrent.ForkJoinPool$WorkQueue):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.runWorker(java.util.concurrent.ForkJoinPool$WorkQueue):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.signalWork():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void signalWork() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.ForkJoinPool.signalWork():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.signalWork():void");
    }

    private static void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(modifyThreadPermission);
        }
    }

    static AccessControlContext contextWithPermissions(Permission... permissionArr) {
        Permissions permissions = new Permissions();
        for (Permission add : permissionArr) {
            permissions.add(add);
        }
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain((CodeSource) null, permissions)});
    }

    private static final class DefaultForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        private static final AccessControlContext ACC = ForkJoinPool.contextWithPermissions(new RuntimePermission("getClassLoader"), new RuntimePermission("setContextClassLoader"));

        private DefaultForkJoinWorkerThreadFactory() {
        }

        public final ForkJoinWorkerThread newThread(final ForkJoinPool forkJoinPool) {
            return (ForkJoinWorkerThread) AccessController.doPrivileged(new PrivilegedAction<ForkJoinWorkerThread>() {
                public ForkJoinWorkerThread run() {
                    return new ForkJoinWorkerThread(forkJoinPool, ClassLoader.getSystemClassLoader());
                }
            }, ACC);
        }
    }

    static final class WorkQueue {
        static final VarHandle BASE;
        static final VarHandle PHASE;
        static final VarHandle TOP;
        ForkJoinTask<?>[] array;
        int base = 4096;

        /* renamed from: id */
        int f754id;
        int nsteals;
        final ForkJoinWorkerThread owner;
        volatile int phase;
        final ForkJoinPool pool;
        volatile int source;
        int stackPred;
        int top = 4096;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.growArray(boolean):void, dex: classes4.dex
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
        final void growArray(boolean r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.growArray(boolean):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.growArray(boolean):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.helpAsyncBlocker(java.util.concurrent.ForkJoinPool$ManagedBlocker):void, dex: classes4.dex
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
        final void helpAsyncBlocker(java.util.concurrent.ForkJoinPool.ManagedBlocker r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.helpAsyncBlocker(java.util.concurrent.ForkJoinPool$ManagedBlocker):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.helpAsyncBlocker(java.util.concurrent.ForkJoinPool$ManagedBlocker):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.helpCC(java.util.concurrent.CountedCompleter, int, boolean):int, dex: classes4.dex
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
        final int helpCC(java.util.concurrent.CountedCompleter<?> r1, int r2, boolean r3) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.helpCC(java.util.concurrent.CountedCompleter, int, boolean):int, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.helpCC(java.util.concurrent.CountedCompleter, int, boolean):int");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.nextLocalTask():java.util.concurrent.ForkJoinTask<?>, dex: classes4.dex
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
        final java.util.concurrent.ForkJoinTask<?> nextLocalTask() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.nextLocalTask():java.util.concurrent.ForkJoinTask<?>, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.nextLocalTask():java.util.concurrent.ForkJoinTask");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.poll():java.util.concurrent.ForkJoinTask<?>, dex: classes4.dex
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
        final java.util.concurrent.ForkJoinTask<?> poll() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.poll():java.util.concurrent.ForkJoinTask<?>, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.poll():java.util.concurrent.ForkJoinTask");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.push(java.util.concurrent.ForkJoinTask):void, dex: classes4.dex
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
        final void push(java.util.concurrent.ForkJoinTask<?> r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.push(java.util.concurrent.ForkJoinTask):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.push(java.util.concurrent.ForkJoinTask):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.queueSize():int, dex: classes4.dex
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
        final int queueSize() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.queueSize():int, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.queueSize():int");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.releasePhaseLock():void, dex: classes4.dex
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
        final void releasePhaseLock() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.releasePhaseLock():void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.releasePhaseLock():void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryLockPhase():boolean, dex: classes4.dex
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
        final boolean tryLockPhase() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryLockPhase():boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.tryLockPhase():boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryLockedUnpush(java.util.concurrent.ForkJoinTask):boolean, dex: classes4.dex
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
        final boolean tryLockedUnpush(java.util.concurrent.ForkJoinTask<?> r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryLockedUnpush(java.util.concurrent.ForkJoinTask):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.tryLockedUnpush(java.util.concurrent.ForkJoinTask):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryRemoveAndExec(java.util.concurrent.ForkJoinTask):void, dex: classes4.dex
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
        final void tryRemoveAndExec(java.util.concurrent.ForkJoinTask<?> r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryRemoveAndExec(java.util.concurrent.ForkJoinTask):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.tryRemoveAndExec(java.util.concurrent.ForkJoinTask):void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryUnpush(java.util.concurrent.ForkJoinTask):boolean, dex: classes4.dex
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
        final boolean tryUnpush(java.util.concurrent.ForkJoinTask<?> r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.ForkJoinPool.WorkQueue.tryUnpush(java.util.concurrent.ForkJoinTask):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.tryUnpush(java.util.concurrent.ForkJoinTask):boolean");
        }

        WorkQueue(ForkJoinPool forkJoinPool, ForkJoinWorkerThread forkJoinWorkerThread) {
            this.pool = forkJoinPool;
            this.owner = forkJoinWorkerThread;
        }

        /* access modifiers changed from: package-private */
        public final int getPoolIndex() {
            return (this.f754id & 65535) >>> 1;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:6:0x0013, code lost:
            r1 = (r4 = r4.array).length;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean isEmpty() {
            /*
                r4 = this;
                java.lang.invoke.VarHandle.acquireFence()
                int r0 = r4.base
                int r1 = r4.top
                int r1 = r0 - r1
                r2 = 1
                if (r1 >= 0) goto L_0x001e
                r3 = -1
                if (r1 != r3) goto L_0x001d
                java.util.concurrent.ForkJoinTask<?>[] r4 = r4.array
                if (r4 == 0) goto L_0x001e
                int r1 = r4.length
                if (r1 == 0) goto L_0x001e
                int r1 = r1 - r2
                r0 = r0 & r1
                r4 = r4[r0]
                if (r4 != 0) goto L_0x001d
                goto L_0x001e
            L_0x001d:
                r2 = 0
            L_0x001e:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.isEmpty():boolean");
        }

        /* access modifiers changed from: package-private */
        public final boolean lockedPush(ForkJoinTask<?> forkJoinTask) {
            int length;
            int i = this.top;
            int i2 = this.base;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr == null || (length = forkJoinTaskArr.length) <= 0) {
                return false;
            }
            forkJoinTaskArr[(length - 1) & i] = forkJoinTask;
            this.top = i + 1;
            if (((i2 - i) + length) - 1 == 0) {
                growArray(true);
                return false;
            }
            this.phase = 0;
            if (((i - this.base) & -2) == 0) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public final ForkJoinTask<?> peek() {
            int length;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr == null || (length = forkJoinTaskArr.length) <= 0) {
                return null;
            }
            return forkJoinTaskArr[((this.f754id & 65536) != 0 ? this.base : this.top - 1) & (length - 1)];
        }

        /* access modifiers changed from: package-private */
        public final void cancelAll() {
            while (true) {
                ForkJoinTask<?> poll = poll();
                if (poll != null) {
                    ForkJoinTask.cancelIgnoringExceptions(poll);
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void topLevelExec(ForkJoinTask<?> forkJoinTask, WorkQueue workQueue, int i) {
            if (forkJoinTask != null && workQueue != null) {
                int i2 = 1;
                while (true) {
                    forkJoinTask.doExec();
                    int i3 = i - 1;
                    if (i < 0) {
                        break;
                    }
                    ForkJoinTask<?> nextLocalTask = nextLocalTask();
                    if (nextLocalTask == null) {
                        nextLocalTask = workQueue.poll();
                        if (nextLocalTask == null) {
                            break;
                        }
                        i2++;
                    }
                    ForkJoinTask<?> forkJoinTask2 = nextLocalTask;
                    i = i3;
                    forkJoinTask = forkJoinTask2;
                }
                ForkJoinWorkerThread forkJoinWorkerThread = this.owner;
                this.nsteals += i2;
                this.source = 0;
                if (forkJoinWorkerThread != null) {
                    forkJoinWorkerThread.afterTopLevelExec();
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
            r1 = r1.getState();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean isApparentlyUnblocked() {
            /*
                r1 = this;
                java.util.concurrent.ForkJoinWorkerThread r1 = r1.owner
                if (r1 == 0) goto L_0x0016
                java.lang.Thread$State r1 = r1.getState()
                java.lang.Thread$State r0 = java.lang.Thread.State.BLOCKED
                if (r1 == r0) goto L_0x0016
                java.lang.Thread$State r0 = java.lang.Thread.State.WAITING
                if (r1 == r0) goto L_0x0016
                java.lang.Thread$State r0 = java.lang.Thread.State.TIMED_WAITING
                if (r1 == r0) goto L_0x0016
                r1 = 1
                goto L_0x0017
            L_0x0016:
                r1 = 0
            L_0x0017:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.WorkQueue.isApparentlyUnblocked():boolean");
        }

        static {
            Class<WorkQueue> cls = WorkQueue.class;
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                PHASE = lookup.findVarHandle(cls, "phase", Integer.TYPE);
                BASE = lookup.findVarHandle(cls, "base", Integer.TYPE);
                TOP = lookup.findVarHandle(cls, "top", Integer.TYPE);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }
    }

    private static final synchronized int nextPoolId() {
        int i;
        synchronized (ForkJoinPool.class) {
            i = poolNumberSequence + 1;
            poolNumberSequence = i;
        }
        return i;
    }

    private boolean createWorker() {
        ForkJoinWorkerThread forkJoinWorkerThread;
        ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = this.factory;
        Throwable th = null;
        if (forkJoinWorkerThreadFactory != null) {
            try {
                forkJoinWorkerThread = forkJoinWorkerThreadFactory.newThread(this);
                if (forkJoinWorkerThread != null) {
                    try {
                        forkJoinWorkerThread.start();
                        return true;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                forkJoinWorkerThread = null;
            }
        } else {
            forkJoinWorkerThread = null;
        }
        deregisterWorker(forkJoinWorkerThread, th);
        return false;
    }

    /* access modifiers changed from: package-private */
    public final WorkQueue registerWorker(ForkJoinWorkerThread forkJoinWorkerThread) {
        int i;
        int length;
        forkJoinWorkerThread.setDaemon(true);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.ueh;
        if (uncaughtExceptionHandler != null) {
            forkJoinWorkerThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        int i2 = this.mode & 65536;
        String str = this.workerNamePrefix;
        WorkQueue workQueue = new WorkQueue(this, forkJoinWorkerThread);
        if (str != null) {
            synchronized (str) {
                WorkQueue[] workQueueArr = this.workQueues;
                int i3 = this.indexSeed + SEED_INCREMENT;
                this.indexSeed = i3;
                int i4 = i2 | (1073610752 & i3);
                i = 0;
                if (workQueueArr != null && (length = workQueueArr.length) > 1) {
                    int i5 = length - 1;
                    int i6 = ((i3 << 1) | 1) & i5;
                    int i7 = length >>> 1;
                    while (true) {
                        WorkQueue workQueue2 = workQueueArr[i6];
                        if (workQueue2 == null) {
                            break;
                        } else if (workQueue2.phase == 1073741824) {
                            break;
                        } else {
                            i7--;
                            if (i7 == 0) {
                                i6 = length | 1;
                                break;
                            }
                            i6 = (i6 + 2) & i5;
                        }
                    }
                    int i8 = i4 | i6;
                    workQueue.f754id = i8;
                    workQueue.phase = i8;
                    if (i6 < length) {
                        workQueueArr[i6] = workQueue;
                    } else {
                        int i9 = length << 1;
                        WorkQueue[] workQueueArr2 = new WorkQueue[i9];
                        workQueueArr2[i6] = workQueue;
                        int i10 = i9 - 1;
                        while (true) {
                            if (i >= length) {
                                break;
                            }
                            WorkQueue workQueue3 = workQueueArr[i];
                            if (workQueue3 != null) {
                                workQueueArr2[workQueue3.f754id & i10 & 126] = workQueue3;
                            }
                            int i11 = i + 1;
                            if (i11 >= length) {
                                break;
                            }
                            workQueueArr2[i11] = workQueueArr[i11];
                            i = i11 + 1;
                        }
                        this.workQueues = workQueueArr2;
                    }
                    i = i6;
                }
            }
            forkJoinWorkerThread.setName(str.concat(Integer.toString(i)));
        }
        return workQueue;
    }

    private ForkJoinTask<?> pollScan(boolean z) {
        WorkQueue[] workQueueArr;
        int length;
        int i;
        int i2;
        if ((this.mode & Integer.MIN_VALUE) != 0 || (workQueueArr = this.workQueues) == null || (length = workQueueArr.length) <= 0) {
            return null;
        }
        int i3 = length - 1;
        int nextSecondarySeed = ThreadLocalRandom.nextSecondarySeed();
        int i4 = nextSecondarySeed >>> 16;
        if (z) {
            i = nextSecondarySeed & -2 & i3;
            i2 = (i4 & -2) | 2;
        } else {
            i = nextSecondarySeed & i3;
            i2 = i4 | 1;
        }
        int i5 = i;
        int i6 = 0;
        boolean z2 = false;
        int i7 = 0;
        while (true) {
            WorkQueue workQueue = workQueueArr[i5];
            if (workQueue != null) {
                int i8 = workQueue.top;
                int i9 = workQueue.base;
                if (i8 - i9 > 0) {
                    ForkJoinTask<?> poll = workQueue.poll();
                    if (poll != null) {
                        return poll;
                    }
                    z2 = true;
                } else {
                    i6 += i9 + workQueue.f754id;
                }
            }
            i5 = (i5 + i2) & i3;
            if (i5 == i) {
                if (!z2) {
                    if (i7 == i6) {
                        return null;
                    }
                    i7 = i6;
                }
                i6 = 0;
                z2 = false;
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r1 = r1.nextLocalTask();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.util.concurrent.ForkJoinTask<?> nextTaskFor(java.util.concurrent.ForkJoinPool.WorkQueue r1) {
        /*
            r0 = this;
            if (r1 == 0) goto L_0x0008
            java.util.concurrent.ForkJoinTask r1 = r1.nextLocalTask()
            if (r1 != 0) goto L_0x000d
        L_0x0008:
            r1 = 0
            java.util.concurrent.ForkJoinTask r1 = r0.pollScan(r1)
        L_0x000d:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.nextTaskFor(java.util.concurrent.ForkJoinPool$WorkQueue):java.util.concurrent.ForkJoinTask");
    }

    /* access modifiers changed from: package-private */
    public final void externalPush(ForkJoinTask<?> forkJoinTask) {
        int length;
        int length2;
        int probe = ThreadLocalRandom.getProbe();
        if (probe == 0) {
            ThreadLocalRandom.localInit();
            probe = ThreadLocalRandom.getProbe();
        }
        while (true) {
            int i = this.mode;
            WorkQueue[] workQueueArr = this.workQueues;
            if ((i & 262144) == 0 && workQueueArr != null && (length = workQueueArr.length) > 0) {
                WorkQueue workQueue = workQueueArr[(length - 1) & probe & 126];
                if (workQueue == null) {
                    int i2 = (probe | 1073741824) & -65538;
                    String str = this.workerNamePrefix;
                    WorkQueue workQueue2 = new WorkQueue(this, (ForkJoinWorkerThread) null);
                    workQueue2.array = new ForkJoinTask[8192];
                    workQueue2.f754id = i2;
                    workQueue2.source = 1073741824;
                    if (str != null) {
                        synchronized (str) {
                            WorkQueue[] workQueueArr2 = this.workQueues;
                            if (workQueueArr2 != null && (length2 = workQueueArr2.length) > 0) {
                                int i3 = i2 & (length2 - 1) & 126;
                                if (workQueueArr2[i3] == null) {
                                    workQueueArr2[i3] = workQueue2;
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                } else if (!workQueue.tryLockPhase()) {
                    probe = ThreadLocalRandom.advanceProbe(probe);
                } else if (workQueue.lockedPush(forkJoinTask)) {
                    signalWork();
                    return;
                } else {
                    return;
                }
            }
        }
        throw new RejectedExecutionException();
    }

    private <T> ForkJoinTask<T> externalSubmit(ForkJoinTask<T> forkJoinTask) {
        WorkQueue workQueue;
        forkJoinTask.getClass();
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread;
            if (forkJoinWorkerThread.pool == this && (workQueue = forkJoinWorkerThread.workQueue) != null) {
                workQueue.push(forkJoinTask);
                return forkJoinTask;
            }
        }
        externalPush(forkJoinTask);
        return forkJoinTask;
    }

    static WorkQueue commonSubmitterQueue() {
        WorkQueue[] workQueueArr;
        int length;
        ForkJoinPool forkJoinPool = common;
        int probe = ThreadLocalRandom.getProbe();
        if (forkJoinPool == null || (workQueueArr = forkJoinPool.workQueues) == null || (length = workQueueArr.length) <= 0) {
            return null;
        }
        return workQueueArr[probe & (length - 1) & 126];
    }

    /* access modifiers changed from: package-private */
    public final boolean tryExternalUnpush(ForkJoinTask<?> forkJoinTask) {
        int length;
        WorkQueue workQueue;
        int probe = ThreadLocalRandom.getProbe();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr == null || (length = workQueueArr.length) <= 0 || (workQueue = workQueueArr[probe & (length - 1) & 126]) == null || !workQueue.tryLockedUnpush(forkJoinTask)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public final int externalHelpComplete(CountedCompleter<?> countedCompleter, int i) {
        int length;
        WorkQueue workQueue;
        int probe = ThreadLocalRandom.getProbe();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr == null || (length = workQueueArr.length) <= 0 || (workQueue = workQueueArr[probe & (length - 1) & 126]) == null) {
            return 0;
        }
        return workQueue.helpCC(countedCompleter, i, true);
    }

    /* access modifiers changed from: package-private */
    public final int helpComplete(WorkQueue workQueue, CountedCompleter<?> countedCompleter, int i) {
        if (workQueue == null) {
            return 0;
        }
        return workQueue.helpCC(countedCompleter, i, false);
    }

    static int getSurplusQueuedTaskCount() {
        ForkJoinWorkerThread forkJoinWorkerThread;
        ForkJoinPool forkJoinPool;
        WorkQueue workQueue;
        Thread currentThread = Thread.currentThread();
        int i = 0;
        if (!(currentThread instanceof ForkJoinWorkerThread) || (forkJoinPool = (forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread).pool) == null || (workQueue = forkJoinWorkerThread.workQueue) == null) {
            return 0;
        }
        int i2 = forkJoinPool.mode & 65535;
        int i3 = ((int) (forkJoinPool.ctl >> 48)) + i2;
        int i4 = workQueue.top - workQueue.base;
        int i5 = i2 >>> 1;
        if (i3 <= i5) {
            int i6 = i5 >>> 1;
            if (i3 > i6) {
                i = 1;
            } else {
                int i7 = i6 >>> 1;
                i = i3 > i7 ? 2 : i3 > (i7 >>> 1) ? 4 : 8;
            }
        }
        return i4 - i;
    }

    public ForkJoinPool() {
        this(Math.min(32767, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, (Thread.UncaughtExceptionHandler) null, false, 0, 32767, 1, (Predicate<? super ForkJoinPool>) null, DEFAULT_KEEPALIVE, TimeUnit.MILLISECONDS);
    }

    public ForkJoinPool(int i) {
        this(i, defaultForkJoinWorkerThreadFactory, (Thread.UncaughtExceptionHandler) null, false, 0, 32767, 1, (Predicate<? super ForkJoinPool>) null, DEFAULT_KEEPALIVE, TimeUnit.MILLISECONDS);
    }

    public ForkJoinPool(int i, ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, boolean z) {
        this(i, forkJoinWorkerThreadFactory, uncaughtExceptionHandler, z, 0, 32767, 1, (Predicate<? super ForkJoinPool>) null, DEFAULT_KEEPALIVE, TimeUnit.MILLISECONDS);
    }

    public ForkJoinPool(int i, ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, boolean z, int i2, int i3, int i4, Predicate<? super ForkJoinPool> predicate, long j, TimeUnit timeUnit) {
        int i5 = i;
        int i6 = i3;
        long j2 = j;
        if (i5 <= 0 || i5 > 32767 || i6 < i5 || j2 <= 0) {
            throw new IllegalArgumentException();
        }
        forkJoinWorkerThreadFactory.getClass();
        long max = Math.max(timeUnit.toMillis(j2), (long) TIMEOUT_SLOP);
        long j3 = ((((long) (-Math.min(Math.max(i2, i), 32767))) << 32) & TC_MASK) | ((((long) (-i5)) << 48) & RC_MASK);
        int i7 = (z ? 65536 : 0) | i5;
        int min = ((Math.min(i6, 32767) - i5) << 16) | ((Math.min(Math.max(i4, 0), 32767) - i5) & 65535);
        int i8 = i5 > 1 ? i5 - 1 : 1;
        int i9 = i8 | (i8 >>> 1);
        int i10 = i9 | (i9 >>> 2);
        int i11 = i10 | (i10 >>> 4);
        int i12 = i11 | (i11 >>> 8);
        this.workerNamePrefix = "ForkJoinPool-" + nextPoolId() + "-worker-";
        this.workQueues = new WorkQueue[(((i12 | (i12 >>> 16)) + 1) << 1)];
        this.factory = forkJoinWorkerThreadFactory;
        this.ueh = uncaughtExceptionHandler;
        this.saturate = predicate;
        this.keepAlive = max;
        this.bounds = min;
        this.mode = i7;
        this.ctl = j3;
        checkPermission();
    }

    private static Object newInstanceFromSystemProperty(String str) throws ReflectiveOperationException {
        String property = System.getProperty(str);
        if (property == null) {
            return null;
        }
        return ClassLoader.getSystemClassLoader().loadClass(property).getConstructor(new Class[0]).newInstance(new Object[0]);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0068  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ForkJoinPool(byte r11) {
        /*
            r10 = this;
            r10.<init>()
            r11 = -1
            r0 = 0
            java.lang.String r1 = "java.util.concurrent.ForkJoinPool.common.parallelism"
            java.lang.String r1 = java.lang.System.getProperty(r1)     // Catch:{ Exception -> 0x0022 }
            if (r1 == 0) goto L_0x0011
            int r11 = java.lang.Integer.parseInt(r1)     // Catch:{ Exception -> 0x0022 }
        L_0x0011:
            java.lang.String r1 = "java.util.concurrent.ForkJoinPool.common.threadFactory"
            java.lang.Object r1 = newInstanceFromSystemProperty(r1)     // Catch:{ Exception -> 0x0022 }
            java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory r1 = (java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory) r1     // Catch:{ Exception -> 0x0022 }
            java.lang.String r2 = "java.util.concurrent.ForkJoinPool.common.exceptionHandler"
            java.lang.Object r2 = newInstanceFromSystemProperty(r2)     // Catch:{ Exception -> 0x0023 }
            java.lang.Thread$UncaughtExceptionHandler r2 = (java.lang.Thread.UncaughtExceptionHandler) r2     // Catch:{ Exception -> 0x0023 }
            goto L_0x0024
        L_0x0022:
            r1 = r0
        L_0x0023:
            r2 = r0
        L_0x0024:
            if (r1 != 0) goto L_0x0034
            java.lang.SecurityManager r1 = java.lang.System.getSecurityManager()
            if (r1 != 0) goto L_0x002f
            java.util.concurrent.ForkJoinPool$ForkJoinWorkerThreadFactory r1 = defaultForkJoinWorkerThreadFactory
            goto L_0x0034
        L_0x002f:
            java.util.concurrent.ForkJoinPool$InnocuousForkJoinWorkerThreadFactory r1 = new java.util.concurrent.ForkJoinPool$InnocuousForkJoinWorkerThreadFactory
            r1.<init>()
        L_0x0034:
            r3 = 1
            if (r11 >= 0) goto L_0x0043
            java.lang.Runtime r11 = java.lang.Runtime.getRuntime()
            int r11 = r11.availableProcessors()
            int r11 = r11 - r3
            if (r11 > 0) goto L_0x0043
            r11 = r3
        L_0x0043:
            r4 = 32767(0x7fff, float:4.5916E-41)
            if (r11 <= r4) goto L_0x0048
            r11 = r4
        L_0x0048:
            int r4 = -r11
            long r4 = (long) r4
            r6 = 32
            long r6 = r4 << r6
            r8 = 281470681743360(0xffff00000000, double:1.39064994160909E-309)
            long r6 = r6 & r8
            r8 = 48
            long r4 = r4 << r8
            r8 = -281474976710656(0xffff000000000000, double:NaN)
            long r4 = r4 & r8
            long r4 = r4 | r6
            int r6 = 1 - r11
            r7 = 65535(0xffff, float:9.1834E-41)
            r6 = r6 & r7
            int r7 = COMMON_MAX_SPARES
            int r7 = r7 << 16
            r6 = r6 | r7
            if (r11 <= r3) goto L_0x006b
            int r7 = r11 + -1
            goto L_0x006c
        L_0x006b:
            r7 = r3
        L_0x006c:
            int r8 = r7 >>> 1
            r7 = r7 | r8
            int r8 = r7 >>> 2
            r7 = r7 | r8
            int r8 = r7 >>> 4
            r7 = r7 | r8
            int r8 = r7 >>> 8
            r7 = r7 | r8
            int r8 = r7 >>> 16
            r7 = r7 | r8
            int r7 = r7 + r3
            int r3 = r7 << 1
            java.lang.String r7 = "ForkJoinPool.commonPool-worker-"
            r10.workerNamePrefix = r7
            java.util.concurrent.ForkJoinPool$WorkQueue[] r3 = new java.util.concurrent.ForkJoinPool.WorkQueue[r3]
            r10.workQueues = r3
            r10.factory = r1
            r10.ueh = r2
            r10.saturate = r0
            r0 = 60000(0xea60, double:2.9644E-319)
            r10.keepAlive = r0
            r10.bounds = r6
            r10.mode = r11
            r10.ctl = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.<init>(byte):void");
    }

    public static ForkJoinPool commonPool() {
        return common;
    }

    public <T> T invoke(ForkJoinTask<T> forkJoinTask) {
        forkJoinTask.getClass();
        externalSubmit(forkJoinTask);
        return forkJoinTask.join();
    }

    public void execute(ForkJoinTask<?> forkJoinTask) {
        externalSubmit(forkJoinTask);
    }

    public void execute(Runnable runnable) {
        ForkJoinTask forkJoinTask;
        runnable.getClass();
        if (runnable instanceof ForkJoinTask) {
            forkJoinTask = (ForkJoinTask) runnable;
        } else {
            forkJoinTask = new ForkJoinTask.RunnableExecuteAction(runnable);
        }
        externalSubmit(forkJoinTask);
    }

    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> forkJoinTask) {
        return externalSubmit(forkJoinTask);
    }

    public <T> ForkJoinTask<T> submit(Callable<T> callable) {
        return externalSubmit(new ForkJoinTask.AdaptedCallable(callable));
    }

    public <T> ForkJoinTask<T> submit(Runnable runnable, T t) {
        return externalSubmit(new ForkJoinTask.AdaptedRunnable(runnable, t));
    }

    public ForkJoinTask<?> submit(Runnable runnable) {
        ForkJoinTask forkJoinTask;
        runnable.getClass();
        if (runnable instanceof ForkJoinTask) {
            forkJoinTask = (ForkJoinTask) runnable;
        } else {
            forkJoinTask = new ForkJoinTask.AdaptedRunnableAction(runnable);
        }
        return externalSubmit(forkJoinTask);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        try {
            for (Callable adaptedCallable : collection) {
                ForkJoinTask.AdaptedCallable adaptedCallable2 = new ForkJoinTask.AdaptedCallable(adaptedCallable);
                arrayList.add(adaptedCallable2);
                externalSubmit(adaptedCallable2);
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((ForkJoinTask) arrayList.get(i)).quietlyJoin();
            }
            return arrayList;
        } catch (Throwable th) {
            int size2 = arrayList.size();
            for (int i2 = 0; i2 < size2; i2++) {
                ((Future) arrayList.get(i2)).cancel(false);
            }
            throw th;
        }
    }

    public ForkJoinWorkerThreadFactory getFactory() {
        return this.factory;
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.ueh;
    }

    public int getParallelism() {
        int i = this.mode & 65535;
        if (i > 0) {
            return i;
        }
        return 1;
    }

    public static int getCommonPoolParallelism() {
        return COMMON_PARALLELISM;
    }

    public int getPoolSize() {
        return (this.mode & UShort.MAX_VALUE) + ((short) ((int) (this.ctl >>> 32)));
    }

    public boolean getAsyncMode() {
        return (this.mode & 65536) != 0;
    }

    public int getRunningThreadCount() {
        VarHandle.acquireFence();
        WorkQueue[] workQueueArr = this.workQueues;
        int i = 0;
        if (workQueueArr != null) {
            for (int i2 = 1; i2 < workQueueArr.length; i2 += 2) {
                WorkQueue workQueue = workQueueArr[i2];
                if (workQueue != null && workQueue.isApparentlyUnblocked()) {
                    i++;
                }
            }
        }
        return i;
    }

    public int getActiveThreadCount() {
        int i = (this.mode & 65535) + ((int) (this.ctl >> 48));
        if (i <= 0) {
            return 0;
        }
        return i;
    }

    public boolean isQuiescent() {
        while (true) {
            long j = this.ctl;
            int i = this.mode;
            short s = 65535 & i;
            int i2 = ((short) ((int) (j >>> 32))) + s;
            int i3 = s + ((int) (j >> 48));
            if ((i & -2146959360) != 0) {
                return true;
            }
            if (i3 > 0) {
                return false;
            }
            WorkQueue[] workQueueArr = this.workQueues;
            if (workQueueArr != null) {
                for (int i4 = 1; i4 < workQueueArr.length; i4 += 2) {
                    WorkQueue workQueue = workQueueArr[i4];
                    if (workQueue != null) {
                        if (workQueue.source > 0) {
                            return false;
                        }
                        i2--;
                    }
                }
            }
            if (i2 == 0 && this.ctl == j) {
                return true;
            }
        }
    }

    public long getStealCount() {
        long j = this.stealCount;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i = 1; i < workQueueArr.length; i += 2) {
                WorkQueue workQueue = workQueueArr[i];
                if (workQueue != null) {
                    j += ((long) workQueue.nsteals) & 4294967295L;
                }
            }
        }
        return j;
    }

    public long getQueuedTaskCount() {
        VarHandle.acquireFence();
        WorkQueue[] workQueueArr = this.workQueues;
        int i = 0;
        if (workQueueArr != null) {
            for (int i2 = 1; i2 < workQueueArr.length; i2 += 2) {
                WorkQueue workQueue = workQueueArr[i2];
                if (workQueue != null) {
                    i += workQueue.queueSize();
                }
            }
        }
        return (long) i;
    }

    public int getQueuedSubmissionCount() {
        VarHandle.acquireFence();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < workQueueArr.length; i2 += 2) {
            WorkQueue workQueue = workQueueArr[i2];
            if (workQueue != null) {
                i += workQueue.queueSize();
            }
        }
        return i;
    }

    public boolean hasQueuedSubmissions() {
        VarHandle.acquireFence();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i = 0; i < workQueueArr.length; i += 2) {
                WorkQueue workQueue = workQueueArr[i];
                if (workQueue != null && !workQueue.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public ForkJoinTask<?> pollSubmission() {
        return pollScan(true);
    }

    /* access modifiers changed from: protected */
    public int drainTasksTo(Collection<? super ForkJoinTask<?>> collection) {
        VarHandle.acquireFence();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr == null) {
            return 0;
        }
        int i = 0;
        for (WorkQueue workQueue : workQueueArr) {
            if (workQueue != null) {
                while (true) {
                    ForkJoinTask<?> poll = workQueue.poll();
                    if (poll == null) {
                        break;
                    }
                    collection.add(poll);
                    i++;
                }
            }
        }
        return i;
    }

    public String toString() {
        int i;
        long j;
        int i2 = this.mode;
        long j2 = this.ctl;
        long j3 = this.stealCount;
        WorkQueue[] workQueueArr = this.workQueues;
        long j4 = 0;
        if (workQueueArr != null) {
            long j5 = 0;
            i = 0;
            for (int i3 = 0; i3 < workQueueArr.length; i3++) {
                WorkQueue workQueue = workQueueArr[i3];
                if (workQueue != null) {
                    int queueSize = workQueue.queueSize();
                    if ((i3 & 1) == 0) {
                        j5 += (long) queueSize;
                    } else {
                        long j6 = j5;
                        j4 += (long) queueSize;
                        j3 += ((long) workQueue.nsteals) & 4294967295L;
                        if (workQueue.isApparentlyUnblocked()) {
                            i++;
                        }
                        j5 = j6;
                    }
                } else {
                    long j7 = j5;
                }
            }
            j = j5;
        } else {
            j = 0;
            i = 0;
        }
        short s = 65535 & i2;
        int i4 = ((short) ((int) (j2 >>> 32))) + s;
        int i5 = ((int) (j2 >> 48)) + s;
        if (i5 < 0) {
            i5 = 0;
        }
        return super.toString() + NavigationBarInflaterView.SIZE_MOD_START + ((524288 & i2) != 0 ? "Terminated" : (Integer.MIN_VALUE & i2) != 0 ? "Terminating" : (i2 & 262144) != 0 ? "Shutting down" : "Running") + ", parallelism = " + s + ", size = " + i4 + ", active = " + i5 + ", running = " + i + ", steals = " + j3 + ", tasks = " + j4 + ", submissions = " + j + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void shutdown() {
        checkPermission();
        tryTerminate(false, true);
    }

    public List<Runnable> shutdownNow() {
        checkPermission();
        tryTerminate(true, true);
        return Collections.emptyList();
    }

    public boolean isTerminated() {
        return (this.mode & 524288) != 0;
    }

    public boolean isTerminating() {
        int i = this.mode;
        return (Integer.MIN_VALUE & i) != 0 && (i & 524288) == 0;
    }

    public boolean isShutdown() {
        return (this.mode & 262144) != 0;
    }

    public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        } else if (this == common) {
            awaitQuiescence(j, timeUnit);
            return false;
        } else {
            long nanos = timeUnit.toNanos(j);
            if (isTerminated()) {
                return true;
            }
            if (nanos <= 0) {
                return false;
            }
            long nanoTime = System.nanoTime() + nanos;
            synchronized (this) {
                while (!isTerminated()) {
                    if (nanos <= 0) {
                        return false;
                    }
                    long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
                    if (millis <= 0) {
                        millis = 1;
                    }
                    wait(millis);
                    nanos = nanoTime - System.nanoTime();
                }
                return true;
            }
        }
    }

    public boolean awaitQuiescence(long j, TimeUnit timeUnit) {
        long nanos = timeUnit.toNanos(j);
        Thread currentThread = Thread.currentThread();
        if (currentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) currentThread;
            if (forkJoinWorkerThread.pool == this) {
                helpQuiescePool(forkJoinWorkerThread.workQueue);
                return true;
            }
        }
        long nanoTime = System.nanoTime();
        while (true) {
            ForkJoinTask<?> pollScan = pollScan(false);
            if (pollScan != null) {
                pollScan.doExec();
            } else if (isQuiescent()) {
                return true;
            } else {
                if (System.nanoTime() - nanoTime > nanos) {
                    return false;
                }
                Thread.yield();
            }
        }
    }

    static void quiesceCommonPool() {
        common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0021, code lost:
        r1 = (r2 = r2.workQueues).length;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void helpAsyncBlocker(java.util.concurrent.Executor r2, java.util.concurrent.ForkJoinPool.ManagedBlocker r3) {
        /*
            boolean r0 = r2 instanceof java.util.concurrent.ForkJoinPool
            if (r0 == 0) goto L_0x0032
            java.util.concurrent.ForkJoinPool r2 = (java.util.concurrent.ForkJoinPool) r2
            java.lang.Thread r0 = java.lang.Thread.currentThread()
            boolean r1 = r0 instanceof java.util.concurrent.ForkJoinWorkerThread
            if (r1 == 0) goto L_0x0017
            java.util.concurrent.ForkJoinWorkerThread r0 = (java.util.concurrent.ForkJoinWorkerThread) r0
            java.util.concurrent.ForkJoinPool r1 = r0.pool
            if (r1 != r2) goto L_0x0017
            java.util.concurrent.ForkJoinPool$WorkQueue r2 = r0.workQueue
            goto L_0x002d
        L_0x0017:
            int r0 = java.util.concurrent.ThreadLocalRandom.getProbe()
            if (r0 == 0) goto L_0x002c
            java.util.concurrent.ForkJoinPool$WorkQueue[] r2 = r2.workQueues
            if (r2 == 0) goto L_0x002c
            int r1 = r2.length
            if (r1 <= 0) goto L_0x002c
            int r1 = r1 + -1
            r0 = r0 & r1
            r0 = r0 & 126(0x7e, float:1.77E-43)
            r2 = r2[r0]
            goto L_0x002d
        L_0x002c:
            r2 = 0
        L_0x002d:
            if (r2 == 0) goto L_0x0032
            r2.helpAsyncBlocker(r3)
        L_0x0032:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.helpAsyncBlocker(java.util.concurrent.Executor, java.util.concurrent.ForkJoinPool$ManagedBlocker):void");
    }

    /* access modifiers changed from: protected */
    public <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t) {
        return new ForkJoinTask.AdaptedRunnable(runnable, t);
    }

    /* access modifiers changed from: protected */
    public <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ForkJoinTask.AdaptedCallable(callable);
    }

    static {
        Class<ForkJoinPool> cls = ForkJoinPool.class;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            CTL = lookup.findVarHandle(cls, "ctl", Long.TYPE);
            MODE = lookup.findVarHandle(cls, "mode", Integer.TYPE);
            f753QA = MethodHandles.arrayElementVarHandle(ForkJoinTask[].class);
            Class<LockSupport> cls2 = LockSupport.class;
            int i = 256;
            try {
                String property = System.getProperty("java.util.concurrent.ForkJoinPool.common.maximumSpares");
                if (property != null) {
                    i = Integer.parseInt(property);
                }
            } catch (Exception unused) {
            }
            COMMON_MAX_SPARES = i;
            ForkJoinPool forkJoinPool = (ForkJoinPool) AccessController.doPrivileged(new PrivilegedAction<ForkJoinPool>() {
                public ForkJoinPool run() {
                    return new ForkJoinPool((byte) 0);
                }
            });
            common = forkJoinPool;
            COMMON_PARALLELISM = Math.max(forkJoinPool.mode & 65535, 1);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    private static final class InnocuousForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        private static final AccessControlContext ACC = ForkJoinPool.contextWithPermissions(ForkJoinPool.modifyThreadPermission, new RuntimePermission("enableContextClassLoaderOverride"), new RuntimePermission("modifyThreadGroup"), new RuntimePermission("getClassLoader"), new RuntimePermission("setContextClassLoader"));

        private InnocuousForkJoinWorkerThreadFactory() {
        }

        public final ForkJoinWorkerThread newThread(final ForkJoinPool forkJoinPool) {
            return (ForkJoinWorkerThread) AccessController.doPrivileged(new PrivilegedAction<ForkJoinWorkerThread>() {
                public ForkJoinWorkerThread run() {
                    return new ForkJoinWorkerThread.InnocuousForkJoinWorkerThread(forkJoinPool);
                }
            }, ACC);
        }
    }
}
