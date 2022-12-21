package java.lang;

import android.system.C0308Os;
import android.system.OsConstants;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.lang.ref.FinalizerReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.util.EmptyArray;

public final class Daemons {
    private static final Daemon[] DAEMONS;
    /* access modifiers changed from: private */
    public static long MAX_FINALIZE_NANOS = 10000000000L;
    private static final int NANOS_PER_MILLI = 1000000;
    /* access modifiers changed from: private */
    public static final CountDownLatch POST_ZYGOTE_START_LATCH;
    /* access modifiers changed from: private */
    public static final CountDownLatch PRE_ZYGOTE_START_LATCH;
    private static boolean postZygoteFork = false;

    static {
        Daemon[] daemonArr = {HeapTaskDaemon.INSTANCE, ReferenceQueueDaemon.INSTANCE, FinalizerDaemon.INSTANCE, FinalizerWatchdogDaemon.INSTANCE};
        DAEMONS = daemonArr;
        POST_ZYGOTE_START_LATCH = new CountDownLatch(daemonArr.length);
        PRE_ZYGOTE_START_LATCH = new CountDownLatch(daemonArr.length);
    }

    public static void start() {
        for (Daemon start : DAEMONS) {
            start.start();
        }
    }

    public static void startPostZygoteFork() {
        postZygoteFork = true;
        for (Daemon startPostZygoteFork : DAEMONS) {
            startPostZygoteFork.startPostZygoteFork();
        }
    }

    public static void stop() {
        for (Daemon stop : DAEMONS) {
            stop.stop();
        }
    }

    private static void waitForDaemonStart() throws Exception {
        if (postZygoteFork) {
            POST_ZYGOTE_START_LATCH.await();
        } else {
            PRE_ZYGOTE_START_LATCH.await();
        }
    }

    private static abstract class Daemon implements Runnable {
        private String name;
        private boolean postZygoteFork;
        private Thread thread;

        public abstract void runInternal();

        protected Daemon(String str) {
            this.name = str;
        }

        public synchronized void start() {
            startInternal();
        }

        public synchronized void startPostZygoteFork() {
            this.postZygoteFork = true;
            startInternal();
        }

        public void startInternal() {
            if (this.thread == null) {
                Thread thread2 = new Thread(ThreadGroup.systemThreadGroup, this, this.name);
                this.thread = thread2;
                thread2.setDaemon(true);
                this.thread.setSystemDaemon(true);
                this.thread.start();
                return;
            }
            throw new IllegalStateException("already running");
        }

        public final void run() {
            if (this.postZygoteFork) {
                VMRuntime.getRuntime();
                VMRuntime.setSystemDaemonThreadPriority();
                Daemons.POST_ZYGOTE_START_LATCH.countDown();
            } else {
                Daemons.PRE_ZYGOTE_START_LATCH.countDown();
            }
            try {
                runInternal();
            } catch (Throwable th) {
                System.logE("Uncaught exception in system thread " + this.name, th);
                throw th;
            }
        }

        /* access modifiers changed from: protected */
        public synchronized boolean isRunning() {
            return this.thread != null;
        }

        public synchronized void interrupt() {
            interrupt(this.thread);
        }

