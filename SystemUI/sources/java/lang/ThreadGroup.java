package java.lang;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.Thread;
import java.p026io.PrintStream;
import java.util.Arrays;
import sun.misc.C4740VM;

public class ThreadGroup implements Thread.UncaughtExceptionHandler {
    static final ThreadGroup mainThreadGroup;
    static final ThreadGroup systemThreadGroup;
    boolean daemon;
    boolean destroyed;
    ThreadGroup[] groups;
    int maxPriority;
    int nUnstartedThreads;
    String name;
    int ngroups;
    int nthreads;
    private final ThreadGroup parent;
    Thread[] threads;
    boolean vmAllowSuspension;

    public final void checkAccess() {
    }

    static {
        ThreadGroup threadGroup = new ThreadGroup();
        systemThreadGroup = threadGroup;
        mainThreadGroup = new ThreadGroup(threadGroup, "main");
    }

    private ThreadGroup() {
        this.nUnstartedThreads = 0;
        this.name = "system";
        this.maxPriority = 10;
        this.parent = null;
    }

    public ThreadGroup(String str) {
        this(Thread.currentThread().getThreadGroup(), str);
    }

    public ThreadGroup(ThreadGroup threadGroup, String str) {
        this(checkParentAccess(threadGroup), threadGroup, str);
    }

    private ThreadGroup(Void voidR, ThreadGroup threadGroup, String str) {
        this.nUnstartedThreads = 0;
        this.name = str;
        this.maxPriority = threadGroup.maxPriority;
        this.daemon = threadGroup.daemon;
        this.vmAllowSuspension = threadGroup.vmAllowSuspension;
        this.parent = threadGroup;
        threadGroup.add(this);
    }

    private static Void checkParentAccess(ThreadGroup threadGroup) {
        threadGroup.checkAccess();
        return null;
    }

    public final String getName() {
        return this.name;
    }

    public final ThreadGroup getParent() {
        ThreadGroup threadGroup = this.parent;
        if (threadGroup != null) {
            threadGroup.checkAccess();
        }
        return this.parent;
    }

    public final int getMaxPriority() {
        return this.maxPriority;
    }

    public final boolean isDaemon() {
        return this.daemon;
    }

    public synchronized boolean isDestroyed() {
        return this.destroyed;
    }

    public final void setDaemon(boolean z) {
        checkAccess();
        this.daemon = z;
    }

