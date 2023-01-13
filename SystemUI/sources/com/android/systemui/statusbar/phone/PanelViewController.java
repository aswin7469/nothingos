package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.BoostFramework;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import androidx.exifinterface.media.ExifInterface;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.systemui.C1894R;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.PanelView;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.time.SystemClock;
import com.nothing.systemui.statusbar.phone.NotificationPanelViewControllerEx;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;

public abstract class PanelViewController {
    public static final boolean DEBUG = false;
    private static final float FACTOR_OF_HIGH_VELOCITY_FOR_MAX_OVERSHOOT = 0.5f;
    public static final float FLING_CLOSING_MAX_LENGTH_SECONDS = 0.6f;
    public static final float FLING_CLOSING_SPEED_UP_FACTOR = 0.6f;
    public static final float FLING_MAX_LENGTH_SECONDS = 0.6f;
    public static final float FLING_SPEED_UP_FACTOR = 0.6f;
    private static final int NO_FIXED_DURATION = -1;
    private static final long SHADE_OPEN_SPRING_BACK_DURATION = 400;
    private static final long SHADE_OPEN_SPRING_OUT_DURATION = 350;
    public static final String TAG = PanelView.TAG;
    protected final AmbientState mAmbientState;
    /* access modifiers changed from: private */
    public boolean mAnimateAfterExpanding;
    /* access modifiers changed from: private */
    public boolean mAnimatingOnDown;
    private Interpolator mBounceInterpolator;
    protected CentralSurfaces mCentralSurfaces;
    /* access modifiers changed from: private */
    public boolean mClosing;
    /* access modifiers changed from: private */
    public boolean mCollapsedAndHeadsUpOnDown;
    protected long mDownTime;
    private final DozeLog mDozeLog;
    protected NotificationPanelViewControllerEx mEx;
    private boolean mExpandLatencyTracking;
    private float mExpandedFraction = 0.0f;
    protected float mExpandedHeight = 0.0f;
    protected boolean mExpanding;
    private float mExpansionDragDownAmountPx = 0.0f;
    private final FalsingManager mFalsingManager;
    private int mFixedDuration = -1;
    private FlingAnimationUtils mFlingAnimationUtils;
    private FlingAnimationUtils mFlingAnimationUtilsClosing;
    private FlingAnimationUtils mFlingAnimationUtilsDismissing;
    private final Runnable mFlingCollapseRunnable = new Runnable() {
        public void run() {
            PanelViewController panelViewController = PanelViewController.this;
            panelViewController.fling(0.0f, false, panelViewController.mNextCollapseSpeedUpFactor, false);
        }
    };
    /* access modifiers changed from: private */
    public boolean mGestureWaitForTouchSlop;
    /* access modifiers changed from: private */
    public boolean mHandlingPointerUp;
    /* access modifiers changed from: private */
    public boolean mHasLayoutedSinceDown;
    protected HeadsUpManagerPhone mHeadsUpManager;
    /* access modifiers changed from: private */
    public ValueAnimator mHeightAnimator;
    protected boolean mHintAnimationRunning;
    private float mHintDistance;
    /* access modifiers changed from: private */
    public boolean mIgnoreXTouchSlop;
    protected boolean mInSplitShade;
    /* access modifiers changed from: private */
    public float mInitialOffsetOnTouch;
    /* access modifiers changed from: private */
    public float mInitialTouchX;
    /* access modifiers changed from: private */
    public float mInitialTouchY;
    /* access modifiers changed from: private */
    public boolean mInstantExpanding;
    private final InteractionJankMonitor mInteractionJankMonitor;
    private boolean mIsFlinging;
    protected boolean mIsLaunchAnimationRunning;
    /* access modifiers changed from: private */
    public boolean mIsSpringBackAnimation;
    protected KeyguardBottomAreaView mKeyguardBottomArea;
    protected final KeyguardStateController mKeyguardStateController;
    private KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    private float mLastGesturedOverExpansion = -1.0f;
    private final LatencyTracker mLatencyTracker;
    protected final LockscreenGestureLogger mLockscreenGestureLogger;
    /* access modifiers changed from: private */
    public float mMinExpandHeight;
    /* access modifiers changed from: private */
    public boolean mMotionAborted;
    /* access modifiers changed from: private */
    public float mNextCollapseSpeedUpFactor = 1.0f;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    /* access modifiers changed from: private */
    public boolean mNotificationsDragEnabled;
    private boolean mOverExpandedBeforeFling;
    protected float mOverExpansion;
    /* access modifiers changed from: private */
    public boolean mPanelClosedOnDown;
    private final PanelExpansionStateManager mPanelExpansionStateManager;
    private float mPanelFlingOvershootAmount;
    private boolean mPanelUpdateWhenAnimatorEnds;
    /* access modifiers changed from: private */
    public BoostFramework mPerf = null;
    protected final Resources mResources;
    private float mSlopMultiplier;
    private final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    protected final SysuiStatusBarStateController mStatusBarStateController;
    protected final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    protected final SystemClock mSystemClock;
    /* access modifiers changed from: private */
    public boolean mTouchAboveFalsingThreshold;
    /* access modifiers changed from: private */
    public boolean mTouchDisabled;
    private final TouchHandler mTouchHandler;
    private int mTouchSlop;
    /* access modifiers changed from: private */
    public boolean mTouchSlopExceeded;
    protected boolean mTouchSlopExceededBeforeDown;
    /* access modifiers changed from: private */
    public boolean mTouchStartedInEmptyArea;
    protected boolean mTracking;
    /* access modifiers changed from: private */
    public int mTrackingPointer;
    private int mUnlockFalsingThreshold;
    /* access modifiers changed from: private */
    public boolean mUpdateFlingOnLayout;
    /* access modifiers changed from: private */
    public float mUpdateFlingVelocity;
    /* access modifiers changed from: private */
    public boolean mUpwardsWhenThresholdReached;
    /* access modifiers changed from: private */
    public final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private boolean mVibrateOnOpening;
    private final VibratorHelper mVibratorHelper;
    /* access modifiers changed from: private */
    public final PanelView mView;
    /* access modifiers changed from: private */
    public String mViewName;

    /* access modifiers changed from: protected */
    public boolean canCollapsePanelOnTouch() {
        return true;
    }

    /* access modifiers changed from: protected */
    public abstract TouchHandler createTouchHandler();

    /* access modifiers changed from: protected */
    public abstract int getMaxPanelHeight();

    /* access modifiers changed from: protected */
    public abstract float getOpeningHeight();

    /* access modifiers changed from: protected */
    public abstract boolean isDozing();

    /* access modifiers changed from: protected */
    public abstract boolean isInContentBounds(float f, float f2);

    /* access modifiers changed from: protected */
    public abstract boolean isPanelVisibleBecauseOfHeadsUp();

