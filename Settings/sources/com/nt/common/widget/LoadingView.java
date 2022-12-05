package com.nt.common.widget;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import com.android.settings.R;
import com.android.settings.R$styleable;
@SuppressLint({"NewApi"})
/* loaded from: classes2.dex */
public class LoadingView extends View {
    private final long LOADING_ANIM_DURATION;
    private int mBackgroundColor;
    private float mCentX;
    private float mCentY;
    private RectF mCircleBounds;
    private Context mContext;
    private Paint mDotPaint;
    private int mForegroundColor;
    private Animator mLoadingAnimator;
    private int mLoadingState;
    private Paint mPaint;
    private Paint mPaintArc;
    private Paint mPaintArcBack;
    private int mPaintWidth;
    private float mRadius;
    private float mRingWidth;
    private float mStartAngle;
    private float mSweepAngle;
    private int mThemeColor;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.NtCommon_LoadingViewStyle);
    }

    public LoadingView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPaint = null;
        this.mPaintArc = null;
        this.mPaintArcBack = null;
        this.mDotPaint = null;
        this.mContext = null;
        this.mLoadingAnimator = null;
        this.LOADING_ANIM_DURATION = 1760L;
        this.mCircleBounds = null;
        this.mPaintWidth = 0;
        this.mLoadingState = 1;
        this.mContext = context;
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setColor(-1);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setTextSize(36.0f);
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(R$styleable.NtTheme);
        this.mThemeColor = obtainStyledAttributes.getInt(R$styleable.NtTheme_ntThemeColor, -16711936);
        this.mForegroundColor = obtainStyledAttributes.getInt(R$styleable.NtTheme_ntThemeColorLevel5, getResources().getColor(R.color.Blue_5));
        this.mBackgroundColor = obtainStyledAttributes.getInt(R$styleable.NtTheme_ntThemeColorLevel1, getResources().getColor(R.color.Blue_1));
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R$styleable.LoadingView, R.attr.NtCommon_LoadingStyle, 0);
        this.mRadius = obtainStyledAttributes2.getDimension(R$styleable.LoadingView_mcLoadingRadius, 24.0f);
        this.mRingWidth = obtainStyledAttributes2.getDimension(R$styleable.LoadingView_mcRingWidth, 10.0f);
        this.mBackgroundColor = obtainStyledAttributes2.getColor(R$styleable.LoadingView_mcLBackground, this.mBackgroundColor);
        this.mForegroundColor = obtainStyledAttributes2.getColor(R$styleable.LoadingView_mcLForeground, this.mForegroundColor);
        this.mLoadingState = obtainStyledAttributes2.getInt(R$styleable.LoadingView_mcLoadingState, 1);
        obtainStyledAttributes2.recycle();
        Paint paint2 = new Paint(1);
        this.mPaintArc = paint2;
        paint2.setAntiAlias(true);
        this.mPaintArc.setColor(this.mForegroundColor);
        this.mPaintArc.setStyle(Paint.Style.STROKE);
        Paint paint3 = new Paint(this.mPaintArc);
        this.mDotPaint = paint3;
        paint3.setStyle(Paint.Style.FILL);
        Paint paint4 = new Paint(1);
        this.mPaintArcBack = paint4;
        paint4.setAntiAlias(true);
        this.mPaintArcBack.setColor(this.mBackgroundColor);
        this.mPaintArcBack.setStyle(Paint.Style.STROKE);
        this.mPaintArc.setStrokeWidth(this.mRingWidth - this.mPaintWidth);
        this.mPaintArcBack.setStrokeWidth(this.mRingWidth - this.mPaintWidth);
        init();
    }

    public LoadingView(Context context, float f, float f2) {
        this(context, null);
        this.mRadius = f;
        this.mRingWidth = f2;
        init();
    }

    private void init() {
        this.mCentX = getX() + getPaddingLeft() + this.mRadius + (this.mPaintWidth * 2) + this.mRingWidth;
        this.mCentY = getY() + getPaddingTop() + this.mRadius + (this.mPaintWidth * 2) + this.mRingWidth;
        RectF rectF = new RectF();
        this.mCircleBounds = rectF;
        float f = this.mCentX;
        float f2 = this.mRadius;
        int i = this.mPaintWidth;
        float f3 = this.mRingWidth;
        rectF.left = ((f - f2) - (i / 2)) - (f3 / 2.0f);
        float f4 = this.mCentY;
        rectF.top = ((f4 - f2) - (i / 2)) - (f3 / 2.0f);
        rectF.right = f + f2 + (i / 2) + (f3 / 2.0f);
        rectF.bottom = f4 + f2 + (i / 2) + (f3 / 2.0f);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        canvas.translate(((getWidth() / 2) - this.mRadius) - this.mRingWidth, ((getHeight() / 2) - this.mRadius) - this.mRingWidth);
        if (this.mForegroundColor == this.mBackgroundColor) {
            this.mPaintArcBack.setAlpha(26);
        }
        if (this.mLoadingState == 1) {
            drawLoadingAnimation(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    private void drawLoadingAnimation(Canvas canvas) {
        canvas.drawArc(this.mCircleBounds, -90.0f, 360.0f, false, this.mPaintArcBack);
        canvas.drawArc(this.mCircleBounds, this.mStartAngle, this.mSweepAngle, false, this.mPaintArc);
        float width = this.mCircleBounds.width() / 2.0f;
        float height = this.mCircleBounds.height() / 2.0f;
        float strokeWidth = this.mPaintArc.getStrokeWidth() / 2.0f;
        canvas.drawCircle(this.mCircleBounds.right - (((float) (1.0d - Math.cos(Math.toRadians(this.mStartAngle)))) * width), this.mCircleBounds.bottom - (((float) (1.0d - Math.sin(Math.toRadians(this.mStartAngle)))) * height), strokeWidth, this.mDotPaint);
        canvas.drawCircle(this.mCircleBounds.right - (width * ((float) (1.0d - Math.cos(Math.toRadians(this.mSweepAngle + this.mStartAngle))))), this.mCircleBounds.bottom - (height * ((float) (1.0d - Math.sin(Math.toRadians(this.mSweepAngle + this.mStartAngle))))), strokeWidth, this.mDotPaint);
    }

    private void startLoadingAnimation() {
        Animator animator = this.mLoadingAnimator;
        if (animator == null || !animator.isRunning()) {
            this.mLoadingState = 1;
            Animator createLoadingAnimator = createLoadingAnimator();
            this.mLoadingAnimator = createLoadingAnimator;
            createLoadingAnimator.start();
        }
    }

    private Animator createLoadingAnimator() {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofKeyframe("startAngle", Keyframe.ofFloat(0.0f, -90.0f), Keyframe.ofFloat(0.5f, 330.0f), Keyframe.ofFloat(1.0f, 630.0f)), PropertyValuesHolder.ofFloat("sweepAngle", 0.0f, -144.0f, 0.0f));
        ofPropertyValuesHolder.setDuration(1760L);
        ofPropertyValuesHolder.setInterpolator(new LinearInterpolator());
        ofPropertyValuesHolder.setRepeatCount(-1);
        return ofPropertyValuesHolder;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        Animator animator;
        super.setVisibility(i);
        if (i == 0) {
            startLoadingAnimation();
        } else if ((i != 4 && i != 8) || (animator = this.mLoadingAnimator) == null) {
        } else {
            animator.cancel();
            this.mLoadingAnimator = null;
        }
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (1 == this.mLoadingState) {
            if (i != 0) {
                Animator animator = this.mLoadingAnimator;
                if (animator == null) {
                    return;
                }
                animator.cancel();
                this.mLoadingAnimator = null;
            } else if (!isShown()) {
            } else {
                startLoadingAnimation();
            }
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (1 == this.mLoadingState) {
            if (i != 0) {
                Animator animator = this.mLoadingAnimator;
                if (animator == null) {
                    return;
                }
                animator.cancel();
                this.mLoadingAnimator = null;
            } else if (!isShown()) {
            } else {
                startLoadingAnimation();
            }
        }
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3 = (int) ((this.mRadius + this.mRingWidth + 2.0f) * 2.0f);
        setMeasuredDimension(View.resolveSizeAndState(getPaddingLeft() + getPaddingRight() + i3, i, 0), View.resolveSizeAndState(i3 + getPaddingTop() + getPaddingBottom(), i2, 0));
    }

    public void setBarColor(int i) {
        Paint paint = this.mPaintArc;
        if (paint == null || paint.getColor() == i) {
            return;
        }
        this.mPaintArc.setColor(i);
        this.mDotPaint.setColor(i);
        this.mForegroundColor = i;
        postInvalidate();
    }

    public int getBarColor() {
        return this.mForegroundColor;
    }

    public void setBarBackgroundColor(int i) {
        Paint paint = this.mPaintArcBack;
        if (paint == null || paint.getColor() == i) {
            return;
        }
        this.mPaintArcBack.setColor(i);
        this.mBackgroundColor = i;
        postInvalidate();
    }

    public int getBarBackgroundColor() {
        return this.mBackgroundColor;
    }

    public float getSweepAngle() {
        return this.mSweepAngle;
    }

    public void setSweepAngle(float f) {
        this.mSweepAngle = f;
        invalidate();
    }

    public float getStartAngle() {
        return this.mStartAngle;
    }

    public void setStartAngle(float f) {
        this.mStartAngle = f;
        invalidate();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(LoadingView.class.getName());
    }
}
