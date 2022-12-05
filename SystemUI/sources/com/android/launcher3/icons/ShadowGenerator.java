package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import com.android.launcher3.icons.ShadowGenerator;
/* loaded from: classes.dex */
public class ShadowGenerator {
    private final BlurMaskFilter mDefaultBlurMaskFilter;
    private final int mIconSize;
    private final Paint mBlurPaint = new Paint(3);
    private final Paint mDrawPaint = new Paint(3);

    public ShadowGenerator(int i) {
        this.mIconSize = i;
        this.mDefaultBlurMaskFilter = new BlurMaskFilter(i * 0.03125f, BlurMaskFilter.Blur.NORMAL);
    }

    public synchronized void recreateIcon(Bitmap bitmap, Canvas canvas) {
        recreateIcon(bitmap, this.mDefaultBlurMaskFilter, 7, 10, canvas);
    }

    public synchronized void recreateIcon(Bitmap bitmap, BlurMaskFilter blurMaskFilter, int i, int i2, Canvas canvas) {
        int[] iArr = new int[2];
        this.mBlurPaint.setMaskFilter(blurMaskFilter);
        Bitmap extractAlpha = bitmap.extractAlpha(this.mBlurPaint, iArr);
        this.mDrawPaint.setAlpha(i);
        canvas.drawBitmap(extractAlpha, iArr[0], iArr[1], this.mDrawPaint);
        this.mDrawPaint.setAlpha(i2);
        canvas.drawBitmap(extractAlpha, iArr[0], iArr[1] + (this.mIconSize * 0.020833334f), this.mDrawPaint);
        this.mDrawPaint.setAlpha(255);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mDrawPaint);
    }

    public static float getScaleForBounds(RectF rectF) {
        float min = Math.min(Math.min(rectF.left, rectF.right), rectF.top);
        float f = min < 0.03125f ? 0.46875f / (0.5f - min) : 1.0f;
        float f2 = rectF.bottom;
        return f2 < 0.052083336f ? Math.min(f, 0.44791666f / (0.5f - f2)) : f;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public final int color;
        public float keyShadowDistance;
        public float radius;
        public float shadowBlur;
        public final RectF bounds = new RectF();
        public int ambientShadowAlpha = 7;
        public int keyShadowAlpha = 10;

        public Builder(int i) {
            this.color = i;
        }

        public Builder setupBlurForSize(int i) {
            float f = i * 1.0f;
            this.shadowBlur = f / 24.0f;
            this.keyShadowDistance = f / 16.0f;
            return this;
        }

        public Bitmap createPill(int i, int i2) {
            return createPill(i, i2, i2 / 2.0f);
        }

        public Bitmap createPill(int i, int i2, float f) {
            this.radius = f;
            float f2 = i;
            float f3 = f2 / 2.0f;
            int max = Math.max(Math.round(this.shadowBlur + f3), Math.round(this.radius + this.shadowBlur + this.keyShadowDistance));
            float f4 = i2;
            this.bounds.set(0.0f, 0.0f, f2, f4);
            float f5 = max;
            this.bounds.offsetTo(f5 - f3, f5 - (f4 / 2.0f));
            int i3 = max * 2;
            return BitmapRenderer.createHardwareBitmap(i3, i3, new BitmapRenderer() { // from class: com.android.launcher3.icons.ShadowGenerator$Builder$$ExternalSyntheticLambda0
                @Override // com.android.launcher3.icons.BitmapRenderer
                public final void draw(Canvas canvas) {
                    ShadowGenerator.Builder.this.drawShadow(canvas);
                }
            });
        }

        public void drawShadow(Canvas canvas) {
            Paint paint = new Paint(3);
            paint.setColor(this.color);
            paint.setShadowLayer(this.shadowBlur, 0.0f, this.keyShadowDistance, GraphicsUtils.setColorAlphaBound(-16777216, this.keyShadowAlpha));
            RectF rectF = this.bounds;
            float f = this.radius;
            canvas.drawRoundRect(rectF, f, f, paint);
            paint.setShadowLayer(this.shadowBlur, 0.0f, 0.0f, GraphicsUtils.setColorAlphaBound(-16777216, this.ambientShadowAlpha));
            RectF rectF2 = this.bounds;
            float f2 = this.radius;
            canvas.drawRoundRect(rectF2, f2, f2, paint);
            if (Color.alpha(this.color) < 255) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                paint.clearShadowLayer();
                paint.setColor(-16777216);
                RectF rectF3 = this.bounds;
                float f3 = this.radius;
                canvas.drawRoundRect(rectF3, f3, f3, paint);
                paint.setXfermode(null);
                paint.setColor(this.color);
                RectF rectF4 = this.bounds;
                float f4 = this.radius;
                canvas.drawRoundRect(rectF4, f4, f4, paint);
            }
        }
    }
}
