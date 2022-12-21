package com.android.systemui;

import android.app.AlarmManager;
import android.app.INotificationManager;
import android.app.IWallpaperManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.IWindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.Preconditions;
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
import com.android.systemui.dagger.SysUISingleton;
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
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Named;

@SysUISingleton
public class Dependency {
    public static final String ALLOW_NOTIFICATION_LONG_PRESS_NAME = "allow_notif_longpress";
    public static final DependencyKey<Executor> BACKGROUND_EXECUTOR = new DependencyKey<>(BACKGROUND_EXECUTOR_NAME);
    private static final String BACKGROUND_EXECUTOR_NAME = "background_executor";
    public static final DependencyKey<Looper> BG_LOOPER = new DependencyKey<>(BG_LOOPER_NAME);
    private static final String BG_LOOPER_NAME = "background_looper";
    public static final DependencyKey<String> LEAK_REPORT_EMAIL = new DependencyKey<>(LEAK_REPORT_EMAIL_NAME);
    public static final String LEAK_REPORT_EMAIL_NAME = "leak_report_email";
    public static final DependencyKey<Executor> MAIN_EXECUTOR = new DependencyKey<>(MAIN_EXECUTOR_NAME);
    private static final String MAIN_EXECUTOR_NAME = "main_executor";
    public static final DependencyKey<Handler> MAIN_HANDLER = new DependencyKey<>(MAIN_HANDLER_NAME);
    private static final String MAIN_HANDLER_NAME = "main_handler";
    public static final DependencyKey<Looper> MAIN_LOOPER = new DependencyKey<>(MAIN_LOOPER_NAME);
    private static final String MAIN_LOOPER_NAME = "main_looper";
    public static final DependencyKey<Handler> TIME_TICK_HANDLER = new DependencyKey<>(TIME_TICK_HANDLER_NAME);
    public static final String TIME_TICK_HANDLER_NAME = "time_tick_handler";
    private static Dependency sDependency;
    @Inject
    Lazy<AccessibilityButtonTargetsObserver> mAccessibilityButtonListController;
    @Inject
    Lazy<AccessibilityButtonModeObserver> mAccessibilityButtonModeObserver;
    @Inject
    Lazy<AccessibilityController> mAccessibilityController;
    @Inject
    Lazy<AccessibilityFloatingMenuController> mAccessibilityFloatingMenuController;
    @Inject
    Lazy<AccessibilityManagerWrapper> mAccessibilityManagerWrapper;
    @Inject
    Lazy<ActivityManagerWrapper> mActivityManagerWrapper;
    @Inject
    Lazy<ActivityStarter> mActivityStarter;
    @Inject
    Lazy<AlarmManager> mAlarmManager;
    @Inject
    Lazy<AmbientState> mAmbientStateLazy;
    @Inject
    Lazy<AppOpsController> mAppOpsController;
    @Inject
    Lazy<AssistManager> mAssistManager;
    @Inject
    Lazy<AsyncSensorManager> mAsyncSensorManager;
    @Inject
    Lazy<AutoHideController> mAutoHideController;
    @Background
    @Inject
    Lazy<Executor> mBackgroundExecutor;
    @Inject
    Lazy<BatteryController> mBatteryController;
    @Background
    @Inject
    Lazy<Handler> mBgHandler;
    @Background
    @Inject
    Lazy<Looper> mBgLooper;
    @Inject
    Lazy<BluetoothController> mBluetoothController;
    @Inject
    Lazy<BroadcastDispatcher> mBroadcastDispatcher;
    @Inject
    Lazy<CastController> mCastController;
    @Inject
    Lazy<ClockManager> mClockManager;
    @Inject
    Lazy<CommandQueue> mCommandQueue;
    @Inject
    Lazy<ConfigurationController> mConfigurationController;
    @Inject
    Lazy<StatusBarContentInsetsProvider> mContentInsetsProviderLazy;
    @Inject
    Lazy<DarkIconDispatcher> mDarkIconDispatcher;
    @Inject
    Lazy<DataSaverController> mDataSaverController;
    private final ArrayMap<Object, Object> mDependencies = new ArrayMap<>();
    @Inject
    Lazy<DeviceConfigProxy> mDeviceConfigProxy;
    @Inject
    Lazy<DevicePolicyManagerWrapper> mDevicePolicyManagerWrapper;
    @Inject
    Lazy<DeviceProvisionedController> mDeviceProvisionedController;
    @Inject
    Lazy<DialogLaunchAnimator> mDialogLaunchAnimatorLazy;
    @Inject
    Lazy<DisplayMetrics> mDisplayMetrics;
    @Inject
    Lazy<DockManager> mDockManager;
    @Inject
    Lazy<DozeParameters> mDozeParameters;
    @Inject
    DumpManager mDumpManager;
    @Inject
    Lazy<EdgeBackGestureHandler.Factory> mEdgeBackGestureHandlerFactoryLazy;
    @Inject
    Lazy<EnhancedEstimates> mEnhancedEstimates;
    @Inject
    Lazy<ExtensionController> mExtensionController;
    @Inject
    Lazy<FeatureFlags> mFeatureFlagsLazy;
    @Inject
    Lazy<FlashlightController> mFlashlightController;
    @Inject
    Lazy<ForegroundServiceController> mForegroundServiceController;
    @Inject
    Lazy<ForegroundServiceNotificationListener> mForegroundServiceNotificationListener;
    @Inject
    Lazy<FragmentService> mFragmentService;
    @Inject
    Lazy<GarbageMonitor> mGarbageMonitor;
    @Inject
    Lazy<GroupExpansionManager> mGroupExpansionManagerLazy;
    @Inject
    Lazy<GroupMembershipManager> mGroupMembershipManagerLazy;
    @Inject
    Lazy<HdmiCecSetMenuLanguageHelper> mHdmiCecSetMenuLanguageHelper;
    @Inject
    Lazy<HotspotController> mHotspotController;
    @Inject
    Lazy<INotificationManager> mINotificationManager;
    @Inject
    Lazy<IStatusBarService> mIStatusBarService;
    @Inject
    Lazy<IWindowManager> mIWindowManager;
    @Inject
    Lazy<InternetDialogFactory> mInternetDialogFactory;
    @Inject
    Lazy<KeyguardDismissUtil> mKeyguardDismissUtil;
    @Inject
    Lazy<NotificationEntryManager.KeyguardEnvironment> mKeyguardEnvironment;
    @Inject
    Lazy<KeyguardStateController> mKeyguardMonitor;
    @Inject
    Lazy<KeyguardSecurityModel> mKeyguardSecurityModel;
    @Inject
    Lazy<KeyguardUpdateMonitor> mKeyguardUpdateMonitor;
    @Inject
    Lazy<LeakDetector> mLeakDetector;
    @Inject
    @Named("leak_report_email")
    Lazy<String> mLeakReportEmail;
    @Inject
    Lazy<LeakReporter> mLeakReporter;
    @Inject
    Lazy<LightBarController> mLightBarController;
    @Inject
    Lazy<LocalBluetoothManager> mLocalBluetoothManager;
    @Inject
    Lazy<LocationController> mLocationController;
    @Inject
    Lazy<LockscreenGestureLogger> mLockscreenGestureLogger;
    @Inject
    @Main
    Lazy<Executor> mMainExecutor;
    @Inject
    @Main
    Lazy<Handler> mMainHandler;
    @Inject
    @Main
    Lazy<Looper> mMainLooper;
    @Inject
    Lazy<ManagedProfileController> mManagedProfileController;
    @Inject
    Lazy<MediaOutputDialogFactory> mMediaOutputDialogFactory;
    @Inject
    Lazy<MetricsLogger> mMetricsLogger;
    @Inject
    Lazy<NavigationModeController> mNavBarModeController;
    @Inject
    Lazy<NavigationBarController> mNavigationBarController;
    @Inject
    Lazy<NextAlarmController> mNextAlarmController;
    @Inject
    Lazy<NightDisplayListener> mNightDisplayListener;
    @Inject
    Lazy<NotificationEntryManager> mNotificationEntryManager;
    @Inject
    Lazy<NotificationFilter> mNotificationFilter;
    @Inject
    Lazy<NotificationGroupAlertTransferHelper> mNotificationGroupAlertTransferHelper;
    @Inject
    Lazy<NotificationGroupManagerLegacy> mNotificationGroupManager;
    @Inject
    Lazy<NotificationGutsManager> mNotificationGutsManager;
    @Inject
    Lazy<NotificationListener> mNotificationListener;
    @Inject
    Lazy<NotificationLockscreenUserManager> mNotificationLockscreenUserManager;
    @Inject
    Lazy<NotificationLogger> mNotificationLogger;
    @Inject
    Lazy<NotificationMediaManager> mNotificationMediaManager;
    @Inject
    Lazy<NotificationRemoteInputManager> mNotificationRemoteInputManager;
    @Inject
    Lazy<NotificationRemoteInputManager.Callback> mNotificationRemoteInputManagerCallback;
    @Inject
    Lazy<NotificationSectionsManager> mNotificationSectionsManagerLazy;
    @Inject
    Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    @Inject
    Lazy<NotificationViewHierarchyManager> mNotificationViewHierarchyManager;
    @Inject
    Lazy<OverviewProxyService> mOverviewProxyService;
    @Inject
    Lazy<PackageManagerWrapper> mPackageManagerWrapper;
    @Inject
    Lazy<PluginDependencyProvider> mPluginDependencyProvider;
    @Inject
    Lazy<PluginManager> mPluginManager;
    @Inject
    Lazy<PrivacyDotViewController> mPrivacyDotViewControllerLazy;
    @Inject
    Lazy<PrivacyItemController> mPrivacyItemController;
    @Inject
    Lazy<ProtoTracer> mProtoTracer;
    private final ArrayMap<Object, LazyDependencyCreator> mProviders = new ArrayMap<>();
    @Inject
    Lazy<RecordingController> mRecordingController;
    @Inject
    Lazy<ReduceBrightColorsController> mReduceBrightColorsController;
    @Inject
    Lazy<RemoteInputQuickSettingsDisabler> mRemoteInputQuickSettingsDisabler;
    @Inject
    Lazy<RotationLockController> mRotationLockController;
    @Inject
    Lazy<ScreenLifecycle> mScreenLifecycle;
    @Inject
    Lazy<ScreenOffAnimationController> mScreenOffAnimationController;
    @Inject
    Lazy<SecurityController> mSecurityController;
    @Inject
    Lazy<SensorPrivacyController> mSensorPrivacyController;
    @Inject
    Lazy<SensorPrivacyManager> mSensorPrivacyManager;
    @Inject
    Lazy<ShadeController> mShadeController;
    @Inject
    Lazy<SmartReplyConstants> mSmartReplyConstants;
    @Inject
    Lazy<SmartReplyController> mSmartReplyController;
    @Inject
    Lazy<StatusBarIconController> mStatusBarIconController;
    @Inject
    Lazy<StatusBarStateController> mStatusBarStateController;
    @Inject
    Lazy<SysUiState> mSysUiStateFlagsContainer;
    @Inject
    Lazy<SystemStatusAnimationScheduler> mSystemStatusAnimationSchedulerLazy;
    @Inject
    Lazy<SystemUIDialogManager> mSystemUIDialogManagerLazy;
    @Inject
    Lazy<SysuiColorExtractor> mSysuiColorExtractor;
    @Inject
    Lazy<TelephonyListenerManager> mTelephonyListenerManager;
    @Inject
    Lazy<StatusBarWindowController> mTempStatusBarWindowController;
    @Inject
    @Named("time_tick_handler")
    Lazy<Handler> mTimeTickHandler;
    @Inject
    Lazy<TunablePadding.TunablePaddingService> mTunablePaddingService;
    @Inject
    Lazy<TunerService> mTunerService;
    @Inject
    Lazy<UiEventLogger> mUiEventLogger;
    @Inject
    Lazy<UiOffloadThread> mUiOffloadThread;
    @Inject
    Lazy<UserInfoController> mUserInfoController;
    @Inject
    Lazy<UserSwitcherController> mUserSwitcherController;
    @Inject
    Lazy<VibratorHelper> mVibratorHelper;
    @Inject
    Lazy<VisualStabilityManager> mVisualStabilityManager;
    @Inject
    Lazy<VolumeDialogController> mVolumeDialogController;
    @Inject
    Lazy<WakefulnessLifecycle> mWakefulnessLifecycle;
    @Inject
    Lazy<IWallpaperManager> mWallpaperManager;
    @Inject
    Lazy<PowerUI.WarningsUI> mWarningsUI;
    @Inject
    Lazy<ZenModeController> mZenModeController;

