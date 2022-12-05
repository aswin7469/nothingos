package kotlin.coroutines.intrinsics;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class IntrinsicsKt extends IntrinsicsKt__IntrinsicsKt {
    @NotNull
    public static /* bridge */ /* synthetic */ <R, T> Continuation<Unit> createCoroutineUnintercepted(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2, R r, @NotNull Continuation<? super T> continuation) {
        return IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted(function2, r, continuation);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ Object getCOROUTINE_SUSPENDED() {
        return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
    }
}
