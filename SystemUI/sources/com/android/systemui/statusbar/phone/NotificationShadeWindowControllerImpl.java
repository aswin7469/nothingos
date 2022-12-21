package com.android.systemui.statusbar.phone;

import android.app.IActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Region;
import android.os.Binder;
import android.os.Build;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Log;
import android.view.Display;
import android.view.IWindow;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.inject.Inject;

@SysUISingleton
public class NotificationShadeWindowControllerImpl implements NotificationShadeWindowController, Dumpable, ConfigurationController.ConfigurationListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "NotificationShadeWindowController";
    private final IActivityManager mActivityManager;
    private final AuthController mAuthController;
    private final ArrayList<WeakReference<StatusBarWindowCallback>> mCallbacks = new ArrayList<>();
    private final SysuiColorExtractor mColorExtractor;
    private final Context mContext;
    private final State mCurrentState = new State();
    private int mDeferWindowLayoutParams;
    private final DozeParameters mDozeParameters;
    private final Set<Object> mForceOpenTokens = new HashSet();
    private NotificationShadeWindowController.ForcePluginOpenListener mForcePluginOpenListener;
    private boolean mHasTopUi;
    private boolean mHasTopUiChanged;
    private final KeyguardBypassController mKeyguardBypassController;
    private final float mKeyguardMaxRefreshRate;
    private final float mKeyguardPreferredRefreshRate;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardViewMediator mKeyguardViewMediator;
    private boolean mLastKeyguardRotationAllowed;
    private NotificationShadeWindowController.OtherwisedCollapsedListener mListener;
    private final long mLockScreenDisplayTimeout;
    private WindowManager.LayoutParams mLp;
    private final WindowManager.LayoutParams mLpChanged;
    private ViewGroup mNotificationShadeView;
    private float mScreenBrightnessDoze;
    private final ScreenOffAnimationController mScreenOffAnimationController;
    private Consumer<Integer> mScrimsVisibilityListener;
    private final StatusBarStateController.StateListener mStateListener;
    private final WindowManager mWindowManager;

    @Inject
    public NotificationShadeWindowControllerImpl(Context context, WindowManager windowManager, IActivityManager iActivityManager, DozeParameters dozeParameters, StatusBarStateController statusBarStateController, ConfigurationController configurationController, KeyguardViewMediator keyguardViewMediator, KeyguardBypassController keyguardBypassController, SysuiColorExtractor sysuiColorExtractor, DumpManager dumpManager, KeyguardStateController keyguardStateController, ScreenOffAnimationController screenOffAnimationController, AuthController authController) {
        C30351 r0 = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                NotificationShadeWindowControllerImpl.this.setStatusBarState(i);
            }

            public void onDozingChanged(boolean z) {
                NotificationShadeWindowControllerImpl.this.setDozing(z);
            }
        };
        this.mStateListener = r0;
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mActivityManager = iActivityManager;
        this.mDozeParameters = dozeParameters;
        this.mKeyguardStateController = keyguardStateController;
        this.mScreenBrightnessDoze = dozeParameters.getScreenBrightnessDoze();
        this.mLpChanged = new WindowManager.LayoutParams();
        this.mKeyguardViewMediator = keyguardViewMediator;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mColorExtractor = sysuiColorExtractor;
        this.mScreenOffAnimationController = screenOffAnimationController;
        dumpManager.registerDumpable(getClass().getName(), this);
        this.mAuthController = authController;
        this.mLastKeyguardRotationAllowed = keyguardStateController.isKeyguardScreenRotationAllowed();
        this.mLockScreenDisplayTimeout = (long) context.getResources().getInteger(C1893R.integer.config_lockScreenDisplayTimeout);
        ((SysuiStatusBarStateController) statusBarStateController).addCallback(r0, 1);
        configurationController.addCallback(this);
        float integer = (float) context.getResources().getInteger(C1893R.integer.config_keyguardRefreshRate);
        float f = -1.0f;
        if (integer > -1.0f) {
            Display.Mode[] supportedModes = context.getDisplay().getSupportedModes();
            int length = supportedModes.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Display.Mode mode = supportedModes[i];
                if (((double) Math.abs(mode.getRefreshRate() - integer)) <= 0.1d) {
                    f = mode.getRefreshRate();
                    break;
                }
                i++;
            }
        }
        this.mKeyguardPreferredRefreshRate = f;
        this.mKeyguardMaxRefreshRate = (float) context.getResources().getInteger(C1893R.integer.config_keyguardMaxRefreshRate);
    }

    public void registerCallback(StatusBarWindowCallback statusBarWindowCallback) {
        int i = 0;
        while (i < this.mCallbacks.size()) {
            if (this.mCallbacks.get(i).get() != statusBarWindowCallback) {
                i++;
            } else {
                return;
            }
        }
        this.mCallbacks.add(new WeakReference(statusBarWindowCallback));
    }

    public void unregisterCallback(StatusBarWindowCallback statusBarWindowCallback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == statusBarWindowCallback) {
                this.mCallbacks.remove(i);
                return;
            }
        }
    }

    public void setScrimsVisibilityListener(Consumer<Integer> consumer) {
        if (consumer != null && this.mScrimsVisibilityListener != consumer) {
            this.mScrimsVisibilityListener = consumer;
        }
    }

    public void attach() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2040, -2138832824, -3);
        this.mLp = layoutParams;
        layoutParams.token = new Binder();
        this.mLp.gravity = 48;
        this.mLp.setFitInsetsTypes(0);
        this.mLp.softInputMode = 16;
        this.mLp.setTitle("NotificationShade");
        this.mLp.packageName = this.mContext.getPackageName();
        this.mLp.layoutInDisplayCutoutMode = 3;
        this.mLp.privateFlags |= 134217728;
        this.mLp.insetsFlags.behavior = 2;
        this.mWindowManager.addView(this.mNotificationShadeView, this.mLp);
        this.mLpChanged.copyFrom(this.mLp);
        onThemeChanged();
        if (this.mKeyguardViewMediator.isShowingAndNotOccluded()) {
            setKeyguardShowing(true);
        }
    }

    public void setNotificationShadeView(ViewGroup viewGroup) {
        this.mNotificationShadeView = viewGroup;
    }

    public ViewGroup getNotificationShadeView() {
        return this.mNotificationShadeView;
    }

    public void setDozeScreenBrightness(int i) {
        this.mScreenBrightnessDoze = ((float) i) / 255.0f;
    }

    private void setKeyguardDark(boolean z) {
        int systemUiVisibility = this.mNotificationShadeView.getSystemUiVisibility();
        this.mNotificationShadeView.setSystemUiVisibility(z ? systemUiVisibility | 16 | 8192 : systemUiVisibility & -17 & -8193);
    }

    private void applyKeyguardFlags(State state) {
        boolean z = false;
        if ((!(state.mKeyguardShowing || (state.mDozing && this.mDozeParameters.getAlwaysOn())) || state.mBackdropShowing || state.mLightRevealScrimOpaque) && !this.mKeyguardViewMediator.isAnimatingBetweenKeyguardAndSurfaceBehind()) {
            this.mLpChanged.flags &= -1048577;
        } else {
            this.mLpChanged.flags |= 1048576;
        }
        if (state.mDozing) {
            this.mLpChanged.privateFlags |= 524288;
        } else {
            this.mLpChanged.privateFlags &= -524289;
        }
        if (this.mKeyguardPreferredRefreshRate > 0.0f) {
            if (state.mStatusBarState == 1 && !state.mKeyguardFadingAway && !state.mKeyguardGoingAway) {
                z = true;
            }
            if (!z || !this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
                this.mLpChanged.preferredMaxDisplayRefreshRate = 0.0f;
            } else {
                this.mLpChanged.preferredMaxDisplayRefreshRate = this.mKeyguardPreferredRefreshRate;
            }
            Trace.setCounter("display_set_preferred_refresh_rate", (long) this.mKeyguardPreferredRefreshRate);
        } else if (this.mKeyguardMaxRefreshRate > 0.0f) {
            if (this.mKeyguardBypassController.getBypassEnabled() && state.mStatusBarState == 1 && !state.mKeyguardFadingAway && !state.mKeyguardGoingAway) {
                z = true;
            }
            if (state.mDozing || z) {
                this.mLpChanged.preferredMaxDisplayRefreshRate = this.mKeyguardMaxRefreshRate;
            } else {
                this.mLpChanged.preferredMaxDisplayRefreshRate = 0.0f;
            }
            Trace.setCounter("display_max_refresh_rate", (long) this.mLpChanged.preferredMaxDisplayRefreshRate);
        }
        if (!state.mBouncerShowing || isDebuggable()) {
            this.mLpChanged.flags &= -8193;
            return;
        }
        this.mLpChanged.flags |= 8192;
    }

    /* access modifiers changed from: protected */
    public boolean isDebuggable() {
        return Build.IS_DEBUGGABLE;
    }

    private void adjustScreenOrientation(State state) {
        if (!state.mBouncerShowing && !state.isKeyguardShowingAndNotOccluded() && !state.mDozing) {
            this.mLpChanged.screenOrientation = -1;
        } else if (this.mKeyguardStateController.isKeyguardScreenRotationAllowed()) {
            this.mLpChanged.screenOrientation = 2;
        } else {
            this.mLpChanged.screenOrientation = 5;
        }
    }

    private void applyFocusableFlag(State state) {
        boolean z = state.mNotificationShadeFocusable && state.mPanelExpanded;
        if ((state.mBouncerShowing && (state.mKeyguardOccluded || state.mKeyguardNeedsInput)) || ((NotificationRemoteInputManager.ENABLE_REMOTE_INPUT && state.mRemoteInputActive) || this.mScreenOffAnimationController.shouldIgnoreKeyguardTouches())) {
            this.mLpChanged.flags &= -9;
            this.mLpChanged.flags &= -131073;
        } else if (state.isKeyguardShowingAndNotOccluded() || z) {
            this.mLpChanged.flags &= -9;
            if (!state.mKeyguardNeedsInput || !state.isKeyguardShowingAndNotOccluded()) {
                this.mLpChanged.flags |= 131072;
            } else {
                this.mLpChanged.flags &= -131073;
            }
        } else {
            this.mLpChanged.flags |= 8;
            this.mLpChanged.flags &= -131073;
        }
        this.mLpChanged.softInputMode = 16;
    }

    private void applyForceShowNavigationFlag(State state) {
        if (state.mPanelExpanded || state.mBouncerShowing || (NotificationRemoteInputManager.ENABLE_REMOTE_INPUT && state.mRemoteInputActive)) {
            this.mLpChanged.privateFlags |= 8388608;
            return;
        }
        this.mLpChanged.privateFlags &= -8388609;
    }

    private void applyVisibility(State state) {
        boolean isExpanded = isExpanded(state);
        if (state.mForcePluginOpen) {
            NotificationShadeWindowController.OtherwisedCollapsedListener otherwisedCollapsedListener = this.mListener;
            if (otherwisedCollapsedListener != null) {
                otherwisedCollapsedListener.setWouldOtherwiseCollapse(isExpanded);
            }
            isExpanded = true;
        }
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup == null) {
            return;
        }
        if (isExpanded) {
            viewGroup.setVisibility(0);
        } else {
            viewGroup.setVisibility(4);
        }
    }

    private boolean isExpanded(State state) {
        return (!state.mForceCollapsed && (state.isKeyguardShowingAndNotOccluded() || state.mPanelVisible || state.mKeyguardFadingAway || state.mBouncerShowing || state.mHeadsUpShowing || state.mScrimsVisibility != 0)) || state.mBackgroundBlurRadius > 0 || state.mLaunchingActivity;
    }

    private void applyFitsSystemWindows(State state) {
        boolean z = !state.isKeyguardShowingAndNotOccluded();
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup != null && viewGroup.getFitsSystemWindows() != z) {
            this.mNotificationShadeView.setFitsSystemWindows(z);
            this.mNotificationShadeView.requestApplyInsets();
        }
    }

    private void applyUserActivityTimeout(State state) {
        long j;
        if (!state.isKeyguardShowingAndNotOccluded() || state.mStatusBarState != 1 || state.mQsExpanded) {
            this.mLpChanged.userActivityTimeout = -1;
            return;
        }
        WindowManager.LayoutParams layoutParams = this.mLpChanged;
        if (state.mBouncerShowing) {
            j = 10000;
        } else {
            j = this.mLockScreenDisplayTimeout;
        }
        layoutParams.userActivityTimeout = j;
    }

    private void applyInputFeatures(State state) {
        if (!state.isKeyguardShowingAndNotOccluded() || state.mStatusBarState != 1 || state.mQsExpanded || state.mForceUserActivity) {
            this.mLpChanged.inputFeatures &= -3;
            return;
        }
        this.mLpChanged.inputFeatures |= 2;
    }

    private void applyStatusBarColorSpaceAgnosticFlag(State state) {
        if (!isExpanded(state)) {
            this.mLpChanged.privateFlags |= 16777216;
            return;
        }
        this.mLpChanged.privateFlags &= -16777217;
    }

    private void applyWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams;
        if (this.mDeferWindowLayoutParams == 0 && (layoutParams = this.mLp) != null && layoutParams.copyFrom(this.mLpChanged) != 0) {
            Trace.beginSection("updateViewLayout");
            this.mWindowManager.updateViewLayout(this.mNotificationShadeView, this.mLp);
            Trace.endSection();
        }
    }

    public void batchApplyWindowLayoutParams(Runnable runnable) {
        this.mDeferWindowLayoutParams++;
        runnable.run();
        this.mDeferWindowLayoutParams--;
        applyWindowLayoutParams();
    }

    private void apply(State state) {
        applyKeyguardFlags(state);
        applyFocusableFlag(state);
        applyForceShowNavigationFlag(state);
        adjustScreenOrientation(state);
        applyVisibility(state);
        applyUserActivityTimeout(state);
        applyInputFeatures(state);
        applyFitsSystemWindows(state);
        applyModalFlag(state);
        applyBrightness(state);
        applyHasTopUi(state);
        applyNotTouchable(state);
        applyStatusBarColorSpaceAgnosticFlag(state);
        applyWindowLayoutParams();
        if (this.mHasTopUi != this.mHasTopUiChanged) {
            DejankUtils.whitelistIpcs((Runnable) new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda2(this));
        }
        notifyStateChangedCallbacks();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$apply$0$com-android-systemui-statusbar-phone-NotificationShadeWindowControllerImpl */
    public /* synthetic */ void mo44746x55fd2eea() {
        try {
            this.mActivityManager.setHasTopUi(this.mHasTopUiChanged);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to call setHasTopUi", e);
        }
        this.mHasTopUi = this.mHasTopUiChanged;
    }

    public void notifyStateChangedCallbacks() {
        for (StatusBarWindowCallback onStateChanged : (List) this.mCallbacks.stream().map(new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda0()).filter(new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda1()).collect(Collectors.toList())) {
            onStateChanged.onStateChanged(this.mCurrentState.mKeyguardShowing, this.mCurrentState.mKeyguardOccluded, this.mCurrentState.mBouncerShowing, this.mCurrentState.mDozing);
        }
    }

    private void applyModalFlag(State state) {
        if (state.mHeadsUpShowing) {
            this.mLpChanged.flags |= 32;
            return;
        }
        this.mLpChanged.flags &= -33;
    }

    private void applyBrightness(State state) {
        if (state.mForceDozeBrightness) {
            this.mLpChanged.screenBrightness = this.mScreenBrightnessDoze;
            return;
        }
        this.mLpChanged.screenBrightness = -1.0f;
    }

    private void applyHasTopUi(State state) {
        this.mHasTopUiChanged = !state.mComponentsForcingTopUi.isEmpty() || isExpanded(state);
    }

    private void applyNotTouchable(State state) {
        if (state.mNotTouchable) {
            this.mLpChanged.flags |= 16;
            return;
        }
        this.mLpChanged.flags &= -17;
    }

    public void setTouchExclusionRegion(Region region) {
        try {
            WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(IWindow.Stub.asInterface(getNotificationShadeView().getWindowToken()), region);
        } catch (RemoteException e) {
            Log.e(TAG, "could not update the tap exclusion region:" + e);
        }
    }

    public void setKeyguardShowing(boolean z) {
        this.mCurrentState.mKeyguardShowing = z;
        apply(this.mCurrentState);
    }

    public void setKeyguardOccluded(boolean z) {
        this.mCurrentState.mKeyguardOccluded = z;
        apply(this.mCurrentState);
    }

    public void setKeyguardNeedsInput(boolean z) {
        this.mCurrentState.mKeyguardNeedsInput = z;
        apply(this.mCurrentState);
    }

    public void setPanelVisible(boolean z) {
        if (this.mCurrentState.mPanelVisible != z || this.mCurrentState.mNotificationShadeFocusable != z) {
            this.mCurrentState.mPanelVisible = z;
            this.mCurrentState.mNotificationShadeFocusable = z;
            apply(this.mCurrentState);
        }
    }

    public void setNotificationShadeFocusable(boolean z) {
        this.mCurrentState.mNotificationShadeFocusable = z;
        apply(this.mCurrentState);
    }

    public void setBouncerShowing(boolean z) {
        this.mCurrentState.mBouncerShowing = z;
        apply(this.mCurrentState);
    }

    public void setBackdropShowing(boolean z) {
        this.mCurrentState.mBackdropShowing = z;
        apply(this.mCurrentState);
    }

    public void setKeyguardFadingAway(boolean z) {
        this.mCurrentState.mKeyguardFadingAway = z;
        apply(this.mCurrentState);
    }

    public void setQsExpanded(boolean z) {
        this.mCurrentState.mQsExpanded = z;
        apply(this.mCurrentState);
    }

    public void setForceUserActivity(boolean z) {
        this.mCurrentState.mForceUserActivity = z;
        apply(this.mCurrentState);
    }

    public void setLaunchingActivity(boolean z) {
        this.mCurrentState.mLaunchingActivity = z;
        apply(this.mCurrentState);
    }

    public boolean isLaunchingActivity() {
        return this.mCurrentState.mLaunchingActivity;
    }

    public void setScrimsVisibility(int i) {
        if (i != this.mCurrentState.mScrimsVisibility) {
            boolean isExpanded = isExpanded(this.mCurrentState);
            this.mCurrentState.mScrimsVisibility = i;
            if (isExpanded != isExpanded(this.mCurrentState)) {
                apply(this.mCurrentState);
            }
            this.mScrimsVisibilityListener.accept(Integer.valueOf(i));
        }
    }

    public void setBackgroundBlurRadius(int i) {
        if (this.mCurrentState.mBackgroundBlurRadius != i) {
            this.mCurrentState.mBackgroundBlurRadius = i;
            apply(this.mCurrentState);
        }
    }

    public void setHeadsUpShowing(boolean z) {
        this.mCurrentState.mHeadsUpShowing = z;
        apply(this.mCurrentState);
    }

    public void setLightRevealScrimOpaque(boolean z) {
        if (this.mCurrentState.mLightRevealScrimOpaque != z) {
            this.mCurrentState.mLightRevealScrimOpaque = z;
            apply(this.mCurrentState);
        }
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mCurrentState.mWallpaperSupportsAmbientMode = z;
        apply(this.mCurrentState);
    }

    /* access modifiers changed from: private */
    public void setStatusBarState(int i) {
        this.mCurrentState.mStatusBarState = i;
        apply(this.mCurrentState);
    }

    public void setForceWindowCollapsed(boolean z) {
        this.mCurrentState.mForceCollapsed = z;
        apply(this.mCurrentState);
    }

    public void setPanelExpanded(boolean z) {
        if (this.mCurrentState.mPanelExpanded != z) {
            this.mCurrentState.mPanelExpanded = z;
            apply(this.mCurrentState);
        }
    }

    public void onRemoteInputActive(boolean z) {
        this.mCurrentState.mRemoteInputActive = z;
        apply(this.mCurrentState);
    }

    public void setForceDozeBrightness(boolean z) {
        if (this.mCurrentState.mForceDozeBrightness != z) {
            this.mCurrentState.mForceDozeBrightness = z;
            apply(this.mCurrentState);
        }
    }

    public void setDozing(boolean z) {
        this.mCurrentState.mDozing = z;
        apply(this.mCurrentState);
    }

    public void setForcePluginOpen(boolean z, Object obj) {
        if (z) {
            this.mForceOpenTokens.add(obj);
        } else {
            this.mForceOpenTokens.remove(obj);
        }
        boolean z2 = this.mCurrentState.mForcePluginOpen;
        this.mCurrentState.mForcePluginOpen = !this.mForceOpenTokens.isEmpty();
        if (z2 != this.mCurrentState.mForcePluginOpen) {
            apply(this.mCurrentState);
            NotificationShadeWindowController.ForcePluginOpenListener forcePluginOpenListener = this.mForcePluginOpenListener;
            if (forcePluginOpenListener != null) {
                forcePluginOpenListener.onChange(this.mCurrentState.mForcePluginOpen);
            }
        }
    }

    public boolean getForcePluginOpen() {
        return this.mCurrentState.mForcePluginOpen;
    }

    public void setNotTouchable(boolean z) {
        this.mCurrentState.mNotTouchable = z;
        apply(this.mCurrentState);
    }

    public boolean getPanelExpanded() {
        return this.mCurrentState.mPanelExpanded;
    }

    public void setStateListener(NotificationShadeWindowController.OtherwisedCollapsedListener otherwisedCollapsedListener) {
        this.mListener = otherwisedCollapsedListener;
    }

    public void setForcePluginOpenListener(NotificationShadeWindowController.ForcePluginOpenListener forcePluginOpenListener) {
        this.mForcePluginOpenListener = forcePluginOpenListener;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationShadeWindowController:");
        printWriter.println("  mKeyguardMaxRefreshRate=" + this.mKeyguardMaxRefreshRate);
        printWriter.println("  mKeyguardPreferredRefreshRate=" + this.mKeyguardPreferredRefreshRate);
        printWriter.println("  mDeferWindowLayoutParams=" + this.mDeferWindowLayoutParams);
        printWriter.println((Object) this.mCurrentState);
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup != null && viewGroup.getViewRootImpl() != null) {
            this.mNotificationShadeView.getViewRootImpl().dump("  ", printWriter);
        }
    }

    public boolean isShowingWallpaper() {
        return !this.mCurrentState.mBackdropShowing;
    }

    public void onThemeChanged() {
        if (this.mNotificationShadeView != null) {
            setKeyguardDark(this.mColorExtractor.getNeutralColors().supportsDarkText());
        }
    }

    public void onConfigChanged(Configuration configuration) {
        boolean isKeyguardScreenRotationAllowed = this.mKeyguardStateController.isKeyguardScreenRotationAllowed();
        if (this.mLastKeyguardRotationAllowed != isKeyguardScreenRotationAllowed) {
            apply(this.mCurrentState);
            this.mLastKeyguardRotationAllowed = isKeyguardScreenRotationAllowed;
        }
    }

    public void setKeyguardGoingAway(boolean z) {
        this.mCurrentState.mKeyguardGoingAway = z;
        apply(this.mCurrentState);
    }

    public void setRequestTopUi(boolean z, String str) {
        if (z) {
            this.mCurrentState.mComponentsForcingTopUi.add(str);
        } else {
            this.mCurrentState.mComponentsForcingTopUi.remove(str);
        }
        apply(this.mCurrentState);
    }

    private static class State {
        boolean mBackdropShowing;
        int mBackgroundBlurRadius;
        boolean mBouncerShowing;
        Set<String> mComponentsForcingTopUi;
        boolean mDozing;
        boolean mForceCollapsed;
        boolean mForceDozeBrightness;
        boolean mForcePluginOpen;
        boolean mForceUserActivity;
        boolean mHeadsUpShowing;
        boolean mKeyguardFadingAway;
        boolean mKeyguardGoingAway;
        boolean mKeyguardNeedsInput;
        boolean mKeyguardOccluded;
        boolean mKeyguardShowing;
        boolean mLaunchingActivity;
        boolean mLightRevealScrimOpaque;
        boolean mNotTouchable;
        boolean mNotificationShadeFocusable;
        boolean mPanelExpanded;
        boolean mPanelVisible;
        boolean mQsExpanded;
        boolean mRemoteInputActive;
        int mScrimsVisibility;
        int mStatusBarState;
        boolean mWallpaperSupportsAmbientMode;

        private State() {
            this.mComponentsForcingTopUi = new HashSet();
        }

        /* access modifiers changed from: private */
        public boolean isKeyguardShowingAndNotOccluded() {
            return this.mKeyguardShowing && !this.mKeyguardOccluded;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Window State {\n");
            for (Field field : getClass().getDeclaredFields()) {
                sb.append("  ");
                try {
                    sb.append(field.getName());
                    sb.append(": ");
                    sb.append(field.get(this));
                } catch (IllegalAccessException unused) {
                }
                sb.append("\n");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
