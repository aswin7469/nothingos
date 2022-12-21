package com.android.p019wm.shell.bubbles;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.YieldKt;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@"}, mo64987d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@DebugMetadata(mo65240c = "com.android.wm.shell.bubbles.BubbleDataRepository$persistToDisk$1", mo65241f = "BubbleDataRepository.kt", mo65242i = {}, mo65243l = {127, 129}, mo65244m = "invokeSuspend", mo65245n = {}, mo65246s = {})
/* renamed from: com.android.wm.shell.bubbles.BubbleDataRepository$persistToDisk$1 */
/* compiled from: BubbleDataRepository.kt */
final class BubbleDataRepository$persistToDisk$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Job $prev;
    int label;
    final /* synthetic */ BubbleDataRepository this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BubbleDataRepository$persistToDisk$1(Job job, BubbleDataRepository bubbleDataRepository, Continuation<? super BubbleDataRepository$persistToDisk$1> continuation) {
        super(2, continuation);
        this.$prev = job;
        this.this$0 = bubbleDataRepository;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new BubbleDataRepository$persistToDisk$1(this.$prev, this.this$0, continuation);
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((BubbleDataRepository$persistToDisk$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Job job = this.$prev;
            if (job != null) {
                this.label = 1;
                if (JobKt.cancelAndJoin(job, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            ResultKt.throwOnFailure(obj);
            this.this$0.persistentRepository.persistsToDisk(this.this$0.volatileRepository.getBubbles());
            return Unit.INSTANCE;
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        this.label = 2;
        if (YieldKt.yield(this) == coroutine_suspended) {
            return coroutine_suspended;
        }
        this.this$0.persistentRepository.persistsToDisk(this.this$0.volatileRepository.getBubbles());
        return Unit.INSTANCE;
    }
}
