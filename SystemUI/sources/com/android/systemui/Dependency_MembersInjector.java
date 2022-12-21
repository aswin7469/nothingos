package com.android.systemui;

import android.app.AlarmManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.clock.ClockManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.p012qs.ReduceBrightColorsController;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.power.PowerUI;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BluetoothController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.ExtensionController;
import com.android.systemui.statusbar.policy.FlashlightController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.statusbar.policy.SecurityController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.sensors.AsyncSensorManager;
import dagger.Lazy;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import java.util.concurrent.Executor;
import javax.inject.Named;
import javax.inject.Provider;

public final class Dependency_MembersInjector implements MembersInjector<Dependency> {
    private final Provider<AccessibilityButtonTargetsObserver> mAccessibilityButtonListControllerProvider;
    private final Provider<AccessibilityButtonModeObserver> mAccessibilityButtonModeObserverProvider;
    private final Provider<AccessibilityController> mAccessibilityControllerProvider;
    private final Provider<AccessibilityFloatingMenuController> mAccessibilityFloatingMenuControllerProvider;
    private final Provider<AccessibilityManagerWrapper> mAccessibilityManagerWrapperProvider;
    private final Provider<ActivityManagerWrapper> mActivityManagerWrapperProvider;
    private final Provider<ActivityStarter> mActivityStarterProvider;
    private final Provider<AlarmManager> mAlarmManagerProvider;
    private final Provider<AmbientState> mAmbientStateLazyProvider;
    private final Provider<AppOpsController> mAppOpsControllerProvider;
    private final Provider<AssistManager> mAssistManagerProvider;
    private final Provider<AsyncSensorManager> mAsyncSensorManagerProvider;
    private final Provider<AutoHideController> mAutoHideControllerProvider;
    private final Provider<Executor> mBackgroundExecutorProvider;
    private final Provider<BatteryController> mBatteryControllerProvider;
    private final Provider<Handler> mBgHandlerProvider;
    private final Provider<Looper> mBgLooperProvider;
    private final Provider<BluetoothController> mBluetoothControllerProvider;
    private final Provider<BroadcastDispatcher> mBroadcastDispatcherProvider;
    private final Provider<CastController> mCastControllerProvider;
    private final Provider<ClockManager> mClockManagerProvider;
    private final Provider<CommandQueue> mCommandQueueProvider;
    private final Provider<ConfigurationController> mConfigurationControllerProvider;
    private final Provider<StatusBarContentInsetsProvider> mContentInsetsProviderLazyProvider;
    private final Provider<DarkIconDispatcher> mDarkIconDispatcherProvider;
    private final Provider<DataSaverController> mDataSaverControllerProvider;
    private final Provider<DeviceConfigProxy> mDeviceConfigProxyProvider;
    private final Provider<DevicePolicyManagerWrapper> mDevicePolicyManagerWrapperProvider;
    private final Provider<DeviceProvisionedController> mDeviceProvisionedControllerProvider;
    private final Provider<DialogLaunchAnimator> mDialogLaunchAnimatorLazyProvider;
    private final Provider<DisplayMetrics> mDisplayMetricsProvider;
    private final Provider<DockManager> mDockManagerProvider;
    private final Provider<DozeParameters> mDozeParametersProvider;
    private final Provider<DumpManager> mDumpManagerProvider;
    private final Provider<EdgeBackGestureHandler.Factory> mEdgeBackGestureHandlerFactoryLazyProvider;
    private final Provider<EnhancedEstimates> mEnhancedEstimatesProvider;
    private final Provider<ExtensionController> mExtensionControllerProvider;
    private final Provider<FeatureFlags> mFeatureFlagsLazyProvider;
    private final Provider<FlashlightController> mFlashlightControllerProvider;
    private final Provider<ForegroundServiceController> mForegroundServiceControllerProvider;
    private final Provider<ForegroundServiceNotificationListener> mForegroundServiceNotificationListenerProvider;
    private final Provider<FragmentService> mFragmentServiceProvider;
    private final Provider<GarbageMonitor> mGarbageMonitorProvider;
    private final Provider<GroupExpansionManager> mGroupExpansionManagerLazyProvider;
    private final Provider<GroupMembershipManager> mGroupMembershipManagerLazyProvider;
    private final Provider<HdmiCecSetMenuLanguageHelper> mHdmiCecSetMenuLanguageHelperProvider;
    private final Provider<HotspotController> mHotspotControllerProvider;
    private final Provider<INotificationManager> mINotificationManagerProvider;
    private final Provider<IStatusBarService> mIStatusBarServiceProvider;
    private final Provider<IWindowManager> mIWindowManagerProvider;
    private final Provider<InternetDialogFactory> mInternetDialogFactoryProvider;
    private final Provider<KeyguardDismissUtil> mKeyguardDismissUtilProvider;
    private final Provider<NotificationEntryManager.KeyguardEnvironment> mKeyguardEnvironmentProvider;
    private final Provider<KeyguardStateController> mKeyguardMonitorProvider;
    private final Provider<KeyguardSecurityModel> mKeyguardSecurityModelProvider;
    private final Provider<KeyguardUpdateMonitor> mKeyguardUpdateMonitorProvider;
    private final Provider<LeakDetector> mLeakDetectorProvider;
    private final Provider<String> mLeakReportEmailProvider;
    private final Provider<LeakReporter> mLeakReporterProvider;
    private final Provider<LightBarController> mLightBarControllerProvider;
    private final Provider<LocalBluetoothManager> mLocalBluetoothManagerProvider;
    private final Provider<LocationController> mLocationControllerProvider;
    private final Provider<LockscreenGestureLogger> mLockscreenGestureLoggerProvider;
    private final Provider<Executor> mMainExecutorProvider;
    private final Provider<Handler> mMainHandlerProvider;
    private final Provider<Looper> mMainLooperProvider;
    private final Provider<ManagedProfileController> mManagedProfileControllerProvider;
    private final Provider<MediaOutputDialogFactory> mMediaOutputDialogFactoryProvider;
    private final Provider<MetricsLogger> mMetricsLoggerProvider;
    private final Provider<NavigationModeController> mNavBarModeControllerProvider;
    private final Provider<NavigationBarController> mNavigationBarControllerProvider;
    private final Provider<NextAlarmController> mNextAlarmControllerProvider;
    private final Provider<NightDisplayListener> mNightDisplayListenerProvider;
    private final Provider<NotificationEntryManager> mNotificationEntryManagerProvider;
    private final Provider<NotificationFilter> mNotificationFilterProvider;
    private final Provider<NotificationGroupAlertTransferHelper> mNotificationGroupAlertTransferHelperProvider;
    private final Provider<NotificationGroupManagerLegacy> mNotificationGroupManagerProvider;
    private final Provider<NotificationGutsManager> mNotificationGutsManagerProvider;
    private final Provider<NotificationListener> mNotificationListenerProvider;
    private final Provider<NotificationLockscreenUserManager> mNotificationLockscreenUserManagerProvider;
    private final Provider<NotificationLogger> mNotificationLoggerProvider;
    private final Provider<NotificationMediaManager> mNotificationMediaManagerProvider;
    private final Provider<NotificationRemoteInputManager.Callback> mNotificationRemoteInputManagerCallbackProvider;
    private final Provider<NotificationRemoteInputManager> mNotificationRemoteInputManagerProvider;
    private final Provider<NotificationSectionsManager> mNotificationSectionsManagerLazyProvider;
    private final Provider<NotificationShadeWindowController> mNotificationShadeWindowControllerProvider;
    private final Provider<NotificationViewHierarchyManager> mNotificationViewHierarchyManagerProvider;
    private final Provider<OverviewProxyService> mOverviewProxyServiceProvider;
    private final Provider<PackageManagerWrapper> mPackageManagerWrapperProvider;
    private final Provider<PluginDependencyProvider> mPluginDependencyProvider;
    private final Provider<PluginManager> mPluginManagerProvider;
    private final Provider<PrivacyDotViewController> mPrivacyDotViewControllerLazyProvider;
    private final Provider<PrivacyItemController> mPrivacyItemControllerProvider;
    private final Provider<ProtoTracer> mProtoTracerProvider;
    private final Provider<RecordingController> mRecordingControllerProvider;
    private final Provider<ReduceBrightColorsController> mReduceBrightColorsControllerProvider;
    private final Provider<RemoteInputQuickSettingsDisabler> mRemoteInputQuickSettingsDisablerProvider;
    private final Provider<RotationLockController> mRotationLockControllerProvider;
    private final Provider<ScreenLifecycle> mScreenLifecycleProvider;
    private final Provider<ScreenOffAnimationController> mScreenOffAnimationControllerProvider;
    private final Provider<SecurityController> mSecurityControllerProvider;
    private final Provider<SensorPrivacyController> mSensorPrivacyControllerProvider;
    private final Provider<SensorPrivacyManager> mSensorPrivacyManagerProvider;
    private final Provider<ShadeController> mShadeControllerProvider;
    private final Provider<SmartReplyConstants> mSmartReplyConstantsProvider;
    private final Provider<SmartReplyController> mSmartReplyControllerProvider;
    private final Provider<StatusBarIconController> mStatusBarIconControllerProvider;
    private final Provider<StatusBarStateController> mStatusBarStateControllerProvider;
    private final Provider<SysUiState> mSysUiStateFlagsContainerProvider;
    private final Provider<SystemStatusAnimationScheduler> mSystemStatusAnimationSchedulerLazyProvider;
    private final Provider<SystemUIDialogManager> mSystemUIDialogManagerLazyProvider;
    private final Provider<SysuiColorExtractor> mSysuiColorExtractorProvider;
    private final Provider<TelephonyListenerManager> mTelephonyListenerManagerProvider;
    private final Provider<StatusBarWindowController> mTempStatusBarWindowControllerProvider;
    private final Provider<Handler> mTimeTickHandlerProvider;
    private final Provider<TunablePadding.TunablePaddingService> mTunablePaddingServiceProvider;
    private final Provider<TunerService> mTunerServiceProvider;
    private final Provider<UiEventLogger> mUiEventLoggerProvider;
    private final Provider<UiOffloadThread> mUiOffloadThreadProvider;
    private final Provider<UserInfoController> mUserInfoControllerProvider;
    private final Provider<UserSwitcherController> mUserSwitcherControllerProvider;
    private final Provider<VibratorHelper> mVibratorHelperProvider;
    private final Provider<VisualStabilityManager> mVisualStabilityManagerProvider;
    private final Provider<VolumeDialogController> mVolumeDialogControllerProvider;
    private final Provider<WakefulnessLifecycle> mWakefulnessLifecycleProvider;
    private final Provider<IWallpaperManager> mWallpaperManagerProvider;
    private final Provider<PowerUI.WarningsUI> mWarningsUIProvider;
    private final Provider<ZenModeController> mZenModeControllerProvider;

