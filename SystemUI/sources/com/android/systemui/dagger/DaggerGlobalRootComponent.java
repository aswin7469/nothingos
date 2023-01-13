package com.android.systemui.dagger;

import android.animation.AnimationHandler;
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
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.om.OverlayManager;
import android.content.pm.IPackageManager;
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
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import com.android.internal.app.AssistUtils;
import com.android.internal.app.IBatteryStats;
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
import com.android.keyguard.CarrierText;
import com.android.keyguard.CarrierTextController;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.CarrierTextManager_Builder_Factory;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.EmergencyButtonController_Factory_Factory;
import com.android.keyguard.KeyguardBiometricLockoutLogger;
import com.android.keyguard.KeyguardBiometricLockoutLogger_Factory;
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
import com.android.keyguard.LockIconView;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.LockIconViewController_Factory;
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
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetBatteryMeterViewFactory;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetCarrierTextFactory;
import com.android.keyguard.dagger.KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardClockSwitchFactory;
import com.android.keyguard.dagger.KeyguardStatusViewModule_GetKeyguardSliceViewFactory;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
import com.android.keyguard.mediator.ScreenOnCoordinator;
import com.android.keyguard.mediator.ScreenOnCoordinator_Factory;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.RootDisplayAreaOrganizer;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;
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
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.FloatingContentCoordinator;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.SyncTransactionQueue;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.common.TaskStackListenerImpl;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.compatui.CompatUI;
import com.android.p019wm.shell.compatui.CompatUIController;
import com.android.p019wm.shell.dagger.C3463x374a97b;
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
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDisplayLayoutFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropControllerFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropFactory;
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideFloatingContentCoordinatorFactory;
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
import com.android.p019wm.shell.dagger.WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory;
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
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideShellMainHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideAppPairsFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideBubbleControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideFreeformTaskListenerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideFullscreenUnfoldControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideLegacySplitScreenFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideOneHandedControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipAnimationControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipAppOpsListenerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipBoundsStateFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipMotionHelperFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipParamsChangedForwarderFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipSnapAlgorithmFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipTaskOrganizerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipTouchHandlerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipTransitionControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidePipTransitionStateFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideSplitScreenControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideStageTaskUnfoldControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvideUnfoldBackgroundControllerFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidesPipBoundsAlgorithmFactory;
import com.android.p019wm.shell.dagger.WMShellModule_ProvidesPipPhoneMenuControllerFactory;
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
import com.android.p019wm.shell.pip.PipBoundsAlgorithm;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipParamsChangedForwarder;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipTransitionController;
import com.android.p019wm.shell.pip.PipTransitionState;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import com.android.p019wm.shell.pip.phone.PhonePipMenuController;
import com.android.p019wm.shell.pip.phone.PipMotionHelper;
import com.android.p019wm.shell.pip.phone.PipTouchHandler;
import com.android.p019wm.shell.recents.RecentTasks;
import com.android.p019wm.shell.recents.RecentTasksController;
import com.android.p019wm.shell.splitscreen.SplitScreen;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import com.android.p019wm.shell.splitscreen.StageTaskUnfoldController;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.android.p019wm.shell.startingsurface.StartingWindowController;
import com.android.p019wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.p019wm.shell.transition.ShellTransitions;
import com.android.p019wm.shell.transition.Transitions;
import com.android.p019wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.p019wm.shell.unfold.UnfoldBackgroundController;
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
import com.android.systemui.LatencyTester;
import com.android.systemui.LatencyTester_Factory;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.ScreenDecorations_Factory;
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
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.biometrics.AuthRippleController_Factory;
import com.android.systemui.biometrics.AuthRippleView;
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
import com.android.systemui.clipboardoverlay.ClipboardListener;
import com.android.systemui.clipboardoverlay.ClipboardListener_Factory;
import com.android.systemui.clipboardoverlay.ClipboardOverlayControllerFactory;
import com.android.systemui.clipboardoverlay.ClipboardOverlayControllerFactory_Factory;
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
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory;
import com.android.systemui.decor.PrivacyDotDecorProviderFactory_Factory;
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
import com.android.systemui.flags.FeatureFlags;
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
import com.android.systemui.log.dagger.LogModule_ProvideCollapsedSbFragmentLogBufferFactory;
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
import com.android.systemui.log.dagger.LogModule_ProvideSwipeAwayGestureLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvideToastLogBufferFactory;
import com.android.systemui.log.dagger.LogModule_ProvidesMediaTimeoutListenerLogBufferFactory;
import com.android.systemui.lowlightclock.LowLightClockController;
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
import com.android.systemui.plugins.DarkIconDispatcher;
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
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController_Factory;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.systemui.recents.OverviewProxyRecentsImpl_Factory;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService_Factory;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.recents.RecentsModule_ProvideRecentsImplFactory;
import com.android.systemui.recents.ScreenPinningRequest;
import com.android.systemui.recents.ScreenPinningRequest_Factory;
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
import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.KeyguardIndicationController_Factory;
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
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeDepthController_Factory;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.NotificationShelfController_Factory;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.PulseExpansionHandler_Factory;
import com.android.systemui.statusbar.QsFrameTranslateImpl;
import com.android.systemui.statusbar.QsFrameTranslateImpl_Factory;
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
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.VibratorHelper_Factory;
import com.android.systemui.statusbar.charging.WiredChargingRippleController;
import com.android.systemui.statusbar.charging.WiredChargingRippleController_Factory;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.commandline.CommandRegistry_Factory;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl;
import com.android.systemui.statusbar.connectivity.AccessPointControllerImpl_WifiPickerTrackerFactory_Factory;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.CallbackHandler_Factory;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl_Factory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory;
import com.android.systemui.statusbar.connectivity.WifiStatusTrackerFactory_Factory;
import com.android.systemui.statusbar.core.StatusBarInitializer;
import com.android.systemui.statusbar.core.StatusBarInitializer_Factory;
import com.android.systemui.statusbar.dagger.C2631x512569cf;
import com.android.systemui.statusbar.dagger.C2632x721ac0b6;
import com.android.systemui.statusbar.dagger.C2633xbcfb28e4;
import com.android.systemui.statusbar.dagger.C2634xa11cf5e4;
import com.android.systemui.statusbar.dagger.C2635x5356ea10;
import com.android.systemui.statusbar.dagger.C2636xca2d86e7;
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
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler_Factory;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger_Factory;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.statusbar.gesture.TapGestureDetector_Factory;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController_Factory;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager_Factory;
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
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.NotificationClickerLogger;
import com.android.systemui.statusbar.notification.NotificationClickerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationClicker_Builder_Factory;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger_Factory;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationFilter_Factory;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider;
import com.android.systemui.statusbar.notification.NotificationLaunchAnimatorControllerProvider_Factory;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager_Factory;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.SectionClassifier_Factory;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider_Factory;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection_Factory;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl;
import com.android.systemui.statusbar.notification.collection.NotifInflaterImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.NotifPipeline_Factory;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager;
import com.android.systemui.statusbar.notification.collection.NotificationRankingManager_Factory;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder_Factory;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver;
import com.android.systemui.statusbar.notification.collection.TargetSdkResolver_Factory;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.AppOpsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.BubbleCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DataStoreCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DataStoreCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DebugModeCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.DeviceProvisionedCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.GutsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HideLocallyDismissedNotifsCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.HideNotifsForOtherUsersCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.KeyguardCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.MediaCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinators;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinatorsImpl;
import com.android.systemui.statusbar.notification.collection.coordinator.NotifCoordinatorsImpl_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.PreparationCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SensitiveContentCoordinatorImpl_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.SmartspaceDedupingCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.StackCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.StackCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.ViewConfigCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.ViewConfigCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator;
import com.android.systemui.statusbar.notification.collection.coordinator.VisualStabilityCoordinator_Factory;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsModule_NotifCoordinatorsFactory;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorsSubcomponent;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManagerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider;
import com.android.systemui.statusbar.notification.collection.inflation.NotifUiAdjustmentProvider_Factory;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl;
import com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl_Factory;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer;
import com.android.systemui.statusbar.notification.collection.init.NotifPipelineInitializer_Factory;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions_Factory;
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
import com.android.systemui.statusbar.notification.collection.render.NodeSpecBuilderLogger;
import com.android.systemui.statusbar.notification.collection.render.NodeSpecBuilderLogger_Factory;
import com.android.systemui.statusbar.notification.collection.render.NotifGutsViewManager;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn;
import com.android.systemui.statusbar.notification.collection.render.NotifViewBarn_Factory;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager_Factory;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewDifferLogger_Factory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManagerFactory_Impl;
import com.android.systemui.statusbar.notification.collection.render.ShadeViewManager_Factory;
import com.android.systemui.statusbar.notification.dagger.C2715x41b9fd82;
import com.android.systemui.statusbar.notification.dagger.C2716x30119a0;
import com.android.systemui.statusbar.notification.dagger.C2717x3fd4641;
import com.android.systemui.statusbar.notification.dagger.C2718x340f4262;
import com.android.systemui.statusbar.notification.dagger.C2719x8d68ee80;
import com.android.systemui.statusbar.notification.dagger.C2720xb614d321;
import com.android.systemui.statusbar.notification.dagger.C2721x812edf99;
import com.android.systemui.statusbar.notification.dagger.C2722xda791837;
import com.android.systemui.statusbar.notification.dagger.C2723x39c1fe98;
import com.android.systemui.statusbar.notification.dagger.C2724xcc90df13;
import com.android.systemui.statusbar.notification.dagger.C2725x9d7acab1;
import com.android.systemui.statusbar.notification.dagger.C2726x34a20792;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideCommonNotifCollectionFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupExpansionManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideGroupMembershipManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotifGutsViewManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotifShadeEventSourceFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationEntryManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationGutsManagerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationPanelLoggerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideNotificationsControllerFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideOnUserInteractionCallbackFactory;
import com.android.systemui.statusbar.notification.dagger.NotificationsModule_ProvideVisualStabilityManagerFactory;
import com.android.systemui.statusbar.notification.dagger.SectionHeaderControllerSubcomponent;
import com.android.systemui.statusbar.notification.icon.IconBuilder;
import com.android.systemui.statusbar.notification.icon.IconBuilder_Factory;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.icon.IconManager_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsController;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl;
import com.android.systemui.statusbar.notification.init.NotificationsControllerImpl_Factory;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub_Factory;
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
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineInitializer_Factory;
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
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AmbientState_Factory;
import com.android.systemui.statusbar.notification.stack.C2839xab7007e4;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLogger;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLogger_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator;
import com.android.systemui.statusbar.notification.stack.NotificationStackSizeCalculator_Factory;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper_Builder_Factory;
import com.android.systemui.statusbar.notification.stack.StackStateLogger;
import com.android.systemui.statusbar.notification.stack.StackStateLogger_Factory;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationHandler;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationHandler_Factory;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanelActivity_Factory;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.AutoHideController_Factory_Factory;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.BiometricUnlockController_Factory;
import com.android.systemui.statusbar.phone.C4838AutoHideController_Factory;
import com.android.systemui.statusbar.phone.C4839LightBarController_Factory;
import com.android.systemui.statusbar.phone.C4840LightBarTransitionsController_Factory;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks;
import com.android.systemui.statusbar.phone.CentralSurfacesCommandQueueCallbacks_Factory;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl;
import com.android.systemui.statusbar.phone.CentralSurfacesImpl_Factory;
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
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController_Factory;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.KeyguardBouncer_Factory_Factory;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardBypassController_Factory;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil_Factory;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl_Factory;
import com.android.systemui.statusbar.phone.KeyguardLiftController;
import com.android.systemui.statusbar.phone.KeyguardLiftController_Factory;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger_Factory;
import com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController;
import com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController_Factory;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.LightBarController_Factory_Factory;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController_Factory_Impl;
import com.android.systemui.statusbar.phone.LightsOutNotifController;
import com.android.systemui.statusbar.phone.LightsOutNotifController_Factory;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger_Factory;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.LockscreenWallpaper_Factory;
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
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_Factory;
import com.android.systemui.statusbar.phone.NotificationPanelViewController_PanelEventsEmitter_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl_Factory;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController_Factory;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import com.android.systemui.statusbar.phone.NotificationTapHelper_Factory_Factory;
import com.android.systemui.statusbar.phone.NotificationsQSContainerController;
import com.android.systemui.statusbar.phone.NotificationsQSContainerController_Factory;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy_Factory;
import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController_Factory_Factory;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimController_Factory;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.ShadeControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider_Factory;
import com.android.systemui.statusbar.phone.StatusBarDemoMode;
import com.android.systemui.statusbar.phone.StatusBarDemoMode_Factory;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener;
import com.android.systemui.statusbar.phone.StatusBarHeadsUpChangeListener_Factory;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarIconControllerImpl_Factory;
import com.android.systemui.statusbar.phone.StatusBarIconController_TintedIconManager_Factory_Factory;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager_Factory;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher_Factory;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarter_Factory;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter_Factory;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback;
import com.android.systemui.statusbar.phone.StatusBarRemoteInputCallback_Factory;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy_Factory;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager;
import com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager_Factory;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.SystemUIDialogManager_Factory;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.TapAgainViewController;
import com.android.systemui.statusbar.phone.TapAgainViewController_Factory;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController;
import com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController_Factory;
import com.android.systemui.statusbar.phone.dagger.C3131x8fd6ebda;
import com.android.systemui.statusbar.phone.dagger.CentralSurfacesComponent;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_CreateBatteryMeterViewControllerFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_CreateBatteryMeterViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetAuthRippleViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetBatteryMeterViewControllerFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetBatteryMeterViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetLockIconViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetNotificationPanelViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_GetTapAgainViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNTQSStatusBarFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNotificationShelfFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesStatusBarWindowViewFactory;
import com.android.systemui.statusbar.phone.dagger.StatusBarViewModule_ProvidesStatusIconContainerFactory;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.C3137xfe780fd7;
import com.android.systemui.statusbar.phone.fragment.dagger.C3138x6673e9b7;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideBatteryMeterViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideClockFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideLightsOutNotifViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvideOperatorNameViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallFlags_Factory;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger_Factory;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager_Factory;
import com.android.systemui.statusbar.phone.shade.transition.C4841SplitShadeOverScroller_Factory;
import com.android.systemui.statusbar.phone.shade.transition.NoOpOverScroller_Factory;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController;
import com.android.systemui.statusbar.phone.shade.transition.ShadeTransitionController_Factory;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller_Factory_Impl;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserInfoTracker_Factory;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherControllerImpl_Factory;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherFeatureController_Factory;
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
import com.android.systemui.statusbar.policy.Clock;
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
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController_Factory;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl;
import com.android.systemui.statusbar.policy.KeyguardStateControllerImpl_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController_Factory;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
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
import com.android.systemui.statusbar.policy.UserInfoController;
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
import com.android.systemui.tuner.TunerService;
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
import com.android.systemui.util.concurrency.SysUIConcurrencyModule_ProvidesMainMessageRouterFactory;
import com.android.systemui.util.concurrency.ThreadFactoryImpl_Factory;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.GarbageMonitor_Factory;
import com.android.systemui.util.leak.GarbageMonitor_MemoryTile_Factory;
import com.android.systemui.util.leak.GarbageMonitor_Service_Factory;
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

public final class DaggerGlobalRootComponent {
    private static final Provider ABSENT_JDK_OPTIONAL_PROVIDER = InstanceFactory.create(Optional.empty());

    private DaggerGlobalRootComponent() {
    }

    public static GlobalRootComponent.Builder builder() {
        return new Builder();
    }

    /* access modifiers changed from: private */
    public static <T> Provider<Optional<T>> absentJdkOptionalProvider() {
        return ABSENT_JDK_OPTIONAL_PROVIDER;
    }

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
        public static <T> Provider<Optional<T>> m378of(Provider<T> provider) {
            return new PresentJdkOptionalInstanceProvider(provider);
        }
    }

    private static final class Builder implements GlobalRootComponent.Builder {
        private Context context;

        private Builder() {
        }

        public Builder context(Context context2) {
            this.context = (Context) Preconditions.checkNotNull(context2);
            return this;
        }

        public GlobalRootComponent build() {
            Preconditions.checkBuilderRequirement(this.context, Context.class);
            return new GlobalRootComponentImpl(new GlobalModule(), new AndroidInternalsModule(), new FrameworkServicesModule(), new UnfoldTransitionModule(), new UnfoldSharedModule(), this.context);
        }
    }

    private static final class WMComponentBuilder implements WMComponent.Builder {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private HandlerThread setShellMainThread;

        private WMComponentBuilder(GlobalRootComponentImpl globalRootComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
        }

        public WMComponentBuilder setShellMainThread(HandlerThread handlerThread) {
            this.setShellMainThread = handlerThread;
            return this;
        }

        public WMComponent build() {
            return new WMComponentImpl(this.globalRootComponentImpl, this.setShellMainThread);
        }
    }

    private static final class SysUIComponentBuilder implements SysUIComponent.Builder {
        private final GlobalRootComponentImpl globalRootComponentImpl;
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

        private SysUIComponentBuilder(GlobalRootComponentImpl globalRootComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.pip.Pip>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setPip(java.util.Optional<com.android.p019wm.shell.pip.Pip> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setPip = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setPip(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setLegacySplitScreen(java.util.Optional<com.android.p019wm.shell.legacysplitscreen.LegacySplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setLegacySplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setLegacySplitScreen(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.splitscreen.SplitScreen>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setSplitScreen(java.util.Optional<com.android.p019wm.shell.splitscreen.SplitScreen> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setSplitScreen = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setSplitScreen(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.apppairs.AppPairs>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setAppPairs(java.util.Optional<com.android.p019wm.shell.apppairs.AppPairs> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setAppPairs = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setAppPairs(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.onehanded.OneHanded>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setOneHanded(java.util.Optional<com.android.p019wm.shell.onehanded.OneHanded> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setOneHanded = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setOneHanded(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.bubbles.Bubbles>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setBubbles(java.util.Optional<com.android.p019wm.shell.bubbles.Bubbles> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBubbles = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setBubbles(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.TaskViewFactory>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setTaskViewFactory(java.util.Optional<com.android.p019wm.shell.TaskViewFactory> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskViewFactory = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setTaskViewFactory(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.hidedisplaycutout.HideDisplayCutout>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setHideDisplayCutout(java.util.Optional<com.android.p019wm.shell.hidedisplaycutout.HideDisplayCutout> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setHideDisplayCutout = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setHideDisplayCutout(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.ShellCommandHandler>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setShellCommandHandler(java.util.Optional<com.android.p019wm.shell.ShellCommandHandler> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setShellCommandHandler = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setShellCommandHandler(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        public SysUIComponentBuilder setTransitions(ShellTransitions shellTransitions) {
            this.setTransitions = (ShellTransitions) Preconditions.checkNotNull(shellTransitions);
            return this;
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.startingsurface.StartingSurface>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setStartingSurface(java.util.Optional<com.android.p019wm.shell.startingsurface.StartingSurface> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setStartingSurface = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setStartingSurface(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.displayareahelper.DisplayAreaHelper>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setDisplayAreaHelper(java.util.Optional<com.android.p019wm.shell.displayareahelper.DisplayAreaHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDisplayAreaHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setDisplayAreaHelper(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setTaskSurfaceHelper(java.util.Optional<com.android.p019wm.shell.tasksurfacehelper.TaskSurfaceHelper> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setTaskSurfaceHelper = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setTaskSurfaceHelper(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.recents.RecentTasks>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setRecentTasks(java.util.Optional<com.android.p019wm.shell.recents.RecentTasks> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setRecentTasks = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setRecentTasks(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.compatui.CompatUI>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setCompatUI(java.util.Optional<com.android.p019wm.shell.compatui.CompatUI> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setCompatUI = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setCompatUI(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.lang.Object, java.util.Optional<com.android.wm.shell.draganddrop.DragAndDrop>] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setDragAndDrop(java.util.Optional<com.android.p019wm.shell.draganddrop.DragAndDrop> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setDragAndDrop = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setDragAndDrop(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        /* JADX WARNING: type inference failed for: r1v0, types: [java.util.Optional<com.android.wm.shell.back.BackAnimation>, java.lang.Object] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder setBackAnimation(java.util.Optional<com.android.p019wm.shell.back.BackAnimation> r1) {
            /*
                r0 = this;
                java.lang.Object r1 = dagger.internal.Preconditions.checkNotNull(r1)
                java.util.Optional r1 = (java.util.Optional) r1
                r0.setBackAnimation = r1
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.dagger.DaggerGlobalRootComponent.SysUIComponentBuilder.setBackAnimation(java.util.Optional):com.android.systemui.dagger.DaggerGlobalRootComponent$SysUIComponentBuilder");
        }

        public SysUIComponent build() {
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
            GlobalRootComponentImpl globalRootComponentImpl2 = this.globalRootComponentImpl;
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
            return new SysUIComponentImpl(globalRootComponentImpl2, leakModule, nightDisplayListenerModule, sharedLibraryModule, keyguardModule, sysUIUnfoldModule, this.setPip, this.setLegacySplitScreen, this.setSplitScreen, this.setAppPairs, this.setOneHanded, this.setBubbles, this.setTaskViewFactory, this.setHideDisplayCutout, this.setShellCommandHandler, this.setTransitions, this.setStartingSurface, this.setDisplayAreaHelper, this.setTaskSurfaceHelper, this.setRecentTasks, this.setCompatUI, this.setDragAndDrop, this.setBackAnimation);
        }
    }

    private static final class ExpandableNotificationRowComponentBuilder implements ExpandableNotificationRowComponent.Builder {
        private ExpandableNotificationRow expandableNotificationRow;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private NotificationListContainer listContainer;
        private NotificationEntry notificationEntry;
        private ExpandableNotificationRow.OnExpandClickListener onExpandClickListener;
        private final SysUIComponentImpl sysUIComponentImpl;

        private ExpandableNotificationRowComponentBuilder(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
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
            return new ExpandableNotificationRowComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.expandableNotificationRow, this.notificationEntry, this.onExpandClickListener, this.listContainer);
        }
    }

    private static final class RemoteInputViewSubcomponentFactory implements RemoteInputViewSubcomponent.Factory {
        private final ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private RemoteInputViewSubcomponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.expandableNotificationRowComponentImpl = expandableNotificationRowComponentImpl2;
        }

        public RemoteInputViewSubcomponent create(RemoteInputView remoteInputView, RemoteInputController remoteInputController) {
            Preconditions.checkNotNull(remoteInputView);
            Preconditions.checkNotNull(remoteInputController);
            return new RemoteInputViewSubcomponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.expandableNotificationRowComponentImpl, remoteInputView, remoteInputController);
        }
    }

    private static final class SysUIUnfoldComponentFactory implements SysUIUnfoldComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private SysUIUnfoldComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public SysUIUnfoldComponent create(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
            Preconditions.checkNotNull(unfoldTransitionProgressProvider);
            Preconditions.checkNotNull(naturalRotationUnfoldProgressProvider);
            Preconditions.checkNotNull(scopedUnfoldTransitionProgressProvider);
            return new SysUIUnfoldComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
        }
    }

    private static final class CoordinatorsSubcomponentFactory implements CoordinatorsSubcomponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private CoordinatorsSubcomponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public CoordinatorsSubcomponent create() {
            return new CoordinatorsSubcomponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl);
        }
    }

    private static final class FragmentCreatorFactory implements FragmentService.FragmentCreator.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private FragmentCreatorFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public FragmentService.FragmentCreator build() {
            return new FragmentCreatorImpl(this.globalRootComponentImpl, this.sysUIComponentImpl);
        }
    }

    private static final class SectionHeaderControllerSubcomponentBuilder implements SectionHeaderControllerSubcomponent.Builder {
        private String clickIntentAction;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Integer headerText;
        private String nodeLabel;
        private final SysUIComponentImpl sysUIComponentImpl;

        private SectionHeaderControllerSubcomponentBuilder(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
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
            return new SectionHeaderControllerSubcomponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.nodeLabel, this.headerText, this.clickIntentAction);
        }
    }

    private static final class NavigationBarComponentFactory implements NavigationBarComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private NavigationBarComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public NavigationBarComponent create(Context context, Bundle bundle) {
            Preconditions.checkNotNull(context);
            return new NavigationBarComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, context, bundle);
        }
    }

    private static final class CentralSurfacesComponentFactory implements CentralSurfacesComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private CentralSurfacesComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public CentralSurfacesComponent create() {
            return new CentralSurfacesComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl);
        }
    }

    private static final class StatusBarFragmentComponentFactory implements StatusBarFragmentComponent.Factory {
        private final CentralSurfacesComponentImpl centralSurfacesComponentImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private StatusBarFragmentComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, CentralSurfacesComponentImpl centralSurfacesComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.centralSurfacesComponentImpl = centralSurfacesComponentImpl2;
        }

        public StatusBarFragmentComponent create(CollapsedStatusBarFragment collapsedStatusBarFragment) {
            Preconditions.checkNotNull(collapsedStatusBarFragment);
            return new StatusBarFragmentComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.centralSurfacesComponentImpl, collapsedStatusBarFragment);
        }
    }

    private static final class KeyguardStatusViewComponentFactory implements KeyguardStatusViewComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardStatusViewComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public KeyguardStatusViewComponent build(KeyguardStatusView keyguardStatusView) {
            Preconditions.checkNotNull(keyguardStatusView);
            return new KeyguardStatusViewComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, keyguardStatusView);
        }
    }

    private static final class KeyguardBouncerComponentFactory implements KeyguardBouncerComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardBouncerComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public KeyguardBouncerComponent create(ViewGroup viewGroup) {
            Preconditions.checkNotNull(viewGroup);
            return new KeyguardBouncerComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, viewGroup);
        }
    }

    private static final class DozeComponentFactory implements DozeComponent.Builder {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private DozeComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public DozeComponent build(DozeMachine.Service service) {
            Preconditions.checkNotNull(service);
            return new DozeComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, service);
        }
    }

    private static final class DreamOverlayComponentFactory implements DreamOverlayComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private DreamOverlayComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public DreamOverlayComponent create(ViewModelStore viewModelStore, Complication.Host host) {
            Preconditions.checkNotNull(viewModelStore);
            Preconditions.checkNotNull(host);
            return new DreamOverlayComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, viewModelStore, host);
        }
    }

    private static final class ComplicationViewModelComponentFactory implements ComplicationViewModelComponent.Factory {
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private ComplicationViewModelComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
        }

        public ComplicationViewModelComponent create(Complication complication, ComplicationId complicationId) {
            Preconditions.checkNotNull(complication);
            Preconditions.checkNotNull(complicationId);
            return new ComplicationViewModelComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.dreamOverlayComponentImpl, complication, complicationId);
        }
    }

    private static final class InputSessionComponentFactory implements InputSessionComponent.Factory {
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private InputSessionComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
        }

        public InputSessionComponent create(String str, InputChannelCompat.InputEventListener inputEventListener, GestureDetector.OnGestureListener onGestureListener, boolean z) {
            Preconditions.checkNotNull(str);
            Preconditions.checkNotNull(inputEventListener);
            Preconditions.checkNotNull(onGestureListener);
            Preconditions.checkNotNull(Boolean.valueOf(z));
            return new InputSessionComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.dreamOverlayComponentImpl, str, inputEventListener, onGestureListener, Boolean.valueOf(z));
        }
    }

    private static final class QSFragmentComponentFactory implements QSFragmentComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private QSFragmentComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public QSFragmentComponent create(QSFragment qSFragment) {
            Preconditions.checkNotNull(qSFragment);
            return new QSFragmentComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, qSFragment);
        }
    }

    private static final class NotificationShelfComponentBuilder implements NotificationShelfComponent.Builder {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private NotificationShelf notificationShelf;
        private final SysUIComponentImpl sysUIComponentImpl;

        private NotificationShelfComponentBuilder(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public NotificationShelfComponentBuilder notificationShelf(NotificationShelf notificationShelf2) {
            this.notificationShelf = (NotificationShelf) Preconditions.checkNotNull(notificationShelf2);
            return this;
        }

        public NotificationShelfComponent build() {
            Preconditions.checkBuilderRequirement(this.notificationShelf, NotificationShelf.class);
            return new NotificationShelfComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, this.notificationShelf);
        }
    }

    private static final class KeyguardQsUserSwitchComponentFactory implements KeyguardQsUserSwitchComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardQsUserSwitchComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public KeyguardQsUserSwitchComponent build(FrameLayout frameLayout) {
            Preconditions.checkNotNull(frameLayout);
            return new KeyguardQsUserSwitchComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, frameLayout);
        }
    }

    private static final class KeyguardUserSwitcherComponentFactory implements KeyguardUserSwitcherComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardUserSwitcherComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public KeyguardUserSwitcherComponent build(KeyguardUserSwitcherView keyguardUserSwitcherView) {
            Preconditions.checkNotNull(keyguardUserSwitcherView);
            return new KeyguardUserSwitcherComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, keyguardUserSwitcherView);
        }
    }

    private static final class KeyguardStatusBarViewComponentFactory implements KeyguardStatusBarViewComponent.Factory {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardStatusBarViewComponentFactory(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        public KeyguardStatusBarViewComponent build(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider) {
            Preconditions.checkNotNull(keyguardStatusBarView);
            Preconditions.checkNotNull(notificationPanelViewStateProvider);
            return new KeyguardStatusBarViewComponentImpl(this.globalRootComponentImpl, this.sysUIComponentImpl, keyguardStatusBarView, notificationPanelViewStateProvider);
        }
    }

    private static final class WMComponentImpl implements WMComponent {
        private Provider<Optional<DisplayImeController>> dynamicOverrideOptionalOfDisplayImeControllerProvider;
        private Provider<Optional<FreeformTaskListener>> dynamicOverrideOptionalOfFreeformTaskListenerProvider;
        private Provider<Optional<FullscreenTaskListener>> dynamicOverrideOptionalOfFullscreenTaskListenerProvider;
        private Provider<Optional<FullscreenUnfoldController>> dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider;
        private Provider<Optional<OneHandedController>> dynamicOverrideOptionalOfOneHandedControllerProvider;
        private Provider<Optional<SplitScreenController>> dynamicOverrideOptionalOfSplitScreenControllerProvider;
        private Provider<Optional<StartingWindowTypeAlgorithm>> dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<Optional<AppPairsController>> optionalOfAppPairsControllerProvider;
        private Provider<Optional<BubbleController>> optionalOfBubbleControllerProvider;
        private Provider<Optional<LegacySplitScreenController>> optionalOfLegacySplitScreenControllerProvider;
        private Provider<Optional<PipTouchHandler>> optionalOfPipTouchHandlerProvider;
        private Provider<Optional<ShellUnfoldProgressProvider>> optionalOfShellUnfoldProgressProvider;
        private Provider<AppPairsController> provideAppPairsProvider;
        private Provider<Optional<AppPairs>> provideAppPairsProvider2;
        private Provider<Optional<BackAnimationController>> provideBackAnimationControllerProvider;
        private Provider<Optional<BackAnimation>> provideBackAnimationProvider;
        private Provider<BubbleController> provideBubbleControllerProvider;
        private Provider<Optional<Bubbles>> provideBubblesProvider;
        private Provider<CompatUIController> provideCompatUIControllerProvider;
        private Provider<Optional<CompatUI>> provideCompatUIProvider;
        private Provider<Optional<DisplayAreaHelper>> provideDisplayAreaHelperProvider;
        private Provider<DisplayController> provideDisplayControllerProvider;
        private Provider<DisplayImeController> provideDisplayImeControllerProvider;
        private Provider<DisplayInsetsController> provideDisplayInsetsControllerProvider;
        private Provider<DisplayLayout> provideDisplayLayoutProvider;
        private Provider<DragAndDropController> provideDragAndDropControllerProvider;
        private Provider<Optional<DragAndDrop>> provideDragAndDropProvider;
        private Provider<FloatingContentCoordinator> provideFloatingContentCoordinatorProvider;
        private Provider<FreeformTaskListener> provideFreeformTaskListenerProvider;
        private Provider<Optional<FreeformTaskListener>> provideFreeformTaskListenerProvider2;
        private Provider<FullscreenTaskListener> provideFullscreenTaskListenerProvider;
        private Provider<FullscreenUnfoldController> provideFullscreenUnfoldControllerProvider;
        private Provider<Optional<FullscreenUnfoldController>> provideFullscreenUnfoldControllerProvider2;
        private Provider<Optional<HideDisplayCutoutController>> provideHideDisplayCutoutControllerProvider;
        private Provider<Optional<HideDisplayCutout>> provideHideDisplayCutoutProvider;
        private Provider<IconProvider> provideIconProvider;
        private Provider<KidsModeTaskOrganizer> provideKidsModeTaskOrganizerProvider;
        private Provider<LegacySplitScreenController> provideLegacySplitScreenProvider;
        private Provider<Optional<LegacySplitScreen>> provideLegacySplitScreenProvider2;
        private Provider<OneHandedController> provideOneHandedControllerProvider;
        private Provider<Optional<OneHanded>> provideOneHandedProvider;
        private Provider<PipAnimationController> providePipAnimationControllerProvider;
        private Provider<PipAppOpsListener> providePipAppOpsListenerProvider;
        private Provider<PipBoundsState> providePipBoundsStateProvider;
        private Provider<PipMediaController> providePipMediaControllerProvider;
        private Provider<PipMotionHelper> providePipMotionHelperProvider;
        private Provider<PipParamsChangedForwarder> providePipParamsChangedForwarderProvider;
        private Provider<Optional<Pip>> providePipProvider;
        private Provider<PipSnapAlgorithm> providePipSnapAlgorithmProvider;
        private Provider<PipSurfaceTransactionHelper> providePipSurfaceTransactionHelperProvider;
        private Provider<PipTaskOrganizer> providePipTaskOrganizerProvider;
        private Provider<PipTouchHandler> providePipTouchHandlerProvider;
        private Provider<PipTransitionController> providePipTransitionControllerProvider;
        private Provider<PipTransitionState> providePipTransitionStateProvider;
        private Provider<PipUiEventLogger> providePipUiEventLoggerProvider;
        private Provider<Optional<RecentTasksController>> provideRecentTasksControllerProvider;
        private Provider<Optional<RecentTasks>> provideRecentTasksProvider;
        private Provider<ShellTransitions> provideRemoteTransitionsProvider;
        private Provider<RootDisplayAreaOrganizer> provideRootDisplayAreaOrganizerProvider;
        private Provider<RootTaskDisplayAreaOrganizer> provideRootTaskDisplayAreaOrganizerProvider;
        private Provider<ShellExecutor> provideSharedBackgroundExecutorProvider;
        private Provider<Handler> provideSharedBackgroundHandlerProvider;
        private Provider<ShellExecutor> provideShellAnimationExecutorProvider;
        private Provider<ShellCommandHandlerImpl> provideShellCommandHandlerImplProvider;
        private Provider<Optional<ShellCommandHandler>> provideShellCommandHandlerProvider;
        private Provider<ShellInitImpl> provideShellInitImplProvider;
        private Provider<ShellInit> provideShellInitProvider;
        private Provider<ShellExecutor> provideShellMainExecutorProvider;
        private Provider<AnimationHandler> provideShellMainExecutorSfVsyncAnimationHandlerProvider;
        private Provider<Handler> provideShellMainHandlerProvider;
        private Provider<ShellTaskOrganizer> provideShellTaskOrganizerProvider;
        private Provider<ShellExecutor> provideSplashScreenExecutorProvider;
        private Provider<SplitScreenController> provideSplitScreenControllerProvider;
        private Provider<Optional<SplitScreen>> provideSplitScreenProvider;
        private Provider<Optional<StageTaskUnfoldController>> provideStageTaskUnfoldControllerProvider;
        private Provider<Optional<StartingSurface>> provideStartingSurfaceProvider;
        private Provider<StartingWindowController> provideStartingWindowControllerProvider;
        private Provider<StartingWindowTypeAlgorithm> provideStartingWindowTypeAlgorithmProvider;
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
        private Provider<UnfoldBackgroundController> provideUnfoldBackgroundControllerProvider;
        private Provider<Optional<UnfoldTransitionHandler>> provideUnfoldTransitionHandlerProvider;
        private Provider<WindowManagerShellWrapper> provideWindowManagerShellWrapperProvider;
        private Provider<TaskStackListenerImpl> providerTaskStackListenerImplProvider;
        private Provider<Optional<OneHandedController>> providesOneHandedControllerProvider;
        private Provider<PipBoundsAlgorithm> providesPipBoundsAlgorithmProvider;
        private Provider<PhonePipMenuController> providesPipPhoneMenuControllerProvider;
        private Provider<Optional<SplitScreenController>> providesSplitScreenControllerProvider;
        private Provider<HandlerThread> setShellMainThreadProvider;
        private final WMComponentImpl wMComponentImpl;

        private WMComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, HandlerThread handlerThread) {
            this.wMComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            initialize(handlerThread);
        }

        private void initialize(HandlerThread handlerThread) {
            this.setShellMainThreadProvider = InstanceFactory.createNullable(handlerThread);
            this.provideShellMainHandlerProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainHandlerFactory.create(this.globalRootComponentImpl.contextProvider, this.setShellMainThreadProvider, WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideSysUIMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSysUIMainExecutorFactory.create(WMShellConcurrencyModule_ProvideMainHandlerFactory.create()));
            this.provideShellMainExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellMainExecutorFactory.create(this.globalRootComponentImpl.contextProvider, this.provideShellMainHandlerProvider, this.provideSysUIMainExecutorProvider));
            this.provideDisplayControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.provideShellMainExecutorProvider));
            this.dynamicOverrideOptionalOfDisplayImeControllerProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.provideDisplayInsetsControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayInsetsControllerFactory.create(this.globalRootComponentImpl.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideTransactionPoolProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransactionPoolFactory.create());
            this.provideDisplayImeControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayImeControllerFactory.create(this.dynamicOverrideOptionalOfDisplayImeControllerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider, this.provideTransactionPoolProvider));
            this.provideIconProvider = DoubleCheck.provider(WMShellBaseModule_ProvideIconProviderFactory.create(this.globalRootComponentImpl.contextProvider));
            this.provideDragAndDropControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideIconProvider, this.provideShellMainExecutorProvider));
            this.provideSyncTransactionQueueProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSyncTransactionQueueFactory.create(this.provideTransactionPoolProvider, this.provideShellMainExecutorProvider));
            this.provideShellTaskOrganizerProvider = new DelegateFactory();
            this.provideShellAnimationExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory.create());
            this.provideTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTransitionsFactory.create(this.provideShellTaskOrganizerProvider, this.provideTransactionPoolProvider, this.provideDisplayControllerProvider, this.globalRootComponentImpl.contextProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.provideShellAnimationExecutorProvider));
            this.provideCompatUIControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDisplayImeControllerProvider, this.provideSyncTransactionQueueProvider, this.provideShellMainExecutorProvider, this.provideTransitionsProvider));
            this.providerTaskStackListenerImplProvider = DoubleCheck.provider(WMShellBaseModule_ProviderTaskStackListenerImplFactory.create(this.provideShellMainHandlerProvider));
            this.provideRecentTasksControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.providerTaskStackListenerImplProvider, this.provideShellMainExecutorProvider));
            DelegateFactory.setDelegate(this.provideShellTaskOrganizerProvider, DoubleCheck.provider(WMShellBaseModule_ProvideShellTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, this.globalRootComponentImpl.contextProvider, this.provideCompatUIControllerProvider, this.provideRecentTasksControllerProvider)));
            this.provideKidsModeTaskOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideKidsModeTaskOrganizerFactory.create(this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.globalRootComponentImpl.contextProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideRecentTasksControllerProvider));
            this.provideFloatingContentCoordinatorProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFloatingContentCoordinatorFactory.create());
            this.provideWindowManagerShellWrapperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideWindowManagerShellWrapperFactory.create(this.provideShellMainExecutorProvider));
            this.provideDisplayLayoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayLayoutFactory.create());
            Provider<OneHandedController> provider = DoubleCheck.provider(WMShellModule_ProvideOneHandedControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.provideDisplayControllerProvider, this.provideDisplayLayoutProvider, this.providerTaskStackListenerImplProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.provideOneHandedControllerProvider = provider;
            this.dynamicOverrideOptionalOfOneHandedControllerProvider = PresentJdkOptionalInstanceProvider.m378of(provider);
            Provider<Handler> provider2 = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSharedBackgroundHandlerFactory.create());
            this.provideSharedBackgroundHandlerProvider = provider2;
            this.provideSharedBackgroundExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSharedBackgroundExecutorFactory.create(provider2));
            this.provideTaskViewTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewTransitionsFactory.create(this.provideTransitionsProvider));
            Provider<BubbleController> provider3 = DoubleCheck.provider(WMShellModule_ProvideBubbleControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideFloatingContentCoordinatorProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.provideWindowManagerShellWrapperProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.provideLauncherAppsProvider, this.providerTaskStackListenerImplProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideDisplayControllerProvider, this.dynamicOverrideOptionalOfOneHandedControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider, this.provideSharedBackgroundExecutorProvider, this.provideTaskViewTransitionsProvider, this.provideSyncTransactionQueueProvider));
            this.provideBubbleControllerProvider = provider3;
            this.optionalOfBubbleControllerProvider = PresentJdkOptionalInstanceProvider.m378of(provider3);
            this.provideRootTaskDisplayAreaOrganizerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRootTaskDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider, this.globalRootComponentImpl.contextProvider));
            this.optionalOfShellUnfoldProgressProvider = PresentJdkOptionalInstanceProvider.m378of(this.globalRootComponentImpl.provideShellProgressProvider);
            this.provideUnfoldBackgroundControllerProvider = DoubleCheck.provider(WMShellModule_ProvideUnfoldBackgroundControllerFactory.create(this.provideRootTaskDisplayAreaOrganizerProvider, this.globalRootComponentImpl.contextProvider));
            this.provideStageTaskUnfoldControllerProvider = WMShellModule_ProvideStageTaskUnfoldControllerFactory.create(this.optionalOfShellUnfoldProgressProvider, this.globalRootComponentImpl.contextProvider, this.provideTransactionPoolProvider, this.provideUnfoldBackgroundControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider);
            Provider<SplitScreenController> provider4 = DoubleCheck.provider(WMShellModule_ProvideSplitScreenControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.globalRootComponentImpl.contextProvider, this.provideRootTaskDisplayAreaOrganizerProvider, this.provideShellMainExecutorProvider, this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideTransitionsProvider, this.provideTransactionPoolProvider, this.provideIconProvider, this.provideRecentTasksControllerProvider, this.provideStageTaskUnfoldControllerProvider));
            this.provideSplitScreenControllerProvider = provider4;
            Provider<Optional<SplitScreenController>> access$3000 = PresentJdkOptionalInstanceProvider.m378of(provider4);
            this.dynamicOverrideOptionalOfSplitScreenControllerProvider = access$3000;
            this.providesSplitScreenControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesSplitScreenControllerFactory.create(access$3000, this.globalRootComponentImpl.contextProvider));
            Provider<AppPairsController> provider5 = DoubleCheck.provider(WMShellModule_ProvideAppPairsFactory.create(this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider));
            this.provideAppPairsProvider = provider5;
            this.optionalOfAppPairsControllerProvider = PresentJdkOptionalInstanceProvider.m378of(provider5);
            this.providePipBoundsStateProvider = DoubleCheck.provider(WMShellModule_ProvidePipBoundsStateFactory.create(this.globalRootComponentImpl.contextProvider));
            this.providePipMediaControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipMediaControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideShellMainHandlerProvider));
            this.provideSystemWindowsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSystemWindowsFactory.create(this.provideDisplayControllerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider));
            this.providePipUiEventLoggerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidePipUiEventLoggerFactory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider, this.globalRootComponentImpl.providePackageManagerProvider));
            this.providesPipPhoneMenuControllerProvider = DoubleCheck.provider(WMShellModule_ProvidesPipPhoneMenuControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.provideSystemWindowsProvider, this.providesSplitScreenControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellMainExecutorProvider, this.provideShellMainHandlerProvider));
            this.providePipSnapAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvidePipSnapAlgorithmFactory.create());
            this.providesPipBoundsAlgorithmProvider = DoubleCheck.provider(WMShellModule_ProvidesPipBoundsAlgorithmFactory.create(this.globalRootComponentImpl.contextProvider, this.providePipBoundsStateProvider, this.providePipSnapAlgorithmProvider));
            this.providePipTransitionStateProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionStateFactory.create());
            Provider<PipSurfaceTransactionHelper> provider6 = DoubleCheck.provider(WMShellBaseModule_ProvidePipSurfaceTransactionHelperFactory.create());
            this.providePipSurfaceTransactionHelperProvider = provider6;
            this.providePipAnimationControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipAnimationControllerFactory.create(provider6));
            this.providePipTransitionControllerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTransitionControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideTransitionsProvider, this.provideShellTaskOrganizerProvider, this.providePipAnimationControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTransitionStateProvider, this.providesPipPhoneMenuControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providesSplitScreenControllerProvider));
            this.providePipParamsChangedForwarderProvider = DoubleCheck.provider(WMShellModule_ProvidePipParamsChangedForwarderFactory.create());
            this.providePipTaskOrganizerProvider = DoubleCheck.provider(WMShellModule_ProvidePipTaskOrganizerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideSyncTransactionQueueProvider, this.providePipTransitionStateProvider, this.providePipBoundsStateProvider, this.providesPipBoundsAlgorithmProvider, this.providesPipPhoneMenuControllerProvider, this.providePipAnimationControllerProvider, this.providePipSurfaceTransactionHelperProvider, this.providePipTransitionControllerProvider, this.providePipParamsChangedForwarderProvider, this.providesSplitScreenControllerProvider, this.provideDisplayControllerProvider, this.providePipUiEventLoggerProvider, this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider));
            this.providePipMotionHelperProvider = DoubleCheck.provider(WMShellModule_ProvidePipMotionHelperFactory.create(this.globalRootComponentImpl.contextProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipSnapAlgorithmProvider, this.providePipTransitionControllerProvider, this.provideFloatingContentCoordinatorProvider));
            Provider<PipTouchHandler> provider7 = DoubleCheck.provider(WMShellModule_ProvidePipTouchHandlerFactory.create(this.globalRootComponentImpl.contextProvider, this.providesPipPhoneMenuControllerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipTaskOrganizerProvider, this.providePipMotionHelperProvider, this.provideFloatingContentCoordinatorProvider, this.providePipUiEventLoggerProvider, this.provideShellMainExecutorProvider));
            this.providePipTouchHandlerProvider = provider7;
            this.optionalOfPipTouchHandlerProvider = PresentJdkOptionalInstanceProvider.m378of(provider7);
            this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            Provider<FullscreenUnfoldController> provider8 = DoubleCheck.provider(WMShellModule_ProvideFullscreenUnfoldControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.optionalOfShellUnfoldProgressProvider, this.provideUnfoldBackgroundControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideShellMainExecutorProvider));
            this.provideFullscreenUnfoldControllerProvider = provider8;
            Provider<Optional<FullscreenUnfoldController>> access$30002 = PresentJdkOptionalInstanceProvider.m378of(provider8);
            this.dynamicOverrideOptionalOfFullscreenUnfoldControllerProvider = access$30002;
            Provider<Optional<FullscreenUnfoldController>> provider9 = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenUnfoldControllerFactory.create(access$30002, this.optionalOfShellUnfoldProgressProvider));
            this.provideFullscreenUnfoldControllerProvider2 = provider9;
            this.provideFullscreenTaskListenerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideFullscreenTaskListenerFactory.create(this.dynamicOverrideOptionalOfFullscreenTaskListenerProvider, this.provideSyncTransactionQueueProvider, provider9, this.provideRecentTasksControllerProvider));
            this.provideUnfoldTransitionHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideUnfoldTransitionHandlerFactory.create(this.optionalOfShellUnfoldProgressProvider, this.provideTransactionPoolProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider));
            Provider<FreeformTaskListener> provider10 = DoubleCheck.provider(WMShellModule_ProvideFreeformTaskListenerFactory.create(this.provideSyncTransactionQueueProvider));
            this.provideFreeformTaskListenerProvider = provider10;
            Provider<Optional<FreeformTaskListener>> access$30003 = PresentJdkOptionalInstanceProvider.m378of(provider10);
            this.dynamicOverrideOptionalOfFreeformTaskListenerProvider = access$30003;
            this.provideFreeformTaskListenerProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideFreeformTaskListenerFactory.create(access$30003, this.globalRootComponentImpl.contextProvider));
            this.provideSplashScreenExecutorProvider = DoubleCheck.provider(WMShellConcurrencyModule_ProvideSplashScreenExecutorFactory.create());
            Provider<Optional<StartingWindowTypeAlgorithm>> access$2600 = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfStartingWindowTypeAlgorithmProvider = access$2600;
            this.provideStartingWindowTypeAlgorithmProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowTypeAlgorithmFactory.create(access$2600));
            Provider<StartingWindowController> provider11 = DoubleCheck.provider(WMShellBaseModule_ProvideStartingWindowControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideSplashScreenExecutorProvider, this.provideStartingWindowTypeAlgorithmProvider, this.provideIconProvider, this.provideTransactionPoolProvider));
            this.provideStartingWindowControllerProvider = provider11;
            Provider<ShellInitImpl> provider12 = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitImplFactory.create(this.provideDisplayControllerProvider, this.provideDisplayImeControllerProvider, this.provideDisplayInsetsControllerProvider, this.provideDragAndDropControllerProvider, this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfBubbleControllerProvider, this.providesSplitScreenControllerProvider, this.optionalOfAppPairsControllerProvider, this.optionalOfPipTouchHandlerProvider, this.provideFullscreenTaskListenerProvider, this.provideFullscreenUnfoldControllerProvider2, this.provideUnfoldTransitionHandlerProvider, this.provideFreeformTaskListenerProvider2, this.provideRecentTasksControllerProvider, this.provideTransitionsProvider, provider11, this.provideShellMainExecutorProvider));
            this.provideShellInitImplProvider = provider12;
            this.provideShellInitProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellInitFactory.create(provider12));
            this.provideShellMainExecutorSfVsyncAnimationHandlerProvider = DoubleCheck.provider(C3463x374a97b.create(this.provideShellMainExecutorProvider));
            Provider<LegacySplitScreenController> provider13 = DoubleCheck.provider(WMShellModule_ProvideLegacySplitScreenFactory.create(this.globalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.provideSystemWindowsProvider, this.provideDisplayImeControllerProvider, this.provideTransactionPoolProvider, this.provideShellTaskOrganizerProvider, this.provideSyncTransactionQueueProvider, this.providerTaskStackListenerImplProvider, this.provideTransitionsProvider, this.provideShellMainExecutorProvider, this.provideShellMainExecutorSfVsyncAnimationHandlerProvider));
            this.provideLegacySplitScreenProvider = provider13;
            this.optionalOfLegacySplitScreenControllerProvider = PresentJdkOptionalInstanceProvider.m378of(provider13);
            this.providePipAppOpsListenerProvider = DoubleCheck.provider(WMShellModule_ProvidePipAppOpsListenerFactory.create(this.globalRootComponentImpl.contextProvider, this.providePipTouchHandlerProvider, this.provideShellMainExecutorProvider));
            this.providesOneHandedControllerProvider = DoubleCheck.provider(WMShellBaseModule_ProvidesOneHandedControllerFactory.create(this.dynamicOverrideOptionalOfOneHandedControllerProvider));
            this.providePipProvider = DoubleCheck.provider(WMShellModule_ProvidePipFactory.create(this.globalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.providePipAppOpsListenerProvider, this.providesPipBoundsAlgorithmProvider, this.providePipBoundsStateProvider, this.providePipMediaControllerProvider, this.providesPipPhoneMenuControllerProvider, this.providePipTaskOrganizerProvider, this.providePipTouchHandlerProvider, this.providePipTransitionControllerProvider, this.provideWindowManagerShellWrapperProvider, this.providerTaskStackListenerImplProvider, this.providePipParamsChangedForwarderProvider, this.providesOneHandedControllerProvider, this.provideShellMainExecutorProvider));
            Provider<Optional<HideDisplayCutoutController>> provider14 = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideDisplayControllerProvider, this.provideShellMainExecutorProvider));
            this.provideHideDisplayCutoutControllerProvider = provider14;
            Provider<ShellCommandHandlerImpl> provider15 = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerImplFactory.create(this.provideShellTaskOrganizerProvider, this.provideKidsModeTaskOrganizerProvider, this.optionalOfLegacySplitScreenControllerProvider, this.providesSplitScreenControllerProvider, this.providePipProvider, this.providesOneHandedControllerProvider, provider14, this.optionalOfAppPairsControllerProvider, this.provideRecentTasksControllerProvider, this.provideShellMainExecutorProvider));
            this.provideShellCommandHandlerImplProvider = provider15;
            this.provideShellCommandHandlerProvider = DoubleCheck.provider(WMShellBaseModule_ProvideShellCommandHandlerFactory.create(provider15));
            this.provideOneHandedProvider = DoubleCheck.provider(WMShellBaseModule_ProvideOneHandedFactory.create(this.providesOneHandedControllerProvider));
            this.provideLegacySplitScreenProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideLegacySplitScreenFactory.create(this.optionalOfLegacySplitScreenControllerProvider));
            this.provideSplitScreenProvider = DoubleCheck.provider(WMShellBaseModule_ProvideSplitScreenFactory.create(this.providesSplitScreenControllerProvider));
            this.provideAppPairsProvider2 = DoubleCheck.provider(WMShellBaseModule_ProvideAppPairsFactory.create(this.optionalOfAppPairsControllerProvider));
            this.provideBubblesProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBubblesFactory.create(this.optionalOfBubbleControllerProvider));
            this.provideHideDisplayCutoutProvider = DoubleCheck.provider(WMShellBaseModule_ProvideHideDisplayCutoutFactory.create(this.provideHideDisplayCutoutControllerProvider));
            Provider<TaskViewFactoryController> provider16 = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider, this.provideSyncTransactionQueueProvider, this.provideTaskViewTransitionsProvider));
            this.provideTaskViewFactoryControllerProvider = provider16;
            this.provideTaskViewFactoryProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskViewFactoryFactory.create(provider16));
            this.provideRemoteTransitionsProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRemoteTransitionsFactory.create(this.provideTransitionsProvider));
            this.provideStartingSurfaceProvider = DoubleCheck.provider(WMShellBaseModule_ProvideStartingSurfaceFactory.create(this.provideStartingWindowControllerProvider));
            Provider<RootDisplayAreaOrganizer> provider17 = DoubleCheck.provider(WMShellBaseModule_ProvideRootDisplayAreaOrganizerFactory.create(this.provideShellMainExecutorProvider));
            this.provideRootDisplayAreaOrganizerProvider = provider17;
            this.provideDisplayAreaHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDisplayAreaHelperFactory.create(this.provideShellMainExecutorProvider, provider17));
            WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory create = WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory.create(this.provideShellTaskOrganizerProvider, this.provideShellMainExecutorProvider);
            this.provideTaskSurfaceHelperControllerProvider = create;
            this.provideTaskSurfaceHelperProvider = DoubleCheck.provider(WMShellBaseModule_ProvideTaskSurfaceHelperFactory.create(create));
            this.provideRecentTasksProvider = DoubleCheck.provider(WMShellBaseModule_ProvideRecentTasksFactory.create(this.provideRecentTasksControllerProvider));
            this.provideCompatUIProvider = DoubleCheck.provider(WMShellBaseModule_ProvideCompatUIFactory.create(this.provideCompatUIControllerProvider));
            this.provideDragAndDropProvider = DoubleCheck.provider(WMShellBaseModule_ProvideDragAndDropFactory.create(this.provideDragAndDropControllerProvider));
            Provider<Optional<BackAnimationController>> provider18 = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideShellMainExecutorProvider, this.provideSharedBackgroundHandlerProvider));
            this.provideBackAnimationControllerProvider = provider18;
            this.provideBackAnimationProvider = DoubleCheck.provider(WMShellBaseModule_ProvideBackAnimationFactory.create(provider18));
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
            return this.provideLegacySplitScreenProvider2.get();
        }

        public Optional<SplitScreen> getSplitScreen() {
            return this.provideSplitScreenProvider.get();
        }

        public Optional<AppPairs> getAppPairs() {
            return this.provideAppPairsProvider2.get();
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

    private static final class RemoteInputViewSubcomponentImpl implements RemoteInputViewSubcomponent {
        private final ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final RemoteInputController remoteInputController;
        private final RemoteInputViewSubcomponentImpl remoteInputViewSubcomponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;
        private final RemoteInputView view;

        private RemoteInputViewSubcomponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, ExpandableNotificationRowComponentImpl expandableNotificationRowComponentImpl2, RemoteInputView remoteInputView, RemoteInputController remoteInputController2) {
            this.remoteInputViewSubcomponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.expandableNotificationRowComponentImpl = expandableNotificationRowComponentImpl2;
            this.view = remoteInputView;
            this.remoteInputController = remoteInputController2;
        }

        private RemoteInputViewControllerImpl remoteInputViewControllerImpl() {
            return new RemoteInputViewControllerImpl(this.view, this.expandableNotificationRowComponentImpl.notificationEntry, (RemoteInputQuickSettingsDisabler) this.sysUIComponentImpl.remoteInputQuickSettingsDisablerProvider.get(), this.remoteInputController, (ShortcutManager) this.globalRootComponentImpl.provideShortcutManagerProvider.get(), (UiEventLogger) this.globalRootComponentImpl.provideUiEventLoggerProvider.get());
        }

        public RemoteInputViewController getController() {
            return remoteInputViewControllerImpl();
        }
    }

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
        /* access modifiers changed from: private */
        public final GlobalRootComponentImpl globalRootComponentImpl;
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
        public final SysUIComponentImpl sysUIComponentImpl;

        private ExpandableNotificationRowComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry2, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
            this.expandableNotificationRowComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.notificationEntry = notificationEntry2;
            initialize(expandableNotificationRow, notificationEntry2, onExpandClickListener, notificationListContainer);
        }

        private void initialize(ExpandableNotificationRow expandableNotificationRow, NotificationEntry notificationEntry2, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, NotificationListContainer notificationListContainer) {
            this.expandableNotificationRowProvider = InstanceFactory.create(expandableNotificationRow);
            this.factoryProvider = NotificationTapHelper_Factory_Factory.create(this.sysUIComponentImpl.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider);
            ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.expandableNotificationRowProvider);
            this.expandableViewControllerProvider = create;
            ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.expandableNotificationRowProvider, create);
            this.expandableOutlineViewControllerProvider = create2;
            this.activatableNotificationViewControllerProvider = ActivatableNotificationViewController_Factory.create(this.expandableNotificationRowProvider, this.factoryProvider, create2, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.falsingCollectorImplProvider);
            this.remoteInputViewSubcomponentFactoryProvider = new Provider<RemoteInputViewSubcomponent.Factory>() {
                public RemoteInputViewSubcomponent.Factory get() {
                    return new RemoteInputViewSubcomponentFactory(ExpandableNotificationRowComponentImpl.this.globalRootComponentImpl, ExpandableNotificationRowComponentImpl.this.sysUIComponentImpl, ExpandableNotificationRowComponentImpl.this.expandableNotificationRowComponentImpl);
                }
            };
            this.listContainerProvider = InstanceFactory.create(notificationListContainer);
            Factory create3 = InstanceFactory.create(notificationEntry2);
            this.notificationEntryProvider = create3;
            this.provideStatusBarNotificationProvider = C2779xc255c3ca.create(create3);
            this.provideAppNameProvider = C2777x3e2d0aca.create(this.globalRootComponentImpl.contextProvider, this.provideStatusBarNotificationProvider);
            this.provideNotificationKeyProvider = C2778xdc9a80a2.create(this.provideStatusBarNotificationProvider);
            this.onExpandClickListenerProvider = InstanceFactory.create(onExpandClickListener);
            this.expandableNotificationRowDragControllerProvider = ExpandableNotificationRowDragController_Factory.create(this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.shadeControllerImplProvider);
            this.expandableNotificationRowControllerProvider = DoubleCheck.provider(ExpandableNotificationRowController_Factory.create(this.expandableNotificationRowProvider, this.activatableNotificationViewControllerProvider, this.remoteInputViewSubcomponentFactoryProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.listContainerProvider, this.sysUIComponentImpl.provideNotificationMediaManagerProvider, this.sysUIComponentImpl.smartReplyConstantsProvider, this.sysUIComponentImpl.provideSmartReplyControllerProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.sysUIComponentImpl.bindSystemClockProvider, this.provideAppNameProvider, this.provideNotificationKeyProvider, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.provideGroupMembershipManagerProvider, this.sysUIComponentImpl.provideGroupExpansionManagerProvider, this.sysUIComponentImpl.rowContentBindStageProvider, this.sysUIComponentImpl.provideNotificationLoggerProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.onExpandClickListenerProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.provideNotificationGutsManagerProvider, this.sysUIComponentImpl.provideAllowNotificationLongPressProvider, this.sysUIComponentImpl.provideOnUserInteractionCallbackProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.falsingCollectorImplProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.peopleNotificationIdentifierImplProvider, this.sysUIComponentImpl.provideBubblesManagerProvider, this.expandableNotificationRowDragControllerProvider));
        }

        public ExpandableNotificationRowController getExpandableNotificationRowController() {
            return this.expandableNotificationRowControllerProvider.get();
        }
    }

    private static final class SysUIUnfoldComponentImpl implements SysUIUnfoldComponent {
        private Provider<FoldAodAnimationController> foldAodAnimationControllerProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<KeyguardUnfoldTransition> keyguardUnfoldTransitionProvider;
        private Provider<NotificationPanelUnfoldAnimationController> notificationPanelUnfoldAnimationControllerProvider;
        private Provider<UnfoldTransitionProgressProvider> p1Provider;
        private Provider<NaturalRotationUnfoldProgressProvider> p2Provider;
        private Provider<ScopedUnfoldTransitionProgressProvider> p3Provider;
        private Provider<StatusBarMoveFromCenterAnimationController> statusBarMoveFromCenterAnimationControllerProvider;
        private final SysUIComponentImpl sysUIComponentImpl;
        private final SysUIUnfoldComponentImpl sysUIUnfoldComponentImpl;
        private Provider<UnfoldLightRevealOverlayAnimation> unfoldLightRevealOverlayAnimationProvider;
        private Provider<UnfoldTransitionWallpaperController> unfoldTransitionWallpaperControllerProvider;

        private SysUIUnfoldComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
            this.sysUIUnfoldComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(unfoldTransitionProgressProvider, naturalRotationUnfoldProgressProvider, scopedUnfoldTransitionProgressProvider);
        }

        private void initialize(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider) {
            this.p2Provider = InstanceFactory.create(naturalRotationUnfoldProgressProvider);
            this.keyguardUnfoldTransitionProvider = DoubleCheck.provider(KeyguardUnfoldTransition_Factory.create(this.globalRootComponentImpl.contextProvider, this.p2Provider));
            Factory create = InstanceFactory.create(scopedUnfoldTransitionProgressProvider);
            this.p3Provider = create;
            this.statusBarMoveFromCenterAnimationControllerProvider = DoubleCheck.provider(StatusBarMoveFromCenterAnimationController_Factory.create(create, this.globalRootComponentImpl.provideWindowManagerProvider));
            this.notificationPanelUnfoldAnimationControllerProvider = DoubleCheck.provider(NotificationPanelUnfoldAnimationController_Factory.create(this.globalRootComponentImpl.contextProvider, this.p2Provider));
            this.foldAodAnimationControllerProvider = DoubleCheck.provider(FoldAodAnimationController_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideDeviceStateManagerProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.sysUIComponentImpl.globalSettingsImplProvider));
            Factory create2 = InstanceFactory.create(unfoldTransitionProgressProvider);
            this.p1Provider = create2;
            this.unfoldTransitionWallpaperControllerProvider = DoubleCheck.provider(UnfoldTransitionWallpaperController_Factory.create(create2, this.sysUIComponentImpl.wallpaperControllerProvider));
            this.unfoldLightRevealOverlayAnimationProvider = DoubleCheck.provider(UnfoldLightRevealOverlayAnimation_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideDeviceStateManagerProvider, this.globalRootComponentImpl.provideDisplayManagerProvider, this.p1Provider, this.sysUIComponentImpl.setDisplayAreaHelperProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.globalRootComponentImpl.provideIWindowManagerProvider));
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

    private static final class CoordinatorsSubcomponentImpl implements CoordinatorsSubcomponent {
        private Provider<AppOpsCoordinator> appOpsCoordinatorProvider;
        private Provider<BubbleCoordinator> bubbleCoordinatorProvider;
        private Provider<ConversationCoordinator> conversationCoordinatorProvider;
        private final CoordinatorsSubcomponentImpl coordinatorsSubcomponentImpl;
        private Provider<DataStoreCoordinator> dataStoreCoordinatorProvider;
        private Provider<DebugModeCoordinator> debugModeCoordinatorProvider;
        private Provider<DeviceProvisionedCoordinator> deviceProvisionedCoordinatorProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<GroupCountCoordinator> groupCountCoordinatorProvider;
        private Provider<GutsCoordinatorLogger> gutsCoordinatorLoggerProvider;
        private Provider<GutsCoordinator> gutsCoordinatorProvider;
        private Provider<HeadsUpCoordinatorLogger> headsUpCoordinatorLoggerProvider;
        private Provider<HeadsUpCoordinator> headsUpCoordinatorProvider;
        private Provider<HideLocallyDismissedNotifsCoordinator> hideLocallyDismissedNotifsCoordinatorProvider;
        private Provider<HideNotifsForOtherUsersCoordinator> hideNotifsForOtherUsersCoordinatorProvider;
        private Provider<KeyguardCoordinator> keyguardCoordinatorProvider;
        private Provider<MediaCoordinator> mediaCoordinatorProvider;
        private Provider<NotifCoordinatorsImpl> notifCoordinatorsImplProvider;
        private Provider<PreparationCoordinatorLogger> preparationCoordinatorLoggerProvider;
        private Provider<PreparationCoordinator> preparationCoordinatorProvider;
        private Provider<RankingCoordinator> rankingCoordinatorProvider;
        private Provider<RemoteInputCoordinator> remoteInputCoordinatorProvider;
        private Provider<RowAppearanceCoordinator> rowAppearanceCoordinatorProvider;
        private Provider sensitiveContentCoordinatorImplProvider;
        private Provider<SharedCoordinatorLogger> sharedCoordinatorLoggerProvider;
        private Provider<SmartspaceDedupingCoordinator> smartspaceDedupingCoordinatorProvider;
        private Provider<StackCoordinator> stackCoordinatorProvider;
        private final SysUIComponentImpl sysUIComponentImpl;
        private Provider<ViewConfigCoordinator> viewConfigCoordinatorProvider;

        private CoordinatorsSubcomponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.coordinatorsSubcomponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize();
        }

        private void initialize() {
            this.dataStoreCoordinatorProvider = DoubleCheck.provider(DataStoreCoordinator_Factory.create(this.sysUIComponentImpl.notifLiveDataStoreImplProvider));
            this.hideLocallyDismissedNotifsCoordinatorProvider = DoubleCheck.provider(HideLocallyDismissedNotifsCoordinator_Factory.create());
            this.sharedCoordinatorLoggerProvider = SharedCoordinatorLogger_Factory.create(this.sysUIComponentImpl.provideNotificationsLogBufferProvider);
            this.hideNotifsForOtherUsersCoordinatorProvider = DoubleCheck.provider(HideNotifsForOtherUsersCoordinator_Factory.create(this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.sharedCoordinatorLoggerProvider));
            this.keyguardCoordinatorProvider = DoubleCheck.provider(KeyguardCoordinator_Factory.create(this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.highPriorityProvider, this.sysUIComponentImpl.sectionHeaderVisibilityProvider, this.sysUIComponentImpl.keyguardNotificationVisibilityProviderImplProvider, this.sharedCoordinatorLoggerProvider));
            this.rankingCoordinatorProvider = DoubleCheck.provider(RankingCoordinator_Factory.create(this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.highPriorityProvider, this.sysUIComponentImpl.sectionClassifierProvider, this.sysUIComponentImpl.providesAlertingHeaderNodeControllerProvider, this.sysUIComponentImpl.providesSilentHeaderControllerProvider, this.sysUIComponentImpl.providesSilentHeaderNodeControllerProvider));
            this.appOpsCoordinatorProvider = DoubleCheck.provider(AppOpsCoordinator_Factory.create(this.sysUIComponentImpl.foregroundServiceControllerProvider, this.sysUIComponentImpl.appOpsControllerImplProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.deviceProvisionedCoordinatorProvider = DoubleCheck.provider(DeviceProvisionedCoordinator_Factory.create(this.sysUIComponentImpl.bindDeviceProvisionedControllerProvider, this.globalRootComponentImpl.provideIPackageManagerProvider));
            this.bubbleCoordinatorProvider = DoubleCheck.provider(BubbleCoordinator_Factory.create(this.sysUIComponentImpl.provideBubblesManagerProvider, this.sysUIComponentImpl.setBubblesProvider, this.sysUIComponentImpl.notifCollectionProvider));
            HeadsUpCoordinatorLogger_Factory create = HeadsUpCoordinatorLogger_Factory.create(this.sysUIComponentImpl.provideNotificationHeadsUpLogBufferProvider);
            this.headsUpCoordinatorLoggerProvider = create;
            this.headsUpCoordinatorProvider = DoubleCheck.provider(HeadsUpCoordinator_Factory.create(create, this.sysUIComponentImpl.bindSystemClockProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.headsUpViewBinderProvider, this.sysUIComponentImpl.notificationInterruptStateProviderImplProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.providesIncomingHeaderNodeControllerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.gutsCoordinatorLoggerProvider = GutsCoordinatorLogger_Factory.create(this.sysUIComponentImpl.provideNotificationsLogBufferProvider);
            this.gutsCoordinatorProvider = DoubleCheck.provider(GutsCoordinator_Factory.create(this.sysUIComponentImpl.provideNotifGutsViewManagerProvider, this.gutsCoordinatorLoggerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.conversationCoordinatorProvider = DoubleCheck.provider(ConversationCoordinator_Factory.create(this.sysUIComponentImpl.peopleNotificationIdentifierImplProvider, this.sysUIComponentImpl.iconManagerProvider, this.sysUIComponentImpl.providesPeopleHeaderNodeControllerProvider));
            this.debugModeCoordinatorProvider = DoubleCheck.provider(DebugModeCoordinator_Factory.create(this.sysUIComponentImpl.debugModeFilterProvider));
            this.groupCountCoordinatorProvider = DoubleCheck.provider(GroupCountCoordinator_Factory.create());
            this.mediaCoordinatorProvider = DoubleCheck.provider(MediaCoordinator_Factory.create(this.sysUIComponentImpl.mediaFeatureFlagProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.sysUIComponentImpl.iconManagerProvider));
            PreparationCoordinatorLogger_Factory create2 = PreparationCoordinatorLogger_Factory.create(this.sysUIComponentImpl.provideNotificationsLogBufferProvider);
            this.preparationCoordinatorLoggerProvider = create2;
            this.preparationCoordinatorProvider = DoubleCheck.provider(PreparationCoordinator_Factory.create(create2, this.sysUIComponentImpl.notifInflaterImplProvider, this.sysUIComponentImpl.notifInflationErrorManagerProvider, this.sysUIComponentImpl.notifViewBarnProvider, this.sysUIComponentImpl.notifUiAdjustmentProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.sysUIComponentImpl.bindEventManagerImplProvider));
            this.remoteInputCoordinatorProvider = DoubleCheck.provider(RemoteInputCoordinator_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.sysUIComponentImpl.remoteInputNotificationRebuilderProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.sysUIComponentImpl.provideSmartReplyControllerProvider));
            this.rowAppearanceCoordinatorProvider = DoubleCheck.provider(RowAppearanceCoordinator_Factory.create(this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.assistantFeedbackControllerProvider, this.sysUIComponentImpl.sectionClassifierProvider));
            this.stackCoordinatorProvider = DoubleCheck.provider(StackCoordinator_Factory.create(this.sysUIComponentImpl.notificationIconAreaControllerProvider));
            this.smartspaceDedupingCoordinatorProvider = DoubleCheck.provider(SmartspaceDedupingCoordinator_Factory.create(this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.lockscreenSmartspaceControllerProvider, this.sysUIComponentImpl.provideNotificationEntryManagerProvider, this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.sysUIComponentImpl.notifPipelineProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.sysUIComponentImpl.bindSystemClockProvider));
            this.viewConfigCoordinatorProvider = DoubleCheck.provider(ViewConfigCoordinator_Factory.create(this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.sysUIComponentImpl.provideNotificationGutsManagerProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider));
            this.sensitiveContentCoordinatorImplProvider = DoubleCheck.provider(SensitiveContentCoordinatorImpl_Factory.create(this.sysUIComponentImpl.dynamicPrivacyControllerProvider, this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider));
            this.notifCoordinatorsImplProvider = DoubleCheck.provider(NotifCoordinatorsImpl_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.sysUIComponentImpl.notifPipelineFlagsProvider, this.dataStoreCoordinatorProvider, this.hideLocallyDismissedNotifsCoordinatorProvider, this.hideNotifsForOtherUsersCoordinatorProvider, this.keyguardCoordinatorProvider, this.rankingCoordinatorProvider, this.appOpsCoordinatorProvider, this.deviceProvisionedCoordinatorProvider, this.bubbleCoordinatorProvider, this.headsUpCoordinatorProvider, this.gutsCoordinatorProvider, this.conversationCoordinatorProvider, this.debugModeCoordinatorProvider, this.groupCountCoordinatorProvider, this.mediaCoordinatorProvider, this.preparationCoordinatorProvider, this.remoteInputCoordinatorProvider, this.rowAppearanceCoordinatorProvider, this.stackCoordinatorProvider, this.sysUIComponentImpl.shadeEventCoordinatorProvider, this.smartspaceDedupingCoordinatorProvider, this.viewConfigCoordinatorProvider, this.sysUIComponentImpl.visualStabilityCoordinatorProvider, this.sensitiveContentCoordinatorImplProvider));
        }

        public NotifCoordinators getNotifCoordinators() {
            return this.notifCoordinatorsImplProvider.get();
        }
    }

    private static final class FragmentCreatorImpl implements FragmentService.FragmentCreator {
        private final FragmentCreatorImpl fragmentCreatorImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final SysUIComponentImpl sysUIComponentImpl;

        private FragmentCreatorImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.fragmentCreatorImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
        }

        private QSFragmentDisableFlagsLogger qSFragmentDisableFlagsLogger() {
            return new QSFragmentDisableFlagsLogger((LogBuffer) this.sysUIComponentImpl.provideQSFragmentDisableLogBufferProvider.get(), (DisableFlagsLogger) this.sysUIComponentImpl.disableFlagsLoggerProvider.get());
        }

        public QSFragment createQSFragment() {
            return new QSFragment((RemoteInputQuickSettingsDisabler) this.sysUIComponentImpl.remoteInputQuickSettingsDisablerProvider.get(), (QSTileHost) this.sysUIComponentImpl.qSTileHostProvider.get(), (StatusBarStateController) this.sysUIComponentImpl.statusBarStateControllerImplProvider.get(), (CommandQueue) this.sysUIComponentImpl.provideCommandQueueProvider.get(), (MediaHost) this.sysUIComponentImpl.providesQSMediaHostProvider.get(), (MediaHost) this.sysUIComponentImpl.providesQuickQSMediaHostProvider.get(), (KeyguardBypassController) this.sysUIComponentImpl.keyguardBypassControllerProvider.get(), new QSFragmentComponentFactory(this.globalRootComponentImpl, this.sysUIComponentImpl), qSFragmentDisableFlagsLogger(), (FalsingManager) this.sysUIComponentImpl.falsingManagerProxyProvider.get(), (DumpManager) this.globalRootComponentImpl.dumpManagerProvider.get());
        }
    }

    private static final class SectionHeaderControllerSubcomponentImpl implements SectionHeaderControllerSubcomponent {
        private Provider<String> clickIntentActionProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<Integer> headerTextProvider;
        private Provider<String> nodeLabelProvider;
        private final SectionHeaderControllerSubcomponentImpl sectionHeaderControllerSubcomponentImpl;
        private Provider<SectionHeaderNodeControllerImpl> sectionHeaderNodeControllerImplProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private SectionHeaderControllerSubcomponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, String str, Integer num, String str2) {
            this.sectionHeaderControllerSubcomponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(str, num, str2);
        }

        private void initialize(String str, Integer num, String str2) {
            this.nodeLabelProvider = InstanceFactory.create(str);
            this.headerTextProvider = InstanceFactory.create(num);
            this.clickIntentActionProvider = InstanceFactory.create(str2);
            this.sectionHeaderNodeControllerImplProvider = DoubleCheck.provider(SectionHeaderNodeControllerImpl_Factory.create(this.nodeLabelProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.headerTextProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.clickIntentActionProvider));
        }

        public NodeController getNodeController() {
            return this.sectionHeaderNodeControllerImplProvider.get();
        }

        public SectionHeaderController getHeaderController() {
            return this.sectionHeaderNodeControllerImplProvider.get();
        }
    }

    private static final class NavigationBarComponentImpl implements NavigationBarComponent {
        private Provider<Context> contextProvider;
        private Provider<DeadZone> deadZoneProvider;
        private Provider<LightBarController.Factory> factoryProvider;
        private Provider<AutoHideController.Factory> factoryProvider2;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final NavigationBarComponentImpl navigationBarComponentImpl;
        private Provider<NavigationBar> navigationBarProvider;
        private Provider<NavigationBarTransitions> navigationBarTransitionsProvider;
        private Provider<EdgeBackGestureHandler> provideEdgeBackGestureHandlerProvider;
        private Provider<LayoutInflater> provideLayoutInflaterProvider;
        private Provider<NavigationBarFrame> provideNavigationBarFrameProvider;
        private Provider<NavigationBarView> provideNavigationBarviewProvider;
        private Provider<WindowManager> provideWindowManagerProvider;
        private Provider<Bundle> savedStateProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private NavigationBarComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, Context context, Bundle bundle) {
            this.navigationBarComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
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
            this.factoryProvider = LightBarController_Factory_Factory.create(this.sysUIComponentImpl.darkIconDispatcherImplProvider, this.sysUIComponentImpl.provideBatteryControllerProvider, this.sysUIComponentImpl.navigationModeControllerProvider, this.globalRootComponentImpl.dumpManagerProvider);
            this.factoryProvider2 = AutoHideController_Factory_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider);
            this.deadZoneProvider = DeadZone_Factory.create(this.provideNavigationBarviewProvider);
            this.navigationBarTransitionsProvider = DoubleCheck.provider(NavigationBarTransitions_Factory.create(this.provideNavigationBarviewProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.sysUIComponentImpl.factoryProvider));
            this.provideEdgeBackGestureHandlerProvider = DoubleCheck.provider(NavigationBarModule_ProvideEdgeBackGestureHandlerFactory.create(this.sysUIComponentImpl.factoryProvider2, this.contextProvider));
            this.navigationBarProvider = DoubleCheck.provider(NavigationBar_Factory.create(this.provideNavigationBarviewProvider, this.provideNavigationBarFrameProvider, this.savedStateProvider, this.contextProvider, this.provideWindowManagerProvider, this.sysUIComponentImpl.assistManagerProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.sysUIComponentImpl.bindDeviceProvisionedControllerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.sysUIComponentImpl.overviewProxyServiceProvider, this.sysUIComponentImpl.navigationModeControllerProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.sysUIComponentImpl.provideSysUiStateProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.sysUIComponentImpl.provideCommandQueueProvider, this.sysUIComponentImpl.setPipProvider, this.sysUIComponentImpl.optionalOfRecentsProvider, this.sysUIComponentImpl.optionalOfCentralSurfacesProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.notificationShadeDepthControllerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.navBarHelperProvider, this.sysUIComponentImpl.lightBarControllerProvider, this.factoryProvider, this.sysUIComponentImpl.autoHideControllerProvider, this.factoryProvider2, this.globalRootComponentImpl.provideOptionalTelecomManagerProvider, this.globalRootComponentImpl.provideInputMethodManagerProvider, this.deadZoneProvider, this.sysUIComponentImpl.deviceConfigProxyProvider, this.navigationBarTransitionsProvider, this.provideEdgeBackGestureHandlerProvider, this.sysUIComponentImpl.setBackAnimationProvider, this.sysUIComponentImpl.provideUserTrackerProvider));
        }

        public NavigationBar getNavigationBar() {
            return this.navigationBarProvider.get();
        }
    }

    private static final class StatusBarFragmentComponentImpl implements StatusBarFragmentComponent {
        private Provider<StatusBarUserSwitcherController> bindStatusBarUserSwitcherControllerProvider;
        private final CentralSurfacesComponentImpl centralSurfacesComponentImpl;
        private Provider<CollapsedStatusBarFragment> collapsedStatusBarFragmentProvider;
        private Provider<PhoneStatusBarViewController.Factory> factoryProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<HeadsUpAppearanceController> headsUpAppearanceControllerProvider;
        private Provider<LightsOutNotifController> lightsOutNotifControllerProvider;
        private Provider<BatteryMeterView> provideBatteryMeterViewProvider;
        private Provider<Clock> provideClockProvider;
        private Provider<View> provideLightsOutNotifViewProvider;
        private Provider<Optional<View>> provideOperatorFrameNameViewProvider;
        private Provider<View> provideOperatorNameViewProvider;
        private Provider<PhoneStatusBarTransitions> providePhoneStatusBarTransitionsProvider;
        private Provider<PhoneStatusBarViewController> providePhoneStatusBarViewControllerProvider;
        private Provider<PhoneStatusBarView> providePhoneStatusBarViewProvider;
        private Provider<StatusBarUserSwitcherContainer> provideStatusBarUserSwitcherContainerProvider;
        private Provider<HeadsUpStatusBarView> providesHeasdUpStatusBarViewProvider;
        private Provider<StatusBarDemoMode> statusBarDemoModeProvider;
        private final StatusBarFragmentComponentImpl statusBarFragmentComponentImpl;
        private Provider<StatusBarUserSwitcherControllerImpl> statusBarUserSwitcherControllerImplProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private StatusBarFragmentComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, CentralSurfacesComponentImpl centralSurfacesComponentImpl2, CollapsedStatusBarFragment collapsedStatusBarFragment) {
            this.statusBarFragmentComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.centralSurfacesComponentImpl = centralSurfacesComponentImpl2;
            initialize(collapsedStatusBarFragment);
        }

        private void initialize(CollapsedStatusBarFragment collapsedStatusBarFragment) {
            Factory create = InstanceFactory.create(collapsedStatusBarFragment);
            this.collapsedStatusBarFragmentProvider = create;
            Provider<PhoneStatusBarView> provider = DoubleCheck.provider(StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory.create(create));
            this.providePhoneStatusBarViewProvider = provider;
            this.provideBatteryMeterViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideBatteryMeterViewFactory.create(provider));
            Provider<StatusBarUserSwitcherContainer> provider2 = DoubleCheck.provider(C3138x6673e9b7.create(this.providePhoneStatusBarViewProvider));
            this.provideStatusBarUserSwitcherContainerProvider = provider2;
            StatusBarUserSwitcherControllerImpl_Factory create2 = StatusBarUserSwitcherControllerImpl_Factory.create(provider2, this.sysUIComponentImpl.statusBarUserInfoTrackerProvider, this.sysUIComponentImpl.statusBarUserSwitcherFeatureControllerProvider, this.sysUIComponentImpl.userSwitchDialogControllerProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.falsingManagerProxyProvider);
            this.statusBarUserSwitcherControllerImplProvider = create2;
            this.bindStatusBarUserSwitcherControllerProvider = DoubleCheck.provider(create2);
            PhoneStatusBarViewController_Factory_Factory create3 = PhoneStatusBarViewController_Factory_Factory.create(this.sysUIComponentImpl.provideSysUIUnfoldComponentProvider, this.globalRootComponentImpl.provideStatusBarScopedTransitionProvider, this.bindStatusBarUserSwitcherControllerProvider, this.sysUIComponentImpl.viewUtilProvider, this.sysUIComponentImpl.configurationControllerImplProvider);
            this.factoryProvider = create3;
            this.providePhoneStatusBarViewControllerProvider = DoubleCheck.provider(C3137xfe780fd7.create(create3, this.providePhoneStatusBarViewProvider, this.centralSurfacesComponentImpl.notificationPanelViewControllerProvider));
            this.providesHeasdUpStatusBarViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory.create(this.providePhoneStatusBarViewProvider));
            this.provideClockProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideClockFactory.create(this.providePhoneStatusBarViewProvider));
            this.provideOperatorFrameNameViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory.create(this.providePhoneStatusBarViewProvider));
            this.headsUpAppearanceControllerProvider = DoubleCheck.provider(HeadsUpAppearanceController_Factory.create(this.sysUIComponentImpl.notificationIconAreaControllerProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.notificationWakeUpCoordinatorProvider, this.sysUIComponentImpl.darkIconDispatcherImplProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.provideCommandQueueProvider, this.centralSurfacesComponentImpl.notificationStackScrollLayoutControllerProvider, this.centralSurfacesComponentImpl.notificationPanelViewControllerProvider, this.providesHeasdUpStatusBarViewProvider, this.provideClockProvider, this.provideOperatorFrameNameViewProvider));
            Provider<View> provider3 = DoubleCheck.provider(StatusBarFragmentModule_ProvideLightsOutNotifViewFactory.create(this.providePhoneStatusBarViewProvider));
            this.provideLightsOutNotifViewProvider = provider3;
            this.lightsOutNotifControllerProvider = DoubleCheck.provider(LightsOutNotifController_Factory.create(provider3, this.globalRootComponentImpl.provideWindowManagerProvider, this.sysUIComponentImpl.notifLiveDataStoreImplProvider, this.sysUIComponentImpl.provideCommandQueueProvider));
            this.provideOperatorNameViewProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvideOperatorNameViewFactory.create(this.providePhoneStatusBarViewProvider));
            this.providePhoneStatusBarTransitionsProvider = DoubleCheck.provider(StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory.create(this.providePhoneStatusBarViewProvider, this.sysUIComponentImpl.statusBarWindowControllerProvider));
            this.statusBarDemoModeProvider = DoubleCheck.provider(StatusBarDemoMode_Factory.create(this.provideClockProvider, this.provideOperatorNameViewProvider, this.sysUIComponentImpl.provideDemoModeControllerProvider, this.providePhoneStatusBarTransitionsProvider, this.sysUIComponentImpl.navigationBarControllerProvider, this.globalRootComponentImpl.provideDisplayIdProvider));
        }

        public BatteryMeterViewController getBatteryMeterViewController() {
            return new BatteryMeterViewController(this.provideBatteryMeterViewProvider.get(), (ConfigurationController) this.sysUIComponentImpl.configurationControllerImplProvider.get(), (TunerService) this.sysUIComponentImpl.tunerServiceImplProvider.get(), (BroadcastDispatcher) this.sysUIComponentImpl.broadcastDispatcherProvider.get(), this.globalRootComponentImpl.mainHandler(), (ContentResolver) this.globalRootComponentImpl.provideContentResolverProvider.get(), (BatteryController) this.sysUIComponentImpl.provideBatteryControllerProvider.get());
        }

        public PhoneStatusBarView getPhoneStatusBarView() {
            return this.providePhoneStatusBarViewProvider.get();
        }

        public PhoneStatusBarViewController getPhoneStatusBarViewController() {
            return this.providePhoneStatusBarViewControllerProvider.get();
        }

        public HeadsUpAppearanceController getHeadsUpAppearanceController() {
            return this.headsUpAppearanceControllerProvider.get();
        }

        public LightsOutNotifController getLightsOutNotifController() {
            return this.lightsOutNotifControllerProvider.get();
        }

        public StatusBarDemoMode getStatusBarDemoMode() {
            return this.statusBarDemoModeProvider.get();
        }

        public PhoneStatusBarTransitions getPhoneStatusBarTransitions() {
            return this.providePhoneStatusBarTransitionsProvider.get();
        }
    }

    private static final class CentralSurfacesComponentImpl implements CentralSurfacesComponent {
        private Provider<AuthRippleController> authRippleControllerProvider;
        private Provider builderProvider;
        private Provider<FlingAnimationUtils.Builder> builderProvider2;
        private Provider<CarrierTextManager.Builder> builderProvider3;
        private Provider<QSCarrierGroupController.Builder> builderProvider4;
        private Provider<CentralSurfacesCommandQueueCallbacks> centralSurfacesCommandQueueCallbacksProvider;
        private final CentralSurfacesComponentImpl centralSurfacesComponentImpl;
        private Provider<BatteryMeterViewController> createBatteryMeterViewControllerProvider;
        private Provider<BatteryMeterView> createBatteryMeterViewProvider;
        private Provider<EmergencyButtonController.Factory> factoryProvider;
        private Provider<AuthRippleView> getAuthRippleViewProvider;
        private Provider<BatteryMeterViewController> getBatteryMeterViewControllerProvider;
        private Provider<BatteryMeterView> getBatteryMeterViewProvider;
        private Provider<View> getLargeScreenShadeHeaderBarViewProvider;
        private Provider<LockIconView> getLockIconViewProvider;
        private Provider<NotificationPanelView> getNotificationPanelViewProvider;
        private Provider<NotificationsQuickSettingsContainer> getNotificationsQuickSettingsContainerProvider;
        private Provider<OngoingPrivacyChip> getSplitShadeOngoingPrivacyChipProvider;
        private Provider<TapAgainView> getTapAgainViewProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<HeaderPrivacyIconsController> headerPrivacyIconsControllerProvider;
        private Provider<LargeScreenShadeHeaderController> largeScreenShadeHeaderControllerProvider;
        private Provider<LockIconViewController> lockIconViewControllerProvider;
        private Provider<NTQSStatusBarController> nTQSStatusBarControllerProvider;
        private Provider<NotificationLaunchAnimatorControllerProvider> notificationLaunchAnimatorControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
        private Provider<NotificationShadeWindowViewController> notificationShadeWindowViewControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationStackScrollLayoutController> notificationStackScrollLayoutControllerProvider;
        private Provider<NotificationStackScrollLogger> notificationStackScrollLoggerProvider;
        private Provider<NotificationsQSContainerController> notificationsQSContainerControllerProvider;
        private Provider<NotificationListContainer> provideListContainerProvider;
        private Provider<NTQSStatusBar> providesNTQSStatusBarProvider;
        private Provider<NotificationShadeWindowView> providesNotificationShadeWindowViewProvider;
        private Provider<NotificationShelf> providesNotificationShelfProvider;
        private Provider<NotificationStackScrollLayout> providesNotificationStackScrollLayoutProvider;
        private Provider<NotificationShelfController> providesStatusBarWindowViewProvider;
        private Provider<StatusIconContainer> providesStatusIconContainerProvider;
        private Provider<StackStateLogger> stackStateLoggerProvider;
        private Provider<StatusBarHeadsUpChangeListener> statusBarHeadsUpChangeListenerProvider;
        private Provider<StatusBarInitializer> statusBarInitializerProvider;
        private Provider<StatusBarNotificationActivityStarterLogger> statusBarNotificationActivityStarterLoggerProvider;
        private Provider statusBarNotificationActivityStarterProvider;
        private Provider statusBarNotificationPresenterProvider;
        private final SysUIComponentImpl sysUIComponentImpl;
        private Provider<TapAgainViewController> tapAgainViewControllerProvider;

        private CentralSurfacesComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2) {
            this.centralSurfacesComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize();
        }

        private CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger() {
            return new CollapsedStatusBarFragmentLogger((LogBuffer) this.sysUIComponentImpl.provideCollapsedSbFragmentLogBufferProvider.get(), (DisableFlagsLogger) this.sysUIComponentImpl.disableFlagsLoggerProvider.get());
        }

        private OperatorNameViewController.Factory operatorNameViewControllerFactory() {
            return new OperatorNameViewController.Factory((DarkIconDispatcher) this.sysUIComponentImpl.darkIconDispatcherImplProvider.get(), (NetworkController) this.sysUIComponentImpl.networkControllerImplProvider.get(), (TunerService) this.sysUIComponentImpl.tunerServiceImplProvider.get(), (TelephonyManager) this.globalRootComponentImpl.provideTelephonyManagerProvider.get(), (KeyguardUpdateMonitor) this.sysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (CarrierConfigTracker) this.sysUIComponentImpl.carrierConfigTrackerProvider.get());
        }

        private void initialize() {
            Provider<NotificationShadeWindowView> provider = DoubleCheck.provider(StatusBarViewModule_ProvidesNotificationShadeWindowViewFactory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesNotificationShadeWindowViewProvider = provider;
            this.providesNotificationStackScrollLayoutProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesNotificationStackScrollLayoutFactory.create(provider));
            this.providesNotificationShelfProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesNotificationShelfFactory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider, this.providesNotificationStackScrollLayoutProvider));
            this.providesStatusBarWindowViewProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesStatusBarWindowViewFactory.create(this.sysUIComponentImpl.notificationShelfComponentBuilderProvider, this.providesNotificationShelfProvider));
            this.builderProvider = NotificationSwipeHelper_Builder_Factory.create(this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideViewConfigurationProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider);
            this.stackStateLoggerProvider = StackStateLogger_Factory.create(this.sysUIComponentImpl.provideNotificationHeadsUpLogBufferProvider);
            this.notificationStackScrollLoggerProvider = NotificationStackScrollLogger_Factory.create(this.sysUIComponentImpl.provideNotificationHeadsUpLogBufferProvider);
            this.notificationStackScrollLayoutControllerProvider = DoubleCheck.provider(NotificationStackScrollLayoutController_Factory.create(this.sysUIComponentImpl.provideAllowNotificationLongPressProvider, this.sysUIComponentImpl.provideNotificationGutsManagerProvider, this.sysUIComponentImpl.provideNotificationVisibilityProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.notificationRoundnessManagerProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.bindDeviceProvisionedControllerProvider, this.sysUIComponentImpl.dynamicPrivacyControllerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.keyguardMediaControllerProvider, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.zenModeControllerImplProvider, this.sysUIComponentImpl.sysuiColorExtractorProvider, this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.sysUIComponentImpl.falsingCollectorImplProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.globalRootComponentImpl.provideResourcesProvider, this.builderProvider, this.sysUIComponentImpl.centralSurfacesImplProvider, this.sysUIComponentImpl.scrimControllerProvider, this.sysUIComponentImpl.notificationGroupManagerLegacyProvider, this.sysUIComponentImpl.provideGroupExpansionManagerProvider, this.sysUIComponentImpl.providesSilentHeaderControllerProvider, this.sysUIComponentImpl.notifPipelineFlagsProvider, this.sysUIComponentImpl.notifPipelineProvider, this.sysUIComponentImpl.notifCollectionProvider, this.sysUIComponentImpl.provideNotificationEntryManagerProvider, this.sysUIComponentImpl.lockscreenShadeTransitionControllerProvider, this.sysUIComponentImpl.shadeTransitionControllerProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.provideVisualStabilityManagerProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.stackStateLoggerProvider, this.notificationStackScrollLoggerProvider, this.sysUIComponentImpl.notificationStackSizeCalculatorProvider));
            this.getNotificationPanelViewProvider = DoubleCheck.provider(StatusBarViewModule_GetNotificationPanelViewFactory.create(this.providesNotificationShadeWindowViewProvider));
            this.builderProvider2 = FlingAnimationUtils_Builder_Factory.create(this.globalRootComponentImpl.provideDisplayMetricsProvider);
            Provider<NotificationsQuickSettingsContainer> provider2 = DoubleCheck.provider(C3131x8fd6ebda.create(this.providesNotificationShadeWindowViewProvider));
            this.getNotificationsQuickSettingsContainerProvider = provider2;
            this.notificationsQSContainerControllerProvider = NotificationsQSContainerController_Factory.create(provider2, this.sysUIComponentImpl.navigationModeControllerProvider, this.sysUIComponentImpl.overviewProxyServiceProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider);
            this.getLockIconViewProvider = DoubleCheck.provider(StatusBarViewModule_GetLockIconViewFactory.create(this.providesNotificationShadeWindowViewProvider));
            this.getAuthRippleViewProvider = DoubleCheck.provider(StatusBarViewModule_GetAuthRippleViewFactory.create(this.providesNotificationShadeWindowViewProvider));
            this.authRippleControllerProvider = DoubleCheck.provider(AuthRippleController_Factory.create(this.sysUIComponentImpl.centralSurfacesImplProvider, this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.authControllerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.sysUIComponentImpl.commandRegistryProvider, this.sysUIComponentImpl.notificationShadeWindowControllerImplProvider, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.biometricUnlockControllerProvider, this.sysUIComponentImpl.udfpsControllerProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.getAuthRippleViewProvider));
            this.lockIconViewControllerProvider = DoubleCheck.provider(LockIconViewController_Factory.create(this.getLockIconViewProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.authControllerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.sysUIComponentImpl.vibratorHelperProvider, this.authRippleControllerProvider, this.globalRootComponentImpl.provideResourcesProvider));
            Provider<TapAgainView> provider3 = DoubleCheck.provider(StatusBarViewModule_GetTapAgainViewFactory.create(this.getNotificationPanelViewProvider));
            this.getTapAgainViewProvider = provider3;
            this.tapAgainViewControllerProvider = DoubleCheck.provider(TapAgainViewController_Factory.create(provider3, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.sysUIComponentImpl.configurationControllerImplProvider, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create()));
            Provider<View> provider4 = DoubleCheck.provider(StatusBarViewModule_GetLargeScreenShadeHeaderBarViewFactory.create(this.providesNotificationShadeWindowViewProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider));
            this.getLargeScreenShadeHeaderBarViewProvider = provider4;
            this.getSplitShadeOngoingPrivacyChipProvider = DoubleCheck.provider(StatusBarViewModule_GetSplitShadeOngoingPrivacyChipFactory.create(provider4));
            this.providesStatusIconContainerProvider = DoubleCheck.provider(StatusBarViewModule_ProvidesStatusIconContainerFactory.create(this.getLargeScreenShadeHeaderBarViewProvider));
            this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(this.sysUIComponentImpl.privacyItemControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.getSplitShadeOngoingPrivacyChipProvider, this.sysUIComponentImpl.privacyDialogControllerProvider, this.sysUIComponentImpl.privacyLoggerProvider, this.providesStatusIconContainerProvider, this.globalRootComponentImpl.providePermissionManagerProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.appOpsControllerImplProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideSafetyCenterManagerProvider);
            this.builderProvider3 = CarrierTextManager_Builder_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideWifiManagerProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.sysUIComponentImpl.telephonyListenerManagerProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.carrierNameCustomizationProvider);
            this.builderProvider4 = QSCarrierGroupController_Builder_Factory.create(this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.sysUIComponentImpl.networkControllerImplProvider, this.builderProvider3, this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.carrierConfigTrackerProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.subscriptionManagerSlotIndexResolverProvider);
            Provider<BatteryMeterView> provider5 = DoubleCheck.provider(StatusBarViewModule_GetBatteryMeterViewFactory.create(this.getLargeScreenShadeHeaderBarViewProvider));
            this.getBatteryMeterViewProvider = provider5;
            this.getBatteryMeterViewControllerProvider = DoubleCheck.provider(StatusBarViewModule_GetBatteryMeterViewControllerFactory.create(provider5, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.sysUIComponentImpl.provideBatteryControllerProvider));
            this.largeScreenShadeHeaderControllerProvider = DoubleCheck.provider(LargeScreenShadeHeaderController_Factory.create(this.getLargeScreenShadeHeaderBarViewProvider, this.sysUIComponentImpl.statusBarIconControllerImplProvider, this.headerPrivacyIconsControllerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.builderProvider4, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.getBatteryMeterViewControllerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.provideListContainerProvider = DoubleCheck.provider(C2839xab7007e4.create(this.notificationStackScrollLayoutControllerProvider));
            this.providesNTQSStatusBarProvider = StatusBarViewModule_ProvidesNTQSStatusBarFactory.create(this.getNotificationPanelViewProvider);
            StatusBarViewModule_CreateBatteryMeterViewFactory create = StatusBarViewModule_CreateBatteryMeterViewFactory.create(this.getNotificationPanelViewProvider);
            this.createBatteryMeterViewProvider = create;
            this.createBatteryMeterViewControllerProvider = StatusBarViewModule_CreateBatteryMeterViewControllerFactory.create(create, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.sysUIComponentImpl.provideBatteryControllerProvider);
            this.nTQSStatusBarControllerProvider = NTQSStatusBarController_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesNTQSStatusBarProvider, this.sysUIComponentImpl.privacyDialogControllerProvider, this.sysUIComponentImpl.privacyItemControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.privacyLoggerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.createBatteryMeterViewControllerProvider);
            this.factoryProvider = EmergencyButtonController_Factory_Factory.create(this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideActivityTaskManagerProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.globalRootComponentImpl.provideTelecomManagerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider);
            this.notificationPanelViewControllerProvider = DoubleCheck.provider(NotificationPanelViewController_Factory.create(this.getNotificationPanelViewProvider, this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.notificationWakeUpCoordinatorProvider, this.sysUIComponentImpl.pulseExpansionHandlerProvider, this.sysUIComponentImpl.dynamicPrivacyControllerProvider, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.falsingCollectorImplProvider, this.sysUIComponentImpl.provideNotificationEntryManagerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.statusBarWindowStateControllerProvider, this.sysUIComponentImpl.notificationShadeWindowControllerImplProvider, this.sysUIComponentImpl.dozeLogProvider, this.sysUIComponentImpl.dozeParametersProvider, this.sysUIComponentImpl.provideCommandQueueProvider, this.sysUIComponentImpl.vibratorHelperProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.globalRootComponentImpl.provideDisplayIdProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.globalRootComponentImpl.provideActivityManagerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.builderProvider2, this.sysUIComponentImpl.statusBarTouchableRegionManagerProvider, this.sysUIComponentImpl.conversationNotificationManagerProvider, this.sysUIComponentImpl.mediaHierarchyManagerProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.notificationsQSContainerControllerProvider, this.notificationStackScrollLayoutControllerProvider, this.sysUIComponentImpl.keyguardStatusViewComponentFactoryProvider, this.sysUIComponentImpl.keyguardQsUserSwitchComponentFactoryProvider, this.sysUIComponentImpl.keyguardUserSwitcherComponentFactoryProvider, this.sysUIComponentImpl.keyguardStatusBarViewComponentFactoryProvider, this.sysUIComponentImpl.lockscreenShadeTransitionControllerProvider, this.sysUIComponentImpl.notificationIconAreaControllerProvider, this.sysUIComponentImpl.authControllerProvider, this.sysUIComponentImpl.scrimControllerProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.sysUIComponentImpl.mediaDataManagerProvider, this.sysUIComponentImpl.notificationShadeDepthControllerProvider, this.sysUIComponentImpl.ambientStateProvider, this.lockIconViewControllerProvider, this.sysUIComponentImpl.keyguardMediaControllerProvider, this.sysUIComponentImpl.privacyDotViewControllerProvider, this.tapAgainViewControllerProvider, this.sysUIComponentImpl.navigationModeControllerProvider, this.sysUIComponentImpl.fragmentServiceProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.sysUIComponentImpl.quickAccessWalletControllerProvider, this.sysUIComponentImpl.qRCodeScannerControllerProvider, this.sysUIComponentImpl.recordingControllerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.secureSettingsImplProvider, this.largeScreenShadeHeaderControllerProvider, this.sysUIComponentImpl.screenOffAnimationControllerProvider, this.sysUIComponentImpl.lockscreenGestureLoggerProvider, this.sysUIComponentImpl.panelExpansionStateManagerProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.provideSysUIUnfoldComponentProvider, this.sysUIComponentImpl.controlsComponentProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.sysUIComponentImpl.qsFrameTranslateImplProvider, this.sysUIComponentImpl.provideSysUiStateProvider, this.sysUIComponentImpl.keyguardUnlockAnimationControllerProvider, this.provideListContainerProvider, this.sysUIComponentImpl.panelEventsEmitterProvider, this.sysUIComponentImpl.notificationStackSizeCalculatorProvider, this.sysUIComponentImpl.unlockedScreenOffAnimationControllerProvider, this.sysUIComponentImpl.shadeTransitionControllerProvider, this.sysUIComponentImpl.bindSystemClockProvider, this.nTQSStatusBarControllerProvider, this.factoryProvider));
            this.notificationShadeWindowViewControllerProvider = DoubleCheck.provider(NotificationShadeWindowViewController_Factory.create(this.sysUIComponentImpl.lockscreenShadeTransitionControllerProvider, this.sysUIComponentImpl.falsingCollectorImplProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.dockManagerImplProvider, this.sysUIComponentImpl.notificationShadeDepthControllerProvider, this.providesNotificationShadeWindowViewProvider, this.notificationPanelViewControllerProvider, this.sysUIComponentImpl.panelExpansionStateManagerProvider, this.notificationStackScrollLayoutControllerProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.sysUIComponentImpl.statusBarWindowStateControllerProvider, this.lockIconViewControllerProvider, this.sysUIComponentImpl.provideLowLightClockControllerProvider, this.sysUIComponentImpl.centralSurfacesImplProvider, this.sysUIComponentImpl.notificationShadeWindowControllerImplProvider, this.sysUIComponentImpl.keyguardUnlockAnimationControllerProvider, this.sysUIComponentImpl.ambientStateProvider));
            this.statusBarHeadsUpChangeListenerProvider = DoubleCheck.provider(StatusBarHeadsUpChangeListener_Factory.create(this.sysUIComponentImpl.notificationShadeWindowControllerImplProvider, this.sysUIComponentImpl.statusBarWindowControllerProvider, this.notificationPanelViewControllerProvider, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.provideNotificationsControllerProvider, this.sysUIComponentImpl.dozeServiceHostProvider, this.sysUIComponentImpl.dozeScrimControllerProvider));
            this.centralSurfacesCommandQueueCallbacksProvider = DoubleCheck.provider(CentralSurfacesCommandQueueCallbacks_Factory.create(this.sysUIComponentImpl.centralSurfacesImplProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideResourcesProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.sysUIComponentImpl.provideCommandQueueProvider, this.notificationPanelViewControllerProvider, this.sysUIComponentImpl.remoteInputQuickSettingsDisablerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.sysUIComponentImpl.bindDeviceProvisionedControllerProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.sysUIComponentImpl.assistManagerProvider, this.sysUIComponentImpl.dozeServiceHostProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.providesNotificationShadeWindowViewProvider, this.notificationStackScrollLayoutControllerProvider, this.sysUIComponentImpl.statusBarHideIconsForBouncerManagerProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.sysUIComponentImpl.vibratorHelperProvider, this.globalRootComponentImpl.provideOptionalVibratorProvider, this.sysUIComponentImpl.lightBarControllerProvider, this.sysUIComponentImpl.disableFlagsLoggerProvider, this.globalRootComponentImpl.provideDisplayIdProvider));
            this.statusBarInitializerProvider = DoubleCheck.provider(StatusBarInitializer_Factory.create(this.sysUIComponentImpl.statusBarWindowControllerProvider));
            this.statusBarNotificationActivityStarterLoggerProvider = StatusBarNotificationActivityStarterLogger_Factory.create(this.sysUIComponentImpl.provideNotifInteractionLogBufferProvider);
            this.statusBarNotificationPresenterProvider = DoubleCheck.provider(StatusBarNotificationPresenter_Factory.create(this.globalRootComponentImpl.contextProvider, this.notificationPanelViewControllerProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.providesNotificationShadeWindowViewProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.notificationStackScrollLayoutControllerProvider, this.sysUIComponentImpl.dozeScrimControllerProvider, this.sysUIComponentImpl.scrimControllerProvider, this.sysUIComponentImpl.notificationShadeWindowControllerImplProvider, this.sysUIComponentImpl.dynamicPrivacyControllerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.keyguardIndicationControllerProvider, this.sysUIComponentImpl.centralSurfacesImplProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.sysUIComponentImpl.lockscreenShadeTransitionControllerProvider, this.sysUIComponentImpl.provideCommandQueueProvider, this.sysUIComponentImpl.provideNotificationViewHierarchyManagerProvider, this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.provideNotifShadeEventSourceProvider, this.sysUIComponentImpl.provideNotificationEntryManagerProvider, this.sysUIComponentImpl.provideNotificationMediaManagerProvider, this.sysUIComponentImpl.provideNotificationGutsManagerProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.lockscreenGestureLoggerProvider, this.sysUIComponentImpl.initControllerProvider, this.sysUIComponentImpl.notificationInterruptStateProviderImplProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.notifPipelineFlagsProvider, this.sysUIComponentImpl.statusBarRemoteInputCallbackProvider, this.provideListContainerProvider));
            this.notificationLaunchAnimatorControllerProvider = DoubleCheck.provider(NotificationLaunchAnimatorControllerProvider_Factory.create(this.notificationShadeWindowViewControllerProvider, this.provideListContainerProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider));
            this.statusBarNotificationActivityStarterProvider = DoubleCheck.provider(StatusBarNotificationActivityStarter_Factory.create(this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.provideCommandQueueProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.sysUIComponentImpl.provideExecutorProvider, this.sysUIComponentImpl.provideNotificationEntryManagerProvider, this.sysUIComponentImpl.notifPipelineProvider, this.sysUIComponentImpl.provideNotificationVisibilityProvider, this.sysUIComponentImpl.provideHeadsUpManagerPhoneProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.notificationClickNotifierProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.globalRootComponentImpl.provideKeyguardManagerProvider, this.globalRootComponentImpl.provideIDreamManagerProvider, this.sysUIComponentImpl.provideBubblesManagerProvider, this.sysUIComponentImpl.assistManagerProvider, this.sysUIComponentImpl.provideNotificationRemoteInputManagerProvider, this.sysUIComponentImpl.provideGroupMembershipManagerProvider, this.sysUIComponentImpl.notificationLockscreenUserManagerImplProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.notificationInterruptStateProviderImplProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.sysUIComponentImpl.statusBarRemoteInputCallbackProvider, this.sysUIComponentImpl.activityIntentHelperProvider, this.sysUIComponentImpl.notifPipelineFlagsProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarNotificationActivityStarterLoggerProvider, this.sysUIComponentImpl.provideOnUserInteractionCallbackProvider, this.sysUIComponentImpl.centralSurfacesImplProvider, this.statusBarNotificationPresenterProvider, this.notificationPanelViewControllerProvider, this.sysUIComponentImpl.provideActivityLaunchAnimatorProvider, this.notificationLaunchAnimatorControllerProvider));
        }

        public NotificationShadeWindowView getNotificationShadeWindowView() {
            return this.providesNotificationShadeWindowViewProvider.get();
        }

        public NotificationShelfController getNotificationShelfController() {
            return this.providesStatusBarWindowViewProvider.get();
        }

        public NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
            return this.notificationStackScrollLayoutControllerProvider.get();
        }

        public NotificationShadeWindowViewController getNotificationShadeWindowViewController() {
            return this.notificationShadeWindowViewControllerProvider.get();
        }

        public NotificationPanelViewController getNotificationPanelViewController() {
            return this.notificationPanelViewControllerProvider.get();
        }

        public LockIconViewController getLockIconViewController() {
            return this.lockIconViewControllerProvider.get();
        }

        public AuthRippleController getAuthRippleController() {
            return this.authRippleControllerProvider.get();
        }

        public StatusBarHeadsUpChangeListener getStatusBarHeadsUpChangeListener() {
            return this.statusBarHeadsUpChangeListenerProvider.get();
        }

        public CentralSurfacesCommandQueueCallbacks getCentralSurfacesCommandQueueCallbacks() {
            return this.centralSurfacesCommandQueueCallbacksProvider.get();
        }

        public LargeScreenShadeHeaderController getLargeScreenShadeHeaderController() {
            return this.largeScreenShadeHeaderControllerProvider.get();
        }

        public CollapsedStatusBarFragment createCollapsedStatusBarFragment() {
            StatusBarFragmentComponentFactory statusBarFragmentComponentFactory = r2;
            StatusBarFragmentComponentFactory statusBarFragmentComponentFactory2 = new StatusBarFragmentComponentFactory(this.globalRootComponentImpl, this.sysUIComponentImpl, this.centralSurfacesComponentImpl);
            return StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory.createCollapsedStatusBarFragment(statusBarFragmentComponentFactory, (OngoingCallController) this.sysUIComponentImpl.provideOngoingCallControllerProvider.get(), (SystemStatusAnimationScheduler) this.sysUIComponentImpl.systemStatusAnimationSchedulerProvider.get(), (StatusBarLocationPublisher) this.sysUIComponentImpl.statusBarLocationPublisherProvider.get(), (NotificationIconAreaController) this.sysUIComponentImpl.notificationIconAreaControllerProvider.get(), (PanelExpansionStateManager) this.sysUIComponentImpl.panelExpansionStateManagerProvider.get(), (FeatureFlags) this.sysUIComponentImpl.featureFlagsReleaseProvider.get(), (StatusBarIconController) this.sysUIComponentImpl.statusBarIconControllerImplProvider.get(), (StatusBarHideIconsForBouncerManager) this.sysUIComponentImpl.statusBarHideIconsForBouncerManagerProvider.get(), (KeyguardStateController) this.sysUIComponentImpl.keyguardStateControllerImplProvider.get(), this.notificationPanelViewControllerProvider.get(), (NetworkController) this.sysUIComponentImpl.networkControllerImplProvider.get(), (StatusBarStateController) this.sysUIComponentImpl.statusBarStateControllerImplProvider.get(), (CommandQueue) this.sysUIComponentImpl.provideCommandQueueProvider.get(), (CarrierConfigTracker) this.sysUIComponentImpl.carrierConfigTrackerProvider.get(), collapsedStatusBarFragmentLogger(), operatorNameViewControllerFactory(), (SecureSettings) this.sysUIComponentImpl.secureSettingsImpl(), (Executor) this.globalRootComponentImpl.provideMainExecutorProvider.get());
        }

        public StatusBarInitializer getStatusBarInitializer() {
            return this.statusBarInitializerProvider.get();
        }

        public Set<CentralSurfacesComponent.Startable> getStartables() {
            return Collections.emptySet();
        }

        public NotificationActivityStarter getNotificationActivityStarter() {
            return (NotificationActivityStarter) this.statusBarNotificationActivityStarterProvider.get();
        }

        public NotificationPresenter getNotificationPresenter() {
            return (NotificationPresenter) this.statusBarNotificationPresenterProvider.get();
        }

        public NotificationRowBinderImpl.BindRowCallback getBindRowCallback() {
            return (NotificationRowBinderImpl.BindRowCallback) this.statusBarNotificationPresenterProvider.get();
        }

        public NotificationListContainer getNotificationListContainer() {
            return this.provideListContainerProvider.get();
        }
    }

    private static final class KeyguardStatusViewComponentImpl implements KeyguardStatusViewComponent {
        private Provider<KeyguardClockSwitch> getKeyguardClockSwitchProvider;
        private Provider<KeyguardSliceView> getKeyguardSliceViewProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<KeyguardSliceViewController> keyguardSliceViewControllerProvider;
        private final KeyguardStatusViewComponentImpl keyguardStatusViewComponentImpl;
        private final KeyguardStatusView presentation;
        private Provider<KeyguardStatusView> presentationProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardStatusViewComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, KeyguardStatusView keyguardStatusView) {
            this.keyguardStatusViewComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
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
            this.keyguardSliceViewControllerProvider = DoubleCheck.provider(KeyguardSliceViewController_Factory.create(create3, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
        }

        public KeyguardClockSwitchController getKeyguardClockSwitchController() {
            return new KeyguardClockSwitchController(keyguardClockSwitch(), (StatusBarStateController) this.sysUIComponentImpl.statusBarStateControllerImplProvider.get(), (SysuiColorExtractor) this.sysUIComponentImpl.sysuiColorExtractorProvider.get(), (ClockManager) this.sysUIComponentImpl.clockManagerProvider.get(), this.keyguardSliceViewControllerProvider.get(), (NotificationIconAreaController) this.sysUIComponentImpl.notificationIconAreaControllerProvider.get(), (BroadcastDispatcher) this.sysUIComponentImpl.broadcastDispatcherProvider.get(), (BatteryController) this.sysUIComponentImpl.provideBatteryControllerProvider.get(), (KeyguardUpdateMonitor) this.sysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (LockscreenSmartspaceController) this.sysUIComponentImpl.lockscreenSmartspaceControllerProvider.get(), (KeyguardUnlockAnimationController) this.sysUIComponentImpl.keyguardUnlockAnimationControllerProvider.get(), (SecureSettings) this.sysUIComponentImpl.secureSettingsImpl(), (Executor) this.globalRootComponentImpl.provideMainExecutorProvider.get(), this.globalRootComponentImpl.mainResources(), (DumpManager) this.globalRootComponentImpl.dumpManagerProvider.get());
        }

        public KeyguardStatusViewController getKeyguardStatusViewController() {
            return new KeyguardStatusViewController(this.presentation, this.keyguardSliceViewControllerProvider.get(), getKeyguardClockSwitchController(), (KeyguardStateController) this.sysUIComponentImpl.keyguardStateControllerImplProvider.get(), (KeyguardUpdateMonitor) this.sysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (ConfigurationController) this.sysUIComponentImpl.configurationControllerImplProvider.get(), (DozeParameters) this.sysUIComponentImpl.dozeParametersProvider.get(), (ScreenOffAnimationController) this.sysUIComponentImpl.screenOffAnimationControllerProvider.get());
        }
    }

    private static final class KeyguardBouncerComponentImpl implements KeyguardBouncerComponent {
        private Provider<ViewGroup> bouncerContainerProvider;
        private Provider<AdminSecondaryLockScreenController.Factory> factoryProvider;
        private Provider<EmergencyButtonController.Factory> factoryProvider2;
        private Provider<KeyguardInputViewController.Factory> factoryProvider3;
        private Provider factoryProvider4;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final KeyguardBouncerComponentImpl keyguardBouncerComponentImpl;
        private Provider<KeyguardHostViewController> keyguardHostViewControllerProvider;
        private Provider<KeyguardSecurityViewFlipperController> keyguardSecurityViewFlipperControllerProvider;
        private Provider liftToActivateListenerProvider;
        private Provider<KeyguardHostView> providesKeyguardHostViewProvider;
        private Provider<KeyguardSecurityContainer> providesKeyguardSecurityContainerProvider;
        private Provider<KeyguardSecurityViewFlipper> providesKeyguardSecurityViewFlipperProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardBouncerComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, ViewGroup viewGroup) {
            this.keyguardBouncerComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(viewGroup);
        }

        private void initialize(ViewGroup viewGroup) {
            Factory create = InstanceFactory.create(viewGroup);
            this.bouncerContainerProvider = create;
            Provider<KeyguardHostView> provider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardHostViewFactory.create(create, this.globalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesKeyguardHostViewProvider = provider;
            this.providesKeyguardSecurityContainerProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityContainerFactory.create(provider));
            this.factoryProvider = DoubleCheck.provider(AdminSecondaryLockScreenController_Factory_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesKeyguardSecurityContainerProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.providesKeyguardSecurityViewFlipperProvider = DoubleCheck.provider(KeyguardBouncerModule_ProvidesKeyguardSecurityViewFlipperFactory.create(this.providesKeyguardSecurityContainerProvider));
            this.liftToActivateListenerProvider = LiftToActivateListener_Factory.create(this.globalRootComponentImpl.provideAccessibilityManagerProvider);
            this.factoryProvider2 = EmergencyButtonController_Factory_Factory.create(this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideActivityTaskManagerProvider, this.sysUIComponentImpl.shadeControllerImplProvider, this.globalRootComponentImpl.provideTelecomManagerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider);
            this.factoryProvider3 = KeyguardInputViewController_Factory_Factory.create(this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider, this.sysUIComponentImpl.factoryProvider8, this.globalRootComponentImpl.provideInputMethodManagerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.globalRootComponentImpl.provideResourcesProvider, this.liftToActivateListenerProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.sysUIComponentImpl.falsingCollectorImplProvider, this.factoryProvider2, this.sysUIComponentImpl.devicePostureControllerImplProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider);
            this.keyguardSecurityViewFlipperControllerProvider = DoubleCheck.provider(KeyguardSecurityViewFlipperController_Factory.create(this.providesKeyguardSecurityViewFlipperProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.factoryProvider3, this.factoryProvider2));
            this.factoryProvider4 = KeyguardSecurityContainerController_Factory_Factory.create(this.providesKeyguardSecurityContainerProvider, this.factoryProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.keyguardSecurityModelProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.keyguardSecurityViewFlipperControllerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.falsingCollectorImplProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.userSwitcherControllerProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.globalSettingsImplProvider, this.sysUIComponentImpl.sessionTrackerProvider);
            this.keyguardHostViewControllerProvider = DoubleCheck.provider(KeyguardHostViewController_Factory.create(this.providesKeyguardHostViewProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideAudioManagerProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.sysUIComponentImpl.providesViewMediatorCallbackProvider, this.factoryProvider4));
        }

        public KeyguardHostViewController getKeyguardHostViewController() {
            return this.keyguardHostViewControllerProvider.get();
        }
    }

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
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private Provider<Optional<Sensor>[]> providesBrightnessSensorsProvider;
        private Provider<DozeMachine.Part[]> providesDozeMachinePartsProvider;
        private Provider<WakeLock> providesDozeWakeLockProvider;
        private Provider<DozeMachine.Service> providesWrappedServiceProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private DozeComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, DozeMachine.Service service) {
            this.dozeComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(service);
        }

        private void initialize(DozeMachine.Service service) {
            Factory create = InstanceFactory.create(service);
            this.dozeMachineServiceProvider = create;
            this.providesWrappedServiceProvider = DoubleCheck.provider(DozeModule_ProvidesWrappedServiceFactory.create(create, this.sysUIComponentImpl.dozeServiceHostProvider, this.sysUIComponentImpl.dozeParametersProvider));
            this.providesDozeWakeLockProvider = DoubleCheck.provider(DozeModule_ProvidesDozeWakeLockFactory.create(this.sysUIComponentImpl.builderProvider4, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.dozePauserProvider = DoubleCheck.provider(DozePauser_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideAlarmManagerProvider, this.sysUIComponentImpl.alwaysOnDisplayPolicyProvider));
            this.dozeFalsingManagerAdapterProvider = DoubleCheck.provider(DozeFalsingManagerAdapter_Factory.create(this.sysUIComponentImpl.falsingCollectorImplProvider));
            this.dozeTriggersProvider = DoubleCheck.provider(DozeTriggers_Factory.create(this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.dozeServiceHostProvider, this.globalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.sysUIComponentImpl.dozeParametersProvider, this.sysUIComponentImpl.asyncSensorManagerProvider, this.providesDozeWakeLockProvider, this.sysUIComponentImpl.dockManagerImplProvider, this.sysUIComponentImpl.provideProximitySensorProvider, this.sysUIComponentImpl.provideProximityCheckProvider, this.sysUIComponentImpl.dozeLogProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.sysUIComponentImpl.secureSettingsImplProvider, this.sysUIComponentImpl.authControllerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.devicePostureControllerImplProvider));
            this.dozeUiProvider = DoubleCheck.provider(DozeUi_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideAlarmManagerProvider, this.providesDozeWakeLockProvider, this.sysUIComponentImpl.dozeServiceHostProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.sysUIComponentImpl.dozeParametersProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.dozeLogProvider));
            this.providesBrightnessSensorsProvider = DozeModule_ProvidesBrightnessSensorsFactory.create(this.sysUIComponentImpl.asyncSensorManagerProvider, this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.dozeParametersProvider);
            this.dozeScreenBrightnessProvider = DoubleCheck.provider(DozeScreenBrightness_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesWrappedServiceProvider, this.sysUIComponentImpl.asyncSensorManagerProvider, this.providesBrightnessSensorsProvider, this.sysUIComponentImpl.dozeServiceHostProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.sysUIComponentImpl.alwaysOnDisplayPolicyProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.sysUIComponentImpl.dozeParametersProvider, this.sysUIComponentImpl.devicePostureControllerImplProvider, this.sysUIComponentImpl.dozeLogProvider));
            this.dozeScreenStateProvider = DoubleCheck.provider(DozeScreenState_Factory.create(this.providesWrappedServiceProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.sysUIComponentImpl.dozeServiceHostProvider, this.sysUIComponentImpl.dozeParametersProvider, this.providesDozeWakeLockProvider, this.sysUIComponentImpl.authControllerProvider, this.sysUIComponentImpl.udfpsControllerProvider, this.sysUIComponentImpl.dozeLogProvider, this.dozeScreenBrightnessProvider));
            this.dozeWallpaperStateProvider = DoubleCheck.provider(DozeWallpaperState_Factory.create(FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.sysUIComponentImpl.biometricUnlockControllerProvider, this.sysUIComponentImpl.dozeParametersProvider));
            this.dozeDockHandlerProvider = DoubleCheck.provider(DozeDockHandler_Factory.create(this.globalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.sysUIComponentImpl.dockManagerImplProvider));
            this.dozeAuthRemoverProvider = DoubleCheck.provider(DozeAuthRemover_Factory.create(this.sysUIComponentImpl.keyguardUpdateMonitorProvider));
            Provider<DozeSuppressor> provider = DoubleCheck.provider(DozeSuppressor_Factory.create(this.sysUIComponentImpl.dozeServiceHostProvider, this.globalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.sysUIComponentImpl.dozeLogProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideUiModeManagerProvider, this.sysUIComponentImpl.biometricUnlockControllerProvider));
            this.dozeSuppressorProvider = provider;
            this.providesDozeMachinePartsProvider = DozeModule_ProvidesDozeMachinePartsFactory.create(this.dozePauserProvider, this.dozeFalsingManagerAdapterProvider, this.dozeTriggersProvider, this.dozeUiProvider, this.dozeScreenStateProvider, this.dozeScreenBrightnessProvider, this.dozeWallpaperStateProvider, this.dozeDockHandlerProvider, this.dozeAuthRemoverProvider, provider);
            this.dozeMachineProvider = DoubleCheck.provider(DozeMachine_Factory.create(this.providesWrappedServiceProvider, this.globalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.providesDozeWakeLockProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.sysUIComponentImpl.provideBatteryControllerProvider, this.sysUIComponentImpl.dozeLogProvider, this.sysUIComponentImpl.dockManagerImplProvider, this.sysUIComponentImpl.dozeServiceHostProvider, this.providesDozeMachinePartsProvider));
        }

        public DozeMachine getDozeMachine() {
            return this.dozeMachineProvider.get();
        }
    }

    private static final class ComplicationViewModelComponentImpl implements ComplicationViewModelComponent {
        private final Complication complication;
        private final ComplicationViewModelComponentImpl complicationViewModelComponentImpl;
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final GlobalRootComponentImpl globalRootComponentImpl;

        /* renamed from: id */
        private final ComplicationId f301id;
        private final SysUIComponentImpl sysUIComponentImpl;

        private ComplicationViewModelComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2, Complication complication2, ComplicationId complicationId) {
            this.complicationViewModelComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.dreamOverlayComponentImpl = dreamOverlayComponentImpl2;
            this.complication = complication2;
            this.f301id = complicationId;
        }

        private ComplicationViewModel complicationViewModel() {
            return new ComplicationViewModel(this.complication, this.f301id, this.dreamOverlayComponentImpl.host);
        }

        public ComplicationViewModelProvider getViewModelProvider() {
            return new ComplicationViewModelProvider(this.dreamOverlayComponentImpl.store, complicationViewModel());
        }
    }

    private static final class InputSessionComponentImpl implements InputSessionComponent {
        private final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private final GestureDetector.OnGestureListener gestureListener;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final InputChannelCompat.InputEventListener inputEventListener;
        private final InputSessionComponentImpl inputSessionComponentImpl;
        private final String name;
        private final Boolean pilferOnGestureConsume;
        private final SysUIComponentImpl sysUIComponentImpl;

        private InputSessionComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, DreamOverlayComponentImpl dreamOverlayComponentImpl2, String str, InputChannelCompat.InputEventListener inputEventListener2, GestureDetector.OnGestureListener onGestureListener, Boolean bool) {
            this.inputSessionComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
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

    private static final class DreamOverlayComponentImpl implements DreamOverlayComponent {
        private Provider<FlingAnimationUtils.Builder> builderProvider;
        private Provider<ComplicationCollectionLiveData> complicationCollectionLiveDataProvider;
        private Provider<ComplicationCollectionViewModel> complicationCollectionViewModelProvider;
        private Provider<ComplicationHostViewController> complicationHostViewControllerProvider;
        private Provider<ComplicationLayoutEngine> complicationLayoutEngineProvider;
        private Provider<ComplicationViewModelComponent.Factory> complicationViewModelComponentFactoryProvider;
        private Provider<ComplicationViewModelTransformer> complicationViewModelTransformerProvider;
        /* access modifiers changed from: private */
        public final DreamOverlayComponentImpl dreamOverlayComponentImpl;
        private Provider<DreamOverlayContainerViewController> dreamOverlayContainerViewControllerProvider;
        private Provider<DreamOverlayStatusBarViewController> dreamOverlayStatusBarViewControllerProvider;
        /* access modifiers changed from: private */
        public final GlobalRootComponentImpl globalRootComponentImpl;
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
        public final SysUIComponentImpl sysUIComponentImpl;

        private DreamOverlayComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, ViewModelStore viewModelStore, Complication.Host host2) {
            this.dreamOverlayComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
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
            return BouncerSwipeModule.providesSwipeToBouncerStartRegion(this.globalRootComponentImpl.mainResources());
        }

        private BouncerSwipeTouchHandler bouncerSwipeTouchHandler() {
            return new BouncerSwipeTouchHandler(this.globalRootComponentImpl.displayMetrics(), (StatusBarKeyguardViewManager) this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider.get(), Optional.m1751of((CentralSurfaces) this.sysUIComponentImpl.centralSurfacesImplProvider.get()), (NotificationShadeWindowController) this.sysUIComponentImpl.notificationShadeWindowControllerImplProvider.get(), BouncerSwipeModule_ProvidesValueAnimatorCreatorFactory.providesValueAnimatorCreator(), BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory.providesVelocityTrackerFactory(), namedFlingAnimationUtils(), namedFlingAnimationUtils2(), namedFloat(), (UiEventLogger) this.globalRootComponentImpl.provideUiEventLoggerProvider.get());
        }

        private DreamTouchHandler providesBouncerSwipeTouchHandler() {
            return BouncerSwipeModule_ProvidesBouncerSwipeTouchHandlerFactory.providesBouncerSwipeTouchHandler(bouncerSwipeTouchHandler());
        }

        private Complication.VisibilityController visibilityController() {
            return ComplicationModule_ProvidesVisibilityControllerFactory.providesVisibilityController(this.complicationLayoutEngineProvider.get());
        }

        private HideComplicationTouchHandler hideComplicationTouchHandler() {
            return HideComplicationTouchHandler_Factory.newInstance(visibilityController(), this.providesComplicationsRestoreTimeoutProvider.get().intValue(), this.providesTouchInsetManagerProvider.get(), (Executor) this.globalRootComponentImpl.provideMainExecutorProvider.get(), this.globalRootComponentImpl.mainHandler());
        }

        private DreamTouchHandler providesHideComplicationTouchHandler() {
            return C2106xc6ed12da.providesHideComplicationTouchHandler(hideComplicationTouchHandler());
        }

        private Set<DreamTouchHandler> setOfDreamTouchHandler() {
            return SetBuilder.newSetBuilder(2).add(providesBouncerSwipeTouchHandler()).add(providesHideComplicationTouchHandler()).build();
        }

        private void initialize(ViewModelStore viewModelStore, Complication.Host host2) {
            this.providesDreamOverlayContainerViewProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesComplicationHostViewProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationHostViewFactory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider));
            this.providesComplicationPaddingProvider = DoubleCheck.provider(ComplicationHostViewModule_ProvidesComplicationPaddingFactory.create(this.globalRootComponentImpl.provideResourcesProvider));
            Provider<TouchInsetManager> provider = DoubleCheck.provider(DreamOverlayModule_ProvidesTouchInsetManagerFactory.create(this.globalRootComponentImpl.provideMainExecutorProvider, this.providesDreamOverlayContainerViewProvider));
            this.providesTouchInsetManagerProvider = provider;
            this.providesTouchInsetSessionProvider = DreamOverlayModule_ProvidesTouchInsetSessionFactory.create(provider);
            this.providesComplicationsFadeInDurationProvider = DoubleCheck.provider(C2091xa92fda9a.create(this.globalRootComponentImpl.provideResourcesProvider));
            Provider<Integer> provider2 = DoubleCheck.provider(C2092x3242d31f.create(this.globalRootComponentImpl.provideResourcesProvider));
            this.providesComplicationsFadeOutDurationProvider = provider2;
            this.complicationLayoutEngineProvider = DoubleCheck.provider(ComplicationLayoutEngine_Factory.create(this.providesComplicationHostViewProvider, this.providesComplicationPaddingProvider, this.providesTouchInsetSessionProvider, this.providesComplicationsFadeInDurationProvider, provider2));
            DelegateFactory delegateFactory = new DelegateFactory();
            this.providesLifecycleOwnerProvider = delegateFactory;
            Provider<LifecycleRegistry> provider3 = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleRegistryFactory.create(delegateFactory));
            this.providesLifecycleRegistryProvider = provider3;
            DelegateFactory.setDelegate(this.providesLifecycleOwnerProvider, DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleOwnerFactory.create(provider3)));
            this.storeProvider = InstanceFactory.create(viewModelStore);
            this.complicationCollectionLiveDataProvider = ComplicationCollectionLiveData_Factory.create(this.sysUIComponentImpl.dreamOverlayStateControllerProvider);
            C20381 r13 = new Provider<ComplicationViewModelComponent.Factory>() {
                public ComplicationViewModelComponent.Factory get() {
                    return new ComplicationViewModelComponentFactory(DreamOverlayComponentImpl.this.globalRootComponentImpl, DreamOverlayComponentImpl.this.sysUIComponentImpl, DreamOverlayComponentImpl.this.dreamOverlayComponentImpl);
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
            Provider<DreamOverlayStatusBarView> provider4 = DoubleCheck.provider(DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory.create(this.providesDreamOverlayContainerViewProvider));
            this.providesDreamOverlayStatusBarViewProvider = provider4;
            this.dreamOverlayStatusBarViewControllerProvider = DoubleCheck.provider(DreamOverlayStatusBarViewController_Factory.create(provider4, this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.provideConnectivityManagagerProvider, this.providesTouchInsetSessionProvider, this.globalRootComponentImpl.provideAlarmManagerProvider, this.sysUIComponentImpl.nextAlarmControllerImplProvider, this.sysUIComponentImpl.dateFormatUtilProvider, this.sysUIComponentImpl.provideIndividualSensorPrivacyControllerProvider, this.sysUIComponentImpl.dreamOverlayNotificationCountProvider, this.sysUIComponentImpl.zenModeControllerImplProvider, this.sysUIComponentImpl.statusBarWindowStateControllerProvider));
            this.providesMaxBurnInOffsetProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesMaxBurnInOffsetFactory.create(this.globalRootComponentImpl.provideResourcesProvider));
            this.providesBurnInProtectionUpdateIntervalProvider = DreamOverlayModule_ProvidesBurnInProtectionUpdateIntervalFactory.create(this.globalRootComponentImpl.provideResourcesProvider);
            this.providesMillisUntilFullJitterProvider = DreamOverlayModule_ProvidesMillisUntilFullJitterFactory.create(this.globalRootComponentImpl.provideResourcesProvider);
            this.dreamOverlayContainerViewControllerProvider = DoubleCheck.provider(DreamOverlayContainerViewController_Factory.create(this.providesDreamOverlayContainerViewProvider, this.complicationHostViewControllerProvider, this.providesDreamOverlayContentViewProvider, this.dreamOverlayStatusBarViewControllerProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider, this.sysUIComponentImpl.blurUtilsProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideResourcesProvider, this.providesMaxBurnInOffsetProvider, this.providesBurnInProtectionUpdateIntervalProvider, this.providesMillisUntilFullJitterProvider));
            this.providesLifecycleProvider = DoubleCheck.provider(DreamOverlayModule_ProvidesLifecycleFactory.create(this.providesLifecycleOwnerProvider));
            this.builderProvider = FlingAnimationUtils_Builder_Factory.create(this.globalRootComponentImpl.provideDisplayMetricsProvider);
            this.providesComplicationsRestoreTimeoutProvider = DoubleCheck.provider(C2093x4a872b5c.create(this.globalRootComponentImpl.provideResourcesProvider));
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
            return new DreamOverlayTouchMonitor((Executor) this.globalRootComponentImpl.provideMainExecutorProvider.get(), this.providesLifecycleProvider.get(), new InputSessionComponentFactory(this.globalRootComponentImpl, this.sysUIComponentImpl, this.dreamOverlayComponentImpl), setOfDreamTouchHandler());
        }
    }

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
        private final GlobalRootComponentImpl globalRootComponentImpl;
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
        private final SysUIComponentImpl sysUIComponentImpl;
        private Provider<TileAdapter> tileAdapterProvider;
        private Provider<TileQueryHelper> tileQueryHelperProvider;

        private QSFragmentComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, QSFragment qSFragment) {
            this.qSFragmentComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(qSFragment);
        }

        private void initialize(QSFragment qSFragment) {
            Factory create = InstanceFactory.create(qSFragment);
            this.qsFragmentProvider = create;
            QSFragmentModule_ProvideRootViewFactory create2 = QSFragmentModule_ProvideRootViewFactory.create(create);
            this.provideRootViewProvider = create2;
            this.provideQSPanelProvider = QSFragmentModule_ProvideQSPanelFactory.create(create2);
            this.providesQSCutomizerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSCutomizerFactory.create(this.provideRootViewProvider));
            this.tileQueryHelperProvider = DoubleCheck.provider(TileQueryHelper_Factory.create(this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.provideUserTrackerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider));
            QSFragmentModule_ProvideThemedContextFactory create3 = QSFragmentModule_ProvideThemedContextFactory.create(this.provideRootViewProvider);
            this.provideThemedContextProvider = create3;
            this.tileAdapterProvider = DoubleCheck.provider(TileAdapter_Factory.create(create3, this.sysUIComponentImpl.qSTileHostProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.qSCustomizerControllerProvider = DoubleCheck.provider(QSCustomizerController_Factory.create(this.providesQSCutomizerProvider, this.tileQueryHelperProvider, this.sysUIComponentImpl.qSTileHostProvider, this.tileAdapterProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.lightBarControllerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.providesQSUsingMediaPlayerProvider = QSFragmentModule_ProvidesQSUsingMediaPlayerFactory.create(this.globalRootComponentImpl.contextProvider);
            this.factoryProvider = DoubleCheck.provider(QSTileRevealController_Factory_Factory.create(this.globalRootComponentImpl.contextProvider, this.qSCustomizerControllerProvider));
            this.factoryProvider2 = BrightnessController_Factory_Factory.create(this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.sysUIComponentImpl.provideBgHandlerProvider);
            this.qSPanelControllerProvider = DoubleCheck.provider(QSPanelController_Factory.create(this.provideQSPanelProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, this.sysUIComponentImpl.providesQSMediaHostProvider, this.factoryProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.qSLoggerProvider, this.factoryProvider2, this.sysUIComponentImpl.factoryProvider6, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.statusBarKeyguardViewManagerProvider));
            QSFragmentModule_ProvidesQuickStatusBarHeaderFactory create4 = QSFragmentModule_ProvidesQuickStatusBarHeaderFactory.create(this.provideRootViewProvider);
            this.providesQuickStatusBarHeaderProvider = create4;
            this.providesQuickQSPanelProvider = QSFragmentModule_ProvidesQuickQSPanelFactory.create(create4);
            this.providesQSUsingCollapsedLandscapeMediaProvider = QSFragmentModule_ProvidesQSUsingCollapsedLandscapeMediaFactory.create(this.globalRootComponentImpl.contextProvider);
            Provider<QuickQSPanelController> provider = DoubleCheck.provider(QuickQSPanelController_Factory.create(this.providesQuickQSPanelProvider, this.sysUIComponentImpl.qSTileHostProvider, this.qSCustomizerControllerProvider, this.providesQSUsingMediaPlayerProvider, this.sysUIComponentImpl.providesQuickQSMediaHostProvider, this.providesQSUsingCollapsedLandscapeMediaProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.qSLoggerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.quickQSPanelControllerProvider = provider;
            this.qSAnimatorProvider = DoubleCheck.provider(QSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, provider, this.sysUIComponentImpl.qSTileHostProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.globalRootComponentImpl.qSExpansionPathInterpolatorProvider));
            this.providesQSContainerImplProvider = QSFragmentModule_ProvidesQSContainerImplFactory.create(this.provideRootViewProvider);
            this.providesPrivacyChipProvider = DoubleCheck.provider(QSFragmentModule_ProvidesPrivacyChipFactory.create(this.providesQuickStatusBarHeaderProvider));
            this.providesStatusIconContainerProvider = DoubleCheck.provider(QSFragmentModule_ProvidesStatusIconContainerFactory.create(this.providesQuickStatusBarHeaderProvider));
            this.headerPrivacyIconsControllerProvider = HeaderPrivacyIconsController_Factory.create(this.sysUIComponentImpl.privacyItemControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.providesPrivacyChipProvider, this.sysUIComponentImpl.privacyDialogControllerProvider, this.sysUIComponentImpl.privacyLoggerProvider, this.providesStatusIconContainerProvider, this.globalRootComponentImpl.providePermissionManagerProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.appOpsControllerImplProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideSafetyCenterManagerProvider);
            this.builderProvider = CarrierTextManager_Builder_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideWifiManagerProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.sysUIComponentImpl.telephonyListenerManagerProvider, this.sysUIComponentImpl.wakefulnessLifecycleProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.carrierNameCustomizationProvider);
            this.builderProvider2 = QSCarrierGroupController_Builder_Factory.create(this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.sysUIComponentImpl.networkControllerImplProvider, this.builderProvider, this.globalRootComponentImpl.contextProvider, this.sysUIComponentImpl.carrierConfigTrackerProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.subscriptionManagerSlotIndexResolverProvider);
            this.factoryProvider3 = VariableDateViewController_Factory_Factory.create(this.sysUIComponentImpl.bindSystemClockProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.sysUIComponentImpl.provideTimeTickHandlerProvider);
            QSFragmentModule_ProvidesBatteryMeterViewFactory create5 = QSFragmentModule_ProvidesBatteryMeterViewFactory.create(this.providesQuickStatusBarHeaderProvider);
            this.providesBatteryMeterViewProvider = create5;
            this.batteryMeterViewControllerProvider = BatteryMeterViewController_Factory.create(create5, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.sysUIComponentImpl.provideBatteryControllerProvider);
            this.quickStatusBarHeaderControllerProvider = DoubleCheck.provider(QuickStatusBarHeaderController_Factory.create(this.providesQuickStatusBarHeaderProvider, this.headerPrivacyIconsControllerProvider, this.sysUIComponentImpl.statusBarIconControllerImplProvider, this.sysUIComponentImpl.provideDemoModeControllerProvider, this.quickQSPanelControllerProvider, this.builderProvider2, this.sysUIComponentImpl.sysuiColorExtractorProvider, this.globalRootComponentImpl.qSExpansionPathInterpolatorProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.factoryProvider3, this.batteryMeterViewControllerProvider, this.sysUIComponentImpl.statusBarContentInsetsProvider));
            this.providesNTQSStatusBarProvider = QSFragmentModule_ProvidesNTQSStatusBarFactory.create(this.provideRootViewProvider);
            QSFragmentModule_GetBatteryMeterViewFactory create6 = QSFragmentModule_GetBatteryMeterViewFactory.create(this.providesQuickStatusBarHeaderProvider);
            this.getBatteryMeterViewProvider = create6;
            this.getBatteryMeterViewControllerProvider = QSFragmentModule_GetBatteryMeterViewControllerFactory.create(create6, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.sysUIComponentImpl.broadcastDispatcherProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.sysUIComponentImpl.provideBatteryControllerProvider);
            NTQSStatusBarController_Factory create7 = NTQSStatusBarController_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesNTQSStatusBarProvider, this.sysUIComponentImpl.privacyDialogControllerProvider, this.sysUIComponentImpl.privacyItemControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.privacyLoggerProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.getBatteryMeterViewControllerProvider);
            this.nTQSStatusBarControllerProvider = create7;
            this.qSContainerImplControllerProvider = DoubleCheck.provider(QSContainerImplController_Factory.create(this.providesQSContainerImplProvider, this.qSPanelControllerProvider, this.quickStatusBarHeaderControllerProvider, create7, this.sysUIComponentImpl.configurationControllerImplProvider));
            QSFragmentModule_ProvidesQSFooterViewFactory create8 = QSFragmentModule_ProvidesQSFooterViewFactory.create(this.provideRootViewProvider);
            this.providesQSFooterViewProvider = create8;
            Provider<QSFooterViewController> provider2 = DoubleCheck.provider(QSFooterViewController_Factory.create(create8, this.sysUIComponentImpl.provideUserTrackerProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.qSPanelControllerProvider));
            this.qSFooterViewControllerProvider = provider2;
            this.providesQSFooterProvider = DoubleCheck.provider(QSFragmentModule_ProvidesQSFooterFactory.create(provider2));
            this.provideThemedLayoutInflaterProvider = QSFragmentModule_ProvideThemedLayoutInflaterFactory.create(this.provideThemedContextProvider);
            QSFragmentModule_ProvidesQSFooterActionsViewFactory create9 = QSFragmentModule_ProvidesQSFooterActionsViewFactory.create(this.provideRootViewProvider);
            this.providesQSFooterActionsViewProvider = create9;
            Provider<View> provider3 = DoubleCheck.provider(QSFragmentModule_ProvidesQSSecurityFooterViewFactory.create(this.provideThemedLayoutInflaterProvider, create9));
            this.providesQSSecurityFooterViewProvider = provider3;
            this.qSSecurityFooterProvider = DoubleCheck.provider(QSSecurityFooter_Factory.create(provider3, this.sysUIComponentImpl.provideUserTrackerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.securityControllerImplProvider, this.sysUIComponentImpl.provideDialogLaunchAnimatorProvider, this.sysUIComponentImpl.provideBgLooperProvider, this.sysUIComponentImpl.broadcastDispatcherProvider));
            Provider<NTQSAnimator> provider4 = DoubleCheck.provider(NTQSAnimator_Factory.create(this.qsFragmentProvider, this.providesQuickQSPanelProvider, this.providesQuickStatusBarHeaderProvider, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider, this.sysUIComponentImpl.qSTileHostProvider, this.qSSecurityFooterProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.tunerServiceImplProvider, this.globalRootComponentImpl.qSExpansionPathInterpolatorProvider, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.nTQSAnimatorProvider = provider4;
            this.qSSquishinessControllerProvider = DoubleCheck.provider(QSSquishinessController_Factory.create(provider4, this.qSPanelControllerProvider, this.quickQSPanelControllerProvider));
            this.factoryProvider4 = DoubleCheck.provider(MultiUserSwitchController_Factory_Factory.create(this.globalRootComponentImpl.provideUserManagerProvider, this.sysUIComponentImpl.userSwitcherControllerProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.userSwitchDialogControllerProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.provideActivityStarterProvider));
            Provider<View> provider5 = DoubleCheck.provider(QSFragmentModule_ProvidesQSFgsManagerFooterViewFactory.create(this.provideThemedLayoutInflaterProvider, this.providesQSFooterActionsViewProvider));
            this.providesQSFgsManagerFooterViewProvider = provider5;
            this.qSFgsManagerFooterProvider = DoubleCheck.provider(QSFgsManagerFooter_Factory.create(provider5, this.globalRootComponentImpl.provideMainExecutorProvider, this.sysUIComponentImpl.provideBackgroundExecutorProvider, this.sysUIComponentImpl.fgsManagerControllerProvider));
            this.footerActionsControllerProvider = DoubleCheck.provider(FooterActionsController_Factory.create(this.providesQSFooterActionsViewProvider, this.factoryProvider4, this.sysUIComponentImpl.provideActivityStarterProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.sysUIComponentImpl.provideUserTrackerProvider, this.sysUIComponentImpl.userInfoControllerImplProvider, this.sysUIComponentImpl.bindDeviceProvisionedControllerProvider, this.qSSecurityFooterProvider, this.qSFgsManagerFooterProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.sysUIComponentImpl.globalActionsDialogLiteProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.sysUIComponentImpl.isPMLiteEnabledProvider, this.sysUIComponentImpl.globalSettingsImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
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

    private static final class NotificationShelfComponentImpl implements NotificationShelfComponent {
        private Provider<ActivatableNotificationViewController> activatableNotificationViewControllerProvider;
        private Provider<ExpandableOutlineViewController> expandableOutlineViewControllerProvider;
        private Provider<ExpandableViewController> expandableViewControllerProvider;
        private Provider<NotificationTapHelper.Factory> factoryProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final NotificationShelfComponentImpl notificationShelfComponentImpl;
        private Provider<NotificationShelfController> notificationShelfControllerProvider;
        private Provider<NotificationShelf> notificationShelfProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private NotificationShelfComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, NotificationShelf notificationShelf) {
            this.notificationShelfComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(notificationShelf);
        }

        private void initialize(NotificationShelf notificationShelf) {
            this.notificationShelfProvider = InstanceFactory.create(notificationShelf);
            this.factoryProvider = NotificationTapHelper_Factory_Factory.create(this.sysUIComponentImpl.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider);
            ExpandableViewController_Factory create = ExpandableViewController_Factory.create(this.notificationShelfProvider);
            this.expandableViewControllerProvider = create;
            ExpandableOutlineViewController_Factory create2 = ExpandableOutlineViewController_Factory.create(this.notificationShelfProvider, create);
            this.expandableOutlineViewControllerProvider = create2;
            ActivatableNotificationViewController_Factory create3 = ActivatableNotificationViewController_Factory.create(this.notificationShelfProvider, this.factoryProvider, create2, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.falsingCollectorImplProvider);
            this.activatableNotificationViewControllerProvider = create3;
            this.notificationShelfControllerProvider = DoubleCheck.provider(NotificationShelfController_Factory.create(this.notificationShelfProvider, create3, this.sysUIComponentImpl.keyguardBypassControllerProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider));
        }

        public NotificationShelfController getNotificationShelfController() {
            return this.notificationShelfControllerProvider.get();
        }
    }

    private static final class KeyguardQsUserSwitchComponentImpl implements KeyguardQsUserSwitchComponent {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final KeyguardQsUserSwitchComponentImpl keyguardQsUserSwitchComponentImpl;
        private Provider<KeyguardQsUserSwitchController> keyguardQsUserSwitchControllerProvider;
        private final SysUIComponentImpl sysUIComponentImpl;
        private Provider<FrameLayout> userAvatarContainerProvider;

        private KeyguardQsUserSwitchComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, FrameLayout frameLayout) {
            this.keyguardQsUserSwitchComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(frameLayout);
        }

        private void initialize(FrameLayout frameLayout) {
            Factory create = InstanceFactory.create(frameLayout);
            this.userAvatarContainerProvider = create;
            this.keyguardQsUserSwitchControllerProvider = DoubleCheck.provider(KeyguardQsUserSwitchController_Factory.create(create, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideResourcesProvider, this.sysUIComponentImpl.userSwitcherControllerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.falsingManagerProxyProvider, this.sysUIComponentImpl.configurationControllerImplProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.dozeParametersProvider, this.sysUIComponentImpl.screenOffAnimationControllerProvider, this.sysUIComponentImpl.userSwitchDialogControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
        }

        public KeyguardQsUserSwitchController getKeyguardQsUserSwitchController() {
            return this.keyguardQsUserSwitchControllerProvider.get();
        }
    }

    private static final class KeyguardUserSwitcherComponentImpl implements KeyguardUserSwitcherComponent {
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final KeyguardUserSwitcherComponentImpl keyguardUserSwitcherComponentImpl;
        private Provider<KeyguardUserSwitcherController> keyguardUserSwitcherControllerProvider;
        private Provider<KeyguardUserSwitcherView> keyguardUserSwitcherViewProvider;
        private final SysUIComponentImpl sysUIComponentImpl;

        private KeyguardUserSwitcherComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, KeyguardUserSwitcherView keyguardUserSwitcherView) {
            this.keyguardUserSwitcherComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            initialize(keyguardUserSwitcherView);
        }

        private void initialize(KeyguardUserSwitcherView keyguardUserSwitcherView) {
            Factory create = InstanceFactory.create(keyguardUserSwitcherView);
            this.keyguardUserSwitcherViewProvider = create;
            this.keyguardUserSwitcherControllerProvider = DoubleCheck.provider(KeyguardUserSwitcherController_Factory.create(create, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.sysUIComponentImpl.userSwitcherControllerProvider, this.sysUIComponentImpl.keyguardStateControllerImplProvider, this.sysUIComponentImpl.statusBarStateControllerImplProvider, this.sysUIComponentImpl.keyguardUpdateMonitorProvider, this.sysUIComponentImpl.dozeParametersProvider, this.sysUIComponentImpl.screenOffAnimationControllerProvider));
        }

        public KeyguardUserSwitcherController getKeyguardUserSwitcherController() {
            return this.keyguardUserSwitcherControllerProvider.get();
        }
    }

    private static final class KeyguardStatusBarViewComponentImpl implements KeyguardStatusBarViewComponent {
        private Provider<StatusBarUserSwitcherController> bindStatusBarUserSwitcherControllerProvider;
        private Provider<BatteryMeterView> getBatteryMeterViewProvider;
        private Provider<CarrierText> getCarrierTextProvider;
        private Provider<StatusBarUserSwitcherContainer> getUserSwitcherContainerProvider;
        private final GlobalRootComponentImpl globalRootComponentImpl;
        private final KeyguardStatusBarViewComponentImpl keyguardStatusBarViewComponentImpl;
        private final NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider;
        private Provider<StatusBarUserSwitcherControllerImpl> statusBarUserSwitcherControllerImplProvider;
        private final SysUIComponentImpl sysUIComponentImpl;
        private final KeyguardStatusBarView view;
        private Provider<KeyguardStatusBarView> viewProvider;

        private KeyguardStatusBarViewComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, SysUIComponentImpl sysUIComponentImpl2, KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider2) {
            this.keyguardStatusBarViewComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
            this.sysUIComponentImpl = sysUIComponentImpl2;
            this.view = keyguardStatusBarView;
            this.notificationPanelViewStateProvider = notificationPanelViewStateProvider2;
            initialize(keyguardStatusBarView, notificationPanelViewStateProvider2);
        }

        private CarrierTextManager.Builder carrierTextManagerBuilder() {
            return new CarrierTextManager.Builder(this.globalRootComponentImpl.context, this.globalRootComponentImpl.mainResources(), (WifiManager) this.globalRootComponentImpl.provideWifiManagerProvider.get(), (TelephonyManager) this.globalRootComponentImpl.provideTelephonyManagerProvider.get(), (TelephonyListenerManager) this.sysUIComponentImpl.telephonyListenerManagerProvider.get(), (WakefulnessLifecycle) this.sysUIComponentImpl.wakefulnessLifecycleProvider.get(), (Executor) this.globalRootComponentImpl.provideMainExecutorProvider.get(), (Executor) this.sysUIComponentImpl.provideBackgroundExecutorProvider.get(), (KeyguardUpdateMonitor) this.sysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (CarrierNameCustomization) this.sysUIComponentImpl.carrierNameCustomizationProvider.get());
        }

        private CarrierTextController carrierTextController() {
            return new CarrierTextController(this.getCarrierTextProvider.get(), carrierTextManagerBuilder(), (KeyguardUpdateMonitor) this.sysUIComponentImpl.keyguardUpdateMonitorProvider.get());
        }

        private BatteryMeterViewController batteryMeterViewController() {
            return new BatteryMeterViewController(this.getBatteryMeterViewProvider.get(), (ConfigurationController) this.sysUIComponentImpl.configurationControllerImplProvider.get(), (TunerService) this.sysUIComponentImpl.tunerServiceImplProvider.get(), (BroadcastDispatcher) this.sysUIComponentImpl.broadcastDispatcherProvider.get(), this.globalRootComponentImpl.mainHandler(), (ContentResolver) this.globalRootComponentImpl.provideContentResolverProvider.get(), (BatteryController) this.sysUIComponentImpl.provideBatteryControllerProvider.get());
        }

        private void initialize(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider2) {
            Factory create = InstanceFactory.create(keyguardStatusBarView);
            this.viewProvider = create;
            this.getCarrierTextProvider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetCarrierTextFactory.create(create));
            this.getBatteryMeterViewProvider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetBatteryMeterViewFactory.create(this.viewProvider));
            Provider<StatusBarUserSwitcherContainer> provider = DoubleCheck.provider(KeyguardStatusBarViewModule_GetUserSwitcherContainerFactory.create(this.viewProvider));
            this.getUserSwitcherContainerProvider = provider;
            StatusBarUserSwitcherControllerImpl_Factory create2 = StatusBarUserSwitcherControllerImpl_Factory.create(provider, this.sysUIComponentImpl.statusBarUserInfoTrackerProvider, this.sysUIComponentImpl.statusBarUserSwitcherFeatureControllerProvider, this.sysUIComponentImpl.userSwitchDialogControllerProvider, this.sysUIComponentImpl.featureFlagsReleaseProvider, this.sysUIComponentImpl.provideActivityStarterProvider, this.sysUIComponentImpl.falsingManagerProxyProvider);
            this.statusBarUserSwitcherControllerImplProvider = create2;
            this.bindStatusBarUserSwitcherControllerProvider = DoubleCheck.provider(create2);
        }

        public KeyguardStatusBarViewController getKeyguardStatusBarViewController() {
            return new KeyguardStatusBarViewController(this.view, carrierTextController(), (ConfigurationController) this.sysUIComponentImpl.configurationControllerImplProvider.get(), (SystemStatusAnimationScheduler) this.sysUIComponentImpl.systemStatusAnimationSchedulerProvider.get(), (BatteryController) this.sysUIComponentImpl.provideBatteryControllerProvider.get(), (UserInfoController) this.sysUIComponentImpl.userInfoControllerImplProvider.get(), (StatusBarIconController) this.sysUIComponentImpl.statusBarIconControllerImplProvider.get(), (StatusBarIconController.TintedIconManager.Factory) this.sysUIComponentImpl.factoryProvider11.get(), batteryMeterViewController(), this.notificationPanelViewStateProvider, (KeyguardStateController) this.sysUIComponentImpl.keyguardStateControllerImplProvider.get(), (KeyguardBypassController) this.sysUIComponentImpl.keyguardBypassControllerProvider.get(), (KeyguardUpdateMonitor) this.sysUIComponentImpl.keyguardUpdateMonitorProvider.get(), (BiometricUnlockController) this.sysUIComponentImpl.biometricUnlockControllerProvider.get(), (SysuiStatusBarStateController) this.sysUIComponentImpl.statusBarStateControllerImplProvider.get(), (StatusBarContentInsetsProvider) this.sysUIComponentImpl.statusBarContentInsetsProvider.get(), (UserManager) this.globalRootComponentImpl.provideUserManagerProvider.get(), (StatusBarUserSwitcherFeatureController) this.sysUIComponentImpl.statusBarUserSwitcherFeatureControllerProvider.get(), this.bindStatusBarUserSwitcherControllerProvider.get(), (StatusBarUserInfoTracker) this.sysUIComponentImpl.statusBarUserInfoTrackerProvider.get(), (SecureSettings) this.sysUIComponentImpl.secureSettingsImpl(), (Executor) this.globalRootComponentImpl.provideMainExecutorProvider.get());
        }
    }

    private static final class SysUIComponentImpl implements SysUIComponent {
        private Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
        private Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
        private Provider<AccessibilityController> accessibilityControllerProvider;
        private Provider<AccessibilityFloatingMenuController> accessibilityFloatingMenuControllerProvider;
        private Provider<AccessibilityManagerWrapper> accessibilityManagerWrapperProvider;
        private Provider<ActionClickLogger> actionClickLoggerProvider;
        private Provider<ActionProxyReceiver> actionProxyReceiverProvider;
        private Provider<ActiveUnlockConfig> activeUnlockConfigProvider;
        /* access modifiers changed from: private */
        public Provider<ActivityIntentHelper> activityIntentHelperProvider;
        private Provider<ActivityStarterDelegate> activityStarterDelegateProvider;
        private Provider<UserDetailView.Adapter> adapterProvider;
        private Provider<AirplaneModeTile> airplaneModeTileProvider;
        private Provider<AlarmTile> alarmTileProvider;
        /* access modifiers changed from: private */
        public Provider<AlwaysOnDisplayPolicy> alwaysOnDisplayPolicyProvider;
        private Provider<AmbientStateEx> ambientStateExProvider;
        /* access modifiers changed from: private */
        public Provider<AmbientState> ambientStateProvider;
        private Provider<AnimatedImageNotificationManager> animatedImageNotificationManagerProvider;
        /* access modifiers changed from: private */
        public Provider<AppOpsControllerImpl> appOpsControllerImplProvider;
        private Provider<AppOpsPrivacyItemMonitor> appOpsPrivacyItemMonitorProvider;
        private Provider<AssistLogger> assistLoggerProvider;
        private Provider<AssistManagerEx> assistManagerExProvider;
        /* access modifiers changed from: private */
        public Provider<AssistManager> assistManagerProvider;
        /* access modifiers changed from: private */
        public Provider<AssistantFeedbackController> assistantFeedbackControllerProvider;
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
        /* access modifiers changed from: private */
        public Provider<DeviceProvisionedController> bindDeviceProvisionedControllerProvider;
        /* access modifiers changed from: private */
        public Provider<BindEventManagerImpl> bindEventManagerImplProvider;
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
        private Provider<NotificationClicker.Builder> builderProvider3;
        /* access modifiers changed from: private */
        public Provider<DelayedWakeLock.Builder> builderProvider4;
        private Provider<CustomTile.Builder> builderProvider5;
        private Provider<NightDisplayListenerModule.Builder> builderProvider6;
        private Provider<AutoAddTracker.Builder> builderProvider7;
        private Provider<TileServiceRequestController.Builder> builderProvider8;
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
        private Provider<CentralSurfacesComponent.Factory> centralSurfacesComponentFactoryProvider;
        private Provider<CentralSurfacesImplEx> centralSurfacesImplExProvider;
        /* access modifiers changed from: private */
        public Provider<CentralSurfacesImpl> centralSurfacesImplProvider;
        private Provider<ChannelEditorDialogController> channelEditorDialogControllerProvider;
        private Provider<ClipboardListener> clipboardListenerProvider;
        private Provider<ClipboardOverlayControllerFactory> clipboardOverlayControllerFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<ClockManager> clockManagerProvider;
        private Provider<ColorCorrectionTile> colorCorrectionTileProvider;
        private Provider<ColorInversionTile> colorInversionTileProvider;
        private Provider<CommandQueueEx> commandQueueExProvider;
        /* access modifiers changed from: private */
        public Provider<CommandRegistry> commandRegistryProvider;
        /* access modifiers changed from: private */
        public Provider<ConfigurationControllerImpl> configurationControllerImplProvider;
        private Provider<ContextComponentResolver> contextComponentResolverProvider;
        private Provider<ControlActionCoordinatorImpl> controlActionCoordinatorImplProvider;
        private Provider<ControlsActivity> controlsActivityProvider;
        private Provider<ControlsBindingControllerImpl> controlsBindingControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<ControlsComponent> controlsComponentProvider;
        private Provider<ControlsControllerImpl> controlsControllerImplProvider;
        private Provider<ControlsEditingActivity> controlsEditingActivityProvider;
        private Provider<ControlsFavoritingActivity> controlsFavoritingActivityProvider;
        private Provider<ControlsListingControllerImpl> controlsListingControllerImplProvider;
        private Provider<ControlsMetricsLoggerImpl> controlsMetricsLoggerImplProvider;
        private Provider<ControlsProviderSelectorActivity> controlsProviderSelectorActivityProvider;
        private Provider<ControlsRequestDialog> controlsRequestDialogProvider;
        private Provider<ControlsUiControllerImpl> controlsUiControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<ConversationNotificationManager> conversationNotificationManagerProvider;
        private Provider<ConversationNotificationProcessor> conversationNotificationProcessorProvider;
        private Provider<CoordinatorsSubcomponent.Factory> coordinatorsSubcomponentFactoryProvider;
        private Provider<CreateUserActivity> createUserActivityProvider;
        private Provider<CustomIconCache> customIconCacheProvider;
        private Provider<CustomTileStatePersister> customTileStatePersisterProvider;
        /* access modifiers changed from: private */
        public Provider<DarkIconDispatcherImpl> darkIconDispatcherImplProvider;
        private Provider<DataSaverTile> dataSaverTileProvider;
        /* access modifiers changed from: private */
        public Provider<DateFormatUtil> dateFormatUtilProvider;
        /* access modifiers changed from: private */
        public Provider<DebugModeFilterProvider> debugModeFilterProvider;
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
        /* access modifiers changed from: private */
        public Provider<DozeScrimController> dozeScrimControllerProvider;
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
        private Provider<Optional<LowLightClockController>> dynamicOverrideOptionalOfLowLightClockControllerProvider;
        /* access modifiers changed from: private */
        public Provider<DynamicPrivacyController> dynamicPrivacyControllerProvider;
        private Provider<EnhancedEstimatesImpl> enhancedEstimatesImplProvider;
        private Provider<ExpandableNotificationRowComponent.Builder> expandableNotificationRowComponentBuilderProvider;
        private Provider<NotificationLogger.ExpansionStateLogger> expansionStateLoggerProvider;
        private Provider<ExtensionControllerImpl> extensionControllerImplProvider;
        private Provider<FaceRecognitionController> faceRecognitionControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LightBarTransitionsController.Factory> factoryProvider;
        private Provider<SplitShadeOverScroller.Factory> factoryProvider10;
        /* access modifiers changed from: private */
        public Provider<StatusBarIconController.TintedIconManager.Factory> factoryProvider11;
        /* access modifiers changed from: private */
        public Provider<EdgeBackGestureHandler.Factory> factoryProvider2;
        private Provider<LockscreenShadeKeyguardTransitionController.Factory> factoryProvider3;
        private Provider<SplitShadeLockScreenOverScroller.Factory> factoryProvider4;
        private Provider<SingleShadeLockScreenOverScroller.Factory> factoryProvider5;
        /* access modifiers changed from: private */
        public Provider<BrightnessSliderController.Factory> factoryProvider6;
        private Provider<KeyguardBouncer.Factory> factoryProvider7;
        /* access modifiers changed from: private */
        public Provider<KeyguardMessageAreaController.Factory> factoryProvider8;
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
        /* access modifiers changed from: private */
        public Provider<ForegroundServiceController> foregroundServiceControllerProvider;
        private Provider<ForegroundServiceNotificationListener> foregroundServiceNotificationListenerProvider;
        private Provider<ForegroundServicesDialog> foregroundServicesDialogProvider;
        private Provider<FragmentService.FragmentCreator.Factory> fragmentCreatorFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<FragmentService> fragmentServiceProvider;
        private Provider<GarbageMonitor> garbageMonitorProvider;
        private Provider<GlobalActionsComponent> globalActionsComponentProvider;
        /* access modifiers changed from: private */
        public Provider<GlobalActionsDialogLite> globalActionsDialogLiteProvider;
        private Provider<GlobalActionsImpl> globalActionsImplProvider;
        /* access modifiers changed from: private */
        public final GlobalRootComponentImpl globalRootComponentImpl;
        /* access modifiers changed from: private */
        public Provider globalSettingsImplProvider;
        private Provider<GlyphsControllerImpl> glyphsControllerImplProvider;
        private Provider<GlyphsTile> glyphsTileProvider;
        private Provider<GroupCoalescerLogger> groupCoalescerLoggerProvider;
        private Provider<GroupCoalescer> groupCoalescerProvider;
        private Provider<HdmiCecSetMenuLanguageActivity> hdmiCecSetMenuLanguageActivityProvider;
        private Provider<HdmiCecSetMenuLanguageHelper> hdmiCecSetMenuLanguageHelperProvider;
        private Provider<HeadsUpControllerEx> headsUpControllerExProvider;
        private Provider<HeadsUpController> headsUpControllerProvider;
        private Provider<HeadsUpManagerLogger> headsUpManagerLoggerProvider;
        private Provider<HeadsUpViewBinderLogger> headsUpViewBinderLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<HeadsUpViewBinder> headsUpViewBinderProvider;
        /* access modifiers changed from: private */
        public Provider<HighPriorityProvider> highPriorityProvider;
        private Provider<HistoryTracker> historyTrackerProvider;
        private Provider<HotspotControllerImpl> hotspotControllerImplProvider;
        private Provider<HotspotTile> hotspotTileProvider;
        private Provider<IconBuilder> iconBuilderProvider;
        /* access modifiers changed from: private */
        public Provider<IconManager> iconManagerProvider;
        private Provider imageExporterProvider;
        private Provider imageTileSetProvider;
        /* access modifiers changed from: private */
        public Provider<InitController> initControllerProvider;
        private Provider<InstantAppNotifier> instantAppNotifierProvider;
        private Provider<InternetDialogController> internetDialogControllerProvider;
        private Provider<InternetDialogFactory> internetDialogFactoryProvider;
        private Provider<InternetTile> internetTileProvider;
        /* access modifiers changed from: private */
        public Provider<Boolean> isPMLiteEnabledProvider;
        private Provider<Boolean> isReduceBrightColorsAvailableProvider;
        private Provider<KeyboardUI> keyboardUIProvider;
        private Provider<KeyguardBiometricLockoutLogger> keyguardBiometricLockoutLoggerProvider;
        private Provider<KeyguardBouncerComponent.Factory> keyguardBouncerComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardBypassController> keyguardBypassControllerProvider;
        private Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
        private Provider<KeyguardDisplayManager> keyguardDisplayManagerProvider;
        private Provider<KeyguardEnvironmentImpl> keyguardEnvironmentImplProvider;
        private Provider<KeyguardIndicationControllerEx> keyguardIndicationControllerExProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardIndicationController> keyguardIndicationControllerProvider;
        private Provider<KeyguardLifecyclesDispatcher> keyguardLifecyclesDispatcherProvider;
        private Provider<KeyguardLiftController> keyguardLiftControllerProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardMediaController> keyguardMediaControllerProvider;
        /* access modifiers changed from: private */
        public Provider keyguardNotificationVisibilityProviderImplProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardQsUserSwitchComponent.Factory> keyguardQsUserSwitchComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
        private Provider<KeyguardService> keyguardServiceProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardStateControllerImpl> keyguardStateControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardStatusBarViewComponent.Factory> keyguardStatusBarViewComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardStatusViewComponent.Factory> keyguardStatusViewComponentFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
        /* access modifiers changed from: private */
        public Provider<KeyguardUserSwitcherComponent.Factory> keyguardUserSwitcherComponentFactoryProvider;
        private Provider<KeyguardWeatherControllerImpl> keyguardWeatherControllerImplProvider;
        private Provider<LSShadeTransitionLogger> lSShadeTransitionLoggerProvider;
        private Provider<LatencyTester> latencyTesterProvider;
        private Provider<LaunchConversationActivity> launchConversationActivityProvider;
        private Provider<LeakReporter> leakReporterProvider;
        private Provider<LegacyNotificationPresenterExtensions> legacyNotificationPresenterExtensionsProvider;
        private Provider<LiftWakeGestureController> liftWakeGestureControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LightBarController> lightBarControllerProvider;
        private C4840LightBarTransitionsController_Factory lightBarTransitionsControllerProvider;
        private Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
        private Provider<LocationControllerImpl> locationControllerImplProvider;
        private Provider<LocationTile> locationTileProvider;
        /* access modifiers changed from: private */
        public Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;
        private C4835LockscreenShadeKeyguardTransitionController_Factory lockscreenShadeKeyguardTransitionControllerProvider;
        private Provider<LockscreenShadeScrimTransitionController> lockscreenShadeScrimTransitionControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LockscreenShadeTransitionController> lockscreenShadeTransitionControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LockscreenSmartspaceController> lockscreenSmartspaceControllerProvider;
        private Provider<LockscreenWallpaper> lockscreenWallpaperProvider;
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
        /* access modifiers changed from: private */
        public Provider<MediaDataManager> mediaDataManagerProvider;
        private Provider<MediaDeviceManager> mediaDeviceManagerProvider;
        /* access modifiers changed from: private */
        public Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
        private Provider<MediaFlags> mediaFlagsProvider;
        /* access modifiers changed from: private */
        public Provider<MediaHierarchyManager> mediaHierarchyManagerProvider;
        private Provider<MediaHostStatesManager> mediaHostStatesManagerProvider;
        private Provider<MediaMuteAwaitConnectionCli> mediaMuteAwaitConnectionCliProvider;
        private Provider<MediaMuteAwaitConnectionManagerFactory> mediaMuteAwaitConnectionManagerFactoryProvider;
        private Provider<MediaMuteAwaitLogger> mediaMuteAwaitLoggerProvider;
        private Provider<MediaOutputBroadcastDialogFactory> mediaOutputBroadcastDialogFactoryProvider;
        private Provider<MediaOutputDialogFactory> mediaOutputDialogFactoryProvider;
        private Provider<MediaOutputDialogReceiver> mediaOutputDialogReceiverProvider;
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
        /* access modifiers changed from: private */
        public Provider<NavigationBarController> navigationBarControllerProvider;
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
        private Provider<NodeSpecBuilderLogger> nodeSpecBuilderLoggerProvider;
        private Provider<NotifBindPipelineInitializer> notifBindPipelineInitializerProvider;
        private Provider<NotifBindPipelineLogger> notifBindPipelineLoggerProvider;
        private Provider<NotifBindPipeline> notifBindPipelineProvider;
        private Provider<NotifCollectionLogger> notifCollectionLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<NotifCollection> notifCollectionProvider;
        private Provider<NotifCoordinators> notifCoordinatorsProvider;
        /* access modifiers changed from: private */
        public Provider<NotifInflaterImpl> notifInflaterImplProvider;
        /* access modifiers changed from: private */
        public Provider<NotifInflationErrorManager> notifInflationErrorManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotifLiveDataStoreImpl> notifLiveDataStoreImplProvider;
        private Provider notifPipelineChoreographerImplProvider;
        /* access modifiers changed from: private */
        public Provider<NotifPipelineFlags> notifPipelineFlagsProvider;
        private Provider<NotifPipelineInitializer> notifPipelineInitializerProvider;
        /* access modifiers changed from: private */
        public Provider<NotifPipeline> notifPipelineProvider;
        private Provider<NotifRemoteViewCacheImpl> notifRemoteViewCacheImplProvider;
        /* access modifiers changed from: private */
        public Provider<NotifUiAdjustmentProvider> notifUiAdjustmentProvider;
        /* access modifiers changed from: private */
        public Provider<NotifViewBarn> notifViewBarnProvider;
        private Provider<NotificationChannels> notificationChannelsProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationClickNotifier> notificationClickNotifierProvider;
        private Provider<NotificationClickerLogger> notificationClickerLoggerProvider;
        private Provider<NotificationContentInflater> notificationContentInflaterProvider;
        private Provider<NotificationEntryManagerLogger> notificationEntryManagerLoggerProvider;
        private Provider<NotificationFilter> notificationFilterProvider;
        private Provider<NotificationGroupAlertTransferHelper> notificationGroupAlertTransferHelperProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationGroupManagerLegacy> notificationGroupManagerLegacyProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationIconAreaController> notificationIconAreaControllerProvider;
        private Provider<NotificationInteractionTracker> notificationInteractionTrackerProvider;
        private Provider<NotificationInterruptLogger> notificationInterruptLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationInterruptStateProviderImpl> notificationInterruptStateProviderImplProvider;
        private Provider<NotificationListener> notificationListenerProvider;
        private Provider<NotificationListenerWithPlugins> notificationListenerWithPluginsProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationLockscreenUserManagerImpl> notificationLockscreenUserManagerImplProvider;
        private Provider<NotificationPersonExtractorPluginBoundary> notificationPersonExtractorPluginBoundaryProvider;
        private Provider<NotificationRankingManager> notificationRankingManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationRoundnessManager> notificationRoundnessManagerProvider;
        private Provider<NotificationRowBinderImpl> notificationRowBinderImplProvider;
        private Provider<NotificationSectionsFeatureManager> notificationSectionsFeatureManagerProvider;
        private Provider<NotificationSectionsLogger> notificationSectionsLoggerProvider;
        private Provider<NotificationSectionsManager> notificationSectionsManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationShadeWindowControllerImpl> notificationShadeWindowControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationShelfComponent.Builder> notificationShelfComponentBuilderProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationStackSizeCalculator> notificationStackSizeCalculatorProvider;
        private Provider<NotificationVisibilityProviderImpl> notificationVisibilityProviderImplProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationWakeUpCoordinator> notificationWakeUpCoordinatorProvider;
        private Provider<NotificationsControllerImpl> notificationsControllerImplProvider;
        private Provider<NotificationsControllerStub> notificationsControllerStubProvider;
        private Provider<OneHandedModeTile> oneHandedModeTileProvider;
        private Provider<OngoingCallFlags> ongoingCallFlagsProvider;
        private Provider<OngoingCallLogger> ongoingCallLoggerProvider;
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
        /* access modifiers changed from: private */
        public Provider panelEventsEmitterProvider;
        /* access modifiers changed from: private */
        public Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
        private Provider<PendingRemovalStore> pendingRemovalStoreProvider;
        /* access modifiers changed from: private */
        public Provider<PeopleNotificationIdentifierImpl> peopleNotificationIdentifierImplProvider;
        private Provider<PeopleSpaceActivity> peopleSpaceActivityProvider;
        private Provider<PeopleSpaceWidgetManager> peopleSpaceWidgetManagerProvider;
        private Provider<PeopleSpaceWidgetPinnedReceiver> peopleSpaceWidgetPinnedReceiverProvider;
        private Provider<PeopleSpaceWidgetProvider> peopleSpaceWidgetProvider;
        private Provider<PhoneStateMonitor> phoneStateMonitorProvider;
        private Provider<PhoneStatusBarPolicy> phoneStatusBarPolicyProvider;
        private Provider pointerCountClassifierProvider;
        private Provider postureDependentProximitySensorProvider;
        private Provider<PowerNotificationWarnings> powerNotificationWarningsProvider;
        private Provider<PowerUI> powerUIProvider;
        private Provider<PrivacyConfig> privacyConfigProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyDialogController> privacyDialogControllerProvider;
        private Provider<PrivacyDotDecorProviderFactory> privacyDotDecorProviderFactoryProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyDotViewController> privacyDotViewControllerProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyItemController> privacyItemControllerProvider;
        /* access modifiers changed from: private */
        public Provider<PrivacyLogger> privacyLoggerProvider;
        private Provider<ProtoTracer> protoTracerProvider;
        private Provider<AccessPointControllerImpl> provideAccessPointControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<ActivityLaunchAnimator> provideActivityLaunchAnimatorProvider;
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
        public Provider<LogBuffer> provideCollapsedSbFragmentLogBufferProvider;
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
        /* access modifiers changed from: private */
        public Provider<Executor> provideExecutorProvider;
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
        /* access modifiers changed from: private */
        public Provider<Optional<LowLightClockController>> provideLowLightClockControllerProvider;
        private Provider<LogBuffer> provideMediaBrowserBufferProvider;
        private Provider<LogBuffer> provideMediaCarouselControllerBufferProvider;
        private Provider<LogBuffer> provideMediaMuteAwaitLogBufferProvider;
        private Provider<LogBuffer> provideMediaTttReceiverLogBufferProvider;
        private Provider<LogBuffer> provideMediaTttSenderLogBufferProvider;
        private Provider<LogBuffer> provideMediaViewLogBufferProvider;
        private Provider<LogBuffer> provideNearbyMediaDevicesLogBufferProvider;
        private Provider<NightDisplayListener> provideNightDisplayListenerProvider;
        /* access modifiers changed from: private */
        public Provider<NotifGutsViewManager> provideNotifGutsViewManagerProvider;
        /* access modifiers changed from: private */
        public Provider<LogBuffer> provideNotifInteractionLogBufferProvider;
        private Provider<NotifRemoteViewCache> provideNotifRemoteViewCacheProvider;
        /* access modifiers changed from: private */
        public Provider<NotifShadeEventSource> provideNotifShadeEventSourceProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationEntryManager> provideNotificationEntryManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationGutsManager> provideNotificationGutsManagerProvider;
        /* access modifiers changed from: private */
        public Provider<LogBuffer> provideNotificationHeadsUpLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationLogger> provideNotificationLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationMediaManager> provideNotificationMediaManagerProvider;
        private Provider<NotificationPanelLogger> provideNotificationPanelLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationRemoteInputManager> provideNotificationRemoteInputManagerProvider;
        private Provider<LogBuffer> provideNotificationSectionLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationViewHierarchyManager> provideNotificationViewHierarchyManagerProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationVisibilityProvider> provideNotificationVisibilityProvider;
        /* access modifiers changed from: private */
        public Provider<NotificationsController> provideNotificationsControllerProvider;
        /* access modifiers changed from: private */
        public Provider<LogBuffer> provideNotificationsLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<OnUserInteractionCallback> provideOnUserInteractionCallbackProvider;
        /* access modifiers changed from: private */
        public Provider<OngoingCallController> provideOngoingCallControllerProvider;
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
        private Provider<LogBuffer> provideSwipeAwayGestureLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<SysUIUnfoldComponent>> provideSysUIUnfoldComponentProvider;
        /* access modifiers changed from: private */
        public Provider<SysUiState> provideSysUiStateProvider;
        private Provider<String> provideThemePickerPackageProvider;
        /* access modifiers changed from: private */
        public Provider<Handler> provideTimeTickHandlerProvider;
        private Provider<LogBuffer> provideToastLogBufferProvider;
        /* access modifiers changed from: private */
        public Provider<UserTracker> provideUserTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<VisualStabilityManager> provideVisualStabilityManagerProvider;
        private Provider<VolumeDialog> provideVolumeDialogProvider;
        private Provider<SectionHeaderController> providesAlertingHeaderControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NodeController> providesAlertingHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesAlertingHeaderSubcomponentProvider;
        private Provider<MessageRouter> providesBackgroundMessageRouterProvider;
        private Provider<Set<FalsingClassifier>> providesBrightLineGestureClassifiersProvider;
        private Provider<Boolean> providesControlsFeatureEnabledProvider;
        private Provider<String[]> providesDeviceStateRotationLockDefaultsProvider;
        private Provider<Float> providesDoubleTapTouchSlopProvider;
        private Provider<SectionHeaderController> providesIncomingHeaderControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NodeController> providesIncomingHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesIncomingHeaderSubcomponentProvider;
        private Provider<MediaHost> providesKeyguardMediaHostProvider;
        private Provider<LeakDetector> providesLeakDetectorProvider;
        private Provider<MessageRouter> providesMainMessageRouterProvider;
        private Provider<Optional<MediaMuteAwaitConnectionCli>> providesMediaMuteAwaitConnectionCliProvider;
        private Provider<LogBuffer> providesMediaTimeoutListenerLogBufferProvider;
        private Provider<Optional<MediaTttChipControllerReceiver>> providesMediaTttChipControllerReceiverProvider;
        private Provider<Optional<MediaTttChipControllerSender>> providesMediaTttChipControllerSenderProvider;
        private Provider<Optional<MediaTttCommandLineHelper>> providesMediaTttCommandLineHelperProvider;
        private Provider<MediaTttLogger> providesMediaTttReceiverLoggerProvider;
        private Provider<MediaTttLogger> providesMediaTttSenderLoggerProvider;
        private Provider<Optional<NearbyMediaDevicesManager>> providesNearbyMediaDevicesManagerProvider;
        private Provider<SectionHeaderController> providesPeopleHeaderControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NodeController> providesPeopleHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesPeopleHeaderSubcomponentProvider;
        private Provider<Executor> providesPluginExecutorProvider;
        /* access modifiers changed from: private */
        public Provider<MediaHost> providesQSMediaHostProvider;
        /* access modifiers changed from: private */
        public Provider<MediaHost> providesQuickQSMediaHostProvider;
        /* access modifiers changed from: private */
        public Provider<SectionHeaderController> providesSilentHeaderControllerProvider;
        /* access modifiers changed from: private */
        public Provider<NodeController> providesSilentHeaderNodeControllerProvider;
        private Provider<SectionHeaderControllerSubcomponent> providesSilentHeaderSubcomponentProvider;
        private Provider<Float> providesSingleTapTouchSlopProvider;
        private Provider<StatusBarWindowView> providesStatusBarWindowViewProvider;
        /* access modifiers changed from: private */
        public Provider<ViewMediatorCallback> providesViewMediatorCallbackProvider;
        private Provider proximityClassifierProvider;
        private Provider proximitySensorImplProvider;
        /* access modifiers changed from: private */
        public Provider<PulseExpansionHandler> pulseExpansionHandlerProvider;
        /* access modifiers changed from: private */
        public Provider<QRCodeScannerController> qRCodeScannerControllerProvider;
        private Provider<QRCodeScannerTile> qRCodeScannerTileProvider;
        private Provider<QSFactoryImpl> qSFactoryImplProvider;
        /* access modifiers changed from: private */
        public Provider<QSLogger> qSLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<QSTileHost> qSTileHostProvider;
        /* access modifiers changed from: private */
        public Provider<QsFrameTranslateImpl> qsFrameTranslateImplProvider;
        /* access modifiers changed from: private */
        public Provider<QuickAccessWalletController> quickAccessWalletControllerProvider;
        private Provider<QuickAccessWalletTile> quickAccessWalletTileProvider;
        /* access modifiers changed from: private */
        public Provider<RecordingController> recordingControllerProvider;
        private Provider<RecordingService> recordingServiceProvider;
        private Provider<ReduceBrightColorsController> reduceBrightColorsControllerProvider;
        private Provider<ReduceBrightColorsTile> reduceBrightColorsTileProvider;
        /* access modifiers changed from: private */
        public Provider<RemoteInputNotificationRebuilder> remoteInputNotificationRebuilderProvider;
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
        private Provider<ScreenDecorations> screenDecorationsProvider;
        /* access modifiers changed from: private */
        public Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
        private Provider<ScreenOnCoordinator> screenOnCoordinatorProvider;
        private Provider<ScreenPinningRequest> screenPinningRequestProvider;
        private Provider<ScreenRecordTile> screenRecordTileProvider;
        private Provider<ScreenshotController> screenshotControllerProvider;
        private Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
        private Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
        /* access modifiers changed from: private */
        public Provider<ScrimController> scrimControllerProvider;
        private Provider<ScrollCaptureClient> scrollCaptureClientProvider;
        private Provider<ScrollCaptureController> scrollCaptureControllerProvider;
        /* access modifiers changed from: private */
        public Provider<SectionClassifier> sectionClassifierProvider;
        private Provider<SectionHeaderControllerSubcomponent.Builder> sectionHeaderControllerSubcomponentBuilderProvider;
        /* access modifiers changed from: private */
        public Provider<SectionHeaderVisibilityProvider> sectionHeaderVisibilityProvider;
        /* access modifiers changed from: private */
        public Provider secureSettingsImplProvider;
        /* access modifiers changed from: private */
        public Provider<SecurityControllerImpl> securityControllerImplProvider;
        private Provider<SeekBarViewModel> seekBarViewModelProvider;
        private Provider<SensorUseStartedActivity> sensorUseStartedActivityProvider;
        private Provider<GarbageMonitor.Service> serviceProvider;
        /* access modifiers changed from: private */
        public Provider<SessionTracker> sessionTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<BackAnimation>> setBackAnimationProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<Bubbles>> setBubblesProvider;
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
        private Provider<ShadeEventCoordinatorLogger> shadeEventCoordinatorLoggerProvider;
        /* access modifiers changed from: private */
        public Provider<ShadeEventCoordinator> shadeEventCoordinatorProvider;
        private Provider<ShadeListBuilderLogger> shadeListBuilderLoggerProvider;
        private Provider<ShadeListBuilder> shadeListBuilderProvider;
        /* access modifiers changed from: private */
        public Provider<ShadeTransitionController> shadeTransitionControllerProvider;
        private Provider<ShadeViewDifferLogger> shadeViewDifferLoggerProvider;
        private Provider<ShadeViewManagerFactory> shadeViewManagerFactoryProvider;
        private ShadeViewManager_Factory shadeViewManagerProvider;
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
        private C4841SplitShadeOverScroller_Factory splitShadeOverScrollerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarContentInsetsProvider> statusBarContentInsetsProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarHideIconsForBouncerManager> statusBarHideIconsForBouncerManagerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarIconControllerImpl> statusBarIconControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarLocationPublisher> statusBarLocationPublisherProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarRemoteInputCallback> statusBarRemoteInputCallbackProvider;
        private Provider<StatusBarSignalPolicy> statusBarSignalPolicyProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarStateControllerImpl> statusBarStateControllerImplProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarTouchableRegionManager> statusBarTouchableRegionManagerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarUserInfoTracker> statusBarUserInfoTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarUserSwitcherFeatureController> statusBarUserSwitcherFeatureControllerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarWindowController> statusBarWindowControllerProvider;
        /* access modifiers changed from: private */
        public Provider<StatusBarWindowStateController> statusBarWindowStateControllerProvider;
        private Provider<StorageNotification> storageNotificationProvider;
        /* access modifiers changed from: private */
        public Provider<QSCarrierGroupController.SubscriptionManagerSlotIndexResolver> subscriptionManagerSlotIndexResolverProvider;
        private Provider<SwipeStatusBarAwayGestureHandler> swipeStatusBarAwayGestureHandlerProvider;
        private Provider<SwipeStatusBarAwayGestureLogger> swipeStatusBarAwayGestureLoggerProvider;
        /* access modifiers changed from: private */
        public final SysUIComponentImpl sysUIComponentImpl;
        private Provider<SysUIUnfoldComponent.Factory> sysUIUnfoldComponentFactoryProvider;
        private Provider<SystemActions> systemActionsProvider;
        private Provider<SystemEventChipAnimationController> systemEventChipAnimationControllerProvider;
        private Provider<SystemEventCoordinator> systemEventCoordinatorProvider;
        private Provider<SystemPropertiesHelper> systemPropertiesHelperProvider;
        /* access modifiers changed from: private */
        public Provider<SystemStatusAnimationScheduler> systemStatusAnimationSchedulerProvider;
        private Provider<SystemUIAuxiliaryDumpService> systemUIAuxiliaryDumpServiceProvider;
        private Provider<SystemUIDialogManager> systemUIDialogManagerProvider;
        private Provider<SystemUIService> systemUIServiceProvider;
        /* access modifiers changed from: private */
        public Provider<SysuiColorExtractor> sysuiColorExtractorProvider;
        private Provider<TakeScreenshotService> takeScreenshotServiceProvider;
        private Provider<TapGestureDetector> tapGestureDetectorProvider;
        private Provider<TargetSdkResolver> targetSdkResolverProvider;
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
        private Provider<TvNotificationHandler> tvNotificationHandlerProvider;
        private Provider<TvNotificationPanelActivity> tvNotificationPanelActivityProvider;
        private Provider<TvUnblockSensorActivity> tvUnblockSensorActivityProvider;
        private Provider<TypeClassifier> typeClassifierProvider;
        /* access modifiers changed from: private */
        public Provider<UdfpsController> udfpsControllerProvider;
        private Provider<UdfpsHapticsSimulator> udfpsHapticsSimulatorProvider;
        private Provider<UdfpsShell> udfpsShellProvider;
        private Provider<UiModeNightTile> uiModeNightTileProvider;
        private Provider<UiOffloadThread> uiOffloadThreadProvider;
        private Provider<UnfoldLatencyTracker> unfoldLatencyTrackerProvider;
        /* access modifiers changed from: private */
        public Provider<UnlockedScreenOffAnimationController> unlockedScreenOffAnimationControllerProvider;
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
        /* access modifiers changed from: private */
        public Provider<VibratorHelper> vibratorHelperProvider;
        /* access modifiers changed from: private */
        public Provider<ViewUtil> viewUtilProvider;
        /* access modifiers changed from: private */
        public Provider<VisualStabilityCoordinator> visualStabilityCoordinatorProvider;
        private Provider<VisualStabilityProvider> visualStabilityProvider;
        private Provider<VolumeDialogComponent> volumeDialogComponentProvider;
        private Provider<VolumeDialogControllerImpl> volumeDialogControllerImplProvider;
        private Provider<VolumeUI> volumeUIProvider;
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
        private Provider<WiredChargingRippleController> wiredChargingRippleControllerProvider;
        private Provider<WorkLockActivity> workLockActivityProvider;
        private Provider<WorkModeTile> workModeTileProvider;
        /* access modifiers changed from: private */
        public Provider<ZenModeControllerImpl> zenModeControllerImplProvider;
        private Provider zigZagClassifierProvider;

        private SysUIComponentImpl(GlobalRootComponentImpl globalRootComponentImpl2, LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            LeakModule leakModule2 = leakModule;
            this.sysUIComponentImpl = this;
            this.globalRootComponentImpl = globalRootComponentImpl2;
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
            return SecureSettingsImpl_Factory.newInstance((ContentResolver) this.globalRootComponentImpl.provideContentResolverProvider.get());
        }

        private void initialize(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.bootCompleteCacheImplProvider = DoubleCheck.provider(BootCompleteCacheImpl_Factory.create(this.globalRootComponentImpl.dumpManagerProvider));
            this.configurationControllerImplProvider = DoubleCheck.provider(ConfigurationControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.globalSettingsImplProvider = GlobalSettingsImpl_Factory.create(this.globalRootComponentImpl.provideContentResolverProvider);
            this.provideDemoModeControllerProvider = DoubleCheck.provider(DemoModeModule_ProvideDemoModeControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalSettingsImplProvider));
            this.providesLeakDetectorProvider = DoubleCheck.provider(LeakModule_ProvidesLeakDetectorFactory.create(leakModule, this.globalRootComponentImpl.dumpManagerProvider, TrackedCollections_Factory.create()));
            Provider<Looper> provider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBgLooperFactory.create());
            this.provideBgLooperProvider = provider;
            this.provideBgHandlerProvider = SysUIConcurrencyModule_ProvideBgHandlerFactory.create(provider);
            this.provideUserTrackerProvider = DoubleCheck.provider(SettingsModule_ProvideUserTrackerFactory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideBgHandlerProvider));
            Provider<TunerServiceImpl> provider2 = DoubleCheck.provider(TunerServiceImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.providesLeakDetectorProvider, this.provideDemoModeControllerProvider, this.provideUserTrackerProvider));
            this.tunerServiceImplProvider = provider2;
            this.tunerActivityProvider = TunerActivity_Factory.create(this.provideDemoModeControllerProvider, provider2);
            this.foregroundServicesDialogProvider = ForegroundServicesDialog_Factory.create(this.globalRootComponentImpl.provideMetricsLoggerProvider);
            this.provideBackgroundExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundExecutorFactory.create(this.provideBgLooperProvider));
            this.provideLogcatEchoTrackerProvider = DoubleCheck.provider(LogModule_ProvideLogcatEchoTrackerFactory.create(this.globalRootComponentImpl.provideContentResolverProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            Provider<LogBufferFactory> provider3 = DoubleCheck.provider(LogBufferFactory_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.provideLogcatEchoTrackerProvider));
            this.logBufferFactoryProvider = provider3;
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideBroadcastDispatcherLogBufferFactory.create(provider3));
            this.provideBroadcastDispatcherLogBufferProvider = provider4;
            BroadcastDispatcherLogger_Factory create = BroadcastDispatcherLogger_Factory.create(provider4);
            this.broadcastDispatcherLoggerProvider = create;
            this.pendingRemovalStoreProvider = PendingRemovalStore_Factory.create(create);
            Provider<BroadcastDispatcher> provider5 = DoubleCheck.provider(BroadcastDispatcher_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.dumpManagerProvider, this.broadcastDispatcherLoggerProvider, this.provideUserTrackerProvider, this.pendingRemovalStoreProvider));
            this.broadcastDispatcherProvider = provider5;
            this.workLockActivityProvider = WorkLockActivity_Factory.create(provider5, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.providePackageManagerProvider);
            this.deviceConfigProxyProvider = DoubleCheck.provider(DeviceConfigProxy_Factory.create());
            this.enhancedEstimatesImplProvider = DoubleCheck.provider(EnhancedEstimatesImpl_Factory.create());
            this.provideBatteryControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideBatteryControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.enhancedEstimatesImplProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.broadcastDispatcherProvider, this.provideDemoModeControllerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.dockManagerImplProvider = DoubleCheck.provider(DockManagerImpl_Factory.create());
            Provider<FalsingDataProvider> provider6 = DoubleCheck.provider(FalsingDataProvider_Factory.create(this.globalRootComponentImpl.provideDisplayMetricsProvider, this.provideBatteryControllerProvider, this.dockManagerImplProvider));
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
            FalsingModule_ProvidesSingleTapTouchSlopFactory create4 = FalsingModule_ProvidesSingleTapTouchSlopFactory.create(this.globalRootComponentImpl.provideViewConfigurationProvider);
            this.providesSingleTapTouchSlopProvider = create4;
            this.singleTapClassifierProvider = SingleTapClassifier_Factory.create(this.falsingDataProvider, create4);
            FalsingModule_ProvidesDoubleTapTouchSlopFactory create5 = FalsingModule_ProvidesDoubleTapTouchSlopFactory.create(this.globalRootComponentImpl.provideResourcesProvider);
            this.providesDoubleTapTouchSlopProvider = create5;
            this.doubleTapClassifierProvider = DoubleTapClassifier_Factory.create(this.falsingDataProvider, this.singleTapClassifierProvider, create5, FalsingModule_ProvidesDoubleTapTimeoutMsFactory.create());
            Provider<SystemClock> provider7 = DoubleCheck.provider(SystemClockImpl_Factory.create());
            this.bindSystemClockProvider = provider7;
            this.historyTrackerProvider = DoubleCheck.provider(HistoryTracker_Factory.create(provider7));
            this.statusBarStateControllerImplProvider = DoubleCheck.provider(StatusBarStateControllerImpl_Factory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider));
            this.protoTracerProvider = DoubleCheck.provider(ProtoTracer_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.commandRegistryProvider = DoubleCheck.provider(CommandRegistry_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.provideCommandQueueProvider = DoubleCheck.provider(CentralSurfacesDependenciesModule_ProvideCommandQueueFactory.create(this.globalRootComponentImpl.contextProvider, this.protoTracerProvider, this.commandRegistryProvider));
            this.panelExpansionStateManagerProvider = DoubleCheck.provider(PanelExpansionStateManager_Factory.create());
            this.falsingManagerProxyProvider = new DelegateFactory();
            this.keyguardUpdateMonitorProvider = new DelegateFactory();
            this.asyncSensorManagerProvider = DoubleCheck.provider(AsyncSensorManager_Factory.create(this.globalRootComponentImpl.providesSensorManagerProvider, ThreadFactoryImpl_Factory.create(), this.globalRootComponentImpl.providesPluginManagerProvider));
            ThresholdSensorImpl_BuilderFactory_Factory create6 = ThresholdSensorImpl_BuilderFactory_Factory.create(this.globalRootComponentImpl.provideResourcesProvider, this.asyncSensorManagerProvider, this.globalRootComponentImpl.provideExecutionProvider);
            this.builderFactoryProvider = create6;
            this.providePostureToProximitySensorMappingProvider = SensorModule_ProvidePostureToProximitySensorMappingFactory.create(create6, this.globalRootComponentImpl.provideResourcesProvider);
            this.providePostureToSecondaryProximitySensorMappingProvider = C3266xe478f0bc.create(this.builderFactoryProvider, this.globalRootComponentImpl.provideResourcesProvider);
            this.devicePostureControllerImplProvider = DoubleCheck.provider(DevicePostureControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideDeviceStateManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.postureDependentProximitySensorProvider = PostureDependentProximitySensor_Factory.create(this.providePostureToProximitySensorMappingProvider, this.providePostureToSecondaryProximitySensorMappingProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.globalRootComponentImpl.provideExecutionProvider, this.devicePostureControllerImplProvider);
            this.builderProvider = ThresholdSensorImpl_Builder_Factory.create(this.globalRootComponentImpl.provideResourcesProvider, this.asyncSensorManagerProvider, this.globalRootComponentImpl.provideExecutionProvider);
            this.providePrimaryProximitySensorProvider = SensorModule_ProvidePrimaryProximitySensorFactory.create(this.globalRootComponentImpl.providesSensorManagerProvider, this.builderProvider);
            SensorModule_ProvideSecondaryProximitySensorFactory create7 = SensorModule_ProvideSecondaryProximitySensorFactory.create(this.builderProvider);
            this.provideSecondaryProximitySensorProvider = create7;
            this.proximitySensorImplProvider = ProximitySensorImpl_Factory.create(this.providePrimaryProximitySensorProvider, create7, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.globalRootComponentImpl.provideExecutionProvider);
            this.provideProximitySensorProvider = SensorModule_ProvideProximitySensorFactory.create(this.globalRootComponentImpl.provideResourcesProvider, this.postureDependentProximitySensorProvider, this.proximitySensorImplProvider);
            DelegateFactory delegateFactory = new DelegateFactory();
            this.keyguardStateControllerImplProvider = delegateFactory;
            this.falsingCollectorImplProvider = DoubleCheck.provider(FalsingCollectorImpl_Factory.create(this.falsingDataProvider, this.falsingManagerProxyProvider, this.keyguardUpdateMonitorProvider, this.historyTrackerProvider, this.provideProximitySensorProvider, this.statusBarStateControllerImplProvider, delegateFactory, this.provideBatteryControllerProvider, this.dockManagerImplProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.bindSystemClockProvider));
            this.statusBarKeyguardViewManagerProvider = new DelegateFactory();
            this.dismissCallbackRegistryProvider = DoubleCheck.provider(DismissCallbackRegistry_Factory.create(this.globalRootComponentImpl.provideUiBackgroundExecutorProvider));
            SecureSettingsImpl_Factory create8 = SecureSettingsImpl_Factory.create(this.globalRootComponentImpl.provideContentResolverProvider);
            this.secureSettingsImplProvider = create8;
            Provider<DeviceProvisionedControllerImpl> provider8 = DoubleCheck.provider(DeviceProvisionedControllerImpl_Factory.create(create8, this.globalSettingsImplProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideBgHandlerProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.deviceProvisionedControllerImplProvider = provider8;
            this.bindDeviceProvisionedControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_BindDeviceProvisionedControllerFactory.create(provider8));
            this.centralSurfacesImplProvider = new DelegateFactory();
            this.systemPropertiesHelperProvider = DoubleCheck.provider(SystemPropertiesHelper_Factory.create());
            this.featureFlagsReleaseProvider = DoubleCheck.provider(FeatureFlagsRelease_Factory.create(this.globalRootComponentImpl.provideResourcesProvider, this.systemPropertiesHelperProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.notifPipelineFlagsProvider = NotifPipelineFlags_Factory.create(this.globalRootComponentImpl.contextProvider, this.featureFlagsReleaseProvider);
            this.notificationListenerProvider = DoubleCheck.provider(NotificationListener_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideNotificationManagerProvider, this.bindSystemClockProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.providesPluginManagerProvider));
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideNotificationsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationsLogBufferProvider = provider9;
            this.notificationEntryManagerLoggerProvider = NotificationEntryManagerLogger_Factory.create(provider9);
            Provider<ExtensionControllerImpl> provider10 = DoubleCheck.provider(ExtensionControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesLeakDetectorProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.tunerServiceImplProvider, this.configurationControllerImplProvider));
            this.extensionControllerImplProvider = provider10;
            this.notificationPersonExtractorPluginBoundaryProvider = DoubleCheck.provider(NotificationPersonExtractorPluginBoundary_Factory.create(provider10));
            DelegateFactory delegateFactory2 = new DelegateFactory();
            this.notificationGroupManagerLegacyProvider = delegateFactory2;
            Provider<GroupMembershipManager> provider11 = DoubleCheck.provider(NotificationsModule_ProvideGroupMembershipManagerFactory.create(this.notifPipelineFlagsProvider, delegateFactory2));
            this.provideGroupMembershipManagerProvider = provider11;
            this.peopleNotificationIdentifierImplProvider = DoubleCheck.provider(PeopleNotificationIdentifierImpl_Factory.create(this.notificationPersonExtractorPluginBoundaryProvider, provider11));
            Factory<Bubbles> create9 = InstanceFactory.create(optional6);
            this.setBubblesProvider = create9;
            DelegateFactory.setDelegate(this.notificationGroupManagerLegacyProvider, DoubleCheck.provider(NotificationGroupManagerLegacy_Factory.create(this.statusBarStateControllerImplProvider, this.peopleNotificationIdentifierImplProvider, create9, this.globalRootComponentImpl.dumpManagerProvider)));
            this.notifLiveDataStoreImplProvider = DoubleCheck.provider(NotifLiveDataStoreImpl_Factory.create(this.globalRootComponentImpl.provideMainExecutorProvider));
            this.notifCollectionLoggerProvider = NotifCollectionLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.filesProvider = DoubleCheck.provider(Files_Factory.create());
            this.logBufferEulogizerProvider = DoubleCheck.provider(LogBufferEulogizer_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider, this.bindSystemClockProvider, this.filesProvider));
            this.notifCollectionProvider = DoubleCheck.provider(NotifCollection_Factory.create(this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.bindSystemClockProvider, this.notifPipelineFlagsProvider, this.notifCollectionLoggerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.logBufferEulogizerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.notifPipelineChoreographerImplProvider = DoubleCheck.provider(NotifPipelineChoreographerImpl_Factory.create(this.globalRootComponentImpl.providesChoreographerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.notificationClickNotifierProvider = DoubleCheck.provider(NotificationClickNotifier_Factory.create(this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            DelegateFactory delegateFactory3 = new DelegateFactory();
            this.provideNotificationEntryManagerProvider = delegateFactory3;
            this.notificationInteractionTrackerProvider = DoubleCheck.provider(NotificationInteractionTracker_Factory.create(this.notificationClickNotifierProvider, delegateFactory3));
            this.shadeListBuilderLoggerProvider = ShadeListBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeListBuilderProvider = DoubleCheck.provider(ShadeListBuilder_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.notifPipelineChoreographerImplProvider, this.notifPipelineFlagsProvider, this.notificationInteractionTrackerProvider, this.shadeListBuilderLoggerProvider, this.bindSystemClockProvider));
            Provider<RenderStageManager> provider12 = DoubleCheck.provider(RenderStageManager_Factory.create());
            this.renderStageManagerProvider = provider12;
            Provider<NotifPipeline> provider13 = DoubleCheck.provider(NotifPipeline_Factory.create(this.notifPipelineFlagsProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, provider12));
            this.notifPipelineProvider = provider13;
            Provider<CommonNotifCollection> provider14 = DoubleCheck.provider(NotificationsModule_ProvideCommonNotifCollectionFactory.create(this.notifPipelineFlagsProvider, provider13, this.provideNotificationEntryManagerProvider));
            this.provideCommonNotifCollectionProvider = provider14;
            NotificationVisibilityProviderImpl_Factory create10 = NotificationVisibilityProviderImpl_Factory.create(this.notifLiveDataStoreImplProvider, provider14);
            this.notificationVisibilityProviderImplProvider = create10;
            this.provideNotificationVisibilityProvider = DoubleCheck.provider(create10);
            this.notificationLockscreenUserManagerImplProvider = DoubleCheck.provider(NotificationLockscreenUserManagerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.provideDevicePolicyManagerProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.notificationClickNotifierProvider, this.globalRootComponentImpl.provideKeyguardManagerProvider, this.statusBarStateControllerImplProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.bindDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.secureSettingsImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.provideSmartReplyControllerProvider = DoubleCheck.provider(C2637xc35bee5b.create(this.globalRootComponentImpl.dumpManagerProvider, this.provideNotificationVisibilityProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.notificationClickNotifierProvider));
            this.remoteInputNotificationRebuilderProvider = DoubleCheck.provider(RemoteInputNotificationRebuilder_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.optionalOfCentralSurfacesProvider = new DelegateFactory();
            this.remoteInputUriControllerProvider = DoubleCheck.provider(RemoteInputUriController_Factory.create(this.globalRootComponentImpl.provideIStatusBarServiceProvider));
            Provider<LogBuffer> provider15 = DoubleCheck.provider(LogModule_ProvideNotifInteractionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotifInteractionLogBufferProvider = provider15;
            this.actionClickLoggerProvider = ActionClickLogger_Factory.create(provider15);
        }

        private void initialize2(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.provideNotificationRemoteInputManagerProvider = DoubleCheck.provider(C2634xa11cf5e4.create(this.globalRootComponentImpl.contextProvider, this.notifPipelineFlagsProvider, this.notificationLockscreenUserManagerImplProvider, this.provideSmartReplyControllerProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.remoteInputNotificationRebuilderProvider, this.optionalOfCentralSurfacesProvider, this.statusBarStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.remoteInputUriControllerProvider, this.notificationClickNotifierProvider, this.actionClickLoggerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            NotifBindPipelineLogger_Factory create = NotifBindPipelineLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifBindPipelineLoggerProvider = create;
            this.notifBindPipelineProvider = DoubleCheck.provider(NotifBindPipeline_Factory.create(this.provideCommonNotifCollectionProvider, create, GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            NotifRemoteViewCacheImpl_Factory create2 = NotifRemoteViewCacheImpl_Factory.create(this.provideCommonNotifCollectionProvider);
            this.notifRemoteViewCacheImplProvider = create2;
            this.provideNotifRemoteViewCacheProvider = DoubleCheck.provider(create2);
            Provider<BindEventManagerImpl> provider = DoubleCheck.provider(BindEventManagerImpl_Factory.create());
            this.bindEventManagerImplProvider = provider;
            this.conversationNotificationManagerProvider = DoubleCheck.provider(ConversationNotificationManager_Factory.create(provider, this.notificationGroupManagerLegacyProvider, this.globalRootComponentImpl.contextProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineFlagsProvider, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.conversationNotificationProcessorProvider = ConversationNotificationProcessor_Factory.create(this.globalRootComponentImpl.provideLauncherAppsProvider, this.conversationNotificationManagerProvider);
            this.mediaFeatureFlagProvider = MediaFeatureFlag_Factory.create(this.globalRootComponentImpl.contextProvider);
            this.smartReplyConstantsProvider = DoubleCheck.provider(SmartReplyConstants_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.contextProvider, this.deviceConfigProxyProvider));
            this.provideActivityManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideActivityManagerWrapperFactory.create(sharedLibraryModule));
            this.provideDevicePolicyManagerWrapperProvider = DoubleCheck.provider(SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory.create(sharedLibraryModule));
            Provider<KeyguardDismissUtil> provider2 = DoubleCheck.provider(KeyguardDismissUtil_Factory.create());
            this.keyguardDismissUtilProvider = provider2;
            this.smartReplyInflaterImplProvider = SmartReplyInflaterImpl_Factory.create(this.smartReplyConstantsProvider, provider2, this.provideNotificationRemoteInputManagerProvider, this.provideSmartReplyControllerProvider, this.globalRootComponentImpl.contextProvider);
            this.provideActivityStarterProvider = new DelegateFactory();
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideNotificationHeadsUpLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationHeadsUpLogBufferProvider = provider3;
            this.headsUpManagerLoggerProvider = HeadsUpManagerLogger_Factory.create(provider3);
            this.keyguardBypassControllerProvider = DoubleCheck.provider(KeyguardBypassController_Factory.create(this.globalRootComponentImpl.contextProvider, this.tunerServiceImplProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.visualStabilityProvider = DoubleCheck.provider(VisualStabilityProvider_Factory.create());
            Provider<HeadsUpManagerPhone> provider4 = DoubleCheck.provider(ReferenceSystemUIModule_ProvideHeadsUpManagerPhoneFactory.create(this.globalRootComponentImpl.contextProvider, this.headsUpManagerLoggerProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.provideGroupMembershipManagerProvider, this.visualStabilityProvider, this.configurationControllerImplProvider));
            this.provideHeadsUpManagerPhoneProvider = provider4;
            this.smartActionInflaterImplProvider = SmartActionInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityStarterProvider, this.provideSmartReplyControllerProvider, provider4);
            SmartReplyStateInflaterImpl_Factory create3 = SmartReplyStateInflaterImpl_Factory.create(this.smartReplyConstantsProvider, this.provideActivityManagerWrapperProvider, this.globalRootComponentImpl.providePackageManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.smartReplyInflaterImplProvider, this.smartActionInflaterImplProvider);
            this.smartReplyStateInflaterImplProvider = create3;
            this.notificationContentInflaterProvider = DoubleCheck.provider(NotificationContentInflater_Factory.create(this.provideNotifRemoteViewCacheProvider, this.provideNotificationRemoteInputManagerProvider, this.conversationNotificationProcessorProvider, this.mediaFeatureFlagProvider, this.provideBackgroundExecutorProvider, create3));
            this.notifInflationErrorManagerProvider = DoubleCheck.provider(NotifInflationErrorManager_Factory.create());
            RowContentBindStageLogger_Factory create4 = RowContentBindStageLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.rowContentBindStageLoggerProvider = create4;
            this.rowContentBindStageProvider = DoubleCheck.provider(RowContentBindStage_Factory.create(this.notificationContentInflaterProvider, this.notifInflationErrorManagerProvider, create4));
            this.expandableNotificationRowComponentBuilderProvider = new Provider<ExpandableNotificationRowComponent.Builder>() {
                public ExpandableNotificationRowComponent.Builder get() {
                    return new ExpandableNotificationRowComponentBuilder(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.iconBuilderProvider = IconBuilder_Factory.create(this.globalRootComponentImpl.contextProvider);
            this.iconManagerProvider = DoubleCheck.provider(IconManager_Factory.create(this.provideCommonNotifCollectionProvider, this.globalRootComponentImpl.provideLauncherAppsProvider, this.iconBuilderProvider));
            this.lowPriorityInflationHelperProvider = DoubleCheck.provider(LowPriorityInflationHelper_Factory.create(this.notificationGroupManagerLegacyProvider, this.rowContentBindStageProvider, this.notifPipelineFlagsProvider));
            Provider<NotificationRowBinderImpl> provider5 = DoubleCheck.provider(NotificationRowBinderImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideNotificationMessagingUtilProvider, this.provideNotificationRemoteInputManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.notifBindPipelineProvider, this.rowContentBindStageProvider, RowInflaterTask_Factory.create(), this.expandableNotificationRowComponentBuilderProvider, this.iconManagerProvider, this.lowPriorityInflationHelperProvider, this.notifPipelineFlagsProvider));
            this.notificationRowBinderImplProvider = provider5;
            DelegateFactory.setDelegate(this.provideNotificationEntryManagerProvider, DoubleCheck.provider(NotificationsModule_ProvideNotificationEntryManagerFactory.create(this.notificationEntryManagerLoggerProvider, this.notificationGroupManagerLegacyProvider, this.notifPipelineFlagsProvider, provider5, this.provideNotificationRemoteInputManagerProvider, this.providesLeakDetectorProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.notifLiveDataStoreImplProvider, this.globalRootComponentImpl.dumpManagerProvider)));
            this.debugModeFilterProvider = DoubleCheck.provider(DebugModeFilterProvider_Factory.create(this.commandRegistryProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.alwaysOnDisplayPolicyProvider = DoubleCheck.provider(AlwaysOnDisplayPolicy_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.sysUIUnfoldComponentFactoryProvider = new Provider<SysUIUnfoldComponent.Factory>() {
                public SysUIUnfoldComponent.Factory get() {
                    return new SysUIUnfoldComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.provideSysUIUnfoldComponentProvider = DoubleCheck.provider(SysUIUnfoldModule_ProvideSysUIUnfoldComponentFactory.create(sysUIUnfoldModule, this.globalRootComponentImpl.unfoldTransitionProgressProvider, this.globalRootComponentImpl.provideNaturalRotationProgressProvider, this.globalRootComponentImpl.provideStatusBarScopedTransitionProvider, this.sysUIUnfoldComponentFactoryProvider));
            this.wakefulnessLifecycleProvider = DoubleCheck.provider(WakefulnessLifecycle_Factory.create(this.globalRootComponentImpl.contextProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.globalRootComponentImpl.dumpManagerProvider));
            this.newKeyguardViewMediatorProvider = new DelegateFactory();
            this.dozeParametersProvider = new DelegateFactory();
            Provider<UnlockedScreenOffAnimationController> provider6 = DoubleCheck.provider(UnlockedScreenOffAnimationController_Factory.create(this.globalRootComponentImpl.contextProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardStateControllerImplProvider, this.dozeParametersProvider, this.globalSettingsImplProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.globalRootComponentImpl.providePowerManagerProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create()));
            this.unlockedScreenOffAnimationControllerProvider = provider6;
            this.screenOffAnimationControllerProvider = DoubleCheck.provider(ScreenOffAnimationController_Factory.create(this.provideSysUIUnfoldComponentProvider, provider6, this.wakefulnessLifecycleProvider));
            Provider<DozeParameters> provider7 = this.dozeParametersProvider;
            Provider access$2400 = this.globalRootComponentImpl.contextProvider;
            Provider<Handler> provider8 = this.provideBgHandlerProvider;
            Provider access$17400 = this.globalRootComponentImpl.provideResourcesProvider;
            Provider access$27500 = this.globalRootComponentImpl.provideAmbientDisplayConfigurationProvider;
            Provider<AlwaysOnDisplayPolicy> provider9 = this.alwaysOnDisplayPolicyProvider;
            Provider access$20600 = this.globalRootComponentImpl.providePowerManagerProvider;
            Provider<BatteryController> provider10 = this.provideBatteryControllerProvider;
            Provider<TunerServiceImpl> provider11 = this.tunerServiceImplProvider;
            Provider access$9900 = this.globalRootComponentImpl.dumpManagerProvider;
            Provider<FeatureFlagsRelease> provider12 = this.featureFlagsReleaseProvider;
            Provider<ScreenOffAnimationController> provider13 = this.screenOffAnimationControllerProvider;
            Provider<Optional<SysUIUnfoldComponent>> provider14 = this.provideSysUIUnfoldComponentProvider;
            Provider<UnlockedScreenOffAnimationController> provider15 = this.unlockedScreenOffAnimationControllerProvider;
            Provider<DozeParameters> provider16 = provider7;
            DelegateFactory.setDelegate(provider16, DoubleCheck.provider(DozeParameters_Factory.create(access$2400, provider8, access$17400, access$27500, provider9, access$20600, provider10, provider11, access$9900, provider12, provider13, provider14, provider15, this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider)));
            this.sysuiColorExtractorProvider = DoubleCheck.provider(SysuiColorExtractor_Factory.create(this.globalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.authControllerProvider = new DelegateFactory();
            this.notificationShadeWindowControllerImplProvider = DoubleCheck.provider(NotificationShadeWindowControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.newKeyguardViewMediatorProvider, this.keyguardBypassControllerProvider, this.sysuiColorExtractorProvider, this.globalRootComponentImpl.dumpManagerProvider, this.keyguardStateControllerImplProvider, this.screenOffAnimationControllerProvider, this.authControllerProvider));
            this.mediaArtworkProcessorProvider = DoubleCheck.provider(MediaArtworkProcessor_Factory.create());
            this.mediaControllerFactoryProvider = MediaControllerFactory_Factory.create(this.globalRootComponentImpl.contextProvider);
            Provider<LogBuffer> provider17 = DoubleCheck.provider(LogModule_ProvidesMediaTimeoutListenerLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providesMediaTimeoutListenerLogBufferProvider = provider17;
            this.mediaTimeoutLoggerProvider = DoubleCheck.provider(MediaTimeoutLogger_Factory.create(provider17));
            this.mediaTimeoutListenerProvider = DoubleCheck.provider(MediaTimeoutListener_Factory.create(this.mediaControllerFactoryProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaTimeoutLoggerProvider, this.statusBarStateControllerImplProvider, this.bindSystemClockProvider));
            this.mediaBrowserFactoryProvider = MediaBrowserFactory_Factory.create(this.globalRootComponentImpl.contextProvider);
            Provider<LogBuffer> provider18 = DoubleCheck.provider(LogModule_ProvideMediaBrowserBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaBrowserBufferProvider = provider18;
            this.resumeMediaBrowserLoggerProvider = DoubleCheck.provider(ResumeMediaBrowserLogger_Factory.create(provider18));
            this.resumeMediaBrowserFactoryProvider = ResumeMediaBrowserFactory_Factory.create(this.globalRootComponentImpl.contextProvider, this.mediaBrowserFactoryProvider, this.resumeMediaBrowserLoggerProvider);
            this.mediaResumeListenerProvider = DoubleCheck.provider(MediaResumeListener_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.tunerServiceImplProvider, this.resumeMediaBrowserFactoryProvider, this.globalRootComponentImpl.dumpManagerProvider, this.bindSystemClockProvider));
            this.mediaSessionBasedFilterProvider = MediaSessionBasedFilter_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMediaSessionManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider);
            this.provideLocalBluetoothControllerProvider = DoubleCheck.provider(SettingsLibraryModule_ProvideLocalBluetoothControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.provideBgHandlerProvider));
            this.localMediaManagerFactoryProvider = LocalMediaManagerFactory_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideLocalBluetoothControllerProvider);
            this.mediaFlagsProvider = DoubleCheck.provider(MediaFlags_Factory.create(this.featureFlagsReleaseProvider));
            Provider<LogBuffer> provider19 = DoubleCheck.provider(LogModule_ProvideMediaMuteAwaitLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaMuteAwaitLogBufferProvider = provider19;
            this.mediaMuteAwaitLoggerProvider = DoubleCheck.provider(MediaMuteAwaitLogger_Factory.create(provider19));
            this.mediaMuteAwaitConnectionManagerFactoryProvider = DoubleCheck.provider(MediaMuteAwaitConnectionManagerFactory_Factory.create(this.mediaFlagsProvider, this.globalRootComponentImpl.contextProvider, this.mediaMuteAwaitLoggerProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.mediaDeviceManagerProvider = MediaDeviceManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.mediaControllerFactoryProvider, this.localMediaManagerFactoryProvider, this.globalRootComponentImpl.provideMediaRouter2ManagerProvider, this.mediaMuteAwaitConnectionManagerFactoryProvider, this.configurationControllerImplProvider, this.provideLocalBluetoothControllerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.dumpManagerProvider);
            this.builderProvider2 = WakeLock_Builder_Factory.create(this.globalRootComponentImpl.contextProvider);
            this.broadcastSenderProvider = DoubleCheck.provider(BroadcastSender_Factory.create(this.globalRootComponentImpl.contextProvider, this.builderProvider2, this.provideBackgroundExecutorProvider));
            this.mediaUiEventLoggerProvider = DoubleCheck.provider(MediaUiEventLogger_Factory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.mediaDataFilterProvider = MediaDataFilter_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.notificationLockscreenUserManagerImplProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider);
            this.mediaDataManagerProvider = DoubleCheck.provider(MediaDataManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaControllerFactoryProvider, this.globalRootComponentImpl.dumpManagerProvider, this.broadcastDispatcherProvider, this.mediaTimeoutListenerProvider, this.mediaResumeListenerProvider, this.mediaSessionBasedFilterProvider, this.mediaDeviceManagerProvider, MediaDataCombineLatest_Factory.create(), this.mediaDataFilterProvider, this.provideActivityStarterProvider, SmartspaceMediaDataProvider_Factory.create(), this.bindSystemClockProvider, this.tunerServiceImplProvider, this.mediaFlagsProvider, this.mediaUiEventLoggerProvider));
            this.provideNotificationMediaManagerProvider = DoubleCheck.provider(C2633xbcfb28e4.create(this.globalRootComponentImpl.contextProvider, this.optionalOfCentralSurfacesProvider, this.notificationShadeWindowControllerImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.mediaArtworkProcessorProvider, this.keyguardBypassControllerProvider, this.notifPipelineProvider, this.notifCollectionProvider, this.notifPipelineFlagsProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.keyguardEnvironmentImplProvider = DoubleCheck.provider(KeyguardEnvironmentImpl_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.bindDeviceProvisionedControllerProvider));
            this.provideIndividualSensorPrivacyControllerProvider = DoubleCheck.provider(C2056x93d1e2b5.create(this.globalRootComponentImpl.provideSensorPrivacyManagerProvider));
            Provider<AppOpsControllerImpl> provider20 = DoubleCheck.provider(AppOpsControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideAudioManagerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.broadcastDispatcherProvider, this.bindSystemClockProvider));
            this.appOpsControllerImplProvider = provider20;
            Provider<ForegroundServiceController> provider21 = DoubleCheck.provider(ForegroundServiceController_Factory.create(provider20, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.foregroundServiceControllerProvider = provider21;
            this.notificationFilterProvider = DoubleCheck.provider(NotificationFilter_Factory.create(this.debugModeFilterProvider, this.statusBarStateControllerImplProvider, this.keyguardEnvironmentImplProvider, provider21, this.notificationLockscreenUserManagerImplProvider, this.mediaFeatureFlagProvider));
            this.notificationSectionsFeatureManagerProvider = DoubleCheck.provider(NotificationSectionsFeatureManager_Factory.create(this.deviceConfigProxyProvider, this.globalRootComponentImpl.contextProvider));
            Provider<HighPriorityProvider> provider22 = DoubleCheck.provider(HighPriorityProvider_Factory.create(this.peopleNotificationIdentifierImplProvider, this.provideGroupMembershipManagerProvider));
            this.highPriorityProvider = provider22;
            this.notificationRankingManagerProvider = NotificationRankingManager_Factory.create(this.provideNotificationMediaManagerProvider, this.notificationGroupManagerLegacyProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationFilterProvider, this.notificationEntryManagerLoggerProvider, this.notificationSectionsFeatureManagerProvider, this.peopleNotificationIdentifierImplProvider, provider22, this.keyguardEnvironmentImplProvider);
            this.targetSdkResolverProvider = DoubleCheck.provider(TargetSdkResolver_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.groupCoalescerLoggerProvider = GroupCoalescerLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.groupCoalescerProvider = GroupCoalescer_Factory.create(this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.bindSystemClockProvider, this.groupCoalescerLoggerProvider);
            C20483 r1 = new Provider<CoordinatorsSubcomponent.Factory>() {
                public CoordinatorsSubcomponent.Factory get() {
                    return new CoordinatorsSubcomponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.coordinatorsSubcomponentFactoryProvider = r1;
            this.notifCoordinatorsProvider = DoubleCheck.provider(CoordinatorsModule_NotifCoordinatorsFactory.create(r1));
            this.notifInflaterImplProvider = DoubleCheck.provider(NotifInflaterImpl_Factory.create(this.notifInflationErrorManagerProvider));
            this.mediaContainerControllerProvider = DoubleCheck.provider(MediaContainerController_Factory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider));
            this.sectionHeaderVisibilityProvider = DoubleCheck.provider(SectionHeaderVisibilityProvider_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.nodeSpecBuilderLoggerProvider = NodeSpecBuilderLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeViewDifferLoggerProvider = ShadeViewDifferLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.notifViewBarnProvider = DoubleCheck.provider(NotifViewBarn_Factory.create());
            ShadeViewManager_Factory create5 = ShadeViewManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.mediaContainerControllerProvider, this.notificationSectionsFeatureManagerProvider, this.sectionHeaderVisibilityProvider, this.nodeSpecBuilderLoggerProvider, this.shadeViewDifferLoggerProvider, this.notifViewBarnProvider);
            this.shadeViewManagerProvider = create5;
            this.shadeViewManagerFactoryProvider = ShadeViewManagerFactory_Impl.create(create5);
            this.notifPipelineInitializerProvider = DoubleCheck.provider(NotifPipelineInitializer_Factory.create(this.notifPipelineProvider, this.groupCoalescerProvider, this.notifCollectionProvider, this.shadeListBuilderProvider, this.renderStageManagerProvider, this.notifCoordinatorsProvider, this.notifInflaterImplProvider, this.globalRootComponentImpl.dumpManagerProvider, this.shadeViewManagerFactoryProvider, this.notifPipelineFlagsProvider));
            this.notifBindPipelineInitializerProvider = NotifBindPipelineInitializer_Factory.create(this.notifBindPipelineProvider, this.rowContentBindStageProvider);
            this.notificationGroupAlertTransferHelperProvider = DoubleCheck.provider(NotificationGroupAlertTransferHelper_Factory.create(this.rowContentBindStageProvider, this.statusBarStateControllerImplProvider, this.notificationGroupManagerLegacyProvider));
            this.headsUpViewBinderLoggerProvider = HeadsUpViewBinderLogger_Factory.create(this.provideNotificationHeadsUpLogBufferProvider);
            this.headsUpViewBinderProvider = DoubleCheck.provider(HeadsUpViewBinder_Factory.create(this.globalRootComponentImpl.provideNotificationMessagingUtilProvider, this.rowContentBindStageProvider, this.headsUpViewBinderLoggerProvider));
            this.notificationInterruptLoggerProvider = NotificationInterruptLogger_Factory.create(this.provideNotificationsLogBufferProvider, this.provideNotificationHeadsUpLogBufferProvider);
            this.keyguardNotificationVisibilityProviderImplProvider = DoubleCheck.provider(KeyguardNotificationVisibilityProviderImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.keyguardStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardUpdateMonitorProvider, this.highPriorityProvider, this.statusBarStateControllerImplProvider, this.broadcastDispatcherProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider));
            this.notificationInterruptStateProviderImplProvider = DoubleCheck.provider(NotificationInterruptStateProviderImpl_Factory.create(this.globalRootComponentImpl.provideContentResolverProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideIDreamManagerProvider, this.globalRootComponentImpl.provideAmbientDisplayConfigurationProvider, this.notificationFilterProvider, this.provideBatteryControllerProvider, this.statusBarStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationInterruptLoggerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.notifPipelineFlagsProvider, this.keyguardNotificationVisibilityProviderImplProvider));
            Provider<VisualStabilityManager> provider23 = DoubleCheck.provider(NotificationsModule_ProvideVisualStabilityManagerFactory.create(this.provideNotificationEntryManagerProvider, this.visualStabilityProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.statusBarStateControllerImplProvider, this.wakefulnessLifecycleProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.provideVisualStabilityManagerProvider = provider23;
            this.headsUpControllerProvider = DoubleCheck.provider(HeadsUpController_Factory.create(this.headsUpViewBinderProvider, this.notificationInterruptStateProviderImplProvider, this.provideHeadsUpManagerPhoneProvider, this.provideNotificationRemoteInputManagerProvider, this.statusBarStateControllerImplProvider, provider23, this.notificationListenerProvider));
        }

        private void initialize3(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            NotificationClickerLogger_Factory create = NotificationClickerLogger_Factory.create(this.provideNotifInteractionLogBufferProvider);
            this.notificationClickerLoggerProvider = create;
            this.builderProvider3 = NotificationClicker_Builder_Factory.create(create);
            this.animatedImageNotificationManagerProvider = DoubleCheck.provider(AnimatedImageNotificationManager_Factory.create(this.provideCommonNotifCollectionProvider, this.bindEventManagerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider));
            Provider<PeopleSpaceWidgetManager> provider = DoubleCheck.provider(PeopleSpaceWidgetManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideLauncherAppsProvider, this.provideCommonNotifCollectionProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.setBubblesProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.provideNotificationManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.peopleSpaceWidgetManagerProvider = provider;
            this.notificationsControllerImplProvider = DoubleCheck.provider(NotificationsControllerImpl_Factory.create(this.centralSurfacesImplProvider, this.notifPipelineFlagsProvider, this.notificationListenerProvider, this.provideNotificationEntryManagerProvider, this.debugModeFilterProvider, this.notificationRankingManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.notifLiveDataStoreImplProvider, this.targetSdkResolverProvider, this.notifPipelineInitializerProvider, this.notifBindPipelineInitializerProvider, this.bindDeviceProvisionedControllerProvider, this.notificationRowBinderImplProvider, this.bindEventManagerImplProvider, this.remoteInputUriControllerProvider, this.notificationGroupManagerLegacyProvider, this.notificationGroupAlertTransferHelperProvider, this.provideHeadsUpManagerPhoneProvider, this.headsUpControllerProvider, this.headsUpViewBinderProvider, this.builderProvider3, this.animatedImageNotificationManagerProvider, provider, this.setBubblesProvider));
            this.notificationsControllerStubProvider = NotificationsControllerStub_Factory.create(this.notificationListenerProvider);
            this.provideNotificationsControllerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationsControllerFactory.create(this.globalRootComponentImpl.contextProvider, this.notificationsControllerImplProvider, this.notificationsControllerStubProvider));
            C20494 r1 = new Provider<FragmentService.FragmentCreator.Factory>() {
                public FragmentService.FragmentCreator.Factory get() {
                    return new FragmentCreatorFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.fragmentCreatorFactoryProvider = r1;
            this.fragmentServiceProvider = DoubleCheck.provider(FragmentService_Factory.create(r1, this.configurationControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            C4840LightBarTransitionsController_Factory create2 = C4840LightBarTransitionsController_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider);
            this.lightBarTransitionsControllerProvider = create2;
            this.factoryProvider = LightBarTransitionsController_Factory_Impl.create(create2);
            this.darkIconDispatcherImplProvider = DoubleCheck.provider(DarkIconDispatcherImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.factoryProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.navigationModeControllerProvider = DoubleCheck.provider(NavigationModeController_Factory.create(this.globalRootComponentImpl.contextProvider, this.bindDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.lightBarControllerProvider = DoubleCheck.provider(C4839LightBarController_Factory.create(this.globalRootComponentImpl.contextProvider, this.darkIconDispatcherImplProvider, this.provideBatteryControllerProvider, this.navigationModeControllerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.autoHideControllerProvider = DoubleCheck.provider(C4838AutoHideController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider));
            this.providesStatusBarWindowViewProvider = DoubleCheck.provider(StatusBarWindowModule_ProvidesStatusBarWindowViewFactory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider));
            this.statusBarContentInsetsProvider = DoubleCheck.provider(StatusBarContentInsetsProvider_Factory.create(this.globalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.statusBarWindowControllerProvider = DoubleCheck.provider(StatusBarWindowController_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesStatusBarWindowViewProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.statusBarContentInsetsProvider, this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.unfoldTransitionProgressProvider));
            this.statusBarWindowStateControllerProvider = DoubleCheck.provider(StatusBarWindowStateController_Factory.create(this.globalRootComponentImpl.provideDisplayIdProvider, this.provideCommandQueueProvider));
            this.statusBarIconControllerImplProvider = DoubleCheck.provider(StatusBarIconControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.provideDemoModeControllerProvider, this.configurationControllerImplProvider, this.tunerServiceImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.carrierConfigTrackerProvider = DoubleCheck.provider(CarrierConfigTracker_Factory.create(this.globalRootComponentImpl.provideCarrierConfigManagerProvider, this.broadcastDispatcherProvider));
            this.callbackHandlerProvider = CallbackHandler_Factory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create());
            this.telephonyListenerManagerProvider = DoubleCheck.provider(TelephonyListenerManager_Factory.create(this.globalRootComponentImpl.provideTelephonyManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, TelephonyCallback_Factory.create()));
            this.wifiPickerTrackerFactoryProvider = DoubleCheck.provider(AccessPointControllerImpl_WifiPickerTrackerFactory_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideWifiManagerProvider, this.globalRootComponentImpl.provideConnectivityManagagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider));
            this.provideAccessPointControllerImplProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAccessPointControllerImplFactory.create(this.globalRootComponentImpl.provideUserManagerProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.wifiPickerTrackerFactoryProvider));
            this.wifiStatusTrackerFactoryProvider = WifiStatusTrackerFactory_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideWifiManagerProvider, this.globalRootComponentImpl.provideNetworkScoreManagerProvider, this.globalRootComponentImpl.provideConnectivityManagagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider);
            this.toastFactoryProvider = DoubleCheck.provider(ToastFactory_Factory.create(this.globalRootComponentImpl.providerLayoutInflaterProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.locationControllerImplProvider = DoubleCheck.provider(LocationControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.appOpsControllerImplProvider, this.deviceConfigProxyProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.bootCompleteCacheImplProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.secureSettingsImplProvider));
            this.provideDialogLaunchAnimatorProvider = DoubleCheck.provider(C2632x721ac0b6.create(this.globalRootComponentImpl.provideIDreamManagerProvider));
            Provider<DelayableExecutor> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideBackgroundDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.provideBackgroundDelayableExecutorProvider = provider2;
            this.wifiStateWorkerProvider = DoubleCheck.provider(WifiStateWorker_Factory.create(this.broadcastDispatcherProvider, provider2, this.globalRootComponentImpl.provideWifiManagerProvider));
            this.carrierNameCustomizationProvider = DoubleCheck.provider(CarrierNameCustomization_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.internetDialogControllerProvider = InternetDialogController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideActivityStarterProvider, this.provideAccessPointControllerImplProvider, this.globalRootComponentImpl.provideSubcriptionManagerProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.globalRootComponentImpl.provideWifiManagerProvider, this.globalRootComponentImpl.provideConnectivityManagagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.keyguardUpdateMonitorProvider, this.globalSettingsImplProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.toastFactoryProvider, this.provideBgHandlerProvider, this.carrierConfigTrackerProvider, this.locationControllerImplProvider, this.provideDialogLaunchAnimatorProvider, this.wifiStateWorkerProvider, this.carrierNameCustomizationProvider);
            this.internetDialogFactoryProvider = DoubleCheck.provider(InternetDialogFactory_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBackgroundExecutorProvider, this.internetDialogControllerProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.keyguardStateControllerImplProvider));
            this.networkControllerImplProvider = DoubleCheck.provider(NetworkControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBgLooperProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideSubcriptionManagerProvider, this.callbackHandlerProvider, this.bindDeviceProvisionedControllerProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.provideConnectivityManagagerProvider, this.globalRootComponentImpl.provideTelephonyManagerProvider, this.telephonyListenerManagerProvider, this.globalRootComponentImpl.provideWifiManagerProvider, this.provideAccessPointControllerImplProvider, this.provideDemoModeControllerProvider, this.carrierConfigTrackerProvider, this.wifiStatusTrackerFactoryProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.internetDialogFactoryProvider, this.featureFlagsReleaseProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.securityControllerImplProvider = DoubleCheck.provider(SecurityControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBgHandlerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.statusBarSignalPolicyProvider = DoubleCheck.provider(StatusBarSignalPolicy_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarIconControllerImplProvider, this.carrierConfigTrackerProvider, this.networkControllerImplProvider, this.securityControllerImplProvider, this.tunerServiceImplProvider, this.featureFlagsReleaseProvider));
            this.notificationWakeUpCoordinatorProvider = DoubleCheck.provider(NotificationWakeUpCoordinator_Factory.create(this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.screenOffAnimationControllerProvider));
            this.notificationRoundnessManagerProvider = DoubleCheck.provider(NotificationRoundnessManager_Factory.create(this.notificationSectionsFeatureManagerProvider));
            this.provideLSShadeTransitionControllerBufferProvider = DoubleCheck.provider(LogModule_ProvideLSShadeTransitionControllerBufferFactory.create(this.logBufferFactoryProvider));
            Provider<LockscreenGestureLogger> provider3 = DoubleCheck.provider(LockscreenGestureLogger_Factory.create(this.globalRootComponentImpl.provideMetricsLoggerProvider));
            this.lockscreenGestureLoggerProvider = provider3;
            this.lSShadeTransitionLoggerProvider = LSShadeTransitionLogger_Factory.create(this.provideLSShadeTransitionControllerBufferProvider, provider3, this.globalRootComponentImpl.provideDisplayMetricsProvider);
            this.mediaHostStatesManagerProvider = DoubleCheck.provider(MediaHostStatesManager_Factory.create());
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideMediaViewLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaViewLogBufferProvider = provider4;
            this.mediaViewLoggerProvider = DoubleCheck.provider(MediaViewLogger_Factory.create(provider4));
            this.mediaViewControllerProvider = MediaViewController_Factory.create(this.globalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.mediaHostStatesManagerProvider, this.mediaViewLoggerProvider);
            Provider<RepeatableExecutor> provider5 = DoubleCheck.provider(C3259xb8fd9db4.create(this.provideBackgroundDelayableExecutorProvider));
            this.provideBackgroundRepeatableExecutorProvider = provider5;
            this.seekBarViewModelProvider = SeekBarViewModel_Factory.create(provider5);
            Provider<LogBuffer> provider6 = DoubleCheck.provider(LogModule_ProvideNearbyMediaDevicesLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNearbyMediaDevicesLogBufferProvider = provider6;
            Provider<NearbyMediaDevicesLogger> provider7 = DoubleCheck.provider(NearbyMediaDevicesLogger_Factory.create(provider6));
            this.nearbyMediaDevicesLoggerProvider = provider7;
            Provider<NearbyMediaDevicesManager> provider8 = DoubleCheck.provider(NearbyMediaDevicesManager_Factory.create(this.provideCommandQueueProvider, provider7));
            this.nearbyMediaDevicesManagerProvider = provider8;
            this.providesNearbyMediaDevicesManagerProvider = DoubleCheck.provider(MediaModule_ProvidesNearbyMediaDevicesManagerFactory.create(this.mediaFlagsProvider, provider8));
            this.mediaOutputDialogFactoryProvider = MediaOutputDialogFactory_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.globalRootComponentImpl.provideAudioManagerProvider, this.globalRootComponentImpl.providePowerExemptionManagerProvider);
            this.mediaCarouselControllerProvider = new DelegateFactory();
            this.activityIntentHelperProvider = DoubleCheck.provider(ActivityIntentHelper_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.broadcastDialogControllerProvider = DoubleCheck.provider(BroadcastDialogController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.mediaOutputDialogFactoryProvider));
            this.mediaControlPanelProvider = MediaControlPanel_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.mediaViewControllerProvider, this.seekBarViewModelProvider, this.mediaDataManagerProvider, this.mediaOutputDialogFactoryProvider, this.mediaCarouselControllerProvider, this.falsingManagerProxyProvider, this.bindSystemClockProvider, this.mediaUiEventLoggerProvider, this.keyguardStateControllerImplProvider, this.activityIntentHelperProvider, this.notificationLockscreenUserManagerImplProvider, this.broadcastDialogControllerProvider);
            Provider<LogBuffer> provider9 = DoubleCheck.provider(LogModule_ProvideMediaCarouselControllerBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaCarouselControllerBufferProvider = provider9;
            this.mediaCarouselControllerLoggerProvider = DoubleCheck.provider(MediaCarouselControllerLogger_Factory.create(provider9));
            DelegateFactory.setDelegate(this.mediaCarouselControllerProvider, DoubleCheck.provider(MediaCarouselController_Factory.create(this.globalRootComponentImpl.contextProvider, this.mediaControlPanelProvider, this.visualStabilityProvider, this.mediaHostStatesManagerProvider, this.provideActivityStarterProvider, this.bindSystemClockProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.mediaDataManagerProvider, this.configurationControllerImplProvider, this.falsingCollectorImplProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.dumpManagerProvider, this.mediaUiEventLoggerProvider, this.mediaCarouselControllerLoggerProvider)));
            this.dreamOverlayStateControllerProvider = DoubleCheck.provider(DreamOverlayStateController_Factory.create(this.globalRootComponentImpl.provideMainExecutorProvider));
            this.mediaHierarchyManagerProvider = DoubleCheck.provider(MediaHierarchyManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardBypassControllerProvider, this.mediaCarouselControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.configurationControllerImplProvider, this.wakefulnessLifecycleProvider, this.statusBarKeyguardViewManagerProvider, this.dreamOverlayStateControllerProvider));
            Provider<MediaHost> provider10 = DoubleCheck.provider(MediaModule_ProvidesKeyguardMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesKeyguardMediaHostProvider = provider10;
            this.keyguardMediaControllerProvider = DoubleCheck.provider(KeyguardMediaController_Factory.create(provider10, this.keyguardBypassControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.globalRootComponentImpl.contextProvider, this.configurationControllerImplProvider));
            Provider<LogBuffer> provider11 = DoubleCheck.provider(LogModule_ProvideNotificationSectionLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideNotificationSectionLogBufferProvider = provider11;
            this.notificationSectionsLoggerProvider = DoubleCheck.provider(NotificationSectionsLogger_Factory.create(provider11));
            C20505 r12 = new Provider<SectionHeaderControllerSubcomponent.Builder>() {
                public SectionHeaderControllerSubcomponent.Builder get() {
                    return new SectionHeaderControllerSubcomponentBuilder(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.sectionHeaderControllerSubcomponentBuilderProvider = r12;
            Provider<SectionHeaderControllerSubcomponent> provider12 = DoubleCheck.provider(C2720xb614d321.create(r12));
            this.providesIncomingHeaderSubcomponentProvider = provider12;
            this.providesIncomingHeaderControllerProvider = C2718x340f4262.create(provider12);
            Provider<SectionHeaderControllerSubcomponent> provider13 = DoubleCheck.provider(C2723x39c1fe98.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesPeopleHeaderSubcomponentProvider = provider13;
            this.providesPeopleHeaderControllerProvider = C2721x812edf99.create(provider13);
            Provider<SectionHeaderControllerSubcomponent> provider14 = DoubleCheck.provider(C2717x3fd4641.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesAlertingHeaderSubcomponentProvider = provider14;
            this.providesAlertingHeaderControllerProvider = C2715x41b9fd82.create(provider14);
            Provider<SectionHeaderControllerSubcomponent> provider15 = DoubleCheck.provider(C2726x34a20792.create(this.sectionHeaderControllerSubcomponentBuilderProvider));
            this.providesSilentHeaderSubcomponentProvider = provider15;
            C2724xcc90df13 create3 = C2724xcc90df13.create(provider15);
            this.providesSilentHeaderControllerProvider = create3;
            this.notificationSectionsManagerProvider = NotificationSectionsManager_Factory.create(this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardMediaControllerProvider, this.notificationSectionsFeatureManagerProvider, this.notificationSectionsLoggerProvider, this.notifPipelineFlagsProvider, this.mediaContainerControllerProvider, this.providesIncomingHeaderControllerProvider, this.providesPeopleHeaderControllerProvider, this.providesAlertingHeaderControllerProvider, create3);
            this.ambientStateProvider = DoubleCheck.provider(AmbientState_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider, this.notificationSectionsManagerProvider, this.keyguardBypassControllerProvider, this.statusBarKeyguardViewManagerProvider));
            this.builderProvider4 = DelayedWakeLock_Builder_Factory.create(this.globalRootComponentImpl.contextProvider);
            Provider<LogBuffer> provider16 = DoubleCheck.provider(LogModule_ProvideDozeLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideDozeLogBufferProvider = provider16;
            this.dozeLoggerProvider = DozeLogger_Factory.create(provider16);
            Provider<DozeLog> provider17 = DoubleCheck.provider(DozeLog_Factory.create(this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.dumpManagerProvider, this.dozeLoggerProvider));
            this.dozeLogProvider = provider17;
            this.dozeScrimControllerProvider = DoubleCheck.provider(DozeScrimController_Factory.create(this.dozeParametersProvider, provider17, this.statusBarStateControllerImplProvider));
            this.scrimControllerProvider = new DelegateFactory();
            this.provideAssistUtilsProvider = DoubleCheck.provider(AssistModule_ProvideAssistUtilsFactory.create(this.globalRootComponentImpl.contextProvider));
            this.phoneStateMonitorProvider = DoubleCheck.provider(PhoneStateMonitor_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.bootCompleteCacheImplProvider, this.statusBarStateControllerImplProvider));
            this.overviewProxyServiceProvider = new DelegateFactory();
            this.provideSysUiStateProvider = DoubleCheck.provider(SystemUIModule_ProvideSysUiStateFactory.create(this.globalRootComponentImpl.dumpManagerProvider));
            this.accessibilityButtonModeObserverProvider = DoubleCheck.provider(AccessibilityButtonModeObserver_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.accessibilityButtonTargetsObserverProvider = DoubleCheck.provider(AccessibilityButtonTargetsObserver_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.contextComponentResolverProvider = new DelegateFactory();
            this.provideRecentsImplProvider = RecentsModule_ProvideRecentsImplFactory.create(this.globalRootComponentImpl.contextProvider, this.contextComponentResolverProvider);
            Provider<Recents> provider18 = DoubleCheck.provider(ReferenceSystemUIModule_ProvideRecentsFactory.create(this.globalRootComponentImpl.contextProvider, this.provideRecentsImplProvider, this.provideCommandQueueProvider));
            this.provideRecentsProvider = provider18;
            this.optionalOfRecentsProvider = PresentJdkOptionalInstanceProvider.m378of(provider18);
            this.systemActionsProvider = DoubleCheck.provider(SystemActions_Factory.create(this.globalRootComponentImpl.contextProvider, this.notificationShadeWindowControllerImplProvider, this.optionalOfCentralSurfacesProvider, this.optionalOfRecentsProvider));
            this.assistManagerProvider = new DelegateFactory();
            this.navBarHelperProvider = DoubleCheck.provider(NavBarHelper_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.systemActionsProvider, this.overviewProxyServiceProvider, this.assistManagerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.factoryProvider2 = EdgeBackGestureHandler_Factory_Factory.create(this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.broadcastDispatcherProvider, this.protoTracerProvider, this.navigationModeControllerProvider, this.globalRootComponentImpl.provideViewConfigurationProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider);
            this.taskbarDelegateProvider = DoubleCheck.provider(TaskbarDelegate_Factory.create(this.globalRootComponentImpl.contextProvider, this.factoryProvider2, this.factoryProvider));
            this.navigationBarComponentFactoryProvider = new Provider<NavigationBarComponent.Factory>() {
                public NavigationBarComponent.Factory get() {
                    return new NavigationBarComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.setPipProvider = InstanceFactory.create(optional);
        }

        private void initialize4(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.setBackAnimationProvider = InstanceFactory.create(optional16);
            this.navigationBarControllerProvider = DoubleCheck.provider(NavigationBarController_Factory.create(this.globalRootComponentImpl.contextProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.provideSysUiStateProvider, this.provideCommandQueueProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.configurationControllerImplProvider, this.navBarHelperProvider, this.taskbarDelegateProvider, this.navigationBarComponentFactoryProvider, this.statusBarKeyguardViewManagerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.autoHideControllerProvider, this.lightBarControllerProvider, this.setPipProvider, this.setBackAnimationProvider));
            this.setSplitScreenProvider = InstanceFactory.create(optional3);
            this.setOneHandedProvider = InstanceFactory.create(optional5);
            this.setRecentTasksProvider = InstanceFactory.create(optional13);
            this.setStartingSurfaceProvider = InstanceFactory.create(optional10);
            this.setTransitionsProvider = InstanceFactory.create(shellTransitions);
            this.keyguardUnlockAnimationControllerProvider = new DelegateFactory();
            DelegateFactory.setDelegate(this.overviewProxyServiceProvider, DoubleCheck.provider(OverviewProxyService_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.navigationBarControllerProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideSysUiStateProvider, this.setPipProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setRecentTasksProvider, this.setBackAnimationProvider, this.setStartingSurfaceProvider, this.broadcastDispatcherProvider, this.setTransitionsProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.keyguardUnlockAnimationControllerProvider, this.provideAssistUtilsProvider, this.globalRootComponentImpl.dumpManagerProvider)));
            this.assistLoggerProvider = DoubleCheck.provider(AssistLogger_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideAssistUtilsProvider, this.phoneStateMonitorProvider));
            this.defaultUiControllerProvider = DoubleCheck.provider(DefaultUiController_Factory.create(this.globalRootComponentImpl.contextProvider, this.assistLoggerProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.assistManagerProvider));
            DelegateFactory.setDelegate(this.assistManagerProvider, DoubleCheck.provider(AssistManager_Factory.create(this.bindDeviceProvisionedControllerProvider, this.globalRootComponentImpl.contextProvider, this.provideAssistUtilsProvider, this.provideCommandQueueProvider, this.phoneStateMonitorProvider, this.overviewProxyServiceProvider, this.provideSysUiStateProvider, this.defaultUiControllerProvider, this.assistLoggerProvider, this.globalRootComponentImpl.provideMainHandlerProvider)));
            this.shadeControllerImplProvider = DoubleCheck.provider(ShadeControllerImpl_Factory.create(this.provideCommandQueueProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.optionalOfCentralSurfacesProvider, this.assistManagerProvider));
            this.sessionTrackerProvider = DoubleCheck.provider(SessionTracker_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.authControllerProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.biometricUnlockControllerProvider = DoubleCheck.provider(BiometricUnlockController_Factory.create(this.dozeScrimControllerProvider, this.newKeyguardViewMediatorProvider, this.scrimControllerProvider, this.shadeControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideResourcesProvider, this.keyguardBypassControllerProvider, this.dozeParametersProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.provideNotificationMediaManagerProvider, this.wakefulnessLifecycleProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.authControllerProvider, this.statusBarStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.sessionTrackerProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider, this.screenOffAnimationControllerProvider));
            DelegateFactory.setDelegate(this.keyguardUnlockAnimationControllerProvider, DoubleCheck.provider(KeyguardUnlockAnimationController_Factory.create(this.globalRootComponentImpl.contextProvider, this.keyguardStateControllerImplProvider, this.newKeyguardViewMediatorProvider, this.statusBarKeyguardViewManagerProvider, this.featureFlagsReleaseProvider, this.biometricUnlockControllerProvider, this.statusBarStateControllerImplProvider, this.notificationShadeWindowControllerImplProvider)));
            DelegateFactory.setDelegate(this.scrimControllerProvider, DoubleCheck.provider(ScrimController_Factory.create(this.lightBarControllerProvider, this.dozeParametersProvider, this.globalRootComponentImpl.provideAlarmManagerProvider, this.keyguardStateControllerImplProvider, this.builderProvider4, GlobalConcurrencyModule_ProvideHandlerFactory.create(), this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.screenOffAnimationControllerProvider, this.panelExpansionStateManagerProvider, this.keyguardUnlockAnimationControllerProvider, this.statusBarKeyguardViewManagerProvider)));
            this.lockscreenShadeScrimTransitionControllerProvider = LockscreenShadeScrimTransitionController_Factory.create(this.scrimControllerProvider, this.globalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider);
            C4835LockscreenShadeKeyguardTransitionController_Factory create = C4835LockscreenShadeKeyguardTransitionController_Factory.create(this.mediaHierarchyManagerProvider, this.globalRootComponentImpl.contextProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider);
            this.lockscreenShadeKeyguardTransitionControllerProvider = create;
            this.factoryProvider3 = LockscreenShadeKeyguardTransitionController_Factory_Impl.create(create);
            this.blurUtilsProvider = DoubleCheck.provider(BlurUtils_Factory.create(this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideCrossWindowBlurListenersProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.wallpaperControllerProvider = DoubleCheck.provider(WallpaperController_Factory.create(this.globalRootComponentImpl.provideWallpaperManagerProvider));
            this.notificationShadeDepthControllerProvider = DoubleCheck.provider(NotificationShadeDepthController_Factory.create(this.statusBarStateControllerImplProvider, this.blurUtilsProvider, this.biometricUnlockControllerProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.providesChoreographerProvider, this.wallpaperControllerProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider, this.configurationControllerImplProvider));
            C4837SplitShadeLockScreenOverScroller_Factory create2 = C4837SplitShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.globalRootComponentImpl.contextProvider, this.scrimControllerProvider, this.statusBarStateControllerImplProvider);
            this.splitShadeLockScreenOverScrollerProvider = create2;
            this.factoryProvider4 = SplitShadeLockScreenOverScroller_Factory_Impl.create(create2);
            C4836SingleShadeLockScreenOverScroller_Factory create3 = C4836SingleShadeLockScreenOverScroller_Factory.create(this.configurationControllerImplProvider, this.globalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider);
            this.singleShadeLockScreenOverScrollerProvider = create3;
            this.factoryProvider5 = SingleShadeLockScreenOverScroller_Factory_Impl.create(create3);
            this.lockscreenShadeTransitionControllerProvider = DoubleCheck.provider(LockscreenShadeTransitionController_Factory.create(this.statusBarStateControllerImplProvider, this.lSShadeTransitionLoggerProvider, this.keyguardBypassControllerProvider, this.notificationLockscreenUserManagerImplProvider, this.falsingCollectorImplProvider, this.ambientStateProvider, this.mediaHierarchyManagerProvider, this.lockscreenShadeScrimTransitionControllerProvider, this.factoryProvider3, this.notificationShadeDepthControllerProvider, this.globalRootComponentImpl.contextProvider, this.factoryProvider4, this.factoryProvider5, this.wakefulnessLifecycleProvider, this.configurationControllerImplProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.pulseExpansionHandlerProvider = DoubleCheck.provider(PulseExpansionHandler_Factory.create(this.globalRootComponentImpl.contextProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.notificationRoundnessManagerProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.falsingManagerProxyProvider, this.lockscreenShadeTransitionControllerProvider, this.falsingCollectorImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.dynamicPrivacyControllerProvider = DoubleCheck.provider(DynamicPrivacyController_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider));
            this.shadeEventCoordinatorLoggerProvider = ShadeEventCoordinatorLogger_Factory.create(this.provideNotificationsLogBufferProvider);
            this.shadeEventCoordinatorProvider = DoubleCheck.provider(ShadeEventCoordinator_Factory.create(this.globalRootComponentImpl.provideMainExecutorProvider, this.shadeEventCoordinatorLoggerProvider));
            LegacyNotificationPresenterExtensions_Factory create4 = LegacyNotificationPresenterExtensions_Factory.create(this.provideNotificationEntryManagerProvider);
            this.legacyNotificationPresenterExtensionsProvider = create4;
            this.provideNotifShadeEventSourceProvider = DoubleCheck.provider(NotificationsModule_ProvideNotifShadeEventSourceFactory.create(this.notifPipelineFlagsProvider, this.shadeEventCoordinatorProvider, create4));
            this.channelEditorDialogControllerProvider = DoubleCheck.provider(ChannelEditorDialogController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideINotificationManagerProvider, ChannelEditorDialog_Builder_Factory.create()));
            this.assistantFeedbackControllerProvider = DoubleCheck.provider(AssistantFeedbackController_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.contextProvider, this.deviceConfigProxyProvider));
            this.zenModeControllerImplProvider = DoubleCheck.provider(ZenModeControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalSettingsImplProvider));
            this.provideBubblesManagerProvider = DoubleCheck.provider(SystemUIModule_ProvideBubblesManagerFactory.create(this.globalRootComponentImpl.contextProvider, this.setBubblesProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.shadeControllerImplProvider, this.configurationControllerImplProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.globalRootComponentImpl.provideINotificationManagerProvider, this.provideNotificationVisibilityProvider, this.notificationInterruptStateProviderImplProvider, this.zenModeControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideNotificationEntryManagerProvider, this.provideCommonNotifCollectionProvider, this.notifPipelineProvider, this.provideSysUiStateProvider, this.notifPipelineFlagsProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.provideDelayableExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideDelayableExecutorFactory.create(this.provideBgLooperProvider));
            this.panelEventsEmitterProvider = DoubleCheck.provider(NotificationPanelViewController_PanelEventsEmitter_Factory.create());
            Provider<VisualStabilityCoordinator> provider = DoubleCheck.provider(VisualStabilityCoordinator_Factory.create(this.provideDelayableExecutorProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideHeadsUpManagerPhoneProvider, this.panelEventsEmitterProvider, this.statusBarStateControllerImplProvider, this.visualStabilityProvider, this.wakefulnessLifecycleProvider));
            this.visualStabilityCoordinatorProvider = provider;
            this.provideOnUserInteractionCallbackProvider = DoubleCheck.provider(NotificationsModule_ProvideOnUserInteractionCallbackFactory.create(this.notifPipelineFlagsProvider, this.provideHeadsUpManagerPhoneProvider, this.statusBarStateControllerImplProvider, this.notifCollectionProvider, this.provideNotificationVisibilityProvider, provider, this.provideNotificationEntryManagerProvider, this.provideVisualStabilityManagerProvider, this.provideGroupMembershipManagerProvider));
            this.provideNotificationGutsManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationGutsManagerFactory.create(this.globalRootComponentImpl.contextProvider, this.optionalOfCentralSurfacesProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.highPriorityProvider, this.globalRootComponentImpl.provideINotificationManagerProvider, this.provideNotificationEntryManagerProvider, this.peopleSpaceWidgetManagerProvider, this.globalRootComponentImpl.provideLauncherAppsProvider, this.globalRootComponentImpl.provideShortcutManagerProvider, this.channelEditorDialogControllerProvider, this.provideUserTrackerProvider, this.assistantFeedbackControllerProvider, this.provideBubblesManagerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideOnUserInteractionCallbackProvider, this.shadeControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.expansionStateLoggerProvider = NotificationLogger_ExpansionStateLogger_Factory.create(this.globalRootComponentImpl.provideUiBackgroundExecutorProvider);
            this.provideNotificationPanelLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationPanelLoggerFactory.create());
            this.provideNotificationLoggerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotificationLoggerFactory.create(this.notificationListenerProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.notifPipelineFlagsProvider, this.notifLiveDataStoreImplProvider, this.provideNotificationVisibilityProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.statusBarStateControllerImplProvider, this.expansionStateLoggerProvider, this.provideNotificationPanelLoggerProvider));
            this.dynamicChildBindControllerProvider = DynamicChildBindController_Factory.create(this.rowContentBindStageProvider);
            this.provideNotificationViewHierarchyManagerProvider = DoubleCheck.provider(C2635x5356ea10.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.featureFlagsReleaseProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.statusBarStateControllerImplProvider, this.provideNotificationEntryManagerProvider, this.keyguardBypassControllerProvider, this.setBubblesProvider, this.dynamicPrivacyControllerProvider, this.dynamicChildBindControllerProvider, this.lowPriorityInflationHelperProvider, this.assistantFeedbackControllerProvider, this.notifPipelineFlagsProvider, this.keyguardUpdateMonitorProvider, this.keyguardStateControllerImplProvider));
            this.userSwitcherControllerProvider = new DelegateFactory();
            this.accessibilityFloatingMenuControllerProvider = DoubleCheck.provider(AccessibilityFloatingMenuController_Factory.create(this.globalRootComponentImpl.contextProvider, this.accessibilityButtonTargetsObserverProvider, this.accessibilityButtonModeObserverProvider, this.keyguardUpdateMonitorProvider));
            this.lockscreenWallpaperProvider = DoubleCheck.provider(LockscreenWallpaper_Factory.create(this.globalRootComponentImpl.provideWallpaperManagerProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideNotificationMediaManagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.notificationIconAreaControllerProvider = DoubleCheck.provider(NotificationIconAreaController_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.provideNotificationMediaManagerProvider, this.notificationListenerProvider, this.dozeParametersProvider, this.setBubblesProvider, this.provideDemoModeControllerProvider, this.darkIconDispatcherImplProvider, this.statusBarWindowControllerProvider, this.screenOffAnimationControllerProvider));
            this.dozeServiceHostProvider = DoubleCheck.provider(DozeServiceHost_Factory.create(this.dozeLogProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.provideBatteryControllerProvider, this.scrimControllerProvider, this.biometricUnlockControllerProvider, this.newKeyguardViewMediatorProvider, this.assistManagerProvider, this.dozeScrimControllerProvider, this.keyguardUpdateMonitorProvider, this.pulseExpansionHandlerProvider, this.provideSysUIUnfoldComponentProvider, this.notificationShadeWindowControllerImplProvider, this.notificationWakeUpCoordinatorProvider, this.authControllerProvider, this.notificationIconAreaControllerProvider));
            this.screenPinningRequestProvider = ScreenPinningRequest_Factory.create(this.globalRootComponentImpl.contextProvider, this.optionalOfCentralSurfacesProvider, this.navigationModeControllerProvider, this.broadcastDispatcherProvider);
            this.ringerModeTrackerImplProvider = DoubleCheck.provider(RingerModeTrackerImpl_Factory.create(this.globalRootComponentImpl.provideAudioManagerProvider, this.broadcastDispatcherProvider, this.provideBackgroundExecutorProvider));
            this.vibratorHelperProvider = DoubleCheck.provider(VibratorHelper_Factory.create(this.globalRootComponentImpl.provideVibratorProvider, this.provideBackgroundExecutorProvider));
            this.volumeDialogControllerImplProvider = DoubleCheck.provider(VolumeDialogControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.ringerModeTrackerImplProvider, ThreadFactoryImpl_Factory.create(), this.globalRootComponentImpl.provideAudioManagerProvider, this.globalRootComponentImpl.provideNotificationManagerProvider, this.vibratorHelperProvider, this.globalRootComponentImpl.provideIAudioServiceProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.wakefulnessLifecycleProvider, this.globalRootComponentImpl.provideCaptioningManagerProvider, this.globalRootComponentImpl.provideKeyguardManagerProvider, this.globalRootComponentImpl.provideActivityManagerProvider));
            this.accessibilityManagerWrapperProvider = DoubleCheck.provider(AccessibilityManagerWrapper_Factory.create(this.globalRootComponentImpl.provideAccessibilityManagerProvider));
            this.provideVolumeDialogProvider = VolumeModule_ProvideVolumeDialogFactory.create(this.globalRootComponentImpl.contextProvider, this.volumeDialogControllerImplProvider, this.accessibilityManagerWrapperProvider, this.bindDeviceProvisionedControllerProvider, this.configurationControllerImplProvider, this.mediaOutputDialogFactoryProvider, this.provideActivityStarterProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider);
            this.volumeDialogComponentProvider = DoubleCheck.provider(VolumeDialogComponent_Factory.create(this.globalRootComponentImpl.contextProvider, this.newKeyguardViewMediatorProvider, this.provideActivityStarterProvider, this.volumeDialogControllerImplProvider, this.provideDemoModeControllerProvider, this.globalRootComponentImpl.pluginDependencyProvider, this.extensionControllerImplProvider, this.tunerServiceImplProvider, this.provideVolumeDialogProvider));
            this.centralSurfacesComponentFactoryProvider = new Provider<CentralSurfacesComponent.Factory>() {
                public CentralSurfacesComponent.Factory get() {
                    return new CentralSurfacesComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.providesViewMediatorCallbackProvider = new DelegateFactory();
            this.initControllerProvider = DoubleCheck.provider(InitController_Factory.create());
            this.provideTimeTickHandlerProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideTimeTickHandlerFactory.create());
            this.userInfoControllerImplProvider = DoubleCheck.provider(UserInfoControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.castControllerImplProvider = DoubleCheck.provider(CastControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.hotspotControllerImplProvider = DoubleCheck.provider(HotspotControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBgHandlerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.bluetoothControllerImplProvider = DoubleCheck.provider(BluetoothControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideBgLooperProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.provideLocalBluetoothControllerProvider));
            this.nextAlarmControllerImplProvider = DoubleCheck.provider(NextAlarmControllerImpl_Factory.create(this.globalRootComponentImpl.provideAlarmManagerProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.dumpManagerProvider));
            RotationPolicyWrapperImpl_Factory create5 = RotationPolicyWrapperImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.secureSettingsImplProvider);
            this.rotationPolicyWrapperImplProvider = create5;
            this.bindRotationPolicyWrapperProvider = DoubleCheck.provider(create5);
            this.provideAutoRotateSettingsManagerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideAutoRotateSettingsManagerFactory.create(this.globalRootComponentImpl.contextProvider));
            this.deviceStateRotationLockSettingControllerProvider = DoubleCheck.provider(DeviceStateRotationLockSettingController_Factory.create(this.bindRotationPolicyWrapperProvider, this.globalRootComponentImpl.provideDeviceStateManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideAutoRotateSettingsManagerProvider));
            C3215xe335f1e6 create6 = C3215xe335f1e6.create(this.globalRootComponentImpl.provideResourcesProvider);
            this.providesDeviceStateRotationLockDefaultsProvider = create6;
            this.rotationLockControllerImplProvider = DoubleCheck.provider(RotationLockControllerImpl_Factory.create(this.bindRotationPolicyWrapperProvider, this.deviceStateRotationLockSettingControllerProvider, create6));
            this.provideDataSaverControllerProvider = DoubleCheck.provider(StatusBarPolicyModule_ProvideDataSaverControllerFactory.create(this.networkControllerImplProvider));
            this.provideSensorPrivacyControllerProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideSensorPrivacyControllerFactory.create(this.globalRootComponentImpl.provideSensorPrivacyManagerProvider));
            this.recordingControllerProvider = DoubleCheck.provider(RecordingController_Factory.create(this.broadcastDispatcherProvider, this.provideUserTrackerProvider));
            this.dateFormatUtilProvider = DateFormatUtil_Factory.create(this.globalRootComponentImpl.contextProvider);
            this.privacyConfigProvider = DoubleCheck.provider(PrivacyConfig_Factory.create(this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.deviceConfigProxyProvider, this.globalRootComponentImpl.dumpManagerProvider));
            Provider<LogBuffer> provider2 = DoubleCheck.provider(LogModule_ProvidePrivacyLogBufferFactory.create(this.logBufferFactoryProvider));
            this.providePrivacyLogBufferProvider = provider2;
            PrivacyLogger_Factory create7 = PrivacyLogger_Factory.create(provider2);
            this.privacyLoggerProvider = create7;
            this.appOpsPrivacyItemMonitorProvider = DoubleCheck.provider(AppOpsPrivacyItemMonitor_Factory.create(this.appOpsControllerImplProvider, this.provideUserTrackerProvider, this.privacyConfigProvider, this.provideBackgroundDelayableExecutorProvider, create7));
            this.setOfPrivacyItemMonitorProvider = SetFactory.builder(1, 0).addProvider(this.appOpsPrivacyItemMonitorProvider).build();
            this.privacyItemControllerProvider = DoubleCheck.provider(PrivacyItemController_Factory.create(this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.privacyConfigProvider, this.setOfPrivacyItemMonitorProvider, this.privacyLoggerProvider, this.bindSystemClockProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.phoneStatusBarPolicyProvider = PhoneStatusBarPolicy_Factory.create(this.statusBarIconControllerImplProvider, this.provideCommandQueueProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.globalRootComponentImpl.provideResourcesProvider, this.castControllerImplProvider, this.hotspotControllerImplProvider, this.bluetoothControllerImplProvider, this.nextAlarmControllerImplProvider, this.userInfoControllerImplProvider, this.rotationLockControllerImplProvider, this.provideDataSaverControllerProvider, this.zenModeControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.keyguardStateControllerImplProvider, this.locationControllerImplProvider, this.provideSensorPrivacyControllerProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.globalRootComponentImpl.provideAlarmManagerProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.provideDevicePolicyManagerProvider, this.recordingControllerProvider, this.globalRootComponentImpl.provideTelecomManagerProvider, this.globalRootComponentImpl.provideDisplayIdProvider, this.globalRootComponentImpl.provideSharePreferencesProvider, this.dateFormatUtilProvider, this.ringerModeTrackerImplProvider, this.privacyItemControllerProvider, this.privacyLoggerProvider);
            this.keyguardIndicationControllerProvider = DoubleCheck.provider(KeyguardIndicationController_Factory.create(this.globalRootComponentImpl.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.builderProvider2, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dockManagerImplProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.provideDevicePolicyManagerProvider, this.globalRootComponentImpl.provideIBatteryStatsProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.keyguardBypassControllerProvider));
            this.statusBarTouchableRegionManagerProvider = DoubleCheck.provider(StatusBarTouchableRegionManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.notificationShadeWindowControllerImplProvider, this.configurationControllerImplProvider, this.provideHeadsUpManagerPhoneProvider));
            this.factoryProvider6 = new DelegateFactory();
            this.ongoingCallLoggerProvider = DoubleCheck.provider(OngoingCallLogger_Factory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider));
            Provider<LogBuffer> provider3 = DoubleCheck.provider(LogModule_ProvideSwipeAwayGestureLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideSwipeAwayGestureLogBufferProvider = provider3;
            this.swipeStatusBarAwayGestureLoggerProvider = SwipeStatusBarAwayGestureLogger_Factory.create(provider3);
            this.swipeStatusBarAwayGestureHandlerProvider = DoubleCheck.provider(SwipeStatusBarAwayGestureHandler_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarWindowControllerProvider, this.swipeStatusBarAwayGestureLoggerProvider));
            this.ongoingCallFlagsProvider = DoubleCheck.provider(OngoingCallFlags_Factory.create(this.featureFlagsReleaseProvider));
            this.provideOngoingCallControllerProvider = DoubleCheck.provider(C2636xca2d86e7.create(this.globalRootComponentImpl.contextProvider, this.provideCommonNotifCollectionProvider, this.bindSystemClockProvider, this.provideActivityStarterProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.ongoingCallLoggerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.statusBarWindowControllerProvider, this.swipeStatusBarAwayGestureHandlerProvider, this.statusBarStateControllerImplProvider, this.ongoingCallFlagsProvider));
            this.statusBarHideIconsForBouncerManagerProvider = DoubleCheck.provider(StatusBarHideIconsForBouncerManager_Factory.create(this.provideCommandQueueProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.statusBarWindowStateControllerProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.providesMainMessageRouterProvider = SysUIConcurrencyModule_ProvidesMainMessageRouterFactory.create(this.globalRootComponentImpl.provideMainDelayableExecutorProvider);
            this.provideActivityLaunchAnimatorProvider = DoubleCheck.provider(C2631x512569cf.create());
            this.wiredChargingRippleControllerProvider = DoubleCheck.provider(WiredChargingRippleController_Factory.create(this.commandRegistryProvider, this.provideBatteryControllerProvider, this.configurationControllerImplProvider, this.featureFlagsReleaseProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.bindSystemClockProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            DelegateFactory.setDelegate(this.centralSurfacesImplProvider, DoubleCheck.provider(CentralSurfacesImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideNotificationsControllerProvider, this.fragmentServiceProvider, this.lightBarControllerProvider, this.autoHideControllerProvider, this.statusBarWindowControllerProvider, this.statusBarWindowStateControllerProvider, this.keyguardUpdateMonitorProvider, this.statusBarSignalPolicyProvider, this.pulseExpansionHandlerProvider, this.notificationWakeUpCoordinatorProvider, this.keyguardBypassControllerProvider, this.keyguardStateControllerImplProvider, this.provideHeadsUpManagerPhoneProvider, this.dynamicPrivacyControllerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.broadcastDispatcherProvider, this.provideNotifShadeEventSourceProvider, this.provideNotificationEntryManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationLoggerProvider, this.notificationInterruptStateProviderImplProvider, this.provideNotificationViewHierarchyManagerProvider, this.panelExpansionStateManagerProvider, this.newKeyguardViewMediatorProvider, this.globalRootComponentImpl.provideDisplayMetricsProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.provideNotificationMediaManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.provideNotificationRemoteInputManagerProvider, this.userSwitcherControllerProvider, this.networkControllerImplProvider, this.provideBatteryControllerProvider, this.sysuiColorExtractorProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.statusBarStateControllerImplProvider, this.provideBubblesManagerProvider, this.setBubblesProvider, this.provideVisualStabilityManagerProvider, this.bindDeviceProvisionedControllerProvider, this.navigationBarControllerProvider, this.accessibilityFloatingMenuControllerProvider, this.assistManagerProvider, this.configurationControllerImplProvider, this.notificationShadeWindowControllerImplProvider, this.dozeParametersProvider, this.scrimControllerProvider, this.lockscreenWallpaperProvider, this.lockscreenGestureLoggerProvider, this.biometricUnlockControllerProvider, this.dozeServiceHostProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.screenPinningRequestProvider, this.dozeScrimControllerProvider, this.volumeDialogComponentProvider, this.provideCommandQueueProvider, this.centralSurfacesComponentFactoryProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.shadeControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.providesViewMediatorCallbackProvider, this.initControllerProvider, this.provideTimeTickHandlerProvider, this.globalRootComponentImpl.pluginDependencyProvider, this.keyguardDismissUtilProvider, this.extensionControllerImplProvider, this.userInfoControllerImplProvider, this.phoneStatusBarPolicyProvider, this.keyguardIndicationControllerProvider, this.provideDemoModeControllerProvider, this.notificationShadeDepthControllerProvider, this.statusBarTouchableRegionManagerProvider, this.notificationIconAreaControllerProvider, this.factoryProvider6, this.screenOffAnimationControllerProvider, this.wallpaperControllerProvider, this.provideOngoingCallControllerProvider, this.statusBarHideIconsForBouncerManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.featureFlagsReleaseProvider, this.keyguardUnlockAnimationControllerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.providesMainMessageRouterProvider, this.globalRootComponentImpl.provideWallpaperManagerProvider, this.setStartingSurfaceProvider, this.provideActivityLaunchAnimatorProvider, this.notifPipelineFlagsProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.globalRootComponentImpl.provideDeviceStateManagerProvider, this.dreamOverlayStateControllerProvider, this.wiredChargingRippleControllerProvider, this.globalRootComponentImpl.provideIDreamManagerProvider)));
        }

        private void initialize5(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            DelegateFactory.setDelegate(this.optionalOfCentralSurfacesProvider, PresentJdkOptionalInstanceProvider.m378of(this.centralSurfacesImplProvider));
            Provider<ActivityStarterDelegate> provider = DoubleCheck.provider(ActivityStarterDelegate_Factory.create(this.optionalOfCentralSurfacesProvider));
            this.activityStarterDelegateProvider = provider;
            DelegateFactory.setDelegate(this.provideActivityStarterProvider, PluginModule_ProvideActivityStarterFactory.create(provider, this.globalRootComponentImpl.pluginDependencyProvider));
            Provider<Looper> provider2 = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningLooperFactory.create());
            this.provideLongRunningLooperProvider = provider2;
            this.provideLongRunningExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideLongRunningExecutorFactory.create(provider2));
            DelegateFactory.setDelegate(this.userSwitcherControllerProvider, DoubleCheck.provider(UserSwitcherController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.globalRootComponentImpl.provideDevicePolicyManagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.falsingManagerProxyProvider, this.telephonyListenerManagerProvider, this.secureSettingsImplProvider, this.globalSettingsImplProvider, this.provideBackgroundExecutorProvider, this.provideLongRunningExecutorProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideDialogLaunchAnimatorProvider)));
            this.keyguardStatusViewComponentFactoryProvider = new Provider<KeyguardStatusViewComponent.Factory>() {
                public KeyguardStatusViewComponent.Factory get() {
                    return new KeyguardStatusViewComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.keyguardDisplayManagerProvider = KeyguardDisplayManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.navigationBarControllerProvider, this.keyguardStatusViewComponentFactoryProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider);
            this.screenOnCoordinatorProvider = DoubleCheck.provider(ScreenOnCoordinator_Factory.create(this.globalRootComponentImpl.screenLifecycleProvider, this.provideSysUIUnfoldComponentProvider, this.globalRootComponentImpl.provideExecutionProvider));
            DelegateFactory.setDelegate(this.newKeyguardViewMediatorProvider, DoubleCheck.provider(KeyguardModule_NewKeyguardViewMediatorFactory.create(this.globalRootComponentImpl.contextProvider, this.falsingCollectorImplProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.statusBarKeyguardViewManagerProvider, this.dismissCallbackRegistryProvider, this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideTrustManagerProvider, this.userSwitcherControllerProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.deviceConfigProxyProvider, this.navigationModeControllerProvider, this.keyguardDisplayManagerProvider, this.dozeParametersProvider, this.statusBarStateControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUnlockAnimationControllerProvider, this.screenOffAnimationControllerProvider, this.notificationShadeDepthControllerProvider, this.screenOnCoordinatorProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.dreamOverlayStateControllerProvider, this.notificationShadeWindowControllerImplProvider, this.provideActivityLaunchAnimatorProvider)));
            DelegateFactory.setDelegate(this.providesViewMediatorCallbackProvider, KeyguardModule_ProvidesViewMediatorCallbackFactory.create(keyguardModule, this.newKeyguardViewMediatorProvider));
            this.keyguardSecurityModelProvider = DoubleCheck.provider(KeyguardSecurityModel_Factory.create(this.globalRootComponentImpl.provideResourcesProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.keyguardUpdateMonitorProvider));
            this.keyguardBouncerComponentFactoryProvider = new Provider<KeyguardBouncerComponent.Factory>() {
                public KeyguardBouncerComponent.Factory get() {
                    return new KeyguardBouncerComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.factoryProvider7 = KeyguardBouncer_Factory_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesViewMediatorCallbackProvider, this.dismissCallbackRegistryProvider, this.falsingCollectorImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.keyguardBypassControllerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.keyguardSecurityModelProvider, this.keyguardBouncerComponentFactoryProvider);
            this.factoryProvider8 = KeyguardMessageAreaController_Factory_Factory.create(this.keyguardUpdateMonitorProvider, this.configurationControllerImplProvider);
            DelegateFactory.setDelegate(this.statusBarKeyguardViewManagerProvider, DoubleCheck.provider(StatusBarKeyguardViewManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesViewMediatorCallbackProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.keyguardUpdateMonitorProvider, this.dreamOverlayStateControllerProvider, this.navigationModeControllerProvider, this.dockManagerImplProvider, this.notificationShadeWindowControllerImplProvider, this.keyguardStateControllerImplProvider, this.provideNotificationMediaManagerProvider, this.factoryProvider7, this.factoryProvider8, this.provideSysUIUnfoldComponentProvider, this.shadeControllerImplProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider)));
            this.udfpsHapticsSimulatorProvider = DoubleCheck.provider(UdfpsHapticsSimulator_Factory.create(this.commandRegistryProvider, this.vibratorHelperProvider, this.keyguardUpdateMonitorProvider));
            this.udfpsShellProvider = DoubleCheck.provider(UdfpsShell_Factory.create(this.commandRegistryProvider));
            this.optionalOfUdfpsHbmProvider = PresentJdkOptionalInstanceProvider.m378of(NTUdfpsHbmModule_OptionalUdfpsHbmProviderFactory.create());
            this.systemUIDialogManagerProvider = DoubleCheck.provider(SystemUIDialogManager_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.statusBarKeyguardViewManagerProvider));
            this.optionalOfAlternateUdfpsTouchProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.providesPluginExecutorProvider = DoubleCheck.provider(BiometricsModule_ProvidesPluginExecutorFactory.create(ThreadFactoryImpl_Factory.create()));
            this.udfpsControllerProvider = DoubleCheck.provider(UdfpsController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideExecutionProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.globalRootComponentImpl.providesFingerprintManagerProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.statusBarStateControllerImplProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.panelExpansionStateManagerProvider, this.statusBarKeyguardViewManagerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.keyguardUpdateMonitorProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.lockscreenShadeTransitionControllerProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.vibratorHelperProvider, this.udfpsHapticsSimulatorProvider, this.udfpsShellProvider, this.optionalOfUdfpsHbmProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.provideDisplayManagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.configurationControllerImplProvider, this.bindSystemClockProvider, this.unlockedScreenOffAnimationControllerProvider, this.systemUIDialogManagerProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider, this.provideActivityLaunchAnimatorProvider, this.optionalOfAlternateUdfpsTouchProvider, this.providesPluginExecutorProvider));
            this.sidefpsControllerProvider = DoubleCheck.provider(SidefpsController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.globalRootComponentImpl.providesFingerprintManagerProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.globalRootComponentImpl.provideActivityTaskManagerProvider, this.overviewProxyServiceProvider, this.globalRootComponentImpl.provideDisplayManagerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.globalRootComponentImpl.provideMainHandlerProvider));
            Provider<AuthController> provider3 = this.authControllerProvider;
            Provider access$2400 = this.globalRootComponentImpl.contextProvider;
            Provider access$31100 = this.globalRootComponentImpl.provideExecutionProvider;
            Provider<CommandQueue> provider4 = this.provideCommandQueueProvider;
            Provider access$20700 = this.globalRootComponentImpl.provideActivityTaskManagerProvider;
            Provider access$2800 = this.globalRootComponentImpl.provideWindowManagerProvider;
            Provider access$34800 = this.globalRootComponentImpl.providesFingerprintManagerProvider;
            Provider access$34900 = this.globalRootComponentImpl.provideFaceManagerProvider;
            Provider<UdfpsController> provider5 = this.udfpsControllerProvider;
            Provider<SidefpsController> provider6 = this.sidefpsControllerProvider;
            Provider access$7300 = this.globalRootComponentImpl.provideDisplayManagerProvider;
            Provider<WakefulnessLifecycle> provider7 = this.wakefulnessLifecycleProvider;
            Provider access$3200 = this.globalRootComponentImpl.provideUserManagerProvider;
            Provider access$25500 = this.globalRootComponentImpl.provideLockPatternUtilsProvider;
            Provider<StatusBarStateControllerImpl> provider8 = this.statusBarStateControllerImplProvider;
            DelegateFactory.setDelegate(provider3, DoubleCheck.provider(AuthController_Factory.create(access$2400, access$31100, provider4, access$20700, access$2800, access$34800, access$34900, provider5, provider6, access$7300, provider7, access$3200, access$25500, provider8, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBackgroundDelayableExecutorProvider)));
            this.activeUnlockConfigProvider = DoubleCheck.provider(ActiveUnlockConfig_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.secureSettingsImplProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.globalRootComponentImpl.dumpManagerProvider));
            DelegateFactory.setDelegate(this.keyguardUpdateMonitorProvider, DoubleCheck.provider(KeyguardUpdateMonitor_Factory.create(this.globalRootComponentImpl.contextProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.broadcastDispatcherProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.authControllerProvider, this.telephonyListenerManagerProvider, this.globalRootComponentImpl.provideInteractionJankMonitorProvider, this.globalRootComponentImpl.provideLatencyTrackerProvider, this.activeUnlockConfigProvider)));
            DelegateFactory.setDelegate(this.keyguardStateControllerImplProvider, DoubleCheck.provider(KeyguardStateControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.keyguardUnlockAnimationControllerProvider, this.globalRootComponentImpl.dumpManagerProvider)));
            this.brightLineFalsingManagerProvider = BrightLineFalsingManager_Factory.create(this.falsingDataProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.namedSetOfFalsingClassifierProvider, this.singleTapClassifierProvider, this.doubleTapClassifierProvider, this.historyTrackerProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.provideAccessibilityManagerProvider, this.globalRootComponentImpl.provideIsTestHarnessProvider);
            DelegateFactory.setDelegate(this.falsingManagerProxyProvider, DoubleCheck.provider(FalsingManagerProxy_Factory.create(this.globalRootComponentImpl.providesPluginManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.deviceConfigProxyProvider, this.globalRootComponentImpl.dumpManagerProvider, this.brightLineFalsingManagerProvider)));
            DelegateFactory.setDelegate(this.factoryProvider6, BrightnessSliderController_Factory_Factory.create(this.falsingManagerProxyProvider));
            this.brightnessDialogProvider = BrightnessDialog_Factory.create(this.broadcastDispatcherProvider, this.factoryProvider6, this.provideBgHandlerProvider);
            this.usbDebuggingActivityProvider = UsbDebuggingActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbDebuggingSecondaryUserActivityProvider = UsbDebuggingSecondaryUserActivity_Factory.create(this.broadcastDispatcherProvider);
            this.usbPermissionActivityProvider = UsbPermissionActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            this.usbConfirmActivityProvider = UsbConfirmActivity_Factory.create(UsbAudioWarningDialogMessage_Factory.create());
            UserCreator_Factory create = UserCreator_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUserManagerProvider);
            this.userCreatorProvider = create;
            this.createUserActivityProvider = CreateUserActivity_Factory.create(create, UserModule_ProvideEditUserInfoControllerFactory.create(), this.globalRootComponentImpl.provideIActivityManagerProvider);
            TvNotificationHandler_Factory create2 = TvNotificationHandler_Factory.create(this.globalRootComponentImpl.contextProvider, this.notificationListenerProvider);
            this.tvNotificationHandlerProvider = create2;
            this.tvNotificationPanelActivityProvider = TvNotificationPanelActivity_Factory.create(create2);
            this.peopleSpaceActivityProvider = PeopleSpaceActivity_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.imageExporterProvider = ImageExporter_Factory.create(this.globalRootComponentImpl.provideContentResolverProvider);
            this.longScreenshotDataProvider = DoubleCheck.provider(LongScreenshotData_Factory.create());
            this.longScreenshotActivityProvider = LongScreenshotActivity_Factory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider, this.imageExporterProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.longScreenshotDataProvider);
            this.launchConversationActivityProvider = LaunchConversationActivity_Factory.create(this.provideNotificationVisibilityProvider, this.provideCommonNotifCollectionProvider, this.provideBubblesManagerProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.provideCommandQueueProvider);
            this.sensorUseStartedActivityProvider = SensorUseStartedActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideBgHandlerProvider);
            this.tvUnblockSensorActivityProvider = TvUnblockSensorActivity_Factory.create(this.provideIndividualSensorPrivacyControllerProvider);
            Provider<HdmiCecSetMenuLanguageHelper> provider9 = DoubleCheck.provider(HdmiCecSetMenuLanguageHelper_Factory.create(this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider));
            this.hdmiCecSetMenuLanguageHelperProvider = provider9;
            this.hdmiCecSetMenuLanguageActivityProvider = HdmiCecSetMenuLanguageActivity_Factory.create(provider9);
            this.provideExecutorProvider = DoubleCheck.provider(SysUIConcurrencyModule_ProvideExecutorFactory.create(this.provideBgLooperProvider));
            this.controlsListingControllerImplProvider = DoubleCheck.provider(ControlsListingControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideExecutorProvider, this.provideUserTrackerProvider));
            this.controlsControllerImplProvider = new DelegateFactory();
            this.setTaskViewFactoryProvider = InstanceFactory.create(optional7);
            this.controlsMetricsLoggerImplProvider = DoubleCheck.provider(ControlsMetricsLoggerImpl_Factory.create());
            this.controlActionCoordinatorImplProvider = DoubleCheck.provider(ControlActionCoordinatorImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideDelayableExecutorProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.keyguardStateControllerImplProvider, this.setTaskViewFactoryProvider, this.controlsMetricsLoggerImplProvider, this.vibratorHelperProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.provideMainHandlerProvider));
            this.customIconCacheProvider = DoubleCheck.provider(CustomIconCache_Factory.create());
            this.controlsUiControllerImplProvider = DoubleCheck.provider(ControlsUiControllerImpl_Factory.create(this.controlsControllerImplProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsListingControllerImplProvider, this.globalRootComponentImpl.provideSharePreferencesProvider, this.controlActionCoordinatorImplProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.customIconCacheProvider, this.controlsMetricsLoggerImplProvider, this.keyguardStateControllerImplProvider));
            this.controlsBindingControllerImplProvider = DoubleCheck.provider(ControlsBindingControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsControllerImplProvider, this.provideUserTrackerProvider));
            this.optionalOfControlsFavoritePersistenceWrapperProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            DelegateFactory.setDelegate(this.controlsControllerImplProvider, DoubleCheck.provider(ControlsControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.controlsUiControllerImplProvider, this.controlsBindingControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.optionalOfControlsFavoritePersistenceWrapperProvider, this.globalRootComponentImpl.dumpManagerProvider, this.provideUserTrackerProvider)));
            this.controlsProviderSelectorActivityProvider = ControlsProviderSelectorActivity_Factory.create(this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.controlsListingControllerImplProvider, this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsFavoritingActivityProvider = ControlsFavoritingActivity_Factory.create(this.globalRootComponentImpl.provideMainExecutorProvider, this.controlsControllerImplProvider, this.controlsListingControllerImplProvider, this.broadcastDispatcherProvider, this.controlsUiControllerImplProvider);
            this.controlsEditingActivityProvider = ControlsEditingActivity_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.customIconCacheProvider, this.controlsUiControllerImplProvider);
            this.controlsRequestDialogProvider = ControlsRequestDialog_Factory.create(this.controlsControllerImplProvider, this.broadcastDispatcherProvider, this.controlsListingControllerImplProvider);
            this.controlsActivityProvider = ControlsActivity_Factory.create(this.controlsUiControllerImplProvider, this.broadcastDispatcherProvider);
            this.userSwitcherActivityProvider = UserSwitcherActivity_Factory.create(this.userSwitcherControllerProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.provideUserTrackerProvider);
            this.walletActivityProvider = WalletActivity_Factory.create(this.keyguardStateControllerImplProvider, this.keyguardDismissUtilProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.falsingCollectorImplProvider, this.provideUserTrackerProvider, this.keyguardUpdateMonitorProvider, this.statusBarKeyguardViewManagerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider);
            this.mapOfClassOfAndProviderOfActivityProvider = MapProviderFactory.builder(24).put((Object) TunerActivity.class, (Provider) this.tunerActivityProvider).put((Object) ForegroundServicesDialog.class, (Provider) this.foregroundServicesDialogProvider).put((Object) WorkLockActivity.class, (Provider) this.workLockActivityProvider).put((Object) BrightnessDialog.class, (Provider) this.brightnessDialogProvider).put((Object) UsbDebuggingActivity.class, (Provider) this.usbDebuggingActivityProvider).put((Object) UsbDebuggingSecondaryUserActivity.class, (Provider) this.usbDebuggingSecondaryUserActivityProvider).put((Object) UsbPermissionActivity.class, (Provider) this.usbPermissionActivityProvider).put((Object) UsbConfirmActivity.class, (Provider) this.usbConfirmActivityProvider).put((Object) CreateUserActivity.class, (Provider) this.createUserActivityProvider).put((Object) TvNotificationPanelActivity.class, (Provider) this.tvNotificationPanelActivityProvider).put((Object) PeopleSpaceActivity.class, (Provider) this.peopleSpaceActivityProvider).put((Object) LongScreenshotActivity.class, (Provider) this.longScreenshotActivityProvider).put((Object) LaunchConversationActivity.class, (Provider) this.launchConversationActivityProvider).put((Object) SensorUseStartedActivity.class, (Provider) this.sensorUseStartedActivityProvider).put((Object) TvUnblockSensorActivity.class, (Provider) this.tvUnblockSensorActivityProvider).put((Object) HdmiCecSetMenuLanguageActivity.class, (Provider) this.hdmiCecSetMenuLanguageActivityProvider).put((Object) NTCriticalTemperatureWarning.class, (Provider) NTCriticalTemperatureWarning_Factory.create()).put((Object) ControlsProviderSelectorActivity.class, (Provider) this.controlsProviderSelectorActivityProvider).put((Object) ControlsFavoritingActivity.class, (Provider) this.controlsFavoritingActivityProvider).put((Object) ControlsEditingActivity.class, (Provider) this.controlsEditingActivityProvider).put((Object) ControlsRequestDialog.class, (Provider) this.controlsRequestDialogProvider).put((Object) ControlsActivity.class, (Provider) this.controlsActivityProvider).put((Object) UserSwitcherActivity.class, (Provider) this.userSwitcherActivityProvider).put((Object) WalletActivity.class, (Provider) this.walletActivityProvider).build();
            C204110 r1 = new Provider<DozeComponent.Builder>() {
                public DozeComponent.Builder get() {
                    return new DozeComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.dozeComponentBuilderProvider = r1;
            this.dozeServiceProvider = DozeService_Factory.create(r1, this.globalRootComponentImpl.providesPluginManagerProvider);
            Provider<KeyguardLifecyclesDispatcher> provider10 = DoubleCheck.provider(KeyguardLifecyclesDispatcher_Factory.create(this.globalRootComponentImpl.screenLifecycleProvider, this.wakefulnessLifecycleProvider));
            this.keyguardLifecyclesDispatcherProvider = provider10;
            this.keyguardServiceProvider = KeyguardService_Factory.create(this.newKeyguardViewMediatorProvider, provider10, this.setTransitionsProvider);
            this.dreamOverlayComponentFactoryProvider = new Provider<DreamOverlayComponent.Factory>() {
                public DreamOverlayComponent.Factory get() {
                    return new DreamOverlayComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.dreamOverlayServiceProvider = DreamOverlayService_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.dreamOverlayComponentFactoryProvider, this.dreamOverlayStateControllerProvider, this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider);
            this.notificationListenerWithPluginsProvider = NotificationListenerWithPlugins_Factory.create(this.globalRootComponentImpl.providesPluginManagerProvider);
            this.broadcastDispatcherStartableProvider = BroadcastDispatcherStartable_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider);
            this.clipboardOverlayControllerFactoryProvider = DoubleCheck.provider(ClipboardOverlayControllerFactory_Factory.create(this.broadcastDispatcherProvider, this.broadcastSenderProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.clipboardListenerProvider = DoubleCheck.provider(ClipboardListener_Factory.create(this.globalRootComponentImpl.contextProvider, this.deviceConfigProxyProvider, this.clipboardOverlayControllerFactoryProvider, this.globalRootComponentImpl.provideClipboardManagerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.providesBackgroundMessageRouterProvider = SysUIConcurrencyModule_ProvidesBackgroundMessageRouterFactory.create(this.provideBackgroundDelayableExecutorProvider);
            this.provideLeakReportEmailProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideLeakReportEmailFactory.create());
            this.leakReporterProvider = DoubleCheck.provider(LeakReporter_Factory.create(this.globalRootComponentImpl.contextProvider, this.providesLeakDetectorProvider, this.provideLeakReportEmailProvider));
            this.garbageMonitorProvider = DoubleCheck.provider(GarbageMonitor_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundDelayableExecutorProvider, this.providesBackgroundMessageRouterProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.serviceProvider = DoubleCheck.provider(GarbageMonitor_Service_Factory.create(this.globalRootComponentImpl.contextProvider, this.garbageMonitorProvider));
            this.globalActionsComponentProvider = new DelegateFactory();
            this.globalActionsDialogLiteProvider = GlobalActionsDialogLite_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalActionsComponentProvider, this.globalRootComponentImpl.provideAudioManagerProvider, this.globalRootComponentImpl.provideIDreamManagerProvider, this.globalRootComponentImpl.provideDevicePolicyManagerProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.broadcastDispatcherProvider, this.telephonyListenerManagerProvider, this.globalSettingsImplProvider, this.secureSettingsImplProvider, this.vibratorHelperProvider, this.globalRootComponentImpl.provideResourcesProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.provideTrustManagerProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.globalRootComponentImpl.provideTelecomManagerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.sysuiColorExtractorProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.notificationShadeWindowControllerImplProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.ringerModeTrackerImplProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.optionalOfCentralSurfacesProvider, this.keyguardUpdateMonitorProvider, this.provideDialogLaunchAnimatorProvider);
            this.globalActionsImplProvider = GlobalActionsImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.globalActionsDialogLiteProvider, this.blurUtilsProvider, this.keyguardStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider);
            DelegateFactory.setDelegate(this.globalActionsComponentProvider, DoubleCheck.provider(GlobalActionsComponent_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.extensionControllerImplProvider, this.globalActionsImplProvider, this.statusBarKeyguardViewManagerProvider)));
            this.instantAppNotifierProvider = DoubleCheck.provider(InstantAppNotifier_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider));
            this.keyboardUIProvider = DoubleCheck.provider(KeyboardUI_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.keyguardBiometricLockoutLoggerProvider = DoubleCheck.provider(KeyguardBiometricLockoutLogger_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.keyguardUpdateMonitorProvider, this.sessionTrackerProvider));
            this.latencyTesterProvider = DoubleCheck.provider(LatencyTester_Factory.create(this.globalRootComponentImpl.contextProvider, this.biometricUnlockControllerProvider, this.broadcastDispatcherProvider, this.deviceConfigProxyProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.powerNotificationWarningsProvider = DoubleCheck.provider(PowerNotificationWarnings_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideBatteryControllerProvider, this.provideDialogLaunchAnimatorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.powerUIProvider = DoubleCheck.provider(PowerUI_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.provideCommandQueueProvider, this.optionalOfCentralSurfacesProvider, this.powerNotificationWarningsProvider, this.enhancedEstimatesImplProvider, this.globalRootComponentImpl.providePowerManagerProvider));
            this.ringtonePlayerProvider = DoubleCheck.provider(RingtonePlayer_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.systemEventCoordinatorProvider = DoubleCheck.provider(SystemEventCoordinator_Factory.create(this.bindSystemClockProvider, this.provideBatteryControllerProvider, this.privacyItemControllerProvider));
            SystemEventChipAnimationController_Factory create3 = SystemEventChipAnimationController_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarWindowControllerProvider, this.statusBarContentInsetsProvider);
            this.systemEventChipAnimationControllerProvider = create3;
            this.systemStatusAnimationSchedulerProvider = DoubleCheck.provider(SystemStatusAnimationScheduler_Factory.create(this.systemEventCoordinatorProvider, create3, this.statusBarWindowControllerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.bindSystemClockProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider));
            this.privacyDotViewControllerProvider = DoubleCheck.provider(PrivacyDotViewController_Factory.create(this.globalRootComponentImpl.provideMainExecutorProvider, this.statusBarStateControllerImplProvider, this.configurationControllerImplProvider, this.statusBarContentInsetsProvider, this.systemStatusAnimationSchedulerProvider));
            this.privacyDotDecorProviderFactoryProvider = DoubleCheck.provider(PrivacyDotDecorProviderFactory_Factory.create(this.globalRootComponentImpl.provideResourcesProvider));
            this.screenDecorationsProvider = DoubleCheck.provider(ScreenDecorations_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.secureSettingsImplProvider, this.broadcastDispatcherProvider, this.tunerServiceImplProvider, this.provideUserTrackerProvider, this.privacyDotViewControllerProvider, ThreadFactoryImpl_Factory.create(), this.privacyDotDecorProviderFactoryProvider));
        }

        private void initialize6(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.shortcutKeyDispatcherProvider = DoubleCheck.provider(ShortcutKeyDispatcher_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.sliceBroadcastRelayHandlerProvider = DoubleCheck.provider(SliceBroadcastRelayHandler_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider));
            this.storageNotificationProvider = DoubleCheck.provider(StorageNotification_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.provideLauncherPackageProvider = ThemeModule_ProvideLauncherPackageFactory.create(this.globalRootComponentImpl.provideResourcesProvider);
            this.provideThemePickerPackageProvider = ThemeModule_ProvideThemePickerPackageFactory.create(this.globalRootComponentImpl.provideResourcesProvider);
            this.themeOverlayApplierProvider = DoubleCheck.provider(ThemeOverlayApplier_Factory.create(this.globalRootComponentImpl.provideOverlayManagerProvider, this.provideBackgroundExecutorProvider, this.provideLauncherPackageProvider, this.provideThemePickerPackageProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.themeOverlayControllerProvider = DoubleCheck.provider(ThemeOverlayController_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, this.provideBgHandlerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.themeOverlayApplierProvider, this.secureSettingsImplProvider, this.globalRootComponentImpl.provideWallpaperManagerProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.bindDeviceProvisionedControllerProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.featureFlagsReleaseProvider, this.globalRootComponentImpl.provideResourcesProvider, this.wakefulnessLifecycleProvider));
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvideToastLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideToastLogBufferProvider = provider;
            this.toastLoggerProvider = ToastLogger_Factory.create(provider);
            this.toastUIProvider = DoubleCheck.provider(ToastUI_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.toastFactoryProvider, this.toastLoggerProvider));
            this.volumeUIProvider = DoubleCheck.provider(VolumeUI_Factory.create(this.globalRootComponentImpl.contextProvider, this.volumeDialogComponentProvider));
            this.modeSwitchesControllerProvider = DoubleCheck.provider(ModeSwitchesController_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.windowMagnificationProvider = DoubleCheck.provider(WindowMagnification_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideCommandQueueProvider, this.modeSwitchesControllerProvider, this.provideSysUiStateProvider, this.overviewProxyServiceProvider));
            this.setHideDisplayCutoutProvider = InstanceFactory.create(optional8);
            this.setShellCommandHandlerProvider = InstanceFactory.create(optional9);
            this.setCompatUIProvider = InstanceFactory.create(optional14);
            this.setDragAndDropProvider = InstanceFactory.create(optional15);
            this.wMShellProvider = DoubleCheck.provider(WMShell_Factory.create(this.globalRootComponentImpl.contextProvider, this.setPipProvider, this.setSplitScreenProvider, this.setOneHandedProvider, this.setHideDisplayCutoutProvider, this.setShellCommandHandlerProvider, this.setCompatUIProvider, this.setDragAndDropProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.navigationModeControllerProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.provideSysUiStateProvider, this.protoTracerProvider, this.wakefulnessLifecycleProvider, this.userInfoControllerImplProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.keyguardLiftControllerProvider = DoubleCheck.provider(KeyguardLiftController_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarStateControllerImplProvider, this.asyncSensorManagerProvider, this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.mapOfClassOfAndProviderOfCoreStartableProvider = MapProviderFactory.builder(27).put((Object) BroadcastDispatcherStartable.class, (Provider) this.broadcastDispatcherStartableProvider).put((Object) KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put((Object) AuthController.class, (Provider) this.authControllerProvider).put((Object) ClipboardListener.class, (Provider) this.clipboardListenerProvider).put((Object) GarbageMonitor.class, (Provider) this.serviceProvider).put((Object) GlobalActionsComponent.class, (Provider) this.globalActionsComponentProvider).put((Object) InstantAppNotifier.class, (Provider) this.instantAppNotifierProvider).put((Object) KeyboardUI.class, (Provider) this.keyboardUIProvider).put((Object) KeyguardBiometricLockoutLogger.class, (Provider) this.keyguardBiometricLockoutLoggerProvider).put((Object) KeyguardViewMediator.class, (Provider) this.newKeyguardViewMediatorProvider).put((Object) LatencyTester.class, (Provider) this.latencyTesterProvider).put((Object) PowerUI.class, (Provider) this.powerUIProvider).put((Object) Recents.class, (Provider) this.provideRecentsProvider).put((Object) RingtonePlayer.class, (Provider) this.ringtonePlayerProvider).put((Object) ScreenDecorations.class, (Provider) this.screenDecorationsProvider).put((Object) SessionTracker.class, (Provider) this.sessionTrackerProvider).put((Object) ShortcutKeyDispatcher.class, (Provider) this.shortcutKeyDispatcherProvider).put((Object) SliceBroadcastRelayHandler.class, (Provider) this.sliceBroadcastRelayHandlerProvider).put((Object) StorageNotification.class, (Provider) this.storageNotificationProvider).put((Object) SystemActions.class, (Provider) this.systemActionsProvider).put((Object) ThemeOverlayController.class, (Provider) this.themeOverlayControllerProvider).put((Object) ToastUI.class, (Provider) this.toastUIProvider).put((Object) VolumeUI.class, (Provider) this.volumeUIProvider).put((Object) WindowMagnification.class, (Provider) this.windowMagnificationProvider).put((Object) WMShell.class, (Provider) this.wMShellProvider).put((Object) KeyguardLiftController.class, (Provider) this.keyguardLiftControllerProvider).put((Object) CentralSurfaces.class, (Provider) this.centralSurfacesImplProvider).build();
            this.dumpHandlerProvider = DumpHandler_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider, this.logBufferEulogizerProvider, this.mapOfClassOfAndProviderOfCoreStartableProvider, this.globalRootComponentImpl.uncaughtExceptionPreHandlerManagerProvider);
            this.logBufferFreezerProvider = LogBufferFreezer_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider);
            this.batteryStateNotifierProvider = BatteryStateNotifier_Factory.create(this.provideBatteryControllerProvider, this.globalRootComponentImpl.provideNotificationManagerProvider, this.provideDelayableExecutorProvider, this.globalRootComponentImpl.contextProvider);
            this.systemUIServiceProvider = SystemUIService_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.dumpHandlerProvider, this.broadcastDispatcherProvider, this.logBufferFreezerProvider, this.batteryStateNotifierProvider);
            this.systemUIAuxiliaryDumpServiceProvider = SystemUIAuxiliaryDumpService_Factory.create(this.dumpHandlerProvider);
            this.recordingServiceProvider = RecordingService_Factory.create(this.recordingControllerProvider, this.provideLongRunningExecutorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.globalRootComponentImpl.provideNotificationManagerProvider, this.provideUserTrackerProvider, this.keyguardDismissUtilProvider);
            this.screenshotSmartActionsProvider = DoubleCheck.provider(ScreenshotSmartActions_Factory.create());
            this.screenshotNotificationsControllerProvider = ScreenshotNotificationsController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideWindowManagerProvider);
            this.scrollCaptureClientProvider = ScrollCaptureClient_Factory.create(this.globalRootComponentImpl.provideIWindowManagerProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.contextProvider);
            this.imageTileSetProvider = ImageTileSet_Factory.create(GlobalConcurrencyModule_ProvideHandlerFactory.create());
            this.scrollCaptureControllerProvider = ScrollCaptureController_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.scrollCaptureClientProvider, this.imageTileSetProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider);
            this.timeoutHandlerProvider = TimeoutHandler_Factory.create(this.globalRootComponentImpl.contextProvider);
            ScreenshotController_Factory create = ScreenshotController_Factory.create(this.globalRootComponentImpl.contextProvider, this.screenshotSmartActionsProvider, this.screenshotNotificationsControllerProvider, this.scrollCaptureClientProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.imageExporterProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.scrollCaptureControllerProvider, this.longScreenshotDataProvider, this.globalRootComponentImpl.provideActivityManagerProvider, this.timeoutHandlerProvider, this.broadcastSenderProvider);
            this.screenshotControllerProvider = create;
            this.takeScreenshotServiceProvider = TakeScreenshotService_Factory.create(create, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.provideDevicePolicyManagerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.screenshotNotificationsControllerProvider, this.globalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider);
            this.mapOfClassOfAndProviderOfServiceProvider = MapProviderFactory.builder(9).put((Object) DozeService.class, (Provider) this.dozeServiceProvider).put((Object) ImageWallpaper.class, (Provider) ImageWallpaper_Factory.create()).put((Object) KeyguardService.class, (Provider) this.keyguardServiceProvider).put((Object) DreamOverlayService.class, (Provider) this.dreamOverlayServiceProvider).put((Object) NotificationListenerWithPlugins.class, (Provider) this.notificationListenerWithPluginsProvider).put((Object) SystemUIService.class, (Provider) this.systemUIServiceProvider).put((Object) SystemUIAuxiliaryDumpService.class, (Provider) this.systemUIAuxiliaryDumpServiceProvider).put((Object) RecordingService.class, (Provider) this.recordingServiceProvider).put((Object) TakeScreenshotService.class, (Provider) this.takeScreenshotServiceProvider).build();
            this.overviewProxyRecentsImplProvider = DoubleCheck.provider(OverviewProxyRecentsImpl_Factory.create(this.optionalOfCentralSurfacesProvider, this.overviewProxyServiceProvider));
            this.mapOfClassOfAndProviderOfRecentsImplementationProvider = MapProviderFactory.builder(1).put((Object) OverviewProxyRecentsImpl.class, (Provider) this.overviewProxyRecentsImplProvider).build();
            this.actionProxyReceiverProvider = ActionProxyReceiver_Factory.create(this.optionalOfCentralSurfacesProvider, this.provideActivityManagerWrapperProvider, this.screenshotSmartActionsProvider);
            this.deleteScreenshotReceiverProvider = DeleteScreenshotReceiver_Factory.create(this.screenshotSmartActionsProvider, this.provideBackgroundExecutorProvider);
            this.smartActionsReceiverProvider = SmartActionsReceiver_Factory.create(this.screenshotSmartActionsProvider);
            MediaOutputBroadcastDialogFactory_Factory create2 = MediaOutputBroadcastDialogFactory_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMediaSessionManagerProvider, this.provideLocalBluetoothControllerProvider, this.provideActivityStarterProvider, this.broadcastSenderProvider, this.provideCommonNotifCollectionProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideDialogLaunchAnimatorProvider, this.providesNearbyMediaDevicesManagerProvider, this.globalRootComponentImpl.provideAudioManagerProvider, this.globalRootComponentImpl.providePowerExemptionManagerProvider);
            this.mediaOutputBroadcastDialogFactoryProvider = create2;
            this.mediaOutputDialogReceiverProvider = MediaOutputDialogReceiver_Factory.create(this.mediaOutputDialogFactoryProvider, create2);
            this.peopleSpaceWidgetPinnedReceiverProvider = PeopleSpaceWidgetPinnedReceiver_Factory.create(this.peopleSpaceWidgetManagerProvider);
            this.peopleSpaceWidgetProvider = PeopleSpaceWidgetProvider_Factory.create(this.peopleSpaceWidgetManagerProvider);
            MapProviderFactory build = MapProviderFactory.builder(6).put((Object) ActionProxyReceiver.class, (Provider) this.actionProxyReceiverProvider).put((Object) DeleteScreenshotReceiver.class, (Provider) this.deleteScreenshotReceiverProvider).put((Object) SmartActionsReceiver.class, (Provider) this.smartActionsReceiverProvider).put((Object) MediaOutputDialogReceiver.class, (Provider) this.mediaOutputDialogReceiverProvider).put((Object) PeopleSpaceWidgetPinnedReceiver.class, (Provider) this.peopleSpaceWidgetPinnedReceiverProvider).put((Object) PeopleSpaceWidgetProvider.class, (Provider) this.peopleSpaceWidgetProvider).build();
            this.mapOfClassOfAndProviderOfBroadcastReceiverProvider = build;
            DelegateFactory.setDelegate(this.contextComponentResolverProvider, DoubleCheck.provider(ContextComponentResolver_Factory.create(this.mapOfClassOfAndProviderOfActivityProvider, this.mapOfClassOfAndProviderOfServiceProvider, this.mapOfClassOfAndProviderOfRecentsImplementationProvider, build)));
            this.unfoldLatencyTrackerProvider = DoubleCheck.provider(UnfoldLatencyTracker_Factory.create(this.globalRootComponentImpl.provideLatencyTrackerProvider, this.globalRootComponentImpl.provideDeviceStateManagerProvider, this.globalRootComponentImpl.provideUiBackgroundExecutorProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.screenLifecycleProvider));
            this.flashlightControllerImplProvider = DoubleCheck.provider(FlashlightControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.provideNightDisplayListenerProvider = NightDisplayListenerModule_ProvideNightDisplayListenerFactory.create(nightDisplayListenerModule, this.globalRootComponentImpl.contextProvider, this.provideBgHandlerProvider);
            this.reduceBrightColorsControllerProvider = DoubleCheck.provider(ReduceBrightColorsController_Factory.create(this.provideUserTrackerProvider, this.provideBgHandlerProvider, this.globalRootComponentImpl.provideColorDisplayManagerProvider, this.secureSettingsImplProvider));
            this.managedProfileControllerImplProvider = DoubleCheck.provider(ManagedProfileControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider));
            this.accessibilityControllerProvider = DoubleCheck.provider(AccessibilityController_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.tunablePaddingServiceProvider = DoubleCheck.provider(TunablePadding_TunablePaddingService_Factory.create(this.tunerServiceImplProvider));
            this.uiOffloadThreadProvider = DoubleCheck.provider(UiOffloadThread_Factory.create());
            this.provideGroupExpansionManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideGroupExpansionManagerFactory.create(this.notifPipelineFlagsProvider, this.provideGroupMembershipManagerProvider, this.notificationGroupManagerLegacyProvider));
            this.statusBarRemoteInputCallbackProvider = DoubleCheck.provider(StatusBarRemoteInputCallback_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideGroupExpansionManagerProvider, this.notificationLockscreenUserManagerImplProvider, this.keyguardStateControllerImplProvider, this.statusBarStateControllerImplProvider, this.statusBarKeyguardViewManagerProvider, this.provideActivityStarterProvider, this.shadeControllerImplProvider, this.provideCommandQueueProvider, this.actionClickLoggerProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.remoteInputQuickSettingsDisablerProvider = DoubleCheck.provider(RemoteInputQuickSettingsDisabler_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideCommandQueueProvider, this.configurationControllerImplProvider));
            this.foregroundServiceNotificationListenerProvider = DoubleCheck.provider(ForegroundServiceNotificationListener_Factory.create(this.globalRootComponentImpl.contextProvider, this.foregroundServiceControllerProvider, this.provideNotificationEntryManagerProvider, this.notifPipelineProvider, this.bindSystemClockProvider));
            this.clockManagerProvider = DoubleCheck.provider(ClockManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.providerLayoutInflaterProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.sysuiColorExtractorProvider, this.dockManagerImplProvider, this.broadcastDispatcherProvider));
            this.dependencyProvider = DoubleCheck.provider(Dependency_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, this.provideActivityStarterProvider, this.broadcastDispatcherProvider, this.asyncSensorManagerProvider, this.bluetoothControllerImplProvider, this.locationControllerImplProvider, this.rotationLockControllerImplProvider, this.zenModeControllerImplProvider, this.hdmiCecSetMenuLanguageHelperProvider, this.hotspotControllerImplProvider, this.castControllerImplProvider, this.flashlightControllerImplProvider, this.userSwitcherControllerProvider, this.userInfoControllerImplProvider, this.keyguardStateControllerImplProvider, this.keyguardUpdateMonitorProvider, this.provideBatteryControllerProvider, this.provideNightDisplayListenerProvider, this.reduceBrightColorsControllerProvider, this.managedProfileControllerImplProvider, this.nextAlarmControllerImplProvider, this.provideDataSaverControllerProvider, this.accessibilityControllerProvider, this.bindDeviceProvisionedControllerProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.assistManagerProvider, this.securityControllerImplProvider, this.providesLeakDetectorProvider, this.leakReporterProvider, this.garbageMonitorProvider, this.tunerServiceImplProvider, this.notificationShadeWindowControllerImplProvider, this.statusBarWindowControllerProvider, this.darkIconDispatcherImplProvider, this.configurationControllerImplProvider, this.statusBarIconControllerImplProvider, this.globalRootComponentImpl.screenLifecycleProvider, this.wakefulnessLifecycleProvider, this.fragmentServiceProvider, this.extensionControllerImplProvider, this.globalRootComponentImpl.pluginDependencyProvider, this.provideLocalBluetoothControllerProvider, this.volumeDialogControllerImplProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.accessibilityManagerWrapperProvider, this.sysuiColorExtractorProvider, this.tunablePaddingServiceProvider, this.foregroundServiceControllerProvider, this.uiOffloadThreadProvider, this.powerNotificationWarningsProvider, this.lightBarControllerProvider, this.globalRootComponentImpl.provideIWindowManagerProvider, this.overviewProxyServiceProvider, this.navigationModeControllerProvider, this.accessibilityButtonModeObserverProvider, this.accessibilityButtonTargetsObserverProvider, this.enhancedEstimatesImplProvider, this.vibratorHelperProvider, this.globalRootComponentImpl.provideIStatusBarServiceProvider, this.globalRootComponentImpl.provideDisplayMetricsProvider, this.lockscreenGestureLoggerProvider, this.keyguardEnvironmentImplProvider, this.shadeControllerImplProvider, this.statusBarRemoteInputCallbackProvider, this.appOpsControllerImplProvider, this.navigationBarControllerProvider, this.accessibilityFloatingMenuControllerProvider, this.statusBarStateControllerImplProvider, this.notificationLockscreenUserManagerImplProvider, this.notificationGroupAlertTransferHelperProvider, this.notificationGroupManagerLegacyProvider, this.provideVisualStabilityManagerProvider, this.provideNotificationGutsManagerProvider, this.provideNotificationMediaManagerProvider, this.provideNotificationRemoteInputManagerProvider, this.smartReplyConstantsProvider, this.notificationListenerProvider, this.provideNotificationLoggerProvider, this.provideNotificationViewHierarchyManagerProvider, this.notificationFilterProvider, this.keyguardDismissUtilProvider, this.provideSmartReplyControllerProvider, this.remoteInputQuickSettingsDisablerProvider, this.provideNotificationEntryManagerProvider, this.globalRootComponentImpl.provideSensorPrivacyManagerProvider, this.autoHideControllerProvider, this.foregroundServiceNotificationListenerProvider, this.privacyItemControllerProvider, this.provideBgLooperProvider, this.provideBgHandlerProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.globalRootComponentImpl.provideMainHandlerProvider, this.provideTimeTickHandlerProvider, this.provideLeakReportEmailProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.clockManagerProvider, this.provideActivityManagerWrapperProvider, this.provideDevicePolicyManagerWrapperProvider, this.globalRootComponentImpl.providePackageManagerWrapperProvider, this.provideSensorPrivacyControllerProvider, this.dockManagerImplProvider, this.globalRootComponentImpl.provideINotificationManagerProvider, this.provideSysUiStateProvider, this.globalRootComponentImpl.provideAlarmManagerProvider, this.keyguardSecurityModelProvider, this.dozeParametersProvider, FrameworkServicesModule_ProvideIWallPaperManagerFactory.create(), this.provideCommandQueueProvider, this.recordingControllerProvider, this.protoTracerProvider, this.mediaOutputDialogFactoryProvider, this.deviceConfigProxyProvider, this.telephonyListenerManagerProvider, this.systemStatusAnimationSchedulerProvider, this.privacyDotViewControllerProvider, this.factoryProvider2, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.statusBarContentInsetsProvider, this.internetDialogFactoryProvider, this.featureFlagsReleaseProvider, this.notificationSectionsManagerProvider, this.screenOffAnimationControllerProvider, this.ambientStateProvider, this.provideGroupMembershipManagerProvider, this.provideGroupExpansionManagerProvider, this.systemUIDialogManagerProvider, this.provideDialogLaunchAnimatorProvider));
            this.nTColorControllerProvider = DoubleCheck.provider(NTColorController_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.keyguardWeatherControllerImplProvider = DoubleCheck.provider(KeyguardWeatherControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.keyguardUpdateMonitorProvider));
            this.liftWakeGestureControllerProvider = DoubleCheck.provider(LiftWakeGestureController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideProximitySensorProvider));
            this.centralSurfacesImplExProvider = CentralSurfacesImplEx_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.keyguardUpdateMonitorProvider, this.globalRootComponentImpl.aODControllerProvider, this.liftWakeGestureControllerProvider);
            this.nfcControllerImplProvider = DoubleCheck.provider(NfcControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBatteryControllerProvider));
            this.teslaInfoControllerImplProvider = DoubleCheck.provider(TeslaInfoControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.broadcastDispatcherProvider, GlobalConcurrencyModule_ProvideMainLooperFactory.create(), this.globalRootComponentImpl.dumpManagerProvider));
            this.faceRecognitionControllerProvider = DoubleCheck.provider(FaceRecognitionController_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.nTLightweightHeadsupManagerProvider = DoubleCheck.provider(NTLightweightHeadsupManager_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.nTGameModeHelperProvider = DoubleCheck.provider(NTGameModeHelper_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.headsUpControllerExProvider = DoubleCheck.provider(HeadsUpControllerEx_Factory.create(this.headsUpControllerProvider, this.provideHeadsUpManagerPhoneProvider, this.headsUpViewBinderProvider));
            this.temperatureControllerProvider = DoubleCheck.provider(TemperatureController_Factory.create());
            this.keyguardIndicationControllerExProvider = KeyguardIndicationControllerEx_Factory.create(this.globalRootComponentImpl.contextProvider, this.keyguardSecurityModelProvider, this.keyguardUpdateMonitorProvider, this.newKeyguardViewMediatorProvider);
            this.ambientStateExProvider = DoubleCheck.provider(AmbientStateEx_Factory.create());
            this.commandQueueExProvider = CommandQueueEx_Factory.create(this.provideCommandQueueProvider);
            this.calendarManagerProvider = DoubleCheck.provider(CalendarManager_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.keyguardUpdateMonitorProvider));
            this.assistManagerExProvider = DoubleCheck.provider(AssistManagerEx_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.nTDependencyExProvider = DoubleCheck.provider(NTDependencyEx_Factory.create(this.globalRootComponentImpl.dumpManagerProvider, BatteryControllerImplEx_Factory.create(), BluetoothTileEx_Factory.create(), BrightnessControllerEx_Factory.create(), InternetTileEx_Factory.create(), this.nTColorControllerProvider, VolumeDialogImplEx_Factory.create(), WifiSignalControllerEx_Factory.create(), OngoingPrivacyChipEx_Factory.create(), PrivacyDialogEx_Factory.create(), PrivacyDialogControllerEx_Factory.create(), QSFragmentEx_Factory.create(), QSIconViewImplEx_Factory.create(), QSTileHostEx_Factory.create(), QSTileImplEx_Factory.create(), QSTileViewImplEx_Factory.create(), QuickStatusBarHeaderEx_Factory.create(), TileLayoutEx_Factory.create(), NavigationBarControllerEx_Factory.create(), NavigationBarEx_Factory.create(), NavigationBarViewEx_Factory.create(), NavigationBarInflaterViewEx_Factory.create(), NavigationModeControllerEx_Factory.create(), KeyButtonViewEx_Factory.create(), EdgeBackGestureHandlerEx_Factory.create(), this.keyguardWeatherControllerImplProvider, this.globalRootComponentImpl.aODControllerProvider, this.liftWakeGestureControllerProvider, this.centralSurfacesImplExProvider, DozeServiceHostEx_Factory.create(), this.nfcControllerImplProvider, this.teslaInfoControllerImplProvider, this.globalRootComponentImpl.keyguardUpdateMonitorExProvider, this.globalRootComponentImpl.keyguardViewMediatorExProvider, this.faceRecognitionControllerProvider, this.nTLightweightHeadsupManagerProvider, this.nTGameModeHelperProvider, this.headsUpControllerExProvider, this.falsingManagerProxyProvider, this.temperatureControllerProvider, this.keyguardIndicationControllerExProvider, MobileSignalControllerEx_Factory.create(), this.ambientStateExProvider, this.commandQueueExProvider, this.lockscreenShadeTransitionControllerProvider, this.scrimControllerProvider, this.calendarManagerProvider, LockIconViewControllerEx_Factory.create(), this.mediaDataManagerProvider, this.configurationControllerImplProvider, this.assistManagerExProvider, this.notificationRoundnessManagerProvider, this.globalRootComponentImpl.authRippleControllerExProvider));
            this.mediaTttFlagsProvider = DoubleCheck.provider(MediaTttFlags_Factory.create(this.featureFlagsReleaseProvider));
            Provider<LogBuffer> provider2 = DoubleCheck.provider(LogModule_ProvideMediaTttSenderLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttSenderLogBufferProvider = provider2;
            this.providesMediaTttSenderLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttSenderLoggerFactory.create(provider2));
            this.viewUtilProvider = DoubleCheck.provider(ViewUtil_Factory.create());
            this.tapGestureDetectorProvider = DoubleCheck.provider(TapGestureDetector_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.mediaTttSenderUiEventLoggerProvider = DoubleCheck.provider(MediaTttSenderUiEventLogger_Factory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerSender> provider3 = DoubleCheck.provider(MediaTttChipControllerSender_Factory.create(this.provideCommandQueueProvider, this.globalRootComponentImpl.contextProvider, this.providesMediaTttSenderLoggerProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.viewUtilProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider, this.tapGestureDetectorProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.mediaTttSenderUiEventLoggerProvider));
            this.mediaTttChipControllerSenderProvider = provider3;
            this.providesMediaTttChipControllerSenderProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerSenderFactory.create(this.mediaTttFlagsProvider, provider3));
            Provider<LogBuffer> provider4 = DoubleCheck.provider(LogModule_ProvideMediaTttReceiverLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideMediaTttReceiverLogBufferProvider = provider4;
            this.providesMediaTttReceiverLoggerProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttReceiverLoggerFactory.create(provider4));
            this.mediaTttReceiverUiEventLoggerProvider = DoubleCheck.provider(MediaTttReceiverUiEventLogger_Factory.create(this.globalRootComponentImpl.provideUiEventLoggerProvider));
            Provider<MediaTttChipControllerReceiver> provider5 = DoubleCheck.provider(MediaTttChipControllerReceiver_Factory.create(this.provideCommandQueueProvider, this.globalRootComponentImpl.contextProvider, this.providesMediaTttReceiverLoggerProvider, this.globalRootComponentImpl.provideWindowManagerProvider, this.viewUtilProvider, this.provideDelayableExecutorProvider, this.tapGestureDetectorProvider, this.globalRootComponentImpl.providePowerManagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.mediaTttReceiverUiEventLoggerProvider));
            this.mediaTttChipControllerReceiverProvider = provider5;
            this.providesMediaTttChipControllerReceiverProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttChipControllerReceiverFactory.create(this.mediaTttFlagsProvider, provider5));
            Provider<MediaTttCommandLineHelper> provider6 = DoubleCheck.provider(MediaTttCommandLineHelper_Factory.create(this.commandRegistryProvider, this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainExecutorProvider));
            this.mediaTttCommandLineHelperProvider = provider6;
            this.providesMediaTttCommandLineHelperProvider = DoubleCheck.provider(MediaModule_ProvidesMediaTttCommandLineHelperFactory.create(this.mediaTttFlagsProvider, provider6));
            Provider<MediaMuteAwaitConnectionCli> provider7 = DoubleCheck.provider(MediaMuteAwaitConnectionCli_Factory.create(this.commandRegistryProvider, this.globalRootComponentImpl.contextProvider));
            this.mediaMuteAwaitConnectionCliProvider = provider7;
            this.providesMediaMuteAwaitConnectionCliProvider = DoubleCheck.provider(MediaModule_ProvidesMediaMuteAwaitConnectionCliFactory.create(this.mediaFlagsProvider, provider7));
            this.notificationChannelsProvider = NotificationChannels_Factory.create(this.globalRootComponentImpl.contextProvider);
            this.provideClockInfoListProvider = ClockModule_ProvideClockInfoListFactory.create(this.clockManagerProvider);
            this.provideAllowNotificationLongPressProvider = DoubleCheck.provider(ReferenceSystemUIModule_ProvideAllowNotificationLongPressFactory.create());
            this.setDisplayAreaHelperProvider = InstanceFactory.create(optional11);
            this.sectionClassifierProvider = DoubleCheck.provider(SectionClassifier_Factory.create());
            this.providesAlertingHeaderNodeControllerProvider = C2716x30119a0.create(this.providesAlertingHeaderSubcomponentProvider);
        }

        private void initialize7(LeakModule leakModule, NightDisplayListenerModule nightDisplayListenerModule, SharedLibraryModule sharedLibraryModule, KeyguardModule keyguardModule, SysUIUnfoldModule sysUIUnfoldModule, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<SplitScreen> optional3, Optional<AppPairs> optional4, Optional<OneHanded> optional5, Optional<Bubbles> optional6, Optional<TaskViewFactory> optional7, Optional<HideDisplayCutout> optional8, Optional<ShellCommandHandler> optional9, ShellTransitions shellTransitions, Optional<StartingSurface> optional10, Optional<DisplayAreaHelper> optional11, Optional<TaskSurfaceHelper> optional12, Optional<RecentTasks> optional13, Optional<CompatUI> optional14, Optional<DragAndDrop> optional15, Optional<BackAnimation> optional16) {
            this.providesSilentHeaderNodeControllerProvider = C2725x9d7acab1.create(this.providesSilentHeaderSubcomponentProvider);
            this.providesIncomingHeaderNodeControllerProvider = C2719x8d68ee80.create(this.providesIncomingHeaderSubcomponentProvider);
            this.provideNotifGutsViewManagerProvider = DoubleCheck.provider(NotificationsModule_ProvideNotifGutsViewManagerFactory.create(this.provideNotificationGutsManagerProvider));
            this.providesPeopleHeaderNodeControllerProvider = C2722xda791837.create(this.providesPeopleHeaderSubcomponentProvider);
            this.notifUiAdjustmentProvider = DoubleCheck.provider(NotifUiAdjustmentProvider_Factory.create(this.notificationLockscreenUserManagerImplProvider, this.sectionClassifierProvider));
            this.optionalOfBcSmartspaceDataPluginProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.lockscreenSmartspaceControllerProvider = DoubleCheck.provider(LockscreenSmartspaceController_Factory.create(this.globalRootComponentImpl.contextProvider, this.featureFlagsReleaseProvider, this.globalRootComponentImpl.provideSmartspaceManagerProvider, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.secureSettingsImplProvider, this.provideUserTrackerProvider, this.globalRootComponentImpl.provideContentResolverProvider, this.configurationControllerImplProvider, this.statusBarStateControllerImplProvider, this.bindDeviceProvisionedControllerProvider, this.globalRootComponentImpl.provideExecutionProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.optionalOfBcSmartspaceDataPluginProvider));
            this.qSTileHostProvider = new DelegateFactory();
            Provider<LogBuffer> provider = DoubleCheck.provider(LogModule_ProvideQuickSettingsLogBufferFactory.create(this.logBufferFactoryProvider));
            this.provideQuickSettingsLogBufferProvider = provider;
            this.qSLoggerProvider = QSLogger_Factory.create(provider);
            this.customTileStatePersisterProvider = CustomTileStatePersister_Factory.create(this.globalRootComponentImpl.contextProvider);
            this.tileServicesProvider = TileServices_Factory.create(this.qSTileHostProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.broadcastDispatcherProvider, this.provideUserTrackerProvider, this.keyguardStateControllerImplProvider, this.provideCommandQueueProvider);
            this.builderProvider5 = CustomTile_Builder_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.customTileStatePersisterProvider, this.tileServicesProvider);
            this.wifiTileProvider = WifiTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider);
            this.internetTileProvider = InternetTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.provideAccessPointControllerImplProvider, this.internetDialogFactoryProvider, this.carrierNameCustomizationProvider);
            this.bluetoothTileProvider = BluetoothTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.bluetoothControllerImplProvider, this.teslaInfoControllerImplProvider);
            this.cellularTileProvider = CellularTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.networkControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.dndTileProvider = DndTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.zenModeControllerImplProvider, this.globalRootComponentImpl.provideSharePreferencesProvider, this.secureSettingsImplProvider, this.provideDialogLaunchAnimatorProvider);
            this.colorInversionTileProvider = ColorInversionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.airplaneModeTileProvider = AirplaneModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.provideConnectivityManagagerProvider, this.globalSettingsImplProvider);
            this.workModeTileProvider = WorkModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.managedProfileControllerImplProvider);
            this.rotationLockTileProvider = RotationLockTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.rotationLockControllerImplProvider, this.globalRootComponentImpl.provideSensorPrivacyManagerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.flashlightTileProvider = FlashlightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.flashlightControllerImplProvider);
            this.locationTileProvider = LocationTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.keyguardStateControllerImplProvider);
            this.castTileProvider = CastTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.castControllerImplProvider, this.keyguardStateControllerImplProvider, this.networkControllerImplProvider, this.hotspotControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            this.hotspotTileProvider = HotspotTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider);
            this.batterySaverTileProvider = BatterySaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideBatteryControllerProvider, this.secureSettingsImplProvider);
            this.dataSaverTileProvider = DataSaverTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideDataSaverControllerProvider, this.provideDialogLaunchAnimatorProvider);
            this.builderProvider6 = NightDisplayListenerModule_Builder_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBgHandlerProvider);
            this.nightDisplayTileProvider = NightDisplayTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.locationControllerImplProvider, this.globalRootComponentImpl.provideColorDisplayManagerProvider, this.builderProvider6);
            this.nfcTileProvider = NfcTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.broadcastDispatcherProvider, this.nfcControllerImplProvider);
            this.memoryTileProvider = GarbageMonitor_MemoryTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.garbageMonitorProvider);
            this.uiModeNightTileProvider = UiModeNightTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.configurationControllerImplProvider, this.provideBatteryControllerProvider, this.locationControllerImplProvider);
            this.screenRecordTileProvider = ScreenRecordTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.recordingControllerProvider, this.keyguardDismissUtilProvider, this.keyguardStateControllerImplProvider, this.provideDialogLaunchAnimatorProvider);
            Provider<Boolean> provider2 = DoubleCheck.provider(QSFlagsModule_IsReduceBrightColorsAvailableFactory.create(this.globalRootComponentImpl.contextProvider));
            this.isReduceBrightColorsAvailableProvider = provider2;
            this.reduceBrightColorsTileProvider = ReduceBrightColorsTile_Factory.create(provider2, this.reduceBrightColorsControllerProvider, this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider);
            this.cameraToggleTileProvider = CameraToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.microphoneToggleTileProvider = MicrophoneToggleTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.falsingManagerProxyProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideIndividualSensorPrivacyControllerProvider, this.keyguardStateControllerImplProvider);
            this.providesControlsFeatureEnabledProvider = DoubleCheck.provider(ControlsModule_ProvidesControlsFeatureEnabledFactory.create(this.globalRootComponentImpl.providePackageManagerProvider));
            this.optionalOfControlsTileResourceConfigurationProvider = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.controlsComponentProvider = DoubleCheck.provider(ControlsComponent_Factory.create(this.providesControlsFeatureEnabledProvider, this.globalRootComponentImpl.contextProvider, this.controlsControllerImplProvider, this.controlsUiControllerImplProvider, this.controlsListingControllerImplProvider, this.globalRootComponentImpl.provideLockPatternUtilsProvider, this.keyguardStateControllerImplProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.optionalOfControlsTileResourceConfigurationProvider));
            this.deviceControlsTileProvider = DeviceControlsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.controlsComponentProvider, this.keyguardStateControllerImplProvider);
            this.alarmTileProvider = AlarmTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.nextAlarmControllerImplProvider);
            this.provideQuickAccessWalletClientProvider = DoubleCheck.provider(WalletModule_ProvideQuickAccessWalletClientFactory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider));
            this.quickAccessWalletControllerProvider = DoubleCheck.provider(QuickAccessWalletController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.provideQuickAccessWalletClientProvider, this.bindSystemClockProvider));
            this.quickAccessWalletTileProvider = QuickAccessWalletTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.keyguardStateControllerImplProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.secureSettingsImplProvider, this.quickAccessWalletControllerProvider);
            this.qRCodeScannerControllerProvider = DoubleCheck.provider(QRCodeScannerController_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBackgroundExecutorProvider, this.secureSettingsImplProvider, this.deviceConfigProxyProvider, this.provideUserTrackerProvider));
            this.qRCodeScannerTileProvider = QRCodeScannerTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.qRCodeScannerControllerProvider);
            this.oneHandedModeTileProvider = OneHandedModeTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.batteryShareControllerImplProvider = DoubleCheck.provider(BatteryShareControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.provideBatteryControllerProvider));
            this.batteryShareTileProvider = BatteryShareTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.batteryShareControllerImplProvider);
            this.glyphsControllerImplProvider = DoubleCheck.provider(GlyphsControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider));
            this.glyphsTileProvider = GlyphsTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.glyphsControllerImplProvider);
            ColorCorrectionTile_Factory create = ColorCorrectionTile_Factory.create(this.qSTileHostProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.falsingManagerProxyProvider, this.globalRootComponentImpl.provideMetricsLoggerProvider, this.statusBarStateControllerImplProvider, this.provideActivityStarterProvider, this.qSLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider);
            this.colorCorrectionTileProvider = create;
            this.qSFactoryImplProvider = DoubleCheck.provider(QSFactoryImpl_Factory.create(this.qSTileHostProvider, this.builderProvider5, this.wifiTileProvider, this.internetTileProvider, this.bluetoothTileProvider, this.cellularTileProvider, this.dndTileProvider, this.colorInversionTileProvider, this.airplaneModeTileProvider, this.workModeTileProvider, this.rotationLockTileProvider, this.flashlightTileProvider, this.locationTileProvider, this.castTileProvider, this.hotspotTileProvider, this.batterySaverTileProvider, this.dataSaverTileProvider, this.nightDisplayTileProvider, this.nfcTileProvider, this.memoryTileProvider, this.uiModeNightTileProvider, this.screenRecordTileProvider, this.reduceBrightColorsTileProvider, this.cameraToggleTileProvider, this.microphoneToggleTileProvider, this.deviceControlsTileProvider, this.alarmTileProvider, this.quickAccessWalletTileProvider, this.qRCodeScannerTileProvider, this.oneHandedModeTileProvider, this.batteryShareTileProvider, this.glyphsTileProvider, create));
            this.builderProvider7 = DoubleCheck.provider(AutoAddTracker_Builder_Factory.create(this.secureSettingsImplProvider, this.broadcastDispatcherProvider, this.qSTileHostProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBackgroundExecutorProvider));
            this.deviceControlsControllerImplProvider = DoubleCheck.provider(DeviceControlsControllerImpl_Factory.create(this.globalRootComponentImpl.contextProvider, this.controlsComponentProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider));
            this.walletControllerImplProvider = DoubleCheck.provider(WalletControllerImpl_Factory.create(this.provideQuickAccessWalletClientProvider));
            this.safetyControllerProvider = SafetyController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.globalRootComponentImpl.provideSafetyCenterManagerProvider, this.provideBgHandlerProvider);
            this.provideAutoTileManagerProvider = QSModule_ProvideAutoTileManagerFactory.create(this.globalRootComponentImpl.contextProvider, this.builderProvider7, this.qSTileHostProvider, this.provideBgHandlerProvider, this.secureSettingsImplProvider, this.hotspotControllerImplProvider, this.provideDataSaverControllerProvider, this.managedProfileControllerImplProvider, this.provideNightDisplayListenerProvider, this.castControllerImplProvider, this.reduceBrightColorsControllerProvider, this.deviceControlsControllerImplProvider, this.walletControllerImplProvider, this.safetyControllerProvider, this.isReduceBrightColorsAvailableProvider);
            this.builderProvider8 = DoubleCheck.provider(TileServiceRequestController_Builder_Factory.create(this.provideCommandQueueProvider, this.commandRegistryProvider));
            this.packageManagerAdapterProvider = PackageManagerAdapter_Factory.create(this.globalRootComponentImpl.contextProvider);
            C4834TileLifecycleManager_Factory create2 = C4834TileLifecycleManager_Factory.create(this.globalRootComponentImpl.provideMainHandlerProvider, this.globalRootComponentImpl.contextProvider, this.tileServicesProvider, this.packageManagerAdapterProvider, this.broadcastDispatcherProvider);
            this.tileLifecycleManagerProvider = create2;
            this.factoryProvider9 = TileLifecycleManager_Factory_Impl.create(create2);
            DelegateFactory.setDelegate(this.qSTileHostProvider, DoubleCheck.provider(QSTileHost_Factory.create(this.globalRootComponentImpl.contextProvider, this.statusBarIconControllerImplProvider, this.qSFactoryImplProvider, this.globalRootComponentImpl.provideMainHandlerProvider, this.provideBgLooperProvider, this.globalRootComponentImpl.providesPluginManagerProvider, this.tunerServiceImplProvider, this.provideAutoTileManagerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.broadcastDispatcherProvider, this.optionalOfCentralSurfacesProvider, this.qSLoggerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.provideUserTrackerProvider, this.secureSettingsImplProvider, this.customTileStatePersisterProvider, this.builderProvider8, this.factoryProvider9)));
            this.providesQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.providesQuickQSMediaHostProvider = DoubleCheck.provider(MediaModule_ProvidesQuickQSMediaHostFactory.create(MediaHost_MediaHostStateHolder_Factory.create(), this.mediaHierarchyManagerProvider, this.mediaDataManagerProvider, this.mediaHostStatesManagerProvider));
            this.provideQSFragmentDisableLogBufferProvider = DoubleCheck.provider(LogModule_ProvideQSFragmentDisableLogBufferFactory.create(this.logBufferFactoryProvider));
            this.disableFlagsLoggerProvider = DoubleCheck.provider(DisableFlagsLogger_Factory.create());
            this.notificationShelfComponentBuilderProvider = new Provider<NotificationShelfComponent.Builder>() {
                public NotificationShelfComponent.Builder get() {
                    return new NotificationShelfComponentBuilder(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            C4841SplitShadeOverScroller_Factory create3 = C4841SplitShadeOverScroller_Factory.create(this.configurationControllerImplProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.contextProvider, this.scrimControllerProvider);
            this.splitShadeOverScrollerProvider = create3;
            this.factoryProvider10 = SplitShadeOverScroller_Factory_Impl.create(create3);
            this.shadeTransitionControllerProvider = DoubleCheck.provider(ShadeTransitionController_Factory.create(this.configurationControllerImplProvider, this.panelExpansionStateManagerProvider, this.globalRootComponentImpl.contextProvider, this.factoryProvider10, NoOpOverScroller_Factory.create()));
            this.notificationStackSizeCalculatorProvider = DoubleCheck.provider(NotificationStackSizeCalculator_Factory.create(this.statusBarStateControllerImplProvider, this.lockscreenShadeTransitionControllerProvider, this.globalRootComponentImpl.provideResourcesProvider));
            this.keyguardQsUserSwitchComponentFactoryProvider = new Provider<KeyguardQsUserSwitchComponent.Factory>() {
                public KeyguardQsUserSwitchComponent.Factory get() {
                    return new KeyguardQsUserSwitchComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.keyguardUserSwitcherComponentFactoryProvider = new Provider<KeyguardUserSwitcherComponent.Factory>() {
                public KeyguardUserSwitcherComponent.Factory get() {
                    return new KeyguardUserSwitcherComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.keyguardStatusBarViewComponentFactoryProvider = new Provider<KeyguardStatusBarViewComponent.Factory>() {
                public KeyguardStatusBarViewComponent.Factory get() {
                    return new KeyguardStatusBarViewComponentFactory(SysUIComponentImpl.this.globalRootComponentImpl, SysUIComponentImpl.this.sysUIComponentImpl);
                }
            };
            this.privacyDialogControllerProvider = DoubleCheck.provider(PrivacyDialogController_Factory.create(this.globalRootComponentImpl.providePermissionManagerProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.privacyItemControllerProvider, this.provideUserTrackerProvider, this.provideActivityStarterProvider, this.provideBackgroundExecutorProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.privacyLoggerProvider, this.keyguardStateControllerImplProvider, this.appOpsControllerImplProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.subscriptionManagerSlotIndexResolverProvider = DoubleCheck.provider(C2359xf95dc14f.create());
            this.qsFrameTranslateImplProvider = DoubleCheck.provider(QsFrameTranslateImpl_Factory.create(this.centralSurfacesImplProvider));
            Provider<Optional<LowLightClockController>> access$2600 = DaggerGlobalRootComponent.absentJdkOptionalProvider();
            this.dynamicOverrideOptionalOfLowLightClockControllerProvider = access$2600;
            this.provideLowLightClockControllerProvider = DoubleCheck.provider(SystemUIModule_ProvideLowLightClockControllerFactory.create(access$2600));
            this.statusBarLocationPublisherProvider = DoubleCheck.provider(StatusBarLocationPublisher_Factory.create());
            this.provideCollapsedSbFragmentLogBufferProvider = DoubleCheck.provider(LogModule_ProvideCollapsedSbFragmentLogBufferFactory.create(this.logBufferFactoryProvider));
            this.statusBarUserInfoTrackerProvider = DoubleCheck.provider(StatusBarUserInfoTracker_Factory.create(this.userInfoControllerImplProvider, this.globalRootComponentImpl.provideUserManagerProvider, this.globalRootComponentImpl.dumpManagerProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider));
            this.statusBarUserSwitcherFeatureControllerProvider = DoubleCheck.provider(StatusBarUserSwitcherFeatureController_Factory.create(this.featureFlagsReleaseProvider));
            UserDetailView_Adapter_Factory create4 = UserDetailView_Adapter_Factory.create(this.globalRootComponentImpl.contextProvider, this.userSwitcherControllerProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider, this.falsingManagerProxyProvider);
            this.adapterProvider = create4;
            this.userSwitchDialogControllerProvider = DoubleCheck.provider(UserSwitchDialogController_Factory.create(create4, this.provideActivityStarterProvider, this.falsingManagerProxyProvider, this.provideDialogLaunchAnimatorProvider, this.globalRootComponentImpl.provideUiEventLoggerProvider));
            this.provideProximityCheckProvider = SensorModule_ProvideProximityCheckFactory.create(this.provideProximitySensorProvider, this.globalRootComponentImpl.provideMainDelayableExecutorProvider);
            this.dreamOverlayNotificationCountProvider = DoubleCheck.provider(DreamOverlayNotificationCountProvider_Factory.create(this.notificationListenerProvider, this.provideBackgroundExecutorProvider));
            this.fgsManagerControllerProvider = DoubleCheck.provider(FgsManagerController_Factory.create(this.globalRootComponentImpl.contextProvider, this.globalRootComponentImpl.provideMainExecutorProvider, this.provideBackgroundExecutorProvider, this.bindSystemClockProvider, this.globalRootComponentImpl.provideIActivityManagerProvider, this.globalRootComponentImpl.providePackageManagerProvider, this.provideUserTrackerProvider, this.deviceConfigProxyProvider, this.provideDialogLaunchAnimatorProvider, this.broadcastDispatcherProvider, this.globalRootComponentImpl.dumpManagerProvider));
            this.isPMLiteEnabledProvider = DoubleCheck.provider(QSFlagsModule_IsPMLiteEnabledFactory.create(this.featureFlagsReleaseProvider, this.globalSettingsImplProvider));
            this.factoryProvider11 = DoubleCheck.provider(StatusBarIconController_TintedIconManager_Factory_Factory.create(this.featureFlagsReleaseProvider));
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
            return (Optional) this.globalRootComponentImpl.providesFoldStateLoggingProvider.get();
        }

        public Optional<FoldStateLogger> getFoldStateLogger() {
            return (Optional) this.globalRootComponentImpl.providesFoldStateLoggerProvider.get();
        }

        public Dependency createDependency() {
            return this.dependencyProvider.get();
        }

        public NTDependencyEx createDependencyEx() {
            return this.nTDependencyExProvider.get();
        }

        public DumpManager createDumpManager() {
            return (DumpManager) this.globalRootComponentImpl.dumpManagerProvider.get();
        }

        public InitController getInitController() {
            return this.initControllerProvider.get();
        }

        public Optional<SysUIUnfoldComponent> getSysUIUnfoldComponent() {
            return this.provideSysUIUnfoldComponentProvider.get();
        }

        public Optional<NaturalRotationUnfoldProgressProvider> getNaturalRotationUnfoldProgressProvider() {
            return (Optional) this.globalRootComponentImpl.provideNaturalRotationProgressProvider.get();
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
            return MapBuilder.newMapBuilder(27).put(BroadcastDispatcherStartable.class, this.broadcastDispatcherStartableProvider).put(KeyguardNotificationVisibilityProvider.class, this.keyguardNotificationVisibilityProviderImplProvider).put(AuthController.class, this.authControllerProvider).put(ClipboardListener.class, this.clipboardListenerProvider).put(GarbageMonitor.class, this.serviceProvider).put(GlobalActionsComponent.class, this.globalActionsComponentProvider).put(InstantAppNotifier.class, this.instantAppNotifierProvider).put(KeyboardUI.class, this.keyboardUIProvider).put(KeyguardBiometricLockoutLogger.class, this.keyguardBiometricLockoutLoggerProvider).put(KeyguardViewMediator.class, this.newKeyguardViewMediatorProvider).put(LatencyTester.class, this.latencyTesterProvider).put(PowerUI.class, this.powerUIProvider).put(Recents.class, this.provideRecentsProvider).put(RingtonePlayer.class, this.ringtonePlayerProvider).put(ScreenDecorations.class, this.screenDecorationsProvider).put(SessionTracker.class, this.sessionTrackerProvider).put(ShortcutKeyDispatcher.class, this.shortcutKeyDispatcherProvider).put(SliceBroadcastRelayHandler.class, this.sliceBroadcastRelayHandlerProvider).put(StorageNotification.class, this.storageNotificationProvider).put(SystemActions.class, this.systemActionsProvider).put(ThemeOverlayController.class, this.themeOverlayControllerProvider).put(ToastUI.class, this.toastUIProvider).put(VolumeUI.class, this.volumeUIProvider).put(WindowMagnification.class, this.windowMagnificationProvider).put(WMShell.class, this.wMShellProvider).put(KeyguardLiftController.class, this.keyguardLiftControllerProvider).put(CentralSurfaces.class, this.centralSurfacesImplProvider).build();
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
            KeyguardSliceProvider_MembersInjector.injectMAlarmManager(keyguardSliceProvider, (AlarmManager) this.globalRootComponentImpl.provideAlarmManagerProvider.get());
            KeyguardSliceProvider_MembersInjector.injectMContentResolver(keyguardSliceProvider, (ContentResolver) this.globalRootComponentImpl.provideContentResolverProvider.get());
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

    private static final class GlobalRootComponentImpl implements GlobalRootComponent {
        /* access modifiers changed from: private */
        public Provider<AODController> aODControllerProvider;
        private Provider<ATraceLoggerTransitionProgressListener> aTraceLoggerTransitionProgressListenerProvider;
        /* access modifiers changed from: private */
        public Provider<AuthRippleControllerEx> authRippleControllerExProvider;
        /* access modifiers changed from: private */
        public final Context context;
        /* access modifiers changed from: private */
        public Provider<Context> contextProvider;
        private Provider<DeviceFoldStateProvider> deviceFoldStateProvider;
        /* access modifiers changed from: private */
        public Provider<DumpManager> dumpManagerProvider;
        private Provider<ScaleAwareTransitionProgressProvider.Factory> factoryProvider;
        private final GlobalModule globalModule;
        private final GlobalRootComponentImpl globalRootComponentImpl;
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
        public Provider<ClipboardManager> provideClipboardManagerProvider;
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
        public Provider<IBatteryStats> provideIBatteryStatsProvider;
        /* access modifiers changed from: private */
        public Provider<IDreamManager> provideIDreamManagerProvider;
        /* access modifiers changed from: private */
        public Provider<INotificationManager> provideINotificationManagerProvider;
        /* access modifiers changed from: private */
        public Provider<IPackageManager> provideIPackageManagerProvider;
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
        public Provider<Optional<Vibrator>> provideOptionalVibratorProvider;
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
        /* access modifiers changed from: private */
        public Provider<UncaughtExceptionPreHandlerManager> uncaughtExceptionPreHandlerManagerProvider;
        /* access modifiers changed from: private */
        public Provider<Optional<UnfoldTransitionProgressProvider>> unfoldTransitionProgressProvider;

        private GlobalRootComponentImpl(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
            this.globalRootComponentImpl = this;
            this.context = context2;
            this.globalModule = globalModule2;
            initialize(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
            initialize2(globalModule2, androidInternalsModule, frameworkServicesModule, unfoldTransitionModule, unfoldSharedModule, context2);
        }

        /* access modifiers changed from: private */
        public Handler mainHandler() {
            return GlobalConcurrencyModule_ProvideMainHandlerFactory.provideMainHandler(GlobalConcurrencyModule_ProvideMainLooperFactory.provideMainLooper());
        }

        /* access modifiers changed from: private */
        public Resources mainResources() {
            return FrameworkServicesModule_ProvideResourcesFactory.provideResources(this.context);
        }

        /* access modifiers changed from: private */
        public DisplayMetrics displayMetrics() {
            return GlobalModule_ProvideDisplayMetricsFactory.provideDisplayMetrics(this.globalModule, this.context);
        }

        private void initialize(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
            this.contextProvider = InstanceFactory.create(context2);
            this.provideIWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIWindowManagerFactory.create());
            this.provideUiEventLoggerProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideUiEventLoggerFactory.create());
            this.provideIStatusBarServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIStatusBarServiceFactory.create());
            this.provideWindowManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWindowManagerFactory.create(this.contextProvider));
            this.provideUserManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUserManagerFactory.create(this.contextProvider));
            this.provideLauncherAppsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLauncherAppsFactory.create(this.contextProvider));
            this.provideInteractionJankMonitorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInteractionJankMonitorFactory.create());
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
            this.provideLockPatternUtilsProvider = DoubleCheck.provider(AndroidInternalsModule_ProvideLockPatternUtilsFactory.create(androidInternalsModule, this.contextProvider));
            this.provideExecutionProvider = DoubleCheck.provider(ExecutionImpl_Factory.create());
            this.provideActivityTaskManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideActivityTaskManagerFactory.create());
            this.providesFingerprintManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesFingerprintManagerFactory.create(this.contextProvider));
            this.provideFaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideFaceManagerFactory.create(this.contextProvider));
            this.providerLayoutInflaterProvider = DoubleCheck.provider(FrameworkServicesModule_ProviderLayoutInflaterFactory.create(frameworkServicesModule, this.contextProvider));
            this.provideMainDelayableExecutorProvider = DoubleCheck.provider(GlobalConcurrencyModule_ProvideMainDelayableExecutorFactory.create(GlobalConcurrencyModule_ProvideMainLooperFactory.create()));
            this.provideTrustManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTrustManagerFactory.create(this.contextProvider));
            this.provideIActivityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIActivityManagerFactory.create());
            this.provideDevicePolicyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDevicePolicyManagerFactory.create(this.contextProvider));
            this.provideNotificationMessagingUtilProvider = AndroidInternalsModule_ProvideNotificationMessagingUtilFactory.create(androidInternalsModule, this.contextProvider);
            this.providesChoreographerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidesChoreographerFactory.create(frameworkServicesModule));
            this.provideKeyguardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideKeyguardManagerFactory.create(this.contextProvider));
            this.providePackageManagerWrapperProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePackageManagerWrapperFactory.create());
            this.provideAmbientDisplayConfigurationProvider = C2055x19afde28.create(frameworkServicesModule, this.contextProvider);
            Provider<Optional<NaturalRotationUnfoldProgressProvider>> provider9 = DoubleCheck.provider(C3249xd9485442.create(unfoldTransitionModule, this.contextProvider, this.provideIWindowManagerProvider, this.unfoldTransitionProgressProvider));
            this.provideNaturalRotationProgressProvider = provider9;
            this.provideStatusBarScopedTransitionProvider = DoubleCheck.provider(C3250x6e72e9f0.create(unfoldTransitionModule, provider9));
            this.provideMediaSessionManagerProvider = FrameworkServicesModule_ProvideMediaSessionManagerFactory.create(this.contextProvider);
            this.provideMediaRouter2ManagerProvider = FrameworkServicesModule_ProvideMediaRouter2ManagerFactory.create(this.contextProvider);
            this.provideAudioManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAudioManagerFactory.create(this.contextProvider));
            this.provideSensorPrivacyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSensorPrivacyManagerFactory.create(this.contextProvider));
            this.provideIDreamManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIDreamManagerFactory.create());
            this.provideDisplayIdProvider = FrameworkServicesModule_ProvideDisplayIdFactory.create(this.contextProvider);
            this.provideCarrierConfigManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCarrierConfigManagerFactory.create(this.contextProvider));
            this.provideSubcriptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSubcriptionManagerFactory.create(this.contextProvider));
            this.provideConnectivityManagagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideConnectivityManagagerFactory.create(this.contextProvider));
            this.provideTelephonyManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelephonyManagerFactory.create(this.contextProvider));
            this.provideWifiManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideWifiManagerFactory.create(this.contextProvider));
            this.provideNetworkScoreManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideNetworkScoreManagerFactory.create(this.contextProvider));
            this.providePowerExemptionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePowerExemptionManagerFactory.create(this.contextProvider));
            this.provideAlarmManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAlarmManagerFactory.create(this.contextProvider));
            this.provideAccessibilityManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideAccessibilityManagerFactory.create(this.contextProvider));
            this.provideLatencyTrackerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideLatencyTrackerFactory.create(this.contextProvider));
            this.provideCrossWindowBlurListenersProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory.create());
            this.provideWallpaperManagerProvider = FrameworkServicesModule_ProvideWallpaperManagerFactory.create(this.contextProvider);
            this.provideINotificationManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideINotificationManagerFactory.create(frameworkServicesModule));
            this.provideShortcutManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideShortcutManagerFactory.create(this.contextProvider));
            this.provideVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideVibratorFactory.create(this.contextProvider));
            this.provideIAudioServiceProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIAudioServiceFactory.create());
            this.provideCaptioningManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideCaptioningManagerFactory.create(this.contextProvider));
            this.pluginDependencyProvider = DoubleCheck.provider(PluginDependencyProvider_Factory.create(this.providesPluginManagerProvider));
            this.provideTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideTelecomManagerFactory.create(this.contextProvider));
            this.provideSharePreferencesProvider = FrameworkServicesModule_ProvideSharePreferencesFactory.create(frameworkServicesModule, this.contextProvider);
            this.provideIBatteryStatsProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIBatteryStatsFactory.create());
            this.provideDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideDisplayManagerFactory.create(this.contextProvider));
            this.provideIsTestHarnessProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIsTestHarnessFactory.create());
            this.provideClipboardManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideClipboardManagerFactory.create(this.contextProvider));
            this.provideOverlayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOverlayManagerFactory.create(this.contextProvider));
            Provider<Optional<FoldStateLoggingProvider>> provider10 = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggingProviderFactory.create(unfoldTransitionModule, this.provideUnfoldTransitionConfigProvider, this.provideFoldStateProvider));
            this.providesFoldStateLoggingProvider = provider10;
            this.providesFoldStateLoggerProvider = DoubleCheck.provider(UnfoldTransitionModule_ProvidesFoldStateLoggerFactory.create(unfoldTransitionModule, provider10));
            this.provideColorDisplayManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideColorDisplayManagerFactory.create(this.contextProvider));
            this.aODControllerProvider = DoubleCheck.provider(AODController_Factory.create(this.contextProvider));
            Provider<KeyguardUpdateMonitorEx> provider11 = DoubleCheck.provider(KeyguardUpdateMonitorEx_Factory.create());
            this.keyguardUpdateMonitorExProvider = provider11;
            this.keyguardViewMediatorExProvider = DoubleCheck.provider(KeyguardViewMediatorEx_Factory.create(this.providePowerManagerProvider, provider11));
            this.authRippleControllerExProvider = DoubleCheck.provider(AuthRippleControllerEx_Factory.create(this.contextProvider));
            this.provideIPackageManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideIPackageManagerFactory.create());
        }

        private void initialize2(GlobalModule globalModule2, AndroidInternalsModule androidInternalsModule, FrameworkServicesModule frameworkServicesModule, UnfoldTransitionModule unfoldTransitionModule, UnfoldSharedModule unfoldSharedModule, Context context2) {
            this.provideSmartspaceManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSmartspaceManagerFactory.create(this.contextProvider));
            this.provideSafetyCenterManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideSafetyCenterManagerFactory.create(this.contextProvider));
            this.provideOptionalTelecomManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalTelecomManagerFactory.create(this.contextProvider));
            this.provideInputMethodManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideInputMethodManagerFactory.create(this.contextProvider));
            this.providePermissionManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvidePermissionManagerFactory.create(this.contextProvider));
            this.provideOptionalVibratorProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideOptionalVibratorFactory.create(this.contextProvider));
            this.provideUiModeManagerProvider = DoubleCheck.provider(FrameworkServicesModule_ProvideUiModeManagerFactory.create(this.contextProvider));
            this.qSExpansionPathInterpolatorProvider = DoubleCheck.provider(QSExpansionPathInterpolator_Factory.create());
        }

        public WMComponent.Builder getWMComponentBuilder() {
            return new WMComponentBuilder(this.globalRootComponentImpl);
        }

        public SysUIComponent.Builder getSysUIComponent() {
            return new SysUIComponentBuilder(this.globalRootComponentImpl);
        }
    }
}
