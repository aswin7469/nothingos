package com.android.systemui.statusbar.notification.collection.render;

import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH&J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotificationVisibilityProvider;", "", "getLocation", "Lcom/android/internal/statusbar/NotificationVisibility$NotificationLocation;", "key", "", "obtain", "Lcom/android/internal/statusbar/NotificationVisibility;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "visible", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationVisibilityProvider.kt */
public interface NotificationVisibilityProvider {
    NotificationVisibility.NotificationLocation getLocation(String str);

    NotificationVisibility obtain(NotificationEntry notificationEntry, boolean z);

    NotificationVisibility obtain(String str, boolean z);
}