    public Dependency_MembersInjector(Provider<DumpManager> provider, Provider<ActivityStarter> provider2, Provider<BroadcastDispatcher> provider3, Provider<AsyncSensorManager> provider4, Provider<BluetoothController> provider5, Provider<LocationController> provider6, Provider<RotationLockController> provider7, Provider<ZenModeController> provider8, Provider<HdmiCecSetMenuLanguageHelper> provider9, Provider<HotspotController> provider10, Provider<CastController> provider11, Provider<FlashlightController> provider12, Provider<UserSwitcherController> provider13, Provider<UserInfoController> provider14, Provider<KeyguardStateController> provider15, Provider<KeyguardUpdateMonitor> provider16, Provider<BatteryController> provider17, Provider<NightDisplayListener> provider18, Provider<ReduceBrightColorsController> provider19, Provider<ManagedProfileController> provider20, Provider<NextAlarmController> provider21, Provider<DataSaverController> provider22, Provider<AccessibilityController> provider23, Provider<DeviceProvisionedController> provider24, Provider<PluginManager> provider25, Provider<AssistManager> provider26, Provider<SecurityController> provider27, Provider<LeakDetector> provider28, Provider<LeakReporter> provider29, Provider<GarbageMonitor> provider30, Provider<TunerService> provider31, Provider<NotificationShadeWindowController> provider32, Provider<StatusBarWindowController> provider33, Provider<DarkIconDispatcher> provider34, Provider<ConfigurationController> provider35, Provider<StatusBarIconController> provider36, Provider<ScreenLifecycle> provider37, Provider<WakefulnessLifecycle> provider38, Provider<FragmentService> provider39, Provider<ExtensionController> provider40, Provider<PluginDependencyProvider> provider41, Provider<LocalBluetoothManager> provider42, Provider<VolumeDialogController> provider43, Provider<MetricsLogger> provider44, Provider<AccessibilityManagerWrapper> provider45, Provider<SysuiColorExtractor> provider46, Provider<TunablePadding.TunablePaddingService> provider47, Provider<ForegroundServiceController> provider48, Provider<UiOffloadThread> provider49, Provider<PowerUI.WarningsUI> provider50, Provider<LightBarController> provider51, Provider<IWindowManager> provider52, Provider<OverviewProxyService> provider53, Provider<NavigationModeController> provider54, Provider<AccessibilityButtonModeObserver> provider55, Provider<AccessibilityButtonTargetsObserver> provider56, Provider<EnhancedEstimates> provider57, Provider<VibratorHelper> provider58, Provider<IStatusBarService> provider59, Provider<DisplayMetrics> provider60, Provider<LockscreenGestureLogger> provider61, Provider<NotificationEntryManager.KeyguardEnvironment> provider62, Provider<ShadeController> provider63, Provider<NotificationRemoteInputManager.Callback> provider64, Provider<AppOpsController> provider65, Provider<NavigationBarController> provider66, Provider<AccessibilityFloatingMenuController> provider67, Provider<StatusBarStateController> provider68, Provider<NotificationLockscreenUserManager> provider69, Provider<NotificationGroupAlertTransferHelper> provider70, Provider<NotificationGroupManagerLegacy> provider71, Provider<VisualStabilityManager> provider72, Provider<NotificationGutsManager> provider73, Provider<NotificationMediaManager> provider74, Provider<NotificationRemoteInputManager> provider75, Provider<SmartReplyConstants> provider76, Provider<NotificationListener> provider77, Provider<NotificationLogger> provider78, Provider<NotificationViewHierarchyManager> provider79, Provider<NotificationFilter> provider80, Provider<KeyguardDismissUtil> provider81, Provider<SmartReplyController> provider82, Provider<RemoteInputQuickSettingsDisabler> provider83, Provider<NotificationEntryManager> provider84, Provider<SensorPrivacyManager> provider85, Provider<AutoHideController> provider86, Provider<ForegroundServiceNotificationListener> provider87, Provider<PrivacyItemController> provider88, Provider<Looper> provider89, Provider<Handler> provider90, Provider<Looper> provider91, Provider<Handler> provider92, Provider<Handler> provider93, Provider<String> provider94, Provider<Executor> provider95, Provider<Executor> provider96, Provider<ClockManager> provider97, Provider<ActivityManagerWrapper> provider98, Provider<DevicePolicyManagerWrapper> provider99, Provider<PackageManagerWrapper> provider100, Provider<SensorPrivacyController> provider101, Provider<DockManager> provider102, Provider<INotificationManager> provider103, Provider<SysUiState> provider104, Provider<AlarmManager> provider105, Provider<KeyguardSecurityModel> provider106, Provider<DozeParameters> provider107, Provider<IWallpaperManager> provider108, Provider<CommandQueue> provider109, Provider<RecordingController> provider110, Provider<ProtoTracer> provider111, Provider<MediaOutputDialogFactory> provider112, Provider<DeviceConfigProxy> provider113, Provider<TelephonyListenerManager> provider114, Provider<SystemStatusAnimationScheduler> provider115, Provider<PrivacyDotViewController> provider116, Provider<EdgeBackGestureHandler.Factory> provider117, Provider<UiEventLogger> provider118, Provider<StatusBarContentInsetsProvider> provider119, Provider<InternetDialogFactory> provider120, Provider<FeatureFlags> provider121, Provider<NotificationSectionsManager> provider122, Provider<ScreenOffAnimationController> provider123, Provider<AmbientState> provider124, Provider<GroupMembershipManager> provider125, Provider<GroupExpansionManager> provider126, Provider<SystemUIDialogManager> provider127, Provider<DialogLaunchAnimator> provider128) {
        this.mDumpManagerProvider = provider;
        this.mActivityStarterProvider = provider2;
        this.mBroadcastDispatcherProvider = provider3;
        this.mAsyncSensorManagerProvider = provider4;
        this.mBluetoothControllerProvider = provider5;
        this.mLocationControllerProvider = provider6;
        this.mRotationLockControllerProvider = provider7;
        this.mZenModeControllerProvider = provider8;
        this.mHdmiCecSetMenuLanguageHelperProvider = provider9;
        this.mHotspotControllerProvider = provider10;
        this.mCastControllerProvider = provider11;
        this.mFlashlightControllerProvider = provider12;
        this.mUserSwitcherControllerProvider = provider13;
        this.mUserInfoControllerProvider = provider14;
        this.mKeyguardMonitorProvider = provider15;
        this.mKeyguardUpdateMonitorProvider = provider16;
        this.mBatteryControllerProvider = provider17;
        this.mNightDisplayListenerProvider = provider18;
        this.mReduceBrightColorsControllerProvider = provider19;
        this.mManagedProfileControllerProvider = provider20;
        this.mNextAlarmControllerProvider = provider21;
        this.mDataSaverControllerProvider = provider22;
        this.mAccessibilityControllerProvider = provider23;
        this.mDeviceProvisionedControllerProvider = provider24;
        this.mPluginManagerProvider = provider25;
        this.mAssistManagerProvider = provider26;
        this.mSecurityControllerProvider = provider27;
        this.mLeakDetectorProvider = provider28;
        this.mLeakReporterProvider = provider29;
        this.mGarbageMonitorProvider = provider30;
        this.mTunerServiceProvider = provider31;
        this.mNotificationShadeWindowControllerProvider = provider32;
        this.mTempStatusBarWindowControllerProvider = provider33;
        this.mDarkIconDispatcherProvider = provider34;
        this.mConfigurationControllerProvider = provider35;
        this.mStatusBarIconControllerProvider = provider36;
        this.mScreenLifecycleProvider = provider37;
        this.mWakefulnessLifecycleProvider = provider38;
        this.mFragmentServiceProvider = provider39;
        this.mExtensionControllerProvider = provider40;
        this.mPluginDependencyProvider = provider41;
        this.mLocalBluetoothManagerProvider = provider42;
        this.mVolumeDialogControllerProvider = provider43;
        this.mMetricsLoggerProvider = provider44;
        this.mAccessibilityManagerWrapperProvider = provider45;
        this.mSysuiColorExtractorProvider = provider46;
        this.mTunablePaddingServiceProvider = provider47;
        this.mForegroundServiceControllerProvider = provider48;
        this.mUiOffloadThreadProvider = provider49;
        this.mWarningsUIProvider = provider50;
        this.mLightBarControllerProvider = provider51;
        this.mIWindowManagerProvider = provider52;
        this.mOverviewProxyServiceProvider = provider53;
        this.mNavBarModeControllerProvider = provider54;
        this.mAccessibilityButtonModeObserverProvider = provider55;
        this.mAccessibilityButtonListControllerProvider = provider56;
        this.mEnhancedEstimatesProvider = provider57;
        this.mVibratorHelperProvider = provider58;
        this.mIStatusBarServiceProvider = provider59;
        this.mDisplayMetricsProvider = provider60;
        this.mLockscreenGestureLoggerProvider = provider61;
        this.mKeyguardEnvironmentProvider = provider62;
        this.mShadeControllerProvider = provider63;
        this.mNotificationRemoteInputManagerCallbackProvider = provider64;
        this.mAppOpsControllerProvider = provider65;
        this.mNavigationBarControllerProvider = provider66;
        this.mAccessibilityFloatingMenuControllerProvider = provider67;
        this.mStatusBarStateControllerProvider = provider68;
        this.mNotificationLockscreenUserManagerProvider = provider69;
        this.mNotificationGroupAlertTransferHelperProvider = provider70;
        this.mNotificationGroupManagerProvider = provider71;
        this.mVisualStabilityManagerProvider = provider72;
        this.mNotificationGutsManagerProvider = provider73;
        this.mNotificationMediaManagerProvider = provider74;
        this.mNotificationRemoteInputManagerProvider = provider75;
        this.mSmartReplyConstantsProvider = provider76;
        this.mNotificationListenerProvider = provider77;
        this.mNotificationLoggerProvider = provider78;
        this.mNotificationViewHierarchyManagerProvider = provider79;
        this.mNotificationFilterProvider = provider80;
        this.mKeyguardDismissUtilProvider = provider81;
        this.mSmartReplyControllerProvider = provider82;
        this.mRemoteInputQuickSettingsDisablerProvider = provider83;
        this.mNotificationEntryManagerProvider = provider84;
        this.mSensorPrivacyManagerProvider = provider85;
        this.mAutoHideControllerProvider = provider86;
        this.mForegroundServiceNotificationListenerProvider = provider87;
        this.mPrivacyItemControllerProvider = provider88;
        this.mBgLooperProvider = provider89;
        this.mBgHandlerProvider = provider90;
        this.mMainLooperProvider = provider91;
        this.mMainHandlerProvider = provider92;
        this.mTimeTickHandlerProvider = provider93;
        this.mLeakReportEmailProvider = provider94;
        this.mMainExecutorProvider = provider95;
        this.mBackgroundExecutorProvider = provider96;
        this.mClockManagerProvider = provider97;
        this.mActivityManagerWrapperProvider = provider98;
        this.mDevicePolicyManagerWrapperProvider = provider99;
        this.mPackageManagerWrapperProvider = provider100;
        this.mSensorPrivacyControllerProvider = provider101;
        this.mDockManagerProvider = provider102;
        this.mINotificationManagerProvider = provider103;
        this.mSysUiStateFlagsContainerProvider = provider104;
        this.mAlarmManagerProvider = provider105;
        this.mKeyguardSecurityModelProvider = provider106;
        this.mDozeParametersProvider = provider107;
        this.mWallpaperManagerProvider = provider108;
        this.mCommandQueueProvider = provider109;
        this.mRecordingControllerProvider = provider110;
        this.mProtoTracerProvider = provider111;
        this.mMediaOutputDialogFactoryProvider = provider112;
        this.mDeviceConfigProxyProvider = provider113;
        this.mTelephonyListenerManagerProvider = provider114;
        this.mSystemStatusAnimationSchedulerLazyProvider = provider115;
        this.mPrivacyDotViewControllerLazyProvider = provider116;
        this.mEdgeBackGestureHandlerFactoryLazyProvider = provider117;
        this.mUiEventLoggerProvider = provider118;
        this.mContentInsetsProviderLazyProvider = provider119;
        this.mInternetDialogFactoryProvider = provider120;
        this.mFeatureFlagsLazyProvider = provider121;
        this.mNotificationSectionsManagerLazyProvider = provider122;
        this.mScreenOffAnimationControllerProvider = provider123;
        this.mAmbientStateLazyProvider = provider124;
        this.mGroupMembershipManagerLazyProvider = provider125;
        this.mGroupExpansionManagerLazyProvider = provider126;
        this.mSystemUIDialogManagerLazyProvider = provider127;
        this.mDialogLaunchAnimatorLazyProvider = provider128;
    }

