package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;

@Metadata(mo65042d1 = {"\u0000\b\n\u0002\b\u0004\n\u0002\u0018\u0002\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001\"\u0006\b\u0001\u0010\u0002 \u00012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H@"}, mo65043d2 = {"<anonymous>", "R", "E", "it", "Lkotlinx/coroutines/channels/ChannelResult;"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.channels.ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1", mo65297f = "Channel.kt", mo65298i = {}, mo65299l = {370}, mo65300m = "invokeSuspend", mo65301n = {}, mo65302s = {})
/* compiled from: Channel.kt */
final class ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1 extends SuspendLambda implements Function2<ChannelResult<? extends E>, Continuation<? super R>, Object> {
    final /* synthetic */ Function2<E, Continuation<? super R>, Object> $block;
    /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1(Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2, Continuation<? super ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1> continuation) {
        super(2, continuation);
        this.$block = function2;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1 receiveChannel$onReceiveOrNull$1$registerSelectClause1$1 = new ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1(this.$block, continuation);
        receiveChannel$onReceiveOrNull$1$registerSelectClause1$1.L$0 = obj;
        return receiveChannel$onReceiveOrNull$1$registerSelectClause1$1;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return m5474invokeWpGqRn0(((ChannelResult) obj).m5465unboximpl(), (Continuation) obj2);
    }

    /* renamed from: invoke-WpGqRn0  reason: not valid java name */
    public final Object m5474invokeWpGqRn0(Object obj, Continuation<? super R> continuation) {
        return ((ReceiveChannel$onReceiveOrNull$1$registerSelectClause1$1) create(ChannelResult.m5453boximpl(obj), continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Object r4 = ((ChannelResult) this.L$0).m5465unboximpl();
            Throwable r1 = ChannelResult.m5457exceptionOrNullimpl(r4);
            if (r1 == null) {
                Function2<E, Continuation<? super R>, Object> function2 = this.$block;
                Object r42 = ChannelResult.m5458getOrNullimpl(r4);
                this.label = 1;
                obj = function2.invoke(r42, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                throw r1;
            }
        } else if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return obj;
    }
}
