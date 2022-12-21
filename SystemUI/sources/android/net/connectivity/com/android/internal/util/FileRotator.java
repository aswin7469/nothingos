package android.net.connectivity.com.android.internal.util;

import java.p026io.BufferedInputStream;
import java.p026io.BufferedOutputStream;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.Objects;
import libcore.p030io.IoUtils;

public class FileRotator {
    private static final boolean LOGD = false;
    private static final String SUFFIX_BACKUP = ".backup";
    private static final String SUFFIX_NO_BACKUP = ".no_backup";
    private static final String TAG = "FileRotator";
    private final File mBasePath;
    private final long mDeleteAgeMillis;
    private final String mPrefix;
    private final long mRotateAgeMillis;

    public interface Reader {
        void read(InputStream inputStream) throws IOException;
    }

    public interface Rewriter extends Reader, Writer {
        void reset();

        boolean shouldWrite();
    }

    public interface Writer {
        void write(OutputStream outputStream) throws IOException;
    }

    public FileRotator(File file, String str, long j, long j2) {
        File file2 = (File) Objects.requireNonNull(file);
        this.mBasePath = file2;
        this.mPrefix = (String) Objects.requireNonNull(str);
        this.mRotateAgeMillis = j;
        this.mDeleteAgeMillis = j2;
        file2.mkdirs();
        for (String str2 : file2.list()) {
            if (str2.startsWith(this.mPrefix)) {
                if (str2.endsWith(SUFFIX_BACKUP)) {
                    new File(this.mBasePath, str2).renameTo(new File(this.mBasePath, str2.substring(0, str2.length() - 7)));
                } else if (str2.endsWith(SUFFIX_NO_BACKUP)) {
                    File file3 = new File(this.mBasePath, str2);
                    File file4 = new File(this.mBasePath, str2.substring(0, str2.length() - 10));
                    file3.delete();
                    file4.delete();
                }
            }
        }
    }

