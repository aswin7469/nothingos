package kotlin.coroutines;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: Continuation.kt */
/* loaded from: classes2.dex */
public final class ContinuationKt {
    public static final <R, T> void startCoroutine(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> startCoroutine, R r, @NotNull Continuation<? super T> completion) {
        Continuation<Unit> createCoroutineUnintercepted;
        Continuation intercepted;
        Intrinsics.checkNotNullParameter(startCoroutine, "$this$startCoroutine");
        Intrinsics.checkNotNullParameter(completion, "completion");
        createCoroutineUnintercepted = IntrinsicsKt__IntrinsicsJvmKt.createCoroutineUnintercepted(startCoroutine, r, completion);
        intercepted = IntrinsicsKt__IntrinsicsJvmKt.intercepted(createCoroutineUnintercepted);
        Unit unit = Unit.INSTANCE;
        Result.Companion companion = Result.Companion;
        intercepted.resumeWith(Result.m1934constructorimpl(unit));
    }
}
