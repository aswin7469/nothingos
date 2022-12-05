package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: CompletedExceptionally.kt */
/* loaded from: classes2.dex */
public final class CompletedExceptionallyKt {
    @Nullable
    public static final <T> Object toState(@NotNull Object obj) {
        if (Result.m1939isSuccessimpl(obj)) {
            ResultKt.throwOnFailure(obj);
            return obj;
        }
        Throwable m1936exceptionOrNullimpl = Result.m1936exceptionOrNullimpl(obj);
        if (m1936exceptionOrNullimpl == null) {
            Intrinsics.throwNpe();
        }
        return new CompletedExceptionally(m1936exceptionOrNullimpl, false, 2, null);
    }
}
