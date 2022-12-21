package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H@"}, mo64987d2 = {"<anonymous>", "", "R", "T", "Lkotlinx/coroutines/CoroutineScope;"}, mo64988k = 3, mo64989mv = {1, 5, 1}, mo64991xi = 48)
@DebugMetadata(mo65240c = "kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2", mo65241f = "Combine.kt", mo65242i = {0, 0, 0, 0}, mo65243l = {57, 79, 82}, mo65244m = "invokeSuspend", mo65245n = {"latestValues", "resultChannel", "lastReceivedEpoch", "remainingAbsentValues"}, mo65246s = {"L$0", "L$1", "L$2", "I$0"})
/* compiled from: Combine.kt */
final class CombineKt$combineInternal$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function0<T[]> $arrayFactory;
    final /* synthetic */ Flow<T>[] $flows;
    final /* synthetic */ FlowCollector<R> $this_combineInternal;
    final /* synthetic */ Function3<FlowCollector<? super R>, T[], Continuation<? super Unit>, Object> $transform;
    int I$0;
    int I$1;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    CombineKt$combineInternal$2(Flow<? extends T>[] flowArr, Function0<T[]> function0, Function3<? super FlowCollector<? super R>, ? super T[], ? super Continuation<? super Unit>, ? extends Object> function3, FlowCollector<? super R> flowCollector, Continuation<? super CombineKt$combineInternal$2> continuation) {
        super(2, continuation);
        this.$flows = flowArr;
        this.$arrayFactory = function0;
        this.$transform = function3;
        this.$this_combineInternal = flowCollector;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        CombineKt$combineInternal$2 combineKt$combineInternal$2 = new CombineKt$combineInternal$2(this.$flows, this.$arrayFactory, this.$transform, this.$this_combineInternal, continuation);
        combineKt$combineInternal$2.L$0 = obj;
        return combineKt$combineInternal$2;
    }

    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((CombineKt$combineInternal$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v3, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v4, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v5, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r21v6, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00ec A[LOOP:1: B:29:0x00ec->B:35:0x0110, LOOP_START, PHI: r6 r8 
      PHI: (r6v1 int) = (r6v0 int), (r6v2 int) binds: [B:26:0x00e7, B:35:0x0110] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r8v2 kotlin.collections.IndexedValue) = (r8v1 kotlin.collections.IndexedValue), (r8v15 kotlin.collections.IndexedValue) binds: [B:26:0x00e7, B:35:0x0110] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r26) {
        /*
            r25 = this;
            r0 = r25
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 3
            r4 = 2
            r5 = 1
            if (r2 == 0) goto L_0x0070
            if (r2 == r5) goto L_0x004c
            if (r2 == r4) goto L_0x0032
            if (r2 != r3) goto L_0x002a
            int r2 = r0.I$1
            int r6 = r0.I$0
            java.lang.Object r7 = r0.L$2
            byte[] r7 = (byte[]) r7
            java.lang.Object r8 = r0.L$1
            kotlinx.coroutines.channels.Channel r8 = (kotlinx.coroutines.channels.Channel) r8
            java.lang.Object r9 = r0.L$0
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            kotlin.ResultKt.throwOnFailure(r26)
            r21 = r2
            r11 = r3
            goto L_0x0047
        L_0x002a:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0032:
            int r2 = r0.I$1
            int r6 = r0.I$0
            java.lang.Object r7 = r0.L$2
            byte[] r7 = (byte[]) r7
            java.lang.Object r8 = r0.L$1
            kotlinx.coroutines.channels.Channel r8 = (kotlinx.coroutines.channels.Channel) r8
            java.lang.Object r9 = r0.L$0
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            kotlin.ResultKt.throwOnFailure(r26)
            r21 = r2
        L_0x0047:
            r2 = r7
            r3 = r8
            r13 = r9
            goto L_0x00c8
        L_0x004c:
            int r2 = r0.I$1
            int r6 = r0.I$0
            java.lang.Object r7 = r0.L$2
            byte[] r7 = (byte[]) r7
            java.lang.Object r8 = r0.L$1
            kotlinx.coroutines.channels.Channel r8 = (kotlinx.coroutines.channels.Channel) r8
            java.lang.Object r9 = r0.L$0
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            kotlin.ResultKt.throwOnFailure(r26)
            r10 = r26
            kotlinx.coroutines.channels.ChannelResult r10 = (kotlinx.coroutines.channels.ChannelResult) r10
            java.lang.Object r10 = r10.m5447unboximpl()
            r3 = r8
            r13 = r9
            r24 = r7
            r7 = r2
            r2 = r24
            goto L_0x00e1
        L_0x0070:
            kotlin.ResultKt.throwOnFailure(r26)
            java.lang.Object r2 = r0.L$0
            kotlinx.coroutines.CoroutineScope r2 = (kotlinx.coroutines.CoroutineScope) r2
            kotlinx.coroutines.flow.Flow<T>[] r6 = r0.$flows
            int r12 = r6.length
            if (r12 != 0) goto L_0x007f
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        L_0x007f:
            java.lang.Object[] r13 = new java.lang.Object[r12]
            kotlinx.coroutines.internal.Symbol r7 = kotlinx.coroutines.flow.internal.NullSurrogateKt.UNINITIALIZED
            r8 = 0
            r9 = 0
            r10 = 6
            r11 = 0
            r6 = r13
            kotlin.collections.ArraysKt.fill$default((java.lang.Object[]) r6, (java.lang.Object) r7, (int) r8, (int) r9, (int) r10, (java.lang.Object) r11)
            r6 = 6
            r7 = 0
            kotlinx.coroutines.channels.Channel r20 = kotlinx.coroutines.channels.ChannelKt.Channel$default(r12, r7, r7, r6, r7)
            java.util.concurrent.atomic.AtomicInteger r11 = new java.util.concurrent.atomic.AtomicInteger
            r11.<init>(r12)
            r21 = 0
            if (r12 <= 0) goto L_0x00c3
            r16 = r21
        L_0x009c:
            int r10 = r16 + 1
            r7 = 0
            r8 = 0
            kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1 r6 = new kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2$1
            kotlinx.coroutines.flow.Flow<T>[] r15 = r0.$flows
            r19 = 0
            r14 = r6
            r17 = r11
            r18 = r20
            r14.<init>(r15, r16, r17, r18, r19)
            r9 = r6
            kotlin.jvm.functions.Function2 r9 = (kotlin.jvm.functions.Function2) r9
            r14 = 3
            r15 = 0
            r6 = r2
            r3 = r10
            r10 = r14
            r14 = r11
            r11 = r15
            kotlinx.coroutines.Job unused = kotlinx.coroutines.BuildersKt__Builders_commonKt.launch$default(r6, r7, r8, r9, r10, r11)
            if (r3 < r12) goto L_0x00be
            goto L_0x00c3
        L_0x00be:
            r16 = r3
            r11 = r14
            r3 = 3
            goto L_0x009c
        L_0x00c3:
            byte[] r2 = new byte[r12]
            r6 = r12
            r3 = r20
        L_0x00c8:
            int r7 = r21 + 1
            byte r7 = (byte) r7
            r8 = r0
            kotlin.coroutines.Continuation r8 = (kotlin.coroutines.Continuation) r8
            r0.L$0 = r13
            r0.L$1 = r3
            r0.L$2 = r2
            r0.I$0 = r6
            r0.I$1 = r7
            r0.label = r5
            java.lang.Object r10 = r3.m5454receiveCatchingJP2dKIU(r8)
            if (r10 != r1) goto L_0x00e1
            return r1
        L_0x00e1:
            java.lang.Object r8 = kotlinx.coroutines.channels.ChannelResult.m5440getOrNullimpl(r10)
            kotlin.collections.IndexedValue r8 = (kotlin.collections.IndexedValue) r8
            if (r8 != 0) goto L_0x00ec
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        L_0x00ec:
            int r9 = r8.getIndex()
            r10 = r13[r9]
            java.lang.Object r8 = r8.getValue()
            r13[r9] = r8
            kotlinx.coroutines.internal.Symbol r8 = kotlinx.coroutines.flow.internal.NullSurrogateKt.UNINITIALIZED
            if (r10 != r8) goto L_0x00fe
            int r6 = r6 + -1
        L_0x00fe:
            byte r8 = r2[r9]
            if (r8 != r7) goto L_0x0103
            goto L_0x0112
        L_0x0103:
            byte r8 = (byte) r7
            r2[r9] = r8
            java.lang.Object r8 = r3.m5455tryReceivePtdJZtk()
            java.lang.Object r8 = kotlinx.coroutines.channels.ChannelResult.m5440getOrNullimpl(r8)
            kotlin.collections.IndexedValue r8 = (kotlin.collections.IndexedValue) r8
            if (r8 != 0) goto L_0x00ec
        L_0x0112:
            if (r6 != 0) goto L_0x015e
            kotlin.jvm.functions.Function0<T[]> r8 = r0.$arrayFactory
            java.lang.Object r8 = r8.invoke()
            java.lang.Object[] r8 = (java.lang.Object[]) r8
            if (r8 != 0) goto L_0x0135
            kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r8 = r0.$transform
            kotlinx.coroutines.flow.FlowCollector<R> r9 = r0.$this_combineInternal
            r0.L$0 = r13
            r0.L$1 = r3
            r0.L$2 = r2
            r0.I$0 = r6
            r0.I$1 = r7
            r0.label = r4
            java.lang.Object r8 = r8.invoke(r9, r13, r0)
            if (r8 != r1) goto L_0x015e
            return r1
        L_0x0135:
            r19 = 0
            r20 = 0
            r21 = 0
            r22 = 14
            r23 = 0
            r17 = r13
            r18 = r8
            kotlin.collections.ArraysKt.copyInto$default((java.lang.Object[]) r17, (java.lang.Object[]) r18, (int) r19, (int) r20, (int) r21, (int) r22, (java.lang.Object) r23)
            kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> r9 = r0.$transform
            kotlinx.coroutines.flow.FlowCollector<R> r10 = r0.$this_combineInternal
            r0.L$0 = r13
            r0.L$1 = r3
            r0.L$2 = r2
            r0.I$0 = r6
            r0.I$1 = r7
            r11 = 3
            r0.label = r11
            java.lang.Object r8 = r9.invoke(r10, r8, r0)
            if (r8 != r1) goto L_0x015e
            return r1
        L_0x015e:
            r21 = r7
            goto L_0x00c8
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.internal.CombineKt$combineInternal$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
