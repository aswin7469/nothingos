package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import dagger.internal.Factory;

public final class NotificationPanelViewController_PanelEventsEmitter_Factory implements Factory<NotificationPanelViewController.PanelEventsEmitter> {
    public NotificationPanelViewController.PanelEventsEmitter get() {
        return newInstance();
    }

    public static NotificationPanelViewController_PanelEventsEmitter_Factory create() {
        return InstanceHolder.INSTANCE;
    }

    public static NotificationPanelViewController.PanelEventsEmitter newInstance() {
        return new NotificationPanelViewController.PanelEventsEmitter();
    }

    private static final class InstanceHolder {
        /* access modifiers changed from: private */
        public static final NotificationPanelViewController_PanelEventsEmitter_Factory INSTANCE = new NotificationPanelViewController_PanelEventsEmitter_Factory();

        private InstanceHolder() {
        }
    }
}
