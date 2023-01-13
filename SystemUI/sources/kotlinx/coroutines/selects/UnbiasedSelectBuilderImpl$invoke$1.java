package kotlinx.coroutines.selects;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002 \u0000H\n"}, mo65043d2 = {"<anonymous>", "", "R"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: SelectUnbiased.kt */
final class UnbiasedSelectBuilderImpl$invoke$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Function1<Continuation<? super R>, Object> $block;
    final /* synthetic */ SelectClause0 $this_invoke;
    final /* synthetic */ UnbiasedSelectBuilderImpl<R> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UnbiasedSelectBuilderImpl$invoke$1(SelectClause0 selectClause0, UnbiasedSelectBuilderImpl<? super R> unbiasedSelectBuilderImpl, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        super(0);
        this.$this_invoke = selectClause0;
        this.this$0 = unbiasedSelectBuilderImpl;
        this.$block = function1;
    }

    public final void invoke() {
        this.$this_invoke.registerSelectClause0(this.this$0.getInstance(), this.$block);
    }
}
