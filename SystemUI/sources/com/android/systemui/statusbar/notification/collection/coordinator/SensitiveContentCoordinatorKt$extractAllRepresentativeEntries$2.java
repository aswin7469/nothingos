package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.SequenceScope;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H@"}, mo65043d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@DebugMetadata(mo65296c = "com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2", mo65297f = "SensitiveContentCoordinator.kt", mo65298i = {0}, mo65299l = {120, 122}, mo65300m = "invokeSuspend", mo65301n = {"$this$sequence"}, mo65302s = {"L$0"})
/* compiled from: SensitiveContentCoordinator.kt */
final class SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super NotificationEntry>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ListEntry $listEntry;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(ListEntry listEntry, Continuation<? super SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2> continuation) {
        super(2, continuation);
        this.$listEntry = listEntry;
    }

    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2 = new SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2(this.$listEntry, continuation);
        sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2.L$0 = obj;
        return sensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2;
    }

    public final Object invoke(SequenceScope<? super NotificationEntry> sequenceScope, Continuation<? super Unit> continuation) {
        return ((SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: kotlin.sequences.SequenceScope} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(java.lang.Object r6) {
        /*
            r5 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r5.label
            r2 = 2
            r3 = 1
            if (r1 == 0) goto L_0x0022
            if (r1 == r3) goto L_0x001a
            if (r1 != r2) goto L_0x0012
            kotlin.ResultKt.throwOnFailure(r6)
            goto L_0x0061
        L_0x0012:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>((java.lang.String) r6)
            throw r5
        L_0x001a:
            java.lang.Object r1 = r5.L$0
            kotlin.sequences.SequenceScope r1 = (kotlin.sequences.SequenceScope) r1
            kotlin.ResultKt.throwOnFailure(r6)
            goto L_0x003d
        L_0x0022:
            kotlin.ResultKt.throwOnFailure(r6)
            java.lang.Object r6 = r5.L$0
            r1 = r6
            kotlin.sequences.SequenceScope r1 = (kotlin.sequences.SequenceScope) r1
            com.android.systemui.statusbar.notification.collection.ListEntry r6 = r5.$listEntry
            com.android.systemui.statusbar.notification.collection.NotificationEntry r6 = r6.getRepresentativeEntry()
            if (r6 == 0) goto L_0x003d
            r5.L$0 = r1
            r5.label = r3
            java.lang.Object r6 = r1.yield(r6, r5)
            if (r6 != r0) goto L_0x003d
            return r0
        L_0x003d:
            com.android.systemui.statusbar.notification.collection.ListEntry r6 = r5.$listEntry
            boolean r3 = r6 instanceof com.android.systemui.statusbar.notification.collection.GroupEntry
            if (r3 == 0) goto L_0x0061
            com.android.systemui.statusbar.notification.collection.GroupEntry r6 = (com.android.systemui.statusbar.notification.collection.GroupEntry) r6
            java.util.List r6 = r6.getChildren()
            java.lang.String r3 = "listEntry.children"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r3)
            kotlin.sequences.Sequence r6 = com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt.extractAllRepresentativeEntries((java.util.List<? extends com.android.systemui.statusbar.notification.collection.ListEntry>) r6)
            r3 = r5
            kotlin.coroutines.Continuation r3 = (kotlin.coroutines.Continuation) r3
            r4 = 0
            r5.L$0 = r4
            r5.label = r2
            java.lang.Object r5 = r1.yieldAll(r6, (kotlin.coroutines.Continuation<? super kotlin.Unit>) r3)
            if (r5 != r0) goto L_0x0061
            return r0
        L_0x0061:
            kotlin.Unit r5 = kotlin.Unit.INSTANCE
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
