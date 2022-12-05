package com.android.settingslib.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.android.settingslib.R$dimen;
/* loaded from: classes.dex */
public class UserIconDrawable extends Drawable implements Drawable.Callback {
    private Drawable mBadge;
    private float mBadgeMargin;
    private float mBadgeRadius;
    private Bitmap mBitmap;
    private Paint mClearPaint;
    private float mDisplayRadius;
    private float mFramePadding;
    private Paint mFramePaint;
    private float mFrameWidth;
    private final Paint mIconPaint;
    private float mIntrinsicRadius;
    private final Paint mPaint;
    private Drawable mUserDrawable;
    private Bitmap mUserIcon;
    private final Matrix mIconMatrix = new Matrix();
    private float mPadding = 0.0f;
    private int mSize = 0;
    private boolean mInvalidated = true;
    private ColorStateList mTintColor = null;
    private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_ATOP;
    private ColorStateList mFrameColor = null;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public static Drawable getManagedUserDrawable(Context context) {
        return getDrawableForDisplayDensity(context, 17302401);
    }

    private static Drawable getDrawableForDisplayDensity(Context context, int i) {
        return context.getResources().getDrawableForDensity(i, context.getResources().getDisplayMetrics().densityDpi, context.getTheme());
    }

    public static int getSizeForList(Context context) {
        return (int) context.getResources().getDimension(R$dimen.circle_avatar_size);
    }

    public UserIconDrawable(int i) {
        Paint paint = new Paint();
        this.mIconPaint = paint;
        Paint paint2 = new Paint();
        this.mPaint = paint2;
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint2.setFilterBitmap(true);
        paint2.setAntiAlias(true);
        if (i > 0) {
            setBounds(0, 0, i, i);
            setIntrinsicSize(i);
        }
        setIcon(null);
    }