    public final void setMaxPriority(int i) {
        int i2;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            if (i < 1) {
                i = 1;
            }
            if (i > 10) {
                i = 10;
            }
            ThreadGroup threadGroup = this.parent;
            this.maxPriority = threadGroup != null ? Math.min(i, threadGroup.maxPriority) : i;
            i2 = this.ngroups;
            ThreadGroup[] threadGroupArr2 = this.groups;
            threadGroupArr = threadGroupArr2 != null ? (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr2, i2) : null;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            threadGroupArr[i3].setMaxPriority(i);
        }
    }

    public final boolean parentOf(ThreadGroup threadGroup) {
        while (threadGroup != null) {
            if (threadGroup == this) {
                return true;
            }
            threadGroup = threadGroup.parent;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0019, code lost:
        if (r1 >= r2) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        r0 = r0 + r3[r1].activeCount();
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0025, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int activeCount() {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.destroyed     // Catch:{ all -> 0x0026 }
            r1 = 0
            if (r0 == 0) goto L_0x0008
            monitor-exit(r4)     // Catch:{ all -> 0x0026 }
            return r1
        L_0x0008:
            int r0 = r4.nthreads     // Catch:{ all -> 0x0026 }
            int r2 = r4.ngroups     // Catch:{ all -> 0x0026 }
            java.lang.ThreadGroup[] r3 = r4.groups     // Catch:{ all -> 0x0026 }
            if (r3 == 0) goto L_0x0017
            java.lang.Object[] r3 = java.util.Arrays.copyOf((T[]) r3, (int) r2)     // Catch:{ all -> 0x0026 }
            java.lang.ThreadGroup[] r3 = (java.lang.ThreadGroup[]) r3     // Catch:{ all -> 0x0026 }
            goto L_0x0018
        L_0x0017:
            r3 = 0
        L_0x0018:
            monitor-exit(r4)     // Catch:{ all -> 0x0026 }
        L_0x0019:
            if (r1 >= r2) goto L_0x0025
            r4 = r3[r1]
            int r4 = r4.activeCount()
            int r0 = r0 + r4
            int r1 = r1 + 1
            goto L_0x0019
        L_0x0025:
            return r0
        L_0x0026:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0026 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadGroup.activeCount():int");
    }

    public int enumerate(Thread[] threadArr) {
        checkAccess();
        return enumerate(threadArr, 0, true);
    }

    public int enumerate(Thread[] threadArr, boolean z) {
        checkAccess();
        return enumerate(threadArr, 0, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        if (r8 == false) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003d, code lost:
        if (r1 >= r2) goto L_0x0049;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x003f, code lost:
        r7 = r0[r1].enumerate(r6, r7, true);
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0049, code lost:
        return r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int enumerate(java.lang.Thread[] r6, int r7, boolean r8) {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.destroyed     // Catch:{ all -> 0x004a }
            r1 = 0
            if (r0 == 0) goto L_0x0008
            monitor-exit(r5)     // Catch:{ all -> 0x004a }
            return r1
        L_0x0008:
            int r0 = r5.nthreads     // Catch:{ all -> 0x004a }
            int r2 = r6.length     // Catch:{ all -> 0x004a }
            int r2 = r2 - r7
            if (r0 <= r2) goto L_0x0010
            int r0 = r6.length     // Catch:{ all -> 0x004a }
            int r0 = r0 - r7
        L_0x0010:
            r2 = r1
        L_0x0011:
            if (r2 >= r0) goto L_0x0029
            java.lang.Thread[] r3 = r5.threads     // Catch:{ all -> 0x004a }
            r3 = r3[r2]     // Catch:{ all -> 0x004a }
            boolean r3 = r3.isAlive()     // Catch:{ all -> 0x004a }
            if (r3 == 0) goto L_0x0026
            int r3 = r7 + 1
            java.lang.Thread[] r4 = r5.threads     // Catch:{ all -> 0x004a }
            r4 = r4[r2]     // Catch:{ all -> 0x004a }
            r6[r7] = r4     // Catch:{ all -> 0x004a }
            r7 = r3
        L_0x0026:
            int r2 = r2 + 1
            goto L_0x0011
        L_0x0029:
            r0 = 0
            if (r8 == 0) goto L_0x0039
            int r2 = r5.ngroups     // Catch:{ all -> 0x004a }
            java.lang.ThreadGroup[] r3 = r5.groups     // Catch:{ all -> 0x004a }
            if (r3 == 0) goto L_0x003a
            java.lang.Object[] r0 = java.util.Arrays.copyOf((T[]) r3, (int) r2)     // Catch:{ all -> 0x004a }
            java.lang.ThreadGroup[] r0 = (java.lang.ThreadGroup[]) r0     // Catch:{ all -> 0x004a }
            goto L_0x003a
        L_0x0039:
            r2 = r1
        L_0x003a:
            monitor-exit(r5)     // Catch:{ all -> 0x004a }
            if (r8 == 0) goto L_0x0049
        L_0x003d:
            if (r1 >= r2) goto L_0x0049
            r5 = r0[r1]
            r8 = 1
            int r7 = r5.enumerate((java.lang.Thread[]) r6, (int) r7, (boolean) r8)
            int r1 = r1 + 1
            goto L_0x003d
        L_0x0049:
            return r7
        L_0x004a:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x004a }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadGroup.enumerate(java.lang.Thread[], int, boolean):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0018, code lost:
        if (r1 >= r0) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001a, code lost:
        r4 = r4 + r2[r1].activeGroupCount();
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
        return r4;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int activeGroupCount() {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.destroyed     // Catch:{ all -> 0x0025 }
            r1 = 0
            if (r0 == 0) goto L_0x0008
            monitor-exit(r4)     // Catch:{ all -> 0x0025 }
            return r1
        L_0x0008:
            int r0 = r4.ngroups     // Catch:{ all -> 0x0025 }
            java.lang.ThreadGroup[] r2 = r4.groups     // Catch:{ all -> 0x0025 }
            if (r2 == 0) goto L_0x0015
            java.lang.Object[] r2 = java.util.Arrays.copyOf((T[]) r2, (int) r0)     // Catch:{ all -> 0x0025 }
            java.lang.ThreadGroup[] r2 = (java.lang.ThreadGroup[]) r2     // Catch:{ all -> 0x0025 }
            goto L_0x0016
        L_0x0015:
            r2 = 0
        L_0x0016:
            monitor-exit(r4)     // Catch:{ all -> 0x0025 }
            r4 = r0
        L_0x0018:
            if (r1 >= r0) goto L_0x0024
            r3 = r2[r1]
            int r3 = r3.activeGroupCount()
            int r4 = r4 + r3
            int r1 = r1 + 1
            goto L_0x0018
        L_0x0024:
            return r4
        L_0x0025:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0025 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadGroup.activeGroupCount():int");
    }

    public int enumerate(ThreadGroup[] threadGroupArr) {
        checkAccess();
        return enumerate(threadGroupArr, 0, true);
    }

    public int enumerate(ThreadGroup[] threadGroupArr, boolean z) {
        checkAccess();
        return enumerate(threadGroupArr, 0, z);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002a, code lost:
        if (r7 == false) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x002c, code lost:
        if (r1 >= r2) goto L_0x0038;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002e, code lost:
        r6 = r0[r1].enumerate(r5, r6, true);
        r1 = r1 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0038, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int enumerate(java.lang.ThreadGroup[] r5, int r6, boolean r7) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.destroyed     // Catch:{ all -> 0x0039 }
            r1 = 0
            if (r0 == 0) goto L_0x0008
            monitor-exit(r4)     // Catch:{ all -> 0x0039 }
            return r1
        L_0x0008:
            int r0 = r4.ngroups     // Catch:{ all -> 0x0039 }
            int r2 = r5.length     // Catch:{ all -> 0x0039 }
            int r2 = r2 - r6
            if (r0 <= r2) goto L_0x0010
            int r0 = r5.length     // Catch:{ all -> 0x0039 }
            int r0 = r0 - r6
        L_0x0010:
            if (r0 <= 0) goto L_0x0018
            java.lang.ThreadGroup[] r2 = r4.groups     // Catch:{ all -> 0x0039 }
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r1, (java.lang.Object) r5, (int) r6, (int) r0)     // Catch:{ all -> 0x0039 }
            int r6 = r6 + r0
        L_0x0018:
            r0 = 0
            if (r7 == 0) goto L_0x0028
            int r2 = r4.ngroups     // Catch:{ all -> 0x0039 }
            java.lang.ThreadGroup[] r3 = r4.groups     // Catch:{ all -> 0x0039 }
            if (r3 == 0) goto L_0x0029
            java.lang.Object[] r0 = java.util.Arrays.copyOf((T[]) r3, (int) r2)     // Catch:{ all -> 0x0039 }
            java.lang.ThreadGroup[] r0 = (java.lang.ThreadGroup[]) r0     // Catch:{ all -> 0x0039 }
            goto L_0x0029
        L_0x0028:
            r2 = r1
        L_0x0029:
            monitor-exit(r4)     // Catch:{ all -> 0x0039 }
            if (r7 == 0) goto L_0x0038
        L_0x002c:
            if (r1 >= r2) goto L_0x0038
            r4 = r0[r1]
            r7 = 1
            int r6 = r4.enumerate((java.lang.ThreadGroup[]) r5, (int) r6, (boolean) r7)
            int r1 = r1 + 1
            goto L_0x002c
        L_0x0038:
            return r6
        L_0x0039:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0039 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadGroup.enumerate(java.lang.ThreadGroup[], int, boolean):int");
    }

    @Deprecated
    public final void stop() {
        if (stopOrSuspend(false)) {
            Thread.currentThread().stop();
        }
    }

    public final void interrupt() {
        int i;
        int i2;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            for (int i3 = 0; i3 < this.nthreads; i3++) {
                this.threads[i3].interrupt();
            }
            i2 = this.ngroups;
            ThreadGroup[] threadGroupArr2 = this.groups;
            threadGroupArr = threadGroupArr2 != null ? (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr2, i2) : null;
        }
        for (i = 0; i < i2; i++) {
            threadGroupArr[i].interrupt();
        }
    }

    @Deprecated
    public final void suspend() {
        if (stopOrSuspend(true)) {
            Thread.currentThread().suspend();
        }
    }

    private boolean stopOrSuspend(boolean z) {
        boolean z2;
        int i;
        ThreadGroup[] threadGroupArr;
        Thread currentThread = Thread.currentThread();
        synchronized (this) {
            checkAccess();
            z2 = false;
            for (int i2 = 0; i2 < this.nthreads; i2++) {
                Thread thread = this.threads[i2];
                if (thread == currentThread) {
                    z2 = true;
                } else if (z) {
                    thread.suspend();
                } else {
                    thread.stop();
                }
            }
            i = this.ngroups;
            ThreadGroup[] threadGroupArr2 = this.groups;
            threadGroupArr = threadGroupArr2 != null ? (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr2, i) : null;
        }
        for (int i3 = 0; i3 < i; i3++) {
            z2 = threadGroupArr[i3].stopOrSuspend(z) || z2;
        }
        return z2;
    }

    @Deprecated
    public final void resume() {
        int i;
        int i2;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            for (int i3 = 0; i3 < this.nthreads; i3++) {
                this.threads[i3].resume();
            }
            i2 = this.ngroups;
            ThreadGroup[] threadGroupArr2 = this.groups;
            threadGroupArr = threadGroupArr2 != null ? (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr2, i2) : null;
        }
        for (i = 0; i < i2; i++) {
            threadGroupArr[i].resume();
        }
    }

    public final void destroy() {
        int i;
        ThreadGroup[] threadGroupArr;
        int i2;
        synchronized (this) {
            checkAccess();
            if (this.destroyed || this.nthreads > 0) {
                throw new IllegalThreadStateException();
            }
            i = this.ngroups;
            ThreadGroup[] threadGroupArr2 = this.groups;
            threadGroupArr = threadGroupArr2 != null ? (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr2, i) : null;
            if (this.parent != null) {
                this.destroyed = true;
                this.ngroups = 0;
                this.groups = null;
                this.nthreads = 0;
                this.threads = null;
            }
        }
        for (i2 = 0; i2 < i; i2++) {
            threadGroupArr[i2].destroy();
        }
        ThreadGroup threadGroup = this.parent;
        if (threadGroup != null) {
            threadGroup.remove(this);
        }
    }

    private final void add(ThreadGroup threadGroup) {
        synchronized (this) {
            if (!this.destroyed) {
                ThreadGroup[] threadGroupArr = this.groups;
                if (threadGroupArr == null) {
                    this.groups = new ThreadGroup[4];
                } else {
                    int i = this.ngroups;
                    if (i == threadGroupArr.length) {
                        this.groups = (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr, i * 2);
                    }
                }
                ThreadGroup[] threadGroupArr2 = this.groups;
                int i2 = this.ngroups;
                threadGroupArr2[i2] = threadGroup;
                this.ngroups = i2 + 1;
            } else {
                throw new IllegalThreadStateException();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0042, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void remove(java.lang.ThreadGroup r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.destroyed     // Catch:{ all -> 0x0043 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r4)     // Catch:{ all -> 0x0043 }
            return
        L_0x0007:
            r0 = 0
        L_0x0008:
            int r1 = r4.ngroups     // Catch:{ all -> 0x0043 }
            if (r0 >= r1) goto L_0x0027
            java.lang.ThreadGroup[] r2 = r4.groups     // Catch:{ all -> 0x0043 }
            r3 = r2[r0]     // Catch:{ all -> 0x0043 }
            if (r3 != r5) goto L_0x0024
            int r1 = r1 + -1
            r4.ngroups = r1     // Catch:{ all -> 0x0043 }
            int r5 = r0 + 1
            int r1 = r1 - r0
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r5, (java.lang.Object) r2, (int) r0, (int) r1)     // Catch:{ all -> 0x0043 }
            java.lang.ThreadGroup[] r5 = r4.groups     // Catch:{ all -> 0x0043 }
            int r0 = r4.ngroups     // Catch:{ all -> 0x0043 }
            r1 = 0
            r5[r0] = r1     // Catch:{ all -> 0x0043 }
            goto L_0x0027
        L_0x0024:
            int r0 = r0 + 1
            goto L_0x0008
        L_0x0027:
            int r5 = r4.nthreads     // Catch:{ all -> 0x0043 }
            if (r5 != 0) goto L_0x002e
            r4.notifyAll()     // Catch:{ all -> 0x0043 }
        L_0x002e:
            boolean r5 = r4.daemon     // Catch:{ all -> 0x0043 }
            if (r5 == 0) goto L_0x0041
            int r5 = r4.nthreads     // Catch:{ all -> 0x0043 }
            if (r5 != 0) goto L_0x0041
            int r5 = r4.nUnstartedThreads     // Catch:{ all -> 0x0043 }
            if (r5 != 0) goto L_0x0041
            int r5 = r4.ngroups     // Catch:{ all -> 0x0043 }
            if (r5 != 0) goto L_0x0041
            r4.destroy()     // Catch:{ all -> 0x0043 }
        L_0x0041:
            monitor-exit(r4)     // Catch:{ all -> 0x0043 }
            return
        L_0x0043:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0043 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadGroup.remove(java.lang.ThreadGroup):void");
    }

    /* access modifiers changed from: package-private */
    public void addUnstarted() {
        synchronized (this) {
            if (!this.destroyed) {
                this.nUnstartedThreads++;
            } else {
                throw new IllegalThreadStateException();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void add(Thread thread) {
        synchronized (this) {
            if (!this.destroyed) {
                Thread[] threadArr = this.threads;
                if (threadArr == null) {
                    this.threads = new Thread[4];
                } else {
                    int i = this.nthreads;
                    if (i == threadArr.length) {
                        this.threads = (Thread[]) Arrays.copyOf((T[]) threadArr, i * 2);
                    }
                }
                Thread[] threadArr2 = this.threads;
                int i2 = this.nthreads;
                threadArr2[i2] = thread;
                this.nthreads = i2 + 1;
                this.nUnstartedThreads--;
            } else {
                throw new IllegalThreadStateException();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void threadStartFailed(Thread thread) {
        synchronized (this) {
            remove(thread);
            this.nUnstartedThreads++;
        }
    }

    /* access modifiers changed from: package-private */
    public void threadTerminated(Thread thread) {
        synchronized (this) {
            remove(thread);
            if (this.nthreads == 0) {
                notifyAll();
            }
            if (this.daemon && this.nthreads == 0 && this.nUnstartedThreads == 0 && this.ngroups == 0) {
                destroy();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0028, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void remove(java.lang.Thread r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.destroyed     // Catch:{ all -> 0x0029 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            return
        L_0x0007:
            r0 = 0
        L_0x0008:
            int r1 = r4.nthreads     // Catch:{ all -> 0x0029 }
            if (r0 >= r1) goto L_0x0027
            java.lang.Thread[] r2 = r4.threads     // Catch:{ all -> 0x0029 }
            r3 = r2[r0]     // Catch:{ all -> 0x0029 }
            if (r3 != r5) goto L_0x0024
            int r5 = r0 + 1
            int r1 = r1 + -1
            r4.nthreads = r1     // Catch:{ all -> 0x0029 }
            int r1 = r1 - r0
            java.lang.System.arraycopy((java.lang.Object) r2, (int) r5, (java.lang.Object) r2, (int) r0, (int) r1)     // Catch:{ all -> 0x0029 }
            java.lang.Thread[] r5 = r4.threads     // Catch:{ all -> 0x0029 }
            int r0 = r4.nthreads     // Catch:{ all -> 0x0029 }
            r1 = 0
            r5[r0] = r1     // Catch:{ all -> 0x0029 }
            goto L_0x0027
        L_0x0024:
            int r0 = r0 + 1
            goto L_0x0008
        L_0x0027:
            monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            return
        L_0x0029:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0029 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ThreadGroup.remove(java.lang.Thread):void");
    }

    public void list() {
        list(System.out, 0);
    }

    /* access modifiers changed from: package-private */
    public void list(PrintStream printStream, int i) {
        int i2;
        int i3;
        int i4;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            for (int i5 = 0; i5 < i; i5++) {
                printStream.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            printStream.println((Object) this);
            i3 = i + 4;
            for (int i6 = 0; i6 < this.nthreads; i6++) {
                for (int i7 = 0; i7 < i3; i7++) {
                    printStream.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                }
                printStream.println((Object) this.threads[i6]);
            }
            i4 = this.ngroups;
            ThreadGroup[] threadGroupArr2 = this.groups;
            threadGroupArr = threadGroupArr2 != null ? (ThreadGroup[]) Arrays.copyOf((T[]) threadGroupArr2, i4) : null;
        }
        for (i2 = 0; i2 < i4; i2++) {
            threadGroupArr[i2].list(printStream, i3);
        }
    }

    public void uncaughtException(Thread thread, Throwable th) {
        ThreadGroup threadGroup = this.parent;
        if (threadGroup != null) {
            threadGroup.uncaughtException(thread, th);
            return;
        }
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler != null) {
            defaultUncaughtExceptionHandler.uncaughtException(thread, th);
        } else if (!(th instanceof ThreadDeath)) {
            PrintStream printStream = System.err;
            printStream.print("Exception in thread \"" + thread.getName() + "\" ");
            th.printStackTrace(System.err);
        }
    }

    @Deprecated
    public boolean allowThreadSuspension(boolean z) {
        this.vmAllowSuspension = z;
        if (z) {
            return true;
        }
        C4740VM.unsuspendSomeThreads();
        return true;
    }

    public String toString() {
        return getClass().getName() + "[name=" + getName() + ",maxpri=" + this.maxPriority + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
