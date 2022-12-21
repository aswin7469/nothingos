package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.util.MathUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.util.function.TriConsumer;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.statusbar.notification.stack.ViewState;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.AlarmTimeout;
import com.android.systemui.util.LargeScreenUtils;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import com.nothing.systemui.util.NTColorUtil;
import com.nothing.systemui.util.NTLogUtil;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;

@SysUISingleton
public class ScrimController implements ViewTreeObserver.OnPreDrawListener, Dumpable {
    public static final long ANIMATION_DURATION = 220;
    public static final long ANIMATION_DURATION_LONG = 1000;
    public static final float BUSY_SCRIM_ALPHA = 1.0f;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    public static final int DEBUG_BEHIND_TINT = -16776961;
    public static final int DEBUG_FRONT_TINT = -16711936;
    public static final boolean DEBUG_MODE = false;
    public static final int DEBUG_NOTIFICATIONS_TINT = -65536;
    public static final float GAR_SCRIM_ALPHA = 0.6f;
    protected static final float KEYGUARD_SCRIM_ALPHA = 0.2f;
    private static final float NOT_INITIALIZED = -1.0f;
    public static final int OPAQUE = 2;
    public static final int SEMI_TRANSPARENT = 1;
    static final String TAG = "ScrimController";
    private static final int TAG_END_ALPHA = 2131428780;
    static final int TAG_KEY_ANIM = 2131428779;
    private static final int TAG_START_ALPHA = 2131428781;
    public static final int TRANSPARENT = 0;
    public static final float WAKE_SENSOR_SCRIM_ALPHA = 0.6f;
    private float mAdditionalScrimBehindAlphaKeyguard = 0.0f;
    private boolean mAnimateChange;
    private boolean mAnimatingPanelExpansionOnUnlock;
    private long mAnimationDelay;
    private long mAnimationDuration = -1;
    private Animator.AnimatorListener mAnimatorListener;
    private float mBehindAlpha = -1.0f;
    private int mBehindTint;
    private boolean mBlankScreen;
    private Runnable mBlankingTransitionRunnable;
    private float mBouncerHiddenFraction = 1.0f;
    /* access modifiers changed from: private */
    public Callback mCallback;
    private boolean mClipsQsScrim;
    private ColorExtractor.GradientColors mColors;
    private boolean mDarkenWhileDragging;
    private final float mDefaultScrimAlpha;
    private final DockManager mDockManager;
    private final DozeParameters mDozeParameters;
    private boolean mExpansionAffectsAlpha = true;
    private final Handler mHandler;
    private float mInFrontAlpha = -1.0f;
    private int mInFrontTint;
    private final Interpolator mInterpolator = new DecelerateInterpolator();
    private boolean mKeyguardOccluded;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardVisibilityCallback mKeyguardVisibilityCallback;
    private final Executor mMainExecutor;
    /* access modifiers changed from: private */
    public boolean mNeedsDrawableColorUpdate;
    private float mNotificationsAlpha = -1.0f;
    private ScrimView mNotificationsScrim;
    private int mNotificationsTint;
    private float mPanelExpansionFraction = 1.0f;
    private float mPanelScrimMinFraction;
    private Runnable mPendingFrameCallback;
    private boolean mQsBottomVisible;
    private float mQsExpansion;
    private float mRawPanelExpansionFraction;
    private boolean mScreenBlankingCallbackCalled;
    private final ScreenOffAnimationController mScreenOffAnimationController;
    private boolean mScreenOn;
    private ScrimView mScrimBehind;
    private float mScrimBehindAlphaKeyguard = 0.2f;
    private Runnable mScrimBehindChangeRunnable;
    private ScrimView mScrimInFront;
    private final TriConsumer<ScrimState, Float, ColorExtractor.GradientColors> mScrimStateListener;
    private Consumer<Integer> mScrimVisibleListener;
    private int mScrimsVisibility;
    /* access modifiers changed from: private */
    public ScrimState mState = ScrimState.UNINITIALIZED;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private final AlarmTimeout mTimeTicker;
    private boolean mTracking;
    private float mTransitionToFullShadeProgress;
    private float mTransitionToLockScreenFullShadeNotificationsProgress;
    private boolean mTransitioningToFullShade;
    private boolean mUnOcclusionAnimationRunning;
    private boolean mUpdatePending;
    private final WakeLock mWakeLock;
    private boolean mWakeLockHeld;
    private boolean mWallpaperSupportsAmbientMode;
    private boolean mWallpaperVisibilityTimedOut;

    public interface Callback {
        void onCancelled() {
        }

        void onDisplayBlanked() {
        }

        void onFinished() {
        }

        void onStart() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrimVisibility {
    }

    public void setCurrentUser(int i) {
    }

    public void setUnocclusionAnimationRunning(boolean z) {
        this.mUnOcclusionAnimationRunning = z;
    }

    @Inject
    public ScrimController(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, final KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, ConfigurationController configurationController, @Main Executor executor, ScreenOffAnimationController screenOffAnimationController, PanelExpansionStateManager panelExpansionStateManager, KeyguardUnlockAnimationController keyguardUnlockAnimationController, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        Objects.requireNonNull(lightBarController);
        LightBarController lightBarController2 = lightBarController;
        this.mScrimStateListener = new ScrimController$$ExternalSyntheticLambda0(lightBarController);
        this.mDefaultScrimAlpha = 1.0f;
        this.mKeyguardStateController = keyguardStateController;
        this.mDarkenWhileDragging = !keyguardStateController.canDismissLockScreen();
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardVisibilityCallback = new KeyguardVisibilityCallback();
        this.mHandler = handler;
        this.mMainExecutor = executor;
        this.mScreenOffAnimationController = screenOffAnimationController;
        AlarmManager alarmManager2 = alarmManager;
        this.mTimeTicker = new AlarmTimeout(alarmManager, new ScrimController$$ExternalSyntheticLambda1(this), "hide_aod_wallpaper", handler);
        this.mWakeLock = builder.setHandler(handler).setTag("Scrims").build();
        this.mDozeParameters = dozeParameters;
        this.mDockManager = dockManager;
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        keyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardFadingAwayChanged() {
                ScrimController.this.setKeyguardFadingAway(keyguardStateController.isKeyguardFadingAway(), keyguardStateController.getKeyguardFadingAwayDuration());
            }
        });
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onThemeChanged() {
                ScrimController.this.onThemeChanged();
            }

