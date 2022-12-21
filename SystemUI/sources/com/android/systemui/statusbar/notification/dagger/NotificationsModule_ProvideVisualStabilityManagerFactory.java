package com.android.systemui.statusbar.notification.dagger;

import android.os.Handler;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NotificationsModule_ProvideVisualStabilityManagerFactory implements Factory<VisualStabilityManager> {
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Handler> handlerProvider;
    private final Provider<NotificationEntryManager> notificationEntryManagerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<VisualStabilityProvider> visualStabilityProvider;
    private final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public NotificationsModule_ProvideVisualStabilityManagerFactory(Provider<NotificationEntryManager> provider, Provider<VisualStabilityProvider> provider2, Provider<Handler> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5, Provider<DumpManager> provider6) {
        this.notificationEntryManagerProvider = provider;
        this.visualStabilityProvider = provider2;
        this.handlerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.wakefulnessLifecycleProvider = provider5;
        this.dumpManagerProvider = provider6;
    }

    public VisualStabilityManager get() {
        return provideVisualStabilityManager(this.notificationEntryManagerProvider.get(), this.visualStabilityProvider.get(), this.handlerProvider.get(), this.statusBarStateControllerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.dumpManagerProvider.get());
    }

    public static NotificationsModule_ProvideVisualStabilityManagerFactory create(Provider<NotificationEntryManager> provider, Provider<VisualStabilityProvider> provider2, Provider<Handler> provider3, Provider<StatusBarStateController> provider4, Provider<WakefulnessLifecycle> provider5, Provider<DumpManager> provider6) {
        return new NotificationsModule_ProvideVisualStabilityManagerFactory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static VisualStabilityManager provideVisualStabilityManager(NotificationEntryManager notificationEntryManager, VisualStabilityProvider visualStabilityProvider2, Handler handler, StatusBarStateController statusBarStateController, WakefulnessLifecycle wakefulnessLifecycle, DumpManager dumpManager) {
        return (VisualStabilityManager) Preconditions.checkNotNullFromProvides(NotificationsModule.provideVisualStabilityManager(notificationEntryManager, visualStabilityProvider2, handler, statusBarStateController, wakefulnessLifecycle, dumpManager));
    }
}
