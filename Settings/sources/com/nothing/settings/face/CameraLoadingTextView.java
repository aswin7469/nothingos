package com.nothing.settings.face;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.TextView;
import com.android.settings.R$string;

public class CameraLoadingTextView extends TextView {
    /* access modifiers changed from: private */
    public int mBottom;
    private Paint mCirclePaint;
    private Context mContext;
    private float mCx;
    private float mCy;
    /* access modifiers changed from: private */
    public int mLeft;
    private int mPaintColor;
    private ViewOutlineProvider mProvider;
    private float mRadius;
    /* access modifiers changed from: private */
    public int mRight;
    private float mStrokeWidth;
    /* access modifiers changed from: private */
    public int mTop;

    public CameraLoadingTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CameraLoadingTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CameraLoadingTextView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public CameraLoadingTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mPaintColor = Color.parseColor("#FFAAAAAA");
        this.mStrokeWidth = 14.0f;
        this.mProvider = new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                Log.d("CameraLoadingTextView", " getOutline mLeft:" + CameraLoadingTextView.this.mLeft + ",mTop:" + CameraLoadingTextView.this.mTop + ",mRight:" + CameraLoadingTextView.this.mRight + ",mBottom:" + CameraLoadingTextView.this.mBottom);
                outline.setOval(CameraLoadingTextView.this.mLeft, CameraLoadingTextView.this.mTop, CameraLoadingTextView.this.mRight, CameraLoadingTextView.this.mBottom);
            }
        };
        this.mContext = context;
        init();
    }

    private void init() {
        Paint paint = new Paint();
        this.mCirclePaint = paint;
        paint.setAntiAlias(true);
        this.mCirclePaint.setStyle(Paint.Style.STROKE);
        this.mCirclePaint.setStrokeWidth(this.mStrokeWidth);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(this.mLeft, this.mTop, this.mRight, this.mBottom);
        Paint paint = new Paint(1);
        paint.setStrokeWidth(3.0f);
        paint.setTextSize(50.0f);
        paint.setColor(this.mPaintColor);
        canvas.drawRect(rect, paint);
        paint.setColor(-16777216);
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(this.mContext.getString(R$string.nt_face_add_camera_preview_loading), (float) rect.centerX(), (float) ((((rect.bottom + rect.top) - fontMetricsInt.bottom) - fontMetricsInt.top) / 2), paint);
    }

    public void setCircleProperties(CircleProperties circleProperties) {
        if (circleProperties == null) {
            Log.e("CameraLoadingTextView", "setCircleProperties: circleProperties == null");
            return;
        }
        this.mRadius = circleProperties.getRadius() + (this.mStrokeWidth / 2.0f);
        this.mCx = circleProperties.getCx();
        this.mCy = circleProperties.getCy();
        this.mLeft = circleProperties.getLeft() - (((int) this.mStrokeWidth) / 2);
        this.mTop = circleProperties.getTop() - (((int) this.mStrokeWidth) / 2);
        this.mRight = circleProperties.getRight() + (((int) this.mStrokeWidth) / 2);
        this.mBottom = circleProperties.getBottom() + (((int) this.mStrokeWidth) / 2);
        Log.d("CameraLoadingTextView", "mLeft:" + this.mLeft + ",mTop:" + this.mTop + ",mRight:" + this.mRight + ",mBottom:" + this.mBottom + ",mRadius:" + this.mRadius);
    }

    public void setCircleOutlineProvider() {
        setOutlineProvider(this.mProvider);
        setClipToOutline(true);
        invalidate();
    }
}
