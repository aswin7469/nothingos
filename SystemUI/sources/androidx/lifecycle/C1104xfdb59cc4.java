package androidx.lifecycle;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"androidx/lifecycle/WithLifecycleStateKt$suspendWithStateAtLeastUnchecked$2$observer$1", "Landroidx/lifecycle/LifecycleEventObserver;", "onStateChanged", "", "source", "Landroidx/lifecycle/LifecycleOwner;", "event", "Landroidx/lifecycle/Lifecycle$Event;", "lifecycle-runtime-ktx_release"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* renamed from: androidx.lifecycle.WithLifecycleStateKt$suspendWithStateAtLeastUnchecked$2$observer$1 */
/* compiled from: WithLifecycleState.kt */
public final class C1104xfdb59cc4 implements LifecycleEventObserver {
    final /* synthetic */ Function0<R> $block;
    final /* synthetic */ CancellableContinuation<R> $co;
    final /* synthetic */ Lifecycle.State $state;
    final /* synthetic */ Lifecycle $this_suspendWithStateAtLeastUnchecked;

    C1104xfdb59cc4(Lifecycle.State state, Lifecycle lifecycle, CancellableContinuation<? super R> cancellableContinuation, Function0<? extends R> function0) {
        this.$state = state;
        this.$this_suspendWithStateAtLeastUnchecked = lifecycle;
        this.$co = cancellableContinuation;
        this.$block = function0;
    }

    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        Object obj;
        Intrinsics.checkNotNullParameter(lifecycleOwner, "source");
        Intrinsics.checkNotNullParameter(event, NotificationCompat.CATEGORY_EVENT);
        if (event == Lifecycle.Event.upTo(this.$state)) {
            this.$this_suspendWithStateAtLeastUnchecked.removeObserver(this);
            CancellableContinuation<R> cancellableContinuation = this.$co;
            Function0<R> function0 = this.$block;
            try {
                Result.Companion companion = Result.Companion;
                obj = Result.m3954constructorimpl(function0.invoke());
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.m3954constructorimpl(ResultKt.createFailure(th));
            }
            cancellableContinuation.resumeWith(obj);
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            this.$this_suspendWithStateAtLeastUnchecked.removeObserver(this);
            Result.Companion companion3 = Result.Companion;
            this.$co.resumeWith(Result.m3954constructorimpl(ResultKt.createFailure(new LifecycleDestroyedException())));
        }
    }
}
