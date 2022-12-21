package com.google.android.setupdesign.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class CardBackgroundDrawable extends Drawable {
    private final RectF cardBounds = new RectF();
    private final Path clipPath = new Path();
    private float cornerRadius;
    private boolean dirty = false;
    private final float inset;
    private final Paint paint;

    public int getOpacity() {
        return -1;
    }

    public void setAlpha(int i) {
    }

    public CardBackgroundDrawable(int i, float f, float f2) {
        this.cornerRadius = f;
        Paint paint2 = new Paint(5);
        this.paint = paint2;
        paint2.setColor(i);
        this.inset = f2;
    }

    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.dirty = true;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
    }

    public void setCornerRadius(float f) {
        if (this.cornerRadius != f) {
            this.cornerRadius = f;
            this.dirty = true;
            invalidateSelf();
        }
    }

    public void draw(Canvas canvas) {
        if (this.dirty) {
            buildComponents(getBounds());
            this.dirty = false;
        }
        if (this.cornerRadius > 0.0f) {
            canvas.clipPath(this.clipPath);
        }
    }

    private void buildComponents(Rect rect) {
        this.cardBounds.set(rect);
        RectF rectF = this.cardBounds;
        float f = this.inset;
        rectF.inset(f, f);
        this.clipPath.reset();
        this.clipPath.setFillType(Path.FillType.EVEN_ODD);
        Path path = this.clipPath;
        RectF rectF2 = this.cardBounds;
        float f2 = this.cornerRadius;
        path.addRoundRect(rectF2, f2, f2, Path.Direction.CW);
    }
}
