package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.SuspendFunction;
import kotlin.jvm.internal.Ref;

@Metadata(mo14007d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H@¢\u0006\u0004\b\u0004\u0010\u0005"}, mo14008d2 = {"<anonymous>", "", "T", "it", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, mo14009k = 3, mo14010mv = {1, 6, 0}, mo14012xi = 48)
/* compiled from: Reduce.kt */
final class FlowKt__ReduceKt$lastOrNull$2<T> implements FlowCollector, SuspendFunction {
    final /* synthetic */ Ref.ObjectRef<T> $result;

    FlowKt__ReduceKt$lastOrNull$2(Ref.ObjectRef<T> objectRef) {
        this.$result = objectRef;
    }

    public final Object emit(T t, Continuation<? super Unit> continuation) {
        this.$result.element = t;
        return Unit.INSTANCE;
    }
}