    public UserIconDrawable setIcon(Bitmap bitmap) {
        Drawable drawable = this.mUserDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
            this.mUserDrawable = null;
        }
        this.mUserIcon = bitmap;
        if (bitmap == null) {
            this.mIconPaint.setShader(null);
            this.mBitmap = null;
        } else {
            Paint paint = this.mIconPaint;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            paint.setShader(new BitmapShader(bitmap, tileMode, tileMode));
        }
        onBoundsChange(getBounds());
        return this;
    }

    public UserIconDrawable setIconDrawable(Drawable drawable) {
        Drawable drawable2 = this.mUserDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.mUserIcon = null;
        this.mUserDrawable = drawable;
        if (drawable == null) {
            this.mBitmap = null;
        } else {
            drawable.setCallback(this);
        }
        onBoundsChange(getBounds());
        return this;
    }

    public void setIntrinsicSize(int i) {
        this.mSize = i;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mInvalidated) {
            rebake();
        }
        if (this.mBitmap != null) {
            ColorStateList colorStateList = this.mTintColor;
            if (colorStateList == null) {
                this.mPaint.setColorFilter(null);
            } else {
                int colorForState = colorStateList.getColorForState(getState(), this.mTintColor.getDefaultColor());
                if (shouldUpdateColorFilter(colorForState, this.mTintMode)) {
                    this.mPaint.setColorFilter(new PorterDuffColorFilter(colorForState, this.mTintMode));
                }
            }
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
        }
    }

    private boolean shouldUpdateColorFilter(int i, PorterDuff.Mode mode) {
        ColorFilter colorFilter = this.mPaint.getColorFilter();
        if (colorFilter instanceof PorterDuffColorFilter) {
            PorterDuffColorFilter porterDuffColorFilter = (PorterDuffColorFilter) colorFilter;
            return (porterDuffColorFilter.getColor() == i && porterDuffColorFilter.getMode() == mode) ? false : true;
        }
        return true;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList colorStateList) {
        this.mTintColor = colorStateList;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return new BitmapDrawable(this.mBitmap).getConstantState();
    }

    public UserIconDrawable bake() {
        if (this.mSize <= 0) {
            throw new IllegalStateException("Baking requires an explicit intrinsic size");
        }
        int i = this.mSize;
        onBoundsChange(new Rect(0, 0, i, i));
        rebake();
        this.mFrameColor = null;
        this.mFramePaint = null;
        this.mClearPaint = null;
        Drawable drawable = this.mUserDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
            this.mUserDrawable = null;
        } else {
            Bitmap bitmap = this.mUserIcon;
            if (bitmap != null) {
                bitmap.recycle();
                this.mUserIcon = null;
            }
        }
        return this;
    }

    private void rebake() {
        this.mInvalidated = false;
        if (this.mBitmap != null) {
            if (this.mUserDrawable == null && this.mUserIcon == null) {
                return;
            }
            Canvas canvas = new Canvas(this.mBitmap);
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            Drawable drawable = this.mUserDrawable;
            if (drawable != null) {
                drawable.draw(canvas);
            } else if (this.mUserIcon != null) {
                int save = canvas.save();
                canvas.concat(this.mIconMatrix);
                canvas.drawCircle(this.mUserIcon.getWidth() * 0.5f, this.mUserIcon.getHeight() * 0.5f, this.mIntrinsicRadius, this.mIconPaint);
                canvas.restoreToCount(save);
            }
            ColorStateList colorStateList = this.mFrameColor;
            if (colorStateList != null) {
                this.mFramePaint.setColor(colorStateList.getColorForState(getState(), 0));
            }
            float f = this.mFrameWidth;
            if (this.mFramePadding + f > 0.001f) {
                canvas.drawCircle(getBounds().exactCenterX(), getBounds().exactCenterY(), (this.mDisplayRadius - this.mPadding) - (f * 0.5f), this.mFramePaint);
            }
            if (this.mBadge == null) {
                return;
            }
            float f2 = this.mBadgeRadius;
            if (f2 <= 0.001f) {
                return;
            }
            float f3 = f2 * 2.0f;
            float height = this.mBitmap.getHeight() - f3;
            float width = this.mBitmap.getWidth() - f3;
            this.mBadge.setBounds((int) width, (int) height, (int) (width + f3), (int) (f3 + height));
            float width2 = (this.mBadge.getBounds().width() * 0.5f) + this.mBadgeMargin;
            float f4 = this.mBadgeRadius;
            canvas.drawCircle(width + f4, height + f4, width2, this.mClearPaint);
            this.mBadge.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        if (!rect.isEmpty()) {
            if (this.mUserIcon == null && this.mUserDrawable == null) {
                return;
            }
            float min = Math.min(rect.width(), rect.height()) * 0.5f;
            int i = (int) (min * 2.0f);
            Bitmap bitmap = this.mBitmap;
            if (bitmap == null || i != ((int) (this.mDisplayRadius * 2.0f))) {
                this.mDisplayRadius = min;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                this.mBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
            }
            float min2 = Math.min(rect.width(), rect.height()) * 0.5f;
            this.mDisplayRadius = min2;
            float f = ((min2 - this.mFrameWidth) - this.mFramePadding) - this.mPadding;
            RectF rectF = new RectF(rect.exactCenterX() - f, rect.exactCenterY() - f, rect.exactCenterX() + f, rect.exactCenterY() + f);
            if (this.mUserDrawable != null) {
                Rect rect2 = new Rect();
                rectF.round(rect2);
                this.mIntrinsicRadius = Math.min(this.mUserDrawable.getIntrinsicWidth(), this.mUserDrawable.getIntrinsicHeight()) * 0.5f;
                this.mUserDrawable.setBounds(rect2);
            } else {
                Bitmap bitmap2 = this.mUserIcon;
                if (bitmap2 != null) {
                    float width = bitmap2.getWidth() * 0.5f;
                    float height = this.mUserIcon.getHeight() * 0.5f;
                    this.mIntrinsicRadius = Math.min(width, height);
                    float f2 = this.mIntrinsicRadius;
                    this.mIconMatrix.setRectToRect(new RectF(width - f2, height - f2, width + f2, height + f2), rectF, Matrix.ScaleToFit.FILL);
                }
            }
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        super.invalidateSelf();
        this.mInvalidated = true;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        ColorStateList colorStateList = this.mFrameColor;
        return colorStateList != null && colorStateList.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        int i = this.mSize;
        return i <= 0 ? ((int) this.mIntrinsicRadius) * 2 : i;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return getIntrinsicWidth();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        scheduleSelf(runnable, j);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        unscheduleSelf(runnable);
    }
}
