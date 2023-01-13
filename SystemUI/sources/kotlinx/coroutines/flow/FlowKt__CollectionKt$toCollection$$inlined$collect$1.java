package kotlinx.coroutines.flow;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0000"}, mo65043d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: Collect.kt */
public final class FlowKt__CollectionKt$toCollection$$inlined$collect$1 implements FlowCollector<T> {
    final /* synthetic */ Collection $destination$inlined;

    public FlowKt__CollectionKt$toCollection$$inlined$collect$1(Collection collection) {
        this.$destination$inlined = collection;
    }

    public Object emit(T t, Continuation<? super Unit> continuation) {
        this.$destination$inlined.add(t);
        return Unit.INSTANCE;
    }
}
