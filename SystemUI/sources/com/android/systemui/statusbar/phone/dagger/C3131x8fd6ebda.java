package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetNotificationsQuickSettingsContainerFactory */
public final class C3131x8fd6ebda implements Factory<NotificationsQuickSettingsContainer> {
    private final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;

    public C3131x8fd6ebda(Provider<NotificationShadeWindowView> provider) {
        this.notificationShadeWindowViewProvider = provider;
    }

    public NotificationsQuickSettingsContainer get() {
        return getNotificationsQuickSettingsContainer(this.notificationShadeWindowViewProvider.get());
    }

    public static C3131x8fd6ebda create(Provider<NotificationShadeWindowView> provider) {
        return new C3131x8fd6ebda(provider);
    }

    public static NotificationsQuickSettingsContainer getNotificationsQuickSettingsContainer(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationsQuickSettingsContainer) Preconditions.checkNotNullFromProvides(StatusBarViewModule.getNotificationsQuickSettingsContainer(notificationShadeWindowView));
    }
}
