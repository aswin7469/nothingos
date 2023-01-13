package sun.net.www;

import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import sun.net.ProgressSource;

public class MeteredStream extends FilterInputStream {
    protected boolean closed = false;
    protected long count = 0;
    protected long expected;
    protected int markLimit = -1;
    protected long markedCount = 0;

    /* renamed from: pi */
    protected ProgressSource f866pi;

    public MeteredStream(InputStream inputStream, ProgressSource progressSource, long j) {
        super(inputStream);
        this.f866pi = progressSource;
        this.expected = j;
        if (progressSource != null) {
            progressSource.updateProgress(0, j);
        }
    }

    private final void justRead(long j) throws IOException {
        if (j != -1) {
            long j2 = this.count + j;
            this.count = j2;
            if (j2 - this.markedCount > ((long) this.markLimit)) {
                this.markLimit = -1;
            }
            ProgressSource progressSource = this.f866pi;
            if (progressSource != null) {
                progressSource.updateProgress(j2, this.expected);
            }
            if (!isMarked()) {
                long j3 = this.expected;
                if (j3 > 0 && this.count >= j3) {
                    close();
                }
            }
        } else if (!isMarked()) {
            close();
        }
    }

    private boolean isMarked() {
        int i = this.markLimit;
        if (i >= 0 && this.count - this.markedCount <= ((long) i)) {
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read() throws java.p026io.IOException {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.closed     // Catch:{ all -> 0x001c }
            r1 = -1
            if (r0 == 0) goto L_0x0008
            monitor-exit(r3)
            return r1
        L_0x0008:
            java.io.InputStream r0 = r3.f519in     // Catch:{ all -> 0x001c }
            int r0 = r0.read()     // Catch:{ all -> 0x001c }
            if (r0 == r1) goto L_0x0016
            r1 = 1
            r3.justRead(r1)     // Catch:{ all -> 0x001c }
            goto L_0x001a
        L_0x0016:
            long r1 = (long) r0     // Catch:{ all -> 0x001c }
            r3.justRead(r1)     // Catch:{ all -> 0x001c }
        L_0x001a:
            monitor-exit(r3)
            return r0
        L_0x001c:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MeteredStream.read():int");
    }

    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.closed) {
            return -1;
        }
        int read = this.f519in.read(bArr, i, i2);
        justRead((long) read);
        return read;
    }

    public synchronized long skip(long j) throws IOException {
        if (this.closed) {
            return 0;
        }
        long j2 = this.expected;
        long j3 = this.count;
        if (j > j2 - j3) {
            j = j2 - j3;
        }
        long skip = this.f519in.skip(j);
        justRead(skip);
        return skip;
    }

    public synchronized void close() throws IOException {
        if (!this.closed) {
            ProgressSource progressSource = this.f866pi;
            if (progressSource != null) {
                progressSource.finishTracking();
            }
            this.closed = true;
            this.f519in.close();
        }
    }

    public synchronized int available() throws IOException {
        return this.closed ? 0 : this.f519in.available();
    }

    public synchronized void mark(int i) {
        if (!this.closed) {
            super.mark(i);
            this.markedCount = this.count;
            this.markLimit = i;
        }
    }

    public synchronized void reset() throws IOException {
        if (!this.closed) {
            if (isMarked()) {
                this.count = this.markedCount;
                super.reset();
                return;
            }
            throw new IOException("Resetting to an invalid mark");
        }
    }

    public boolean markSupported() {
        if (this.closed) {
            return false;
        }
        return super.markSupported();
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            close();
            ProgressSource progressSource = this.f866pi;
            if (progressSource != null) {
                progressSource.close();
            }
        } finally {
            super.finalize();
        }
    }
}
