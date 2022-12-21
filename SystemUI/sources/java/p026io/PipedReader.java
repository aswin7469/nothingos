package java.p026io;

/* renamed from: java.io.PipedReader */
public class PipedReader extends Reader {
    private static final int DEFAULT_PIPE_SIZE = 1024;
    char[] buffer;
    boolean closedByReader;
    boolean closedByWriter;
    boolean connected;

    /* renamed from: in */
    int f529in;
    int out;
    Thread readSide;
    Thread writeSide;

    public PipedReader(PipedWriter pipedWriter) throws IOException {
        this(pipedWriter, 1024);
    }

    public PipedReader(PipedWriter pipedWriter, int i) throws IOException {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.f529in = -1;
        this.out = 0;
        initPipe(i);
        connect(pipedWriter);
    }

    public PipedReader() {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.f529in = -1;
        this.out = 0;
        initPipe(1024);
    }

    public PipedReader(int i) {
        this.closedByWriter = false;
        this.closedByReader = false;
        this.connected = false;
        this.f529in = -1;
        this.out = 0;
        initPipe(i);
    }

    private void initPipe(int i) {
        if (i > 0) {
            this.buffer = new char[i];
            return;
        }
        throw new IllegalArgumentException("Pipe size <= 0");
    }

    public void connect(PipedWriter pipedWriter) throws IOException {
        pipedWriter.connect(this);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(10:18|(2:20|(1:22)(3:49|23|24))|25|26|27|28|29|30|51|47) */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0026, code lost:
        continue;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0048 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:39:0x0065=Splitter:B:39:0x0065, B:29:0x0048=Splitter:B:29:0x0048} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void receive(int r5) throws java.p026io.IOException {
        /*
            r4 = this;
            monitor-enter(r4)
            boolean r0 = r4.connected     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x006d
            boolean r0 = r4.closedByWriter     // Catch:{ all -> 0x0075 }
            if (r0 != 0) goto L_0x0065
            boolean r0 = r4.closedByReader     // Catch:{ all -> 0x0075 }
            if (r0 != 0) goto L_0x0065
            java.lang.Thread r0 = r4.readSide     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x0020
            boolean r0 = r0.isAlive()     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x0018
            goto L_0x0020
        L_0x0018:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0075 }
            java.lang.String r0 = "Read end dead"
            r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0075 }
            throw r5     // Catch:{ all -> 0x0075 }
        L_0x0020:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0075 }
            r4.writeSide = r0     // Catch:{ all -> 0x0075 }
        L_0x0026:
            int r0 = r4.f529in     // Catch:{ all -> 0x0075 }
            int r1 = r4.out     // Catch:{ all -> 0x0075 }
            if (r0 != r1) goto L_0x004c
            java.lang.Thread r0 = r4.readSide     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x003f
            boolean r0 = r0.isAlive()     // Catch:{ all -> 0x0075 }
            if (r0 == 0) goto L_0x0037
            goto L_0x003f
        L_0x0037:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0075 }
            java.lang.String r0 = "Pipe broken"
            r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0075 }
            throw r5     // Catch:{ all -> 0x0075 }
        L_0x003f:
            r4.notifyAll()     // Catch:{ all -> 0x0075 }
            r0 = 1000(0x3e8, double:4.94E-321)
            r4.wait(r0)     // Catch:{ InterruptedException -> 0x0048 }
            goto L_0x0026
        L_0x0048:
            libcore.p030io.IoUtils.throwInterruptedIoException()     // Catch:{ all -> 0x0075 }
            goto L_0x0026
        L_0x004c:
            r1 = 0
            if (r0 >= 0) goto L_0x0053
            r4.f529in = r1     // Catch:{ all -> 0x0075 }
            r4.out = r1     // Catch:{ all -> 0x0075 }
        L_0x0053:
            char[] r0 = r4.buffer     // Catch:{ all -> 0x0075 }
            int r2 = r4.f529in     // Catch:{ all -> 0x0075 }
            int r3 = r2 + 1
            r4.f529in = r3     // Catch:{ all -> 0x0075 }
            char r5 = (char) r5     // Catch:{ all -> 0x0075 }
            r0[r2] = r5     // Catch:{ all -> 0x0075 }
            int r5 = r0.length     // Catch:{ all -> 0x0075 }
            if (r3 < r5) goto L_0x0063
            r4.f529in = r1     // Catch:{ all -> 0x0075 }
        L_0x0063:
            monitor-exit(r4)
            return
        L_0x0065:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0075 }
            java.lang.String r0 = "Pipe closed"
            r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0075 }
            throw r5     // Catch:{ all -> 0x0075 }
        L_0x006d:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0075 }
            java.lang.String r0 = "Pipe not connected"
            r5.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0075 }
            throw r5     // Catch:{ all -> 0x0075 }
        L_0x0075:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PipedReader.receive(int):void");
    }

    /* access modifiers changed from: package-private */
    public synchronized void receive(char[] cArr, int i, int i2) throws IOException {
        while (true) {
            i2--;
            if (i2 >= 0) {
                int i3 = i + 1;
                receive(cArr[i]);
                i = i3;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void receivedLast() {
        this.closedByWriter = true;
        notifyAll();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:24|25|(2:29|(1:31)(3:57|32|33))|34|35|36|37|38|39|61|56) */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0071, code lost:
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
            boolean r0 = r5.connected     // Catch:{ all -> 0x0082 }
            if (r0 == 0) goto L_0x007a
            boolean r0 = r5.closedByReader     // Catch:{ all -> 0x0082 }
            if (r0 != 0) goto L_0x0072
            java.lang.Thread r0 = r5.writeSide     // Catch:{ all -> 0x0082 }
            if (r0 == 0) goto L_0x0024
            boolean r0 = r0.isAlive()     // Catch:{ all -> 0x0082 }
            if (r0 != 0) goto L_0x0024
            boolean r0 = r5.closedByWriter     // Catch:{ all -> 0x0082 }
            if (r0 != 0) goto L_0x0024
            int r0 = r5.f529in     // Catch:{ all -> 0x0082 }
            if (r0 < 0) goto L_0x001c
            goto L_0x0024
        L_0x001c:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0082 }
            java.lang.String r1 = "Write end dead"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0082 }
            throw r0     // Catch:{ all -> 0x0082 }
        L_0x0024:
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0082 }
            r5.readSide = r0     // Catch:{ all -> 0x0082 }
            r0 = 2
        L_0x002b:
            int r1 = r5.f529in     // Catch:{ all -> 0x0082 }
            r2 = -1
            if (r1 >= 0) goto L_0x005a
            boolean r1 = r5.closedByWriter     // Catch:{ all -> 0x0082 }
            if (r1 == 0) goto L_0x0036
            monitor-exit(r5)
            return r2
        L_0x0036:
            java.lang.Thread r1 = r5.writeSide     // Catch:{ all -> 0x0082 }
            if (r1 == 0) goto L_0x004d
            boolean r1 = r1.isAlive()     // Catch:{ all -> 0x0082 }
            if (r1 != 0) goto L_0x004d
            int r0 = r0 + -1
            if (r0 < 0) goto L_0x0045
            goto L_0x004d
        L_0x0045:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0082 }
            java.lang.String r1 = "Pipe broken"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0082 }
            throw r0     // Catch:{ all -> 0x0082 }
        L_0x004d:
            r5.notifyAll()     // Catch:{ all -> 0x0082 }
            r1 = 1000(0x3e8, double:4.94E-321)
            r5.wait(r1)     // Catch:{ InterruptedException -> 0x0056 }
            goto L_0x002b
        L_0x0056:
            libcore.p030io.IoUtils.throwInterruptedIoException()     // Catch:{ all -> 0x0082 }
            goto L_0x002b
        L_0x005a:
            char[] r0 = r5.buffer     // Catch:{ all -> 0x0082 }
            int r3 = r5.out     // Catch:{ all -> 0x0082 }
            int r4 = r3 + 1
            r5.out = r4     // Catch:{ all -> 0x0082 }
            char r3 = r0[r3]     // Catch:{ all -> 0x0082 }
            int r0 = r0.length     // Catch:{ all -> 0x0082 }
            if (r4 < r0) goto L_0x006a
            r0 = 0
            r5.out = r0     // Catch:{ all -> 0x0082 }
        L_0x006a:
            int r0 = r5.out     // Catch:{ all -> 0x0082 }
            if (r1 != r0) goto L_0x0070
            r5.f529in = r2     // Catch:{ all -> 0x0082 }
        L_0x0070:
            monitor-exit(r5)
            return r3
        L_0x0072:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0082 }
            java.lang.String r1 = "Pipe closed"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0082 }
            throw r0     // Catch:{ all -> 0x0082 }
        L_0x007a:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x0082 }
            java.lang.String r1 = "Pipe not connected"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0082 }
            throw r0     // Catch:{ all -> 0x0082 }
        L_0x0082:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PipedReader.read():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0068, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(char[] r9, int r10, int r11) throws java.p026io.IOException {
        /*
            r8 = this;
            monitor-enter(r8)
            boolean r0 = r8.connected     // Catch:{ all -> 0x007f }
            if (r0 == 0) goto L_0x0077
            boolean r0 = r8.closedByReader     // Catch:{ all -> 0x007f }
            if (r0 != 0) goto L_0x006f
            java.lang.Thread r0 = r8.writeSide     // Catch:{ all -> 0x007f }
            if (r0 == 0) goto L_0x0024
            boolean r0 = r0.isAlive()     // Catch:{ all -> 0x007f }
            if (r0 != 0) goto L_0x0024
            boolean r0 = r8.closedByWriter     // Catch:{ all -> 0x007f }
            if (r0 != 0) goto L_0x0024
            int r0 = r8.f529in     // Catch:{ all -> 0x007f }
            if (r0 < 0) goto L_0x001c
            goto L_0x0024
        L_0x001c:
            java.io.IOException r9 = new java.io.IOException     // Catch:{ all -> 0x007f }
            java.lang.String r10 = "Write end dead"
            r9.<init>((java.lang.String) r10)     // Catch:{ all -> 0x007f }
            throw r9     // Catch:{ all -> 0x007f }
        L_0x0024:
            if (r10 < 0) goto L_0x0069
            int r0 = r9.length     // Catch:{ all -> 0x007f }
            if (r10 > r0) goto L_0x0069
            if (r11 < 0) goto L_0x0069
            int r0 = r10 + r11
            int r1 = r9.length     // Catch:{ all -> 0x007f }
            if (r0 > r1) goto L_0x0069
            if (r0 < 0) goto L_0x0069
            r0 = 0
            if (r11 != 0) goto L_0x0037
            monitor-exit(r8)
            return r0
        L_0x0037:
            int r1 = r8.read()     // Catch:{ all -> 0x007f }
            r2 = -1
            if (r1 >= 0) goto L_0x0040
            monitor-exit(r8)
            return r2
        L_0x0040:
            char r1 = (char) r1
            r9[r10] = r1     // Catch:{ all -> 0x007f }
            r1 = 1
        L_0x0044:
            int r3 = r8.f529in     // Catch:{ all -> 0x007f }
            if (r3 < 0) goto L_0x0067
            int r11 = r11 + r2
            if (r11 <= 0) goto L_0x0067
            int r4 = r10 + r1
            char[] r5 = r8.buffer     // Catch:{ all -> 0x007f }
            int r6 = r8.out     // Catch:{ all -> 0x007f }
            int r7 = r6 + 1
            r8.out = r7     // Catch:{ all -> 0x007f }
            char r6 = r5[r6]     // Catch:{ all -> 0x007f }
            r9[r4] = r6     // Catch:{ all -> 0x007f }
            int r1 = r1 + 1
            int r4 = r5.length     // Catch:{ all -> 0x007f }
            if (r7 < r4) goto L_0x0060
            r8.out = r0     // Catch:{ all -> 0x007f }
        L_0x0060:
            int r4 = r8.out     // Catch:{ all -> 0x007f }
            if (r3 != r4) goto L_0x0044
            r8.f529in = r2     // Catch:{ all -> 0x007f }
            goto L_0x0044
        L_0x0067:
            monitor-exit(r8)
            return r1
        L_0x0069:
            java.lang.IndexOutOfBoundsException r9 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x007f }
            r9.<init>()     // Catch:{ all -> 0x007f }
            throw r9     // Catch:{ all -> 0x007f }
        L_0x006f:
            java.io.IOException r9 = new java.io.IOException     // Catch:{ all -> 0x007f }
            java.lang.String r10 = "Pipe closed"
            r9.<init>((java.lang.String) r10)     // Catch:{ all -> 0x007f }
            throw r9     // Catch:{ all -> 0x007f }
        L_0x0077:
            java.io.IOException r9 = new java.io.IOException     // Catch:{ all -> 0x007f }
            java.lang.String r10 = "Pipe not connected"
            r9.<init>((java.lang.String) r10)     // Catch:{ all -> 0x007f }
            throw r9     // Catch:{ all -> 0x007f }
        L_0x007f:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.PipedReader.read(char[], int, int):int");
    }

    public synchronized boolean ready() throws IOException {
        if (!this.connected) {
            throw new IOException("Pipe not connected");
        } else if (!this.closedByReader) {
            Thread thread = this.writeSide;
            if (thread != null && !thread.isAlive() && !this.closedByWriter) {
                if (this.f529in < 0) {
                    throw new IOException("Write end dead");
                }
            }
            if (this.f529in < 0) {
                return false;
            }
            return true;
        } else {
            throw new IOException("Pipe closed");
        }
    }

    public void close() throws IOException {
        this.f529in = -1;
        this.closedByReader = true;
    }
}
