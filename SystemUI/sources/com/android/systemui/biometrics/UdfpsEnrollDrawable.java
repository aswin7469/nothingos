package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.android.systemui.C1894R;

public class UdfpsEnrollDrawable extends UdfpsDrawable {
    private static final float SCALE_MAX = 0.25f;
    private static final String TAG = "UdfpsAnimationEnroll";
    private static final long TARGET_ANIM_DURATION_LONG = 800;
    private static final long TARGET_ANIM_DURATION_SHORT = 600;
    private final Paint mBlueFill;
    float mCurrentScale = 1.0f;
    float mCurrentX;
    float mCurrentY;
    private UdfpsEnrollHelper mEnrollHelper;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Drawable mMovingTargetFpIcon;
    private final Paint mSensorOutlinePaint;
    private RectF mSensorRect;
    private boolean mShouldShowEdgeHint = false;
    private boolean mShouldShowTipHint = false;
    private final Animator.AnimatorListener mTargetAnimListener;
    AnimatorSet mTargetAnimatorSet;

    UdfpsEnrollDrawable(Context context) {
        super(context);
        Paint paint = new Paint(0);
        this.mSensorOutlinePaint = paint;
        paint.setAntiAlias(true);
        paint.setColor(context.getColor(C1894R.C1895color.udfps_moving_target_fill));
        paint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint(0);
        this.mBlueFill = paint2;
        paint2.setAntiAlias(true);
        paint2.setColor(context.getColor(C1894R.C1895color.udfps_moving_target_fill));
        paint2.setStyle(Paint.Style.FILL);
        Drawable drawable = context.getResources().getDrawable(C1894R.C1896drawable.ic_kg_fingerprint, (Resources.Theme) null);
        this.mMovingTargetFpIcon = drawable;
        drawable.setTint(context.getColor(C1894R.C1895color.udfps_enroll_icon));
        drawable.mutate();
        getFingerprintDrawable().setTint(context.getColor(C1894R.C1895color.udfps_enroll_icon));
        this.mTargetAnimListener = new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                UdfpsEnrollDrawable.this.updateTipHintVisibility();
            }
        };
    }

    /* access modifiers changed from: package-private */
    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mEnrollHelper = udfpsEnrollHelper;
    }

    public void onSensorRectUpdated(RectF rectF) {
        super.onSensorRectUpdated(rectF);
        this.mSensorRect = rectF;
    }

    /* access modifiers changed from: protected */
    public void updateFingerprintIconBounds(Rect rect) {
        super.updateFingerprintIconBounds(rect);
        this.mMovingTargetFpIcon.setBounds(rect);
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i, int i2) {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        if (udfpsEnrollHelper != null) {
            if (!udfpsEnrollHelper.isCenterEnrollmentStage()) {
                AnimatorSet animatorSet = this.mTargetAnimatorSet;
                if (animatorSet != null && animatorSet.isRunning()) {
                    this.mTargetAnimatorSet.end();
                }
                PointF nextGuidedEnrollmentPoint = this.mEnrollHelper.getNextGuidedEnrollmentPoint();
                if (this.mCurrentX == nextGuidedEnrollmentPoint.x && this.mCurrentY == nextGuidedEnrollmentPoint.y) {
                    updateTipHintVisibility();
                } else {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mCurrentX, nextGuidedEnrollmentPoint.x});
                    ofFloat.addUpdateListener(new UdfpsEnrollDrawable$$ExternalSyntheticLambda0(this));
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{this.mCurrentY, nextGuidedEnrollmentPoint.y});
                    ofFloat2.addUpdateListener(new UdfpsEnrollDrawable$$ExternalSyntheticLambda1(this));
                    long j = (nextGuidedEnrollmentPoint.x > 0.0f ? 1 : (nextGuidedEnrollmentPoint.x == 0.0f ? 0 : -1)) == 0 && (nextGuidedEnrollmentPoint.y > 0.0f ? 1 : (nextGuidedEnrollmentPoint.y == 0.0f ? 0 : -1)) == 0 ? TARGET_ANIM_DURATION_SHORT : TARGET_ANIM_DURATION_LONG;
                    ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{0.0f, 3.1415927f});
                    ofFloat3.setDuration(j);
                    ofFloat3.addUpdateListener(new UdfpsEnrollDrawable$$ExternalSyntheticLambda2(this));
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.mTargetAnimatorSet = animatorSet2;
                    animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
                    this.mTargetAnimatorSet.setDuration(j);
                    this.mTargetAnimatorSet.addListener(this.mTargetAnimListener);
                    this.mTargetAnimatorSet.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3});
                    this.mTargetAnimatorSet.start();
                }
            } else {
                updateTipHintVisibility();
            }
            updateEdgeHintVisibility();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onEnrollmentProgress$0$com-android-systemui-biometrics-UdfpsEnrollDrawable */
    public /* synthetic */ void mo30903x1e33c2a1(ValueAnimator valueAnimator) {
        this.mCurrentX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onEnrollmentProgress$1$com-android-systemui-biometrics-UdfpsEnrollDrawable */
    public /* synthetic */ void mo30904xab6e7422(ValueAnimator valueAnimator) {
        this.mCurrentY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onEnrollmentProgress$2$com-android-systemui-biometrics-UdfpsEnrollDrawable */
    public /* synthetic */ void mo30905x38a925a3(ValueAnimator valueAnimator) {
        this.mCurrentScale = (((float) Math.sin((double) ((Float) valueAnimator.getAnimatedValue()).floatValue())) * 0.25f) + 1.0f;
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public void updateTipHintVisibility() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        boolean z = udfpsEnrollHelper != null && udfpsEnrollHelper.isTipEnrollmentStage();
        if (this.mShouldShowTipHint != z) {
            this.mShouldShowTipHint = z;
        }
    }

    private void updateEdgeHintVisibility() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        boolean z = udfpsEnrollHelper != null && udfpsEnrollHelper.isEdgeEnrollmentStage();
        if (this.mShouldShowEdgeHint != z) {
            this.mShouldShowEdgeHint = z;
        }
    }

    public void draw(Canvas canvas) {
        if (!isIlluminationShowing()) {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper == null || udfpsEnrollHelper.isCenterEnrollmentStage()) {
                RectF rectF = this.mSensorRect;
                if (rectF != null) {
                    canvas.drawOval(rectF, this.mSensorOutlinePaint);
                }
                getFingerprintDrawable().draw(canvas);
                getFingerprintDrawable().setAlpha(getAlpha());
                this.mSensorOutlinePaint.setAlpha(getAlpha());
                return;
            }
            canvas.save();
            canvas.translate(this.mCurrentX, this.mCurrentY);
            RectF rectF2 = this.mSensorRect;
            if (rectF2 != null) {
                float f = this.mCurrentScale;
                canvas.scale(f, f, rectF2.centerX(), this.mSensorRect.centerY());
                canvas.drawOval(this.mSensorRect, this.mBlueFill);
            }
            this.mMovingTargetFpIcon.draw(canvas);
            canvas.restore();
        }
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mSensorOutlinePaint.setAlpha(i);
        this.mBlueFill.setAlpha(i);
        this.mMovingTargetFpIcon.setAlpha(i);
        invalidateSelf();
    }
}
