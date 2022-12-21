package com.android.systemui.dreams.touch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.android.internal.logging.UiEventLogger;
import com.android.p019wm.shell.animation.FlingAnimationUtils;
import com.android.systemui.assist.PhoneStateMonitor$$ExternalSyntheticLambda1;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;

public class BouncerSwipeTouchHandler implements DreamTouchHandler {
    public static final float FLING_PERCENTAGE_THRESHOLD = 0.5f;
    private static final String TAG = "BouncerSwipeTouchHandler";
    /* access modifiers changed from: private */
    public boolean mBouncerInitiallyShowing;
    private final float mBouncerZoneScreenPercentage;
    /* access modifiers changed from: private */
    public Boolean mCapture;
    /* access modifiers changed from: private */
    public final Optional<CentralSurfaces> mCentralSurfaces;
    private float mCurrentExpansion;
    private final DisplayMetrics mDisplayMetrics;
    private final FlingAnimationUtils mFlingAnimationUtils;
    private final FlingAnimationUtils mFlingAnimationUtilsClosing;
    private final NotificationShadeWindowController mNotificationShadeWindowController;
    private final GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (BouncerSwipeTouchHandler.this.mCapture == null) {
                Boolean unused = BouncerSwipeTouchHandler.this.mCapture = Boolean.valueOf(Math.abs(f2) > Math.abs(f));
                BouncerSwipeTouchHandler bouncerSwipeTouchHandler = BouncerSwipeTouchHandler.this;
                boolean unused2 = bouncerSwipeTouchHandler.mBouncerInitiallyShowing = ((Boolean) bouncerSwipeTouchHandler.mCentralSurfaces.map(new PhoneStateMonitor$$ExternalSyntheticLambda1()).orElse(false)).booleanValue();
                if (BouncerSwipeTouchHandler.this.mCapture.booleanValue()) {
                    BouncerSwipeTouchHandler.this.mStatusBarKeyguardViewManager.showBouncer(false);
                }
            }
            if (!BouncerSwipeTouchHandler.this.mCapture.booleanValue()) {
                return false;
            }
            if (!BouncerSwipeTouchHandler.this.mBouncerInitiallyShowing && motionEvent.getY() < motionEvent2.getY()) {
                return true;
            }
            if ((BouncerSwipeTouchHandler.this.mBouncerInitiallyShowing && motionEvent.getY() > motionEvent2.getY()) || !BouncerSwipeTouchHandler.this.mCentralSurfaces.isPresent()) {
                return true;
            }
            float y = motionEvent2.getY() - motionEvent.getY();
            float abs = Math.abs(motionEvent.getY() - motionEvent2.getY()) / ((CentralSurfaces) BouncerSwipeTouchHandler.this.mCentralSurfaces.get()).getDisplayHeight();
            BouncerSwipeTouchHandler bouncerSwipeTouchHandler2 = BouncerSwipeTouchHandler.this;
            if (!bouncerSwipeTouchHandler2.mBouncerInitiallyShowing) {
                abs = 1.0f - abs;
            }
            bouncerSwipeTouchHandler2.setPanelExpansion(abs, y);
            return true;
        }
    };
    /* access modifiers changed from: private */
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    private DreamTouchHandler.TouchSession mTouchSession;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;
    private ValueAnimatorCreator mValueAnimatorCreator;
    private VelocityTracker mVelocityTracker;
    private VelocityTrackerFactory mVelocityTrackerFactory;

    public interface ValueAnimatorCreator {
        ValueAnimator create(float f, float f2);
    }

    public interface VelocityTrackerFactory {
        VelocityTracker obtain();
    }

    /* access modifiers changed from: private */
    public void setPanelExpansion(float f, float f2) {
        this.mCurrentExpansion = f;
        this.mStatusBarKeyguardViewManager.onPanelExpansionChanged(new PanelExpansionChangeEvent(this.mCurrentExpansion, false, true, f2));
    }

    public enum DreamEvent implements UiEventLogger.UiEventEnum {
        DREAM_SWIPED(988),
        DREAM_BOUNCER_FULLY_VISIBLE(1056);
        
        private final int mId;

        private DreamEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    @Inject
    public BouncerSwipeTouchHandler(DisplayMetrics displayMetrics, StatusBarKeyguardViewManager statusBarKeyguardViewManager, Optional<CentralSurfaces> optional, NotificationShadeWindowController notificationShadeWindowController, ValueAnimatorCreator valueAnimatorCreator, VelocityTrackerFactory velocityTrackerFactory, @Named("swipe_to_bouncer_fling_animation_utils_opening") FlingAnimationUtils flingAnimationUtils, @Named("swipe_to_bouncer_fling_animation_utils_closing") FlingAnimationUtils flingAnimationUtils2, @Named("swipe_to_bouncer_start_region") float f, UiEventLogger uiEventLogger) {
        this.mDisplayMetrics = displayMetrics;
        this.mCentralSurfaces = optional;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mBouncerZoneScreenPercentage = f;
        this.mFlingAnimationUtils = flingAnimationUtils;
        this.mFlingAnimationUtilsClosing = flingAnimationUtils2;
        this.mValueAnimatorCreator = valueAnimatorCreator;
        this.mVelocityTrackerFactory = velocityTrackerFactory;
        this.mUiEventLogger = uiEventLogger;
    }

    public void getTouchInitiationRegion(Region region) {
        if (((Boolean) this.mCentralSurfaces.map(new PhoneStateMonitor$$ExternalSyntheticLambda1()).orElse(false)).booleanValue()) {
            region.op(new Rect(0, 0, this.mDisplayMetrics.widthPixels, Math.round(((float) this.mDisplayMetrics.heightPixels) * this.mBouncerZoneScreenPercentage)), Region.Op.UNION);
        } else {
            region.op(new Rect(0, Math.round(((float) this.mDisplayMetrics.heightPixels) * (1.0f - this.mBouncerZoneScreenPercentage)), this.mDisplayMetrics.widthPixels, this.mDisplayMetrics.heightPixels), Region.Op.UNION);
        }
    }

    public void onSessionStart(DreamTouchHandler.TouchSession touchSession) {
        VelocityTracker obtain = this.mVelocityTrackerFactory.obtain();
        this.mVelocityTracker = obtain;
        this.mTouchSession = touchSession;
        obtain.clear();
        this.mNotificationShadeWindowController.setForcePluginOpen(true, this);
        touchSession.registerCallback(new BouncerSwipeTouchHandler$$ExternalSyntheticLambda1(this));
        touchSession.registerGestureListener(this.mOnGestureListener);
        touchSession.registerInputListener(new BouncerSwipeTouchHandler$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onSessionStart$0$com-android-systemui-dreams-touch-BouncerSwipeTouchHandler */
    public /* synthetic */ void mo32623xabef0776() {
        this.mVelocityTracker.recycle();
        this.mCapture = null;
        this.mNotificationShadeWindowController.setForcePluginOpen(false, this);
    }

    /* access modifiers changed from: private */
    /* renamed from: onMotionEvent */
    public void mo32624xb1f2d2d5(InputEvent inputEvent) {
        if (!(inputEvent instanceof MotionEvent)) {
            Log.e(TAG, "non MotionEvent received:" + inputEvent);
            return;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            this.mTouchSession.pop();
            Boolean bool = this.mCapture;
            if (bool != null && bool.booleanValue()) {
                this.mVelocityTracker.computeCurrentVelocity(1000);
                float yVelocity = this.mVelocityTracker.getYVelocity();
                float f = flingRevealsOverlay(yVelocity, (float) Math.hypot((double) this.mVelocityTracker.getXVelocity(), (double) yVelocity)) ? 1.0f : 0.0f;
                if (!this.mBouncerInitiallyShowing && f == 0.0f) {
                    this.mUiEventLogger.log(DreamEvent.DREAM_SWIPED);
                }
                flingToExpansion(yVelocity, f);
                if (f == 1.0f) {
                    this.mStatusBarKeyguardViewManager.reset(false);
                    return;
                }
                return;
            }
            return;
        }
        this.mVelocityTracker.addMovement(motionEvent);
    }

    private ValueAnimator createExpansionAnimator(float f, float f2) {
        ValueAnimator create = this.mValueAnimatorCreator.create(this.mCurrentExpansion, f);
        create.addUpdateListener(new BouncerSwipeTouchHandler$$ExternalSyntheticLambda0(this, f2));
        if (!this.mBouncerInitiallyShowing && f == 0.0f) {
            create.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    BouncerSwipeTouchHandler.this.mUiEventLogger.log(DreamEvent.DREAM_BOUNCER_FULLY_VISIBLE);
                }
            });
        }
        return create;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createExpansionAnimator$2$com-android-systemui-dreams-touch-BouncerSwipeTouchHandler */
    public /* synthetic */ void mo32622xfaaef06f(float f, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        setPanelExpansion(floatValue, f * floatValue);
    }

    /* access modifiers changed from: protected */
    public boolean flingRevealsOverlay(float f, float f2) {
        if (Math.abs(f2) < this.mFlingAnimationUtils.getMinVelocityPxPerSecond()) {
            if (this.mCurrentExpansion > 0.5f) {
                return true;
            }
            return false;
        } else if (f > 0.0f) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void flingToExpansion(float f, float f2) {
        if (this.mCentralSurfaces.isPresent()) {
            float displayHeight = this.mCentralSurfaces.get().getDisplayHeight();
            float f3 = displayHeight * this.mCurrentExpansion;
            float f4 = displayHeight * f2;
            ValueAnimator createExpansionAnimator = createExpansionAnimator(f2, f4 - f3);
            if (f2 == 1.0f) {
                this.mFlingAnimationUtilsClosing.apply((Animator) createExpansionAnimator, f3, f4, f, displayHeight);
            } else {
                this.mFlingAnimationUtils.apply((Animator) createExpansionAnimator, f3, f4, f, displayHeight);
            }
            createExpansionAnimator.start();
        }
    }
}
