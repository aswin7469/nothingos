package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;

@Metadata(mo64986d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H@"}, mo64987d2 = {"<anonymous>", "", "it", "Lkotlinx/coroutines/flow/SharingCommand;"}, mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "kotlinx.coroutines.flow.StartedWhileSubscribed$command$2", mo65241f = "SharingStarted.kt", mo65242i = {}, mo65243l = {}, mo65244m = "invokeSuspend", mo65245n = {}, mo65246s = {})
/* compiled from: SharingStarted.kt */
final class StartedWhileSubscribed$command$2 extends SuspendLambda implements Function2<SharingCommand, Continuation<? super Boolean>, Object> {
    /* synthetic */ Object L$0;
    int label;

    StartedWhileSubscribed$command$2(Continuation<? super StartedWhileSubscribed$command$2> continuation) {
        super(2, continuation);
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        StartedWhileSubscribed$command$2 startedWhileSubscribed$command$2 = new StartedWhileSubscribed$command$2(continuation);
        startedWhileSubscribed$command$2.L$0 = obj;
        return startedWhileSubscribed$command$2;
    }

    public final Object invoke(SharingCommand sharingCommand, Continuation<? super Boolean> continuation) {
        return ((StartedWhileSubscribed$command$2) create(sharingCommand, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            return Boxing.boxBoolean(((SharingCommand) this.L$0) != SharingCommand.START);
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
