package com.android.systemui.statusbar.notification.row;

import com.android.internal.logging.UiEventLogger;
/* loaded from: classes.dex */
enum NotificationControlsEvent implements UiEventLogger.UiEventEnum {
    NOTIFICATION_CONTROLS_OPEN(594),
    NOTIFICATION_CONTROLS_SAVE_IMPORTANCE(595),
    NOTIFICATION_CONTROLS_CLOSE(596);
    
    private final int mId;

    NotificationControlsEvent(int i) {
        this.mId = i;
    }

    public int getId() {
        return this.mId;
    }
}
