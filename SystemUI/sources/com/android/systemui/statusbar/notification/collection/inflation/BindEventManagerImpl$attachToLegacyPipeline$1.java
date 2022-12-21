package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/statusbar/notification/collection/inflation/BindEventManagerImpl$attachToLegacyPipeline$1", "Lcom/android/systemui/statusbar/notification/NotificationEntryListener;", "onEntryInflated", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onEntryReinflated", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BindEventManagerImpl.kt */
public final class BindEventManagerImpl$attachToLegacyPipeline$1 implements NotificationEntryListener {
    final /* synthetic */ BindEventManagerImpl this$0;

    BindEventManagerImpl$attachToLegacyPipeline$1(BindEventManagerImpl bindEventManagerImpl) {
        this.this$0 = bindEventManagerImpl;
    }

    public void onEntryInflated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.this$0.notifyViewBound(notificationEntry);
    }

    public void onEntryReinflated(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        this.this$0.notifyViewBound(notificationEntry);
    }
}
