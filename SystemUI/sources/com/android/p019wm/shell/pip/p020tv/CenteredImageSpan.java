package com.android.p019wm.shell.pip.p020tv;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;

/* renamed from: com.android.wm.shell.pip.tv.CenteredImageSpan */
public class CenteredImageSpan extends ImageSpan {
    private Drawable mDrawable;

    public /* bridge */ /* synthetic */ CharacterStyle getUnderlying() {
        return super.getUnderlying();
    }

    public CenteredImageSpan(Drawable drawable) {
        super(drawable);
    }

    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        Rect bounds = getCachedDrawable().getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fontMetricsInt2 = paint.getFontMetricsInt();
            fontMetricsInt.ascent = fontMetricsInt2.ascent;
            fontMetricsInt.descent = fontMetricsInt2.descent;
            fontMetricsInt.top = fontMetricsInt2.top;
            fontMetricsInt.bottom = fontMetricsInt2.bottom;
        }
        return bounds.right;
    }

    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        Drawable cachedDrawable = getCachedDrawable();
        canvas.save();
        canvas.translate(f, (float) ((i5 - cachedDrawable.getBounds().bottom) / 2));
        cachedDrawable.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        if (this.mDrawable == null) {
            this.mDrawable = getDrawable();
        }
        return this.mDrawable;
    }
}
