package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000f\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$findBestTransferChild$3 extends Lambda implements Function1<NotificationEntry, Comparable<?>> {
    final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$findBestTransferChild$3(HeadsUpCoordinator headsUpCoordinator) {
        super(1);
        this.this$0 = headsUpCoordinator;
    }

    public final Comparable<?> invoke(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "it");
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "it.key");
        return Boolean.valueOf(!this.this$0.mPostedEntries.containsKey(key));
    }
}
