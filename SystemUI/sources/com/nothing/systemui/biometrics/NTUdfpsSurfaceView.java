package com.nothing.systemui.biometrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.nothing.systemui.util.NTLogUtil;

public class NTUdfpsSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "NTUdfpsSurfaceView";
    boolean mHasValidSurface;
    private final SurfaceHolder mHolder;
    boolean mPendingDraw = false;
    RectF mRectF = null;
    private final Paint mSensorPaint;

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public NTUdfpsSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        this.mHolder = holder;
        holder.addCallback(this);
        holder.setFormat(1);
        Paint paint = new Paint(0);
        this.mSensorPaint = paint;
        paint.setAntiAlias(true);
        paint.setARGB(255, 255, 255, 255);
        paint.setStyle(Paint.Style.FILL);
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        RectF rectF;
        this.mHasValidSurface = true;
        NTLogUtil.m1686d(TAG, "surfaceCreated mPendingDraw:" + this.mPendingDraw);
        if (this.mPendingDraw && (rectF = this.mRectF) != null) {
            drawIlluminationDot(rectF);
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        NTLogUtil.m1686d(TAG, "surfaceDestroyed");
        this.mHasValidSurface = false;
    }

    public void drawIlluminationDot(RectF rectF) {
        this.mPendingDraw = false;
        Canvas canvas = null;
        try {
            NTLogUtil.m1686d(TAG, "drawIlluminationDot " + this.mHolder + " mHasValidSurface " + this.mHasValidSurface);
            if (this.mHasValidSurface) {
                canvas = this.mHolder.lockCanvas();
                canvas.drawOval(rectF, this.mSensorPaint);
            } else {
                this.mPendingDraw = true;
                this.mRectF = rectF;
            }
            if (canvas == null) {
                return;
            }
        } catch (Exception e) {
            NTLogUtil.m1686d(TAG, "drawIlluminationDot Exception=" + e.toString());
            if (0 == 0) {
                return;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                this.mHolder.unlockCanvasAndPost((Canvas) null);
            }
            throw th;
        }
        this.mHolder.unlockCanvasAndPost(canvas);
    }
}
