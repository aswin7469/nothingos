package java.p026io;

/* renamed from: java.io.FilterOutputStream */
public class FilterOutputStream extends OutputStream {
    private final Object closeLock = new Object();
    private volatile boolean closed;
    protected OutputStream out;

    public FilterOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    public void write(int i) throws IOException {
        this.out.write(i);
    }

    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i2 + i;
        if ((i | i2 | (bArr.length - i3) | i3) >= 0) {
            for (int i4 = 0; i4 < i2; i4++) {
                write((int) bArr[i + i4]);
            }
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x001d, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r2.out.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0023, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0024, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0027, code lost:
        if ((r0 instanceof java.lang.ThreadDeath) == false) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x002e, code lost:
        r0.addSuppressed(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0033, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0034, code lost:
        if (r0 != r2) goto L_0x0036;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0036, code lost:
        r2.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0039, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.p026io.IOException {
        /*
            r2 = this;
            boolean r0 = r2.closed
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.Object r0 = r2.closeLock
            monitor-enter(r0)
            boolean r1 = r2.closed     // Catch:{ all -> 0x003a }
            if (r1 == 0) goto L_0x000e
            monitor-exit(r0)     // Catch:{ all -> 0x003a }
            return
        L_0x000e:
            r1 = 1
            r2.closed = r1     // Catch:{ all -> 0x003a }
            monitor-exit(r0)     // Catch:{ all -> 0x003a }
            r2.flush()     // Catch:{ all -> 0x001b }
            java.io.OutputStream r2 = r2.out
            r2.close()
            return
        L_0x001b:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x001d }
        L_0x001d:
            r1 = move-exception
            java.io.OutputStream r2 = r2.out     // Catch:{ all -> 0x0024 }
            r2.close()     // Catch:{ all -> 0x0024 }
            throw r1
        L_0x0024:
            r2 = move-exception
            boolean r1 = r0 instanceof java.lang.ThreadDeath
            if (r1 == 0) goto L_0x0034
            boolean r1 = r2 instanceof java.lang.ThreadDeath
            if (r1 == 0) goto L_0x002e
            goto L_0x0034
        L_0x002e:
            r0.addSuppressed(r2)
            java.lang.ThreadDeath r0 = (java.lang.ThreadDeath) r0
            throw r0
        L_0x0034:
            if (r0 == r2) goto L_0x0039
            r2.addSuppressed(r0)
        L_0x0039:
            throw r2
        L_0x003a:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003a }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.FilterOutputStream.close():void");
    }
}