    public static MembersInjector<Dependency> create(Provider<DumpManager> provider, Provider<ActivityStarter> provider2, Provider<BroadcastDispatcher> provider3, Provider<AsyncSensorManager> provider4, Provider<BluetoothController> provider5, Provider<LocationController> provider6, Provider<RotationLockController> provider7, Provider<ZenModeController> provider8, Provider<HdmiCecSetMenuLanguageHelper> provider9, Provider<HotspotController> provider10, Provider<CastController> provider11, Provider<FlashlightController> provider12, Provider<UserSwitcherController> provider13, Provider<UserInfoController> provider14, Provider<KeyguardStateController> provider15, Provider<KeyguardUpdateMonitor> provider16, Provider<BatteryController> provider17, Provider<NightDisplayListener> provider18, Provider<ReduceBrightColorsController> provider19, Provider<ManagedProfileController> provider20, Provider<NextAlarmController> provider21, Provider<DataSaverController> provider22, Provider<AccessibilityController> provider23, Provider<DeviceProvisionedController> provider24, Provider<PluginManager> provider25, Provider<AssistManager> provider26, Provider<SecurityController> provider27, Provider<LeakDetector> provider28, Provider<LeakReporter> provider29, Provider<GarbageMonitor> provider30, Provider<TunerService> provider31, Provider<NotificationShadeWindowController> provider32, Provider<StatusBarWindowController> provider33, Provider<DarkIconDispatcher> provider34, Provider<ConfigurationController> provider35, Provider<StatusBarIconController> provider36, Provider<ScreenLifecycle> provider37, Provider<WakefulnessLifecycle> provider38, Provider<FragmentService> provider39, Provider<ExtensionController> provider40, Provider<PluginDependencyProvider> provider41, Provider<LocalBluetoothManager> provider42, Provider<VolumeDialogController> provider43, Provider<MetricsLogger> provider44, Provider<AccessibilityManagerWrapper> provider45, Provider<SysuiColorExtractor> provider46, Provider<TunablePadding.TunablePaddingService> provider47, Provider<ForegroundServiceController> provider48, Provider<UiOffloadThread> provider49, Provider<PowerUI.WarningsUI> provider50, Provider<LightBarController> provider51, Provider<IWindowManager> provider52, Provider<OverviewProxyService> provider53, Provider<NavigationModeController> provider54, Provider<AccessibilityButtonModeObserver> provider55, Provider<AccessibilityButtonTargetsObserver> provider56, Provider<EnhancedEstimates> provider57, Provider<VibratorHelper> provider58, Provider<IStatusBarService> provider59, Provider<DisplayMetrics> provider60, Provider<LockscreenGestureLogger> provider61, Provider<NotificationEntryManager.KeyguardEnvironment> provider62, Provider<ShadeController> provider63, Provider<NotificationRemoteInputManager.Callback> provider64, Provider<AppOpsController> provider65, Provider<NavigationBarController> provider66, Provider<AccessibilityFloatingMenuController> provider67, Provider<StatusBarStateController> provider68, Provider<NotificationLockscreenUserManager> provider69, Provider<NotificationGroupAlertTransferHelper> provider70, Provider<NotificationGroupManagerLegacy> provider71, Provider<VisualStabilityManager> provider72, Provider<NotificationGutsManager> provider73, Provider<NotificationMediaManager> provider74, Provider<NotificationRemoteInputManager> provider75, Provider<SmartReplyConstants> provider76, Provider<NotificationListener> provider77, Provider<NotificationLogger> provider78, Provider<NotificationViewHierarchyManager> provider79, Provider<NotificationFilter> provider80, Provider<KeyguardDismissUtil> provider81, Provider<SmartReplyController> provider82, Provider<RemoteInputQuickSettingsDisabler> provider83, Provider<NotificationEntryManager> provider84, Provider<SensorPrivacyManager> provider85, Provider<AutoHideController> provider86, Provider<ForegroundServiceNotificationListener> provider87, Provider<PrivacyItemController> provider88, Provider<Looper> provider89, Provider<Handler> provider90, Provider<Looper> provider91, Provider<Handler> provider92, Provider<Handler> provider93, Provider<String> provider94, Provider<Executor> provider95, Provider<Executor> provider96, Provider<ClockManager> provider97, Provider<ActivityManagerWrapper> provider98, Provider<DevicePolicyManagerWrapper> provider99, Provider<PackageManagerWrapper> provider100, Provider<SensorPrivacyController> provider101, Provider<DockManager> provider102, Provider<INotificationManager> provider103, Provider<SysUiState> provider104, Provider<AlarmManager> provider105, Provider<KeyguardSecurityModel> provider106, Provider<DozeParameters> provider107, Provider<IWallpaperManager> provider108, Provider<CommandQueue> provider109, Provider<RecordingController> provider110, Provider<ProtoTracer> provider111, Provider<MediaOutputDialogFactory> provider112, Provider<DeviceConfigProxy> provider113, Provider<TelephonyListenerManager> provider114, Provider<SystemStatusAnimationScheduler> provider115, Provider<PrivacyDotViewController> provider116, Provider<EdgeBackGestureHandler.Factory> provider117, Provider<UiEventLogger> provider118, Provider<StatusBarContentInsetsProvider> provider119, Provider<InternetDialogFactory> provider120, Provider<FeatureFlags> provider121, Provider<NotificationSectionsManager> provider122, Provider<ScreenOffAnimationController> provider123, Provider<AmbientState> provider124, Provider<GroupMembershipManager> provider125, Provider<GroupExpansionManager> provider126, Provider<SystemUIDialogManager> provider127, Provider<DialogLaunchAnimator> provider128) {
        return new Dependency_MembersInjector(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39, provider40, provider41, provider42, provider43, provider44, provider45, provider46, provider47, provider48, provider49, provider50, provider51, provider52, provider53, provider54, provider55, provider56, provider57, provider58, provider59, provider60, provider61, provider62, provider63, provider64, provider65, provider66, provider67, provider68, provider69, provider70, provider71, provider72, provider73, provider74, provider75, provider76, provider77, provider78, provider79, provider80, provider81, provider82, provider83, provider84, provider85, provider86, provider87, provider88, provider89, provider90, provider91, provider92, provider93, provider94, provider95, provider96, provider97, provider98, provider99, provider100, provider101, provider102, provider103, provider104, provider105, provider106, provider107, provider108, provider109, provider110, provider111, provider112, provider113, provider114, provider115, provider116, provider117, provider118, provider119, provider120, provider121, provider122, provider123, provider124, provider125, provider126, provider127, provider128);
    }

