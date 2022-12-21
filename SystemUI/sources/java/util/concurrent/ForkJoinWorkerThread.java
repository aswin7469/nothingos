package java.util.concurrent;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinWorkerThread extends Thread {
    /* access modifiers changed from: private */
    public static final AccessControlContext INNOCUOUS_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain((CodeSource) null, (PermissionCollection) null)});
    final ForkJoinPool pool;
    final ForkJoinPool.WorkQueue workQueue;

    /* access modifiers changed from: package-private */
    public void afterTopLevelExec() {
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    /* access modifiers changed from: protected */
    public void onTermination(Throwable th) {
    }

    protected ForkJoinWorkerThread(ForkJoinPool forkJoinPool) {
        super("aForkJoinWorkerThread");
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    ForkJoinWorkerThread(ForkJoinPool forkJoinPool, ClassLoader classLoader) {
        super("aForkJoinWorkerThread");
        super.setContextClassLoader(classLoader);
        ThreadLocalRandom.setInheritedAccessControlContext(this, INNOCUOUS_ACC);
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    ForkJoinWorkerThread(ForkJoinPool forkJoinPool, ClassLoader classLoader, ThreadGroup threadGroup, AccessControlContext accessControlContext) {
        super(threadGroup, (Runnable) null, "aForkJoinWorkerThread");
        super.setContextClassLoader(classLoader);
        ThreadLocalRandom.setInheritedAccessControlContext(this, accessControlContext);
        ThreadLocalRandom.eraseThreadLocals(this);
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    public ForkJoinPool getPool() {
        return this.pool;
    }

    public int getPoolIndex() {
        return this.workQueue.getPoolIndex();
    }

    public void run() {
        if (this.workQueue.array == null) {
            try {
                onStart();
                this.pool.runWorker(this.workQueue);
                th = null;
                try {
                    onTermination((Throwable) null);
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable unused) {
            }
            this.pool.deregisterWorker(this, th);
        }
    }

    static final class InnocuousForkJoinWorkerThread extends ForkJoinWorkerThread {
        private static final ThreadGroup innocuousThreadGroup = ((ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() {
            public ThreadGroup run() {
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                while (true) {
                    ThreadGroup parent = threadGroup.getParent();
                    if (parent == null) {
                        return new ThreadGroup(threadGroup, "InnocuousForkJoinWorkerThreadGroup");
                    }
                    threadGroup = parent;
                }
            }
        }));

        public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        }

        InnocuousForkJoinWorkerThread(ForkJoinPool forkJoinPool) {
            super(forkJoinPool, ClassLoader.getSystemClassLoader(), innocuousThreadGroup, ForkJoinWorkerThread.INNOCUOUS_ACC);
        }

        /* access modifiers changed from: package-private */
        public void afterTopLevelExec() {
            ThreadLocalRandom.eraseThreadLocals(this);
        }

        public void setContextClassLoader(ClassLoader classLoader) {
            throw new SecurityException("setContextClassLoader");
        }
    }
}
