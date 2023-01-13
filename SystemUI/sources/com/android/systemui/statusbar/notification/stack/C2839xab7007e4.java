package com.android.systemui.statusbar.notification.stack;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutListContainerModule_ProvideListContainerFactory */
public final class C2839xab7007e4 implements Factory<NotificationListContainer> {
    private final Provider<NotificationStackScrollLayoutController> nsslControllerProvider;

    public C2839xab7007e4(Provider<NotificationStackScrollLayoutController> provider) {
        this.nsslControllerProvider = provider;
    }

    public NotificationListContainer get() {
        return provideListContainer(this.nsslControllerProvider.get());
    }

    public static C2839xab7007e4 create(Provider<NotificationStackScrollLayoutController> provider) {
        return new C2839xab7007e4(provider);
    }

    public static NotificationListContainer provideListContainer(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return (NotificationListContainer) Preconditions.checkNotNullFromProvides(NotificationStackScrollLayoutListContainerModule.provideListContainer(notificationStackScrollLayoutController));
    }
}
