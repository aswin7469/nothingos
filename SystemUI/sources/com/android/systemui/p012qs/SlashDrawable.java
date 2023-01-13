package com.android.systemui.p012qs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.FloatProperty;

/* renamed from: com.android.systemui.qs.SlashDrawable */
public class SlashDrawable extends Drawable {
    private static final float CENTER_X = 10.65f;
    private static final float CENTER_Y = 11.869239f;
    public static final float CORNER_RADIUS = 1.0f;
    private static final float DEFAULT_ROTATION = -45.0f;
    private static final float LEFT = 0.40544835f;
    private static final float RIGHT = 0.4820516f;
    private static final float SCALE = 24.0f;
    private static final float SLASH_HEIGHT = 28.0f;
    private static final float SLASH_WIDTH = 1.8384776f;
    private static final float TOP = -0.088781714f;
    private boolean mAnimationEnabled = true;
    /* access modifiers changed from: private */
    public float mCurrentSlashLength;
    private Drawable mDrawable;
    private final Paint mPaint = new Paint(1);
    private final Path mPath = new Path();
    private float mRotation;
    private final FloatProperty mSlashLengthProp = new FloatProperty<SlashDrawable>("slashLength") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(SlashDrawable slashDrawable, float f) {
            float unused = slashDrawable.mCurrentSlashLength = f;
        }

        public Float get(SlashDrawable slashDrawable) {
            return Float.valueOf(slashDrawable.mCurrentSlashLength);
        }
    };
    private final RectF mSlashRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
    private boolean mSlashed;
    private ColorStateList mTintList;
    private PorterDuff.Mode mTintMode;

    private float scale(float f, int i) {
        return f * ((float) i);
    }

    public int getOpacity() {
        return 255;
    }

    public SlashDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public int getIntrinsicHeight() {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 0;
    }

    public int getIntrinsicWidth() {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mDrawable.setBounds(rect);
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        drawable.setCallback(getCallback());
        this.mDrawable.setBounds(getBounds());
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != null) {
            this.mDrawable.setTintMode(mode);
        }
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            this.mDrawable.setTintList(colorStateList);
        }
        invalidateSelf();
    }

    public void setRotation(float f) {
        if (this.mRotation != f) {
            this.mRotation = f;
            invalidateSelf();
        }
    }

    public void setAnimationEnabled(boolean z) {
        this.mAnimationEnabled = z;
    }

    public void setSlashed(boolean z) {
        if (this.mSlashed != z) {
            this.mSlashed = z;
            float f = 1.1666666f;
            float f2 = z ? 1.1666666f : 0.0f;
            if (z) {
                f = 0.0f;
            }
            if (this.mAnimationEnabled) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, this.mSlashLengthProp, new float[]{f, f2});
                ofFloat.addUpdateListener(new SlashDrawable$$ExternalSyntheticLambda0(this));
                ofFloat.setDuration(350);
                ofFloat.start();
                return;
            }
            this.mCurrentSlashLength = f2;
            invalidateSelf();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setSlashed$0$com-android-systemui-qs-SlashDrawable  reason: not valid java name */
    public /* synthetic */ void m2949lambda$setSlashed$0$comandroidsystemuiqsSlashDrawable(ValueAnimator valueAnimator) {
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        Matrix matrix = new Matrix();
        int width = getBounds().width();
        int height = getBounds().height();
        float scale = scale(1.0f, width);
        float scale2 = scale(1.0f, height);
        updateRect(scale(LEFT, width), scale(TOP, height), scale(RIGHT, width), scale(this.mCurrentSlashLength + TOP, height));
        this.mPath.reset();
        this.mPath.addRoundRect(this.mSlashRect, scale, scale2, Path.Direction.CW);
        float f = (float) (width / 2);
        float f2 = (float) (height / 2);
        matrix.setRotate(this.mRotation + DEFAULT_ROTATION, f, f2);
        this.mPath.transform(matrix);
        canvas.drawPath(this.mPath, this.mPaint);
        matrix.setRotate((-this.mRotation) - DEFAULT_ROTATION, f, f2);
        this.mPath.transform(matrix);
        matrix.setTranslate(this.mSlashRect.width(), 0.0f);
        this.mPath.transform(matrix);
        this.mPath.addRoundRect(this.mSlashRect, ((float) width) * 1.0f, ((float) height) * 1.0f, Path.Direction.CW);
        matrix.setRotate(this.mRotation + DEFAULT_ROTATION, f, f2);
        this.mPath.transform(matrix);
        canvas.clipOutPath(this.mPath);
        this.mDrawable.draw(canvas);
        canvas.restore();
    }

    private void updateRect(float f, float f2, float f3, float f4) {
        this.mSlashRect.left = f;
        this.mSlashRect.top = f2;
        this.mSlashRect.right = f3;
        this.mSlashRect.bottom = f4;
    }

    public void setTint(int i) {
        super.setTint(i);
        this.mDrawable.setTint(i);
        this.mPaint.setColor(i);
    }

    public void setTintList(ColorStateList colorStateList) {
        this.mTintList = colorStateList;
        super.setTintList(colorStateList);
        setDrawableTintList(colorStateList);
        this.mPaint.setColor(colorStateList.getDefaultColor());
        invalidateSelf();
    }

    /* access modifiers changed from: protected */
    public void setDrawableTintList(ColorStateList colorStateList) {
        this.mDrawable.setTintList(colorStateList);
    }

    public void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        super.setTintMode(mode);
        this.mDrawable.setTintMode(mode);
    }

    public void setAlpha(int i) {
        this.mDrawable.setAlpha(i);
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
        this.mPaint.setColorFilter(colorFilter);
    }
}
