package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.util.ArrayUtils;
import com.android.systemui.C1894R;

public class NotificationBackgroundView extends View {
    private int mActualHeight = -1;
    private int mActualWidth = -1;
    private Drawable mBackground;
    private int mBackgroundTop;
    private boolean mBottomAmountClips = true;
    private boolean mBottomIsRounded;
    private int mClipBottomAmount;
    private int mClipTopAmount;
    private final float[] mCornerRadii = new float[8];
    private final boolean mDontModifyCorners = getResources().getBoolean(C1894R.bool.config_clipNotificationsToOutline);
    private int mDrawableAlpha = 255;
    private int mExpandAnimationHeight = -1;
    private boolean mExpandAnimationRunning;
    private int mExpandAnimationWidth = -1;
    private boolean mIsPressedAllowed;
    private int mTintColor;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public NotificationBackgroundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mClipTopAmount + this.mClipBottomAmount < getActualHeight() - this.mBackgroundTop || this.mExpandAnimationRunning) {
            canvas.save();
            if (!this.mExpandAnimationRunning) {
                canvas.clipRect(0, this.mClipTopAmount, getWidth(), getActualHeight() - this.mClipBottomAmount);
            }
            draw(canvas, this.mBackground);
            canvas.restore();
        }
    }

    private void draw(Canvas canvas, Drawable drawable) {
        if (drawable != null) {
            int i = this.mBackgroundTop;
            int actualHeight = getActualHeight();
            if (this.mBottomIsRounded && this.mBottomAmountClips && !this.mExpandAnimationRunning) {
                actualHeight -= this.mClipBottomAmount;
            }
            boolean isLayoutRtl = isLayoutRtl();
            int width = getWidth();
            int actualWidth = getActualWidth();
            int i2 = isLayoutRtl ? width - actualWidth : 0;
            int i3 = isLayoutRtl ? width : actualWidth;
            if (this.mExpandAnimationRunning) {
                i2 = (int) (((float) (width - actualWidth)) / 2.0f);
                i3 = i2 + actualWidth;
            }
            drawable.setBounds(i2, i, i3, actualHeight);
            drawable.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mBackground;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        setState(getDrawableState());
    }

    public void drawableHotspotChanged(float f, float f2) {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setHotspot(f, f2);
        }
    }

    public void setCustomBackground(Drawable drawable) {
        Drawable drawable2 = this.mBackground;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback) null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = drawable;
        drawable.mutate();
        Drawable drawable3 = this.mBackground;
        if (drawable3 != null) {
            drawable3.setCallback(this);
            setTint(this.mTintColor);
        }
        Drawable drawable4 = this.mBackground;
        if (drawable4 instanceof RippleDrawable) {
            ((RippleDrawable) drawable4).setForceSoftware(true);
        }
        updateBackgroundRadii();
        invalidate();
    }

    public void setCustomBackground(int i) {
        setCustomBackground(this.mContext.getDrawable(i));
    }

    public void setTint(int i) {
        if (i != 0) {
            this.mBackground.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
        } else {
            this.mBackground.clearColorFilter();
        }
        this.mTintColor = i;
        invalidate();
    }

    public void setActualHeight(int i) {
        if (!this.mExpandAnimationRunning) {
            this.mActualHeight = i;
            invalidate();
        }
    }

    private int getActualHeight() {
        int i;
        if (this.mExpandAnimationRunning && (i = this.mExpandAnimationHeight) > -1) {
            return i;
        }
        int i2 = this.mActualHeight;
        if (i2 > -1) {
            return i2;
        }
        return getHeight();
    }

    public void setActualWidth(int i) {
        this.mActualWidth = i;
    }

    private int getActualWidth() {
        int i;
        if (this.mExpandAnimationRunning && (i = this.mExpandAnimationWidth) > -1) {
            return i;
        }
        int i2 = this.mActualWidth;
        if (i2 > -1) {
            return i2;
        }
        return getWidth();
    }

    public void setClipTopAmount(int i) {
        this.mClipTopAmount = i;
        invalidate();
    }

    public void setClipBottomAmount(int i) {
        this.mClipBottomAmount = i;
        invalidate();
    }

    public void setState(int[] iArr) {
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            if (!this.mIsPressedAllowed) {
                iArr = ArrayUtils.removeInt(iArr, 16842919);
            }
            this.mBackground.setState(iArr);
        }
    }

    public void setRippleColor(int i) {
        Drawable drawable = this.mBackground;
        if (drawable instanceof RippleDrawable) {
            ((RippleDrawable) drawable).setColor(ColorStateList.valueOf(i));
        }
    }

    public void setDrawableAlpha(int i) {
        this.mDrawableAlpha = i;
        if (!this.mExpandAnimationRunning) {
            this.mBackground.setAlpha(i);
        }
    }

    public void setRadius(float f, float f2) {
        float[] fArr = this.mCornerRadii;
        if (f != fArr[0] || f2 != fArr[4]) {
            this.mBottomIsRounded = f2 != 0.0f;
            fArr[0] = f;
            fArr[1] = f;
            fArr[2] = f;
            fArr[3] = f;
            fArr[4] = f2;
            fArr[5] = f2;
            fArr[6] = f2;
            fArr[7] = f2;
            updateBackgroundRadii();
        }
    }

    public void setBottomAmountClips(boolean z) {
        if (z != this.mBottomAmountClips) {
            this.mBottomAmountClips = z;
            invalidate();
        }
    }

    private void updateBackgroundRadii() {
        if (!this.mDontModifyCorners) {
            Drawable drawable = this.mBackground;
            if (drawable instanceof LayerDrawable) {
                ((GradientDrawable) ((LayerDrawable) drawable).getDrawable(0)).setCornerRadii(this.mCornerRadii);
            }
        }
    }

    public void setBackgroundTop(int i) {
        this.mBackgroundTop = i;
        invalidate();
    }

    public void setExpandAnimationSize(int i, int i2) {
        this.mExpandAnimationHeight = i2;
        this.mExpandAnimationWidth = i;
        invalidate();
    }

    public void setExpandAnimationRunning(boolean z) {
        this.mExpandAnimationRunning = z;
        Drawable drawable = this.mBackground;
        if (drawable instanceof LayerDrawable) {
            ((GradientDrawable) ((LayerDrawable) drawable).getDrawable(0)).setAntiAlias(!z);
        }
        if (!this.mExpandAnimationRunning) {
            setDrawableAlpha(this.mDrawableAlpha);
        }
        invalidate();
    }

    public void setPressedAllowed(boolean z) {
        this.mIsPressedAllowed = z;
    }
}
