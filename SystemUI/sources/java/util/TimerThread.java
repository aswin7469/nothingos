package java.util;

/* compiled from: Timer */
class TimerThread extends Thread {
    boolean newTasksMayBeScheduled = true;
    private TaskQueue queue;

    TimerThread(TaskQueue taskQueue) {
        this.queue = taskQueue;
    }

    public void run() {
        try {
            mainLoop();
            synchronized (this.queue) {
                this.newTasksMayBeScheduled = false;
                this.queue.clear();
            }
        } catch (Throwable th) {
            synchronized (this.queue) {
                this.newTasksMayBeScheduled = false;
                this.queue.clear();
                throw th;
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:368)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:172)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    private void mainLoop() {
        /*
            r14 = this;
        L_0x0000:
            java.util.TaskQueue r0 = r14.queue     // Catch:{ InterruptedException -> 0x0000 }
            monitor-enter(r0)     // Catch:{ InterruptedException -> 0x0000 }
        L_0x0003:
            java.util.TaskQueue r1 = r14.queue     // Catch:{ all -> 0x007b }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x0015
            boolean r1 = r14.newTasksMayBeScheduled     // Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x0015
            java.util.TaskQueue r1 = r14.queue     // Catch:{ all -> 0x007b }
            r1.wait()     // Catch:{ all -> 0x007b }
            goto L_0x0003
        L_0x0015:
            java.util.TaskQueue r1 = r14.queue     // Catch:{ all -> 0x007b }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x007b }
            if (r1 == 0) goto L_0x001f
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            return
        L_0x001f:
            java.util.TaskQueue r1 = r14.queue     // Catch:{ all -> 0x007b }
            java.util.TimerTask r1 = r1.getMin()     // Catch:{ all -> 0x007b }
            java.lang.Object r2 = r1.lock     // Catch:{ all -> 0x007b }
            monitor-enter(r2)     // Catch:{ all -> 0x007b }
            int r3 = r1.state     // Catch:{ all -> 0x0078 }
            r4 = 3
            if (r3 != r4) goto L_0x0035
            java.util.TaskQueue r1 = r14.queue     // Catch:{ all -> 0x0078 }
            r1.removeMin()     // Catch:{ all -> 0x0078 }
            monitor-exit(r2)     // Catch:{ all -> 0x0078 }
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            goto L_0x0000
        L_0x0035:
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0078 }
            long r5 = r1.nextExecutionTime     // Catch:{ all -> 0x0078 }
            int r7 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r7 > 0) goto L_0x0041
            r7 = 1
            goto L_0x0042
        L_0x0041:
            r7 = 0
        L_0x0042:
            if (r7 == 0) goto L_0x0068
            long r8 = r1.period     // Catch:{ all -> 0x0078 }
            r10 = 0
            int r8 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r8 != 0) goto L_0x0055
            java.util.TaskQueue r8 = r14.queue     // Catch:{ all -> 0x0078 }
            r8.removeMin()     // Catch:{ all -> 0x0078 }
            r8 = 2
            r1.state = r8     // Catch:{ all -> 0x0078 }
            goto L_0x0068
        L_0x0055:
            java.util.TaskQueue r8 = r14.queue     // Catch:{ all -> 0x0078 }
            long r12 = r1.period     // Catch:{ all -> 0x0078 }
            int r9 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
            if (r9 >= 0) goto L_0x0062
            long r9 = r1.period     // Catch:{ all -> 0x0078 }
            long r9 = r3 - r9
            goto L_0x0065
        L_0x0062:
            long r9 = r1.period     // Catch:{ all -> 0x0078 }
            long r9 = r9 + r5
        L_0x0065:
            r8.rescheduleMin(r9)     // Catch:{ all -> 0x0078 }
        L_0x0068:
            monitor-exit(r2)     // Catch:{ all -> 0x0078 }
            if (r7 != 0) goto L_0x0071
            java.util.TaskQueue r2 = r14.queue     // Catch:{ all -> 0x007b }
            long r5 = r5 - r3
            r2.wait(r5)     // Catch:{ all -> 0x007b }
        L_0x0071:
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            if (r7 == 0) goto L_0x0000
            r1.run()     // Catch:{ InterruptedException -> 0x0000 }
            goto L_0x0000
        L_0x0078:
            r1 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0078 }
            throw r1     // Catch:{ all -> 0x007b }
        L_0x007b:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x007b }
            throw r1     // Catch:{ InterruptedException -> 0x0000 }
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.TimerThread.mainLoop():void");
    }
}
