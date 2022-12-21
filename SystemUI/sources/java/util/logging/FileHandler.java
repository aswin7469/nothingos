package java.util.logging;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.p026io.BufferedOutputStream;
import java.p026io.File;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class FileHandler extends StreamHandler {
    private static final int MAX_LOCKS = 100;
    private static final Set<String> locks = new HashSet();
    private boolean append;
    private int count;
    private File[] files;
    private int limit;
    private FileChannel lockFileChannel;
    private String lockFileName;
    private MeteredStream meter;
    private String pattern;

    private class MeteredStream extends OutputStream {
        final OutputStream out;
        int written;

        MeteredStream(OutputStream outputStream, int i) {
            this.out = outputStream;
            this.written = i;
        }

        public void write(int i) throws IOException {
            this.out.write(i);
            this.written++;
        }

        public void write(byte[] bArr) throws IOException {
            this.out.write(bArr);
            this.written += bArr.length;
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            this.out.write(bArr, i, i2);
            this.written += i2;
        }

        public void flush() throws IOException {
            this.out.flush();
        }

        public void close() throws IOException {
            this.out.close();
        }
    }

    private void open(File file, boolean z) throws IOException {
        MeteredStream meteredStream = new MeteredStream(new BufferedOutputStream(new FileOutputStream(file.toString(), z)), z ? (int) file.length() : 0);
        this.meter = meteredStream;
        setOutputStream(meteredStream);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:0|(1:2)|3|(1:5)|6|7|8|9|10|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x00dd */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void configure() {
        /*
            r5 = this;
            java.util.logging.LogManager r0 = java.util.logging.LogManager.getLogManager()
            java.lang.Class r1 = r5.getClass()
            java.lang.String r1 = r1.getName()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".pattern"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            java.lang.String r3 = "%h/java%u.log"
            java.lang.String r2 = r0.getStringProperty(r2, r3)
            r5.pattern = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".limit"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            r3 = 0
            int r2 = r0.getIntProperty(r2, r3)
            r5.limit = r2
            if (r2 >= 0) goto L_0x0041
            r5.limit = r3
        L_0x0041:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r4 = ".count"
            r2.append((java.lang.String) r4)
            java.lang.String r2 = r2.toString()
            r4 = 1
            int r2 = r0.getIntProperty(r2, r4)
            r5.count = r2
            if (r2 > 0) goto L_0x005d
            r5.count = r4
        L_0x005d:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r4 = ".append"
            r2.append((java.lang.String) r4)
            java.lang.String r2 = r2.toString()
            boolean r2 = r0.getBooleanProperty(r2, r3)
            r5.append = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".level"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            java.util.logging.Level r3 = java.util.logging.Level.ALL
            java.util.logging.Level r2 = r0.getLevelProperty(r2, r3)
            r5.setLevel(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r3 = ".filter"
            r2.append((java.lang.String) r3)
            java.lang.String r2 = r2.toString()
            r3 = 0
            java.util.logging.Filter r2 = r0.getFilterProperty(r2, r3)
            r5.setFilter(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r1)
            java.lang.String r4 = ".formatter"
            r2.append((java.lang.String) r4)
            java.lang.String r2 = r2.toString()
            java.util.logging.XMLFormatter r4 = new java.util.logging.XMLFormatter
            r4.<init>()
            java.util.logging.Formatter r2 = r0.getFormatterProperty(r2, r4)
            r5.setFormatter(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00dd }
            r2.<init>()     // Catch:{ Exception -> 0x00dd }
            r2.append((java.lang.String) r1)     // Catch:{ Exception -> 0x00dd }
            java.lang.String r1 = ".encoding"
            r2.append((java.lang.String) r1)     // Catch:{ Exception -> 0x00dd }
            java.lang.String r1 = r2.toString()     // Catch:{ Exception -> 0x00dd }
            java.lang.String r0 = r0.getStringProperty(r1, r3)     // Catch:{ Exception -> 0x00dd }
            r5.setEncoding(r0)     // Catch:{ Exception -> 0x00dd }
            goto L_0x00e0
        L_0x00dd:
            r5.setEncoding(r3)     // Catch:{ Exception -> 0x00e0 }
        L_0x00e0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.FileHandler.configure():void");
    }

    public FileHandler() throws IOException, SecurityException {
        checkPermission();
        configure();
        openFiles();
    }

    public FileHandler(String str) throws IOException, SecurityException {
        if (str.length() >= 1) {
            checkPermission();
            configure();
            this.pattern = str;
            this.limit = 0;
            this.count = 1;
            openFiles();
            return;
        }
        throw new IllegalArgumentException();
    }

    public FileHandler(String str, boolean z) throws IOException, SecurityException {
        if (str.length() >= 1) {
            checkPermission();
            configure();
            this.pattern = str;
            this.limit = 0;
            this.count = 1;
            this.append = z;
            openFiles();
            return;
        }
        throw new IllegalArgumentException();
    }

    public FileHandler(String str, int i, int i2) throws IOException, SecurityException {
        if (i < 0 || i2 < 1 || str.length() < 1) {
            throw new IllegalArgumentException();
        }
        checkPermission();
        configure();
        this.pattern = str;
        this.limit = i;
        this.count = i2;
        openFiles();
    }

    public FileHandler(String str, int i, int i2, boolean z) throws IOException, SecurityException {
        if (i < 0 || i2 < 1 || str.length() < 1) {
            throw new IllegalArgumentException();
        }
        checkPermission();
        configure();
        this.pattern = str;
        this.limit = i;
        this.count = i2;
        this.append = z;
        openFiles();
    }

    private boolean isParentWritable(Path path) {
        Path parent = path.getParent();
        if (parent == null) {
            parent = path.toAbsolutePath().getParent();
        }
        return parent != null && Files.isWritable(parent);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:26|27|(3:31|32|93)(1:88)) */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r8 = java.nio.channels.FileChannel.open(r7, java.nio.file.StandardOpenOption.WRITE, java.nio.file.StandardOpenOption.APPEND);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        locks.add(r14.lockFileName);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00aa, code lost:
        r14.files = new java.p026io.File[r14.count];
        r3 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00b3, code lost:
        if (r3 >= r14.count) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00b5, code lost:
        r14.files[r3] = generate(r14.pattern, r3, r5);
        r3 = r3 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00c4, code lost:
        if (r14.append == false) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00c6, code lost:
        open(r14.files[0], true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00ce, code lost:
        rotate();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00d1, code lost:
        r0 = r0.lastException;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00d3, code lost:
        if (r0 == null) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00d7, code lost:
        if ((r0 instanceof java.p026io.IOException) != false) goto L_0x00f4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00db, code lost:
        if ((r0 instanceof java.lang.SecurityException) == false) goto L_0x00e0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00df, code lost:
        throw ((java.lang.SecurityException) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00f3, code lost:
        throw new java.p026io.IOException("Exception: " + r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00f6, code lost:
        throw ((java.p026io.IOException) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00f7, code lost:
        setErrorManager(new java.util.logging.ErrorManager());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00ff, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x0070 */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a2 A[EDGE_INSN: B:47:0x00a2->B:48:? ?: BREAK  , SYNTHETIC, Splitter:B:47:0x00a2] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0100 A[SYNTHETIC, Splitter:B:72:0x0100] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void openFiles() throws java.p026io.IOException {
        /*
            r14 = this;
            java.util.logging.LogManager r0 = java.util.logging.LogManager.getLogManager()
            r0.checkPermission()
            int r0 = r14.count
            r1 = 1
            if (r0 < r1) goto L_0x0121
            int r0 = r14.limit
            r2 = 0
            if (r0 >= 0) goto L_0x0013
            r14.limit = r2
        L_0x0013:
            java.util.logging.FileHandler$InitializationErrorManager r0 = new java.util.logging.FileHandler$InitializationErrorManager
            r3 = 0
            r0.<init>()
            r14.setErrorManager(r0)
            r4 = -1
            r5 = r4
        L_0x001e:
            int r5 = r5 + r1
            r6 = 100
            if (r5 > r6) goto L_0x010b
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = r14.pattern
            java.io.File r7 = r14.generate(r7, r2, r5)
            java.lang.String r7 = r7.toString()
            r6.append((java.lang.String) r7)
            java.lang.String r7 = ".lck"
            r6.append((java.lang.String) r7)
            java.lang.String r6 = r6.toString()
            r14.lockFileName = r6
            java.util.Set<java.lang.String> r6 = locks
            monitor-enter(r6)
            java.lang.String r7 = r14.lockFileName     // Catch:{ all -> 0x0108 }
            boolean r7 = r6.contains(r7)     // Catch:{ all -> 0x0108 }
            if (r7 == 0) goto L_0x004d
            monitor-exit(r6)     // Catch:{ all -> 0x0108 }
            goto L_0x001e
        L_0x004d:
            java.lang.String r7 = r14.lockFileName     // Catch:{ all -> 0x0108 }
            java.lang.String[] r8 = new java.lang.String[r2]     // Catch:{ all -> 0x0108 }
            java.nio.file.Path r7 = java.nio.file.Paths.get(r7, r8)     // Catch:{ all -> 0x0108 }
            r10 = r2
            r8 = r3
            r9 = r4
        L_0x0058:
            if (r8 != 0) goto L_0x0091
            int r11 = r9 + 1
            if (r9 >= r1) goto L_0x0091
            r9 = 2
            java.nio.file.OpenOption[] r12 = new java.nio.file.OpenOption[r9]     // Catch:{ FileAlreadyExistsException -> 0x0070 }
            java.nio.file.StandardOpenOption r13 = java.nio.file.StandardOpenOption.CREATE_NEW     // Catch:{ FileAlreadyExistsException -> 0x0070 }
            r12[r2] = r13     // Catch:{ FileAlreadyExistsException -> 0x0070 }
            java.nio.file.StandardOpenOption r13 = java.nio.file.StandardOpenOption.WRITE     // Catch:{ FileAlreadyExistsException -> 0x0070 }
            r12[r1] = r13     // Catch:{ FileAlreadyExistsException -> 0x0070 }
            java.nio.channels.FileChannel r8 = java.nio.channels.FileChannel.open(r7, r12)     // Catch:{ FileAlreadyExistsException -> 0x0070 }
            r10 = r1
        L_0x006e:
            r9 = r11
            goto L_0x0058
        L_0x0070:
            java.nio.file.LinkOption[] r12 = new java.nio.file.LinkOption[r1]     // Catch:{ all -> 0x0108 }
            java.nio.file.LinkOption r13 = java.nio.file.LinkOption.NOFOLLOW_LINKS     // Catch:{ all -> 0x0108 }
            r12[r2] = r13     // Catch:{ all -> 0x0108 }
            boolean r12 = java.nio.file.Files.isRegularFile(r7, r12)     // Catch:{ all -> 0x0108 }
            if (r12 == 0) goto L_0x0091
            boolean r12 = r14.isParentWritable(r7)     // Catch:{ all -> 0x0108 }
            if (r12 == 0) goto L_0x0091
            java.nio.file.OpenOption[] r9 = new java.nio.file.OpenOption[r9]     // Catch:{ NoSuchFileException -> 0x006e, IOException -> 0x0091 }
            java.nio.file.StandardOpenOption r12 = java.nio.file.StandardOpenOption.WRITE     // Catch:{ NoSuchFileException -> 0x006e, IOException -> 0x0091 }
            r9[r2] = r12     // Catch:{ NoSuchFileException -> 0x006e, IOException -> 0x0091 }
            java.nio.file.StandardOpenOption r12 = java.nio.file.StandardOpenOption.APPEND     // Catch:{ NoSuchFileException -> 0x006e, IOException -> 0x0091 }
            r9[r1] = r12     // Catch:{ NoSuchFileException -> 0x006e, IOException -> 0x0091 }
            java.nio.channels.FileChannel r8 = java.nio.channels.FileChannel.open(r7, r9)     // Catch:{ NoSuchFileException -> 0x006e, IOException -> 0x0091 }
            goto L_0x006e
        L_0x0091:
            if (r8 != 0) goto L_0x0095
            monitor-exit(r6)     // Catch:{ all -> 0x0108 }
            goto L_0x001e
        L_0x0095:
            r14.lockFileChannel = r8     // Catch:{ all -> 0x0108 }
            java.nio.channels.FileLock r7 = r8.tryLock()     // Catch:{ IOException -> 0x00a0, OverlappingFileLockException -> 0x009f }
            if (r7 == 0) goto L_0x009f
            r10 = r1
            goto L_0x00a0
        L_0x009f:
            r10 = r2
        L_0x00a0:
            if (r10 == 0) goto L_0x0100
            java.util.Set<java.lang.String> r3 = locks     // Catch:{ all -> 0x0108 }
            java.lang.String r4 = r14.lockFileName     // Catch:{ all -> 0x0108 }
            r3.add(r4)     // Catch:{ all -> 0x0108 }
            monitor-exit(r6)     // Catch:{ all -> 0x0108 }
            int r3 = r14.count
            java.io.File[] r3 = new java.p026io.File[r3]
            r14.files = r3
            r3 = r2
        L_0x00b1:
            int r4 = r14.count
            if (r3 >= r4) goto L_0x00c2
            java.io.File[] r4 = r14.files
            java.lang.String r6 = r14.pattern
            java.io.File r6 = r14.generate(r6, r3, r5)
            r4[r3] = r6
            int r3 = r3 + 1
            goto L_0x00b1
        L_0x00c2:
            boolean r3 = r14.append
            if (r3 == 0) goto L_0x00ce
            java.io.File[] r3 = r14.files
            r2 = r3[r2]
            r14.open(r2, r1)
            goto L_0x00d1
        L_0x00ce:
            r14.rotate()
        L_0x00d1:
            java.lang.Exception r0 = r0.lastException
            if (r0 == 0) goto L_0x00f7
            boolean r14 = r0 instanceof java.p026io.IOException
            if (r14 != 0) goto L_0x00f4
            boolean r14 = r0 instanceof java.lang.SecurityException
            if (r14 == 0) goto L_0x00e0
            java.lang.SecurityException r0 = (java.lang.SecurityException) r0
            throw r0
        L_0x00e0:
            java.io.IOException r14 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Exception: "
            r1.<init>((java.lang.String) r2)
            r1.append((java.lang.Object) r0)
            java.lang.String r0 = r1.toString()
            r14.<init>((java.lang.String) r0)
            throw r14
        L_0x00f4:
            java.io.IOException r0 = (java.p026io.IOException) r0
            throw r0
        L_0x00f7:
            java.util.logging.ErrorManager r0 = new java.util.logging.ErrorManager
            r0.<init>()
            r14.setErrorManager(r0)
            return
        L_0x0100:
            java.nio.channels.FileChannel r7 = r14.lockFileChannel     // Catch:{ all -> 0x0108 }
            r7.close()     // Catch:{ all -> 0x0108 }
            monitor-exit(r6)     // Catch:{ all -> 0x0108 }
            goto L_0x001e
        L_0x0108:
            r14 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0108 }
            throw r14
        L_0x010b:
            java.io.IOException r0 = new java.io.IOException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Couldn't get lock for "
            r1.<init>((java.lang.String) r2)
            java.lang.String r14 = r14.pattern
            r1.append((java.lang.String) r14)
            java.lang.String r14 = r1.toString()
            r0.<init>((java.lang.String) r14)
            throw r0
        L_0x0121:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "file count = "
            r1.<init>((java.lang.String) r2)
            int r14 = r14.count
            r1.append((int) r14)
            java.lang.String r14 = r1.toString()
            r0.<init>((java.lang.String) r14)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.FileHandler.openFiles():void");
    }

    private File generate(String str, int i, int i2) throws IOException {
        String str2 = str;
        int i3 = i;
        int i4 = i2;
        File file = null;
        String str3 = "";
        int i5 = 0;
        boolean z = false;
        boolean z2 = false;
        while (i5 < str.length()) {
            char charAt = str2.charAt(i5);
            i5++;
            char lowerCase = i5 < str.length() ? Character.toLowerCase(str2.charAt(i5)) : 0;
            if (charAt != '/') {
                if (charAt == '%') {
                    if (lowerCase == 't') {
                        String property = System.getProperty("java.io.tmpdir");
                        if (property == null) {
                            property = System.getProperty("user.home");
                        }
                        i5++;
                        file = new File(property);
                    } else if (lowerCase == 'h') {
                        file = new File(System.getProperty("user.home"));
                        i5++;
                    } else if (lowerCase == 'g') {
                        str3 = str3 + i3;
                        i5++;
                        z = true;
                    } else if (lowerCase == 'u') {
                        str3 = str3 + i4;
                        i5++;
                        z2 = true;
                    } else if (lowerCase == '%') {
                        str3 = str3 + "%";
                        i5++;
                    }
                }
                str3 = str3 + charAt;
            } else if (file == null) {
                file = new File(str3);
            } else {
                file = new File(file, str3);
            }
            str3 = "";
        }
        if (this.count > 1 && !z) {
            str3 = str3 + BaseIconCache.EMPTY_CLASS_NAME + i3;
        }
        if (i4 > 0 && !z2) {
            str3 = str3 + BaseIconCache.EMPTY_CLASS_NAME + i4;
        }
        if (str3.length() <= 0) {
            return file;
        }
        if (file == null) {
            return new File(str3);
        }
        return new File(file, str3);
    }

    /* access modifiers changed from: private */
    public synchronized void rotate() {
        Level level = getLevel();
        setLevel(Level.OFF);
        super.close();
        for (int i = this.count - 2; i >= 0; i--) {
            File[] fileArr = this.files;
            File file = fileArr[i];
            File file2 = fileArr[i + 1];
            if (file.exists()) {
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
            }
        }
        try {
            open(this.files[0], false);
        } catch (IOException e) {
            reportError((String) null, e, 4);
        }
        setLevel(level);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void publish(java.util.logging.LogRecord r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.isLoggable(r2)     // Catch:{ all -> 0x0025 }
            if (r0 != 0) goto L_0x0009
            monitor-exit(r1)
            return
        L_0x0009:
            super.publish(r2)     // Catch:{ all -> 0x0025 }
            r1.flush()     // Catch:{ all -> 0x0025 }
            int r2 = r1.limit     // Catch:{ all -> 0x0025 }
            if (r2 <= 0) goto L_0x0023
            java.util.logging.FileHandler$MeteredStream r2 = r1.meter     // Catch:{ all -> 0x0025 }
            int r2 = r2.written     // Catch:{ all -> 0x0025 }
            int r0 = r1.limit     // Catch:{ all -> 0x0025 }
            if (r2 < r0) goto L_0x0023
            java.util.logging.FileHandler$1 r2 = new java.util.logging.FileHandler$1     // Catch:{ all -> 0x0025 }
            r2.<init>()     // Catch:{ all -> 0x0025 }
            java.security.AccessController.doPrivileged(r2)     // Catch:{ all -> 0x0025 }
        L_0x0023:
            monitor-exit(r1)
            return
        L_0x0025:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.FileHandler.publish(java.util.logging.LogRecord):void");
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
        	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
        	at java.base/java.util.Objects.checkIndex(Objects.java:372)
        	at java.base/java.util.ArrayList.get(ArrayList.java:458)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    public synchronized void close() throws java.lang.SecurityException {
        /*
            r2 = this;
            monitor-enter(r2)
            super.close()     // Catch:{ all -> 0x002c }
            java.lang.String r0 = r2.lockFileName     // Catch:{ all -> 0x002c }
            if (r0 != 0) goto L_0x000a
            monitor-exit(r2)
            return
        L_0x000a:
            java.nio.channels.FileChannel r0 = r2.lockFileChannel     // Catch:{ Exception -> 0x000f }
            r0.close()     // Catch:{ Exception -> 0x000f }
        L_0x000f:
            java.util.Set<java.lang.String> r0 = locks     // Catch:{ all -> 0x002c }
            monitor-enter(r0)     // Catch:{ all -> 0x002c }
            java.lang.String r1 = r2.lockFileName     // Catch:{ all -> 0x0029 }
            r0.remove(r1)     // Catch:{ all -> 0x0029 }
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x002c }
            java.lang.String r1 = r2.lockFileName     // Catch:{ all -> 0x002c }
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x002c }
            r0.delete()     // Catch:{ all -> 0x002c }
            r0 = 0
            r2.lockFileName = r0     // Catch:{ all -> 0x002c }
            r2.lockFileChannel = r0     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return
        L_0x0029:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            throw r1     // Catch:{ all -> 0x002c }
        L_0x002c:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.logging.FileHandler.close():void");
    }

    private static class InitializationErrorManager extends ErrorManager {
        Exception lastException;

        private InitializationErrorManager() {
        }

        public void error(String str, Exception exc, int i) {
            this.lastException = exc;
        }
    }
}
