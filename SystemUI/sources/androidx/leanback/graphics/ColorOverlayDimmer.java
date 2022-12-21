package androidx.leanback.graphics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import androidx.leanback.C0742R;

public final class ColorOverlayDimmer {
    private final float mActiveLevel;
    private int mAlpha;
    private float mAlphaFloat;
    private final float mDimmedLevel;
    private final Paint mPaint;

    public static ColorOverlayDimmer createDefault(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(C0742R.styleable.LeanbackTheme);
        int color = obtainStyledAttributes.getColor(C0742R.styleable.LeanbackTheme_overlayDimMaskColor, context.getResources().getColor(C0742R.C0743color.lb_view_dim_mask_color));
        float fraction = obtainStyledAttributes.getFraction(C0742R.styleable.LeanbackTheme_overlayDimActiveLevel, 1, 1, context.getResources().getFraction(C0742R.fraction.lb_view_active_level, 1, 0));
        float fraction2 = obtainStyledAttributes.getFraction(C0742R.styleable.LeanbackTheme_overlayDimDimmedLevel, 1, 1, context.getResources().getFraction(C0742R.fraction.lb_view_dimmed_level, 1, 1));
        obtainStyledAttributes.recycle();
        return new ColorOverlayDimmer(color, fraction, fraction2);
    }

    public static ColorOverlayDimmer createColorOverlayDimmer(int i, float f, float f2) {
        return new ColorOverlayDimmer(i, f, f2);
    }

    private ColorOverlayDimmer(int i, float f, float f2) {
        f = f > 1.0f ? 1.0f : f;
        float f3 = 0.0f;
        f = f < 0.0f ? 0.0f : f;
        f2 = f2 > 1.0f ? 1.0f : f2;
        f3 = f2 >= 0.0f ? f2 : f3;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(Color.rgb(Color.red(i), Color.green(i), Color.blue(i)));
        this.mActiveLevel = f;
        this.mDimmedLevel = f3;
        setActiveLevel(1.0f);
    }

    public void setActiveLevel(float f) {
        float f2 = this.mDimmedLevel;
        float f3 = f2 + (f * (this.mActiveLevel - f2));
        this.mAlphaFloat = f3;
        int i = (int) (f3 * 255.0f);
        this.mAlpha = i;
        this.mPaint.setAlpha(i);
    }

    public boolean needsDraw() {
        return this.mAlpha != 0;
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public float getAlphaFloat() {
        return this.mAlphaFloat;
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public int applyToColor(int i) {
        float f = 1.0f - this.mAlphaFloat;
        return Color.argb(Color.alpha(i), (int) (((float) Color.red(i)) * f), (int) (((float) Color.green(i)) * f), (int) (((float) Color.blue(i)) * f));
    }

    public void drawColorOverlay(Canvas canvas, View view, boolean z) {
        canvas.save();
        float left = ((float) view.getLeft()) + view.getTranslationX();
        float top = ((float) view.getTop()) + view.getTranslationY();
        canvas.translate(left, top);
        canvas.concat(view.getMatrix());
        canvas.translate(-left, -top);
        if (z) {
            canvas.drawRect((float) view.getLeft(), (float) view.getTop(), (float) view.getRight(), (float) view.getBottom(), this.mPaint);
        } else {
            canvas.drawRect((float) (view.getLeft() + view.getPaddingLeft()), (float) (view.getTop() + view.getPaddingTop()), (float) (view.getRight() - view.getPaddingRight()), (float) (view.getBottom() - view.getPaddingBottom()), this.mPaint);
        }
        canvas.restore();
    }
}
