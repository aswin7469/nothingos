package java.util.zip;

import com.android.settingslib.datetime.ZoneGetter;
import dalvik.system.CloseGuard;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.p026io.Closeable;
import java.p026io.EOFException;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.WeakHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ZipFile implements ZipConstants, Closeable {
    private static final int DEFLATED = 8;
    private static final int JZENTRY_COMMENT = 2;
    private static final int JZENTRY_EXTRA = 1;
    private static final int JZENTRY_NAME = 0;
    public static final int OPEN_DELETE = 4;
    public static final int OPEN_READ = 1;
    private static final int STORED = 0;
    private static final boolean usemmap = true;
    /* access modifiers changed from: private */
    public volatile boolean closeRequested;
    private final File fileToRemoveOnClose;
    private final CloseGuard guard;
    private Deque<Inflater> inflaterCache;
    /* access modifiers changed from: private */
    public long jzfile;
    private final boolean locsig;
    /* access modifiers changed from: private */
    public final String name;
    /* access modifiers changed from: private */
    public final Map<InputStream, Inflater> streams;
    /* access modifiers changed from: private */
    public final int total;

    /* renamed from: zc */
    private ZipCoder f806zc;

    private static native void close(long j);

    /* access modifiers changed from: private */
    public static native void freeEntry(long j, long j2);

    private static native byte[] getCommentBytes(long j);

    private static native long getEntry(long j, byte[] bArr, boolean z);

    private static native byte[] getEntryBytes(long j, int i);

    /* access modifiers changed from: private */
    public static native long getEntryCSize(long j);

    private static native long getEntryCrc(long j);

    private static native int getEntryFlag(long j);

    private static native int getEntryMethod(long j);

    /* access modifiers changed from: private */
    public static native long getEntrySize(long j);

    private static native long getEntryTime(long j);

    private static native int getFileDescriptor(long j);

    /* access modifiers changed from: private */
    public static native long getNextEntry(long j, int i);

    private static native int getTotal(long j);

    /* access modifiers changed from: private */
    public static native String getZipMessage(long j);

    private static native long open(String str, int i, long j, boolean z) throws IOException;

    /* access modifiers changed from: private */
    public static native int read(long j, long j2, long j3, byte[] bArr, int i, int i2);

    private static native boolean startsWithLOC(long j);

    public ZipFile(String str) throws IOException {
        this(new File(str), 1);
    }

    public ZipFile(File file, int i) throws IOException {
        this(file, i, StandardCharsets.UTF_8);
    }

    public ZipFile(File file) throws ZipException, IOException {
        this(file, 1);
    }

    public ZipFile(File file, int i, Charset charset) throws IOException {
        this.closeRequested = false;
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.streams = new WeakHashMap();
        this.inflaterCache = new ArrayDeque();
        if ((i & 1) == 0 || (i & -6) != 0) {
            throw new IllegalArgumentException("Illegal mode: 0x" + Integer.toHexString(i));
        }
        String path = file.getPath();
        this.fileToRemoveOnClose = (i & 4) != 0 ? file : null;
        if (charset != null) {
            this.f806zc = ZipCoder.get(charset);
            long open = open(path, i, file.lastModified(), usemmap);
            this.jzfile = open;
            this.name = path;
            this.total = getTotal(open);
            this.locsig = startsWithLOC(this.jzfile);
            closeGuard.open("close");
            return;
        }
        throw new NullPointerException("charset is null");
    }

    public ZipFile(String str, Charset charset) throws IOException {
        this(new File(str), 1, charset);
    }

    public ZipFile(File file, Charset charset) throws IOException {
        this(file, 1, charset);
    }

    public String getComment() {
        synchronized (this) {
            ensureOpen();
            byte[] commentBytes = getCommentBytes(this.jzfile);
            if (commentBytes == null) {
                return null;
            }
            String zipCoder = this.f806zc.toString(commentBytes, commentBytes.length);
            return zipCoder;
        }
    }

    public ZipEntry getEntry(String str) {
        if (str != null) {
            synchronized (this) {
                ensureOpen();
                long entry = getEntry(this.jzfile, this.f806zc.getBytes(str), true);
                if (entry == 0) {
                    return null;
                }
                ZipEntry zipEntry = getZipEntry(str, entry);
                freeEntry(this.jzfile, entry);
                return zipEntry;
            }
        }
        throw new NullPointerException(ZoneGetter.KEY_DISPLAYNAME);
    }

    public InputStream getInputStream(ZipEntry zipEntry) throws IOException {
        long j;
        if (zipEntry != null) {
            synchronized (this) {
                ensureOpen();
                if (this.f806zc.isUTF8() || (zipEntry.flag & 2048) == 0) {
                    j = getEntry(this.jzfile, this.f806zc.getBytes(zipEntry.name), true);
                } else {
                    j = getEntry(this.jzfile, this.f806zc.getBytesUTF8(zipEntry.name), true);
                }
                if (j == 0) {
                    return null;
                }
                ZipFileInputStream zipFileInputStream = new ZipFileInputStream(j);
                int entryMethod = getEntryMethod(j);
                if (entryMethod == 0) {
                    synchronized (this.streams) {
                        this.streams.put(zipFileInputStream, null);
                    }
                    return zipFileInputStream;
                } else if (entryMethod == 8) {
                    long entrySize = getEntrySize(j) + 2;
                    if (entrySize > 65536) {
                        entrySize = 65536;
                    }
                    if (entrySize <= 0) {
                        entrySize = 4096;
                    }
                    Inflater inflater = getInflater();
                    ZipFileInflaterInputStream zipFileInflaterInputStream = new ZipFileInflaterInputStream(zipFileInputStream, inflater, (int) entrySize);
                    synchronized (this.streams) {
                        this.streams.put(zipFileInflaterInputStream, inflater);
                    }
                    return zipFileInflaterInputStream;
                } else {
                    throw new ZipException("invalid compression method");
                }
            }
        } else {
            throw new NullPointerException("entry");
        }
    }

    private class ZipFileInflaterInputStream extends InflaterInputStream {
        private volatile boolean closeRequested = false;
        private boolean eof = false;
        private final ZipFileInputStream zfin;

        ZipFileInflaterInputStream(ZipFileInputStream zipFileInputStream, Inflater inflater, int i) {
            super(zipFileInputStream, inflater, i);
            this.zfin = zipFileInputStream;
        }

        public void close() throws IOException {
            Inflater inflater;
            if (!this.closeRequested) {
                this.closeRequested = true;
                super.close();
                synchronized (ZipFile.this.streams) {
                    inflater = (Inflater) ZipFile.this.streams.remove(this);
                }
                if (inflater != null) {
                    ZipFile.this.releaseInflater(inflater);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void fill() throws IOException {
            if (!this.eof) {
                this.len = this.f519in.read(this.buf, 0, this.buf.length);
                if (this.len == -1) {
                    this.buf[0] = 0;
                    this.len = 1;
                    this.eof = true;
                }
                this.inf.setInput(this.buf, 0, this.len);
                return;
            }
            throw new EOFException("Unexpected end of ZLIB input stream");
        }

        public int available() throws IOException {
            if (this.closeRequested) {
                return 0;
            }
            long size = this.zfin.size() - this.inf.getBytesWritten();
            if (size > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) size;
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            close();
        }
    }

    private Inflater getInflater() {
        Inflater poll;
        synchronized (this.inflaterCache) {
            do {
                poll = this.inflaterCache.poll();
                if (poll == null) {
                    return new Inflater(true);
                }
            } while (poll.ended());
            return poll;
        }
    }

    /* access modifiers changed from: private */
    public void releaseInflater(Inflater inflater) {
        if (!inflater.ended()) {
            inflater.reset();
            synchronized (this.inflaterCache) {
                this.inflaterCache.add(inflater);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    private class ZipEntryIterator implements Enumeration<ZipEntry>, Iterator<ZipEntry> {

        /* renamed from: i */
        private int f807i = 0;

        public ZipEntryIterator() {
            ZipFile.this.ensureOpen();
        }

        public boolean hasMoreElements() {
            return hasNext();
        }

        public boolean hasNext() {
            boolean z;
            synchronized (ZipFile.this) {
                ZipFile.this.ensureOpen();
                z = this.f807i < ZipFile.this.total;
            }
            return z;
        }

        public ZipEntry nextElement() {
            return next();
        }

        public ZipEntry next() {
            ZipEntry r0;
            String str;
            synchronized (ZipFile.this) {
                ZipFile.this.ensureOpen();
                if (this.f807i < ZipFile.this.total) {
                    long r2 = ZipFile.this.jzfile;
                    int i = this.f807i;
                    this.f807i = i + 1;
                    long r22 = ZipFile.getNextEntry(r2, i);
                    if (r22 == 0) {
                        if (ZipFile.this.closeRequested) {
                            str = "ZipFile concurrently closed";
                        } else {
                            str = ZipFile.getZipMessage(ZipFile.this.jzfile);
                        }
                        throw new ZipError("jzentry == 0,\n jzfile = " + ZipFile.this.jzfile + ",\n total = " + ZipFile.this.total + ",\n name = " + ZipFile.this.name + ",\n i = " + this.f807i + ",\n message = " + str);
                    }
                    r0 = ZipFile.this.getZipEntry((String) null, r22);
                    ZipFile.freeEntry(ZipFile.this.jzfile, r22);
                } else {
                    throw new NoSuchElementException();
                }
            }
            return r0;
        }
    }

    public Enumeration<? extends ZipEntry> entries() {
        return new ZipEntryIterator();
    }

    public Stream<? extends ZipEntry> stream() {
        return StreamSupport.stream(Spliterators.spliterator(new ZipEntryIterator(), (long) size(), 1297), false);
    }

    /* access modifiers changed from: private */
    public ZipEntry getZipEntry(String str, long j) {
        ZipEntry zipEntry = new ZipEntry();
        zipEntry.flag = getEntryFlag(j);
        if (str != null) {
            zipEntry.name = str;
        } else {
            byte[] entryBytes = getEntryBytes(j, 0);
            if (this.f806zc.isUTF8() || (zipEntry.flag & 2048) == 0) {
                zipEntry.name = this.f806zc.toString(entryBytes, entryBytes.length);
            } else {
                zipEntry.name = this.f806zc.toStringUTF8(entryBytes, entryBytes.length);
            }
        }
        zipEntry.xdostime = getEntryTime(j);
        zipEntry.crc = getEntryCrc(j);
        zipEntry.size = getEntrySize(j);
        zipEntry.csize = getEntryCSize(j);
        zipEntry.method = getEntryMethod(j);
        zipEntry.setExtra0(getEntryBytes(j, 1), false);
        byte[] entryBytes2 = getEntryBytes(j, 2);
        if (entryBytes2 == null) {
            zipEntry.comment = null;
        } else if (this.f806zc.isUTF8() || (zipEntry.flag & 2048) == 0) {
            zipEntry.comment = this.f806zc.toString(entryBytes2, entryBytes2.length);
        } else {
            zipEntry.comment = this.f806zc.toStringUTF8(entryBytes2, entryBytes2.length);
        }
        return zipEntry;
    }

    public int size() {
        ensureOpen();
        return this.total;
    }

    public void close() throws IOException {
        if (!this.closeRequested) {
            CloseGuard closeGuard = this.guard;
            if (closeGuard != null) {
                closeGuard.close();
            }
            this.closeRequested = true;
            synchronized (this) {
                Map<InputStream, Inflater> map = this.streams;
                if (map != null) {
                    synchronized (map) {
                        if (!this.streams.isEmpty()) {
                            HashMap hashMap = new HashMap(this.streams);
                            this.streams.clear();
                            for (Map.Entry entry : hashMap.entrySet()) {
                                ((InputStream) entry.getKey()).close();
                                Inflater inflater = (Inflater) entry.getValue();
                                if (inflater != null) {
                                    inflater.end();
                                }
                            }
                        }
                    }
                }
                Deque<Inflater> deque = this.inflaterCache;
                if (deque != null) {
                    synchronized (deque) {
                        while (true) {
                            Inflater poll = this.inflaterCache.poll();
                            if (poll != null) {
                                poll.end();
                            }
                        }
                    }
                    break;
                }
                long j = this.jzfile;
                if (j != 0) {
                    this.jzfile = 0;
                    close(j);
                }
                File file = this.fileToRemoveOnClose;
                if (file != null) {
                    file.delete();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws IOException {
        CloseGuard closeGuard = this.guard;
        if (closeGuard != null) {
            closeGuard.warnIfOpen();
        }
        close();
    }

    /* access modifiers changed from: private */
    public void ensureOpen() {
        if (this.closeRequested) {
            throw new IllegalStateException("zip file closed");
        } else if (this.jzfile == 0) {
            throw new IllegalStateException("The object is not initialized.");
        }
    }

    /* access modifiers changed from: private */
    public void ensureOpenOrZipException() throws IOException {
        if (this.closeRequested) {
            throw new ZipException("ZipFile closed");
        }
    }

    private class ZipFileInputStream extends InputStream {
        protected long jzentry;
        private long pos = 0;
        protected long rem;
        protected long size;
        private volatile boolean zfisCloseRequested = false;

        ZipFileInputStream(long j) {
            this.rem = ZipFile.getEntryCSize(j);
            this.size = ZipFile.getEntrySize(j);
            this.jzentry = j;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0044, code lost:
            if (r0.rem != 0) goto L_0x0049;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0046, code lost:
            close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0049, code lost:
            return r1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int read(byte[] r19, int r20, int r21) throws java.p026io.IOException {
            /*
                r18 = this;
                r0 = r18
                r1 = r21
                java.util.zip.ZipFile r2 = java.util.zip.ZipFile.this
                r2.ensureOpenOrZipException()
                java.util.zip.ZipFile r2 = java.util.zip.ZipFile.this
                monitor-enter(r2)
                long r3 = r0.rem     // Catch:{ all -> 0x004a }
                long r14 = r0.pos     // Catch:{ all -> 0x004a }
                r16 = 0
                int r5 = (r3 > r16 ? 1 : (r3 == r16 ? 0 : -1))
                if (r5 != 0) goto L_0x0019
                monitor-exit(r2)     // Catch:{ all -> 0x004a }
                r0 = -1
                return r0
            L_0x0019:
                if (r1 > 0) goto L_0x001e
                monitor-exit(r2)     // Catch:{ all -> 0x004a }
                r0 = 0
                return r0
            L_0x001e:
                long r5 = (long) r1     // Catch:{ all -> 0x004a }
                int r5 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
                if (r5 <= 0) goto L_0x0024
                int r1 = (int) r3     // Catch:{ all -> 0x004a }
            L_0x0024:
                r13 = r1
                java.util.zip.ZipFile r1 = java.util.zip.ZipFile.this     // Catch:{ all -> 0x004a }
                long r5 = r1.jzfile     // Catch:{ all -> 0x004a }
                long r7 = r0.jzentry     // Catch:{ all -> 0x004a }
                r9 = r14
                r11 = r19
                r12 = r20
                int r1 = java.util.zip.ZipFile.read(r5, r7, r9, r11, r12, r13)     // Catch:{ all -> 0x004a }
                if (r1 <= 0) goto L_0x003f
                long r5 = (long) r1     // Catch:{ all -> 0x004a }
                long r14 = r14 + r5
                r0.pos = r14     // Catch:{ all -> 0x004a }
                long r3 = r3 - r5
                r0.rem = r3     // Catch:{ all -> 0x004a }
            L_0x003f:
                monitor-exit(r2)     // Catch:{ all -> 0x004a }
                long r2 = r0.rem
                int r2 = (r2 > r16 ? 1 : (r2 == r16 ? 0 : -1))
                if (r2 != 0) goto L_0x0049
                r18.close()
            L_0x0049:
                return r1
            L_0x004a:
                r0 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x004a }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.zip.ZipFile.ZipFileInputStream.read(byte[], int, int):int");
        }

        public int read() throws IOException {
            byte[] bArr = new byte[1];
            if (read(bArr, 0, 1) == 1) {
                return bArr[0] & 255;
            }
            return -1;
        }

        public long skip(long j) {
            long j2 = this.rem;
            if (j > j2) {
                j = j2;
            }
            this.pos += j;
            long j3 = j2 - j;
            this.rem = j3;
            if (j3 == 0) {
                close();
            }
            return j;
        }

        public int available() {
            long j = this.rem;
            if (j > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) j;
        }

        public long size() {
            return this.size;
        }

        public void close() {
            if (!this.zfisCloseRequested) {
                this.zfisCloseRequested = true;
                this.rem = 0;
                synchronized (ZipFile.this) {
                    if (!(this.jzentry == 0 || ZipFile.this.jzfile == 0)) {
                        ZipFile.freeEntry(ZipFile.this.jzfile, this.jzentry);
                        this.jzentry = 0;
                    }
                }
                synchronized (ZipFile.this.streams) {
                    ZipFile.this.streams.remove(this);
                }
            }
        }

        /* access modifiers changed from: protected */
        public void finalize() {
            close();
        }
    }

    public boolean startsWithLocHeader() {
        return this.locsig;
    }

    public int getFileDescriptor() {
        return getFileDescriptor(this.jzfile);
    }
}
