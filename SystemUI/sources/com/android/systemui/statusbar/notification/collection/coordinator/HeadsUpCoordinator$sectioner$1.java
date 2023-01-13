package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\n\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$sectioner$1", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "getComparator", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifComparator;", "getHeaderNodeController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "isInSection", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$sectioner$1 extends NotifSectioner {
    final /* synthetic */ HeadsUpCoordinator this$0;

    public NodeController getHeaderNodeController() {
        return null;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$sectioner$1(HeadsUpCoordinator headsUpCoordinator) {
        super("HeadsUp", 2);
        this.this$0 = headsUpCoordinator;
    }

    public boolean isInSection(ListEntry listEntry) {
        Intrinsics.checkNotNullParameter(listEntry, "entry");
        return this.this$0.isGoingToShowHunNoRetract(listEntry);
    }

    public NotifComparator getComparator() {
        return new HeadsUpCoordinator$sectioner$1$getComparator$1(this.this$0);
    }
}
