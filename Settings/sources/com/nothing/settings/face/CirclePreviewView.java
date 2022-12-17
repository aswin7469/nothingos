package com.nothing.settings.face;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CirclePreviewView extends View {
    private int mArcPaintColor;
    private int mBottom;
    private Paint mCirclePaint;
    private int mCirclePaintColor;
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
        this(context, (AttributeSet) null);
    }

    public CirclePreviewView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CirclePreviewView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public CirclePreviewView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCirclePaintColor = Color.parseColor("#FFAAAAAA");
        this.mArcPaintColor = Color.parseColor("#FF198DED");
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
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCirclePaint.setColor(this.mCirclePaintColor);
        canvas.drawCircle(this.mCx, this.mCy, this.mRadius, this.mCirclePaint);
        this.mCirclePaint.setColor(this.mArcPaintColor);
        float f = ((float) (this.mProgress * 360)) / 100.0f;
        Log.d("CirclePreviewView", "mProgress: " + this.mProgress + ",sweepAngle:" + f);
        canvas.drawArc((float) this.mLeft, (float) this.mTop, (float) this.mRight, (float) this.mBottom, -90.0f, f, false, this.mCirclePaint);
    }

    public void setCircleProperties(CircleProperties circleProperties) {
        if (circleProperties == null) {
            Log.e("CirclePreviewView", "setCircleProperties: circleProperties == null");
            return;
        }
        this.mRadius = circleProperties.getRadius() + (this.mStrokeWidth / 2.0f);
        this.mCx = ((float) circleProperties.getMarginLayoutParams().leftMargin) + circleProperties.getCx();
        this.mCy = circleProperties.getCy();
        this.mLeft = (circleProperties.getMarginLayoutParams().leftMargin + circleProperties.getLeft()) - (((int) this.mStrokeWidth) / 2);
        this.mTop = circleProperties.getTop() - (((int) this.mStrokeWidth) / 2);
        this.mRight = circleProperties.getMarginLayoutParams().leftMargin + circleProperties.getRight() + (((int) this.mStrokeWidth) / 2);
        this.mBottom = circleProperties.getBottom() + (((int) this.mStrokeWidth) / 2);
        Log.d("CirclePreviewView", "mLeft:" + this.mLeft + ",mTop:" + this.mTop + ",mRight:" + this.mRight + ",mBottom:" + this.mBottom + ",mRadius:" + this.mRadius);
        invalidate();
    }

    public Rect getMaskRect(float f, float f2) {
        if (this.mIsFirstScale) {
            this.mMaskRect.set((int) (((float) this.mLeft) / f), (int) (((float) this.mTop) / f2), (int) (((float) this.mRight) / f), (int) (((float) this.mBottom) / f2));
            this.mIsFirstScale = false;
        }
        Log.d("CirclePreviewView", "mMaskRect:" + this.mMaskRect);
        return this.mMaskRect;
    }
}
