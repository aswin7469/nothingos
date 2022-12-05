package com.android.systemui.statusbar.notification.row.dagger;

import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
/* loaded from: classes.dex */
public interface NotificationShelfComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        NotificationShelfComponent build();

        /* renamed from: notificationShelf */
        Builder mo1413notificationShelf(NotificationShelf notificationShelf);
    }

    NotificationShelfController getNotificationShelfController();
}
