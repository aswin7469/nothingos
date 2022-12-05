package com.android.systemui.util;

import android.view.View;
import android.view.ViewGroup;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConvenienceExtensions.kt */
@DebugMetadata(c = "com.android.systemui.util.ConvenienceExtensionsKt$children$1", f = "ConvenienceExtensions.kt", l = {26}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class ConvenienceExtensionsKt$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ViewGroup $this_children;
    int I$0;
    int I$1;
    int label;
    private /* synthetic */ SequenceScope<View> p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConvenienceExtensionsKt$children$1(ViewGroup viewGroup, Continuation<? super ConvenienceExtensionsKt$children$1> continuation) {
        super(2, continuation);
        this.$this_children = viewGroup;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        ConvenienceExtensionsKt$children$1 convenienceExtensionsKt$children$1 = new ConvenienceExtensionsKt$children$1(this.$this_children, continuation);
        convenienceExtensionsKt$children$1.p$ = (SequenceScope) obj;
        return convenienceExtensionsKt$children$1;
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Object mo1950invoke(@NotNull SequenceScope<? super View> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
        return ((ConvenienceExtensionsKt$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0043, code lost:
        return kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0026, code lost:
        if (r1 > 0) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x003f, code lost:
        if (r7 >= r1) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0028, code lost:
        r3 = r7 + 1;
        r4 = r6.p$;
        r7 = r6.$this_children.getChildAt(r7);
        r6.I$0 = r3;
        r6.I$1 = r1;
        r6.label = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x003c, code lost:
        if (r4.yield(r7, r6) != r0) goto L5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x003e, code lost:
        return r0;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:8:0x003c -> B:5:0x0012). Please submit an issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended;
        int i;
        int childCount;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i2 = this.label;
        if (i2 == 0) {
            ResultKt.throwOnFailure(obj);
            i = 0;
            childCount = this.$this_children.getChildCount();
        } else if (i2 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            childCount = this.I$1;
            int i3 = this.I$0;
            ResultKt.throwOnFailure(obj);
            i = i3;
        }
    }
}
