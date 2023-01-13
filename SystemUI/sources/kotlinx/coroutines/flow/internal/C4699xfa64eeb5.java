package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.FlowCollector;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0000"}, mo65043d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* renamed from: kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$invokeSuspend$$inlined$collect$1 */
/* compiled from: Collect.kt */
public final class C4699xfa64eeb5 implements FlowCollector<T> {
    final /* synthetic */ CoroutineScope $$this$flowScope$inlined;
    final /* synthetic */ FlowCollector $collector$inlined;
    final /* synthetic */ Ref.ObjectRef $previousFlow$inlined;
    final /* synthetic */ ChannelFlowTransformLatest this$0;

    public C4699xfa64eeb5(Ref.ObjectRef objectRef, CoroutineScope coroutineScope, ChannelFlowTransformLatest channelFlowTransformLatest, FlowCollector flowCollector) {
        this.$previousFlow$inlined = objectRef;
        this.$$this$flowScope$inlined = coroutineScope;
        this.this$0 = channelFlowTransformLatest;
        this.$collector$inlined = flowCollector;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object emit(T r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) {
        /*
            r6 = this;
            boolean r0 = r8 instanceof kotlinx.coroutines.flow.internal.C4699xfa64eeb5.C47001
            if (r0 == 0) goto L_0x0014
            r0 = r8
            kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$invokeSuspend$$inlined$collect$1$1 r0 = (kotlinx.coroutines.flow.internal.C4699xfa64eeb5.C47001) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0014
            int r8 = r0.label
            int r8 = r8 - r2
            r0.label = r8
            goto L_0x0019
        L_0x0014:
            kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$invokeSuspend$$inlined$collect$1$1 r0 = new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$invokeSuspend$$inlined$collect$1$1
            r0.<init>(r6, r8)
        L_0x0019:
            java.lang.Object r8 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x003c
            if (r2 != r3) goto L_0x0034
            java.lang.Object r6 = r0.L$2
            kotlinx.coroutines.Job r6 = (kotlinx.coroutines.Job) r6
            java.lang.Object r7 = r0.L$1
            java.lang.Object r6 = r0.L$0
            kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$invokeSuspend$$inlined$collect$1 r6 = (kotlinx.coroutines.flow.internal.C4699xfa64eeb5) r6
            kotlin.ResultKt.throwOnFailure(r8)
            goto L_0x0064
        L_0x0034:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x003c:
            kotlin.ResultKt.throwOnFailure(r8)
            r8 = r0
            kotlin.coroutines.Continuation r8 = (kotlin.coroutines.Continuation) r8
            kotlin.jvm.internal.Ref$ObjectRef r8 = r6.$previousFlow$inlined
            T r8 = r8.element
            kotlinx.coroutines.Job r8 = (kotlinx.coroutines.Job) r8
            if (r8 != 0) goto L_0x004b
            goto L_0x0064
        L_0x004b:
            kotlinx.coroutines.flow.internal.ChildCancelledException r2 = new kotlinx.coroutines.flow.internal.ChildCancelledException
            r2.<init>()
            java.util.concurrent.CancellationException r2 = (java.util.concurrent.CancellationException) r2
            r8.cancel((java.util.concurrent.CancellationException) r2)
            r0.L$0 = r6
            r0.L$1 = r7
            r0.L$2 = r8
            r0.label = r3
            java.lang.Object r8 = r8.join(r0)
            if (r8 != r1) goto L_0x0064
            return r1
        L_0x0064:
            kotlin.jvm.internal.Ref$ObjectRef r8 = r6.$previousFlow$inlined
            kotlinx.coroutines.CoroutineScope r0 = r6.$$this$flowScope$inlined
            r1 = 0
            kotlinx.coroutines.CoroutineStart r2 = kotlinx.coroutines.CoroutineStart.UNDISPATCHED
            kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2 r3 = new kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest$flowCollect$3$1$2
            kotlinx.coroutines.flow.internal.ChannelFlowTransformLatest r4 = r6.this$0
            kotlinx.coroutines.flow.FlowCollector r6 = r6.$collector$inlined
            r5 = 0
            r3.<init>(r4, r6, r7, r5)
            kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3
            r4 = 1
            kotlinx.coroutines.Job r6 = kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(r0, r1, r2, r3, r4, r5)
            r8.element = r6
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.C4699xfa64eeb5.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
