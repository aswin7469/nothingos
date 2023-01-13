package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.C1894R;

public class UserAvatarView extends View {
    private final UserIconDrawable mDrawable;

    public UserAvatarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDrawable = new UserIconDrawable();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.UserAvatarView, i, i2);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == 1) {
                setAvatarPadding(obtainStyledAttributes.getDimension(index, 0.0f));
            } else if (index == 6) {
                setFrameWidth(obtainStyledAttributes.getDimension(index, 0.0f));
            } else if (index == 5) {
                setFramePadding(obtainStyledAttributes.getDimension(index, 0.0f));
            } else if (index == 4) {
                setFrameColor(obtainStyledAttributes.getColorStateList(index));
            } else if (index == 2) {
                setBadgeDiameter(obtainStyledAttributes.getDimension(index, 0.0f));
            } else if (index == 3) {
                setBadgeMargin(obtainStyledAttributes.getDimension(index, 0.0f));
            }
        }
        obtainStyledAttributes.recycle();
        setBackground(this.mDrawable);
    }

    public UserAvatarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public UserAvatarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UserAvatarView(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setActivated(boolean z) {
        super.setActivated(z);
        this.mDrawable.invalidateSelf();
    }

    @Deprecated
    public void setBitmap(Bitmap bitmap) {
        setAvatar(bitmap);
    }

    public void setFrameColor(ColorStateList colorStateList) {
        this.mDrawable.setFrameColor(colorStateList);
    }

    public void setFrameWidth(float f) {
        this.mDrawable.setFrameWidth(f);
    }

    public void setFramePadding(float f) {
        this.mDrawable.setFramePadding(f);
    }

    public void setAvatarPadding(float f) {
        this.mDrawable.setPadding(f);
    }

    public void setBadgeDiameter(float f) {
        this.mDrawable.setBadgeRadius(f * 0.5f);
    }

    public void setBadgeMargin(float f) {
        this.mDrawable.setBadgeMargin(f);
    }

    public void setAvatar(Bitmap bitmap) {
        this.mDrawable.setIcon(bitmap);
        this.mDrawable.setBadge((Drawable) null);
    }

    public void setAvatarWithBadge(Bitmap bitmap, int i) {
        this.mDrawable.setIcon(bitmap);
        this.mDrawable.setBadgeIfManagedUser(getContext(), i);
    }

    public void setDrawable(Drawable drawable) {
        if (!(drawable instanceof UserIconDrawable)) {
            this.mDrawable.setIconDrawable(drawable);
            this.mDrawable.setBadge((Drawable) null);
            return;
        }
        throw new RuntimeException("Recursively adding UserIconDrawable");
    }

    public void setDrawableWithBadge(Drawable drawable, int i) {
        if (!(drawable instanceof UserIconDrawable)) {
            this.mDrawable.setIconDrawable(drawable);
            this.mDrawable.setBadgeIfManagedUser(getContext(), i);
            return;
        }
        throw new RuntimeException("Recursively adding UserIconDrawable");
    }

    public boolean isEmpty() {
        return this.mDrawable.isEmpty();
    }
}
