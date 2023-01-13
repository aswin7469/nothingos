package java.p026io;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* renamed from: java.io.BufferedReader */
public class BufferedReader extends Reader {
    private static final int INVALIDATED = -2;
    private static final int UNMARKED = -1;
    private static int defaultCharBufferSize = 8192;
    private static int defaultExpectedLineLength = 80;

    /* renamed from: cb */
    private char[] f508cb;

    /* renamed from: in */
    private Reader f509in;
    private int markedChar;
    private boolean markedSkipLF;
    private int nChars;
    private int nextChar;
    private int readAheadLimit;
    private boolean skipLF;

    public boolean markSupported() {
        return true;
    }

    public BufferedReader(Reader reader, int i) {
        super(reader);
        this.markedChar = -1;
        this.readAheadLimit = 0;
        this.skipLF = false;
        this.markedSkipLF = false;
        if (i > 0) {
            this.f509in = reader;
            this.f508cb = new char[i];
            this.nChars = 0;
            this.nextChar = 0;
            return;
        }
        throw new IllegalArgumentException("Buffer size <= 0");
    }

    public BufferedReader(Reader reader) {
        this(reader, defaultCharBufferSize);
    }

    private void ensureOpen() throws IOException {
        if (this.f509in == null) {
            throw new IOException("Stream closed");
        }
    }