            public void onUiModeChanged() {
                ScrimController.this.onThemeChanged();
            }
        });
        panelExpansionStateManager.addExpansionListener(new ScrimController$$ExternalSyntheticLambda2(this));
        this.mColors = new ColorExtractor.GradientColors();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-phone-ScrimController */
    public /* synthetic */ void mo45047x8c817aac(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        setRawPanelExpansionFraction(panelExpansionChangeEvent.getFraction());
    }

    public void attachViews(ScrimView scrimView, ScrimView scrimView2, ScrimView scrimView3) {
        this.mNotificationsScrim = scrimView2;
        this.mScrimBehind = scrimView;
        this.mScrimInFront = scrimView3;
        updateThemeColors();
        scrimView.enableBottomEdgeConcave(this.mClipsQsScrim);
        this.mNotificationsScrim.enableRoundedCorners(true);
        Runnable runnable = this.mScrimBehindChangeRunnable;
        if (runnable != null) {
            this.mScrimBehind.setChangeRunnable(runnable, this.mMainExecutor);
            this.mScrimBehindChangeRunnable = null;
        }
        ScrimState[] values = ScrimState.values();
        for (int i = 0; i < values.length; i++) {
            values[i].init(this.mScrimInFront, this.mScrimBehind, this.mDozeParameters, this.mDockManager);
            values[i].setScrimBehindAlphaKeyguard(this.mScrimBehindAlphaKeyguard);
            values[i].setDefaultScrimAlpha(this.mDefaultScrimAlpha);
        }
        this.mScrimBehind.setDefaultFocusHighlightEnabled(false);
        this.mNotificationsScrim.setDefaultFocusHighlightEnabled(false);
        this.mScrimInFront.setDefaultFocusHighlightEnabled(false);
        updateScrims();
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardVisibilityCallback);
    }

    public void setScrimCornerRadius(int i) {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null && this.mNotificationsScrim != null) {
            scrimView.setCornerRadius(i);
            this.mNotificationsScrim.setCornerRadius(i);
        }
    }

    /* access modifiers changed from: package-private */
    public void setScrimVisibleListener(Consumer<Integer> consumer) {
        this.mScrimVisibleListener = consumer;
    }

    public void transitionTo(ScrimState scrimState) {
        transitionTo(scrimState, (Callback) null);
    }

    public void transitionTo(ScrimState scrimState, Callback callback) {
        if (scrimState != this.mState) {
            if (DEBUG) {
                Log.d(TAG, "State changed to: " + scrimState);
            }
            if (scrimState != ScrimState.UNINITIALIZED) {
                ScrimState scrimState2 = this.mState;
                this.mState = scrimState;
                Trace.traceCounter(4096, "scrim_state", scrimState.ordinal());
                Callback callback2 = this.mCallback;
                if (callback2 != null) {
                    callback2.onCancelled();
                }
                this.mCallback = callback;
                scrimState.prepare(scrimState2);
                this.mScreenBlankingCallbackCalled = false;
                this.mAnimationDelay = 0;
                this.mBlankScreen = scrimState.getBlanksScreen();
                this.mAnimateChange = scrimState.getAnimateChange();
                this.mAnimationDuration = scrimState.getAnimationDuration();
                applyState();
                boolean z = true;
                this.mScrimInFront.setFocusable(!scrimState.isLowPowerState());
                this.mScrimBehind.setFocusable(!scrimState.isLowPowerState());
                this.mNotificationsScrim.setFocusable(!scrimState.isLowPowerState());
                this.mScrimInFront.setBlendWithMainColor(scrimState.shouldBlendWithMainColor());
                Runnable runnable = this.mPendingFrameCallback;
                if (runnable != null) {
                    this.mScrimBehind.removeCallbacks(runnable);
                    this.mPendingFrameCallback = null;
                }
                if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
                    this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
                    this.mBlankingTransitionRunnable = null;
                }
                if (scrimState == ScrimState.BRIGHTNESS_MIRROR) {
                    z = false;
                }
                this.mNeedsDrawableColorUpdate = z;
                if (this.mState.isLowPowerState()) {
                    holdWakeLock();
                }
                this.mWallpaperVisibilityTimedOut = false;
                if (shouldFadeAwayWallpaper()) {
                    DejankUtils.postAfterTraversal(new ScrimController$$ExternalSyntheticLambda3(this));
                } else {
                    AlarmTimeout alarmTimeout = this.mTimeTicker;
                    Objects.requireNonNull(alarmTimeout);
                    DejankUtils.postAfterTraversal(new ScrimController$$ExternalSyntheticLambda4(alarmTimeout));
                }
                if (this.mKeyguardUpdateMonitor.needsSlowUnlockTransition() && this.mState == ScrimState.UNLOCKED) {
                    this.mAnimationDelay = 100;
                    scheduleUpdate();
                } else if (((scrimState2 == ScrimState.AOD || scrimState2 == ScrimState.PULSING) && (!this.mDozeParameters.getAlwaysOn() || this.mState == ScrimState.UNLOCKED)) || (this.mState == ScrimState.AOD && !this.mDozeParameters.getDisplayNeedsBlanking())) {
                    onPreDraw();
                } else {
                    scheduleUpdate();
                }
                dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
                return;
            }
            throw new IllegalArgumentException("Cannot change to UNINITIALIZED.");
        } else if (callback != null && this.mCallback != callback) {
            callback.onFinished();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$transitionTo$1$com-android-systemui-statusbar-phone-ScrimController */
    public /* synthetic */ void mo45049x3926e8e1() {
        this.mTimeTicker.schedule(this.mDozeParameters.getWallpaperAodDuration(), 1);
    }

    private boolean shouldFadeAwayWallpaper() {
        if (this.mWallpaperSupportsAmbientMode && this.mState == ScrimState.AOD && (this.mDozeParameters.getAlwaysOn() || this.mDockManager.isDocked())) {
            return true;
        }
        return false;
    }

    public ScrimState getState() {
        return this.mState;
    }

    /* access modifiers changed from: protected */
    public void setAdditionalScrimBehindAlphaKeyguard(float f) {
        this.mAdditionalScrimBehindAlphaKeyguard = f;
    }

    /* access modifiers changed from: protected */
    public void applyCompositeAlphaOnScrimBehindKeyguard() {
        setScrimBehindValues(((float) ColorUtils.compositeAlpha((int) (this.mAdditionalScrimBehindAlphaKeyguard * 255.0f), 51)) / 255.0f);
    }

    private void setScrimBehindValues(float f) {
        this.mScrimBehindAlphaKeyguard = f;
        ScrimState[] values = ScrimState.values();
        for (ScrimState scrimBehindAlphaKeyguard : values) {
            scrimBehindAlphaKeyguard.setScrimBehindAlphaKeyguard(f);
        }
        scheduleUpdate();
    }

    public void onTrackingStarted() {
        this.mTracking = true;
        this.mDarkenWhileDragging = true ^ this.mKeyguardStateController.canDismissLockScreen();
        if (!this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation()) {
            this.mAnimatingPanelExpansionOnUnlock = false;
        }
    }

    public void onExpandingFinished() {
        this.mTracking = false;
        setUnocclusionAnimationRunning(false);
    }

    /* access modifiers changed from: protected */
    public void onHideWallpaperTimeout() {
        if (this.mState == ScrimState.AOD || this.mState == ScrimState.PULSING) {
            holdWakeLock();
            this.mWallpaperVisibilityTimedOut = true;
            this.mAnimateChange = true;
            this.mAnimationDuration = this.mDozeParameters.getWallpaperFadeOutDuration();
            scheduleUpdate();
        }
    }

    private void holdWakeLock() {
        if (!this.mWakeLockHeld) {
            WakeLock wakeLock = this.mWakeLock;
            if (wakeLock != null) {
                this.mWakeLockHeld = true;
                wakeLock.acquire(TAG);
                return;
            }
            Log.w(TAG, "Cannot hold wake lock, it has not been set yet");
        }
    }

    /* access modifiers changed from: package-private */
    public void setRawPanelExpansionFraction(float f) {
        if (!Float.isNaN(f)) {
            this.mRawPanelExpansionFraction = f;
            calculateAndUpdatePanelExpansion();
            return;
        }
        throw new IllegalArgumentException("rawPanelExpansionFraction should not be NaN");
    }

    public void setPanelScrimMinFraction(float f) {
        if (!Float.isNaN(f)) {
            this.mPanelScrimMinFraction = f;
            calculateAndUpdatePanelExpansion();
            return;
        }
        throw new IllegalArgumentException("minFraction should not be NaN");
    }

    private void calculateAndUpdatePanelExpansion() {
        calculateAndUpdatePanelExpansion(false);
    }

    public void calculateAndUpdatePanelExpansion(boolean z) {
        float f = this.mRawPanelExpansionFraction;
        float f2 = this.mPanelScrimMinFraction;
        if (f2 < 1.0f) {
            f = Math.max((f - f2) / (1.0f - f2), 0.0f);
        }
        if (this.mPanelExpansionFraction != f || z) {
            int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
            boolean z2 = true;
            if (i != 0 && this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation()) {
                this.mAnimatingPanelExpansionOnUnlock = true;
            } else if (i == 0) {
                this.mAnimatingPanelExpansionOnUnlock = false;
            } else if (!this.mKeyguardUnlockAnimationController.isPlayingCannedUnlockAnimation() && z && this.mAnimatingPanelExpansionOnUnlock) {
                NTLogUtil.m1680d(TAG, "onUnlockAnimationFinished and expand case, remove animating flag, mPanelExpansionFraction = " + this.mPanelExpansionFraction + ", panelExpansionFraction = " + f);
                this.mAnimatingPanelExpansionOnUnlock = false;
            }
            this.mPanelExpansionFraction = f;
            if (!(this.mState == ScrimState.UNLOCKED || this.mState == ScrimState.KEYGUARD || this.mState == ScrimState.DREAMING || this.mState == ScrimState.SHADE_LOCKED || this.mState == ScrimState.PULSING)) {
                z2 = false;
            }
            if (z2 && this.mExpansionAffectsAlpha && !this.mAnimatingPanelExpansionOnUnlock) {
                applyAndDispatchState();
            }
        }
    }

    public void setTransitionToFullShadeProgress(float f, float f2) {
        if (f != this.mTransitionToFullShadeProgress || f2 != this.mTransitionToLockScreenFullShadeNotificationsProgress) {
            this.mTransitionToFullShadeProgress = f;
            this.mTransitionToLockScreenFullShadeNotificationsProgress = f2;
            setTransitionToFullShade(f > 0.0f || f2 > 0.0f);
            applyAndDispatchState();
        }
    }

    private void setTransitionToFullShade(boolean z) {
        if (z != this.mTransitioningToFullShade) {
            this.mTransitioningToFullShade = z;
            if (z) {
                ScrimState.SHADE_LOCKED.prepare(this.mState);
            }
        }
    }

    public void setNotificationsBounds(float f, float f2, float f3, float f4) {
        if (this.mClipsQsScrim) {
            this.mNotificationsScrim.setDrawableBounds(f - 1.0f, f2, f3 + 1.0f, f4);
            this.mScrimBehind.setBottomEdgePosition((int) f2);
            return;
        }
        this.mNotificationsScrim.setDrawableBounds(f, f2, f3, f4);
    }

    public void setNotificationsOverScrollAmount(int i) {
        this.mNotificationsScrim.setTranslationY((float) i);
    }

    public void setQsPosition(float f, int i) {
        if (!Float.isNaN(f)) {
            float notificationScrimAlpha = ShadeInterpolation.getNotificationScrimAlpha(f);
            boolean z = true;
            boolean z2 = i > 0;
            if (this.mQsExpansion != notificationScrimAlpha || this.mQsBottomVisible != z2) {
                this.mQsExpansion = notificationScrimAlpha;
                this.mQsBottomVisible = z2;
                if (!(this.mState == ScrimState.SHADE_LOCKED || this.mState == ScrimState.KEYGUARD || this.mState == ScrimState.PULSING)) {
                    z = false;
                }
                if (z && this.mExpansionAffectsAlpha) {
                    applyAndDispatchState();
                }
            }
        }
    }

    public void setBouncerHiddenFraction(float f) {
        if (this.mBouncerHiddenFraction != f) {
            this.mBouncerHiddenFraction = f;
            if (this.mState == ScrimState.DREAMING) {
                applyAndDispatchState();
            }
        }
    }

    public void setClipsQsScrim(boolean z) {
        if (z != this.mClipsQsScrim) {
            this.mClipsQsScrim = z;
            for (ScrimState clipQsScrim : ScrimState.values()) {
                clipQsScrim.setClipQsScrim(this.mClipsQsScrim);
            }
            ScrimView scrimView = this.mScrimBehind;
            if (scrimView != null) {
                scrimView.enableBottomEdgeConcave(this.mClipsQsScrim);
            }
            if (this.mState != ScrimState.UNINITIALIZED) {
                ScrimState scrimState = this.mState;
                scrimState.prepare(scrimState);
                applyAndDispatchState();
            }
        }
    }

    public boolean getClipQsScrim() {
        return this.mClipsQsScrim;
    }

    private void setOrAdaptCurrentAnimation(View view) {
        if (view != null) {
            float currentScrimAlpha = getCurrentScrimAlpha(view);
            boolean z = view == this.mScrimBehind && this.mQsBottomVisible;
            if (!isAnimating(view) || z) {
                updateScrimColor(view, currentScrimAlpha, getCurrentScrimTint(view));
                return;
            }
            ValueAnimator valueAnimator = (ValueAnimator) view.getTag(C1893R.C1897id.scrim);
            view.setTag(C1893R.C1897id.scrim_alpha_start, Float.valueOf(((Float) view.getTag(C1893R.C1897id.scrim_alpha_start)).floatValue() + (currentScrimAlpha - ((Float) view.getTag(C1893R.C1897id.scrim_alpha_end)).floatValue())));
            view.setTag(C1893R.C1897id.scrim_alpha_end, Float.valueOf(currentScrimAlpha));
            valueAnimator.setCurrentPlayTime(valueAnimator.getCurrentPlayTime());
        }
    }

    private void applyState() {
        this.mInFrontTint = this.mState.getFrontTint();
        this.mBehindTint = this.mState.getBehindTint();
        this.mNotificationsTint = this.mState.getNotifTint();
        this.mInFrontAlpha = this.mState.getFrontAlpha();
        this.mBehindAlpha = this.mState.getBehindAlpha();
        this.mNotificationsAlpha = this.mState.getNotifAlpha();
        assertAlphasValid();
        if (this.mExpansionAffectsAlpha) {
            if (this.mState == ScrimState.UNLOCKED || this.mState == ScrimState.DREAMING) {
                if (!this.mScreenOffAnimationController.shouldExpandNotifications() && !this.mAnimatingPanelExpansionOnUnlock) {
                    float pow = (float) Math.pow((double) getInterpolatedFraction(), 0.800000011920929d);
                    if (this.mClipsQsScrim) {
                        this.mBehindAlpha = 1.0f;
                        this.mNotificationsAlpha = pow * this.mDefaultScrimAlpha;
                    } else {
                        this.mBehindAlpha = pow * this.mDefaultScrimAlpha;
                        this.mNotificationsAlpha = MathUtils.constrainedMap(0.0f, 1.0f, 0.3f, 0.75f, this.mPanelExpansionFraction);
                        if (LargeScreenUtils.shouldUseSplitNotificationShade(this.mNotificationsScrim.getResources())) {
                            this.mNotificationsAlpha = ShadeInterpolation.getNotificationScrimAlpha(this.mPanelExpansionFraction) * ShadeInterpolation.getContentAlpha(this.mPanelExpansionFraction);
                        }
                    }
                    this.mBehindTint = this.mState.getBehindTint();
                    this.mInFrontAlpha = 0.0f;
                }
                float f = this.mBouncerHiddenFraction;
                if (f != 1.0f) {
                    float aboutToShowBouncerProgress = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f);
                    this.mBehindAlpha = MathUtils.lerp(this.mDefaultScrimAlpha, this.mBehindAlpha, aboutToShowBouncerProgress);
                    this.mBehindTint = ColorUtils.blendARGB(ScrimState.BOUNCER.getBehindTint(), this.mBehindTint, aboutToShowBouncerProgress);
                }
            } else if (this.mState == ScrimState.AUTH_SCRIMMED_SHADE) {
                float pow2 = ((float) Math.pow((double) getInterpolatedFraction(), 0.800000011920929d)) * this.mDefaultScrimAlpha;
                this.mBehindAlpha = pow2;
                this.mNotificationsAlpha = pow2;
                if (this.mClipsQsScrim) {
                    this.mBehindAlpha = 1.0f;
                    this.mBehindTint = NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext());
                }
            } else if (this.mState == ScrimState.KEYGUARD || this.mState == ScrimState.SHADE_LOCKED || this.mState == ScrimState.PULSING) {
                Pair<Integer, Float> calculateBackStateForState = calculateBackStateForState(this.mState);
                int intValue = ((Integer) calculateBackStateForState.first).intValue();
                float floatValue = ((Float) calculateBackStateForState.second).floatValue();
                if (this.mTransitionToFullShadeProgress > 0.0f) {
                    Pair<Integer, Float> calculateBackStateForState2 = calculateBackStateForState(ScrimState.SHADE_LOCKED);
                    floatValue = MathUtils.lerp(floatValue, ((Float) calculateBackStateForState2.second).floatValue(), this.mTransitionToFullShadeProgress);
                    intValue = ColorUtils.blendARGB(intValue, ((Integer) calculateBackStateForState2.first).intValue(), this.mTransitionToFullShadeProgress);
                }
                this.mInFrontAlpha = this.mState.getFrontAlpha();
                if (this.mClipsQsScrim) {
                    this.mNotificationsAlpha = floatValue;
                    this.mNotificationsTint = intValue;
                    this.mBehindAlpha = 1.0f;
                    this.mBehindTint = NTColorUtil.getScrimBehindTintColor(this.mScrimBehind.getContext());
                } else {
                    this.mBehindAlpha = floatValue;
                    if (this.mState == ScrimState.SHADE_LOCKED) {
                        this.mNotificationsAlpha = getInterpolatedFraction();
                    } else {
                        this.mNotificationsAlpha = Math.max(1.0f - getInterpolatedFraction(), this.mQsExpansion);
                    }
                    if (this.mState == ScrimState.KEYGUARD && this.mTransitionToLockScreenFullShadeNotificationsProgress > 0.0f) {
                        this.mNotificationsAlpha = MathUtils.lerp(this.mNotificationsAlpha, getInterpolatedFraction(), this.mTransitionToLockScreenFullShadeNotificationsProgress);
                    }
                    this.mNotificationsTint = this.mState.getNotifTint();
                    this.mBehindTint = intValue;
                }
                boolean z = this.mState == ScrimState.KEYGUARD && this.mTransitionToFullShadeProgress == 0.0f && this.mQsExpansion == 0.0f && !this.mClipsQsScrim;
                if (this.mKeyguardOccluded || z) {
                    this.mNotificationsAlpha = 0.0f;
                }
                if (this.mUnOcclusionAnimationRunning && this.mState == ScrimState.KEYGUARD) {
                    this.mNotificationsAlpha = ScrimState.KEYGUARD.getNotifAlpha();
                    this.mNotificationsTint = ScrimState.KEYGUARD.getNotifTint();
                    this.mBehindAlpha = ScrimState.KEYGUARD.getBehindAlpha();
                    this.mBehindTint = ScrimState.KEYGUARD.getBehindTint();
                }
            }
            if (this.mState != ScrimState.UNLOCKED) {
                this.mAnimatingPanelExpansionOnUnlock = false;
            }
            assertAlphasValid();
        }
    }

    private void assertAlphasValid() {
        if (Float.isNaN(this.mBehindAlpha) || Float.isNaN(this.mInFrontAlpha) || Float.isNaN(this.mNotificationsAlpha)) {
            throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", front: " + this.mInFrontAlpha + ", back: " + this.mBehindAlpha + ", notif: " + this.mNotificationsAlpha);
        }
    }

    private Pair<Integer, Float> calculateBackStateForState(ScrimState scrimState) {
        float f;
        int i;
        int i2;
        float interpolatedFraction = getInterpolatedFraction();
        float notifAlpha = this.mClipsQsScrim ? scrimState.getNotifAlpha() : scrimState.getBehindAlpha();
        float f2 = 0.0f;
        if (this.mDarkenWhileDragging) {
            f = MathUtils.lerp(this.mDefaultScrimAlpha, notifAlpha, interpolatedFraction);
        } else {
            f = MathUtils.lerp(0.0f, notifAlpha, interpolatedFraction);
        }
        if (this.mClipsQsScrim) {
            i = ColorUtils.blendARGB(ScrimState.BOUNCER.getNotifTint(), scrimState.getNotifTint(), interpolatedFraction);
        } else {
            i = ColorUtils.blendARGB(ScrimState.BOUNCER.getBehindTint(), scrimState.getBehindTint(), interpolatedFraction);
        }
        float f3 = this.mQsExpansion;
        if (f3 > 0.0f) {
            f = MathUtils.lerp(f, this.mDefaultScrimAlpha, f3);
            float f4 = this.mQsExpansion;
            if (this.mStatusBarKeyguardViewManager.isBouncerInTransit()) {
                f4 = BouncerPanelExpansionCalculator.showBouncerProgress(this.mPanelExpansionFraction);
            }
            if (this.mClipsQsScrim) {
                i2 = ScrimState.SHADE_LOCKED.getNotifTint();
            } else {
                i2 = ScrimState.SHADE_LOCKED.getBehindTint();
            }
            i = ColorUtils.blendARGB(i, i2, f4);
        }
        if (!this.mKeyguardStateController.isKeyguardGoingAway()) {
            f2 = f;
        }
        return new Pair<>(Integer.valueOf(i), Float.valueOf(f2));
    }

    private void applyAndDispatchState() {
        applyState();
        if (!this.mUpdatePending) {
            setOrAdaptCurrentAnimation(this.mScrimBehind);
            setOrAdaptCurrentAnimation(this.mNotificationsScrim);
            setOrAdaptCurrentAnimation(this.mScrimInFront);
            dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
            if (this.mWallpaperVisibilityTimedOut) {
                this.mWallpaperVisibilityTimedOut = false;
                DejankUtils.postAfterTraversal(new ScrimController$$ExternalSyntheticLambda7(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$applyAndDispatchState$2$com-android-systemui-statusbar-phone-ScrimController */
    public /* synthetic */ void mo45044xcbfcf15c() {
        this.mTimeTicker.schedule(this.mDozeParameters.getWallpaperAodDuration(), 1);
    }

    public void setAodFrontScrimAlpha(float f) {
        if (this.mInFrontAlpha != f && shouldUpdateFrontScrimAlpha()) {
            this.mInFrontAlpha = f;
            updateScrims();
        }
        ScrimState.AOD.setAodFrontScrimAlpha(f);
        ScrimState.PULSING.setAodFrontScrimAlpha(f);
    }

    private boolean shouldUpdateFrontScrimAlpha() {
        if ((this.mState != ScrimState.AOD || (!this.mDozeParameters.getAlwaysOn() && !this.mDockManager.isDocked())) && this.mState != ScrimState.PULSING) {
            return false;
        }
        return true;
    }

    public void setWakeLockScreenSensorActive(boolean z) {
        for (ScrimState wakeLockScreenSensorActive : ScrimState.values()) {
            wakeLockScreenSensorActive.setWakeLockScreenSensorActive(z);
        }
        if (this.mState == ScrimState.PULSING) {
            float behindAlpha = this.mState.getBehindAlpha();
            if (this.mBehindAlpha != behindAlpha) {
                this.mBehindAlpha = behindAlpha;
                if (!Float.isNaN(behindAlpha)) {
                    updateScrims();
                    return;
                }
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", back: " + this.mBehindAlpha);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void scheduleUpdate() {
        ScrimView scrimView;
        if (!this.mUpdatePending && (scrimView = this.mScrimBehind) != null) {
            scrimView.invalidate();
            this.mScrimBehind.getViewTreeObserver().addOnPreDrawListener(this);
            this.mUpdatePending = true;
        }
    }

    /* access modifiers changed from: protected */
    public void updateScrims() {
        boolean z = true;
        if (this.mNeedsDrawableColorUpdate) {
            this.mNeedsDrawableColorUpdate = false;
            boolean z2 = this.mScrimInFront.getViewAlpha() != 0.0f && !this.mBlankScreen;
            boolean z3 = this.mScrimBehind.getViewAlpha() != 0.0f && !this.mBlankScreen;
            boolean z4 = this.mNotificationsScrim.getViewAlpha() != 0.0f && !this.mBlankScreen;
            this.mScrimInFront.setColors(this.mColors, z2);
            this.mScrimBehind.setColors(this.mColors, z3);
            this.mNotificationsScrim.setColors(this.mColors, z4);
            dispatchBackScrimState(this.mScrimBehind.getViewAlpha());
        }
        boolean z5 = (this.mState == ScrimState.AOD || this.mState == ScrimState.PULSING) && this.mWallpaperVisibilityTimedOut;
        if (!(this.mState == ScrimState.PULSING || this.mState == ScrimState.AOD) || !this.mKeyguardOccluded) {
            z = false;
        }
        if (z5 || z) {
            this.mBehindAlpha = 1.0f;
        }
        if (this.mKeyguardStateController.isKeyguardGoingAway()) {
            this.mNotificationsAlpha = 0.0f;
        }
        if (this.mKeyguardOccluded && (this.mState == ScrimState.KEYGUARD || this.mState == ScrimState.SHADE_LOCKED)) {
            this.mBehindAlpha = 0.0f;
            this.mNotificationsAlpha = 0.0f;
        }
        setScrimAlpha(this.mScrimInFront, this.mInFrontAlpha);
        setScrimAlpha(this.mScrimBehind, this.mBehindAlpha);
        setScrimAlpha(this.mNotificationsScrim, this.mNotificationsAlpha);
        onFinished(this.mState);
        dispatchScrimsVisible();
    }

    private void dispatchBackScrimState(float f) {
        if (this.mClipsQsScrim && this.mQsBottomVisible) {
            f = this.mNotificationsAlpha;
        }
        this.mScrimStateListener.accept(this.mState, Float.valueOf(f), this.mScrimInFront.getColors());
    }

    /* access modifiers changed from: private */
    public void dispatchScrimsVisible() {
        ScrimView scrimView = this.mClipsQsScrim ? this.mNotificationsScrim : this.mScrimBehind;
        int i = (this.mScrimInFront.getViewAlpha() == 1.0f || scrimView.getViewAlpha() == 1.0f) ? 2 : (this.mScrimInFront.getViewAlpha() == 0.0f && scrimView.getViewAlpha() == 0.0f) ? 0 : 1;
        if (this.mScrimsVisibility != i) {
            this.mScrimsVisibility = i;
            this.mScrimVisibleListener.accept(Integer.valueOf(i));
        }
    }

    private float getInterpolatedFraction() {
        if (this.mStatusBarKeyguardViewManager.isBouncerInTransit()) {
            return BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(this.mPanelExpansionFraction);
        }
        return ShadeInterpolation.getNotificationScrimAlpha(this.mPanelExpansionFraction);
    }

    private void setScrimAlpha(ScrimView scrimView, float f) {
        boolean z = false;
        if (f == 0.0f) {
            scrimView.setClickable(false);
        } else {
            if (this.mState != ScrimState.AOD) {
                z = true;
            }
            scrimView.setClickable(z);
        }
        updateScrim(scrimView, f);
    }

    private String getScrimName(ScrimView scrimView) {
        if (scrimView == this.mScrimInFront) {
            return "front_scrim";
        }
        if (scrimView == this.mScrimBehind) {
            return "behind_scrim";
        }
        return scrimView == this.mNotificationsScrim ? "notifications_scrim" : "unknown_scrim";
    }

    private void updateScrimColor(View view, float f, int i) {
        float max = Math.max(0.0f, Math.min(1.0f, f));
        if (view instanceof ScrimView) {
            ScrimView scrimView = (ScrimView) view;
            Trace.traceCounter(4096, getScrimName(scrimView) + "_alpha", (int) (255.0f * max));
            Trace.traceCounter(4096, getScrimName(scrimView) + "_tint", Color.alpha(i));
            scrimView.setTint(i);
            scrimView.setViewAlpha(max);
        } else {
            view.setAlpha(max);
        }
        dispatchScrimsVisible();
    }

    private int getDebugScrimTint(ScrimView scrimView) {
        if (scrimView == this.mScrimBehind) {
            return DEBUG_BEHIND_TINT;
        }
        if (scrimView == this.mScrimInFront) {
            return DEBUG_FRONT_TINT;
        }
        if (scrimView == this.mNotificationsScrim) {
            return -65536;
        }
        throw new RuntimeException("scrim can't be matched with known scrims");
    }

    private void startScrimAnimation(final View view, float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        Animator.AnimatorListener animatorListener = this.mAnimatorListener;
        if (animatorListener != null) {
            ofFloat.addListener(animatorListener);
        }
        ofFloat.addUpdateListener(new ScrimController$$ExternalSyntheticLambda5(this, view, view instanceof ScrimView ? ((ScrimView) view).getTint() : 0));
        ofFloat.setInterpolator(this.mInterpolator);
        ofFloat.setStartDelay(this.mAnimationDelay);
        ofFloat.setDuration(this.mAnimationDuration);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            private final Callback mLastCallback;
            private final ScrimState mLastState;

            {
                this.mLastState = ScrimController.this.mState;
                this.mLastCallback = ScrimController.this.mCallback;
            }

            public void onAnimationEnd(Animator animator) {
                view.setTag(C1893R.C1897id.scrim, (Object) null);
                ScrimController.this.onFinished(this.mLastCallback, this.mLastState);
                ScrimController.this.dispatchScrimsVisible();
            }
        });
        view.setTag(C1893R.C1897id.scrim_alpha_start, Float.valueOf(f));
        view.setTag(C1893R.C1897id.scrim_alpha_end, Float.valueOf(getCurrentScrimAlpha(view)));
        view.setTag(C1893R.C1897id.scrim, ofFloat);
        ofFloat.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startScrimAnimation$3$com-android-systemui-statusbar-phone-ScrimController */
    public /* synthetic */ void mo45048x6161ffc9(View view, int i, ValueAnimator valueAnimator) {
        float floatValue = ((Float) view.getTag(C1893R.C1897id.scrim_alpha_start)).floatValue();
        float floatValue2 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateScrimColor(view, MathUtils.constrain(MathUtils.lerp(floatValue, getCurrentScrimAlpha(view), floatValue2), 0.0f, 1.0f), ColorUtils.blendARGB(i, getCurrentScrimTint(view), floatValue2));
        dispatchScrimsVisible();
    }

    private float getCurrentScrimAlpha(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontAlpha;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindAlpha;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsAlpha;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    private int getCurrentScrimTint(View view) {
        if (view == this.mScrimInFront) {
            return this.mInFrontTint;
        }
        if (view == this.mScrimBehind) {
            return this.mBehindTint;
        }
        if (view == this.mNotificationsScrim) {
            return this.mNotificationsTint;
        }
        throw new IllegalArgumentException("Unknown scrim view");
    }

    public boolean onPreDraw() {
        this.mScrimBehind.getViewTreeObserver().removeOnPreDrawListener(this);
        this.mUpdatePending = false;
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onStart();
        }
        updateScrims();
        return true;
    }

    private void onFinished(ScrimState scrimState) {
        onFinished(this.mCallback, scrimState);
    }

    /* access modifiers changed from: private */
    public void onFinished(Callback callback, ScrimState scrimState) {
        if (this.mPendingFrameCallback == null) {
            if (!isAnimating(this.mScrimBehind) && !isAnimating(this.mNotificationsScrim) && !isAnimating(this.mScrimInFront)) {
                if (this.mWakeLockHeld) {
                    this.mWakeLock.release(TAG);
                    this.mWakeLockHeld = false;
                }
                if (callback != null) {
                    callback.onFinished();
                    if (callback == this.mCallback) {
                        this.mCallback = null;
                    }
                }
                if (scrimState == ScrimState.UNLOCKED) {
                    this.mInFrontTint = 0;
                    this.mBehindTint = this.mState.getBehindTint();
                    this.mNotificationsTint = this.mState.getNotifTint();
                    updateScrimColor(this.mScrimInFront, this.mInFrontAlpha, this.mInFrontTint);
                    updateScrimColor(this.mScrimBehind, this.mBehindAlpha, this.mBehindTint);
                    updateScrimColor(this.mNotificationsScrim, this.mNotificationsAlpha, this.mNotificationsTint);
                }
            } else if (callback != null && callback != this.mCallback) {
                callback.onFinished();
            }
        }
    }

    private boolean isAnimating(View view) {
        return (view == null || view.getTag(C1893R.C1897id.scrim) == null) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }

    private void updateScrim(ScrimView scrimView, float f) {
        Callback callback;
        float viewAlpha = scrimView.getViewAlpha();
        ValueAnimator valueAnimator = (ValueAnimator) ViewState.getChildTag(scrimView, C1893R.C1897id.scrim);
        if (valueAnimator != null) {
            cancelAnimator(valueAnimator);
        }
        if (this.mPendingFrameCallback == null) {
            if (this.mBlankScreen) {
                blankDisplay();
                return;
            }
            boolean z = true;
            if (!this.mScreenBlankingCallbackCalled && (callback = this.mCallback) != null) {
                callback.onDisplayBlanked();
                this.mScreenBlankingCallbackCalled = true;
            }
            if (scrimView == this.mScrimBehind) {
                dispatchBackScrimState(f);
            }
            boolean z2 = f != viewAlpha;
            if (scrimView.getTint() == getCurrentScrimTint(scrimView)) {
                z = false;
            }
            if (!z2 && !z) {
                return;
            }
            if (this.mAnimateChange) {
                startScrimAnimation(scrimView, viewAlpha);
            } else {
                updateScrimColor(scrimView, f, getCurrentScrimTint(scrimView));
            }
        }
    }

    private void cancelAnimator(ValueAnimator valueAnimator) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    private void blankDisplay() {
        updateScrimColor(this.mScrimInFront, 1.0f, ViewCompat.MEASURED_STATE_MASK);
        ScrimController$$ExternalSyntheticLambda6 scrimController$$ExternalSyntheticLambda6 = new ScrimController$$ExternalSyntheticLambda6(this);
        this.mPendingFrameCallback = scrimController$$ExternalSyntheticLambda6;
        doOnTheNextFrame(scrimController$$ExternalSyntheticLambda6);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$blankDisplay$5$com-android-systemui-statusbar-phone-ScrimController */
    public /* synthetic */ void mo45046xcbf2177f() {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDisplayBlanked();
            this.mScreenBlankingCallbackCalled = true;
        }
        this.mBlankingTransitionRunnable = new ScrimController$$ExternalSyntheticLambda8(this);
        int i = this.mScreenOn ? 32 : 500;
        if (DEBUG) {
            Log.d(TAG, "Fading out scrims with delay: " + i);
        }
        this.mHandler.postDelayed(this.mBlankingTransitionRunnable, (long) i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$blankDisplay$4$com-android-systemui-statusbar-phone-ScrimController */
    public /* synthetic */ void mo45045xb1d698e0() {
        this.mBlankingTransitionRunnable = null;
        this.mPendingFrameCallback = null;
        this.mBlankScreen = false;
        updateScrims();
    }

    /* access modifiers changed from: protected */
    public void doOnTheNextFrame(Runnable runnable) {
        this.mScrimBehind.postOnAnimationDelayed(runnable, 32);
    }

    public void setScrimBehindChangeRunnable(Runnable runnable) {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView == null) {
            this.mScrimBehindChangeRunnable = runnable;
        } else {
            scrimView.setChangeRunnable(runnable, this.mMainExecutor);
        }
    }

    private void updateThemeColors() {
        ScrimView scrimView = this.mScrimBehind;
        if (scrimView != null) {
            int defaultColor = Utils.getColorAttr(scrimView.getContext(), 16844002).getDefaultColor();
            int defaultColor2 = Utils.getColorAccent(this.mScrimBehind.getContext()).getDefaultColor();
            this.mColors.setMainColor(defaultColor);
            this.mColors.setSecondaryColor(defaultColor2);
            ColorExtractor.GradientColors gradientColors = this.mColors;
            gradientColors.setSupportsDarkText(ColorUtils.calculateContrast(gradientColors.getMainColor(), -1) > 4.5d);
            this.mNeedsDrawableColorUpdate = true;
        }
    }

    /* access modifiers changed from: private */
    public void onThemeChanged() {
        updateThemeColors();
        scheduleUpdate();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(" ScrimController: ");
        printWriter.print("  state: ");
        printWriter.println((Object) this.mState);
        printWriter.println("    mClipQsScrim = " + this.mState.mClipQsScrim);
        printWriter.print("  frontScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimInFront.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mInFrontAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimInFront.getTint()));
        printWriter.print("  behindScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mScrimBehind.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mBehindAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mScrimBehind.getTint()));
        printWriter.print("  notificationsScrim:");
        printWriter.print(" viewAlpha=");
        printWriter.print(this.mNotificationsScrim.getViewAlpha());
        printWriter.print(" alpha=");
        printWriter.print(this.mNotificationsAlpha);
        printWriter.print(" tint=0x");
        printWriter.println(Integer.toHexString(this.mNotificationsScrim.getTint()));
        printWriter.print("  mTracking=");
        printWriter.println(this.mTracking);
        printWriter.print("  mDefaultScrimAlpha=");
        printWriter.println(this.mDefaultScrimAlpha);
        printWriter.print("  mPanelExpansionFraction=");
        printWriter.println(this.mPanelExpansionFraction);
        printWriter.print("  mExpansionAffectsAlpha=");
        printWriter.println(this.mExpansionAffectsAlpha);
        printWriter.print("  mState.getMaxLightRevealScrimAlpha=");
        printWriter.println(this.mState.getMaxLightRevealScrimAlpha());
    }

    public void setWallpaperSupportsAmbientMode(boolean z) {
        this.mWallpaperSupportsAmbientMode = z;
        ScrimState[] values = ScrimState.values();
        for (ScrimState wallpaperSupportsAmbientMode : values) {
            wallpaperSupportsAmbientMode.setWallpaperSupportsAmbientMode(z);
        }
    }

    public void onScreenTurnedOn() {
        this.mScreenOn = true;
        if (this.mHandler.hasCallbacks(this.mBlankingTransitionRunnable)) {
            if (DEBUG) {
                Log.d(TAG, "Shorter blanking because screen turned on. All good.");
            }
            this.mHandler.removeCallbacks(this.mBlankingTransitionRunnable);
            this.mBlankingTransitionRunnable.run();
        }
    }

    public void onScreenTurnedOff() {
        this.mScreenOn = false;
    }

    public void setExpansionAffectsAlpha(boolean z) {
        this.mExpansionAffectsAlpha = z;
    }

    public void setKeyguardOccluded(boolean z) {
        this.mKeyguardOccluded = z;
        updateScrims();
    }

    public void setHasBackdrop(boolean z) {
        for (ScrimState hasBackdrop : ScrimState.values()) {
            hasBackdrop.setHasBackdrop(z);
        }
        if (this.mState == ScrimState.AOD || this.mState == ScrimState.PULSING) {
            float behindAlpha = this.mState.getBehindAlpha();
            if (Float.isNaN(behindAlpha)) {
                throw new IllegalStateException("Scrim opacity is NaN for state: " + this.mState + ", back: " + this.mBehindAlpha);
            } else if (this.mBehindAlpha != behindAlpha) {
                this.mBehindAlpha = behindAlpha;
                updateScrims();
            }
        }
    }

    /* access modifiers changed from: private */
    public void setKeyguardFadingAway(boolean z, long j) {
        for (ScrimState keyguardFadingAway : ScrimState.values()) {
            keyguardFadingAway.setKeyguardFadingAway(z, j);
        }
    }

    public void setLaunchingAffordanceWithPreview(boolean z) {
        for (ScrimState launchingAffordanceWithPreview : ScrimState.values()) {
            launchingAffordanceWithPreview.setLaunchingAffordanceWithPreview(z);
        }
    }

    private class KeyguardVisibilityCallback extends KeyguardUpdateMonitorCallback {
        private KeyguardVisibilityCallback() {
        }

        public void onKeyguardVisibilityChanged(boolean z) {
            boolean unused = ScrimController.this.mNeedsDrawableColorUpdate = true;
            ScrimController.this.scheduleUpdate();
        }
    }
}
