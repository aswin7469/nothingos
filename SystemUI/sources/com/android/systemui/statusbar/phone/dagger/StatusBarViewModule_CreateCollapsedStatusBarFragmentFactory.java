package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory implements Factory<CollapsedStatusBarFragment> {
    private final Provider<SystemStatusAnimationScheduler> animationSchedulerProvider;
    private final Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
    private final Provider<CollapsedStatusBarFragmentLogger> collapsedStatusBarFragmentLoggerProvider;
    private final Provider<CommandQueue> commandQueueProvider;
    private final Provider<FeatureFlags> featureFlagsProvider;
    private final Provider<KeyguardStateController> keyguardStateControllerProvider;
    private final Provider<StatusBarLocationPublisher> locationPublisherProvider;
    private final Provider<Executor> mainExecutorProvider;
    private final Provider<NetworkController> networkControllerProvider;
    private final Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
    private final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    private final Provider<OngoingCallController> ongoingCallControllerProvider;
    private final Provider<OperatorNameViewController.Factory> operatorNameViewControllerFactoryProvider;
    private final Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
    private final Provider<SecureSettings> secureSettingsProvider;
    private final Provider<StatusBarFragmentComponent.Factory> statusBarFragmentComponentFactoryProvider;
    private final Provider<StatusBarHideIconsForBouncerManager> statusBarHideIconsForBouncerManagerProvider;
    private final Provider<StatusBarIconController> statusBarIconControllerProvider;
    private final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory(Provider<StatusBarFragmentComponent.Factory> provider, Provider<OngoingCallController> provider2, Provider<SystemStatusAnimationScheduler> provider3, Provider<StatusBarLocationPublisher> provider4, Provider<NotificationIconAreaController> provider5, Provider<PanelExpansionStateManager> provider6, Provider<FeatureFlags> provider7, Provider<StatusBarIconController> provider8, Provider<StatusBarHideIconsForBouncerManager> provider9, Provider<KeyguardStateController> provider10, Provider<NotificationPanelViewController> provider11, Provider<NetworkController> provider12, Provider<StatusBarStateController> provider13, Provider<CommandQueue> provider14, Provider<CarrierConfigTracker> provider15, Provider<CollapsedStatusBarFragmentLogger> provider16, Provider<OperatorNameViewController.Factory> provider17, Provider<SecureSettings> provider18, Provider<Executor> provider19) {
        this.statusBarFragmentComponentFactoryProvider = provider;
        this.ongoingCallControllerProvider = provider2;
        this.animationSchedulerProvider = provider3;
        this.locationPublisherProvider = provider4;
        this.notificationIconAreaControllerProvider = provider5;
        this.panelExpansionStateManagerProvider = provider6;
        this.featureFlagsProvider = provider7;
        this.statusBarIconControllerProvider = provider8;
        this.statusBarHideIconsForBouncerManagerProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
        this.notificationPanelViewControllerProvider = provider11;
        this.networkControllerProvider = provider12;
        this.statusBarStateControllerProvider = provider13;
        this.commandQueueProvider = provider14;
        this.carrierConfigTrackerProvider = provider15;
        this.collapsedStatusBarFragmentLoggerProvider = provider16;
        this.operatorNameViewControllerFactoryProvider = provider17;
        this.secureSettingsProvider = provider18;
        this.mainExecutorProvider = provider19;
    }

    public CollapsedStatusBarFragment get() {
        return createCollapsedStatusBarFragment(this.statusBarFragmentComponentFactoryProvider.get(), this.ongoingCallControllerProvider.get(), this.animationSchedulerProvider.get(), this.locationPublisherProvider.get(), this.notificationIconAreaControllerProvider.get(), this.panelExpansionStateManagerProvider.get(), this.featureFlagsProvider.get(), this.statusBarIconControllerProvider.get(), this.statusBarHideIconsForBouncerManagerProvider.get(), this.keyguardStateControllerProvider.get(), this.notificationPanelViewControllerProvider.get(), this.networkControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.commandQueueProvider.get(), this.carrierConfigTrackerProvider.get(), this.collapsedStatusBarFragmentLoggerProvider.get(), this.operatorNameViewControllerFactoryProvider.get(), this.secureSettingsProvider.get(), this.mainExecutorProvider.get());
    }

    public static StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory create(Provider<StatusBarFragmentComponent.Factory> provider, Provider<OngoingCallController> provider2, Provider<SystemStatusAnimationScheduler> provider3, Provider<StatusBarLocationPublisher> provider4, Provider<NotificationIconAreaController> provider5, Provider<PanelExpansionStateManager> provider6, Provider<FeatureFlags> provider7, Provider<StatusBarIconController> provider8, Provider<StatusBarHideIconsForBouncerManager> provider9, Provider<KeyguardStateController> provider10, Provider<NotificationPanelViewController> provider11, Provider<NetworkController> provider12, Provider<StatusBarStateController> provider13, Provider<CommandQueue> provider14, Provider<CarrierConfigTracker> provider15, Provider<CollapsedStatusBarFragmentLogger> provider16, Provider<OperatorNameViewController.Factory> provider17, Provider<SecureSettings> provider18, Provider<Executor> provider19) {
        return new StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19);
    }

    public static CollapsedStatusBarFragment createCollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CarrierConfigTracker carrierConfigTracker, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2, SecureSettings secureSettings, Executor executor) {
        return (CollapsedStatusBarFragment) Preconditions.checkNotNullFromProvides(StatusBarViewModule.createCollapsedStatusBarFragment(factory, ongoingCallController, systemStatusAnimationScheduler, statusBarLocationPublisher, notificationIconAreaController, panelExpansionStateManager, featureFlags, statusBarIconController, statusBarHideIconsForBouncerManager, keyguardStateController, notificationPanelViewController, networkController, statusBarStateController, commandQueue, carrierConfigTracker, collapsedStatusBarFragmentLogger, factory2, secureSettings, executor));
    }
}
