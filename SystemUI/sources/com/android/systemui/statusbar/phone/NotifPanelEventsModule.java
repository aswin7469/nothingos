package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class NotifPanelEventsModule {
    /* access modifiers changed from: package-private */
    @Binds
    public abstract NotifPanelEvents bindPanelEvents(NotificationPanelViewController.PanelEventsEmitter panelEventsEmitter);
}