    private interface LazyDependencyCreator<T> {
        T createDependency();
    }

    /* access modifiers changed from: protected */
    public void start() {
        ArrayMap<Object, LazyDependencyCreator> arrayMap = this.mProviders;
        DependencyKey<Handler> dependencyKey = TIME_TICK_HANDLER;
        Lazy<Handler> lazy = this.mTimeTickHandler;
        Objects.requireNonNull(lazy);
        arrayMap.put(dependencyKey, new Dependency$$ExternalSyntheticLambda0(lazy));
        ArrayMap<Object, LazyDependencyCreator> arrayMap2 = this.mProviders;
        DependencyKey<Looper> dependencyKey2 = BG_LOOPER;
        Lazy<Looper> lazy2 = this.mBgLooper;
        Objects.requireNonNull(lazy2);
        arrayMap2.put(dependencyKey2, new Dependency$$ExternalSyntheticLambda0(lazy2));
        ArrayMap<Object, LazyDependencyCreator> arrayMap3 = this.mProviders;
        DependencyKey<Looper> dependencyKey3 = MAIN_LOOPER;
        Lazy<Looper> lazy3 = this.mMainLooper;
        Objects.requireNonNull(lazy3);
        arrayMap3.put(dependencyKey3, new Dependency$$ExternalSyntheticLambda0(lazy3));
        ArrayMap<Object, LazyDependencyCreator> arrayMap4 = this.mProviders;
        DependencyKey<Handler> dependencyKey4 = MAIN_HANDLER;
        Lazy<Handler> lazy4 = this.mMainHandler;
        Objects.requireNonNull(lazy4);
        arrayMap4.put(dependencyKey4, new Dependency$$ExternalSyntheticLambda0(lazy4));
        ArrayMap<Object, LazyDependencyCreator> arrayMap5 = this.mProviders;
        DependencyKey<Executor> dependencyKey5 = MAIN_EXECUTOR;
        Lazy<Executor> lazy5 = this.mMainExecutor;
        Objects.requireNonNull(lazy5);
        arrayMap5.put(dependencyKey5, new Dependency$$ExternalSyntheticLambda0(lazy5));
        ArrayMap<Object, LazyDependencyCreator> arrayMap6 = this.mProviders;
        DependencyKey<Executor> dependencyKey6 = BACKGROUND_EXECUTOR;
        Lazy<Executor> lazy6 = this.mBackgroundExecutor;
        Objects.requireNonNull(lazy6);
        arrayMap6.put(dependencyKey6, new Dependency$$ExternalSyntheticLambda0(lazy6));
        Lazy<ActivityStarter> lazy7 = this.mActivityStarter;
        Objects.requireNonNull(lazy7);
        this.mProviders.put(ActivityStarter.class, new Dependency$$ExternalSyntheticLambda0(lazy7));
        Lazy<BroadcastDispatcher> lazy8 = this.mBroadcastDispatcher;
        Objects.requireNonNull(lazy8);
        this.mProviders.put(BroadcastDispatcher.class, new Dependency$$ExternalSyntheticLambda0(lazy8));
        Lazy<AsyncSensorManager> lazy9 = this.mAsyncSensorManager;
        Objects.requireNonNull(lazy9);
        this.mProviders.put(AsyncSensorManager.class, new Dependency$$ExternalSyntheticLambda0(lazy9));
        Lazy<BluetoothController> lazy10 = this.mBluetoothController;
        Objects.requireNonNull(lazy10);
        this.mProviders.put(BluetoothController.class, new Dependency$$ExternalSyntheticLambda0(lazy10));
        Lazy<SensorPrivacyManager> lazy11 = this.mSensorPrivacyManager;
        Objects.requireNonNull(lazy11);
        this.mProviders.put(SensorPrivacyManager.class, new Dependency$$ExternalSyntheticLambda0(lazy11));
        Lazy<LocationController> lazy12 = this.mLocationController;
        Objects.requireNonNull(lazy12);
        this.mProviders.put(LocationController.class, new Dependency$$ExternalSyntheticLambda0(lazy12));
        Lazy<RotationLockController> lazy13 = this.mRotationLockController;
        Objects.requireNonNull(lazy13);
        this.mProviders.put(RotationLockController.class, new Dependency$$ExternalSyntheticLambda0(lazy13));
        Lazy<ZenModeController> lazy14 = this.mZenModeController;
        Objects.requireNonNull(lazy14);
        this.mProviders.put(ZenModeController.class, new Dependency$$ExternalSyntheticLambda0(lazy14));
        Lazy<HotspotController> lazy15 = this.mHotspotController;
        Objects.requireNonNull(lazy15);
        this.mProviders.put(HotspotController.class, new Dependency$$ExternalSyntheticLambda0(lazy15));
        Lazy<CastController> lazy16 = this.mCastController;
        Objects.requireNonNull(lazy16);
        this.mProviders.put(CastController.class, new Dependency$$ExternalSyntheticLambda0(lazy16));
        Lazy<FlashlightController> lazy17 = this.mFlashlightController;
        Objects.requireNonNull(lazy17);
        this.mProviders.put(FlashlightController.class, new Dependency$$ExternalSyntheticLambda0(lazy17));
        Lazy<KeyguardStateController> lazy18 = this.mKeyguardMonitor;
        Objects.requireNonNull(lazy18);
        this.mProviders.put(KeyguardStateController.class, new Dependency$$ExternalSyntheticLambda0(lazy18));
        Lazy<KeyguardUpdateMonitor> lazy19 = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(lazy19);
        this.mProviders.put(KeyguardUpdateMonitor.class, new Dependency$$ExternalSyntheticLambda0(lazy19));
        Lazy<UserSwitcherController> lazy20 = this.mUserSwitcherController;
        Objects.requireNonNull(lazy20);
        this.mProviders.put(UserSwitcherController.class, new Dependency$$ExternalSyntheticLambda0(lazy20));
        Lazy<UserInfoController> lazy21 = this.mUserInfoController;
        Objects.requireNonNull(lazy21);
        this.mProviders.put(UserInfoController.class, new Dependency$$ExternalSyntheticLambda0(lazy21));
        Lazy<BatteryController> lazy22 = this.mBatteryController;
        Objects.requireNonNull(lazy22);
        this.mProviders.put(BatteryController.class, new Dependency$$ExternalSyntheticLambda0(lazy22));
        Lazy<NightDisplayListener> lazy23 = this.mNightDisplayListener;
        Objects.requireNonNull(lazy23);
        this.mProviders.put(NightDisplayListener.class, new Dependency$$ExternalSyntheticLambda0(lazy23));
        Lazy<ReduceBrightColorsController> lazy24 = this.mReduceBrightColorsController;
        Objects.requireNonNull(lazy24);
        this.mProviders.put(ReduceBrightColorsController.class, new Dependency$$ExternalSyntheticLambda0(lazy24));
        Lazy<ManagedProfileController> lazy25 = this.mManagedProfileController;
        Objects.requireNonNull(lazy25);
        this.mProviders.put(ManagedProfileController.class, new Dependency$$ExternalSyntheticLambda0(lazy25));
        Lazy<NextAlarmController> lazy26 = this.mNextAlarmController;
        Objects.requireNonNull(lazy26);
        this.mProviders.put(NextAlarmController.class, new Dependency$$ExternalSyntheticLambda0(lazy26));
        Lazy<DataSaverController> lazy27 = this.mDataSaverController;
        Objects.requireNonNull(lazy27);
        this.mProviders.put(DataSaverController.class, new Dependency$$ExternalSyntheticLambda0(lazy27));
        Lazy<AccessibilityController> lazy28 = this.mAccessibilityController;
        Objects.requireNonNull(lazy28);
        this.mProviders.put(AccessibilityController.class, new Dependency$$ExternalSyntheticLambda0(lazy28));
        Lazy<DeviceProvisionedController> lazy29 = this.mDeviceProvisionedController;
        Objects.requireNonNull(lazy29);
        this.mProviders.put(DeviceProvisionedController.class, new Dependency$$ExternalSyntheticLambda0(lazy29));
        Lazy<PluginManager> lazy30 = this.mPluginManager;
        Objects.requireNonNull(lazy30);
        this.mProviders.put(PluginManager.class, new Dependency$$ExternalSyntheticLambda0(lazy30));
        Lazy<AssistManager> lazy31 = this.mAssistManager;
        Objects.requireNonNull(lazy31);
        this.mProviders.put(AssistManager.class, new Dependency$$ExternalSyntheticLambda0(lazy31));
        Lazy<SecurityController> lazy32 = this.mSecurityController;
        Objects.requireNonNull(lazy32);
        this.mProviders.put(SecurityController.class, new Dependency$$ExternalSyntheticLambda0(lazy32));
        Lazy<LeakDetector> lazy33 = this.mLeakDetector;
        Objects.requireNonNull(lazy33);
        this.mProviders.put(LeakDetector.class, new Dependency$$ExternalSyntheticLambda0(lazy33));
        ArrayMap<Object, LazyDependencyCreator> arrayMap7 = this.mProviders;
        DependencyKey<String> dependencyKey7 = LEAK_REPORT_EMAIL;
        Lazy<String> lazy34 = this.mLeakReportEmail;
        Objects.requireNonNull(lazy34);
        arrayMap7.put(dependencyKey7, new Dependency$$ExternalSyntheticLambda0(lazy34));
        Lazy<LeakReporter> lazy35 = this.mLeakReporter;
        Objects.requireNonNull(lazy35);
        this.mProviders.put(LeakReporter.class, new Dependency$$ExternalSyntheticLambda0(lazy35));
        Lazy<GarbageMonitor> lazy36 = this.mGarbageMonitor;
        Objects.requireNonNull(lazy36);
        this.mProviders.put(GarbageMonitor.class, new Dependency$$ExternalSyntheticLambda0(lazy36));
        Lazy<TunerService> lazy37 = this.mTunerService;
        Objects.requireNonNull(lazy37);
        this.mProviders.put(TunerService.class, new Dependency$$ExternalSyntheticLambda0(lazy37));
        Lazy<NotificationShadeWindowController> lazy38 = this.mNotificationShadeWindowController;
        Objects.requireNonNull(lazy38);
        this.mProviders.put(NotificationShadeWindowController.class, new Dependency$$ExternalSyntheticLambda0(lazy38));
        Lazy<StatusBarWindowController> lazy39 = this.mTempStatusBarWindowController;
        Objects.requireNonNull(lazy39);
        this.mProviders.put(StatusBarWindowController.class, new Dependency$$ExternalSyntheticLambda0(lazy39));
        Lazy<DarkIconDispatcher> lazy40 = this.mDarkIconDispatcher;
        Objects.requireNonNull(lazy40);
        this.mProviders.put(DarkIconDispatcher.class, new Dependency$$ExternalSyntheticLambda0(lazy40));
        Lazy<ConfigurationController> lazy41 = this.mConfigurationController;
        Objects.requireNonNull(lazy41);
        this.mProviders.put(ConfigurationController.class, new Dependency$$ExternalSyntheticLambda0(lazy41));
        Lazy<StatusBarIconController> lazy42 = this.mStatusBarIconController;
        Objects.requireNonNull(lazy42);
        this.mProviders.put(StatusBarIconController.class, new Dependency$$ExternalSyntheticLambda0(lazy42));
        Lazy<ScreenLifecycle> lazy43 = this.mScreenLifecycle;
        Objects.requireNonNull(lazy43);
        this.mProviders.put(ScreenLifecycle.class, new Dependency$$ExternalSyntheticLambda0(lazy43));
        Lazy<WakefulnessLifecycle> lazy44 = this.mWakefulnessLifecycle;
        Objects.requireNonNull(lazy44);
        this.mProviders.put(WakefulnessLifecycle.class, new Dependency$$ExternalSyntheticLambda0(lazy44));
        Lazy<FragmentService> lazy45 = this.mFragmentService;
        Objects.requireNonNull(lazy45);
        this.mProviders.put(FragmentService.class, new Dependency$$ExternalSyntheticLambda0(lazy45));
        Lazy<ExtensionController> lazy46 = this.mExtensionController;
        Objects.requireNonNull(lazy46);
        this.mProviders.put(ExtensionController.class, new Dependency$$ExternalSyntheticLambda0(lazy46));
        Lazy<PluginDependencyProvider> lazy47 = this.mPluginDependencyProvider;
        Objects.requireNonNull(lazy47);
        this.mProviders.put(PluginDependencyProvider.class, new Dependency$$ExternalSyntheticLambda0(lazy47));
        Lazy<LocalBluetoothManager> lazy48 = this.mLocalBluetoothManager;
        Objects.requireNonNull(lazy48);
        this.mProviders.put(LocalBluetoothManager.class, new Dependency$$ExternalSyntheticLambda0(lazy48));
        Lazy<VolumeDialogController> lazy49 = this.mVolumeDialogController;
        Objects.requireNonNull(lazy49);
        this.mProviders.put(VolumeDialogController.class, new Dependency$$ExternalSyntheticLambda0(lazy49));
        Lazy<MetricsLogger> lazy50 = this.mMetricsLogger;
        Objects.requireNonNull(lazy50);
        this.mProviders.put(MetricsLogger.class, new Dependency$$ExternalSyntheticLambda0(lazy50));
        Lazy<AccessibilityManagerWrapper> lazy51 = this.mAccessibilityManagerWrapper;
        Objects.requireNonNull(lazy51);
        this.mProviders.put(AccessibilityManagerWrapper.class, new Dependency$$ExternalSyntheticLambda0(lazy51));
        Lazy<SysuiColorExtractor> lazy52 = this.mSysuiColorExtractor;
        Objects.requireNonNull(lazy52);
        this.mProviders.put(SysuiColorExtractor.class, new Dependency$$ExternalSyntheticLambda0(lazy52));
        Lazy<TunablePadding.TunablePaddingService> lazy53 = this.mTunablePaddingService;
        Objects.requireNonNull(lazy53);
        this.mProviders.put(TunablePadding.TunablePaddingService.class, new Dependency$$ExternalSyntheticLambda0(lazy53));
        Lazy<ForegroundServiceController> lazy54 = this.mForegroundServiceController;
        Objects.requireNonNull(lazy54);
        this.mProviders.put(ForegroundServiceController.class, new Dependency$$ExternalSyntheticLambda0(lazy54));
        Lazy<UiOffloadThread> lazy55 = this.mUiOffloadThread;
        Objects.requireNonNull(lazy55);
        this.mProviders.put(UiOffloadThread.class, new Dependency$$ExternalSyntheticLambda0(lazy55));
        Lazy<PowerUI.WarningsUI> lazy56 = this.mWarningsUI;
        Objects.requireNonNull(lazy56);
        this.mProviders.put(PowerUI.WarningsUI.class, new Dependency$$ExternalSyntheticLambda0(lazy56));
        Lazy<LightBarController> lazy57 = this.mLightBarController;
        Objects.requireNonNull(lazy57);
        this.mProviders.put(LightBarController.class, new Dependency$$ExternalSyntheticLambda0(lazy57));
        Lazy<IWindowManager> lazy58 = this.mIWindowManager;
        Objects.requireNonNull(lazy58);
        this.mProviders.put(IWindowManager.class, new Dependency$$ExternalSyntheticLambda0(lazy58));
        Lazy<OverviewProxyService> lazy59 = this.mOverviewProxyService;
        Objects.requireNonNull(lazy59);
        this.mProviders.put(OverviewProxyService.class, new Dependency$$ExternalSyntheticLambda0(lazy59));
        Lazy<NavigationModeController> lazy60 = this.mNavBarModeController;
        Objects.requireNonNull(lazy60);
        this.mProviders.put(NavigationModeController.class, new Dependency$$ExternalSyntheticLambda0(lazy60));
        Lazy<AccessibilityButtonModeObserver> lazy61 = this.mAccessibilityButtonModeObserver;
        Objects.requireNonNull(lazy61);
        this.mProviders.put(AccessibilityButtonModeObserver.class, new Dependency$$ExternalSyntheticLambda0(lazy61));
        Lazy<AccessibilityButtonTargetsObserver> lazy62 = this.mAccessibilityButtonListController;
        Objects.requireNonNull(lazy62);
        this.mProviders.put(AccessibilityButtonTargetsObserver.class, new Dependency$$ExternalSyntheticLambda0(lazy62));
        Lazy<EnhancedEstimates> lazy63 = this.mEnhancedEstimates;
        Objects.requireNonNull(lazy63);
        this.mProviders.put(EnhancedEstimates.class, new Dependency$$ExternalSyntheticLambda0(lazy63));
        Lazy<VibratorHelper> lazy64 = this.mVibratorHelper;
        Objects.requireNonNull(lazy64);
        this.mProviders.put(VibratorHelper.class, new Dependency$$ExternalSyntheticLambda0(lazy64));
        Lazy<IStatusBarService> lazy65 = this.mIStatusBarService;
        Objects.requireNonNull(lazy65);
        this.mProviders.put(IStatusBarService.class, new Dependency$$ExternalSyntheticLambda0(lazy65));
        Lazy<DisplayMetrics> lazy66 = this.mDisplayMetrics;
        Objects.requireNonNull(lazy66);
        this.mProviders.put(DisplayMetrics.class, new Dependency$$ExternalSyntheticLambda0(lazy66));
        Lazy<LockscreenGestureLogger> lazy67 = this.mLockscreenGestureLogger;
        Objects.requireNonNull(lazy67);
        this.mProviders.put(LockscreenGestureLogger.class, new Dependency$$ExternalSyntheticLambda0(lazy67));
        Lazy<NotificationEntryManager.KeyguardEnvironment> lazy68 = this.mKeyguardEnvironment;
        Objects.requireNonNull(lazy68);
        this.mProviders.put(NotificationEntryManager.KeyguardEnvironment.class, new Dependency$$ExternalSyntheticLambda0(lazy68));
        Lazy<ShadeController> lazy69 = this.mShadeController;
        Objects.requireNonNull(lazy69);
        this.mProviders.put(ShadeController.class, new Dependency$$ExternalSyntheticLambda0(lazy69));
        Lazy<NotificationRemoteInputManager.Callback> lazy70 = this.mNotificationRemoteInputManagerCallback;
        Objects.requireNonNull(lazy70);
        this.mProviders.put(NotificationRemoteInputManager.Callback.class, new Dependency$$ExternalSyntheticLambda0(lazy70));
        Lazy<AppOpsController> lazy71 = this.mAppOpsController;
        Objects.requireNonNull(lazy71);
        this.mProviders.put(AppOpsController.class, new Dependency$$ExternalSyntheticLambda0(lazy71));
        Lazy<NavigationBarController> lazy72 = this.mNavigationBarController;
        Objects.requireNonNull(lazy72);
        this.mProviders.put(NavigationBarController.class, new Dependency$$ExternalSyntheticLambda0(lazy72));
        Lazy<AccessibilityFloatingMenuController> lazy73 = this.mAccessibilityFloatingMenuController;
        Objects.requireNonNull(lazy73);
        this.mProviders.put(AccessibilityFloatingMenuController.class, new Dependency$$ExternalSyntheticLambda0(lazy73));
        Lazy<StatusBarStateController> lazy74 = this.mStatusBarStateController;
        Objects.requireNonNull(lazy74);
        this.mProviders.put(StatusBarStateController.class, new Dependency$$ExternalSyntheticLambda0(lazy74));
        Lazy<NotificationLockscreenUserManager> lazy75 = this.mNotificationLockscreenUserManager;
        Objects.requireNonNull(lazy75);
        this.mProviders.put(NotificationLockscreenUserManager.class, new Dependency$$ExternalSyntheticLambda0(lazy75));
        Lazy<VisualStabilityManager> lazy76 = this.mVisualStabilityManager;
        Objects.requireNonNull(lazy76);
        this.mProviders.put(VisualStabilityManager.class, new Dependency$$ExternalSyntheticLambda0(lazy76));
        Lazy<NotificationGroupManagerLegacy> lazy77 = this.mNotificationGroupManager;
        Objects.requireNonNull(lazy77);
        this.mProviders.put(NotificationGroupManagerLegacy.class, new Dependency$$ExternalSyntheticLambda0(lazy77));
        Lazy<NotificationGroupAlertTransferHelper> lazy78 = this.mNotificationGroupAlertTransferHelper;
        Objects.requireNonNull(lazy78);
        this.mProviders.put(NotificationGroupAlertTransferHelper.class, new Dependency$$ExternalSyntheticLambda0(lazy78));
        Lazy<NotificationMediaManager> lazy79 = this.mNotificationMediaManager;
        Objects.requireNonNull(lazy79);
        this.mProviders.put(NotificationMediaManager.class, new Dependency$$ExternalSyntheticLambda0(lazy79));
        Lazy<NotificationGutsManager> lazy80 = this.mNotificationGutsManager;
        Objects.requireNonNull(lazy80);
        this.mProviders.put(NotificationGutsManager.class, new Dependency$$ExternalSyntheticLambda0(lazy80));
        Lazy<NotificationRemoteInputManager> lazy81 = this.mNotificationRemoteInputManager;
        Objects.requireNonNull(lazy81);
        this.mProviders.put(NotificationRemoteInputManager.class, new Dependency$$ExternalSyntheticLambda0(lazy81));
        Lazy<SmartReplyConstants> lazy82 = this.mSmartReplyConstants;
        Objects.requireNonNull(lazy82);
        this.mProviders.put(SmartReplyConstants.class, new Dependency$$ExternalSyntheticLambda0(lazy82));
        Lazy<NotificationListener> lazy83 = this.mNotificationListener;
        Objects.requireNonNull(lazy83);
        this.mProviders.put(NotificationListener.class, new Dependency$$ExternalSyntheticLambda0(lazy83));
        Lazy<NotificationLogger> lazy84 = this.mNotificationLogger;
        Objects.requireNonNull(lazy84);
        this.mProviders.put(NotificationLogger.class, new Dependency$$ExternalSyntheticLambda0(lazy84));
        Lazy<NotificationViewHierarchyManager> lazy85 = this.mNotificationViewHierarchyManager;
        Objects.requireNonNull(lazy85);
        this.mProviders.put(NotificationViewHierarchyManager.class, new Dependency$$ExternalSyntheticLambda0(lazy85));
        Lazy<NotificationFilter> lazy86 = this.mNotificationFilter;
        Objects.requireNonNull(lazy86);
        this.mProviders.put(NotificationFilter.class, new Dependency$$ExternalSyntheticLambda0(lazy86));
        Lazy<KeyguardDismissUtil> lazy87 = this.mKeyguardDismissUtil;
        Objects.requireNonNull(lazy87);
        this.mProviders.put(KeyguardDismissUtil.class, new Dependency$$ExternalSyntheticLambda0(lazy87));
        Lazy<SmartReplyController> lazy88 = this.mSmartReplyController;
        Objects.requireNonNull(lazy88);
        this.mProviders.put(SmartReplyController.class, new Dependency$$ExternalSyntheticLambda0(lazy88));
        Lazy<RemoteInputQuickSettingsDisabler> lazy89 = this.mRemoteInputQuickSettingsDisabler;
        Objects.requireNonNull(lazy89);
        this.mProviders.put(RemoteInputQuickSettingsDisabler.class, new Dependency$$ExternalSyntheticLambda0(lazy89));
        Lazy<NotificationEntryManager> lazy90 = this.mNotificationEntryManager;
        Objects.requireNonNull(lazy90);
        this.mProviders.put(NotificationEntryManager.class, new Dependency$$ExternalSyntheticLambda0(lazy90));
        Lazy<ForegroundServiceNotificationListener> lazy91 = this.mForegroundServiceNotificationListener;
        Objects.requireNonNull(lazy91);
        this.mProviders.put(ForegroundServiceNotificationListener.class, new Dependency$$ExternalSyntheticLambda0(lazy91));
        Lazy<ClockManager> lazy92 = this.mClockManager;
        Objects.requireNonNull(lazy92);
        this.mProviders.put(ClockManager.class, new Dependency$$ExternalSyntheticLambda0(lazy92));
        Lazy<PrivacyItemController> lazy93 = this.mPrivacyItemController;
        Objects.requireNonNull(lazy93);
        this.mProviders.put(PrivacyItemController.class, new Dependency$$ExternalSyntheticLambda0(lazy93));
        Lazy<ActivityManagerWrapper> lazy94 = this.mActivityManagerWrapper;
        Objects.requireNonNull(lazy94);
        this.mProviders.put(ActivityManagerWrapper.class, new Dependency$$ExternalSyntheticLambda0(lazy94));
        Lazy<DevicePolicyManagerWrapper> lazy95 = this.mDevicePolicyManagerWrapper;
        Objects.requireNonNull(lazy95);
        this.mProviders.put(DevicePolicyManagerWrapper.class, new Dependency$$ExternalSyntheticLambda0(lazy95));
        Lazy<PackageManagerWrapper> lazy96 = this.mPackageManagerWrapper;
        Objects.requireNonNull(lazy96);
        this.mProviders.put(PackageManagerWrapper.class, new Dependency$$ExternalSyntheticLambda0(lazy96));
        Lazy<SensorPrivacyController> lazy97 = this.mSensorPrivacyController;
        Objects.requireNonNull(lazy97);
        this.mProviders.put(SensorPrivacyController.class, new Dependency$$ExternalSyntheticLambda0(lazy97));
        Lazy<DockManager> lazy98 = this.mDockManager;
        Objects.requireNonNull(lazy98);
        this.mProviders.put(DockManager.class, new Dependency$$ExternalSyntheticLambda0(lazy98));
        Lazy<INotificationManager> lazy99 = this.mINotificationManager;
        Objects.requireNonNull(lazy99);
        this.mProviders.put(INotificationManager.class, new Dependency$$ExternalSyntheticLambda0(lazy99));
        Lazy<SysUiState> lazy100 = this.mSysUiStateFlagsContainer;
        Objects.requireNonNull(lazy100);
        this.mProviders.put(SysUiState.class, new Dependency$$ExternalSyntheticLambda0(lazy100));
        Lazy<AlarmManager> lazy101 = this.mAlarmManager;
        Objects.requireNonNull(lazy101);
        this.mProviders.put(AlarmManager.class, new Dependency$$ExternalSyntheticLambda0(lazy101));
        Lazy<KeyguardSecurityModel> lazy102 = this.mKeyguardSecurityModel;
        Objects.requireNonNull(lazy102);
        this.mProviders.put(KeyguardSecurityModel.class, new Dependency$$ExternalSyntheticLambda0(lazy102));
        Lazy<DozeParameters> lazy103 = this.mDozeParameters;
        Objects.requireNonNull(lazy103);
        this.mProviders.put(DozeParameters.class, new Dependency$$ExternalSyntheticLambda0(lazy103));
        Lazy<IWallpaperManager> lazy104 = this.mWallpaperManager;
        Objects.requireNonNull(lazy104);
        this.mProviders.put(IWallpaperManager.class, new Dependency$$ExternalSyntheticLambda0(lazy104));
        Lazy<CommandQueue> lazy105 = this.mCommandQueue;
        Objects.requireNonNull(lazy105);
        this.mProviders.put(CommandQueue.class, new Dependency$$ExternalSyntheticLambda0(lazy105));
        Lazy<ProtoTracer> lazy106 = this.mProtoTracer;
        Objects.requireNonNull(lazy106);
        this.mProviders.put(ProtoTracer.class, new Dependency$$ExternalSyntheticLambda0(lazy106));
        Lazy<DeviceConfigProxy> lazy107 = this.mDeviceConfigProxy;
        Objects.requireNonNull(lazy107);
        this.mProviders.put(DeviceConfigProxy.class, new Dependency$$ExternalSyntheticLambda0(lazy107));
        Lazy<TelephonyListenerManager> lazy108 = this.mTelephonyListenerManager;
        Objects.requireNonNull(lazy108);
        this.mProviders.put(TelephonyListenerManager.class, new Dependency$$ExternalSyntheticLambda0(lazy108));
        Lazy<AutoHideController> lazy109 = this.mAutoHideController;
        Objects.requireNonNull(lazy109);
        this.mProviders.put(AutoHideController.class, new Dependency$$ExternalSyntheticLambda0(lazy109));
        Lazy<RecordingController> lazy110 = this.mRecordingController;
        Objects.requireNonNull(lazy110);
        this.mProviders.put(RecordingController.class, new Dependency$$ExternalSyntheticLambda0(lazy110));
        Lazy<MediaOutputDialogFactory> lazy111 = this.mMediaOutputDialogFactory;
        Objects.requireNonNull(lazy111);
        this.mProviders.put(MediaOutputDialogFactory.class, new Dependency$$ExternalSyntheticLambda0(lazy111));
        Lazy<SystemStatusAnimationScheduler> lazy112 = this.mSystemStatusAnimationSchedulerLazy;
        Objects.requireNonNull(lazy112);
        this.mProviders.put(SystemStatusAnimationScheduler.class, new Dependency$$ExternalSyntheticLambda0(lazy112));
        Lazy<PrivacyDotViewController> lazy113 = this.mPrivacyDotViewControllerLazy;
        Objects.requireNonNull(lazy113);
        this.mProviders.put(PrivacyDotViewController.class, new Dependency$$ExternalSyntheticLambda0(lazy113));
        Lazy<InternetDialogFactory> lazy114 = this.mInternetDialogFactory;
        Objects.requireNonNull(lazy114);
        this.mProviders.put(InternetDialogFactory.class, new Dependency$$ExternalSyntheticLambda0(lazy114));
        Lazy<EdgeBackGestureHandler.Factory> lazy115 = this.mEdgeBackGestureHandlerFactoryLazy;
        Objects.requireNonNull(lazy115);
        this.mProviders.put(EdgeBackGestureHandler.Factory.class, new Dependency$$ExternalSyntheticLambda0(lazy115));
        Lazy<UiEventLogger> lazy116 = this.mUiEventLogger;
        Objects.requireNonNull(lazy116);
        this.mProviders.put(UiEventLogger.class, new Dependency$$ExternalSyntheticLambda0(lazy116));
        Lazy<FeatureFlags> lazy117 = this.mFeatureFlagsLazy;
        Objects.requireNonNull(lazy117);
        this.mProviders.put(FeatureFlags.class, new Dependency$$ExternalSyntheticLambda0(lazy117));
        Lazy<StatusBarContentInsetsProvider> lazy118 = this.mContentInsetsProviderLazy;
        Objects.requireNonNull(lazy118);
        this.mProviders.put(StatusBarContentInsetsProvider.class, new Dependency$$ExternalSyntheticLambda0(lazy118));
        Lazy<NotificationSectionsManager> lazy119 = this.mNotificationSectionsManagerLazy;
        Objects.requireNonNull(lazy119);
        this.mProviders.put(NotificationSectionsManager.class, new Dependency$$ExternalSyntheticLambda0(lazy119));
        Lazy<ScreenOffAnimationController> lazy120 = this.mScreenOffAnimationController;
        Objects.requireNonNull(lazy120);
        this.mProviders.put(ScreenOffAnimationController.class, new Dependency$$ExternalSyntheticLambda0(lazy120));
        Lazy<AmbientState> lazy121 = this.mAmbientStateLazy;
        Objects.requireNonNull(lazy121);
        this.mProviders.put(AmbientState.class, new Dependency$$ExternalSyntheticLambda0(lazy121));
        Lazy<GroupMembershipManager> lazy122 = this.mGroupMembershipManagerLazy;
        Objects.requireNonNull(lazy122);
        this.mProviders.put(GroupMembershipManager.class, new Dependency$$ExternalSyntheticLambda0(lazy122));
        Lazy<GroupExpansionManager> lazy123 = this.mGroupExpansionManagerLazy;
        Objects.requireNonNull(lazy123);
        this.mProviders.put(GroupExpansionManager.class, new Dependency$$ExternalSyntheticLambda0(lazy123));
        Lazy<SystemUIDialogManager> lazy124 = this.mSystemUIDialogManagerLazy;
        Objects.requireNonNull(lazy124);
        this.mProviders.put(SystemUIDialogManager.class, new Dependency$$ExternalSyntheticLambda0(lazy124));
        Lazy<DialogLaunchAnimator> lazy125 = this.mDialogLaunchAnimatorLazy;
        Objects.requireNonNull(lazy125);
        this.mProviders.put(DialogLaunchAnimator.class, new Dependency$$ExternalSyntheticLambda0(lazy125));
        setInstance(this);
    }

