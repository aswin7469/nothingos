package pl.droidsonroids.gif;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import pl.droidsonroids.gif.GifViewUtils;
/* loaded from: classes2.dex */
public class GifImageView extends ImageView {
    private boolean mFreezesAnimation;

    public GifImageView(Context context) {
        super(context);
    }

    public GifImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        postInit(GifViewUtils.initImageView(this, attributeSet, 0, 0));
    }

    public GifImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        postInit(GifViewUtils.initImageView(this, attributeSet, i, 0));
    }

    @RequiresApi(21)
    public GifImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        postInit(GifViewUtils.initImageView(this, attributeSet, i, i2));
    }

    private void postInit(GifViewUtils.GifImageViewAttributes gifImageViewAttributes) {
        this.mFreezesAnimation = gifImageViewAttributes.freezesAnimation;
        int i = gifImageViewAttributes.mSourceResId;
        if (i > 0) {
            super.setImageResource(i);
        }
        int i2 = gifImageViewAttributes.mBackgroundResId;
        if (i2 > 0) {
            super.setBackgroundResource(i2);
        }
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        if (!GifViewUtils.setGifImageUri(this, uri)) {
            super.setImageURI(uri);
        }
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        if (!GifViewUtils.setResource(this, true, i)) {
            super.setImageResource(i);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i) {
        if (!GifViewUtils.setResource(this, false, i)) {
            super.setBackgroundResource(i);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Drawable drawable = null;
        Drawable drawable2 = this.mFreezesAnimation ? getDrawable() : null;
        if (this.mFreezesAnimation) {
            drawable = getBackground();
        }
        return new GifViewSavedState(super.onSaveInstanceState(), drawable2, drawable);
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof GifViewSavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        GifViewSavedState gifViewSavedState = (GifViewSavedState) parcelable;
        super.onRestoreInstanceState(gifViewSavedState.getSuperState());
        gifViewSavedState.restoreState(getDrawable(), 0);
        gifViewSavedState.restoreState(getBackground(), 1);
    }

    public void setFreezesAnimation(boolean z) {
        this.mFreezesAnimation = z;
    }
}
