package com.android.systemui.navigationbar.buttons;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.FloatProperty;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;

public class KeyButtonDrawable extends Drawable {
    public static final FloatProperty<KeyButtonDrawable> KEY_DRAWABLE_ROTATE = new FloatProperty<KeyButtonDrawable>("KeyButtonRotation") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(KeyButtonDrawable keyButtonDrawable, float f) {
            keyButtonDrawable.setRotation(f);
        }

        public Float get(KeyButtonDrawable keyButtonDrawable) {
            return Float.valueOf(keyButtonDrawable.getRotation());
        }
    };
    public static final FloatProperty<KeyButtonDrawable> KEY_DRAWABLE_TRANSLATE_Y = new FloatProperty<KeyButtonDrawable>("KeyButtonTranslateY") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(KeyButtonDrawable keyButtonDrawable, float f) {
            keyButtonDrawable.setTranslationY(f);
        }

        public Float get(KeyButtonDrawable keyButtonDrawable) {
            return Float.valueOf(keyButtonDrawable.getTranslationY());
        }
    };
    private AnimatedVectorDrawable mAnimatedDrawable;
    private final Drawable.Callback mAnimatedDrawableCallback;
    private final Paint mIconPaint;
    private final Paint mShadowPaint;
    private final ShadowDrawableState mState;

    public int getOpacity() {
        return -3;
    }

    public KeyButtonDrawable(Drawable drawable, int i, int i2, boolean z, Color color) {
        this(drawable, new ShadowDrawableState(i, i2, drawable instanceof AnimatedVectorDrawable, z, color));
    }

    private KeyButtonDrawable(Drawable drawable, ShadowDrawableState shadowDrawableState) {
        this.mIconPaint = new Paint(3);
        this.mShadowPaint = new Paint(3);
        C22723 r0 = new Drawable.Callback() {
            public void invalidateDrawable(Drawable drawable) {
                KeyButtonDrawable.this.invalidateSelf();
            }

            public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
                KeyButtonDrawable.this.scheduleSelf(runnable, j);
            }

            public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
                KeyButtonDrawable.this.unscheduleSelf(runnable);
            }
        };
        this.mAnimatedDrawableCallback = r0;
        this.mState = shadowDrawableState;
        if (drawable != null) {
            shadowDrawableState.mBaseHeight = drawable.getIntrinsicHeight();
            shadowDrawableState.mBaseWidth = drawable.getIntrinsicWidth();
            shadowDrawableState.mChangingConfigurations = drawable.getChangingConfigurations();
            shadowDrawableState.mChildState = drawable.getConstantState();
        }
        if (canAnimate()) {
            AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) shadowDrawableState.mChildState.newDrawable().mutate();
            this.mAnimatedDrawable = animatedVectorDrawable;
            animatedVectorDrawable.setCallback(r0);
            setDrawableBounds(this.mAnimatedDrawable);
        }
    }

    public void setDarkIntensity(float f) {
        this.mState.mDarkIntensity = f;
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mState.mLightColor), Integer.valueOf(this.mState.mDarkColor))).intValue();
        updateShadowAlpha();
        setColorFilter(new PorterDuffColorFilter(intValue, PorterDuff.Mode.SRC_ATOP));
    }

    public void setRotation(float f) {
        if (!canAnimate() && this.mState.mRotateDegrees != f) {
            this.mState.mRotateDegrees = f;
            invalidateSelf();
        }
    }

    public void setTranslationX(float f) {
        setTranslation(f, this.mState.mTranslationY);
    }

    public void setTranslationY(float f) {
        setTranslation(this.mState.mTranslationX, f);
    }

    public void setTranslation(float f, float f2) {
        if (this.mState.mTranslationX != f || this.mState.mTranslationY != f2) {
            this.mState.mTranslationX = f;
            this.mState.mTranslationY = f2;
            invalidateSelf();
        }
    }

    public void setShadowProperties(int i, int i2, int i3, int i4) {
        if (!canAnimate()) {
            if (this.mState.mShadowOffsetX != i || this.mState.mShadowOffsetY != i2 || this.mState.mShadowSize != i3 || this.mState.mShadowColor != i4) {
                this.mState.mShadowOffsetX = i;
                this.mState.mShadowOffsetY = i2;
                this.mState.mShadowSize = i3;
                this.mState.mShadowColor = i4;
                this.mShadowPaint.setColorFilter(new PorterDuffColorFilter(this.mState.mShadowColor, PorterDuff.Mode.SRC_ATOP));
                updateShadowAlpha();
                invalidateSelf();
            }
        }
    }

    public boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        if (visible) {
            jumpToCurrentState();
        }
        return visible;
    }

    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.jumpToCurrentState();
        }
    }

    public void setAlpha(int i) {
        this.mState.mAlpha = i;
        this.mIconPaint.setAlpha(i);
        updateShadowAlpha();
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mIconPaint.setColorFilter(colorFilter);
        if (this.mAnimatedDrawable != null) {
            if (hasOvalBg()) {
                this.mAnimatedDrawable.setColorFilter(new PorterDuffColorFilter(this.mState.mLightColor, PorterDuff.Mode.SRC_IN));
            } else {
                this.mAnimatedDrawable.setColorFilter(colorFilter);
            }
        }
        invalidateSelf();
    }

    public float getDarkIntensity() {
        return this.mState.mDarkIntensity;
    }

    public float getRotation() {
        return this.mState.mRotateDegrees;
    }

    public float getTranslationX() {
        return this.mState.mTranslationX;
    }

    public float getTranslationY() {
        return this.mState.mTranslationY;
    }

    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    public int getIntrinsicHeight() {
        return this.mState.mBaseHeight + ((this.mState.mShadowSize + Math.abs(this.mState.mShadowOffsetY)) * 2);
    }

    public int getIntrinsicWidth() {
        return this.mState.mBaseWidth + ((this.mState.mShadowSize + Math.abs(this.mState.mShadowOffsetX)) * 2);
    }

    public boolean canAnimate() {
        return this.mState.mSupportsAnimation;
    }

    public void startAnimation() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.start();
        }
    }

    public void resetAnimation() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.reset();
        }
    }

    public void clearAnimationCallbacks() {
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.clearAnimationCallbacks();
        }
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (!bounds.isEmpty()) {
            AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
            if (animatedVectorDrawable != null) {
                animatedVectorDrawable.draw(canvas);
                return;
            }
            boolean z = this.mState.mIsHardwareBitmap != canvas.isHardwareAccelerated();
            if (z) {
                this.mState.mIsHardwareBitmap = canvas.isHardwareAccelerated();
            }
            if (this.mState.mLastDrawnIcon == null || z) {
                regenerateBitmapIconCache();
            }
            canvas.save();
            canvas.translate(this.mState.mTranslationX, this.mState.mTranslationY);
            canvas.rotate(this.mState.mRotateDegrees, (float) (getIntrinsicWidth() / 2), (float) (getIntrinsicHeight() / 2));
            if (this.mState.mShadowSize > 0) {
                if (this.mState.mLastDrawnShadow == null || z) {
                    regenerateBitmapShadowCache();
                }
                double d = (double) ((float) ((((double) this.mState.mRotateDegrees) * 3.141592653589793d) / 180.0d));
                canvas.drawBitmap(this.mState.mLastDrawnShadow, ((float) ((Math.sin(d) * ((double) this.mState.mShadowOffsetY)) + (Math.cos(d) * ((double) this.mState.mShadowOffsetX)))) - this.mState.mTranslationX, ((float) ((Math.cos(d) * ((double) this.mState.mShadowOffsetY)) - (Math.sin(d) * ((double) this.mState.mShadowOffsetX)))) - this.mState.mTranslationY, this.mShadowPaint);
            }
            canvas.drawBitmap(this.mState.mLastDrawnIcon, (Rect) null, bounds, this.mIconPaint);
            canvas.restore();
        }
    }

    public boolean canApplyTheme() {
        return this.mState.canApplyTheme();
    }

    /* access modifiers changed from: package-private */
    public int getDrawableBackgroundColor() {
        return this.mState.mOvalBackgroundColor.toArgb();
    }

    /* access modifiers changed from: package-private */
    public boolean hasOvalBg() {
        return this.mState.mOvalBackgroundColor != null;
    }

    private void regenerateBitmapIconCache() {
        int intrinsicWidth = getIntrinsicWidth();
        int intrinsicHeight = getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Drawable mutate = this.mState.mChildState.newDrawable().mutate();
        setDrawableBounds(mutate);
        canvas.save();
        if (this.mState.mHorizontalFlip) {
            canvas.scale(-1.0f, 1.0f, ((float) intrinsicWidth) * 0.5f, ((float) intrinsicHeight) * 0.5f);
        }
        mutate.draw(canvas);
        canvas.restore();
        if (this.mState.mIsHardwareBitmap) {
            createBitmap = createBitmap.copy(Bitmap.Config.HARDWARE, false);
        }
        this.mState.mLastDrawnIcon = createBitmap;
    }

    private void regenerateBitmapShadowCache() {
        if (this.mState.mShadowSize == 0) {
            this.mState.mLastDrawnIcon = null;
            return;
        }
        int intrinsicWidth = getIntrinsicWidth();
        int intrinsicHeight = getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Drawable mutate = this.mState.mChildState.newDrawable().mutate();
        setDrawableBounds(mutate);
        canvas.save();
        if (this.mState.mHorizontalFlip) {
            canvas.scale(-1.0f, 1.0f, ((float) intrinsicWidth) * 0.5f, ((float) intrinsicHeight) * 0.5f);
        }
        mutate.draw(canvas);
        canvas.restore();
        Paint paint = new Paint(3);
        paint.setMaskFilter(new BlurMaskFilter((float) this.mState.mShadowSize, BlurMaskFilter.Blur.NORMAL));
        int[] iArr = new int[2];
        Bitmap extractAlpha = createBitmap.extractAlpha(paint, iArr);
        paint.setMaskFilter((MaskFilter) null);
        createBitmap.eraseColor(0);
        canvas.drawBitmap(extractAlpha, (float) iArr[0], (float) iArr[1], paint);
        if (this.mState.mIsHardwareBitmap) {
            createBitmap = createBitmap.copy(Bitmap.Config.HARDWARE, false);
        }
        this.mState.mLastDrawnShadow = createBitmap;
    }

    private void updateShadowAlpha() {
        this.mShadowPaint.setAlpha(Math.round(((float) Color.alpha(this.mState.mShadowColor)) * (((float) this.mState.mAlpha) / 255.0f) * (1.0f - this.mState.mDarkIntensity)));
    }

    private void setDrawableBounds(Drawable drawable) {
        int abs = this.mState.mShadowSize + Math.abs(this.mState.mShadowOffsetX);
        int abs2 = this.mState.mShadowSize + Math.abs(this.mState.mShadowOffsetY);
        drawable.setBounds(abs, abs2, getIntrinsicWidth() - abs, getIntrinsicHeight() - abs2);
    }

    private static class ShadowDrawableState extends Drawable.ConstantState {
        int mAlpha = 255;
        int mBaseHeight;
        int mBaseWidth;
        int mChangingConfigurations;
        Drawable.ConstantState mChildState;
        final int mDarkColor;
        float mDarkIntensity;
        boolean mHorizontalFlip;
        boolean mIsHardwareBitmap;
        Bitmap mLastDrawnIcon;
        Bitmap mLastDrawnShadow;
        final int mLightColor;
        final Color mOvalBackgroundColor;
        float mRotateDegrees;
        int mShadowColor;
        int mShadowOffsetX;
        int mShadowOffsetY;
        int mShadowSize;
        final boolean mSupportsAnimation;
        float mTranslationX;
        float mTranslationY;

        public boolean canApplyTheme() {
            return true;
        }

        public ShadowDrawableState(int i, int i2, boolean z, boolean z2, Color color) {
            this.mLightColor = i;
            this.mDarkColor = i2;
            this.mSupportsAnimation = z;
            this.mHorizontalFlip = z2;
            this.mOvalBackgroundColor = color;
        }

        public Drawable newDrawable() {
            return new KeyButtonDrawable((Drawable) null, this);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
    }

    public static KeyButtonDrawable create(Context context, Context context2, int i, boolean z, Color color) {
        return create(context, Utils.getColorAttrDefaultColor(context, C1893R.attr.singleToneColor), Utils.getColorAttrDefaultColor(context2, C1893R.attr.singleToneColor), i, z, color);
    }

    public static KeyButtonDrawable create(Context context, int i, int i2, int i3, boolean z, Color color) {
        Resources resources = context.getResources();
        boolean z2 = resources.getConfiguration().getLayoutDirection() == 1;
        Drawable drawable = context.getDrawable(i3);
        KeyButtonDrawable keyButtonDrawable = new KeyButtonDrawable(drawable, i, i2, z2 && drawable.isAutoMirrored(), color);
        if (z) {
            keyButtonDrawable.setShadowProperties(resources.getDimensionPixelSize(C1893R.dimen.nav_key_button_shadow_offset_x), resources.getDimensionPixelSize(C1893R.dimen.nav_key_button_shadow_offset_y), resources.getDimensionPixelSize(C1893R.dimen.nav_key_button_shadow_radius), context.getColor(C1893R.C1894color.nav_key_button_shadow_color));
        }
        return keyButtonDrawable;
    }
}
