package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GutsCoordinator_Factory implements Factory<GutsCoordinator> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<GutsCoordinatorLogger> loggerProvider;
    private final Provider<NotifGutsViewManager> notifGutsViewManagerProvider;

    public GutsCoordinator_Factory(Provider<NotifGutsViewManager> provider, Provider<GutsCoordinatorLogger> provider2, Provider<DumpManager> provider3) {
        this.notifGutsViewManagerProvider = provider;
        this.loggerProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public GutsCoordinator get() {
        return newInstance(this.notifGutsViewManagerProvider.get(), this.loggerProvider.get(), this.dumpManagerProvider.get());
    }

    public static GutsCoordinator_Factory create(Provider<NotifGutsViewManager> provider, Provider<GutsCoordinatorLogger> provider2, Provider<DumpManager> provider3) {
        return new GutsCoordinator_Factory(provider, provider2, provider3);
    }

    public static GutsCoordinator newInstance(NotifGutsViewManager notifGutsViewManager, GutsCoordinatorLogger gutsCoordinatorLogger, DumpManager dumpManager) {
        return new GutsCoordinator(notifGutsViewManager, gutsCoordinatorLogger, dumpManager);
    }
}
