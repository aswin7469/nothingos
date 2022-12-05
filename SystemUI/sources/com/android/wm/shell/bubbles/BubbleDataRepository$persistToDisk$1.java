package com.android.wm.shell.bubbles;

import androidx.constraintlayout.widget.R$styleable;
import com.android.wm.shell.bubbles.storage.BubblePersistentRepository;
import com.android.wm.shell.bubbles.storage.BubbleVolatileRepository;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.YieldKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: BubbleDataRepository.kt */
@DebugMetadata(c = "com.android.wm.shell.bubbles.BubbleDataRepository$persistToDisk$1", f = "BubbleDataRepository.kt", l = {R$styleable.Constraint_visibilityMode, 112}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class BubbleDataRepository$persistToDisk$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ Job $prev;
    int label;
    private /* synthetic */ CoroutineScope p$;
    final /* synthetic */ BubbleDataRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BubbleDataRepository$persistToDisk$1(Job job, BubbleDataRepository bubbleDataRepository, Continuation<? super BubbleDataRepository$persistToDisk$1> continuation) {
        super(2, continuation);
        this.$prev = job;
        this.this$0 = bubbleDataRepository;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        BubbleDataRepository$persistToDisk$1 bubbleDataRepository$persistToDisk$1 = new BubbleDataRepository$persistToDisk$1(this.$prev, this.this$0, continuation);
        bubbleDataRepository$persistToDisk$1.p$ = (CoroutineScope) obj;
        return bubbleDataRepository$persistToDisk$1;
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final Object mo1950invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
        return ((BubbleDataRepository$persistToDisk$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object coroutine_suspended;
        BubblePersistentRepository bubblePersistentRepository;
        BubbleVolatileRepository bubbleVolatileRepository;
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            Job job = this.$prev;
            if (job != null) {
                this.label = 1;
                if (JobKt.cancelAndJoin(job, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
        } else if (i != 1) {
            if (i == 2) {
                ResultKt.throwOnFailure(obj);
                bubblePersistentRepository = this.this$0.persistentRepository;
                bubbleVolatileRepository = this.this$0.volatileRepository;
                bubblePersistentRepository.persistsToDisk(bubbleVolatileRepository.getBubbles());
                return Unit.INSTANCE;
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        } else {
            ResultKt.throwOnFailure(obj);
        }
        this.label = 2;
        if (YieldKt.yield(this) == coroutine_suspended) {
            return coroutine_suspended;
        }
        bubblePersistentRepository = this.this$0.persistentRepository;
        bubbleVolatileRepository = this.this$0.volatileRepository;
        bubblePersistentRepository.persistsToDisk(bubbleVolatileRepository.getBubbles());
        return Unit.INSTANCE;
    }
}
