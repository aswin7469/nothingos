package kotlinx.coroutines.intrinsics;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.DispatchedKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: Cancellable.kt */
/* loaded from: classes2.dex */
public final class CancellableKt {
    public static final <R, T> void startCoroutineCancellable(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> startCoroutineCancellable, R r, @NotNull Continuation<? super T> completion) {
        Continuation<Unit> createCoroutineUnintercepted;
        Continuation intercepted;
        Intrinsics.checkParameterIsNotNull(startCoroutineCancellable, "$this$startCoroutineCancellable");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        try {
            createCoroutineUnintercepted = IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted(startCoroutineCancellable, r, completion);
            intercepted = IntrinsicsKt__IntrinsicsJvmKt.intercepted(createCoroutineUnintercepted);
            DispatchedKt.resumeCancellable(intercepted, Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion = Result.Companion;
            completion.resumeWith(Result.m1934constructorimpl(ResultKt.createFailure(th)));
        }
    }
}
