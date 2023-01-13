package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\u00020\u0004H@"}, mo65043d2 = {"<anonymous>", "Lkotlinx/coroutines/channels/ChannelResult;", "", "E", "Lkotlinx/coroutines/CoroutineScope;"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.channels.ChannelsKt__ChannelsKt$trySendBlocking$2", mo65297f = "Channels.kt", mo65298i = {}, mo65299l = {92}, mo65300m = "invokeSuspend", mo65301n = {}, mo65302s = {})
/* compiled from: Channels.kt */
final class ChannelsKt__ChannelsKt$trySendBlocking$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super ChannelResult<? extends Unit>>, Object> {
    final /* synthetic */ E $element;
    final /* synthetic */ SendChannel<E> $this_trySendBlocking;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ChannelsKt__ChannelsKt$trySendBlocking$2(SendChannel<? super E> sendChannel, E e, Continuation<? super ChannelsKt__ChannelsKt$trySendBlocking$2> continuation) {
        super(2, continuation);
        this.$this_trySendBlocking = sendChannel;
        this.$element = e;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ChannelsKt__ChannelsKt$trySendBlocking$2 channelsKt__ChannelsKt$trySendBlocking$2 = new ChannelsKt__ChannelsKt$trySendBlocking$2(this.$this_trySendBlocking, this.$element, continuation);
        channelsKt__ChannelsKt$trySendBlocking$2.L$0 = obj;
        return channelsKt__ChannelsKt$trySendBlocking$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super ChannelResult<Unit>> continuation) {
        return ((ChannelsKt__ChannelsKt$trySendBlocking$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        Object obj2;
        Object obj3;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            CoroutineScope coroutineScope = (CoroutineScope) this.L$0;
            SendChannel<E> sendChannel = this.$this_trySendBlocking;
            E e = this.$element;
            Result.Companion companion = Result.Companion;
            this.label = 1;
            if (sendChannel.send(e, this) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj2 = Result.m3954constructorimpl(ResultKt.createFailure(th));
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        obj2 = Result.m3954constructorimpl(Unit.INSTANCE);
        if (Result.m3961isSuccessimpl(obj2)) {
            obj3 = ChannelResult.Companion.m5468successJP2dKIU(Unit.INSTANCE);
        } else {
            obj3 = ChannelResult.Companion.m5466closedJP2dKIU(Result.m3957exceptionOrNullimpl(obj2));
        }
        return ChannelResult.m5453boximpl(obj3);
    }
}
