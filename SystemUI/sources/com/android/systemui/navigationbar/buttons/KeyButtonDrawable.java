package com.android.systemui.navigationbar.buttons;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.util.FloatProperty;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
/* loaded from: classes.dex */
public class KeyButtonDrawable extends Drawable {
    public static final FloatProperty<KeyButtonDrawable> KEY_DRAWABLE_ROTATE = new FloatProperty<KeyButtonDrawable>("KeyButtonRotation") { // from class: com.android.systemui.navigationbar.buttons.KeyButtonDrawable.1
        @Override // android.util.FloatProperty
        public void setValue(KeyButtonDrawable keyButtonDrawable, float f) {
            keyButtonDrawable.setRotation(f);
        }

        @Override // android.util.Property
        public Float get(KeyButtonDrawable keyButtonDrawable) {
            return Float.valueOf(keyButtonDrawable.getRotation());
        }
    };
    public static final FloatProperty<KeyButtonDrawable> KEY_DRAWABLE_TRANSLATE_Y = new FloatProperty<KeyButtonDrawable>("KeyButtonTranslateY") { // from class: com.android.systemui.navigationbar.buttons.KeyButtonDrawable.2
        @Override // android.util.FloatProperty
        public void setValue(KeyButtonDrawable keyButtonDrawable, float f) {
            keyButtonDrawable.setTranslationY(f);
        }

        @Override // android.util.Property
        public Float get(KeyButtonDrawable keyButtonDrawable) {
            return Float.valueOf(keyButtonDrawable.getTranslationY());
        }
    };
    private AnimatedVectorDrawable mAnimatedDrawable;
    private final Drawable.Callback mAnimatedDrawableCallback;
    private final Paint mIconPaint;
    private final Paint mShadowPaint;
    private final ShadowDrawableState mState;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public KeyButtonDrawable(Drawable drawable, int i, int i2, boolean z, Color color) {
        this(drawable, new ShadowDrawableState(i, i2, drawable instanceof AnimatedVectorDrawable, z, color));
    }

