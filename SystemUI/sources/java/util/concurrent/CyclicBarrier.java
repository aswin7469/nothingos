package java.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CyclicBarrier {
    private final Runnable barrierCommand;
    private int count;
    private Generation generation;
    private final ReentrantLock lock;
    private final int parties;
    private final Condition trip;

    private static class Generation {
        boolean broken;

        Generation() {
        }
    }

    private void nextGeneration() {
        this.trip.signalAll();
        this.count = this.parties;
        this.generation = new Generation();
    }

    private void breakBarrier() {
        this.generation.broken = true;
        this.count = this.parties;
        this.trip.signalAll();
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x002e A[SYNTHETIC, Splitter:B:21:0x002e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int dowait(boolean r8, long r9) throws java.lang.InterruptedException, java.util.concurrent.BrokenBarrierException, java.util.concurrent.TimeoutException {
        /*
            r7 = this;
            java.util.concurrent.locks.ReentrantLock r0 = r7.lock
            r0.lock()
            java.util.concurrent.CyclicBarrier$Generation r1 = r7.generation     // Catch:{ all -> 0x008e }
            boolean r2 = r1.broken     // Catch:{ all -> 0x008e }
            if (r2 != 0) goto L_0x0088
            boolean r2 = java.lang.Thread.interrupted()     // Catch:{ all -> 0x008e }
            if (r2 != 0) goto L_0x007f
            int r2 = r7.count     // Catch:{ all -> 0x008e }
            r3 = 1
            int r2 = r2 - r3
            r7.count = r2     // Catch:{ all -> 0x008e }
            if (r2 != 0) goto L_0x0032
            r8 = 0
            java.lang.Runnable r9 = r7.barrierCommand     // Catch:{ all -> 0x002a }
            if (r9 == 0) goto L_0x0021
            r9.run()     // Catch:{ all -> 0x002a }
        L_0x0021:
            r7.nextGeneration()     // Catch:{ all -> 0x0028 }
            r0.unlock()
            return r8
        L_0x0028:
            r9 = move-exception
            goto L_0x002c
        L_0x002a:
            r9 = move-exception
            r3 = r8
        L_0x002c:
            if (r3 != 0) goto L_0x0031
            r7.breakBarrier()     // Catch:{ all -> 0x008e }
        L_0x0031:
            throw r9     // Catch:{ all -> 0x008e }
        L_0x0032:
            r3 = 0
            if (r8 != 0) goto L_0x003e
            java.util.concurrent.locks.Condition r5 = r7.trip     // Catch:{ InterruptedException -> 0x003c }
            r5.await()     // Catch:{ InterruptedException -> 0x003c }
            goto L_0x005d
        L_0x003c:
            r5 = move-exception
            goto L_0x0049
        L_0x003e:
            int r5 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r5 <= 0) goto L_0x005d
            java.util.concurrent.locks.Condition r5 = r7.trip     // Catch:{ InterruptedException -> 0x003c }
            long r9 = r5.awaitNanos(r9)     // Catch:{ InterruptedException -> 0x003c }
            goto L_0x005d
        L_0x0049:
            java.util.concurrent.CyclicBarrier$Generation r6 = r7.generation     // Catch:{ all -> 0x008e }
            if (r1 != r6) goto L_0x0056
            boolean r6 = r1.broken     // Catch:{ all -> 0x008e }
            if (r6 == 0) goto L_0x0052
            goto L_0x0056
        L_0x0052:
            r7.breakBarrier()     // Catch:{ all -> 0x008e }
            throw r5     // Catch:{ all -> 0x008e }
        L_0x0056:
            java.lang.Thread r5 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x008e }
            r5.interrupt()     // Catch:{ all -> 0x008e }
        L_0x005d:
            boolean r5 = r1.broken     // Catch:{ all -> 0x008e }
            if (r5 != 0) goto L_0x0079
            java.util.concurrent.CyclicBarrier$Generation r5 = r7.generation     // Catch:{ all -> 0x008e }
            if (r1 == r5) goto L_0x0069
            r0.unlock()
            return r2
        L_0x0069:
            if (r8 == 0) goto L_0x0032
            int r3 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0070
            goto L_0x0032
        L_0x0070:
            r7.breakBarrier()     // Catch:{ all -> 0x008e }
            java.util.concurrent.TimeoutException r7 = new java.util.concurrent.TimeoutException     // Catch:{ all -> 0x008e }
            r7.<init>()     // Catch:{ all -> 0x008e }
            throw r7     // Catch:{ all -> 0x008e }
        L_0x0079:
            java.util.concurrent.BrokenBarrierException r7 = new java.util.concurrent.BrokenBarrierException     // Catch:{ all -> 0x008e }
            r7.<init>()     // Catch:{ all -> 0x008e }
            throw r7     // Catch:{ all -> 0x008e }
        L_0x007f:
            r7.breakBarrier()     // Catch:{ all -> 0x008e }
            java.lang.InterruptedException r7 = new java.lang.InterruptedException     // Catch:{ all -> 0x008e }
            r7.<init>()     // Catch:{ all -> 0x008e }
            throw r7     // Catch:{ all -> 0x008e }
        L_0x0088:
            java.util.concurrent.BrokenBarrierException r7 = new java.util.concurrent.BrokenBarrierException     // Catch:{ all -> 0x008e }
            r7.<init>()     // Catch:{ all -> 0x008e }
            throw r7     // Catch:{ all -> 0x008e }
        L_0x008e:
            r7 = move-exception
            r0.unlock()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CyclicBarrier.dowait(boolean, long):int");
    }

    public CyclicBarrier(int i, Runnable runnable) {
        ReentrantLock reentrantLock = new ReentrantLock();
        this.lock = reentrantLock;
        this.trip = reentrantLock.newCondition();
        this.generation = new Generation();
        if (i > 0) {
            this.parties = i;
            this.count = i;
            this.barrierCommand = runnable;
            return;
        }
        throw new IllegalArgumentException();
    }

    public CyclicBarrier(int i) {
        this(i, (Runnable) null);
    }

    public int getParties() {
        return this.parties;
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0);
        } catch (TimeoutException e) {
            throw new Error((Throwable) e);
        }
    }

    public int await(long j, TimeUnit timeUnit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        return dowait(true, timeUnit.toNanos(j));
    }

    public boolean isBroken() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.generation.broken;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void reset() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            breakBarrier();
            nextGeneration();
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getNumberWaiting() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.parties - this.count;
        } finally {
            reentrantLock.unlock();
        }
    }
}