        public synchronized void interrupt(Thread thread2) {
            if (thread2 != null) {
                thread2.interrupt();
            } else {
                throw new IllegalStateException("not running");
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(4:6|7|15|8) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x000c */
        /* JADX WARNING: Removed duplicated region for block: B:6:0x000c A[LOOP:0: B:6:0x000c->B:7:?, LOOP_START, SYNTHETIC, Splitter:B:6:0x000c] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void stop() {
            /*
                r2 = this;
                monitor-enter(r2)
                java.lang.Thread r0 = r2.thread     // Catch:{ all -> 0x0018 }
                r1 = 0
                r2.thread = r1     // Catch:{ all -> 0x0018 }
                monitor-exit(r2)     // Catch:{ all -> 0x0018 }
                if (r0 == 0) goto L_0x0010
                r2.interrupt(r0)
            L_0x000c:
                r0.join()     // Catch:{ InterruptedException | OutOfMemoryError -> 0x000c }
                return
            L_0x0010:
                java.lang.IllegalStateException r2 = new java.lang.IllegalStateException
                java.lang.String r0 = "not running"
                r2.<init>((java.lang.String) r0)
                throw r2
            L_0x0018:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x0018 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.Daemons.Daemon.stop():void");
        }

        public synchronized StackTraceElement[] getStackTrace() {
            Thread thread2;
            thread2 = this.thread;
            return thread2 != null ? thread2.getStackTrace() : EmptyArray.STACK_TRACE_ELEMENT;
        }
    }

    private static class ReferenceQueueDaemon extends Daemon {
        /* access modifiers changed from: private */
        public static final ReferenceQueueDaemon INSTANCE = new ReferenceQueueDaemon();
        /* access modifiers changed from: private */
        public final AtomicInteger progressCounter = new AtomicInteger(0);

        ReferenceQueueDaemon() {
            super("ReferenceQueueDaemon");
        }

        public void runInternal() {
            Reference<?> reference;
            FinalizerWatchdogDaemon.INSTANCE.monitoringNeeded(2);
            while (isRunning()) {
                try {
                    synchronized (ReferenceQueue.class) {
                        if (ReferenceQueue.unenqueued == null) {
                            this.progressCounter.incrementAndGet();
                            FinalizerWatchdogDaemon.INSTANCE.monitoringNotNeeded(2);
                            do {
                                ReferenceQueue.class.wait();
                            } while (ReferenceQueue.unenqueued == null);
                            this.progressCounter.incrementAndGet();
                            FinalizerWatchdogDaemon.INSTANCE.monitoringNeeded(2);
                        }
                        reference = ReferenceQueue.unenqueued;
                        ReferenceQueue.unenqueued = null;
                    }
                    ReferenceQueue.enqueuePending(reference, this.progressCounter);
                    FinalizerWatchdogDaemon.INSTANCE.resetTimeouts();
                } catch (InterruptedException | OutOfMemoryError unused) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public ReferenceQueue currentlyProcessing() {
            return ReferenceQueue.getCurrentQueue();
        }
    }

    private static class FinalizerDaemon extends Daemon {
        /* access modifiers changed from: private */
        public static final FinalizerDaemon INSTANCE = new FinalizerDaemon();
        /* access modifiers changed from: private */
        public Object finalizingObject = null;
        /* access modifiers changed from: private */
        public final AtomicInteger progressCounter = new AtomicInteger(0);
        private final ReferenceQueue<Object> queue = FinalizerReference.queue;

        FinalizerDaemon() {
            super("FinalizerDaemon");
        }

        public void runInternal() {
            int i = this.progressCounter.get();
            FinalizerWatchdogDaemon.INSTANCE.monitoringNeeded(1);
            while (isRunning()) {
                try {
                    FinalizerReference finalizerReference = (FinalizerReference) this.queue.poll();
                    if (finalizerReference != null) {
                        this.finalizingObject = finalizerReference.get();
                        i++;
                        this.progressCounter.lazySet(i);
                    } else {
                        this.finalizingObject = null;
                        int i2 = i + 1;
                        this.progressCounter.lazySet(i2);
                        FinalizerWatchdogDaemon.INSTANCE.monitoringNotNeeded(1);
                        finalizerReference = (FinalizerReference) this.queue.remove();
                        this.finalizingObject = finalizerReference.get();
                        i = i2 + 1;
                        this.progressCounter.set(i);
                        FinalizerWatchdogDaemon.INSTANCE.monitoringNeeded(1);
                    }
                    doFinalize(finalizerReference);
                } catch (InterruptedException | OutOfMemoryError unused) {
                }
            }
        }

        private void doFinalize(FinalizerReference<?> finalizerReference) {
            FinalizerReference.remove(finalizerReference);
            Object obj = finalizerReference.get();
            finalizerReference.clear();
            try {
                obj.finalize();
            } catch (Throwable th) {
                this.finalizingObject = null;
                throw th;
            }
            this.finalizingObject = null;
        }
    }

    private static class FinalizerWatchdogDaemon extends Daemon {
        static final int FINALIZER_DAEMON = 1;
        /* access modifiers changed from: private */
        public static final FinalizerWatchdogDaemon INSTANCE = new FinalizerWatchdogDaemon();
        static final int RQ_DAEMON = 2;
        private static final int TOLERATED_REFERENCE_QUEUE_TIMEOUTS = 5;
        private static final AtomicInteger observedReferenceQueueTimeouts = new AtomicInteger(0);
        private int activeWatchees;
        private long finalizerTimeoutNs = 0;

        FinalizerWatchdogDaemon() {
            super("FinalizerWatchdogDaemon");
        }

        /* access modifiers changed from: package-private */
        public void resetTimeouts() {
            observedReferenceQueueTimeouts.lazySet(0);
        }

        public void runInternal() {
            TimeoutException waitForProgress;
            while (isRunning()) {
                if (sleepUntilNeeded() && (waitForProgress = waitForProgress()) != null && !VMDebug.isDebuggerConnected()) {
                    timedOut(waitForProgress);
                    return;
                }
            }
        }

        private synchronized boolean sleepUntilNeeded() {
            while (this.activeWatchees == 0) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    return false;
                } catch (OutOfMemoryError unused2) {
                    return false;
                }
            }
            return true;
        }

        /* access modifiers changed from: private */
        public synchronized void monitoringNotNeeded(int i) {
            this.activeWatchees = (~i) & this.activeWatchees;
        }

        /* access modifiers changed from: private */
        public synchronized void monitoringNeeded(int i) {
            int i2 = this.activeWatchees;
            this.activeWatchees = i | i2;
            if (i2 == 0) {
                notify();
            }
        }

        private synchronized boolean isActive(int i) {
            return (i & this.activeWatchees) != 0;
        }

        private boolean sleepForNanos(long j) {
            long nanoTime = System.nanoTime();
            while (true) {
                long nanoTime2 = j - (System.nanoTime() - nanoTime);
                if (nanoTime2 <= 0) {
                    return true;
                }
                try {
                    Thread.sleep(((nanoTime2 + 1000000) - 1) / 1000000);
                } catch (InterruptedException unused) {
                    if (!isRunning()) {
                        return false;
                    }
                } catch (OutOfMemoryError unused2) {
                    if (!isRunning()) {
                        return false;
                    }
                }
            }
        }

        private TimeoutException waitForProgress() {
            boolean z;
            int i;
            int i2;
            if (this.finalizerTimeoutNs == 0) {
                long finalizerTimeoutMs = VMRuntime.getRuntime().getFinalizerTimeoutMs() * 1000000;
                this.finalizerTimeoutNs = finalizerTimeoutMs;
                Daemons.MAX_FINALIZE_NANOS = finalizerTimeoutMs;
            }
            boolean z2 = false;
            if (isActive(1)) {
                i = FinalizerDaemon.INSTANCE.progressCounter.get();
                z = true;
            } else {
                i = 0;
                z = false;
            }
            if (isActive(2)) {
                i2 = ReferenceQueueDaemon.INSTANCE.progressCounter.get();
                z2 = true;
            } else {
                i2 = 0;
            }
            if (!sleepForNanos(this.finalizerTimeoutNs)) {
                return null;
            }
            if (FinalizerDaemon.INSTANCE.progressCounter.get() == i && z && isActive(1)) {
                Object r3 = FinalizerDaemon.INSTANCE.finalizingObject;
                sleepForNanos(500000000);
                if (isActive(1) && FinalizerDaemon.INSTANCE.progressCounter.get() == i) {
                    return finalizerTimeoutException(r3);
                }
            }
            if (ReferenceQueueDaemon.INSTANCE.progressCounter.get() != i2 || !z2 || !isActive(2) || observedReferenceQueueTimeouts.incrementAndGet() <= 5) {
                return null;
            }
            return refQueueTimeoutException(ReferenceQueueDaemon.INSTANCE.currentlyProcessing());
        }

        private static TimeoutException finalizerTimeoutException(Object obj) {
            TimeoutException timeoutException = new TimeoutException(obj.getClass().getName() + ".finalize() timed out after " + (VMRuntime.getRuntime().getFinalizerTimeoutMs() / 1000) + " seconds");
            timeoutException.setStackTrace(FinalizerDaemon.INSTANCE.getStackTrace());
            return timeoutException;
        }

        private static TimeoutException refQueueTimeoutException(ReferenceQueue referenceQueue) {
            return new TimeoutException("ReferenceQueueDaemon timed out while targeting " + referenceQueue);
        }

        private static void timedOut(TimeoutException timeoutException) {
            try {
                C0308Os.kill(C0308Os.getpid(), OsConstants.SIGQUIT);
                Thread.sleep(5000);
            } catch (Exception e) {
                System.logE("failed to send SIGQUIT", e);
            } catch (OutOfMemoryError unused) {
            }
            if (Thread.getUncaughtExceptionPreHandler() == null && Thread.getDefaultUncaughtExceptionHandler() == null) {
                System.logE(timeoutException.getMessage(), timeoutException);
                System.exit(2);
            }
            Thread.currentThread().dispatchUncaughtException(timeoutException);
        }
    }

    public static void requestHeapTrim() {
        VMRuntime.getRuntime().requestHeapTrim();
    }

    public static void requestGC() {
        VMRuntime.getRuntime().requestConcurrentGC();
    }

    private static class HeapTaskDaemon extends Daemon {
        /* access modifiers changed from: private */
        public static final HeapTaskDaemon INSTANCE = new HeapTaskDaemon();

        HeapTaskDaemon() {
            super("HeapTaskDaemon");
        }

        public synchronized void interrupt(Thread thread) {
            VMRuntime.getRuntime().stopHeapTaskProcessor();
        }

        public void runInternal() {
            synchronized (this) {
                if (isRunning()) {
                    VMRuntime.getRuntime().startHeapTaskProcessor();
                }
            }
            VMRuntime.getRuntime().runHeapTasks();
        }
    }
}
