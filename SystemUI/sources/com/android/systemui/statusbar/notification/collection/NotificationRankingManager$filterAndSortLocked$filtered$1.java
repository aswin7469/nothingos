package com.android.systemui.statusbar.notification.collection;

import com.android.settingslib.SliceBroadcastRelay;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationRankingManager.kt */
/* synthetic */ class NotificationRankingManager$filterAndSortLocked$filtered$1 extends FunctionReferenceImpl implements Function1<NotificationEntry, Boolean> {
    NotificationRankingManager$filterAndSortLocked$filtered$1(Object obj) {
        super(1, obj, NotificationRankingManager.class, SliceBroadcastRelay.EXTRA_FILTER, "filter(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Z", 0);
    }

    public final Boolean invoke(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "p0");
        return Boolean.valueOf(((NotificationRankingManager) this.receiver).filter(notificationEntry));
    }
}
