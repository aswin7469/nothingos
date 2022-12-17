package androidx.window.area;

import androidx.window.area.WindowAreaStatus;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.flow.FlowCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DebugMetadata(mo24969c = "androidx.window.area.WindowAreaControllerImpl$rearDisplayStatus$1", mo24970f = "WindowAreaControllerImpl.kt", mo24971l = {65, 66}, mo24972m = "invokeSuspend")
/* compiled from: WindowAreaControllerImpl.kt */
final class WindowAreaControllerImpl$rearDisplayStatus$1 extends SuspendLambda implements Function2<FlowCollector<? super WindowAreaStatus>, Continuation<? super Unit>, Object> {
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;
    final /* synthetic */ WindowAreaControllerImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    WindowAreaControllerImpl$rearDisplayStatus$1(WindowAreaControllerImpl windowAreaControllerImpl, Continuation<? super WindowAreaControllerImpl$rearDisplayStatus$1> continuation) {
        super(2, continuation);
        this.this$0 = windowAreaControllerImpl;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        WindowAreaControllerImpl$rearDisplayStatus$1 windowAreaControllerImpl$rearDisplayStatus$1 = new WindowAreaControllerImpl$rearDisplayStatus$1(this.this$0, continuation);
        windowAreaControllerImpl$rearDisplayStatus$1.L$0 = obj;
        return windowAreaControllerImpl$rearDisplayStatus$1;
    }

    @Nullable
    public final Object invoke(@NotNull FlowCollector<? super WindowAreaStatus> flowCollector, @Nullable Continuation<? super Unit> continuation) {
        return ((WindowAreaControllerImpl$rearDisplayStatus$1) create(flowCollector, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x006e A[Catch:{ all -> 0x0037 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0079 A[Catch:{ all -> 0x0037 }] */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r9) {
        /*
            r8 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r8.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x0039
            if (r1 == r3) goto L_0x0027
            if (r1 != r2) goto L_0x001f
            java.lang.Object r1 = r8.L$2
            kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
            java.lang.Object r4 = r8.L$1
            java.util.function.Consumer r4 = (java.util.function.Consumer) r4
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.flow.FlowCollector r5 = (kotlinx.coroutines.flow.FlowCollector) r5
            kotlin.ResultKt.throwOnFailure(r9)     // Catch:{ all -> 0x0037 }
        L_0x001d:
            r9 = r5
            goto L_0x005f
        L_0x001f:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r9)
            throw r8
        L_0x0027:
            java.lang.Object r1 = r8.L$2
            kotlinx.coroutines.channels.ChannelIterator r1 = (kotlinx.coroutines.channels.ChannelIterator) r1
            java.lang.Object r4 = r8.L$1
            java.util.function.Consumer r4 = (java.util.function.Consumer) r4
            java.lang.Object r5 = r8.L$0
            kotlinx.coroutines.flow.FlowCollector r5 = (kotlinx.coroutines.flow.FlowCollector) r5
            kotlin.ResultKt.throwOnFailure(r9)     // Catch:{ all -> 0x0037 }
            goto L_0x0071
        L_0x0037:
            r9 = move-exception
            goto L_0x009c
        L_0x0039:
            kotlin.ResultKt.throwOnFailure(r9)
            java.lang.Object r9 = r8.L$0
            kotlinx.coroutines.flow.FlowCollector r9 = (kotlinx.coroutines.flow.FlowCollector) r9
            r1 = 10
            kotlinx.coroutines.channels.BufferOverflow r4 = kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
            r5 = 4
            r6 = 0
            kotlinx.coroutines.channels.Channel r1 = kotlinx.coroutines.channels.ChannelKt.Channel$default(r1, r4, r6, r5, r6)
            androidx.window.area.WindowAreaControllerImpl r4 = r8.this$0
            androidx.window.area.WindowAreaControllerImpl$rearDisplayStatus$1$$ExternalSyntheticLambda0 r5 = new androidx.window.area.WindowAreaControllerImpl$rearDisplayStatus$1$$ExternalSyntheticLambda0
            r5.<init>(r4, r1)
            androidx.window.area.WindowAreaControllerImpl r4 = r8.this$0
            androidx.window.extensions.area.WindowAreaComponent r4 = r4.windowAreaComponent
            r4.addRearDisplayStatusListener(r5)
            kotlinx.coroutines.channels.ChannelIterator r1 = r1.iterator()     // Catch:{ all -> 0x009a }
            r4 = r5
        L_0x005f:
            r8.L$0 = r9     // Catch:{ all -> 0x0037 }
            r8.L$1 = r4     // Catch:{ all -> 0x0037 }
            r8.L$2 = r1     // Catch:{ all -> 0x0037 }
            r8.label = r3     // Catch:{ all -> 0x0037 }
            java.lang.Object r5 = r1.hasNext(r8)     // Catch:{ all -> 0x0037 }
            if (r5 != r0) goto L_0x006e
            return r0
        L_0x006e:
            r7 = r5
            r5 = r9
            r9 = r7
        L_0x0071:
            java.lang.Boolean r9 = (java.lang.Boolean) r9     // Catch:{ all -> 0x0037 }
            boolean r9 = r9.booleanValue()     // Catch:{ all -> 0x0037 }
            if (r9 == 0) goto L_0x008e
            java.lang.Object r9 = r1.next()     // Catch:{ all -> 0x0037 }
            androidx.window.area.WindowAreaStatus r9 = (androidx.window.area.WindowAreaStatus) r9     // Catch:{ all -> 0x0037 }
            r8.L$0 = r5     // Catch:{ all -> 0x0037 }
            r8.L$1 = r4     // Catch:{ all -> 0x0037 }
            r8.L$2 = r1     // Catch:{ all -> 0x0037 }
            r8.label = r2     // Catch:{ all -> 0x0037 }
            java.lang.Object r9 = r5.emit(r9, r8)     // Catch:{ all -> 0x0037 }
            if (r9 != r0) goto L_0x001d
            return r0
        L_0x008e:
            androidx.window.area.WindowAreaControllerImpl r8 = r8.this$0
            androidx.window.extensions.area.WindowAreaComponent r8 = r8.windowAreaComponent
            r8.removeRearDisplayStatusListener(r4)
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        L_0x009a:
            r9 = move-exception
            r4 = r5
        L_0x009c:
            androidx.window.area.WindowAreaControllerImpl r8 = r8.this$0
            androidx.window.extensions.area.WindowAreaComponent r8 = r8.windowAreaComponent
            r8.removeRearDisplayStatusListener(r4)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.area.WindowAreaControllerImpl$rearDisplayStatus$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeSuspend$lambda-0  reason: not valid java name */
    public static final void m48invokeSuspend$lambda0(WindowAreaControllerImpl windowAreaControllerImpl, Channel channel, Integer num) {
        WindowAreaStatus.Companion companion = WindowAreaStatus.Companion;
        Intrinsics.checkNotNullExpressionValue(num, "status");
        windowAreaControllerImpl.currentStatus = companion.translate$window_release(num.intValue());
        WindowAreaStatus access$getCurrentStatus$p = windowAreaControllerImpl.currentStatus;
        if (access$getCurrentStatus$p == null) {
            access$getCurrentStatus$p = WindowAreaStatus.UNSUPPORTED;
        }
        channel.trySend-JP2dKIU(access$getCurrentStatus$p);
    }
}
