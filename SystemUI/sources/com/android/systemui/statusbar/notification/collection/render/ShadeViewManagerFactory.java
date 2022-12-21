package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import dagger.assisted.AssistedFactory;
import kotlin.Metadata;

@AssistedFactory
@Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewManagerFactory;", "", "create", "Lcom/android/systemui/statusbar/notification/collection/render/ShadeViewManager;", "listContainer", "Lcom/android/systemui/statusbar/notification/stack/NotificationListContainer;", "stackController", "Lcom/android/systemui/statusbar/notification/collection/render/NotifStackController;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ShadeViewManager.kt */
public interface ShadeViewManagerFactory {
    ShadeViewManager create(NotificationListContainer notificationListContainer, NotifStackController notifStackController);
}
