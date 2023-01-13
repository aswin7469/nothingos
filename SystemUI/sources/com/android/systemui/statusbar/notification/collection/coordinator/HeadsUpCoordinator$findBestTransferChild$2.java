package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo65043d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "invoke", "(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Ljava/lang/Boolean;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: HeadsUpCoordinator.kt */
final class HeadsUpCoordinator$findBestTransferChild$2 extends Lambda implements Function1<NotificationEntry, Boolean> {
    final /* synthetic */ Function1<String, GroupLocation> $locationLookupByKey;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    HeadsUpCoordinator$findBestTransferChild$2(Function1<? super String, ? extends GroupLocation> function1) {
        super(1);
        this.$locationLookupByKey = function1;
    }

    public final Boolean invoke(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "it");
        Function1<String, GroupLocation> function1 = this.$locationLookupByKey;
        String key = notificationEntry.getKey();
        Intrinsics.checkNotNullExpressionValue(key, "it.key");
        return Boolean.valueOf(function1.invoke(key) != GroupLocation.Detached);
    }
}
