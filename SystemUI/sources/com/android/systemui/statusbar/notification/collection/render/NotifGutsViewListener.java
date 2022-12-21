package com.android.systemui.statusbar.notification.collection.render;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NotifGutsViewListener;", "", "onGutsClose", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "onGutsOpen", "guts", "Lcom/android/systemui/statusbar/notification/row/NotificationGuts;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotifGutsViewListener.kt */
public interface NotifGutsViewListener {
    void onGutsClose(NotificationEntry notificationEntry);

    void onGutsOpen(NotificationEntry notificationEntry, NotificationGuts notificationGuts);
}
