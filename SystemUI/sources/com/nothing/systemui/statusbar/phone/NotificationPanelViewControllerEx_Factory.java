package com.nothing.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationPanelViewControllerEx_Factory implements Factory<NotificationPanelViewControllerEx> {
    private final Provider<NotificationPanelViewController> controllerProvider;

    public NotificationPanelViewControllerEx_Factory(Provider<NotificationPanelViewController> provider) {
        this.controllerProvider = provider;
    }

    public NotificationPanelViewControllerEx get() {
        return newInstance(this.controllerProvider.get());
    }

    public static NotificationPanelViewControllerEx_Factory create(Provider<NotificationPanelViewController> provider) {
        return new NotificationPanelViewControllerEx_Factory(provider);
    }

    public static NotificationPanelViewControllerEx newInstance(NotificationPanelViewController notificationPanelViewController) {
        return new NotificationPanelViewControllerEx(notificationPanelViewController);
    }
}
