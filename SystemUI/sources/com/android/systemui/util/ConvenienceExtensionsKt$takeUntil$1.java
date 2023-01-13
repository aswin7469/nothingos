package com.android.systemui.util;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;

@Metadata(mo65042d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@"}, mo65043d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@DebugMetadata(mo65296c = "com.android.systemui.util.ConvenienceExtensionsKt$takeUntil$1", mo65297f = "ConvenienceExtensions.kt", mo65298i = {0, 0}, mo65299l = {32}, mo65300m = "invokeSuspend", mo65301n = {"$this$sequence", "x"}, mo65302s = {"L$0", "L$2"})
/* compiled from: ConvenienceExtensions.kt */
final class ConvenienceExtensionsKt$takeUntil$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function1<T, Boolean> $pred;
    final /* synthetic */ Sequence<T> $this_takeUntil;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ConvenienceExtensionsKt$takeUntil$1(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1, Continuation<? super ConvenienceExtensionsKt$takeUntil$1> continuation) {
        super(2, continuation);
        this.$this_takeUntil = sequence;
        this.$pred = function1;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ConvenienceExtensionsKt$takeUntil$1 convenienceExtensionsKt$takeUntil$1 = new ConvenienceExtensionsKt$takeUntil$1(this.$this_takeUntil, this.$pred, continuation);
        convenienceExtensionsKt$takeUntil$1.L$0 = obj;
        return convenienceExtensionsKt$takeUntil$1;
    }

    public final Object invoke(SequenceScope<? super T> sequenceScope, Continuation<? super Unit> continuation) {
        return ((ConvenienceExtensionsKt$takeUntil$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0058, code lost:
        if (r5.$pred.invoke(r1).booleanValue() == false) goto L_0x0030;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r6) {
        /*
            r5 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r5.label
            r2 = 1
            if (r1 == 0) goto L_0x0021
            if (r1 != r2) goto L_0x0019
            java.lang.Object r1 = r5.L$2
            java.lang.Object r3 = r5.L$1
            java.util.Iterator r3 = (java.util.Iterator) r3
            java.lang.Object r4 = r5.L$0
            kotlin.sequences.SequenceScope r4 = (kotlin.sequences.SequenceScope) r4
            kotlin.ResultKt.throwOnFailure(r6)
            goto L_0x004c
        L_0x0019:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x0021:
            kotlin.ResultKt.throwOnFailure(r6)
            java.lang.Object r6 = r5.L$0
            kotlin.sequences.SequenceScope r6 = (kotlin.sequences.SequenceScope) r6
            kotlin.sequences.Sequence<T> r1 = r5.$this_takeUntil
            java.util.Iterator r1 = r1.iterator()
            r4 = r6
            r3 = r1
        L_0x0030:
            boolean r6 = r3.hasNext()
            if (r6 == 0) goto L_0x005a
            java.lang.Object r1 = r3.next()
            r6 = r5
            kotlin.coroutines.Continuation r6 = (kotlin.coroutines.Continuation) r6
            r5.L$0 = r4
            r5.L$1 = r3
            r5.L$2 = r1
            r5.label = r2
            java.lang.Object r6 = r4.yield(r1, r6)
            if (r6 != r0) goto L_0x004c
            return r0
        L_0x004c:
            kotlin.jvm.functions.Function1<T, java.lang.Boolean> r6 = r5.$pred
            java.lang.Object r6 = r6.invoke(r1)
            java.lang.Boolean r6 = (java.lang.Boolean) r6
            boolean r6 = r6.booleanValue()
            if (r6 == 0) goto L_0x0030
        L_0x005a:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.ConvenienceExtensionsKt$takeUntil$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
