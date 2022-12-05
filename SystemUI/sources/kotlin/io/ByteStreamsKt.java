package kotlin.io;

import java.io.InputStream;
import java.io.OutputStream;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: IOStreams.kt */
/* loaded from: classes2.dex */
public final class ByteStreamsKt {
    public static final long copyTo(@NotNull InputStream copyTo, @NotNull OutputStream out, int i) {
        Intrinsics.checkNotNullParameter(copyTo, "$this$copyTo");
        Intrinsics.checkNotNullParameter(out, "out");
        byte[] bArr = new byte[i];
        int read = copyTo.read(bArr);
        long j = 0;
        while (read >= 0) {
            out.write(bArr, 0, read);
            j += read;
            read = copyTo.read(bArr);
        }
        return j;
    }
}
