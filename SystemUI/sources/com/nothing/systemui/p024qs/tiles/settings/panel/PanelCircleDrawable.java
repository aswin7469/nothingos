package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.PanelCircleDrawable */
public class PanelCircleDrawable extends Drawable {
    private Bitmap mBitmap;
    private Paint mPaint;
    private int mSize = Math.min(this.mBitmap.getWidth(), this.mBitmap.getHeight());

    public int getOpacity() {
        return -3;
    }

    public PanelCircleDrawable(Drawable drawable) {
        this.mBitmap = drawableToBitmap(drawable);
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        BitmapShader bitmapShader = new BitmapShader(this.mBitmap, tileMode, tileMode);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setShader(bitmapShader);
        Matrix matrix = new Matrix();
        matrix.setTranslate(((float) (this.mSize - this.mBitmap.getWidth())) / 2.0f, ((float) (this.mSize - this.mBitmap.getHeight())) / 2.0f);
        bitmapShader.setLocalMatrix(matrix);
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public void draw(Canvas canvas) {
        int i = this.mSize;
        canvas.drawCircle(((float) i) / 2.0f, ((float) i) / 2.0f, ((float) (i - 5)) / 2.0f, this.mPaint);
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public int getIntrinsicWidth() {
        return this.mSize;
    }

    public int getIntrinsicHeight() {
        return this.mSize;
    }
}
