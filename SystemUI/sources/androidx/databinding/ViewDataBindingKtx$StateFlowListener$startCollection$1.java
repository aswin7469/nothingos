package androidx.databinding;

import androidx.databinding.ViewDataBindingKtx;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.RepeatOnLifecycleKt;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@"}, mo64987d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, mo64988k = 3, mo64989mv = {1, 7, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "androidx.databinding.ViewDataBindingKtx$StateFlowListener$startCollection$1", mo65241f = "ViewDataBindingKtx.kt", mo65242i = {}, mo65243l = {95}, mo65244m = "invokeSuspend", mo65245n = {}, mo65246s = {})
/* compiled from: ViewDataBindingKtx.kt */
final class ViewDataBindingKtx$StateFlowListener$startCollection$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow<Object> $flow;
    final /* synthetic */ LifecycleOwner $owner;
    int label;
    final /* synthetic */ ViewDataBindingKtx.StateFlowListener this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ViewDataBindingKtx$StateFlowListener$startCollection$1(LifecycleOwner lifecycleOwner, Flow<? extends Object> flow, ViewDataBindingKtx.StateFlowListener stateFlowListener, Continuation<? super ViewDataBindingKtx$StateFlowListener$startCollection$1> continuation) {
        super(2, continuation);
        this.$owner = lifecycleOwner;
        this.$flow = flow;
        this.this$0 = stateFlowListener;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new ViewDataBindingKtx$StateFlowListener$startCollection$1(this.$owner, this.$flow, this.this$0, continuation);
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((ViewDataBindingKtx$StateFlowListener$startCollection$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@"}, mo64987d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, mo64988k = 3, mo64989mv = {1, 7, 1}, mo64991xi = 48)
    @DebugMetadata(mo65240c = "androidx.databinding.ViewDataBindingKtx$StateFlowListener$startCollection$1$1", mo65241f = "ViewDataBindingKtx.kt", mo65242i = {}, mo65243l = {123}, mo65244m = "invokeSuspend", mo65245n = {}, mo65246s = {})
    /* renamed from: androidx.databinding.ViewDataBindingKtx$StateFlowListener$startCollection$1$1 */
    /* compiled from: ViewDataBindingKtx.kt */
    static final class C05851 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C05851(flow, stateFlowListener, continuation);
        }

        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C05851) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                this.label = 1;
                if (flow.collect(new C0584xf3deb20(stateFlowListener), this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else if (i == 1) {
                ResultKt.throwOnFailure(obj);
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Lifecycle lifecycle = this.$owner.getLifecycle();
            Intrinsics.checkNotNullExpressionValue(lifecycle, "owner.lifecycle");
            Lifecycle.State state = Lifecycle.State.STARTED;
            final Flow<Object> flow = this.$flow;
            final ViewDataBindingKtx.StateFlowListener stateFlowListener = this.this$0;
            this.label = 1;
            if (RepeatOnLifecycleKt.repeatOnLifecycle(lifecycle, state, (Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object>) new C05851((Continuation<? super C05851>) null), (Continuation<? super Unit>) this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }
}
