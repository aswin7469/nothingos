package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$findBestTransferChild$4 extends Lambda implements Function1<NotificationEntry, Comparable<?>> {
    public static final HeadsUpCoordinator$findBestTransferChild$4 INSTANCE = new HeadsUpCoordinator$findBestTransferChild$4();

    HeadsUpCoordinator$findBestTransferChild$4() {
        super(1);
    }

    public final Comparable<?> invoke(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "it");
        return Long.valueOf(-notificationEntry.getSbn().getNotification().when);
    }
}
