package androidx.lifecycle;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0006\n\u0000\n\u0002\u0010\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, mo65043d2 = {"<anonymous>", ""}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: DispatchQueue.kt */
final class DispatchQueue$dispatchAndEnqueue$1$1 implements Runnable {
    final /* synthetic */ Runnable $runnable;
    final /* synthetic */ DispatchQueue this$0;

    DispatchQueue$dispatchAndEnqueue$1$1(DispatchQueue dispatchQueue, Runnable runnable) {
        this.this$0 = dispatchQueue;
        this.$runnable = runnable;
    }

    public final void run() {
        this.this$0.enqueue(this.$runnable);
    }
}
