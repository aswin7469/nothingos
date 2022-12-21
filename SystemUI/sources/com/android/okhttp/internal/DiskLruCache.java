package com.android.okhttp.internal;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.okhttp.internal.p006io.FileSystem;
import com.android.okhttp.okio.Buffer;
import com.android.okhttp.okio.BufferedSink;
import com.android.okhttp.okio.Okio;
import com.android.okhttp.okio.Sink;
import com.android.okhttp.okio.Source;
import com.android.okhttp.okio.Timeout;
import java.p026io.Closeable;
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class DiskLruCache implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    static final String MAGIC = "libcore.io.DiskLruCache";
    /* access modifiers changed from: private */
    public static final Sink NULL_SINK = new Sink() {
        public void close() throws IOException {
        }

        public void flush() throws IOException {
        }

        public void write(Buffer buffer, long j) throws IOException {
            buffer.skip(j);
        }

        public Timeout timeout() {
            return Timeout.NONE;
        }
    };
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    private final Runnable cleanupRunnable = new Runnable() {
        public void run() {
            synchronized (DiskLruCache.this) {
                if (!(!DiskLruCache.this.initialized) && !DiskLruCache.this.closed) {
                    try {
                        DiskLruCache.this.trimToSize();
                        if (DiskLruCache.this.journalRebuildRequired()) {
                            DiskLruCache.this.rebuildJournal();
                            DiskLruCache.this.redundantOpCount = 0;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException((Throwable) e);
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean closed;
    /* access modifiers changed from: private */
    public final File directory;
    private final Executor executor;
    /* access modifiers changed from: private */
    public final FileSystem fileSystem;
    /* access modifiers changed from: private */
    public boolean hasJournalErrors;
    /* access modifiers changed from: private */
    public boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    private BufferedSink journalWriter;
    /* access modifiers changed from: private */
    public final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<>(0, 0.75f, true);
    private long maxSize;
    private long nextSequenceNumber = 0;
    /* access modifiers changed from: private */
    public int redundantOpCount;
    private long size = 0;
    /* access modifiers changed from: private */
    public final int valueCount;

    DiskLruCache(FileSystem fileSystem2, File file, int i, int i2, long j, Executor executor2) {
        this.fileSystem = fileSystem2;
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = i2;
        this.maxSize = j;
        this.executor = executor2;
    }

    public synchronized void initialize() throws IOException {
        if (!this.initialized) {
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException e) {
                    Platform platform = Platform.get();
                    platform.logW("DiskLruCache " + this.directory + " is corrupt: " + e.getMessage() + ", removing");
                    delete();
                    this.closed = false;
                }
            }
            rebuildJournal();
            this.initialized = true;
        }
    }

    public static DiskLruCache create(FileSystem fileSystem2, File file, int i, int i2, long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        } else if (i2 > 0) {
            return new DiskLruCache(fileSystem2, file, i, i2, j, new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, (BlockingQueue<Runnable>) new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
        } else {
            throw new IllegalArgumentException("valueCount <= 0");
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:16|17|(1:19)(1:20)|21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r9.redundantOpCount = r0 - r9.lruEntries.size();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006d, code lost:
        if (r2.exhausted() == false) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006f, code lost:
        rebuildJournal();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0073, code lost:
        r9.journalWriter = newJournalWriter();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x007c, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0060 */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:16:0x0060=Splitter:B:16:0x0060, B:23:0x007d=Splitter:B:23:0x007d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readJournal() throws java.p026io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = ", "
            java.lang.String r1 = "unexpected journal header: ["
            com.android.okhttp.internal.io.FileSystem r2 = r9.fileSystem
            java.io.File r3 = r9.journalFile
            com.android.okhttp.okio.Source r2 = r2.source(r3)
            com.android.okhttp.okio.BufferedSource r2 = com.android.okhttp.okio.Okio.buffer((com.android.okhttp.okio.Source) r2)
            java.lang.String r3 = r2.readUtf8LineStrict()     // Catch:{ all -> 0x00a6 }
            java.lang.String r4 = r2.readUtf8LineStrict()     // Catch:{ all -> 0x00a6 }
            java.lang.String r5 = r2.readUtf8LineStrict()     // Catch:{ all -> 0x00a6 }
            java.lang.String r6 = r2.readUtf8LineStrict()     // Catch:{ all -> 0x00a6 }
            java.lang.String r7 = r2.readUtf8LineStrict()     // Catch:{ all -> 0x00a6 }
            java.lang.String r8 = "libcore.io.DiskLruCache"
            boolean r8 = r8.equals(r3)     // Catch:{ all -> 0x00a6 }
            if (r8 == 0) goto L_0x007d
            java.lang.String r8 = "1"
            boolean r8 = r8.equals(r4)     // Catch:{ all -> 0x00a6 }
            if (r8 == 0) goto L_0x007d
            int r8 = r9.appVersion     // Catch:{ all -> 0x00a6 }
            java.lang.String r8 = java.lang.Integer.toString(r8)     // Catch:{ all -> 0x00a6 }
            boolean r5 = r8.equals(r5)     // Catch:{ all -> 0x00a6 }
            if (r5 == 0) goto L_0x007d
            int r5 = r9.valueCount     // Catch:{ all -> 0x00a6 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ all -> 0x00a6 }
            boolean r5 = r5.equals(r6)     // Catch:{ all -> 0x00a6 }
            if (r5 == 0) goto L_0x007d
            java.lang.String r5 = ""
            boolean r5 = r5.equals(r7)     // Catch:{ all -> 0x00a6 }
            if (r5 == 0) goto L_0x007d
            r0 = 0
        L_0x0056:
            java.lang.String r1 = r2.readUtf8LineStrict()     // Catch:{ EOFException -> 0x0060 }
            r9.readJournalLine(r1)     // Catch:{ EOFException -> 0x0060 }
            int r0 = r0 + 1
            goto L_0x0056
        L_0x0060:
            java.util.LinkedHashMap<java.lang.String, com.android.okhttp.internal.DiskLruCache$Entry> r1 = r9.lruEntries     // Catch:{ all -> 0x00a6 }
            int r1 = r1.size()     // Catch:{ all -> 0x00a6 }
            int r0 = r0 - r1
            r9.redundantOpCount = r0     // Catch:{ all -> 0x00a6 }
            boolean r0 = r2.exhausted()     // Catch:{ all -> 0x00a6 }
            if (r0 != 0) goto L_0x0073
            r9.rebuildJournal()     // Catch:{ all -> 0x00a6 }
            goto L_0x0079
        L_0x0073:
            com.android.okhttp.okio.BufferedSink r0 = r9.newJournalWriter()     // Catch:{ all -> 0x00a6 }
            r9.journalWriter = r0     // Catch:{ all -> 0x00a6 }
        L_0x0079:
            com.android.okhttp.internal.Util.closeQuietly((java.p026io.Closeable) r2)
            return
        L_0x007d:
            java.io.IOException r9 = new java.io.IOException     // Catch:{ all -> 0x00a6 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a6 }
            r5.<init>((java.lang.String) r1)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r3)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r0)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r4)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r0)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r6)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r0)     // Catch:{ all -> 0x00a6 }
            r5.append((java.lang.String) r7)     // Catch:{ all -> 0x00a6 }
            java.lang.String r0 = "]"
            r5.append((java.lang.String) r0)     // Catch:{ all -> 0x00a6 }
            java.lang.String r0 = r5.toString()     // Catch:{ all -> 0x00a6 }
            r9.<init>((java.lang.String) r0)     // Catch:{ all -> 0x00a6 }
            throw r9     // Catch:{ all -> 0x00a6 }
        L_0x00a6:
            r9 = move-exception
            com.android.okhttp.internal.Util.closeQuietly((java.p026io.Closeable) r2)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.DiskLruCache.readJournal():void");
    }

    private BufferedSink newJournalWriter() throws FileNotFoundException {
        return Okio.buffer((Sink) new FaultHidingSink(this.fileSystem.appendingSink(this.journalFile)) {
            static final /* synthetic */ boolean $assertionsDisabled = false;

            static {
                Class<DiskLruCache> cls = DiskLruCache.class;
            }

            /* access modifiers changed from: protected */
            public void onException(IOException iOException) {
                DiskLruCache.this.hasJournalErrors = true;
            }
        });
    }

    private void readJournalLine(String str) throws IOException {
        String str2;
        int indexOf = str.indexOf(32);
        if (indexOf != -1) {
            int i = indexOf + 1;
            int indexOf2 = str.indexOf(32, i);
            if (indexOf2 == -1) {
                str2 = str.substring(i);
                if (indexOf == 6 && str.startsWith(REMOVE)) {
                    this.lruEntries.remove(str2);
                    return;
                }
            } else {
                str2 = str.substring(i, indexOf2);
            }
            Entry entry = this.lruEntries.get(str2);
            if (entry == null) {
                entry = new Entry(str2);
                this.lruEntries.put(str2, entry);
            }
            if (indexOf2 != -1 && indexOf == 5 && str.startsWith(CLEAN)) {
                String[] split = str.substring(indexOf2 + 1).split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                entry.readable = true;
                entry.currentEditor = null;
                entry.setLengths(split);
            } else if (indexOf2 == -1 && indexOf == 5 && str.startsWith(DIRTY)) {
                entry.currentEditor = new Editor(entry);
            } else if (indexOf2 != -1 || indexOf != 4 || !str.startsWith(READ)) {
                throw new IOException("unexpected journal line: " + str);
            }
        } else {
            throw new IOException("unexpected journal line: " + str);
        }
    }

    private void processJournal() throws IOException {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator<Entry> it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            Entry next = it.next();
            int i = 0;
            if (next.currentEditor == null) {
                while (i < this.valueCount) {
                    this.size += next.lengths[i];
                    i++;
                }
            } else {
                next.currentEditor = null;
                while (i < this.valueCount) {
                    this.fileSystem.delete(next.cleanFiles[i]);
                    this.fileSystem.delete(next.dirtyFiles[i]);
                    i++;
                }
                it.remove();
            }
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    public synchronized void rebuildJournal() throws IOException {
        BufferedSink bufferedSink = this.journalWriter;
        if (bufferedSink != null) {
            bufferedSink.close();
        }
        BufferedSink buffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
        try {
            buffer.writeUtf8(MAGIC).writeByte(10);
            buffer.writeUtf8("1").writeByte(10);
            buffer.writeDecimalLong((long) this.appVersion).writeByte(10);
            buffer.writeDecimalLong((long) this.valueCount).writeByte(10);
            buffer.writeByte(10);
            for (Entry next : this.lruEntries.values()) {
                if (next.currentEditor != null) {
                    buffer.writeUtf8(DIRTY).writeByte(32);
                    buffer.writeUtf8(next.key);
                    buffer.writeByte(10);
                } else {
                    buffer.writeUtf8(CLEAN).writeByte(32);
                    buffer.writeUtf8(next.key);
                    next.writeLengths(buffer);
                    buffer.writeByte(10);
                }
            }
            buffer.close();
            if (this.fileSystem.exists(this.journalFile)) {
                this.fileSystem.rename(this.journalFile, this.journalFileBackup);
            }
            this.fileSystem.rename(this.journalFileTmp, this.journalFile);
            this.fileSystem.delete(this.journalFileBackup);
            this.journalWriter = newJournalWriter();
            this.hasJournalErrors = false;
        } catch (Throwable th) {
            buffer.close();
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004f, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0051, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.android.okhttp.internal.DiskLruCache.Snapshot get(java.lang.String r4) throws java.p026io.IOException {
        /*
            r3 = this;
            monitor-enter(r3)
            r3.initialize()     // Catch:{ all -> 0x0052 }
            r3.checkNotClosed()     // Catch:{ all -> 0x0052 }
            r3.validateKey(r4)     // Catch:{ all -> 0x0052 }
            java.util.LinkedHashMap<java.lang.String, com.android.okhttp.internal.DiskLruCache$Entry> r0 = r3.lruEntries     // Catch:{ all -> 0x0052 }
            java.lang.Object r0 = r0.get(r4)     // Catch:{ all -> 0x0052 }
            com.android.okhttp.internal.DiskLruCache$Entry r0 = (com.android.okhttp.internal.DiskLruCache.Entry) r0     // Catch:{ all -> 0x0052 }
            r1 = 0
            if (r0 == 0) goto L_0x0050
            boolean r2 = r0.readable     // Catch:{ all -> 0x0052 }
            if (r2 != 0) goto L_0x001c
            goto L_0x0050
        L_0x001c:
            com.android.okhttp.internal.DiskLruCache$Snapshot r0 = r0.snapshot()     // Catch:{ all -> 0x0052 }
            if (r0 != 0) goto L_0x0024
            monitor-exit(r3)
            return r1
        L_0x0024:
            int r1 = r3.redundantOpCount     // Catch:{ all -> 0x0052 }
            int r1 = r1 + 1
            r3.redundantOpCount = r1     // Catch:{ all -> 0x0052 }
            com.android.okhttp.okio.BufferedSink r1 = r3.journalWriter     // Catch:{ all -> 0x0052 }
            java.lang.String r2 = "READ"
            com.android.okhttp.okio.BufferedSink r1 = r1.writeUtf8(r2)     // Catch:{ all -> 0x0052 }
            r2 = 32
            com.android.okhttp.okio.BufferedSink r1 = r1.writeByte(r2)     // Catch:{ all -> 0x0052 }
            com.android.okhttp.okio.BufferedSink r4 = r1.writeUtf8(r4)     // Catch:{ all -> 0x0052 }
            r1 = 10
            r4.writeByte(r1)     // Catch:{ all -> 0x0052 }
            boolean r4 = r3.journalRebuildRequired()     // Catch:{ all -> 0x0052 }
            if (r4 == 0) goto L_0x004e
            java.util.concurrent.Executor r4 = r3.executor     // Catch:{ all -> 0x0052 }
            java.lang.Runnable r1 = r3.cleanupRunnable     // Catch:{ all -> 0x0052 }
            r4.execute(r1)     // Catch:{ all -> 0x0052 }
        L_0x004e:
            monitor-exit(r3)
            return r0
        L_0x0050:
            monitor-exit(r3)
            return r1
        L_0x0052:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.DiskLruCache.get(java.lang.String):com.android.okhttp.internal.DiskLruCache$Snapshot");
    }

    public Editor edit(String str) throws IOException {
        return edit(str, -1);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.android.okhttp.internal.DiskLruCache.Editor edit(java.lang.String r6, long r7) throws java.p026io.IOException {
        /*
            r5 = this;
            monitor-enter(r5)
            r5.initialize()     // Catch:{ all -> 0x0067 }
            r5.checkNotClosed()     // Catch:{ all -> 0x0067 }
            r5.validateKey(r6)     // Catch:{ all -> 0x0067 }
            java.util.LinkedHashMap<java.lang.String, com.android.okhttp.internal.DiskLruCache$Entry> r0 = r5.lruEntries     // Catch:{ all -> 0x0067 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ all -> 0x0067 }
            com.android.okhttp.internal.DiskLruCache$Entry r0 = (com.android.okhttp.internal.DiskLruCache.Entry) r0     // Catch:{ all -> 0x0067 }
            r1 = -1
            int r1 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            r2 = 0
            if (r1 == 0) goto L_0x0025
            if (r0 == 0) goto L_0x0023
            long r3 = r0.sequenceNumber     // Catch:{ all -> 0x0067 }
            int r7 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r7 == 0) goto L_0x0025
        L_0x0023:
            monitor-exit(r5)
            return r2
        L_0x0025:
            if (r0 == 0) goto L_0x002f
            com.android.okhttp.internal.DiskLruCache$Editor r7 = r0.currentEditor     // Catch:{ all -> 0x0067 }
            if (r7 == 0) goto L_0x002f
            monitor-exit(r5)
            return r2
        L_0x002f:
            com.android.okhttp.okio.BufferedSink r7 = r5.journalWriter     // Catch:{ all -> 0x0067 }
            java.lang.String r8 = "DIRTY"
            com.android.okhttp.okio.BufferedSink r7 = r7.writeUtf8(r8)     // Catch:{ all -> 0x0067 }
            r8 = 32
            com.android.okhttp.okio.BufferedSink r7 = r7.writeByte(r8)     // Catch:{ all -> 0x0067 }
            com.android.okhttp.okio.BufferedSink r7 = r7.writeUtf8(r6)     // Catch:{ all -> 0x0067 }
            r8 = 10
            r7.writeByte(r8)     // Catch:{ all -> 0x0067 }
            com.android.okhttp.okio.BufferedSink r7 = r5.journalWriter     // Catch:{ all -> 0x0067 }
            r7.flush()     // Catch:{ all -> 0x0067 }
            boolean r7 = r5.hasJournalErrors     // Catch:{ all -> 0x0067 }
            if (r7 == 0) goto L_0x0051
            monitor-exit(r5)
            return r2
        L_0x0051:
            if (r0 != 0) goto L_0x005d
            com.android.okhttp.internal.DiskLruCache$Entry r0 = new com.android.okhttp.internal.DiskLruCache$Entry     // Catch:{ all -> 0x0067 }
            r0.<init>(r6)     // Catch:{ all -> 0x0067 }
            java.util.LinkedHashMap<java.lang.String, com.android.okhttp.internal.DiskLruCache$Entry> r7 = r5.lruEntries     // Catch:{ all -> 0x0067 }
            r7.put(r6, r0)     // Catch:{ all -> 0x0067 }
        L_0x005d:
            com.android.okhttp.internal.DiskLruCache$Editor r6 = new com.android.okhttp.internal.DiskLruCache$Editor     // Catch:{ all -> 0x0067 }
            r6.<init>(r0)     // Catch:{ all -> 0x0067 }
            r0.currentEditor = r6     // Catch:{ all -> 0x0067 }
            monitor-exit(r5)
            return r6
        L_0x0067:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.DiskLruCache.edit(java.lang.String, long):com.android.okhttp.internal.DiskLruCache$Editor");
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void setMaxSize(long j) {
        this.maxSize = j;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public synchronized long size() throws IOException {
        initialize();
        return this.size;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0111, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void completeEdit(com.android.okhttp.internal.DiskLruCache.Editor r10, boolean r11) throws java.p026io.IOException {
        /*
            r9 = this;
            monitor-enter(r9)
            com.android.okhttp.internal.DiskLruCache$Entry r0 = r10.entry     // Catch:{ all -> 0x0118 }
            com.android.okhttp.internal.DiskLruCache$Editor r1 = r0.currentEditor     // Catch:{ all -> 0x0118 }
            if (r1 != r10) goto L_0x0112
            r1 = 0
            if (r11 == 0) goto L_0x0051
            boolean r2 = r0.readable     // Catch:{ all -> 0x0118 }
            if (r2 != 0) goto L_0x0051
            r2 = r1
        L_0x0015:
            int r3 = r9.valueCount     // Catch:{ all -> 0x0118 }
            if (r2 >= r3) goto L_0x0051
            boolean[] r3 = r10.written     // Catch:{ all -> 0x0118 }
            boolean r3 = r3[r2]     // Catch:{ all -> 0x0118 }
            if (r3 == 0) goto L_0x0037
            com.android.okhttp.internal.io.FileSystem r3 = r9.fileSystem     // Catch:{ all -> 0x0118 }
            java.io.File[] r4 = r0.dirtyFiles     // Catch:{ all -> 0x0118 }
            r4 = r4[r2]     // Catch:{ all -> 0x0118 }
            boolean r3 = r3.exists(r4)     // Catch:{ all -> 0x0118 }
            if (r3 != 0) goto L_0x0034
            r10.abort()     // Catch:{ all -> 0x0118 }
            monitor-exit(r9)
            return
        L_0x0034:
            int r2 = r2 + 1
            goto L_0x0015
        L_0x0037:
            r10.abort()     // Catch:{ all -> 0x0118 }
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0118 }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0118 }
            r11.<init>()     // Catch:{ all -> 0x0118 }
            java.lang.String r0 = "Newly created entry didn't create value for index "
            r11.append((java.lang.String) r0)     // Catch:{ all -> 0x0118 }
            r11.append((int) r2)     // Catch:{ all -> 0x0118 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0118 }
            r10.<init>((java.lang.String) r11)     // Catch:{ all -> 0x0118 }
            throw r10     // Catch:{ all -> 0x0118 }
        L_0x0051:
            int r10 = r9.valueCount     // Catch:{ all -> 0x0118 }
            if (r1 >= r10) goto L_0x0091
            java.io.File[] r10 = r0.dirtyFiles     // Catch:{ all -> 0x0118 }
            r10 = r10[r1]     // Catch:{ all -> 0x0118 }
            if (r11 == 0) goto L_0x0089
            com.android.okhttp.internal.io.FileSystem r2 = r9.fileSystem     // Catch:{ all -> 0x0118 }
            boolean r2 = r2.exists(r10)     // Catch:{ all -> 0x0118 }
            if (r2 == 0) goto L_0x008e
            java.io.File[] r2 = r0.cleanFiles     // Catch:{ all -> 0x0118 }
            r2 = r2[r1]     // Catch:{ all -> 0x0118 }
            com.android.okhttp.internal.io.FileSystem r3 = r9.fileSystem     // Catch:{ all -> 0x0118 }
            r3.rename(r10, r2)     // Catch:{ all -> 0x0118 }
            long[] r10 = r0.lengths     // Catch:{ all -> 0x0118 }
            r3 = r10[r1]     // Catch:{ all -> 0x0118 }
            com.android.okhttp.internal.io.FileSystem r10 = r9.fileSystem     // Catch:{ all -> 0x0118 }
            long r5 = r10.size(r2)     // Catch:{ all -> 0x0118 }
            long[] r10 = r0.lengths     // Catch:{ all -> 0x0118 }
            r10[r1] = r5     // Catch:{ all -> 0x0118 }
            long r7 = r9.size     // Catch:{ all -> 0x0118 }
            long r7 = r7 - r3
            long r7 = r7 + r5
            r9.size = r7     // Catch:{ all -> 0x0118 }
            goto L_0x008e
        L_0x0089:
            com.android.okhttp.internal.io.FileSystem r2 = r9.fileSystem     // Catch:{ all -> 0x0118 }
            r2.delete(r10)     // Catch:{ all -> 0x0118 }
        L_0x008e:
            int r1 = r1 + 1
            goto L_0x0051
        L_0x0091:
            int r10 = r9.redundantOpCount     // Catch:{ all -> 0x0118 }
            r1 = 1
            int r10 = r10 + r1
            r9.redundantOpCount = r10     // Catch:{ all -> 0x0118 }
            r10 = 0
            r0.currentEditor = r10     // Catch:{ all -> 0x0118 }
            boolean r10 = r0.readable     // Catch:{ all -> 0x0118 }
            r10 = r10 | r11
            r2 = 10
            r3 = 32
            if (r10 == 0) goto L_0x00d4
            r0.readable = r1     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            java.lang.String r1 = "CLEAN"
            com.android.okhttp.okio.BufferedSink r10 = r10.writeUtf8(r1)     // Catch:{ all -> 0x0118 }
            r10.writeByte(r3)     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            java.lang.String r1 = r0.key     // Catch:{ all -> 0x0118 }
            r10.writeUtf8(r1)     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            r0.writeLengths(r10)     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            r10.writeByte(r2)     // Catch:{ all -> 0x0118 }
            if (r11 == 0) goto L_0x00f6
            long r10 = r9.nextSequenceNumber     // Catch:{ all -> 0x0118 }
            r1 = 1
            long r1 = r1 + r10
            r9.nextSequenceNumber = r1     // Catch:{ all -> 0x0118 }
            r0.sequenceNumber = r10     // Catch:{ all -> 0x0118 }
            goto L_0x00f6
        L_0x00d4:
            java.util.LinkedHashMap<java.lang.String, com.android.okhttp.internal.DiskLruCache$Entry> r10 = r9.lruEntries     // Catch:{ all -> 0x0118 }
            java.lang.String r11 = r0.key     // Catch:{ all -> 0x0118 }
            r10.remove(r11)     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            java.lang.String r11 = "REMOVE"
            com.android.okhttp.okio.BufferedSink r10 = r10.writeUtf8(r11)     // Catch:{ all -> 0x0118 }
            r10.writeByte(r3)     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            java.lang.String r11 = r0.key     // Catch:{ all -> 0x0118 }
            r10.writeUtf8(r11)     // Catch:{ all -> 0x0118 }
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            r10.writeByte(r2)     // Catch:{ all -> 0x0118 }
        L_0x00f6:
            com.android.okhttp.okio.BufferedSink r10 = r9.journalWriter     // Catch:{ all -> 0x0118 }
            r10.flush()     // Catch:{ all -> 0x0118 }
            long r10 = r9.size     // Catch:{ all -> 0x0118 }
            long r0 = r9.maxSize     // Catch:{ all -> 0x0118 }
            int r10 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r10 > 0) goto L_0x0109
            boolean r10 = r9.journalRebuildRequired()     // Catch:{ all -> 0x0118 }
            if (r10 == 0) goto L_0x0110
        L_0x0109:
            java.util.concurrent.Executor r10 = r9.executor     // Catch:{ all -> 0x0118 }
            java.lang.Runnable r11 = r9.cleanupRunnable     // Catch:{ all -> 0x0118 }
            r10.execute(r11)     // Catch:{ all -> 0x0118 }
        L_0x0110:
            monitor-exit(r9)
            return
        L_0x0112:
            java.lang.IllegalStateException r10 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0118 }
            r10.<init>()     // Catch:{ all -> 0x0118 }
            throw r10     // Catch:{ all -> 0x0118 }
        L_0x0118:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.DiskLruCache.completeEdit(com.android.okhttp.internal.DiskLruCache$Editor, boolean):void");
    }

    /* access modifiers changed from: private */
    public boolean journalRebuildRequired() {
        int i = this.redundantOpCount;
        return i >= 2000 && i >= this.lruEntries.size();
    }

    public synchronized boolean remove(String str) throws IOException {
        initialize();
        checkNotClosed();
        validateKey(str);
        Entry entry = this.lruEntries.get(str);
        if (entry == null) {
            return false;
        }
        return removeEntry(entry);
    }

    private boolean removeEntry(Entry entry) throws IOException {
        if (entry.currentEditor != null) {
            entry.currentEditor.detach();
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(entry.cleanFiles[i]);
            this.size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(entry.key).writeByte(10);
        this.lruEntries.remove(entry.key);
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public synchronized void flush() throws IOException {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    public synchronized void close() throws IOException {
        if (this.initialized) {
            if (!this.closed) {
                for (Entry entry : (Entry[]) this.lruEntries.values().toArray((T[]) new Entry[this.lruEntries.size()])) {
                    if (entry.currentEditor != null) {
                        entry.currentEditor.abort();
                    }
                }
                trimToSize();
                this.journalWriter.close();
                this.journalWriter = null;
                this.closed = true;
                return;
            }
        }
        this.closed = true;
    }

    /* access modifiers changed from: private */
    public void trimToSize() throws IOException {
        while (this.size > this.maxSize) {
            removeEntry(this.lruEntries.values().iterator().next());
        }
    }

    public void delete() throws IOException {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    public synchronized void evictAll() throws IOException {
        initialize();
        for (Entry removeEntry : (Entry[]) this.lruEntries.values().toArray((T[]) new Entry[this.lruEntries.size()])) {
            removeEntry(removeEntry);
        }
    }

    private void validateKey(String str) {
        if (!LEGAL_KEY_PATTERN.matcher(str).matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
        }
    }

    public synchronized Iterator<Snapshot> snapshots() throws IOException {
        initialize();
        return new Iterator<Snapshot>() {
            final Iterator<Entry> delegate;
            Snapshot nextSnapshot;
            Snapshot removeSnapshot;

            {
                this.delegate = new ArrayList(DiskLruCache.this.lruEntries.values()).iterator();
            }

            public boolean hasNext() {
                if (this.nextSnapshot != null) {
                    return true;
                }
                synchronized (DiskLruCache.this) {
                    if (DiskLruCache.this.closed) {
                        return false;
                    }
                    while (this.delegate.hasNext()) {
                        Snapshot snapshot = this.delegate.next().snapshot();
                        if (snapshot != null) {
                            this.nextSnapshot = snapshot;
                            return true;
                        }
                    }
                    return false;
                }
            }

            public Snapshot next() {
                if (hasNext()) {
                    Snapshot snapshot = this.nextSnapshot;
                    this.removeSnapshot = snapshot;
                    this.nextSnapshot = null;
                    return snapshot;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                Snapshot snapshot = this.removeSnapshot;
                if (snapshot != null) {
                    try {
                        DiskLruCache.this.remove(snapshot.key);
                    } catch (IOException unused) {
                    } catch (Throwable th) {
                        this.removeSnapshot = null;
                        throw th;
                    }
                    this.removeSnapshot = null;
                    return;
                }
                throw new IllegalStateException("remove() before next()");
            }
        };
    }

    public final class Snapshot implements Closeable {
        /* access modifiers changed from: private */
        public final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        private Snapshot(String str, long j, Source[] sourceArr, long[] jArr) {
            this.key = str;
            this.sequenceNumber = j;
            this.sources = sourceArr;
            this.lengths = jArr;
        }

        public String key() {
            return this.key;
        }

        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public Source getSource(int i) {
            return this.sources[i];
        }

        public long getLength(int i) {
            return this.lengths[i];
        }

        public void close() {
            for (Source closeQuietly : this.sources) {
                Util.closeQuietly((Closeable) closeQuietly);
            }
        }
    }

    public final class Editor {
        private boolean done;
        /* access modifiers changed from: private */
        public final Entry entry;
        /* access modifiers changed from: private */
        public final boolean[] written;

        private Editor(Entry entry2) {
            this.entry = entry2;
            this.written = entry2.readable ? null : new boolean[DiskLruCache.this.valueCount];
        }

        /* access modifiers changed from: package-private */
        public void detach() {
            if (this.entry.currentEditor == this) {
                for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                    try {
                        DiskLruCache.this.fileSystem.delete(this.entry.dirtyFiles[i]);
                    } catch (IOException unused) {
                    }
                }
                this.entry.currentEditor = null;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0030, code lost:
            return null;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.okhttp.okio.Source newSource(int r4) throws java.p026io.IOException {
            /*
                r3 = this;
                com.android.okhttp.internal.DiskLruCache r0 = com.android.okhttp.internal.DiskLruCache.this
                monitor-enter(r0)
                boolean r1 = r3.done     // Catch:{ all -> 0x0037 }
                if (r1 != 0) goto L_0x0031
                com.android.okhttp.internal.DiskLruCache$Entry r1 = r3.entry     // Catch:{ all -> 0x0037 }
                boolean r1 = r1.readable     // Catch:{ all -> 0x0037 }
                r2 = 0
                if (r1 == 0) goto L_0x002f
                com.android.okhttp.internal.DiskLruCache$Entry r1 = r3.entry     // Catch:{ all -> 0x0037 }
                com.android.okhttp.internal.DiskLruCache$Editor r1 = r1.currentEditor     // Catch:{ all -> 0x0037 }
                if (r1 == r3) goto L_0x0019
                goto L_0x002f
            L_0x0019:
                com.android.okhttp.internal.DiskLruCache r1 = com.android.okhttp.internal.DiskLruCache.this     // Catch:{ FileNotFoundException -> 0x002d }
                com.android.okhttp.internal.io.FileSystem r1 = r1.fileSystem     // Catch:{ FileNotFoundException -> 0x002d }
                com.android.okhttp.internal.DiskLruCache$Entry r3 = r3.entry     // Catch:{ FileNotFoundException -> 0x002d }
                java.io.File[] r3 = r3.cleanFiles     // Catch:{ FileNotFoundException -> 0x002d }
                r3 = r3[r4]     // Catch:{ FileNotFoundException -> 0x002d }
                com.android.okhttp.okio.Source r3 = r1.source(r3)     // Catch:{ FileNotFoundException -> 0x002d }
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                return r3
            L_0x002d:
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                return r2
            L_0x002f:
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                return r2
            L_0x0031:
                java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch:{ all -> 0x0037 }
                r3.<init>()     // Catch:{ all -> 0x0037 }
                throw r3     // Catch:{ all -> 0x0037 }
            L_0x0037:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0037 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.DiskLruCache.Editor.newSource(int):com.android.okhttp.okio.Source");
        }

        public Sink newSink(int i) throws IOException {
            synchronized (DiskLruCache.this) {
                if (this.done) {
                    throw new IllegalStateException();
                } else if (this.entry.currentEditor != this) {
                    Sink r3 = DiskLruCache.NULL_SINK;
                    return r3;
                } else {
                    if (!this.entry.readable) {
                        this.written[i] = true;
                    }
                    try {
                        C17161 r1 = new FaultHidingSink(DiskLruCache.this.fileSystem.sink(this.entry.dirtyFiles[i])) {
                            /* access modifiers changed from: protected */
                            public void onException(IOException iOException) {
                                synchronized (DiskLruCache.this) {
                                    Editor.this.detach();
                                }
                            }
                        };
                        return r1;
                    } catch (FileNotFoundException unused) {
                        return DiskLruCache.NULL_SINK;
                    }
                }
            }
        }

        public void commit() throws IOException {
            synchronized (DiskLruCache.this) {
                if (!this.done) {
                    if (this.entry.currentEditor == this) {
                        DiskLruCache.this.completeEdit(this, true);
                    }
                    this.done = true;
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        public void abort() throws IOException {
            synchronized (DiskLruCache.this) {
                if (!this.done) {
                    if (this.entry.currentEditor == this) {
                        DiskLruCache.this.completeEdit(this, false);
                    }
                    this.done = true;
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:2|3|(2:7|8)|9|10) */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0015 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void abortUnlessCommitted() {
            /*
                r3 = this;
                com.android.okhttp.internal.DiskLruCache r0 = com.android.okhttp.internal.DiskLruCache.this
                monitor-enter(r0)
                boolean r1 = r3.done     // Catch:{ all -> 0x0017 }
                if (r1 != 0) goto L_0x0015
                com.android.okhttp.internal.DiskLruCache$Entry r1 = r3.entry     // Catch:{ all -> 0x0017 }
                com.android.okhttp.internal.DiskLruCache$Editor r1 = r1.currentEditor     // Catch:{ all -> 0x0017 }
                if (r1 != r3) goto L_0x0015
                com.android.okhttp.internal.DiskLruCache r1 = com.android.okhttp.internal.DiskLruCache.this     // Catch:{ IOException -> 0x0015 }
                r2 = 0
                r1.completeEdit(r3, r2)     // Catch:{ IOException -> 0x0015 }
            L_0x0015:
                monitor-exit(r0)     // Catch:{ all -> 0x0017 }
                return
            L_0x0017:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x0017 }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.okhttp.internal.DiskLruCache.Editor.abortUnlessCommitted():void");
        }
    }

    private final class Entry {
        /* access modifiers changed from: private */
        public final File[] cleanFiles;
        /* access modifiers changed from: private */
        public Editor currentEditor;
        /* access modifiers changed from: private */
        public final File[] dirtyFiles;
        /* access modifiers changed from: private */
        public final String key;
        /* access modifiers changed from: private */
        public final long[] lengths;
        /* access modifiers changed from: private */
        public boolean readable;
        /* access modifiers changed from: private */
        public long sequenceNumber;

        private Entry(String str) {
            this.key = str;
            this.lengths = new long[DiskLruCache.this.valueCount];
            this.cleanFiles = new File[DiskLruCache.this.valueCount];
            this.dirtyFiles = new File[DiskLruCache.this.valueCount];
            StringBuilder sb = new StringBuilder(str);
            sb.append('.');
            int length = sb.length();
            for (int i = 0; i < DiskLruCache.this.valueCount; i++) {
                sb.append(i);
                this.cleanFiles[i] = new File(DiskLruCache.this.directory, sb.toString());
                sb.append(".tmp");
                this.dirtyFiles[i] = new File(DiskLruCache.this.directory, sb.toString());
                sb.setLength(length);
            }
        }

        /* access modifiers changed from: private */
        public void setLengths(String[] strArr) throws IOException {
            if (strArr.length == DiskLruCache.this.valueCount) {
                int i = 0;
                while (i < strArr.length) {
                    try {
                        this.lengths[i] = Long.parseLong(strArr[i]);
                        i++;
                    } catch (NumberFormatException unused) {
                        throw invalidLengths(strArr);
                    }
                }
                return;
            }
            throw invalidLengths(strArr);
        }

        /* access modifiers changed from: package-private */
        public void writeLengths(BufferedSink bufferedSink) throws IOException {
            for (long writeDecimalLong : this.lengths) {
                bufferedSink.writeByte(32).writeDecimalLong(writeDecimalLong);
            }
        }

        private IOException invalidLengths(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString((Object[]) strArr));
        }

        /* access modifiers changed from: package-private */
        public Snapshot snapshot() {
            Source source;
            if (Thread.holdsLock(DiskLruCache.this)) {
                Source[] sourceArr = new Source[DiskLruCache.this.valueCount];
                long[] jArr = (long[]) this.lengths.clone();
                int i = 0;
                int i2 = 0;
                while (i2 < DiskLruCache.this.valueCount) {
                    try {
                        sourceArr[i2] = DiskLruCache.this.fileSystem.source(this.cleanFiles[i2]);
                        i2++;
                    } catch (FileNotFoundException unused) {
                        while (i < DiskLruCache.this.valueCount && (source = sourceArr[i]) != null) {
                            Util.closeQuietly((Closeable) source);
                            i++;
                        }
                        return null;
                    }
                }
                return new Snapshot(this.key, this.sequenceNumber, sourceArr, jArr);
            }
            throw new AssertionError();
        }
    }
}