    /* access modifiers changed from: protected */
    public abstract boolean isTrackingBlocked();

    /* access modifiers changed from: protected */
    public abstract void onClosingFinished();

    /* access modifiers changed from: protected */
    public abstract void onExpandingFinished();

    /* access modifiers changed from: protected */
    public void onExpandingStarted() {
    }

    /* access modifiers changed from: protected */
    public abstract void onHeightUpdated(float f);

    /* access modifiers changed from: protected */
    public abstract boolean onMiddleClicked();

    public abstract void resetViews(boolean z);

    /* access modifiers changed from: protected */
    public abstract boolean shouldGestureIgnoreXTouchSlop(float f, float f2);

    /* access modifiers changed from: protected */
    public abstract boolean shouldGestureWaitForTouchSlop();

    /* access modifiers changed from: package-private */
    public abstract boolean shouldPanelBeVisible();

    /* access modifiers changed from: protected */
    public abstract boolean shouldUseDismissingAnimation();

    static {
        Class<PanelView> cls = PanelView.class;
    }

    private void logf(String str, Object... objArr) {
        Log.v(TAG, (this.mViewName != null ? this.mViewName + ": " : "") + String.format(str, objArr));
    }

    /* access modifiers changed from: protected */
    public void notifyExpandingStarted() {
        if (!this.mExpanding) {
            this.mExpanding = true;
            onExpandingStarted();
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyExpandingFinished() {
        endClosing();
        if (this.mExpanding) {
            this.mExpanding = false;
            onExpandingFinished();
        }
    }

    /* access modifiers changed from: protected */
    public AmbientState getAmbientState() {
        return this.mAmbientState;
    }

    public PanelViewController(PanelView panelView, FalsingManager falsingManager, DozeLog dozeLog, KeyguardStateController keyguardStateController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowController notificationShadeWindowController, VibratorHelper vibratorHelper, StatusBarKeyguardViewManager statusBarKeyguardViewManager, LatencyTracker latencyTracker, FlingAnimationUtils.Builder builder, StatusBarTouchableRegionManager statusBarTouchableRegionManager, LockscreenGestureLogger lockscreenGestureLogger, PanelExpansionStateManager panelExpansionStateManager, AmbientState ambientState, InteractionJankMonitor interactionJankMonitor, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SystemClock systemClock) {
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
        keyguardStateController.addCallback(new KeyguardStateController.Callback() {
            public void onKeyguardFadingAwayChanged() {
                PanelViewController.this.requestPanelHeightUpdate();
            }
        });
        this.mAmbientState = ambientState;
        this.mView = panelView;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        TouchHandler createTouchHandler = createTouchHandler();
        this.mTouchHandler = createTouchHandler;
        panelView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewDetachedFromWindow(View view) {
            }

            public void onViewAttachedToWindow(View view) {
                PanelViewController panelViewController = PanelViewController.this;
                String unused = panelViewController.mViewName = panelViewController.mResources.getResourceName(PanelViewController.this.mView.getId());
            }
        });
        panelView.addOnLayoutChangeListener(createLayoutChangeListener());
        panelView.setOnTouchListener(createTouchHandler);
        panelView.setOnConfigurationChangedListener(createOnConfigurationChangedListener());
        Resources resources = panelView.getResources();
        this.mResources = resources;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mFlingAnimationUtils = builder.reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
        this.mFlingAnimationUtilsClosing = builder.reset().setMaxLengthSeconds(0.6f).setSpeedUpFactor(0.6f).build();
        this.mFlingAnimationUtilsDismissing = builder.reset().setMaxLengthSeconds(0.5f).setSpeedUpFactor(0.6f).setX2(0.6f).setY2(0.84f).build();
        this.mLatencyTracker = latencyTracker;
        this.mBounceInterpolator = new BounceInterpolator();
        this.mFalsingManager = falsingManager;
        this.mDozeLog = dozeLog;
        this.mNotificationsDragEnabled = resources.getBoolean(C1894R.bool.config_enableNotificationShadeDrag);
        this.mVibratorHelper = vibratorHelper;
        this.mVibrateOnOpening = resources.getBoolean(C1894R.bool.config_vibrateOnIconAnimation);
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
        this.mInteractionJankMonitor = interactionJankMonitor;
        this.mSystemClock = systemClock;
        this.mPerf = new BoostFramework();
    }

    /* access modifiers changed from: protected */
    public void loadDimens() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(this.mView.getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mHintDistance = this.mResources.getDimension(C1894R.dimen.hint_move_distance);
        this.mPanelFlingOvershootAmount = this.mResources.getDimension(C1894R.dimen.panel_overshoot_amount);
        this.mUnlockFalsingThreshold = this.mResources.getDimensionPixelSize(C1894R.dimen.unlock_falsing_threshold);
        this.mInSplitShade = this.mResources.getBoolean(C1894R.bool.config_use_split_notification_shade);
    }

