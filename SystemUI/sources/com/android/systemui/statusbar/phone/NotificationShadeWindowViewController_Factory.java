package com.android.systemui.statusbar.phone;

import com.android.keyguard.LockIconViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.tuner.TunerService;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class NotificationShadeWindowViewController_Factory implements Factory<NotificationShadeWindowViewController> {
    private final Provider<AmbientState> ambientStateProvider;
    private final Provider<CentralSurfaces> centralSurfacesProvider;
    private final Provider<NotificationShadeWindowController> controllerProvider;
    private final Provider<NotificationShadeDepthController> depthControllerProvider;
    private final Provider<DockManager> dockManagerProvider;
    private final Provider<FalsingCollector> falsingCollectorProvider;
    private final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    private final Provider<LockIconViewController> lockIconViewControllerProvider;
    private final Provider<Optional<LowLightClockController>> lowLightClockControllerProvider;
    private final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    private final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;
    private final Provider<NotificationStackScrollLayoutController> notificationStackScrollLayoutControllerProvider;
    private final Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
    private final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    private final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    private final Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
    private final Provider<LockscreenShadeTransitionController> transitionControllerProvider;
    private final Provider<TunerService> tunerServiceProvider;

    public NotificationShadeWindowViewController_Factory(Provider<LockscreenShadeTransitionController> provider, Provider<FalsingCollector> provider2, Provider<TunerService> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<DockManager> provider5, Provider<NotificationShadeDepthController> provider6, Provider<NotificationShadeWindowView> provider7, Provider<NotificationPanelViewController> provider8, Provider<PanelExpansionStateManager> provider9, Provider<NotificationStackScrollLayoutController> provider10, Provider<StatusBarKeyguardViewManager> provider11, Provider<StatusBarWindowStateController> provider12, Provider<LockIconViewController> provider13, Provider<Optional<LowLightClockController>> provider14, Provider<CentralSurfaces> provider15, Provider<NotificationShadeWindowController> provider16, Provider<KeyguardUnlockAnimationController> provider17, Provider<AmbientState> provider18) {
        this.transitionControllerProvider = provider;
        this.falsingCollectorProvider = provider2;
        this.tunerServiceProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.dockManagerProvider = provider5;
        this.depthControllerProvider = provider6;
        this.notificationShadeWindowViewProvider = provider7;
        this.notificationPanelViewControllerProvider = provider8;
        this.panelExpansionStateManagerProvider = provider9;
        this.notificationStackScrollLayoutControllerProvider = provider10;
        this.statusBarKeyguardViewManagerProvider = provider11;
        this.statusBarWindowStateControllerProvider = provider12;
        this.lockIconViewControllerProvider = provider13;
        this.lowLightClockControllerProvider = provider14;
        this.centralSurfacesProvider = provider15;
        this.controllerProvider = provider16;
        this.keyguardUnlockAnimationControllerProvider = provider17;
        this.ambientStateProvider = provider18;
    }

    public NotificationShadeWindowViewController get() {
        return newInstance(this.transitionControllerProvider.get(), this.falsingCollectorProvider.get(), this.tunerServiceProvider.get(), this.statusBarStateControllerProvider.get(), this.dockManagerProvider.get(), this.depthControllerProvider.get(), this.notificationShadeWindowViewProvider.get(), this.notificationPanelViewControllerProvider.get(), this.panelExpansionStateManagerProvider.get(), this.notificationStackScrollLayoutControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.statusBarWindowStateControllerProvider.get(), this.lockIconViewControllerProvider.get(), this.lowLightClockControllerProvider.get(), this.centralSurfacesProvider.get(), this.controllerProvider.get(), this.keyguardUnlockAnimationControllerProvider.get(), this.ambientStateProvider.get());
    }

    public static NotificationShadeWindowViewController_Factory create(Provider<LockscreenShadeTransitionController> provider, Provider<FalsingCollector> provider2, Provider<TunerService> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<DockManager> provider5, Provider<NotificationShadeDepthController> provider6, Provider<NotificationShadeWindowView> provider7, Provider<NotificationPanelViewController> provider8, Provider<PanelExpansionStateManager> provider9, Provider<NotificationStackScrollLayoutController> provider10, Provider<StatusBarKeyguardViewManager> provider11, Provider<StatusBarWindowStateController> provider12, Provider<LockIconViewController> provider13, Provider<Optional<LowLightClockController>> provider14, Provider<CentralSurfaces> provider15, Provider<NotificationShadeWindowController> provider16, Provider<KeyguardUnlockAnimationController> provider17, Provider<AmbientState> provider18) {
        return new NotificationShadeWindowViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static NotificationShadeWindowViewController newInstance(LockscreenShadeTransitionController lockscreenShadeTransitionController, FalsingCollector falsingCollector, TunerService tunerService, SysuiStatusBarStateController sysuiStatusBarStateController, DockManager dockManager, NotificationShadeDepthController notificationShadeDepthController, NotificationShadeWindowView notificationShadeWindowView, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, NotificationStackScrollLayoutController notificationStackScrollLayoutController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBarWindowStateController statusBarWindowStateController, LockIconViewController lockIconViewController, Optional<LowLightClockController> optional, CentralSurfaces centralSurfaces, NotificationShadeWindowController notificationShadeWindowController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, AmbientState ambientState) {
        return new NotificationShadeWindowViewController(lockscreenShadeTransitionController, falsingCollector, tunerService, sysuiStatusBarStateController, dockManager, notificationShadeDepthController, notificationShadeWindowView, notificationPanelViewController, panelExpansionStateManager, notificationStackScrollLayoutController, statusBarKeyguardViewManager, statusBarWindowStateController, lockIconViewController, optional, centralSurfaces, notificationShadeWindowController, keyguardUnlockAnimationController, ambientState);
    }
}
