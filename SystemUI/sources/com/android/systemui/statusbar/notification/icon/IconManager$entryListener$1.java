package com.android.systemui.statusbar.notification.icon;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/statusbar/notification/icon/IconManager$entryListener$1", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/NotifCollectionListener;", "onEntryCleanUp", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onEntryInit", "onRankingApplied", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: IconManager.kt */
public final class IconManager$entryListener$1 implements NotifCollectionListener {
    final /* synthetic */ IconManager this$0;

    IconManager$entryListener$1(IconManager iconManager) {
        this.this$0 = iconManager;
    }

    public void onEntryInit(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        notificationEntry.addOnSensitivityChangedListener(this.this$0.sensitivityListener);
    }

    public void onEntryCleanUp(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        notificationEntry.removeOnSensitivityChangedListener(this.this$0.sensitivityListener);
    }

    public void onRankingApplied() {
        this.this$0.recalculateForImportantConversationChange();
    }
}
