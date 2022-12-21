package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t¨\u0006\n"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManagerImpl;", "Lcom/android/systemui/statusbar/notification/collection/inflation/BindEventManager;", "()V", "attachToLegacyPipeline", "", "notificationEntryManager", "Lcom/android/systemui/statusbar/notification/NotificationEntryManager;", "notifyViewBound", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BindEventManagerImpl.kt */
public final class BindEventManagerImpl extends BindEventManager {
    public final void notifyViewBound(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        for (BindEventManager.Listener onViewBound : getListeners()) {
            onViewBound.onViewBound(notificationEntry);
        }
    }

    public final void attachToLegacyPipeline(NotificationEntryManager notificationEntryManager) {
        Intrinsics.checkNotNullParameter(notificationEntryManager, "notificationEntryManager");
        notificationEntryManager.addNotificationEntryListener(new BindEventManagerImpl$attachToLegacyPipeline$1(this));
    }
}
