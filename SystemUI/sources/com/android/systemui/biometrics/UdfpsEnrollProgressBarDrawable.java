package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import com.android.systemui.C1894R;

public class UdfpsEnrollProgressBarDrawable extends Drawable {
    private static final long CHECKMARK_ANIMATION_DELAY_MS = 200;
    private static final long CHECKMARK_ANIMATION_DURATION_MS = 300;
    private static final Interpolator DEACCEL = new DecelerateInterpolator();
    private static final long FILL_COLOR_ANIMATION_DURATION_MS = 350;
    private static final VibrationAttributes FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES = VibrationAttributes.createForUsage(66);
    private static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    private static final long PROGRESS_ANIMATION_DURATION_MS = 400;
    private static final float STROKE_WIDTH_DP = 12.0f;
    private static final VibrationEffect SUCCESS_VIBRATION_EFFECT = VibrationEffect.get(0);
    private static final String TAG = "UdfpsProgressBar";
    private static final VibrationEffect VIBRATE_EFFECT_ERROR = VibrationEffect.createWaveform(new long[]{0, 5, 55, 60}, -1);
    private boolean mAfterFirstTouch;
    private ValueAnimator mBackgroundColorAnimator;
    private final ValueAnimator.AnimatorUpdateListener mBackgroundColorUpdateListener;
    private final Paint mBackgroundPaint;
    private ValueAnimator mCheckmarkAnimator;
    private final Drawable mCheckmarkDrawable;
    private final Interpolator mCheckmarkInterpolator;
    private float mCheckmarkScale = 0.0f;
    private final ValueAnimator.AnimatorUpdateListener mCheckmarkUpdateListener;
    private boolean mComplete = false;
    private final Context mContext;
    private ValueAnimator mFillColorAnimator;
    private final ValueAnimator.AnimatorUpdateListener mFillColorUpdateListener;
    private final Paint mFillPaint;
    private final int mHelpColor;
    private final boolean mIsAccessibilityEnabled;
    private final int mOnFirstBucketFailedColor;
    private float mProgress = 0.0f;
    private ValueAnimator mProgressAnimator;
    private final int mProgressColor;
    private final ValueAnimator.AnimatorUpdateListener mProgressUpdateListener;
    private int mRemainingSteps = 0;
    private boolean mShowingHelp = false;
    private final float mStrokeWidthPx;
    private int mTotalSteps = 0;
    private final Vibrator mVibrator;

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public UdfpsEnrollProgressBarDrawable(Context context) {
        this.mContext = context;
        float dpToPixels = Utils.dpToPixels(context, STROKE_WIDTH_DP);
        this.mStrokeWidthPx = dpToPixels;
        int color = context.getColor(C1894R.C1895color.udfps_enroll_progress);
        this.mProgressColor = color;
        boolean isTouchExplorationEnabled = ((AccessibilityManager) context.getSystemService(AccessibilityManager.class)).isTouchExplorationEnabled();
        this.mIsAccessibilityEnabled = isTouchExplorationEnabled;
        if (!isTouchExplorationEnabled) {
            this.mHelpColor = context.getColor(C1894R.C1895color.udfps_enroll_progress_help);
            this.mOnFirstBucketFailedColor = context.getColor(C1894R.C1895color.udfps_moving_target_fill_error);
        } else {
            int color2 = context.getColor(C1894R.C1895color.udfps_enroll_progress_help_with_talkback);
            this.mHelpColor = color2;
            this.mOnFirstBucketFailedColor = color2;
        }
        Drawable drawable = context.getDrawable(C1894R.C1896drawable.udfps_enroll_checkmark);
        this.mCheckmarkDrawable = drawable;
        drawable.mutate();
        this.mCheckmarkInterpolator = new OvershootInterpolator();
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setStrokeWidth(dpToPixels);
        paint.setColor(context.getColor(C1894R.C1895color.udfps_moving_target_fill));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.mFillPaint = paint2;
        paint2.setStrokeWidth(dpToPixels);
        paint2.setColor(color);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        this.mVibrator = (Vibrator) context.getSystemService(Vibrator.class);
        this.mProgressUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0(this);
        this.mFillColorUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda1(this);
        this.mCheckmarkUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda2(this);
        this.mBackgroundColorUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda3(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-biometrics-UdfpsEnrollProgressBarDrawable */
    public /* synthetic */ void mo30932x7bc80d4b(ValueAnimator valueAnimator) {
        this.mProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-biometrics-UdfpsEnrollProgressBarDrawable */
    public /* synthetic */ void mo30933xfa29112a(ValueAnimator valueAnimator) {
        this.mFillPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-biometrics-UdfpsEnrollProgressBarDrawable */
    public /* synthetic */ void mo30934x788a1509(ValueAnimator valueAnimator) {
        this.mCheckmarkScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-biometrics-UdfpsEnrollProgressBarDrawable */
    public /* synthetic */ void mo30935xf6eb18e8(ValueAnimator valueAnimator) {
        this.mBackgroundPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        invalidateSelf();
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i, int i2) {
        this.mAfterFirstTouch = true;
        updateState(i, i2, false);
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentHelp(int i, int i2) {
        updateState(i, i2, true);
    }

    /* access modifiers changed from: package-private */
    public void onLastStepAcquired() {
        updateState(0, this.mTotalSteps, false);
    }

    private void updateState(int i, int i2, boolean z) {
        updateProgress(i, i2, z);
        updateFillColor(z);
    }

    private void updateProgress(int i, int i2, boolean z) {
        if (this.mRemainingSteps != i || this.mTotalSteps != i2) {
            this.mRemainingSteps = i;
            this.mTotalSteps = i2;
            int max = Math.max(0, i2 - i);
            boolean z2 = this.mAfterFirstTouch;
            if (z2) {
                max++;
            }
            float min = Math.min(1.0f, ((float) max) / ((float) (z2 ? this.mTotalSteps + 1 : this.mTotalSteps)));
            ValueAnimator valueAnimator = this.mProgressAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mProgress, min});
            this.mProgressAnimator = ofFloat;
            ofFloat.setDuration(PROGRESS_ANIMATION_DURATION_MS);
            this.mProgressAnimator.addUpdateListener(this.mProgressUpdateListener);
            this.mProgressAnimator.start();
            if (i == 0) {
                startCompletionAnimation();
            } else if (i > 0) {
                rollBackCompletionAnimation();
            }
        }
    }

    private void animateBackgroundColor() {
        ValueAnimator valueAnimator = this.mBackgroundColorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mBackgroundColorAnimator.end();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mBackgroundPaint.getColor(), this.mOnFirstBucketFailedColor});
        this.mBackgroundColorAnimator = ofArgb;
        ofArgb.setDuration(350);
        this.mBackgroundColorAnimator.setRepeatCount(1);
        this.mBackgroundColorAnimator.setRepeatMode(2);
        this.mBackgroundColorAnimator.setInterpolator(DEACCEL);
        this.mBackgroundColorAnimator.addUpdateListener(this.mBackgroundColorUpdateListener);
        this.mBackgroundColorAnimator.start();
    }

    private void updateFillColor(boolean z) {
        if (this.mAfterFirstTouch || !z) {
            ValueAnimator valueAnimator = this.mFillColorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mFillColorAnimator.end();
            }
            ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mFillPaint.getColor(), z ? this.mHelpColor : this.mProgressColor});
            this.mFillColorAnimator = ofArgb;
            ofArgb.setDuration(350);
            this.mFillColorAnimator.setRepeatCount(1);
            this.mFillColorAnimator.setRepeatMode(2);
            this.mFillColorAnimator.setInterpolator(DEACCEL);
            this.mFillColorAnimator.addUpdateListener(this.mFillColorUpdateListener);
            this.mFillColorAnimator.start();
            return;
        }
        animateBackgroundColor();
    }

    private void startCompletionAnimation() {
        if (!this.mComplete) {
            this.mComplete = true;
            ValueAnimator valueAnimator = this.mCheckmarkAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mCheckmarkAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mCheckmarkScale, 1.0f});
            this.mCheckmarkAnimator = ofFloat;
            ofFloat.setStartDelay(200);
            this.mCheckmarkAnimator.setDuration(300);
            this.mCheckmarkAnimator.setInterpolator(this.mCheckmarkInterpolator);
            this.mCheckmarkAnimator.addUpdateListener(this.mCheckmarkUpdateListener);
            this.mCheckmarkAnimator.start();
        }
    }

    private void rollBackCompletionAnimation() {
        if (this.mComplete) {
            this.mComplete = false;
            ValueAnimator valueAnimator = this.mCheckmarkAnimator;
            long round = (long) Math.round((valueAnimator != null ? valueAnimator.getAnimatedFraction() : 0.0f) * 200.0f);
            ValueAnimator valueAnimator2 = this.mCheckmarkAnimator;
            if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                this.mCheckmarkAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mCheckmarkScale, 0.0f});
            this.mCheckmarkAnimator = ofFloat;
            ofFloat.setDuration(round);
            this.mCheckmarkAnimator.addUpdateListener(this.mCheckmarkUpdateListener);
            this.mCheckmarkAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90.0f, (float) getBounds().centerX(), (float) getBounds().centerY());
        float f = this.mStrokeWidthPx / 2.0f;
        if (this.mProgress < 1.0f) {
            canvas.drawArc(f, f, ((float) getBounds().right) - f, ((float) getBounds().bottom) - f, 0.0f, 360.0f, false, this.mBackgroundPaint);
        }
        if (this.mProgress > 0.0f) {
            canvas.drawArc(f, f, ((float) getBounds().right) - f, ((float) getBounds().bottom) - f, 0.0f, this.mProgress * 360.0f, false, this.mFillPaint);
        }
        canvas.restore();
        if (this.mCheckmarkScale > 0.0f) {
            float sqrt = ((float) Math.sqrt(2.0d)) / 2.0f;
            float width = ((((float) getBounds().width()) - this.mStrokeWidthPx) / 2.0f) * sqrt;
            float height = ((((float) getBounds().height()) - this.mStrokeWidthPx) / 2.0f) * sqrt;
            float centerX = ((float) getBounds().centerX()) + width;
            float centerY = ((float) getBounds().centerY()) + height;
            float intrinsicWidth = (((float) this.mCheckmarkDrawable.getIntrinsicWidth()) / 2.0f) * this.mCheckmarkScale;
            float intrinsicHeight = (((float) this.mCheckmarkDrawable.getIntrinsicHeight()) / 2.0f) * this.mCheckmarkScale;
            this.mCheckmarkDrawable.setBounds(Math.round(centerX - intrinsicWidth), Math.round(centerY - intrinsicHeight), Math.round(centerX + intrinsicWidth), Math.round(centerY + intrinsicHeight));
            this.mCheckmarkDrawable.draw(canvas);
        }
    }
}
