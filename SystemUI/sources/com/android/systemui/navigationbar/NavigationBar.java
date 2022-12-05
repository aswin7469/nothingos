package com.android.systemui.navigationbar;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.IWindowManager;
import android.view.InsetsState;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.mediarouter.media.MediaRoute2Provider$$ExternalSyntheticLambda0;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.LatencyTracker;
import com.android.internal.view.AppearanceRegion;
import com.android.nothingos.gamemode.GameModeHelper;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NavigationBar implements View.OnAttachStateChangeListener, CommandQueue.Callbacks, NavigationModeController.ModeChangedListener, AccessibilityButtonModeObserver.ModeChangedListener {
    private final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    private final AccessibilityManager mAccessibilityManager;
    private final AccessibilityManagerWrapper mAccessibilityManagerWrapper;
    private boolean mAllowForceNavBarHandleOpaque;
    private int mAppearance;
    private final Lazy<AssistManager> mAssistManagerLazy;
    private boolean mAssistantAvailable;
    private boolean mAssistantTouchGestureEnabled;
    private AutoHideController mAutoHideController;
    private int mBehavior;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final CommandQueue mCommandQueue;
    private ContentResolver mContentResolver;
    private final Context mContext;
    private int mCurrentRotation;
    private final DeviceProvisionedController mDeviceProvisionedController;
    private int mDisabledFlags1;
    private int mDisabledFlags2;
    public int mDisplayId;
    private boolean mForceNavBarHandleOpaque;
    private final Handler mHandler;
    public boolean mHomeBlockedThisTouch;
    private Optional<Long> mHomeButtonLongPressDurationMs;
    private boolean mIsCurrentUserSetup;
    private boolean mIsOnDefaultDisplay;
    private long mLastLockToAppLongPress;
    private int mLayoutDirection;
    private LightBarController mLightBarController;
    private Locale mLocale;
    private boolean mLongPressHomeEnabled;
    private final MetricsLogger mMetricsLogger;
    private int mNavBarMode;
    private final NavigationBarOverlayController mNavbarOverlayController;
    private int mNavigationBarMode;
    private NavigationBarView mNavigationBarView;
    private final NavigationModeController mNavigationModeController;
    private final NotificationRemoteInputManager mNotificationRemoteInputManager;
    private final NotificationShadeDepthController mNotificationShadeDepthController;
    private QuickswitchOrientedNavHandle mOrientationHandle;
    private ViewTreeObserver.OnGlobalLayoutListener mOrientationHandleGlobalLayoutListener;
    private WindowManager.LayoutParams mOrientationParams;
    private final OverviewProxyService mOverviewProxyService;
    private final Optional<Pip> mPipOptional;
    private long mRecentInterceptTime;
    private final Optional<Recents> mRecentsOptional;
    private Bundle mSavedState;
    private final ShadeController mShadeController;
    private boolean mShowOrientedHandleForImmersiveMode;
    private final Optional<LegacySplitScreen> mSplitScreenOptional;
    private final Lazy<StatusBar> mStatusBarLazy;
    private final StatusBarStateController mStatusBarStateController;
    private final SysUiState mSysUiFlagsContainer;
    private final SystemActions mSystemActions;
    private Toast mToast;
    private boolean mTransientShown;
    private final UiEventLogger mUiEventLogger;
    private final UserTracker mUserTracker;
    private final WindowManager mWindowManager;
    private int mNavigationBarWindowState = 0;
    private int mNavigationIconHints = 0;
    private int mStartingQuickSwitchRotation = -1;
    private final AutoHideUiElement mAutoHideUiElement = new AutoHideUiElement() { // from class: com.android.systemui.navigationbar.NavigationBar.1
        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public void synchronizeState() {
            NavigationBar.this.checkNavBarModes();
        }

        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public boolean shouldHideOnTouch() {
            return !NavigationBar.this.mNotificationRemoteInputManager.getController().isRemoteInputActive();
        }

        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public boolean isVisible() {
            return NavigationBar.this.isTransientShown();
        }

        @Override // com.android.systemui.statusbar.AutoHideUiElement
        public void hide() {
            NavigationBar.this.clearTransient();
        }
    };
    private final OverviewProxyService.OverviewProxyListener mOverviewProxyListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.navigationbar.NavigationBar.2
        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onConnectionChanged(boolean z) {
            NavigationBar.this.mNavigationBarView.updateStates();
            NavigationBar.this.updateScreenPinningGestures();
            if (z) {
                NavigationBar.this.updateAssistantEntrypoints();
            }
        }

        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onPrioritizedRotation(int i) {
            NavigationBar.this.mStartingQuickSwitchRotation = i;
            if (i == -1) {
                NavigationBar.this.mShowOrientedHandleForImmersiveMode = false;
            }
            NavigationBar.this.orientSecondaryHomeHandle();
        }

        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void startAssistant(Bundle bundle) {
            ((AssistManager) NavigationBar.this.mAssistManagerLazy.get()).startAssist(bundle);
        }

        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onNavBarButtonAlphaChanged(float f, boolean z) {
            boolean z2;
            boolean z3;
            if (NavigationBar.this.mIsCurrentUserSetup && !QuickStepContract.isLegacyMode(NavigationBar.this.mNavBarMode)) {
                ButtonDispatcher buttonDispatcher = null;
                int i = 0;
                if (QuickStepContract.isGesturalMode(NavigationBar.this.mNavBarMode)) {
                    z3 = NavigationBar.this.mAllowForceNavBarHandleOpaque && NavigationBar.this.mForceNavBarHandleOpaque;
                    buttonDispatcher = NavigationBar.this.mNavigationBarView.getHomeHandle();
                    if (NavigationBar.this.getBarTransitions() != null) {
                        NavigationBar.this.getBarTransitions().setBackgroundOverrideAlpha(f);
                    }
                    z2 = false;
                } else {
                    if (QuickStepContract.isSwipeUpMode(NavigationBar.this.mNavBarMode)) {
                        buttonDispatcher = NavigationBar.this.mNavigationBarView.getBackButton();
                    }
                    z2 = z;
                    z3 = false;
                }
                if (buttonDispatcher == null) {
                    return;
                }
                if (!z3 && f <= 0.0f) {
                    i = 4;
                }
                buttonDispatcher.setVisibility(i);
                if (z3) {
                    f = 1.0f;
                }
                buttonDispatcher.setAlpha(f, z2);
            }
        }

        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onHomeRotationEnabled(boolean z) {
            NavigationBar.this.mNavigationBarView.getRotationButtonController().setHomeRotationEnabled(z);
        }

        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onOverviewShown(boolean z) {
            NavigationBar.this.mNavigationBarView.getRotationButtonController().setSkipOverrideUserLockPrefsOnce();
        }

        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public void onToggleRecentApps() {
            NavigationBar.this.mNavigationBarView.getRotationButtonController().setSkipOverrideUserLockPrefsOnce();
        }
    };
    private NavigationBarTransitions.DarkIntensityListener mOrientationHandleIntensityListener = new NavigationBarTransitions.DarkIntensityListener() { // from class: com.android.systemui.navigationbar.NavigationBar.3
        @Override // com.android.systemui.navigationbar.NavigationBarTransitions.DarkIntensityListener
        public void onDarkIntensity(float f) {
            NavigationBar.this.mOrientationHandle.setDarkIntensity(f);
        }
    };
    private final Runnable mAutoDim = new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda15
        @Override // java.lang.Runnable
        public final void run() {
            NavigationBar.this.lambda$new$0();
        }
    };
    private final Runnable mEnableLayoutTransitions = new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda14
        @Override // java.lang.Runnable
        public final void run() {
            NavigationBar.this.lambda$new$1();
        }
    };
    private final Runnable mOnVariableDurationHomeLongClick = new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda13
        @Override // java.lang.Runnable
        public final void run() {
            NavigationBar.this.lambda$new$2();
        }
    };
    private final ContentObserver mAssistContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.systemui.navigationbar.NavigationBar.4
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (NavigationBar.this.mNavigationBarView == null) {
                return;
            }
            NavigationBar.this.updateAssistantEntrypoints();
        }
    };
    private final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener = new AnonymousClass5();
    private final DeviceProvisionedController.DeviceProvisionedListener mUserSetupListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.navigationbar.NavigationBar.6
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onUserSetupChanged() {
            NavigationBar navigationBar = NavigationBar.this;
            navigationBar.mIsCurrentUserSetup = navigationBar.mDeviceProvisionedController.isCurrentUserSetup();
        }
    };
    private final NotificationShadeDepthController.DepthListener mDepthListener = new NotificationShadeDepthController.DepthListener() { // from class: com.android.systemui.navigationbar.NavigationBar.7
        boolean mHasBlurs;

        @Override // com.android.systemui.statusbar.NotificationShadeDepthController.DepthListener
        public void onWallpaperZoomOutChanged(float f) {
        }

        @Override // com.android.systemui.statusbar.NotificationShadeDepthController.DepthListener
        public void onBlurRadiusChanged(int i) {
            boolean z = i != 0;
            if (z == this.mHasBlurs) {
                return;
            }
            this.mHasBlurs = z;
            NavigationBar.this.mNavigationBarView.setWindowHasBlurs(z);
        }
    };
    private final AccessibilityManager.AccessibilityServicesStateChangeListener mAccessibilityListener = new AccessibilityManager.AccessibilityServicesStateChangeListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda11
        public final void onAccessibilityServicesStateChanged(AccessibilityManager accessibilityManager) {
            NavigationBar.this.updateAccessibilityServicesState(accessibilityManager);
        }
    };
    private final Consumer<Integer> mRotationWatcher = new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda16
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            NavigationBar.this.lambda$new$7((Integer) obj);
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.navigationbar.NavigationBar.8
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (NavigationBar.this.mNavigationBarView == null) {
                return;
            }
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
                NavigationBar.this.notifyNavigationBarScreenOn();
                NavigationBar.this.mNavigationBarView.onScreenStateChanged("android.intent.action.SCREEN_ON".equals(action));
            }
            if (!"android.intent.action.USER_SWITCHED".equals(action)) {
                return;
            }
            NavigationBar navigationBar = NavigationBar.this;
            navigationBar.updateAccessibilityServicesState(navigationBar.mAccessibilityManager);
        }
    };

    private static int barMode(boolean z, int i) {
        if (z) {
            return 1;
        }
        if ((i & 6) == 6) {
            return 3;
        }
        if ((i & 4) != 0) {
            return 6;
        }
        if ((i & 2) != 0) {
            return 4;
        }
        return (i & 64) != 0 ? 1 : 0;
    }

    private int deltaRotation(int i, int i2) {
        int i3 = i2 - i;
        return i3 < 0 ? i3 + 4 : i3;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum NavBarActionEvent implements UiEventLogger.UiEventEnum {
        NAVBAR_ASSIST_LONGPRESS(550);
        
        private final int mId;

        NavBarActionEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        getBarTransitions().setAutoDim(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.mNavigationBarView.setLayoutTransitionsEnabled(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        if (onHomeLongClick(this.mNavigationBarView.getHomeButton().getCurrentView())) {
            this.mNavigationBarView.getHomeButton().getCurrentView().performHapticFeedback(0, 1);
        }
    }

    /* renamed from: com.android.systemui.navigationbar.NavigationBar$5  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass5 implements DeviceConfig.OnPropertiesChangedListener {
        AnonymousClass5() {
        }

        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains("nav_bar_handle_force_opaque")) {
                NavigationBar.this.mForceNavBarHandleOpaque = properties.getBoolean("nav_bar_handle_force_opaque", true);
            }
            if (properties.getKeyset().contains("home_button_long_press_duration_ms")) {
                NavigationBar.this.mHomeButtonLongPressDurationMs = Optional.of(Long.valueOf(properties.getLong("home_button_long_press_duration_ms", 0L))).filter(NavigationBar$5$$ExternalSyntheticLambda0.INSTANCE);
                NavigationBar.this.reconfigureHomeLongClick();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onPropertiesChanged$0(Long l) {
            return l.longValue() != 0;
        }
    }

    public NavigationBar(Context context, WindowManager windowManager, Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, AccessibilityButtonModeObserver accessibilityButtonModeObserver, StatusBarStateController statusBarStateController, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<LegacySplitScreen> optional2, Optional<Recents> optional3, Lazy<StatusBar> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, SystemActions systemActions, Handler handler, NavigationBarOverlayController navigationBarOverlayController, UiEventLogger uiEventLogger, UserTracker userTracker) {
        this.mNavBarMode = 0;
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mAccessibilityManagerWrapper = accessibilityManagerWrapper;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mStatusBarStateController = statusBarStateController;
        this.mMetricsLogger = metricsLogger;
        this.mAssistManagerLazy = lazy;
        this.mSysUiFlagsContainer = sysUiState;
        this.mStatusBarLazy = lazy2;
        this.mShadeController = shadeController;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mOverviewProxyService = overviewProxyService;
        this.mNavigationModeController = navigationModeController;
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mCommandQueue = commandQueue;
        this.mPipOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mRecentsOptional = optional3;
        this.mSystemActions = systemActions;
        this.mHandler = handler;
        this.mNavbarOverlayController = navigationBarOverlayController;
        this.mUiEventLogger = uiEventLogger;
        this.mUserTracker = userTracker;
        this.mNotificationShadeDepthController = notificationShadeDepthController;
        this.mNavBarMode = navigationModeController.addListener(this);
        accessibilityButtonModeObserver.addListener(this);
    }

    public NavigationBarView getView() {
        return this.mNavigationBarView;
    }

    public View createView(Bundle bundle) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2019, 545521768, -3);
        layoutParams.token = new Binder();
        layoutParams.accessibilityTitle = this.mContext.getString(R$string.nav_bar);
        layoutParams.privateFlags |= 16777216;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.windowAnimations = 0;
        layoutParams.setTitle("NavigationBar" + this.mContext.getDisplayId());
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTrustedOverlay();
        NavigationBarFrame navigationBarFrame = (NavigationBarFrame) LayoutInflater.from(this.mContext).inflate(R$layout.navigation_bar_window, (ViewGroup) null);
        View inflate = LayoutInflater.from(navigationBarFrame.getContext()).inflate(R$layout.navigation_bar, navigationBarFrame);
        inflate.addOnAttachStateChangeListener(this);
        this.mNavigationBarView = (NavigationBarView) inflate.findViewById(R$id.navigation_bar_view);
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(navigationBarFrame, layoutParams);
        int displayId = this.mContext.getDisplayId();
        this.mDisplayId = displayId;
        this.mIsOnDefaultDisplay = displayId == 0;
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mAssistantAvailable = this.mAssistManagerLazy.get().getAssistInfoForUser(-2) != null;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        this.mContentResolver = contentResolver;
        contentResolver.registerContentObserver(Settings.Secure.getUriFor("assistant"), false, this.mAssistContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_long_press_home_enabled"), false, this.mAssistContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_touch_gesture_enabled"), false, this.mAssistContentObserver, -1);
        this.mAllowForceNavBarHandleOpaque = this.mContext.getResources().getBoolean(R$bool.allow_force_nav_bar_handle_opaque);
        this.mForceNavBarHandleOpaque = DeviceConfig.getBoolean("systemui", "nav_bar_handle_force_opaque", true);
        this.mHomeButtonLongPressDurationMs = Optional.of(Long.valueOf(DeviceConfig.getLong("systemui", "home_button_long_press_duration_ms", 0L))).filter(NavigationBar$$ExternalSyntheticLambda22.INSTANCE);
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        DeviceConfig.addOnPropertiesChangedListener("systemui", new MediaRoute2Provider$$ExternalSyntheticLambda0(handler), this.mOnPropertiesChangedListener);
        updateAssistantEntrypoints();
        if (bundle != null) {
            this.mDisabledFlags1 = bundle.getInt("disabled_state", 0);
            this.mDisabledFlags2 = bundle.getInt("disabled2_state", 0);
            this.mAppearance = bundle.getInt("appearance", 0);
            this.mBehavior = bundle.getInt("behavior", 0);
            this.mTransientShown = bundle.getBoolean("transient_state", false);
        }
        this.mSavedState = bundle;
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, false);
        this.mIsCurrentUserSetup = this.mDeviceProvisionedController.isCurrentUserSetup();
        this.mDeviceProvisionedController.addCallback(this.mUserSetupListener);
        this.mNotificationShadeDepthController.addListener(this.mDepthListener);
        setAccessibilityFloatingMenuModeIfNeeded();
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$3(Long l) {
        return l.longValue() != 0;
    }

    public void destroyView() {
        setAutoHideController(null);
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).removeViewImmediate(this.mNavigationBarView.getRootView());
        this.mNavigationModeController.removeListener(this);
        this.mAccessibilityButtonModeObserver.removeListener(this);
        this.mAccessibilityManagerWrapper.removeCallback(this.mAccessibilityListener);
        this.mContentResolver.unregisterContentObserver(this.mAssistContentObserver);
        this.mDeviceProvisionedController.removeCallback(this.mUserSetupListener);
        this.mNotificationShadeDepthController.removeListener(this.mDepthListener);
        DeviceConfig.removeOnPropertiesChangedListener(this.mOnPropertiesChangedListener);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        LightBarController lightBarController;
        AutoHideController autoHideController;
        Display display = view.getDisplay();
        this.mNavigationBarView.setComponents(this.mStatusBarLazy.get().getPanelController());
        this.mNavigationBarView.setDisabledFlags(this.mDisabledFlags1);
        this.mNavigationBarView.setOnVerticalChangedListener(new NavigationBarView.OnVerticalChangedListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda12
            @Override // com.android.systemui.navigationbar.NavigationBarView.OnVerticalChangedListener
            public final void onVerticalChanged(boolean z) {
                NavigationBar.this.onVerticalChanged(z);
            }
        });
        this.mNavigationBarView.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda9
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                boolean onNavigationTouch;
                onNavigationTouch = NavigationBar.this.onNavigationTouch(view2, motionEvent);
                return onNavigationTouch;
            }
        });
        if (this.mSavedState != null) {
            this.mNavigationBarView.getLightTransitionsController().restoreState(this.mSavedState);
        }
        this.mNavigationBarView.setNavigationIconHints(this.mNavigationIconHints);
        this.mNavigationBarView.setWindowVisible(isNavBarWindowVisible());
        this.mNavigationBarView.setBehavior(this.mBehavior);
        this.mAccessibilityManagerWrapper.addCallback(this.mAccessibilityListener);
        Optional<LegacySplitScreen> optional = this.mSplitScreenOptional;
        final NavigationBarView navigationBarView = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView);
        optional.ifPresent(new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda18
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NavigationBarView.this.registerDockedListener((LegacySplitScreen) obj);
            }
        });
        Optional<Pip> optional2 = this.mPipOptional;
        final NavigationBarView navigationBarView2 = this.mNavigationBarView;
        Objects.requireNonNull(navigationBarView2);
        optional2.ifPresent(new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda19
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NavigationBarView.this.registerPipExclusionBoundsChangeListener((Pip) obj);
            }
        });
        prepareNavigationBarView();
        checkNavBarModes();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mBroadcastReceiver, intentFilter, Handler.getMain(), UserHandle.ALL);
        notifyNavigationBarScreenOn();
        this.mOverviewProxyService.addCallback(this.mOverviewProxyListener);
        updateSystemUiStateFlags(-1);
        if (this.mIsOnDefaultDisplay) {
            RotationButtonController rotationButtonController = this.mNavigationBarView.getRotationButtonController();
            rotationButtonController.setRotationCallback(this.mRotationWatcher);
            if (display != null && rotationButtonController.isRotationLocked()) {
                rotationButtonController.setRotationLockedAtAngle(display.getRotation());
            }
        } else {
            this.mDisabledFlags2 |= 16;
        }
        setDisabled2Flags(this.mDisabledFlags2);
        initSecondaryHomeHandleForRotation();
        if (this.mIsOnDefaultDisplay) {
            lightBarController = (LightBarController) Dependency.get(LightBarController.class);
        } else {
            lightBarController = new LightBarController(this.mContext, (DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class), (BatteryController) Dependency.get(BatteryController.class), (NavigationModeController) Dependency.get(NavigationModeController.class));
        }
        setLightBarController(lightBarController);
        if (this.mIsOnDefaultDisplay) {
            autoHideController = (AutoHideController) Dependency.get(AutoHideController.class);
        } else {
            autoHideController = new AutoHideController(this.mContext, this.mHandler, (IWindowManager) Dependency.get(IWindowManager.class));
        }
        setAutoHideController(autoHideController);
        restoreAppearanceAndTransientState();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        this.mNavigationBarView.getRotationButtonController().setRotationCallback(null);
        this.mNavigationBarView.getBarTransitions().destroy();
        this.mNavigationBarView.getLightTransitionsController().destroy(this.mContext);
        this.mOverviewProxyService.removeCallback(this.mOverviewProxyListener);
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
        if (this.mOrientationHandle != null) {
            resetSecondaryHandle();
            getBarTransitions().removeDarkIntensityListener(this.mOrientationHandleIntensityListener);
            this.mWindowManager.removeView(this.mOrientationHandle);
            this.mOrientationHandle.getViewTreeObserver().removeOnGlobalLayoutListener(this.mOrientationHandleGlobalLayoutListener);
        }
        this.mHandler.removeCallbacks(this.mAutoDim);
        this.mHandler.removeCallbacks(this.mOnVariableDurationHomeLongClick);
        this.mHandler.removeCallbacks(this.mEnableLayoutTransitions);
        this.mNavigationBarView = null;
        this.mOrientationHandle = null;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt("disabled_state", this.mDisabledFlags1);
        bundle.putInt("disabled2_state", this.mDisabledFlags2);
        bundle.putInt("appearance", this.mAppearance);
        bundle.putInt("behavior", this.mBehavior);
        bundle.putBoolean("transient_state", this.mTransientShown);
        this.mNavigationBarView.getLightTransitionsController().saveState(bundle);
    }

    public void onConfigurationChanged(Configuration configuration) {
        int rotation;
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        int layoutDirectionFromLocale = TextUtils.getLayoutDirectionFromLocale(locale);
        if (!locale.equals(this.mLocale) || layoutDirectionFromLocale != this.mLayoutDirection) {
            this.mLocale = locale;
            this.mLayoutDirection = layoutDirectionFromLocale;
            refreshLayout(layoutDirectionFromLocale);
        }
        repositionNavigationBar();
        if (!canShowSecondaryHandle() || (rotation = configuration.windowConfiguration.getRotation()) == this.mCurrentRotation) {
            return;
        }
        this.mCurrentRotation = rotation;
        orientSecondaryHomeHandle();
    }

    private void initSecondaryHomeHandleForRotation() {
        if (this.mNavBarMode != 2) {
            return;
        }
        QuickswitchOrientedNavHandle quickswitchOrientedNavHandle = new QuickswitchOrientedNavHandle(this.mContext);
        this.mOrientationHandle = quickswitchOrientedNavHandle;
        quickswitchOrientedNavHandle.setId(R$id.secondary_home_handle);
        getBarTransitions().addDarkIntensityListener(this.mOrientationHandleIntensityListener);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(0, 0, 2024, 536871224, -3);
        this.mOrientationParams = layoutParams;
        layoutParams.setTitle("SecondaryHomeHandle" + this.mContext.getDisplayId());
        WindowManager.LayoutParams layoutParams2 = this.mOrientationParams;
        layoutParams2.privateFlags = layoutParams2.privateFlags | 64;
        this.mWindowManager.addView(this.mOrientationHandle, layoutParams2);
        this.mOrientationHandle.setVisibility(8);
        this.mOrientationParams.setFitInsetsTypes(0);
        this.mOrientationHandleGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda10
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                NavigationBar.this.lambda$initSecondaryHomeHandleForRotation$4();
            }
        };
        this.mOrientationHandle.getViewTreeObserver().addOnGlobalLayoutListener(this.mOrientationHandleGlobalLayoutListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initSecondaryHomeHandleForRotation$4() {
        if (this.mStartingQuickSwitchRotation == -1) {
            return;
        }
        RectF computeHomeHandleBounds = this.mOrientationHandle.computeHomeHandleBounds();
        this.mOrientationHandle.mapRectFromViewToScreenCoords(computeHomeHandleBounds, true);
        Rect rect = new Rect();
        computeHomeHandleBounds.roundOut(rect);
        this.mNavigationBarView.setOrientedHandleSamplingRegion(rect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x009e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void orientSecondaryHomeHandle() {
        int width;
        int height;
        if (!canShowSecondaryHandle()) {
            return;
        }
        if (this.mStartingQuickSwitchRotation == -1 || ((Boolean) this.mSplitScreenOptional.map(NavigationBar$$ExternalSyntheticLambda21.INSTANCE).orElse(Boolean.FALSE)).booleanValue()) {
            resetSecondaryHandle();
            return;
        }
        int deltaRotation = deltaRotation(this.mCurrentRotation, this.mStartingQuickSwitchRotation);
        if (this.mStartingQuickSwitchRotation == -1 || deltaRotation == -1) {
            Log.d("NavigationBar", "secondary nav delta rotation: " + deltaRotation + " current: " + this.mCurrentRotation + " starting: " + this.mStartingQuickSwitchRotation);
        }
        Rect bounds = this.mWindowManager.getCurrentWindowMetrics().getBounds();
        this.mOrientationHandle.setDeltaRotation(deltaRotation);
        int i = 3;
        if (deltaRotation != 0) {
            if (deltaRotation != 1) {
                if (deltaRotation != 2) {
                    if (deltaRotation != 3) {
                        height = 0;
                        width = 0;
                        WindowManager.LayoutParams layoutParams = this.mOrientationParams;
                        if (deltaRotation == 0) {
                            i = 80;
                        } else if (deltaRotation != 1) {
                            i = 5;
                        }
                        layoutParams.gravity = i;
                        layoutParams.height = height;
                        layoutParams.width = width;
                        this.mWindowManager.updateViewLayout(this.mOrientationHandle, layoutParams);
                        this.mNavigationBarView.setVisibility(8);
                        this.mOrientationHandle.setVisibility(0);
                    }
                }
            }
            height = bounds.height();
            width = this.mNavigationBarView.getHeight();
            WindowManager.LayoutParams layoutParams2 = this.mOrientationParams;
            if (deltaRotation == 0) {
            }
            layoutParams2.gravity = i;
            layoutParams2.height = height;
            layoutParams2.width = width;
            this.mWindowManager.updateViewLayout(this.mOrientationHandle, layoutParams2);
            this.mNavigationBarView.setVisibility(8);
            this.mOrientationHandle.setVisibility(0);
        }
        if (!this.mShowOrientedHandleForImmersiveMode) {
            resetSecondaryHandle();
            return;
        }
        width = bounds.width();
        height = this.mNavigationBarView.getHeight();
        WindowManager.LayoutParams layoutParams22 = this.mOrientationParams;
        if (deltaRotation == 0) {
        }
        layoutParams22.gravity = i;
        layoutParams22.height = height;
        layoutParams22.width = width;
        this.mWindowManager.updateViewLayout(this.mOrientationHandle, layoutParams22);
        this.mNavigationBarView.setVisibility(8);
        this.mOrientationHandle.setVisibility(0);
    }

    private void resetSecondaryHandle() {
        QuickswitchOrientedNavHandle quickswitchOrientedNavHandle = this.mOrientationHandle;
        if (quickswitchOrientedNavHandle != null) {
            quickswitchOrientedNavHandle.setVisibility(8);
        }
        this.mNavigationBarView.setVisibility(0);
        this.mNavigationBarView.setOrientedHandleSamplingRegion(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reconfigureHomeLongClick() {
        if (this.mNavigationBarView.getHomeButton().getCurrentView() == null) {
            return;
        }
        if (this.mHomeButtonLongPressDurationMs.isPresent() || !this.mLongPressHomeEnabled) {
            this.mNavigationBarView.getHomeButton().getCurrentView().setLongClickable(false);
            this.mNavigationBarView.getHomeButton().getCurrentView().setHapticFeedbackEnabled(false);
            this.mNavigationBarView.getHomeButton().setOnLongClickListener(null);
            return;
        }
        this.mNavigationBarView.getHomeButton().getCurrentView().setLongClickable(true);
        this.mNavigationBarView.getHomeButton().getCurrentView().setHapticFeedbackEnabled(true);
        this.mNavigationBarView.getHomeButton().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda3
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                return NavigationBar.this.onHomeLongClick(view);
            }
        });
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBar (displayId=" + this.mDisplayId + "):");
        StringBuilder sb = new StringBuilder();
        sb.append("  mStartingQuickSwitchRotation=");
        sb.append(this.mStartingQuickSwitchRotation);
        printWriter.println(sb.toString());
        printWriter.println("  mCurrentRotation=" + this.mCurrentRotation);
        printWriter.println("  mHomeButtonLongPressDurationMs=" + this.mHomeButtonLongPressDurationMs);
        printWriter.println("  mLongPressHomeEnabled=" + this.mLongPressHomeEnabled);
        printWriter.println("  mAssistantTouchGestureEnabled=" + this.mAssistantTouchGestureEnabled);
        printWriter.println("  mNavigationBarWindowState=" + StatusBarManager.windowStateToString(this.mNavigationBarWindowState));
        printWriter.println("  mNavigationBarMode=" + BarTransitions.modeToString(this.mNavigationBarMode));
        StatusBar.dumpBarTransitions(printWriter, "mNavigationBarView", this.mNavigationBarView.getBarTransitions());
        this.mNavigationBarView.dump(printWriter);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        int i4;
        if (i != this.mDisplayId) {
            return;
        }
        boolean z2 = (i2 & 2) != 0;
        int i5 = this.mNavigationIconHints;
        if (i3 == 0 || i3 == 1 || i3 == 2) {
            if (z2) {
                i4 = i5 | 1;
            }
            i4 = i5 & (-2);
        } else {
            if (i3 != 3) {
                i4 = i5;
            }
            i4 = i5 & (-2);
        }
        int i6 = z ? 2 | i4 : i4 & (-3);
        if (i6 == i5) {
            return;
        }
        this.mNavigationIconHints = i6;
        this.mNavigationBarView.setNavigationIconHints(i6);
        checkBarModes();
        updateSystemUiStateFlags(-1);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setWindowState(int i, int i2, int i3) {
        if (i == this.mDisplayId && i2 == 2 && this.mNavigationBarWindowState != i3) {
            this.mNavigationBarWindowState = i3;
            updateSystemUiStateFlags(-1);
            this.mShowOrientedHandleForImmersiveMode = i3 == 2;
            if (this.mOrientationHandle != null && this.mStartingQuickSwitchRotation != -1) {
                orientSecondaryHomeHandle();
            }
            this.mNavigationBarView.setWindowVisible(isNavBarWindowVisible());
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onRotationProposal(int i, boolean z) {
        if (!this.mNavigationBarView.isAttachedToWindow()) {
            return;
        }
        int rotation = this.mNavigationBarView.getDisplay().getRotation();
        boolean hasDisable2RotateSuggestionFlag = RotationButtonController.hasDisable2RotateSuggestionFlag(this.mDisabledFlags2);
        RotationButtonController rotationButtonController = this.mNavigationBarView.getRotationButtonController();
        rotationButtonController.getRotationButton();
        if (hasDisable2RotateSuggestionFlag) {
            return;
        }
        rotationButtonController.onRotationProposal(i, rotation, z);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onRecentsAnimationStateChanged(boolean z) {
        if (z) {
            this.mNavbarOverlayController.setButtonState(false, true);
        }
        this.mNavigationBarView.getRotationButtonController().setRecentsAnimationRunning(z);
    }

    public void restoreAppearanceAndTransientState() {
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        this.mNavigationBarMode = barMode;
        checkNavBarModes();
        AutoHideController autoHideController = this.mAutoHideController;
        if (autoHideController != null) {
            autoHideController.touchAutoHide();
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null) {
            lightBarController.onNavigationBarAppearanceChanged(this.mAppearance, true, barMode, false);
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, boolean z2) {
        if (i != this.mDisplayId) {
            return;
        }
        boolean z3 = false;
        if (this.mAppearance != i2) {
            this.mAppearance = i2;
            z3 = updateBarMode(barMode(this.mTransientShown, i2));
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null) {
            lightBarController.onNavigationBarAppearanceChanged(i2, z3, this.mNavigationBarMode, z);
        }
        if (this.mBehavior == i3) {
            return;
        }
        this.mBehavior = i3;
        this.mNavigationBarView.setBehavior(i3);
        updateSystemUiStateFlags(-1);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 1) && !this.mTransientShown) {
            this.mTransientShown = true;
            handleTransientChanged();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 1)) {
            clearTransient();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearTransient() {
        if (this.mTransientShown) {
            this.mTransientShown = false;
            handleTransientChanged();
        }
    }

    private void handleTransientChanged() {
        LightBarController lightBarController;
        this.mNavigationBarView.onTransientStateChanged(this.mTransientShown);
        int barMode = barMode(this.mTransientShown, this.mAppearance);
        if (!updateBarMode(barMode) || (lightBarController = this.mLightBarController) == null) {
            return;
        }
        lightBarController.onNavigationBarModeChanged(barMode);
    }

    private boolean updateBarMode(int i) {
        if (this.mNavigationBarMode != i) {
            this.mNavigationBarMode = i;
            checkNavBarModes();
            AutoHideController autoHideController = this.mAutoHideController;
            if (autoHideController == null) {
                return true;
            }
            autoHideController.touchAutoHide();
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void disable(int i, int i2, int i3, boolean z) {
        int i4;
        if (i != this.mDisplayId) {
            return;
        }
        int i5 = 56623104 & i2;
        if (i5 != this.mDisabledFlags1) {
            this.mDisabledFlags1 = i5;
            this.mNavigationBarView.setDisabledFlags(i2);
            updateScreenPinningGestures();
        }
        if (!this.mIsOnDefaultDisplay || (i4 = i3 & 16) == this.mDisabledFlags2) {
            return;
        }
        this.mDisabledFlags2 = i4;
        setDisabled2Flags(i4);
    }

    private void setDisabled2Flags(int i) {
        this.mNavigationBarView.getRotationButtonController().onDisable2FlagChanged(i);
    }

    private void refreshLayout(int i) {
        this.mNavigationBarView.setLayoutDirection(i);
    }

    private boolean shouldDisableNavbarGestures() {
        return !this.mDeviceProvisionedController.isDeviceProvisioned() || (this.mDisabledFlags1 & 33554432) != 0;
    }

    private void repositionNavigationBar() {
        if (!this.mNavigationBarView.isAttachedToWindow()) {
            return;
        }
        prepareNavigationBarView();
        this.mWindowManager.updateViewLayout((View) this.mNavigationBarView.getParent(), ((View) this.mNavigationBarView.getParent()).getLayoutParams());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreenPinningGestures() {
        View.OnLongClickListener onLongClickListener;
        boolean isScreenPinningActive = ActivityManagerWrapper.getInstance().isScreenPinningActive();
        ButtonDispatcher backButton = this.mNavigationBarView.getBackButton();
        ButtonDispatcher recentsButton = this.mNavigationBarView.getRecentsButton();
        if (isScreenPinningActive) {
            if (this.mNavigationBarView.isRecentsButtonVisible()) {
                onLongClickListener = new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda6
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        boolean onLongPressBackRecents;
                        onLongPressBackRecents = NavigationBar.this.onLongPressBackRecents(view);
                        return onLongPressBackRecents;
                    }
                };
            } else {
                onLongClickListener = new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda4
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        boolean onLongPressBackHome;
                        onLongPressBackHome = NavigationBar.this.onLongPressBackHome(view);
                        return onLongPressBackHome;
                    }
                };
            }
            backButton.setOnLongClickListener(onLongClickListener);
            recentsButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda6
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    boolean onLongPressBackRecents;
                    onLongPressBackRecents = NavigationBar.this.onLongPressBackRecents(view);
                    return onLongPressBackRecents;
                }
            });
        } else {
            backButton.setOnLongClickListener(null);
            recentsButton.setOnLongClickListener(null);
        }
        backButton.setLongClickable(isScreenPinningActive);
        recentsButton.setLongClickable(isScreenPinningActive);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyNavigationBarScreenOn() {
        this.mNavigationBarView.updateNavButtonIcons();
    }

    private void prepareNavigationBarView() {
        this.mNavigationBarView.reorient();
        ButtonDispatcher recentsButton = this.mNavigationBarView.getRecentsButton();
        recentsButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationBar.this.onRecentsClick(view);
            }
        });
        recentsButton.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda8
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean onRecentsTouch;
                onRecentsTouch = NavigationBar.this.onRecentsTouch(view, motionEvent);
                return onRecentsTouch;
            }
        });
        this.mNavigationBarView.getHomeButton().setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda7
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return NavigationBar.this.onHomeTouch(view, motionEvent);
            }
        });
        reconfigureHomeLongClick();
        ButtonDispatcher accessibilityButton = this.mNavigationBarView.getAccessibilityButton();
        accessibilityButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationBar.this.onAccessibilityClick(view);
            }
        });
        accessibilityButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda5
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean onAccessibilityLongClick;
                onAccessibilityLongClick = NavigationBar.this.onAccessibilityLongClick(view);
                return onAccessibilityLongClick;
            }
        });
        updateAccessibilityServicesState(this.mAccessibilityManager);
        this.mNavigationBarView.getImeSwitchButton().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NavigationBar.this.onImeSwitcherClick(view);
            }
        });
        updateScreenPinningGestures();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onHomeTouch(View view, MotionEvent motionEvent) {
        if (!this.mHomeBlockedThisTouch || motionEvent.getActionMasked() == 0) {
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mHomeBlockedThisTouch = false;
                TelecomManager telecomManager = (TelecomManager) this.mContext.getSystemService(TelecomManager.class);
                if (telecomManager != null && telecomManager.isRinging() && this.mStatusBarLazy.get().isKeyguardShowing()) {
                    Log.i("NavigationBar", "Ignoring HOME; there's a ringing incoming call. No heads up");
                    this.mHomeBlockedThisTouch = true;
                    return true;
                } else if (this.mLongPressHomeEnabled) {
                    this.mHomeButtonLongPressDurationMs.ifPresent(new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda17
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            NavigationBar.this.lambda$onHomeTouch$5((Long) obj);
                        }
                    });
                }
            } else if (action == 1 || action == 3) {
                this.mHandler.removeCallbacks(this.mOnVariableDurationHomeLongClick);
                this.mStatusBarLazy.get().awakenDreams();
            }
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onHomeTouch$5(Long l) {
        this.mHandler.postDelayed(this.mOnVariableDurationHomeLongClick, l.longValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onVerticalChanged(boolean z) {
        this.mStatusBarLazy.get().setQsScrimEnabled(!z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onNavigationTouch(View view, MotionEvent motionEvent) {
        AutoHideController autoHideController = this.mAutoHideController;
        if (autoHideController != null) {
            autoHideController.checkUserAutoHide(motionEvent);
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean onHomeLongClick(View view) {
        if (!this.mNavigationBarView.isRecentsButtonVisible() && ActivityManagerWrapper.getInstance().isScreenPinningActive()) {
            return onLongPressBackHome(view);
        }
        if (shouldDisableNavbarGestures()) {
            return false;
        }
        this.mMetricsLogger.action(239);
        this.mUiEventLogger.log(NavBarActionEvent.NAVBAR_ASSIST_LONGPRESS);
        Bundle bundle = new Bundle();
        bundle.putInt("invocation_type", 5);
        this.mAssistManagerLazy.get().startAssist(bundle);
        this.mStatusBarLazy.get().awakenDreams();
        this.mNavigationBarView.abortCurrentGesture();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onRecentsTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (action == 0) {
            this.mCommandQueue.preloadRecentApps();
            return false;
        } else if (action == 3) {
            this.mCommandQueue.cancelPreloadRecentApps();
            return false;
        } else if (action != 1 || view.isPressed()) {
            return false;
        } else {
            this.mCommandQueue.cancelPreloadRecentApps();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRecentsClick(View view) {
        if (GameModeHelper.getInstance(this.mContext).isMistouchPreventionOn() && SystemClock.uptimeMillis() - this.mRecentInterceptTime >= 2000) {
            this.mRecentInterceptTime = SystemClock.uptimeMillis();
            showToast();
            return;
        }
        if (LatencyTracker.isEnabled(this.mContext)) {
            LatencyTracker.getInstance(this.mContext).onActionStart(1);
        }
        this.mStatusBarLazy.get().awakenDreams();
        this.mCommandQueue.toggleRecentApps();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onImeSwitcherClick(View view) {
        ((InputMethodManager) this.mContext.getSystemService(InputMethodManager.class)).showInputMethodPickerFromSystem(true, this.mDisplayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onLongPressBackHome(View view) {
        return onLongPressNavigationButtons(view, R$id.back, R$id.home);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onLongPressBackRecents(View view) {
        return onLongPressNavigationButtons(view, R$id.back, R$id.recent_apps);
    }

    private boolean onLongPressNavigationButtons(View view, int i, int i2) {
        boolean z;
        ButtonDispatcher homeButton;
        try {
            IActivityTaskManager service = ActivityTaskManager.getService();
            boolean isTouchExplorationEnabled = this.mAccessibilityManager.isTouchExplorationEnabled();
            boolean isInLockTaskMode = service.isInLockTaskMode();
            if (isInLockTaskMode && !isTouchExplorationEnabled) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - this.mLastLockToAppLongPress >= 200) {
                    if (view.getId() == i) {
                        if (i2 == R$id.recent_apps) {
                            homeButton = this.mNavigationBarView.getRecentsButton();
                        } else {
                            homeButton = this.mNavigationBarView.getHomeButton();
                        }
                        if (!homeButton.getCurrentView().isPressed()) {
                            z = true;
                            this.mLastLockToAppLongPress = currentTimeMillis;
                        }
                    }
                    z = false;
                    this.mLastLockToAppLongPress = currentTimeMillis;
                } else {
                    service.stopSystemLockTaskMode();
                    this.mNavigationBarView.updateNavButtonIcons();
                    return true;
                }
            } else if (view.getId() == i) {
                z = true;
            } else if (!isTouchExplorationEnabled || !isInLockTaskMode) {
                if (view.getId() == i2) {
                    if (i2 == R$id.recent_apps) {
                        return onLongPressRecents();
                    }
                    return onHomeLongClick(this.mNavigationBarView.getHomeButton().getCurrentView());
                }
                z = false;
            } else {
                service.stopSystemLockTaskMode();
                this.mNavigationBarView.updateNavButtonIcons();
                return true;
            }
            if (z) {
                KeyButtonView keyButtonView = (KeyButtonView) view;
                keyButtonView.sendEvent(0, 128);
                keyButtonView.sendAccessibilityEvent(2);
                return true;
            }
        } catch (RemoteException e) {
            Log.d("NavigationBar", "Unable to reach activity manager", e);
        }
        return false;
    }

    private boolean onLongPressRecents() {
        if (this.mRecentsOptional.isPresent() || !ActivityTaskManager.supportsMultiWindow(this.mContext) || ActivityManager.isLowRamDeviceStatic() || this.mOverviewProxyService.getProxy() != null || !((Boolean) this.mSplitScreenOptional.map(NavigationBar$$ExternalSyntheticLambda20.INSTANCE).orElse(Boolean.FALSE)).booleanValue()) {
            return false;
        }
        return this.mStatusBarLazy.get().toggleSplitScreenMode(271, 286);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Boolean lambda$onLongPressRecents$6(LegacySplitScreen legacySplitScreen) {
        return Boolean.valueOf(legacySplitScreen.getDividerView().getSnapAlgorithm().isSplitScreenFeasible());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessibilityClick(View view) {
        Display display = view.getDisplay();
        this.mAccessibilityManager.notifyAccessibilityButtonClicked(display != null ? display.getDisplayId() : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onAccessibilityLongClick(View view) {
        Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
        intent.addFlags(268468224);
        intent.setClassName("android", AccessibilityButtonChooserActivity.class.getName());
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateAccessibilityServicesState(AccessibilityManager accessibilityManager) {
        boolean z = true;
        int a11yButtonState = getA11yButtonState(new boolean[1]);
        boolean z2 = (a11yButtonState & 16) != 0;
        if ((a11yButtonState & 32) == 0) {
            z = false;
        }
        this.mNavigationBarView.setAccessibilityButtonState(z2, z);
        updateSystemUiStateFlags(a11yButtonState);
    }

    private void setAccessibilityFloatingMenuModeIfNeeded() {
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            Settings.Secure.putIntForUser(this.mContentResolver, "accessibility_button_mode", 1, -2);
        }
    }

    public void updateSystemUiStateFlags(int i) {
        if (i < 0) {
            i = getA11yButtonState(null);
        }
        boolean z = false;
        boolean z2 = (i & 16) != 0;
        boolean z3 = (i & 32) != 0;
        SysUiState flag = this.mSysUiFlagsContainer.setFlag(16, z2).setFlag(32, z3).setFlag(2, !isNavBarWindowVisible());
        if ((this.mNavigationIconHints & 1) != 0) {
            z = true;
        }
        flag.setFlag(262144, z).setFlag(131072, allowSystemGestureIgnoringBarVisibility()).commitUpdate(this.mDisplayId);
        registerAction(z2, 11);
        registerAction(z3, 12);
    }

    private void registerAction(boolean z, int i) {
        if (z) {
            this.mSystemActions.register(i);
        } else {
            this.mSystemActions.unregister(i);
        }
    }

    public int getA11yButtonState(boolean[] zArr) {
        int i;
        List<AccessibilityServiceInfo> enabledAccessibilityServiceList = this.mAccessibilityManager.getEnabledAccessibilityServiceList(-1);
        int i2 = 0;
        int size = this.mAccessibilityManager.getAccessibilityShortcutTargets(0).size();
        int size2 = enabledAccessibilityServiceList.size() - 1;
        boolean z = false;
        while (true) {
            i = 16;
            if (size2 < 0) {
                break;
            }
            int i3 = enabledAccessibilityServiceList.get(size2).feedbackType;
            if (i3 != 0 && i3 != 16) {
                z = true;
            }
            size2--;
        }
        if (zArr != null) {
            zArr[0] = z;
        }
        if (this.mAccessibilityButtonModeObserver.getCurrentAccessibilityButtonMode() == 1) {
            return 0;
        }
        if (size < 1) {
            i = 0;
        }
        if (size >= 2) {
            i2 = 32;
        }
        return i | i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAssistantEntrypoints() {
        boolean z = true;
        this.mAssistantAvailable = this.mAssistManagerLazy.get().getAssistInfoForUser(-2) != null;
        this.mLongPressHomeEnabled = Settings.Secure.getIntForUser(this.mContentResolver, "assist_long_press_home_enabled", this.mContext.getResources().getBoolean(17891367) ? 1 : 0, this.mUserTracker.getUserId()) != 0;
        this.mAssistantTouchGestureEnabled = Settings.Secure.getIntForUser(this.mContentResolver, "assist_touch_gesture_enabled", this.mContext.getResources().getBoolean(17891368) ? 1 : 0, this.mUserTracker.getUserId()) != 0;
        if (this.mOverviewProxyService.getProxy() != null) {
            try {
                IOverviewProxy proxy = this.mOverviewProxyService.getProxy();
                if (!this.mAssistantAvailable || !this.mAssistantTouchGestureEnabled) {
                    z = false;
                }
                proxy.onAssistantAvailable(z);
            } catch (RemoteException unused) {
                Log.w("NavigationBar", "Unable to send assistant availability data to launcher");
            }
        }
        reconfigureHomeLongClick();
    }

    public void touchAutoDim() {
        getBarTransitions().setAutoDim(false);
        this.mHandler.removeCallbacks(this.mAutoDim);
        int state = this.mStatusBarStateController.getState();
        if (state == 1 || state == 2) {
            return;
        }
        this.mHandler.postDelayed(this.mAutoDim, 2250L);
    }

    public void setLightBarController(LightBarController lightBarController) {
        this.mLightBarController = lightBarController;
        if (lightBarController != null) {
            lightBarController.setNavigationBar(this.mNavigationBarView.getLightTransitionsController());
        }
    }

    private void setAutoHideController(AutoHideController autoHideController) {
        this.mAutoHideController = autoHideController;
        if (autoHideController != null) {
            autoHideController.setNavigationBar(this.mAutoHideUiElement);
        }
        this.mNavigationBarView.setAutoHideController(autoHideController);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isTransientShown() {
        return this.mTransientShown;
    }

    private void checkBarModes() {
        if (this.mIsOnDefaultDisplay) {
            this.mStatusBarLazy.get().checkBarModes();
        } else {
            checkNavBarModes();
        }
    }

    public boolean isNavBarWindowVisible() {
        return this.mNavigationBarWindowState == 0;
    }

    private boolean allowSystemGestureIgnoringBarVisibility() {
        return this.mBehavior != 2;
    }

    public void checkNavBarModes() {
        this.mNavigationBarView.getBarTransitions().transitionTo(this.mNavigationBarMode, this.mStatusBarLazy.get().isDeviceInteractive() && this.mNavigationBarWindowState != 2);
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
        if (!QuickStepContract.isGesturalMode(i) && getBarTransitions() != null) {
            getBarTransitions().setBackgroundOverrideAlpha(1.0f);
        }
        updateScreenPinningGestures();
        setAccessibilityFloatingMenuModeIfNeeded();
        if (!canShowSecondaryHandle()) {
            resetSecondaryHandle();
        }
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonModeObserver.ModeChangedListener
    public void onAccessibilityButtonModeChanged(int i) {
        updateAccessibilityServicesState(this.mAccessibilityManager);
    }

    public void disableAnimationsDuringHide(long j) {
        this.mNavigationBarView.setLayoutTransitionsEnabled(false);
        this.mHandler.postDelayed(this.mEnableLayoutTransitions, j + 448);
    }

    public void transitionTo(int i, boolean z) {
        getBarTransitions().transitionTo(i, z);
    }

    public NavigationBarTransitions getBarTransitions() {
        return this.mNavigationBarView.getBarTransitions();
    }

    public void finishBarAnimations() {
        this.mNavigationBarView.getBarTransitions().finishAnimations();
    }

    private boolean canShowSecondaryHandle() {
        return this.mNavBarMode == 2 && this.mOrientationHandle != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(Integer num) {
        if (this.mNavigationBarView.needsReorient(num.intValue())) {
            repositionNavigationBar();
        }
    }

    int getNavigationIconHints() {
        return this.mNavigationIconHints;
    }

    private void showToast() {
        Toast makeText = Toast.makeText(this.mContext, R$string.swipe_again, 0);
        this.mToast = makeText;
        makeText.show();
    }
}
