package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Yield.kt */
/* loaded from: classes2.dex */
public final class YieldKt {
    @Nullable
    public static final Object yield(@NotNull Continuation<? super Unit> continuation) {
        Continuation intercepted;
        Object obj;
        Object coroutine_suspended;
        CoroutineContext context = continuation.getContext();
        checkCompletion(context);
        intercepted = IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation);
        if (!(intercepted instanceof DispatchedContinuation)) {
            intercepted = null;
        }
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) intercepted;
        if (dispatchedContinuation == null) {
            obj = Unit.INSTANCE;
        } else if (!dispatchedContinuation.dispatcher.isDispatchNeeded(context)) {
            obj = DispatchedKt.yieldUndispatched(dispatchedContinuation) ? IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED() : Unit.INSTANCE;
        } else {
            dispatchedContinuation.dispatchYield$kotlinx_coroutines_core(Unit.INSTANCE);
            obj = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (obj == coroutine_suspended) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return obj;
    }

    public static final void checkCompletion(@NotNull CoroutineContext checkCompletion) {
        Intrinsics.checkParameterIsNotNull(checkCompletion, "$this$checkCompletion");
        Job job = (Job) checkCompletion.get(Job.Key);
        if (job == null || job.isActive()) {
            return;
        }
        throw job.getCancellationException();
    }
}
