package java.p026io;

import libcore.p030io.IoUtils;

/* renamed from: java.io.PipedInputStream */
public class PipedInputStream extends InputStream {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int DEFAULT_PIPE_SIZE = 1024;
    protected static final int PIPE_SIZE = 1024;
    protected byte[] buffer;
    volatile boolean closedByReader;
    boolean closedByWriter;
    boolean connected;

    /* renamed from: in */
    protected int f528in;
    protected int out;
    Thread readSide;
    Thread writeSide;

    public PipedInputStream(PipedOutputStream pipedOutputStream) throws IOException {
        this(pipedOutputStream, 1024);
    }

    public PipedInputStream(PipedOutputStream pipedOutputStream, int i) throws IOException {
        this.f528in = -1;
        this.out = 0;
        initPipe(i);
        connect(pipedOutputStream);
    }

    public PipedInputStream() {
        this.f528in = -1;
        this.out = 0;
        initPipe(1024);
    }

    public PipedInputStream(int i) {
        this.f528in = -1;
        this.out = 0;
        initPipe(i);
    }

    private void initPipe(int i) {
        if (i > 0) {
            this.buffer = new byte[i];
            return;
        }
        throw new IllegalArgumentException("Pipe Size <= 0");
    }

    public void connect(PipedOutputStream pipedOutputStream) throws IOException {
        pipedOutputStream.connect(this);
    }