    public void injectMembers(Dependency dependency) {
        injectMDumpManager(dependency, this.mDumpManagerProvider.get());
        injectMActivityStarter(dependency, DoubleCheck.lazy(this.mActivityStarterProvider));
        injectMBroadcastDispatcher(dependency, DoubleCheck.lazy(this.mBroadcastDispatcherProvider));
        injectMAsyncSensorManager(dependency, DoubleCheck.lazy(this.mAsyncSensorManagerProvider));
        injectMBluetoothController(dependency, DoubleCheck.lazy(this.mBluetoothControllerProvider));
        injectMLocationController(dependency, DoubleCheck.lazy(this.mLocationControllerProvider));
        injectMRotationLockController(dependency, DoubleCheck.lazy(this.mRotationLockControllerProvider));
        injectMZenModeController(dependency, DoubleCheck.lazy(this.mZenModeControllerProvider));
        injectMHdmiCecSetMenuLanguageHelper(dependency, DoubleCheck.lazy(this.mHdmiCecSetMenuLanguageHelperProvider));
        injectMHotspotController(dependency, DoubleCheck.lazy(this.mHotspotControllerProvider));
        injectMCastController(dependency, DoubleCheck.lazy(this.mCastControllerProvider));
        injectMFlashlightController(dependency, DoubleCheck.lazy(this.mFlashlightControllerProvider));
        injectMUserSwitcherController(dependency, DoubleCheck.lazy(this.mUserSwitcherControllerProvider));
        injectMUserInfoController(dependency, DoubleCheck.lazy(this.mUserInfoControllerProvider));
        injectMKeyguardMonitor(dependency, DoubleCheck.lazy(this.mKeyguardMonitorProvider));
        injectMKeyguardUpdateMonitor(dependency, DoubleCheck.lazy(this.mKeyguardUpdateMonitorProvider));
        injectMBatteryController(dependency, DoubleCheck.lazy(this.mBatteryControllerProvider));
        injectMNightDisplayListener(dependency, DoubleCheck.lazy(this.mNightDisplayListenerProvider));
        injectMReduceBrightColorsController(dependency, DoubleCheck.lazy(this.mReduceBrightColorsControllerProvider));
        injectMManagedProfileController(dependency, DoubleCheck.lazy(this.mManagedProfileControllerProvider));
        injectMNextAlarmController(dependency, DoubleCheck.lazy(this.mNextAlarmControllerProvider));
        injectMDataSaverController(dependency, DoubleCheck.lazy(this.mDataSaverControllerProvider));
        injectMAccessibilityController(dependency, DoubleCheck.lazy(this.mAccessibilityControllerProvider));
        injectMDeviceProvisionedController(dependency, DoubleCheck.lazy(this.mDeviceProvisionedControllerProvider));
        injectMPluginManager(dependency, DoubleCheck.lazy(this.mPluginManagerProvider));
        injectMAssistManager(dependency, DoubleCheck.lazy(this.mAssistManagerProvider));
        injectMSecurityController(dependency, DoubleCheck.lazy(this.mSecurityControllerProvider));
        injectMLeakDetector(dependency, DoubleCheck.lazy(this.mLeakDetectorProvider));
        injectMLeakReporter(dependency, DoubleCheck.lazy(this.mLeakReporterProvider));
        injectMGarbageMonitor(dependency, DoubleCheck.lazy(this.mGarbageMonitorProvider));
        injectMTunerService(dependency, DoubleCheck.lazy(this.mTunerServiceProvider));
        injectMNotificationShadeWindowController(dependency, DoubleCheck.lazy(this.mNotificationShadeWindowControllerProvider));
        injectMTempStatusBarWindowController(dependency, DoubleCheck.lazy(this.mTempStatusBarWindowControllerProvider));
        injectMDarkIconDispatcher(dependency, DoubleCheck.lazy(this.mDarkIconDispatcherProvider));
        injectMConfigurationController(dependency, DoubleCheck.lazy(this.mConfigurationControllerProvider));
        injectMStatusBarIconController(dependency, DoubleCheck.lazy(this.mStatusBarIconControllerProvider));
        injectMScreenLifecycle(dependency, DoubleCheck.lazy(this.mScreenLifecycleProvider));
        injectMWakefulnessLifecycle(dependency, DoubleCheck.lazy(this.mWakefulnessLifecycleProvider));
        injectMFragmentService(dependency, DoubleCheck.lazy(this.mFragmentServiceProvider));
        injectMExtensionController(dependency, DoubleCheck.lazy(this.mExtensionControllerProvider));
        injectMPluginDependencyProvider(dependency, DoubleCheck.lazy(this.mPluginDependencyProvider));
        injectMLocalBluetoothManager(dependency, DoubleCheck.lazy(this.mLocalBluetoothManagerProvider));
        injectMVolumeDialogController(dependency, DoubleCheck.lazy(this.mVolumeDialogControllerProvider));
        injectMMetricsLogger(dependency, DoubleCheck.lazy(this.mMetricsLoggerProvider));
        injectMAccessibilityManagerWrapper(dependency, DoubleCheck.lazy(this.mAccessibilityManagerWrapperProvider));
        injectMSysuiColorExtractor(dependency, DoubleCheck.lazy(this.mSysuiColorExtractorProvider));
        injectMTunablePaddingService(dependency, DoubleCheck.lazy(this.mTunablePaddingServiceProvider));
        injectMForegroundServiceController(dependency, DoubleCheck.lazy(this.mForegroundServiceControllerProvider));
        injectMUiOffloadThread(dependency, DoubleCheck.lazy(this.mUiOffloadThreadProvider));
        injectMWarningsUI(dependency, DoubleCheck.lazy(this.mWarningsUIProvider));
        injectMLightBarController(dependency, DoubleCheck.lazy(this.mLightBarControllerProvider));
        injectMIWindowManager(dependency, DoubleCheck.lazy(this.mIWindowManagerProvider));
        injectMOverviewProxyService(dependency, DoubleCheck.lazy(this.mOverviewProxyServiceProvider));
        injectMNavBarModeController(dependency, DoubleCheck.lazy(this.mNavBarModeControllerProvider));
        injectMAccessibilityButtonModeObserver(dependency, DoubleCheck.lazy(this.mAccessibilityButtonModeObserverProvider));
        injectMAccessibilityButtonListController(dependency, DoubleCheck.lazy(this.mAccessibilityButtonListControllerProvider));
        injectMEnhancedEstimates(dependency, DoubleCheck.lazy(this.mEnhancedEstimatesProvider));
        injectMVibratorHelper(dependency, DoubleCheck.lazy(this.mVibratorHelperProvider));
        injectMIStatusBarService(dependency, DoubleCheck.lazy(this.mIStatusBarServiceProvider));
        injectMDisplayMetrics(dependency, DoubleCheck.lazy(this.mDisplayMetricsProvider));
        injectMLockscreenGestureLogger(dependency, DoubleCheck.lazy(this.mLockscreenGestureLoggerProvider));
        injectMKeyguardEnvironment(dependency, DoubleCheck.lazy(this.mKeyguardEnvironmentProvider));
        injectMShadeController(dependency, DoubleCheck.lazy(this.mShadeControllerProvider));
        injectMNotificationRemoteInputManagerCallback(dependency, DoubleCheck.lazy(this.mNotificationRemoteInputManagerCallbackProvider));
        injectMAppOpsController(dependency, DoubleCheck.lazy(this.mAppOpsControllerProvider));
        injectMNavigationBarController(dependency, DoubleCheck.lazy(this.mNavigationBarControllerProvider));
        injectMAccessibilityFloatingMenuController(dependency, DoubleCheck.lazy(this.mAccessibilityFloatingMenuControllerProvider));
        injectMStatusBarStateController(dependency, DoubleCheck.lazy(this.mStatusBarStateControllerProvider));
        injectMNotificationLockscreenUserManager(dependency, DoubleCheck.lazy(this.mNotificationLockscreenUserManagerProvider));
        injectMNotificationGroupAlertTransferHelper(dependency, DoubleCheck.lazy(this.mNotificationGroupAlertTransferHelperProvider));
        injectMNotificationGroupManager(dependency, DoubleCheck.lazy(this.mNotificationGroupManagerProvider));
        injectMVisualStabilityManager(dependency, DoubleCheck.lazy(this.mVisualStabilityManagerProvider));
        injectMNotificationGutsManager(dependency, DoubleCheck.lazy(this.mNotificationGutsManagerProvider));
        injectMNotificationMediaManager(dependency, DoubleCheck.lazy(this.mNotificationMediaManagerProvider));
        injectMNotificationRemoteInputManager(dependency, DoubleCheck.lazy(this.mNotificationRemoteInputManagerProvider));
        injectMSmartReplyConstants(dependency, DoubleCheck.lazy(this.mSmartReplyConstantsProvider));
        injectMNotificationListener(dependency, DoubleCheck.lazy(this.mNotificationListenerProvider));
        injectMNotificationLogger(dependency, DoubleCheck.lazy(this.mNotificationLoggerProvider));
        injectMNotificationViewHierarchyManager(dependency, DoubleCheck.lazy(this.mNotificationViewHierarchyManagerProvider));
        injectMNotificationFilter(dependency, DoubleCheck.lazy(this.mNotificationFilterProvider));
        injectMKeyguardDismissUtil(dependency, DoubleCheck.lazy(this.mKeyguardDismissUtilProvider));
        injectMSmartReplyController(dependency, DoubleCheck.lazy(this.mSmartReplyControllerProvider));
        injectMRemoteInputQuickSettingsDisabler(dependency, DoubleCheck.lazy(this.mRemoteInputQuickSettingsDisablerProvider));
        injectMNotificationEntryManager(dependency, DoubleCheck.lazy(this.mNotificationEntryManagerProvider));
        injectMSensorPrivacyManager(dependency, DoubleCheck.lazy(this.mSensorPrivacyManagerProvider));
        injectMAutoHideController(dependency, DoubleCheck.lazy(this.mAutoHideControllerProvider));
        injectMForegroundServiceNotificationListener(dependency, DoubleCheck.lazy(this.mForegroundServiceNotificationListenerProvider));
        injectMPrivacyItemController(dependency, DoubleCheck.lazy(this.mPrivacyItemControllerProvider));
        injectMBgLooper(dependency, DoubleCheck.lazy(this.mBgLooperProvider));
        injectMBgHandler(dependency, DoubleCheck.lazy(this.mBgHandlerProvider));
        injectMMainLooper(dependency, DoubleCheck.lazy(this.mMainLooperProvider));
        injectMMainHandler(dependency, DoubleCheck.lazy(this.mMainHandlerProvider));
        injectMTimeTickHandler(dependency, DoubleCheck.lazy(this.mTimeTickHandlerProvider));
        injectMLeakReportEmail(dependency, DoubleCheck.lazy(this.mLeakReportEmailProvider));
        injectMMainExecutor(dependency, DoubleCheck.lazy(this.mMainExecutorProvider));
        injectMBackgroundExecutor(dependency, DoubleCheck.lazy(this.mBackgroundExecutorProvider));
        injectMClockManager(dependency, DoubleCheck.lazy(this.mClockManagerProvider));
        injectMActivityManagerWrapper(dependency, DoubleCheck.lazy(this.mActivityManagerWrapperProvider));
        injectMDevicePolicyManagerWrapper(dependency, DoubleCheck.lazy(this.mDevicePolicyManagerWrapperProvider));
        injectMPackageManagerWrapper(dependency, DoubleCheck.lazy(this.mPackageManagerWrapperProvider));
        injectMSensorPrivacyController(dependency, DoubleCheck.lazy(this.mSensorPrivacyControllerProvider));
        injectMDockManager(dependency, DoubleCheck.lazy(this.mDockManagerProvider));
        injectMINotificationManager(dependency, DoubleCheck.lazy(this.mINotificationManagerProvider));
        injectMSysUiStateFlagsContainer(dependency, DoubleCheck.lazy(this.mSysUiStateFlagsContainerProvider));
        injectMAlarmManager(dependency, DoubleCheck.lazy(this.mAlarmManagerProvider));
        injectMKeyguardSecurityModel(dependency, DoubleCheck.lazy(this.mKeyguardSecurityModelProvider));
        injectMDozeParameters(dependency, DoubleCheck.lazy(this.mDozeParametersProvider));
        injectMWallpaperManager(dependency, DoubleCheck.lazy(this.mWallpaperManagerProvider));
        injectMCommandQueue(dependency, DoubleCheck.lazy(this.mCommandQueueProvider));
        injectMRecordingController(dependency, DoubleCheck.lazy(this.mRecordingControllerProvider));
        injectMProtoTracer(dependency, DoubleCheck.lazy(this.mProtoTracerProvider));
        injectMMediaOutputDialogFactory(dependency, DoubleCheck.lazy(this.mMediaOutputDialogFactoryProvider));
        injectMDeviceConfigProxy(dependency, DoubleCheck.lazy(this.mDeviceConfigProxyProvider));
        injectMTelephonyListenerManager(dependency, DoubleCheck.lazy(this.mTelephonyListenerManagerProvider));
        injectMSystemStatusAnimationSchedulerLazy(dependency, DoubleCheck.lazy(this.mSystemStatusAnimationSchedulerLazyProvider));
        injectMPrivacyDotViewControllerLazy(dependency, DoubleCheck.lazy(this.mPrivacyDotViewControllerLazyProvider));
        injectMEdgeBackGestureHandlerFactoryLazy(dependency, DoubleCheck.lazy(this.mEdgeBackGestureHandlerFactoryLazyProvider));
        injectMUiEventLogger(dependency, DoubleCheck.lazy(this.mUiEventLoggerProvider));
        injectMContentInsetsProviderLazy(dependency, DoubleCheck.lazy(this.mContentInsetsProviderLazyProvider));
        injectMInternetDialogFactory(dependency, DoubleCheck.lazy(this.mInternetDialogFactoryProvider));
        injectMFeatureFlagsLazy(dependency, DoubleCheck.lazy(this.mFeatureFlagsLazyProvider));
        injectMNotificationSectionsManagerLazy(dependency, DoubleCheck.lazy(this.mNotificationSectionsManagerLazyProvider));
        injectMScreenOffAnimationController(dependency, DoubleCheck.lazy(this.mScreenOffAnimationControllerProvider));
        injectMAmbientStateLazy(dependency, DoubleCheck.lazy(this.mAmbientStateLazyProvider));
        injectMGroupMembershipManagerLazy(dependency, DoubleCheck.lazy(this.mGroupMembershipManagerLazyProvider));
        injectMGroupExpansionManagerLazy(dependency, DoubleCheck.lazy(this.mGroupExpansionManagerLazyProvider));
        injectMSystemUIDialogManagerLazy(dependency, DoubleCheck.lazy(this.mSystemUIDialogManagerLazyProvider));
        injectMDialogLaunchAnimatorLazy(dependency, DoubleCheck.lazy(this.mDialogLaunchAnimatorLazyProvider));
    }

