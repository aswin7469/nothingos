package com.android.systemui.navigationbar;

import android.app.StatusBarManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.LatencyTracker;
import com.android.internal.view.AppearanceRegion;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.p019wm.shell.pip.Pip;
import com.android.systemui.C1893R;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.globalactions.C2137x58d1e4b7;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavBarHelper;
import com.android.systemui.navigationbar.NavigationBarComponent;
import com.android.systemui.navigationbar.NavigationBarTransitions;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.shared.recents.utilities.Utilities;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.AutoHideUiElement;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.ViewController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.NavigationBarEx;
import com.nothing.systemui.util.NTLogUtil;
import dagger.Lazy;
import java.p026io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;

@NavigationBarComponent.NavigationBarScope
public class NavigationBar extends ViewController<NavigationBarView> implements CommandQueue.Callbacks {
    private static final long AUTODIM_TIMEOUT_MS = 2250;
    private static final boolean DEBUG = false;
    private static final String EXTRA_APPEARANCE = "appearance";
    private static final String EXTRA_BEHAVIOR = "behavior";
    private static final String EXTRA_DISABLE2_STATE = "disabled2_state";
    private static final String EXTRA_DISABLE_STATE = "disabled_state";
    private static final String EXTRA_TRANSIENT_STATE = "transient_state";
    private static final int LOCK_TO_APP_GESTURE_TOLERENCE = 200;
    public static final String TAG = "NavigationBar";
    private final AccessibilityManager mAccessibilityManager;
    /* access modifiers changed from: private */
    public boolean mAllowForceNavBarHandleOpaque;
    private int mAppearance;
    /* access modifiers changed from: private */
    public final Lazy<AssistManager> mAssistManagerLazy;
    private final Runnable mAutoDim = new NavigationBar$$ExternalSyntheticLambda17(this);
    private AutoHideController mAutoHideController;
    private final AutoHideController.Factory mAutoHideControllerFactory;
    private final AutoHideUiElement mAutoHideUiElement = new AutoHideUiElement() {
        public void synchronizeState() {
            NavigationBar.this.checkNavBarModes();
        }

        public boolean shouldHideOnTouch() {
            return !NavigationBar.this.mNotificationRemoteInputManager.isRemoteInputActive();
        }

        public boolean isVisible() {
            return NavigationBar.this.isTransientShown();
        }

        public void hide() {
            NavigationBar.this.clearTransient();
        }
    };
    private final Optional<BackAnimation> mBackAnimation;
    private int mBehavior;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (NavigationBar.this.mView != null) {
                String action = intent.getAction();
                if ("android.intent.action.SCREEN_OFF".equals(action) || "android.intent.action.SCREEN_ON".equals(action)) {
                    NavigationBar.this.notifyNavigationBarScreenOn();
                    boolean equals = "android.intent.action.SCREEN_ON".equals(action);
                    ((NavigationBarView) NavigationBar.this.mView).onScreenStateChanged(equals);
                    if (!equals) {
                        NavigationBar.this.mRegionSamplingHelper.stop();
                    } else if (NavigationBar.this.mEx.isRegionSamplingAvailable(NavigationBar.this.mNavigationBarTransitions.getMode())) {
                        NavigationBar.this.mRegionSamplingHelper.start(NavigationBar.this.mSamplingBounds);
                    }
                }
                if ("android.intent.action.USER_SWITCHED".equals(action)) {
                    NavigationBar.this.updateAccessibilityStateFlags();
                }
            }
        }
    };
    private final Lazy<Optional<CentralSurfaces>> mCentralSurfacesOptionalLazy;
    private final CommandQueue mCommandQueue;
    private final Context mContext;
    private int mCurrentRotation;
    /* access modifiers changed from: private */
    public final DeadZone mDeadZone;
    private final NotificationShadeDepthController.DepthListener mDepthListener = new NotificationShadeDepthController.DepthListener() {
        boolean mHasBlurs;

        public void onWallpaperZoomOutChanged(float f) {
        }

        public void onBlurRadiusChanged(int i) {
            boolean z = i != 0;
            if (z != this.mHasBlurs) {
                this.mHasBlurs = z;
                NavigationBar.this.mRegionSamplingHelper.setWindowHasBlurs(z);
            }
        }
    };
    private final DeviceConfigProxy mDeviceConfigProxy;
    /* access modifiers changed from: private */
    public final DeviceProvisionedController mDeviceProvisionedController;
    private int mDisabledFlags1;
    private int mDisabledFlags2;
    public int mDisplayId;
    private final EdgeBackGestureHandler mEdgeBackGestureHandler;
    private final Runnable mEnableLayoutTransitions = new NavigationBar$$ExternalSyntheticLambda18(this);
    /* access modifiers changed from: private */
    public NavigationBarEx mEx = ((NavigationBarEx) NTDependencyEx.get(NavigationBarEx.class));
    /* access modifiers changed from: private */
    public boolean mForceNavBarHandleOpaque;
    private NavigationBarFrame mFrame;
    private final Handler mHandler;
    public boolean mHomeBlockedThisTouch;
    /* access modifiers changed from: private */
    public Optional<Long> mHomeButtonLongPressDurationMs;
    /* access modifiers changed from: private */
    public boolean mImeVisible;
    private final InputMethodManager mInputMethodManager;
    /* access modifiers changed from: private */
    public boolean mIsCurrentUserSetup;
    private boolean mIsOnDefaultDisplay;
    private long mLastLockToAppLongPress;
    private int mLayoutDirection;
    private LightBarController mLightBarController;
    private final LightBarController.Factory mLightBarControllerFactory;
    private Locale mLocale;
    /* access modifiers changed from: private */
    public boolean mLongPressHomeEnabled;
    private final AutoHideController mMainAutoHideController;
    private final LightBarController mMainLightBarController;
    private final MetricsLogger mMetricsLogger;
    private final NavigationModeController.ModeChangedListener mModeChangedListener;
    /* access modifiers changed from: private */
    public final NavBarHelper mNavBarHelper;
    /* access modifiers changed from: private */
    public int mNavBarMode = 0;
    private final int mNavColorSampleMargin;
    private final NavBarHelper.NavbarTaskbarStateUpdater mNavbarTaskbarStateUpdater = new NavBarHelper.NavbarTaskbarStateUpdater() {
        public void updateAccessibilityServicesState() {
            NavigationBar.this.updateAccessibilityStateFlags();
        }

        public void updateAssistantAvailable(boolean z) {
            if (NavigationBar.this.mView != null) {
                NavigationBar navigationBar = NavigationBar.this;
                boolean unused = navigationBar.mLongPressHomeEnabled = navigationBar.mNavBarHelper.getLongPressHomeEnabled();
                NavigationBar.this.updateAssistantEntrypoints(z);
            }
        }
    };
    /* access modifiers changed from: private */
    public final NavigationBarTransitions mNavigationBarTransitions;
    private int mNavigationBarWindowState = 0;
    private int mNavigationIconHints = 0;
    private final NavigationModeController mNavigationModeController;
    /* access modifiers changed from: private */
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    private final NotificationShadeDepthController mNotificationShadeDepthController;
    private final ViewTreeObserver.OnComputeInternalInsetsListener mOnComputeInternalInsetsListener;
    private final DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() {
        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains("nav_bar_handle_force_opaque")) {
                boolean unused = NavigationBar.this.mForceNavBarHandleOpaque = properties.getBoolean("nav_bar_handle_force_opaque", true);
            }
            if (properties.getKeyset().contains("home_button_long_press_duration_ms")) {
                Optional unused2 = NavigationBar.this.mHomeButtonLongPressDurationMs = Optional.m1745of(Long.valueOf(properties.getLong("home_button_long_press_duration_ms", 0))).filter(new NavigationBar$5$$ExternalSyntheticLambda0());
                if (NavigationBar.this.mView != null) {
                    NavigationBar.this.reconfigureHomeLongClick();
                }
            }
        }

        static /* synthetic */ boolean lambda$onPropertiesChanged$0(Long l) {
            return l.longValue() != 0;
        }
    };
    private final Runnable mOnVariableDurationHomeLongClick = new NavigationBar$$ExternalSyntheticLambda19(this);
    /* access modifiers changed from: private */
    public QuickswitchOrientedNavHandle mOrientationHandle;
    private ViewTreeObserver.OnGlobalLayoutListener mOrientationHandleGlobalLayoutListener;
    private NavigationBarTransitions.DarkIntensityListener mOrientationHandleIntensityListener = new NavigationBarTransitions.DarkIntensityListener() {
        public void onDarkIntensity(float f) {
            NavigationBar.this.mOrientationHandle.setDarkIntensity(f);
        }
    };
    private WindowManager.LayoutParams mOrientationParams;
    /* access modifiers changed from: private */
    public Rect mOrientedHandleSamplingRegion;
    private final OverviewProxyService.OverviewProxyListener mOverviewProxyListener = new OverviewProxyService.OverviewProxyListener() {
        public void onConnectionChanged(boolean z) {
            ((NavigationBarView) NavigationBar.this.mView).onOverviewProxyConnectionChange(NavigationBar.this.mOverviewProxyService.isEnabled());
            ((NavigationBarView) NavigationBar.this.mView).setShouldShowSwipeUpUi(NavigationBar.this.mOverviewProxyService.shouldShowSwipeUpUI());
            NavigationBar.this.updateScreenPinningGestures();
        }

        public void onQuickStepStarted() {
            ((NavigationBarView) NavigationBar.this.mView).getRotationButtonController().setRotateSuggestionButtonState(false);
            NavigationBar.this.mShadeController.collapsePanel(true);
        }

        public void onPrioritizedRotation(int i) {
            int unused = NavigationBar.this.mStartingQuickSwitchRotation = i;
            if (i == -1) {
                boolean unused2 = NavigationBar.this.mShowOrientedHandleForImmersiveMode = false;
            }
            NavigationBar.this.orientSecondaryHomeHandle();
        }

        public void startAssistant(Bundle bundle) {
            ((AssistManager) NavigationBar.this.mAssistManagerLazy.get()).startAssist(bundle);
        }

        public void onNavBarButtonAlphaChanged(float f, boolean z) {
            boolean z2;
            boolean z3;
            ButtonDispatcher buttonDispatcher;
            if (NavigationBar.this.mIsCurrentUserSetup && !QuickStepContract.isLegacyMode(NavigationBar.this.mNavBarMode)) {
                int i = 0;
                if (QuickStepContract.isGesturalMode(NavigationBar.this.mNavBarMode)) {
                    z2 = NavigationBar.this.mAllowForceNavBarHandleOpaque && NavigationBar.this.mForceNavBarHandleOpaque;
                    buttonDispatcher = ((NavigationBarView) NavigationBar.this.mView).getHomeHandle();
                    if (NavigationBar.this.getBarTransitions() != null) {
                        NavigationBar.this.getBarTransitions().setBackgroundOverrideAlpha(f);
                    }
                    z3 = false;
                } else {
                    buttonDispatcher = QuickStepContract.isSwipeUpMode(NavigationBar.this.mNavBarMode) ? ((NavigationBarView) NavigationBar.this.mView).getBackButton() : null;
                    z3 = z;
                    z2 = false;
                }
                if (buttonDispatcher != null) {
                    if (!z2 && f <= 0.0f) {
                        i = 4;
                    }
                    buttonDispatcher.setVisibility(i);
                    if (z2) {
                        f = 1.0f;
                    }
                    buttonDispatcher.setAlpha(f, z3);
                }
            }
        }

        public void onHomeRotationEnabled(boolean z) {
            ((NavigationBarView) NavigationBar.this.mView).getRotationButtonController().setHomeRotationEnabled(z);
        }

        public void onOverviewShown(boolean z) {
            ((NavigationBarView) NavigationBar.this.mView).getRotationButtonController().setSkipOverrideUserLockPrefsOnce();
        }

        public void onTaskbarStatusUpdated(boolean z, boolean z2) {
            ((NavigationBarView) NavigationBar.this.mView).getFloatingRotationButton().onTaskbarStateChanged(z, z2);
        }

        public void onToggleRecentApps() {
            ((NavigationBarView) NavigationBar.this.mView).getRotationButtonController().setSkipOverrideUserLockPrefsOnce();
        }
    };
    /* access modifiers changed from: private */
    public final OverviewProxyService mOverviewProxyService;
    private final Optional<Pip> mPipOptional;
    private final Optional<Recents> mRecentsOptional;
    /* access modifiers changed from: private */
    public final RegionSamplingHelper mRegionSamplingHelper;
    private final Consumer<Integer> mRotationWatcher = new NavigationBar$$ExternalSyntheticLambda20(this);
    /* access modifiers changed from: private */
    public final Rect mSamplingBounds = new Rect();
    private final Bundle mSavedState;
    /* access modifiers changed from: private */
    public final ShadeController mShadeController;
    /* access modifiers changed from: private */
    public boolean mShowOrientedHandleForImmersiveMode;
    /* access modifiers changed from: private */
    public int mStartingQuickSwitchRotation = -1;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final StatusBarStateController mStatusBarStateController;
    private final SysUiState mSysUiFlagsContainer;
    private final Optional<TelecomManager> mTelecomManagerOptional;
    private final Gefingerpoken mTouchHandler;
    private boolean mTransientShown;
    private boolean mTransientShownFromGestureOnSystemBar;
    private int mTransitionMode;
    private final UiEventLogger mUiEventLogger;
    private final UserContextProvider mUserContextProvider;
    private final DeviceProvisionedController.DeviceProvisionedListener mUserSetupListener = new DeviceProvisionedController.DeviceProvisionedListener() {
        public void onUserSetupChanged() {
            NavigationBar navigationBar = NavigationBar.this;
            boolean unused = navigationBar.mIsCurrentUserSetup = navigationBar.mDeviceProvisionedController.isCurrentUserSetup();
        }
    };
    private final WindowManager mWindowManager;

    private int deltaRotation(int i, int i2) {
        int i3 = i2 - i;
        return i3 < 0 ? i3 + 4 : i3;
    }

    private static int transitionMode(boolean z, int i) {
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

    public enum NavBarActionEvent implements UiEventLogger.UiEventEnum {
        NAVBAR_ASSIST_LONGPRESS(550);
        
        private final int mId;

        private NavBarActionEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-navigationbar-NavigationBar  reason: not valid java name */
    public /* synthetic */ void m2850lambda$new$0$comandroidsystemuinavigationbarNavigationBar() {
        getBarTransitions().setAutoDim(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-navigationbar-NavigationBar  reason: not valid java name */
    public /* synthetic */ void m2851lambda$new$1$comandroidsystemuinavigationbarNavigationBar() {
        ((NavigationBarView) this.mView).setLayoutTransitionsEnabled(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-navigationbar-NavigationBar  reason: not valid java name */
    public /* synthetic */ void m2852lambda$new$2$comandroidsystemuinavigationbarNavigationBar() {
        if (onHomeLongClick(((NavigationBarView) this.mView).getHomeButton().getCurrentView())) {
            ((NavigationBarView) this.mView).getHomeButton().getCurrentView().performHapticFeedback(0, 1);
        }
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    NavigationBar(NavigationBarView navigationBarView, NavigationBarFrame navigationBarFrame, Bundle bundle, @DisplayId Context context, @DisplayId WindowManager windowManager, Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<Recents> optional2, Lazy<Optional<CentralSurfaces>> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, @Main Handler handler, @Main Executor executor, @Background Executor executor2, UiEventLogger uiEventLogger, NavBarHelper navBarHelper, LightBarController lightBarController, LightBarController.Factory factory, AutoHideController autoHideController, AutoHideController.Factory factory2, Optional<TelecomManager> optional3, InputMethodManager inputMethodManager, DeadZone deadZone, DeviceConfigProxy deviceConfigProxy, NavigationBarTransitions navigationBarTransitions, EdgeBackGestureHandler edgeBackGestureHandler, Optional<BackAnimation> optional4, UserContextProvider userContextProvider) {
        super(navigationBarView);
        NavigationModeController navigationModeController2 = navigationModeController;
        EdgeBackGestureHandler edgeBackGestureHandler2 = edgeBackGestureHandler;
        C224710 r3 = new NavigationModeController.ModeChangedListener() {
            public void onNavigationModeChanged(int i) {
                int unused = NavigationBar.this.mNavBarMode = i;
                if (!QuickStepContract.isGesturalMode(i) && NavigationBar.this.getBarTransitions() != null) {
                    NavigationBar.this.getBarTransitions().setBackgroundOverrideAlpha(1.0f);
                }
                NavigationBar.this.updateScreenPinningGestures();
                if (!NavigationBar.this.canShowSecondaryHandle()) {
                    NavigationBar.this.resetSecondaryHandle();
                }
                NavigationBar.this.setNavBarMode(i);
                ((NavigationBarView) NavigationBar.this.mView).setShouldShowSwipeUpUi(NavigationBar.this.mOverviewProxyService.shouldShowSwipeUpUI());
            }
        };
        this.mModeChangedListener = r3;
        this.mTouchHandler = new Gefingerpoken() {
            private boolean mDeadZoneConsuming;

            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (QuickStepContract.isGesturalMode(NavigationBar.this.mNavBarMode) && NavigationBar.this.mImeVisible && motionEvent.getAction() == 0) {
                    SysUiStatsLog.write(304, (int) motionEvent.getX(), (int) motionEvent.getY());
                }
                return shouldDeadZoneConsumeTouchEvents(motionEvent);
            }

            public boolean onTouchEvent(MotionEvent motionEvent) {
                shouldDeadZoneConsumeTouchEvents(motionEvent);
                return false;
            }

            private boolean shouldDeadZoneConsumeTouchEvents(MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    this.mDeadZoneConsuming = false;
                }
                if (!NavigationBar.this.mDeadZone.onTouchEvent(motionEvent) && !this.mDeadZoneConsuming) {
                    return false;
                }
                if (actionMasked == 0) {
                    ((NavigationBarView) NavigationBar.this.mView).setSlippery(true);
                    this.mDeadZoneConsuming = true;
                } else if (actionMasked == 1 || actionMasked == 3) {
                    ((NavigationBarView) NavigationBar.this.mView).updateSlippery();
                    this.mDeadZoneConsuming = false;
                }
                return true;
            }
        };
        this.mFrame = navigationBarFrame;
        this.mContext = context;
        this.mSavedState = bundle;
        this.mWindowManager = windowManager;
        this.mAccessibilityManager = accessibilityManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mStatusBarStateController = statusBarStateController;
        this.mMetricsLogger = metricsLogger;
        this.mAssistManagerLazy = lazy;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mSysUiFlagsContainer = sysUiState;
        this.mCentralSurfacesOptionalLazy = lazy2;
        this.mShadeController = shadeController;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mOverviewProxyService = overviewProxyService;
        this.mNavigationModeController = navigationModeController2;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mCommandQueue = commandQueue;
        this.mPipOptional = optional;
        this.mRecentsOptional = optional2;
        this.mDeadZone = deadZone;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mNavigationBarTransitions = navigationBarTransitions;
        this.mEdgeBackGestureHandler = edgeBackGestureHandler2;
        this.mBackAnimation = optional4;
        this.mHandler = handler;
        this.mUiEventLogger = uiEventLogger;
        this.mNavBarHelper = navBarHelper;
        this.mNotificationShadeDepthController = notificationShadeDepthController;
        this.mMainLightBarController = lightBarController;
        this.mLightBarControllerFactory = factory;
        this.mMainAutoHideController = autoHideController;
        this.mAutoHideControllerFactory = factory2;
        this.mTelecomManagerOptional = optional3;
        this.mInputMethodManager = inputMethodManager;
        this.mUserContextProvider = userContextProvider;
        this.mNavColorSampleMargin = getResources().getDimensionPixelSize(C1893R.dimen.navigation_handle_sample_horizontal_margin);
        this.mOnComputeInternalInsetsListener = new NavigationBar$$ExternalSyntheticLambda22(this);
        this.mRegionSamplingHelper = new RegionSamplingHelper(this.mView, new RegionSamplingHelper.SamplingCallback() {
            public void onRegionDarknessChanged(boolean z) {
                NTLogUtil.m1680d(NavigationBar.TAG, "onRegionDarknessChanged: isRegionDark = " + z);
                NavigationBar.this.getBarTransitions().getLightTransitionsController().setIconsDark(!z, true);
            }

            public Rect getSampledRegion(View view) {
                if (NavigationBar.this.mOrientedHandleSamplingRegion != null) {
                    return NavigationBar.this.mOrientedHandleSamplingRegion;
                }
                return NavigationBar.this.calculateSamplingRect();
            }

            public boolean isSamplingEnabled() {
                boolean isRegionSamplingAvailable = NavigationBar.this.mEx.isRegionSamplingAvailable(NavigationBar.this.mNavigationBarTransitions.getMode());
                NTLogUtil.m1680d(NavigationBar.TAG, "isSamplingEnabled: " + isRegionSamplingAvailable);
                return isRegionSamplingAvailable;
            }
        }, executor, executor2);
        ((NavigationBarView) this.mView).setEdgeBackGestureHandler(edgeBackGestureHandler2);
        this.mNavBarMode = navigationModeController2.addListener(r3);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-navigationbar-NavigationBar  reason: not valid java name */
    public /* synthetic */ void m2853lambda$new$3$comandroidsystemuinavigationbarNavigationBar(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        if (!this.mEdgeBackGestureHandler.isHandlingGestures()) {
            if (!this.mImeVisible) {
                internalInsetsInfo.setTouchableInsets(0);
                return;
            } else if (!((NavigationBarView) this.mView).isImeRenderingNavButtons()) {
                internalInsetsInfo.setTouchableInsets(0);
                return;
            }
        }
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(getButtonLocations(false, false, false));
    }

    public NavigationBarView getView() {
        return (NavigationBarView) this.mView;
    }

    public void onInit() {
        ((NavigationBarView) this.mView).setBarTransitions(this.mNavigationBarTransitions);
        ((NavigationBarView) this.mView).setTouchHandler(this.mTouchHandler);
        setNavBarMode(this.mNavBarMode);
        EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
        NavigationBarView navigationBarView = (NavigationBarView) this.mView;
        Objects.requireNonNull(navigationBarView);
        edgeBackGestureHandler.setStateChangeCallback(new NavigationBar$$ExternalSyntheticLambda6(navigationBarView));
        this.mNavigationBarTransitions.addListener(new NavigationBar$$ExternalSyntheticLambda7(this));
        ((NavigationBarView) this.mView).updateRotationButton();
        ((NavigationBarView) this.mView).setVisibility(this.mStatusBarKeyguardViewManager.isNavBarVisible() ? 0 : 4);
        this.mWindowManager.addView(this.mFrame, getBarLayoutParams(this.mContext.getResources().getConfiguration().windowConfiguration.getRotation()));
        int displayId = this.mContext.getDisplayId();
        this.mDisplayId = displayId;
        this.mIsOnDefaultDisplay = displayId == 0;
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mLongPressHomeEnabled = this.mNavBarHelper.getLongPressHomeEnabled();
        this.mNavBarHelper.init();
        this.mAllowForceNavBarHandleOpaque = this.mContext.getResources().getBoolean(C1893R.bool.allow_force_nav_bar_handle_opaque);
        this.mForceNavBarHandleOpaque = this.mDeviceConfigProxy.getBoolean("systemui", "nav_bar_handle_force_opaque", true);
        this.mHomeButtonLongPressDurationMs = Optional.m1745of(Long.valueOf(this.mDeviceConfigProxy.getLong("systemui", "home_button_long_press_duration_ms", 0))).filter(new NavigationBar$$ExternalSyntheticLambda8());
        DeviceConfigProxy deviceConfigProxy = this.mDeviceConfigProxy;
        Handler handler = this.mHandler;
        Objects.requireNonNull(handler);
        deviceConfigProxy.addOnPropertiesChangedListener("systemui", new NavigationBar$$ExternalSyntheticLambda9(handler), this.mOnPropertiesChangedListener);
        Bundle bundle = this.mSavedState;
        if (bundle != null) {
            this.mDisabledFlags1 = bundle.getInt(EXTRA_DISABLE_STATE, 0);
            this.mDisabledFlags2 = this.mSavedState.getInt(EXTRA_DISABLE2_STATE, 0);
            this.mAppearance = this.mSavedState.getInt(EXTRA_APPEARANCE, 0);
            this.mBehavior = this.mSavedState.getInt(EXTRA_BEHAVIOR, 0);
            this.mTransientShown = this.mSavedState.getBoolean(EXTRA_TRANSIENT_STATE, false);
        }
        this.mCommandQueue.recomputeDisableFlags(this.mDisplayId, false);
        this.mIsCurrentUserSetup = this.mDeviceProvisionedController.isCurrentUserSetup();
        this.mDeviceProvisionedController.addCallback(this.mUserSetupListener);
        this.mNotificationShadeDepthController.addListener(this.mDepthListener);
    }

    static /* synthetic */ boolean lambda$onInit$4(Long l) {
        return l.longValue() != 0;
    }

    public void destroyView() {
        setAutoHideController((AutoHideController) null);
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mWindowManager.removeViewImmediate(((NavigationBarView) this.mView).getRootView());
        this.mNavigationModeController.removeListener(this.mModeChangedListener);
        this.mNavBarHelper.removeNavTaskStateUpdater(this.mNavbarTaskbarStateUpdater);
        this.mNavBarHelper.destroy();
        this.mDeviceProvisionedController.removeCallback(this.mUserSetupListener);
        this.mNotificationShadeDepthController.removeListener(this.mDepthListener);
        this.mDeviceConfigProxy.removeOnPropertiesChangedListener(this.mOnPropertiesChangedListener);
    }

    public void onViewAttached() {
        Display display = ((NavigationBarView) this.mView).getDisplay();
        ((NavigationBarView) this.mView).setComponents(this.mRecentsOptional);
        ((NavigationBarView) this.mView).setComponents(((CentralSurfaces) this.mCentralSurfacesOptionalLazy.get().get()).getPanelController());
        ((NavigationBarView) this.mView).setDisabledFlags(this.mDisabledFlags1, this.mSysUiFlagsContainer);
        ((NavigationBarView) this.mView).setOnVerticalChangedListener(new NavigationBar$$ExternalSyntheticLambda12(this));
        ((NavigationBarView) this.mView).setOnTouchListener(new NavigationBar$$ExternalSyntheticLambda13(this));
        if (this.mSavedState != null) {
            getBarTransitions().getLightTransitionsController().restoreState(this.mSavedState);
        }
        setNavigationIconHints(this.mNavigationIconHints);
        setWindowVisible(isNavBarWindowVisible());
        ((NavigationBarView) this.mView).setBehavior(this.mBehavior);
        setNavBarMode(this.mNavBarMode);
        ((NavigationBarView) this.mView).setUpdateActiveTouchRegionsCallback(new NavigationBar$$ExternalSyntheticLambda14(this));
        ((NavigationBarView) this.mView).getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
        this.mNavBarHelper.registerNavTaskStateUpdater(this.mNavbarTaskbarStateUpdater);
        Optional<Pip> optional = this.mPipOptional;
        NavigationBarView navigationBarView = (NavigationBarView) this.mView;
        Objects.requireNonNull(navigationBarView);
        optional.ifPresent(new NavigationBar$$ExternalSyntheticLambda15(navigationBarView));
        Optional<BackAnimation> optional2 = this.mBackAnimation;
        NavigationBarView navigationBarView2 = (NavigationBarView) this.mView;
        Objects.requireNonNull(navigationBarView2);
        optional2.ifPresent(new NavigationBar$$ExternalSyntheticLambda16(navigationBarView2));
        prepareNavigationBarView();
        checkNavBarModes();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        this.mBroadcastDispatcher.registerReceiverWithHandler(this.mBroadcastReceiver, intentFilter, Handler.getMain(), UserHandle.ALL);
        notifyNavigationBarScreenOn();
        this.mOverviewProxyService.addCallback(this.mOverviewProxyListener);
        updateSystemUiStateFlags();
        if (this.mIsOnDefaultDisplay) {
            RotationButtonController rotationButtonController = ((NavigationBarView) this.mView).getRotationButtonController();
            rotationButtonController.setRotationCallback(this.mRotationWatcher);
            if (display != null && rotationButtonController.isRotationLocked()) {
                rotationButtonController.setRotationLockedAtAngle(display.getRotation());
            }
        } else {
            this.mDisabledFlags2 |= 16;
        }
        setDisabled2Flags(this.mDisabledFlags2);
        initSecondaryHomeHandleForRotation();
        setLightBarController(this.mIsOnDefaultDisplay ? this.mMainLightBarController : this.mLightBarControllerFactory.create(this.mContext));
        setAutoHideController(this.mIsOnDefaultDisplay ? this.mMainAutoHideController : this.mAutoHideControllerFactory.create(this.mContext));
        restoreAppearanceAndTransientState();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$5$com-android-systemui-navigationbar-NavigationBar */
    public /* synthetic */ void mo34640xba59c33f() {
        this.mOverviewProxyService.onActiveNavBarRegionChanges(getButtonLocations(true, true, true));
    }

    public void onViewDetached() {
        ((NavigationBarView) this.mView).getRotationButtonController().setRotationCallback((Consumer<Integer>) null);
        ((NavigationBarView) this.mView).setUpdateActiveTouchRegionsCallback((NavigationBarView.UpdateActiveTouchRegionsCallback) null);
        getBarTransitions().destroy();
        this.mOverviewProxyService.removeCallback(this.mOverviewProxyListener);
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
        if (this.mOrientationHandle != null) {
            resetSecondaryHandle();
            getBarTransitions().removeDarkIntensityListener(this.mOrientationHandleIntensityListener);
            this.mWindowManager.removeView(this.mOrientationHandle);
            this.mOrientationHandle.getViewTreeObserver().removeOnGlobalLayoutListener(this.mOrientationHandleGlobalLayoutListener);
        }
        ((NavigationBarView) this.mView).getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
        this.mHandler.removeCallbacks(this.mAutoDim);
        this.mHandler.removeCallbacks(this.mOnVariableDurationHomeLongClick);
        this.mHandler.removeCallbacks(this.mEnableLayoutTransitions);
        this.mNavBarHelper.removeNavTaskStateUpdater(this.mNavbarTaskbarStateUpdater);
        Optional<Pip> optional = this.mPipOptional;
        NavigationBarView navigationBarView = (NavigationBarView) this.mView;
        Objects.requireNonNull(navigationBarView);
        optional.ifPresent(new NavigationBar$$ExternalSyntheticLambda4(navigationBarView));
        this.mFrame = null;
        this.mOrientationHandle = null;
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(EXTRA_DISABLE_STATE, this.mDisabledFlags1);
        bundle.putInt(EXTRA_DISABLE2_STATE, this.mDisabledFlags2);
        bundle.putInt(EXTRA_APPEARANCE, this.mAppearance);
        bundle.putInt(EXTRA_BEHAVIOR, this.mBehavior);
        bundle.putBoolean(EXTRA_TRANSIENT_STATE, this.mTransientShown);
        getBarTransitions().getLightTransitionsController().saveState(bundle);
    }

    public void onConfigurationChanged(Configuration configuration) {
        int rotation = configuration.windowConfiguration.getRotation();
        Locale locale = this.mContext.getResources().getConfiguration().locale;
        int layoutDirectionFromLocale = TextUtils.getLayoutDirectionFromLocale(locale);
        if (!locale.equals(this.mLocale) || layoutDirectionFromLocale != this.mLayoutDirection) {
            this.mLocale = locale;
            this.mLayoutDirection = layoutDirectionFromLocale;
            refreshLayout(layoutDirectionFromLocale);
        }
        repositionNavigationBar(rotation);
        if (canShowSecondaryHandle() && rotation != this.mCurrentRotation) {
            this.mCurrentRotation = rotation;
            orientSecondaryHomeHandle();
        }
    }

    private void initSecondaryHomeHandleForRotation() {
        if (this.mNavBarMode == 2) {
            QuickswitchOrientedNavHandle quickswitchOrientedNavHandle = new QuickswitchOrientedNavHandle(this.mContext);
            this.mOrientationHandle = quickswitchOrientedNavHandle;
            quickswitchOrientedNavHandle.setId(C1893R.C1897id.secondary_home_handle);
            getBarTransitions().addDarkIntensityListener(this.mOrientationHandleIntensityListener);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(0, 0, 2024, 536871224, -3);
            this.mOrientationParams = layoutParams;
            layoutParams.setTitle("SecondaryHomeHandle" + this.mContext.getDisplayId());
            this.mOrientationParams.privateFlags |= 8256;
            this.mWindowManager.addView(this.mOrientationHandle, this.mOrientationParams);
            this.mOrientationHandle.setVisibility(8);
            this.mOrientationParams.setFitInsetsTypes(0);
            this.mOrientationHandleGlobalLayoutListener = new NavigationBar$$ExternalSyntheticLambda3(this);
            this.mOrientationHandle.getViewTreeObserver().addOnGlobalLayoutListener(this.mOrientationHandleGlobalLayoutListener);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initSecondaryHomeHandleForRotation$6$com-android-systemui-navigationbar-NavigationBar */
    public /* synthetic */ void mo34633x2a92272a() {
        if (this.mStartingQuickSwitchRotation != -1) {
            RectF computeHomeHandleBounds = this.mOrientationHandle.computeHomeHandleBounds();
            this.mOrientationHandle.mapRectFromViewToScreenCoords(computeHomeHandleBounds, true);
            Rect rect = new Rect();
            computeHomeHandleBounds.roundOut(rect);
            setOrientedHandleSamplingRegion(rect);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0090  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void orientSecondaryHomeHandle() {
        /*
            r7 = this;
            boolean r0 = r7.canShowSecondaryHandle()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            int r0 = r7.mStartingQuickSwitchRotation
            r1 = -1
            if (r0 != r1) goto L_0x0011
            r7.resetSecondaryHandle()
            goto L_0x00b5
        L_0x0011:
            int r2 = r7.mCurrentRotation
            int r0 = r7.deltaRotation(r2, r0)
            int r2 = r7.mStartingQuickSwitchRotation
            if (r2 == r1) goto L_0x001d
            if (r0 != r1) goto L_0x004a
        L_0x001d:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "secondary nav delta rotation: "
            r1.<init>((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((int) r0)
            java.lang.String r2 = " current: "
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            int r2 = r7.mCurrentRotation
            java.lang.StringBuilder r1 = r1.append((int) r2)
            java.lang.String r2 = " starting: "
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            int r2 = r7.mStartingQuickSwitchRotation
            java.lang.StringBuilder r1 = r1.append((int) r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "NavigationBar"
            android.util.Log.d(r2, r1)
        L_0x004a:
            android.view.WindowManager r1 = r7.mWindowManager
            android.view.WindowMetrics r1 = r1.getCurrentWindowMetrics()
            android.graphics.Rect r1 = r1.getBounds()
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r2 = r7.mOrientationHandle
            r2.setDeltaRotation(r0)
            r2 = 3
            r3 = 1
            r4 = 0
            if (r0 == 0) goto L_0x0075
            if (r0 == r3) goto L_0x0068
            r5 = 2
            if (r0 == r5) goto L_0x0075
            if (r0 == r2) goto L_0x0068
            r1 = r4
            r5 = r1
            goto L_0x0089
        L_0x0068:
            int r1 = r1.height()
            android.view.View r5 = r7.mView
            com.android.systemui.navigationbar.NavigationBarView r5 = (com.android.systemui.navigationbar.NavigationBarView) r5
            int r5 = r5.getHeight()
            goto L_0x0089
        L_0x0075:
            boolean r5 = r7.mShowOrientedHandleForImmersiveMode
            if (r5 != 0) goto L_0x007d
            r7.resetSecondaryHandle()
            return
        L_0x007d:
            int r5 = r1.width()
            android.view.View r1 = r7.mView
            com.android.systemui.navigationbar.NavigationBarView r1 = (com.android.systemui.navigationbar.NavigationBarView) r1
            int r1 = r1.getHeight()
        L_0x0089:
            android.view.WindowManager$LayoutParams r6 = r7.mOrientationParams
            if (r0 != 0) goto L_0x0090
            r2 = 80
            goto L_0x0094
        L_0x0090:
            if (r0 != r3) goto L_0x0093
            goto L_0x0094
        L_0x0093:
            r2 = 5
        L_0x0094:
            r6.gravity = r2
            android.view.WindowManager$LayoutParams r0 = r7.mOrientationParams
            r0.height = r1
            android.view.WindowManager$LayoutParams r0 = r7.mOrientationParams
            r0.width = r5
            android.view.WindowManager r0 = r7.mWindowManager
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r1 = r7.mOrientationHandle
            android.view.WindowManager$LayoutParams r2 = r7.mOrientationParams
            r0.updateViewLayout(r1, r2)
            android.view.View r0 = r7.mView
            com.android.systemui.navigationbar.NavigationBarView r0 = (com.android.systemui.navigationbar.NavigationBarView) r0
            r1 = 8
            r0.setVisibility(r1)
            com.android.systemui.navigationbar.gestural.QuickswitchOrientedNavHandle r7 = r7.mOrientationHandle
            r7.setVisibility(r4)
        L_0x00b5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBar.orientSecondaryHomeHandle():void");
    }

    /* access modifiers changed from: private */
    public void resetSecondaryHandle() {
        QuickswitchOrientedNavHandle quickswitchOrientedNavHandle = this.mOrientationHandle;
        if (quickswitchOrientedNavHandle != null) {
            quickswitchOrientedNavHandle.setVisibility(8);
        }
        ((NavigationBarView) this.mView).setVisibility(0);
        setOrientedHandleSamplingRegion((Rect) null);
    }

    /* access modifiers changed from: private */
    public void reconfigureHomeLongClick() {
        if (((NavigationBarView) this.mView).getHomeButton().getCurrentView() != null) {
            if (this.mHomeButtonLongPressDurationMs.isPresent() || !this.mLongPressHomeEnabled) {
                ((NavigationBarView) this.mView).getHomeButton().getCurrentView().setLongClickable(false);
                ((NavigationBarView) this.mView).getHomeButton().getCurrentView().setHapticFeedbackEnabled(false);
                ((NavigationBarView) this.mView).getHomeButton().setOnLongClickListener((View.OnLongClickListener) null);
                return;
            }
            ((NavigationBarView) this.mView).getHomeButton().getCurrentView().setLongClickable(true);
            ((NavigationBarView) this.mView).getHomeButton().getCurrentView().setHapticFeedbackEnabled(true);
            ((NavigationBarView) this.mView).getHomeButton().setOnLongClickListener(new NavigationBar$$ExternalSyntheticLambda10(this));
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBar (displayId=" + this.mDisplayId + "):");
        printWriter.println("  mStartingQuickSwitchRotation=" + this.mStartingQuickSwitchRotation);
        printWriter.println("  mCurrentRotation=" + this.mCurrentRotation);
        printWriter.println("  mHomeButtonLongPressDurationMs=" + this.mHomeButtonLongPressDurationMs);
        printWriter.println("  mLongPressHomeEnabled=" + this.mLongPressHomeEnabled);
        printWriter.println("  mNavigationBarWindowState=" + StatusBarManager.windowStateToString(this.mNavigationBarWindowState));
        printWriter.println("  mTransitionMode=" + BarTransitions.modeToString(this.mTransitionMode));
        printWriter.println("  mTransientShown=" + this.mTransientShown);
        printWriter.println("  mTransientShownFromGestureOnSystemBar=" + this.mTransientShownFromGestureOnSystemBar);
        CentralSurfaces.dumpBarTransitions(printWriter, "mNavigationBarView", getBarTransitions());
        printWriter.println("  mOrientedHandleSamplingRegion: " + this.mOrientedHandleSamplingRegion);
        ((NavigationBarView) this.mView).dump(printWriter);
        this.mRegionSamplingHelper.dump(printWriter);
    }

    public void setImeWindowStatus(int i, IBinder iBinder, int i2, int i3, boolean z) {
        if (i == this.mDisplayId) {
            boolean isImeShown = this.mNavBarHelper.isImeShown(i2);
            int calculateBackDispositionHints = Utilities.calculateBackDispositionHints(this.mNavigationIconHints, i3, isImeShown, isImeShown && z);
            if (calculateBackDispositionHints != this.mNavigationIconHints) {
                setNavigationIconHints(calculateBackDispositionHints);
                checkBarModes();
                updateSystemUiStateFlags();
            }
        }
    }

    public void setWindowState(int i, int i2, int i3) {
        if (i == this.mDisplayId && i2 == 2 && this.mNavigationBarWindowState != i3) {
            this.mNavigationBarWindowState = i3;
            updateSystemUiStateFlags();
            this.mShowOrientedHandleForImmersiveMode = i3 == 2;
            if (!(this.mOrientationHandle == null || this.mStartingQuickSwitchRotation == -1)) {
                orientSecondaryHomeHandle();
            }
            setWindowVisible(isNavBarWindowVisible());
        }
    }

    public void onRotationProposal(int i, boolean z) {
        if (((NavigationBarView) this.mView).isAttachedToWindow()) {
            boolean hasDisable2RotateSuggestionFlag = RotationButtonController.hasDisable2RotateSuggestionFlag(this.mDisabledFlags2);
            RotationButtonController rotationButtonController = ((NavigationBarView) this.mView).getRotationButtonController();
            rotationButtonController.getRotationButton();
            if (!hasDisable2RotateSuggestionFlag) {
                rotationButtonController.onRotationProposal(i, z);
            }
        }
    }

    public void onRecentsAnimationStateChanged(boolean z) {
        ((NavigationBarView) this.mView).getRotationButtonController().setRecentsAnimationRunning(z);
    }

    public void restoreAppearanceAndTransientState() {
        int transitionMode = transitionMode(this.mTransientShown, this.mAppearance);
        this.mTransitionMode = transitionMode;
        checkNavBarModes();
        AutoHideController autoHideController = this.mAutoHideController;
        if (autoHideController != null) {
            autoHideController.touchAutoHide();
        }
        LightBarController lightBarController = this.mLightBarController;
        if (lightBarController != null) {
            lightBarController.onNavigationBarAppearanceChanged(this.mAppearance, true, transitionMode, false);
        }
    }

    public void onSystemBarAttributesChanged(int i, int i2, AppearanceRegion[] appearanceRegionArr, boolean z, int i3, InsetsVisibilities insetsVisibilities, String str) {
        boolean z2;
        if (i == this.mDisplayId) {
            if (this.mAppearance != i2) {
                this.mAppearance = i2;
                z2 = updateTransitionMode(transitionMode(this.mTransientShown, i2));
            } else {
                z2 = false;
            }
            LightBarController lightBarController = this.mLightBarController;
            if (lightBarController != null) {
                lightBarController.onNavigationBarAppearanceChanged(i2, z2, this.mTransitionMode, z);
            }
            if (this.mBehavior != i3) {
                this.mBehavior = i3;
                ((NavigationBarView) this.mView).setBehavior(i3);
                updateSystemUiStateFlags();
            }
        }
    }

    public void showTransient(int i, int[] iArr, boolean z) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 1) && !this.mTransientShown) {
            this.mTransientShown = true;
            this.mTransientShownFromGestureOnSystemBar = z;
            handleTransientChanged();
        }
    }

    public void abortTransient(int i, int[] iArr) {
        if (i == this.mDisplayId && InsetsState.containsType(iArr, 1)) {
            clearTransient();
        }
    }

    /* access modifiers changed from: private */
    public void clearTransient() {
        if (this.mTransientShown) {
            this.mTransientShown = false;
            this.mTransientShownFromGestureOnSystemBar = false;
            handleTransientChanged();
        }
    }

    private void handleTransientChanged() {
        LightBarController lightBarController;
        this.mEdgeBackGestureHandler.onNavBarTransientStateChanged(this.mTransientShown);
        int transitionMode = transitionMode(this.mTransientShown, this.mAppearance);
        if (updateTransitionMode(transitionMode) && (lightBarController = this.mLightBarController) != null) {
            lightBarController.onNavigationBarModeChanged(transitionMode);
        }
    }

    private boolean updateTransitionMode(int i) {
        if (this.mTransitionMode == i) {
            return false;
        }
        this.mTransitionMode = i;
        checkNavBarModes();
        AutoHideController autoHideController = this.mAutoHideController;
        if (autoHideController == null) {
            return true;
        }
        autoHideController.touchAutoHide();
        return true;
    }

    public void disable(int i, int i2, int i3, boolean z) {
        int i4;
        if (i == this.mDisplayId) {
            int i5 = 56623104 & i2;
            if (i5 != this.mDisabledFlags1) {
                this.mDisabledFlags1 = i5;
                ((NavigationBarView) this.mView).setDisabledFlags(i2, this.mSysUiFlagsContainer);
                updateScreenPinningGestures();
            }
            if (this.mIsOnDefaultDisplay && (i4 = i3 & 16) != this.mDisabledFlags2) {
                this.mDisabledFlags2 = i4;
                setDisabled2Flags(i4);
            }
        }
    }

    private void setDisabled2Flags(int i) {
        ((NavigationBarView) this.mView).getRotationButtonController().onDisable2FlagChanged(i);
    }

    private void refreshLayout(int i) {
        ((NavigationBarView) this.mView).setLayoutDirection(i);
    }

    private boolean shouldDisableNavbarGestures() {
        return !this.mDeviceProvisionedController.isDeviceProvisioned() || (this.mDisabledFlags1 & QuickStepContract.SYSUI_STATE_VOICE_INTERACTION_WINDOW_SHOWING) != 0;
    }

    private void repositionNavigationBar(int i) {
        if (this.mView != null && ((NavigationBarView) this.mView).isAttachedToWindow()) {
            prepareNavigationBarView();
            this.mWindowManager.updateViewLayout(this.mFrame, getBarLayoutParams(i));
        }
    }

    /* access modifiers changed from: private */
    public void updateScreenPinningGestures() {
        View.OnLongClickListener onLongClickListener;
        boolean isScreenPinningActive = ActivityManagerWrapper.getInstance().isScreenPinningActive();
        ButtonDispatcher backButton = ((NavigationBarView) this.mView).getBackButton();
        ButtonDispatcher recentsButton = ((NavigationBarView) this.mView).getRecentsButton();
        if (isScreenPinningActive) {
            if (((NavigationBarView) this.mView).isRecentsButtonVisible()) {
                onLongClickListener = new NavigationBar$$ExternalSyntheticLambda24(this);
            } else {
                onLongClickListener = new NavigationBar$$ExternalSyntheticLambda25(this);
            }
            backButton.setOnLongClickListener(onLongClickListener);
            recentsButton.setOnLongClickListener(new NavigationBar$$ExternalSyntheticLambda24(this));
        } else {
            backButton.setOnLongClickListener((View.OnLongClickListener) null);
            recentsButton.setOnLongClickListener((View.OnLongClickListener) null);
        }
        backButton.setLongClickable(isScreenPinningActive);
        recentsButton.setLongClickable(isScreenPinningActive);
    }

    /* access modifiers changed from: private */
    public void notifyNavigationBarScreenOn() {
        ((NavigationBarView) this.mView).updateNavButtonIcons();
    }

    private void prepareNavigationBarView() {
        ((NavigationBarView) this.mView).reorient();
        ButtonDispatcher recentsButton = ((NavigationBarView) this.mView).getRecentsButton();
        recentsButton.setOnClickListener(new NavigationBar$$ExternalSyntheticLambda26(this));
        recentsButton.setOnTouchListener(new NavigationBar$$ExternalSyntheticLambda27(this));
        ((NavigationBarView) this.mView).getHomeButton().setOnTouchListener(new NavigationBar$$ExternalSyntheticLambda28(this));
        reconfigureHomeLongClick();
        ButtonDispatcher accessibilityButton = ((NavigationBarView) this.mView).getAccessibilityButton();
        accessibilityButton.setOnClickListener(new NavigationBar$$ExternalSyntheticLambda29(this));
        accessibilityButton.setOnLongClickListener(new NavigationBar$$ExternalSyntheticLambda1(this));
        updateAccessibilityStateFlags();
        ((NavigationBarView) this.mView).getImeSwitchButton().setOnClickListener(new NavigationBar$$ExternalSyntheticLambda2(this));
        updateScreenPinningGestures();
    }

    /* access modifiers changed from: package-private */
    public boolean onHomeTouch(View view, MotionEvent motionEvent) {
        if (this.mHomeBlockedThisTouch && motionEvent.getActionMasked() != 0) {
            return true;
        }
        Optional optional = this.mCentralSurfacesOptionalLazy.get();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mHomeBlockedThisTouch = false;
            if (this.mTelecomManagerOptional.isPresent() && this.mTelecomManagerOptional.get().isRinging() && ((Boolean) optional.map(new C2137x58d1e4b7()).orElse(false)).booleanValue()) {
                Log.i(TAG, "Ignoring HOME; there's a ringing incoming call. No heads up");
                this.mHomeBlockedThisTouch = true;
                return true;
            } else if (this.mLongPressHomeEnabled) {
                this.mHomeButtonLongPressDurationMs.ifPresent(new NavigationBar$$ExternalSyntheticLambda21(this));
            }
        } else if (action == 1 || action == 3) {
            this.mHandler.removeCallbacks(this.mOnVariableDurationHomeLongClick);
            optional.ifPresent(new NavigationBar$$ExternalSyntheticLambda0());
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onHomeTouch$7$com-android-systemui-navigationbar-NavigationBar */
    public /* synthetic */ void mo34639x5349b95e(Long l) {
        this.mHandler.postDelayed(this.mOnVariableDurationHomeLongClick, l.longValue());
    }

    /* access modifiers changed from: private */
    public void onVerticalChanged(boolean z) {
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new NavigationBar$$ExternalSyntheticLambda11(z));
    }

    /* access modifiers changed from: private */
    public boolean onNavigationTouch(View view, MotionEvent motionEvent) {
        AutoHideController autoHideController = this.mAutoHideController;
        if (autoHideController == null) {
            return false;
        }
        autoHideController.checkUserAutoHide(motionEvent);
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean onHomeLongClick(View view) {
        if (!((NavigationBarView) this.mView).isRecentsButtonVisible() && ActivityManagerWrapper.getInstance().isScreenPinningActive()) {
            return onLongPressBackHome(view);
        }
        if (shouldDisableNavbarGestures()) {
            return false;
        }
        this.mMetricsLogger.action(239);
        this.mUiEventLogger.log(NavBarActionEvent.NAVBAR_ASSIST_LONGPRESS);
        Bundle bundle = new Bundle();
        bundle.putInt(AssistManager.INVOCATION_TYPE_KEY, 5);
        this.mAssistManagerLazy.get().startAssist(bundle);
        this.mCentralSurfacesOptionalLazy.get().ifPresent(new NavigationBar$$ExternalSyntheticLambda0());
        ((NavigationBarView) this.mView).abortCurrentGesture();
        return true;
    }

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: private */
    public void onRecentsClick(View view) {
        if (!this.mEx.shouldInterceptRecentKey(this.mContext)) {
            if (LatencyTracker.isEnabled(this.mContext)) {
                LatencyTracker.getInstance(this.mContext).onActionStart(1);
            }
            this.mCentralSurfacesOptionalLazy.get().ifPresent(new NavigationBar$$ExternalSyntheticLambda0());
            this.mCommandQueue.toggleRecentApps();
        }
    }

    /* access modifiers changed from: private */
    public void onImeSwitcherClick(View view) {
        this.mInputMethodManager.showInputMethodPickerFromSystem(true, this.mDisplayId);
        this.mUiEventLogger.log(KeyButtonView.NavBarButtonEvent.NAVBAR_IME_SWITCHER_BUTTON_TAP);
    }

    /* access modifiers changed from: private */
    public boolean onLongPressBackHome(View view) {
        return onLongPressNavigationButtons(view, C1893R.C1897id.back, C1893R.C1897id.home);
    }

    /* access modifiers changed from: private */
    public boolean onLongPressBackRecents(View view) {
        return onLongPressNavigationButtons(view, C1893R.C1897id.back, C1893R.C1897id.recent_apps);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009b, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x009c, code lost:
        android.util.Log.d(TAG, "Unable to reach activity manager", r10);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean onLongPressNavigationButtons(android.view.View r11, int r12, int r13) {
        /*
            r10 = this;
            r0 = 0
            android.app.IActivityTaskManager r1 = android.app.ActivityTaskManager.getService()     // Catch:{ RemoteException -> 0x009b }
            android.view.accessibility.AccessibilityManager r2 = r10.mAccessibilityManager     // Catch:{ RemoteException -> 0x009b }
            boolean r2 = r2.isTouchExplorationEnabled()     // Catch:{ RemoteException -> 0x009b }
            boolean r3 = r1.isInLockTaskMode()     // Catch:{ RemoteException -> 0x009b }
            r4 = 2131428670(0x7f0b053e, float:1.8478991E38)
            r5 = 1
            if (r3 == 0) goto L_0x0059
            if (r2 != 0) goto L_0x0059
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0099 }
            long r6 = r10.mLastLockToAppLongPress     // Catch:{ all -> 0x0099 }
            long r6 = r2 - r6
            r8 = 200(0xc8, double:9.9E-322)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0030
            r1.stopSystemLockTaskMode()     // Catch:{ RemoteException -> 0x009b }
            android.view.View r10 = r10.mView     // Catch:{ RemoteException -> 0x009b }
            com.android.systemui.navigationbar.NavigationBarView r10 = (com.android.systemui.navigationbar.NavigationBarView) r10     // Catch:{ RemoteException -> 0x009b }
            r10.updateNavButtonIcons()     // Catch:{ RemoteException -> 0x009b }
            return r5
        L_0x0030:
            int r1 = r11.getId()     // Catch:{ all -> 0x0099 }
            if (r1 != r12) goto L_0x0055
            if (r13 != r4) goto L_0x0041
            android.view.View r12 = r10.mView     // Catch:{ all -> 0x0099 }
            com.android.systemui.navigationbar.NavigationBarView r12 = (com.android.systemui.navigationbar.NavigationBarView) r12     // Catch:{ all -> 0x0099 }
            com.android.systemui.navigationbar.buttons.ButtonDispatcher r12 = r12.getRecentsButton()     // Catch:{ all -> 0x0099 }
            goto L_0x0049
        L_0x0041:
            android.view.View r12 = r10.mView     // Catch:{ all -> 0x0099 }
            com.android.systemui.navigationbar.NavigationBarView r12 = (com.android.systemui.navigationbar.NavigationBarView) r12     // Catch:{ all -> 0x0099 }
            com.android.systemui.navigationbar.buttons.ButtonDispatcher r12 = r12.getHomeButton()     // Catch:{ all -> 0x0099 }
        L_0x0049:
            android.view.View r12 = r12.getCurrentView()     // Catch:{ all -> 0x0099 }
            boolean r12 = r12.isPressed()     // Catch:{ all -> 0x0099 }
            if (r12 != 0) goto L_0x0055
            r12 = r5
            goto L_0x0056
        L_0x0055:
            r12 = r0
        L_0x0056:
            r10.mLastLockToAppLongPress = r2     // Catch:{ all -> 0x0099 }
            goto L_0x008b
        L_0x0059:
            int r6 = r11.getId()     // Catch:{ all -> 0x0099 }
            if (r6 != r12) goto L_0x0061
            r12 = r5
            goto L_0x008b
        L_0x0061:
            if (r2 == 0) goto L_0x0070
            if (r3 == 0) goto L_0x0070
            r1.stopSystemLockTaskMode()     // Catch:{ RemoteException -> 0x009b }
            android.view.View r10 = r10.mView     // Catch:{ RemoteException -> 0x009b }
            com.android.systemui.navigationbar.NavigationBarView r10 = (com.android.systemui.navigationbar.NavigationBarView) r10     // Catch:{ RemoteException -> 0x009b }
            r10.updateNavButtonIcons()     // Catch:{ RemoteException -> 0x009b }
            return r5
        L_0x0070:
            int r12 = r11.getId()     // Catch:{ all -> 0x0099 }
            if (r12 != r13) goto L_0x008a
            if (r13 != r4) goto L_0x0079
            goto L_0x0089
        L_0x0079:
            android.view.View r11 = r10.mView     // Catch:{ all -> 0x0099 }
            com.android.systemui.navigationbar.NavigationBarView r11 = (com.android.systemui.navigationbar.NavigationBarView) r11     // Catch:{ all -> 0x0099 }
            com.android.systemui.navigationbar.buttons.ButtonDispatcher r11 = r11.getHomeButton()     // Catch:{ all -> 0x0099 }
            android.view.View r11 = r11.getCurrentView()     // Catch:{ all -> 0x0099 }
            boolean r0 = r10.onHomeLongClick(r11)     // Catch:{ all -> 0x0099 }
        L_0x0089:
            return r0
        L_0x008a:
            r12 = r0
        L_0x008b:
            if (r12 == 0) goto L_0x00a3
            com.android.systemui.navigationbar.buttons.KeyButtonView r11 = (com.android.systemui.navigationbar.buttons.KeyButtonView) r11     // Catch:{ RemoteException -> 0x009b }
            r10 = 128(0x80, float:1.794E-43)
            r11.sendEvent(r0, r10)     // Catch:{ RemoteException -> 0x009b }
            r10 = 2
            r11.sendAccessibilityEvent(r10)     // Catch:{ RemoteException -> 0x009b }
            return r5
        L_0x0099:
            r10 = move-exception
            throw r10     // Catch:{ RemoteException -> 0x009b }
        L_0x009b:
            r10 = move-exception
            java.lang.String r11 = "NavigationBar"
            java.lang.String r12 = "Unable to reach activity manager"
            android.util.Log.d(r11, r12, r10)
        L_0x00a3:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBar.onLongPressNavigationButtons(android.view.View, int, int):boolean");
    }

    /* access modifiers changed from: private */
    public void onAccessibilityClick(View view) {
        Display display = view.getDisplay();
        this.mAccessibilityManager.notifyAccessibilityButtonClicked(display != null ? display.getDisplayId() : 0);
    }

    /* access modifiers changed from: private */
    public boolean onAccessibilityLongClick(View view) {
        Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
        intent.addFlags(268468224);
        intent.setClassName("android", AccessibilityButtonChooserActivity.class.getName());
        this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateAccessibilityStateFlags() {
        if (this.mView != null) {
            int a11yButtonState = this.mNavBarHelper.getA11yButtonState();
            boolean z = true;
            boolean z2 = (a11yButtonState & 16) != 0;
            if ((a11yButtonState & 32) == 0) {
                z = false;
            }
            ((NavigationBarView) this.mView).setAccessibilityButtonState(z2, z);
        }
        updateSystemUiStateFlags();
    }

    public void updateSystemUiStateFlags() {
        int a11yButtonState = this.mNavBarHelper.getA11yButtonState();
        boolean z = false;
        SysUiState flag = this.mSysUiFlagsContainer.setFlag(16, (a11yButtonState & 16) != 0).setFlag(32, (a11yButtonState & 32) != 0).setFlag(2, !isNavBarWindowVisible()).setFlag(262144, (this.mNavigationIconHints & 1) != 0);
        if ((this.mNavigationIconHints & 4) != 0) {
            z = true;
        }
        flag.setFlag(1048576, z).setFlag(131072, allowSystemGestureIgnoringBarVisibility()).commitUpdate(this.mDisplayId);
    }

    /* access modifiers changed from: private */
    public void updateAssistantEntrypoints(boolean z) {
        if (this.mOverviewProxyService.getProxy() != null) {
            try {
                this.mOverviewProxyService.getProxy().onAssistantAvailable(z);
            } catch (RemoteException unused) {
                Log.w(TAG, "Unable to send assistant availability data to launcher");
            }
        }
        reconfigureHomeLongClick();
    }

    public void touchAutoDim() {
        getBarTransitions().setAutoDim(false);
        this.mHandler.removeCallbacks(this.mAutoDim);
        int state = this.mStatusBarStateController.getState();
        if (state != 1 && state != 2) {
            this.mHandler.postDelayed(this.mAutoDim, AUTODIM_TIMEOUT_MS);
        }
    }

    public void setLightBarController(LightBarController lightBarController) {
        this.mLightBarController = lightBarController;
        if (lightBarController != null) {
            lightBarController.setNavigationBar(getBarTransitions().getLightTransitionsController());
        }
    }

    private void setWindowVisible(boolean z) {
        this.mRegionSamplingHelper.setWindowVisible(z);
        ((NavigationBarView) this.mView).setWindowVisible(z);
    }

    private void setAutoHideController(AutoHideController autoHideController) {
        this.mAutoHideController = autoHideController;
        if (autoHideController != null) {
            autoHideController.setNavigationBar(this.mAutoHideUiElement);
        }
        ((NavigationBarView) this.mView).setAutoHideController(autoHideController);
    }

    /* access modifiers changed from: private */
    public boolean isTransientShown() {
        return this.mTransientShown;
    }

    private void checkBarModes() {
        if (this.mIsOnDefaultDisplay) {
            this.mCentralSurfacesOptionalLazy.get().ifPresent(new NavigationBar$$ExternalSyntheticLambda5());
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
        boolean z = false;
        if (((Boolean) this.mCentralSurfacesOptionalLazy.get().map(new NavigationBar$$ExternalSyntheticLambda23()).orElse(false)).booleanValue() && this.mNavigationBarWindowState != 2) {
            z = true;
        }
        getBarTransitions().transitionTo(this.mTransitionMode, z);
    }

    public void disableAnimationsDuringHide(long j) {
        ((NavigationBarView) this.mView).setLayoutTransitionsEnabled(false);
        this.mHandler.postDelayed(this.mEnableLayoutTransitions, j + 448);
    }

    public void transitionTo(int i, boolean z) {
        getBarTransitions().transitionTo(i, z);
    }

    public NavigationBarTransitions getBarTransitions() {
        return this.mNavigationBarTransitions;
    }

    public void finishBarAnimations() {
        getBarTransitions().finishAnimations();
    }

    private WindowManager.LayoutParams getBarLayoutParams(int i) {
        WindowManager.LayoutParams barLayoutParamsForRotation = getBarLayoutParamsForRotation(i);
        barLayoutParamsForRotation.paramsForRotation = new WindowManager.LayoutParams[4];
        for (int i2 = 0; i2 <= 3; i2++) {
            barLayoutParamsForRotation.paramsForRotation[i2] = getBarLayoutParamsForRotation(i2);
        }
        return barLayoutParamsForRotation;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0057 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.view.WindowManager.LayoutParams getBarLayoutParamsForRotation(int r15) {
        /*
            r14 = this;
            com.android.systemui.settings.UserContextProvider r0 = r14.mUserContextProvider
            android.content.Context r1 = r14.mContext
            android.content.Context r0 = r0.createCurrentUserContext(r1)
            android.view.WindowManager r1 = r14.mWindowManager
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x0038
            android.view.WindowMetrics r1 = r1.getCurrentWindowMetrics()
            if (r1 == 0) goto L_0x0038
            android.view.WindowManager r14 = r14.mWindowManager
            android.view.WindowMetrics r14 = r14.getCurrentWindowMetrics()
            android.graphics.Rect r14 = r14.getBounds()
            int r1 = r14.width()
            int r14 = r14.height()
            if (r1 == r14) goto L_0x0036
            android.content.res.Resources r14 = r0.getResources()
            r1 = 17891709(0x111017d, float:2.6633362E-38)
            boolean r14 = r14.getBoolean(r1)
            if (r14 == 0) goto L_0x0036
            goto L_0x0038
        L_0x0036:
            r14 = r2
            goto L_0x0039
        L_0x0038:
            r14 = r3
        L_0x0039:
            r1 = 17105359(0x10501cf, float:2.442954E-38)
            r4 = 17105355(0x10501cb, float:2.4429528E-38)
            r5 = 3
            r6 = -1
            r7 = 80
            if (r14 != 0) goto L_0x0057
            android.content.res.Resources r14 = r0.getResources()
            int r14 = r14.getDimensionPixelSize(r4)
            android.content.res.Resources r15 = r0.getResources()
            int r15 = r15.getDimensionPixelSize(r1)
        L_0x0055:
            r9 = r6
            goto L_0x0092
        L_0x0057:
            if (r15 == r6) goto L_0x0081
            if (r15 == 0) goto L_0x0081
            r14 = 17105364(0x10501d4, float:2.4429554E-38)
            if (r15 == r3) goto L_0x0074
            r8 = 2
            if (r15 == r8) goto L_0x0081
            if (r15 == r5) goto L_0x0069
            r14 = r6
            r15 = r14
            r9 = r15
            goto L_0x0092
        L_0x0069:
            android.content.res.Resources r15 = r0.getResources()
            int r14 = r15.getDimensionPixelSize(r14)
            r9 = r14
            r7 = r5
            goto L_0x007e
        L_0x0074:
            android.content.res.Resources r15 = r0.getResources()
            int r14 = r15.getDimensionPixelSize(r14)
            r7 = 5
            r9 = r14
        L_0x007e:
            r14 = r6
            r15 = r14
            goto L_0x0092
        L_0x0081:
            android.content.res.Resources r14 = r0.getResources()
            int r14 = r14.getDimensionPixelSize(r4)
            android.content.res.Resources r15 = r0.getResources()
            int r15 = r15.getDimensionPixelSize(r1)
            goto L_0x0055
        L_0x0092:
            android.view.WindowManager$LayoutParams r1 = new android.view.WindowManager$LayoutParams
            r11 = 2019(0x7e3, float:2.829E-42)
            r12 = 545521768(0x20840068, float:2.2361939E-19)
            r13 = -3
            r8 = r1
            r10 = r14
            r8.<init>(r9, r10, r11, r12, r13)
            r1.gravity = r7
            r4 = 24
            android.graphics.Insets[] r4 = new android.graphics.Insets[r4]
            r1.providedInternalInsets = r4
            if (r15 == r6) goto L_0x00b3
            android.graphics.Insets[] r4 = r1.providedInternalInsets
            int r14 = r14 - r15
            android.graphics.Insets r14 = android.graphics.Insets.of(r2, r14, r2, r2)
            r4[r3] = r14
            goto L_0x00b8
        L_0x00b3:
            android.graphics.Insets[] r14 = r1.providedInternalInsets
            r15 = 0
            r14[r3] = r15
        L_0x00b8:
            android.os.Binder r14 = new android.os.Binder
            r14.<init>()
            r1.token = r14
            r14 = 2131952867(0x7f1304e3, float:1.9542189E38)
            java.lang.String r14 = r0.getString(r14)
            r1.accessibilityTitle = r14
            int r14 = r1.privateFlags
            r15 = 16785408(0x1002000, float:2.3532846E-38)
            r14 = r14 | r15
            r1.privateFlags = r14
            r1.layoutInDisplayCutoutMode = r5
            r1.windowAnimations = r2
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            java.lang.String r15 = "NavigationBar"
            r14.<init>((java.lang.String) r15)
            int r15 = r0.getDisplayId()
            java.lang.StringBuilder r14 = r14.append((int) r15)
            java.lang.String r14 = r14.toString()
            r1.setTitle(r14)
            r1.setFitInsetsTypes(r2)
            r1.setTrustedOverlay()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBar.getBarLayoutParamsForRotation(int):android.view.WindowManager$LayoutParams");
    }

    /* access modifiers changed from: private */
    public boolean canShowSecondaryHandle() {
        return this.mNavBarMode == 2 && this.mOrientationHandle != null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$9$com-android-systemui-navigationbar-NavigationBar  reason: not valid java name */
    public /* synthetic */ void m2854lambda$new$9$comandroidsystemuinavigationbarNavigationBar(Integer num) {
        if (this.mView != null && ((NavigationBarView) this.mView).needsReorient(num.intValue())) {
            repositionNavigationBar(num.intValue());
        }
    }

    /* access modifiers changed from: package-private */
    public int getNavigationIconHints() {
        return this.mNavigationIconHints;
    }

    private void setNavigationIconHints(int i) {
        if (i != this.mNavigationIconHints) {
            if (!Utilities.isTablet(this.mContext)) {
                boolean z = false;
                boolean z2 = (i & 1) != 0;
                if ((this.mNavigationIconHints & 1) != 0) {
                    z = true;
                }
                if (z2 != z) {
                    ((NavigationBarView) this.mView).onImeVisibilityChanged(z2);
                    this.mImeVisible = z2;
                }
                ((NavigationBarView) this.mView).setNavigationIconHints(i);
            }
            this.mNavigationIconHints = i;
        }
    }

    /* access modifiers changed from: package-private */
    public Region getButtonLocations(boolean z, boolean z2, boolean z3) {
        if (z3 && !z2) {
            z3 = false;
        }
        Region region = new Region();
        Map<View, Rect> buttonTouchRegionCache = ((NavigationBarView) this.mView).getButtonTouchRegionCache();
        Region region2 = region;
        Map<View, Rect> map = buttonTouchRegionCache;
        boolean z4 = z2;
        boolean z5 = z3;
        updateButtonLocation(region2, map, ((NavigationBarView) this.mView).getBackButton(), z4, z5);
        updateButtonLocation(region2, map, ((NavigationBarView) this.mView).getHomeButton(), z4, z5);
        updateButtonLocation(region2, map, ((NavigationBarView) this.mView).getRecentsButton(), z4, z5);
        updateButtonLocation(region2, map, ((NavigationBarView) this.mView).getImeSwitchButton(), z4, z5);
        updateButtonLocation(region2, map, ((NavigationBarView) this.mView).getAccessibilityButton(), z4, z5);
        if (!z || !((NavigationBarView) this.mView).getFloatingRotationButton().isVisible()) {
            updateButtonLocation(region, buttonTouchRegionCache, ((NavigationBarView) this.mView).getRotateSuggestionButton(), z2, z3);
        } else {
            updateButtonLocation(region, ((NavigationBarView) this.mView).getFloatingRotationButton().getCurrentView(), z2);
        }
        return region;
    }

    private void updateButtonLocation(Region region, Map<View, Rect> map, ButtonDispatcher buttonDispatcher, boolean z, boolean z2) {
        View currentView;
        if (buttonDispatcher != null && (currentView = buttonDispatcher.getCurrentView()) != null && buttonDispatcher.isVisible()) {
            if (!z2 || !map.containsKey(currentView)) {
                updateButtonLocation(region, currentView, z);
            } else {
                region.op(map.get(currentView), Region.Op.UNION);
            }
        }
    }

    private void updateButtonLocation(Region region, View view, boolean z) {
        Rect rect = new Rect();
        if (z) {
            view.getBoundsOnScreen(rect);
        } else {
            int[] iArr = new int[2];
            view.getLocationInWindow(iArr);
            int i = iArr[0];
            rect.set(i, iArr[1], view.getWidth() + i, iArr[1] + view.getHeight());
        }
        region.op(rect, Region.Op.UNION);
    }

    /* access modifiers changed from: package-private */
    public void setOrientedHandleSamplingRegion(Rect rect) {
        this.mOrientedHandleSamplingRegion = rect;
        this.mRegionSamplingHelper.updateSamplingRect();
    }

    /* access modifiers changed from: private */
    public Rect calculateSamplingRect() {
        this.mSamplingBounds.setEmpty();
        View currentView = ((NavigationBarView) this.mView).getHomeHandle().getCurrentView();
        if (!QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            currentView = ((NavigationBarView) this.mView).getCurrentView().findViewById(C1893R.C1897id.nav_buttons);
        }
        if (currentView != null) {
            int[] iArr = new int[2];
            currentView.getLocationOnScreen(iArr);
            Point point = new Point();
            currentView.getContext().getDisplay().getRealSize(point);
            this.mSamplingBounds.set(new Rect(iArr[0] - this.mNavColorSampleMargin, point.y - ((NavigationBarView) this.mView).getNavBarHeight(), iArr[0] + currentView.getWidth() + this.mNavColorSampleMargin, point.y));
        }
        return this.mSamplingBounds;
    }

    /* access modifiers changed from: package-private */
    public void setNavigationBarLumaSamplingEnabled(boolean z) {
        if (z) {
            this.mRegionSamplingHelper.start(this.mSamplingBounds);
        } else {
            this.mRegionSamplingHelper.stop();
        }
    }

    /* access modifiers changed from: private */
    public void setNavBarMode(int i) {
        ((NavigationBarView) this.mView).setNavBarMode(i, this.mNavigationModeController.getImeDrawsImeNavBar());
        if (this.mEx.isRegionSamplingAvailable(this.mNavigationBarTransitions.getMode())) {
            this.mRegionSamplingHelper.start(this.mSamplingBounds);
        } else {
            this.mRegionSamplingHelper.stop();
        }
    }

    /* access modifiers changed from: package-private */
    public void onBarTransition(int i) {
        if (i == 4) {
            this.mRegionSamplingHelper.stop();
            getBarTransitions().getLightTransitionsController().setIconsDark(false, true);
            return;
        }
        this.mRegionSamplingHelper.start(this.mSamplingBounds);
    }
}
