package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.biometrics.UdfpsEnrollDrawable;
/* loaded from: classes.dex */
public class UdfpsEnrollDrawable extends UdfpsDrawable {
    private final Paint mBlueFill;
    float mCurrentX;
    float mCurrentY;
    private AnimatorSet mEdgeHintAnimatorSet;
    private ValueAnimator mEdgeHintColorAnimator;
    private final Paint mEdgeHintPaint;
    private ValueAnimator mEdgeHintWidthAnimator;
    private UdfpsEnrollHelper mEnrollHelper;
    private final int mHintColorFaded;
    private final int mHintColorHighlight;
    private final float mHintMaxWidthPx;
    private final float mHintPaddingPx;
    private final Drawable mMovingTargetFpIcon;
    private final Paint mSensorOutlinePaint;
    private RectF mSensorRect;
    AnimatorSet mTargetAnimatorSet;
    private AnimatorSet mTipHintAnimatorSet;
    private ValueAnimator mTipHintColorAnimator;
    private final Paint mTipHintPaint;
    private ValueAnimator mTipHintWidthAnimator;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    float mCurrentScale = 1.0f;
    private boolean mShouldShowTipHint = false;
    private boolean mShouldShowEdgeHint = false;
    private final Animator.AnimatorListener mTargetAnimListener = new Animator.AnimatorListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable.1
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            UdfpsEnrollDrawable.this.updateTipHintVisibility();
        }
    };
    private final ValueAnimator.AnimatorUpdateListener mTipHintColorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda2
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollDrawable.this.lambda$new$0(valueAnimator);
        }
    };
    private final ValueAnimator.AnimatorUpdateListener mTipHintWidthUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda3
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollDrawable.this.lambda$new$1(valueAnimator);
        }
    };
    private final Animator.AnimatorListener mTipHintPulseListener = new AnonymousClass2();
    private final ValueAnimator.AnimatorUpdateListener mEdgeHintColorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda0
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollDrawable.this.lambda$new$2(valueAnimator);
        }
    };
    private final ValueAnimator.AnimatorUpdateListener mEdgeHintWidthUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollDrawable.this.lambda$new$3(valueAnimator);
        }
    };
    private final Animator.AnimatorListener mEdgeHintPulseListener = new AnonymousClass3();

    /* JADX INFO: Access modifiers changed from: package-private */
    public UdfpsEnrollDrawable(Context context) {
        super(context);
        Paint paint = new Paint(0);
        this.mSensorOutlinePaint = paint;
        paint.setAntiAlias(true);
        Context context2 = this.mContext;
        int i = R$color.udfps_enroll_icon;
        paint.setColor(context2.getColor(i));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        Paint paint2 = new Paint(0);
        this.mBlueFill = paint2;
        paint2.setAntiAlias(true);
        paint2.setColor(context.getColor(R$color.udfps_moving_target_fill));
        paint2.setStyle(Paint.Style.FILL);
        Drawable drawable = context.getResources().getDrawable(R$drawable.ic_fingerprint, null);
        this.mMovingTargetFpIcon = drawable;
        drawable.setTint(-1);
        drawable.mutate();
        this.mFingerprintDrawable.setTint(this.mContext.getColor(i));
        int hintColorFaded = getHintColorFaded(context);
        this.mHintColorFaded = hintColorFaded;
        this.mHintColorHighlight = context.getColor(R$color.udfps_enroll_progress);
        this.mHintMaxWidthPx = Utils.dpToPixels(context, 6.0f);
        this.mHintPaddingPx = Utils.dpToPixels(context, 10.0f);
        Paint paint3 = new Paint(0);
        this.mTipHintPaint = paint3;
        paint3.setAntiAlias(true);
        paint3.setColor(hintColorFaded);
        paint3.setStyle(Paint.Style.STROKE);
        paint3.setStrokeCap(Paint.Cap.ROUND);
        paint3.setStrokeWidth(0.0f);
        Paint paint4 = new Paint(0);
        this.mEdgeHintPaint = paint4;
        paint4.setAntiAlias(true);
        paint4.setColor(hintColorFaded);
        paint4.setStyle(Paint.Style.STROKE);
        paint4.setStrokeCap(Paint.Cap.ROUND);
        paint4.setStrokeWidth(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mTipHintPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.mTipHintPaint.setStrokeWidth(((Float) valueAnimator.getAnimatedValue()).floatValue());
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.biometrics.UdfpsEnrollDrawable$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        AnonymousClass2() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            UdfpsEnrollDrawable.this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsEnrollDrawable.AnonymousClass2.this.lambda$onAnimationEnd$0();
                }
            }, 233L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            UdfpsEnrollDrawable udfpsEnrollDrawable = UdfpsEnrollDrawable.this;
            udfpsEnrollDrawable.mTipHintColorAnimator = ValueAnimator.ofArgb(udfpsEnrollDrawable.mTipHintPaint.getColor(), UdfpsEnrollDrawable.this.mHintColorFaded);
            UdfpsEnrollDrawable.this.mTipHintColorAnimator.setInterpolator(new LinearInterpolator());
            UdfpsEnrollDrawable.this.mTipHintColorAnimator.setDuration(517L);
            UdfpsEnrollDrawable.this.mTipHintColorAnimator.addUpdateListener(UdfpsEnrollDrawable.this.mTipHintColorUpdateListener);
            UdfpsEnrollDrawable.this.mTipHintColorAnimator.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(ValueAnimator valueAnimator) {
        this.mEdgeHintPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(ValueAnimator valueAnimator) {
        this.mEdgeHintPaint.setStrokeWidth(((Float) valueAnimator.getAnimatedValue()).floatValue());
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.biometrics.UdfpsEnrollDrawable$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements Animator.AnimatorListener {
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        AnonymousClass3() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            UdfpsEnrollDrawable.this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    UdfpsEnrollDrawable.AnonymousClass3.this.lambda$onAnimationEnd$0();
                }
            }, 233L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0() {
            UdfpsEnrollDrawable udfpsEnrollDrawable = UdfpsEnrollDrawable.this;
            udfpsEnrollDrawable.mEdgeHintColorAnimator = ValueAnimator.ofArgb(udfpsEnrollDrawable.mEdgeHintPaint.getColor(), UdfpsEnrollDrawable.this.mHintColorFaded);
            UdfpsEnrollDrawable.this.mEdgeHintColorAnimator.setInterpolator(new LinearInterpolator());
            UdfpsEnrollDrawable.this.mEdgeHintColorAnimator.setDuration(517L);
            UdfpsEnrollDrawable.this.mEdgeHintColorAnimator.addUpdateListener(UdfpsEnrollDrawable.this.mEdgeHintColorUpdateListener);
            UdfpsEnrollDrawable.this.mEdgeHintColorAnimator.start();
        }
    }

    private static int getHintColorFaded(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16842803, typedValue, true);
        int i = (int) (typedValue.getFloat() * 255.0f);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843817});
        try {
            return ColorUtils.setAlphaComponent(obtainStyledAttributes.getColor(0, context.getColor(R$color.white_disabled)), i);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mEnrollHelper = udfpsEnrollHelper;
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable
    public void onSensorRectUpdated(RectF rectF) {
        super.onSensorRectUpdated(rectF);
        this.mSensorRect = rectF;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.biometrics.UdfpsDrawable
    public void updateFingerprintIconBounds(Rect rect) {
        super.updateFingerprintIconBounds(rect);
        this.mMovingTargetFpIcon.setBounds(rect);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i, int i2) {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        if (udfpsEnrollHelper == null) {
            return;
        }
        if (!udfpsEnrollHelper.isCenterEnrollmentStage()) {
            AnimatorSet animatorSet = this.mTargetAnimatorSet;
            if (animatorSet != null && animatorSet.isRunning()) {
                this.mTargetAnimatorSet.end();
            }
            PointF nextGuidedEnrollmentPoint = this.mEnrollHelper.getNextGuidedEnrollmentPoint();
            float f = this.mCurrentX;
            float f2 = nextGuidedEnrollmentPoint.x;
            if (f != f2 || this.mCurrentY != nextGuidedEnrollmentPoint.y) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(f, f2);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda5
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        UdfpsEnrollDrawable.this.lambda$onEnrollmentProgress$4(valueAnimator);
                    }
                });
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.mCurrentY, nextGuidedEnrollmentPoint.y);
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda6
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        UdfpsEnrollDrawable.this.lambda$onEnrollmentProgress$5(valueAnimator);
                    }
                });
                long j = (nextGuidedEnrollmentPoint.x > 0.0f ? 1 : (nextGuidedEnrollmentPoint.x == 0.0f ? 0 : -1)) == 0 && (nextGuidedEnrollmentPoint.y > 0.0f ? 1 : (nextGuidedEnrollmentPoint.y == 0.0f ? 0 : -1)) == 0 ? 600L : 800L;
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 3.1415927f);
                ofFloat3.setDuration(j);
                ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollDrawable$$ExternalSyntheticLambda4
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        UdfpsEnrollDrawable.this.lambda$onEnrollmentProgress$6(valueAnimator);
                    }
                });
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.mTargetAnimatorSet = animatorSet2;
                animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
                this.mTargetAnimatorSet.setDuration(j);
                this.mTargetAnimatorSet.addListener(this.mTargetAnimListener);
                this.mTargetAnimatorSet.playTogether(ofFloat, ofFloat2, ofFloat3);
                this.mTargetAnimatorSet.start();
            } else {
                updateTipHintVisibility();
            }
        } else {
            updateTipHintVisibility();
        }
        updateEdgeHintVisibility();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$4(ValueAnimator valueAnimator) {
        this.mCurrentX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$5(ValueAnimator valueAnimator) {
        this.mCurrentY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$6(ValueAnimator valueAnimator) {
        this.mCurrentScale = (((float) Math.sin(((Float) valueAnimator.getAnimatedValue()).floatValue())) * 0.25f) + 1.0f;
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTipHintVisibility() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        boolean z = udfpsEnrollHelper != null && udfpsEnrollHelper.isTipEnrollmentStage();
        if (this.mShouldShowTipHint == z) {
            return;
        }
        this.mShouldShowTipHint = z;
        ValueAnimator valueAnimator = this.mTipHintWidthAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mTipHintWidthAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mTipHintPaint.getStrokeWidth(), z ? this.mHintMaxWidthPx : 0.0f);
        this.mTipHintWidthAnimator = ofFloat;
        ofFloat.setDuration(233L);
        this.mTipHintWidthAnimator.addUpdateListener(this.mTipHintWidthUpdateListener);
        if (z) {
            startTipHintPulseAnimation();
        } else {
            this.mTipHintWidthAnimator.start();
        }
    }

    private void updateEdgeHintVisibility() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        boolean z = udfpsEnrollHelper != null && udfpsEnrollHelper.isEdgeEnrollmentStage();
        if (this.mShouldShowEdgeHint == z) {
            return;
        }
        this.mShouldShowEdgeHint = z;
        ValueAnimator valueAnimator = this.mEdgeHintWidthAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mEdgeHintWidthAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mEdgeHintPaint.getStrokeWidth(), z ? this.mHintMaxWidthPx : 0.0f);
        this.mEdgeHintWidthAnimator = ofFloat;
        ofFloat.setDuration(233L);
        this.mEdgeHintWidthAnimator.addUpdateListener(this.mEdgeHintWidthUpdateListener);
        if (z) {
            startEdgeHintPulseAnimation();
        } else {
            this.mEdgeHintWidthAnimator.start();
        }
    }

    private void startTipHintPulseAnimation() {
        this.mHandler.removeCallbacksAndMessages(null);
        AnimatorSet animatorSet = this.mTipHintAnimatorSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mTipHintAnimatorSet.cancel();
        }
        ValueAnimator valueAnimator = this.mTipHintColorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mTipHintColorAnimator.cancel();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mTipHintPaint.getColor(), this.mHintColorHighlight);
        this.mTipHintColorAnimator = ofArgb;
        ofArgb.setDuration(233L);
        this.mTipHintColorAnimator.addUpdateListener(this.mTipHintColorUpdateListener);
        this.mTipHintColorAnimator.addListener(this.mTipHintPulseListener);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mTipHintAnimatorSet = animatorSet2;
        animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mTipHintAnimatorSet.playTogether(this.mTipHintColorAnimator, this.mTipHintWidthAnimator);
        this.mTipHintAnimatorSet.start();
    }

    private void startEdgeHintPulseAnimation() {
        this.mHandler.removeCallbacksAndMessages(null);
        AnimatorSet animatorSet = this.mEdgeHintAnimatorSet;
        if (animatorSet != null && animatorSet.isRunning()) {
            this.mEdgeHintAnimatorSet.cancel();
        }
        ValueAnimator valueAnimator = this.mEdgeHintColorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mEdgeHintColorAnimator.cancel();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mEdgeHintPaint.getColor(), this.mHintColorHighlight);
        this.mEdgeHintColorAnimator = ofArgb;
        ofArgb.setDuration(233L);
        this.mEdgeHintColorAnimator.addUpdateListener(this.mEdgeHintColorUpdateListener);
        this.mEdgeHintColorAnimator.addListener(this.mEdgeHintPulseListener);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mEdgeHintAnimatorSet = animatorSet2;
        animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
        this.mEdgeHintAnimatorSet.playTogether(this.mEdgeHintColorAnimator, this.mEdgeHintWidthAnimator);
        this.mEdgeHintAnimatorSet.start();
    }

    private boolean isTipHintVisible() {
        return this.mTipHintPaint.getStrokeWidth() > 0.0f;
    }

    private boolean isEdgeHintVisible() {
        return this.mEdgeHintPaint.getStrokeWidth() > 0.0f;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (isIlluminationShowing()) {
            return;
        }
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        if (udfpsEnrollHelper != null && !udfpsEnrollHelper.isCenterEnrollmentStage()) {
            canvas.save();
            canvas.translate(this.mCurrentX, this.mCurrentY);
            RectF rectF = this.mSensorRect;
            if (rectF != null) {
                float f = this.mCurrentScale;
                canvas.scale(f, f, rectF.centerX(), this.mSensorRect.centerY());
                canvas.drawOval(this.mSensorRect, this.mBlueFill);
            }
            this.mMovingTargetFpIcon.draw(canvas);
            canvas.restore();
        } else {
            RectF rectF2 = this.mSensorRect;
            if (rectF2 != null) {
                canvas.drawOval(rectF2, this.mSensorOutlinePaint);
            }
            this.mFingerprintDrawable.draw(canvas);
            this.mFingerprintDrawable.setAlpha(this.mAlpha);
            this.mSensorOutlinePaint.setAlpha(this.mAlpha);
        }
        if (!isTipHintVisible() && !isEdgeHintVisible()) {
            return;
        }
        canvas.save();
        canvas.rotate(-90.0f, this.mSensorRect.centerX(), this.mSensorRect.centerY());
        RectF rectF3 = this.mSensorRect;
        RectF rectF4 = this.mSensorRect;
        float f2 = this.mHintPaddingPx;
        float abs = (Math.abs(rectF4.right - rectF4.left) / 2.0f) + f2;
        float abs2 = (Math.abs(rectF3.bottom - rectF3.top) / 2.0f) + f2;
        if (isTipHintVisible()) {
            canvas.drawArc(this.mSensorRect.centerX() - abs, this.mSensorRect.centerY() - abs2, this.mSensorRect.centerX() + abs, this.mSensorRect.centerY() + abs2, -20.0f, 40.0f, false, this.mTipHintPaint);
        }
        if (isEdgeHintVisible()) {
            canvas.rotate(-90.0f, this.mSensorRect.centerX(), this.mSensorRect.centerY());
            canvas.drawArc(this.mSensorRect.centerX() - abs, this.mSensorRect.centerY() - abs2, this.mSensorRect.centerX() + abs, this.mSensorRect.centerY() + abs2, -20.0f, 40.0f, false, this.mEdgeHintPaint);
            canvas.rotate(180.0f, this.mSensorRect.centerX(), this.mSensorRect.centerY());
            canvas.drawArc(this.mSensorRect.centerX() - abs, this.mSensorRect.centerY() - abs2, this.mSensorRect.centerX() + abs, this.mSensorRect.centerY() + abs2, -20.0f, 40.0f, false, this.mEdgeHintPaint);
        }
        canvas.restore();
    }

    @Override // com.android.systemui.biometrics.UdfpsDrawable, android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mSensorOutlinePaint.setAlpha(i);
        this.mBlueFill.setAlpha(i);
        this.mMovingTargetFpIcon.setAlpha(i);
        invalidateSelf();
    }
}