    public static void injectMDumpManager(Dependency dependency, DumpManager dumpManager) {
        dependency.mDumpManager = dumpManager;
    }

    public static void injectMActivityStarter(Dependency dependency, Lazy<ActivityStarter> lazy) {
        dependency.mActivityStarter = lazy;
    }

    public static void injectMBroadcastDispatcher(Dependency dependency, Lazy<BroadcastDispatcher> lazy) {
        dependency.mBroadcastDispatcher = lazy;
    }

    public static void injectMAsyncSensorManager(Dependency dependency, Lazy<AsyncSensorManager> lazy) {
        dependency.mAsyncSensorManager = lazy;
    }

    public static void injectMBluetoothController(Dependency dependency, Lazy<BluetoothController> lazy) {
        dependency.mBluetoothController = lazy;
    }

    public static void injectMLocationController(Dependency dependency, Lazy<LocationController> lazy) {
        dependency.mLocationController = lazy;
    }

    public static void injectMRotationLockController(Dependency dependency, Lazy<RotationLockController> lazy) {
        dependency.mRotationLockController = lazy;
    }

    public static void injectMZenModeController(Dependency dependency, Lazy<ZenModeController> lazy) {
        dependency.mZenModeController = lazy;
    }

    public static void injectMHdmiCecSetMenuLanguageHelper(Dependency dependency, Lazy<HdmiCecSetMenuLanguageHelper> lazy) {
        dependency.mHdmiCecSetMenuLanguageHelper = lazy;
    }

