package com.nothing.systemui.p024qs;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/* renamed from: com.nothing.systemui.qs.CircleDrawable */
public class CircleDrawable extends Drawable {
    private Bitmap mBitmap;
    private Paint mPaint;
    private int mWidth = Math.min(this.mBitmap.getWidth(), this.mBitmap.getHeight());

    public int getOpacity() {
        return -3;
    }

    public CircleDrawable(Drawable drawable) {
        this.mBitmap = drawableToBitmap(drawable);
        BitmapShader bitmapShader = new BitmapShader(this.mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setShader(bitmapShader);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public void draw(Canvas canvas) {
        int i = this.mWidth;
        canvas.drawCircle((float) (i / 2), (float) (i / 2), (float) ((i - 5) / 2), this.mPaint);
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public int getIntrinsicWidth() {
        return this.mWidth;
    }

    public int getIntrinsicHeight() {
        return this.mWidth;
    }
}
