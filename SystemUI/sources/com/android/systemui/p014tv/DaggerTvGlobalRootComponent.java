package com.android.systemui.p014tv;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AlarmManager;
import android.app.IActivityManager;
import android.app.INotificationManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.Service;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.app.smartspace.SmartspaceManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorPrivacyManager;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.NightDisplayListener;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.AudioManager;
import android.media.IAudioService;
import android.media.MediaRouter2Manager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.net.ConnectivityManager;
import android.net.NetworkScoreManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerExemptionManager;
import android.os.PowerManager;
import android.os.UserManager;
import android.os.Vibrator;
import android.permission.PermissionManager;
import android.safetycenter.SafetyCenterManager;
import android.service.dreams.IDreamManager;
import android.service.notification.StatusBarNotification;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.CrossWindowBlurListeners;
import android.view.GestureDetector;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.inputmethod.InputMethodManager;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.app.AssistUtils;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.LatencyTracker;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.keyguard.ActiveUnlockConfig_Factory;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.AdminSecondaryLockScreenController_Factory_Factory;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.EmergencyButtonController_Factory_Factory;
import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardDisplayManager;
import com.android.keyguard.KeyguardDisplayManager_Factory;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardHostViewController_Factory;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardInputViewController_Factory_Factory;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardMessageAreaController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController_Factory_Factory;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardSecurityModel_Factory;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.keyguard.KeyguardSecurityViewFlipperController;
import com.android.keyguard.KeyguardSecurityViewFlipperController_Factory;
import com.android.keyguard.KeyguardSliceView;
import com.android.keyguard.KeyguardSliceViewController;
import com.android.keyguard.KeyguardSliceViewController_Factory;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.keyguard.KeyguardUnfoldTransition_Factory;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitor_Factory;
import com.android.keyguard.LiftToActivateListener_Factory;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.clock.ClockManager;
import com.android.keyguard.clock.ClockManager_Factory;
import com.android.keyguard.clock.ClockModule_ProvideClockInfoListFactory;
import com.android.keyguard.clock.ClockOptionsProvider;
import com.android.keyguard.clock.ClockOptionsProvider_MembersInjector;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardHostViewFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory;
import com.android.keyguard.dagger.KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardClockSwitchFactory;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardSliceViewFactory;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.keyguard.mediator.ScreenOnCoordinator_Factory;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.RootDisplayAreaOrganizer;
import com.android.p019wm.shell.ShellCommandHandler;
import com.android.p019wm.shell.ShellCommandHandlerImpl;
import com.android.p019wm.shell.ShellInit;
import com.android.p019wm.shell.ShellInitImpl;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.TaskViewFactory;
import com.android.p019wm.shell.TaskViewFactoryController;
import com.android.p019wm.shell.TaskViewTransitions;
import com.android.p019wm.shell.WindowManagerShellWrapper;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.p019wm.shell.animation.FlingAnimationUtils_Builder_Factory;
import com.android.p019wm.shell.apppairs.AppPairs;
import com.android.p019wm.shell.apppairs.AppPairsController;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.back.BackAnimationController;
import com.android.p019wm.shell.bubbles.BubbleController;
import com.android.p019wm.shell.bubbles.Bubbles;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipAnimationControllerFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipAppOpsListenerFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipParamsChangedForwarderFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipSnapAlgorithmFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipTaskOrganizerFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidePipTransitionStateFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvideTvPipBoundsAlgorithmFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvideTvPipBoundsControllerFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvideTvPipBoundsStateFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvideTvPipNotificationControllerFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvideTvPipTransitionFactory;
import com.android.p019wm.shell.dagger.TvPipModule_ProvidesTvPipMenuControllerFactory;
import com.android.p019wm.shell.dagger.TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideAppPairsFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideBackAnimationControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideBackAnimationFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideBubblesFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideCompatUIControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideCompatUIFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDisplayAreaHelperFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDisplayControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDisplayImeControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDisplayInsetsControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideFreeformTaskListenerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideFullscreenTaskListenerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideHideDisplayCutoutFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideIconProviderFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideLegacySplitScreenFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideOneHandedFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvidePipMediaControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvidePipUiEventLoggerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideRecentTasksFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideRemoteTransitionsFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideShellCommandHandlerImplFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideShellInitFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideShellInitImplFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideShellTaskOrganizerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideSplitScreenFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideStartingSurfaceFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideStartingWindowControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideSyncTransactionQueueFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideSystemWindowsFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTaskSurfaceHelperFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTaskViewFactoryControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTaskViewFactoryFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTaskViewTransitionsFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTransactionPoolFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideTransitionsFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideWindowManagerShellWrapperFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProviderTaskStackListenerImplFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvidesOneHandedControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvidesSplitScreenControllerFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideMainHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.p019wm.shell.draganddrop.DragAndDrop;
import com.android.p019wm.shell.draganddrop.DragAndDropController;
import com.android.p019wm.shell.freeform.FreeformTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenTaskListener;
import com.android.p019wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.p019wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.p019wm.shell.onehanded.OneHanded;
import com.android.p019wm.shell.onehanded.OneHandedController;
import com.android.p019wm.shell.pip.Pip;
import com.android.p019wm.shell.pip.PipAnimationController;
import com.android.p019wm.shell.pip.PipAppOpsListener;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipTransitionState;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsAlgorithm;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsController;
import com.android.p019wm.shell.pip.p020tv.TvPipBoundsState;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuController;
import com.android.p019wm.shell.pip.p020tv.TvPipNotificationController;
import com.android.p019wm.shell.pip.phone.PipTouchHandler;
import com.android.p019wm.shell.recents.RecentTasks;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldTransitionHandler;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.devicestate.DeviceStateRotationLockSettingsManager;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.ActivityIntentHelper_Factory;
import com.android.systemui.ActivityStarterDelegate;
import com.android.systemui.ActivityStarterDelegate_Factory;
import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.BootCompleteCacheImpl_Factory;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.Dependency_Factory;
import com.android.systemui.ForegroundServiceController;
import com.android.systemui.ForegroundServiceController_Factory;
import com.android.systemui.ForegroundServiceNotificationListener;
import com.android.systemui.ForegroundServiceNotificationListener_Factory;
import com.android.systemui.ForegroundServicesDialog;
import com.android.systemui.ForegroundServicesDialog_Factory;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.ImageWallpaper_Factory;
import com.android.systemui.InitController;
import com.android.systemui.InitController_Factory;
import com.android.systemui.SliceBroadcastRelayHandler;
import com.android.systemui.SliceBroadcastRelayHandler_Factory;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.SystemUIAppComponentFactory_MembersInjector;
import com.android.systemui.SystemUIService;
import com.android.systemui.SystemUIService_Factory;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.UiOffloadThread_Factory;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver_Factory;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver_Factory;
import com.android.systemui.accessibility.ModeSwitchesController;
import com.android.systemui.accessibility.ModeSwitchesController_Factory;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.accessibility.SystemActions_Factory;
import com.android.systemui.accessibility.WindowMagnification;
import com.android.systemui.accessibility.WindowMagnification_Factory;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController_Factory;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.appops.AppOpsControllerImpl;
import com.android.systemui.appops.AppOpsControllerImpl_Factory;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistLogger_Factory;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistManager_Factory;
import com.android.systemui.assist.AssistModule_ProvideAssistUtilsFactory;
import com.android.systemui.assist.PhoneStateMonitor;
import com.android.systemui.assist.PhoneStateMonitor_Factory;
import com.android.systemui.assist.p009ui.DefaultUiController;
import com.android.systemui.assist.p009ui.DefaultUiController_Factory;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.battery.BatteryMeterViewController_Factory;
import com.android.systemui.biometrics.AlternateUdfpsTouchProvider;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthController_Factory;
import com.android.systemui.biometrics.SidefpsController;
import com.android.systemui.biometrics.SidefpsController_Factory;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.biometrics.UdfpsController_Factory;
import com.android.systemui.biometrics.UdfpsHapticsSimulator;
import com.android.systemui.biometrics.UdfpsHapticsSimulator_Factory;
import com.android.systemui.biometrics.UdfpsHbmProvider;
import com.android.systemui.biometrics.UdfpsShell;
import com.android.systemui.biometrics.UdfpsShell_Factory;
import com.android.systemui.biometrics.dagger.BiometricsModule_ProvidesPluginExecutorFactory;
import com.android.systemui.bluetooth.BroadcastDialogController;
import com.android.systemui.bluetooth.BroadcastDialogController_Factory;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastDispatcherStartable;
import com.android.systemui.broadcast.BroadcastDispatcherStartable_Factory;
import com.android.systemui.broadcast.BroadcastDispatcher_Factory;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.broadcast.BroadcastSender_Factory;
import com.android.systemui.broadcast.PendingRemovalStore;
import com.android.systemui.broadcast.PendingRemovalStore_Factory;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger_Factory;
import com.android.systemui.classifier.BrightLineFalsingManager;
import com.android.systemui.classifier.BrightLineFalsingManager_Factory;
import com.android.systemui.classifier.DiagonalClassifier_Factory;
import com.android.systemui.classifier.DistanceClassifier_Factory;
import com.android.systemui.classifier.DoubleTapClassifier;
import com.android.systemui.classifier.DoubleTapClassifier_Factory;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.classifier.FalsingCollectorImpl_Factory;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.FalsingDataProvider_Factory;
import com.android.systemui.classifier.FalsingManagerProxy;
import com.android.systemui.classifier.FalsingManagerProxy_Factory;
import com.android.systemui.classifier.FalsingModule_ProvidesBrightLineGestureClassifiersFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTimeoutMsFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesDoubleTapTouchSlopFactory;
import com.android.systemui.classifier.FalsingModule_ProvidesSingleTapTouchSlopFactory;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.classifier.HistoryTracker_Factory;
import com.android.systemui.classifier.PointerCountClassifier_Factory;
import com.android.systemui.classifier.ProximityClassifier_Factory;
import com.android.systemui.classifier.SingleTapClassifier;
import com.android.systemui.classifier.SingleTapClassifier_Factory;
import com.android.systemui.classifier.TypeClassifier;
import com.android.systemui.classifier.TypeClassifier_Factory;
import com.android.systemui.classifier.ZigZagClassifier_Factory;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.colorextraction.SysuiColorExtractor_Factory;
import com.android.systemui.controls.ControlsMetricsLoggerImpl;
import com.android.systemui.controls.ControlsMetricsLoggerImpl_Factory;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.CustomIconCache_Factory;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsControllerImpl_Factory;
import com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.dagger.ControlsComponent_Factory;
import com.android.systemui.controls.dagger.ControlsModule_ProvidesControlsFeatureEnabledFactory;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsEditingActivity_Factory;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity_Factory;
import com.android.systemui.controls.management.ControlsListingControllerImpl;
import com.android.systemui.controls.management.ControlsListingControllerImpl_Factory;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity_Factory;
import com.android.systemui.controls.management.ControlsRequestDialog;
import com.android.systemui.controls.management.ControlsRequestDialog_Factory;
import com.android.systemui.controls.p010ui.ControlActionCoordinatorImpl;
import com.android.systemui.controls.p010ui.ControlActionCoordinatorImpl_Factory;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.controls.p010ui.ControlsActivity_Factory;
import com.android.systemui.controls.p010ui.ControlsUiControllerImpl;
import com.android.systemui.controls.p010ui.ControlsUiControllerImpl_Factory;
import com.android.systemui.dagger.AndroidInternalsModule;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideLockPatternUtilsFactory;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideMetricsLoggerFactory;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideNotificationMessagingUtilFactory;
import com.android.systemui.dagger.AndroidInternalsModule_ProvideUiEventLoggerFactory;
import com.android.systemui.dagger.C2055x19afde28;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.ContextComponentResolver;
import com.android.systemui.dagger.ContextComponentResolver_Factory;
import com.android.systemui.dagger.FrameworkServicesModule;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAccessibilityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideActivityTaskManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAlarmManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideAudioManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCaptioningManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCarrierConfigManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideColorDisplayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideConnectivityManagagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideContentResolverFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDevicePolicyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDeviceStateManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDisplayIdFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideDisplayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideFaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIActivityManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIAudioServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIDreamManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideINotificationManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIStatusBarServiceFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWallPaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideInputMethodManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideInteractionJankMonitorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideIsTestHarnessFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideKeyguardManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideLatencyTrackerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideLauncherAppsFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaProjectionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaRouter2ManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideMediaSessionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideNetworkScoreManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideNotificationManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOptionalTelecomManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideOverlayManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePackageManagerWrapperFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePermissionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePowerExemptionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidePowerManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideResourcesFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSafetyCenterManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSensorPrivacyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSharePreferencesFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideShortcutManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSmartspaceManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideSubcriptionManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTelecomManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTelephonyManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideTrustManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideUiModeManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideUserManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideVibratorFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideViewConfigurationFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWallpaperManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWifiManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvideWindowManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProviderLayoutInflaterFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesChoreographerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesFingerprintManagerFactory;
import com.android.systemui.dagger.FrameworkServicesModule_ProvidesSensorManagerFactory;
import com.android.systemui.dagger.GlobalModule;
import com.android.systemui.dagger.GlobalModule_ProvideDisplayMetricsFactory;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.NightDisplayListenerModule_Builder_Factory;
import com.android.systemui.dagger.NightDisplayListenerModule_ProvideNightDisplayListenerFactory;
import com.android.systemui.dagger.PluginModule_ProvideActivityStarterFactory;
import com.android.systemui.dagger.SettingsLibraryModule_ProvideLocalBluetoothControllerFactory;
import com.android.systemui.dagger.SharedLibraryModule;
import com.android.systemui.dagger.SharedLibraryModule_ProvideActivityManagerWrapperFactory;
import com.android.systemui.dagger.SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory;
import com.android.systemui.dagger.SharedLibraryModule_ProvideTaskStackChangeListenersFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideBubblesManagerFactory;
import com.android.systemui.dagger.SystemUIModule_ProvideSysUiStateFactory;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.demomode.dagger.DemoModeModule_ProvideDemoModeControllerFactory;
import com.android.systemui.dock.DockManagerImpl;
import com.android.systemui.dock.DockManagerImpl_Factory;
import com.android.systemui.doze.AlwaysOnDisplayPolicy;
import com.android.systemui.doze.AlwaysOnDisplayPolicy_Factory;
import com.android.systemui.doze.DozeAuthRemover;
import com.android.systemui.doze.DozeAuthRemover_Factory;
import com.android.systemui.doze.DozeDockHandler;
import com.android.systemui.doze.DozeDockHandler_Factory;
import com.android.systemui.doze.DozeFalsingManagerAdapter;
import com.android.systemui.doze.DozeFalsingManagerAdapter_Factory;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.doze.DozeLog_Factory;
import com.android.systemui.doze.DozeLogger;
import com.android.systemui.doze.DozeLogger_Factory;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.DozeMachine_Factory;
import com.android.systemui.doze.DozePauser;
import com.android.systemui.doze.DozePauser_Factory;
import com.android.systemui.doze.DozeScreenBrightness;
import com.android.systemui.doze.DozeScreenBrightness_Factory;
import com.android.systemui.doze.DozeScreenState;
import com.android.systemui.doze.DozeScreenState_Factory;
import com.android.systemui.doze.DozeService;
import com.android.systemui.doze.DozeService_Factory;
import com.android.systemui.doze.DozeSuppressor;
import com.android.systemui.doze.DozeSuppressor_Factory;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.doze.DozeTriggers_Factory;
import com.android.systemui.doze.DozeUi;
import com.android.systemui.doze.DozeUi_Factory;
import com.android.systemui.doze.DozeWallpaperState;
import com.android.systemui.doze.DozeWallpaperState_Factory;
import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.doze.dagger.DozeModule_ProvidesBrightnessSensorsFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeMachinePartsFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesDozeWakeLockFactory;
import com.android.systemui.doze.dagger.DozeModule_ProvidesWrappedServiceFactory;
import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.dreams.DreamOverlayContainerViewController_Factory;
import com.android.systemui.dreams.DreamOverlayNotificationCountProvider;
import com.android.systemui.dreams.DreamOverlayNotificationCountProvider_Factory;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.dreams.DreamOverlayService_Factory;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.DreamOverlayStateController_Factory;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import com.android.systemui.dreams.DreamOverlayStatusBarViewController;
import com.android.systemui.dreams.DreamOverlayStatusBarViewController_Factory;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationCollectionLiveData;
import com.android.systemui.dreams.complication.ComplicationCollectionLiveData_Factory;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel;
import com.android.systemui.dreams.complication.ComplicationCollectionViewModel_Factory;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.dreams.complication.ComplicationHostViewController_Factory;
import com.android.systemui.dreams.complication.ComplicationId;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine_Factory;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.dreams.complication.ComplicationViewModelProvider;
import com.android.systemui.dreams.complication.ComplicationViewModelTransformer;
import com.android.systemui.dreams.complication.ComplicationViewModelTransformer_Factory;
import com.android.systemui.dreams.complication.dagger.C2091xa92fda9a;
import com.android.systemui.dreams.complication.dagger.C2092x3242d31f;
import com.android.systemui.dreams.complication.dagger.C2093x4a872b5c;
import com.android.systemui.dreams.complication.dagger.C2094x1653c7a9;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationHostViewFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationHostViewModule_ProvidesComplicationPaddingFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationModule_ProvidesVisibilityControllerFactory;
import com.android.systemui.dreams.complication.dagger.ComplicationViewModelComponent;
import com.android.systemui.dreams.dagger.DreamOverlayComponent;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesDreamOverlayContentViewFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesLifecycleFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesLifecycleOwnerFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesLifecycleRegistryFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesMaxBurnInOffsetFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesMillisUntilFullJitterFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesTouchInsetManagerFactory;
import com.android.systemui.dreams.dagger.DreamOverlayModule_ProvidesTouchInsetSessionFactory;
import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler;
import com.android.systemui.dreams.touch.HideComplicationTouchHandler_Factory;
import com.android.systemui.dreams.touch.InputSession;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory;
import com.android.systemui.dreams.touch.dagger.BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory;
import com.android.systemui.dreams.touch.dagger.C2104x4f80e514;
import com.android.systemui.dreams.touch.dagger.C2105x6a8a7f11;
import com.android.systemui.dreams.touch.dagger.C2106xc6ed12da;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.DumpHandler_Factory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.dump.DumpManager_Factory;
import com.android.systemui.dump.LogBufferEulogizer;
import com.android.systemui.dump.LogBufferEulogizer_Factory;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.LogBufferFreezer_Factory;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService_Factory;
import com.android.systemui.flags.FeatureFlagsRelease;
import com.android.systemui.flags.FeatureFlagsRelease_Factory;
import com.android.systemui.flags.SystemPropertiesHelper;
import com.android.systemui.flags.SystemPropertiesHelper_Factory;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.fragments.FragmentService_Factory;
import com.android.systemui.globalactions.GlobalActionsComponent;
import com.android.systemui.globalactions.GlobalActionsComponent_Factory;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.globalactions.GlobalActionsDialogLite_Factory;
import com.android.systemui.globalactions.GlobalActionsImpl;
import com.android.systemui.globalactions.GlobalActionsImpl_Factory;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity_Factory;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper_Factory;
import com.android.systemui.keyboard.KeyboardUI;
import com.android.systemui.keyboard.KeyboardUI_Factory;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.DismissCallbackRegistry_Factory;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher;
import com.android.systemui.keyguard.KeyguardLifecyclesDispatcher_Factory;
import com.android.systemui.keyguard.KeyguardService;
import com.android.systemui.keyguard.KeyguardService_Factory;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.keyguard.KeyguardSliceProvider_MembersInjector;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController_Factory;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider;
import com.android.systemui.keyguard.LifecycleScreenStatusProvider_Factory;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.ScreenLifecycle_Factory;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle_Factory;
import com.android.systemui.keyguard.WorkLockActivity;
import com.android.systemui.keyguard.WorkLockActivity_Factory;
import com.android.systemui.keyguard.dagger.KeyguardModule;
import com.android.systemui.keyguard.dagger.KeyguardModule_NewKeyguardViewMediatorFactory;
import com.android.systemui.keyguard.dagger.KeyguardModule_ProvidesViewMediatorCallbackFactory;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.log.LogBufferFactory_Factory;
import com.android.systemui.log.LogcatEchoTracker;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.log.SessionTracker_Factory;
import com.android.systemui.log.dagger.LogModule_ProvideBroadcastDispatcherLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideDozeLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideLSShadeTransitionControllerBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideLogcatEchoTrackerFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaBrowserBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaCarouselControllerBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaMuteAwaitLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaTttReceiverLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaTttSenderLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideMediaViewLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNearbyMediaDevicesLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotifInteractionLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationHeadsUpLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationSectionLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideNotificationsLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidePrivacyLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideQSFragmentDisableLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideQuickSettingsLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideToastLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidesMediaTimeoutListenerLogBufferFactory;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.KeyguardMediaController_Factory;
import com.android.systemui.media.LocalMediaManagerFactory;
import com.android.systemui.media.LocalMediaManagerFactory_Factory;
import com.android.systemui.media.MediaBrowserFactory;
import com.android.systemui.media.MediaBrowserFactory_Factory;
import com.android.systemui.media.MediaCarouselController;
import com.android.systemui.media.MediaCarouselControllerLogger;
import com.android.systemui.media.MediaCarouselControllerLogger_Factory;
import com.android.systemui.media.MediaCarouselController_Factory;
import com.android.systemui.media.MediaControlPanel;
import com.android.systemui.media.MediaControlPanel_Factory;
import com.android.systemui.media.MediaControllerFactory;
import com.android.systemui.media.MediaControllerFactory_Factory;
import com.android.systemui.media.MediaDataCombineLatest_Factory;
import com.android.systemui.media.MediaDataFilter;
import com.android.systemui.media.MediaDataFilter_Factory;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaDataManager_Factory;
import com.android.systemui.media.MediaDeviceManager;
import com.android.systemui.media.MediaDeviceManager_Factory;
import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.media.MediaFeatureFlag_Factory;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.MediaFlags_Factory;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHierarchyManager_Factory;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import com.android.systemui.media.MediaHostStatesManager_Factory;
import com.android.systemui.media.MediaHost_MediaHostStateHolder_Factory;
import com.android.systemui.media.MediaResumeListener;
import com.android.systemui.media.MediaResumeListener_Factory;
import com.android.systemui.media.MediaSessionBasedFilter;
import com.android.systemui.media.MediaSessionBasedFilter_Factory;
import com.android.systemui.media.MediaTimeoutListener;
import com.android.systemui.media.MediaTimeoutListener_Factory;
import com.android.systemui.media.MediaTimeoutLogger;
import com.android.systemui.media.MediaTimeoutLogger_Factory;
import com.android.systemui.media.MediaUiEventLogger;
import com.android.systemui.media.MediaUiEventLogger_Factory;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.MediaViewController_Factory;
import com.android.systemui.media.MediaViewLogger;
import com.android.systemui.media.MediaViewLogger_Factory;
import com.android.systemui.media.ResumeMediaBrowserFactory;
import com.android.systemui.media.ResumeMediaBrowserFactory_Factory;
import com.android.systemui.media.ResumeMediaBrowserLogger;
import com.android.systemui.media.ResumeMediaBrowserLogger_Factory;
import com.android.systemui.media.RingtonePlayer;
import com.android.systemui.media.RingtonePlayer_Factory;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.SeekBarViewModel_Factory;
import com.android.systemui.media.SmartspaceMediaDataProvider_Factory;
import com.android.systemui.media.dagger.MediaModule_ProvidesKeyguardMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerReceiverFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttChipControllerSenderFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttCommandLineHelperFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttReceiverLoggerFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesMediaTttSenderLoggerFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesNearbyMediaDevicesManagerFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQSMediaHostFactory;
import com.android.systemui.media.dagger.MediaModule_ProvidesQuickQSMediaHostFactory;
import com.android.systemui.media.dialog.MediaOutputBroadcastDialogFactory;
import com.android.systemui.media.dialog.MediaOutputBroadcastDialogFactory_Factory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.media.dialog.MediaOutputDialogFactory_Factory;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver;
import com.android.systemui.media.dialog.MediaOutputDialogReceiver_Factory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli_Factory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory;
import com.android.systemui.media.muteawait.MediaMuteAwaitConnectionManagerFactory_Factory;
import com.android.systemui.media.muteawait.MediaMuteAwaitLogger;
import com.android.systemui.media.muteawait.MediaMuteAwaitLogger_Factory;
import com.android.systemui.media.nearby.NearbyMediaDevicesLogger;
import com.android.systemui.media.nearby.NearbyMediaDevicesLogger_Factory;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager_Factory;
import com.android.systemui.media.systemsounds.HomeSoundEffectController;
import com.android.systemui.media.systemsounds.HomeSoundEffectController_Factory;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper;
import com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper_Factory;
import com.android.systemui.media.taptotransfer.MediaTttFlags;
import com.android.systemui.media.taptotransfer.MediaTttFlags_Factory;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver;
import com.android.systemui.media.taptotransfer.receiver.MediaTttChipControllerReceiver_Factory;
import com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverUiEventLogger;
import com.android.systemui.media.taptotransfer.receiver.MediaTttReceiverUiEventLogger_Factory;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender;
import com.android.systemui.media.taptotransfer.sender.MediaTttChipControllerSender_Factory;
import com.android.systemui.media.taptotransfer.sender.MediaTttSenderUiEventLogger;
import com.android.systemui.media.taptotransfer.sender.MediaTttSenderUiEventLogger_Factory;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavBarHelper_Factory;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarController_Factory;
import com.android.systemui.navigationbar.NavigationBarFrame;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideEdgeBackGestureHandlerFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideLayoutInflaterFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideNavigationBarFrameFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideNavigationBarviewFactory;
import com.android.systemui.navigationbar.NavigationBarModule_ProvideWindowManagerFactory;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.NavigationBarTransitions_Factory;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationBar_Factory;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.NavigationModeController_Factory;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.navigationbar.TaskbarDelegate_Factory;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.DeadZone_Factory;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler_Factory_Factory;
import com.android.systemui.p012qs.AutoAddTracker;
import com.android.systemui.p012qs.AutoAddTracker_Builder_Factory;
import com.android.systemui.p012qs.FgsManagerController;
import com.android.systemui.p012qs.FgsManagerController_Factory;
import com.android.systemui.p012qs.FooterActionsController;
import com.android.systemui.p012qs.FooterActionsController_Factory;
import com.android.systemui.p012qs.FooterActionsView;
import com.android.systemui.p012qs.HeaderPrivacyIconsController;
import com.android.systemui.p012qs.HeaderPrivacyIconsController_Factory;
import com.android.systemui.p012qs.QSAnimator;
import com.android.systemui.p012qs.QSAnimator_Factory;
import com.android.systemui.p012qs.QSContainerImpl;
import com.android.systemui.p012qs.QSContainerImplController;
import com.android.systemui.p012qs.QSContainerImplController_Factory;
import com.android.systemui.p012qs.QSExpansionPathInterpolator;
import com.android.systemui.p012qs.QSExpansionPathInterpolator_Factory;
import com.android.systemui.p012qs.QSFgsManagerFooter;
import com.android.systemui.p012qs.QSFgsManagerFooter_Factory;
import com.android.systemui.p012qs.QSFooter;
import com.android.systemui.p012qs.QSFooterView;
import com.android.systemui.p012qs.QSFooterViewController;
import com.android.systemui.p012qs.QSFooterViewController_Factory;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.p012qs.QSFragmentDisableFlagsLogger;
import com.android.systemui.p012qs.QSPanel;
import com.android.systemui.p012qs.QSPanelController;
import com.android.systemui.p012qs.QSPanelController_Factory;
import com.android.systemui.p012qs.QSSecurityFooter;
import com.android.systemui.p012qs.QSSecurityFooter_Factory;
import com.android.systemui.p012qs.QSSquishinessController;
import com.android.systemui.p012qs.QSSquishinessController_Factory;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.QSTileHost_Factory;
import com.android.systemui.p012qs.QSTileRevealController_Factory_Factory;
import com.android.systemui.p012qs.QuickQSPanel;
import com.android.systemui.p012qs.QuickQSPanelController;
import com.android.systemui.p012qs.QuickQSPanelController_Factory;
import com.android.systemui.p012qs.QuickStatusBarHeader;
import com.android.systemui.p012qs.QuickStatusBarHeaderController_Factory;
import com.android.systemui.p012qs.ReduceBrightColorsController;
import com.android.systemui.p012qs.ReduceBrightColorsController_Factory;
import com.android.systemui.p012qs.carrier.C2359xf95dc14f;
import com.android.systemui.p012qs.carrier.QSCarrierGroupController;
import com.android.systemui.p012qs.carrier.QSCarrierGroupController_Builder_Factory;
import com.android.systemui.p012qs.customize.QSCustomizer;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.customize.QSCustomizerController_Factory;
import com.android.systemui.p012qs.customize.TileAdapter;
import com.android.systemui.p012qs.customize.TileAdapter_Factory;
import com.android.systemui.p012qs.customize.TileQueryHelper;
import com.android.systemui.p012qs.customize.TileQueryHelper_Factory;
import com.android.systemui.p012qs.dagger.QSFlagsModule_IsPMLiteEnabledFactory;
import com.android.systemui.p012qs.dagger.QSFlagsModule_IsReduceBrightColorsAvailableFactory;
import com.android.systemui.p012qs.dagger.QSFragmentComponent;
import com.android.systemui.p012qs.dagger.QSFragmentModule_GetBatteryMeterViewControllerFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_GetBatteryMeterViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvideQSPanelFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvideRootViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvideThemedContextFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvideThemedLayoutInflaterFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesBatteryMeterViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesNTQSStatusBarFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesPrivacyChipFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSContainerImplFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSCutomizerFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSFooterActionsViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSFooterFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSFooterViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSSecurityFooterViewFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQSUsingMediaPlayerFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQuickQSPanelFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesQuickStatusBarHeaderFactory;
import com.android.systemui.p012qs.dagger.QSFragmentModule_ProvidesStatusIconContainerFactory;
import com.android.systemui.p012qs.dagger.QSModule_ProvideAutoTileManagerFactory;
import com.android.systemui.p012qs.external.C4834TileLifecycleManager_Factory;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.external.CustomTileStatePersister;
import com.android.systemui.p012qs.external.CustomTileStatePersister_Factory;
import com.android.systemui.p012qs.external.CustomTile_Builder_Factory;
import com.android.systemui.p012qs.external.PackageManagerAdapter;
import com.android.systemui.p012qs.external.PackageManagerAdapter_Factory;
import com.android.systemui.p012qs.external.TileLifecycleManager;
import com.android.systemui.p012qs.external.TileLifecycleManager_Factory_Impl;
import com.android.systemui.p012qs.external.TileServiceRequestController;
import com.android.systemui.p012qs.external.TileServiceRequestController_Builder_Factory;
import com.android.systemui.p012qs.external.TileServices;
import com.android.systemui.p012qs.external.TileServices_Factory;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.logging.QSLogger_Factory;
import com.android.systemui.p012qs.tileimpl.QSFactoryImpl;
import com.android.systemui.p012qs.tileimpl.QSFactoryImpl_Factory;
import com.android.systemui.p012qs.tiles.AirplaneModeTile;
import com.android.systemui.p012qs.tiles.AirplaneModeTile_Factory;
import com.android.systemui.p012qs.tiles.AlarmTile;
import com.android.systemui.p012qs.tiles.AlarmTile_Factory;
import com.android.systemui.p012qs.tiles.BatterySaverTile;
import com.android.systemui.p012qs.tiles.BatterySaverTile_Factory;
import com.android.systemui.p012qs.tiles.BluetoothTile;
import com.android.systemui.p012qs.tiles.BluetoothTile_Factory;
import com.android.systemui.p012qs.tiles.CameraToggleTile;
import com.android.systemui.p012qs.tiles.CameraToggleTile_Factory;
import com.android.systemui.p012qs.tiles.CastTile;
import com.android.systemui.p012qs.tiles.CastTile_Factory;
import com.android.systemui.p012qs.tiles.CellularTile;
import com.android.systemui.p012qs.tiles.CellularTile_Factory;
import com.android.systemui.p012qs.tiles.ColorCorrectionTile;
import com.android.systemui.p012qs.tiles.ColorCorrectionTile_Factory;
import com.android.systemui.p012qs.tiles.ColorInversionTile;
import com.android.systemui.p012qs.tiles.ColorInversionTile_Factory;
import com.android.systemui.p012qs.tiles.DataSaverTile;
import com.android.systemui.p012qs.tiles.DataSaverTile_Factory;
import com.android.systemui.p012qs.tiles.DeviceControlsTile;
import com.android.systemui.p012qs.tiles.DeviceControlsTile_Factory;
import com.android.systemui.p012qs.tiles.DndTile;
import com.android.systemui.p012qs.tiles.DndTile_Factory;
import com.android.systemui.p012qs.tiles.FlashlightTile;
import com.android.systemui.p012qs.tiles.FlashlightTile_Factory;
import com.android.systemui.p012qs.tiles.HotspotTile;
import com.android.systemui.p012qs.tiles.HotspotTile_Factory;
import com.android.systemui.p012qs.tiles.InternetTile;
import com.android.systemui.p012qs.tiles.InternetTile_Factory;
import com.android.systemui.p012qs.tiles.LocationTile;
import com.android.systemui.p012qs.tiles.LocationTile_Factory;
import com.android.systemui.p012qs.tiles.MicrophoneToggleTile;
import com.android.systemui.p012qs.tiles.MicrophoneToggleTile_Factory;
import com.android.systemui.p012qs.tiles.NfcTile;
import com.android.systemui.p012qs.tiles.NfcTile_Factory;
import com.android.systemui.p012qs.tiles.NightDisplayTile;
import com.android.systemui.p012qs.tiles.NightDisplayTile_Factory;
import com.android.systemui.p012qs.tiles.OneHandedModeTile;
import com.android.systemui.p012qs.tiles.OneHandedModeTile_Factory;
import com.android.systemui.p012qs.tiles.QRCodeScannerTile;
import com.android.systemui.p012qs.tiles.QRCodeScannerTile_Factory;
import com.android.systemui.p012qs.tiles.QuickAccessWalletTile;
import com.android.systemui.p012qs.tiles.QuickAccessWalletTile_Factory;
import com.android.systemui.p012qs.tiles.ReduceBrightColorsTile;
import com.android.systemui.p012qs.tiles.ReduceBrightColorsTile_Factory;
import com.android.systemui.p012qs.tiles.RotationLockTile;
import com.android.systemui.p012qs.tiles.RotationLockTile_Factory;
import com.android.systemui.p012qs.tiles.ScreenRecordTile;
import com.android.systemui.p012qs.tiles.ScreenRecordTile_Factory;
import com.android.systemui.p012qs.tiles.UiModeNightTile;
import com.android.systemui.p012qs.tiles.UiModeNightTile_Factory;
import com.android.systemui.p012qs.tiles.UserDetailView;
import com.android.systemui.p012qs.tiles.UserDetailView_Adapter_Factory;
import com.android.systemui.p012qs.tiles.WifiTile;
import com.android.systemui.p012qs.tiles.WifiTile_Factory;
import com.android.systemui.p012qs.tiles.WorkModeTile;
import com.android.systemui.p012qs.tiles.WorkModeTile_Factory;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController_Factory;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory_Factory;
import com.android.systemui.p012qs.tiles.dialog.WifiStateWorker;
import com.android.systemui.p012qs.tiles.dialog.WifiStateWorker_Factory;
import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.p012qs.user.UserSwitchDialogController_Factory;
import com.android.systemui.p014tv.TvGlobalRootComponent;
import com.android.systemui.p014tv.TvSysUIComponent;
import com.android.systemui.p014tv.TvWMComponent;
import com.android.systemui.people.PeopleProvider;
import com.android.systemui.people.PeopleProvider_MembersInjector;
import com.android.systemui.people.PeopleSpaceActivity;
import com.android.systemui.people.PeopleSpaceActivity_Factory;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.people.widget.LaunchConversationActivity_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver;
import com.android.systemui.people.widget.PeopleSpaceWidgetPinnedReceiver_Factory;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider_Factory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginDependencyProvider;
import com.android.systemui.plugins.PluginDependencyProvider_Factory;
import com.android.systemui.plugins.PluginEnablerImpl;
import com.android.systemui.plugins.PluginEnablerImpl_Factory;
import com.android.systemui.plugins.PluginsModule_ProvidePluginInstanceManagerFactoryFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginDebugFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginExecutorFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginInstanceFactoryFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginManagerFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPluginPrefsFactory;
import com.android.systemui.plugins.PluginsModule_ProvidesPrivilegedPluginsFactory;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimatesImpl;
import com.android.systemui.power.EnhancedEstimatesImpl_Factory;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.power.PowerNotificationWarnings_Factory;
import com.android.systemui.power.PowerUI;
import com.android.systemui.power.PowerUI_Factory;
import com.android.systemui.power.TemperatureController;
import com.android.systemui.power.TemperatureController_Factory;
import com.android.systemui.privacy.AppOpsPrivacyItemMonitor;
import com.android.systemui.privacy.AppOpsPrivacyItemMonitor_Factory;
import com.android.systemui.privacy.MediaProjectionPrivacyItemMonitor;
import com.android.systemui.privacy.MediaProjectionPrivacyItemMonitor_Factory;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyConfig_Factory;
import com.android.systemui.privacy.PrivacyDialogController;
import com.android.systemui.privacy.PrivacyDialogController_Factory;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyItemController_Factory;
import com.android.systemui.privacy.PrivacyItemMonitor;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.privacy.logging.PrivacyLogger_Factory;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip_Factory;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController_Factory;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.systemui.recents.OverviewProxyRecentsImpl_Factory;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService_Factory;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.recents.RecentsModule_ProvideRecentsImplFactory;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.screenrecord.RecordingController_Factory;
import com.android.systemui.screenrecord.RecordingService;
import com.android.systemui.screenrecord.RecordingService_Factory;
import com.android.systemui.screenshot.ActionProxyReceiver;
import com.android.systemui.screenshot.ActionProxyReceiver_Factory;
import com.android.systemui.screenshot.DeleteScreenshotReceiver;
import com.android.systemui.screenshot.DeleteScreenshotReceiver_Factory;
import com.android.systemui.screenshot.ImageExporter_Factory;
import com.android.systemui.screenshot.ImageTileSet_Factory;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.screenshot.LongScreenshotActivity_Factory;
import com.android.systemui.screenshot.LongScreenshotData;
import com.android.systemui.screenshot.LongScreenshotData_Factory;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotController_Factory;
import com.android.systemui.screenshot.ScreenshotNotificationsController;
import com.android.systemui.screenshot.ScreenshotNotificationsController_Factory;
import com.android.systemui.screenshot.ScreenshotSmartActions;
import com.android.systemui.screenshot.ScreenshotSmartActions_Factory;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.screenshot.ScrollCaptureClient_Factory;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.screenshot.ScrollCaptureController_Factory;
import com.android.systemui.screenshot.SmartActionsReceiver;
import com.android.systemui.screenshot.SmartActionsReceiver_Factory;
import com.android.systemui.screenshot.TakeScreenshotService;
import com.android.systemui.screenshot.TakeScreenshotService_Factory;
import com.android.systemui.screenshot.TimeoutHandler;
import com.android.systemui.screenshot.TimeoutHandler_Factory;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity_Factory;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity_Factory;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessController_Factory_Factory;
import com.android.systemui.settings.brightness.BrightnessDialog;
import com.android.systemui.settings.brightness.BrightnessDialog_Factory;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.settings.brightness.BrightnessSliderController_Factory_Factory;
import com.android.systemui.settings.dagger.SettingsModule_ProvideUserTrackerFactory;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginInstance;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginPrefs;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.InputChannelCompat;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager;
import com.android.systemui.shared.system.UncaughtExceptionPreHandlerManager_Factory;
import com.android.systemui.shortcut.ShortcutKeyDispatcher;
import com.android.systemui.shortcut.ShortcutKeyDispatcher_Factory;
import com.android.systemui.statusbar.ActionClickLogger;
import com.android.systemui.statusbar.ActionClickLogger_Factory;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.BlurUtils_Factory;
import com.android.systemui.statusbar.C4835LockscreenShadeKeyguardTransitionController_Factory;
import com.android.systemui.statusbar.C4836SingleShadeLockScreenOverScroller_Factory;
import com.android.systemui.statusbar.C4837SplitShadeLockScreenOverScroller_Factory;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.DisableFlagsLogger_Factory;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController_Factory_Impl;
import com.android.systemui.statusbar.LockscreenShadeScrimTransitionController;
import com.android.systemui.statusbar.LockscreenShadeScrimTransitionController_Factory;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController_Factory;
import com.android.systemui.statusbar.MediaArtworkProcessor;
import com.android.systemui.statusbar.MediaArtworkProcessor_Factory;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationClickNotifier_Factory;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.NotificationInteractionTracker_Factory;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationListener_Factory;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl_Factory;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeDepthController_Factory;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.PulseExpansionHandler_Factory;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder_Factory;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller_Factory_Impl;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller_Factory_Impl;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.StatusBarStateControllerImpl_Factory;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.commandline.CommandRegistry_Factory;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl_WifiPickerTrackerFactory_Factory;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.CallbackHandler_Factory;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl_Factory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory_Factory;
import com.android.systemui.statusbar.dagger.C2631x512569cf;
import com.android.systemui.statusbar.dagger.C2632x721ac0b6;
import com.android.systemui.statusbar.dagger.C2633xbcfb28e4;
import com.android.systemui.statusbar.dagger.C2634xa11cf5e4;
import com.android.systemui.statusbar.dagger.C2635x5356ea10;
import com.android.systemui.statusbar.dagger.C2637xc35bee5b;
import com.android.systemui.statusbar.dagger.CentralSurfacesDependenciesModule_ProvideCommandQueueFactory;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.PrivacyDotViewController_Factory;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController;
import com.android.systemui.statusbar.events.SystemEventChipAnimationController_Factory;
import com.android.systemui.statusbar.events.SystemEventCoordinator;
import com.android.systemui.statusbar.events.SystemEventCoordinator_Factory;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler_Factory;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.statusbar.gesture.TapGestureDetector_Factory;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController_Factory;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.AssistantFeedbackController_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.ConversationNotificationManager_Factory;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor_Factory;
import com.android.systemui.statusbar.notification.DynamicChildBindController;
import com.android.systemui.statusbar.notification.DynamicChildBindController_Factory;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.DynamicPrivacyController_Factory;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.notification.InstantAppNotifier_Factory;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotifPipelineFlags_Factory;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationFilter_Factory;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager_Factory;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection_Factory;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline_Factory;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.VisualStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger_Factory;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionLogger_Factory;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider_Factory;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.provider.NotificationVisibilityProviderImpl;
import com.android.systemui.statusbar.notification.collection.provider.NotificationVisibilityProviderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController;
import com.android.systemui.statusbar.notification.collection.render.MediaContainerController_Factory;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager_Factory;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl_Factory;
import com.android.systemui.statusbar.notification.dagger.C2715x41b9fd82;
import com.android.systemui.statusbar.notification.dagger.C2717x3fd4641;
import com.android.systemui.statusbar.notification.dagger.C2718x340f4262;
import com.android.systemui.statusbar.notification.dagger.C2720xb614d321;
import com.android.systemui.statusbar.notification.dagger.C2721x812edf99;
import com.android.systemui.statusbar.notification.dagger.C2723x39c1fe98;
import com.android.systemui.statusbar.notification.dagger.C2724xcc90df13;
import com.android.systemui.statusbar.notification.dagger.C2726x34a20792;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideCommonNotifCollectionFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupExpansionManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupMembershipManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationEntryManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationGutsManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationPanelLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideOnUserInteractionCallbackFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideVisualStabilityManagerFactory;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.notification.icon.IconBuilder;
import com.android.systemui.statusbar.notification.icon.IconBuilder_Factory;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.icon.IconManager_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController;
import com.android.systemui.statusbar.notification.interruption.HeadsUpController_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinderLogger;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinderLogger_Factory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinder_Factory;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProviderImpl_Factory;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptLogger_Factory;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProviderImpl_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.logging.NotificationLogger_ExpansionStateLogger_Factory;
import com.android.systemui.statusbar.notification.logging.NotificationPanelLogger;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary;
import com.android.systemui.statusbar.notification.people.NotificationPersonExtractorPluginBoundary_Factory;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifierImpl_Factory;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationViewController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialogController_Factory;
import com.android.systemui.statusbar.notification.row.ChannelEditorDialog_Builder_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController;
import com.android.systemui.statusbar.notification.row.ExpandableOutlineViewController_Factory;
import com.android.systemui.statusbar.notification.row.ExpandableViewController;
import com.android.systemui.statusbar.notification.row.ExpandableViewController_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger_Factory;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline_Factory;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager;
import com.android.systemui.statusbar.notification.row.NotifInflationErrorManager_Factory;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCache;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl;
import com.android.systemui.statusbar.notification.row.NotifRemoteViewCacheImpl_Factory;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater;
import com.android.systemui.statusbar.notification.row.NotificationContentInflater_Factory;
import com.android.systemui.statusbar.notification.row.NotificationGutsManager;
import com.android.systemui.statusbar.notification.row.OnUserInteractionCallback;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger;
import com.android.systemui.statusbar.notification.row.RowContentBindStageLogger_Factory;
import com.android.systemui.statusbar.notification.row.RowContentBindStage_Factory;
import com.android.systemui.statusbar.notification.row.RowInflaterTask_Factory;
import com.android.systemui.statusbar.notification.row.dagger.C2777x3e2d0aca;
import com.android.systemui.statusbar.notification.row.dagger.C2778xdc9a80a2;
import com.android.systemui.statusbar.notification.row.dagger.C2779xc255c3ca;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AmbientState_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager_Factory;
import com.android.systemui.statusbar.p013tv.TvStatusBar;
import com.android.systemui.statusbar.p013tv.TvStatusBar_Factory;
import com.android.systemui.statusbar.p013tv.VpnStatusObserver;
import com.android.systemui.statusbar.p013tv.VpnStatusObserver_Factory;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanel;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanelActivity_Factory;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanel_Factory;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.AutoHideController_Factory_Factory;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.BiometricUnlockController_Factory;
import com.android.systemui.statusbar.phone.C4838AutoHideController_Factory;
import com.android.systemui.statusbar.phone.C4839LightBarController_Factory;
import com.android.systemui.statusbar.phone.C4840LightBarTransitionsController_Factory;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl_Factory;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl;
import com.android.systemui.statusbar.phone.DarkIconDispatcherImpl_Factory;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.DozeParameters_Factory;
import com.android.systemui.statusbar.phone.DozeScrimController;
import com.android.systemui.statusbar.phone.DozeScrimController_Factory;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.DozeServiceHost_Factory;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBouncer_Factory_Factory;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardBypassController_Factory;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil_Factory;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl_Factory;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger_Factory;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarController_Factory_Factory;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController_Factory_Impl;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger_Factory;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl;
import com.android.systemui.statusbar.phone.ManagedProfileControllerImpl_Factory;
import com.android.systemui.statusbar.phone.MultiUserSwitchController;
import com.android.systemui.statusbar.phone.MultiUserSwitchController_Factory_Factory;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper;
import com.android.systemui.statusbar.phone.NotificationGroupAlertTransferHelper_Factory;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController_Factory;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_PanelEventsEmitter_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl_Factory;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.statusbar.phone.NotificationTapHelper_Factory_Factory;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimController_Factory;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.ShadeControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController_Factory;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback_Factory;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager_Factory;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager_Factory;
import com.android.systemui.statusbar.policy.AccessibilityController;
import com.android.systemui.statusbar.policy.AccessibilityController_Factory;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper_Factory;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import com.android.systemui.statusbar.policy.BatteryStateNotifier_Factory;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl;
import com.android.systemui.statusbar.policy.BluetoothControllerImpl_Factory;
import com.android.systemui.statusbar.policy.CastControllerImpl;
import com.android.systemui.statusbar.policy.CastControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl;
import com.android.systemui.statusbar.policy.DeviceControlsControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl;
import com.android.systemui.statusbar.policy.DevicePostureControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl_Factory;
import com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController;
import com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController_Factory;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl;
import com.android.systemui.statusbar.policy.ExtensionControllerImpl_Factory;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl;
import com.android.systemui.statusbar.policy.FlashlightControllerImpl_Factory;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger_Factory;
import com.android.systemui.statusbar.policy.HotspotControllerImpl;
import com.android.systemui.statusbar.policy.HotspotControllerImpl_Factory;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl_Factory;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.systemui.statusbar.policy.LocationControllerImpl_Factory;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl;
import com.android.systemui.statusbar.policy.NextAlarmControllerImpl_Factory;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler_Factory;
import com.android.systemui.statusbar.policy.RemoteInputUriController;
import com.android.systemui.statusbar.policy.RemoteInputUriController_Factory;
import com.android.systemui.statusbar.policy.RemoteInputView;
import com.android.systemui.statusbar.policy.RemoteInputViewController;
import com.android.systemui.statusbar.policy.RemoteInputViewControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl;
import com.android.systemui.statusbar.policy.RotationLockControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SafetyController;
import com.android.systemui.statusbar.policy.SafetyController_Factory;
import com.android.systemui.statusbar.policy.SecurityControllerImpl;
import com.android.systemui.statusbar.policy.SecurityControllerImpl_Factory;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl;
import com.android.systemui.statusbar.policy.SmartActionInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyConstants;
import com.android.systemui.statusbar.policy.SmartReplyConstants_Factory;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl;
import com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl_Factory;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl_Factory;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.statusbar.policy.UserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import com.android.systemui.statusbar.policy.VariableDateViewController_Factory_Factory;
import com.android.systemui.statusbar.policy.WalletControllerImpl;
import com.android.systemui.statusbar.policy.WalletControllerImpl_Factory;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl_Factory;
import com.android.systemui.statusbar.policy.dagger.C3215xe335f1e6;
import com.android.systemui.statusbar.policy.dagger.RemoteInputViewSubcomponent;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAccessPointControllerImplFactory;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory;
import com.android.systemui.statusbar.policy.dagger.StatusBarPolicyModule_ProvideDataSaverControllerFactory;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.statusbar.window.StatusBarWindowController_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowModule_ProvidesStatusBarWindowViewFactory;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.statusbar.window.StatusBarWindowStateController_Factory;
import com.android.systemui.statusbar.window.StatusBarWindowView;
import com.android.systemui.telephony.TelephonyCallback_Factory;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.telephony.TelephonyListenerManager_Factory;
import com.android.systemui.theme.ThemeModule_ProvideLauncherPackageFactory;
import com.android.systemui.theme.ThemeModule_ProvideThemePickerPackageFactory;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayApplier_Factory;
import com.android.systemui.theme.ThemeOverlayController;
import com.android.systemui.theme.ThemeOverlayController_Factory;
import com.android.systemui.toast.ToastFactory;
import com.android.systemui.toast.ToastFactory_Factory;
import com.android.systemui.toast.ToastLogger;
import com.android.systemui.toast.ToastLogger_Factory;
import com.android.systemui.toast.ToastUI;
import com.android.systemui.toast.ToastUI_Factory;
import com.android.systemui.touch.TouchInsetManager;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.ProtoTracer_Factory;
import com.android.systemui.tuner.TunablePadding;
import com.android.systemui.tuner.TunablePadding_TunablePaddingService_Factory;
import com.android.systemui.tuner.TunerActivity;
import com.android.systemui.tuner.TunerActivity_Factory;
import com.android.systemui.tuner.TunerServiceImpl;
import com.android.systemui.tuner.TunerServiceImpl_Factory;
import com.android.systemui.unfold.C3249xd9485442;
import com.android.systemui.unfold.C3250x6e72e9f0;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.FoldAodAnimationController_Factory;
import com.android.systemui.unfold.FoldStateLogger;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.SysUIUnfoldModule;
import com.android.systemui.unfold.SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory;
import com.android.systemui.unfold.UnfoldLatencyTracker;
import com.android.systemui.unfold.UnfoldLatencyTracker_Factory;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation_Factory;
import com.android.systemui.unfold.UnfoldSharedModule;
import com.android.systemui.unfold.UnfoldSharedModule_HingeAngleProviderFactory;
import com.android.systemui.unfold.UnfoldSharedModule_ProvideFoldStateProviderFactory;
import com.android.systemui.unfold.UnfoldSharedModule_UnfoldTransitionProgressProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvideShellProgressProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvidesFoldStateLoggerFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_ScreenStatusProviderFactory;
import com.android.systemui.unfold.UnfoldTransitionModule_TracingTagPrefixFactory;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.UnfoldTransitionWallpaperController;
import com.android.systemui.unfold.UnfoldTransitionWallpaperController_Factory;
import com.android.systemui.unfold.config.UnfoldTransitionConfig;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import com.android.systemui.unfold.updates.DeviceFoldStateProvider_Factory;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener;
import com.android.systemui.unfold.util.ATraceLoggerTransitionProgressListener_Factory;
import com.android.systemui.unfold.util.C4842ScaleAwareTransitionProgressProvider_Factory;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider;
import com.android.systemui.unfold.util.ScaleAwareTransitionProgressProvider_Factory_Impl;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.usb.StorageNotification;
import com.android.systemui.usb.StorageNotification_Factory;
import com.android.systemui.usb.UsbAudioWarningDialogMessage_Factory;
import com.android.systemui.usb.UsbConfirmActivity;
import com.android.systemui.usb.UsbConfirmActivity_Factory;
import com.android.systemui.usb.UsbDebuggingActivity;
import com.android.systemui.usb.UsbDebuggingActivity_Factory;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity_Factory;
import com.android.systemui.usb.UsbPermissionActivity;
import com.android.systemui.usb.UsbPermissionActivity_Factory;
import com.android.systemui.user.CreateUserActivity;
import com.android.systemui.user.CreateUserActivity_Factory;
import com.android.systemui.user.UserCreator;
import com.android.systemui.user.UserCreator_Factory;
import com.android.systemui.user.UserModule_ProvideEditUserInfoControllerFactory;
import com.android.systemui.user.UserSwitcherActivity;
import com.android.systemui.user.UserSwitcherActivity_Factory;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.CarrierConfigTracker_Factory;
import com.android.systemui.util.CarrierNameCustomization;
import com.android.systemui.util.CarrierNameCustomization_Factory;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.DeviceConfigProxy_Factory;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.util.NotificationChannels_Factory;
import com.android.systemui.util.RingerModeTrackerImpl;
import com.android.systemui.util.RingerModeTrackerImpl_Factory;
import com.android.systemui.util.WallpaperController;
import com.android.systemui.util.WallpaperController_Factory;
import com.android.systemui.util.concurrency.C3259xb8fd9db4;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.concurrency.ExecutionImpl_Factory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideHandlerFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainExecutorFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainHandlerFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideMainLooperFactory;
import com.android.systemui.util.concurrency.GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBackgroundExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgHandlerFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideBgLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideDelayableExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideLongRunningExecutorFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideLongRunningLooperFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvideTimeTickHandlerFactory;
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.GarbageMonitor_Factory;
import com.android.systemui.util.leak.GarbageMonitor_MemoryTile_Factory;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakModule;
import com.android.systemui.util.leak.LeakModule_ProvidesLeakDetectorFactory;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.leak.LeakReporter_Factory;
import com.android.systemui.util.leak.TrackedCollections_Factory;
import com.android.systemui.util.p016io.Files;
import com.android.systemui.util.p016io.Files_Factory;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.AsyncSensorManager_Factory;
import com.android.systemui.util.sensors.C3266xe478f0bc;
import com.android.systemui.util.sensors.PostureDependentProximitySensor_Factory;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ProximitySensorImpl_Factory;
import com.android.systemui.util.sensors.SensorModule_ProvidePostureToProximitySensorMappingFactory;
import com.android.systemui.util.sensors.SensorModule_ProvidePrimaryProximitySensorFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideProximityCheckFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideProximitySensorFactory;
import com.android.systemui.util.sensors.SensorModule_ProvideSecondaryProximitySensorFactory;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorImpl;
import com.android.systemui.util.sensors.ThresholdSensorImpl_BuilderFactory_Factory;
import com.android.systemui.util.sensors.ThresholdSensorImpl_Builder_Factory;
import com.android.systemui.util.settings.GlobalSettingsImpl_Factory;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.settings.SecureSettingsImpl_Factory;
import com.android.systemui.util.time.DateFormatUtil;
import com.android.systemui.util.time.DateFormatUtil_Factory;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.util.time.SystemClockImpl_Factory;
import com.android.systemui.util.view.ViewUtil;
import com.android.systemui.util.view.ViewUtil_Factory;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.DelayedWakeLock_Builder_Factory;
import com.android.systemui.util.wakelock.WakeLock;
import com.android.systemui.util.wakelock.WakeLock_Builder_Factory;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import com.android.systemui.util.wrapper.RotationPolicyWrapperImpl;
import com.android.systemui.util.wrapper.RotationPolicyWrapperImpl_Factory;
import com.android.systemui.volume.VolumeDialogComponent;
import com.android.systemui.volume.VolumeDialogComponent_Factory;
import com.android.systemui.volume.VolumeDialogControllerImpl;
import com.android.systemui.volume.VolumeDialogControllerImpl_Factory;
import com.android.systemui.volume.VolumeUI;
import com.android.systemui.volume.VolumeUI_Factory;
import com.android.systemui.volume.dagger.VolumeModule_ProvideVolumeDialogFactory;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wallet.controller.QuickAccessWalletController_Factory;
import com.android.systemui.wallet.dagger.WalletModule_ProvideQuickAccessWalletClientFactory;
import com.android.systemui.wallet.p017ui.WalletActivity;
import com.android.systemui.wallet.p017ui.WalletActivity_Factory;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.WMShell;
import com.android.systemui.wmshell.WMShell_Factory;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.gamemode.NTGameModeHelper_Factory;
import com.nothing.keyguard.LockIconViewControllerEx_Factory;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.NTDependencyEx_Factory;
import com.nothing.systemui.assist.AssistManagerEx;
import com.nothing.systemui.assist.AssistManagerEx_Factory;
import com.nothing.systemui.biometrics.AuthRippleControllerEx;
import com.nothing.systemui.biometrics.AuthRippleControllerEx_Factory;
import com.nothing.systemui.biometrics.NTColorController;
import com.nothing.systemui.biometrics.NTColorController_Factory;
import com.nothing.systemui.biometrics.NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory;
import com.nothing.systemui.doze.AODController;
import com.nothing.systemui.doze.AODController_Factory;
import com.nothing.systemui.doze.LiftWakeGestureController;
import com.nothing.systemui.doze.LiftWakeGestureController_Factory;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.facerecognition.FaceRecognitionController_Factory;
import com.nothing.systemui.keyguard.KeyguardUpdateMonitorEx;
import com.nothing.systemui.keyguard.KeyguardUpdateMonitorEx_Factory;
import com.nothing.systemui.keyguard.KeyguardViewMediatorEx;
import com.nothing.systemui.keyguard.KeyguardViewMediatorEx_Factory;
import com.nothing.systemui.keyguard.calendar.CalendarManager;
import com.nothing.systemui.keyguard.calendar.CalendarManager_Factory;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherControllerImpl;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherControllerImpl_Factory;
import com.nothing.systemui.navigationbar.NavigationBarControllerEx_Factory;
import com.nothing.systemui.navigationbar.NavigationBarEx_Factory;
import com.nothing.systemui.navigationbar.NavigationBarInflaterViewEx_Factory;
import com.nothing.systemui.navigationbar.NavigationBarViewEx_Factory;
import com.nothing.systemui.navigationbar.NavigationModeControllerEx_Factory;
import com.nothing.systemui.navigationbar.buttons.KeyButtonViewEx_Factory;
import com.nothing.systemui.navigationbar.gestural.EdgeBackGestureHandlerEx_Factory;
import com.nothing.systemui.p024qs.NTQSAnimator;
import com.nothing.systemui.p024qs.NTQSAnimator_Factory;
import com.nothing.systemui.p024qs.NTQSStatusBar;
import com.nothing.systemui.p024qs.NTQSStatusBarController;
import com.nothing.systemui.p024qs.NTQSStatusBarController_Factory;
import com.nothing.systemui.p024qs.QSFragmentEx_Factory;
import com.nothing.systemui.p024qs.QSTileHostEx_Factory;
import com.nothing.systemui.p024qs.QuickStatusBarHeaderEx_Factory;
import com.nothing.systemui.p024qs.TileLayoutEx_Factory;
import com.nothing.systemui.p024qs.tileimpl.QSIconViewImplEx_Factory;
import com.nothing.systemui.p024qs.tileimpl.QSTileImplEx_Factory;
import com.nothing.systemui.p024qs.tileimpl.QSTileViewImplEx_Factory;
import com.nothing.systemui.p024qs.tiles.BatteryShareTile;
import com.nothing.systemui.p024qs.tiles.BatteryShareTile_Factory;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx_Factory;
import com.nothing.systemui.p024qs.tiles.GlyphsTile;
import com.nothing.systemui.p024qs.tiles.GlyphsTile_Factory;
import com.nothing.systemui.p024qs.tiles.InternetTileEx_Factory;
import com.nothing.systemui.power.NTCriticalTemperatureWarning;
import com.nothing.systemui.power.NTCriticalTemperatureWarning_Factory;
import com.nothing.systemui.privacy.OngoingPrivacyChipEx_Factory;
import com.nothing.systemui.privacy.PrivacyDialogControllerEx_Factory;
import com.nothing.systemui.privacy.PrivacyDialogEx_Factory;
import com.nothing.systemui.settings.brightness.BrightnessControllerEx_Factory;
import com.nothing.systemui.statusbar.CommandQueueEx;
import com.nothing.systemui.statusbar.CommandQueueEx_Factory;
import com.nothing.systemui.statusbar.KeyguardIndicationControllerEx;
import com.nothing.systemui.statusbar.KeyguardIndicationControllerEx_Factory;
import com.nothing.systemui.statusbar.connectivity.MobileSignalControllerEx_Factory;
import com.nothing.systemui.statusbar.connectivity.WifiSignalControllerEx_Factory;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager;
import com.nothing.systemui.statusbar.notification.NTLightweightHeadsupManager_Factory;
import com.nothing.systemui.statusbar.notification.interruption.HeadsUpControllerEx;
import com.nothing.systemui.statusbar.notification.interruption.HeadsUpControllerEx_Factory;
import com.nothing.systemui.statusbar.notification.stack.AmbientStateEx;
import com.nothing.systemui.statusbar.notification.stack.AmbientStateEx_Factory;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx_Factory;
import com.nothing.systemui.statusbar.phone.DozeServiceHostEx_Factory;
import com.nothing.systemui.statusbar.policy.BatteryControllerImplEx_Factory;
import com.nothing.systemui.statusbar.policy.BatteryShareControllerImpl;
import com.nothing.systemui.statusbar.policy.BatteryShareControllerImpl_Factory;
import com.nothing.systemui.statusbar.policy.GlyphsControllerImpl;
import com.nothing.systemui.statusbar.policy.GlyphsControllerImpl_Factory;
import com.nothing.systemui.statusbar.policy.NfcControllerImpl;
import com.nothing.systemui.statusbar.policy.NfcControllerImpl_Factory;
import com.nothing.systemui.statusbar.policy.TeslaInfoControllerImpl;
import com.nothing.systemui.statusbar.policy.TeslaInfoControllerImpl_Factory;
import com.nothing.systemui.volume.VolumeDialogImplEx_Factory;
import dagger.internal.DelegateFactory;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import dagger.internal.InstanceFactory;
import dagger.internal.MapBuilder;
import dagger.internal.MapProviderFactory;
import dagger.internal.Preconditions;
import dagger.internal.SetBuilder;
import dagger.internal.SetFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Provider;

/* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent */
public final class DaggerTvGlobalRootComponent {
    private static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());

    private DaggerTvGlobalRootComponent() {
    }

    public static TvGlobalRootComponent.Builder builder() {
        return new Builder();
    }

    /* access modifiers changed from: private */
    public static <T> Provider<Optional<T>> absentJdkOptionalProvider() {
        return ABSENT_JDK_OPTIONAL_PROVIDER;
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$PresentJdkOptionalInstanceProvider */
    private static final class PresentJdkOptionalInstanceProvider<T> implements Provider<Optional<T>> {
        private final Provider<T> delegate;

        private PresentJdkOptionalInstanceProvider(Provider<T> provider) {
            this.delegate = (Provider) Preconditions.checkNotNull(provider);
        }

        public Optional<T> get() {
            return Optional.m1751of(this.delegate.get());
        }

        /* access modifiers changed from: private */
        /* renamed from: of */
        public static <T> Provider<Optional<T>> m1189of(Provider<T> provider) {
            return new PresentJdkOptionalInstanceProvider(provider);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$Builder */
    private static final class Builder implements TvGlobalRootComponent.Builder {
        private Context context;

        private Builder() {
        }

        public Builder context(Context context2) {
            this.context = (Context) Preconditions.checkNotNull(context2);
            return this;
        }

        public TvGlobalRootComponent build() {
            Preconditions.checkBuilderRequirement(this.context, Context.class);
            return new TvGlobalRootComponentImpl(new GlobalModule(), new AndroidInternalsModule(), new FrameworkServicesModule(), new UnfoldTransitionModule(), new UnfoldSharedModule(), this.context);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$TvWMComponentBuilder */
    private static final class TvWMComponentBuilder implements TvWMComponent.Builder {
        private HandlerThread setShellMainThread;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;

        private TvWMComponentBuilder(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
        }

        public TvWMComponentBuilder setShellMainThread(HandlerThread handlerThread) {
            this.setShellMainThread = handlerThread;
            return this;
        }

        public TvWMComponent build() {
            return new TvWMComponentImpl(this.tvGlobalRootComponentImpl, this.setShellMainThread);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder */
    private static final class TvSysUIComponentBuilder implements TvSysUIComponent.Builder {
        private Optional<AppPairs> setAppPairs;
        private Optional<BackAnimation> setBackAnimation;
        private Optional<Bubbles> setBubbles;
        private Optional<CompatUI> setCompatUI;
        private Optional<DisplayAreaHelper> setDisplayAreaHelper;
        private Optional<DragAndDrop> setDragAndDrop;
        private Optional<HideDisplayCutout> setHideDisplayCutout;
        private Optional<LegacySplitScreen> setLegacySplitScreen;
        private Optional<OneHanded> setOneHanded;
        private Optional<Pip> setPip;
        private Optional<RecentTasks> setRecentTasks;
        private Optional<ShellCommandHandler> setShellCommandHandler;
        private Optional<SplitScreen> setSplitScreen;
        private Optional<StartingSurface> setStartingSurface;
        private Optional<TaskSurfaceHelper> setTaskSurfaceHelper;
        private Optional<TaskViewFactory> setTaskViewFactory;
        private ShellTransitions setTransitions;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;

        private TvSysUIComponentBuilder(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.pip.Pip>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setPip(java.util.Optional<com.android.p019wm.shell.pip.Pip> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setPip = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setPip(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setLegacySplitScreen(java.util.Optional<com.android.p019wm.shell.legacysplitscreen.LegacySplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setLegacySplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setLegacySplitScreen(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setSplitScreen(java.util.Optional<com.android.p019wm.shell.splitscreen.SplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setSplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setSplitScreen(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.apppairs.AppPairs>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setAppPairs(java.util.Optional<com.android.p019wm.shell.apppairs.AppPairs> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setAppPairs = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setAppPairs(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.onehanded.OneHanded>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setOneHanded(java.util.Optional<com.android.p019wm.shell.onehanded.OneHanded> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setOneHanded = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setOneHanded(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.bubbles.Bubbles>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setBubbles(java.util.Optional<com.android.p019wm.shell.bubbles.Bubbles> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBubbles = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setBubbles(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.TaskViewFactory>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setTaskViewFactory(java.util.Optional<com.android.p019wm.shell.TaskViewFactory> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskViewFactory = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setTaskViewFactory(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setHideDisplayCutout(java.util.Optional<com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setHideDisplayCutout = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setHideDisplayCutout(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.ShellCommandHandler>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setShellCommandHandler(java.util.Optional<com.android.p019wm.shell.ShellCommandHandler> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setShellCommandHandler = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setShellCommandHandler(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        public TvSysUIComponentBuilder setTransitions(ShellTransitions shellTransitions) {
            this.setTransitions = (ShellTransitions) Preconditions.checkNotNull(shellTransitions);
            return this;
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setStartingSurface(java.util.Optional<com.android.p019wm.shell.startingsurface.StartingSurface> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setStartingSurface = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setStartingSurface(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setDisplayAreaHelper(java.util.Optional<com.android.p019wm.shell.displayareahelper.DisplayAreaHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDisplayAreaHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setDisplayAreaHelper(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setTaskSurfaceHelper(java.util.Optional<com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskSurfaceHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setTaskSurfaceHelper(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.recents.RecentTasks>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setRecentTasks(java.util.Optional<com.android.p019wm.shell.recents.RecentTasks> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setRecentTasks = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setRecentTasks(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.compatui.CompatUI>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setCompatUI(java.util.Optional<com.android.p019wm.shell.compatui.CompatUI> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setCompatUI = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setCompatUI(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setDragAndDrop(java.util.Optional<com.android.p019wm.shell.draganddrop.DragAndDrop> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDragAndDrop = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setDragAndDrop(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.back.BackAnimation>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder setBackAnimation(java.util.Optional<com.android.p019wm.shell.back.BackAnimation> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBackAnimation = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p014tv.DaggerTvGlobalRootComponent.TvSysUIComponentBuilder.setBackAnimation(java.util.Optional):com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentBuilder");
        }

        public TvSysUIComponent build() {
            Preconditions.checkBuilderRequirement(this.setPip, Optional.class);
            Preconditions.checkBuilderRequirement(this.setLegacySplitScreen, Optional.class);
            Preconditions.checkBuilderRequirement(this.setSplitScreen, Optional.class);
            Preconditions.checkBuilderRequirement(this.setAppPairs, Optional.class);
            Preconditions.checkBuilderRequirement(this.setOneHanded, Optional.class);
            Preconditions.checkBuilderRequirement(this.setBubbles, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTaskViewFactory, Optional.class);
            Preconditions.checkBuilderRequirement(this.setHideDisplayCutout, Optional.class);
            Preconditions.checkBuilderRequirement(this.setShellCommandHandler, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTransitions, ShellTransitions.class);
            Preconditions.checkBuilderRequirement(this.setStartingSurface, Optional.class);
            Preconditions.checkBuilderRequirement(this.setDisplayAreaHelper, Optional.class);
            Preconditions.checkBuilderRequirement(this.setTaskSurfaceHelper, Optional.class);
            Preconditions.checkBuilderRequirement(this.setRecentTasks, Optional.class);
            Preconditions.checkBuilderRequirement(this.setCompatUI, Optional.class);
            Preconditions.checkBuilderRequirement(this.setDragAndDrop, Optional.class);
            Preconditions.checkBuilderRequirement(this.setBackAnimation, Optional.class);
            TvGlobalRootComponentImpl tvGlobalRootComponentImpl2 = this.tvGlobalRootComponentImpl;
            LeakModule leakModule = r2;
            LeakModule leakModule2 = new LeakModule();
            NightDisplayListenerModule nightDisplayListenerModule = r2;
            NightDisplayListenerModule nightDisplayListenerModule2 = new NightDisplayListenerModule();
            SharedLibraryModule sharedLibraryModule = r2;
            SharedLibraryModule sharedLibraryModule2 = new SharedLibraryModule();
            KeyguardModule keyguardModule = r2;
            KeyguardModule keyguardModule2 = new KeyguardModule();
            SysUIUnfoldModule sysUIUnfoldModule = r2;
            SysUIUnfoldModule sysUIUnfoldModule2 = new SysUIUnfoldModule();
            return new TvSysUIComponentImpl(tvGlobalRootComponentImpl2, leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, this.setPip, this.setLegacySplitScreen, this.setSplitScreen, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setDisplayAreaHelper, this.setTaskSurfaceHelper, this.setRecentTasks, this.setCompatUI, this.setDragAndDrop, this.setBackAnimation);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$SysUIUnfoldComponentFactory */
    private static final class SysUIUnfoldComponentFactory implements SysUIUnfoldComponent.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private SysUIUnfoldComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public SysUIUnfoldComponent create(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
            Preconditions.checkNotNull(unfoldTransitionProgressProvider);
            Preconditions.checkNotNull(naturalRotationUnfoldProgressProvider);
            Preconditions.checkNotNull(scopedUnfoldTransitionProgressProvider);
            return new SysUIUnfoldComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$ExpandableNotificationRowComponentBuilder */
    private static final class ExpandableNotificationRowComponentBuilder implements ExpandableNotificationRowComponent.Builder {
        private ExpandableNotificationRow expandableNotificationRow;
        private NotificationListContainer listContainer;
        private NotificationEntry notificationEntry;
        private ExpandableNotificationRow.OnExpandClickListener onExpandClickListener;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private ExpandableNotificationRowComponentBuilder(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public ExpandableNotificationRowComponentBuilder expandableNotificationRow(ExpandableNotificationRow expandableNotificationRow2) {
            this.expandableNotificationRow = (ExpandableNotificationRow) Preconditions.checkNotNull(expandableNotificationRow2);
            return this;
        }

        public ExpandableNotificationRowComponentBuilder notificationEntry(NotificationEntry notificationEntry2) {
            this.notificationEntry = (NotificationEntry) Preconditions.checkNotNull(notificationEntry2);
            return this;
        }

        public ExpandableNotificationRowComponentBuilder onExpandClickListener(ExpandableNotificationRow.OnExpandClickListener onExpandClickListener2) {
            this.onExpandClickListener = (ExpandableNotificationRow.OnExpandClickListener) Preconditions.checkNotNull(onExpandClickListener2);
            return this;
        }

        public ExpandableNotificationRowComponentBuilder listContainer(NotificationListContainer notificationListContainer) {
            this.listContainer = (NotificationListContainer) Preconditions.checkNotNull(notificationListContainer);
            return this;
        }

        public ExpandableNotificationRowComponent build() {
            Preconditions.checkBuilderRequirement(this.expandableNotificationRow, ExpandableNotificationRow.class);
            Preconditions.checkBuilderRequirement(this.notificationEntry, NotificationEntry.class);
            Preconditions.checkBuilderRequirement(this.onExpandClickListener, ExpandableNotificationRow.OnExpandClickListener.class);
            Preconditions.checkBuilderRequirement(this.listContainer, NotificationListContainer.class);
            return new ExpandableNotificationRowComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, this.expandableNotificationRow, this.notificationEntry, this.onExpandClickListener, this.listContainer);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$RemoteInputViewSubcomponentFactory */
    private static final class RemoteInputViewSubcomponentFactory implements RemoteInputViewSubcomponent.Factory {
        private final ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private RemoteInputViewSubcomponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.expandableNotificationRowComponentImpl = expandableNotificationRowComponentImpl2;
        }

        public RemoteInputViewSubcomponent create(RemoteInputView remoteInputView, RemoteInputController remoteInputController) {
            Preconditions.checkNotNull(remoteInputView);
            Preconditions.checkNotNull(remoteInputController);
            return new RemoteInputViewSubcomponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, this.expandableNotificationRowComponentImpl, remoteInputView, remoteInputController);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$NavigationBarComponentFactory */
    private static final class NavigationBarComponentFactory implements NavigationBarComponent.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private NavigationBarComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public NavigationBarComponent create(Context context, Bundle bundle) {
            Preconditions.checkNotNull(context);
            return new NavigationBarComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, context, bundle);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$KeyguardStatusViewComponentFactory */
    private static final class KeyguardStatusViewComponentFactory implements KeyguardStatusViewComponent.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private KeyguardStatusViewComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public KeyguardStatusViewComponent build(KeyguardStatusView keyguardStatusView) {
            Preconditions.checkNotNull(keyguardStatusView);
            return new KeyguardStatusViewComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, keyguardStatusView);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$KeyguardBouncerComponentFactory */
    private static final class KeyguardBouncerComponentFactory implements KeyguardBouncerComponent.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private KeyguardBouncerComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public KeyguardBouncerComponent create(ViewGroup viewGroup) {
            Preconditions.checkNotNull(viewGroup);
            return new KeyguardBouncerComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, viewGroup);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$SectionHeaderControllerSubcomponentBuilder */
    private static final class SectionHeaderControllerSubcomponentBuilder implements SectionHeaderControllerSubcomponent.Builder {
        private String clickIntentAction;
        private Integer headerText;
        private String nodeLabel;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private SectionHeaderControllerSubcomponentBuilder(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public SectionHeaderControllerSubcomponentBuilder nodeLabel(String str) {
            this.nodeLabel = (String) Preconditions.checkNotNull(str);
            return this;
        }

        public SectionHeaderControllerSubcomponentBuilder headerText(int i) {
            this.headerText = (Integer) Preconditions.checkNotNull(Integer.valueOf(i));
            return this;
        }

        public SectionHeaderControllerSubcomponentBuilder clickIntentAction(String str) {
            this.clickIntentAction = (String) Preconditions.checkNotNull(str);
            return this;
        }

        public SectionHeaderControllerSubcomponent build() {
            Preconditions.checkBuilderRequirement(this.nodeLabel, String.class);
            Preconditions.checkBuilderRequirement(this.headerText, Integer.class);
            Preconditions.checkBuilderRequirement(this.clickIntentAction, String.class);
            return new SectionHeaderControllerSubcomponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, this.nodeLabel, this.headerText, this.clickIntentAction);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$DozeComponentFactory */
    private static final class DozeComponentFactory implements DozeComponent.Builder {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private DozeComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public DozeComponent build(DozeMachine.Service service) {
            Preconditions.checkNotNull(service);
            return new DozeComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, service);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$DreamOverlayComponentFactory */
    private static final class DreamOverlayComponentFactory implements DreamOverlayComponent.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private DreamOverlayComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public DreamOverlayComponent create(ViewModelStore viewModelStore, Complication.Host host) {
            Preconditions.checkNotNull(viewModelStore);
            Preconditions.checkNotNull(host);
            return new DreamOverlayComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, viewModelStore, host);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$ComplicationViewModelComponentFactory */
    private static final class ComplicationViewModelComponentFactory implements ComplicationViewModelComponent.Factory {
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private ComplicationViewModelComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
        }

        public ComplicationViewModelComponent create(Complication complication, ComplicationId complicationId) {
            Preconditions.checkNotNull(complication);
            Preconditions.checkNotNull(complicationId);
            return new ComplicationViewModelComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, this.dreamOverlayComponentImpl, complication, complicationId);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$InputSessionComponentFactory */
    private static final class InputSessionComponentFactory implements InputSessionComponent.Factory {
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private InputSessionComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
        }

        public InputSessionComponent create(String str, InputChannelCompat.InputEventListener inputEventListener, GestureDetector.OnGestureListener onGestureListener, boolean z) {
            Preconditions.checkNotNull(str);
            Preconditions.checkNotNull(inputEventListener);
            Preconditions.checkNotNull(onGestureListener);
            Preconditions.checkNotNull(Boolean.valueOf(z));
            return new InputSessionComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, this.dreamOverlayComponentImpl, str, inputEventListener, onGestureListener, Boolean.valueOf(z));
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$FragmentCreatorFactory */
    private static final class FragmentCreatorFactory implements FragmentService.FragmentCreator.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private FragmentCreatorFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public FragmentService.FragmentCreator build() {
            return new FragmentCreatorImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$QSFragmentComponentFactory */
    private static final class QSFragmentComponentFactory implements QSFragmentComponent.Factory {
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private QSFragmentComponentFactory(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        public QSFragmentComponent create(QSFragment qSFragment) {
            Preconditions.checkNotNull(qSFragment);
            return new QSFragmentComponentImpl(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, qSFragment);
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$TvWMComponentImpl */
    private static final class TvWMComponentImpl implements TvWMComponent {
        private Provider<Optional<DisplayImeController>> dynamicOverrideOptionalOfDisplayImeControllerProvider;
        private Provider<Optional<FreeformTaskListener>> dynamicOverrideOptionalOfFreeformTaskListenerProvider;
        private Provider<Optional<FullscreenTaskListener>> dynamicOverrideOptionalOfFullscreenTaskListenerProvider;
        private Provider<Optional<FullscreenUnfoldController>> dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider;
        private Provider<Optional<OneHandedController>> dynamicOverrideOptionalOfOneHandedControllerProvider;
        private Provider<Optional<SplitScreenController>> dynamicOverrideOptionalOfSplitScreenControllerProvider;
        private Provider<Optional<StartingWindowTypeAlgorithm>> dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider;
        private Provider<Optional<AppPairsController>> optionalOfAppPairsControllerProvider;
        private Provider<Optional<BubbleController>> optionalOfBubbleControllerProvider;
        private Provider<Optional<LegacySplitScreenController>> optionalOfLegacySplitScreenControllerProvider;
        private Provider<Optional<PipTouchHandler>> optionalOfPipTouchHandlerProvider;
        private Provider<Optional<ShellUnfoldProgressProvider>> optionalOfShellUnfoldProgressProvider;
        private Provider<Optional<AppPairs>> provideAppPairsProvider;
        private Provider<Optional<BackAnimationController>> provideBackAnimationControllerProvider;
        private Provider<Optional<BackAnimation>> provideBackAnimationProvider;
        private Provider<Optional<Bubbles>> provideBubblesProvider;
        private Provider<CompatUIController> provideCompatUIControllerProvider;
        private Provider<Optional<CompatUI>> provideCompatUIProvider;
        private Provider<Optional<DisplayAreaHelper>> provideDisplayAreaHelperProvider;
        private Provider<DisplayController> provideDisplayControllerProvider;
        private Provider<DisplayImeController> provideDisplayImeControllerProvider;
        private Provider<DisplayInsetsController> provideDisplayInsetsControllerProvider;
        private Provider<DragAndDropController> provideDragAndDropControllerProvider;
        private Provider<Optional<DragAndDrop>> provideDragAndDropProvider;
        private Provider<Optional<FreeformTaskListener>> provideFreeformTaskListenerProvider;
        private Provider<FullscreenTaskListener> provideFullscreenTaskListenerProvider;
        private Provider<Optional<FullscreenUnfoldController>> provideFullscreenUnfoldControllerProvider;
        private Provider<Optional<HideDisplayCutoutController>> provideHideDisplayCutoutControllerProvider;
        private Provider<Optional<HideDisplayCutout>> provideHideDisplayCutoutProvider;
        private Provider<IconProvider> provideIconProvider;
        private Provider<KidsModeTaskOrganizer> provideKidsModeTaskOrganizerProvider;
        private Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider;
        private Provider<Optional<OneHanded>> provideOneHandedProvider;
        private Provider<PipAnimationController> providePipAnimationControllerProvider;
        private Provider<PipAppOpsListener> providePipAppOpsListenerProvider;
        private Provider<PipMediaController> providePipMediaControllerProvider;
        private Provider<PipParamsChangedForwarder> providePipParamsChangedForwarderProvider;
        private Provider<Optional<Pip>> providePipProvider;
        private Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        private Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        private Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
        private Provider<PipTransitionState> providePipTransitionStateProvider;
        private Provider<PipUiEventLogger> providePipUiEventLoggerProvider;
        private Provider<Optional<RecentTasksController>> provideRecentTasksControllerProvider;
        private Provider<Optional<RecentTasks>> provideRecentTasksProvider;
        private Provider<ShellTransitions> provideRemoteTransitionsProvider;
        private Provider<RootDisplayAreaOrganizer> provideRootDisplayAreaOrganizerProvider;
        private Provider<Handler> provideSharedBackgroundHandlerProvider;
        private Provider<ShellExecutor> provideShellAnimationExecutorProvider;
        private Provider<ShellCommandHandlerImpl> provideShellCommandHandlerImplProvider;
        private Provider<Optional<ShellCommandHandler>> provideShellCommandHandlerProvider;
        private Provider<ShellInitImpl> provideShellInitImplProvider;
        private Provider<ShellInit> provideShellInitProvider;
        private Provider<ShellExecutor> provideShellMainExecutorProvider;
        private Provider<Handler> provideShellMainHandlerProvider;
        private Provider<ShellTaskOrganizer> provideShellTaskOrganizerProvider;
        private Provider<ShellExecutor> provideSplashScreenExecutorProvider;
        private Provider<Optional<SplitScreen>> provideSplitScreenProvider;
        private Provider<Optional<StartingSurface>> provideStartingSurfaceProvider;
        private Provider<StartingWindowController> provideStartingWindowControllerProvider;
        private Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider;
        private Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider2;
        private Provider<SyncTransactionQueue> provideSyncTransactionQueueProvider;
        private Provider<ShellExecutor> provideSysUIMainExecutorProvider;
        private Provider<SystemWindows> provideSystemWindowsProvider;
        private Provider<Optional<TaskSurfaceHelperController>> provideTaskSurfaceHelperControllerProvider;
        private Provider<Optional<TaskSurfaceHelper>> provideTaskSurfaceHelperProvider;
        private Provider<TaskViewFactoryController> provideTaskViewFactoryControllerProvider;
        private Provider<Optional<TaskViewFactory>> provideTaskViewFactoryProvider;
        private Provider<TaskViewTransitions> provideTaskViewTransitionsProvider;
        private Provider<TransactionPool> provideTransactionPoolProvider;
        private Provider<Transitions> provideTransitionsProvider;
        private Provider<TvPipBoundsAlgorithm> provideTvPipBoundsAlgorithmProvider;
        private Provider<TvPipBoundsController> provideTvPipBoundsControllerProvider;
        private Provider<TvPipBoundsState> provideTvPipBoundsStateProvider;
        private Provider<TvPipNotificationController> provideTvPipNotificationControllerProvider;
        private Provider<PipTransitionController> provideTvPipTransitionProvider;
        private Provider<Optional<UnfoldTransitionHandler>> provideUnfoldTransitionHandlerProvider;
        private Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        private Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        private Provider<Optional<OneHandedController>> providesOneHandedControllerProvider;
        private Provider<Optional<SplitScreenController>> providesSplitScreenControllerProvider;
        private Provider<TvPipMenuController> providesTvPipMenuControllerProvider;
        private Provider<HandlerThread> setShellMainThreadProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvWMComponentImpl tvWMComponentImpl;

        private TvWMComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, HandlerThread handlerThread) {
            this.tvWMComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            initialize(handlerThread);
        }

        private void initialize(HandlerThread handlerThread) {
            this.setShellMainThreadProvider = InstanceFactory.createNullable(handlerThread);
            this.provideShellMainHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainHandlerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.setShellMainThreadProvider, WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideSysUIMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory.create(WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideShellMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideShellMainHandlerProvider, this.provideSysUIMainExecutorProvider));
            this.provideDisplayControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.provideShellMainExecutorProvider));
            this.dynamicOverrideOptionalOfDisplayImeControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.provideDisplayInsetsControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayInsetsControllerFactory.create(this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideTransactionPoolProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.create());
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayImeControllerFactory.create(this.dynamicOverrideOptionalOfDisplayImeControllerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider, this.provideTransactionPoolProvider));
            this.provideIconProvider = DoubleCheck.provider(WMShellBaseModule_ProvideIconProviderFactory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideIconProvider, this.provideShellMainExecutorProvider));
            this.provideSyncTransactionQueueProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSyncTransactionQueueFactory.create(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider));
            this.provideShellTaskOrganizerProvider = new DelegateFactory();
            this.provideShellAnimationExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.create());
            this.provideTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransitionsFactory.create(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, this.provideDisplayControllerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.provideShellAnimationExecutorProvider));
            this.provideCompatUIControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDisplayImeControllerProvider, this.provideSyncTransactionQueueProvider, this.provideShellMainExecutorProvider, this.provideTransitionsProvider));
            this.providerTaskStackListenerImplProvider = DoubleCheck.provider(WMShellBaseModule_ProviderTaskStackListenerImplFactory.create(this.provideShellMainHandlerProvider));
            this.provideRecentTasksControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providerTaskStackListenerImplProvider, this.provideShellMainExecutorProvider));
            DelegateFactory.setDelegate(this.provideShellTaskOrganizerProvider, DoubleCheck.provider(WMShellBaseModule_ProvideShellTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, this.tvGlobalRootComponentImpl.contextProvider, this.provideCompatUIControllerProvider, this.provideRecentTasksControllerProvider)));
            this.provideKidsModeTaskOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideRecentTasksControllerProvider));
            this.optionalOfBubbleControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            Provider<Optional<SplitScreenController>> access$1900 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfSplitScreenControllerProvider = access$1900;
            this.providesSplitScreenControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesSplitScreenControllerFactory.create(access$1900, this.tvGlobalRootComponentImpl.contextProvider));
            this.optionalOfAppPairsControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.optionalOfPipTouchHandlerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            Provider<Optional<ShellUnfoldProgressProvider>> access$2200 = PresentJdkOptionalInstanceProvider.m1189of(this.tvGlobalRootComponentImpl.provideShellProgressProvider);
            this.optionalOfShellUnfoldProgressProvider = access$2200;
            Provider<Optional<FullscreenUnfoldController>> provider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory.create(this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider, access$2200));
            this.provideFullscreenUnfoldControllerProvider = provider;
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenTaskListenerFactory.create(this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider, this.provideSyncTransactionQueueProvider, provider, this.provideRecentTasksControllerProvider));
            this.provideUnfoldTransitionHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory.create(this.optionalOfShellUnfoldProgressProvider, this.provideTransactionPoolProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<FreeformTaskListener>> access$19002 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfFreeformTaskListenerProvider = access$19002;
            this.provideFreeformTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFreeformTaskListenerFactory.create(access$19002, this.tvGlobalRootComponentImpl.contextProvider));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.create());
            Provider<StartingWindowTypeAlgorithm> provider2 = DoubleCheck.provider(TvWMShellModule_ProvideStartingWindowTypeAlgorithmFactory.create());
            this.provideStartingWindowTypeAlgorithmProvider = provider2;
            Provider<Optional<StartingWindowTypeAlgorithm>> access$22002 = PresentJdkOptionalInstanceProvider.m1189of(provider2);
            this.dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider = access$22002;
            this.provideStartingWindowTypeAlgorithmProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory.create(access$22002));
            Provider<StartingWindowController> provider3 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideSplashScreenExecutorProvider, this.provideStartingWindowTypeAlgorithmProvider2, this.provideIconProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider3;
            Provider<ShellInitImpl> provider4 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfBubbleControllerProvider, this.providesSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideFullscreenUnfoldControllerProvider, this.provideUnfoldTransitionHandlerProvider, this.provideFreeformTaskListenerProvider, this.provideRecentTasksControllerProvider, this.provideTransitionsProvider, provider3, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider4;
            this.provideShellInitProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitFactory.create(provider4));
            this.optionalOfLegacySplitScreenControllerProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.provideTvPipBoundsStateProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipBoundsStateFactory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.providePipSnapAlgorithmProvider = DoubleCheck.provider(TvPipModule_ProvidePipSnapAlgorithmFactory.create());
            this.provideTvPipBoundsAlgorithmProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipBoundsAlgorithmFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideTvPipBoundsStateProvider, this.providePipSnapAlgorithmProvider));
            this.provideTvPipBoundsControllerProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipBoundsControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideShellMainHandlerProvider, this.provideTvPipBoundsStateProvider, this.provideTvPipBoundsAlgorithmProvider));
            this.provideSystemWindowsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSystemWindowsFactory.create(this.provideDisplayControllerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider));
            this.providePipMediaControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipMediaControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideShellMainHandlerProvider));
            this.providesTvPipMenuControllerProvider = DoubleCheck.provider(TvPipModule_ProvidesTvPipMenuControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideTvPipBoundsStateProvider, this.provideSystemWindowsProvider, this.providePipMediaControllerProvider, this.provideShellMainHandlerProvider));
            this.providePipTransitionStateProvider = DoubleCheck.provider(TvPipModule_ProvidePipTransitionStateFactory.create());
            Provider<PipSurfaceTransactionHelper> provider5 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.create());
            this.providePipSurfaceTransactionHelperProvider = provider5;
            Provider<PipAnimationController> provider6 = DoubleCheck.provider(TvPipModule_ProvidePipAnimationControllerFactory.create(provider5));
            this.providePipAnimationControllerProvider = provider6;
            this.provideTvPipTransitionProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipTransitionFactory.create(this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, provider6, this.provideTvPipBoundsAlgorithmProvider, this.provideTvPipBoundsStateProvider, this.providesTvPipMenuControllerProvider));
            this.providePipParamsChangedForwarderProvider = DoubleCheck.provider(TvPipModule_ProvidePipParamsChangedForwarderFactory.create());
            this.providePipUiEventLoggerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipUiEventLoggerFactory.create(this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider));
            this.providePipTaskOrganizerProvider = DoubleCheck.provider(TvPipModule_ProvidePipTaskOrganizerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesTvPipMenuControllerProvider, this.provideSyncTransactionQueueProvider, this.provideTvPipBoundsStateProvider, this.providePipTransitionStateProvider, this.provideTvPipBoundsAlgorithmProvider, this.providePipAnimationControllerProvider, this.provideTvPipTransitionProvider, this.providePipParamsChangedForwarderProvider, this.providePipSurfaceTransactionHelperProvider, this.providesSplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.providePipAppOpsListenerProvider = DoubleCheck.provider(TvPipModule_ProvidePipAppOpsListenerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providePipTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.provideTvPipNotificationControllerProvider = DoubleCheck.provider(TvPipModule_ProvideTvPipNotificationControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providePipMediaControllerProvider, this.providePipParamsChangedForwarderProvider, this.provideTvPipBoundsStateProvider, this.provideShellMainHandlerProvider));
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideWindowManagerShellWrapperFactory.create(this.provideShellMainExecutorProvider));
            this.providePipProvider = DoubleCheck.provider(TvPipModule_ProvidePipFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideTvPipBoundsStateProvider, this.provideTvPipBoundsAlgorithmProvider, this.provideTvPipBoundsControllerProvider, this.providePipAppOpsListenerProvider, this.providePipTaskOrganizerProvider, this.providesTvPipMenuControllerProvider, this.providePipMediaControllerProvider, this.provideTvPipTransitionProvider, this.provideTvPipNotificationControllerProvider, this.providerTaskStackListenerImplProvider, this.providePipParamsChangedForwarderProvider, this.provideDisplayControllerProvider, this.provideWindowManagerShellWrapperProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<OneHandedController>> access$19003 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfOneHandedControllerProvider = access$19003;
            this.providesOneHandedControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesOneHandedControllerFactory.create(access$19003));
            Provider<Optional<HideDisplayCutoutController>> provider7 = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideHideDisplayCutoutControllerProvider = provider7;
            Provider<ShellCommandHandlerImpl> provider8 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.providesSplitScreenControllerProvider, this.providePipProvider, this.providesOneHandedControllerProvider, provider7, this.optionalOfAppPairsControllerProvider, this.provideRecentTasksControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider8;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerFactory.create(provider8));
            this.provideOneHandedProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedFactory.create(this.providesOneHandedControllerProvider));
            this.provideLegacySplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideLegacySplitScreenFactory.create(this.optionalOfLegacySplitScreenControllerProvider));
            this.provideSplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenFactory.create(this.providesSplitScreenControllerProvider));
            this.provideAppPairsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideAppPairsFactory.create(this.optionalOfAppPairsControllerProvider));
            this.provideBubblesProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubblesFactory.create(this.optionalOfBubbleControllerProvider));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutFactory.create(this.provideHideDisplayCutoutControllerProvider));
            Provider<TaskViewTransitions> provider9 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideTaskViewTransitionsProvider = provider9;
            Provider<TaskViewFactoryController> provider10 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider, this.provideSyncTransactionQueueProvider, provider9));
            this.provideTaskViewFactoryControllerProvider = provider10;
            this.provideTaskViewFactoryProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryFactory.create(provider10));
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRemoteTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideStartingSurfaceProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingSurfaceFactory.create(this.provideStartingWindowControllerProvider));
            Provider<RootDisplayAreaOrganizer> provider11 = DoubleCheck.provider(WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider));
            this.provideRootDisplayAreaOrganizerProvider = provider11;
            this.provideDisplayAreaHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayAreaHelperFactory.create(this.provideShellMainExecutorProvider, provider11));
            WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory create = WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider);
            this.provideTaskSurfaceHelperControllerProvider = create;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperFactory.create(create));
            this.provideRecentTasksProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksFactory.create(this.provideRecentTasksControllerProvider));
            this.provideCompatUIProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIFactory.create(this.provideCompatUIControllerProvider));
            this.provideDragAndDropProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropFactory.create(this.provideDragAndDropControllerProvider));
            this.provideSharedBackgroundHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory.create());
            Provider<Optional<BackAnimationController>> provider12 = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideShellMainExecutorProvider, this.provideSharedBackgroundHandlerProvider));
            this.provideBackAnimationControllerProvider = provider12;
            this.provideBackAnimationProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationFactory.create(provider12));
        }

        public ShellInit getShellInit() {
            return this.provideShellInitProvider.get();
        }

        public Optional<ShellCommandHandler> getShellCommandHandler() {
            return this.provideShellCommandHandlerProvider.get();
        }

        public Optional<OneHanded> getOneHanded() {
            return this.provideOneHandedProvider.get();
        }

        public Optional<Pip> getPip() {
            return this.providePipProvider.get();
        }

        public Optional<LegacySplitScreen> getLegacySplitScreen() {
            return this.provideLegacySplitScreenProvider.get();
        }

        public Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider.get();
        }

        public Optional<AppPairs> getAppPairs() {
            return this.provideAppPairsProvider.get();
        }

        public Optional<Bubbles> getBubbles() {
            return this.provideBubblesProvider.get();
        }

        public Optional<HideDisplayCutout> getHideDisplayCutout() {
            return this.provideHideDisplayCutoutProvider.get();
        }

        public Optional<TaskViewFactory> getTaskViewFactory() {
            return this.provideTaskViewFactoryProvider.get();
        }

        public ShellTransitions getTransitions() {
            return this.provideRemoteTransitionsProvider.get();
        }

        public Optional<StartingSurface> getStartingSurface() {
            return this.provideStartingSurfaceProvider.get();
        }

        public Optional<DisplayAreaHelper> getDisplayAreaHelper() {
            return this.provideDisplayAreaHelperProvider.get();
        }

        public Optional<TaskSurfaceHelper> getTaskSurfaceHelper() {
            return this.provideTaskSurfaceHelperProvider.get();
        }

        public Optional<RecentTasks> getRecentTasks() {
            return this.provideRecentTasksProvider.get();
        }

        public Optional<CompatUI> getCompatUI() {
            return this.provideCompatUIProvider.get();
        }

        public Optional<DragAndDrop> getDragAndDrop() {
            return this.provideDragAndDropProvider.get();
        }

        public Optional<BackAnimation> getBackAnimation() {
            return this.provideBackAnimationProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$SysUIUnfoldComponentImpl */
    private static final class SysUIUnfoldComponentImpl implements SysUIUnfoldComponent {
        private Provider<FoldAodAnimationController> foldAodAnimationControllerProvider;
        private Provider<KeyguardUnfoldTransition> keyguardUnfoldTransitionProvider;
        private Provider<NotificationPanelUnfoldAnimationController> notificationPanelUnfoldAnimationControllerProvider;
        private Provider<UnfoldTransitionProgressProvider> p1Provider;
        private Provider<NaturalRotationUnfoldProgressProvider> p2Provider;
        private Provider<ScopedUnfoldTransitionProgressProvider> p3Provider;
        private Provider<StatusBarMoveFromCenterAnimationController> statusBarMoveFromCenterAnimationControllerProvider;
        private final SysUIUnfoldComponentImpl sysUIUnfoldComponentImpl;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;
        private Provider<UnfoldLightRevealOverlayAnimation> unfoldLightRevealOverlayAnimationProvider;
        private Provider<UnfoldTransitionWallpaperController> unfoldTransitionWallpaperControllerProvider;

        private SysUIUnfoldComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
            this.sysUIUnfoldComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            initialize(unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
        }

        private void initialize(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
            this.p2Provider = InstanceFactory.create(naturalRotationUnfoldProgressProvider);
            this.keyguardUnfoldTransitionProvider = DoubleCheck.provider(KeyguardUnfoldTransition_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.p2Provider));
            Factory create = InstanceFactory.create(scopedUnfoldTransitionProgressProvider);
            this.p3Provider = create;
            this.statusBarMoveFromCenterAnimationControllerProvider = DoubleCheck.provider(StatusBarMoveFromCenterAnimationController_Factory.create(create, this.tvGlobalRootComponentImpl.provideWindowManagerProvider));
            this.notificationPanelUnfoldAnimationControllerProvider = DoubleCheck.provider(NotificationPanelUnfoldAnimationController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.p2Provider));
            this.foldAodAnimationControllerProvider = DoubleCheck.provider(FoldAodAnimationController_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideDeviceStateManagerProvider, this.tvSysUIComponentImpl.wakefulnessLifecycleProvider, this.tvSysUIComponentImpl.globalSettingsImplProvider));
            Factory create2 = InstanceFactory.create(unfoldTransitionProgressProvider);
            this.p1Provider = create2;
            this.unfoldTransitionWallpaperControllerProvider = DoubleCheck.provider(UnfoldTransitionWallpaperController_Factory.create(create2, this.tvSysUIComponentImpl.wallpaperControllerProvider));
            this.unfoldLightRevealOverlayAnimationProvider = DoubleCheck.provider(UnfoldLightRevealOverlayAnimation_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideDeviceStateManagerProvider, this.tvGlobalRootComponentImpl.provideDisplayManagerProvider, this.p1Provider, this.tvSysUIComponentImpl.setDisplayAreaHelperProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider));
        }

        public KeyguardUnfoldTransition getKeyguardUnfoldTransition() {
            return this.keyguardUnfoldTransitionProvider.get();
        }

        public StatusBarMoveFromCenterAnimationController getStatusBarMoveFromCenterAnimationController() {
            return this.statusBarMoveFromCenterAnimationControllerProvider.get();
        }

        public NotificationPanelUnfoldAnimationController getNotificationPanelUnfoldAnimationController() {
            return this.notificationPanelUnfoldAnimationControllerProvider.get();
        }

        public FoldAodAnimationController getFoldAodAnimationController() {
            return this.foldAodAnimationControllerProvider.get();
        }

        public UnfoldTransitionWallpaperController getUnfoldTransitionWallpaperController() {
            return this.unfoldTransitionWallpaperControllerProvider.get();
        }

        public UnfoldLightRevealOverlayAnimation getUnfoldLightRevealOverlayAnimation() {
            return this.unfoldLightRevealOverlayAnimationProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$RemoteInputViewSubcomponentImpl */
    private static final class RemoteInputViewSubcomponentImpl implements RemoteInputViewSubcomponent {
        private final ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl;
        private final RemoteInputController remoteInputController;
        private final RemoteInputViewSubcomponentImpl remoteInputViewSubcomponentImpl;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;
        private final RemoteInputView view;

        private RemoteInputViewSubcomponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl2, RemoteInputView remoteInputView, RemoteInputController remoteInputController2) {
            this.remoteInputViewSubcomponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.expandableNotificationRowComponentImpl = expandableNotificationRowComponentImpl2;
            this.view = remoteInputView;
            this.remoteInputController = remoteInputController2;
        }

        private RemoteInputViewControllerImpl remoteInputViewControllerImpl() {
            return new RemoteInputViewControllerImpl(this.view, this.expandableNotificationRowComponentImpl.notificationEntry, (RemoteInputQuickSettingsDisabler) this.tvSysUIComponentImpl.remoteInputQuickSettingsDisablerProvider.get(), this.remoteInputController, (ShortcutManager) this.tvGlobalRootComponentImpl.provideShortcutManagerProvider.get(), (UiEventLogger) this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider.get());
        }

        public RemoteInputViewController getController() {
            return remoteInputViewControllerImpl();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$ExpandableNotificationRowComponentImpl */
    private static final class ExpandableNotificationRowComponentImpl implements ExpandableNotificationRowComponent {
        private Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
        /* access modifiers changed from: private */
        public final ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl;
        private Provider<ExpandableNotificationRowController> expandableNotificationRowControllerProvider;
        private Provider<ExpandableNotificationRowDragController> expandableNotificationRowDragControllerProvider;
        private Provider<ExpandableNotificationRow> expandableNotificationRowProvider;
        private Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
        private Provider<ExpandableViewController> expandableViewControllerProvider;
        private Provider<NotificationTapHelper.Factory> factoryProvider;
        private Provider<NotificationListContainer> listContainerProvider;
        /* access modifiers changed from: private */
        public final NotificationEntry notificationEntry;
        private Provider<NotificationEntry> notificationEntryProvider;
        private Provider<ExpandableNotificationRow.OnExpandClickListener> onExpandClickListenerProvider;
        private Provider<String> provideAppNameProvider;
        private Provider<String> provideNotificationKeyProvider;
        private Provider<StatusBarNotification> provideStatusBarNotificationProvider;
        private Provider<RemoteInputViewSubcomponent.Factory> remoteInputViewSubcomponentFactoryProvider;
        /* access modifiers changed from: private */
        public final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        /* access modifiers changed from: private */
        public final TvSysUIComponentImpl tvSysUIComponentImpl;

        private ExpandableNotificationRowComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry2, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
            this.expandableNotificationRowComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.notificationEntry = notificationEntry2;
            initialize(expandableNotificationRow, notificationEntry2, onExpandClickListener, notificationListContainer);
        }

        private void initialize(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry2, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
            this.expandableNotificationRowProvider = InstanceFactory.create(expandableNotificationRow);
            this.factoryProvider = NotificationTapHelper_Factory_Factory.create(this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider);
            ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.expandableNotificationRowProvider);
            this.expandableViewControllerProvider = create;
            ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.expandableNotificationRowProvider, create);
            this.expandableOutlineViewControllerProvider = create2;
            this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create2, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvSysUIComponentImpl.falsingCollectorImplProvider);
            this.remoteInputViewSubcomponentFactoryProvider = new Provider<RemoteInputViewSubcomponent.Factory>() {
                public RemoteInputViewSubcomponent.Factory get() {
                    return new RemoteInputViewSubcomponentFactory(ExpandableNotificationRowComponentImpl.this.tvGlobalRootComponentImpl, ExpandableNotificationRowComponentImpl.this.tvSysUIComponentImpl, ExpandableNotificationRowComponentImpl.this.expandableNotificationRowComponentImpl);
                }
            };
            this.listContainerProvider = InstanceFactory.create(notificationListContainer);
            Factory create3 = InstanceFactory.create(notificationEntry2);
            this.notificationEntryProvider = create3;
            this.provideStatusBarNotificationProvider = C2779xc255c3ca.create(create3);
            this.provideAppNameProvider = C2777x3e2d0aca.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideStatusBarNotificationProvider);
            this.provideNotificationKeyProvider = C2778xdc9a80a2.create(this.provideStatusBarNotificationProvider);
            this.onExpandClickListenerProvider = InstanceFactory.create(onExpandClickListener);
            this.expandableNotificationRowDragControllerProvider = ExpandableNotificationRowDragController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvSysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.tvSysUIComponentImpl.shadeControllerImplProvider);
            this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.activatableNotificationViewControllerProvider, this.remoteInputViewSubcomponentFactoryProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.listContainerProvider, this.tvSysUIComponentImpl.provideNotificationMediaManagerProvider, this.tvSysUIComponentImpl.smartReplyConstantsProvider, this.tvSysUIComponentImpl.provideSmartReplyControllerProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.tvSysUIComponentImpl.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, this.tvSysUIComponentImpl.keyguardBypassControllerProvider, this.tvSysUIComponentImpl.provideGroupMembershipManagerProvider, this.tvSysUIComponentImpl.provideGroupExpansionManagerProvider, this.tvSysUIComponentImpl.rowContentBindStageProvider, this.tvSysUIComponentImpl.provideNotificationLoggerProvider, this.tvSysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.onExpandClickListenerProvider, this.tvSysUIComponentImpl.statusBarStateControllerImplProvider, this.tvSysUIComponentImpl.provideNotificationGutsManagerProvider, this.tvSysUIComponentImpl.provideAllowNotificationLongPressProvider, this.tvSysUIComponentImpl.provideOnUserInteractionCallbackProvider, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvSysUIComponentImpl.falsingCollectorImplProvider, this.tvSysUIComponentImpl.featureFlagsReleaseProvider, this.tvSysUIComponentImpl.peopleNotificationIdentifierImplProvider, this.tvSysUIComponentImpl.provideBubblesManagerProvider, this.expandableNotificationRowDragControllerProvider));
        }

        public ExpandableNotificationRowController getExpandableNotificationRowController() {
            return this.expandableNotificationRowControllerProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$NavigationBarComponentImpl */
    private static final class NavigationBarComponentImpl implements NavigationBarComponent {
        private Provider<Context> contextProvider;
        private Provider<DeadZone> deadZoneProvider;
        private Provider<LightBarController.Factory> factoryProvider;
        private Provider<AutoHideController.Factory> factoryProvider2;
        private final NavigationBarComponentImpl navigationBarComponentImpl;
        private Provider<NavigationBar> navigationBarProvider;
        private Provider<NavigationBarTransitions> navigationBarTransitionsProvider;
        private Provider<EdgeBackGestureHandler> provideEdgeBackGestureHandlerProvider;
        private Provider<LayoutInflater> provideLayoutInflaterProvider;
        private Provider<NavigationBarFrame> provideNavigationBarFrameProvider;
        private Provider<NavigationBarView> provideNavigationBarviewProvider;
        private Provider<WindowManager> provideWindowManagerProvider;
        private Provider<Bundle> savedStateProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private NavigationBarComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, Context context, Bundle bundle) {
            this.navigationBarComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            initialize(context, bundle);
        }

        private void initialize(Context context, Bundle bundle) {
            Factory create = InstanceFactory.create(context);
            this.contextProvider = create;
            Provider<LayoutInflater> provider = DoubleCheck.provider(NavigationBarModule_ProvideLayoutInflaterFactory.create(create));
            this.provideLayoutInflaterProvider = provider;
            Provider<NavigationBarFrame> provider2 = DoubleCheck.provider(NavigationBarModule_ProvideNavigationBarFrameFactory.create(provider));
            this.provideNavigationBarFrameProvider = provider2;
            this.provideNavigationBarviewProvider = DoubleCheck.provider(NavigationBarModule_ProvideNavigationBarviewFactory.create(this.provideLayoutInflaterProvider, provider2));
            this.savedStateProvider = InstanceFactory.createNullable(bundle);
            this.provideWindowManagerProvider = DoubleCheck.provider(NavigationBarModule_ProvideWindowManagerFactory.create(this.contextProvider));
            this.factoryProvider = LightBarController_Factory_Factory.create(this.tvSysUIComponentImpl.darkIconDispatcherImplProvider, this.tvSysUIComponentImpl.provideBatteryControllerProvider, this.tvSysUIComponentImpl.navigationModeControllerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider);
            this.factoryProvider2 = AutoHideController_Factory_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider);
            this.deadZoneProvider = DeadZone_Factory.create(this.provideNavigationBarviewProvider);
            this.navigationBarTransitionsProvider = DoubleCheck.provider(NavigationBarTransitions_Factory.create(this.provideNavigationBarviewProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.tvSysUIComponentImpl.factoryProvider));
            this.provideEdgeBackGestureHandlerProvider = DoubleCheck.provider(NavigationBarModule_ProvideEdgeBackGestureHandlerFactory.create(this.tvSysUIComponentImpl.factoryProvider2, this.contextProvider));
            this.navigationBarProvider = DoubleCheck.provider(NavigationBar_Factory.create(this.provideNavigationBarviewProvider, this.provideNavigationBarFrameProvider, this.savedStateProvider, this.contextProvider, this.provideWindowManagerProvider, this.tvSysUIComponentImpl.assistManagerProvider, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.tvSysUIComponentImpl.providesDeviceProvisionedControllerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.tvSysUIComponentImpl.overviewProxyServiceProvider, this.tvSysUIComponentImpl.navigationModeControllerProvider, this.tvSysUIComponentImpl.statusBarStateControllerImplProvider, this.tvSysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.tvSysUIComponentImpl.provideSysUiStateProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvSysUIComponentImpl.provideCommandQueueProvider, this.tvSysUIComponentImpl.setPipProvider, this.tvSysUIComponentImpl.optionalOfRecentsProvider, this.tvSysUIComponentImpl.optionalOfCentralSurfacesProvider, this.tvSysUIComponentImpl.shadeControllerImplProvider, this.tvSysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.tvSysUIComponentImpl.notificationShadeDepthControllerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.navBarHelperProvider, this.tvSysUIComponentImpl.lightBarControllerProvider, this.factoryProvider, this.tvSysUIComponentImpl.autoHideControllerProvider, this.factoryProvider2, this.tvGlobalRootComponentImpl.provideOptionalTelecomManagerProvider, this.tvGlobalRootComponentImpl.provideInputMethodManagerProvider, this.deadZoneProvider, this.tvSysUIComponentImpl.deviceConfigProxyProvider, this.navigationBarTransitionsProvider, this.provideEdgeBackGestureHandlerProvider, this.tvSysUIComponentImpl.setBackAnimationProvider, this.tvSysUIComponentImpl.provideUserTrackerProvider));
        }

        public NavigationBar getNavigationBar() {
            return this.navigationBarProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$KeyguardStatusViewComponentImpl */
    private static final class KeyguardStatusViewComponentImpl implements KeyguardStatusViewComponent {
        private Provider<KeyguardClockSwitch> getKeyguardClockSwitchProvider;
        private Provider<KeyguardSliceView> getKeyguardSliceViewProvider;
        private Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;
        private final KeyguardStatusViewComponentImpl keyguardStatusViewComponentImpl;
        private final KeyguardStatusView presentation;
        private Provider<KeyguardStatusView> presentationProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private KeyguardStatusViewComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, KeyguardStatusView keyguardStatusView) {
            this.keyguardStatusViewComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.presentation = keyguardStatusView;
            initialize(keyguardStatusView);
        }

        private KeyguardClockSwitch keyguardClockSwitch() {
            return KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.getKeyguardClockSwitch(this.presentation);
        }

        private void initialize(KeyguardStatusView keyguardStatusView) {
            Factory create = InstanceFactory.create(keyguardStatusView);
            this.presentationProvider = create;
            KeyguardStatusViewModule_GetKeyguardClockSwitchFactory create2 = KeyguardStatusViewModule_GetKeyguardClockSwitchFactory.create(create);
            this.getKeyguardClockSwitchProvider = create2;
            KeyguardStatusViewModule_GetKeyguardSliceViewFactory create3 = KeyguardStatusViewModule_GetKeyguardSliceViewFactory.create(create2);
            this.getKeyguardSliceViewProvider = create3;
            this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(create3, this.tvSysUIComponentImpl.provideActivityStarterProvider, this.tvSysUIComponentImpl.configurationControllerImplProvider, this.tvSysUIComponentImpl.tunerServiceImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
        }

        public KeyguardClockSwitchController getKeyguardClockSwitchController() {
            return new KeyguardClockSwitchController(keyguardClockSwitch(), (StatusBarStateController) this.tvSysUIComponentImpl.statusBarStateControllerImplProvider.get(), (SysuiColorExtractor) this.tvSysUIComponentImpl.sysuiColorExtractorProvider.get(), (ClockManager) this.tvSysUIComponentImpl.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), (NotificationIconAreaController) this.tvSysUIComponentImpl.notificationIconAreaControllerProvider.get(), (BroadcastDispatcher) this.tvSysUIComponentImpl.broadcastDispatcherProvider.get(), (BatteryController) this.tvSysUIComponentImpl.provideBatteryControllerProvider.get(), (KeyguardUpdateMonitor) this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (LockscreenSmartspaceController) this.tvSysUIComponentImpl.lockscreenSmartspaceControllerProvider.get(), (KeyguardUnlockAnimationController) this.tvSysUIComponentImpl.keyguardUnlockAnimationControllerProvider.get(), (SecureSettings) this.tvSysUIComponentImpl.secureSettingsImpl(), (Executor) this.tvGlobalRootComponentImpl.provideMainExecutorProvider.get(), this.tvGlobalRootComponentImpl.mainResources(), (DumpManager) this.tvGlobalRootComponentImpl.dumpManagerProvider.get());
        }

        public KeyguardStatusViewController getKeyguardStatusViewController() {
            return new KeyguardStatusViewController(this.presentation, this.keyguardSliceViewControllerProvider.get(), getKeyguardClockSwitchController(), (KeyguardStateController) this.tvSysUIComponentImpl.keyguardStateControllerImplProvider.get(), (KeyguardUpdateMonitor) this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (ConfigurationController) this.tvSysUIComponentImpl.configurationControllerImplProvider.get(), (DozeParameters) this.tvSysUIComponentImpl.dozeParametersProvider.get(), (ScreenOffAnimationController) this.tvSysUIComponentImpl.screenOffAnimationControllerProvider.get());
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$KeyguardBouncerComponentImpl */
    private static final class KeyguardBouncerComponentImpl implements KeyguardBouncerComponent {
        private Provider<ViewGroup> bouncerContainerProvider;
        private Provider<AdminSecondaryLockScreenController.Factory> factoryProvider;
        private Provider<EmergencyButtonController.Factory> factoryProvider2;
        private Provider<KeyguardInputViewController.Factory> factoryProvider3;
        private Provider factoryProvider4;
        private final KeyguardBouncerComponentImpl keyguardBouncerComponentImpl;
        private Provider<KeyguardHostViewController> keyguardHostViewControllerProvider;
        private Provider<KeyguardSecurityViewFlipperController> keyguardSecurityViewFlipperControllerProvider;
        private Provider liftToActivateListenerProvider;
        private Provider<KeyguardHostView> providesKeyguardHostViewProvider;
        private Provider<KeyguardSecurityContainer> providesKeyguardSecurityContainerProvider;
        private Provider<KeyguardSecurityViewFlipper> providesKeyguardSecurityViewFlipperProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private KeyguardBouncerComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, ViewGroup viewGroup) {
            this.keyguardBouncerComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            initialize(viewGroup);
        }

        private void initialize(ViewGroup viewGroup) {
            Factory create = InstanceFactory.create(viewGroup);
            this.bouncerContainerProvider = create;
            Provider<KeyguardHostView> provider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardHostViewFactory.create(create, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesKeyguardHostViewProvider = provider;
            this.providesKeyguardSecurityContainerProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory.create(provider));
            this.factoryProvider = DoubleCheck.provider(AdminSecondaryLockScreenController_Factory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesKeyguardSecurityContainerProvider, this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            this.providesKeyguardSecurityViewFlipperProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory.create(this.providesKeyguardSecurityContainerProvider));
            this.liftToActivateListenerProvider = LiftToActivateListener_Factory.create(this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider);
            this.factoryProvider2 = EmergencyButtonController_Factory_Factory.create(this.tvSysUIComponentImpl.configurationControllerImplProvider, this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.tvGlobalRootComponentImpl.provideActivityTaskManagerProvider, this.tvSysUIComponentImpl.shadeControllerImplProvider, this.tvGlobalRootComponentImpl.provideTelecomManagerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider);
            this.factoryProvider3 = KeyguardInputViewController_Factory_Factory.create(this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider, this.tvSysUIComponentImpl.factoryProvider4, this.tvGlobalRootComponentImpl.provideInputMethodManagerProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.liftToActivateListenerProvider, this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.tvSysUIComponentImpl.falsingCollectorImplProvider, this.factoryProvider2, this.tvSysUIComponentImpl.devicePostureControllerImplProvider, this.tvSysUIComponentImpl.statusBarKeyguardViewManagerProvider);
            this.keyguardSecurityViewFlipperControllerProvider = DoubleCheck.provider(KeyguardSecurityViewFlipperController_Factory.create(this.providesKeyguardSecurityViewFlipperProvider, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.factoryProvider3, this.factoryProvider2));
            this.factoryProvider4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvSysUIComponentImpl.keyguardSecurityModelProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.keyguardStateControllerImplProvider, this.keyguardSecurityViewFlipperControllerProvider, this.tvSysUIComponentImpl.configurationControllerImplProvider, this.tvSysUIComponentImpl.falsingCollectorImplProvider, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvSysUIComponentImpl.userSwitcherControllerProvider, this.tvSysUIComponentImpl.featureFlagsReleaseProvider, this.tvSysUIComponentImpl.globalSettingsImplProvider, this.tvSysUIComponentImpl.sessionTrackerProvider);
            this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(this.providesKeyguardHostViewProvider, this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.tvSysUIComponentImpl.providesViewMediatorCallbackProvider, this.factoryProvider4));
        }

        public KeyguardHostViewController getKeyguardHostViewController() {
            return this.keyguardHostViewControllerProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$SectionHeaderControllerSubcomponentImpl */
    private static final class SectionHeaderControllerSubcomponentImpl implements SectionHeaderControllerSubcomponent {
        private Provider<String> clickIntentActionProvider;
        private Provider<Integer> headerTextProvider;
        private Provider<String> nodeLabelProvider;
        private final SectionHeaderControllerSubcomponentImpl sectionHeaderControllerSubcomponentImpl;
        private Provider<SectionHeaderNodeControllerImpl> sectionHeaderNodeControllerImplProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private SectionHeaderControllerSubcomponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, String str, Integer num, String str2) {
            this.sectionHeaderControllerSubcomponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            initialize(str, num, str2);
        }

        private void initialize(String str, Integer num, String str2) {
            this.nodeLabelProvider = InstanceFactory.create(str);
            this.headerTextProvider = InstanceFactory.create(num);
            this.clickIntentActionProvider = InstanceFactory.create(str2);
            this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(SectionHeaderNodeControllerImpl_Factory.create(this.nodeLabelProvider, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.headerTextProvider, this.tvSysUIComponentImpl.provideActivityStarterProvider, this.clickIntentActionProvider));
        }

        public NodeController getNodeController() {
            return this.sectionHeaderNodeControllerImplProvider.get();
        }

        public SectionHeaderController getHeaderController() {
            return this.sectionHeaderNodeControllerImplProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$DozeComponentImpl */
    private static final class DozeComponentImpl implements DozeComponent {
        private Provider<DozeAuthRemover> dozeAuthRemoverProvider;
        private final DozeComponentImpl dozeComponentImpl;
        private Provider<DozeDockHandler> dozeDockHandlerProvider;
        private Provider<DozeFalsingManagerAdapter> dozeFalsingManagerAdapterProvider;
        private Provider<DozeMachine> dozeMachineProvider;
        private Provider<DozeMachine.Service> dozeMachineServiceProvider;
        private Provider<DozePauser> dozePauserProvider;
        private Provider<DozeScreenBrightness> dozeScreenBrightnessProvider;
        private Provider<DozeScreenState> dozeScreenStateProvider;
        private Provider<DozeSuppressor> dozeSuppressorProvider;
        private Provider<DozeTriggers> dozeTriggersProvider;
        private Provider<DozeUi> dozeUiProvider;
        private Provider<DozeWallpaperState> dozeWallpaperStateProvider;
        private Provider<Optional<Sensor>[]> providesBrightnessSensorsProvider;
        private Provider<DozeMachine.Part[]> providesDozeMachinePartsProvider;
        private Provider<WakeLock> providesDozeWakeLockProvider;
        private Provider<DozeMachine.Service> providesWrappedServiceProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private DozeComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, DozeMachine.Service service) {
            this.dozeComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            initialize(service);
        }

        private void initialize(DozeMachine.Service service) {
            Factory create = InstanceFactory.create(service);
            this.dozeMachineServiceProvider = create;
            this.providesWrappedServiceProvider = DoubleCheck.provider(DozeModule_ProvidesWrappedServiceFactory.create(create, this.tvSysUIComponentImpl.dozeServiceHostProvider, this.tvSysUIComponentImpl.dozeParametersProvider));
            this.providesDozeWakeLockProvider = DoubleCheck.provider(DozeModule_ProvidesDozeWakeLockFactory.create(this.tvSysUIComponentImpl.builderProvider3, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            this.dozePauserProvider = DoubleCheck.provider(DozePauser_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideAlarmManagerProvider, this.tvSysUIComponentImpl.alwaysOnDisplayPolicyProvider));
            this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(DozeFalsingManagerAdapter_Factory.create(this.tvSysUIComponentImpl.falsingCollectorImplProvider));
            this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvSysUIComponentImpl.dozeServiceHostProvider, this.tvGlobalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.tvSysUIComponentImpl.dozeParametersProvider, this.tvSysUIComponentImpl.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, this.tvSysUIComponentImpl.dockManagerImplProvider, this.tvSysUIComponentImpl.provideProximitySensorProvider, this.tvSysUIComponentImpl.provideProximityCheckProvider, this.tvSysUIComponentImpl.dozeLogProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvSysUIComponentImpl.secureSettingsImplProvider, this.tvSysUIComponentImpl.authControllerProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.keyguardStateControllerImplProvider, this.tvSysUIComponentImpl.devicePostureControllerImplProvider));
            this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, this.tvSysUIComponentImpl.dozeServiceHostProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvSysUIComponentImpl.dozeParametersProvider, this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvSysUIComponentImpl.statusBarStateControllerImplProvider, this.tvSysUIComponentImpl.dozeLogProvider));
            this.providesBrightnessSensorsProvider = DozeModule_ProvidesBrightnessSensorsFactory.create(this.tvSysUIComponentImpl.asyncSensorManagerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvSysUIComponentImpl.dozeParametersProvider);
            this.dozeScreenBrightnessProvider = DoubleCheck.provider(DozeScreenBrightness_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesWrappedServiceProvider, this.tvSysUIComponentImpl.asyncSensorManagerProvider, this.providesBrightnessSensorsProvider, this.tvSysUIComponentImpl.dozeServiceHostProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.tvSysUIComponentImpl.alwaysOnDisplayPolicyProvider, this.tvSysUIComponentImpl.wakefulnessLifecycleProvider, this.tvSysUIComponentImpl.dozeParametersProvider, this.tvSysUIComponentImpl.devicePostureControllerImplProvider, this.tvSysUIComponentImpl.dozeLogProvider));
            this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvSysUIComponentImpl.dozeServiceHostProvider, this.tvSysUIComponentImpl.dozeParametersProvider, this.providesDozeWakeLockProvider, this.tvSysUIComponentImpl.authControllerProvider, this.tvSysUIComponentImpl.udfpsControllerProvider, this.tvSysUIComponentImpl.dozeLogProvider, this.dozeScreenBrightnessProvider));
            this.dozeWallpaperStateProvider = DoubleCheck.provider(DozeWallpaperState_Factory.create(FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.tvSysUIComponentImpl.biometricUnlockControllerProvider, this.tvSysUIComponentImpl.dozeParametersProvider));
            this.dozeDockHandlerProvider = DoubleCheck.provider(DozeDockHandler_Factory.create(this.tvGlobalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.tvSysUIComponentImpl.dockManagerImplProvider));
            this.dozeAuthRemoverProvider = DoubleCheck.provider(DozeAuthRemover_Factory.create(this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider));
            Provider<DozeSuppressor> provider = DoubleCheck.provider(DozeSuppressor_Factory.create(this.tvSysUIComponentImpl.dozeServiceHostProvider, this.tvGlobalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.tvSysUIComponentImpl.dozeLogProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideUiModeManagerProvider, this.tvSysUIComponentImpl.biometricUnlockControllerProvider));
            this.dozeSuppressorProvider = provider;
            this.providesDozeMachinePartsProvider = DozeModule_ProvidesDozeMachinePartsFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, this.dozeAuthRemoverProvider, provider);
            this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, this.tvGlobalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, this.tvSysUIComponentImpl.wakefulnessLifecycleProvider, this.tvSysUIComponentImpl.provideBatteryControllerProvider, this.tvSysUIComponentImpl.dozeLogProvider, this.tvSysUIComponentImpl.dockManagerImplProvider, this.tvSysUIComponentImpl.dozeServiceHostProvider, this.providesDozeMachinePartsProvider));
        }

        public DozeMachine getDozeMachine() {
            return this.dozeMachineProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$ComplicationViewModelComponentImpl */
    private static final class ComplicationViewModelComponentImpl implements ComplicationViewModelComponent {
        private final Complication complication;
        private final ComplicationViewModelComponentImpl complicationViewModelComponentImpl;
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;

        /* renamed from: id */
        private final ComplicationId f396id;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private ComplicationViewModelComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2, Complication complication2, ComplicationId complicationId) {
            this.complicationViewModelComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
            this.complication = complication2;
            this.f396id = complicationId;
        }

        private ComplicationViewModel complicationViewModel() {
            return new ComplicationViewModel(this.complication, this.f396id, this.dreamOverlayComponentImpl.host);
        }

        public ComplicationViewModelProvider getViewModelProvider() {
            return new ComplicationViewModelProvider(this.dreamOverlayComponentImpl.store, complicationViewModel());
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$InputSessionComponentImpl */
    private static final class InputSessionComponentImpl implements InputSessionComponent {
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final GestureDetector.OnGestureListener gestureListener;
        private final InputChannelCompat.InputEventListener inputEventListener;
        private final InputSessionComponentImpl inputSessionComponentImpl;
        private final String name;
        private final Boolean pilferOnGestureConsume;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private InputSessionComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2, String str, InputChannelCompat.InputEventListener inputEventListener2, GestureDetector.OnGestureListener onGestureListener, Boolean bool) {
            this.inputSessionComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
            this.name = str;
            this.inputEventListener = inputEventListener2;
            this.gestureListener = onGestureListener;
            this.pilferOnGestureConsume = bool;
        }

        public InputSession getInputSession() {
            return new InputSession(this.name, this.inputEventListener, this.gestureListener, this.pilferOnGestureConsume.booleanValue());
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$DreamOverlayComponentImpl */
    private static final class DreamOverlayComponentImpl implements DreamOverlayComponent {
        private Provider<FlingAnimationUtils.Builder> builderProvider;
        private Provider<ComplicationCollectionLiveData> complicationCollectionLiveDataProvider;
        private Provider<ComplicationCollectionViewModel> complicationCollectionViewModelProvider;
        private Provider<ComplicationHostViewController> complicationHostViewControllerProvider;
        private Provider<ComplicationLayoutEngine> complicationLayoutEngineProvider;
        private Provider<ComplicationViewModelComponent.Factory> complicationViewModelComponentFactoryProvider;
        private Provider<ComplicationViewModelTransformer> complicationViewModelTransformerProvider;
        private Provider<DateFormatUtil> dateFormatUtilProvider;
        /* access modifiers changed from: private */
        public final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private Provider<DreamOverlayContainerViewController> dreamOverlayContainerViewControllerProvider;
        private Provider<DreamOverlayStatusBarViewController> dreamOverlayStatusBarViewControllerProvider;
        /* access modifiers changed from: private */
        public final Complication.Host host;
        private Provider<Long> providesBurnInProtectionUpdateIntervalProvider;
        private Provider<ComplicationCollectionViewModel> providesComplicationCollectionViewModelProvider;
        private Provider<ConstraintLayout> providesComplicationHostViewProvider;
        private Provider<Integer> providesComplicationPaddingProvider;
        private Provider<Integer> providesComplicationsFadeInDurationProvider;
        private Provider<Integer> providesComplicationsFadeOutDurationProvider;
        private Provider<Integer> providesComplicationsRestoreTimeoutProvider;
        private Provider<DreamOverlayContainerView> providesDreamOverlayContainerViewProvider;
        private Provider<ViewGroup> providesDreamOverlayContentViewProvider;
        private Provider<DreamOverlayStatusBarView> providesDreamOverlayStatusBarViewProvider;
        private Provider<LifecycleOwner> providesLifecycleOwnerProvider;
        private Provider<Lifecycle> providesLifecycleProvider;
        private Provider<LifecycleRegistry> providesLifecycleRegistryProvider;
        private Provider<Integer> providesMaxBurnInOffsetProvider;
        private Provider<Long> providesMillisUntilFullJitterProvider;
        private Provider<TouchInsetManager> providesTouchInsetManagerProvider;
        private Provider<TouchInsetManager.TouchInsetSession> providesTouchInsetSessionProvider;
        /* access modifiers changed from: private */
        public final ViewModelStore store;
        private Provider<ViewModelStore> storeProvider;
        /* access modifiers changed from: private */
        public final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        /* access modifiers changed from: private */
        public final TvSysUIComponentImpl tvSysUIComponentImpl;

        private DreamOverlayComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, ViewModelStore viewModelStore, Complication.Host host2) {
            this.dreamOverlayComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            this.store = viewModelStore;
            this.host = host2;
            initialize(viewModelStore, host2);
        }

        private FlingAnimationUtils namedFlingAnimationUtils() {
            return C2105x6a8a7f11.providesSwipeToBouncerFlingAnimationUtilsOpening(this.builderProvider);
        }

        private FlingAnimationUtils namedFlingAnimationUtils2() {
            return C2104x4f80e514.providesSwipeToBouncerFlingAnimationUtilsClosing(this.builderProvider);
        }

        private float namedFloat() {
            return BouncerSwipeModule.providesSwipeToBouncerStartRegion(this.tvGlobalRootComponentImpl.mainResources());
        }

        private BouncerSwipeTouchHandler bouncerSwipeTouchHandler() {
            return new BouncerSwipeTouchHandler(this.tvGlobalRootComponentImpl.displayMetrics(), (StatusBarKeyguardViewManager) this.tvSysUIComponentImpl.statusBarKeyguardViewManagerProvider.get(), Optional.empty(), (NotificationShadeWindowController) this.tvSysUIComponentImpl.notificationShadeWindowControllerImplProvider.get(), BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory.providesValueAnimatorCreator(), BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory.providesVelocityTrackerFactory(), namedFlingAnimationUtils(), namedFlingAnimationUtils2(), namedFloat(), (UiEventLogger) this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider.get());
        }

        private DreamTouchHandler providesBouncerSwipeTouchHandler() {
            return BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory.providesBouncerSwipeTouchHandler(bouncerSwipeTouchHandler());
        }

        private Complication.VisibilityController visibilityController() {
            return ComplicationModule_ProvidesVisibilityControllerFactory.providesVisibilityController(this.complicationLayoutEngineProvider.get());
        }

        private HideComplicationTouchHandler hideComplicationTouchHandler() {
            return HideComplicationTouchHandler_Factory.newInstance(visibilityController(), this.providesComplicationsRestoreTimeoutProvider.get().intValue(), this.providesTouchInsetManagerProvider.get(), (Executor) this.tvGlobalRootComponentImpl.provideMainExecutorProvider.get(), this.tvGlobalRootComponentImpl.mainHandler());
        }

        private DreamTouchHandler providesHideComplicationTouchHandler() {
            return C2106xc6ed12da.providesHideComplicationTouchHandler(hideComplicationTouchHandler());
        }

        private Set<DreamTouchHandler> setOfDreamTouchHandler() {
            return SetBuilder.newSetBuilder(2).add(providesBouncerSwipeTouchHandler()).add(providesHideComplicationTouchHandler()).build();
        }

        private void initialize(ViewModelStore viewModelStore, Complication.Host host2) {
            this.providesDreamOverlayContainerViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory.create(this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesComplicationHostViewProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationHostViewFactory.create(this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesComplicationPaddingProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationPaddingFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider));
            Provider<TouchInsetManager> provider = DoubleCheck.provider(DreamOverlayModule_ProvidesTouchInsetManagerFactory.create(this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.providesDreamOverlayContainerViewProvider));
            this.providesTouchInsetManagerProvider = provider;
            this.providesTouchInsetSessionProvider = DreamOverlayModule_ProvidesTouchInsetSessionFactory.create(provider);
            this.providesComplicationsFadeInDurationProvider = DoubleCheck.provider(C2091xa92fda9a.create(this.tvGlobalRootComponentImpl.provideResourcesProvider));
            Provider<Integer> provider2 = DoubleCheck.provider(C2092x3242d31f.create(this.tvGlobalRootComponentImpl.provideResourcesProvider));
            this.providesComplicationsFadeOutDurationProvider = provider2;
            this.complicationLayoutEngineProvider = DoubleCheck.provider(ComplicationLayoutEngine_Factory.create(this.providesComplicationHostViewProvider, this.providesComplicationPaddingProvider, this.providesTouchInsetSessionProvider, this.providesComplicationsFadeInDurationProvider, provider2));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.providesLifecycleOwnerProvider = delegateFactory;
            Provider<LifecycleRegistry> provider3 = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleRegistryFactory.create(delegateFactory));
            this.providesLifecycleRegistryProvider = provider3;
            DelegateFactory.setDelegate(this.providesLifecycleOwnerProvider, DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleOwnerFactory.create(provider3)));
            this.storeProvider = InstanceFactory.create(viewModelStore);
            this.complicationCollectionLiveDataProvider = ComplicationCollectionLiveData_Factory.create(this.tvSysUIComponentImpl.dreamOverlayStateControllerProvider);
            C32351 r13 = new Provider<ComplicationViewModelComponent.Factory>() {
                public ComplicationViewModelComponent.Factory get() {
                    return new ComplicationViewModelComponentFactory(DreamOverlayComponentImpl.this.tvGlobalRootComponentImpl, DreamOverlayComponentImpl.this.tvSysUIComponentImpl, DreamOverlayComponentImpl.this.dreamOverlayComponentImpl);
                }
            };
            this.complicationViewModelComponentFactoryProvider = r13;
            ComplicationViewModelTransformer_Factory create = ComplicationViewModelTransformer_Factory.create(r13);
            this.complicationViewModelTransformerProvider = create;
            ComplicationCollectionViewModel_Factory create2 = ComplicationCollectionViewModel_Factory.create(this.complicationCollectionLiveDataProvider, create);
            this.complicationCollectionViewModelProvider = create2;
            C2094x1653c7a9 create3 = C2094x1653c7a9.create(this.storeProvider, create2);
            this.providesComplicationCollectionViewModelProvider = create3;
            this.complicationHostViewControllerProvider = ComplicationHostViewController_Factory.create(this.providesComplicationHostViewProvider, this.complicationLayoutEngineProvider, this.providesLifecycleOwnerProvider, create3);
            this.providesDreamOverlayContentViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayContentViewFactory.create(this.providesDreamOverlayContainerViewProvider));
            this.providesDreamOverlayStatusBarViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory.create(this.providesDreamOverlayContainerViewProvider));
            this.dateFormatUtilProvider = DateFormatUtil_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.dreamOverlayStatusBarViewControllerProvider = DoubleCheck.provider(DreamOverlayStatusBarViewController_Factory.create(this.providesDreamOverlayStatusBarViewProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvGlobalRootComponentImpl.provideConnectivityManagagerProvider, this.providesTouchInsetSessionProvider, this.tvGlobalRootComponentImpl.provideAlarmManagerProvider, this.tvSysUIComponentImpl.nextAlarmControllerImplProvider, this.dateFormatUtilProvider, this.tvSysUIComponentImpl.provideIndividualSensorPrivacyControllerProvider, this.tvSysUIComponentImpl.dreamOverlayNotificationCountProvider, this.tvSysUIComponentImpl.zenModeControllerImplProvider, this.tvSysUIComponentImpl.statusBarWindowStateControllerProvider));
            this.providesMaxBurnInOffsetProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesMaxBurnInOffsetFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider));
            this.providesBurnInProtectionUpdateIntervalProvider = DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.providesMillisUntilFullJitterProvider = DreamOverlayModule_ProvidesMillisUntilFullJitterFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.dreamOverlayContainerViewControllerProvider = DoubleCheck.provider(DreamOverlayContainerViewController_Factory.create(this.providesDreamOverlayContainerViewProvider, this.complicationHostViewControllerProvider, this.providesDreamOverlayContentViewProvider, this.dreamOverlayStatusBarViewControllerProvider, this.tvSysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.tvSysUIComponentImpl.blurUtilsProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.providesMaxBurnInOffsetProvider, this.providesBurnInProtectionUpdateIntervalProvider, this.providesMillisUntilFullJitterProvider));
            this.providesLifecycleProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleFactory.create(this.providesLifecycleOwnerProvider));
            this.builderProvider = FlingAnimationUtils_Builder_Factory.create(this.tvGlobalRootComponentImpl.provideDisplayMetricsProvider);
            this.providesComplicationsRestoreTimeoutProvider = DoubleCheck.provider(C2093x4a872b5c.create(this.tvGlobalRootComponentImpl.provideResourcesProvider));
        }

        public DreamOverlayContainerViewController getDreamOverlayContainerViewController() {
            return this.dreamOverlayContainerViewControllerProvider.get();
        }

        public LifecycleRegistry getLifecycleRegistry() {
            return this.providesLifecycleRegistryProvider.get();
        }

        public LifecycleOwner getLifecycleOwner() {
            return this.providesLifecycleOwnerProvider.get();
        }

        public DreamOverlayTouchMonitor getDreamOverlayTouchMonitor() {
            return new DreamOverlayTouchMonitor((Executor) this.tvGlobalRootComponentImpl.provideMainExecutorProvider.get(), this.providesLifecycleProvider.get(), new InputSessionComponentFactory(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl, this.dreamOverlayComponentImpl), setOfDreamTouchHandler());
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$FragmentCreatorImpl */
    private static final class FragmentCreatorImpl implements FragmentService.FragmentCreator {
        private final FragmentCreatorImpl fragmentCreatorImpl;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private FragmentCreatorImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2) {
            this.fragmentCreatorImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
        }

        private QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger() {
            return new QSFragmentDisableFlagsLogger((LogBuffer) this.tvSysUIComponentImpl.provideQSFragmentDisableLogBufferProvider.get(), (DisableFlagsLogger) this.tvSysUIComponentImpl.disableFlagsLoggerProvider.get());
        }

        public QSFragment createQSFragment() {
            return new QSFragment((RemoteInputQuickSettingsDisabler) this.tvSysUIComponentImpl.remoteInputQuickSettingsDisablerProvider.get(), (QSTileHost) this.tvSysUIComponentImpl.qSTileHostProvider.get(), (StatusBarStateController) this.tvSysUIComponentImpl.statusBarStateControllerImplProvider.get(), (CommandQueue) this.tvSysUIComponentImpl.provideCommandQueueProvider.get(), (MediaHost) this.tvSysUIComponentImpl.providesQSMediaHostProvider.get(), (MediaHost) this.tvSysUIComponentImpl.providesQuickQSMediaHostProvider.get(), (KeyguardBypassController) this.tvSysUIComponentImpl.keyguardBypassControllerProvider.get(), new QSFragmentComponentFactory(this.tvGlobalRootComponentImpl, this.tvSysUIComponentImpl), qSFragmentDisableFlagsLogger(), (FalsingManager) this.tvSysUIComponentImpl.falsingManagerProxyProvider.get(), (DumpManager) this.tvGlobalRootComponentImpl.dumpManagerProvider.get());
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$QSFragmentComponentImpl */
    private static final class QSFragmentComponentImpl implements QSFragmentComponent {
        private Provider<BatteryMeterViewController> batteryMeterViewControllerProvider;
        private Provider<CarrierTextManager.Builder> builderProvider;
        private Provider<QSCarrierGroupController.Builder> builderProvider2;
        private Provider factoryProvider;
        private Provider<BrightnessController.Factory> factoryProvider2;
        private Provider<VariableDateViewController.Factory> factoryProvider3;
        private Provider<MultiUserSwitchController.Factory> factoryProvider4;
        private Provider<FooterActionsController> footerActionsControllerProvider;
        private Provider<BatteryMeterViewController> getBatteryMeterViewControllerProvider;
        private Provider<BatteryMeterView> getBatteryMeterViewProvider;
        private Provider<HeaderPrivacyIconsController> headerPrivacyIconsControllerProvider;
        private Provider<NTQSAnimator> nTQSAnimatorProvider;
        private Provider<NTQSStatusBarController> nTQSStatusBarControllerProvider;
        private Provider<QSPanel> provideQSPanelProvider;
        private Provider<View> provideRootViewProvider;
        private Provider<Context> provideThemedContextProvider;
        private Provider<LayoutInflater> provideThemedLayoutInflaterProvider;
        private Provider<BatteryMeterView> providesBatteryMeterViewProvider;
        private Provider<NTQSStatusBar> providesNTQSStatusBarProvider;
        private Provider<OngoingPrivacyChip> providesPrivacyChipProvider;
        private Provider<QSContainerImpl> providesQSContainerImplProvider;
        private Provider<QSCustomizer> providesQSCutomizerProvider;
        private Provider<View> providesQSFgsManagerFooterViewProvider;
        private Provider<FooterActionsView> providesQSFooterActionsViewProvider;
        private Provider<QSFooter> providesQSFooterProvider;
        private Provider<QSFooterView> providesQSFooterViewProvider;
        private Provider<View> providesQSSecurityFooterViewProvider;
        private Provider<Boolean> providesQSUsingCollapsedLandscapeMediaProvider;
        private Provider<Boolean> providesQSUsingMediaPlayerProvider;
        private Provider<QuickQSPanel> providesQuickQSPanelProvider;
        private Provider<QuickStatusBarHeader> providesQuickStatusBarHeaderProvider;
        private Provider<StatusIconContainer> providesStatusIconContainerProvider;
        private Provider<QSAnimator> qSAnimatorProvider;
        private Provider<QSContainerImplController> qSContainerImplControllerProvider;
        private Provider<QSCustomizerController> qSCustomizerControllerProvider;
        private Provider<QSFgsManagerFooter> qSFgsManagerFooterProvider;
        private Provider<QSFooterViewController> qSFooterViewControllerProvider;
        private final QSFragmentComponentImpl qSFragmentComponentImpl;
        private Provider<QSPanelController> qSPanelControllerProvider;
        private Provider<QSSecurityFooter> qSSecurityFooterProvider;
        private Provider<QSSquishinessController> qSSquishinessControllerProvider;
        private Provider<QSFragment> qsFragmentProvider;
        private Provider<QuickQSPanelController> quickQSPanelControllerProvider;
        private Provider quickStatusBarHeaderControllerProvider;
        private Provider<TileAdapter> tileAdapterProvider;
        private Provider<TileQueryHelper> tileQueryHelperProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private final TvSysUIComponentImpl tvSysUIComponentImpl;

        private QSFragmentComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, TvSysUIComponentImpl tvSysUIComponentImpl2, QSFragment qSFragment) {
            this.qSFragmentComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            this.tvSysUIComponentImpl = tvSysUIComponentImpl2;
            initialize(qSFragment);
        }

        private void initialize(QSFragment qSFragment) {
            Factory create = InstanceFactory.create(qSFragment);
            this.qsFragmentProvider = create;
            QSFragmentModule_ProvideRootViewFactory create2 = QSFragmentModule_ProvideRootViewFactory.create(create);
            this.provideRootViewProvider = create2;
            this.provideQSPanelProvider = QSFragmentModule_ProvideQSPanelFactory.create(create2);
            this.providesQSCutomizerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSCutomizerFactory.create(this.provideRootViewProvider));
            this.tileQueryHelperProvider = DoubleCheck.provider(TileQueryHelper_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvSysUIComponentImpl.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.provideBackgroundExecutorProvider));
            QSFragmentModule_ProvideThemedContextFactory create3 = QSFragmentModule_ProvideThemedContextFactory.create(this.provideRootViewProvider);
            this.provideThemedContextProvider = create3;
            this.tileAdapterProvider = DoubleCheck.provider(TileAdapter_Factory.create(create3, this.tvSysUIComponentImpl.qSTileHostProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            this.qSCustomizerControllerProvider = DoubleCheck.provider(QSCustomizerController_Factory.create(this.providesQSCutomizerProvider, this.tileQueryHelperProvider, this.tvSysUIComponentImpl.qSTileHostProvider, this.tileAdapterProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.tvSysUIComponentImpl.keyguardStateControllerImplProvider, this.tvSysUIComponentImpl.lightBarControllerProvider, this.tvSysUIComponentImpl.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            this.providesQSUsingMediaPlayerProvider = QSFragmentModule_ProvidesQSUsingMediaPlayerFactory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.factoryProvider = DoubleCheck.provider(QSTileRevealController_Factory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.qSCustomizerControllerProvider));
            this.factoryProvider2 = BrightnessController_Factory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvSysUIComponentImpl.provideBgHandlerProvider);
            this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, this.tvSysUIComponentImpl.tunerServiceImplProvider, this.tvSysUIComponentImpl.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, this.tvSysUIComponentImpl.providesQSMediaHostProvider, this.factoryProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.qSLoggerProvider, this.factoryProvider2, this.tvSysUIComponentImpl.factoryProvider8, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvSysUIComponentImpl.statusBarKeyguardViewManagerProvider));
            QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create4 = QSFragmentModule_ProvidesQuickStatusBarHeaderFactory.create(this.provideRootViewProvider);
            this.providesQuickStatusBarHeaderProvider = create4;
            this.providesQuickQSPanelProvider = QSFragmentModule_ProvidesQuickQSPanelFactory.create(create4);
            this.providesQSUsingCollapsedLandscapeMediaProvider = QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory.create(this.tvGlobalRootComponentImpl.contextProvider);
            Provider<QuickQSPanelController> provider = DoubleCheck.provider(QuickQSPanelController_Factory.create(this.providesQuickQSPanelProvider, this.tvSysUIComponentImpl.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, this.tvSysUIComponentImpl.providesQuickQSMediaHostProvider, this.providesQSUsingCollapsedLandscapeMediaProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.qSLoggerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.quickQSPanelControllerProvider = provider;
            this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, provider, this.tvSysUIComponentImpl.qSTileHostProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.tunerServiceImplProvider, this.tvGlobalRootComponentImpl.qSExpansionPathInterpolatorProvider));
            this.providesQSContainerImplProvider = QSFragmentModule_ProvidesQSContainerImplFactory.create(this.provideRootViewProvider);
            this.providesPrivacyChipProvider = DoubleCheck.provider(QSFragmentModule_ProvidesPrivacyChipFactory.create(this.providesQuickStatusBarHeaderProvider));
            this.providesStatusIconContainerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesStatusIconContainerFactory.create(this.providesQuickStatusBarHeaderProvider));
            this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(this.tvSysUIComponentImpl.privacyItemControllerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.providesPrivacyChipProvider, this.tvSysUIComponentImpl.privacyDialogControllerProvider, this.tvSysUIComponentImpl.privacyLoggerProvider, this.providesStatusIconContainerProvider, this.tvGlobalRootComponentImpl.providePermissionManagerProvider, this.tvSysUIComponentImpl.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.provideActivityStarterProvider, this.tvSysUIComponentImpl.appOpsControllerImplProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideSafetyCenterManagerProvider);
            this.builderProvider = CarrierTextManager_Builder_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.tvGlobalRootComponentImpl.provideWifiManagerProvider, this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.tvSysUIComponentImpl.telephonyListenerManagerProvider, this.tvSysUIComponentImpl.wakefulnessLifecycleProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.provideBackgroundExecutorProvider, this.tvSysUIComponentImpl.keyguardUpdateMonitorProvider, this.tvSysUIComponentImpl.carrierNameCustomizationProvider);
            this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(this.tvSysUIComponentImpl.provideActivityStarterProvider, this.tvSysUIComponentImpl.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.tvSysUIComponentImpl.networkControllerImplProvider, this.builderProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvSysUIComponentImpl.carrierConfigTrackerProvider, this.tvSysUIComponentImpl.featureFlagsReleaseProvider, this.tvSysUIComponentImpl.subscriptionManagerSlotIndexResolverProvider);
            this.factoryProvider3 = VariableDateViewController_Factory_Factory.create(this.tvSysUIComponentImpl.bindSystemClockProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvSysUIComponentImpl.provideTimeTickHandlerProvider);
            QSFragmentModule_ProvidesBatteryMeterViewFactory create5 = QSFragmentModule_ProvidesBatteryMeterViewFactory.create(this.providesQuickStatusBarHeaderProvider);
            this.providesBatteryMeterViewProvider = create5;
            this.batteryMeterViewControllerProvider = BatteryMeterViewController_Factory.create(create5, this.tvSysUIComponentImpl.configurationControllerImplProvider, this.tvSysUIComponentImpl.tunerServiceImplProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideContentResolverProvider, this.tvSysUIComponentImpl.provideBatteryControllerProvider);
            this.quickStatusBarHeaderControllerProvider = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, this.headerPrivacyIconsControllerProvider, this.tvSysUIComponentImpl.statusBarIconControllerImplProvider, this.tvSysUIComponentImpl.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, this.tvSysUIComponentImpl.sysuiColorExtractorProvider, this.tvGlobalRootComponentImpl.qSExpansionPathInterpolatorProvider, this.tvSysUIComponentImpl.featureFlagsReleaseProvider, this.factoryProvider3, this.batteryMeterViewControllerProvider, this.tvSysUIComponentImpl.statusBarContentInsetsProvider));
            this.providesNTQSStatusBarProvider = QSFragmentModule_ProvidesNTQSStatusBarFactory.create(this.provideRootViewProvider);
            QSFragmentModule_GetBatteryMeterViewFactory create6 = QSFragmentModule_GetBatteryMeterViewFactory.create(this.providesQuickStatusBarHeaderProvider);
            this.getBatteryMeterViewProvider = create6;
            this.getBatteryMeterViewControllerProvider = QSFragmentModule_GetBatteryMeterViewControllerFactory.create(create6, this.tvSysUIComponentImpl.configurationControllerImplProvider, this.tvSysUIComponentImpl.tunerServiceImplProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideContentResolverProvider, this.tvSysUIComponentImpl.provideBatteryControllerProvider);
            NTQSStatusBarController_Factory create7 = NTQSStatusBarController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesNTQSStatusBarProvider, this.tvSysUIComponentImpl.privacyDialogControllerProvider, this.tvSysUIComponentImpl.privacyItemControllerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.privacyLoggerProvider, this.tvSysUIComponentImpl.configurationControllerImplProvider, this.getBatteryMeterViewControllerProvider);
            this.nTQSStatusBarControllerProvider = create7;
            this.qSContainerImplControllerProvider = DoubleCheck.provider(QSContainerImplController_Factory.create(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, this.quickStatusBarHeaderControllerProvider, create7, this.tvSysUIComponentImpl.configurationControllerImplProvider));
            QSFragmentModule_ProvidesQSFooterViewFactory create8 = QSFragmentModule_ProvidesQSFooterViewFactory.create(this.provideRootViewProvider);
            this.providesQSFooterViewProvider = create8;
            Provider<QSFooterViewController> provider2 = DoubleCheck.provider(QSFooterViewController_Factory.create(create8, this.tvSysUIComponentImpl.provideUserTrackerProvider, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvSysUIComponentImpl.provideActivityStarterProvider, this.qSPanelControllerProvider));
            this.qSFooterViewControllerProvider = provider2;
            this.providesQSFooterProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSFooterFactory.create(provider2));
            this.provideThemedLayoutInflaterProvider = QSFragmentModule_ProvideThemedLayoutInflaterFactory.create(this.provideThemedContextProvider);
            QSFragmentModule_ProvidesQSFooterActionsViewFactory create9 = QSFragmentModule_ProvidesQSFooterActionsViewFactory.create(this.provideRootViewProvider);
            this.providesQSFooterActionsViewProvider = create9;
            Provider<View> provider3 = DoubleCheck.provider(QSFragmentModule_ProvidesQSSecurityFooterViewFactory.create(this.provideThemedLayoutInflaterProvider, create9));
            this.providesQSSecurityFooterViewProvider = provider3;
            this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider3, this.tvSysUIComponentImpl.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvSysUIComponentImpl.provideActivityStarterProvider, this.tvSysUIComponentImpl.securityControllerImplProvider, this.tvSysUIComponentImpl.provideDialogLaunchAnimatorProvider, this.tvSysUIComponentImpl.provideBgLooperProvider, this.tvSysUIComponentImpl.broadcastDispatcherProvider));
            Provider<NTQSAnimator> provider4 = DoubleCheck.provider(NTQSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider, this.tvSysUIComponentImpl.qSTileHostProvider, this.qSSecurityFooterProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.tunerServiceImplProvider, this.tvGlobalRootComponentImpl.qSExpansionPathInterpolatorProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            this.nTQSAnimatorProvider = provider4;
            this.qSSquishinessControllerProvider = DoubleCheck.provider(QSSquishinessController_Factory.create(provider4, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider));
            this.factoryProvider4 = DoubleCheck.provider(MultiUserSwitchController_Factory_Factory.create(this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvSysUIComponentImpl.userSwitcherControllerProvider, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvSysUIComponentImpl.userSwitchDialogControllerProvider, this.tvSysUIComponentImpl.featureFlagsReleaseProvider, this.tvSysUIComponentImpl.provideActivityStarterProvider));
            Provider<View> provider5 = DoubleCheck.provider(QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory.create(this.provideThemedLayoutInflaterProvider, this.providesQSFooterActionsViewProvider));
            this.providesQSFgsManagerFooterViewProvider = provider5;
            this.qSFgsManagerFooterProvider = DoubleCheck.provider(QSFgsManagerFooter_Factory.create(provider5, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvSysUIComponentImpl.provideBackgroundExecutorProvider, this.tvSysUIComponentImpl.fgsManagerControllerProvider));
            this.footerActionsControllerProvider = DoubleCheck.provider(FooterActionsController_Factory.create(this.providesQSFooterActionsViewProvider, this.factoryProvider4, this.tvSysUIComponentImpl.provideActivityStarterProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvSysUIComponentImpl.provideUserTrackerProvider, this.tvSysUIComponentImpl.userInfoControllerImplProvider, this.tvSysUIComponentImpl.providesDeviceProvisionedControllerProvider, this.qSSecurityFooterProvider, this.qSFgsManagerFooterProvider, this.tvSysUIComponentImpl.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.tvSysUIComponentImpl.globalActionsDialogLiteProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvSysUIComponentImpl.isPMLiteEnabledProvider, this.tvSysUIComponentImpl.globalSettingsImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
        }

        public QSPanelController getQSPanelController() {
            return this.qSPanelControllerProvider.get();
        }

        public QuickQSPanelController getQuickQSPanelController() {
            return this.quickQSPanelControllerProvider.get();
        }

        public QSAnimator getQSAnimator() {
            return this.qSAnimatorProvider.get();
        }

        public QSContainerImplController getQSContainerImplController() {
            return this.qSContainerImplControllerProvider.get();
        }

        public QSFooter getQSFooter() {
            return this.providesQSFooterProvider.get();
        }

        public QSCustomizerController getQSCustomizerController() {
            return this.qSCustomizerControllerProvider.get();
        }

        public QSSquishinessController getQSSquishinessController() {
            return this.qSSquishinessControllerProvider.get();
        }

        public FooterActionsController getQSFooterActionController() {
            return this.footerActionsControllerProvider.get();
        }

        public NTQSAnimator getNTQSAnimator() {
            return this.nTQSAnimatorProvider.get();
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$TvSysUIComponentImpl */
    private static final class TvSysUIComponentImpl implements TvSysUIComponent {
        private Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
        private Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
        private Provider<AccessibilityController> accessibilityControllerProvider;
        private Provider<AccessibilityFloatingMenuController> accessibilityFloatingMenuControllerProvider;
        private Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
        private Provider<ActionClickLogger> actionClickLoggerProvider;
        private Provider<ActionProxyReceiver> actionProxyReceiverProvider;
        private Provider<ActiveUnlockConfig> activeUnlockConfigProvider;
        private Provider<ActivityIntentHelper> activityIntentHelperProvider;
        private Provider<ActivityStarterDelegate> activityStarterDelegateProvider;
        private Provider<UserDetailView.Adapter> adapterProvider;
        private Provider<AirplaneModeTile> airplaneModeTileProvider;
        private Provider<AlarmTile> alarmTileProvider;
        /* access modifiers changed from: private */
        public Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
        private Provider<AmbientStateEx> ambientStateExProvider;
        private Provider<AmbientState> ambientStateProvider;
        /* access modifiers changed from: private */
        public Provider<AppOpsControllerImpl> appOpsControllerImplProvider;
        private Provider<AppOpsPrivacyItemMonitor> appOpsPrivacyItemMonitorProvider;
        private Provider<AssistLogger> assistLoggerProvider;
        private Provider<AssistManagerEx> assistManagerExProvider;
        /* access modifiers changed from: private */
        public Provider<AssistManager> assistManagerProvider;
        private Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
        /* access modifiers changed from: private */
        public Provider<AsyncSensorManager> asyncSensorManagerProvider;
        /* access modifiers changed from: private */
        public Provider<AuthController> authControllerProvider;
        /* access modifiers changed from: private */
        public Provider<AutoHideController> autoHideControllerProvider;
        private Provider<BatterySaverTile> batterySaverTileProvider;
        private Provider<BatteryShareControllerImpl> batteryShareControllerImplProvider;
        private Provider<BatteryShareTile> batteryShareTileProvider;
        private Provider<BatteryStateNotifier> batteryStateNotifierProvider;
        private Provider<BindEventManagerImpl> bindEventManagerImplProvider;
        private Provider<RotationPolicyWrapper> bindRotationPolicyWrapperProvider;
        /* access modifiers changed from: private */
        public Provider<SystemClock> bindSystemClockProvider;
        /* access modifiers changed from: private */
        public Provider<BiometricUnlockController> biometricUnlockControllerProvider;
        private Provider<BluetoothControllerImpl> bluetoothControllerImplProvider;
        private Provider<BluetoothTile> bluetoothTileProvider;
        /* access modifiers changed from: private */
        public Provider<BlurUtils> blurUtilsProvider;
        private Provider<BootCompleteCacheImpl> bootCompleteCacheImplProvider;
        private Provider<BrightLineFalsingManager> brightLineFalsingManagerProvider;
        private Provider<BrightnessDialog> brightnessDialogProvider;
        private Provider<BroadcastDialogController> broadcastDialogControllerProvider;
        private Provider<BroadcastDispatcherLogger> broadcastDispatcherLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<BroadcastDispatcher> broadcastDispatcherProvider;
        private Provider<BroadcastDispatcherStartable> broadcastDispatcherStartableProvider;
        private Provider<BroadcastSender> broadcastSenderProvider;
        private Provider<ThresholdSensorImpl.BuilderFactory> builderFactoryProvider;
        private Provider<ThresholdSensorImpl.Builder> builderProvider;
        private Provider<WakeLock.Builder> builderProvider2;
        /* access modifiers changed from: private */
        public Provider<DelayedWakeLock.Builder> builderProvider3;
        private Provider<CustomTile.Builder> builderProvider4;
        private Provider<NightDisplayListenerModule.Builder> builderProvider5;
        private Provider<AutoAddTracker.Builder> builderProvider6;
        private Provider<TileServiceRequestController.Builder> builderProvider7;
        private Provider<CalendarManager> calendarManagerProvider;
        private Provider<CallbackHandler> callbackHandlerProvider;
        private Provider<CameraToggleTile> cameraToggleTileProvider;
        /* access modifiers changed from: private */
        public Provider<CarrierConfigTracker> carrierConfigTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<CarrierNameCustomization> carrierNameCustomizationProvider;
        private Provider<CastControllerImpl> castControllerImplProvider;
        private Provider<CastTile> castTileProvider;
        private Provider<CellularTile> cellularTileProvider;
        private Provider<CentralSurfacesImplEx> centralSurfacesImplExProvider;
        private Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        /* access modifiers changed from: private */
        public Provider<ClockManager> clockManagerProvider;
        private Provider<ColorCorrectionTile> colorCorrectionTileProvider;
        private Provider<ColorInversionTile> colorInversionTileProvider;
        private Provider<CommandQueueEx> commandQueueExProvider;
        private Provider<CommandRegistry> commandRegistryProvider;
        /* access modifiers changed from: private */
        public Provider<ConfigurationControllerImpl> configurationControllerImplProvider;
        private Provider<ContextComponentResolver> contextComponentResolverProvider;
        private Provider<ControlActionCoordinatorImpl> controlActionCoordinatorImplProvider;
        private Provider<ControlsActivity> controlsActivityProvider;
        private Provider<ControlsBindingControllerImpl> controlsBindingControllerImplProvider;
        private Provider<ControlsComponent> controlsComponentProvider;
        private Provider<ControlsControllerImpl> controlsControllerImplProvider;
        private Provider<ControlsEditingActivity> controlsEditingActivityProvider;
        private Provider<ControlsFavoritingActivity> controlsFavoritingActivityProvider;
        private Provider<ControlsListingControllerImpl> controlsListingControllerImplProvider;
        private Provider<ControlsMetricsLoggerImpl> controlsMetricsLoggerImplProvider;
        private Provider<ControlsProviderSelectorActivity> controlsProviderSelectorActivityProvider;
        private Provider<ControlsRequestDialog> controlsRequestDialogProvider;
        private Provider<ControlsUiControllerImpl> controlsUiControllerImplProvider;
        private Provider<ConversationNotificationManager> conversationNotificationManagerProvider;
        private Provider<ConversationNotificationProcessor> conversationNotificationProcessorProvider;
        private Provider<CreateUserActivity> createUserActivityProvider;
        private Provider<CustomIconCache> customIconCacheProvider;
        private Provider<CustomTileStatePersister> customTileStatePersisterProvider;
        /* access modifiers changed from: private */
        public Provider<DarkIconDispatcherImpl> darkIconDispatcherImplProvider;
        private Provider<DataSaverTile> dataSaverTileProvider;
        private Provider<DebugModeFilterProvider> debugModeFilterProvider;
        private Provider<DefaultUiController> defaultUiControllerProvider;
        private Provider<DeleteScreenshotReceiver> deleteScreenshotReceiverProvider;
        private Provider<Dependency> dependencyProvider;
        /* access modifiers changed from: private */
        public Provider<DeviceConfigProxy> deviceConfigProxyProvider;
        private Provider<DeviceControlsControllerImpl> deviceControlsControllerImplProvider;
        private Provider<DeviceControlsTile> deviceControlsTileProvider;
        /* access modifiers changed from: private */
        public Provider<DevicePostureControllerImpl> devicePostureControllerImplProvider;
        private Provider<DeviceProvisionedControllerImpl> deviceProvisionedControllerImplProvider;
        private Provider<DeviceStateRotationLockSettingController> deviceStateRotationLockSettingControllerProvider;
        private Provider diagonalClassifierProvider;
        /* access modifiers changed from: private */
        public Provider<DisableFlagsLogger> disableFlagsLoggerProvider;
        private Provider<DismissCallbackRegistry> dismissCallbackRegistryProvider;
        private Provider distanceClassifierProvider;
        private Provider<DndTile> dndTileProvider;
        /* access modifiers changed from: private */
        public Provider<DockManagerImpl> dockManagerImplProvider;
        private Provider<DoubleTapClassifier> doubleTapClassifierProvider;
        private Provider<DozeComponent.Builder> dozeComponentBuilderProvider;
        /* access modifiers changed from: private */
        public Provider<DozeLog> dozeLogProvider;
        private Provider<DozeLogger> dozeLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<DozeParameters> dozeParametersProvider;
        private Provider<DozeScrimController> dozeScrimControllerProvider;
        /* access modifiers changed from: private */
        public Provider<DozeServiceHost> dozeServiceHostProvider;
        private Provider<DozeService> dozeServiceProvider;
        private Provider<DreamOverlayComponent.Factory> dreamOverlayComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<DreamOverlayNotificationCountProvider> dreamOverlayNotificationCountProvider;
        private Provider<DreamOverlayService> dreamOverlayServiceProvider;
        /* access modifiers changed from: private */
        public Provider<DreamOverlayStateController> dreamOverlayStateControllerProvider;
        private Provider<DumpHandler> dumpHandlerProvider;
        private Provider<DynamicChildBindController> dynamicChildBindControllerProvider;
        private Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
        private Provider<EnhancedEstimatesImpl> enhancedEstimatesImplProvider;
        private Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        private Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        private Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        private Provider<FaceRecognitionController> faceRecognitionControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LightBarTransitionsController.Factory> factoryProvider;
        /* access modifiers changed from: private */
        public Provider<EdgeBackGestureHandler.Factory> factoryProvider2;
        private Provider<KeyguardBouncer.Factory> factoryProvider3;
        /* access modifiers changed from: private */
        public Provider<KeyguardMessageAreaController.Factory> factoryProvider4;
        private Provider<LockscreenShadeKeyguardTransitionController.Factory> factoryProvider5;
        private Provider<SplitShadeLockScreenOverScroller.Factory> factoryProvider6;
        private Provider<SingleShadeLockScreenOverScroller.Factory> factoryProvider7;
        /* access modifiers changed from: private */
        public Provider<BrightnessSliderController.Factory> factoryProvider8;
        private Provider<TileLifecycleManager.Factory> factoryProvider9;
        /* access modifiers changed from: private */
        public Provider falsingCollectorImplProvider;
        private Provider<FalsingDataProvider> falsingDataProvider;
        /* access modifiers changed from: private */
        public Provider<FalsingManagerProxy> falsingManagerProxyProvider;
        /* access modifiers changed from: private */
        public Provider<FeatureFlagsRelease> featureFlagsReleaseProvider;
        /* access modifiers changed from: private */
        public Provider<FgsManagerController> fgsManagerControllerProvider;
        private Provider<Files> filesProvider;
        private Provider<FlashlightControllerImpl> flashlightControllerImplProvider;
        private Provider<FlashlightTile> flashlightTileProvider;
        private Provider<ForegroundServiceController> foregroundServiceControllerProvider;
        private Provider<ForegroundServiceNotificationListener> foregroundServiceNotificationListenerProvider;
        private Provider<ForegroundServicesDialog> foregroundServicesDialogProvider;
        private Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;
        private Provider<FragmentService> fragmentServiceProvider;
        private Provider<GarbageMonitor> garbageMonitorProvider;
        private Provider<GlobalActionsComponent> globalActionsComponentProvider;
        /* access modifiers changed from: private */
        public Provider<GlobalActionsDialogLite> globalActionsDialogLiteProvider;
        private Provider<GlobalActionsImpl> globalActionsImplProvider;
        /* access modifiers changed from: private */
        public Provider globalSettingsImplProvider;
        private Provider<GlyphsControllerImpl> glyphsControllerImplProvider;
        private Provider<GlyphsTile> glyphsTileProvider;
        private Provider<HdmiCecSetMenuLanguageActivity> hdmiCecSetMenuLanguageActivityProvider;
        private Provider<HdmiCecSetMenuLanguageHelper> hdmiCecSetMenuLanguageHelperProvider;
        private Provider<HeadsUpControllerEx> headsUpControllerExProvider;
        private Provider<HeadsUpController> headsUpControllerProvider;
        private Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
        private Provider<HeadsUpViewBinderLogger> headsUpViewBinderLoggerProvider;
        private Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
        private Provider<HighPriorityProvider> highPriorityProvider;
        private Provider<HistoryTracker> historyTrackerProvider;
        private Provider<HomeSoundEffectController> homeSoundEffectControllerProvider;
        private Provider<HotspotControllerImpl> hotspotControllerImplProvider;
        private Provider<HotspotTile> hotspotTileProvider;
        private Provider<IconBuilder> iconBuilderProvider;
        private Provider<IconManager> iconManagerProvider;
        private Provider imageExporterProvider;
        private Provider imageTileSetProvider;
        private Provider<InitController> initControllerProvider;
        private Provider<InstantAppNotifier> instantAppNotifierProvider;
        private Provider<InternetDialogController> internetDialogControllerProvider;
        private Provider<InternetDialogFactory> internetDialogFactoryProvider;
        private Provider<InternetTile> internetTileProvider;
        /* access modifiers changed from: private */
        public Provider<Boolean> isPMLiteEnabledProvider;
        private Provider<Boolean> isReduceBrightColorsAvailableProvider;
        private Provider<KeyboardUI> keyboardUIProvider;
        private Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        private Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        private Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        private Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        private Provider<KeyguardIndicationControllerEx> keyguardIndicationControllerExProvider;
        private Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        private Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        private Provider keyguardNotificationVisibilityProviderImplProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        private Provider<KeyguardService> keyguardServiceProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        private Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        private Provider<KeyguardWeatherControllerImpl> keyguardWeatherControllerImplProvider;
        private Provider<LSShadeTransitionLogger> lSShadeTransitionLoggerProvider;
        private Provider<LaunchConversationActivity> launchConversationActivityProvider;
        private Provider<LeakReporter> leakReporterProvider;
        private Provider<LiftWakeGestureController> liftWakeGestureControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LightBarController> lightBarControllerProvider;
        private C4840LightBarTransitionsController_Factory lightBarTransitionsControllerProvider;
        private Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
        private Provider<LocationControllerImpl> locationControllerImplProvider;
        private Provider<LocationTile> locationTileProvider;
        private Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
        private C4835LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionControllerProvider;
        private Provider<LockscreenShadeScrimTransitionController> lockscreenShadeScrimTransitionControllerProvider;
        private Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LockscreenSmartspaceController> lockscreenSmartspaceControllerProvider;
        private Provider<LogBufferEulogizer> logBufferEulogizerProvider;
        private Provider<LogBufferFactory> logBufferFactoryProvider;
        private Provider<LogBufferFreezer> logBufferFreezerProvider;
        private Provider<LongScreenshotActivity> longScreenshotActivityProvider;
        private Provider<LongScreenshotData> longScreenshotDataProvider;
        private Provider<LowPriorityInflationHelper> lowPriorityInflationHelperProvider;
        private Provider<ManagedProfileControllerImpl> managedProfileControllerImplProvider;
        private Provider<Map<Class<?>, Provider<Activity>>> mapOfClassOfAndProviderOfActivityProvider;
        private Provider<Map<Class<?>, Provider<BroadcastReceiver>>> mapOfClassOfAndProviderOfBroadcastReceiverProvider;
        private Provider<Map<Class<?>, Provider<CoreStartable>>> mapOfClassOfAndProviderOfCoreStartableProvider;
        private Provider<Map<Class<?>, Provider<RecentsImplementation>>> mapOfClassOfAndProviderOfRecentsImplementationProvider;
        private Provider<Map<Class<?>, Provider<Service>>> mapOfClassOfAndProviderOfServiceProvider;
        private Provider<MediaArtworkProcessor> mediaArtworkProcessorProvider;
        private Provider<MediaBrowserFactory> mediaBrowserFactoryProvider;
        private Provider<MediaCarouselControllerLogger> mediaCarouselControllerLoggerProvider;
        private Provider<MediaCarouselController> mediaCarouselControllerProvider;
        private Provider<MediaContainerController> mediaContainerControllerProvider;
        private Provider<MediaControlPanel> mediaControlPanelProvider;
        private Provider<MediaControllerFactory> mediaControllerFactoryProvider;
        private Provider<MediaDataFilter> mediaDataFilterProvider;
        private Provider<MediaDataManager> mediaDataManagerProvider;
        private Provider<MediaDeviceManager> mediaDeviceManagerProvider;
        private Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
        private Provider<MediaFlags> mediaFlagsProvider;
        private Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
        private Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
        private Provider<MediaMuteAwaitConnectionCli> mediaMuteAwaitConnectionCliProvider;
        private Provider<MediaMuteAwaitConnectionManagerFactory> mediaMuteAwaitConnectionManagerFactoryProvider;
        private Provider<MediaMuteAwaitLogger> mediaMuteAwaitLoggerProvider;
        private Provider<MediaOutputBroadcastDialogFactory> mediaOutputBroadcastDialogFactoryProvider;
        private Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
        private Provider<MediaOutputDialogReceiver> mediaOutputDialogReceiverProvider;
        private Provider<MediaProjectionPrivacyItemMonitor> mediaProjectionPrivacyItemMonitorProvider;
        private Provider<MediaResumeListener> mediaResumeListenerProvider;
        private Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
        private Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
        private Provider<MediaTimeoutLogger> mediaTimeoutLoggerProvider;
        private Provider<MediaTttChipControllerReceiver> mediaTttChipControllerReceiverProvider;
        private Provider<MediaTttChipControllerSender> mediaTttChipControllerSenderProvider;
        private Provider<MediaTttCommandLineHelper> mediaTttCommandLineHelperProvider;
        private Provider<MediaTttFlags> mediaTttFlagsProvider;
        private Provider<MediaTttReceiverUiEventLogger> mediaTttReceiverUiEventLoggerProvider;
        private Provider<MediaTttSenderUiEventLogger> mediaTttSenderUiEventLoggerProvider;
        private Provider<MediaUiEventLogger> mediaUiEventLoggerProvider;
        private Provider<MediaViewController> mediaViewControllerProvider;
        private Provider<MediaViewLogger> mediaViewLoggerProvider;
        private Provider<GarbageMonitor.MemoryTile> memoryTileProvider;
        private Provider<MicrophoneToggleTile> microphoneToggleTileProvider;
        private Provider<ModeSwitchesController> modeSwitchesControllerProvider;
        private Provider<NTColorController> nTColorControllerProvider;
        private Provider<NTDependencyEx> nTDependencyExProvider;
        private Provider<NTGameModeHelper> nTGameModeHelperProvider;
        private Provider<NTLightweightHeadsupManager> nTLightweightHeadsupManagerProvider;
        private Provider<Set<FalsingClassifier>> namedSetOfFalsingClassifierProvider;
        /* access modifiers changed from: private */
        public Provider<NavBarHelper> navBarHelperProvider;
        private Provider<NavigationBarComponent.Factory> navigationBarComponentFactoryProvider;
        private Provider<NavigationBarController> navigationBarControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NavigationModeController> navigationModeControllerProvider;
        private Provider<NearbyMediaDevicesLogger> nearbyMediaDevicesLoggerProvider;
        private Provider<NearbyMediaDevicesManager> nearbyMediaDevicesManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NetworkControllerImpl> networkControllerImplProvider;
        private Provider<KeyguardViewMediator> newKeyguardViewMediatorProvider;
        /* access modifiers changed from: private */
        public Provider<NextAlarmControllerImpl> nextAlarmControllerImplProvider;
        private Provider<NfcControllerImpl> nfcControllerImplProvider;
        private Provider<NfcTile> nfcTileProvider;
        private Provider<NightDisplayTile> nightDisplayTileProvider;
        private Provider<NotifBindPipelineLogger> notifBindPipelineLoggerProvider;
        private Provider<NotifBindPipeline> notifBindPipelineProvider;
        private Provider<NotifCollectionLogger> notifCollectionLoggerProvider;
        private Provider<NotifCollection> notifCollectionProvider;
        private Provider<NotifInflationErrorManager> notifInflationErrorManagerProvider;
        private Provider<NotifLiveDataStoreImpl> notifLiveDataStoreImplProvider;
        private Provider notifPipelineChoreographerImplProvider;
        private Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
        private Provider<NotifPipeline> notifPipelineProvider;
        private Provider<NotifRemoteViewCacheImpl> notifRemoteViewCacheImplProvider;
        private Provider<NotificationChannels> notificationChannelsProvider;
        private Provider<NotificationClickNotifier> notificationClickNotifierProvider;
        private Provider<NotificationContentInflater> notificationContentInflaterProvider;
        private Provider<NotificationEntryManagerLogger> notificationEntryManagerLoggerProvider;
        private Provider<NotificationFilter> notificationFilterProvider;
        private Provider<NotificationGroupAlertTransferHelper> notificationGroupAlertTransferHelperProvider;
        private Provider<NotificationGroupManagerLegacy> notificationGroupManagerLegacyProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
        private Provider<NotificationInteractionTracker> notificationInteractionTrackerProvider;
        private Provider<NotificationInterruptLogger> notificationInterruptLoggerProvider;
        private Provider<NotificationInterruptStateProviderImpl> notificationInterruptStateProviderImplProvider;
        private Provider<NotificationListener> notificationListenerProvider;
        private Provider<NotificationListenerWithPlugins> notificationListenerWithPluginsProvider;
        private Provider<NotificationLockscreenUserManagerImpl> notificationLockscreenUserManagerImplProvider;
        private Provider<NotificationPersonExtractorPluginBoundary> notificationPersonExtractorPluginBoundaryProvider;
        private Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
        private Provider<NotificationRowBinderImpl> notificationRowBinderImplProvider;
        private Provider<NotificationSectionsFeatureManager> notificationSectionsFeatureManagerProvider;
        private Provider<NotificationSectionsLogger> notificationSectionsLoggerProvider;
        private Provider<NotificationSectionsManager> notificationSectionsManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationShadeWindowControllerImpl> notificationShadeWindowControllerImplProvider;
        private Provider<NotificationVisibilityProviderImpl> notificationVisibilityProviderImplProvider;
        private Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        private Provider<OneHandedModeTile> oneHandedModeTileProvider;
        private Provider<Optional<AlternateUdfpsTouchProvider>> optionalOfAlternateUdfpsTouchProvider;
        private Provider<Optional<BcSmartspaceDataPlugin>> optionalOfBcSmartspaceDataPluginProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<CentralSurfaces>> optionalOfCentralSurfacesProvider;
        private Provider<Optional<ControlsFavoritePersistenceWrapper>> optionalOfControlsFavoritePersistenceWrapperProvider;
        private Provider<Optional<ControlsTileResourceConfiguration>> optionalOfControlsTileResourceConfigurationProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<Recents>> optionalOfRecentsProvider;
        private Provider<Optional<UdfpsHbmProvider>> optionalOfUdfpsHbmProvider;
        private Provider<OverviewProxyRecentsImpl> overviewProxyRecentsImplProvider;
        /* access modifiers changed from: private */
        public Provider<OverviewProxyService> overviewProxyServiceProvider;
        private Provider<PackageManagerAdapter> packageManagerAdapterProvider;
        private Provider panelEventsEmitterProvider;
        private Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
        private Provider<PendingRemovalStore> pendingRemovalStoreProvider;
        /* access modifiers changed from: private */
        public Provider<PeopleNotificationIdentifierImpl> peopleNotificationIdentifierImplProvider;
        private Provider<PeopleSpaceActivity> peopleSpaceActivityProvider;
        private Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
        private Provider<PeopleSpaceWidgetPinnedReceiver> peopleSpaceWidgetPinnedReceiverProvider;
        private Provider<PeopleSpaceWidgetProvider> peopleSpaceWidgetProvider;
        private Provider<PhoneStateMonitor> phoneStateMonitorProvider;
        private Provider pointerCountClassifierProvider;
        private Provider postureDependentProximitySensorProvider;
        private Provider<PowerNotificationWarnings> powerNotificationWarningsProvider;
        private Provider<PowerUI> powerUIProvider;
        private Provider<PrivacyConfig> privacyConfigProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyDialogController> privacyDialogControllerProvider;
        private Provider<PrivacyDotViewController> privacyDotViewControllerProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyItemController> privacyItemControllerProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyLogger> privacyLoggerProvider;
        private Provider<ProtoTracer> protoTracerProvider;
        private Provider<AccessPointControllerImpl> provideAccessPointControllerImplProvider;
        private Provider<ActivityLaunchAnimator> provideActivityLaunchAnimatorProvider;
        private Provider<ActivityManagerWrapper> provideActivityManagerWrapperProvider;
        /* access modifiers changed from: private */
        public Provider<ActivityStarter> provideActivityStarterProvider;
        /* access modifiers changed from: private */
        public Provider<Boolean> provideAllowNotificationLongPressProvider;
        private Provider<AssistUtils> provideAssistUtilsProvider;
        private Provider<DeviceStateRotationLockSettingsManager> provideAutoRotateSettingsManagerProvider;
        private Provider<AutoTileManager> provideAutoTileManagerProvider;
        private Provider<DelayableExecutor> provideBackgroundDelayableExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<Executor> provideBackgroundExecutorProvider;
        private Provider<RepeatableExecutor> provideBackgroundRepeatableExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<BatteryController> provideBatteryControllerProvider;
        /* access modifiers changed from: private */
        public Provider<Handler> provideBgHandlerProvider;
        /* access modifiers changed from: private */
        public Provider<Looper> provideBgLooperProvider;
        private Provider<LogBuffer> provideBroadcastDispatcherLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<BubblesManager>> provideBubblesManagerProvider;
        private Provider provideClockInfoListProvider;
        /* access modifiers changed from: private */
        public Provider<CommandQueue> provideCommandQueueProvider;
        private Provider<CommonNotifCollection> provideCommonNotifCollectionProvider;
        private Provider<DataSaverController> provideDataSaverControllerProvider;
        private Provider<DelayableExecutor> provideDelayableExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<DemoModeController> provideDemoModeControllerProvider;
        private Provider<DevicePolicyManagerWrapper> provideDevicePolicyManagerWrapperProvider;
        /* access modifiers changed from: private */
        public Provider<DialogLaunchAnimator> provideDialogLaunchAnimatorProvider;
        private Provider<LogBuffer> provideDozeLogBufferProvider;
        private Provider<Executor> provideExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<GroupExpansionManager> provideGroupExpansionManagerProvider;
        /* access modifiers changed from: private */
        public Provider<GroupMembershipManager> provideGroupMembershipManagerProvider;
        /* access modifiers changed from: private */
        public Provider<HeadsUpManagerPhone> provideHeadsUpManagerPhoneProvider;
        /* access modifiers changed from: private */
        public Provider<IndividualSensorPrivacyController> provideIndividualSensorPrivacyControllerProvider;
        private Provider<LogBuffer> provideLSShadeTransitionControllerBufferProvider;
        private Provider<String> provideLauncherPackageProvider;
        private Provider<String> provideLeakReportEmailProvider;
        private Provider<LocalBluetoothManager> provideLocalBluetoothControllerProvider;
        private Provider<LogcatEchoTracker> provideLogcatEchoTrackerProvider;
        private Provider<Executor> provideLongRunningExecutorProvider;
        private Provider<Looper> provideLongRunningLooperProvider;
        private Provider<LogBuffer> provideMediaBrowserBufferProvider;
        private Provider<LogBuffer> provideMediaCarouselControllerBufferProvider;
        private Provider<LogBuffer> provideMediaMuteAwaitLogBufferProvider;
        private Provider<LogBuffer> provideMediaTttReceiverLogBufferProvider;
        private Provider<LogBuffer> provideMediaTttSenderLogBufferProvider;
        private Provider<LogBuffer> provideMediaViewLogBufferProvider;
        private Provider<LogBuffer> provideNearbyMediaDevicesLogBufferProvider;
        private Provider<NightDisplayListener> provideNightDisplayListenerProvider;
        private Provider<LogBuffer> provideNotifInteractionLogBufferProvider;
        private Provider<NotifRemoteViewCache> provideNotifRemoteViewCacheProvider;
        private Provider<NotificationEntryManager> provideNotificationEntryManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationGutsManager> provideNotificationGutsManagerProvider;
        private Provider<LogBuffer> provideNotificationHeadsUpLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationLogger> provideNotificationLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationMediaManager> provideNotificationMediaManagerProvider;
        private Provider<NotificationPanelLogger> provideNotificationPanelLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationRemoteInputManager> provideNotificationRemoteInputManagerProvider;
        private Provider<LogBuffer> provideNotificationSectionLogBufferProvider;
        private Provider<NotificationViewHierarchyManager> provideNotificationViewHierarchyManagerProvider;
        private Provider<NotificationVisibilityProvider> provideNotificationVisibilityProvider;
        private Provider<LogBuffer> provideNotificationsLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        private Provider<ThresholdSensor[]> providePostureToProximitySensorMappingProvider;
        private Provider<ThresholdSensor[]> providePostureToSecondaryProximitySensorMappingProvider;
        private Provider<ThresholdSensor> providePrimaryProximitySensorProvider;
        private Provider<LogBuffer> providePrivacyLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<ProximityCheck> provideProximityCheckProvider;
        /* access modifiers changed from: private */
        public Provider<ProximitySensor> provideProximitySensorProvider;
        /* access modifiers changed from: private */
        public Provider<LogBuffer> provideQSFragmentDisableLogBufferProvider;
        private Provider<QuickAccessWalletClient> provideQuickAccessWalletClientProvider;
        private Provider<LogBuffer> provideQuickSettingsLogBufferProvider;
        private Provider<RecentsImplementation> provideRecentsImplProvider;
        private Provider<Recents> provideRecentsProvider;
        private Provider<ThresholdSensor> provideSecondaryProximitySensorProvider;
        private Provider<SensorPrivacyController> provideSensorPrivacyControllerProvider;
        /* access modifiers changed from: private */
        public Provider<SmartReplyController> provideSmartReplyControllerProvider;
        private Provider<Optional<SysUIUnfoldComponent>> provideSysUIUnfoldComponentProvider;
        /* access modifiers changed from: private */
        public Provider<SysUiState> provideSysUiStateProvider;
        private Provider<TaskStackChangeListeners> provideTaskStackChangeListenersProvider;
        private Provider<String> provideThemePickerPackageProvider;
        /* access modifiers changed from: private */
        public Provider<Handler> provideTimeTickHandlerProvider;
        private Provider<LogBuffer> provideToastLogBufferProvider;
        private Provider<TvNotificationHandler> provideTvNotificationHandlerProvider;
        /* access modifiers changed from: private */
        public Provider<UserTracker> provideUserTrackerProvider;
        private Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
        private Provider<VolumeDialog> provideVolumeDialogProvider;
        private Provider<SectionHeaderController> providesAlertingHeaderControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesAlertingHeaderSubcomponentProvider;
        private Provider<MessageRouter> providesBackgroundMessageRouterProvider;
        private Provider<Set<FalsingClassifier>> providesBrightLineGestureClassifiersProvider;
        private Provider<Boolean> providesControlsFeatureEnabledProvider;
        /* access modifiers changed from: private */
        public Provider<DeviceProvisionedController> providesDeviceProvisionedControllerProvider;
        private Provider<String[]> providesDeviceStateRotationLockDefaultsProvider;
        private Provider<Float> providesDoubleTapTouchSlopProvider;
        private Provider<SectionHeaderController> providesIncomingHeaderControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesIncomingHeaderSubcomponentProvider;
        private Provider<MediaHost> providesKeyguardMediaHostProvider;
        private Provider<LeakDetector> providesLeakDetectorProvider;
        private Provider<Optional<MediaMuteAwaitConnectionCli>> providesMediaMuteAwaitConnectionCliProvider;
        private Provider<LogBuffer> providesMediaTimeoutListenerLogBufferProvider;
        private Provider<Optional<MediaTttChipControllerReceiver>> providesMediaTttChipControllerReceiverProvider;
        private Provider<Optional<MediaTttChipControllerSender>> providesMediaTttChipControllerSenderProvider;
        private Provider<Optional<MediaTttCommandLineHelper>> providesMediaTttCommandLineHelperProvider;
        private Provider<MediaTttLogger> providesMediaTttReceiverLoggerProvider;
        private Provider<MediaTttLogger> providesMediaTttSenderLoggerProvider;
        private Provider<Optional<NearbyMediaDevicesManager>> providesNearbyMediaDevicesManagerProvider;
        private Provider<SectionHeaderController> providesPeopleHeaderControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesPeopleHeaderSubcomponentProvider;
        private Provider<Executor> providesPluginExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<MediaHost> providesQSMediaHostProvider;
        /* access modifiers changed from: private */
        public Provider<MediaHost> providesQuickQSMediaHostProvider;
        private Provider<SectionHeaderController> providesSilentHeaderControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesSilentHeaderSubcomponentProvider;
        private Provider<Float> providesSingleTapTouchSlopProvider;
        private Provider<StatusBarWindowView> providesStatusBarWindowViewProvider;
        /* access modifiers changed from: private */
        public Provider<ViewMediatorCallback> providesViewMediatorCallbackProvider;
        private Provider proximityClassifierProvider;
        private Provider proximitySensorImplProvider;
        private Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
        private Provider<QRCodeScannerController> qRCodeScannerControllerProvider;
        private Provider<QRCodeScannerTile> qRCodeScannerTileProvider;
        private Provider<QSFactoryImpl> qSFactoryImplProvider;
        /* access modifiers changed from: private */
        public Provider<QSLogger> qSLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<QSTileHost> qSTileHostProvider;
        private Provider<QuickAccessWalletController> quickAccessWalletControllerProvider;
        private Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
        private Provider<RecordingController> recordingControllerProvider;
        private Provider<RecordingService> recordingServiceProvider;
        private Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
        private Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
        private Provider<RemoteInputNotificationRebuilder> remoteInputNotificationRebuilderProvider;
        /* access modifiers changed from: private */
        public Provider<RemoteInputQuickSettingsDisabler> remoteInputQuickSettingsDisablerProvider;
        private Provider<RemoteInputUriController> remoteInputUriControllerProvider;
        private Provider<RenderStageManager> renderStageManagerProvider;
        private Provider<ResumeMediaBrowserFactory> resumeMediaBrowserFactoryProvider;
        private Provider<ResumeMediaBrowserLogger> resumeMediaBrowserLoggerProvider;
        private Provider<RingerModeTrackerImpl> ringerModeTrackerImplProvider;
        private Provider<RingtonePlayer> ringtonePlayerProvider;
        private Provider<RotationLockControllerImpl> rotationLockControllerImplProvider;
        private Provider<RotationLockTile> rotationLockTileProvider;
        private Provider<RotationPolicyWrapperImpl> rotationPolicyWrapperImplProvider;
        private Provider<RowContentBindStageLogger> rowContentBindStageLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<RowContentBindStage> rowContentBindStageProvider;
        private Provider<SafetyController> safetyControllerProvider;
        /* access modifiers changed from: private */
        public Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
        private Provider<ScreenOnCoordinator> screenOnCoordinatorProvider;
        private Provider<ScreenRecordTile> screenRecordTileProvider;
        private Provider<ScreenshotController> screenshotControllerProvider;
        private Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
        private Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
        private Provider<ScrimController> scrimControllerProvider;
        private Provider<ScrollCaptureClient> scrollCaptureClientProvider;
        private Provider<ScrollCaptureController> scrollCaptureControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent.Builder> sectionHeaderControllerSubcomponentBuilderProvider;
        /* access modifiers changed from: private */
        public Provider secureSettingsImplProvider;
        /* access modifiers changed from: private */
        public Provider<SecurityControllerImpl> securityControllerImplProvider;
        private Provider<SeekBarViewModel> seekBarViewModelProvider;
        private Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        /* access modifiers changed from: private */
        public Provider<SessionTracker> sessionTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<BackAnimation>> setBackAnimationProvider;
        private Provider<Optional<Bubbles>> setBubblesProvider;
        private Provider<Optional<CompatUI>> setCompatUIProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<DisplayAreaHelper>> setDisplayAreaHelperProvider;
        private Provider<Optional<DragAndDrop>> setDragAndDropProvider;
        private Provider<Optional<HideDisplayCutout>> setHideDisplayCutoutProvider;
        private Provider<Set<PrivacyItemMonitor>> setOfPrivacyItemMonitorProvider;
        private Provider<Optional<OneHanded>> setOneHandedProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<Pip>> setPipProvider;
        private Provider<Optional<RecentTasks>> setRecentTasksProvider;
        private Provider<Optional<ShellCommandHandler>> setShellCommandHandlerProvider;
        private Provider<Optional<SplitScreen>> setSplitScreenProvider;
        private Provider<Optional<StartingSurface>> setStartingSurfaceProvider;
        private Provider<Optional<TaskViewFactory>> setTaskViewFactoryProvider;
        private Provider<ShellTransitions> setTransitionsProvider;
        /* access modifiers changed from: private */
        public Provider<ShadeControllerImpl> shadeControllerImplProvider;
        private Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        private Provider<ShadeListBuilder> shadeListBuilderProvider;
        private Provider<ShortcutKeyDispatcher> shortcutKeyDispatcherProvider;
        private Provider<SidefpsController> sidefpsControllerProvider;
        private C4836SingleShadeLockScreenOverScroller_Factory singleShadeLockScreenOverScrollerProvider;
        private Provider<SingleTapClassifier> singleTapClassifierProvider;
        private Provider<SliceBroadcastRelayHandler> sliceBroadcastRelayHandlerProvider;
        private Provider<SmartActionInflaterImpl> smartActionInflaterImplProvider;
        private Provider<SmartActionsReceiver> smartActionsReceiverProvider;
        /* access modifiers changed from: private */
        public Provider<SmartReplyConstants> smartReplyConstantsProvider;
        private Provider<SmartReplyInflaterImpl> smartReplyInflaterImplProvider;
        private Provider<SmartReplyStateInflaterImpl> smartReplyStateInflaterImplProvider;
        private C4837SplitShadeLockScreenOverScroller_Factory splitShadeLockScreenOverScrollerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarIconControllerImpl> statusBarIconControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
        private Provider<StatusBarRemoteInputCallback> statusBarRemoteInputCallbackProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
        private Provider<StatusBarWindowController> statusBarWindowControllerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
        private Provider<StorageNotification> storageNotificationProvider;
        /* access modifiers changed from: private */
        public Provider<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> subscriptionManagerSlotIndexResolverProvider;
        private Provider<SysUIUnfoldComponent.Factory> sysUIUnfoldComponentFactoryProvider;
        private Provider<SystemActions> systemActionsProvider;
        private Provider<SystemEventChipAnimationController> systemEventChipAnimationControllerProvider;
        private Provider<SystemEventCoordinator> systemEventCoordinatorProvider;
        private Provider<SystemPropertiesHelper> systemPropertiesHelperProvider;
        private Provider<SystemStatusAnimationScheduler> systemStatusAnimationSchedulerProvider;
        private Provider<SystemUIAuxiliaryDumpService> systemUIAuxiliaryDumpServiceProvider;
        private Provider<SystemUIDialogManager> systemUIDialogManagerProvider;
        private Provider<SystemUIService> systemUIServiceProvider;
        /* access modifiers changed from: private */
        public Provider<SysuiColorExtractor> sysuiColorExtractorProvider;
        private Provider<TakeScreenshotService> takeScreenshotServiceProvider;
        private Provider<TapGestureDetector> tapGestureDetectorProvider;
        private Provider<TaskbarDelegate> taskbarDelegateProvider;
        /* access modifiers changed from: private */
        public Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
        private Provider<TemperatureController> temperatureControllerProvider;
        private Provider<TeslaInfoControllerImpl> teslaInfoControllerImplProvider;
        private Provider<ThemeOverlayApplier> themeOverlayApplierProvider;
        private Provider<ThemeOverlayController> themeOverlayControllerProvider;
        private C4834TileLifecycleManager_Factory tileLifecycleManagerProvider;
        private Provider<TileServices> tileServicesProvider;
        private Provider<TimeoutHandler> timeoutHandlerProvider;
        private Provider<ToastFactory> toastFactoryProvider;
        private Provider<ToastLogger> toastLoggerProvider;
        private Provider<ToastUI> toastUIProvider;
        private Provider<TunablePadding.TunablePaddingService> tunablePaddingServiceProvider;
        private Provider<TunerActivity> tunerActivityProvider;
        /* access modifiers changed from: private */
        public Provider<TunerServiceImpl> tunerServiceImplProvider;
        /* access modifiers changed from: private */
        public final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        private Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
        private Provider<TvNotificationPanel> tvNotificationPanelProvider;
        private Provider<TvOngoingPrivacyChip> tvOngoingPrivacyChipProvider;
        private Provider<TvStatusBar> tvStatusBarProvider;
        /* access modifiers changed from: private */
        public final TvSysUIComponentImpl tvSysUIComponentImpl;
        private Provider<TvUnblockSensorActivity> tvUnblockSensorActivityProvider;
        private Provider<TypeClassifier> typeClassifierProvider;
        /* access modifiers changed from: private */
        public Provider<UdfpsController> udfpsControllerProvider;
        private Provider<UdfpsHapticsSimulator> udfpsHapticsSimulatorProvider;
        private Provider<UdfpsShell> udfpsShellProvider;
        private Provider<UiModeNightTile> uiModeNightTileProvider;
        private Provider<UiOffloadThread> uiOffloadThreadProvider;
        private Provider<UnfoldLatencyTracker> unfoldLatencyTrackerProvider;
        private Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
        private Provider<UsbConfirmActivity> usbConfirmActivityProvider;
        private Provider<UsbDebuggingActivity> usbDebuggingActivityProvider;
        private Provider<UsbDebuggingSecondaryUserActivity> usbDebuggingSecondaryUserActivityProvider;
        private Provider<UsbPermissionActivity> usbPermissionActivityProvider;
        private Provider<UserCreator> userCreatorProvider;
        /* access modifiers changed from: private */
        public Provider<UserInfoControllerImpl> userInfoControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<UserSwitchDialogController> userSwitchDialogControllerProvider;
        private Provider<UserSwitcherActivity> userSwitcherActivityProvider;
        /* access modifiers changed from: private */
        public Provider<UserSwitcherController> userSwitcherControllerProvider;
        private Provider<VibratorHelper> vibratorHelperProvider;
        private Provider<ViewUtil> viewUtilProvider;
        private Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
        private Provider<VisualStabilityProvider> visualStabilityProvider;
        private Provider<VolumeDialogComponent> volumeDialogComponentProvider;
        private Provider<VolumeDialogControllerImpl> volumeDialogControllerImplProvider;
        private Provider<VolumeUI> volumeUIProvider;
        private Provider<VpnStatusObserver> vpnStatusObserverProvider;
        private Provider<WMShell> wMShellProvider;
        /* access modifiers changed from: private */
        public Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;
        private Provider<WalletActivity> walletActivityProvider;
        private Provider<WalletControllerImpl> walletControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<WallpaperController> wallpaperControllerProvider;
        private Provider<AccessPointControllerImpl.WifiPickerTrackerFactory> wifiPickerTrackerFactoryProvider;
        private Provider<WifiStateWorker> wifiStateWorkerProvider;
        private Provider<WifiStatusTrackerFactory> wifiStatusTrackerFactoryProvider;
        private Provider<WifiTile> wifiTileProvider;
        private Provider<WindowMagnification> windowMagnificationProvider;
        private Provider<WorkLockActivity> workLockActivityProvider;
        private Provider<WorkModeTile> workModeTileProvider;
        /* access modifiers changed from: private */
        public Provider<ZenModeControllerImpl> zenModeControllerImplProvider;
        private Provider zigZagClassifierProvider;

        private TvSysUIComponentImpl(TvGlobalRootComponentImpl tvGlobalRootComponentImpl2, LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            LeakModule leakModule2 = leakModule;
            this.tvSysUIComponentImpl = this;
            this.tvGlobalRootComponentImpl = tvGlobalRootComponentImpl2;
            initialize(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize2(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize3(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize4(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize5(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize6(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
            initialize7(leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, optional, optional2, optional3, optional4, optional5, optional6, optional7, optional8, optional9, shellTransitions, optional10, optional11, optional12, optional13, optional14, optional15, optional16);
        }

        /* access modifiers changed from: private */
        public Object secureSettingsImpl() {
            return SecureSettingsImpl_Factory.newInstance((ContentResolver) this.tvGlobalRootComponentImpl.provideContentResolverProvider.get());
        }

        private void initialize(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.bootCompleteCacheImplProvider = DoubleCheck.provider(BootCompleteCacheImpl_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.configurationControllerImplProvider = DoubleCheck.provider(ConfigurationControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.globalSettingsImplProvider = GlobalSettingsImpl_Factory.create(this.tvGlobalRootComponentImpl.provideContentResolverProvider);
            this.provideDemoModeControllerProvider = DoubleCheck.provider(DemoModeModule_ProvideDemoModeControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.globalSettingsImplProvider));
            this.providesLeakDetectorProvider = DoubleCheck.provider(LeakModule_ProvidesLeakDetectorFactory.create(leakModule, this.tvGlobalRootComponentImpl.dumpManagerProvider, TrackedCollections_Factory.create()));
            Provider<Looper> provider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBgLooperFactory.create());
            this.provideBgLooperProvider = provider;
            this.provideBgHandlerProvider = SysUIConcurrencyModule_ProvideBgHandlerFactory.create(provider);
            this.provideUserTrackerProvider = DoubleCheck.provider(SettingsModule_ProvideUserTrackerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideBgHandlerProvider));
            Provider<TunerServiceImpl> provider2 = DoubleCheck.provider(TunerServiceImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.providesLeakDetectorProvider, this.provideDemoModeControllerProvider, this.provideUserTrackerProvider));
            this.tunerServiceImplProvider = provider2;
            this.tunerActivityProvider = TunerActivity_Factory.create(this.provideDemoModeControllerProvider, provider2);
            this.foregroundServicesDialogProvider = ForegroundServicesDialog_Factory.create(this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider);
            this.provideBackgroundExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundExecutorFactory.create(this.provideBgLooperProvider));
            this.provideLogcatEchoTrackerProvider = DoubleCheck.provider(LogModule_ProvideLogcatEchoTrackerFactory.create(this.tvGlobalRootComponentImpl.provideContentResolverProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            Provider<LogBufferFactory> provider3 = DoubleCheck.provider(LogBufferFactory_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideLogcatEchoTrackerProvider));
            this.logBufferFactoryProvider = provider3;
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideBroadcastDispatcherLogBufferFactory.create(provider3));
            this.provideBroadcastDispatcherLogBufferProvider = provider4;
            BroadcastDispatcherLogger_Factory create = BroadcastDispatcherLogger_Factory.create(provider4);
            this.broadcastDispatcherLoggerProvider = create;
            this.pendingRemovalStoreProvider = PendingRemovalStore_Factory.create(create);
            Provider<BroadcastDispatcher> provider5 = DoubleCheck.provider(BroadcastDispatcher_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.broadcastDispatcherLoggerProvider, this.provideUserTrackerProvider, this.pendingRemovalStoreProvider));
            this.broadcastDispatcherProvider = provider5;
            this.workLockActivityProvider = WorkLockActivity_Factory.create(provider5, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider);
            this.deviceConfigProxyProvider = DoubleCheck.provider(DeviceConfigProxy_Factory.create());
            this.enhancedEstimatesImplProvider = DoubleCheck.provider(EnhancedEstimatesImpl_Factory.create());
            this.provideBatteryControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideBatteryControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.enhancedEstimatesImplProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.broadcastDispatcherProvider, this.provideDemoModeControllerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.dockManagerImplProvider = DoubleCheck.provider(DockManagerImpl_Factory.create());
            Provider<FalsingDataProvider> provider6 = DoubleCheck.provider(FalsingDataProvider_Factory.create(this.tvGlobalRootComponentImpl.provideDisplayMetricsProvider, this.provideBatteryControllerProvider, this.dockManagerImplProvider));
            this.falsingDataProvider = provider6;
            DistanceClassifier_Factory create2 = DistanceClassifier_Factory.create(provider6, this.deviceConfigProxyProvider);
            this.distanceClassifierProvider = create2;
            this.proximityClassifierProvider = ProximityClassifier_Factory.create(create2, this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.pointerCountClassifierProvider = PointerCountClassifier_Factory.create(this.falsingDataProvider);
            this.typeClassifierProvider = TypeClassifier_Factory.create(this.falsingDataProvider);
            this.diagonalClassifierProvider = DiagonalClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            ZigZagClassifier_Factory create3 = ZigZagClassifier_Factory.create(this.falsingDataProvider, this.deviceConfigProxyProvider);
            this.zigZagClassifierProvider = create3;
            this.providesBrightLineGestureClassifiersProvider = FalsingModule_ProvidesBrightLineGestureClassifiersFactory.create(this.distanceClassifierProvider, this.proximityClassifierProvider, this.pointerCountClassifierProvider, this.typeClassifierProvider, this.diagonalClassifierProvider, create3);
            this.namedSetOfFalsingClassifierProvider = SetFactory.builder(0, 1).addCollectionProvider(this.providesBrightLineGestureClassifiersProvider).build();
            FalsingModule_ProvidesSingleTapTouchSlopFactory create4 = FalsingModule_ProvidesSingleTapTouchSlopFactory.create(this.tvGlobalRootComponentImpl.provideViewConfigurationProvider);
            this.providesSingleTapTouchSlopProvider = create4;
            this.singleTapClassifierProvider = SingleTapClassifier_Factory.create(this.falsingDataProvider, create4);
            FalsingModule_ProvidesDoubleTapTouchSlopFactory create5 = FalsingModule_ProvidesDoubleTapTouchSlopFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.providesDoubleTapTouchSlopProvider = create5;
            this.doubleTapClassifierProvider = DoubleTapClassifier_Factory.create(this.falsingDataProvider, this.singleTapClassifierProvider, create5, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create());
            Provider<SystemClock> provider7 = DoubleCheck.provider(SystemClockImpl_Factory.create());
            this.bindSystemClockProvider = provider7;
            this.historyTrackerProvider = DoubleCheck.provider(HistoryTracker_Factory.create(provider7));
            this.statusBarStateControllerImplProvider = DoubleCheck.provider(StatusBarStateControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.provideInteractionJankMonitorProvider));
            this.protoTracerProvider = DoubleCheck.provider(ProtoTracer_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.commandRegistryProvider = DoubleCheck.provider(CommandRegistry_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.provideCommandQueueProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideCommandQueueFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.protoTracerProvider, this.commandRegistryProvider));
            this.panelExpansionStateManagerProvider = DoubleCheck.provider(PanelExpansionStateManager_Factory.create());
            this.falsingManagerProxyProvider = new DelegateFactory();
            this.keyguardUpdateMonitorProvider = new DelegateFactory();
            this.asyncSensorManagerProvider = DoubleCheck.provider(AsyncSensorManager_Factory.create(this.tvGlobalRootComponentImpl.providesSensorManagerProvider, ThreadFactoryImpl_Factory.create(), this.tvGlobalRootComponentImpl.providesPluginManagerProvider));
            ThresholdSensorImpl_BuilderFactory_Factory create6 = ThresholdSensorImpl_BuilderFactory_Factory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider, this.asyncSensorManagerProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider);
            this.builderFactoryProvider = create6;
            this.providePostureToProximitySensorMappingProvider = SensorModule_ProvidePostureToProximitySensorMappingFactory.create(create6, this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.providePostureToSecondaryProximitySensorMappingProvider = C3266xe478f0bc.create(this.builderFactoryProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.devicePostureControllerImplProvider = DoubleCheck.provider(DevicePostureControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideDeviceStateManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.postureDependentProximitySensorProvider = PostureDependentProximitySensor_Factory.create(this.providePostureToProximitySensorMappingProvider, this.providePostureToSecondaryProximitySensorMappingProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider, this.devicePostureControllerImplProvider);
            this.builderProvider = ThresholdSensorImpl_Builder_Factory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider, this.asyncSensorManagerProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider);
            this.providePrimaryProximitySensorProvider = SensorModule_ProvidePrimaryProximitySensorFactory.create(this.tvGlobalRootComponentImpl.providesSensorManagerProvider, this.builderProvider);
            SensorModule_ProvideSecondaryProximitySensorFactory create7 = SensorModule_ProvideSecondaryProximitySensorFactory.create(this.builderProvider);
            this.provideSecondaryProximitySensorProvider = create7;
            this.proximitySensorImplProvider = ProximitySensorImpl_Factory.create(this.providePrimaryProximitySensorProvider, create7, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider);
            this.provideProximitySensorProvider = SensorModule_ProvideProximitySensorFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider, this.postureDependentProximitySensorProvider, this.proximitySensorImplProvider);
            DelegateFactory delegateFactory = new DelegateFactory();
            this.keyguardStateControllerImplProvider = delegateFactory;
            this.falsingCollectorImplProvider = DoubleCheck.provider(FalsingCollectorImpl_Factory.create(this.falsingDataProvider, this.falsingManagerProxyProvider, this.keyguardUpdateMonitorProvider, this.historyTrackerProvider, this.provideProximitySensorProvider, this.statusBarStateControllerImplProvider, delegateFactory, this.provideBatteryControllerProvider, this.dockManagerImplProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            this.statusBarKeyguardViewManagerProvider = new DelegateFactory();
            this.dismissCallbackRegistryProvider = DoubleCheck.provider(DismissCallbackRegistry_Factory.create(this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider));
            SecureSettingsImpl_Factory create8 = SecureSettingsImpl_Factory.create(this.tvGlobalRootComponentImpl.provideContentResolverProvider);
            this.secureSettingsImplProvider = create8;
            Provider<DeviceProvisionedControllerImpl> provider8 = DoubleCheck.provider(DeviceProvisionedControllerImpl_Factory.create(create8, this.globalSettingsImplProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideBgHandlerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.deviceProvisionedControllerImplProvider = provider8;
            this.providesDeviceProvisionedControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvidesDeviceProvisionedControllerFactory.create(provider8));
            Provider<Optional<CentralSurfaces>> access$1900 = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.optionalOfCentralSurfacesProvider = access$1900;
            Provider<ActivityStarterDelegate> provider9 = DoubleCheck.provider(ActivityStarterDelegate_Factory.create(access$1900));
            this.activityStarterDelegateProvider = provider9;
            this.provideActivityStarterProvider = PluginModule_ProvideActivityStarterFactory.create(provider9, this.tvGlobalRootComponentImpl.pluginDependencyProvider);
            this.builderProvider2 = WakeLock_Builder_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.broadcastSenderProvider = DoubleCheck.provider(BroadcastSender_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.builderProvider2, this.provideBackgroundExecutorProvider));
            this.telephonyListenerManagerProvider = DoubleCheck.provider(TelephonyListenerManager_Factory.create(this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, TelephonyCallback_Factory.create()));
            Provider<Looper> provider10 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningLooperFactory.create());
            this.provideLongRunningLooperProvider = provider10;
            this.provideLongRunningExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningExecutorFactory.create(provider10));
            this.provideDialogLaunchAnimatorProvider = DoubleCheck.provider(C2632x721ac0b6.create(this.tvGlobalRootComponentImpl.provideIDreamManagerProvider));
            this.userSwitcherControllerProvider = DoubleCheck.provider(UserSwitcherController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideIActivityManagerProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider, this.tvGlobalRootComponentImpl.provideDevicePolicyManagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider, this.provideBackgroundExecutorProvider, this.provideLongRunningExecutorProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvGlobalRootComponentImpl.provideInteractionJankMonitorProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideDialogLaunchAnimatorProvider));
            this.navigationModeControllerProvider = DoubleCheck.provider(NavigationModeController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.navigationBarControllerProvider = new DelegateFactory();
            this.alwaysOnDisplayPolicyProvider = DoubleCheck.provider(AlwaysOnDisplayPolicy_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.systemPropertiesHelperProvider = DoubleCheck.provider(SystemPropertiesHelper_Factory.create());
            this.featureFlagsReleaseProvider = DoubleCheck.provider(FeatureFlagsRelease_Factory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider, this.systemPropertiesHelperProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.sysUIUnfoldComponentFactoryProvider = new Provider<SysUIUnfoldComponent.Factory>() {
                public SysUIUnfoldComponent.Factory get() {
                    return new SysUIUnfoldComponentFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.provideSysUIUnfoldComponentProvider = DoubleCheck.provider(SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory.create(sysUIUnfoldModule, this.tvGlobalRootComponentImpl.unfoldTransitionProgressProvider, this.tvGlobalRootComponentImpl.provideNaturalRotationProgressProvider, this.tvGlobalRootComponentImpl.provideStatusBarScopedTransitionProvider, this.sysUIUnfoldComponentFactoryProvider));
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(WakefulnessLifecycle_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.newKeyguardViewMediatorProvider = new DelegateFactory();
            this.dozeParametersProvider = new DelegateFactory();
            Provider<UnlockedScreenOffAnimationController> provider11 = DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardStateControllerImplProvider, this.dozeParametersProvider, this.globalSettingsImplProvider, this.tvGlobalRootComponentImpl.provideInteractionJankMonitorProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
            this.unlockedScreenOffAnimationControllerProvider = provider11;
            this.screenOffAnimationControllerProvider = DoubleCheck.provider(ScreenOffAnimationController_Factory.create(this.provideSysUIUnfoldComponentProvider, provider11, this.wakefulnessLifecycleProvider));
            Provider<DozeParameters> provider12 = this.dozeParametersProvider;
            Provider<DozeParameters> provider13 = provider12;
            DelegateFactory.setDelegate(provider13, DoubleCheck.provider(DozeParameters_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgHandlerProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.tvGlobalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.alwaysOnDisplayPolicyProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.provideBatteryControllerProvider, this.tunerServiceImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.featureFlagsReleaseProvider, this.screenOffAnimationControllerProvider, this.provideSysUIUnfoldComponentProvider, this.unlockedScreenOffAnimationControllerProvider, this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider)));
            this.notifLiveDataStoreImplProvider = DoubleCheck.provider(NotifLiveDataStoreImpl_Factory.create(this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.notifPipelineFlagsProvider = NotifPipelineFlags_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.featureFlagsReleaseProvider);
            Provider<LogBuffer> provider14 = DoubleCheck.provider(LogModule_ProvideNotificationsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationsLogBufferProvider = provider14;
            this.notifCollectionLoggerProvider = NotifCollectionLogger_Factory.create(provider14);
            this.filesProvider = DoubleCheck.provider(Files_Factory.create());
            this.logBufferEulogizerProvider = DoubleCheck.provider(LogBufferEulogizer_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.bindSystemClockProvider, this.filesProvider));
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.notifPipelineFlagsProvider, this.notifCollectionLoggerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.logBufferEulogizerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.notifPipelineChoreographerImplProvider = DoubleCheck.provider(NotifPipelineChoreographerImpl_Factory.create(this.tvGlobalRootComponentImpl.providesChoreographerProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.notificationClickNotifierProvider = DoubleCheck.provider(NotificationClickNotifier_Factory.create(this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.notificationEntryManagerLoggerProvider = NotificationEntryManagerLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            Provider<ExtensionControllerImpl> provider15 = DoubleCheck.provider(ExtensionControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesLeakDetectorProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.tunerServiceImplProvider, this.configurationControllerImplProvider));
            this.extensionControllerImplProvider = provider15;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(NotificationPersonExtractorPluginBoundary_Factory.create(provider15));
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.notificationGroupManagerLegacyProvider = delegateFactory2;
            this.provideGroupMembershipManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupMembershipManagerFactory.create(this.notifPipelineFlagsProvider, delegateFactory2));
        }

        private void initialize2(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(PeopleNotificationIdentifierImpl_Factory.create(this.notificationPersonExtractorPluginBoundaryProvider, this.provideGroupMembershipManagerProvider));
            Factory<Bubbles> create = InstanceFactory.create(optional6);
            this.setBubblesProvider = create;
            DelegateFactory.setDelegate(this.notificationGroupManagerLegacyProvider, DoubleCheck.provider(NotificationGroupManagerLegacy_Factory.create(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create, this.tvGlobalRootComponentImpl.dumpManagerProvider)));
            this.notificationLockscreenUserManagerImplProvider = new DelegateFactory();
            this.provideNotificationVisibilityProvider = new DelegateFactory();
            this.provideSmartReplyControllerProvider = DoubleCheck.provider(C2637xc35bee5b.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideNotificationVisibilityProvider, this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider));
            this.provideNotificationEntryManagerProvider = new DelegateFactory();
            this.remoteInputNotificationRebuilderProvider = DoubleCheck.provider(RemoteInputNotificationRebuilder_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.remoteInputUriControllerProvider = DoubleCheck.provider(RemoteInputUriController_Factory.create(this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider));
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvideNotifInteractionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotifInteractionLogBufferProvider = provider;
            this.actionClickLoggerProvider = ActionClickLogger_Factory.create(provider);
            this.provideNotificationRemoteInputManagerProvider = DoubleCheck.provider(C2634xa11cf5e4.create(this.tvGlobalRootComponentImpl.contextProvider, this.notifPipelineFlagsProvider, this.notificationLockscreenUserManagerImplProvider, this.provideSmartReplyControllerProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.remoteInputNotificationRebuilderProvider, this.optionalOfCentralSurfacesProvider, this.statusBarStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.provideCommonNotifCollectionProvider = new DelegateFactory();
            NotifBindPipelineLogger_Factory create2 = NotifBindPipelineLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifBindPipelineLoggerProvider = create2;
            this.notifBindPipelineProvider = DoubleCheck.provider(NotifBindPipeline_Factory.create(this.provideCommonNotifCollectionProvider, create2, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            NotifRemoteViewCacheImpl_Factory create3 = NotifRemoteViewCacheImpl_Factory.create(this.provideCommonNotifCollectionProvider);
            this.notifRemoteViewCacheImplProvider = create3;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(create3);
            Provider<BindEventManagerImpl> provider2 = DoubleCheck.provider(BindEventManagerImpl_Factory.create());
            this.bindEventManagerImplProvider = provider2;
            this.conversationNotificationManagerProvider = DoubleCheck.provider(ConversationNotificationManager_Factory.create(provider2, this.notificationGroupManagerLegacyProvider, this.tvGlobalRootComponentImpl.contextProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineFlagsProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            this.conversationNotificationProcessorProvider = ConversationNotificationProcessor_Factory.create(this.tvGlobalRootComponentImpl.provideLauncherAppsProvider, this.conversationNotificationManagerProvider);
            this.mediaFeatureFlagProvider = MediaFeatureFlag_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.smartReplyConstantsProvider = DoubleCheck.provider(SmartReplyConstants_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.deviceConfigProxyProvider));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideActivityManagerWrapperFactory.create(sharedLibraryModule));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory.create(sharedLibraryModule));
            Provider<KeyguardDismissUtil> provider3 = DoubleCheck.provider(KeyguardDismissUtil_Factory.create());
            this.keyguardDismissUtilProvider = provider3;
            this.smartReplyInflaterImplProvider = SmartReplyInflaterImpl_Factory.create(this.smartReplyConstantsProvider, provider3, this.provideNotificationRemoteInputManagerProvider, this.provideSmartReplyControllerProvider, this.tvGlobalRootComponentImpl.contextProvider);
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideNotificationHeadsUpLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationHeadsUpLogBufferProvider = provider4;
            this.headsUpManagerLoggerProvider = HeadsUpManagerLogger_Factory.create(provider4);
            this.keyguardBypassControllerProvider = new DelegateFactory();
            this.visualStabilityProvider = DoubleCheck.provider(VisualStabilityProvider_Factory.create());
            Provider<HeadsUpManagerPhone> provider5 = DoubleCheck.provider(TvSystemUIModule_ProvideHeadsUpManagerPhoneFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.headsUpManagerLoggerProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.provideGroupMembershipManagerProvider, this.visualStabilityProvider, this.configurationControllerImplProvider));
            this.provideHeadsUpManagerPhoneProvider = provider5;
            this.smartActionInflaterImplProvider = SmartActionInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityStarterProvider, this.provideSmartReplyControllerProvider, provider5);
            SmartReplyStateInflaterImpl_Factory create4 = SmartReplyStateInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityManagerWrapperProvider, this.tvGlobalRootComponentImpl.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, this.smartActionInflaterImplProvider);
            this.smartReplyStateInflaterImplProvider = create4;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create4));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.create());
            RowContentBindStageLogger_Factory create5 = RowContentBindStageLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.rowContentBindStageLoggerProvider = create5;
            this.rowContentBindStageProvider = DoubleCheck.provider(RowContentBindStage_Factory.create(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, create5));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() {
                public ExpandableNotificationRowComponent.Builder get() {
                    return new ExpandableNotificationRowComponentBuilder(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.iconBuilderProvider = IconBuilder_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.iconManagerProvider = DoubleCheck.provider(IconManager_Factory.create(this.provideCommonNotifCollectionProvider, this.tvGlobalRootComponentImpl.provideLauncherAppsProvider, this.iconBuilderProvider));
            this.lowPriorityInflationHelperProvider = DoubleCheck.provider(LowPriorityInflationHelper_Factory.create(this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider, this.notifPipelineFlagsProvider));
            Provider<NotificationRowBinderImpl> provider6 = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, RowInflaterTask_Factory.create(), this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, this.lowPriorityInflationHelperProvider, this.notifPipelineFlagsProvider));
            this.notificationRowBinderImplProvider = provider6;
            DelegateFactory.setDelegate(this.provideNotificationEntryManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(this.notificationEntryManagerLoggerProvider, this.notificationGroupManagerLegacyProvider, this.notifPipelineFlagsProvider, provider6, this.provideNotificationRemoteInputManagerProvider, this.providesLeakDetectorProvider, this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.notifLiveDataStoreImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider)));
            this.notificationInteractionTrackerProvider = DoubleCheck.provider(NotificationInteractionTracker_Factory.create(this.notificationClickNotifierProvider, this.provideNotificationEntryManagerProvider));
            this.shadeListBuilderLoggerProvider = ShadeListBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeListBuilderProvider = DoubleCheck.provider(ShadeListBuilder_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, this.notifPipelineChoreographerImplProvider, this.notifPipelineFlagsProvider, this.notificationInteractionTrackerProvider, this.shadeListBuilderLoggerProvider, this.bindSystemClockProvider));
            Provider<RenderStageManager> provider7 = DoubleCheck.provider(RenderStageManager_Factory.create());
            this.renderStageManagerProvider = provider7;
            Provider<NotifPipeline> provider8 = DoubleCheck.provider(NotifPipeline_Factory.create(this.notifPipelineFlagsProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, provider7));
            this.notifPipelineProvider = provider8;
            DelegateFactory.setDelegate(this.provideCommonNotifCollectionProvider, DoubleCheck.provider(NotificationsModule_ProvideCommonNotifCollectionFactory.create(this.notifPipelineFlagsProvider, provider8, this.provideNotificationEntryManagerProvider)));
            NotificationVisibilityProviderImpl_Factory create6 = NotificationVisibilityProviderImpl_Factory.create(this.notifLiveDataStoreImplProvider, this.provideCommonNotifCollectionProvider);
            this.notificationVisibilityProviderImplProvider = create6;
            DelegateFactory.setDelegate(this.provideNotificationVisibilityProvider, DoubleCheck.provider(create6));
            DelegateFactory.setDelegate(this.notificationLockscreenUserManagerImplProvider, DoubleCheck.provider(NotificationLockscreenUserManagerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideDevicePolicyManagerProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.notificationClickNotifierProvider, this.tvGlobalRootComponentImpl.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.providesDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.secureSettingsImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider)));
            DelegateFactory.setDelegate(this.keyguardBypassControllerProvider, DoubleCheck.provider(KeyguardBypassController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider)));
            this.sysuiColorExtractorProvider = DoubleCheck.provider(SysuiColorExtractor_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.authControllerProvider = new DelegateFactory();
            this.notificationShadeWindowControllerImplProvider = DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.tvGlobalRootComponentImpl.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, this.sysuiColorExtractorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.screenOffAnimationControllerProvider, this.authControllerProvider));
            this.provideSysUiStateProvider = DoubleCheck.provider(SystemUIModule_ProvideSysUiStateFactory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.setPipProvider = InstanceFactory.create(optional);
            this.setSplitScreenProvider = InstanceFactory.create(optional3);
            this.setOneHandedProvider = InstanceFactory.create(optional5);
            this.setRecentTasksProvider = InstanceFactory.create(optional13);
            this.setBackAnimationProvider = InstanceFactory.create(optional16);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional10);
            this.setTransitionsProvider = InstanceFactory.create(shellTransitions);
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideDozeLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideDozeLogBufferProvider = provider9;
            this.dozeLoggerProvider = DozeLogger_Factory.create(provider9);
            Provider<DozeLog> provider10 = DoubleCheck.provider(DozeLog_Factory.create(this.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.dozeLoggerProvider));
            this.dozeLogProvider = provider10;
            this.dozeScrimControllerProvider = DoubleCheck.provider(DozeScrimController_Factory.create(this.dozeParametersProvider, provider10, this.statusBarStateControllerImplProvider));
            C4840LightBarTransitionsController_Factory create7 = C4840LightBarTransitionsController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider);
            this.lightBarTransitionsControllerProvider = create7;
            this.factoryProvider = LightBarTransitionsController_Factory_Impl.create(create7);
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(DarkIconDispatcherImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.factoryProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.lightBarControllerProvider = DoubleCheck.provider(C4839LightBarController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, this.navigationModeControllerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.builderProvider3 = DelayedWakeLock_Builder_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.keyguardUnlockAnimationControllerProvider = new DelegateFactory();
            this.scrimControllerProvider = DoubleCheck.provider(ScrimController_Factory.create(this.lightBarControllerProvider, this.dozeParametersProvider, this.tvGlobalRootComponentImpl.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, this.builderProvider3, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.screenOffAnimationControllerProvider, this.panelExpansionStateManagerProvider, this.keyguardUnlockAnimationControllerProvider, this.statusBarKeyguardViewManagerProvider));
            this.provideAssistUtilsProvider = DoubleCheck.provider(AssistModule_ProvideAssistUtilsFactory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.phoneStateMonitorProvider = DoubleCheck.provider(PhoneStateMonitor_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.bootCompleteCacheImplProvider, this.statusBarStateControllerImplProvider));
            this.overviewProxyServiceProvider = new DelegateFactory();
            this.assistLoggerProvider = DoubleCheck.provider(AssistLogger_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, this.phoneStateMonitorProvider));
            this.assistManagerProvider = new DelegateFactory();
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.assistLoggerProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.assistManagerProvider));
            DelegateFactory.setDelegate(this.assistManagerProvider, DoubleCheck.provider(AssistManager_Factory.create(this.providesDeviceProvisionedControllerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.provideAssistUtilsProvider, this.provideCommandQueueProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.defaultUiControllerProvider, this.assistLoggerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider)));
            this.shadeControllerImplProvider = DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.optionalOfCentralSurfacesProvider, this.assistManagerProvider));
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.create());
            this.mediaControllerFactoryProvider = MediaControllerFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            Provider<LogBuffer> provider11 = DoubleCheck.provider(LogModule_ProvidesMediaTimeoutListenerLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providesMediaTimeoutListenerLogBufferProvider = provider11;
            this.mediaTimeoutLoggerProvider = DoubleCheck.provider(MediaTimeoutLogger_Factory.create(provider11));
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(MediaTimeoutListener_Factory.create(this.mediaControllerFactoryProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaTimeoutLoggerProvider, this.statusBarStateControllerImplProvider, this.bindSystemClockProvider));
            this.mediaBrowserFactoryProvider = MediaBrowserFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            Provider<LogBuffer> provider12 = DoubleCheck.provider(LogModule_ProvideMediaBrowserBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaBrowserBufferProvider = provider12;
            this.resumeMediaBrowserLoggerProvider = DoubleCheck.provider(ResumeMediaBrowserLogger_Factory.create(provider12));
            this.resumeMediaBrowserFactoryProvider = ResumeMediaBrowserFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.mediaBrowserFactoryProvider, this.resumeMediaBrowserLoggerProvider);
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, this.resumeMediaBrowserFactoryProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.bindSystemClockProvider));
            this.mediaSessionBasedFilterProvider = MediaSessionBasedFilter_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMediaSessionManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider);
            this.provideLocalBluetoothControllerProvider = DoubleCheck.provider(SettingsLibraryModule_ProvideLocalBluetoothControllerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgHandlerProvider));
            this.localMediaManagerFactoryProvider = LocalMediaManagerFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideLocalBluetoothControllerProvider);
            this.mediaFlagsProvider = DoubleCheck.provider(MediaFlags_Factory.create(this.featureFlagsReleaseProvider));
            Provider<LogBuffer> provider13 = DoubleCheck.provider(LogModule_ProvideMediaMuteAwaitLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaMuteAwaitLogBufferProvider = provider13;
            this.mediaMuteAwaitLoggerProvider = DoubleCheck.provider(MediaMuteAwaitLogger_Factory.create(provider13));
            this.mediaMuteAwaitConnectionManagerFactoryProvider = DoubleCheck.provider(MediaMuteAwaitConnectionManagerFactory_Factory.create(this.mediaFlagsProvider, this.tvGlobalRootComponentImpl.contextProvider, this.mediaMuteAwaitLoggerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
        }

        private void initialize3(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.mediaControllerFactoryProvider, this.localMediaManagerFactoryProvider, this.tvGlobalRootComponentImpl.provideMediaRouter2ManagerProvider, this.mediaMuteAwaitConnectionManagerFactoryProvider, this.configurationControllerImplProvider, this.provideLocalBluetoothControllerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider);
            this.mediaUiEventLoggerProvider = DoubleCheck.provider(MediaUiEventLogger_Factory.create(this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            this.mediaDataFilterProvider = MediaDataFilter_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.notificationLockscreenUserManagerImplProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider);
            this.mediaDataManagerProvider = DoubleCheck.provider(MediaDataManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.broadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, MediaDataCombineLatest_Factory.create(), this.mediaDataFilterProvider, this.provideActivityStarterProvider, SmartspaceMediaDataProvider_Factory.create(), this.bindSystemClockProvider, this.tunerServiceImplProvider, this.mediaFlagsProvider, this.mediaUiEventLoggerProvider));
            this.provideNotificationMediaManagerProvider = DoubleCheck.provider(C2633xbcfb28e4.create(this.tvGlobalRootComponentImpl.contextProvider, this.optionalOfCentralSurfacesProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.notifPipelineFlagsProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.sessionTrackerProvider = DoubleCheck.provider(SessionTracker_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.authControllerProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.biometricUnlockControllerProvider = DoubleCheck.provider(BiometricUnlockController_Factory.create(this.dozeScrimControllerProvider, this.newKeyguardViewMediatorProvider, this.scrimControllerProvider, this.shadeControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.authControllerProvider, this.statusBarStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.sessionTrackerProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider, this.screenOffAnimationControllerProvider));
            DelegateFactory.setDelegate(this.keyguardUnlockAnimationControllerProvider, DoubleCheck.provider(KeyguardUnlockAnimationController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.featureFlagsReleaseProvider, this.biometricUnlockControllerProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider)));
            DelegateFactory.setDelegate(this.overviewProxyServiceProvider, DoubleCheck.provider(OverviewProxyService_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.navigationBarControllerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setRecentTasksProvider, this.setBackAnimationProvider, this.setStartingSurfaceProvider, this.broadcastDispatcherProvider, this.setTransitionsProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.keyguardUnlockAnimationControllerProvider, this.provideAssistUtilsProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider)));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(AccessibilityButtonModeObserver_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(AccessibilityButtonTargetsObserver_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.contextComponentResolverProvider = new DelegateFactory();
            this.provideRecentsImplProvider = RecentsModule_ProvideRecentsImplFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.contextComponentResolverProvider);
            Provider<Recents> provider = DoubleCheck.provider(TvSystemUIModule_ProvideRecentsFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideRecentsImplProvider, this.provideCommandQueueProvider));
            this.provideRecentsProvider = provider;
            this.optionalOfRecentsProvider = PresentJdkOptionalInstanceProvider.m1189of(provider);
            this.systemActionsProvider = DoubleCheck.provider(SystemActions_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.notificationShadeWindowControllerImplProvider, this.optionalOfCentralSurfacesProvider, this.optionalOfRecentsProvider));
            this.navBarHelperProvider = DoubleCheck.provider(NavBarHelper_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.systemActionsProvider, this.overviewProxyServiceProvider, this.assistManagerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.factoryProvider2 = EdgeBackGestureHandler_Factory_Factory.create(this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.protoTracerProvider, this.navigationModeControllerProvider, this.tvGlobalRootComponentImpl.provideViewConfigurationProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider);
            this.taskbarDelegateProvider = DoubleCheck.provider(TaskbarDelegate_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.factoryProvider2, this.factoryProvider));
            this.navigationBarComponentFactoryProvider = new Provider<NavigationBarComponent.Factory>() {
                public NavigationBarComponent.Factory get() {
                    return new NavigationBarComponentFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.autoHideControllerProvider = DoubleCheck.provider(C4838AutoHideController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider));
            Provider<NavigationBarController> provider2 = this.navigationBarControllerProvider;
            Provider<NavigationBarController> provider3 = provider2;
            DelegateFactory.setDelegate(provider3, DoubleCheck.provider(NavigationBarController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.provideSysUiStateProvider, this.provideCommandQueueProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.configurationControllerImplProvider, this.navBarHelperProvider, this.taskbarDelegateProvider, this.navigationBarComponentFactoryProvider, this.statusBarKeyguardViewManagerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.autoHideControllerProvider, this.lightBarControllerProvider, this.setPipProvider, this.setBackAnimationProvider)));
            this.keyguardStatusViewComponentFactoryProvider = new Provider<KeyguardStatusViewComponent.Factory>() {
                public KeyguardStatusViewComponent.Factory get() {
                    return new KeyguardStatusViewComponentFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.keyguardDisplayManagerProvider = KeyguardDisplayManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.navigationBarControllerProvider, this.keyguardStatusViewComponentFactoryProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider);
            this.blurUtilsProvider = DoubleCheck.provider(BlurUtils_Factory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider, this.tvGlobalRootComponentImpl.provideCrossWindowBlurListenersProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.wallpaperControllerProvider = DoubleCheck.provider(WallpaperController_Factory.create(this.tvGlobalRootComponentImpl.provideWallpaperManagerProvider));
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.providesChoreographerProvider, this.wallpaperControllerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.configurationControllerImplProvider));
            this.screenOnCoordinatorProvider = DoubleCheck.provider(ScreenOnCoordinator_Factory.create(this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.provideSysUIUnfoldComponentProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider));
            this.dreamOverlayStateControllerProvider = DoubleCheck.provider(DreamOverlayStateController_Factory.create(this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.provideActivityLaunchAnimatorProvider = DoubleCheck.provider(C2631x512569cf.create());
            DelegateFactory.setDelegate(this.newKeyguardViewMediatorProvider, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.falsingCollectorImplProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.tvGlobalRootComponentImpl.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.screenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider, this.screenOnCoordinatorProvider, this.tvGlobalRootComponentImpl.provideInteractionJankMonitorProvider, this.dreamOverlayStateControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideActivityLaunchAnimatorProvider)));
            this.providesViewMediatorCallbackProvider = KeyguardModule_ProvidesViewMediatorCallbackFactory.create(keyguardModule, this.newKeyguardViewMediatorProvider);
            this.keyguardSecurityModelProvider = DoubleCheck.provider(KeyguardSecurityModel_Factory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider));
            this.keyguardBouncerComponentFactoryProvider = new Provider<KeyguardBouncerComponent.Factory>() {
                public KeyguardBouncerComponent.Factory get() {
                    return new KeyguardBouncerComponentFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.factoryProvider3 = KeyguardBouncer_Factory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.keyguardSecurityModelProvider, this.keyguardBouncerComponentFactoryProvider);
            this.factoryProvider4 = KeyguardMessageAreaController_Factory_Factory.create(this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider);
            DelegateFactory.setDelegate(this.statusBarKeyguardViewManagerProvider, DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesViewMediatorCallbackProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dreamOverlayStateControllerProvider, this.navigationModeControllerProvider, this.dockManagerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideNotificationMediaManagerProvider, this.factoryProvider3, this.factoryProvider4, this.provideSysUIUnfoldComponentProvider, this.shadeControllerImplProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider)));
            this.provideLSShadeTransitionControllerBufferProvider = DoubleCheck.provider(LogModule_ProvideLSShadeTransitionControllerBufferFactory.create(this.logBufferFactoryProvider));
            Provider<LockscreenGestureLogger> provider4 = DoubleCheck.provider(LockscreenGestureLogger_Factory.create(this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider));
            this.lockscreenGestureLoggerProvider = provider4;
            this.lSShadeTransitionLoggerProvider = LSShadeTransitionLogger_Factory.create(this.provideLSShadeTransitionControllerBufferProvider, provider4, this.tvGlobalRootComponentImpl.provideDisplayMetricsProvider);
            this.mediaHostStatesManagerProvider = DoubleCheck.provider(MediaHostStatesManager_Factory.create());
            Provider<LogBuffer> provider5 = DoubleCheck.provider(LogModule_ProvideMediaViewLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaViewLogBufferProvider = provider5;
            this.mediaViewLoggerProvider = DoubleCheck.provider(MediaViewLogger_Factory.create(provider5));
            this.mediaViewControllerProvider = MediaViewController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.mediaHostStatesManagerProvider, this.mediaViewLoggerProvider);
            Provider<DelayableExecutor> provider6 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.provideBackgroundDelayableExecutorProvider = provider6;
            Provider<RepeatableExecutor> provider7 = DoubleCheck.provider(C3259xb8fd9db4.create(provider6));
            this.provideBackgroundRepeatableExecutorProvider = provider7;
            this.seekBarViewModelProvider = SeekBarViewModel_Factory.create(provider7);
            Provider<LogBuffer> provider8 = DoubleCheck.provider(LogModule_ProvideNearbyMediaDevicesLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNearbyMediaDevicesLogBufferProvider = provider8;
            Provider<NearbyMediaDevicesLogger> provider9 = DoubleCheck.provider(NearbyMediaDevicesLogger_Factory.create(provider8));
            this.nearbyMediaDevicesLoggerProvider = provider9;
            Provider<NearbyMediaDevicesManager> provider10 = DoubleCheck.provider(NearbyMediaDevicesManager_Factory.create(this.provideCommandQueueProvider, provider9));
            this.nearbyMediaDevicesManagerProvider = provider10;
            this.providesNearbyMediaDevicesManagerProvider = DoubleCheck.provider(MediaModule_ProvidesNearbyMediaDevicesManagerFactory.create(this.mediaFlagsProvider, provider10));
            this.mediaOutputDialogFactoryProvider = MediaOutputDialogFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.tvGlobalRootComponentImpl.providePowerExemptionManagerProvider);
            this.mediaCarouselControllerProvider = new DelegateFactory();
            this.activityIntentHelperProvider = DoubleCheck.provider(ActivityIntentHelper_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.broadcastDialogControllerProvider = DoubleCheck.provider(BroadcastDialogController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.mediaOutputDialogFactoryProvider));
            this.mediaControlPanelProvider = MediaControlPanel_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, this.mediaOutputDialogFactoryProvider, this.mediaCarouselControllerProvider, this.falsingManagerProxyProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider, this.keyguardStateControllerImplProvider, this.activityIntentHelperProvider, this.notificationLockscreenUserManagerImplProvider, this.broadcastDialogControllerProvider);
            Provider<LogBuffer> provider11 = DoubleCheck.provider(LogModule_ProvideMediaCarouselControllerBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaCarouselControllerBufferProvider = provider11;
            this.mediaCarouselControllerLoggerProvider = DoubleCheck.provider(MediaCarouselControllerLogger_Factory.create(provider11));
            DelegateFactory.setDelegate(this.mediaCarouselControllerProvider, DoubleCheck.provider(MediaCarouselController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.mediaControlPanelProvider, this.visualStabilityProvider, this.mediaHostStatesManagerProvider, this.provideActivityStarterProvider, this.bindSystemClockProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.configurationControllerImplProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.mediaUiEventLoggerProvider, this.mediaCarouselControllerLoggerProvider)));
            this.mediaHierarchyManagerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.configurationControllerImplProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider, this.dreamOverlayStateControllerProvider));
            Provider<MediaHost> provider12 = DoubleCheck.provider(MediaModule_ProvidesKeyguardMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider12;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider12, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.tvGlobalRootComponentImpl.contextProvider, this.configurationControllerImplProvider));
            this.notificationSectionsFeatureManagerProvider = DoubleCheck.provider(NotificationSectionsFeatureManager_Factory.create(this.deviceConfigProxyProvider, this.tvGlobalRootComponentImpl.contextProvider));
            Provider<LogBuffer> provider13 = DoubleCheck.provider(LogModule_ProvideNotificationSectionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationSectionLogBufferProvider = provider13;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(NotificationSectionsLogger_Factory.create(provider13));
            this.mediaContainerControllerProvider = DoubleCheck.provider(MediaContainerController_Factory.create(this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider));
            C32426 r1 = new Provider<SectionHeaderControllerSubcomponent.Builder>() {
                public SectionHeaderControllerSubcomponent.Builder get() {
                    return new SectionHeaderControllerSubcomponentBuilder(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.sectionHeaderControllerSubcomponentBuilderProvider = r1;
            Provider<SectionHeaderControllerSubcomponent> provider14 = DoubleCheck.provider(C2720xb614d321.create(r1));
            this.providesIncomingHeaderSubcomponentProvider = provider14;
            this.providesIncomingHeaderControllerProvider = C2718x340f4262.create(provider14);
            Provider<SectionHeaderControllerSubcomponent> provider15 = DoubleCheck.provider(C2723x39c1fe98.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider15;
            this.providesPeopleHeaderControllerProvider = C2721x812edf99.create(provider15);
            Provider<SectionHeaderControllerSubcomponent> provider16 = DoubleCheck.provider(C2717x3fd4641.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesAlertingHeaderSubcomponentProvider = provider16;
            this.providesAlertingHeaderControllerProvider = C2715x41b9fd82.create(provider16);
            Provider<SectionHeaderControllerSubcomponent> provider17 = DoubleCheck.provider(C2726x34a20792.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider17;
            C2724xcc90df13 create = C2724xcc90df13.create(provider17);
            this.providesSilentHeaderControllerProvider = create;
            this.notificationSectionsManagerProvider = NotificationSectionsManager_Factory.create(this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.notifPipelineFlagsProvider, this.mediaContainerControllerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, create);
            this.ambientStateProvider = DoubleCheck.provider(AmbientState_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.notificationSectionsManagerProvider, this.keyguardBypassControllerProvider, this.statusBarKeyguardViewManagerProvider));
            this.lockscreenShadeScrimTransitionControllerProvider = LockscreenShadeScrimTransitionController_Factory.create(this.scrimControllerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider);
            C4835LockscreenShadeKeyguardTransitionController_Factory create2 = C4835LockscreenShadeKeyguardTransitionController_Factory.create(this.mediaHierarchyManagerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider);
            this.lockscreenShadeKeyguardTransitionControllerProvider = create2;
            this.factoryProvider5 = LockscreenShadeKeyguardTransitionController_Factory_Impl.create(create2);
            C4837SplitShadeLockScreenOverScroller_Factory create3 = C4837SplitShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.contextProvider, this.scrimControllerProvider, this.statusBarStateControllerImplProvider);
            this.splitShadeLockScreenOverScrollerProvider = create3;
            this.factoryProvider6 = SplitShadeLockScreenOverScroller_Factory_Impl.create(create3);
            C4836SingleShadeLockScreenOverScroller_Factory create4 = C4836SingleShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider);
            this.singleShadeLockScreenOverScrollerProvider = create4;
            this.factoryProvider7 = SingleShadeLockScreenOverScroller_Factory_Impl.create(create4);
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(this.statusBarStateControllerImplProvider, this.lSShadeTransitionLoggerProvider, this.keyguardBypassControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.falsingCollectorImplProvider, this.ambientStateProvider, this.mediaHierarchyManagerProvider, this.lockscreenShadeScrimTransitionControllerProvider, this.factoryProvider5, this.notificationShadeDepthControllerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.factoryProvider6, this.factoryProvider7, this.wakefulnessLifecycleProvider, this.configurationControllerImplProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            Provider<VibratorHelper> provider18 = DoubleCheck.provider(VibratorHelper_Factory.create(this.tvGlobalRootComponentImpl.provideVibratorProvider, this.provideBackgroundExecutorProvider));
            this.vibratorHelperProvider = provider18;
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(UdfpsHapticsSimulator_Factory.create(this.commandRegistryProvider, provider18, this.keyguardUpdateMonitorProvider));
            this.udfpsShellProvider = DoubleCheck.provider(UdfpsShell_Factory.create(this.commandRegistryProvider));
            this.optionalOfUdfpsHbmProvider = PresentJdkOptionalInstanceProvider.m1189of(NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory.create());
            this.systemUIDialogManagerProvider = DoubleCheck.provider(SystemUIDialogManager_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, this.statusBarKeyguardViewManagerProvider));
            this.optionalOfAlternateUdfpsTouchProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.providesPluginExecutorProvider = DoubleCheck.provider(BiometricsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.create()));
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.tvGlobalRootComponentImpl.providesFingerprintManagerProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.statusBarStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.panelExpansionStateManagerProvider, this.statusBarKeyguardViewManagerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.keyguardUpdateMonitorProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.vibratorHelperProvider, this.udfpsHapticsSimulatorProvider, this.udfpsShellProvider, this.optionalOfUdfpsHbmProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideDisplayManagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.configurationControllerImplProvider, this.bindSystemClockProvider, this.unlockedScreenOffAnimationControllerProvider, this.systemUIDialogManagerProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider, this.provideActivityLaunchAnimatorProvider, this.optionalOfAlternateUdfpsTouchProvider, this.providesPluginExecutorProvider));
            this.sidefpsControllerProvider = DoubleCheck.provider(SidefpsController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.tvGlobalRootComponentImpl.providesFingerprintManagerProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.tvGlobalRootComponentImpl.provideActivityTaskManagerProvider, this.overviewProxyServiceProvider, this.tvGlobalRootComponentImpl.provideDisplayManagerProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            Provider<AuthController> provider19 = this.authControllerProvider;
            Provider access$1700 = this.tvGlobalRootComponentImpl.contextProvider;
            Provider access$19500 = this.tvGlobalRootComponentImpl.provideExecutionProvider;
            Provider<CommandQueue> provider20 = this.provideCommandQueueProvider;
            Provider access$10900 = this.tvGlobalRootComponentImpl.provideActivityTaskManagerProvider;
            Provider access$2400 = this.tvGlobalRootComponentImpl.provideWindowManagerProvider;
            Provider access$22400 = this.tvGlobalRootComponentImpl.providesFingerprintManagerProvider;
            Provider access$22500 = this.tvGlobalRootComponentImpl.provideFaceManagerProvider;
            Provider<UdfpsController> provider21 = this.udfpsControllerProvider;
            Provider<SidefpsController> provider22 = this.sidefpsControllerProvider;
            Provider access$3100 = this.tvGlobalRootComponentImpl.provideDisplayManagerProvider;
            Provider<WakefulnessLifecycle> provider23 = this.wakefulnessLifecycleProvider;
            Provider access$18600 = this.tvGlobalRootComponentImpl.provideUserManagerProvider;
            Provider access$11100 = this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider;
            Provider<AuthController> provider24 = provider19;
            DelegateFactory.setDelegate(provider24, DoubleCheck.provider(AuthController_Factory.create(access$1700, access$19500, provider20, access$10900, access$2400, access$22400, access$22500, provider21, provider22, access$3100, provider23, access$18600, access$11100, this.statusBarStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBackgroundDelayableExecutorProvider)));
            this.activeUnlockConfigProvider = DoubleCheck.provider(ActiveUnlockConfig_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.secureSettingsImplProvider, this.tvGlobalRootComponentImpl.provideContentResolverProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            DelegateFactory.setDelegate(this.keyguardUpdateMonitorProvider, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, this.tvGlobalRootComponentImpl.provideInteractionJankMonitorProvider, this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider, this.activeUnlockConfigProvider)));
            DelegateFactory.setDelegate(this.keyguardStateControllerImplProvider, DoubleCheck.provider(KeyguardStateControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.keyguardUnlockAnimationControllerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider)));
            this.brightLineFalsingManagerProvider = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, this.historyTrackerProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.tvGlobalRootComponentImpl.provideIsTestHarnessProvider);
            DelegateFactory.setDelegate(this.falsingManagerProxyProvider, DoubleCheck.provider(FalsingManagerProxy_Factory.create(this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.deviceConfigProxyProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.brightLineFalsingManagerProvider)));
        }

        private void initialize4(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            BrightnessSliderController_Factory_Factory create = BrightnessSliderController_Factory_Factory.create(this.falsingManagerProxyProvider);
            this.factoryProvider8 = create;
            this.brightnessDialogProvider = BrightnessDialog_Factory.create(this.broadcastDispatcherProvider, create, this.provideBgHandlerProvider);
            this.usbDebuggingActivityProvider = UsbDebuggingActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbDebuggingSecondaryUserActivityProvider = UsbDebuggingSecondaryUserActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbPermissionActivityProvider = UsbPermissionActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            this.usbConfirmActivityProvider = UsbConfirmActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            UserCreator_Factory create2 = UserCreator_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider);
            this.userCreatorProvider = create2;
            this.createUserActivityProvider = CreateUserActivity_Factory.create(create2, UserModule_ProvideEditUserInfoControllerFactory.create(), this.tvGlobalRootComponentImpl.provideIActivityManagerProvider);
            this.notificationListenerProvider = DoubleCheck.provider(NotificationListener_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideNotificationManagerProvider, this.bindSystemClockProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider));
            Provider<TvNotificationHandler> provider = DoubleCheck.provider(TvSystemUIModule_ProvideTvNotificationHandlerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.notificationListenerProvider));
            this.provideTvNotificationHandlerProvider = provider;
            this.tvNotificationPanelActivityProvider = TvNotificationPanelActivity_Factory.create(provider);
            Provider<PeopleSpaceWidgetManager> provider2 = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideLauncherAppsProvider, this.provideCommonNotifCollectionProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.setBubblesProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvGlobalRootComponentImpl.provideNotificationManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.peopleSpaceWidgetManagerProvider = provider2;
            this.peopleSpaceActivityProvider = PeopleSpaceActivity_Factory.create(provider2);
            this.imageExporterProvider = ImageExporter_Factory.create(this.tvGlobalRootComponentImpl.provideContentResolverProvider);
            this.longScreenshotDataProvider = DoubleCheck.provider(LongScreenshotData_Factory.create());
            this.longScreenshotActivityProvider = LongScreenshotActivity_Factory.create(this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.imageExporterProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.longScreenshotDataProvider);
            this.debugModeFilterProvider = DoubleCheck.provider(DebugModeFilterProvider_Factory.create(this.commandRegistryProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(KeyguardEnvironmentImpl_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.providesDeviceProvisionedControllerProvider));
            this.provideIndividualSensorPrivacyControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideIndividualSensorPrivacyControllerFactory.create(this.tvGlobalRootComponentImpl.provideSensorPrivacyManagerProvider));
            Provider<AppOpsControllerImpl> provider3 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.broadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider3;
            Provider<ForegroundServiceController> provider4 = DoubleCheck.provider(ForegroundServiceController_Factory.create(provider3, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            this.foregroundServiceControllerProvider = provider4;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.debugModeFilterProvider, this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, provider4, this.notificationLockscreenUserManagerImplProvider, this.mediaFeatureFlagProvider));
            this.notificationInterruptLoggerProvider = NotificationInterruptLogger_Factory.create(this.provideNotificationsLogBufferProvider, this.provideNotificationHeadsUpLogBufferProvider);
            this.highPriorityProvider = DoubleCheck.provider(HighPriorityProvider_Factory.create(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider));
            this.keyguardNotificationVisibilityProviderImplProvider = DoubleCheck.provider(KeyguardNotificationVisibilityProviderImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.keyguardStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardUpdateMonitorProvider, this.highPriorityProvider, this.statusBarStateControllerImplProvider, this.broadcastDispatcherProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider));
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(this.tvGlobalRootComponentImpl.provideContentResolverProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.tvGlobalRootComponentImpl.provideIDreamManagerProvider, this.tvGlobalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationInterruptLoggerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.notifPipelineFlagsProvider, this.keyguardNotificationVisibilityProviderImplProvider));
            this.zenModeControllerImplProvider = DoubleCheck.provider(ZenModeControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.globalSettingsImplProvider));
            Provider<Optional<BubblesManager>> provider5 = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.shadeControllerImplProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.tvGlobalRootComponentImpl.provideINotificationManagerProvider, this.provideNotificationVisibilityProvider, this.notificationInterruptStateProviderImplProvider, this.zenModeControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.notifPipelineFlagsProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.provideBubblesManagerProvider = provider5;
            this.launchConversationActivityProvider = LaunchConversationActivity_Factory.create(this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, provider5, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.provideCommandQueueProvider);
            this.sensorUseStartedActivityProvider = SensorUseStartedActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = TvUnblockSensorActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider);
            Provider<HdmiCecSetMenuLanguageHelper> provider6 = DoubleCheck.provider(HdmiCecSetMenuLanguageHelper_Factory.create(this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider));
            this.hdmiCecSetMenuLanguageHelperProvider = provider6;
            this.hdmiCecSetMenuLanguageActivityProvider = HdmiCecSetMenuLanguageActivity_Factory.create(provider6);
            this.provideExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideExecutorFactory.create(this.provideBgLooperProvider));
            this.controlsListingControllerImplProvider = DoubleCheck.provider(ControlsListingControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideExecutorProvider, this.provideUserTrackerProvider));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.provideDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional7);
            this.controlsMetricsLoggerImplProvider = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.create());
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.keyguardStateControllerImplProvider, this.setTaskViewFactoryProvider, this.controlsMetricsLoggerImplProvider, this.vibratorHelperProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.create());
            this.controlsUiControllerImplProvider = DoubleCheck.provider(ControlsUiControllerImpl_Factory.create(this.controlsControllerImplProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.tvGlobalRootComponentImpl.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider));
            this.controlsBindingControllerImplProvider = DoubleCheck.provider(ControlsBindingControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider));
            this.optionalOfControlsFavoritePersistenceWrapperProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            DelegateFactory.setDelegate(this.controlsControllerImplProvider, DoubleCheck.provider(ControlsControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, this.controlsBindingControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.optionalOfControlsFavoritePersistenceWrapperProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsFavoritingActivityProvider = ControlsFavoritingActivity_Factory.create(this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsEditingActivityProvider = ControlsEditingActivity_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.customIconCacheProvider, this.controlsUiControllerImplProvider);
            this.controlsRequestDialogProvider = ControlsRequestDialog_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsListingControllerImplProvider);
            this.controlsActivityProvider = ControlsActivity_Factory.create(this.controlsUiControllerImplProvider, this.broadcastDispatcherProvider);
            this.userSwitcherActivityProvider = UserSwitcherActivity_Factory.create(this.userSwitcherControllerProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.provideUserTrackerProvider);
            this.walletActivityProvider = WalletActivity_Factory.create(this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = MapProviderFactory.builder(24).put((Object) TunerActivity.class, (Provider) this.tunerActivityProvider).put((Object) ForegroundServicesDialog.class, (Provider) this.foregroundServicesDialogProvider).put((Object) WorkLockActivity.class, (Provider) this.workLockActivityProvider).put((Object) BrightnessDialog.class, (Provider) this.brightnessDialogProvider).put((Object) UsbDebuggingActivity.class, (Provider) this.usbDebuggingActivityProvider).put((Object) UsbDebuggingSecondaryUserActivity.class, (Provider) this.usbDebuggingSecondaryUserActivityProvider).put((Object) UsbPermissionActivity.class, (Provider) this.usbPermissionActivityProvider).put((Object) UsbConfirmActivity.class, (Provider) this.usbConfirmActivityProvider).put((Object) CreateUserActivity.class, (Provider) this.createUserActivityProvider).put((Object) TvNotificationPanelActivity.class, (Provider) this.tvNotificationPanelActivityProvider).put((Object) PeopleSpaceActivity.class, (Provider) this.peopleSpaceActivityProvider).put((Object) LongScreenshotActivity.class, (Provider) this.longScreenshotActivityProvider).put((Object) LaunchConversationActivity.class, (Provider) this.launchConversationActivityProvider).put((Object) SensorUseStartedActivity.class, (Provider) this.sensorUseStartedActivityProvider).put((Object) TvUnblockSensorActivity.class, (Provider) this.tvUnblockSensorActivityProvider).put((Object) HdmiCecSetMenuLanguageActivity.class, (Provider) this.hdmiCecSetMenuLanguageActivityProvider).put((Object) NTCriticalTemperatureWarning.class, (Provider) NTCriticalTemperatureWarning_Factory.create()).put((Object) ControlsProviderSelectorActivity.class, (Provider) this.controlsProviderSelectorActivityProvider).put((Object) ControlsFavoritingActivity.class, (Provider) this.controlsFavoritingActivityProvider).put((Object) ControlsEditingActivity.class, (Provider) this.controlsEditingActivityProvider).put((Object) ControlsRequestDialog.class, (Provider) this.controlsRequestDialogProvider).put((Object) ControlsActivity.class, (Provider) this.controlsActivityProvider).put((Object) UserSwitcherActivity.class, (Provider) this.userSwitcherActivityProvider).put((Object) WalletActivity.class, (Provider) this.walletActivityProvider).build();
            C32437 r1 = new Provider<DozeComponent.Builder>() {
                public DozeComponent.Builder get() {
                    return new DozeComponentFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.dozeComponentBuilderProvider = r1;
            this.dozeServiceProvider = DozeService_Factory.create(r1, this.tvGlobalRootComponentImpl.providesPluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider7 = DoubleCheck.provider(KeyguardLifecyclesDispatcher_Factory.create(this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.wakefulnessLifecycleProvider));
            this.keyguardLifecyclesDispatcherProvider = provider7;
            this.keyguardServiceProvider = KeyguardService_Factory.create(this.newKeyguardViewMediatorProvider, provider7, this.setTransitionsProvider);
            this.dreamOverlayComponentFactoryProvider = new Provider<DreamOverlayComponent.Factory>() {
                public DreamOverlayComponent.Factory get() {
                    return new DreamOverlayComponentFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.dreamOverlayServiceProvider = DreamOverlayService_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.dreamOverlayComponentFactoryProvider, this.dreamOverlayStateControllerProvider, this.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider);
            this.notificationListenerWithPluginsProvider = NotificationListenerWithPlugins_Factory.create(this.tvGlobalRootComponentImpl.providesPluginManagerProvider);
            this.broadcastDispatcherStartableProvider = BroadcastDispatcherStartable_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider);
            this.globalActionsComponentProvider = new DelegateFactory();
            this.ringerModeTrackerImplProvider = DoubleCheck.provider(RingerModeTrackerImpl_Factory.create(this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.globalActionsDialogLiteProvider = GlobalActionsDialogLite_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.globalActionsComponentProvider, this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.tvGlobalRootComponentImpl.provideIDreamManagerProvider, this.tvGlobalRootComponentImpl.provideDevicePolicyManagerProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.telephonyListenerManagerProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider, this.vibratorHelperProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvGlobalRootComponentImpl.provideTrustManagerProvider, this.tvGlobalRootComponentImpl.provideIActivityManagerProvider, this.tvGlobalRootComponentImpl.provideTelecomManagerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.sysuiColorExtractorProvider, this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.notificationShadeWindowControllerImplProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.ringerModeTrackerImplProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.optionalOfCentralSurfacesProvider, this.keyguardUpdateMonitorProvider, this.provideDialogLaunchAnimatorProvider);
            this.globalActionsImplProvider = GlobalActionsImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.globalActionsDialogLiteProvider, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider);
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(GlobalActionsComponent_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, this.globalActionsImplProvider, this.statusBarKeyguardViewManagerProvider)));
            this.provideTaskStackChangeListenersProvider = DoubleCheck.provider(SharedLibraryModule_ProvideTaskStackChangeListenersFactory.create(sharedLibraryModule));
            this.homeSoundEffectControllerProvider = DoubleCheck.provider(HomeSoundEffectController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.provideTaskStackChangeListenersProvider, this.provideActivityManagerWrapperProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider));
            this.instantAppNotifierProvider = DoubleCheck.provider(InstantAppNotifier_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider));
            this.keyboardUIProvider = DoubleCheck.provider(KeyboardUI_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.powerNotificationWarningsProvider = DoubleCheck.provider(PowerNotificationWarnings_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideBatteryControllerProvider, this.provideDialogLaunchAnimatorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.provideCommandQueueProvider, this.optionalOfCentralSurfacesProvider, this.powerNotificationWarningsProvider, this.enhancedEstimatesImplProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider));
            this.ringtonePlayerProvider = DoubleCheck.provider(RingtonePlayer_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(ShortcutKeyDispatcher_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(SliceBroadcastRelayHandler_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider));
            this.storageNotificationProvider = DoubleCheck.provider(StorageNotification_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.provideLauncherPackageProvider = ThemeModule_ProvideLauncherPackageFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.provideThemePickerPackageProvider = ThemeModule_ProvideThemePickerPackageFactory.create(this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.themeOverlayApplierProvider = DoubleCheck.provider(ThemeOverlayApplier_Factory.create(this.tvGlobalRootComponentImpl.provideOverlayManagerProvider, this.provideBackgroundExecutorProvider, this.provideLauncherPackageProvider, this.provideThemePickerPackageProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.themeOverlayControllerProvider = DoubleCheck.provider(ThemeOverlayController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.provideBgHandlerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.themeOverlayApplierProvider, this.secureSettingsImplProvider, this.tvGlobalRootComponentImpl.provideWallpaperManagerProvider, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.providesDeviceProvisionedControllerProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.featureFlagsReleaseProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.wakefulnessLifecycleProvider));
            this.toastFactoryProvider = DoubleCheck.provider(ToastFactory_Factory.create(this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            Provider<LogBuffer> provider8 = DoubleCheck.provider(LogModule_ProvideToastLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideToastLogBufferProvider = provider8;
            this.toastLoggerProvider = ToastLogger_Factory.create(provider8);
            this.toastUIProvider = DoubleCheck.provider(ToastUI_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, this.toastLoggerProvider));
            this.tvNotificationPanelProvider = DoubleCheck.provider(TvNotificationPanel_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider));
            this.privacyConfigProvider = DoubleCheck.provider(PrivacyConfig_Factory.create(this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.deviceConfigProxyProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvidePrivacyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providePrivacyLogBufferProvider = provider9;
            PrivacyLogger_Factory create3 = PrivacyLogger_Factory.create(provider9);
            this.privacyLoggerProvider = create3;
            this.appOpsPrivacyItemMonitorProvider = DoubleCheck.provider(AppOpsPrivacyItemMonitor_Factory.create(this.appOpsControllerImplProvider, this.provideUserTrackerProvider, this.privacyConfigProvider, this.provideBackgroundDelayableExecutorProvider, create3));
            this.mediaProjectionPrivacyItemMonitorProvider = DoubleCheck.provider(MediaProjectionPrivacyItemMonitor_Factory.create(this.tvGlobalRootComponentImpl.provideMediaProjectionManagerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.privacyConfigProvider, this.provideBgHandlerProvider, this.bindSystemClockProvider, this.privacyLoggerProvider));
            this.setOfPrivacyItemMonitorProvider = SetFactory.builder(2, 0).addProvider(this.appOpsPrivacyItemMonitorProvider).addProvider(this.mediaProjectionPrivacyItemMonitorProvider).build();
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.privacyConfigProvider, this.setOfPrivacyItemMonitorProvider, this.privacyLoggerProvider, this.bindSystemClockProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.tvOngoingPrivacyChipProvider = DoubleCheck.provider(TvOngoingPrivacyChip_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.privacyItemControllerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider));
            this.tvStatusBarProvider = DoubleCheck.provider(TvStatusBar_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.assistManagerProvider));
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.ringerModeTrackerImplProvider, ThreadFactoryImpl_Factory.create(), this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.tvGlobalRootComponentImpl.provideNotificationManagerProvider, this.vibratorHelperProvider, this.tvGlobalRootComponentImpl.provideIAudioServiceProvider, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.wakefulnessLifecycleProvider, this.tvGlobalRootComponentImpl.provideCaptioningManagerProvider, this.tvGlobalRootComponentImpl.provideKeyguardManagerProvider, this.tvGlobalRootComponentImpl.provideActivityManagerProvider));
            this.accessibilityManagerWrapperProvider = DoubleCheck.provider(AccessibilityManagerWrapper_Factory.create(this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider));
            this.provideVolumeDialogProvider = VolumeModule_ProvideVolumeDialogFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.volumeDialogControllerImplProvider, this.accessibilityManagerWrapperProvider, this.providesDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.mediaOutputDialogFactoryProvider, this.provideActivityStarterProvider, this.tvGlobalRootComponentImpl.provideInteractionJankMonitorProvider);
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.newKeyguardViewMediatorProvider, this.provideActivityStarterProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider, this.tvGlobalRootComponentImpl.pluginDependencyProvider, this.extensionControllerImplProvider, this.tunerServiceImplProvider, this.provideVolumeDialogProvider));
            this.volumeUIProvider = DoubleCheck.provider(VolumeUI_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.volumeDialogComponentProvider));
            this.securityControllerImplProvider = DoubleCheck.provider(SecurityControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
        }

        private void initialize5(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.vpnStatusObserverProvider = DoubleCheck.provider(VpnStatusObserver_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.securityControllerImplProvider));
            this.modeSwitchesControllerProvider = DoubleCheck.provider(ModeSwitchesController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideCommandQueueProvider, this.modeSwitchesControllerProvider, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional8);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional9);
            this.setCompatUIProvider = InstanceFactory.create(optional14);
            this.setDragAndDropProvider = InstanceFactory.create(optional15);
            this.userInfoControllerImplProvider = DoubleCheck.provider(UserInfoControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.setPipProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setHideDisplayCutoutProvider, this.setShellCommandHandlerProvider, this.setCompatUIProvider, this.setDragAndDropProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, this.userInfoControllerImplProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.mapOfClassOfAndProviderOfCoreStartableProvider = MapProviderFactory.builder(21).put((Object) BroadcastDispatcherStartable.class, (Provider) this.broadcastDispatcherStartableProvider).put((Object) KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put((Object) GlobalActionsComponent.class, (Provider) this.globalActionsComponentProvider).put((Object) HomeSoundEffectController.class, (Provider) this.homeSoundEffectControllerProvider).put((Object) InstantAppNotifier.class, (Provider) this.instantAppNotifierProvider).put((Object) KeyboardUI.class, (Provider) this.keyboardUIProvider).put((Object) PowerUI.class, (Provider) this.powerUIProvider).put((Object) RingtonePlayer.class, (Provider) this.ringtonePlayerProvider).put((Object) ShortcutKeyDispatcher.class, (Provider) this.shortcutKeyDispatcherProvider).put((Object) SliceBroadcastRelayHandler.class, (Provider) this.sliceBroadcastRelayHandlerProvider).put((Object) StorageNotification.class, (Provider) this.storageNotificationProvider).put((Object) ThemeOverlayController.class, (Provider) this.themeOverlayControllerProvider).put((Object) ToastUI.class, (Provider) this.toastUIProvider).put((Object) TvNotificationHandler.class, (Provider) this.provideTvNotificationHandlerProvider).put((Object) TvNotificationPanel.class, (Provider) this.tvNotificationPanelProvider).put((Object) TvOngoingPrivacyChip.class, (Provider) this.tvOngoingPrivacyChipProvider).put((Object) TvStatusBar.class, (Provider) this.tvStatusBarProvider).put((Object) VolumeUI.class, (Provider) this.volumeUIProvider).put((Object) VpnStatusObserver.class, (Provider) this.vpnStatusObserverProvider).put((Object) WindowMagnification.class, (Provider) this.windowMagnificationProvider).put((Object) WMShell.class, (Provider) this.wMShellProvider).build();
            this.dumpHandlerProvider = DumpHandler_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.logBufferEulogizerProvider, this.mapOfClassOfAndProviderOfCoreStartableProvider, this.tvGlobalRootComponentImpl.uncaughtExceptionPreHandlerManagerProvider);
            this.logBufferFreezerProvider = LogBufferFreezer_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider);
            this.batteryStateNotifierProvider = BatteryStateNotifier_Factory.create(this.provideBatteryControllerProvider, this.tvGlobalRootComponentImpl.provideNotificationManagerProvider, this.provideDelayableExecutorProvider, this.tvGlobalRootComponentImpl.contextProvider);
            this.systemUIServiceProvider = SystemUIService_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.dumpHandlerProvider, this.broadcastDispatcherProvider, this.logBufferFreezerProvider, this.batteryStateNotifierProvider);
            this.systemUIAuxiliaryDumpServiceProvider = SystemUIAuxiliaryDumpService_Factory.create(this.dumpHandlerProvider);
            Provider<RecordingController> provider = DoubleCheck.provider(RecordingController_Factory.create(this.broadcastDispatcherProvider, this.provideUserTrackerProvider));
            this.recordingControllerProvider = provider;
            this.recordingServiceProvider = RecordingService_Factory.create(provider, this.provideLongRunningExecutorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.tvGlobalRootComponentImpl.provideNotificationManagerProvider, this.provideUserTrackerProvider, this.keyguardDismissUtilProvider);
            this.screenshotSmartActionsProvider = DoubleCheck.provider(ScreenshotSmartActions_Factory.create());
            this.screenshotNotificationsControllerProvider = ScreenshotNotificationsController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider);
            this.scrollCaptureClientProvider = ScrollCaptureClient_Factory.create(this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.contextProvider);
            this.imageTileSetProvider = ImageTileSet_Factory.create(GlobalConcurrencyModule_ProvideHandlerFactory.create());
            this.scrollCaptureControllerProvider = ScrollCaptureController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.scrollCaptureClientProvider, this.imageTileSetProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider);
            this.timeoutHandlerProvider = TimeoutHandler_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            ScreenshotController_Factory create = ScreenshotController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.screenshotSmartActionsProvider, this.screenshotNotificationsControllerProvider, this.scrollCaptureClientProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.imageExporterProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.scrollCaptureControllerProvider, this.longScreenshotDataProvider, this.tvGlobalRootComponentImpl.provideActivityManagerProvider, this.timeoutHandlerProvider, this.broadcastSenderProvider);
            this.screenshotControllerProvider = create;
            this.takeScreenshotServiceProvider = TakeScreenshotService_Factory.create(create, this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.tvGlobalRootComponentImpl.provideDevicePolicyManagerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.screenshotNotificationsControllerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider);
            this.mapOfClassOfAndProviderOfServiceProvider = MapProviderFactory.builder(9).put((Object) DozeService.class, (Provider) this.dozeServiceProvider).put((Object) ImageWallpaper.class, (Provider) ImageWallpaper_Factory.create()).put((Object) KeyguardService.class, (Provider) this.keyguardServiceProvider).put((Object) DreamOverlayService.class, (Provider) this.dreamOverlayServiceProvider).put((Object) NotificationListenerWithPlugins.class, (Provider) this.notificationListenerWithPluginsProvider).put((Object) SystemUIService.class, (Provider) this.systemUIServiceProvider).put((Object) SystemUIAuxiliaryDumpService.class, (Provider) this.systemUIAuxiliaryDumpServiceProvider).put((Object) RecordingService.class, (Provider) this.recordingServiceProvider).put((Object) TakeScreenshotService.class, (Provider) this.takeScreenshotServiceProvider).build();
            this.overviewProxyRecentsImplProvider = DoubleCheck.provider(OverviewProxyRecentsImpl_Factory.create(this.optionalOfCentralSurfacesProvider, this.overviewProxyServiceProvider));
            this.mapOfClassOfAndProviderOfRecentsImplementationProvider = MapProviderFactory.builder(1).put((Object) OverviewProxyRecentsImpl.class, (Provider) this.overviewProxyRecentsImplProvider).build();
            this.actionProxyReceiverProvider = ActionProxyReceiver_Factory.create(this.optionalOfCentralSurfacesProvider, this.provideActivityManagerWrapperProvider, this.screenshotSmartActionsProvider);
            this.deleteScreenshotReceiverProvider = DeleteScreenshotReceiver_Factory.create(this.screenshotSmartActionsProvider, this.provideBackgroundExecutorProvider);
            this.smartActionsReceiverProvider = SmartActionsReceiver_Factory.create(this.screenshotSmartActionsProvider);
            MediaOutputBroadcastDialogFactory_Factory create2 = MediaOutputBroadcastDialogFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.tvGlobalRootComponentImpl.provideAudioManagerProvider, this.tvGlobalRootComponentImpl.providePowerExemptionManagerProvider);
            this.mediaOutputBroadcastDialogFactoryProvider = create2;
            this.mediaOutputDialogReceiverProvider = MediaOutputDialogReceiver_Factory.create(this.mediaOutputDialogFactoryProvider, create2);
            this.peopleSpaceWidgetPinnedReceiverProvider = PeopleSpaceWidgetPinnedReceiver_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.peopleSpaceWidgetProvider = PeopleSpaceWidgetProvider_Factory.create(this.peopleSpaceWidgetManagerProvider);
            MapProviderFactory build = MapProviderFactory.builder(6).put((Object) ActionProxyReceiver.class, (Provider) this.actionProxyReceiverProvider).put((Object) DeleteScreenshotReceiver.class, (Provider) this.deleteScreenshotReceiverProvider).put((Object) SmartActionsReceiver.class, (Provider) this.smartActionsReceiverProvider).put((Object) MediaOutputDialogReceiver.class, (Provider) this.mediaOutputDialogReceiverProvider).put((Object) PeopleSpaceWidgetPinnedReceiver.class, (Provider) this.peopleSpaceWidgetPinnedReceiverProvider).put((Object) PeopleSpaceWidgetProvider.class, (Provider) this.peopleSpaceWidgetProvider).build();
            this.mapOfClassOfAndProviderOfBroadcastReceiverProvider = build;
            DelegateFactory.setDelegate(this.contextComponentResolverProvider, DoubleCheck.provider(ContextComponentResolver_Factory.create(this.mapOfClassOfAndProviderOfActivityProvider, this.mapOfClassOfAndProviderOfServiceProvider, this.mapOfClassOfAndProviderOfRecentsImplementationProvider, build)));
            this.unfoldLatencyTrackerProvider = DoubleCheck.provider(UnfoldLatencyTracker_Factory.create(this.tvGlobalRootComponentImpl.provideLatencyTrackerProvider, this.tvGlobalRootComponentImpl.provideDeviceStateManagerProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider));
            this.bluetoothControllerImplProvider = DoubleCheck.provider(BluetoothControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideBgLooperProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideLocalBluetoothControllerProvider));
            this.locationControllerImplProvider = DoubleCheck.provider(LocationControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.appOpsControllerImplProvider, this.deviceConfigProxyProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.secureSettingsImplProvider));
            RotationPolicyWrapperImpl_Factory create3 = RotationPolicyWrapperImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.secureSettingsImplProvider);
            this.rotationPolicyWrapperImplProvider = create3;
            this.bindRotationPolicyWrapperProvider = DoubleCheck.provider(create3);
            this.provideAutoRotateSettingsManagerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.deviceStateRotationLockSettingControllerProvider = DoubleCheck.provider(DeviceStateRotationLockSettingController_Factory.create(this.bindRotationPolicyWrapperProvider, this.tvGlobalRootComponentImpl.provideDeviceStateManagerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideAutoRotateSettingsManagerProvider));
            C3215xe335f1e6 create4 = C3215xe335f1e6.create(this.tvGlobalRootComponentImpl.provideResourcesProvider);
            this.providesDeviceStateRotationLockDefaultsProvider = create4;
            this.rotationLockControllerImplProvider = DoubleCheck.provider(RotationLockControllerImpl_Factory.create(this.bindRotationPolicyWrapperProvider, this.deviceStateRotationLockSettingControllerProvider, create4));
            this.hotspotControllerImplProvider = DoubleCheck.provider(HotspotControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.castControllerImplProvider = DoubleCheck.provider(CastControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.flashlightControllerImplProvider = DoubleCheck.provider(FlashlightControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            NightDisplayListenerModule nightDisplayListenerModule2 = nightDisplayListenerModule;
            this.provideNightDisplayListenerProvider = NightDisplayListenerModule_ProvideNightDisplayListenerFactory.create(nightDisplayListenerModule, this.tvGlobalRootComponentImpl.contextProvider, this.provideBgHandlerProvider);
            this.reduceBrightColorsControllerProvider = DoubleCheck.provider(ReduceBrightColorsController_Factory.create(this.provideUserTrackerProvider, this.provideBgHandlerProvider, this.tvGlobalRootComponentImpl.provideColorDisplayManagerProvider, this.secureSettingsImplProvider));
            this.managedProfileControllerImplProvider = DoubleCheck.provider(ManagedProfileControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider));
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(NextAlarmControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.provideAlarmManagerProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.callbackHandlerProvider = CallbackHandler_Factory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.wifiPickerTrackerFactoryProvider = DoubleCheck.provider(AccessPointControllerImpl_WifiPickerTrackerFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideWifiManagerProvider, this.tvGlobalRootComponentImpl.provideConnectivityManagagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.create(this.tvGlobalRootComponentImpl.provideUserManagerProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.wifiPickerTrackerFactoryProvider));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(CarrierConfigTracker_Factory.create(this.tvGlobalRootComponentImpl.provideCarrierConfigManagerProvider, this.broadcastDispatcherProvider));
            this.wifiStatusTrackerFactoryProvider = WifiStatusTrackerFactory_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideWifiManagerProvider, this.tvGlobalRootComponentImpl.provideNetworkScoreManagerProvider, this.tvGlobalRootComponentImpl.provideConnectivityManagagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider);
            this.wifiStateWorkerProvider = DoubleCheck.provider(WifiStateWorker_Factory.create(this.broadcastDispatcherProvider, this.provideBackgroundDelayableExecutorProvider, this.tvGlobalRootComponentImpl.provideWifiManagerProvider));
            this.carrierNameCustomizationProvider = DoubleCheck.provider(CarrierNameCustomization_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.internetDialogControllerProvider = InternetDialogController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideActivityStarterProvider, this.provideAccessPointControllerImplProvider, this.tvGlobalRootComponentImpl.provideSubcriptionManagerProvider, this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.tvGlobalRootComponentImpl.provideWifiManagerProvider, this.tvGlobalRootComponentImpl.provideConnectivityManagagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.toastFactoryProvider, this.provideBgHandlerProvider, this.carrierConfigTrackerProvider, this.locationControllerImplProvider, this.provideDialogLaunchAnimatorProvider, this.wifiStateWorkerProvider, this.carrierNameCustomizationProvider);
            this.internetDialogFactoryProvider = DoubleCheck.provider(InternetDialogFactory_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBackgroundExecutorProvider, this.internetDialogControllerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.keyguardStateControllerImplProvider));
            Provider<NetworkControllerImpl> provider2 = DoubleCheck.provider(NetworkControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.providesDeviceProvisionedControllerProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideConnectivityManagagerProvider, this.tvGlobalRootComponentImpl.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, this.tvGlobalRootComponentImpl.provideWifiManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, this.wifiStatusTrackerFactoryProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.internetDialogFactoryProvider, this.featureFlagsReleaseProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.networkControllerImplProvider = provider2;
            this.provideDataSaverControllerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideDataSaverControllerFactory.create(provider2));
            this.accessibilityControllerProvider = DoubleCheck.provider(AccessibilityController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.provideLeakReportEmailProvider = DoubleCheck.provider(TvSystemUIModule_ProvideLeakReportEmailFactory.create());
            this.leakReporterProvider = DoubleCheck.provider(LeakReporter_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesLeakDetectorProvider, this.provideLeakReportEmailProvider));
            this.providesBackgroundMessageRouterProvider = SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory.create(this.provideBackgroundDelayableExecutorProvider);
            this.garbageMonitorProvider = DoubleCheck.provider(GarbageMonitor_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.providesBackgroundMessageRouterProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.providesStatusBarWindowViewProvider = DoubleCheck.provider(StatusBarWindowModule_ProvidesStatusBarWindowViewFactory.create(this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider));
            this.statusBarContentInsetsProvider = DoubleCheck.provider(StatusBarContentInsetsProvider_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.providesStatusBarWindowViewProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.statusBarContentInsetsProvider, this.tvGlobalRootComponentImpl.provideResourcesProvider, this.tvGlobalRootComponentImpl.unfoldTransitionProgressProvider));
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider, this.configurationControllerImplProvider, this.tunerServiceImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            C32459 r1 = new Provider<FragmentService.FragmentCreator.Factory>() {
                public FragmentService.FragmentCreator.Factory get() {
                    return new FragmentCreatorFactory(TvSysUIComponentImpl.this.tvGlobalRootComponentImpl, TvSysUIComponentImpl.this.tvSysUIComponentImpl);
                }
            };
            this.fragmentCreatorFactoryProvider = r1;
            this.fragmentServiceProvider = DoubleCheck.provider(FragmentService_Factory.create(r1, this.configurationControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.tunablePaddingServiceProvider = DoubleCheck.provider(TunablePadding_TunablePaddingService_Factory.create(this.tunerServiceImplProvider));
            this.uiOffloadThreadProvider = DoubleCheck.provider(UiOffloadThread_Factory.create());
            this.provideGroupExpansionManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupExpansionManagerFactory.create(this.notifPipelineFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider));
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideGroupExpansionManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.accessibilityFloatingMenuControllerProvider = DoubleCheck.provider(AccessibilityFloatingMenuController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            this.notificationGroupAlertTransferHelperProvider = DoubleCheck.provider(NotificationGroupAlertTransferHelper_Factory.create(this.rowContentBindStageProvider, this.statusBarStateControllerImplProvider, this.notificationGroupManagerLegacyProvider));
            this.provideVisualStabilityManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(this.provideNotificationEntryManagerProvider, this.visualStabilityProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(ChannelEditorDialogController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideINotificationManagerProvider, ChannelEditorDialog_Builder_Factory.create()));
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(AssistantFeedbackController_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.deviceConfigProxyProvider));
            this.panelEventsEmitterProvider = DoubleCheck.provider(NotificationPanelViewController_PanelEventsEmitter_Factory.create());
            Provider<VisualStabilityCoordinator> provider3 = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideDelayableExecutorProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideHeadsUpManagerPhoneProvider, this.panelEventsEmitterProvider, this.statusBarStateControllerImplProvider, this.visualStabilityProvider, this.wakefulnessLifecycleProvider));
            this.visualStabilityCoordinatorProvider = provider3;
            this.provideOnUserInteractionCallbackProvider = DoubleCheck.provider(NotificationsModule_ProvideOnUserInteractionCallbackFactory.create(this.notifPipelineFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifCollectionProvider, this.provideNotificationVisibilityProvider, provider3, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.optionalOfCentralSurfacesProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider, this.tvGlobalRootComponentImpl.provideAccessibilityManagerProvider, this.highPriorityProvider, this.tvGlobalRootComponentImpl.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, this.tvGlobalRootComponentImpl.provideLauncherAppsProvider, this.tvGlobalRootComponentImpl.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideOnUserInteractionCallbackProvider, this.shadeControllerImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.expansionStateLoggerProvider = NotificationLogger_ExpansionStateLogger_Factory.create(this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider);
            this.provideNotificationPanelLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.create());
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.notificationListenerProvider, this.tvGlobalRootComponentImpl.provideUiBackgroundExecutorProvider, this.notifPipelineFlagsProvider, this.notifLiveDataStoreImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, this.provideNotificationPanelLoggerProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(DynamicPrivacyController_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider));
            this.dynamicChildBindControllerProvider = DynamicChildBindController_Factory.create(this.rowContentBindStageProvider);
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(C2635x5356ea10.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.featureFlagsReleaseProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.dynamicChildBindControllerProvider, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider, this.notifPipelineFlagsProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.remoteInputQuickSettingsDisablerProvider = DoubleCheck.provider(RemoteInputQuickSettingsDisabler_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider));
            this.foregroundServiceNotificationListenerProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.foregroundServiceControllerProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.bindSystemClockProvider));
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideTimeTickHandlerFactory.create());
            this.clockManagerProvider = DoubleCheck.provider(ClockManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.providerLayoutInflaterProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.sysuiColorExtractorProvider, this.dockManagerImplProvider, this.broadcastDispatcherProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(TvSystemUIModule_ProvideSensorPrivacyControllerFactory.create(this.tvGlobalRootComponentImpl.provideSensorPrivacyManagerProvider));
            this.systemEventCoordinatorProvider = DoubleCheck.provider(SystemEventCoordinator_Factory.create(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider));
        }

        private void initialize6(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            SystemEventChipAnimationController_Factory create = SystemEventChipAnimationController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.statusBarWindowControllerProvider, this.statusBarContentInsetsProvider);
            this.systemEventChipAnimationControllerProvider = create;
            this.systemStatusAnimationSchedulerProvider = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(this.systemEventCoordinatorProvider, create, this.statusBarWindowControllerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.bindSystemClockProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.statusBarContentInsetsProvider, this.systemStatusAnimationSchedulerProvider));
            this.dependencyProvider = DoubleCheck.provider(Dependency_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.asyncSensorManagerProvider, this.bluetoothControllerImplProvider, this.locationControllerImplProvider, this.rotationLockControllerImplProvider, this.zenModeControllerImplProvider, this.hdmiCecSetMenuLanguageHelperProvider, this.hotspotControllerImplProvider, this.castControllerImplProvider, this.flashlightControllerImplProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideBatteryControllerProvider, this.provideNightDisplayListenerProvider, this.reduceBrightColorsControllerProvider, this.managedProfileControllerImplProvider, this.nextAlarmControllerImplProvider, this.provideDataSaverControllerProvider, this.accessibilityControllerProvider, this.providesDeviceProvisionedControllerProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.assistManagerProvider, this.securityControllerImplProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.garbageMonitorProvider, this.tunerServiceImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarWindowControllerProvider, this.darkIconDispatcherImplProvider, this.configurationControllerImplProvider, this.statusBarIconControllerImplProvider, this.tvGlobalRootComponentImpl.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.fragmentServiceProvider, this.extensionControllerImplProvider, this.tvGlobalRootComponentImpl.pluginDependencyProvider, this.provideLocalBluetoothControllerProvider, this.volumeDialogControllerImplProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.accessibilityManagerWrapperProvider, this.sysuiColorExtractorProvider, this.tunablePaddingServiceProvider, this.foregroundServiceControllerProvider, this.uiOffloadThreadProvider, this.powerNotificationWarningsProvider, this.lightBarControllerProvider, this.tvGlobalRootComponentImpl.provideIWindowManagerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.enhancedEstimatesImplProvider, this.vibratorHelperProvider, this.tvGlobalRootComponentImpl.provideIStatusBarServiceProvider, this.tvGlobalRootComponentImpl.provideDisplayMetricsProvider, this.lockscreenGestureLoggerProvider, this.keyguardEnvironmentImplProvider, this.shadeControllerImplProvider, this.statusBarRemoteInputCallbackProvider, this.appOpsControllerImplProvider, this.navigationBarControllerProvider, this.accessibilityFloatingMenuControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupAlertTransferHelperProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.smartReplyConstantsProvider, this.notificationListenerProvider, this.provideNotificationLoggerProvider, this.provideNotificationViewHierarchyManagerProvider, this.notificationFilterProvider, this.keyguardDismissUtilProvider, this.provideSmartReplyControllerProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationEntryManagerProvider, this.tvGlobalRootComponentImpl.provideSensorPrivacyManagerProvider, this.autoHideControllerProvider, this.foregroundServiceNotificationListenerProvider, this.privacyItemControllerProvider, this.provideBgLooperProvider, this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideTimeTickHandlerProvider, this.provideLeakReportEmailProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.clockManagerProvider, this.provideActivityManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.tvGlobalRootComponentImpl.providePackageManagerWrapperProvider, this.provideSensorPrivacyControllerProvider, this.dockManagerImplProvider, this.tvGlobalRootComponentImpl.provideINotificationManagerProvider, this.provideSysUiStateProvider, this.tvGlobalRootComponentImpl.provideAlarmManagerProvider, this.keyguardSecurityModelProvider, this.dozeParametersProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.provideCommandQueueProvider, this.recordingControllerProvider, this.protoTracerProvider, this.mediaOutputDialogFactoryProvider, this.deviceConfigProxyProvider, this.telephonyListenerManagerProvider, this.systemStatusAnimationSchedulerProvider, this.privacyDotViewControllerProvider, this.factoryProvider2, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.statusBarContentInsetsProvider, this.internetDialogFactoryProvider, this.featureFlagsReleaseProvider, this.notificationSectionsManagerProvider, this.screenOffAnimationControllerProvider, this.ambientStateProvider, this.provideGroupMembershipManagerProvider, this.provideGroupExpansionManagerProvider, this.systemUIDialogManagerProvider, this.provideDialogLaunchAnimatorProvider));
            this.nTColorControllerProvider = DoubleCheck.provider(NTColorController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.keyguardWeatherControllerImplProvider = DoubleCheck.provider(KeyguardWeatherControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.keyguardUpdateMonitorProvider));
            this.liftWakeGestureControllerProvider = DoubleCheck.provider(LiftWakeGestureController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideProximitySensorProvider));
            this.centralSurfacesImplExProvider = CentralSurfacesImplEx_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.keyguardUpdateMonitorProvider, this.tvGlobalRootComponentImpl.aODControllerProvider, this.liftWakeGestureControllerProvider);
            this.nfcControllerImplProvider = DoubleCheck.provider(NfcControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBatteryControllerProvider));
            this.teslaInfoControllerImplProvider = DoubleCheck.provider(TeslaInfoControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.faceRecognitionControllerProvider = DoubleCheck.provider(FaceRecognitionController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.nTLightweightHeadsupManagerProvider = DoubleCheck.provider(NTLightweightHeadsupManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.nTGameModeHelperProvider = DoubleCheck.provider(NTGameModeHelper_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.headsUpViewBinderLoggerProvider = HeadsUpViewBinderLogger_Factory.create(this.provideNotificationHeadsUpLogBufferProvider);
            Provider<HeadsUpViewBinder> provider = DoubleCheck.provider(HeadsUpViewBinder_Factory.create(this.tvGlobalRootComponentImpl.provideNotificationMessagingUtilProvider, this.rowContentBindStageProvider, this.headsUpViewBinderLoggerProvider));
            this.headsUpViewBinderProvider = provider;
            Provider<HeadsUpController> provider2 = DoubleCheck.provider(HeadsUpController_Factory.create(provider, this.notificationInterruptStateProviderImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideNotificationRemoteInputManagerProvider, this.statusBarStateControllerImplProvider, this.provideVisualStabilityManagerProvider, this.notificationListenerProvider));
            this.headsUpControllerProvider = provider2;
            this.headsUpControllerExProvider = DoubleCheck.provider(HeadsUpControllerEx_Factory.create(provider2, this.provideHeadsUpManagerPhoneProvider, this.headsUpViewBinderProvider));
            this.temperatureControllerProvider = DoubleCheck.provider(TemperatureController_Factory.create());
            this.keyguardIndicationControllerExProvider = KeyguardIndicationControllerEx_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.keyguardSecurityModelProvider, this.keyguardUpdateMonitorProvider, this.newKeyguardViewMediatorProvider);
            this.ambientStateExProvider = DoubleCheck.provider(AmbientStateEx_Factory.create());
            this.commandQueueExProvider = CommandQueueEx_Factory.create(this.provideCommandQueueProvider);
            this.calendarManagerProvider = DoubleCheck.provider(CalendarManager_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.keyguardUpdateMonitorProvider));
            this.assistManagerExProvider = DoubleCheck.provider(AssistManagerEx_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.notificationRoundnessManagerProvider = DoubleCheck.provider(NotificationRoundnessManager_Factory.create(this.notificationSectionsFeatureManagerProvider));
            this.nTDependencyExProvider = DoubleCheck.provider(NTDependencyEx_Factory.create(this.tvGlobalRootComponentImpl.dumpManagerProvider, BatteryControllerImplEx_Factory.create(), BluetoothTileEx_Factory.create(), BrightnessControllerEx_Factory.create(), InternetTileEx_Factory.create(), this.nTColorControllerProvider, VolumeDialogImplEx_Factory.create(), WifiSignalControllerEx_Factory.create(), OngoingPrivacyChipEx_Factory.create(), PrivacyDialogEx_Factory.create(), PrivacyDialogControllerEx_Factory.create(), QSFragmentEx_Factory.create(), QSIconViewImplEx_Factory.create(), QSTileHostEx_Factory.create(), QSTileImplEx_Factory.create(), QSTileViewImplEx_Factory.create(), QuickStatusBarHeaderEx_Factory.create(), TileLayoutEx_Factory.create(), NavigationBarControllerEx_Factory.create(), NavigationBarEx_Factory.create(), NavigationBarViewEx_Factory.create(), NavigationBarInflaterViewEx_Factory.create(), NavigationModeControllerEx_Factory.create(), KeyButtonViewEx_Factory.create(), EdgeBackGestureHandlerEx_Factory.create(), this.keyguardWeatherControllerImplProvider, this.tvGlobalRootComponentImpl.aODControllerProvider, this.liftWakeGestureControllerProvider, this.centralSurfacesImplExProvider, DozeServiceHostEx_Factory.create(), this.nfcControllerImplProvider, this.teslaInfoControllerImplProvider, this.tvGlobalRootComponentImpl.keyguardUpdateMonitorExProvider, this.tvGlobalRootComponentImpl.keyguardViewMediatorExProvider, this.faceRecognitionControllerProvider, this.nTLightweightHeadsupManagerProvider, this.nTGameModeHelperProvider, this.headsUpControllerExProvider, this.falsingManagerProxyProvider, this.temperatureControllerProvider, this.keyguardIndicationControllerExProvider, MobileSignalControllerEx_Factory.create(), this.ambientStateExProvider, this.commandQueueExProvider, this.lockscreenShadeTransitionControllerProvider, this.scrimControllerProvider, this.calendarManagerProvider, LockIconViewControllerEx_Factory.create(), this.mediaDataManagerProvider, this.configurationControllerImplProvider, this.assistManagerExProvider, this.notificationRoundnessManagerProvider, this.tvGlobalRootComponentImpl.authRippleControllerExProvider));
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.create());
            this.mediaTttFlagsProvider = DoubleCheck.provider(MediaTttFlags_Factory.create(this.featureFlagsReleaseProvider));
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideMediaTttSenderLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttSenderLogBufferProvider = provider3;
            this.providesMediaTttSenderLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttSenderLoggerFactory.create(provider3));
            this.viewUtilProvider = DoubleCheck.provider(ViewUtil_Factory.create());
            this.tapGestureDetectorProvider = DoubleCheck.provider(TapGestureDetector_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.mediaTttSenderUiEventLoggerProvider = DoubleCheck.provider(MediaTttSenderUiEventLogger_Factory.create(this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerSender> provider4 = DoubleCheck.provider(MediaTttChipControllerSender_Factory.create(this.provideCommandQueueProvider, this.tvGlobalRootComponentImpl.contextProvider, this.providesMediaTttSenderLoggerProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.viewUtilProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider, this.tapGestureDetectorProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.mediaTttSenderUiEventLoggerProvider));
            this.mediaTttChipControllerSenderProvider = provider4;
            this.providesMediaTttChipControllerSenderProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerSenderFactory.create(this.mediaTttFlagsProvider, provider4));
            Provider<LogBuffer> provider5 = DoubleCheck.provider(LogModule_ProvideMediaTttReceiverLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttReceiverLogBufferProvider = provider5;
            this.providesMediaTttReceiverLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttReceiverLoggerFactory.create(provider5));
            this.mediaTttReceiverUiEventLoggerProvider = DoubleCheck.provider(MediaTttReceiverUiEventLogger_Factory.create(this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerReceiver> provider6 = DoubleCheck.provider(MediaTttChipControllerReceiver_Factory.create(this.provideCommandQueueProvider, this.tvGlobalRootComponentImpl.contextProvider, this.providesMediaTttReceiverLoggerProvider, this.tvGlobalRootComponentImpl.provideWindowManagerProvider, this.viewUtilProvider, this.provideDelayableExecutorProvider, this.tapGestureDetectorProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.mediaTttReceiverUiEventLoggerProvider));
            this.mediaTttChipControllerReceiverProvider = provider6;
            this.providesMediaTttChipControllerReceiverProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerReceiverFactory.create(this.mediaTttFlagsProvider, provider6));
            Provider<MediaTttCommandLineHelper> provider7 = DoubleCheck.provider(MediaTttCommandLineHelper_Factory.create(this.commandRegistryProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider));
            this.mediaTttCommandLineHelperProvider = provider7;
            this.providesMediaTttCommandLineHelperProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttCommandLineHelperFactory.create(this.mediaTttFlagsProvider, provider7));
            Provider<MediaMuteAwaitConnectionCli> provider8 = DoubleCheck.provider(MediaMuteAwaitConnectionCli_Factory.create(this.commandRegistryProvider, this.tvGlobalRootComponentImpl.contextProvider));
            this.mediaMuteAwaitConnectionCliProvider = provider8;
            this.providesMediaMuteAwaitConnectionCliProvider = DoubleCheck.provider(MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory.create(this.mediaFlagsProvider, provider8));
            this.notificationChannelsProvider = NotificationChannels_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.provideClockInfoListProvider = ClockModule_ProvideClockInfoListFactory.create(this.clockManagerProvider);
            this.setDisplayAreaHelperProvider = InstanceFactory.create(optional11);
            this.provideAllowNotificationLongPressProvider = DoubleCheck.provider(TvSystemUIModule_ProvideAllowNotificationLongPressFactory.create());
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(NotificationWakeUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.screenOffAnimationControllerProvider));
            this.notificationIconAreaControllerProvider = DoubleCheck.provider(NotificationIconAreaController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.notificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.screenOffAnimationControllerProvider));
            this.optionalOfBcSmartspaceDataPluginProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.lockscreenSmartspaceControllerProvider = DoubleCheck.provider(LockscreenSmartspaceController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.featureFlagsReleaseProvider, this.tvGlobalRootComponentImpl.provideSmartspaceManagerProvider, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.tvGlobalRootComponentImpl.provideContentResolverProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider, this.tvGlobalRootComponentImpl.provideExecutionProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.optionalOfBcSmartspaceDataPluginProvider));
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationRoundnessManagerProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, this.tvGlobalRootComponentImpl.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.providesDeviceProvisionedControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.scrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.provideSysUIUnfoldComponentProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, this.notificationIconAreaControllerProvider));
            this.provideProximityCheckProvider = SensorModule_ProvideProximityCheckFactory.create(this.provideProximitySensorProvider, this.tvGlobalRootComponentImpl.provideMainDelayableExecutorProvider);
            this.dreamOverlayNotificationCountProvider = DoubleCheck.provider(DreamOverlayNotificationCountProvider_Factory.create(this.notificationListenerProvider, this.provideBackgroundExecutorProvider));
            this.statusBarWindowStateControllerProvider = DoubleCheck.provider(StatusBarWindowStateController_Factory.create(this.tvGlobalRootComponentImpl.provideDisplayIdProvider, this.provideCommandQueueProvider));
            this.qSTileHostProvider = new DelegateFactory();
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideQuickSettingsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideQuickSettingsLogBufferProvider = provider9;
            this.qSLoggerProvider = QSLogger_Factory.create(provider9);
            this.customTileStatePersisterProvider = CustomTileStatePersister_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            this.tileServicesProvider = TileServices_Factory.create(this.qSTileHostProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.provideCommandQueueProvider);
            this.builderProvider4 = CustomTile_Builder_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.customTileStatePersisterProvider, this.tileServicesProvider);
            this.wifiTileProvider = WifiTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider);
            this.internetTileProvider = InternetTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider, this.internetDialogFactoryProvider, this.carrierNameCustomizationProvider);
            this.bluetoothTileProvider = BluetoothTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.bluetoothControllerImplProvider, this.teslaInfoControllerImplProvider);
            this.cellularTileProvider = CellularTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.dndTileProvider = DndTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.zenModeControllerImplProvider, this.tvGlobalRootComponentImpl.provideSharePreferencesProvider, this.secureSettingsImplProvider, this.provideDialogLaunchAnimatorProvider);
            this.colorInversionTileProvider = ColorInversionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.airplaneModeTileProvider = AirplaneModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.provideConnectivityManagagerProvider, this.globalSettingsImplProvider);
            this.workModeTileProvider = WorkModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.managedProfileControllerImplProvider);
            this.rotationLockTileProvider = RotationLockTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.rotationLockControllerImplProvider, this.tvGlobalRootComponentImpl.provideSensorPrivacyManagerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.flashlightTileProvider = FlashlightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.flashlightControllerImplProvider);
            this.locationTileProvider = LocationTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.castTileProvider = CastTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.castControllerImplProvider, this.keyguardStateControllerImplProvider, this.networkControllerImplProvider, this.hotspotControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            this.hotspotTileProvider = HotspotTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider);
            this.batterySaverTileProvider = BatterySaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.dataSaverTileProvider = DataSaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideDataSaverControllerProvider, this.provideDialogLaunchAnimatorProvider);
            this.builderProvider5 = NightDisplayListenerModule_Builder_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBgHandlerProvider);
            this.nightDisplayTileProvider = NightDisplayTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.tvGlobalRootComponentImpl.provideColorDisplayManagerProvider, this.builderProvider5);
            this.nfcTileProvider = NfcTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider, this.nfcControllerImplProvider);
            this.memoryTileProvider = GarbageMonitor_MemoryTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.garbageMonitorProvider);
            this.uiModeNightTileProvider = UiModeNightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.configurationControllerImplProvider, this.provideBatteryControllerProvider, this.locationControllerImplProvider);
            this.screenRecordTileProvider = ScreenRecordTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.recordingControllerProvider, this.keyguardDismissUtilProvider, this.keyguardStateControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            Provider<Boolean> provider10 = DoubleCheck.provider(QSFlagsModule_IsReduceBrightColorsAvailableFactory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.isReduceBrightColorsAvailableProvider = provider10;
            this.reduceBrightColorsTileProvider = ReduceBrightColorsTile_Factory.create(provider10, this.reduceBrightColorsControllerProvider, this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.cameraToggleTileProvider = CameraToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.providesControlsFeatureEnabledProvider = DoubleCheck.provider(ControlsModule_ProvidesControlsFeatureEnabledFactory.create(this.tvGlobalRootComponentImpl.providePackageManagerProvider));
            this.optionalOfControlsTileResourceConfigurationProvider = DaggerTvGlobalRootComponent.absentJdkOptionalProvider();
            this.controlsComponentProvider = DoubleCheck.provider(ControlsComponent_Factory.create(this.providesControlsFeatureEnabledProvider, this.tvGlobalRootComponentImpl.contextProvider, this.controlsControllerImplProvider, this.controlsUiControllerImplProvider, this.controlsListingControllerImplProvider, this.tvGlobalRootComponentImpl.provideLockPatternUtilsProvider, this.keyguardStateControllerImplProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.optionalOfControlsTileResourceConfigurationProvider));
            this.deviceControlsTileProvider = DeviceControlsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.controlsComponentProvider, this.keyguardStateControllerImplProvider);
            this.alarmTileProvider = AlarmTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.nextAlarmControllerImplProvider);
            this.provideQuickAccessWalletClientProvider = DoubleCheck.provider(WalletModule_ProvideQuickAccessWalletClientFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider));
            this.quickAccessWalletControllerProvider = DoubleCheck.provider(QuickAccessWalletController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.provideQuickAccessWalletClientProvider, this.bindSystemClockProvider));
            this.quickAccessWalletTileProvider = QuickAccessWalletTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.keyguardStateControllerImplProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.secureSettingsImplProvider, this.quickAccessWalletControllerProvider);
            this.qRCodeScannerControllerProvider = DoubleCheck.provider(QRCodeScannerController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider));
            this.qRCodeScannerTileProvider = QRCodeScannerTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.qRCodeScannerControllerProvider);
            this.oneHandedModeTileProvider = OneHandedModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.batteryShareControllerImplProvider = DoubleCheck.provider(BatteryShareControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.provideBatteryControllerProvider));
            this.batteryShareTileProvider = BatteryShareTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.batteryShareControllerImplProvider);
        }

        private void initialize7(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.glyphsControllerImplProvider = DoubleCheck.provider(GlyphsControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider));
            this.glyphsTileProvider = GlyphsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.glyphsControllerImplProvider);
            ColorCorrectionTile_Factory create = ColorCorrectionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.tvGlobalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.colorCorrectionTileProvider = create;
            this.qSFactoryImplProvider = DoubleCheck.provider(QSFactoryImpl_Factory.create(this.qSTileHostProvider, this.builderProvider4, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qRCodeScannerTileProvider, this.oneHandedModeTileProvider, this.batteryShareTileProvider, this.glyphsTileProvider, create));
            this.builderProvider6 = DoubleCheck.provider(AutoAddTracker_Builder_Factory.create(this.secureSettingsImplProvider, this.broadcastDispatcherProvider, this.qSTileHostProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBackgroundExecutorProvider));
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(DeviceControlsControllerImpl_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.walletControllerImplProvider = DoubleCheck.provider(WalletControllerImpl_Factory.create(this.provideQuickAccessWalletClientProvider));
            this.safetyControllerProvider = SafetyController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.tvGlobalRootComponentImpl.provideSafetyCenterManagerProvider, this.provideBgHandlerProvider);
            this.provideAutoTileManagerProvider = QSModule_ProvideAutoTileManagerFactory.create(this.tvGlobalRootComponentImpl.contextProvider, this.builderProvider6, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.reduceBrightColorsControllerProvider, this.deviceControlsControllerImplProvider, this.walletControllerImplProvider, this.safetyControllerProvider, this.isReduceBrightColorsAvailableProvider);
            this.builderProvider7 = DoubleCheck.provider(TileServiceRequestController_Builder_Factory.create(this.provideCommandQueueProvider, this.commandRegistryProvider));
            this.packageManagerAdapterProvider = PackageManagerAdapter_Factory.create(this.tvGlobalRootComponentImpl.contextProvider);
            C4834TileLifecycleManager_Factory create2 = C4834TileLifecycleManager_Factory.create(this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.tvGlobalRootComponentImpl.contextProvider, this.tileServicesProvider, this.packageManagerAdapterProvider, this.broadcastDispatcherProvider);
            this.tileLifecycleManagerProvider = create2;
            this.factoryProvider9 = TileLifecycleManager_Factory_Impl.create(create2);
            DelegateFactory.setDelegate(this.qSTileHostProvider, DoubleCheck.provider(QSTileHost_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplProvider, this.tvGlobalRootComponentImpl.provideMainHandlerProvider, this.provideBgLooperProvider, this.tvGlobalRootComponentImpl.providesPluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.qSLoggerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider, this.builderProvider7, this.factoryProvider9)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQuickQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.provideQSFragmentDisableLogBufferProvider = DoubleCheck.provider(LogModule_ProvideQSFragmentDisableLogBufferFactory.create(this.logBufferFactoryProvider));
            this.disableFlagsLoggerProvider = DoubleCheck.provider(DisableFlagsLogger_Factory.create());
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(this.tvGlobalRootComponentImpl.providePermissionManagerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(C2359xf95dc14f.create());
            UserDetailView_Adapter_Factory create3 = UserDetailView_Adapter_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.userSwitcherControllerProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider, this.falsingManagerProxyProvider);
            this.adapterProvider = create3;
            this.userSwitchDialogControllerProvider = DoubleCheck.provider(UserSwitchDialogController_Factory.create(create3, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.provideDialogLaunchAnimatorProvider, this.tvGlobalRootComponentImpl.provideUiEventLoggerProvider));
            this.fgsManagerControllerProvider = DoubleCheck.provider(FgsManagerController_Factory.create(this.tvGlobalRootComponentImpl.contextProvider, this.tvGlobalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bindSystemClockProvider, this.tvGlobalRootComponentImpl.provideIActivityManagerProvider, this.tvGlobalRootComponentImpl.providePackageManagerProvider, this.provideUserTrackerProvider, this.deviceConfigProxyProvider, this.provideDialogLaunchAnimatorProvider, this.broadcastDispatcherProvider, this.tvGlobalRootComponentImpl.dumpManagerProvider));
            this.isPMLiteEnabledProvider = DoubleCheck.provider(QSFlagsModule_IsPMLiteEnabledFactory.create(this.featureFlagsReleaseProvider, this.globalSettingsImplProvider));
        }

        public BootCompleteCacheImpl provideBootCacheImpl() {
            return this.bootCompleteCacheImplProvider.get();
        }

        public ConfigurationController getConfigurationController() {
            return this.configurationControllerImplProvider.get();
        }

        public ContextComponentHelper getContextComponentHelper() {
            return this.contextComponentResolverProvider.get();
        }

        public UnfoldLatencyTracker getUnfoldLatencyTracker() {
            return this.unfoldLatencyTrackerProvider.get();
        }

        public Optional<FoldStateLoggingProvider> getFoldStateLoggingProvider() {
            return (Optional) this.tvGlobalRootComponentImpl.providesFoldStateLoggingProvider.get();
        }

        public Optional<FoldStateLogger> getFoldStateLogger() {
            return (Optional) this.tvGlobalRootComponentImpl.providesFoldStateLoggerProvider.get();
        }

        public Dependency createDependency() {
            return this.dependencyProvider.get();
        }

        public NTDependencyEx createDependencyEx() {
            return this.nTDependencyExProvider.get();
        }

        public DumpManager createDumpManager() {
            return (DumpManager) this.tvGlobalRootComponentImpl.dumpManagerProvider.get();
        }

        public InitController getInitController() {
            return this.initControllerProvider.get();
        }

        public Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent() {
            return this.provideSysUIUnfoldComponentProvider.get();
        }

        public Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider() {
            return (Optional) this.tvGlobalRootComponentImpl.provideNaturalRotationProgressProvider.get();
        }

        public Optional<MediaTttChipControllerSender> getMediaTttChipControllerSender() {
            return this.providesMediaTttChipControllerSenderProvider.get();
        }

        public Optional<MediaTttChipControllerReceiver> getMediaTttChipControllerReceiver() {
            return this.providesMediaTttChipControllerReceiverProvider.get();
        }

        public Optional<MediaTttCommandLineHelper> getMediaTttCommandLineHelper() {
            return this.providesMediaTttCommandLineHelperProvider.get();
        }

        public Optional<MediaMuteAwaitConnectionCli> getMediaMuteAwaitConnectionCli() {
            return this.providesMediaMuteAwaitConnectionCliProvider.get();
        }

        public Optional<NearbyMediaDevicesManager> getNearbyMediaDevicesManager() {
            return this.providesNearbyMediaDevicesManagerProvider.get();
        }

        public Map<Class<?>, Provider<CoreStartable>> getStartables() {
            return MapBuilder.newMapBuilder(21).put(BroadcastDispatcherStartable.class, this.broadcastDispatcherStartableProvider).put(KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put(GlobalActionsComponent.class, this.globalActionsComponentProvider).put(HomeSoundEffectController.class, this.homeSoundEffectControllerProvider).put(InstantAppNotifier.class, this.instantAppNotifierProvider).put(KeyboardUI.class, this.keyboardUIProvider).put(PowerUI.class, this.powerUIProvider).put(RingtonePlayer.class, this.ringtonePlayerProvider).put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider).put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider).put(StorageNotification.class, this.storageNotificationProvider).put(ThemeOverlayController.class, this.themeOverlayControllerProvider).put(ToastUI.class, this.toastUIProvider).put(TvNotificationHandler.class, this.provideTvNotificationHandlerProvider).put(TvNotificationPanel.class, this.tvNotificationPanelProvider).put(TvOngoingPrivacyChip.class, this.tvOngoingPrivacyChipProvider).put(TvStatusBar.class, this.tvStatusBarProvider).put(VolumeUI.class, this.volumeUIProvider).put(VpnStatusObserver.class, this.vpnStatusObserverProvider).put(WindowMagnification.class, this.windowMagnificationProvider).put(WMShell.class, this.wMShellProvider).build();
        }

        public Map<Class<?>, Provider<CoreStartable>> getPerUserStartables() {
            return Collections.singletonMap(NotificationChannels.class, this.notificationChannelsProvider);
        }

        public void inject(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            injectSystemUIAppComponentFactory(systemUIAppComponentFactory);
        }

        public void inject(KeyguardSliceProvider keyguardSliceProvider) {
            injectKeyguardSliceProvider(keyguardSliceProvider);
        }

        public void inject(ClockOptionsProvider clockOptionsProvider) {
            injectClockOptionsProvider(clockOptionsProvider);
        }

        public void inject(PeopleProvider peopleProvider) {
            injectPeopleProvider(peopleProvider);
        }

        private SystemUIAppComponentFactory injectSystemUIAppComponentFactory(SystemUIAppComponentFactory systemUIAppComponentFactory) {
            SystemUIAppComponentFactory_MembersInjector.injectMComponentHelper(systemUIAppComponentFactory, this.contextComponentResolverProvider.get());
            return systemUIAppComponentFactory;
        }

        private KeyguardSliceProvider injectKeyguardSliceProvider(KeyguardSliceProvider keyguardSliceProvider) {
            KeyguardSliceProvider_MembersInjector.injectMDozeParameters(keyguardSliceProvider, this.dozeParametersProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMZenModeController(keyguardSliceProvider, this.zenModeControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMNextAlarmController(keyguardSliceProvider, this.nextAlarmControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMAlarmManager(keyguardSliceProvider, (AlarmManager) this.tvGlobalRootComponentImpl.provideAlarmManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMContentResolver(keyguardSliceProvider, (ContentResolver) this.tvGlobalRootComponentImpl.provideContentResolverProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMMediaManager(keyguardSliceProvider, this.provideNotificationMediaManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMStatusBarStateController(keyguardSliceProvider, this.statusBarStateControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardBypassController(keyguardSliceProvider, this.keyguardBypassControllerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardUpdateMonitor(keyguardSliceProvider, this.keyguardUpdateMonitorProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMKeyguardWeatherController(keyguardSliceProvider, this.keyguardWeatherControllerImplProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMCalendarManager(keyguardSliceProvider, this.calendarManagerProvider.get());
            return keyguardSliceProvider;
        }

        private ClockOptionsProvider injectClockOptionsProvider(ClockOptionsProvider clockOptionsProvider) {
            ClockOptionsProvider_MembersInjector.injectMClockInfosProvider(clockOptionsProvider, this.provideClockInfoListProvider);
            return clockOptionsProvider;
        }

        private PeopleProvider injectPeopleProvider(PeopleProvider peopleProvider) {
            PeopleProvider_MembersInjector.injectMPeopleSpaceWidgetManager(peopleProvider, this.peopleSpaceWidgetManagerProvider.get());
            return peopleProvider;
        }
    }

    /* renamed from: com.android.systemui.tv.DaggerTvGlobalRootComponent$TvGlobalRootComponentImpl */
    private static final class TvGlobalRootComponentImpl implements TvGlobalRootComponent {
        /* access modifiers changed from: private */
        public Provider<AODController> aODControllerProvider;
        private Provider<ATraceLoggerTransitionProgressListener> aTraceLoggerTransitionProgressListenerProvider;
        /* access modifiers changed from: private */
        public Provider<AuthRippleControllerEx> authRippleControllerExProvider;
        private final Context context;
        /* access modifiers changed from: private */
        public Provider<Context> contextProvider;
        private Provider<DeviceFoldStateProvider> deviceFoldStateProvider;
        /* access modifiers changed from: private */
        public Provider<DumpManager> dumpManagerProvider;
        private Provider<ScaleAwareTransitionProgressProvider.Factory> factoryProvider;
        private final GlobalModule globalModule;
        private Provider<HingeAngleProvider> hingeAngleProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardUpdateMonitorEx> keyguardUpdateMonitorExProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardViewMediatorEx> keyguardViewMediatorExProvider;
        private Provider<LifecycleScreenStatusProvider> lifecycleScreenStatusProvider;
        /* access modifiers changed from: private */
        public Provider<PluginDependencyProvider> pluginDependencyProvider;
        private Provider<PluginEnablerImpl> pluginEnablerImplProvider;
        /* access modifiers changed from: private */
        public Provider<AccessibilityManager> provideAccessibilityManagerProvider;
        /* access modifiers changed from: private */
        public Provider<ActivityManager> provideActivityManagerProvider;
        /* access modifiers changed from: private */
        public Provider<ActivityTaskManager> provideActivityTaskManagerProvider;
        /* access modifiers changed from: private */
        public Provider<AlarmManager> provideAlarmManagerProvider;
        /* access modifiers changed from: private */
        public Provider<AmbientDisplayConfiguration> provideAmbientDisplayConfigurationProvider;
        /* access modifiers changed from: private */
        public Provider<AudioManager> provideAudioManagerProvider;
        /* access modifiers changed from: private */
        public Provider<CaptioningManager> provideCaptioningManagerProvider;
        /* access modifiers changed from: private */
        public Provider<CarrierConfigManager> provideCarrierConfigManagerProvider;
        /* access modifiers changed from: private */
        public Provider<ColorDisplayManager> provideColorDisplayManagerProvider;
        /* access modifiers changed from: private */
        public Provider<ConnectivityManager> provideConnectivityManagagerProvider;
        /* access modifiers changed from: private */
        public Provider<ContentResolver> provideContentResolverProvider;
        /* access modifiers changed from: private */
        public Provider<CrossWindowBlurListeners> provideCrossWindowBlurListenersProvider;
        /* access modifiers changed from: private */
        public Provider<DevicePolicyManager> provideDevicePolicyManagerProvider;
        /* access modifiers changed from: private */
        public Provider<DeviceStateManager> provideDeviceStateManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Integer> provideDisplayIdProvider;
        /* access modifiers changed from: private */
        public Provider<DisplayManager> provideDisplayManagerProvider;
        /* access modifiers changed from: private */
        public Provider<DisplayMetrics> provideDisplayMetricsProvider;
        /* access modifiers changed from: private */
        public Provider<Execution> provideExecutionProvider;
        /* access modifiers changed from: private */
        public Provider<FaceManager> provideFaceManagerProvider;
        private Provider<FoldStateProvider> provideFoldStateProvider;
        /* access modifiers changed from: private */
        public Provider<IActivityManager> provideIActivityManagerProvider;
        /* access modifiers changed from: private */
        public Provider<IAudioService> provideIAudioServiceProvider;
        /* access modifiers changed from: private */
        public Provider<IDreamManager> provideIDreamManagerProvider;
        /* access modifiers changed from: private */
        public Provider<INotificationManager> provideINotificationManagerProvider;
        /* access modifiers changed from: private */
        public Provider<IStatusBarService> provideIStatusBarServiceProvider;
        /* access modifiers changed from: private */
        public Provider<IWindowManager> provideIWindowManagerProvider;
        /* access modifiers changed from: private */
        public Provider<InputMethodManager> provideInputMethodManagerProvider;
        /* access modifiers changed from: private */
        public Provider<InteractionJankMonitor> provideInteractionJankMonitorProvider;
        /* access modifiers changed from: private */
        public Provider<Boolean> provideIsTestHarnessProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardManager> provideKeyguardManagerProvider;
        /* access modifiers changed from: private */
        public Provider<LatencyTracker> provideLatencyTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<LauncherApps> provideLauncherAppsProvider;
        /* access modifiers changed from: private */
        public Provider<LockPatternUtils> provideLockPatternUtilsProvider;
        /* access modifiers changed from: private */
        public Provider<DelayableExecutor> provideMainDelayableExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<Executor> provideMainExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<Handler> provideMainHandlerProvider;
        /* access modifiers changed from: private */
        public Provider<MediaProjectionManager> provideMediaProjectionManagerProvider;
        /* access modifiers changed from: private */
        public Provider<MediaRouter2Manager> provideMediaRouter2ManagerProvider;
        /* access modifiers changed from: private */
        public Provider<MediaSessionManager> provideMediaSessionManagerProvider;
        /* access modifiers changed from: private */
        public Provider<MetricsLogger> provideMetricsLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<NaturalRotationUnfoldProgressProvider>> provideNaturalRotationProgressProvider;
        /* access modifiers changed from: private */
        public Provider<NetworkScoreManager> provideNetworkScoreManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationManager> provideNotificationManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationMessagingUtil> provideNotificationMessagingUtilProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<TelecomManager>> provideOptionalTelecomManagerProvider;
        /* access modifiers changed from: private */
        public Provider<OverlayManager> provideOverlayManagerProvider;
        /* access modifiers changed from: private */
        public Provider<PackageManager> providePackageManagerProvider;
        /* access modifiers changed from: private */
        public Provider<PackageManagerWrapper> providePackageManagerWrapperProvider;
        /* access modifiers changed from: private */
        public Provider<PermissionManager> providePermissionManagerProvider;
        private Provider<PluginActionManager.Factory> providePluginInstanceManagerFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<PowerExemptionManager> providePowerExemptionManagerProvider;
        /* access modifiers changed from: private */
        public Provider<PowerManager> providePowerManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Resources> provideResourcesProvider;
        /* access modifiers changed from: private */
        public Provider<SafetyCenterManager> provideSafetyCenterManagerProvider;
        /* access modifiers changed from: private */
        public Provider<SensorPrivacyManager> provideSensorPrivacyManagerProvider;
        /* access modifiers changed from: private */
        public Provider<SharedPreferences> provideSharePreferencesProvider;
        /* access modifiers changed from: private */
        public Provider<ShellUnfoldProgressProvider> provideShellProgressProvider;
        /* access modifiers changed from: private */
        public Provider<ShortcutManager> provideShortcutManagerProvider;
        /* access modifiers changed from: private */
        public Provider<SmartspaceManager> provideSmartspaceManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provideStatusBarScopedTransitionProvider;
        /* access modifiers changed from: private */
        public Provider<SubscriptionManager> provideSubcriptionManagerProvider;
        /* access modifiers changed from: private */
        public Provider<TelecomManager> provideTelecomManagerProvider;
        /* access modifiers changed from: private */
        public Provider<TelephonyManager> provideTelephonyManagerProvider;
        /* access modifiers changed from: private */
        public Provider<TrustManager> provideTrustManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Executor> provideUiBackgroundExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<UiEventLogger> provideUiEventLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<UiModeManager> provideUiModeManagerProvider;
        private Provider<UnfoldTransitionConfig> provideUnfoldTransitionConfigProvider;
        /* access modifiers changed from: private */
        public Provider<UserManager> provideUserManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Vibrator> provideVibratorProvider;
        /* access modifiers changed from: private */
        public Provider<ViewConfiguration> provideViewConfigurationProvider;
        /* access modifiers changed from: private */
        public Provider<WallpaperManager> provideWallpaperManagerProvider;
        /* access modifiers changed from: private */
        public Provider<WifiManager> provideWifiManagerProvider;
        /* access modifiers changed from: private */
        public Provider<WindowManager> provideWindowManagerProvider;
        /* access modifiers changed from: private */
        public Provider<LayoutInflater> providerLayoutInflaterProvider;
        /* access modifiers changed from: private */
        public Provider<Choreographer> providesChoreographerProvider;
        /* access modifiers changed from: private */
        public Provider<FingerprintManager> providesFingerprintManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<FoldStateLogger>> providesFoldStateLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<FoldStateLoggingProvider>> providesFoldStateLoggingProvider;
        private Provider<Executor> providesPluginExecutorProvider;
        private Provider<PluginInstance.Factory> providesPluginInstanceFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<PluginManager> providesPluginManagerProvider;
        private Provider<PluginPrefs> providesPluginPrefsProvider;
        private Provider<List<String>> providesPrivilegedPluginsProvider;
        /* access modifiers changed from: private */
        public Provider<SensorManager> providesSensorManagerProvider;
        /* access modifiers changed from: private */
        public Provider<QSExpansionPathInterpolator> qSExpansionPathInterpolatorProvider;
        private C4842ScaleAwareTransitionProgressProvider_Factory scaleAwareTransitionProgressProvider;
        /* access modifiers changed from: private */
        public Provider<ScreenLifecycle> screenLifecycleProvider;
        private Provider<ScreenStatusProvider> screenStatusProvider;
        private Provider<String> tracingTagPrefixProvider;
        private final TvGlobalRootComponentImpl tvGlobalRootComponentImpl;
        /* access modifiers changed from: private */
        public Provider<UncaughtExceptionPreHandlerManager> uncaughtExceptionPreHandlerManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;

        private TvGlobalRootComponentImpl(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
            this.tvGlobalRootComponentImpl = this;
            this.context = context2;
            this.globalModule = globalModule2;
            initialize(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
            initialize2(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
        }

        /* access modifiers changed from: private */
        public Resources mainResources() {
            return FrameworkServicesModule_ProvideResourcesFactory.provideResources(this.context);
        }

        /* access modifiers changed from: private */
        public DisplayMetrics displayMetrics() {
            return GlobalModule_ProvideDisplayMetricsFactory.provideDisplayMetrics(this.globalModule, this.context);
        }

        /* access modifiers changed from: private */
        public Handler mainHandler() {
            return GlobalConcurrencyModule_ProvideMainHandlerFactory.provideMainHandler(GlobalConcurrencyModule_ProvideMainLooperFactory.provideMainLooper());
        }

        private void initialize(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
            this.contextProvider = InstanceFactory.create(context2);
            this.provideIWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIWindowManagerFactory.create());
            this.provideUiEventLoggerProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideUiEventLoggerFactory.create());
            this.provideUnfoldTransitionConfigProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvideUnfoldTransitionConfigFactory.create(unfoldTransitionModule, this.contextProvider));
            Provider<ContentResolver> provider = DoubleCheck.provider(FrameworkServicesModule_ProvideContentResolverFactory.create(this.contextProvider));
            this.provideContentResolverProvider = provider;
            C4842ScaleAwareTransitionProgressProvider_Factory create = C4842ScaleAwareTransitionProgressProvider_Factory.create(provider);
            this.scaleAwareTransitionProgressProvider = create;
            this.factoryProvider = ScaleAwareTransitionProgressProvider_Factory_Impl.create(create);
            UnfoldTransitionModule_TracingTagPrefixFactory create2 = UnfoldTransitionModule_TracingTagPrefixFactory.create(unfoldTransitionModule);
            this.tracingTagPrefixProvider = create2;
            this.aTraceLoggerTransitionProgressListenerProvider = ATraceLoggerTransitionProgressListener_Factory.create(create2);
            this.providesSensorManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesSensorManagerFactory.create(this.contextProvider));
            Provider<Executor> provider2 = DoubleCheck.provider(GlobalConcurrencyModule_ProvideUiBackgroundExecutorFactory.create());
            this.provideUiBackgroundExecutorProvider = provider2;
            this.hingeAngleProvider = UnfoldSharedModule_HingeAngleProviderFactory.create(unfoldSharedModule, this.provideUnfoldTransitionConfigProvider, this.providesSensorManagerProvider, provider2);
            Provider<DumpManager> provider3 = DoubleCheck.provider(DumpManager_Factory.create());
            this.dumpManagerProvider = provider3;
            Provider<ScreenLifecycle> provider4 = DoubleCheck.provider(ScreenLifecycle_Factory.create(provider3));
            this.screenLifecycleProvider = provider4;
            Provider<LifecycleScreenStatusProvider> provider5 = DoubleCheck.provider(LifecycleScreenStatusProvider_Factory.create(provider4));
            this.lifecycleScreenStatusProvider = provider5;
            this.screenStatusProvider = UnfoldTransitionModule_ScreenStatusProviderFactory.create(unfoldTransitionModule, provider5);
            this.provideDeviceStateManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDeviceStateManagerFactory.create(this.contextProvider));
            this.provideActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityManagerFactory.create(this.contextProvider));
            this.provideMainExecutorProvider = DoubleCheck.provider(GlobalConcurrencyModule_ProvideMainExecutorFactory.create(this.contextProvider));
            GlobalConcurrencyModule_ProvideMainHandlerFactory create3 = GlobalConcurrencyModule_ProvideMainHandlerFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.provideMainHandlerProvider = create3;
            DeviceFoldStateProvider_Factory create4 = DeviceFoldStateProvider_Factory.create(this.contextProvider, this.hingeAngleProvider, this.screenStatusProvider, this.provideDeviceStateManagerProvider, this.provideActivityManagerProvider, this.provideMainExecutorProvider, create3);
            this.deviceFoldStateProvider = create4;
            Provider<FoldStateProvider> provider6 = DoubleCheck.provider(UnfoldSharedModule_ProvideFoldStateProviderFactory.create(unfoldSharedModule, create4));
            this.provideFoldStateProvider = provider6;
            Provider<Optional<UnfoldTransitionProgressProvider>> provider7 = DoubleCheck.provider(UnfoldSharedModule_UnfoldTransitionProgressProviderFactory.create(unfoldSharedModule, this.provideUnfoldTransitionConfigProvider, this.factoryProvider, this.aTraceLoggerTransitionProgressListenerProvider, provider6));
            this.unfoldTransitionProgressProvider = provider7;
            this.provideShellProgressProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvideShellProgressProviderFactory.create(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, provider7));
            this.providePackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerFactory.create(this.contextProvider));
            this.provideUserManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUserManagerFactory.create(this.contextProvider));
            this.provideMetricsLoggerProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideMetricsLoggerFactory.create(androidInternalsModule));
            this.providesPluginExecutorProvider = DoubleCheck.provider(PluginsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.create()));
            this.provideNotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNotificationManagerFactory.create(this.contextProvider));
            this.pluginEnablerImplProvider = DoubleCheck.provider(PluginEnablerImpl_Factory.create(this.contextProvider, this.providePackageManagerProvider));
            PluginsModule_ProvidesPrivilegedPluginsFactory create5 = PluginsModule_ProvidesPrivilegedPluginsFactory.create(this.contextProvider);
            this.providesPrivilegedPluginsProvider = create5;
            Provider<PluginInstance.Factory> provider8 = DoubleCheck.provider(PluginsModule_ProvidesPluginInstanceFactoryFactory.create(create5, PluginsModule_ProvidesPluginDebugFactory.create()));
            this.providesPluginInstanceFactoryProvider = provider8;
            this.providePluginInstanceManagerFactoryProvider = DoubleCheck.provider(PluginsModule_ProvidePluginInstanceManagerFactoryFactory.create(this.contextProvider, this.providePackageManagerProvider, this.provideMainExecutorProvider, this.providesPluginExecutorProvider, this.provideNotificationManagerProvider, this.pluginEnablerImplProvider, this.providesPrivilegedPluginsProvider, provider8));
            this.uncaughtExceptionPreHandlerManagerProvider = DoubleCheck.provider(UncaughtExceptionPreHandlerManager_Factory.create());
            this.providesPluginPrefsProvider = PluginsModule_ProvidesPluginPrefsFactory.create(this.contextProvider);
            this.providesPluginManagerProvider = DoubleCheck.provider(PluginsModule_ProvidesPluginManagerFactory.create(this.contextProvider, this.providePluginInstanceManagerFactoryProvider, PluginsModule_ProvidesPluginDebugFactory.create(), this.uncaughtExceptionPreHandlerManagerProvider, this.pluginEnablerImplProvider, this.providesPluginPrefsProvider, this.providesPrivilegedPluginsProvider));
            this.provideDisplayMetricsProvider = GlobalModule_ProvideDisplayMetricsFactory.create(globalModule2, this.contextProvider);
            this.providePowerManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerManagerFactory.create(this.contextProvider));
            this.provideViewConfigurationProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideViewConfigurationFactory.create(this.contextProvider));
            this.provideResourcesProvider = FrameworkServicesModule_ProvideResourcesFactory.create(this.contextProvider);
            this.provideInteractionJankMonitorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInteractionJankMonitorFactory.create());
            this.provideLockPatternUtilsProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideLockPatternUtilsFactory.create(androidInternalsModule, this.contextProvider));
            this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.create());
            this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.create());
            this.provideWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWindowManagerFactory.create(this.contextProvider));
            this.providesFingerprintManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesFingerprintManagerFactory.create(this.contextProvider));
            this.provideFaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideFaceManagerFactory.create(this.contextProvider));
            this.providerLayoutInflaterProvider = DoubleCheck.provider(FrameworkServicesModule_ProviderLayoutInflaterFactory.create(frameworkServicesModule, this.contextProvider));
            this.provideMainDelayableExecutorProvider = DoubleCheck.provider(GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            this.provideTrustManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTrustManagerFactory.create(this.contextProvider));
            this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.create());
            this.provideDevicePolicyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDevicePolicyManagerFactory.create(this.contextProvider));
            this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providesPluginManagerProvider));
            this.provideTelephonyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelephonyManagerFactory.create(this.contextProvider));
            this.provideLatencyTrackerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLatencyTrackerFactory.create(this.contextProvider));
            this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.create());
            this.provideAmbientDisplayConfigurationProvider = C2055x19afde28.create(frameworkServicesModule, this.contextProvider);
            Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider9 = DoubleCheck.provider(C3249xd9485442.create(unfoldTransitionModule, this.contextProvider, this.provideIWindowManagerProvider, this.unfoldTransitionProgressProvider));
            this.provideNaturalRotationProgressProvider = provider9;
            this.provideStatusBarScopedTransitionProvider = DoubleCheck.provider(C3250x6e72e9f0.create(unfoldTransitionModule, provider9));
            this.provideIStatusBarServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIStatusBarServiceFactory.create());
            this.providesChoreographerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesChoreographerFactory.create(frameworkServicesModule));
            this.provideNotificationMessagingUtilProvider = AndroidInternalsModule_ProvideNotificationMessagingUtilFactory.create(androidInternalsModule, this.contextProvider);
            this.provideLauncherAppsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLauncherAppsFactory.create(this.contextProvider));
            this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.create());
            this.provideKeyguardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideKeyguardManagerFactory.create(this.contextProvider));
            this.provideAlarmManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAlarmManagerFactory.create(this.contextProvider));
            this.provideMediaSessionManagerProvider = FrameworkServicesModule_ProvideMediaSessionManagerFactory.create(this.contextProvider);
            this.provideMediaRouter2ManagerProvider = FrameworkServicesModule_ProvideMediaRouter2ManagerFactory.create(this.contextProvider);
            this.provideAccessibilityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAccessibilityManagerFactory.create(this.contextProvider));
            this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.create());
            this.provideWallpaperManagerProvider = FrameworkServicesModule_ProvideWallpaperManagerFactory.create(this.contextProvider);
            this.provideAudioManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAudioManagerFactory.create(this.contextProvider));
            this.providePowerExemptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerExemptionManagerFactory.create(this.contextProvider));
            this.provideVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideVibratorFactory.create(this.contextProvider));
            this.provideDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDisplayManagerFactory.create(this.contextProvider));
            this.provideIsTestHarnessProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIsTestHarnessFactory.create());
            this.provideINotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideINotificationManagerFactory.create(frameworkServicesModule));
            this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSensorPrivacyManagerFactory.create(this.contextProvider));
            this.provideSharePreferencesProvider = FrameworkServicesModule_ProvideSharePreferencesFactory.create(frameworkServicesModule, this.contextProvider);
            this.provideTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelecomManagerFactory.create(this.contextProvider));
            this.provideOverlayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOverlayManagerFactory.create(this.contextProvider));
            this.provideMediaProjectionManagerProvider = FrameworkServicesModule_ProvideMediaProjectionManagerFactory.create(this.contextProvider);
            this.provideIAudioServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIAudioServiceFactory.create());
            this.provideCaptioningManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCaptioningManagerFactory.create(this.contextProvider));
            Provider<Optional<FoldStateLoggingProvider>> provider10 = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory.create(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, this.provideFoldStateProvider));
            this.providesFoldStateLoggingProvider = provider10;
            this.providesFoldStateLoggerProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggerFactory.create(unfoldTransitionModule, provider10));
            this.provideColorDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideColorDisplayManagerFactory.create(this.contextProvider));
            this.provideSubcriptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSubcriptionManagerFactory.create(this.contextProvider));
            this.provideConnectivityManagagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideConnectivityManagagerFactory.create(this.contextProvider));
            this.provideWifiManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWifiManagerFactory.create(this.contextProvider));
            this.provideCarrierConfigManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCarrierConfigManagerFactory.create(this.contextProvider));
            this.provideNetworkScoreManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNetworkScoreManagerFactory.create(this.contextProvider));
            this.provideShortcutManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideShortcutManagerFactory.create(this.contextProvider));
            this.aODControllerProvider = DoubleCheck.provider(AODController_Factory.create(this.contextProvider));
            Provider<KeyguardUpdateMonitorEx> provider11 = DoubleCheck.provider(KeyguardUpdateMonitorEx_Factory.create());
            this.keyguardUpdateMonitorExProvider = provider11;
            this.keyguardViewMediatorExProvider = DoubleCheck.provider(KeyguardViewMediatorEx_Factory.create(this.providePowerManagerProvider, provider11));
            this.authRippleControllerExProvider = DoubleCheck.provider(AuthRippleControllerEx_Factory.create(this.contextProvider));
            this.provideOptionalTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalTelecomManagerFactory.create(this.contextProvider));
            this.provideInputMethodManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInputMethodManagerFactory.create(this.contextProvider));
            this.provideSmartspaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSmartspaceManagerFactory.create(this.contextProvider));
        }

        private void initialize2(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
            this.provideUiModeManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUiModeManagerFactory.create(this.contextProvider));
            this.provideDisplayIdProvider = FrameworkServicesModule_ProvideDisplayIdFactory.create(this.contextProvider);
            this.provideSafetyCenterManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSafetyCenterManagerFactory.create(this.contextProvider));
            this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.create());
            this.providePermissionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePermissionManagerFactory.create(this.contextProvider));
        }

        public TvWMComponent.Builder getWMComponentBuilder() {
            return new TvWMComponentBuilder(this.tvGlobalRootComponentImpl);
        }

        public TvSysUIComponent.Builder getSysUIComponent() {
            return new TvSysUIComponentBuilder(this.tvGlobalRootComponentImpl);
        }
    }
}
