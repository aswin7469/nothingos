package com.android.volley.toolbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class NetworkImageView extends ImageView {
    private Bitmap mDefaultImageBitmap;
    private Drawable mDefaultImageDrawable;
    private int mDefaultImageId;
    private Bitmap mErrorImageBitmap;
    private Drawable mErrorImageDrawable;
    private int mErrorImageId;
    private String mUrl;

    public NetworkImageView(Context context) {
        this(context, null);
    }

    public NetworkImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NetworkImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setDefaultImageResId(int i) {
        this.mDefaultImageBitmap = null;
        this.mDefaultImageDrawable = null;
        this.mDefaultImageId = i;
    }

    public void setDefaultImageDrawable(Drawable drawable) {
        this.mDefaultImageId = 0;
        this.mDefaultImageBitmap = null;
        this.mDefaultImageDrawable = drawable;
    }

    public void setDefaultImageBitmap(Bitmap bitmap) {
        this.mDefaultImageId = 0;
        this.mDefaultImageDrawable = null;
        this.mDefaultImageBitmap = bitmap;
    }

    public void setErrorImageResId(int i) {
        this.mErrorImageBitmap = null;
        this.mErrorImageDrawable = null;
        this.mErrorImageId = i;
    }

    public void setErrorImageDrawable(Drawable drawable) {
        this.mErrorImageId = 0;
        this.mErrorImageBitmap = null;
        this.mErrorImageDrawable = drawable;
    }

    public void setErrorImageBitmap(Bitmap bitmap) {
        this.mErrorImageId = 0;
        this.mErrorImageDrawable = null;
        this.mErrorImageBitmap = bitmap;
    }

    void loadImageIfNecessary(final boolean z) {
        boolean z2;
        boolean z3;
        int width = getWidth();
        int height = getHeight();
        getScaleType();
        boolean z4 = true;
        if (getLayoutParams() != null) {
            z2 = getLayoutParams().width == -2;
            z3 = getLayoutParams().height == -2;
        } else {
            z2 = false;
            z3 = false;
        }
        if (!z2 || !z3) {
            z4 = false;
        }
        if (width == 0 && height == 0 && !z4) {
            return;
        }
        if (TextUtils.isEmpty(this.mUrl)) {
            setDefaultImageOrNull();
        } else {
            new Object() { // from class: com.android.volley.toolbox.NetworkImageView.1
            };
            throw null;
        }
    }

    private void setDefaultImageOrNull() {
        int i = this.mDefaultImageId;
        if (i != 0) {
            setImageResource(i);
            return;
        }
        Drawable drawable = this.mDefaultImageDrawable;
        if (drawable != null) {
            setImageDrawable(drawable);
            return;
        }
        Bitmap bitmap = this.mDefaultImageBitmap;
        if (bitmap != null) {
            setImageBitmap(bitmap);
        } else {
            setImageBitmap(null);
        }
    }

    @Override // android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        loadImageIfNecessary(true);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}
