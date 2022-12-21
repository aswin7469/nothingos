package com.android.keyguard;

import android.content.res.Resources;
import com.android.keyguard.clock.ClockManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class KeyguardClockSwitchController_Factory implements Factory<KeyguardClockSwitchController> {
    private final Provider<BatteryController> batteryControllerProvider;
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    private final Provider<ClockManager> clockManagerProvider;
    private final Provider<SysuiColorExtractor> colorExtractorProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<KeyguardClockSwitch> keyguardClockSwitchProvider;
    private final Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;
    private final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    private final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    private final Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
    private final Provider<Resources> resourcesProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<LockscreenSmartspaceController> smartspaceControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;
    private final Provider<Executor> uiExecutorProvider;

    public KeyguardClockSwitchController_Factory(Provider<KeyguardClockSwitch> provider, Provider<StatusBarStateController> provider2, Provider<SysuiColorExtractor> provider3, Provider<ClockManager> provider4, Provider<KeyguardSliceViewController> provider5, Provider<NotificationIconAreaController> provider6, Provider<BroadcastDispatcher> provider7, Provider<BatteryController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<LockscreenSmartspaceController> provider10, Provider<KeyguardUnlockAnimationController> provider11, Provider<SecureSettings> provider12, Provider<Executor> provider13, Provider<Resources> provider14, Provider<DumpManager> provider15) {
        this.keyguardClockSwitchProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.colorExtractorProvider = provider3;
        this.clockManagerProvider = provider4;
        this.keyguardSliceViewControllerProvider = provider5;
        this.notificationIconAreaControllerProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.batteryControllerProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.smartspaceControllerProvider = provider10;
        this.keyguardUnlockAnimationControllerProvider = provider11;
        this.secureSettingsProvider = provider12;
        this.uiExecutorProvider = provider13;
        this.resourcesProvider = provider14;
        this.dumpManagerProvider = provider15;
    }

    public KeyguardClockSwitchController get() {
        return newInstance(this.keyguardClockSwitchProvider.get(), this.statusBarStateControllerProvider.get(), this.colorExtractorProvider.get(), this.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), this.notificationIconAreaControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.batteryControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.smartspaceControllerProvider.get(), this.keyguardUnlockAnimationControllerProvider.get(), this.secureSettingsProvider.get(), this.uiExecutorProvider.get(), this.resourcesProvider.get(), this.dumpManagerProvider.get());
    }

    public static KeyguardClockSwitchController_Factory create(Provider<KeyguardClockSwitch> provider, Provider<StatusBarStateController> provider2, Provider<SysuiColorExtractor> provider3, Provider<ClockManager> provider4, Provider<KeyguardSliceViewController> provider5, Provider<NotificationIconAreaController> provider6, Provider<BroadcastDispatcher> provider7, Provider<BatteryController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<LockscreenSmartspaceController> provider10, Provider<KeyguardUnlockAnimationController> provider11, Provider<SecureSettings> provider12, Provider<Executor> provider13, Provider<Resources> provider14, Provider<DumpManager> provider15) {
        return new KeyguardClockSwitchController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15);
    }

    public static KeyguardClockSwitchController newInstance(KeyguardClockSwitch keyguardClockSwitch, StatusBarStateController statusBarStateController, SysuiColorExtractor sysuiColorExtractor, ClockManager clockManager, KeyguardSliceViewController keyguardSliceViewController, NotificationIconAreaController notificationIconAreaController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, LockscreenSmartspaceController lockscreenSmartspaceController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SecureSettings secureSettings, Executor executor, Resources resources, DumpManager dumpManager) {
        return new KeyguardClockSwitchController(keyguardClockSwitch, statusBarStateController, sysuiColorExtractor, clockManager, keyguardSliceViewController, notificationIconAreaController, broadcastDispatcher, batteryController, keyguardUpdateMonitor, lockscreenSmartspaceController, keyguardUnlockAnimationController, secureSettings, executor, resources, dumpManager);
    }
}
