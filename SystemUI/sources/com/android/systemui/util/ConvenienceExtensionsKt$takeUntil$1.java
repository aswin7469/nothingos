package com.android.systemui.util;

import java.util.Iterator;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ConvenienceExtensions.kt */
@DebugMetadata(c = "com.android.systemui.util.ConvenienceExtensionsKt$takeUntil$1", f = "ConvenienceExtensions.kt", l = {32}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class ConvenienceExtensionsKt$takeUntil$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ Function1<T, Boolean> $pred;
    final /* synthetic */ Sequence<T> $this_takeUntil;
    Object L$0;
    Object L$1;
    int label;
    private /* synthetic */ SequenceScope<T> p$;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public ConvenienceExtensionsKt$takeUntil$1(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1, Continuation<? super ConvenienceExtensionsKt$takeUntil$1> continuation) {
        super(2, continuation);
        this.$this_takeUntil = sequence;
        this.$pred = function1;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        ConvenienceExtensionsKt$takeUntil$1 convenienceExtensionsKt$takeUntil$1 = new ConvenienceExtensionsKt$takeUntil$1(this.$this_takeUntil, this.$pred, continuation);
        convenienceExtensionsKt$takeUntil$1.p$ = (SequenceScope) obj;
        return convenienceExtensionsKt$takeUntil$1;
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke */
    public final Object mo1950invoke(@NotNull SequenceScope<? super T> sequenceScope, @Nullable Continuation<? super Unit> continuation) {
        return ((ConvenienceExtensionsKt$takeUntil$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x004c, code lost:
        if (r4.$pred.mo1949invoke(r1).booleanValue() == false) goto L7;
     */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:10:0x003d -> B:5:0x0040). Please submit an issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended;
        Iterator it;
        Object next;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            it = this.$this_takeUntil.iterator();
            if (it.hasNext()) {
                next = it.next();
                SequenceScope<T> sequenceScope = this.p$;
                this.L$0 = it;
                this.L$1 = next;
                this.label = 1;
                if (sequenceScope.yield(next, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
            return Unit.INSTANCE;
        } else if (i != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            next = this.L$1;
            it = (Iterator) this.L$0;
            ResultKt.throwOnFailure(obj);
        }
    }
}
