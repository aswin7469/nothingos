package kotlinx.coroutines.test;

import kotlin.Metadata;
import kotlin.Unit;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.test.TestCoroutineContext;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¨\u0006\u0002"}, mo64987d2 = {"<anonymous>", "", "kotlinx/coroutines/RunnableKt$Runnable$1"}, mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* renamed from: kotlinx.coroutines.test.TestCoroutineContext$Dispatcher$scheduleResumeAfterDelay$$inlined$Runnable$1 */
/* compiled from: Runnable.kt */
public final class C4694xe7664c50 implements Runnable {
    final /* synthetic */ CancellableContinuation $continuation$inlined;
    final /* synthetic */ TestCoroutineContext.Dispatcher this$0;

    public C4694xe7664c50(CancellableContinuation cancellableContinuation, TestCoroutineContext.Dispatcher dispatcher) {
        this.$continuation$inlined = cancellableContinuation;
        this.this$0 = dispatcher;
    }

    public final void run() {
        this.$continuation$inlined.resumeUndispatched(this.this$0, Unit.INSTANCE);
    }
}
