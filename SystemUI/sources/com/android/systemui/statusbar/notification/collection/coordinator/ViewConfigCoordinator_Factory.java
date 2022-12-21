package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ViewConfigCoordinator_Factory implements Factory<ViewConfigCoordinator> {
    private final Provider<ConfigurationController> mConfigurationControllerProvider;
    private final Provider<NotificationGutsManager> mGutsManagerProvider;
    private final Provider<KeyguardUpdateMonitor> mKeyguardUpdateMonitorProvider;
    private final Provider<NotificationLockscreenUserManager> mLockscreenUserManagerProvider;

    public ViewConfigCoordinator_Factory(Provider<ConfigurationController> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationGutsManager> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        this.mConfigurationControllerProvider = provider;
        this.mLockscreenUserManagerProvider = provider2;
        this.mGutsManagerProvider = provider3;
        this.mKeyguardUpdateMonitorProvider = provider4;
    }

    public ViewConfigCoordinator get() {
        return newInstance(this.mConfigurationControllerProvider.get(), this.mLockscreenUserManagerProvider.get(), this.mGutsManagerProvider.get(), this.mKeyguardUpdateMonitorProvider.get());
    }

    public static ViewConfigCoordinator_Factory create(Provider<ConfigurationController> provider, Provider<NotificationLockscreenUserManager> provider2, Provider<NotificationGutsManager> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        return new ViewConfigCoordinator_Factory(provider, provider2, provider3, provider4);
    }

    public static ViewConfigCoordinator newInstance(ConfigurationController configurationController, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationGutsManager notificationGutsManager, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        return new ViewConfigCoordinator(configurationController, notificationLockscreenUserManager, notificationGutsManager, keyguardUpdateMonitor);
    }
}
