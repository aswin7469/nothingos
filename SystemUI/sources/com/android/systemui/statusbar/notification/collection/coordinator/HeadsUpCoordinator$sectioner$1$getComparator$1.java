package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$sectioner$1$getComparator$1", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifComparator;", "compare", "", "o1", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "o2", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$sectioner$1$getComparator$1 extends NotifComparator {
    final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$sectioner$1$getComparator$1(HeadsUpCoordinator headsUpCoordinator) {
        super("HeadsUp");
        this.this$0 = headsUpCoordinator;
    }

    public int compare(ListEntry listEntry, ListEntry listEntry2) {
        Intrinsics.checkNotNullParameter(listEntry, "o1");
        Intrinsics.checkNotNullParameter(listEntry2, "o2");
        return this.this$0.mHeadsUpManager.compare(listEntry.getRepresentativeEntry(), listEntry2.getRepresentativeEntry());
    }
}
