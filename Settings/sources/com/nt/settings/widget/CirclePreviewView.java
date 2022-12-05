package com.nt.settings.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
/* loaded from: classes2.dex */
public class CirclePreviewView extends View {
    private int mBottom;
    private Paint mCirclePaint;
    private Paint mClearPaint;
    private float mCx;
    private float mCy;
    private boolean mIsFirstScale;
    private int mLeft;
    private Rect mMaskRect;
    private int mProgress;
    private float mRadius;
    private int mRight;
    private float mStrokeWidth;
    private int mTop;

    public CirclePreviewView(Context context) {
        this(context, null);
    }

    public CirclePreviewView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CirclePreviewView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public CirclePreviewView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mIsFirstScale = true;
        this.mStrokeWidth = 14.0f;
        init();
    }

    private void init() {
        Paint paint = new Paint();
        this.mClearPaint = paint;
        paint.setAntiAlias(true);
        this.mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        Paint paint2 = new Paint();
        this.mCirclePaint = paint2;
        paint2.setAntiAlias(true);
        this.mCirclePaint.setStyle(Paint.Style.STROKE);
        this.mCirclePaint.setStrokeWidth(this.mStrokeWidth);
        this.mMaskRect = new Rect();
    }

    public void setProgress(int i) {
        this.mProgress = i;
        if (Build.VERSION.SDK_INT >= 21) {
            invalidate();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mCirclePaint.setColor(-5592406);
            canvas.drawCircle(this.mCx, this.mCy, this.mRadius, this.mCirclePaint);
            this.mCirclePaint.setColor(-15102483);
            float f = (this.mProgress * 360) / 100.0f;
            Log.d("CirclePreviewView", "mProgress: " + this.mProgress + ",sweepAngle:" + f);
            canvas.drawArc((float) this.mLeft, (float) this.mTop, (float) this.mRight, (float) this.mBottom, -90.0f, f, false, this.mCirclePaint);
        }
    }

    public void setCircleProperties(CircleProperties circleProperties) {
        if (circleProperties == null) {
            Log.e("CirclePreviewView", "setCircleProperties: circleProperties == null");
            return;
        }
        this.mRadius = circleProperties.getRadius() + (this.mStrokeWidth / 2.0f);
        this.mCx = circleProperties.getMarginLayoutParams().leftMargin + circleProperties.getCx();
        this.mCy = circleProperties.getCy();
        this.mLeft = (circleProperties.getMarginLayoutParams().leftMargin + circleProperties.getLeft()) - (((int) this.mStrokeWidth) / 2);
        this.mTop = circleProperties.getTop() - (((int) this.mStrokeWidth) / 2);
        this.mRight = circleProperties.getMarginLayoutParams().leftMargin + circleProperties.getRight() + (((int) this.mStrokeWidth) / 2);
        this.mBottom = circleProperties.getBottom() + (((int) this.mStrokeWidth) / 2);
        Log.d("CirclePreviewView", "mLeft:" + this.mLeft + ",mTop:" + this.mTop + ",mRight:" + this.mRight + ",mBottom:" + this.mBottom + ",mRadius:" + this.mRadius);
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        invalidate();
    }

    public Rect getMaskRect(float f, float f2) {
        if (this.mIsFirstScale) {
            int i = (int) (this.mRight / f);
            int i2 = (int) (this.mBottom / f2);
            this.mMaskRect.set((int) (this.mLeft / f), (int) (this.mTop / f2), i, i2);
            this.mIsFirstScale = false;
        }
        Log.d("CirclePreviewView", "mMaskRect:" + this.mMaskRect);
        return this.mMaskRect;
    }
}
