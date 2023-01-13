package java.lang;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.ProcessBuilder;
import java.p026io.BufferedInputStream;
import java.p026io.BufferedOutputStream;
import java.p026io.FileDescriptor;
import java.p026io.FileInputStream;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class UNIXProcess extends Process {
    private static final Executor processReaperExecutor = ((Executor) AccessController.doPrivileged(new PrivilegedAction<Executor>() {
        public Executor run() {
            return Executors.newCachedThreadPool(new ProcessReaperThreadFactory());
        }
    }));
    private int exitcode;
    private boolean hasExited;
    /* access modifiers changed from: private */
    public final int pid;
    private InputStream stderr;
    private OutputStream stdin;
    private InputStream stdout;

    private static native void destroyProcess(int i);

    private native int forkAndExec(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2, byte[] bArr4, int[] iArr, boolean z) throws IOException;

    private static native void initIDs();

    /* access modifiers changed from: private */
    public native int waitForProcessExit(int i);

    private static class ProcessReaperThreadFactory implements ThreadFactory {
        private static final ThreadGroup group = getRootThreadGroup();

        private ProcessReaperThreadFactory() {
        }

        private static ThreadGroup getRootThreadGroup() {
            return (ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() {
                public ThreadGroup run() {
                    ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                    while (threadGroup.getParent() != null) {
                        threadGroup = threadGroup.getParent();
                    }
                    return threadGroup;
                }
            });
        }

        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(group, runnable, "process reaper", 32768);
            thread.setDaemon(true);
            thread.setPriority(10);
            return thread;
        }
    }

    static {
        initIDs();
    }

    UNIXProcess(byte[] bArr, byte[] bArr2, int i, byte[] bArr3, int i2, byte[] bArr4, final int[] iArr, boolean z) throws IOException {
        this.pid = forkAndExec(bArr, bArr2, i, bArr3, i2, bArr4, iArr, z);
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws IOException {
                    UNIXProcess.this.initStreams(iArr);
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    static FileDescriptor newFileDescriptor(int i) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        fileDescriptor.setInt$(i);
        return fileDescriptor;
    }

    /* access modifiers changed from: package-private */
    public void initStreams(int[] iArr) throws IOException {
        OutputStream outputStream;
        InputStream inputStream;
        InputStream inputStream2;
        int i = iArr[0];
        if (i == -1) {
            outputStream = ProcessBuilder.NullOutputStream.INSTANCE;
        } else {
            outputStream = new ProcessPipeOutputStream(i);
        }
        this.stdin = outputStream;
        if (iArr[1] == -1) {
            inputStream = ProcessBuilder.NullInputStream.INSTANCE;
        } else {
            inputStream = new ProcessPipeInputStream(iArr[1]);
        }
        this.stdout = inputStream;
        if (iArr[2] == -1) {
            inputStream2 = ProcessBuilder.NullInputStream.INSTANCE;
        } else {
            inputStream2 = new ProcessPipeInputStream(iArr[2]);
        }
        this.stderr = inputStream2;
        processReaperExecutor.execute(new Runnable() {
            public void run() {
                UNIXProcess uNIXProcess = UNIXProcess.this;
                UNIXProcess.this.processExited(uNIXProcess.waitForProcessExit(uNIXProcess.pid));
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void processExited(int i) {
        synchronized (this) {
            this.exitcode = i;
            this.hasExited = true;
            notifyAll();
        }
        InputStream inputStream = this.stdout;
        if (inputStream instanceof ProcessPipeInputStream) {
            ((ProcessPipeInputStream) inputStream).processExited();
        }
        InputStream inputStream2 = this.stderr;
        if (inputStream2 instanceof ProcessPipeInputStream) {
            ((ProcessPipeInputStream) inputStream2).processExited();
        }
        OutputStream outputStream = this.stdin;
        if (outputStream instanceof ProcessPipeOutputStream) {
            ((ProcessPipeOutputStream) outputStream).processExited();
        }
    }

    public OutputStream getOutputStream() {
        return this.stdin;
    }

    public InputStream getInputStream() {
        return this.stdout;
    }

    public InputStream getErrorStream() {
        return this.stderr;
    }

    public synchronized int waitFor() throws InterruptedException {
        while (!this.hasExited) {
            wait();
        }
        return this.exitcode;
    }

    public synchronized int exitValue() {
        if (this.hasExited) {
        } else {
            throw new IllegalThreadStateException("process hasn't exited");
        }
        return this.exitcode;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:0|6|7|8|9|10|11|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0015 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0010 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void destroy() {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.hasExited     // Catch:{ all -> 0x001b }
            if (r0 != 0) goto L_0x000a
            int r0 = r1.pid     // Catch:{ all -> 0x001b }
            destroyProcess(r0)     // Catch:{ all -> 0x001b }
        L_0x000a:
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            java.io.OutputStream r0 = r1.stdin     // Catch:{ IOException -> 0x0010 }
            r0.close()     // Catch:{ IOException -> 0x0010 }
        L_0x0010:
            java.io.InputStream r0 = r1.stdout     // Catch:{ IOException -> 0x0015 }
            r0.close()     // Catch:{ IOException -> 0x0015 }
        L_0x0015:
            java.io.InputStream r1 = r1.stderr     // Catch:{ IOException -> 0x001a }
            r1.close()     // Catch:{ IOException -> 0x001a }
        L_0x001a:
            return
        L_0x001b:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.UNIXProcess.destroy():void");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Process[pid=");
        sb.append(this.pid);
        if (this.hasExited) {
            sb.append(" ,hasExited=true, exitcode=");
            sb.append(this.exitcode);
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        } else {
            sb.append(", hasExited=false]");
        }
        return sb.toString();
    }

    static class ProcessPipeInputStream extends BufferedInputStream {
        ProcessPipeInputStream(int i) {
            super(new FileInputStream(UNIXProcess.newFileDescriptor(i), true));
        }

        private static byte[] drainInputStream(InputStream inputStream) throws IOException {
            byte[] bArr = null;
            if (inputStream == null) {
                return null;
            }
            int i = 0;
            while (true) {
                int available = inputStream.available();
                if (available <= 0) {
                    break;
                }
                bArr = bArr == null ? new byte[available] : Arrays.copyOf(bArr, i + available);
                i += inputStream.read(bArr, i, available);
            }
            return (bArr == null || i == bArr.length) ? bArr : Arrays.copyOf(bArr, i);
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void processExited() {
            /*
                r2 = this;
                monitor-enter(r2)
                java.io.InputStream r0 = r2.f519in     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                if (r0 == 0) goto L_0x0023
                byte[] r1 = drainInputStream(r0)     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                r0.close()     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                if (r1 != 0) goto L_0x0011
                java.lang.ProcessBuilder$NullInputStream r0 = java.lang.ProcessBuilder.NullInputStream.INSTANCE     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                goto L_0x0016
            L_0x0011:
                java.io.ByteArrayInputStream r0 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                r0.<init>(r1)     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
            L_0x0016:
                r2.f519in = r0     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                byte[] r0 = r2.buf     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                if (r0 != 0) goto L_0x0023
                r0 = 0
                r2.f519in = r0     // Catch:{ IOException -> 0x0023, all -> 0x0020 }
                goto L_0x0023
            L_0x0020:
                r0 = move-exception
                monitor-exit(r2)
                throw r0
            L_0x0023:
                monitor-exit(r2)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.UNIXProcess.ProcessPipeInputStream.processExited():void");
        }
    }

    static class ProcessPipeOutputStream extends BufferedOutputStream {
        ProcessPipeOutputStream(int i) {
            super(new FileOutputStream(UNIXProcess.newFileDescriptor(i), true));
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Can't wrap try/catch for region: R(4:4|5|6|7) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0008 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void processExited() {
            /*
                r1 = this;
                monitor-enter(r1)
                java.io.OutputStream r0 = r1.out     // Catch:{ all -> 0x000e }
                if (r0 == 0) goto L_0x000c
                r0.close()     // Catch:{ IOException -> 0x0008 }
            L_0x0008:
                java.lang.ProcessBuilder$NullOutputStream r0 = java.lang.ProcessBuilder.NullOutputStream.INSTANCE     // Catch:{ all -> 0x000e }
                r1.out = r0     // Catch:{ all -> 0x000e }
            L_0x000c:
                monitor-exit(r1)
                return
            L_0x000e:
                r0 = move-exception
                monitor-exit(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.UNIXProcess.ProcessPipeOutputStream.processExited():void");
        }
    }
}
