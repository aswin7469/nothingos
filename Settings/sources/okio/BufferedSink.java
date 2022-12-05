package okio;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import org.jetbrains.annotations.NotNull;
/* compiled from: BufferedSink.kt */
/* loaded from: classes2.dex */
public interface BufferedSink extends Closeable, Flushable, WritableByteChannel {
    @NotNull
    /* renamed from: writeByte */
    BufferedSink mo964writeByte(int i) throws IOException;

    @NotNull
    /* renamed from: writeUtf8 */
    BufferedSink mo965writeUtf8(@NotNull String str) throws IOException;

    @NotNull
    /* renamed from: writeUtf8 */
    BufferedSink mo966writeUtf8(@NotNull String str, int i, int i2) throws IOException;
}
