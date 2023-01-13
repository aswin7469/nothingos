package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlinx.coroutines.CoroutineScope;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0005H@"}, mo65043d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/CoroutineScope;", "downstream", "Lkotlinx/coroutines/flow/FlowCollector;"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1", mo65297f = "Delay.kt", mo65298i = {}, mo65299l = {224, 358}, mo65300m = "invokeSuspend", mo65301n = {}, mo65302s = {})
/* compiled from: Delay.kt */
final class FlowKt__DelayKt$debounceInternal$1 extends SuspendLambda implements Function3<CoroutineScope, FlowCollector<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Flow<T> $this_debounceInternal;
    final /* synthetic */ Function1<T, Long> $timeoutMillisSelector;
    private /* synthetic */ Object L$0;
    /* synthetic */ Object L$1;
    Object L$2;
    Object L$3;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FlowKt__DelayKt$debounceInternal$1(Function1<? super T, Long> function1, Flow<? extends T> flow, Continuation<? super FlowKt__DelayKt$debounceInternal$1> continuation) {
        super(3, continuation);
        this.$timeoutMillisSelector = function1;
        this.$this_debounceInternal = flow;
    }

    public final Object invoke(CoroutineScope coroutineScope, FlowCollector<? super T> flowCollector, Continuation<? super Unit> continuation) {
        FlowKt__DelayKt$debounceInternal$1 flowKt__DelayKt$debounceInternal$1 = new FlowKt__DelayKt$debounceInternal$1(this.$timeoutMillisSelector, this.$this_debounceInternal, continuation);
        flowKt__DelayKt$debounceInternal$1.L$0 = coroutineScope;
        flowKt__DelayKt$debounceInternal$1.L$1 = flowCollector;
        return flowKt__DelayKt$debounceInternal$1.invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:7|29|30|33|(4:35|(1:40)(1:39)|41|(2:43|44))|45|46|47|(1:49)|50|53|(1:55)|(1:57)(2:58|59)|57) */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0130, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0131, code lost:
        r13.handleBuilderException(r0);
     */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00dd  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0115 A[Catch:{ all -> 0x0130 }] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x013e  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0144  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r18) {
        /*
            r17 = this;
            r0 = r17
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r4 = 2
            r5 = 0
            r7 = 1
            r8 = 0
            if (r2 == 0) goto L_0x0048
            if (r2 == r7) goto L_0x0033
            if (r2 != r4) goto L_0x002b
            java.lang.Object r2 = r0.L$3
            kotlin.jvm.internal.Ref$LongRef r2 = (kotlin.jvm.internal.Ref.LongRef) r2
            java.lang.Object r2 = r0.L$2
            kotlin.jvm.internal.Ref$ObjectRef r2 = (kotlin.jvm.internal.Ref.ObjectRef) r2
            java.lang.Object r9 = r0.L$1
            kotlinx.coroutines.channels.ReceiveChannel r9 = (kotlinx.coroutines.channels.ReceiveChannel) r9
            java.lang.Object r10 = r0.L$0
            kotlinx.coroutines.flow.FlowCollector r10 = (kotlinx.coroutines.flow.FlowCollector) r10
            kotlin.ResultKt.throwOnFailure(r18)
            r11 = r10
            r10 = r9
            r9 = r2
            goto L_0x0146
        L_0x002b:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0033:
            java.lang.Object r2 = r0.L$3
            kotlin.jvm.internal.Ref$LongRef r2 = (kotlin.jvm.internal.Ref.LongRef) r2
            java.lang.Object r9 = r0.L$2
            kotlin.jvm.internal.Ref$ObjectRef r9 = (kotlin.jvm.internal.Ref.ObjectRef) r9
            java.lang.Object r10 = r0.L$1
            kotlinx.coroutines.channels.ReceiveChannel r10 = (kotlinx.coroutines.channels.ReceiveChannel) r10
            java.lang.Object r11 = r0.L$0
            kotlinx.coroutines.flow.FlowCollector r11 = (kotlinx.coroutines.flow.FlowCollector) r11
            kotlin.ResultKt.throwOnFailure(r18)
            goto L_0x00c0
        L_0x0048:
            kotlin.ResultKt.throwOnFailure(r18)
            java.lang.Object r2 = r0.L$0
            r9 = r2
            kotlinx.coroutines.CoroutineScope r9 = (kotlinx.coroutines.CoroutineScope) r9
            java.lang.Object r2 = r0.L$1
            kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
            r10 = 0
            r11 = 0
            kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$values$1 r12 = new kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$values$1
            kotlinx.coroutines.flow.Flow<T> r13 = r0.$this_debounceInternal
            r12.<init>(r13, r8)
            kotlin.jvm.functions.Function2 r12 = (kotlin.jvm.functions.Function2) r12
            r13 = 3
            r14 = 0
            kotlinx.coroutines.channels.ReceiveChannel r9 = kotlinx.coroutines.channels.ProduceKt.produce$default(r9, r10, r11, r12, r13, r14)
            kotlin.jvm.internal.Ref$ObjectRef r10 = new kotlin.jvm.internal.Ref$ObjectRef
            r10.<init>()
            r11 = r2
            r16 = r10
            r10 = r9
            r9 = r16
        L_0x0070:
            T r2 = r9.element
            kotlinx.coroutines.internal.Symbol r12 = kotlinx.coroutines.flow.internal.NullSurrogateKt.DONE
            if (r2 == r12) goto L_0x0149
            kotlin.jvm.internal.Ref$LongRef r2 = new kotlin.jvm.internal.Ref$LongRef
            r2.<init>()
            T r12 = r9.element
            if (r12 == 0) goto L_0x00c2
            kotlin.jvm.functions.Function1<T, java.lang.Long> r12 = r0.$timeoutMillisSelector
            kotlinx.coroutines.internal.Symbol r13 = kotlinx.coroutines.flow.internal.NullSurrogateKt.NULL
            T r14 = r9.element
            if (r14 != r13) goto L_0x0088
            r14 = r8
        L_0x0088:
            java.lang.Object r12 = r12.invoke(r14)
            java.lang.Number r12 = (java.lang.Number) r12
            long r12 = r12.longValue()
            r2.element = r12
            long r12 = r2.element
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 < 0) goto L_0x009c
            r12 = r7
            goto L_0x009d
        L_0x009c:
            r12 = 0
        L_0x009d:
            if (r12 == 0) goto L_0x00c9
            long r12 = r2.element
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 != 0) goto L_0x00c2
            kotlinx.coroutines.internal.Symbol r12 = kotlinx.coroutines.flow.internal.NullSurrogateKt.NULL
            T r13 = r9.element
            if (r13 != r12) goto L_0x00ac
            r13 = r8
        L_0x00ac:
            r12 = r0
            kotlin.coroutines.Continuation r12 = (kotlin.coroutines.Continuation) r12
            r0.L$0 = r11
            r0.L$1 = r10
            r0.L$2 = r9
            r0.L$3 = r2
            r0.label = r7
            java.lang.Object r12 = r11.emit(r13, r12)
            if (r12 != r1) goto L_0x00c0
            return r1
        L_0x00c0:
            r9.element = r8
        L_0x00c2:
            r16 = r1
            r1 = r0
            r0 = r2
            r2 = r16
            goto L_0x00d7
        L_0x00c9:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Debounce timeout should not be negative"
            java.lang.String r1 = r1.toString()
            r0.<init>((java.lang.String) r1)
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            throw r0
        L_0x00d7:
            boolean r12 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r12 == 0) goto L_0x00fc
            T r12 = r9.element
            if (r12 == 0) goto L_0x00ea
            long r12 = r0.element
            int r12 = (r12 > r5 ? 1 : (r12 == r5 ? 0 : -1))
            if (r12 <= 0) goto L_0x00e8
            goto L_0x00ea
        L_0x00e8:
            r12 = 0
            goto L_0x00eb
        L_0x00ea:
            r12 = r7
        L_0x00eb:
            java.lang.Boolean r12 = kotlin.coroutines.jvm.internal.Boxing.boxBoolean(r12)
            boolean r12 = r12.booleanValue()
            if (r12 == 0) goto L_0x00f6
            goto L_0x00fc
        L_0x00f6:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L_0x00fc:
            r1.L$0 = r11
            r1.L$1 = r10
            r1.L$2 = r9
            r1.L$3 = r0
            r1.label = r4
            r12 = r1
            kotlin.coroutines.Continuation r12 = (kotlin.coroutines.Continuation) r12
            kotlinx.coroutines.selects.SelectBuilderImpl r13 = new kotlinx.coroutines.selects.SelectBuilderImpl
            r13.<init>(r12)
            r14 = r13
            kotlinx.coroutines.selects.SelectBuilder r14 = (kotlinx.coroutines.selects.SelectBuilder) r14     // Catch:{ all -> 0x0130 }
            T r15 = r9.element     // Catch:{ all -> 0x0130 }
            if (r15 == 0) goto L_0x0121
            long r3 = r0.element     // Catch:{ all -> 0x0130 }
            kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1 r0 = new kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$1     // Catch:{ all -> 0x0130 }
            r0.<init>(r11, r9, r8)     // Catch:{ all -> 0x0130 }
            kotlin.jvm.functions.Function1 r0 = (kotlin.jvm.functions.Function1) r0     // Catch:{ all -> 0x0130 }
            r14.onTimeout(r3, r0)     // Catch:{ all -> 0x0130 }
        L_0x0121:
            kotlinx.coroutines.selects.SelectClause1 r0 = r10.getOnReceiveCatching()     // Catch:{ all -> 0x0130 }
            kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$2 r3 = new kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1$3$2     // Catch:{ all -> 0x0130 }
            r3.<init>(r9, r11, r8)     // Catch:{ all -> 0x0130 }
            kotlin.jvm.functions.Function2 r3 = (kotlin.jvm.functions.Function2) r3     // Catch:{ all -> 0x0130 }
            r14.invoke(r0, r3)     // Catch:{ all -> 0x0130 }
            goto L_0x0134
        L_0x0130:
            r0 = move-exception
            r13.handleBuilderException(r0)
        L_0x0134:
            java.lang.Object r0 = r13.getResult()
            java.lang.Object r3 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            if (r0 != r3) goto L_0x0141
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(r12)
        L_0x0141:
            if (r0 != r2) goto L_0x0144
            return r2
        L_0x0144:
            r0 = r1
            r1 = r2
        L_0x0146:
            r4 = 2
            goto L_0x0070
        L_0x0149:
            kotlin.Unit r0 = kotlin.Unit.INSTANCE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__DelayKt$debounceInternal$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