    public static void setInstance(Dependency dependency) {
        sDependency = dependency;
    }

    /* access modifiers changed from: protected */
    public final <T> T getDependency(Class<T> cls) {
        return getDependencyInner(cls);
    }

    /* access modifiers changed from: protected */
    public final <T> T getDependency(DependencyKey<T> dependencyKey) {
        return getDependencyInner(dependencyKey);
    }

    private synchronized <T> T getDependencyInner(Object obj) {
        T t;
        t = this.mDependencies.get(obj);
        if (t == null) {
            t = createDependency(obj);
            this.mDependencies.put(obj, t);
        }
        return t;
    }

    public <T> T createDependency(Object obj) {
        Preconditions.checkArgument((obj instanceof DependencyKey) || (obj instanceof Class));
        LazyDependencyCreator lazyDependencyCreator = this.mProviders.get(obj);
        if (lazyDependencyCreator != null) {
            return lazyDependencyCreator.createDependency();
        }
        throw new IllegalArgumentException("Unsupported dependency " + obj + ". " + this.mProviders.size() + " providers known.");
    }

    private <T> void destroyDependency(Class<T> cls, Consumer<T> consumer) {
        Object remove = this.mDependencies.remove(cls);
        if (remove instanceof Dumpable) {
            this.mDumpManager.unregisterDumpable(remove.getClass().getName());
        }
        if (remove != null && consumer != null) {
            consumer.accept(remove);
        }
    }

    public static void clearDependencies() {
        sDependency = null;
    }

    public static <T> void destroy(Class<T> cls, Consumer<T> consumer) {
        sDependency.destroyDependency(cls, consumer);
    }

    @Deprecated
    public static <T> T get(Class<T> cls) {
        return sDependency.getDependency(cls);
    }

    @Deprecated
    public static <T> T get(DependencyKey<T> dependencyKey) {
        return sDependency.getDependency(dependencyKey);
    }

    public static final class DependencyKey<V> {
        private final String mDisplayName;

        public DependencyKey(String str) {
            this.mDisplayName = str;
        }

        public String toString() {
            return this.mDisplayName;
        }
    }
}
