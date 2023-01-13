package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.FakeShadowView;
import com.android.systemui.statusbar.notification.NotificationUtils;

public abstract class ActivatableNotificationView extends ExpandableOutlineView {
    private static final Interpolator ACTIVATE_INVERSE_ALPHA_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.5f, 1.0f);
    private static final Interpolator ACTIVATE_INVERSE_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.5f, 1.0f);
    private static final float ALPHA_APPEAR_END_FRACTION = 1.0f;
    private static final float ALPHA_APPEAR_START_FRACTION = 0.4f;
    private static final float HORIZONTAL_ANIMATION_END = 0.2f;
    private static final float HORIZONTAL_ANIMATION_START = 1.0f;
    private static final float HORIZONTAL_COLLAPSED_REST_PARTIAL = 0.05f;
    protected static final int NO_COLOR = 0;
    private static final float VERTICAL_ANIMATION_START = 1.0f;
    private AccessibilityManager mAccessibilityManager;
    private boolean mActivated;
    private float mAnimationTranslationY;
    private float mAppearAnimationFraction = -1.0f;
    private float mAppearAnimationTranslation;
    private ValueAnimator mAppearAnimator;
    /* access modifiers changed from: private */
    public ValueAnimator mBackgroundColorAnimator;
    NotificationBackgroundView mBackgroundNormal;
    int mBgTint = 0;
    private Interpolator mCurrentAppearInterpolator;
    private int mCurrentBackgroundTint;
    private boolean mDismissed;
    private boolean mDrawingAppearAnimation;
    private FakeShadowView mFakeShadow;
    private boolean mIsBelowSpeedBump;
    private boolean mIsHeadsUpAnimation;
    private long mLastActionUpTime;
    private float mNormalBackgroundVisibilityAmount;
    private int mNormalColor;
    private int mNormalRippleColor;
    private OnActivatedListener mOnActivatedListener;
    private float mOverrideAmount;
    private int mOverrideTint;
    private boolean mRefocusOnDismiss;
    private boolean mShadowHidden;
    private final Interpolator mSlowOutFastInInterpolator = new PathInterpolator(0.8f, 0.0f, 0.6f, 1.0f);
    private final Interpolator mSlowOutLinearInInterpolator = new PathInterpolator(0.8f, 0.0f, 1.0f, 1.0f);
    private int mStartTint;
    protected Point mTargetPoint;
    private int mTargetTint;
    private int mTintedRippleColor;
    private Gefingerpoken mTouchHandler;

    public interface OnActivatedListener {
        void onActivated(ActivatableNotificationView activatableNotificationView);

        void onActivationReset(ActivatableNotificationView activatableNotificationView);
    }

    /* access modifiers changed from: protected */
    public boolean disallowSingleClick(MotionEvent motionEvent) {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract View getContentView();

    /* access modifiers changed from: protected */
    public boolean handleSlideBack() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean hideBackground() {
        return false;
    }

    public boolean isHeadsUp() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean isInteractive() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onAppearAnimationFinished(boolean z) {
    }

    /* access modifiers changed from: protected */
    public void onBelowSpeedBumpChanged() {
    }

    public void onTap() {
    }

    /* access modifiers changed from: protected */
    public void resetAllContentAlphas() {
    }

    public ActivatableNotificationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setClipChildren(false);
        setClipToPadding(false);
        updateColors();
    }

    private void updateColors() {
        this.mNormalColor = Utils.getColorAttrDefaultColor(this.mContext, 17956909);
        this.mTintedRippleColor = this.mContext.getColor(C1894R.C1895color.notification_ripple_tinted_color);
        this.mNormalRippleColor = this.mContext.getColor(C1894R.C1895color.notification_ripple_untinted_color);
    }

    public void updateBackgroundColors() {
        updateColors();
        initBackground();
        updateBackgroundTint();
    }

    public void setBackgroundWidth(int i) {
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        if (notificationBackgroundView != null) {
            notificationBackgroundView.setActualWidth(i);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBackgroundNormal = (NotificationBackgroundView) findViewById(C1894R.C1898id.backgroundNormal);
        FakeShadowView fakeShadowView = (FakeShadowView) findViewById(C1894R.C1898id.fake_shadow);
        this.mFakeShadow = fakeShadowView;
        this.mShadowHidden = fakeShadowView.getVisibility() != 0;
        initBackground();
        updateBackgroundTint();
        updateOutlineAlpha();
    }

    /* access modifiers changed from: protected */
    public void initBackground() {
        this.mBackgroundNormal.setCustomBackground((int) C1894R.C1896drawable.notification_material_bg);
    }

    /* access modifiers changed from: protected */
    public void updateBackground() {
        this.mBackgroundNormal.setVisibility(hideBackground() ? 4 : 0);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mTouchHandler;
        if (gefingerpoken == null || !gefingerpoken.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void setLastActionUpTime(long j) {
        this.mLastActionUpTime = j;
    }

    public long getAndResetLastActionUpTime() {
        long j = this.mLastActionUpTime;
        this.mLastActionUpTime = 0;
        return j;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.mBackgroundNormal.setState(getDrawableState());
    }

    /* access modifiers changed from: package-private */
    public void setRippleAllowed(boolean z) {
        this.mBackgroundNormal.setPressedAllowed(z);
    }

    /* access modifiers changed from: package-private */
    public void makeActive() {
        this.mActivated = true;
        OnActivatedListener onActivatedListener = this.mOnActivatedListener;
        if (onActivatedListener != null) {
            onActivatedListener.onActivated(this);
        }
    }

    public boolean isActive() {
        return this.mActivated;
    }

    public void makeInactive(boolean z) {
        if (this.mActivated) {
            this.mActivated = false;
        }
        OnActivatedListener onActivatedListener = this.mOnActivatedListener;
        if (onActivatedListener != null) {
            onActivatedListener.onActivationReset(this);
        }
    }

    private void updateOutlineAlpha() {
        setOutlineAlpha((0.3f * this.mNormalBackgroundVisibilityAmount) + 0.7f);
    }

    private void setNormalBackgroundVisibilityAmount(float f) {
        this.mNormalBackgroundVisibilityAmount = f;
        updateOutlineAlpha();
    }

    public void setBelowSpeedBump(boolean z) {
        super.setBelowSpeedBump(z);
        if (z != this.mIsBelowSpeedBump) {
            this.mIsBelowSpeedBump = z;
            updateBackgroundTint();
            onBelowSpeedBumpChanged();
        }
    }

    public boolean isBelowSpeedBump() {
        return this.mIsBelowSpeedBump;
    }

    /* access modifiers changed from: protected */
    public void setTintColor(int i) {
        setTintColor(i, false);
    }

    /* access modifiers changed from: package-private */
    public void setTintColor(int i, boolean z) {
        if (i != this.mBgTint) {
            this.mBgTint = i;
            updateBackgroundTint(z);
        }
    }

    public void setOverrideTintColor(int i, float f) {
        this.mOverrideTint = i;
        this.mOverrideAmount = f;
        setBackgroundTintColor(calculateBgColor());
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundTint() {
        updateBackgroundTint(false);
    }

    private void updateBackgroundTint(boolean z) {
        ValueAnimator valueAnimator = this.mBackgroundColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mBackgroundNormal.setRippleColor(getRippleColor());
        int calculateBgColor = calculateBgColor();
        if (!z) {
            setBackgroundTintColor(calculateBgColor);
            return;
        }
        int i = this.mCurrentBackgroundTint;
        if (calculateBgColor != i) {
            this.mStartTint = i;
            this.mTargetTint = calculateBgColor;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mBackgroundColorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ActivatableNotificationView$$ExternalSyntheticLambda0(this));
            this.mBackgroundColorAnimator.setDuration(360);
            this.mBackgroundColorAnimator.setInterpolator(Interpolators.LINEAR);
            this.mBackgroundColorAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    ValueAnimator unused = ActivatableNotificationView.this.mBackgroundColorAnimator = null;
                }
            });
            this.mBackgroundColorAnimator.start();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateBackgroundTint$0$com-android-systemui-statusbar-notification-row-ActivatableNotificationView */
    public /* synthetic */ void mo40901x14e5c68a(ValueAnimator valueAnimator) {
        setBackgroundTintColor(NotificationUtils.interpolateColors(this.mStartTint, this.mTargetTint, valueAnimator.getAnimatedFraction()));
    }

    /* access modifiers changed from: protected */
    public void setBackgroundTintColor(int i) {
        if (i != this.mCurrentBackgroundTint) {
            this.mCurrentBackgroundTint = i;
            if (i == this.mNormalColor) {
                i = 0;
            }
            this.mBackgroundNormal.setTint(i);
        }
    }

    /* access modifiers changed from: protected */
    public void updateBackgroundClipping() {
        this.mBackgroundNormal.setBottomAmountClips(!isChildInGroup());
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setPivotX((float) (getWidth() / 2));
    }

    public void setActualHeight(int i, boolean z) {
        super.setActualHeight(i, z);
        setPivotY((float) (i / 2));
        this.mBackgroundNormal.setActualHeight(i);
    }

    public void setClipTopAmount(int i) {
        super.setClipTopAmount(i);
        this.mBackgroundNormal.setClipTopAmount(i);
    }

    public void setClipBottomAmount(int i) {
        super.setClipBottomAmount(i);
        this.mBackgroundNormal.setClipBottomAmount(i);
    }

    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(false, f, j2, j, runnable, animatorListenerAdapter);
            return 0;
        } else if (runnable == null) {
            return 0;
        } else {
            runnable.run();
            return 0;
        }
    }

    public void performAddAnimation(long j, long j2, boolean z, Runnable runnable) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(true, z ? 0.0f : -1.0f, j, j2, (Runnable) null, (AnimatorListenerAdapter) null);
        }
    }

    private void startAppearAnimation(final boolean z, float f, long j, long j2, final Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        this.mAnimationTranslationY = f * ((float) getActualHeight());
        cancelAppearAnimation();
        float f2 = 1.0f;
        if (this.mAppearAnimationFraction == -1.0f) {
            if (z) {
                this.mAppearAnimationFraction = 0.0f;
                this.mAppearAnimationTranslation = this.mAnimationTranslationY;
            } else {
                this.mAppearAnimationFraction = 1.0f;
                this.mAppearAnimationTranslation = 0.0f;
            }
        }
        if (z) {
            this.mCurrentAppearInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        } else {
            this.mCurrentAppearInterpolator = this.mSlowOutFastInInterpolator;
            f2 = 0.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mAppearAnimationFraction, f2});
        this.mAppearAnimator = ofFloat;
        ofFloat.setInterpolator(Interpolators.LINEAR);
        this.mAppearAnimator.setDuration((long) (((float) j2) * Math.abs(this.mAppearAnimationFraction - f2)));
        this.mAppearAnimator.addUpdateListener(new ActivatableNotificationView$$ExternalSyntheticLambda1(this));
        if (animatorListenerAdapter != null) {
            this.mAppearAnimator.addListener(animatorListenerAdapter);
        }
        if (j > 0) {
            updateAppearAnimationAlpha();
            updateAppearRect();
            this.mAppearAnimator.setStartDelay(j);
        }
        this.mAppearAnimator.addListener(new AnimatorListenerAdapter() {
            private boolean mWasCancelled;

            public void onAnimationEnd(Animator animator) {
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
                if (!this.mWasCancelled) {
                    ActivatableNotificationView.this.enableAppearDrawing(false);
                    ActivatableNotificationView.this.onAppearAnimationFinished(z);
                    InteractionJankMonitor.getInstance().end(ActivatableNotificationView.this.getCujType(z));
                    return;
                }
                InteractionJankMonitor.getInstance().cancel(ActivatableNotificationView.this.getCujType(z));
            }

            public void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                InteractionJankMonitor.getInstance().begin(InteractionJankMonitor.Configuration.Builder.withView(ActivatableNotificationView.this.getCujType(z), ActivatableNotificationView.this));
            }

            public void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }
        });
        this.mAppearAnimator.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startAppearAnimation$1$com-android-systemui-statusbar-notification-row-ActivatableNotificationView */
    public /* synthetic */ void mo40900x484fec06(ValueAnimator valueAnimator) {
        this.mAppearAnimationFraction = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateAppearAnimationAlpha();
        updateAppearRect();
        invalidate();
    }

    /* access modifiers changed from: private */
    public int getCujType(boolean z) {
        return this.mIsHeadsUpAnimation ? z ? 12 : 13 : z ? 14 : 15;
    }

    private void cancelAppearAnimation() {
        ValueAnimator valueAnimator = this.mAppearAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mAppearAnimator = null;
        }
    }

    public void cancelAppearDrawing() {
        cancelAppearAnimation();
        enableAppearDrawing(false);
    }

    private void updateAppearRect() {
        float interpolation = this.mCurrentAppearInterpolator.getInterpolation(this.mAppearAnimationFraction);
        this.mAppearAnimationTranslation = (1.0f - interpolation) * this.mAnimationTranslationY;
        int actualHeight = getActualHeight();
        float f = (float) actualHeight;
        float f2 = interpolation * f;
        if (this.mTargetPoint != null) {
            int width = getWidth();
            float f3 = 1.0f - this.mAppearAnimationFraction;
            float f4 = this.mAnimationTranslationY;
            setOutlineRect(((float) this.mTargetPoint.x) * f3, f4 + ((f4 - ((float) this.mTargetPoint.y)) * f3), ((float) width) - (((float) (width - this.mTargetPoint.x)) * f3), f - (((float) (actualHeight - this.mTargetPoint.y)) * f3));
            return;
        }
        setOutlineRect(0.0f, this.mAppearAnimationTranslation, (float) getWidth(), f2 + this.mAppearAnimationTranslation);
    }

    private float getInterpolatedAppearAnimationFraction() {
        float f = this.mAppearAnimationFraction;
        if (f >= 0.0f) {
            return this.mCurrentAppearInterpolator.getInterpolation(f);
        }
        return 1.0f;
    }

    private void updateAppearAnimationAlpha() {
        setContentAlpha(Interpolators.ALPHA_IN.getInterpolation((MathUtils.constrain(this.mAppearAnimationFraction, ALPHA_APPEAR_START_FRACTION, 1.0f) - ALPHA_APPEAR_START_FRACTION) / 0.6f));
    }

    private void setContentAlpha(float f) {
        View contentView = getContentView();
        if (contentView.hasOverlappingRendering()) {
            contentView.setLayerType((f == 0.0f || f == 1.0f) ? 0 : 2, (Paint) null);
        }
        contentView.setAlpha(f);
        if (f == 1.0f) {
            resetAllContentAlphas();
        }
    }

    /* access modifiers changed from: protected */
    public void applyRoundness() {
        super.applyRoundness();
        applyBackgroundRoundness(getCurrentBackgroundRadiusTop(), getCurrentBackgroundRadiusBottom());
    }

    public float getCurrentBackgroundRadiusTop() {
        return MathUtils.lerp(0.0f, super.getCurrentBackgroundRadiusTop(), getInterpolatedAppearAnimationFraction());
    }

    public float getCurrentBackgroundRadiusBottom() {
        return MathUtils.lerp(0.0f, super.getCurrentBackgroundRadiusBottom(), getInterpolatedAppearAnimationFraction());
    }

    private void applyBackgroundRoundness(float f, float f2) {
        this.mBackgroundNormal.setRadius(f, f2);
    }

    /* access modifiers changed from: protected */
    public void setBackgroundTop(int i) {
        this.mBackgroundNormal.setBackgroundTop(i);
    }

    public int calculateBgColor() {
        return calculateBgColor(true, true);
    }

    /* access modifiers changed from: protected */
    public boolean childNeedsClipping(View view) {
        if (!(view instanceof NotificationBackgroundView) || !isClippingNeeded()) {
            return super.childNeedsClipping(view);
        }
        return true;
    }

    private int calculateBgColor(boolean z, boolean z2) {
        int i;
        if (z2 && this.mOverrideTint != 0) {
            return NotificationUtils.interpolateColors(calculateBgColor(z, false), this.mOverrideTint, this.mOverrideAmount);
        }
        if (!z || (i = this.mBgTint) == 0) {
            return this.mNormalColor;
        }
        return i;
    }

    private int getRippleColor() {
        if (this.mBgTint != 0) {
            return this.mTintedRippleColor;
        }
        return this.mNormalRippleColor;
    }

    /* access modifiers changed from: private */
    public void enableAppearDrawing(boolean z) {
        if (z != this.mDrawingAppearAnimation) {
            this.mDrawingAppearAnimation = z;
            if (!z) {
                setContentAlpha(1.0f);
                this.mAppearAnimationFraction = -1.0f;
                setOutlineRect((RectF) null);
            }
            invalidate();
        }
    }

    public boolean isDrawingAppearAnimation() {
        return this.mDrawingAppearAnimation;
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawingAppearAnimation) {
            canvas.save();
            canvas.translate(0.0f, this.mAppearAnimationTranslation);
        }
        super.dispatchDraw(canvas);
        if (this.mDrawingAppearAnimation) {
            canvas.restore();
        }
    }

    public void setOnActivatedListener(OnActivatedListener onActivatedListener) {
        this.mOnActivatedListener = onActivatedListener;
    }

    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        boolean z = this.mShadowHidden;
        boolean z2 = f == 0.0f;
        this.mShadowHidden = z2;
        if (!z2 || !z) {
            this.mFakeShadow.setFakeShadowTranslationZ(f * (getTranslationZ() + 0.1f), f2, i, i2);
        }
    }

    public int getBackgroundColorWithoutTint() {
        return calculateBgColor(false, false);
    }

    public int getCurrentBackgroundTint() {
        return this.mCurrentBackgroundTint;
    }

    public int getHeadsUpHeightWithoutHeader() {
        return getHeight();
    }

    public void dismiss(boolean z) {
        this.mDismissed = true;
        this.mRefocusOnDismiss = z;
    }

    public void unDismiss() {
        this.mDismissed = false;
    }

    public boolean isDismissed() {
        return this.mDismissed;
    }

    public boolean shouldRefocusOnDismiss() {
        return this.mRefocusOnDismiss || isAccessibilityFocused();
    }

    /* access modifiers changed from: package-private */
    public void setTouchHandler(Gefingerpoken gefingerpoken) {
        this.mTouchHandler = gefingerpoken;
    }

    public void setAccessibilityManager(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }
}
