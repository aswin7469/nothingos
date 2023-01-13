package kotlinx.coroutines.flow;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.channels.BroadcastChannel;
import kotlinx.coroutines.channels.BroadcastKt;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.flow.internal.ChannelFlow;
import kotlinx.coroutines.flow.internal.ChannelFlowKt;

@Metadata(mo65042d1 = {"\u00006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0005\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0007\u001a0\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0003\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007\u001a\u001c\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n\u001a/\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\nH@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a9\u0010\u0010\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\n2\u0006\u0010\u0011\u001a\u00020\u0012H@ø\u0001\u0000¢\u0006\u0004\b\u0013\u0010\u0014\u001a&\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\n\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u001a\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\n\u0002\u0004\n\u0002\b\u0019¨\u0006\u0017"}, mo65043d2 = {"asFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "Lkotlinx/coroutines/channels/BroadcastChannel;", "broadcastIn", "scope", "Lkotlinx/coroutines/CoroutineScope;", "start", "Lkotlinx/coroutines/CoroutineStart;", "consumeAsFlow", "Lkotlinx/coroutines/channels/ReceiveChannel;", "emitAll", "", "Lkotlinx/coroutines/flow/FlowCollector;", "channel", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "emitAllImpl", "consume", "", "emitAllImpl$FlowKt__ChannelsKt", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlinx/coroutines/channels/ReceiveChannel;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "produceIn", "receiveAsFlow", "kotlinx-coroutines-core"}, mo65044k = 5, mo65045mv = {1, 5, 1}, mo65047xi = 48, mo65048xs = "kotlinx/coroutines/flow/FlowKt")
/* compiled from: Channels.kt */
final /* synthetic */ class FlowKt__ChannelsKt {

    @Metadata(mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
    /* compiled from: Channels.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[BufferOverflow.values().length];
            iArr[BufferOverflow.SUSPEND.ordinal()] = 1;
            iArr[BufferOverflow.DROP_OLDEST.ordinal()] = 2;
            iArr[BufferOverflow.DROP_LATEST.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final <T> Object emitAll(FlowCollector<? super T> flowCollector, ReceiveChannel<? extends T> receiveChannel, Continuation<? super Unit> continuation) {
        Object emitAllImpl$FlowKt__ChannelsKt = emitAllImpl$FlowKt__ChannelsKt(flowCollector, receiveChannel, true, continuation);
        return emitAllImpl$FlowKt__ChannelsKt == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? emitAllImpl$FlowKt__ChannelsKt : Unit.INSTANCE;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0058  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006c A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007c  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0085 A[SYNTHETIC, Splitter:B:35:0x0085] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0086 A[Catch:{ all -> 0x0056 }] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0025  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> java.lang.Object emitAllImpl$FlowKt__ChannelsKt(kotlinx.coroutines.flow.FlowCollector<? super T> r6, kotlinx.coroutines.channels.ReceiveChannel<? extends T> r7, boolean r8, kotlin.coroutines.Continuation<? super kotlin.Unit> r9) {
        /*
            boolean r0 = r9 instanceof kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1
            if (r0 == 0) goto L_0x0014
            r0 = r9
            kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1 r0 = (kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0014
            int r9 = r0.label
            int r9 = r9 - r2
            r0.label = r9
            goto L_0x0019
        L_0x0014:
            kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1 r0 = new kotlinx.coroutines.flow.FlowKt__ChannelsKt$emitAllImpl$1
            r0.<init>(r9)
        L_0x0019:
            java.lang.Object r9 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x0058
            if (r2 == r4) goto L_0x0042
            if (r2 != r3) goto L_0x003a
            boolean r6 = r0.Z$0
            java.lang.Object r7 = r0.L$1
            kotlinx.coroutines.channels.ReceiveChannel r7 = (kotlinx.coroutines.channels.ReceiveChannel) r7
            java.lang.Object r8 = r0.L$0
            kotlinx.coroutines.flow.FlowCollector r8 = (kotlinx.coroutines.flow.FlowCollector) r8
            kotlin.ResultKt.throwOnFailure(r9)     // Catch:{ all -> 0x0056 }
        L_0x0036:
            r5 = r8
            r8 = r6
            r6 = r5
            goto L_0x005e
        L_0x003a:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x0042:
            boolean r6 = r0.Z$0
            java.lang.Object r7 = r0.L$1
            kotlinx.coroutines.channels.ReceiveChannel r7 = (kotlinx.coroutines.channels.ReceiveChannel) r7
            java.lang.Object r8 = r0.L$0
            kotlinx.coroutines.flow.FlowCollector r8 = (kotlinx.coroutines.flow.FlowCollector) r8
            kotlin.ResultKt.throwOnFailure(r9)     // Catch:{ all -> 0x0056 }
            kotlinx.coroutines.channels.ChannelResult r9 = (kotlinx.coroutines.channels.ChannelResult) r9     // Catch:{ all -> 0x0056 }
            java.lang.Object r9 = r9.m5465unboximpl()     // Catch:{ all -> 0x0056 }
            goto L_0x0070
        L_0x0056:
            r8 = move-exception
            goto L_0x009d
        L_0x0058:
            kotlin.ResultKt.throwOnFailure(r9)
            kotlinx.coroutines.flow.FlowKt.ensureActive(r6)
        L_0x005e:
            r0.L$0 = r6     // Catch:{ all -> 0x0099 }
            r0.L$1 = r7     // Catch:{ all -> 0x0099 }
            r0.Z$0 = r8     // Catch:{ all -> 0x0099 }
            r0.label = r4     // Catch:{ all -> 0x0099 }
            java.lang.Object r9 = r7.m5472receiveCatchingJP2dKIU(r0)     // Catch:{ all -> 0x0099 }
            if (r9 != r1) goto L_0x006d
            return r1
        L_0x006d:
            r5 = r8
            r8 = r6
            r6 = r5
        L_0x0070:
            boolean r2 = kotlinx.coroutines.channels.ChannelResult.m5461isClosedimpl(r9)     // Catch:{ all -> 0x0056 }
            if (r2 == 0) goto L_0x0086
            java.lang.Throwable r8 = kotlinx.coroutines.channels.ChannelResult.m5457exceptionOrNullimpl(r9)     // Catch:{ all -> 0x0056 }
            if (r8 != 0) goto L_0x0085
            if (r6 == 0) goto L_0x0082
            r6 = 0
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r6)
        L_0x0082:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        L_0x0085:
            throw r8     // Catch:{ all -> 0x0056 }
        L_0x0086:
            java.lang.Object r9 = kotlinx.coroutines.channels.ChannelResult.m5459getOrThrowimpl(r9)     // Catch:{ all -> 0x0056 }
            r0.L$0 = r8     // Catch:{ all -> 0x0056 }
            r0.L$1 = r7     // Catch:{ all -> 0x0056 }
            r0.Z$0 = r6     // Catch:{ all -> 0x0056 }
            r0.label = r3     // Catch:{ all -> 0x0056 }
            java.lang.Object r9 = r8.emit(r9, r0)     // Catch:{ all -> 0x0056 }
            if (r9 != r1) goto L_0x0036
            return r1
        L_0x0099:
            r6 = move-exception
            r5 = r8
            r8 = r6
            r6 = r5
        L_0x009d:
            throw r8     // Catch:{ all -> 0x009e }
        L_0x009e:
            r9 = move-exception
            if (r6 == 0) goto L_0x00a4
            kotlinx.coroutines.channels.ChannelsKt.cancelConsumed(r7, r8)
        L_0x00a4:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ChannelsKt.emitAllImpl$FlowKt__ChannelsKt(kotlinx.coroutines.flow.FlowCollector, kotlinx.coroutines.channels.ReceiveChannel, boolean, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static final <T> Flow<T> receiveAsFlow(ReceiveChannel<? extends T> receiveChannel) {
        return new ChannelAsFlow<>(receiveChannel, false, (CoroutineContext) null, 0, (BufferOverflow) null, 28, (DefaultConstructorMarker) null);
    }

    public static final <T> Flow<T> consumeAsFlow(ReceiveChannel<? extends T> receiveChannel) {
        return new ChannelAsFlow<>(receiveChannel, true, (CoroutineContext) null, 0, (BufferOverflow) null, 28, (DefaultConstructorMarker) null);
    }

    public static /* synthetic */ BroadcastChannel broadcastIn$default(Flow flow, CoroutineScope coroutineScope, CoroutineStart coroutineStart, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineStart = CoroutineStart.LAZY;
        }
        return FlowKt.broadcastIn(flow, coroutineScope, coroutineStart);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use shareIn operator and the resulting SharedFlow as a replacement for BroadcastChannel", replaceWith = @ReplaceWith(expression = "this.shareIn(scope, SharingStarted.Lazily, 0)", imports = {}))
    public static final <T> BroadcastChannel<T> broadcastIn(Flow<? extends T> flow, CoroutineScope coroutineScope, CoroutineStart coroutineStart) {
        int i;
        ChannelFlow<? extends T> asChannelFlow = ChannelFlowKt.asChannelFlow(flow);
        int i2 = WhenMappings.$EnumSwitchMapping$0[asChannelFlow.onBufferOverflow.ordinal()];
        if (i2 == 1) {
            i = asChannelFlow.getProduceCapacity$kotlinx_coroutines_core();
        } else if (i2 == 2) {
            i = -1;
        } else if (i2 != 3) {
            throw new NoWhenBranchMatchedException();
        } else {
            throw new IllegalArgumentException("Broadcast channel does not support BufferOverflow.DROP_LATEST");
        }
        return BroadcastKt.broadcast$default(coroutineScope, asChannelFlow.context, i, coroutineStart, (Function1) null, new FlowKt__ChannelsKt$broadcastIn$1(flow, (Continuation<? super FlowKt__ChannelsKt$broadcastIn$1>) null), 8, (Object) null);
    }

    public static final <T> ReceiveChannel<T> produceIn(Flow<? extends T> flow, CoroutineScope coroutineScope) {
        return ChannelFlowKt.asChannelFlow(flow).produceImpl(coroutineScope);
    }

    @Deprecated(level = DeprecationLevel.WARNING, message = "'BroadcastChannel' is obsolete and all coreresponding operators are deprecated in the favour of StateFlow and SharedFlow")
    public static final <T> Flow<T> asFlow(BroadcastChannel<T> broadcastChannel) {
        return new FlowKt__ChannelsKt$asFlow$$inlined$unsafeFlow$1(broadcastChannel);
    }
}
