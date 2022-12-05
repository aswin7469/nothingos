package kotlin.jdk7;

import kotlin.ExceptionsKt__ExceptionsKt;
import org.jetbrains.annotations.Nullable;
/* compiled from: AutoCloseable.kt */
/* loaded from: classes2.dex */
public final class AutoCloseableKt {
    public static final void closeFinally(@Nullable AutoCloseable autoCloseable, @Nullable Throwable th) {
        if (autoCloseable == null) {
            return;
        }
        if (th == null) {
            autoCloseable.close();
            return;
        }
        try {
            autoCloseable.close();
        } catch (Throwable th2) {
            ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
        }
    }
}
