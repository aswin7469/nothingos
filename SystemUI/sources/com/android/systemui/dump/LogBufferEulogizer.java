package com.android.systemui.dump;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.p016io.Files;
import com.android.systemui.util.time.SystemClock;
import java.nio.file.Path;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B'\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nB5\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u000e¢\u0006\u0002\u0010\u0010J\u0010\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\fH\u0002J\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016J!\u0010\u0017\u001a\u0002H\u0018\"\f\b\u0000\u0010\u0018*\u00060\u0019j\u0002`\u001a2\u0006\u0010\u001b\u001a\u0002H\u0018¢\u0006\u0002\u0010\u001cR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, mo64987d2 = {"Lcom/android/systemui/dump/LogBufferEulogizer;", "", "context", "Landroid/content/Context;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "systemClock", "Lcom/android/systemui/util/time/SystemClock;", "files", "Lcom/android/systemui/util/io/Files;", "(Landroid/content/Context;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/util/io/Files;)V", "logPath", "Ljava/nio/file/Path;", "minWriteGap", "", "maxLogAgeToDump", "(Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/util/io/Files;Ljava/nio/file/Path;JJ)V", "getMillisSinceLastWrite", "path", "readEulogyIfPresent", "", "pw", "Ljava/io/PrintWriter;", "record", "T", "Ljava/lang/Exception;", "Lkotlin/Exception;", "reason", "(Ljava/lang/Exception;)Ljava/lang/Exception;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogBufferEulogizer.kt */
public final class LogBufferEulogizer {
    private final DumpManager dumpManager;
    private final Files files;
    private final Path logPath;
    private final long maxLogAgeToDump;
    private final long minWriteGap;
    private final SystemClock systemClock;