    public static void injectMHotspotController(Dependency dependency, Lazy<HotspotController> lazy) {
        dependency.mHotspotController = lazy;
    }

    public static void injectMCastController(Dependency dependency, Lazy<CastController> lazy) {
        dependency.mCastController = lazy;
    }

    public static void injectMFlashlightController(Dependency dependency, Lazy<FlashlightController> lazy) {
        dependency.mFlashlightController = lazy;
    }

    public static void injectMUserSwitcherController(Dependency dependency, Lazy<UserSwitcherController> lazy) {
        dependency.mUserSwitcherController = lazy;
    }

    public static void injectMUserInfoController(Dependency dependency, Lazy<UserInfoController> lazy) {
        dependency.mUserInfoController = lazy;
    }

    public static void injectMKeyguardMonitor(Dependency dependency, Lazy<KeyguardStateController> lazy) {
        dependency.mKeyguardMonitor = lazy;
    }

    public static void injectMKeyguardUpdateMonitor(Dependency dependency, Lazy<KeyguardUpdateMonitor> lazy) {
        dependency.mKeyguardUpdateMonitor = lazy;
    }

    public static void injectMBatteryController(Dependency dependency, Lazy<BatteryController> lazy) {
        dependency.mBatteryController = lazy;
    }

    public static void injectMNightDisplayListener(Dependency dependency, Lazy<NightDisplayListener> lazy) {
        dependency.mNightDisplayListener = lazy;
    }

    public static void injectMReduceBrightColorsController(Dependency dependency, Lazy<ReduceBrightColorsController> lazy) {
        dependency.mReduceBrightColorsController = lazy;
    }

    public static void injectMManagedProfileController(Dependency dependency, Lazy<ManagedProfileController> lazy) {
        dependency.mManagedProfileController = lazy;
    }

    public static void injectMNextAlarmController(Dependency dependency, Lazy<NextAlarmController> lazy) {
        dependency.mNextAlarmController = lazy;
    }

    public static void injectMDataSaverController(Dependency dependency, Lazy<DataSaverController> lazy) {
        dependency.mDataSaverController = lazy;
    }

    public static void injectMAccessibilityController(Dependency dependency, Lazy<AccessibilityController> lazy) {
        dependency.mAccessibilityController = lazy;
    }

    public static void injectMDeviceProvisionedController(Dependency dependency, Lazy<DeviceProvisionedController> lazy) {
        dependency.mDeviceProvisionedController = lazy;
    }

    public static void injectMPluginManager(Dependency dependency, Lazy<PluginManager> lazy) {
        dependency.mPluginManager = lazy;
    }

    public static void injectMAssistManager(Dependency dependency, Lazy<AssistManager> lazy) {
        dependency.mAssistManager = lazy;
    }

    public static void injectMSecurityController(Dependency dependency, Lazy<SecurityController> lazy) {
        dependency.mSecurityController = lazy;
    }

    public static void injectMLeakDetector(Dependency dependency, Lazy<LeakDetector> lazy) {
        dependency.mLeakDetector = lazy;
    }

    public static void injectMLeakReporter(Dependency dependency, Lazy<LeakReporter> lazy) {
        dependency.mLeakReporter = lazy;
    }

    public static void injectMGarbageMonitor(Dependency dependency, Lazy<GarbageMonitor> lazy) {
        dependency.mGarbageMonitor = lazy;
    }

    public static void injectMTunerService(Dependency dependency, Lazy<TunerService> lazy) {
        dependency.mTunerService = lazy;
    }

    public static void injectMNotificationShadeWindowController(Dependency dependency, Lazy<NotificationShadeWindowController> lazy) {
        dependency.mNotificationShadeWindowController = lazy;
    }

