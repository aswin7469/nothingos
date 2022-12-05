package com.android.systemui.dump;

import android.content.Context;
import android.util.Log;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.time.SystemClock;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jdk7.AutoCloseableKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: LogBufferEulogizer.kt */
/* loaded from: classes.dex */
public final class LogBufferEulogizer {
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final Files files;
    @NotNull
    private final Path logPath;
    private final long maxLogAgeToDump;
    private final long minWriteGap;
    @NotNull
    private final SystemClock systemClock;

    public LogBufferEulogizer(@NotNull DumpManager dumpManager, @NotNull SystemClock systemClock, @NotNull Files files, @NotNull Path logPath, long j, long j2) {
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(files, "files");
        Intrinsics.checkNotNullParameter(logPath, "logPath");
        this.dumpManager = dumpManager;
        this.systemClock = systemClock;
        this.files = files;
        this.logPath = logPath;
        this.minWriteGap = j;
        this.maxLogAgeToDump = j2;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public LogBufferEulogizer(@NotNull Context context, @NotNull DumpManager dumpManager, @NotNull SystemClock systemClock, @NotNull Files files) {
        this(dumpManager, systemClock, files, r5, r6, r8);
        long j;
        long j2;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(files, "files");
        Path path = Paths.get(context.getFilesDir().toPath().toString(), "log_buffers.txt");
        Intrinsics.checkNotNullExpressionValue(path, "get(context.filesDir.toPath().toString(), \"log_buffers.txt\")");
        j = LogBufferEulogizerKt.MIN_WRITE_GAP;
        j2 = LogBufferEulogizerKt.MAX_AGE_TO_DUMP;
    }

    @NotNull
    public final <T extends Exception> T record(@NotNull T reason) {
        SimpleDateFormat simpleDateFormat;
        Intrinsics.checkNotNullParameter(reason, "reason");
        long uptimeMillis = this.systemClock.uptimeMillis();
        Log.i("BufferEulogizer", "Performing emergency dump of log buffers");
        long millisSinceLastWrite = getMillisSinceLastWrite(this.logPath);
        if (millisSinceLastWrite < this.minWriteGap) {
            Log.w("BufferEulogizer", "Cannot dump logs, last write was only " + millisSinceLastWrite + " ms ago");
            return reason;
        }
        long j = 0;
        try {
            BufferedWriter newBufferedWriter = this.files.newBufferedWriter(this.logPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            PrintWriter printWriter = new PrintWriter(newBufferedWriter);
            simpleDateFormat = LogBufferEulogizerKt.DATE_FORMAT;
            printWriter.println(simpleDateFormat.format(Long.valueOf(this.systemClock.currentTimeMillis())));
            printWriter.println();
            printWriter.println("Dump triggered by exception:");
            reason.printStackTrace(printWriter);
            this.dumpManager.dumpBuffers(printWriter, 0);
            j = this.systemClock.uptimeMillis() - uptimeMillis;
            printWriter.println();
            printWriter.println("Buffer eulogy took " + j + "ms");
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(newBufferedWriter, null);
        } catch (Exception e) {
            Log.e("BufferEulogizer", "Exception while attempting to dump buffers, bailing", e);
        }
        Log.i("BufferEulogizer", "Buffer eulogy took " + j + "ms");
        return reason;
    }

    public final void readEulogyIfPresent(@NotNull final PrintWriter pw) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        try {
            long millisSinceLastWrite = getMillisSinceLastWrite(this.logPath);
            if (millisSinceLastWrite > this.maxLogAgeToDump) {
                Log.i("BufferEulogizer", "Not eulogizing buffers; they are " + TimeUnit.HOURS.convert(millisSinceLastWrite, TimeUnit.MILLISECONDS) + " hours old");
                return;
            }
            Stream<String> lines = this.files.lines(this.logPath);
            pw.println();
            pw.println();
            pw.println("=============== BUFFERS FROM MOST RECENT CRASH ===============");
            lines.forEach(new Consumer<String>() { // from class: com.android.systemui.dump.LogBufferEulogizer$readEulogyIfPresent$1$1
                @Override // java.util.function.Consumer
                public final void accept(String str) {
                    pw.println(str);
                }
            });
            Unit unit = Unit.INSTANCE;
            AutoCloseableKt.closeFinally(lines, null);
        } catch (IOException unused) {
        } catch (UncheckedIOException e) {
            Log.e("BufferEulogizer", "UncheckedIOException while dumping the core", e);
        }
    }

    private final long getMillisSinceLastWrite(Path path) {
        BasicFileAttributes basicFileAttributes;
        FileTime fileTime = null;
        try {
            basicFileAttributes = this.files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
        } catch (IOException unused) {
            basicFileAttributes = null;
        }
        long currentTimeMillis = this.systemClock.currentTimeMillis();
        if (basicFileAttributes != null) {
            fileTime = basicFileAttributes.lastModifiedTime();
        }
        return currentTimeMillis - (fileTime == null ? 0L : fileTime.toMillis());
    }
}