    /* access modifiers changed from: protected */
    public float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return ((float) this.mTouchSlop) * this.mSlopMultiplier;
        }
        return (float) this.mTouchSlop;
    }

    /* access modifiers changed from: private */
    public void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.mVelocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    public void setTouchAndAnimationDisabled(boolean z) {
        this.mTouchDisabled = z;
        if (z) {
            cancelHeightAnimator();
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            notifyExpandingFinished();
        }
    }

    public void startExpandLatencyTracking() {
        if (this.mLatencyTracker.isEnabled()) {
            this.mLatencyTracker.onActionStart(0);
            this.mExpandLatencyTracking = true;
        }
    }

    /* access modifiers changed from: private */
    public void startOpening(MotionEvent motionEvent) {
        updatePanelExpansionAndVisibility();
        maybeVibrateOnOpening();
        float displayWidth = this.mCentralSurfaces.getDisplayWidth();
        float displayHeight = this.mCentralSurfaces.getDisplayHeight();
        this.mLockscreenGestureLogger.writeAtFractionalPosition(1328, (int) ((motionEvent.getX() / displayWidth) * 100.0f), (int) ((motionEvent.getY() / displayHeight) * 100.0f), this.mCentralSurfaces.getRotation());
        this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_UNLOCKED_NOTIFICATION_PANEL_EXPAND);
    }

    /* access modifiers changed from: protected */
    public void maybeVibrateOnOpening() {
        if (this.mVibrateOnOpening) {
            this.mVibratorHelper.vibrate(2);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDirectionUpwards(float f, float f2) {
        float f3 = f - this.mInitialTouchX;
        float f4 = f2 - this.mInitialTouchY;
        if (f4 < 0.0f && Math.abs(f4) >= Math.abs(f3)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void startExpandMotion(float f, float f2, boolean z, float f3) {
        if (!this.mHandlingPointerUp && !this.mStatusBarStateController.isDozing()) {
            beginJankMonitoring(0);
        }
        this.mInitialOffsetOnTouch = f3;
        this.mInitialTouchY = f2;
        this.mInitialTouchX = f;
        if (z) {
            this.mTouchSlopExceeded = true;
            onTrackingStarted();
            setExpandedHeight(this.mInitialOffsetOnTouch);
        }
    }

    /* access modifiers changed from: private */
    public void endMotionEvent(MotionEvent motionEvent, float f, float f2, boolean z) {
        boolean z2;
        int i;
        this.mTrackingPointer = -1;
        boolean z3 = false;
        this.mAmbientState.setSwipingUp(false);
        if ((this.mTracking && this.mTouchSlopExceeded) || Math.abs(f - this.mInitialTouchX) > ((float) this.mTouchSlop) || Math.abs(f2 - this.mInitialTouchY) > ((float) this.mTouchSlop) || motionEvent.getActionMasked() == 3 || z) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float yVelocity = this.mVelocityTracker.getYVelocity();
            float hypot = (float) Math.hypot((double) this.mVelocityTracker.getXVelocity(), (double) this.mVelocityTracker.getYVelocity());
            boolean z4 = this.mStatusBarStateController.getState() == 1;
            if (motionEvent.getActionMasked() == 3 || z) {
                if (!this.mKeyguardStateController.isKeyguardFadingAway()) {
                    if (z4) {
                        z2 = true;
                    } else if (!this.mKeyguardStateController.isKeyguardFadingAway()) {
                        z2 = !this.mPanelClosedOnDown;
                    }
                }
                z2 = false;
            } else {
                z2 = flingExpands(yVelocity, hypot, f, f2);
            }
            this.mDozeLog.traceFling(z2, this.mTouchAboveFalsingThreshold, this.mCentralSurfaces.isFalsingThresholdNeeded(), this.mCentralSurfaces.isWakeUpComingFromTouch());
            if (!z2 && z4) {
                float displayDensity = this.mCentralSurfaces.getDisplayDensity();
                this.mLockscreenGestureLogger.write(186, (int) Math.abs((f2 - this.mInitialTouchY) / displayDensity), (int) Math.abs(yVelocity / displayDensity));
                this.mLockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_UNLOCK);
            }
            if (yVelocity == 0.0f) {
                i = 7;
            } else if (f2 - this.mInitialTouchY > 0.0f) {
                i = 0;
            } else {
                i = this.mKeyguardStateController.canDismissLockScreen() ? 4 : 8;
            }
            fling(yVelocity, z2, isFalseTouch(f, f2, i));
            onTrackingStopped(z2);
            if (z2 && this.mPanelClosedOnDown && !this.mHasLayoutedSinceDown) {
                z3 = true;
            }
            this.mUpdateFlingOnLayout = z3;
            if (z3) {
                this.mUpdateFlingVelocity = yVelocity;
            }
        } else if (!this.mCentralSurfaces.isBouncerShowing() && !this.mStatusBarKeyguardViewManager.isShowingAlternateAuthOrAnimating() && !this.mKeyguardStateController.isKeyguardGoingAway()) {
            onTrackingStopped(onEmptySpaceClick(this.mInitialTouchX));
        }
        this.mVelocityTracker.clear();
    }

    /* access modifiers changed from: protected */
    public float getCurrentExpandVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000);
        return this.mVelocityTracker.getYVelocity();
    }

    /* access modifiers changed from: private */
    public int getFalsingThreshold() {
        return (int) (((float) this.mUnlockFalsingThreshold) * (this.mCentralSurfaces.isWakeUpComingFromTouch() ? 1.5f : 1.0f));
    }

    /* access modifiers changed from: protected */
    public void onTrackingStopped(boolean z) {
        this.mTracking = false;
        this.mCentralSurfaces.onTrackingStopped(z);
        updatePanelExpansionAndVisibility();
    }

    /* access modifiers changed from: protected */
    public void onTrackingStarted() {
        endClosing();
        this.mTracking = true;
        this.mCentralSurfaces.onTrackingStarted();
        notifyExpandingStarted();
        updatePanelExpansionAndVisibility();
    }

    /* access modifiers changed from: protected */
    public void cancelHeightAnimator() {
        ValueAnimator valueAnimator = this.mHeightAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mPanelUpdateWhenAnimatorEnds = false;
            }
            this.mHeightAnimator.cancel();
            NTLogUtil.m1686d(TAG, "cancelHeightAnimator");
        }
        endClosing();
    }

    private void endClosing() {
        if (this.mClosing) {
            setIsClosing(false);
            onClosingFinished();
        }
    }

    /* access modifiers changed from: protected */
    public float getContentHeight() {
        return this.mExpandedHeight;
    }

    /* access modifiers changed from: protected */
    public boolean flingExpands(float f, float f2, float f3, float f4) {
        int i;
        if (this.mFalsingManager.isUnlockingDisabled()) {
            return true;
        }
        if (f4 - this.mInitialTouchY > 0.0f) {
            i = 0;
        } else {
            i = this.mKeyguardStateController.canDismissLockScreen() ? 4 : 8;
        }
        if (isFalseTouch(f3, f4, i)) {
            return true;
        }
        if (Math.abs(f2) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond()) {
            return shouldExpandWhenNotFlinging();
        }
        if (f > 0.0f) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean shouldExpandWhenNotFlinging() {
        return getExpandedFraction() > 0.5f;
    }

    private boolean isFalseTouch(float f, float f2, int i) {
        if (!this.mCentralSurfaces.isFalsingThresholdNeeded()) {
            return false;
        }
        if (this.mFalsingManager.isClassifierEnabled()) {
            return this.mFalsingManager.isFalseTouch(i);
        }
        if (!this.mTouchAboveFalsingThreshold) {
            return true;
        }
        if (this.mUpwardsWhenThresholdReached) {
            return false;
        }
        return !isDirectionUpwards(f, f2);
    }

    /* access modifiers changed from: protected */
    public void fling(float f, boolean z) {
        fling(f, z, 1.0f, false);
    }

    /* access modifiers changed from: protected */
    public void fling(float f, boolean z, boolean z2) {
        fling(f, z, 1.0f, z2);
    }

    /* access modifiers changed from: protected */
    public void fling(float f, boolean z, float f2, boolean z2) {
        float maxPanelHeight = z ? (float) getMaxPanelHeight() : 0.0f;
        if (!z) {
            setIsClosing(true);
        }
        flingToHeight(f, z, maxPanelHeight, f2, z2);
    }

    /* access modifiers changed from: protected */
    public void flingToHeight(float f, boolean z, float f2, float f3, boolean z2) {
        ValueAnimator valueAnimator;
        int i;
        String str;
        float f4 = f;
        boolean z3 = z;
        float f5 = f2;
        if (f5 == this.mExpandedHeight && this.mOverExpansion == 0.0f) {
            onFlingEnd(false);
            return;
        }
        boolean z4 = this.mIsFlinging;
        this.mIsFlinging = true;
        boolean z5 = z3 && !this.mInSplitShade && this.mStatusBarStateController.getState() != 1 && this.mOverExpansion == 0.0f && f4 >= 0.0f;
        final boolean z6 = z5 || (this.mOverExpansion != 0.0f && z3);
        float lerp = z5 ? MathUtils.lerp(0.2f, 1.0f, MathUtils.saturate(f4 / (this.mFlingAnimationUtils.getHighVelocityPxPerSecond() * 0.5f))) + (this.mOverExpansion / this.mPanelFlingOvershootAmount) : 0.0f;
        ValueAnimator createHeightAnimator = createHeightAnimator(f5, lerp);
        if (this.mEx.shouldIgnoreStartFlingAnimavor(createHeightAnimator, this.mHeightAnimator, f4, z3)) {
            this.mIsFlinging = z4;
            NTLogUtil.m1686d(TAG, "skip flingToHeight because exist a similar animator vel = " + f4 + ", expand = " + z3);
            return;
        }
        if (z3) {
            if (z2 && f4 < 0.0f) {
                f4 = 0.0f;
            }
            this.mFlingAnimationUtils.apply((Animator) createHeightAnimator, this.mExpandedHeight, f5 + (lerp * this.mPanelFlingOvershootAmount), f4, (float) this.mView.getHeight());
            if (f4 == 0.0f) {
                createHeightAnimator.setDuration(350);
            }
            i = -1;
            str = ", expand = ";
            valueAnimator = createHeightAnimator;
        } else {
            if (!shouldUseDismissingAnimation()) {
                i = -1;
                str = ", expand = ";
                valueAnimator = createHeightAnimator;
                this.mFlingAnimationUtilsClosing.apply((Animator) valueAnimator, this.mExpandedHeight, f2, f, (float) this.mView.getHeight());
            } else if (f4 == 0.0f) {
                createHeightAnimator.setInterpolator(Interpolators.PANEL_CLOSE_ACCELERATED);
                createHeightAnimator.setDuration((long) (((this.mExpandedHeight / ((float) this.mView.getHeight())) * 100.0f) + 200.0f));
                i = -1;
                str = ", expand = ";
                valueAnimator = createHeightAnimator;
            } else {
                i = -1;
                str = ", expand = ";
                valueAnimator = createHeightAnimator;
                this.mFlingAnimationUtilsDismissing.apply((Animator) createHeightAnimator, this.mExpandedHeight, f2, f, (float) this.mView.getHeight());
            }
            if (f4 == 0.0f) {
                valueAnimator.setDuration((long) (((float) valueAnimator.getDuration()) / f3));
            }
            int i2 = this.mFixedDuration;
            if (i2 != i) {
                valueAnimator.setDuration((long) i2);
            }
        }
        if (this.mPerf != null) {
            this.mPerf.perfHint(4224, this.mView.getContext().getPackageName(), i, 3);
        }
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            private boolean mCancelled;

            public void onAnimationStart(Animator animator) {
                if (!PanelViewController.this.mStatusBarStateController.isDozing()) {
                    PanelViewController.this.beginJankMonitoring(0);
                }
            }

            public void onAnimationCancel(Animator animator) {
                if (PanelViewController.this.mPerf != null) {
                    PanelViewController.this.mPerf.perfLockRelease();
                }
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (PanelViewController.this.mPerf != null) {
                    PanelViewController.this.mPerf.perfLockRelease();
                }
                if (!z6 || this.mCancelled) {
                    PanelViewController.this.onFlingEnd(this.mCancelled);
                } else {
                    PanelViewController.this.springBack();
                }
            }
        });
        NTLogUtil.m1686d(TAG, "PanelViewController start fling animator vel = " + f4 + str + z3);
        setAnimator(valueAnimator);
        valueAnimator.start();
    }

    /* access modifiers changed from: private */
    public void springBack() {
        float f = this.mOverExpansion;
        if (f == 0.0f) {
            onFlingEnd(false);
            return;
        }
        this.mIsSpringBackAnimation = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f, 0.0f});
        ofFloat.addUpdateListener(new PanelViewController$$ExternalSyntheticLambda0(this));
        ofFloat.setDuration(SHADE_OPEN_SPRING_BACK_DURATION);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            private boolean mCancelled;

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                boolean unused = PanelViewController.this.mIsSpringBackAnimation = false;
                PanelViewController.this.onFlingEnd(this.mCancelled);
            }
        });
        setAnimator(ofFloat);
        ofFloat.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$springBack$0$com-android-systemui-statusbar-phone-PanelViewController */
    public /* synthetic */ void mo44947x8909a8e1(ValueAnimator valueAnimator) {
        setOverExpansionInternal(((Float) valueAnimator.getAnimatedValue()).floatValue(), false);
    }

    /* access modifiers changed from: package-private */
    public void onFlingEnd(boolean z) {
        this.mIsFlinging = false;
        setOverExpansionInternal(0.0f, false);
        setAnimator((ValueAnimator) null);
        this.mKeyguardStateController.notifyPanelFlingEnd();
        if (!z) {
            endJankMonitoring(0);
            notifyExpandingFinished();
        } else {
            cancelJankMonitoring(0);
        }
        updatePanelExpansionAndVisibility();
    }

    public String getName() {
        return this.mViewName;
    }

    public void setExpandedHeight(float f) {
        setExpandedHeightInternal(f);
    }

    /* access modifiers changed from: protected */
    public void requestPanelHeightUpdate() {
        float maxPanelHeight = (float) getMaxPanelHeight();
        if (isFullyCollapsed() || maxPanelHeight == this.mExpandedHeight) {
            return;
        }
        if (this.mTracking && !isTrackingBlocked()) {
            return;
        }
        if (this.mHeightAnimator == null || this.mIsSpringBackAnimation) {
            setExpandedHeight(maxPanelHeight);
        } else {
            this.mPanelUpdateWhenAnimatorEnds = true;
        }
    }

    private float getStackHeightFraction(float f) {
        return Interpolators.ACCELERATE_DECELERATE.getInterpolation(f / ((float) getMaxPanelHeight()));
    }

    public void setExpandedHeightInternal(float f) {
        if (Float.isNaN(f)) {
            Log.wtf(TAG, "ExpandedHeight set to NaN");
        }
        this.mNotificationShadeWindowController.batchApplyWindowLayoutParams(new PanelViewController$$ExternalSyntheticLambda1(this, f));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setExpandedHeightInternal$2$com-android-systemui-statusbar-phone-PanelViewController */
    public /* synthetic */ void mo44946x7186338e(float f) {
        float f2 = 0.0f;
        if (this.mExpandLatencyTracking && f != 0.0f) {
            DejankUtils.postAfterTraversal(new PanelViewController$$ExternalSyntheticLambda5(this));
            this.mExpandLatencyTracking = false;
        }
        float maxPanelHeight = (float) getMaxPanelHeight();
        if (this.mHeightAnimator == null) {
            if (this.mTracking && !this.mInSplitShade) {
                setOverExpansionInternal(Math.max(0.0f, f - maxPanelHeight), true);
            }
            this.mExpandedHeight = Math.min(f, maxPanelHeight);
        } else {
            this.mExpandedHeight = f;
        }
        float f3 = this.mExpandedHeight;
        if (f3 < 1.0f && f3 != 0.0f && this.mClosing) {
            this.mExpandedHeight = 0.0f;
            ValueAnimator valueAnimator = this.mHeightAnimator;
            if (valueAnimator != null) {
                valueAnimator.end();
            }
        }
        this.mExpansionDragDownAmountPx = f;
        if (maxPanelHeight != 0.0f) {
            f2 = this.mExpandedHeight / maxPanelHeight;
        }
        this.mExpandedFraction = Math.min(1.0f, f2);
        onHeightUpdated(this.mExpandedHeight);
        updatePanelExpansionAndVisibility();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setExpandedHeightInternal$1$com-android-systemui-statusbar-phone-PanelViewController */
    public /* synthetic */ void mo44945xe4991c6f() {
        this.mLatencyTracker.onActionEnd(0);
    }

    /* access modifiers changed from: protected */
    public void setOverExpansion(float f) {
        this.mOverExpansion = f;
    }

    private void setOverExpansionInternal(float f, boolean z) {
        if (!z) {
            this.mLastGesturedOverExpansion = -1.0f;
            setOverExpansion(f);
        } else if (this.mLastGesturedOverExpansion != f) {
            this.mLastGesturedOverExpansion = f;
            setOverExpansion(Interpolators.getOvershootInterpolation(MathUtils.saturate(f / (((float) this.mView.getHeight()) / 3.0f))) * this.mPanelFlingOvershootAmount * 2.0f);
        }
    }

    public void setExpandedFraction(float f) {
        setExpandedHeight(((float) getMaxPanelHeight()) * f);
    }

    public float getExpandedHeight() {
        return this.mExpandedHeight;
    }

    public float getExpandedFraction() {
        return this.mExpandedFraction;
    }

    public boolean isFullyExpanded() {
        return this.mExpandedHeight >= ((float) getMaxPanelHeight());
    }

    public boolean isFullyCollapsed() {
        return this.mExpandedFraction <= 0.0f;
    }

    public boolean isCollapsing() {
        return this.mClosing || this.mIsLaunchAnimationRunning;
    }

    public boolean isFlinging() {
        return this.mIsFlinging;
    }

    public boolean isTracking() {
        return this.mTracking;
    }

    public void collapse(boolean z, float f) {
        if (canPanelBeCollapsed()) {
            cancelHeightAnimator();
            notifyExpandingStarted();
            setIsClosing(true);
            if (z) {
                this.mNextCollapseSpeedUpFactor = f;
                this.mView.postDelayed(this.mFlingCollapseRunnable, 120);
                return;
            }
            fling(0.0f, false, f, false);
        }
    }

    public boolean canPanelBeCollapsed() {
        return !isFullyCollapsed() && !this.mTracking && !this.mClosing;
    }

    public void expand(boolean z) {
        if (isFullyCollapsed() || isCollapsing()) {
            this.mInstantExpanding = true;
            this.mAnimateAfterExpanding = z;
            this.mUpdateFlingOnLayout = false;
            abortAnimations();
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            if (this.mExpanding) {
                notifyExpandingFinished();
            }
            updatePanelExpansionAndVisibility();
            this.mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (!PanelViewController.this.mInstantExpanding) {
                        PanelViewController.this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else if (PanelViewController.this.mCentralSurfaces.getNotificationShadeWindowView().isVisibleToUser()) {
                        PanelViewController.this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        if (PanelViewController.this.mAnimateAfterExpanding) {
                            PanelViewController.this.notifyExpandingStarted();
                            PanelViewController.this.beginJankMonitoring(0);
                            PanelViewController.this.fling(0.0f, true);
                        } else {
                            PanelViewController.this.setExpandedFraction(1.0f);
                        }
                        boolean unused = PanelViewController.this.mInstantExpanding = false;
                    }
                }
            });
            this.mView.requestLayout();
        }
    }

    public void instantCollapse() {
        abortAnimations();
        setExpandedFraction(0.0f);
        if (this.mExpanding) {
            notifyExpandingFinished();
        }
        if (this.mInstantExpanding) {
            this.mInstantExpanding = false;
            updatePanelExpansionAndVisibility();
        }
    }

    /* access modifiers changed from: private */
    public void abortAnimations() {
        cancelHeightAnimator();
        this.mView.removeCallbacks(this.mFlingCollapseRunnable);
    }

    /* access modifiers changed from: protected */
    public void startUnlockHintAnimation() {
        if (this.mHeightAnimator == null && !this.mTracking) {
            notifyExpandingStarted();
            startUnlockHintAnimationPhase1(new PanelViewController$$ExternalSyntheticLambda4(this));
            onUnlockHintStarted();
            this.mHintAnimationRunning = true;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startUnlockHintAnimation$3$com-android-systemui-statusbar-phone-PanelViewController */
    public /* synthetic */ void mo44948x15a26b9b() {
        notifyExpandingFinished();
        onUnlockHintFinished();
        this.mHintAnimationRunning = false;
    }

    /* access modifiers changed from: protected */
    public void onUnlockHintFinished() {
        this.mCentralSurfaces.onHintFinished();
    }

    /* access modifiers changed from: protected */
    public void onUnlockHintStarted() {
        this.mCentralSurfaces.onUnlockHintStarted();
    }

    public boolean isUnlockHintRunning() {
        return this.mHintAnimationRunning;
    }

    private void startUnlockHintAnimationPhase1(final Runnable runnable) {
        ValueAnimator createHeightAnimator = createHeightAnimator(Math.max(0.0f, ((float) getMaxPanelHeight()) - this.mHintDistance));
        createHeightAnimator.setDuration(250);
        createHeightAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        createHeightAnimator.addListener(new AnimatorListenerAdapter() {
            private boolean mCancelled;

            public void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (this.mCancelled) {
                    PanelViewController.this.setAnimator((ValueAnimator) null);
                    runnable.run();
                    return;
                }
                PanelViewController.this.startUnlockHintAnimationPhase2(runnable);
            }
        });
        createHeightAnimator.start();
        setAnimator(createHeightAnimator);
        View[] viewArr = {this.mKeyguardBottomArea.getIndicationArea(), this.mCentralSurfaces.getAmbientIndicationContainer()};
        for (int i = 0; i < 2; i++) {
            View view = viewArr[i];
            if (view != null) {
                view.animate().translationY(-this.mHintDistance).setDuration(250).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).withEndAction(new PanelViewController$$ExternalSyntheticLambda2(this, view)).start();
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startUnlockHintAnimationPhase1$4$com-android-systemui-statusbar-phone-PanelViewController */
    public /* synthetic */ void mo44949xb73085c4(View view) {
        view.animate().translationY(0.0f).setDuration(450).setInterpolator(this.mBounceInterpolator).start();
    }

    /* access modifiers changed from: private */
    public void setAnimator(ValueAnimator valueAnimator) {
        this.mHeightAnimator = valueAnimator;
        if (valueAnimator == null && this.mPanelUpdateWhenAnimatorEnds) {
            this.mPanelUpdateWhenAnimatorEnds = false;
            requestPanelHeightUpdate();
        }
    }

    /* access modifiers changed from: private */
    public void startUnlockHintAnimationPhase2(final Runnable runnable) {
        ValueAnimator createHeightAnimator = createHeightAnimator((float) getMaxPanelHeight());
        createHeightAnimator.setDuration(450);
        createHeightAnimator.setInterpolator(this.mBounceInterpolator);
        createHeightAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                PanelViewController.this.setAnimator((ValueAnimator) null);
                runnable.run();
                PanelViewController.this.updatePanelExpansionAndVisibility();
            }
        });
        createHeightAnimator.start();
        setAnimator(createHeightAnimator);
    }

    private ValueAnimator createHeightAnimator(float f) {
        return createHeightAnimator(f, 0.0f);
    }

    private ValueAnimator createHeightAnimator(float f, float f2) {
        float f3 = this.mOverExpansion;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mExpandedHeight, f});
        ofFloat.addUpdateListener(new PanelViewController$$ExternalSyntheticLambda3(this, f2, f, f3, ofFloat));
        return ofFloat;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createHeightAnimator$5$com-android-systemui-statusbar-phone-PanelViewController */
    public /* synthetic */ void mo44944x3aa4d246(float f, float f2, float f3, ValueAnimator valueAnimator, ValueAnimator valueAnimator2) {
        if (f > 0.0f || (f2 == 0.0f && f3 != 0.0f)) {
            setOverExpansionInternal(MathUtils.lerp(f3, this.mPanelFlingOvershootAmount * f, Interpolators.FAST_OUT_SLOW_IN.getInterpolation(valueAnimator.getAnimatedFraction())), false);
        }
        setExpandedHeightInternal(((Float) valueAnimator2.getAnimatedValue()).floatValue());
    }

    public void updateVisibility() {
        this.mView.setVisibility(shouldPanelBeVisible() ? 0 : 4);
    }

    public void updatePanelExpansionAndVisibility() {
        this.mPanelExpansionStateManager.onPanelExpansionChanged(this.mExpandedFraction, isExpanded(), this.mTracking, this.mExpansionDragDownAmountPx);
        updateVisibility();
    }

    public boolean isExpanded() {
        return this.mExpandedFraction > 0.0f || this.mInstantExpanding || isPanelVisibleBecauseOfHeadsUp() || this.mTracking || (this.mHeightAnimator != null && !this.mIsSpringBackAnimation);
    }

    /* access modifiers changed from: protected */
    public boolean onEmptySpaceClick(float f) {
        if (this.mHintAnimationRunning) {
            return true;
        }
        return onMiddleClicked();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Object[] objArr = new Object[8];
        objArr[0] = getClass().getSimpleName();
        objArr[1] = Float.valueOf(getExpandedHeight());
        objArr[2] = Integer.valueOf(getMaxPanelHeight());
        boolean z = this.mClosing;
        String str = ExifInterface.GPS_DIRECTION_TRUE;
        objArr[3] = z ? str : "f";
        objArr[4] = this.mTracking ? str : "f";
        ValueAnimator valueAnimator = this.mHeightAnimator;
        objArr[5] = valueAnimator;
        objArr[6] = (valueAnimator == null || !valueAnimator.isStarted()) ? "" : " (started)";
        if (!this.mTouchDisabled) {
            str = "f";
        }
        objArr[7] = str;
        printWriter.println(String.format("[PanelView(%s): expandedHeight=%f maxPanelHeight=%d closing=%s tracking=%s timeAnim=%s%s touchDisabled=%s]", objArr));
    }

    public void setHeadsUpManager(HeadsUpManagerPhone headsUpManagerPhone) {
        this.mHeadsUpManager = headsUpManagerPhone;
    }

    public void setIsLaunchAnimationRunning(boolean z) {
        this.mIsLaunchAnimationRunning = z;
    }

    /* access modifiers changed from: protected */
    public void setIsClosing(boolean z) {
        this.mClosing = z;
    }

    /* access modifiers changed from: protected */
    public boolean isClosing() {
        return this.mClosing;
    }

    public void collapseWithDuration(int i) {
        this.mFixedDuration = i;
        collapse(false, 1.0f);
        this.mFixedDuration = -1;
    }

    public ViewGroup getView() {
        return this.mView;
    }

    public OnLayoutChangeListener createLayoutChangeListener() {
        return new OnLayoutChangeListener();
    }

    /* access modifiers changed from: protected */
    public OnConfigurationChangedListener createOnConfigurationChangedListener() {
        return new OnConfigurationChangedListener();
    }

    public class TouchHandler implements View.OnTouchListener {
        public TouchHandler() {
        }

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int pointerId;
            if (!PanelViewController.this.mInstantExpanding && PanelViewController.this.mNotificationsDragEnabled && !PanelViewController.this.mTouchDisabled && (!PanelViewController.this.mMotionAborted || motionEvent.getActionMasked() == 0)) {
                int findPointerIndex = motionEvent.findPointerIndex(PanelViewController.this.mTrackingPointer);
                if (findPointerIndex < 0) {
                    int unused = PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(0);
                    findPointerIndex = 0;
                }
                float x = motionEvent.getX(findPointerIndex);
                float y = motionEvent.getY(findPointerIndex);
                boolean canCollapsePanelOnTouch = PanelViewController.this.canCollapsePanelOnTouch();
                int actionMasked = motionEvent.getActionMasked();
                int i = 1;
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked == 2) {
                            float access$2000 = y - PanelViewController.this.mInitialTouchY;
                            PanelViewController.this.addMovement(motionEvent);
                            boolean z = PanelViewController.this.mPanelClosedOnDown && !PanelViewController.this.mCollapsedAndHeadsUpOnDown;
                            if (canCollapsePanelOnTouch || PanelViewController.this.mTouchStartedInEmptyArea || PanelViewController.this.mAnimatingOnDown || z) {
                                float abs = Math.abs(access$2000);
                                float touchSlop = PanelViewController.this.getTouchSlop(motionEvent);
                                if ((access$2000 < (-touchSlop) || ((z || PanelViewController.this.mAnimatingOnDown) && abs > touchSlop)) && abs > Math.abs(x - PanelViewController.this.mInitialTouchX)) {
                                    PanelViewController.this.cancelHeightAnimator();
                                    PanelViewController panelViewController = PanelViewController.this;
                                    panelViewController.startExpandMotion(x, y, true, panelViewController.mExpandedHeight);
                                    return true;
                                }
                            }
                        } else if (actionMasked != 3) {
                            if (actionMasked != 5) {
                                if (actionMasked == 6 && PanelViewController.this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                                    if (motionEvent.getPointerId(0) != pointerId) {
                                        i = 0;
                                    }
                                    int unused2 = PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(i);
                                    float unused3 = PanelViewController.this.mInitialTouchX = motionEvent.getX(i);
                                    float unused4 = PanelViewController.this.mInitialTouchY = motionEvent.getY(i);
                                }
                            } else if (PanelViewController.this.mStatusBarStateController.getState() == 1) {
                                boolean unused5 = PanelViewController.this.mMotionAborted = true;
                                PanelViewController.this.mVelocityTracker.clear();
                            }
                        }
                    }
                    PanelViewController.this.mVelocityTracker.clear();
                } else {
                    PanelViewController.this.mCentralSurfaces.userActivity();
                    PanelViewController panelViewController2 = PanelViewController.this;
                    boolean unused6 = panelViewController2.mAnimatingOnDown = panelViewController2.mHeightAnimator != null && !PanelViewController.this.mIsSpringBackAnimation;
                    float unused7 = PanelViewController.this.mMinExpandHeight = 0.0f;
                    PanelViewController panelViewController3 = PanelViewController.this;
                    panelViewController3.mDownTime = panelViewController3.mSystemClock.uptimeMillis();
                    if (!PanelViewController.this.mAnimatingOnDown || !PanelViewController.this.mClosing || PanelViewController.this.mHintAnimationRunning) {
                        float unused8 = PanelViewController.this.mInitialTouchY = y;
                        float unused9 = PanelViewController.this.mInitialTouchX = x;
                        PanelViewController panelViewController4 = PanelViewController.this;
                        boolean unused10 = panelViewController4.mTouchStartedInEmptyArea = !panelViewController4.isInContentBounds(x, y);
                        PanelViewController panelViewController5 = PanelViewController.this;
                        boolean unused11 = panelViewController5.mTouchSlopExceeded = panelViewController5.mTouchSlopExceededBeforeDown;
                        boolean unused12 = PanelViewController.this.mMotionAborted = false;
                        PanelViewController panelViewController6 = PanelViewController.this;
                        boolean unused13 = panelViewController6.mPanelClosedOnDown = panelViewController6.isFullyCollapsed();
                        boolean unused14 = PanelViewController.this.mCollapsedAndHeadsUpOnDown = false;
                        boolean unused15 = PanelViewController.this.mHasLayoutedSinceDown = false;
                        boolean unused16 = PanelViewController.this.mUpdateFlingOnLayout = false;
                        boolean unused17 = PanelViewController.this.mTouchAboveFalsingThreshold = false;
                        PanelViewController.this.addMovement(motionEvent);
                    } else {
                        PanelViewController.this.cancelHeightAnimator();
                        boolean unused18 = PanelViewController.this.mTouchSlopExceeded = true;
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int pointerId;
            if (PanelViewController.this.mInstantExpanding) {
                return false;
            }
            if (PanelViewController.this.mTouchDisabled && motionEvent.getActionMasked() != 3) {
                return false;
            }
            if (PanelViewController.this.mMotionAborted && motionEvent.getActionMasked() != 0) {
                return false;
            }
            if (!PanelViewController.this.mNotificationsDragEnabled) {
                if (PanelViewController.this.mTracking) {
                    PanelViewController.this.onTrackingStopped(true);
                }
                return false;
            } else if (!PanelViewController.this.isFullyCollapsed() || !motionEvent.isFromSource(8194)) {
                int findPointerIndex = motionEvent.findPointerIndex(PanelViewController.this.mTrackingPointer);
                if (findPointerIndex < 0) {
                    int unused = PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(0);
                    findPointerIndex = 0;
                }
                float x = motionEvent.getX(findPointerIndex);
                float y = motionEvent.getY(findPointerIndex);
                if (motionEvent.getActionMasked() == 0) {
                    PanelViewController panelViewController = PanelViewController.this;
                    boolean unused2 = panelViewController.mGestureWaitForTouchSlop = panelViewController.shouldGestureWaitForTouchSlop();
                    PanelViewController panelViewController2 = PanelViewController.this;
                    boolean unused3 = panelViewController2.mIgnoreXTouchSlop = panelViewController2.isFullyCollapsed() || PanelViewController.this.shouldGestureIgnoreXTouchSlop(x, y);
                }
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked != 0) {
                    if (actionMasked != 1) {
                        if (actionMasked == 2) {
                            PanelViewController.this.addMovement(motionEvent);
                            float access$2000 = y - PanelViewController.this.mInitialTouchY;
                            if (PanelViewController.this.mEx.shouldIgnorePanelViewTouch(access$2000)) {
                                return false;
                            }
                            if (Math.abs(access$2000) > PanelViewController.this.getTouchSlop(motionEvent) && (Math.abs(access$2000) > Math.abs(x - PanelViewController.this.mInitialTouchX) || PanelViewController.this.mIgnoreXTouchSlop)) {
                                boolean unused4 = PanelViewController.this.mTouchSlopExceeded = true;
                                if (PanelViewController.this.mGestureWaitForTouchSlop && !PanelViewController.this.mTracking && !PanelViewController.this.mCollapsedAndHeadsUpOnDown) {
                                    if (PanelViewController.this.mInitialOffsetOnTouch != 0.0f) {
                                        PanelViewController panelViewController3 = PanelViewController.this;
                                        panelViewController3.startExpandMotion(x, y, false, panelViewController3.mExpandedHeight);
                                        access$2000 = 0.0f;
                                    }
                                    PanelViewController.this.cancelHeightAnimator();
                                    PanelViewController.this.onTrackingStarted();
                                }
                            }
                            float max = Math.max(Math.max(0.0f, PanelViewController.this.mInitialOffsetOnTouch + access$2000), PanelViewController.this.mMinExpandHeight);
                            if ((-access$2000) >= ((float) PanelViewController.this.getFalsingThreshold())) {
                                boolean unused5 = PanelViewController.this.mTouchAboveFalsingThreshold = true;
                                PanelViewController panelViewController4 = PanelViewController.this;
                                boolean unused6 = panelViewController4.mUpwardsWhenThresholdReached = panelViewController4.isDirectionUpwards(x, y);
                            }
                            if ((!PanelViewController.this.mGestureWaitForTouchSlop || PanelViewController.this.mTracking) && !PanelViewController.this.isTrackingBlocked()) {
                                PanelViewController.this.mAmbientState.setSwipingUp(access$2000 <= 0.0f);
                                PanelViewController.this.setExpandedHeightInternal(max);
                            }
                        } else if (actionMasked != 3) {
                            if (actionMasked != 5) {
                                if (actionMasked == 6 && PanelViewController.this.mTrackingPointer == (pointerId = motionEvent.getPointerId(motionEvent.getActionIndex()))) {
                                    int i = motionEvent.getPointerId(0) != pointerId ? 0 : 1;
                                    float y2 = motionEvent.getY(i);
                                    float x2 = motionEvent.getX(i);
                                    int unused7 = PanelViewController.this.mTrackingPointer = motionEvent.getPointerId(i);
                                    boolean unused8 = PanelViewController.this.mHandlingPointerUp = true;
                                    PanelViewController panelViewController5 = PanelViewController.this;
                                    panelViewController5.startExpandMotion(x2, y2, true, panelViewController5.mExpandedHeight);
                                    boolean unused9 = PanelViewController.this.mHandlingPointerUp = false;
                                }
                            } else if (PanelViewController.this.mStatusBarStateController.getState() == 1) {
                                boolean unused10 = PanelViewController.this.mMotionAborted = true;
                                PanelViewController.this.endMotionEvent(motionEvent, x, y, true);
                                return false;
                            }
                        }
                    }
                    PanelViewController.this.addMovement(motionEvent);
                    PanelViewController.this.endMotionEvent(motionEvent, x, y, false);
                    if (PanelViewController.this.mHeightAnimator == null) {
                        if (motionEvent.getActionMasked() == 1) {
                            PanelViewController.this.endJankMonitoring(0);
                        } else {
                            PanelViewController.this.cancelJankMonitoring(0);
                        }
                    }
                } else {
                    PanelViewController panelViewController6 = PanelViewController.this;
                    panelViewController6.startExpandMotion(x, y, false, panelViewController6.mExpandedHeight);
                    float unused11 = PanelViewController.this.mMinExpandHeight = 0.0f;
                    PanelViewController panelViewController7 = PanelViewController.this;
                    boolean unused12 = panelViewController7.mPanelClosedOnDown = panelViewController7.isFullyCollapsed();
                    boolean unused13 = PanelViewController.this.mHasLayoutedSinceDown = false;
                    boolean unused14 = PanelViewController.this.mUpdateFlingOnLayout = false;
                    boolean unused15 = PanelViewController.this.mMotionAborted = false;
                    PanelViewController panelViewController8 = PanelViewController.this;
                    panelViewController8.mDownTime = panelViewController8.mSystemClock.uptimeMillis();
                    boolean unused16 = PanelViewController.this.mTouchAboveFalsingThreshold = false;
                    PanelViewController panelViewController9 = PanelViewController.this;
                    boolean unused17 = panelViewController9.mCollapsedAndHeadsUpOnDown = panelViewController9.isFullyCollapsed() && PanelViewController.this.mHeadsUpManager.hasPinnedHeadsUp();
                    PanelViewController.this.addMovement(motionEvent);
                    boolean z = PanelViewController.this.mHeightAnimator != null && !PanelViewController.this.mHintAnimationRunning && !PanelViewController.this.mIsSpringBackAnimation;
                    if (!PanelViewController.this.mGestureWaitForTouchSlop || z) {
                        PanelViewController panelViewController10 = PanelViewController.this;
                        boolean unused18 = panelViewController10.mTouchSlopExceeded = z || panelViewController10.mTouchSlopExceededBeforeDown;
                        PanelViewController.this.cancelHeightAnimator();
                        PanelViewController.this.onTrackingStarted();
                    }
                    if (PanelViewController.this.isFullyCollapsed() && !PanelViewController.this.mHeadsUpManager.hasPinnedHeadsUp() && !PanelViewController.this.mCentralSurfaces.isBouncerShowing()) {
                        PanelViewController.this.startOpening(motionEvent);
                    }
                    PanelViewController.this.mEx.onPanelViewTouchTrackStarted();
                }
                if (!PanelViewController.this.mGestureWaitForTouchSlop || PanelViewController.this.mTracking) {
                    return true;
                }
                return false;
            } else {
                if (motionEvent.getAction() == 1) {
                    PanelViewController.this.expand(true);
                }
                return true;
            }
        }
    }

    public class OnLayoutChangeListener implements View.OnLayoutChangeListener {
        public OnLayoutChangeListener() {
        }

        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            PanelViewController.this.requestPanelHeightUpdate();
            boolean unused = PanelViewController.this.mHasLayoutedSinceDown = true;
            if (PanelViewController.this.mUpdateFlingOnLayout) {
                PanelViewController.this.abortAnimations();
                PanelViewController panelViewController = PanelViewController.this;
                panelViewController.fling(panelViewController.mUpdateFlingVelocity, true);
                boolean unused2 = PanelViewController.this.mUpdateFlingOnLayout = false;
            }
        }
    }

    public class OnConfigurationChangedListener implements PanelView.OnConfigurationChangedListener {
        public OnConfigurationChangedListener() {
        }

        public void onConfigurationChanged(Configuration configuration) {
            PanelViewController.this.loadDimens();
        }
    }

    /* access modifiers changed from: private */
    public void beginJankMonitoring(int i) {
        if (this.mInteractionJankMonitor != null) {
            this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(i, this.mView).setTag(isFullyCollapsed() ? "Expand" : "Collapse"));
        }
    }

    /* access modifiers changed from: private */
    public void endJankMonitoring(int i) {
        if (this.mInteractionJankMonitor != null) {
            InteractionJankMonitor.getInstance().end(i);
        }
    }

    /* access modifiers changed from: private */
    public void cancelJankMonitoring(int i) {
        if (this.mInteractionJankMonitor != null) {
            InteractionJankMonitor.getInstance().cancel(i);
        }
    }

    /* access modifiers changed from: protected */
    public float getExpansionFraction() {
        return this.mExpandedFraction;
    }

    /* access modifiers changed from: protected */
    public PanelExpansionStateManager getPanelExpansionStateManager() {
        return this.mPanelExpansionStateManager;
    }
}