    public static void injectMTempStatusBarWindowController(Dependency dependency, Lazy<StatusBarWindowController> lazy) {
        dependency.mTempStatusBarWindowController = lazy;
    }

    public static void injectMDarkIconDispatcher(Dependency dependency, Lazy<DarkIconDispatcher> lazy) {
        dependency.mDarkIconDispatcher = lazy;
    }

    public static void injectMConfigurationController(Dependency dependency, Lazy<ConfigurationController> lazy) {
        dependency.mConfigurationController = lazy;
    }

    public static void injectMStatusBarIconController(Dependency dependency, Lazy<StatusBarIconController> lazy) {
        dependency.mStatusBarIconController = lazy;
    }

    public static void injectMScreenLifecycle(Dependency dependency, Lazy<ScreenLifecycle> lazy) {
        dependency.mScreenLifecycle = lazy;
    }

    public static void injectMWakefulnessLifecycle(Dependency dependency, Lazy<WakefulnessLifecycle> lazy) {
        dependency.mWakefulnessLifecycle = lazy;
    }

    public static void injectMFragmentService(Dependency dependency, Lazy<FragmentService> lazy) {
        dependency.mFragmentService = lazy;
    }

    public static void injectMExtensionController(Dependency dependency, Lazy<ExtensionController> lazy) {
        dependency.mExtensionController = lazy;
    }

    public static void injectMPluginDependencyProvider(Dependency dependency, Lazy<PluginDependencyProvider> lazy) {
        dependency.mPluginDependencyProvider = lazy;
    }

    public static void injectMLocalBluetoothManager(Dependency dependency, Lazy<LocalBluetoothManager> lazy) {
        dependency.mLocalBluetoothManager = lazy;
    }

    public static void injectMVolumeDialogController(Dependency dependency, Lazy<VolumeDialogController> lazy) {
        dependency.mVolumeDialogController = lazy;
    }

    public static void injectMMetricsLogger(Dependency dependency, Lazy<MetricsLogger> lazy) {
        dependency.mMetricsLogger = lazy;
    }

    public static void injectMAccessibilityManagerWrapper(Dependency dependency, Lazy<AccessibilityManagerWrapper> lazy) {
        dependency.mAccessibilityManagerWrapper = lazy;
    }

    public static void injectMSysuiColorExtractor(Dependency dependency, Lazy<SysuiColorExtractor> lazy) {
        dependency.mSysuiColorExtractor = lazy;
    }

    public static void injectMTunablePaddingService(Dependency dependency, Lazy<TunablePadding.TunablePaddingService> lazy) {
        dependency.mTunablePaddingService = lazy;
    }

    public static void injectMForegroundServiceController(Dependency dependency, Lazy<ForegroundServiceController> lazy) {
        dependency.mForegroundServiceController = lazy;
    }

    public static void injectMUiOffloadThread(Dependency dependency, Lazy<UiOffloadThread> lazy) {
        dependency.mUiOffloadThread = lazy;
    }

    public static void injectMWarningsUI(Dependency dependency, Lazy<PowerUI.WarningsUI> lazy) {
        dependency.mWarningsUI = lazy;
    }

    public static void injectMLightBarController(Dependency dependency, Lazy<LightBarController> lazy) {
        dependency.mLightBarController = lazy;
    }

    public static void injectMIWindowManager(Dependency dependency, Lazy<IWindowManager> lazy) {
        dependency.mIWindowManager = lazy;
    }

    public static void injectMOverviewProxyService(Dependency dependency, Lazy<OverviewProxyService> lazy) {
        dependency.mOverviewProxyService = lazy;
    }

    public static void injectMNavBarModeController(Dependency dependency, Lazy<NavigationModeController> lazy) {
        dependency.mNavBarModeController = lazy;
    }

    public static void injectMAccessibilityButtonModeObserver(Dependency dependency, Lazy<AccessibilityButtonModeObserver> lazy) {
        dependency.mAccessibilityButtonModeObserver = lazy;
    }

    public static void injectMAccessibilityButtonListController(Dependency dependency, Lazy<AccessibilityButtonTargetsObserver> lazy) {
        dependency.mAccessibilityButtonListController = lazy;
    }

    public static void injectMEnhancedEstimates(Dependency dependency, Lazy<EnhancedEstimates> lazy) {
        dependency.mEnhancedEstimates = lazy;
    }

    public static void injectMVibratorHelper(Dependency dependency, Lazy<VibratorHelper> lazy) {
        dependency.mVibratorHelper = lazy;
    }

    public static void injectMIStatusBarService(Dependency dependency, Lazy<IStatusBarService> lazy) {
        dependency.mIStatusBarService = lazy;
    }

    public static void injectMDisplayMetrics(Dependency dependency, Lazy<DisplayMetrics> lazy) {
        dependency.mDisplayMetrics = lazy;
    }

    public static void injectMLockscreenGestureLogger(Dependency dependency, Lazy<LockscreenGestureLogger> lazy) {
        dependency.mLockscreenGestureLogger = lazy;
    }

    public static void injectMKeyguardEnvironment(Dependency dependency, Lazy<NotificationEntryManager.KeyguardEnvironment> lazy) {
        dependency.mKeyguardEnvironment = lazy;
    }

    public static void injectMShadeController(Dependency dependency, Lazy<ShadeController> lazy) {
        dependency.mShadeController = lazy;
    }

    public static void injectMNotificationRemoteInputManagerCallback(Dependency dependency, Lazy<NotificationRemoteInputManager.Callback> lazy) {
        dependency.mNotificationRemoteInputManagerCallback = lazy;
    }

    public static void injectMAppOpsController(Dependency dependency, Lazy<AppOpsController> lazy) {
        dependency.mAppOpsController = lazy;
    }

    public static void injectMNavigationBarController(Dependency dependency, Lazy<NavigationBarController> lazy) {
        dependency.mNavigationBarController = lazy;
    }

    public static void injectMAccessibilityFloatingMenuController(Dependency dependency, Lazy<AccessibilityFloatingMenuController> lazy) {
        dependency.mAccessibilityFloatingMenuController = lazy;
    }

    public static void injectMStatusBarStateController(Dependency dependency, Lazy<StatusBarStateController> lazy) {
        dependency.mStatusBarStateController = lazy;
    }

    public static void injectMNotificationLockscreenUserManager(Dependency dependency, Lazy<NotificationLockscreenUserManager> lazy) {
        dependency.mNotificationLockscreenUserManager = lazy;
    }

    public static void injectMNotificationGroupAlertTransferHelper(Dependency dependency, Lazy<NotificationGroupAlertTransferHelper> lazy) {
        dependency.mNotificationGroupAlertTransferHelper = lazy;
    }

    public static void injectMNotificationGroupManager(Dependency dependency, Lazy<NotificationGroupManagerLegacy> lazy) {
        dependency.mNotificationGroupManager = lazy;
    }

    public static void injectMVisualStabilityManager(Dependency dependency, Lazy<VisualStabilityManager> lazy) {
        dependency.mVisualStabilityManager = lazy;
    }

    public static void injectMNotificationGutsManager(Dependency dependency, Lazy<NotificationGutsManager> lazy) {
        dependency.mNotificationGutsManager = lazy;
    }

    public static void injectMNotificationMediaManager(Dependency dependency, Lazy<NotificationMediaManager> lazy) {
        dependency.mNotificationMediaManager = lazy;
    }

    public static void injectMNotificationRemoteInputManager(Dependency dependency, Lazy<NotificationRemoteInputManager> lazy) {
        dependency.mNotificationRemoteInputManager = lazy;
    }

    public static void injectMSmartReplyConstants(Dependency dependency, Lazy<SmartReplyConstants> lazy) {
        dependency.mSmartReplyConstants = lazy;
    }

    public static void injectMNotificationListener(Dependency dependency, Lazy<NotificationListener> lazy) {
        dependency.mNotificationListener = lazy;
    }

    public static void injectMNotificationLogger(Dependency dependency, Lazy<NotificationLogger> lazy) {
        dependency.mNotificationLogger = lazy;
    }

    public static void injectMNotificationViewHierarchyManager(Dependency dependency, Lazy<NotificationViewHierarchyManager> lazy) {
        dependency.mNotificationViewHierarchyManager = lazy;
    }

    public static void injectMNotificationFilter(Dependency dependency, Lazy<NotificationFilter> lazy) {
        dependency.mNotificationFilter = lazy;
    }

    public static void injectMKeyguardDismissUtil(Dependency dependency, Lazy<KeyguardDismissUtil> lazy) {
        dependency.mKeyguardDismissUtil = lazy;
    }

