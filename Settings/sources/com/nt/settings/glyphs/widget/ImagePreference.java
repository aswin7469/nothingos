package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.R$styleable;
/* loaded from: classes2.dex */
public class ImagePreference extends Preference {
    private Drawable mBackgroundDrawable;
    private Drawable mImageDrawable;

    public ImagePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public ImagePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ImagePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ImagePreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.preference_image);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ImagePreference, i, i2);
        this.mImageDrawable = obtainStyledAttributes.getDrawable(R$styleable.ImagePreference_image);
        this.mBackgroundDrawable = obtainStyledAttributes.getDrawable(R$styleable.ImagePreference_nt_background);
        setSelectable(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.image);
        Drawable drawable = this.mImageDrawable;
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        }
        Drawable drawable2 = this.mBackgroundDrawable;
        if (drawable2 != null) {
            imageView.setBackground(drawable2);
        }
    }
}
