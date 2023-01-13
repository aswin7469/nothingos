package com.android.systemui.navigationbar.gestural;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.MathUtils;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.internal.util.LatencyTracker;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.statusbar.VibratorHelper;
import java.p026io.PrintWriter;
import java.util.concurrent.Executor;

public class NavigationBarEdgePanel extends View implements NavigationEdgeBackPlugin {
    private static final int ARROW_ANGLE_ADDED_PER_1000_SPEED = 4;
    private static final int ARROW_ANGLE_WHEN_EXTENDED_DEGREES = 56;
    private static final int ARROW_LENGTH_DP = 18;
    private static final int ARROW_MAX_ANGLE_SPEED_OFFSET_DEGREES = 4;
    private static final float ARROW_THICKNESS_DP = 2.5f;
    private static final int BASE_TRANSLATION_DP = 32;
    private static final long COLOR_ANIMATION_DURATION_MS = 120;
    private static final FloatPropertyCompat<NavigationBarEdgePanel> CURRENT_ANGLE = new FloatPropertyCompat<NavigationBarEdgePanel>("currentAngle") {
        public void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            navigationBarEdgePanel.setCurrentAngle(f);
        }

        public float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            return navigationBarEdgePanel.getCurrentAngle();
        }
    };
    private static final FloatPropertyCompat<NavigationBarEdgePanel> CURRENT_TRANSLATION = new FloatPropertyCompat<NavigationBarEdgePanel>("currentTranslation") {
        public void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            navigationBarEdgePanel.setCurrentTranslation(f);
        }

        public float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            return navigationBarEdgePanel.getCurrentTranslation();
        }
    };
    private static final FloatPropertyCompat<NavigationBarEdgePanel> CURRENT_VERTICAL_TRANSLATION = new FloatPropertyCompat<NavigationBarEdgePanel>("verticalTranslation") {
        public void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            navigationBarEdgePanel.setVerticalTranslation(f);
        }

        public float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            return navigationBarEdgePanel.getVerticalTranslation();
        }
    };
    private static final long DISAPPEAR_ARROW_ANIMATION_DURATION_MS = 100;
    private static final long DISAPPEAR_FADE_ANIMATION_DURATION_MS = 80;
    private static final boolean ENABLE_FAILSAFE = true;
    private static final long FAILSAFE_DELAY_MS = 200;
    private static final int GESTURE_DURATION_FOR_CLICK_MS = 400;
    private static final int PROTECTION_WIDTH_PX = 2;
    private static final int RUBBER_BAND_AMOUNT = 15;
    private static final int RUBBER_BAND_AMOUNT_APPEAR = 4;
    private static final Interpolator RUBBER_BAND_INTERPOLATOR = new PathInterpolator(0.2f, 1.0f, 1.0f, 1.0f);
    private static final Interpolator RUBBER_BAND_INTERPOLATOR_APPEAR = new PathInterpolator(0.25f, 1.0f, 1.0f, 1.0f);
    private static final String TAG = "NavigationBarEdgePanel";
    private final SpringAnimation mAngleAnimation;
    private final SpringForce mAngleAppearForce;
    private final SpringForce mAngleDisappearForce;
    private float mAngleOffset;
    private int mArrowColor;
    private final ValueAnimator mArrowColorAnimator;
    private int mArrowColorDark;
    private int mArrowColorLight;
    private final ValueAnimator mArrowDisappearAnimation;
    private final float mArrowLength;
    private int mArrowPaddingEnd;
    private final Path mArrowPath = new Path();
    private int mArrowStartColor;
    private final float mArrowThickness;
    private boolean mArrowsPointLeft;
    private BackAnimation mBackAnimation;
    private NavigationEdgeBackPlugin.BackCallback mBackCallback;
    private final float mBaseTranslation;
    private float mCurrentAngle;
    private int mCurrentArrowColor;
    private float mCurrentTranslation;
    private final float mDensity;
    private float mDesiredAngle;
    private float mDesiredTranslation;
    private float mDesiredVerticalTranslation;
    private float mDisappearAmount;
    private final Point mDisplaySize = new Point();
    private boolean mDragSlopPassed;
    private final Runnable mFailsafeRunnable;
    private int mFingerOffset;
    private final Handler mHandler;
    private boolean mIsDark;
    private boolean mIsLeftPanel;
    private final LatencyTracker mLatencyTracker;
    private WindowManager.LayoutParams mLayoutParams;
    private int mLeftInset;
    private float mMaxTranslation;
    private int mMinArrowPosition;
    private final float mMinDeltaForSwitch;
    private final Paint mPaint;
    private float mPreviousTouchTranslation;
    private int mProtectionColor;
    private int mProtectionColorDark;
    private int mProtectionColorLight;
    private final Paint mProtectionPaint;
    private RegionSamplingHelper mRegionSamplingHelper;
    private final SpringForce mRegularTranslationSpring;
    private int mRightInset;
    /* access modifiers changed from: private */
    public final Rect mSamplingRect;
    private int mScreenSize;
    private DynamicAnimation.OnAnimationEndListener mSetGoneEndListener;
    private boolean mShowProtection;
    private float mStartX;
    private float mStartY;
    private final float mSwipeProgressThreshold;
    private final float mSwipeTriggerThreshold;
    private float mTotalTouchDelta;
    private boolean mTrackingBackArrowLatency;
    private final SpringAnimation mTranslationAnimation;
    private boolean mTriggerBack;
    private boolean mTriggerBackSilently;
    private final SpringForce mTriggerBackSpring;
    private VelocityTracker mVelocityTracker;
    private float mVerticalTranslation;
    private final SpringAnimation mVerticalTranslationAnimation;
    private long mVibrationTime;
    private final VibratorHelper mVibratorHelper;
    private final WindowManager mWindowManager;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public NavigationBarEdgePanel(Context context, BackAnimation backAnimation, LatencyTracker latencyTracker) {
        super(context);
        Paint paint = new Paint();
        this.mPaint = paint;
        final boolean z = false;
        this.mIsDark = false;
        this.mShowProtection = false;
        this.mSamplingRect = new Rect();
        this.mTrackingBackArrowLatency = false;
        this.mHandler = new Handler();
        this.mFailsafeRunnable = new NavigationBarEdgePanel$$ExternalSyntheticLambda2(this);
        this.mSetGoneEndListener = new DynamicAnimation.OnAnimationEndListener() {
            public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                dynamicAnimation.removeEndListener(this);
                if (!z) {
                    NavigationBarEdgePanel.this.setVisibility(8);
                }
            }
        };
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mBackAnimation = backAnimation;
        this.mVibratorHelper = (VibratorHelper) Dependency.get(VibratorHelper.class);
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mBaseTranslation = m526dp(32.0f);
        this.mArrowLength = m526dp(18.0f);
        float dp = m526dp(ARROW_THICKNESS_DP);
        this.mArrowThickness = dp;
        this.mMinDeltaForSwitch = m526dp(32.0f);
        paint.setStrokeWidth(dp);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mArrowColorAnimator = ofFloat;
        ofFloat.setDuration(COLOR_ANIMATION_DURATION_MS);
        ofFloat.addUpdateListener(new NavigationBarEdgePanel$$ExternalSyntheticLambda3(this));
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mArrowDisappearAnimation = ofFloat2;
        ofFloat2.setDuration(100);
        ofFloat2.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat2.addUpdateListener(new NavigationBarEdgePanel$$ExternalSyntheticLambda4(this));
        SpringAnimation springAnimation = new SpringAnimation(this, CURRENT_ANGLE);
        this.mAngleAnimation = springAnimation;
        SpringForce dampingRatio = new SpringForce().setStiffness(500.0f).setDampingRatio(0.5f);
        this.mAngleAppearForce = dampingRatio;
        this.mAngleDisappearForce = new SpringForce().setStiffness(1500.0f).setDampingRatio(0.5f).setFinalPosition(90.0f);
        springAnimation.setSpring(dampingRatio).setMaxValue(90.0f);
        SpringAnimation springAnimation2 = new SpringAnimation(this, CURRENT_TRANSLATION);
        this.mTranslationAnimation = springAnimation2;
        SpringForce dampingRatio2 = new SpringForce().setStiffness(1500.0f).setDampingRatio(0.75f);
        this.mRegularTranslationSpring = dampingRatio2;
        this.mTriggerBackSpring = new SpringForce().setStiffness(450.0f).setDampingRatio(0.75f);
        springAnimation2.setSpring(dampingRatio2);
        SpringAnimation springAnimation3 = new SpringAnimation(this, CURRENT_VERTICAL_TRANSLATION);
        this.mVerticalTranslationAnimation = springAnimation3;
        springAnimation3.setSpring(new SpringForce().setStiffness(1500.0f).setDampingRatio(0.75f));
        Paint paint2 = new Paint(paint);
        this.mProtectionPaint = paint2;
        paint2.setStrokeWidth(dp + 2.0f);
        loadDimens();
        loadColors(context);
        updateArrowDirection();
        this.mSwipeTriggerThreshold = context.getResources().getDimension(C1894R.dimen.navigation_edge_action_drag_threshold);
        this.mSwipeProgressThreshold = context.getResources().getDimension(C1894R.dimen.navigation_edge_action_progress_threshold);
        initializeBackAnimation();
        setVisibility(8);
        Executor executor = (Executor) Dependency.get(Dependency.BACKGROUND_EXECUTOR);
        z = this.mContext.getDisplayId() == 0 ? true : z;
        RegionSamplingHelper regionSamplingHelper = new RegionSamplingHelper(this, new RegionSamplingHelper.SamplingCallback() {
            public void onRegionDarknessChanged(boolean z) {
                NavigationBarEdgePanel.this.setIsDark(!z, true);
            }

            public Rect getSampledRegion(View view) {
                return NavigationBarEdgePanel.this.mSamplingRect;
            }

            public boolean isSamplingEnabled() {
                return z;
            }
        }, executor);
        this.mRegionSamplingHelper = regionSamplingHelper;
        regionSamplingHelper.setWindowVisible(true);
        this.mShowProtection = !z;
        this.mLatencyTracker = latencyTracker;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-navigationbar-gestural-NavigationBarEdgePanel */
    public /* synthetic */ void mo35078x6e8f2d95(ValueAnimator valueAnimator) {
        setCurrentArrowColor(ColorUtils.blendARGB(this.mArrowStartColor, this.mArrowColor, valueAnimator.getAnimatedFraction()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-navigationbar-gestural-NavigationBarEdgePanel */
    public /* synthetic */ void mo35079xafd29f4(ValueAnimator valueAnimator) {
        this.mDisappearAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void setBackAnimation(BackAnimation backAnimation) {
        this.mBackAnimation = backAnimation;
        initializeBackAnimation();
    }

    private void initializeBackAnimation() {
        BackAnimation backAnimation = this.mBackAnimation;
        if (backAnimation != null) {
            backAnimation.setSwipeThresholds(this.mSwipeTriggerThreshold, this.mSwipeProgressThreshold);
        }
    }

    public void onDestroy() {
        cancelFailsafe();
        this.mWindowManager.removeView(this);
        this.mRegionSamplingHelper.stop();
        this.mRegionSamplingHelper = null;
    }

    /* access modifiers changed from: private */
    public void setIsDark(boolean z, boolean z2) {
        this.mIsDark = z;
        updateIsDark(z2);
    }

    public void setIsLeftPanel(boolean z) {
        this.mIsLeftPanel = z;
        this.mLayoutParams.gravity = z ? 51 : 53;
    }

    public void setInsets(int i, int i2) {
        this.mLeftInset = i;
        this.mRightInset = i2;
    }

    public void setDisplaySize(Point point) {
        this.mDisplaySize.set(point.x, point.y);
        this.mScreenSize = Math.min(this.mDisplaySize.x, this.mDisplaySize.y);
    }

    public void setBackCallback(NavigationEdgeBackPlugin.BackCallback backCallback) {
        this.mBackCallback = backCallback;
    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mLayoutParams = layoutParams;
        this.mWindowManager.addView(this, layoutParams);
    }

    private void adjustSamplingRectToBoundingBox() {
        float f = this.mDesiredTranslation;
        if (!this.mTriggerBack) {
            f = this.mBaseTranslation;
            boolean z = this.mIsLeftPanel;
            if ((z && this.mArrowsPointLeft) || (!z && !this.mArrowsPointLeft)) {
                f -= getStaticArrowWidth();
            }
        }
        float f2 = f - (this.mArrowThickness / 2.0f);
        if (!this.mIsLeftPanel) {
            f2 = ((float) this.mSamplingRect.width()) - f2;
        }
        float staticArrowWidth = getStaticArrowWidth();
        float polarToCartY = polarToCartY(56.0f) * this.mArrowLength * 2.0f;
        if (!this.mArrowsPointLeft) {
            f2 -= staticArrowWidth;
        }
        this.mSamplingRect.offset((int) f2, (int) (((((float) getHeight()) * 0.5f) + this.mDesiredVerticalTranslation) - (polarToCartY / 2.0f)));
        Rect rect = this.mSamplingRect;
        rect.set(rect.left, this.mSamplingRect.top, (int) (((float) this.mSamplingRect.left) + staticArrowWidth), (int) (((float) this.mSamplingRect.top) + polarToCartY));
        this.mRegionSamplingHelper.updateSamplingRect();
    }

    public void onMotionEvent(MotionEvent motionEvent) {
        BackAnimation backAnimation = this.mBackAnimation;
        if (backAnimation != null && !this.mTriggerBackSilently) {
            backAnimation.onBackMotion(motionEvent.getX(), motionEvent.getY(), motionEvent.getActionMasked(), this.mIsLeftPanel ^ true ? 1 : 0);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            int i = 0;
            this.mDragSlopPassed = false;
            resetOnDown();
            this.mStartX = motionEvent.getX();
            this.mStartY = motionEvent.getY();
            if (this.mTriggerBackSilently) {
                i = 4;
            }
            setVisibility(i);
            updatePosition(motionEvent.getY());
            this.mRegionSamplingHelper.start(this.mSamplingRect);
            this.mWindowManager.updateViewLayout(this, this.mLayoutParams);
            this.mLatencyTracker.onActionStart(15);
            this.mTrackingBackArrowLatency = true;
        } else if (actionMasked == 1) {
            if (this.mTriggerBack) {
                triggerBack();
            } else {
                cancelBack();
            }
            this.mRegionSamplingHelper.stop();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        } else if (actionMasked == 2) {
            handleMoveEvent(motionEvent);
        } else if (actionMasked == 3) {
            cancelBack();
            this.mRegionSamplingHelper.stop();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateArrowDirection();
        loadDimens();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f = this.mCurrentTranslation - (this.mArrowThickness / 2.0f);
        canvas.save();
        if (!this.mIsLeftPanel) {
            f = ((float) getWidth()) - f;
        }
        canvas.translate(f, (((float) getHeight()) * 0.5f) + this.mVerticalTranslation);
        Path calculatePath = calculatePath(polarToCartX(this.mCurrentAngle) * this.mArrowLength, polarToCartY(this.mCurrentAngle) * this.mArrowLength);
        if (this.mShowProtection) {
            canvas.drawPath(calculatePath, this.mProtectionPaint);
        }
        canvas.drawPath(calculatePath, this.mPaint);
        canvas.restore();
        if (this.mTrackingBackArrowLatency) {
            this.mLatencyTracker.onActionEnd(15);
            this.mTrackingBackArrowLatency = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mMaxTranslation = (float) (getWidth() - this.mArrowPaddingEnd);
    }

    private void loadDimens() {
        Resources resources = getResources();
        this.mArrowPaddingEnd = resources.getDimensionPixelSize(C1894R.dimen.navigation_edge_panel_padding);
        this.mMinArrowPosition = resources.getDimensionPixelSize(C1894R.dimen.navigation_edge_arrow_min_y);
        this.mFingerOffset = resources.getDimensionPixelSize(C1894R.dimen.navigation_edge_finger_offset);
    }

    private void updateArrowDirection() {
        this.mArrowsPointLeft = getLayoutDirection() == 0;
        invalidate();
    }

    private void loadColors(Context context) {
        int themeAttr = Utils.getThemeAttr(context, C1894R.attr.darkIconTheme);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, C1894R.attr.lightIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, themeAttr);
        this.mArrowColorLight = Utils.getColorAttrDefaultColor(contextThemeWrapper, C1894R.attr.singleToneColor);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, C1894R.attr.singleToneColor);
        this.mArrowColorDark = colorAttrDefaultColor;
        this.mProtectionColorDark = this.mArrowColorLight;
        this.mProtectionColorLight = colorAttrDefaultColor;
        updateIsDark(false);
    }

    private void updateIsDark(boolean z) {
        int i = this.mIsDark ? this.mProtectionColorDark : this.mProtectionColorLight;
        this.mProtectionColor = i;
        this.mProtectionPaint.setColor(i);
        this.mArrowColor = this.mIsDark ? this.mArrowColorDark : this.mArrowColorLight;
        this.mArrowColorAnimator.cancel();
        if (!z) {
            setCurrentArrowColor(this.mArrowColor);
            return;
        }
        this.mArrowStartColor = this.mCurrentArrowColor;
        this.mArrowColorAnimator.start();
    }

    private void setCurrentArrowColor(int i) {
        this.mCurrentArrowColor = i;
        this.mPaint.setColor(i);
        invalidate();
    }

    private float getStaticArrowWidth() {
        return polarToCartX(56.0f) * this.mArrowLength;
    }

    private float polarToCartX(float f) {
        return (float) Math.cos(Math.toRadians((double) f));
    }

    private float polarToCartY(float f) {
        return (float) Math.sin(Math.toRadians((double) f));
    }

    private Path calculatePath(float f, float f2) {
        if (!this.mArrowsPointLeft) {
            f = -f;
        }
        float lerp = MathUtils.lerp(1.0f, 0.75f, this.mDisappearAmount);
        float f3 = f * lerp;
        float f4 = f2 * lerp;
        this.mArrowPath.reset();
        this.mArrowPath.moveTo(f3, f4);
        this.mArrowPath.lineTo(0.0f, 0.0f);
        this.mArrowPath.lineTo(f3, -f4);
        return this.mArrowPath;
    }

    /* access modifiers changed from: private */
    public float getCurrentAngle() {
        return this.mCurrentAngle;
    }

    /* access modifiers changed from: private */
    public float getCurrentTranslation() {
        return this.mCurrentTranslation;
    }

    private void triggerBack() {
        this.mBackCallback.triggerBack();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.computeCurrentVelocity(1000);
        Math.abs(this.mVelocityTracker.getXVelocity());
        float f = this.mAngleOffset;
        if (f > -4.0f) {
            this.mAngleOffset = Math.max(-8.0f, f - 8.0f);
            updateAngle(true);
        }
        final NavigationBarEdgePanel$$ExternalSyntheticLambda1 navigationBarEdgePanel$$ExternalSyntheticLambda1 = new NavigationBarEdgePanel$$ExternalSyntheticLambda1(this);
        if (this.mTranslationAnimation.isRunning()) {
            this.mTranslationAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
                public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    dynamicAnimation.removeEndListener(this);
                    if (!z) {
                        navigationBarEdgePanel$$ExternalSyntheticLambda1.run();
                    }
                }
            });
            scheduleFailsafe();
            return;
        }
        navigationBarEdgePanel$$ExternalSyntheticLambda1.run();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$triggerBack$3$com-android-systemui-navigationbar-gestural-NavigationBarEdgePanel */
    public /* synthetic */ void mo35081xd9d95ff3() {
        this.mAngleOffset = Math.max(0.0f, this.mAngleOffset + 8.0f);
        updateAngle(true);
        this.mTranslationAnimation.setSpring(this.mTriggerBackSpring);
        setDesiredTranslation(this.mDesiredTranslation - m526dp(32.0f), true);
        animate().alpha(0.0f).setDuration(DISAPPEAR_FADE_ANIMATION_DURATION_MS).withEndAction(new NavigationBarEdgePanel$$ExternalSyntheticLambda0(this));
        this.mArrowDisappearAnimation.start();
        scheduleFailsafe();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$triggerBack$2$com-android-systemui-navigationbar-gestural-NavigationBarEdgePanel */
    public /* synthetic */ void mo35080x3d6b6394() {
        setVisibility(8);
    }

    private void cancelBack() {
        this.mBackCallback.cancelBack();
        if (this.mTranslationAnimation.isRunning()) {
            this.mTranslationAnimation.addEndListener(this.mSetGoneEndListener);
            scheduleFailsafe();
            return;
        }
        setVisibility(8);
    }

    private void resetOnDown() {
        animate().cancel();
        this.mAngleAnimation.cancel();
        this.mTranslationAnimation.cancel();
        this.mVerticalTranslationAnimation.cancel();
        this.mArrowDisappearAnimation.cancel();
        this.mAngleOffset = 0.0f;
        this.mTranslationAnimation.setSpring(this.mRegularTranslationSpring);
        setTriggerBack(false, false);
        setDesiredTranslation(0.0f, false);
        setCurrentTranslation(0.0f);
        updateAngle(false);
        this.mPreviousTouchTranslation = 0.0f;
        this.mTotalTouchDelta = 0.0f;
        this.mVibrationTime = 0;
        setDesiredVerticalTransition(0.0f, false);
        cancelFailsafe();
    }

    private void handleMoveEvent(MotionEvent motionEvent) {
        float f;
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float abs = MathUtils.abs(x - this.mStartX);
        float f2 = y - this.mStartY;
        float f3 = abs - this.mPreviousTouchTranslation;
        if (Math.abs(f3) > 0.0f) {
            if (Math.signum(f3) == Math.signum(this.mTotalTouchDelta)) {
                this.mTotalTouchDelta += f3;
            } else {
                this.mTotalTouchDelta = f3;
            }
        }
        this.mPreviousTouchTranslation = abs;
        if (!this.mDragSlopPassed && abs > this.mSwipeTriggerThreshold) {
            this.mDragSlopPassed = true;
            this.mVibratorHelper.vibrate(2);
            this.mVibrationTime = SystemClock.uptimeMillis();
            this.mDisappearAmount = 0.0f;
            setAlpha(1.0f);
            setTriggerBack(true, true);
        }
        float f4 = this.mBaseTranslation;
        if (abs > f4) {
            float interpolation = RUBBER_BAND_INTERPOLATOR.getInterpolation(MathUtils.saturate((abs - f4) / (((float) this.mScreenSize) - f4)));
            float f5 = this.mMaxTranslation;
            float f6 = this.mBaseTranslation;
            f = f6 + (interpolation * (f5 - f6));
        } else {
            float interpolation2 = RUBBER_BAND_INTERPOLATOR_APPEAR.getInterpolation(MathUtils.saturate((f4 - abs) / f4));
            float f7 = this.mBaseTranslation;
            f = f7 - (interpolation2 * (f7 / 4.0f));
        }
        boolean z = this.mTriggerBack;
        boolean z2 = false;
        if (Math.abs(this.mTotalTouchDelta) > this.mMinDeltaForSwitch) {
            z = this.mTotalTouchDelta > 0.0f;
        }
        this.mVelocityTracker.computeCurrentVelocity(1000);
        float xVelocity = this.mVelocityTracker.getXVelocity();
        float min = Math.min((MathUtils.mag(xVelocity, this.mVelocityTracker.getYVelocity()) / 1000.0f) * 4.0f, 4.0f) * Math.signum(xVelocity);
        this.mAngleOffset = min;
        boolean z3 = this.mIsLeftPanel;
        if ((z3 && this.mArrowsPointLeft) || (!z3 && !this.mArrowsPointLeft)) {
            this.mAngleOffset = min * -1.0f;
        }
        if (Math.abs(f2) <= Math.abs(x - this.mStartX) * 2.0f) {
            z2 = z;
        }
        setTriggerBack(z2, true);
        if (!this.mTriggerBack) {
            f = 0.0f;
        } else {
            boolean z4 = this.mIsLeftPanel;
            if ((z4 && this.mArrowsPointLeft) || (!z4 && !this.mArrowsPointLeft)) {
                f -= getStaticArrowWidth();
            }
        }
        setDesiredTranslation(f, true);
        updateAngle(true);
        float height = (((float) getHeight()) / 2.0f) - this.mArrowLength;
        setDesiredVerticalTransition(RUBBER_BAND_INTERPOLATOR.getInterpolation(MathUtils.constrain(Math.abs(f2) / (15.0f * height), 0.0f, 1.0f)) * height * Math.signum(f2), true);
        updateSamplingRect();
    }

    private void updatePosition(float f) {
        float max = Math.max(f - ((float) this.mFingerOffset), (float) this.mMinArrowPosition) - (((float) this.mLayoutParams.height) / 2.0f);
        this.mLayoutParams.y = MathUtils.constrain((int) max, 0, this.mDisplaySize.y);
        updateSamplingRect();
    }

    private void updateSamplingRect() {
        int i = this.mLayoutParams.y;
        int i2 = this.mIsLeftPanel ? this.mLeftInset : (this.mDisplaySize.x - this.mRightInset) - this.mLayoutParams.width;
        this.mSamplingRect.set(i2, i, this.mLayoutParams.width + i2, this.mLayoutParams.height + i);
        adjustSamplingRectToBoundingBox();
    }

    private void setDesiredVerticalTransition(float f, boolean z) {
        if (this.mDesiredVerticalTranslation != f) {
            this.mDesiredVerticalTranslation = f;
            if (!z) {
                setVerticalTranslation(f);
            } else {
                this.mVerticalTranslationAnimation.animateToFinalPosition(f);
            }
            invalidate();
        }
    }

    /* access modifiers changed from: private */
    public void setVerticalTranslation(float f) {
        this.mVerticalTranslation = f;
        invalidate();
    }

    /* access modifiers changed from: private */
    public float getVerticalTranslation() {
        return this.mVerticalTranslation;
    }

    private void setDesiredTranslation(float f, boolean z) {
        if (this.mDesiredTranslation != f) {
            this.mDesiredTranslation = f;
            if (!z) {
                setCurrentTranslation(f);
            } else {
                this.mTranslationAnimation.animateToFinalPosition(f);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setCurrentTranslation(float f) {
        this.mCurrentTranslation = f;
        invalidate();
    }

    private void setTriggerBack(boolean z, boolean z2) {
        if (this.mTriggerBack != z) {
            this.mTriggerBack = z;
            this.mAngleAnimation.cancel();
            updateAngle(z2);
            this.mTranslationAnimation.cancel();
            BackAnimation backAnimation = this.mBackAnimation;
            if (backAnimation != null) {
                backAnimation.setTriggerBack(z);
            }
        }
    }

    private void updateAngle(boolean z) {
        boolean z2 = this.mTriggerBack;
        float f = z2 ? this.mAngleOffset + 56.0f : 90.0f;
        if (f != this.mDesiredAngle) {
            if (!z) {
                setCurrentAngle(f);
            } else {
                this.mAngleAnimation.setSpring(z2 ? this.mAngleAppearForce : this.mAngleDisappearForce);
                this.mAngleAnimation.animateToFinalPosition(f);
            }
            this.mDesiredAngle = f;
        }
    }

    /* access modifiers changed from: private */
    public void setCurrentAngle(float f) {
        this.mCurrentAngle = f;
        invalidate();
    }

    private void scheduleFailsafe() {
        cancelFailsafe();
        this.mHandler.postDelayed(this.mFailsafeRunnable, 200);
    }

    private void cancelFailsafe() {
        this.mHandler.removeCallbacks(this.mFailsafeRunnable);
    }

    /* access modifiers changed from: private */
    public void onFailsafe() {
        setVisibility(8);
    }

    /* renamed from: dp */
    private float m526dp(float f) {
        return this.mDensity * f;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBarEdgePanel:");
        printWriter.println("  mIsLeftPanel=" + this.mIsLeftPanel);
        printWriter.println("  mTriggerBack=" + this.mTriggerBack);
        printWriter.println("  mDragSlopPassed=" + this.mDragSlopPassed);
        printWriter.println("  mCurrentAngle=" + this.mCurrentAngle);
        printWriter.println("  mDesiredAngle=" + this.mDesiredAngle);
        printWriter.println("  mCurrentTranslation=" + this.mCurrentTranslation);
        printWriter.println("  mDesiredTranslation=" + this.mDesiredTranslation);
        printWriter.println("  mTranslationAnimation running=" + this.mTranslationAnimation.isRunning());
        this.mRegionSamplingHelper.dump(printWriter);
    }

    public void setTriggerBackSilently(boolean z) {
        this.mTriggerBackSilently = z;
    }
}
