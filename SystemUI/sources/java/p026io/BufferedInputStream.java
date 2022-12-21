package java.p026io;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* renamed from: java.io.BufferedInputStream */
public class BufferedInputStream extends FilterInputStream {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;
    private static final AtomicReferenceFieldUpdater<BufferedInputStream, byte[]> bufUpdater = AtomicReferenceFieldUpdater.newUpdater(BufferedInputStream.class, byte[].class, "buf");
    protected volatile byte[] buf;
    protected int count;
    protected int marklimit;
    protected int markpos;
    protected int pos;

    public boolean markSupported() {
        return true;
    }

    private InputStream getInIfOpen() throws IOException {
        InputStream inputStream = this.f521in;
        if (inputStream != null) {
            return inputStream;
        }
        throw new IOException("Stream closed");
    }

    private byte[] getBufIfOpen() throws IOException {
        byte[] bArr = this.buf;
        if (bArr != null) {
            return bArr;
        }
        throw new IOException("Stream closed");
    }

    public BufferedInputStream(InputStream inputStream) {
        this(inputStream, 8192);
    }

    public BufferedInputStream(InputStream inputStream, int i) {
        super(inputStream);
        this.markpos = -1;
        if (i > 0) {
            this.buf = new byte[i];
            return;
        }
        throw new IllegalArgumentException("Buffer size <= 0");
    }

    private void fill() throws IOException {
        byte[] bufIfOpen = getBufIfOpen();
        int i = this.markpos;
        if (i < 0) {
            this.pos = 0;
        } else {
            int i2 = this.pos;
            if (i2 >= bufIfOpen.length) {
                if (i > 0) {
                    int i3 = i2 - i;
                    System.arraycopy((Object) bufIfOpen, i, (Object) bufIfOpen, 0, i3);
                    this.pos = i3;
                    this.markpos = 0;
                } else {
                    int length = bufIfOpen.length;
                    int i4 = this.marklimit;
                    if (length >= i4) {
                        this.markpos = -1;
                        this.pos = 0;
                    } else {
                        int length2 = bufIfOpen.length;
                        int i5 = MAX_BUFFER_SIZE;
                        if (length2 < MAX_BUFFER_SIZE) {
                            if (i2 <= MAX_BUFFER_SIZE - i2) {
                                i5 = i2 * 2;
                            }
                            if (i5 <= i4) {
                                i4 = i5;
                            }
                            byte[] bArr = new byte[i4];
                            System.arraycopy((Object) bufIfOpen, 0, (Object) bArr, 0, i2);
                            if (bufUpdater.compareAndSet(this, bufIfOpen, bArr)) {
                                bufIfOpen = bArr;
                            } else {
                                throw new IOException("Stream closed");
                            }
                        } else {
                            throw new OutOfMemoryError("Required array size too large");
                        }
                    }
                }
            }
        }
        this.count = this.pos;
        InputStream inIfOpen = getInIfOpen();
        int i6 = this.pos;
        int read = inIfOpen.read(bufIfOpen, i6, bufIfOpen.length - i6);
        if (read > 0) {
            this.count = read + this.pos;
        }
    }

    public synchronized int read() throws IOException {
        if (this.pos >= this.count) {
            fill();
            if (this.pos >= this.count) {
                return -1;
            }
        }
        byte[] bufIfOpen = getBufIfOpen();
        int i = this.pos;
        this.pos = i + 1;
        return bufIfOpen[i] & 255;
    }

    private int read1(byte[] bArr, int i, int i2) throws IOException {
        int i3 = this.count - this.pos;
        if (i3 <= 0) {
            if (i2 >= getBufIfOpen().length && this.markpos < 0) {
                return getInIfOpen().read(bArr, i, i2);
            }
            fill();
            i3 = this.count - this.pos;
            if (i3 <= 0) {
                return -1;
            }
        }
        if (i3 < i2) {
            i2 = i3;
        }
        System.arraycopy((Object) getBufIfOpen(), this.pos, (Object) bArr, i, i2);
        this.pos += i2;
        return i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0021, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(byte[] r4, int r5, int r6) throws java.p026io.IOException {
        /*
            r3 = this;
            monitor-enter(r3)
            r3.getBufIfOpen()     // Catch:{ all -> 0x0039 }
            r0 = r5 | r6
            int r1 = r5 + r6
            r0 = r0 | r1
            int r2 = r4.length     // Catch:{ all -> 0x0039 }
            int r2 = r2 - r1
            r0 = r0 | r2
            if (r0 < 0) goto L_0x0033
            r0 = 0
            if (r6 != 0) goto L_0x0013
            monitor-exit(r3)
            return r0
        L_0x0013:
            int r1 = r5 + r0
            int r2 = r6 - r0
            int r1 = r3.read1(r4, r1, r2)     // Catch:{ all -> 0x0039 }
            if (r1 > 0) goto L_0x0022
            if (r0 != 0) goto L_0x0020
            r0 = r1
        L_0x0020:
            monitor-exit(r3)
            return r0
        L_0x0022:
            int r0 = r0 + r1
            if (r0 < r6) goto L_0x0027
            monitor-exit(r3)
            return r0
        L_0x0027:
            java.io.InputStream r1 = r3.f521in     // Catch:{ all -> 0x0039 }
            if (r1 == 0) goto L_0x0013
            int r1 = r1.available()     // Catch:{ all -> 0x0039 }
            if (r1 > 0) goto L_0x0013
            monitor-exit(r3)
            return r0
        L_0x0033:
            java.lang.IndexOutOfBoundsException r4 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x0039 }
            r4.<init>()     // Catch:{ all -> 0x0039 }
            throw r4     // Catch:{ all -> 0x0039 }
        L_0x0039:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.BufferedInputStream.read(byte[], int, int):int");
    }

    public synchronized long skip(long j) throws IOException {
        getBufIfOpen();
        if (j <= 0) {
            return 0;
        }
        long j2 = (long) (this.count - this.pos);
        if (j2 <= 0) {
            if (this.markpos < 0) {
                return getInIfOpen().skip(j);
            }
            fill();
            j2 = (long) (this.count - this.pos);
            if (j2 <= 0) {
                return 0;
            }
        }
        if (j2 < j) {
            j = j2;
        }
        this.pos = (int) (((long) this.pos) + j);
        return j;
    }

    public synchronized int available() throws IOException {
        int i;
        int i2 = this.count - this.pos;
        int available = getInIfOpen().available();
        i = Integer.MAX_VALUE;
        if (i2 <= Integer.MAX_VALUE - available) {
            i = i2 + available;
        }
        return i;
    }

    public synchronized void mark(int i) {
        this.marklimit = i;
        this.markpos = this.pos;
    }

    public synchronized void reset() throws IOException {
        getBufIfOpen();
        int i = this.markpos;
        if (i >= 0) {
            this.pos = i;
        } else {
            throw new IOException("Resetting to invalid mark");
        }
    }

    public void close() throws IOException {
        byte[] bArr;
        do {
            bArr = this.buf;
            if (bArr == null) {
                return;
            }
        } while (!bufUpdater.compareAndSet(this, bArr, null));
        InputStream inputStream = this.f521in;
        this.f521in = null;
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
