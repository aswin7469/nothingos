package sun.nio.p035fs;

import java.util.concurrent.ExecutionException;
import sun.misc.Unsafe;

/* renamed from: sun.nio.fs.Cancellable */
abstract class Cancellable implements Runnable {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private boolean completed;
    private Throwable exception;
    private final Object lock = new Object();
    private final long pollingAddress;

    /* access modifiers changed from: protected */
    public int cancelValue() {
        return Integer.MAX_VALUE;
    }

    /* access modifiers changed from: package-private */
    public abstract void implRun() throws Throwable;

    protected Cancellable() {
        Unsafe unsafe2 = unsafe;
        long allocateMemory = unsafe2.allocateMemory(4);
        this.pollingAddress = allocateMemory;
        unsafe2.putIntVolatile((Object) null, allocateMemory, 0);
    }

    /* access modifiers changed from: protected */
    public long addressToPollForCancel() {
        return this.pollingAddress;
    }

    /* access modifiers changed from: package-private */
    public final void cancel() {
        synchronized (this.lock) {
            if (!this.completed) {
                unsafe.putIntVolatile((Object) null, this.pollingAddress, cancelValue());
            }
        }
    }

    private Throwable exception() {
        Throwable th;
        synchronized (this.lock) {
            th = this.exception;
        }
        return th;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r5 = this;
            r0 = 1
            r5.implRun()     // Catch:{ all -> 0x0015 }
            java.lang.Object r1 = r5.lock
            monitor-enter(r1)
            r5.completed = r0     // Catch:{ all -> 0x0012 }
            sun.misc.Unsafe r0 = unsafe     // Catch:{ all -> 0x0012 }
            long r2 = r5.pollingAddress     // Catch:{ all -> 0x0012 }
            r0.freeMemory(r2)     // Catch:{ all -> 0x0012 }
            monitor-exit(r1)     // Catch:{ all -> 0x0012 }
            goto L_0x0029
        L_0x0012:
            r5 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0012 }
            throw r5
        L_0x0015:
            r1 = move-exception
            java.lang.Object r2 = r5.lock     // Catch:{ all -> 0x0030 }
            monitor-enter(r2)     // Catch:{ all -> 0x0030 }
            r5.exception = r1     // Catch:{ all -> 0x002d }
            monitor-exit(r2)     // Catch:{ all -> 0x002d }
            java.lang.Object r1 = r5.lock
            monitor-enter(r1)
            r5.completed = r0     // Catch:{ all -> 0x002a }
            sun.misc.Unsafe r0 = unsafe     // Catch:{ all -> 0x002a }
            long r2 = r5.pollingAddress     // Catch:{ all -> 0x002a }
            r0.freeMemory(r2)     // Catch:{ all -> 0x002a }
            monitor-exit(r1)     // Catch:{ all -> 0x002a }
        L_0x0029:
            return
        L_0x002a:
            r5 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002a }
            throw r5
        L_0x002d:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x002d }
            throw r1     // Catch:{ all -> 0x0030 }
        L_0x0030:
            r1 = move-exception
            java.lang.Object r2 = r5.lock
            monitor-enter(r2)
            r5.completed = r0     // Catch:{ all -> 0x003f }
            sun.misc.Unsafe r0 = unsafe     // Catch:{ all -> 0x003f }
            long r3 = r5.pollingAddress     // Catch:{ all -> 0x003f }
            r0.freeMemory(r3)     // Catch:{ all -> 0x003f }
            monitor-exit(r2)     // Catch:{ all -> 0x003f }
            throw r1
        L_0x003f:
            r5 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x003f }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.Cancellable.run():void");
    }

    static void runInterruptibly(Cancellable cancellable) throws ExecutionException {
        Thread thread = new Thread((Runnable) cancellable);
        thread.start();
        boolean z = false;
        while (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException unused) {
                cancellable.cancel();
                z = true;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        Throwable exception2 = cancellable.exception();
        if (exception2 != null) {
            throw new ExecutionException(exception2);
        }
    }
}