    private KeyButtonDrawable(Drawable drawable, ShadowDrawableState shadowDrawableState) {
        this.mIconPaint = new Paint(3);
        this.mShadowPaint = new Paint(3);
        Drawable.Callback callback = new Drawable.Callback() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonDrawable.3
            @Override // android.graphics.drawable.Drawable.Callback
            public void invalidateDrawable(Drawable drawable2) {
                KeyButtonDrawable.this.invalidateSelf();
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void scheduleDrawable(Drawable drawable2, Runnable runnable, long j) {
                KeyButtonDrawable.this.scheduleSelf(runnable, j);
            }

            @Override // android.graphics.drawable.Drawable.Callback
            public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
                KeyButtonDrawable.this.unscheduleSelf(runnable);
            }
        };
        this.mAnimatedDrawableCallback = callback;
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
            animatedVectorDrawable.setCallback(callback);
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
        if (canAnimate()) {
            return;
        }
        ShadowDrawableState shadowDrawableState = this.mState;
        if (shadowDrawableState.mRotateDegrees == f) {
            return;
        }
        shadowDrawableState.mRotateDegrees = f;
        invalidateSelf();
    }

    public void setTranslationY(float f) {
        setTranslation(this.mState.mTranslationX, f);
    }

    public void setTranslation(float f, float f2) {
        ShadowDrawableState shadowDrawableState = this.mState;
        if (shadowDrawableState.mTranslationX == f && shadowDrawableState.mTranslationY == f2) {
            return;
        }
        shadowDrawableState.mTranslationX = f;
        shadowDrawableState.mTranslationY = f2;
        invalidateSelf();
    }

    public void setShadowProperties(int i, int i2, int i3, int i4) {
        if (canAnimate()) {
            return;
        }
        ShadowDrawableState shadowDrawableState = this.mState;
        if (shadowDrawableState.mShadowOffsetX == i && shadowDrawableState.mShadowOffsetY == i2 && shadowDrawableState.mShadowSize == i3 && shadowDrawableState.mShadowColor == i4) {
            return;
        }
        shadowDrawableState.mShadowOffsetX = i;
        shadowDrawableState.mShadowOffsetY = i2;
        shadowDrawableState.mShadowSize = i3;
        shadowDrawableState.mShadowColor = i4;
        this.mShadowPaint.setColorFilter(new PorterDuffColorFilter(this.mState.mShadowColor, PorterDuff.Mode.SRC_ATOP));
        updateShadowAlpha();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z, boolean z2) {
        boolean visible = super.setVisible(z, z2);
        if (visible) {
            jumpToCurrentState();
        }
        return visible;
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.jumpToCurrentState();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mState.mAlpha = i;
        this.mIconPaint.setAlpha(i);
        updateShadowAlpha();
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
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

    public float getTranslationY() {
        return this.mState.mTranslationY;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        ShadowDrawableState shadowDrawableState = this.mState;
        return shadowDrawableState.mBaseHeight + ((shadowDrawableState.mShadowSize + Math.abs(shadowDrawableState.mShadowOffsetY)) * 2);
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        ShadowDrawableState shadowDrawableState = this.mState;
        return shadowDrawableState.mBaseWidth + ((shadowDrawableState.mShadowSize + Math.abs(shadowDrawableState.mShadowOffsetX)) * 2);
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

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        ShadowDrawableState shadowDrawableState;
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
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
        ShadowDrawableState shadowDrawableState2 = this.mState;
        canvas.translate(shadowDrawableState2.mTranslationX, shadowDrawableState2.mTranslationY);
        canvas.rotate(this.mState.mRotateDegrees, getIntrinsicWidth() / 2, getIntrinsicHeight() / 2);
        ShadowDrawableState shadowDrawableState3 = this.mState;
        if (shadowDrawableState3.mShadowSize > 0) {
            if (shadowDrawableState3.mLastDrawnShadow == null || z) {
                regenerateBitmapShadowCache();
            }
            double d = (float) ((this.mState.mRotateDegrees * 3.141592653589793d) / 180.0d);
            float sin = ((float) ((Math.sin(d) * this.mState.mShadowOffsetY) + (Math.cos(d) * shadowDrawableState.mShadowOffsetX))) - this.mState.mTranslationX;
            double cos = Math.cos(d) * this.mState.mShadowOffsetY;
            double sin2 = Math.sin(d);
            ShadowDrawableState shadowDrawableState4 = this.mState;
            canvas.drawBitmap(shadowDrawableState4.mLastDrawnShadow, sin, ((float) (cos - (sin2 * shadowDrawableState4.mShadowOffsetX))) - shadowDrawableState4.mTranslationY, this.mShadowPaint);
        }
        canvas.drawBitmap(this.mState.mLastDrawnIcon, (Rect) null, bounds, this.mIconPaint);
        canvas.restore();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return this.mState.canApplyTheme();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getDrawableBackgroundColor() {
        return this.mState.mOvalBackgroundColor.toArgb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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
            canvas.scale(-1.0f, 1.0f, intrinsicWidth * 0.5f, intrinsicHeight * 0.5f);
        }
        mutate.draw(canvas);
        canvas.restore();
        if (this.mState.mIsHardwareBitmap) {
            createBitmap = createBitmap.copy(Bitmap.Config.HARDWARE, false);
        }
        this.mState.mLastDrawnIcon = createBitmap;
    }

    private void regenerateBitmapShadowCache() {
        ShadowDrawableState shadowDrawableState = this.mState;
        if (shadowDrawableState.mShadowSize == 0) {
            shadowDrawableState.mLastDrawnIcon = null;
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
            canvas.scale(-1.0f, 1.0f, intrinsicWidth * 0.5f, intrinsicHeight * 0.5f);
        }
        mutate.draw(canvas);
        canvas.restore();
        Paint paint = new Paint(3);
        paint.setMaskFilter(new BlurMaskFilter(this.mState.mShadowSize, BlurMaskFilter.Blur.NORMAL));
        int[] iArr = new int[2];
        Bitmap extractAlpha = createBitmap.extractAlpha(paint, iArr);
        paint.setMaskFilter(null);
        createBitmap.eraseColor(0);
        canvas.drawBitmap(extractAlpha, iArr[0], iArr[1], paint);
        if (this.mState.mIsHardwareBitmap) {
            createBitmap = createBitmap.copy(Bitmap.Config.HARDWARE, false);
        }
        this.mState.mLastDrawnShadow = createBitmap;
    }

    private void updateShadowAlpha() {
        int alpha = Color.alpha(this.mState.mShadowColor);
        Paint paint = this.mShadowPaint;
        ShadowDrawableState shadowDrawableState = this.mState;
        paint.setAlpha(Math.round(alpha * (shadowDrawableState.mAlpha / 255.0f) * (1.0f - shadowDrawableState.mDarkIntensity)));
    }

    private void setDrawableBounds(Drawable drawable) {
        ShadowDrawableState shadowDrawableState = this.mState;
        int abs = shadowDrawableState.mShadowSize + Math.abs(shadowDrawableState.mShadowOffsetX);
        ShadowDrawableState shadowDrawableState2 = this.mState;
        int abs2 = shadowDrawableState2.mShadowSize + Math.abs(shadowDrawableState2.mShadowOffsetY);
        drawable.setBounds(abs, abs2, getIntrinsicWidth() - abs, getIntrinsicHeight() - abs2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ShadowDrawableState extends Drawable.ConstantState {
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

        @Override // android.graphics.drawable.Drawable.ConstantState
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

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new KeyButtonDrawable(null, this);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
    }

    public static KeyButtonDrawable create(Context context, int i, int i2, int i3, boolean z, Color color) {
        Resources resources = context.getResources();
        boolean z2 = resources.getConfiguration().getLayoutDirection() == 1;
        Drawable drawable = context.getDrawable(i3);
        KeyButtonDrawable keyButtonDrawable = new KeyButtonDrawable(drawable, i, i2, z2 && drawable.isAutoMirrored(), color);
        if (z) {
            keyButtonDrawable.setShadowProperties(resources.getDimensionPixelSize(R$dimen.nav_key_button_shadow_offset_x), resources.getDimensionPixelSize(R$dimen.nav_key_button_shadow_offset_y), resources.getDimensionPixelSize(R$dimen.nav_key_button_shadow_radius), context.getColor(R$color.nav_key_button_shadow_color));
        }
        return keyButtonDrawable;
    }
}
