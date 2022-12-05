package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import com.android.systemui.R$color;
/* loaded from: classes.dex */
public class UdfpsEnrollProgressBarSegment {
    private final Paint mBackgroundPaint;
    private final Rect mBounds;
    private ValueAnimator mFillColorAnimator;
    private final int mHelpColor;
    private final Runnable mInvalidateRunnable;
    private final float mMaxOverSweepAngle;
    private ValueAnimator mOverSweepAnimator;
    private ValueAnimator mOverSweepReverseAnimator;
    private ValueAnimator mProgressAnimator;
    private final int mProgressColor;
    private final Paint mProgressPaint;
    private final float mStartAngle;
    private final float mStrokeWidthPx;
    private final float mSweepAngle;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private float mProgress = 0.0f;
    private float mAnimatedProgress = 0.0f;
    private boolean mIsShowingHelp = false;
    private float mOverSweepAngle = 0.0f;
    private final ValueAnimator.AnimatorUpdateListener mProgressUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarSegment$$ExternalSyntheticLambda0
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollProgressBarSegment.this.lambda$new$0(valueAnimator);
        }
    };
    private final ValueAnimator.AnimatorUpdateListener mFillColorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarSegment$$ExternalSyntheticLambda1
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollProgressBarSegment.this.lambda$new$1(valueAnimator);
        }
    };
    private final ValueAnimator.AnimatorUpdateListener mOverSweepUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarSegment$$ExternalSyntheticLambda2
        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            UdfpsEnrollProgressBarSegment.this.lambda$new$2(valueAnimator);
        }
    };
    private final Runnable mOverSweepAnimationRunnable = new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollProgressBarSegment$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            UdfpsEnrollProgressBarSegment.this.lambda$new$3();
        }
    };

    public UdfpsEnrollProgressBarSegment(Context context, Rect rect, float f, float f2, float f3, Runnable runnable) {
        this.mBounds = rect;
        this.mInvalidateRunnable = runnable;
        this.mStartAngle = f;
        this.mSweepAngle = f2;
        this.mMaxOverSweepAngle = f3;
        float dpToPixels = Utils.dpToPixels(context, 12.0f);
        this.mStrokeWidthPx = dpToPixels;
        int color = context.getColor(R$color.udfps_enroll_progress);
        this.mProgressColor = color;
        this.mHelpColor = context.getColor(R$color.udfps_enroll_progress_help);
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setStrokeWidth(dpToPixels);
        paint.setColor(context.getColor(R$color.white_disabled));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16843817});
        paint.setColor(obtainStyledAttributes.getColor(0, paint.getColor()));
        obtainStyledAttributes.recycle();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16842803, typedValue, true);
        paint.setAlpha((int) (typedValue.getFloat() * 255.0f));
        Paint paint2 = new Paint();
        this.mProgressPaint = paint2;
        paint2.setStrokeWidth(dpToPixels);
        paint2.setColor(color);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mAnimatedProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mInvalidateRunnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.mProgressPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        this.mInvalidateRunnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(ValueAnimator valueAnimator) {
        this.mOverSweepAngle = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.mInvalidateRunnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        ValueAnimator valueAnimator = this.mOverSweepAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mOverSweepAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mOverSweepAngle, this.mMaxOverSweepAngle);
        this.mOverSweepAnimator = ofFloat;
        ofFloat.setDuration(200L);
        this.mOverSweepAnimator.addUpdateListener(this.mOverSweepUpdateListener);
        this.mOverSweepAnimator.start();
    }

    public void draw(Canvas canvas) {
        float f = this.mStrokeWidthPx / 2.0f;
        if (this.mAnimatedProgress < 1.0f) {
            Rect rect = this.mBounds;
            canvas.drawArc(f, f, rect.right - f, rect.bottom - f, this.mStartAngle, this.mSweepAngle, false, this.mBackgroundPaint);
        }
        float f2 = this.mAnimatedProgress;
        if (f2 > 0.0f) {
            Rect rect2 = this.mBounds;
            canvas.drawArc(f, f, rect2.right - f, rect2.bottom - f, this.mStartAngle, (this.mSweepAngle * f2) + this.mOverSweepAngle, false, this.mProgressPaint);
        }
    }

    public float getProgress() {
        return this.mProgress;
    }

    public void updateProgress(float f) {
        updateProgress(f, 400L);
    }

    private void updateProgress(float f, long j) {
        if (this.mProgress == f) {
            return;
        }
        this.mProgress = f;
        ValueAnimator valueAnimator = this.mProgressAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mProgressAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mAnimatedProgress, f);
        this.mProgressAnimator = ofFloat;
        ofFloat.setDuration(j);
        this.mProgressAnimator.addUpdateListener(this.mProgressUpdateListener);
        this.mProgressAnimator.start();
    }

    public void updateFillColor(boolean z) {
        if (this.mIsShowingHelp == z) {
            return;
        }
        this.mIsShowingHelp = z;
        ValueAnimator valueAnimator = this.mFillColorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mFillColorAnimator.cancel();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(this.mProgressPaint.getColor(), z ? this.mHelpColor : this.mProgressColor);
        this.mFillColorAnimator = ofArgb;
        ofArgb.setDuration(200L);
        this.mFillColorAnimator.addUpdateListener(this.mFillColorUpdateListener);
        this.mFillColorAnimator.start();
    }

    public void startCompletionAnimation() {
        boolean hasCallbacks = this.mHandler.hasCallbacks(this.mOverSweepAnimationRunnable);
        if (hasCallbacks || this.mOverSweepAngle >= this.mMaxOverSweepAngle) {
            Log.d("UdfpsProgressBarSegment", "startCompletionAnimation skipped: hasCallback = " + hasCallbacks + ", mOverSweepAngle = " + this.mOverSweepAngle);
            return;
        }
        ValueAnimator valueAnimator = this.mOverSweepReverseAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mOverSweepReverseAnimator.cancel();
            this.mOverSweepAngle = 0.0f;
        }
        if (this.mAnimatedProgress < 1.0f) {
            updateProgress(1.0f, 200L);
            updateFillColor(false);
        }
        this.mHandler.postDelayed(this.mOverSweepAnimationRunnable, 200L);
    }

    public void cancelCompletionAnimation() {
        this.mHandler.removeCallbacks(this.mOverSweepAnimationRunnable);
        ValueAnimator valueAnimator = this.mOverSweepAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mOverSweepAnimator.cancel();
        }
        if (this.mOverSweepAngle > 0.0f) {
            ValueAnimator valueAnimator2 = this.mOverSweepReverseAnimator;
            if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                this.mOverSweepReverseAnimator.cancel();
            }
            float f = this.mOverSweepAngle;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(f, 0.0f);
            this.mOverSweepReverseAnimator = ofFloat;
            ofFloat.setDuration((f / this.mMaxOverSweepAngle) * 200.0f);
            this.mOverSweepReverseAnimator.addUpdateListener(this.mOverSweepUpdateListener);
            this.mOverSweepReverseAnimator.start();
        }
    }
}
