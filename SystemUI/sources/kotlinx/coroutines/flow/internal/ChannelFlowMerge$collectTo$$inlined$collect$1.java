package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.channels.ProducerScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.sync.Semaphore;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0000"}, mo64987d2 = {"kotlinx/coroutines/flow/FlowKt__CollectKt$collect$3", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* compiled from: Collect.kt */
public final class ChannelFlowMerge$collectTo$$inlined$collect$1 implements FlowCollector<Flow<? extends T>> {
    final /* synthetic */ SendingCollector $collector$inlined;
    final /* synthetic */ Job $job$inlined;
    final /* synthetic */ ProducerScope $scope$inlined;
    final /* synthetic */ Semaphore $semaphore$inlined;

    public ChannelFlowMerge$collectTo$$inlined$collect$1(Job job, Semaphore semaphore, ProducerScope producerScope, SendingCollector sendingCollector) {
        this.$job$inlined = job;
        this.$semaphore$inlined = semaphore;
        this.$scope$inlined = producerScope;
        this.$collector$inlined = sendingCollector;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x003d  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0024  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object emit(kotlinx.coroutines.flow.Flow<? extends T> r8, kotlin.coroutines.Continuation<? super kotlin.Unit> r9) {
        /*
            r7 = this;
            boolean r0 = r9 instanceof kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1.C46861
            if (r0 == 0) goto L_0x0014
            r0 = r9
            kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1$1 r0 = (kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1.C46861) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0014
            int r9 = r0.label
            int r9 = r9 - r2
            r0.label = r9
            goto L_0x0019
        L_0x0014:
            kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1$1 r0 = new kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1$1
            r0.<init>(r7, r9)
        L_0x0019:
            java.lang.Object r9 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x003d
            if (r2 != r3) goto L_0x0035
            java.lang.Object r7 = r0.L$1
            kotlinx.coroutines.flow.Flow r7 = (kotlinx.coroutines.flow.Flow) r7
            java.lang.Object r8 = r0.L$0
            kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1 r8 = (kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1) r8
            kotlin.ResultKt.throwOnFailure(r9)
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x005c
        L_0x0035:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>((java.lang.String) r8)
            throw r7
        L_0x003d:
            kotlin.ResultKt.throwOnFailure(r9)
            r9 = r0
            kotlin.coroutines.Continuation r9 = (kotlin.coroutines.Continuation) r9
            kotlinx.coroutines.flow.Flow r8 = (kotlinx.coroutines.flow.Flow) r8
            kotlinx.coroutines.Job r9 = r7.$job$inlined
            if (r9 != 0) goto L_0x004a
            goto L_0x004d
        L_0x004a:
            kotlinx.coroutines.JobKt.ensureActive((kotlinx.coroutines.Job) r9)
        L_0x004d:
            kotlinx.coroutines.sync.Semaphore r9 = r7.$semaphore$inlined
            r0.L$0 = r7
            r0.L$1 = r8
            r0.label = r3
            java.lang.Object r9 = r9.acquire(r0)
            if (r9 != r1) goto L_0x005c
            return r1
        L_0x005c:
            kotlinx.coroutines.channels.ProducerScope r9 = r7.$scope$inlined
            r0 = r9
            kotlinx.coroutines.CoroutineScope r0 = (kotlinx.coroutines.CoroutineScope) r0
            r1 = 0
            r2 = 0
            kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$1 r9 = new kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$2$1
            kotlinx.coroutines.flow.internal.SendingCollector r3 = r7.$collector$inlined
            kotlinx.coroutines.sync.Semaphore r7 = r7.$semaphore$inlined
            r4 = 0
            r9.<init>(r8, r3, r7, r4)
            r3 = r9
            kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3
            r4 = 3
            r5 = 0
            kotlinx.coroutines.Job unused = kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(r0, r1, r2, r3, r4, r5)
            kotlin.Unit r7 = kotlin.Unit.INSTANCE
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.ChannelFlowMerge$collectTo$$inlined$collect$1.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