    public void deleteAll() {
        FileInfo fileInfo = new FileInfo(this.mPrefix);
        for (String str : this.mBasePath.list()) {
            if (fileInfo.parse(str)) {
                new File(this.mBasePath, str).delete();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003c, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0040, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dumpAll(java.p026io.OutputStream r8) throws java.p026io.IOException {
        /*
            r7 = this;
            java.util.zip.ZipOutputStream r0 = new java.util.zip.ZipOutputStream
            r0.<init>(r8)
            android.net.connectivity.com.android.internal.util.FileRotator$FileInfo r8 = new android.net.connectivity.com.android.internal.util.FileRotator$FileInfo     // Catch:{ all -> 0x0048 }
            java.lang.String r1 = r7.mPrefix     // Catch:{ all -> 0x0048 }
            r8.<init>(r1)     // Catch:{ all -> 0x0048 }
            java.io.File r1 = r7.mBasePath     // Catch:{ all -> 0x0048 }
            java.lang.String[] r1 = r1.list()     // Catch:{ all -> 0x0048 }
            int r2 = r1.length     // Catch:{ all -> 0x0048 }
            r3 = 0
        L_0x0014:
            if (r3 >= r2) goto L_0x0044
            r4 = r1[r3]     // Catch:{ all -> 0x0048 }
            boolean r5 = r8.parse(r4)     // Catch:{ all -> 0x0048 }
            if (r5 == 0) goto L_0x0041
            java.util.zip.ZipEntry r5 = new java.util.zip.ZipEntry     // Catch:{ all -> 0x0048 }
            r5.<init>((java.lang.String) r4)     // Catch:{ all -> 0x0048 }
            r0.putNextEntry(r5)     // Catch:{ all -> 0x0048 }
            java.io.File r5 = new java.io.File     // Catch:{ all -> 0x0048 }
            java.io.File r6 = r7.mBasePath     // Catch:{ all -> 0x0048 }
            r5.<init>((java.p026io.File) r6, (java.lang.String) r4)     // Catch:{ all -> 0x0048 }
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ all -> 0x0048 }
            r4.<init>((java.p026io.File) r5)     // Catch:{ all -> 0x0048 }
            android.os.FileUtils.copy(r4, r0)     // Catch:{ all -> 0x003c }
            libcore.p030io.IoUtils.closeQuietly((java.lang.AutoCloseable) r4)     // Catch:{ all -> 0x0048 }
            r0.closeEntry()     // Catch:{ all -> 0x0048 }
            goto L_0x0041
        L_0x003c:
            r7 = move-exception
            libcore.p030io.IoUtils.closeQuietly((java.lang.AutoCloseable) r4)     // Catch:{ all -> 0x0048 }
            throw r7     // Catch:{ all -> 0x0048 }
        L_0x0041:
            int r3 = r3 + 1
            goto L_0x0014
        L_0x0044:
            libcore.p030io.IoUtils.closeQuietly((java.lang.AutoCloseable) r0)
            return
        L_0x0048:
            r7 = move-exception
            libcore.p030io.IoUtils.closeQuietly((java.lang.AutoCloseable) r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.connectivity.com.android.internal.util.FileRotator.dumpAll(java.io.OutputStream):void");
    }

    public void rewriteActive(Rewriter rewriter, long j) throws IOException {
        rewriteSingle(rewriter, getActiveName(j));
    }

    @Deprecated
    public void combineActive(final Reader reader, final Writer writer, long j) throws IOException {
        rewriteActive(new Rewriter() {
            public void reset() {
            }

            public boolean shouldWrite() {
                return true;
            }

            public void read(InputStream inputStream) throws IOException {
                reader.read(inputStream);
            }

            public void write(OutputStream outputStream) throws IOException {
                writer.write(outputStream);
            }
        }, j);
    }

    public void rewriteAll(Rewriter rewriter) throws IOException {
        FileInfo fileInfo = new FileInfo(this.mPrefix);
        for (String str : this.mBasePath.list()) {
            if (fileInfo.parse(str)) {
                rewriteSingle(rewriter, str);
            }
        }
    }

    private void rewriteSingle(Rewriter rewriter, String str) throws IOException {
        File file = new File(this.mBasePath, str);
        rewriter.reset();
        if (file.exists()) {
            readFile(file, rewriter);
            if (rewriter.shouldWrite()) {
                File file2 = this.mBasePath;
                File file3 = new File(file2, str + SUFFIX_BACKUP);
                file.renameTo(file3);
                try {
                    writeFile(file, rewriter);
                    file3.delete();
                } catch (Throwable th) {
                    file.delete();
                    file3.renameTo(file);
                    throw rethrowAsIoException(th);
                }
            }
        } else {
            File file4 = this.mBasePath;
            File file5 = new File(file4, str + SUFFIX_NO_BACKUP);
            file5.createNewFile();
            try {
                writeFile(file, rewriter);
                file5.delete();
            } catch (Throwable th2) {
                file.delete();
                file5.delete();
                throw rethrowAsIoException(th2);
            }
        }
    }

    public void rewriteSingle(Rewriter rewriter, long j, long j2) throws IOException {
        FileInfo fileInfo = new FileInfo(this.mPrefix);
        fileInfo.startMillis = j;
        fileInfo.endMillis = j2;
        rewriteSingle(rewriter, fileInfo.build());
    }

    public void readMatching(Reader reader, long j, long j2) throws IOException {
        FileInfo fileInfo = new FileInfo(this.mPrefix);
        for (String str : this.mBasePath.list()) {
            if (fileInfo.parse(str) && fileInfo.startMillis <= j2 && j <= fileInfo.endMillis) {
                readFile(new File(this.mBasePath, str), reader);
            }
        }
    }

    private String getActiveName(long j) {
        FileInfo fileInfo = new FileInfo(this.mPrefix);
        String str = null;
        long j2 = Long.MAX_VALUE;
        for (String str2 : this.mBasePath.list()) {
            if (fileInfo.parse(str2) && fileInfo.isActive() && fileInfo.startMillis < j && fileInfo.startMillis < j2) {
                j2 = fileInfo.startMillis;
                str = str2;
            }
        }
        if (str != null) {
            return str;
        }
        fileInfo.startMillis = j;
        fileInfo.endMillis = Long.MAX_VALUE;
        return fileInfo.build();
    }

    public void maybeRotate(long j) {
        long j2 = j - this.mRotateAgeMillis;
        long j3 = j - this.mDeleteAgeMillis;
        FileInfo fileInfo = new FileInfo(this.mPrefix);
        String[] list = this.mBasePath.list();
        if (list != null) {
            for (String str : list) {
                if (fileInfo.parse(str)) {
                    if (fileInfo.isActive()) {
                        if (fileInfo.startMillis <= j2) {
                            fileInfo.endMillis = j;
                            new File(this.mBasePath, str).renameTo(new File(this.mBasePath, fileInfo.build()));
                        }
                    } else if (fileInfo.endMillis <= j3) {
                        new File(this.mBasePath, str).delete();
                    }
                }
            }
        }
    }

    private static void readFile(File file, Reader reader) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        try {
            reader.read(bufferedInputStream);
        } finally {
            IoUtils.closeQuietly((AutoCloseable) bufferedInputStream);
        }
    }

    private static void writeFile(File file, Writer writer) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        try {
            writer.write(bufferedOutputStream);
            bufferedOutputStream.flush();
        } finally {
            try {
                fileOutputStream.getFD().sync();
            } catch (IOException unused) {
            }
            IoUtils.closeQuietly((AutoCloseable) bufferedOutputStream);
        }
    }

    private static IOException rethrowAsIoException(Throwable th) throws IOException {
        if (th instanceof IOException) {
            throw ((IOException) th);
        }
        throw new IOException(th.getMessage(), th);
    }

    private static class FileInfo {
        public long endMillis;
        public final String prefix;
        public long startMillis;

        public FileInfo(String str) {
            this.prefix = (String) Objects.requireNonNull(str);
        }

        public boolean parse(String str) {
            this.endMillis = -1;
            this.startMillis = -1;
            int lastIndexOf = str.lastIndexOf(46);
            int lastIndexOf2 = str.lastIndexOf(45);
            if (lastIndexOf == -1 || lastIndexOf2 == -1 || !this.prefix.equals(str.substring(0, lastIndexOf))) {
                return false;
            }
            try {
                this.startMillis = Long.parseLong(str.substring(lastIndexOf + 1, lastIndexOf2));
                if (str.length() - lastIndexOf2 == 1) {
                    this.endMillis = Long.MAX_VALUE;
                } else {
                    this.endMillis = Long.parseLong(str.substring(lastIndexOf2 + 1));
                }
                return true;
            } catch (NumberFormatException unused) {
                return false;
            }
        }

        public String build() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.prefix);
            sb.append('.');
            sb.append(this.startMillis);
            sb.append('-');
            long j = this.endMillis;
            if (j != Long.MAX_VALUE) {
                sb.append(j);
            }
            return sb.toString();
        }

        public boolean isActive() {
            return this.endMillis == Long.MAX_VALUE;
        }
    }
}
