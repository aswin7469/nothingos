package com.android.systemui.util;

import com.android.systemui.util.SparseArrayMapWrapper;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u0016\u0012\u0012\u0012\u0010\u0012\f\u0012\n \u0005*\u0004\u0018\u0001H\u0002H\u00020\u00040\u0003H@"}, mo65043d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;", "Lcom/android/systemui/util/SparseArrayMapWrapper$Entry;", "kotlin.jvm.PlatformType"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@DebugMetadata(mo65296c = "com.android.systemui.util.SparseArrayMapWrapper$entrySequence$1", mo65297f = "SparseArrayUtils.kt", mo65298i = {0, 0, 0}, mo65299l = {91}, mo65300m = "invokeSuspend", mo65301n = {"$this$sequence", "size", "i"}, mo65302s = {"L$0", "I$0", "I$1"})
/* compiled from: SparseArrayUtils.kt */
final class SparseArrayMapWrapper$entrySequence$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super SparseArrayMapWrapper.Entry<T>>, Continuation<? super Unit>, Object> {
    int I$0;
    int I$1;
    private /* synthetic */ Object L$0;
    int label;
    final /* synthetic */ SparseArrayMapWrapper<T> this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SparseArrayMapWrapper$entrySequence$1(SparseArrayMapWrapper<T> sparseArrayMapWrapper, Continuation<? super SparseArrayMapWrapper$entrySequence$1> continuation) {
        super(2, continuation);
        this.this$0 = sparseArrayMapWrapper;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        SparseArrayMapWrapper$entrySequence$1 sparseArrayMapWrapper$entrySequence$1 = new SparseArrayMapWrapper$entrySequence$1(this.this$0, continuation);
        sparseArrayMapWrapper$entrySequence$1.L$0 = obj;
        return sparseArrayMapWrapper$entrySequence$1;
    }

    public final Object invoke(SequenceScope<? super SparseArrayMapWrapper.Entry<T>> sequenceScope, Continuation<? super Unit> continuation) {
        return ((SparseArrayMapWrapper$entrySequence$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0037  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r9) {
        /*
            r8 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r8.label
            r2 = 1
            if (r1 == 0) goto L_0x001f
            if (r1 != r2) goto L_0x0017
            int r1 = r8.I$1
            int r3 = r8.I$0
            java.lang.Object r4 = r8.L$0
            kotlin.sequences.SequenceScope r4 = (kotlin.sequences.SequenceScope) r4
            kotlin.ResultKt.throwOnFailure(r9)
            goto L_0x0062
        L_0x0017:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r9 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>((java.lang.String) r9)
            throw r8
        L_0x001f:
            kotlin.ResultKt.throwOnFailure(r9)
            java.lang.Object r9 = r8.L$0
            kotlin.sequences.SequenceScope r9 = (kotlin.sequences.SequenceScope) r9
            com.android.systemui.util.SparseArrayMapWrapper<T> r1 = r8.this$0
            android.util.SparseArray r1 = r1.sparseArray
            int r1 = r1.size()
            r3 = 0
            r4 = r9
            r7 = r3
            r3 = r1
            r1 = r7
        L_0x0035:
            if (r1 >= r3) goto L_0x0064
            com.android.systemui.util.SparseArrayMapWrapper<T> r9 = r8.this$0
            android.util.SparseArray r9 = r9.sparseArray
            int r9 = r9.keyAt(r1)
            com.android.systemui.util.SparseArrayMapWrapper<T> r5 = r8.this$0
            android.util.SparseArray r5 = r5.sparseArray
            java.lang.Object r5 = r5.get(r9)
            com.android.systemui.util.SparseArrayMapWrapper$Entry r6 = new com.android.systemui.util.SparseArrayMapWrapper$Entry
            r6.<init>(r9, r5)
            r9 = r8
            kotlin.coroutines.Continuation r9 = (kotlin.coroutines.Continuation) r9
            r8.L$0 = r4
            r8.I$0 = r3
            r8.I$1 = r1
            r8.label = r2
            java.lang.Object r9 = r4.yield(r6, r9)
            if (r9 != r0) goto L_0x0062
            return r0
        L_0x0062:
            int r1 = r1 + r2
            goto L_0x0035
        L_0x0064:
            kotlin.Unit r8 = kotlin.Unit.INSTANCE
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.SparseArrayMapWrapper$entrySequence$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
