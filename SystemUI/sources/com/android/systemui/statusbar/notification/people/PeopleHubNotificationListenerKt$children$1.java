package com.android.systemui.statusbar.notification.people;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H@"}, mo64987d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Landroid/view/View;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@DebugMetadata(mo65240c = "com.android.systemui.statusbar.notification.people.PeopleHubNotificationListenerKt$children$1", mo65241f = "PeopleHubNotificationListener.kt", mo65242i = {0, 0}, mo65243l = {292}, mo65244m = "invokeSuspend", mo65245n = {"$this$sequence", "i"}, mo65246s = {"L$0", "I$0"})
/* compiled from: PeopleHubNotificationListener.kt */
final class PeopleHubNotificationListenerKt$children$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ViewGroup $this_children;
    int I$0;
    int I$1;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PeopleHubNotificationListenerKt$children$1(ViewGroup viewGroup, Continuation<? super PeopleHubNotificationListenerKt$children$1> continuation) {
        super(2, continuation);
        this.$this_children = viewGroup;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        PeopleHubNotificationListenerKt$children$1 peopleHubNotificationListenerKt$children$1 = new PeopleHubNotificationListenerKt$children$1(this.$this_children, continuation);
        peopleHubNotificationListenerKt$children$1.L$0 = obj;
        return peopleHubNotificationListenerKt$children$1;
    }

    public final Object invoke(SequenceScope<? super View> sequenceScope, Continuation<? super Unit> continuation) {
        return ((PeopleHubNotificationListenerKt$children$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x0030  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r7) {
        /*
            r6 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r6.label
            r2 = 1
            if (r1 == 0) goto L_0x001f
            if (r1 != r2) goto L_0x0017
            int r1 = r6.I$1
            int r3 = r6.I$0
            java.lang.Object r4 = r6.L$0
            kotlin.sequences.SequenceScope r4 = (kotlin.sequences.SequenceScope) r4
            kotlin.ResultKt.throwOnFailure(r7)
            goto L_0x004d
        L_0x0017:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x001f:
            kotlin.ResultKt.throwOnFailure(r7)
            java.lang.Object r7 = r6.L$0
            kotlin.sequences.SequenceScope r7 = (kotlin.sequences.SequenceScope) r7
            android.view.ViewGroup r1 = r6.$this_children
            int r1 = r1.getChildCount()
            r3 = 0
            r4 = r7
        L_0x002e:
            if (r3 >= r1) goto L_0x004f
            android.view.ViewGroup r7 = r6.$this_children
            android.view.View r7 = r7.getChildAt(r3)
            java.lang.String r5 = "getChildAt(i)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r5)
            r5 = r6
            kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
            r6.L$0 = r4
            r6.I$0 = r3
            r6.I$1 = r1
            r6.label = r2
            java.lang.Object r7 = r4.yield(r7, r5)
            if (r7 != r0) goto L_0x004d
            return r0
        L_0x004d:
            int r3 = r3 + r2
            goto L_0x002e
        L_0x004f:
            kotlin.Unit r6 = kotlin.Unit.INSTANCE
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.people.PeopleHubNotificationListenerKt$children$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
