package kotlinx.coroutines.intrinsics;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlinx.coroutines.internal.ThreadContextKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: Undispatched.kt */
/* loaded from: classes2.dex */
public final class UndispatchedKt {
    public static final <R, T> void startCoroutineUndispatched(@NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> startCoroutineUndispatched, R r, @NotNull Continuation<? super T> completion) {
        Object coroutine_suspended;
        Intrinsics.checkParameterIsNotNull(startCoroutineUndispatched, "$this$startCoroutineUndispatched");
        Intrinsics.checkParameterIsNotNull(completion, "completion");
        Continuation probeCoroutineCreated = DebugProbesKt.probeCoroutineCreated(completion);
        try {
            CoroutineContext context = completion.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, null);
            Object mo1950invoke = ((Function2) TypeIntrinsics.beforeCheckcastToFunctionOfArity(startCoroutineUndispatched, 2)).mo1950invoke(r, probeCoroutineCreated);
            ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (mo1950invoke == coroutine_suspended) {
                return;
            }
            Result.Companion companion = Result.Companion;
            probeCoroutineCreated.resumeWith(Result.m1934constructorimpl(mo1950invoke));
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            probeCoroutineCreated.resumeWith(Result.m1934constructorimpl(ResultKt.createFailure(th)));
        }
    }
}
