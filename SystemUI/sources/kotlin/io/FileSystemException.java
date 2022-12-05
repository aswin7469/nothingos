package kotlin.io;

import java.io.File;
import java.io.IOException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Exceptions.kt */
/* loaded from: classes2.dex */
public class FileSystemException extends IOException {
    @NotNull
    private final File file;
    @Nullable
    private final File other;
    @Nullable
    private final String reason;

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public FileSystemException(@NotNull File file, @Nullable File file2, @Nullable String str) {
        super(r0);
        String constructMessage;
        Intrinsics.checkNotNullParameter(file, "file");
        constructMessage = ExceptionsKt.constructMessage(file, file2, str);
        this.file = file;
        this.other = file2;
        this.reason = str;
    }
}
