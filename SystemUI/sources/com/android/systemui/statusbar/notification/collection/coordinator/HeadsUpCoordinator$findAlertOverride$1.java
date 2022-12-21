package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "posted", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;", "invoke", "(Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator$PostedEntry;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$findAlertOverride$1 extends Lambda implements Function1<HeadsUpCoordinator.PostedEntry, Boolean> {
    public static final HeadsUpCoordinator$findAlertOverride$1 INSTANCE = new HeadsUpCoordinator$findAlertOverride$1();

    HeadsUpCoordinator$findAlertOverride$1() {
        super(1);
    }

    public final Boolean invoke(HeadsUpCoordinator.PostedEntry postedEntry) {
        Intrinsics.checkNotNullParameter(postedEntry, "posted");
        return Boolean.valueOf(!postedEntry.getEntry().getSbn().getNotification().isGroupSummary());
    }
}
