package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "hunMutator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HunMutator;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$onBeforeTransformGroups$1 extends Lambda implements Function1<HunMutator, Unit> {
    final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$onBeforeTransformGroups$1(HeadsUpCoordinator headsUpCoordinator) {
        super(1);
        this.this$0 = headsUpCoordinator;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((HunMutator) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(HunMutator hunMutator) {
        Intrinsics.checkNotNullParameter(hunMutator, "hunMutator");
        Collection values = this.this$0.mPostedEntries.values();
        Intrinsics.checkNotNullExpressionValue(values, "mPostedEntries.values");
        HeadsUpCoordinator headsUpCoordinator = this.this$0;
        for (HeadsUpCoordinator.PostedEntry postedEntry : CollectionsKt.toList(values)) {
            if (!postedEntry.getEntry().getSbn().isGroup()) {
                Intrinsics.checkNotNullExpressionValue(postedEntry, "posted");
                headsUpCoordinator.handlePostedEntry(postedEntry, hunMutator, "non-group");
                headsUpCoordinator.mPostedEntries.remove(postedEntry.getKey());
            }
        }
    }
}