    /* access modifiers changed from: protected */
    public synchronized void receive(int i) throws IOException {
        checkStateForReceive();
        this.writeSide = Thread.currentThread();
        if (this.f528in == this.out) {
            awaitSpace();
        }
        if (this.f528in < 0) {
            this.f528in = 0;
            this.out = 0;
        }
        byte[] bArr = this.buffer;
        int i2 = this.f528in;
        int i3 = i2 + 1;
        this.f528in = i3;
        bArr[i2] = (byte) (i & 255);
        if (i3 >= bArr.length) {
            this.f528in = 0;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x000a A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void receive(byte[] r5, int r6, int r7) throws java.p026io.IOException {
        /*
            r4 = this;
            monitor-enter(r4)
            r4.checkStateForReceive()     // Catch:{ all -> 0x004b }
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x004b }
            r4.writeSide = r0     // Catch:{ all -> 0x004b }
        L_0x000a:
            if (r7 <= 0) goto L_0x0049
            int r0 = r4.f528in     // Catch:{ all -> 0x004b }
            int r1 = r4.out     // Catch:{ all -> 0x004b }
            if (r0 != r1) goto L_0x0015
            r4.awaitSpace()     // Catch:{ all -> 0x004b }
        L_0x0015:
            int r0 = r4.out     // Catch:{ all -> 0x004b }
            int r1 = r4.f528in     // Catch:{ all -> 0x004b }
            r2 = 0
            if (r0 >= r1) goto L_0x0021
            byte[] r0 = r4.buffer     // Catch:{ all -> 0x004b }
            int r0 = r0.length     // Catch:{ all -> 0x004b }
        L_0x001f:
            int r0 = r0 - r1
            goto L_0x0030
        L_0x0021:
            if (r1 >= r0) goto L_0x002f
            r3 = -1
            if (r1 != r3) goto L_0x001f
            r4.out = r2     // Catch:{ all -> 0x004b }
            r4.f528in = r2     // Catch:{ all -> 0x004b }
            byte[] r0 = r4.buffer     // Catch:{ all -> 0x004b }
            int r0 = r0.length     // Catch:{ all -> 0x004b }
            int r0 = r0 - r2
            goto L_0x0030
        L_0x002f:
            r0 = r2
        L_0x0030:
            if (r0 <= r7) goto L_0x0033
            r0 = r7
        L_0x0033:
            byte[] r1 = r4.buffer     // Catch:{ all -> 0x004b }
            int r3 = r4.f528in     // Catch:{ all -> 0x004b }
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r6, (java.lang.Object) r1, (int) r3, (int) r0)     // Catch:{ all -> 0x004b }
            int r7 = r7 - r0
            int r6 = r6 + r0
            int r1 = r4.f528in     // Catch:{ all -> 0x004b }
            int r1 = r1 + r0
            r4.f528in = r1     // Catch:{ all -> 0x004b }
            byte[] r0 = r4.buffer     // Catch:{ all -> 0x004b }
            int r0 = r0.length     // Catch:{ all -> 0x004b }
            if (r1 < r0) goto L_0x000a
            r4.f528in = r2     // Catch:{ all -> 0x004b }
            goto L_0x000a
        L_0x0049:
            monitor-exit(r4)
            return
        L_0x004b:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PipedInputStream.receive(byte[], int, int):void");
    }

    private void checkStateForReceive() throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        } else if (this.closedByWriter || this.closedByReader) {
            throw new IOException("Pipe closed");
        } else {
            Thread thread = this.readSide;
            if (thread != null && !thread.isAlive()) {
                throw new IOException("Read end dead");
            }
        }
    }

    private void awaitSpace() throws IOException {
        while (this.f528in == this.out) {
            checkStateForReceive();
            notifyAll();
            try {
                wait(1000);
            } catch (InterruptedException unused) {
                IoUtils.throwInterruptedIoException();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void receivedLast() {
        this.closedByWriter = true;
        notifyAll();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:24|25|(2:29|(1:31)(3:57|32|33))|34|35|36|37|38|39|61|56) */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0073, code lost:
        return r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x002b, code lost:
        continue;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:38:0x0056 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read() throws java.p026io.IOException {
        /*
            r5 = this;
            monitor-enter(r5)
            boolean r0 = r5.connected     // Catch:{ all -> 0x0084 }
            if (r0 == 0) goto L_0x007c
            boolean r0 = r5.closedByReader     // Catch:{ all -> 0x0084 }
            if (r0 != 0) goto L_0x0074
            java.lang.Thread r0 = r5.writeSide     // Catch:{ all -> 0x0084 }
            if (r0 == 0) goto L_0x0024
            boolean r0 = r0.isAlive()     // Catch:{ all -> 0x0084 }
            if (r0 != 0) goto L_0x0024
            boolean r0 = r5.closedByWriter     // Catch:{ all -> 0x0084 }
            if (r0 != 0) goto L_0x0024
            int r0 = r5.f528in     // Catch:{ all -> 0x0084 }
            if (r0 < 0) goto L_0x001c
            goto L_0x0024
        L_0x001c:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0084 }
            java.lang.String r1 = "Write end dead"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0084 }
            throw r0     // Catch:{ all -> 0x0084 }
        L_0x0024:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0084 }
            r5.readSide = r0     // Catch:{ all -> 0x0084 }
            r0 = 2
        L_0x002b:
            int r1 = r5.f528in     // Catch:{ all -> 0x0084 }
            r2 = -1
            if (r1 >= 0) goto L_0x005a
            boolean r1 = r5.closedByWriter     // Catch:{ all -> 0x0084 }
            if (r1 == 0) goto L_0x0036
            monitor-exit(r5)
            return r2
        L_0x0036:
            java.lang.Thread r1 = r5.writeSide     // Catch:{ all -> 0x0084 }
            if (r1 == 0) goto L_0x004d
            boolean r1 = r1.isAlive()     // Catch:{ all -> 0x0084 }
            if (r1 != 0) goto L_0x004d
            int r0 = r0 + -1
            if (r0 < 0) goto L_0x0045
            goto L_0x004d
        L_0x0045:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0084 }
            java.lang.String r1 = "Pipe broken"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0084 }
            throw r0     // Catch:{ all -> 0x0084 }
        L_0x004d:
            r5.notifyAll()     // Catch:{ all -> 0x0084 }
            r1 = 1000(0x3e8, double:4.94E-321)
            r5.wait(r1)     // Catch:{ InterruptedException -> 0x0056 }
            goto L_0x002b
        L_0x0056:
            libcore.p030io.IoUtils.throwInterruptedIoException()     // Catch:{ all -> 0x0084 }
            goto L_0x002b
        L_0x005a:
            byte[] r0 = r5.buffer     // Catch:{ all -> 0x0084 }
            int r3 = r5.out     // Catch:{ all -> 0x0084 }
            int r4 = r3 + 1
            r5.out = r4     // Catch:{ all -> 0x0084 }
            byte r3 = r0[r3]     // Catch:{ all -> 0x0084 }
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r0 = r0.length     // Catch:{ all -> 0x0084 }
            if (r4 < r0) goto L_0x006c
            r0 = 0
            r5.out = r0     // Catch:{ all -> 0x0084 }
        L_0x006c:
            int r0 = r5.out     // Catch:{ all -> 0x0084 }
            if (r1 != r0) goto L_0x0072
            r5.f528in = r2     // Catch:{ all -> 0x0084 }
        L_0x0072:
            monitor-exit(r5)
            return r3
        L_0x0074:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0084 }
            java.lang.String r1 = "Pipe closed"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0084 }
            throw r0     // Catch:{ all -> 0x0084 }
        L_0x007c:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0084 }
            java.lang.String r1 = "Pipe not connected"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0084 }
            throw r0     // Catch:{ all -> 0x0084 }
        L_0x0084:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PipedInputStream.read():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x005c, code lost:
        return r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(byte[] r9, int r10, int r11) throws java.p026io.IOException {
        /*
            r8 = this;
            monitor-enter(r8)
            if (r9 == 0) goto L_0x0063
            if (r10 < 0) goto L_0x005d
            if (r11 < 0) goto L_0x005d
            int r0 = r9.length     // Catch:{ all -> 0x0069 }
            int r0 = r0 - r10
            if (r11 > r0) goto L_0x005d
            r0 = 0
            if (r11 != 0) goto L_0x0010
            monitor-exit(r8)
            return r0
        L_0x0010:
            int r1 = r8.read()     // Catch:{ all -> 0x0069 }
            r2 = -1
            if (r1 >= 0) goto L_0x0019
            monitor-exit(r8)
            return r2
        L_0x0019:
            byte r1 = (byte) r1
            r9[r10] = r1     // Catch:{ all -> 0x0069 }
            r1 = 1
            r3 = r1
        L_0x001e:
            int r4 = r8.f528in     // Catch:{ all -> 0x0069 }
            if (r4 < 0) goto L_0x005b
            if (r11 <= r1) goto L_0x005b
            int r5 = r8.out     // Catch:{ all -> 0x0069 }
            if (r4 <= r5) goto L_0x0032
            byte[] r6 = r8.buffer     // Catch:{ all -> 0x0069 }
            int r6 = r6.length     // Catch:{ all -> 0x0069 }
            int r6 = r6 - r5
            int r4 = r4 - r5
            int r4 = java.lang.Math.min((int) r6, (int) r4)     // Catch:{ all -> 0x0069 }
            goto L_0x0036
        L_0x0032:
            byte[] r4 = r8.buffer     // Catch:{ all -> 0x0069 }
            int r4 = r4.length     // Catch:{ all -> 0x0069 }
            int r4 = r4 - r5
        L_0x0036:
            int r5 = r11 + -1
            if (r4 <= r5) goto L_0x003b
            r4 = r5
        L_0x003b:
            byte[] r5 = r8.buffer     // Catch:{ all -> 0x0069 }
            int r6 = r8.out     // Catch:{ all -> 0x0069 }
            int r7 = r10 + r3
            java.lang.System.arraycopy((java.lang.Object) r5, (int) r6, (java.lang.Object) r9, (int) r7, (int) r4)     // Catch:{ all -> 0x0069 }
            int r5 = r8.out     // Catch:{ all -> 0x0069 }
            int r5 = r5 + r4
            r8.out = r5     // Catch:{ all -> 0x0069 }
            int r3 = r3 + r4
            int r11 = r11 - r4
            byte[] r4 = r8.buffer     // Catch:{ all -> 0x0069 }
            int r4 = r4.length     // Catch:{ all -> 0x0069 }
            if (r5 < r4) goto L_0x0052
            r8.out = r0     // Catch:{ all -> 0x0069 }
        L_0x0052:
            int r4 = r8.f528in     // Catch:{ all -> 0x0069 }
            int r5 = r8.out     // Catch:{ all -> 0x0069 }
            if (r4 != r5) goto L_0x001e
            r8.f528in = r2     // Catch:{ all -> 0x0069 }
            goto L_0x001e
        L_0x005b:
            monitor-exit(r8)
            return r3
        L_0x005d:
            java.lang.IndexOutOfBoundsException r9 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x0069 }
            r9.<init>()     // Catch:{ all -> 0x0069 }
            throw r9     // Catch:{ all -> 0x0069 }
        L_0x0063:
            java.lang.NullPointerException r9 = new java.lang.NullPointerException     // Catch:{ all -> 0x0069 }
            r9.<init>()     // Catch:{ all -> 0x0069 }
            throw r9     // Catch:{ all -> 0x0069 }
        L_0x0069:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PipedInputStream.read(byte[], int, int):int");
    }

    public synchronized int available() throws IOException {
        int i = this.f528in;
        if (i < 0) {
            return 0;
        }
        int i2 = this.out;
        if (i == i2) {
            return this.buffer.length;
        } else if (i > i2) {
            return i - i2;
        } else {
            return (i + this.buffer.length) - i2;
        }
    }

    public void close() throws IOException {
        this.closedByReader = true;
        synchronized (this) {
            this.f528in = -1;
        }
    }
}
