package com.android.systemui.statusbar.phone;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationPanelViewController$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ NotificationPanelViewController f$0;

    public /* synthetic */ NotificationPanelViewController$$ExternalSyntheticLambda6(NotificationPanelViewController notificationPanelViewController) {
        this.f$0 = notificationPanelViewController;
    }

    public final void accept(Object obj) {
        this.f$0.onNotificationScrolled(((Integer) obj).intValue());
    }
}