    public LogBufferEulogizer(DumpManager dumpManager2, SystemClock systemClock2, Files files2, Path path, long j, long j2) {
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock2, "systemClock");
        Intrinsics.checkNotNullParameter(files2, "files");
        Intrinsics.checkNotNullParameter(path, "logPath");
        this.dumpManager = dumpManager2;
        this.systemClock = systemClock2;
        this.files = files2;
        this.logPath = path;
        this.minWriteGap = j;
        this.maxLogAgeToDump = j2;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public LogBufferEulogizer(android.content.Context r11, com.android.systemui.dump.DumpManager r12, com.android.systemui.util.time.SystemClock r13, com.android.systemui.util.p016io.Files r14) {
        /*
            r10 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "dumpManager"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.lang.String r0 = "systemClock"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "files"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.io.File r11 = r11.getFilesDir()
            java.nio.file.Path r11 = r11.toPath()
            java.lang.String r11 = r11.toString()
            java.lang.String r0 = "log_buffers.txt"
            java.lang.String[] r0 = new java.lang.String[]{r0}
            java.nio.file.Path r5 = java.nio.file.Paths.get(r11, r0)
            java.lang.String r11 = "get(context.filesDir.toP…ing(), \"log_buffers.txt\")"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r11)
            long r6 = com.android.systemui.dump.LogBufferEulogizerKt.MIN_WRITE_GAP
            long r8 = com.android.systemui.dump.LogBufferEulogizerKt.MAX_AGE_TO_DUMP
            r1 = r10
            r2 = r12
            r3 = r13
            r4 = r14
            r1.<init>(r2, r3, r4, r5, r6, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.<init>(android.content.Context, com.android.systemui.dump.DumpManager, com.android.systemui.util.time.SystemClock, com.android.systemui.util.io.Files):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x00af, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        kotlin.p028io.CloseableKt.closeFinally(r4, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00b3, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <T extends java.lang.Exception> T record(T r15) {
        /*
            r14 = this;
            java.lang.String r0 = "ms"
            java.lang.String r1 = "Buffer eulogy took "
            java.lang.String r2 = "reason"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r15, r2)
            com.android.systemui.util.time.SystemClock r2 = r14.systemClock
            long r2 = r2.uptimeMillis()
            java.lang.String r4 = "Performing emergency dump of log buffers"
            java.lang.String r5 = "BufferEulogizer"
            android.util.Log.i(r5, r4)
            java.nio.file.Path r4 = r14.logPath
            long r6 = r14.getMillisSinceLastWrite(r4)
            long r8 = r14.minWriteGap
            int r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r4 >= 0) goto L_0x003c
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            java.lang.String r0 = "Cannot dump logs, last write was only "
            r14.<init>((java.lang.String) r0)
            java.lang.StringBuilder r14 = r14.append((long) r6)
            java.lang.String r0 = " ms ago"
            java.lang.StringBuilder r14 = r14.append((java.lang.String) r0)
            java.lang.String r14 = r14.toString()
            android.util.Log.w(r5, r14)
            return r15
        L_0x003c:
            r6 = 0
            com.android.systemui.util.io.Files r4 = r14.files     // Catch:{ Exception -> 0x00b4 }
            java.nio.file.Path r8 = r14.logPath     // Catch:{ Exception -> 0x00b4 }
            r9 = 2
            java.nio.file.OpenOption[] r9 = new java.nio.file.OpenOption[r9]     // Catch:{ Exception -> 0x00b4 }
            java.nio.file.StandardOpenOption r10 = java.nio.file.StandardOpenOption.CREATE     // Catch:{ Exception -> 0x00b4 }
            java.nio.file.OpenOption r10 = (java.nio.file.OpenOption) r10     // Catch:{ Exception -> 0x00b4 }
            r11 = 0
            r9[r11] = r10     // Catch:{ Exception -> 0x00b4 }
            java.nio.file.StandardOpenOption r10 = java.nio.file.StandardOpenOption.TRUNCATE_EXISTING     // Catch:{ Exception -> 0x00b4 }
            java.nio.file.OpenOption r10 = (java.nio.file.OpenOption) r10     // Catch:{ Exception -> 0x00b4 }
            r12 = 1
            r9[r12] = r10     // Catch:{ Exception -> 0x00b4 }
            java.io.BufferedWriter r4 = r4.newBufferedWriter(r8, r9)     // Catch:{ Exception -> 0x00b4 }
            java.io.Closeable r4 = (java.p026io.Closeable) r4     // Catch:{ Exception -> 0x00b4 }
            r8 = r4
            java.io.BufferedWriter r8 = (java.p026io.BufferedWriter) r8     // Catch:{ all -> 0x00ad }
            java.io.PrintWriter r9 = new java.io.PrintWriter     // Catch:{ all -> 0x00ad }
            java.io.Writer r8 = (java.p026io.Writer) r8     // Catch:{ all -> 0x00ad }
            r9.<init>((java.p026io.Writer) r8)     // Catch:{ all -> 0x00ad }
            java.text.SimpleDateFormat r8 = com.android.systemui.dump.LogBufferEulogizerKt.DATE_FORMAT     // Catch:{ all -> 0x00ad }
            com.android.systemui.util.time.SystemClock r10 = r14.systemClock     // Catch:{ all -> 0x00ad }
            long r12 = r10.currentTimeMillis()     // Catch:{ all -> 0x00ad }
            java.lang.Long r10 = java.lang.Long.valueOf((long) r12)     // Catch:{ all -> 0x00ad }
            java.lang.String r8 = r8.format(r10)     // Catch:{ all -> 0x00ad }
            r9.println((java.lang.String) r8)     // Catch:{ all -> 0x00ad }
            r9.println()     // Catch:{ all -> 0x00ad }
            java.lang.String r8 = "Dump triggered by exception:"
            r9.println((java.lang.String) r8)     // Catch:{ all -> 0x00ad }
            r15.printStackTrace((java.p026io.PrintWriter) r9)     // Catch:{ all -> 0x00ad }
            com.android.systemui.dump.DumpManager r8 = r14.dumpManager     // Catch:{ all -> 0x00ad }
            r8.dumpBuffers(r9, r11)     // Catch:{ all -> 0x00ad }
            com.android.systemui.util.time.SystemClock r14 = r14.systemClock     // Catch:{ all -> 0x00ad }
            long r6 = r14.uptimeMillis()     // Catch:{ all -> 0x00ad }
            long r6 = r6 - r2
            r9.println()     // Catch:{ all -> 0x00ad }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ad }
            r14.<init>((java.lang.String) r1)     // Catch:{ all -> 0x00ad }
            java.lang.StringBuilder r14 = r14.append((long) r6)     // Catch:{ all -> 0x00ad }
            java.lang.StringBuilder r14 = r14.append((java.lang.String) r0)     // Catch:{ all -> 0x00ad }
            java.lang.String r14 = r14.toString()     // Catch:{ all -> 0x00ad }
            r9.println((java.lang.String) r14)     // Catch:{ all -> 0x00ad }
            kotlin.Unit r14 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00ad }
            r14 = 0
            kotlin.p028io.CloseableKt.closeFinally(r4, r14)     // Catch:{ Exception -> 0x00b4 }
            goto L_0x00bc
        L_0x00ad:
            r14 = move-exception
            throw r14     // Catch:{ all -> 0x00af }
        L_0x00af:
            r2 = move-exception
            kotlin.p028io.CloseableKt.closeFinally(r4, r14)     // Catch:{ Exception -> 0x00b4 }
            throw r2     // Catch:{ Exception -> 0x00b4 }
        L_0x00b4:
            r14 = move-exception
            java.lang.String r2 = "Exception while attempting to dump buffers, bailing"
            java.lang.Throwable r14 = (java.lang.Throwable) r14
            android.util.Log.e(r5, r2, r14)
        L_0x00bc:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>((java.lang.String) r1)
            java.lang.StringBuilder r14 = r14.append((long) r6)
            java.lang.StringBuilder r14 = r14.append((java.lang.String) r0)
            java.lang.String r14 = r14.toString()
            android.util.Log.i(r5, r14)
            return r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.record(java.lang.Exception):java.lang.Exception");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005e, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        kotlin.jdk7.AutoCloseableKt.closeFinally(r6, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0062, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void readEulogyIfPresent(java.p026io.PrintWriter r7) {
        /*
            r6 = this;
            java.lang.String r0 = "BufferEulogizer"
            java.lang.String r1 = "Not eulogizing buffers; they are "
            java.lang.String r2 = "pw"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r2)
            java.nio.file.Path r2 = r6.logPath     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            long r2 = r6.getMillisSinceLastWrite(r2)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            long r4 = r6.maxLogAgeToDump     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 <= 0) goto L_0x0035
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            r6.<init>((java.lang.String) r1)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.util.concurrent.TimeUnit r7 = java.util.concurrent.TimeUnit.HOURS     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            long r1 = r7.convert(r2, r1)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.lang.StringBuilder r6 = r6.append((long) r1)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.lang.String r7 = " hours old"
            java.lang.StringBuilder r6 = r6.append((java.lang.String) r7)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            android.util.Log.i(r0, r6)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            return
        L_0x0035:
            com.android.systemui.util.io.Files r1 = r6.files     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.nio.file.Path r6 = r6.logPath     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.util.stream.Stream r6 = r1.lines(r6)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            java.lang.AutoCloseable r6 = (java.lang.AutoCloseable) r6     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            r1 = r6
            java.util.stream.Stream r1 = (java.util.stream.Stream) r1     // Catch:{ all -> 0x005c }
            r7.println()     // Catch:{ all -> 0x005c }
            r7.println()     // Catch:{ all -> 0x005c }
            java.lang.String r2 = "=============== BUFFERS FROM MOST RECENT CRASH ==============="
            r7.println((java.lang.String) r2)     // Catch:{ all -> 0x005c }
            com.android.systemui.dump.LogBufferEulogizer$$ExternalSyntheticLambda0 r2 = new com.android.systemui.dump.LogBufferEulogizer$$ExternalSyntheticLambda0     // Catch:{ all -> 0x005c }
            r2.<init>(r7)     // Catch:{ all -> 0x005c }
            r1.forEach(r2)     // Catch:{ all -> 0x005c }
            kotlin.Unit r7 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x005c }
            r7 = 0
            kotlin.jdk7.AutoCloseableKt.closeFinally(r6, r7)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            goto L_0x006b
        L_0x005c:
            r7 = move-exception
            throw r7     // Catch:{ all -> 0x005e }
        L_0x005e:
            r1 = move-exception
            kotlin.jdk7.AutoCloseableKt.closeFinally(r6, r7)     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
            throw r1     // Catch:{ IOException -> 0x006b, UncheckedIOException -> 0x0063 }
        L_0x0063:
            r6 = move-exception
            java.lang.String r7 = "UncheckedIOException while dumping the core"
            java.lang.Throwable r6 = (java.lang.Throwable) r6
            android.util.Log.e(r0, r7, r6)
        L_0x006b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.readEulogyIfPresent(java.io.PrintWriter):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: readEulogyIfPresent$lambda-2$lambda-1  reason: not valid java name */
    public static final void m2748readEulogyIfPresent$lambda2$lambda1(PrintWriter printWriter, String str) {
        Intrinsics.checkNotNullParameter(printWriter, "$pw");
        printWriter.println(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0018, code lost:
        r3 = r4.lastModifiedTime();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final long getMillisSinceLastWrite(java.nio.file.Path r4) {
        /*
            r3 = this;
            com.android.systemui.util.io.Files r0 = r3.files     // Catch:{ IOException -> 0x000c }
            java.lang.Class<java.nio.file.attribute.BasicFileAttributes> r1 = java.nio.file.attribute.BasicFileAttributes.class
            r2 = 0
            java.nio.file.LinkOption[] r2 = new java.nio.file.LinkOption[r2]     // Catch:{ IOException -> 0x000c }
            java.nio.file.attribute.BasicFileAttributes r4 = r0.readAttributes(r4, r1, r2)     // Catch:{ IOException -> 0x000c }
            goto L_0x0010
        L_0x000c:
            r4 = 0
            r0 = r4
            java.nio.file.attribute.BasicFileAttributes r0 = (java.nio.file.attribute.BasicFileAttributes) r0
        L_0x0010:
            com.android.systemui.util.time.SystemClock r3 = r3.systemClock
            long r0 = r3.currentTimeMillis()
            if (r4 == 0) goto L_0x0023
            java.nio.file.attribute.FileTime r3 = r4.lastModifiedTime()
            if (r3 == 0) goto L_0x0023
            long r3 = r3.toMillis()
            goto L_0x0025
        L_0x0023:
            r3 = 0
        L_0x0025:
            long r0 = r0 - r3
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dump.LogBufferEulogizer.getMillisSinceLastWrite(java.nio.file.Path):long");
    }
}
