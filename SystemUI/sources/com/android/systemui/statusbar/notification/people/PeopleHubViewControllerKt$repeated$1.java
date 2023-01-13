package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

@Metadata(mo65042d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H@"}, mo65043d2 = {"<anonymous>", "", "T", "Lkotlin/sequences/SequenceScope;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@DebugMetadata(mo65296c = "com.android.systemui.statusbar.notification.people.PeopleHubViewControllerKt$repeated$1", mo65297f = "PeopleHubViewController.kt", mo65298i = {0}, mo65299l = {191}, mo65300m = "invokeSuspend", mo65301n = {"$this$sequence"}, mo65302s = {"L$0"})
/* compiled from: PeopleHubViewController.kt */
final class PeopleHubViewControllerKt$repeated$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super T>, Continuation<? super Unit>, Object> {
    final /* synthetic */ T $value;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PeopleHubViewControllerKt$repeated$1(T t, Continuation<? super PeopleHubViewControllerKt$repeated$1> continuation) {
        super(2, continuation);
        this.$value = t;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        PeopleHubViewControllerKt$repeated$1 peopleHubViewControllerKt$repeated$1 = new PeopleHubViewControllerKt$repeated$1(this.$value, continuation);
        peopleHubViewControllerKt$repeated$1.L$0 = obj;
        return peopleHubViewControllerKt$repeated$1;
    }

    public final Object invoke(SequenceScope<? super T> sequenceScope, Continuation<? super Unit> continuation) {
        return ((PeopleHubViewControllerKt$repeated$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    public final Object invokeSuspend(Object obj) {
        SequenceScope sequenceScope;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            sequenceScope = (SequenceScope) this.L$0;
        } else if (i == 1) {
            sequenceScope = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(obj);
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        do {
            this.L$0 = sequenceScope;
            this.label = 1;
        } while (sequenceScope.yield(this.$value, this) != coroutine_suspended);
        return coroutine_suspended;
    }
}