    private void fill() throws IOException {
        int read;
        int i = this.markedChar;
        int i2 = 0;
        if (i > -1) {
            int i3 = this.nextChar - i;
            int i4 = this.readAheadLimit;
            if (i3 >= i4) {
                this.markedChar = -2;
                this.readAheadLimit = 0;
            } else {
                char[] cArr = this.f508cb;
                if (i4 <= cArr.length) {
                    System.arraycopy((Object) cArr, i, (Object) cArr, 0, i3);
                    this.markedChar = 0;
                } else {
                    int length = cArr.length * 2;
                    if (length <= i4) {
                        i4 = length;
                    }
                    char[] cArr2 = new char[i4];
                    System.arraycopy((Object) cArr, i, (Object) cArr2, 0, i3);
                    this.f508cb = cArr2;
                    this.markedChar = 0;
                }
                this.nChars = i3;
                this.nextChar = i3;
                i2 = i3;
            }
        }
        do {
            Reader reader = this.f509in;
            char[] cArr3 = this.f508cb;
            read = reader.read(cArr3, i2, cArr3.length - i2);
        } while (read == 0);
        if (read > 0) {
            this.nChars = read + i2;
            this.nextChar = i2;
        }
    }

    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            while (true) {
                if (this.nextChar >= this.nChars) {
                    fill();
                    if (this.nextChar >= this.nChars) {
                        return -1;
                    }
                }
                if (!this.skipLF) {
                    break;
                }
                this.skipLF = false;
                char[] cArr = this.f508cb;
                int i = this.nextChar;
                if (cArr[i] != 10) {
                    break;
                }
                this.nextChar = i + 1;
            }
            char[] cArr2 = this.f508cb;
            int i2 = this.nextChar;
            this.nextChar = i2 + 1;
            char c = cArr2[i2];
            return c;
        }
    }

    private int read1(char[] cArr, int i, int i2) throws IOException {
        if (this.nextChar >= this.nChars) {
            if (i2 >= this.f508cb.length && this.markedChar <= -1 && !this.skipLF) {
                return this.f509in.read(cArr, i, i2);
            }
            fill();
        }
        int i3 = this.nextChar;
        int i4 = this.nChars;
        if (i3 >= i4) {
            return -1;
        }
        if (this.skipLF) {
            this.skipLF = false;
            if (this.f508cb[i3] == 10) {
                int i5 = i3 + 1;
                this.nextChar = i5;
                if (i5 >= i4) {
                    fill();
                }
                if (this.nextChar >= this.nChars) {
                    return -1;
                }
            }
        }
        int min = Math.min(i2, this.nChars - this.nextChar);
        System.arraycopy((Object) this.f508cb, this.nextChar, (Object) cArr, i, min);
        this.nextChar += min;
        return min;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0039, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(char[] r5, int r6, int r7) throws java.p026io.IOException {
        /*
            r4 = this;
            java.lang.Object r0 = r4.lock
            monitor-enter(r0)
            r4.ensureOpen()     // Catch:{ all -> 0x0040 }
            if (r6 < 0) goto L_0x003a
            int r1 = r5.length     // Catch:{ all -> 0x0040 }
            if (r6 > r1) goto L_0x003a
            if (r7 < 0) goto L_0x003a
            int r1 = r6 + r7
            int r2 = r5.length     // Catch:{ all -> 0x0040 }
            if (r1 > r2) goto L_0x003a
            if (r1 < 0) goto L_0x003a
            if (r7 != 0) goto L_0x0019
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            r4 = 0
            return r4
        L_0x0019:
            int r1 = r4.read1(r5, r6, r7)     // Catch:{ all -> 0x0040 }
            if (r1 > 0) goto L_0x0021
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return r1
        L_0x0021:
            if (r1 >= r7) goto L_0x0038
            java.io.Reader r2 = r4.f509in     // Catch:{ all -> 0x0040 }
            boolean r2 = r2.ready()     // Catch:{ all -> 0x0040 }
            if (r2 == 0) goto L_0x0038
            int r2 = r6 + r1
            int r3 = r7 - r1
            int r2 = r4.read1(r5, r2, r3)     // Catch:{ all -> 0x0040 }
            if (r2 > 0) goto L_0x0036
            goto L_0x0038
        L_0x0036:
            int r1 = r1 + r2
            goto L_0x0021
        L_0x0038:
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            return r1
        L_0x003a:
            java.lang.IndexOutOfBoundsException r4 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x0040 }
            r4.<init>()     // Catch:{ all -> 0x0040 }
            throw r4     // Catch:{ all -> 0x0040 }
        L_0x0040:
            r4 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0040 }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.BufferedReader.read(char[], int, int):int");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0032, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0058, code lost:
        r6 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0080, code lost:
        return r1;
     */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x001a  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0033  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0023 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readLine(boolean r10) throws java.p026io.IOException {
        /*
            r9 = this;
            java.lang.Object r0 = r9.lock
            monitor-enter(r0)
            r9.ensureOpen()     // Catch:{ all -> 0x0092 }
            r1 = 0
            r2 = 1
            if (r10 != 0) goto L_0x0011
            boolean r10 = r9.skipLF     // Catch:{ all -> 0x0092 }
            if (r10 == 0) goto L_0x000f
            goto L_0x0011
        L_0x000f:
            r10 = r1
            goto L_0x0012
        L_0x0011:
            r10 = r2
        L_0x0012:
            r3 = 0
            r4 = r3
        L_0x0014:
            int r5 = r9.nextChar     // Catch:{ all -> 0x0092 }
            int r6 = r9.nChars     // Catch:{ all -> 0x0092 }
            if (r5 < r6) goto L_0x001d
            r9.fill()     // Catch:{ all -> 0x0092 }
        L_0x001d:
            int r5 = r9.nextChar     // Catch:{ all -> 0x0092 }
            int r6 = r9.nChars     // Catch:{ all -> 0x0092 }
            if (r5 < r6) goto L_0x0033
            if (r4 == 0) goto L_0x0031
            int r9 = r4.length()     // Catch:{ all -> 0x0092 }
            if (r9 <= 0) goto L_0x0031
            java.lang.String r9 = r4.toString()     // Catch:{ all -> 0x0092 }
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r9
        L_0x0031:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r3
        L_0x0033:
            r6 = 10
            if (r10 == 0) goto L_0x0041
            char[] r10 = r9.f508cb     // Catch:{ all -> 0x0092 }
            char r10 = r10[r5]     // Catch:{ all -> 0x0092 }
            if (r10 != r6) goto L_0x0041
            int r5 = r5 + 1
            r9.nextChar = r5     // Catch:{ all -> 0x0092 }
        L_0x0041:
            r9.skipLF = r1     // Catch:{ all -> 0x0092 }
            int r10 = r9.nextChar     // Catch:{ all -> 0x0092 }
            r5 = r1
        L_0x0046:
            int r7 = r9.nChars     // Catch:{ all -> 0x0092 }
            r8 = 13
            if (r10 >= r7) goto L_0x005a
            char[] r5 = r9.f508cb     // Catch:{ all -> 0x0092 }
            char r5 = r5[r10]     // Catch:{ all -> 0x0092 }
            if (r5 == r6) goto L_0x0058
            if (r5 != r8) goto L_0x0055
            goto L_0x0058
        L_0x0055:
            int r10 = r10 + 1
            goto L_0x0046
        L_0x0058:
            r6 = r2
            goto L_0x005b
        L_0x005a:
            r6 = r1
        L_0x005b:
            int r7 = r9.nextChar     // Catch:{ all -> 0x0092 }
            r9.nextChar = r10     // Catch:{ all -> 0x0092 }
            if (r6 == 0) goto L_0x0081
            if (r4 != 0) goto L_0x006c
            java.lang.String r1 = new java.lang.String     // Catch:{ all -> 0x0092 }
            char[] r3 = r9.f508cb     // Catch:{ all -> 0x0092 }
            int r10 = r10 - r7
            r1.<init>((char[]) r3, (int) r7, (int) r10)     // Catch:{ all -> 0x0092 }
            goto L_0x0076
        L_0x006c:
            char[] r1 = r9.f508cb     // Catch:{ all -> 0x0092 }
            int r10 = r10 - r7
            r4.append((char[]) r1, (int) r7, (int) r10)     // Catch:{ all -> 0x0092 }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x0092 }
        L_0x0076:
            int r10 = r9.nextChar     // Catch:{ all -> 0x0092 }
            int r10 = r10 + r2
            r9.nextChar = r10     // Catch:{ all -> 0x0092 }
            if (r5 != r8) goto L_0x007f
            r9.skipLF = r2     // Catch:{ all -> 0x0092 }
        L_0x007f:
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            return r1
        L_0x0081:
            if (r4 != 0) goto L_0x008a
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0092 }
            int r5 = defaultExpectedLineLength     // Catch:{ all -> 0x0092 }
            r4.<init>((int) r5)     // Catch:{ all -> 0x0092 }
        L_0x008a:
            char[] r5 = r9.f508cb     // Catch:{ all -> 0x0092 }
            int r10 = r10 - r7
            r4.append((char[]) r5, (int) r7, (int) r10)     // Catch:{ all -> 0x0092 }
            r10 = r1
            goto L_0x0014
        L_0x0092:
            r9 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0092 }
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.BufferedReader.readLine(boolean):java.lang.String");
    }

    public String readLine() throws IOException {
        return readLine(false);
    }

    public long skip(long j) throws IOException {
        long j2;
        long j3 = 0;
        if (j >= 0) {
            synchronized (this.lock) {
                ensureOpen();
                long j4 = j;
                while (true) {
                    if (j4 <= 0) {
                        break;
                    }
                    if (this.nextChar >= this.nChars) {
                        fill();
                    }
                    int i = this.nextChar;
                    int i2 = this.nChars;
                    if (i >= i2) {
                        break;
                    }
                    if (this.skipLF) {
                        this.skipLF = false;
                        if (this.f508cb[i] == 10) {
                            this.nextChar = i + 1;
                        }
                    }
                    int i3 = this.nextChar;
                    long j5 = (long) (i2 - i3);
                    if (j4 <= j5) {
                        this.nextChar = (int) (((long) i3) + j4);
                        break;
                    }
                    j4 -= j5;
                    this.nextChar = i2;
                }
                j3 = j4;
                j2 = j - j3;
            }
            return j2;
        }
        throw new IllegalArgumentException("skip value is negative");
    }

    public boolean ready() throws IOException {
        boolean z;
        synchronized (this.lock) {
            ensureOpen();
            z = false;
            if (this.skipLF) {
                if (this.nextChar >= this.nChars && this.f509in.ready()) {
                    fill();
                }
                int i = this.nextChar;
                if (i < this.nChars) {
                    if (this.f508cb[i] == 10) {
                        this.nextChar = i + 1;
                    }
                    this.skipLF = false;
                }
            }
            if (this.nextChar < this.nChars || this.f509in.ready()) {
                z = true;
            }
        }
        return z;
    }

    public void mark(int i) throws IOException {
        if (i >= 0) {
            synchronized (this.lock) {
                ensureOpen();
                this.readAheadLimit = i;
                this.markedChar = this.nextChar;
                this.markedSkipLF = this.skipLF;
            }
            return;
        }
        throw new IllegalArgumentException("Read-ahead limit < 0");
    }

    public void reset() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            int i = this.markedChar;
            if (i < 0) {
                throw new IOException(i == -2 ? "Mark invalid" : "Stream not marked");
            }
            this.nextChar = i;
            this.skipLF = this.markedSkipLF;
        }
    }

    public void close() throws IOException {
        synchronized (this.lock) {
            Reader reader = this.f509in;
            if (reader != null) {
                try {
                    reader.close();
                } finally {
                    this.f509in = null;
                    this.f508cb = null;
                }
            }
        }
    }

    public Stream<String> lines() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<String>() {
            String nextLine = null;

            public boolean hasNext() {
                if (this.nextLine != null) {
                    return true;
                }
                try {
                    String readLine = BufferedReader.this.readLine();
                    this.nextLine = readLine;
                    if (readLine != null) {
                        return true;
                    }
                    return false;
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            public String next() {
                if (this.nextLine != null || hasNext()) {
                    String str = this.nextLine;
                    this.nextLine = null;
                    return str;
                }
                throw new NoSuchElementException();
            }
        }, 272), false);
    }
}