    public static void injectMSmartReplyController(Dependency dependency, Lazy<SmartReplyController> lazy) {
        dependency.mSmartReplyController = lazy;
    }

    public static void injectMRemoteInputQuickSettingsDisabler(Dependency dependency, Lazy<RemoteInputQuickSettingsDisabler> lazy) {
        dependency.mRemoteInputQuickSettingsDisabler = lazy;
    }

    public static void injectMNotificationEntryManager(Dependency dependency, Lazy<NotificationEntryManager> lazy) {
        dependency.mNotificationEntryManager = lazy;
    }

    public static void injectMSensorPrivacyManager(Dependency dependency, Lazy<SensorPrivacyManager> lazy) {
        dependency.mSensorPrivacyManager = lazy;
    }

    public static void injectMAutoHideController(Dependency dependency, Lazy<AutoHideController> lazy) {
        dependency.mAutoHideController = lazy;
    }

    public static void injectMForegroundServiceNotificationListener(Dependency dependency, Lazy<ForegroundServiceNotificationListener> lazy) {
        dependency.mForegroundServiceNotificationListener = lazy;
    }

    public static void injectMPrivacyItemController(Dependency dependency, Lazy<PrivacyItemController> lazy) {
        dependency.mPrivacyItemController = lazy;
    }

    @Background
    public static void injectMBgLooper(Dependency dependency, Lazy<Looper> lazy) {
        dependency.mBgLooper = lazy;
    }

    @Background
    public static void injectMBgHandler(Dependency dependency, Lazy<Handler> lazy) {
        dependency.mBgHandler = lazy;
    }

    @Main
    public static void injectMMainLooper(Dependency dependency, Lazy<Looper> lazy) {
        dependency.mMainLooper = lazy;
    }

    @Main
    public static void injectMMainHandler(Dependency dependency, Lazy<Handler> lazy) {
        dependency.mMainHandler = lazy;
    }

    @Named("time_tick_handler")
    public static void injectMTimeTickHandler(Dependency dependency, Lazy<Handler> lazy) {
        dependency.mTimeTickHandler = lazy;
    }

    @Named("leak_report_email")
    public static void injectMLeakReportEmail(Dependency dependency, Lazy<String> lazy) {
        dependency.mLeakReportEmail = lazy;
    }

    @Main
    public static void injectMMainExecutor(Dependency dependency, Lazy<Executor> lazy) {
        dependency.mMainExecutor = lazy;
    }

    @Background
    public static void injectMBackgroundExecutor(Dependency dependency, Lazy<Executor> lazy) {
        dependency.mBackgroundExecutor = lazy;
    }

    public static void injectMClockManager(Dependency dependency, Lazy<ClockManager> lazy) {
        dependency.mClockManager = lazy;
    }

    public static void injectMActivityManagerWrapper(Dependency dependency, Lazy<ActivityManagerWrapper> lazy) {
        dependency.mActivityManagerWrapper = lazy;
    }

    public static void injectMDevicePolicyManagerWrapper(Dependency dependency, Lazy<DevicePolicyManagerWrapper> lazy) {
        dependency.mDevicePolicyManagerWrapper = lazy;
    }

    public static void injectMPackageManagerWrapper(Dependency dependency, Lazy<PackageManagerWrapper> lazy) {
        dependency.mPackageManagerWrapper = lazy;
    }

    public static void injectMSensorPrivacyController(Dependency dependency, Lazy<SensorPrivacyController> lazy) {
        dependency.mSensorPrivacyController = lazy;
    }

    public static void injectMDockManager(Dependency dependency, Lazy<DockManager> lazy) {
        dependency.mDockManager = lazy;
    }

    public static void injectMINotificationManager(Dependency dependency, Lazy<INotificationManager> lazy) {
        dependency.mINotificationManager = lazy;
    }

    public static void injectMSysUiStateFlagsContainer(Dependency dependency, Lazy<SysUiState> lazy) {
        dependency.mSysUiStateFlagsContainer = lazy;
    }

    public static void injectMAlarmManager(Dependency dependency, Lazy<AlarmManager> lazy) {
        dependency.mAlarmManager = lazy;
    }

    public static void injectMKeyguardSecurityModel(Dependency dependency, Lazy<KeyguardSecurityModel> lazy) {
        dependency.mKeyguardSecurityModel = lazy;
    }

    public static void injectMDozeParameters(Dependency dependency, Lazy<DozeParameters> lazy) {
        dependency.mDozeParameters = lazy;
    }

    public static void injectMWallpaperManager(Dependency dependency, Lazy<IWallpaperManager> lazy) {
        dependency.mWallpaperManager = lazy;
    }

    public static void injectMCommandQueue(Dependency dependency, Lazy<CommandQueue> lazy) {
        dependency.mCommandQueue = lazy;
    }

    public static void injectMRecordingController(Dependency dependency, Lazy<RecordingController> lazy) {
        dependency.mRecordingController = lazy;
    }

    public static void injectMProtoTracer(Dependency dependency, Lazy<ProtoTracer> lazy) {
        dependency.mProtoTracer = lazy;
    }

    public static void injectMMediaOutputDialogFactory(Dependency dependency, Lazy<MediaOutputDialogFactory> lazy) {
        dependency.mMediaOutputDialogFactory = lazy;
    }

    public static void injectMDeviceConfigProxy(Dependency dependency, Lazy<DeviceConfigProxy> lazy) {
        dependency.mDeviceConfigProxy = lazy;
    }

    public static void injectMTelephonyListenerManager(Dependency dependency, Lazy<TelephonyListenerManager> lazy) {
        dependency.mTelephonyListenerManager = lazy;
    }

    public static void injectMSystemStatusAnimationSchedulerLazy(Dependency dependency, Lazy<SystemStatusAnimationScheduler> lazy) {
        dependency.mSystemStatusAnimationSchedulerLazy = lazy;
    }

    public static void injectMPrivacyDotViewControllerLazy(Dependency dependency, Lazy<PrivacyDotViewController> lazy) {
        dependency.mPrivacyDotViewControllerLazy = lazy;
    }

    public static void injectMEdgeBackGestureHandlerFactoryLazy(Dependency dependency, Lazy<EdgeBackGestureHandler.Factory> lazy) {
        dependency.mEdgeBackGestureHandlerFactoryLazy = lazy;
    }

    public static void injectMUiEventLogger(Dependency dependency, Lazy<UiEventLogger> lazy) {
        dependency.mUiEventLogger = lazy;
    }

    public static void injectMContentInsetsProviderLazy(Dependency dependency, Lazy<StatusBarContentInsetsProvider> lazy) {
        dependency.mContentInsetsProviderLazy = lazy;
    }

    public static void injectMInternetDialogFactory(Dependency dependency, Lazy<InternetDialogFactory> lazy) {
        dependency.mInternetDialogFactory = lazy;
    }

    public static void injectMFeatureFlagsLazy(Dependency dependency, Lazy<FeatureFlags> lazy) {
        dependency.mFeatureFlagsLazy = lazy;
    }

    public static void injectMNotificationSectionsManagerLazy(Dependency dependency, Lazy<NotificationSectionsManager> lazy) {
        dependency.mNotificationSectionsManagerLazy = lazy;
    }

    public static void injectMScreenOffAnimationController(Dependency dependency, Lazy<ScreenOffAnimationController> lazy) {
        dependency.mScreenOffAnimationController = lazy;
    }

    public static void injectMAmbientStateLazy(Dependency dependency, Lazy<AmbientState> lazy) {
        dependency.mAmbientStateLazy = lazy;
    }

    public static void injectMGroupMembershipManagerLazy(Dependency dependency, Lazy<GroupMembershipManager> lazy) {
        dependency.mGroupMembershipManagerLazy = lazy;
    }

    public static void injectMGroupExpansionManagerLazy(Dependency dependency, Lazy<GroupExpansionManager> lazy) {
        dependency.mGroupExpansionManagerLazy = lazy;
    }

    public static void injectMSystemUIDialogManagerLazy(Dependency dependency, Lazy<SystemUIDialogManager> lazy) {
        dependency.mSystemUIDialogManagerLazy = lazy;
    }

    public static void injectMDialogLaunchAnimatorLazy(Dependency dependency, Lazy<DialogLaunchAnimator> lazy) {
        dependency.mDialogLaunchAnimatorLazy = lazy;
    }
}
